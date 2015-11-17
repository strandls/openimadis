/**
 * Tool bar for editing image (adding overlays)
 */
Ext.define('Manage.view.ImageEditToolBar', {
    extend:'Ext.panel.Panel',
    xtype:'imageEditToolbar',
    alias:'widget.imageEditToolbar',
    initComponent : function() {
        var view = this;
        Ext.apply(this, {
             items: [{
                xtype : 'toolbar',
                items : [{
                    'icon' : 'images/icons/draw/fhpath.png',
                    'tooltip' : 'Freehand',
                    'scale' : 'medium',
                    listeners:{
                    	click:function(){
                    		this.up().up().fireEvent("addFreeHand");
                    	}
                    }
                },{
                    icon : 'images/icons/draw/line.png',
                    tooltip : 'Line',
                    scale : 'medium',
                    listeners:{
                    	click:function(){
                    		this.up().up().fireEvent("addLine");
                    	}
                    }
                }, {
                    'icon' : 'images/icons/draw/ellipse.png',
                    'tooltip' : 'Ellipse',
                    'scale' : 'medium',
                    listeners:{
                    	click:function(){
                    		this.up().up().fireEvent("addEllipse");
                    	}
                    }
                }, {
                    'icon' : 'images/icons/draw/rect.png',
                    'tooltip' : 'Rectange',
                    'scale' : 'medium',
                    listeners:{
                    	click:function(){
                    		this.up().up().fireEvent("addRect");
                    	}
                    }
                }, {
                    icon : 'images/icons/draw/polygon.png',
                    tooltip : 'Polygon',
                    scale : 'medium',
                    listeners:{
                    	click:function(){
                    		this.up().up().fireEvent("addPolygon");
                    	}
                    }
                }, {
                    icon: 'images/icons/draw/text.png',
                    tooltip : 'Text',
                    scale : 'medium',
                    listeners:{
                    	click:function(){
                    		this.up().up().fireEvent("addText");
                    	}
                    }
                }, {
                    icon: 'images/icons/draw/arrow.png',
                    tooltip : 'Arrow',
                    scale : 'medium',
                    listeners:{
                    	click:function(){
                    		this.up().up().fireEvent("addArrow");
                    	}
                    }
                }]
            }, {
                xtype : 'toolbar',
                items : [ {
                    icon : 'images/icons/delete.png',
                    tooltip : 'Erase',
                    scale : 'medium',
                    handler : function() {
                        removeShapes();
                    }
                },{
                    icon : 'images/icons/draw/undo.png',
                    tooltip : 'Undo',
                    scale : 'medium',
                    handler : function() {
                        undo();
                    }
                }, {
                   icon : 'images/icons/draw/redo.png',
                   tooltip : 'Redo',
                   scale : 'medium',
                   handler : function() {
                        redo();
                   }
                },{
                    tooltip : 'Choose Color',
                    style : {
                        "background-color" : getStrokecolour(),
                        "marginRight" : '10px',
                        "paddingRight" : '10px',
                        "background-image": "none"
                    },
                    border : 3,
                    scale : 'medium',
                    handler : function(button, e) {
                        	view.colorWin = Ext.create('Ext.window.Window', {
                            title : 'Color Picker',
                            width : 210,
                            height : 165,
                            items : [{
                                xtype : 'colorpicker',
                                value : getStrokecolour(),
                                listeners : {
                                    select : function(picker, selcolor) {
                                        console.log("Selected color: " + selcolor);
                                        setStrokecolour('#'+selcolor),
                                        button.getEl().setStyle('background-color', '#'+selcolor);                                       
                                        picker.up().close();
                                    }
                                }
                            }]
                        });
                        view.colorWin.showAt(e.getXY());
                    }
                }]
            },
            {
                xtype : 'toolbar',
                items : [{
                    xtype : 'numberfield',
                    fieldLabel : 'Width',
                    labelAlign : 'top',
                    minValue : 1,
                    maxValue : 30,
                    value : getStrokewidth(),
                    width : 50,
                    listeners : {
                        change : function(field, newValue, oldValue, opts) {
                        	setStrokewidth(newValue);
                        }
                    }
                }, {
                    xtype : 'numberfield',
                    fieldLabel : 'Opacity',
                    labelAlign : 'top',
                    minValue : 0.1,
                    step : 0.1,
                    allowDecimals : true,
                    maxValue : 1.0,
                    value : getStrokeopacity(),
                    width : 50,
                    listeners : {
                        change : function(field, newValue, oldValue, opts) {
                        	setStrokeopacity(newValue);
                        }
                    }
                },{
                        xtype : 'numberfield',
                        fieldLabel : 'Font Size',
                        labelAlign : 'top',
                        minValue : 1,
                        step : 1,
                        allowDecimals : true,
                        maxValue : 50,
                        value : getFontSize(),
                        width : 70,
                        listeners : {
                            change : function(field, newValue, oldValue, opts) {
                            	setFontSize(newValue);
                            }
                        }
                    }]
                    
            }],
            buttons : [{
                text : 'Save',
                xtype : 'splitbutton',
                handler : function() {
                    view.saveOverlay(false);
                },
                menu : {
                    xtype : 'menu',
                    items : [
                        {
                            text: 'Save on All Slices',
                            scope : this,
                            handler: function() { 
                                view.saveAllSlicesOverlay();
                            }
                        },
                        {
                            text: 'Save on All Frames',
                            handler: function() { 
                                view.saveAllFramesOverlay();
                            }
                        },
                        {
                            text: 'Save on All Slices and Frames',
                            handler: function() { 
                                view.saveAllSlicesFramesOverlay();
                            }
                        }
                    ]
                }
            }
            ]
        });
        this.callParent();
    },
    
    /**
     * Untoggle all buttons except the button in the argument
     */
    unToggleOthers : function(button) {
        var toolbars = this.items.items;
        _.each(toolbars, function(toolbar) {
            var buttons = toolbar.items.items;
            _.each(buttons, function(next) {
                if ((next.getXType() === "button") && next.enableToggle && (next.getId() !== button.getId())) {
                    if (next.pressed)
                        next.toggle();
                }
            });
        });
    },
    
    /**
     * Save the current overlay
     */
    saveOverlay : function (isToolClosed) {
        var overlayName = this.overlayName;
        console.log("Save overlay: " + overlayName);
        if (overlayName !== null && overlayName.length > 0) {
            this.fireEvent('saveOverlay', this.recordid, overlayName, false, false, this.siteNo, isToolClosed);
        }
    },

    /**
     * Check if the overlay is dirty, if yes, ask the user if he wants to save and do the saving
     */
    checkAndSave : function(cb,saveInProgress) {
        var view = this;
        console.log(this.colorWin);
        if(this.colorWin !== undefined)
        	this.colorWin.close();
        if (isDirty()&&!saveInProgress) {
            Ext.Msg.confirm("Save", "Save unsaved changes?", function(id)  {
                if (id === "yes") {
                    view.saveOverlay(true);
                }
                cb();
            });
        } else {
            cb();
        }
    },

    /**
     * Save overlay on all slices
     */
    saveAllSlicesOverlay : function() {
        var overlayName = this.overlayName;
        if (overlayName !== null && overlayName.length > 0) {
            this.fireEvent('saveOverlay', this.recordid, overlayName, true, false, this.siteNo,false);
        }
    },

    /**
     * Save overlay on all frames
     */
    saveAllFramesOverlay : function() {
        var overlayName = this.overlayName;
        if (overlayName !== null && overlayName.length > 0) {
            this.fireEvent('saveOverlay', this.recordid, overlayName, false, true, this.siteNo, false);
        }
    },

    /**
     * Save overlay on all frames and slices
     */
    saveAllSlicesFramesOverlay : function() {
        var overlayName = this.overlayName;
        if (overlayName !== null && overlayName.length > 0) {
            this.fireEvent('saveOverlay', this.recordid, overlayName, true, true, this.siteNo, false);
        }
    }
});
