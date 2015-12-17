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
                    'icon' : 'images/icons/draw/path.png',
                    'tooltip' : 'Freehand',
                    'scale' : 'medium',
                    enableToggle : true,
                    toggleHandler : function(btn, state) {
                        if (state) {
                            view.unToggleOthers(this);
                            sketchpad.mode("drawing");
                            sketchpad.pen("pencil");
                        } else {
                            sketchpad.mode(false);
                        }
                    }
                }, {
                    'icon' : 'images/icons/draw/ellipse.png',
                    'tooltip' : 'Ellipse',
                    'scale' : 'medium',
                    enableToggle : true,
                    toggleHandler : function(btn, state) {
                        if (state) {
                            view.unToggleOthers(this);
                            sketchpad.mode("drawing");
                            sketchpad.pen("ellipse");
                        } else {
                            sketchpad.mode(false);
                        }
                    }
                }, {
                    'icon' : 'images/icons/draw/rect.png',
                    'tooltip' : 'Rectange',
                    'scale' : 'medium',
                    enableToggle : true,
                    toggleHandler : function(btn, state) {
                        if (state) {
                            view.unToggleOthers(this);
                            sketchpad.mode("drawing");
                            sketchpad.pen("rectangle");
                        } else {
                            sketchpad.mode(false);
                        }
                    }
                }, {
                    icon : 'images/icons/draw/line.png',
                    tooltip : 'Line',
                    scale : 'medium',
                    enableToggle : true,
                    toggleHandler : function(btn, state) {
                        if (state) {
                            view.unToggleOthers(this);
                            sketchpad.mode("drawing");
                            sketchpad.pen("line");
                        } else {
                            sketchpad.mode(false);
                        }
                    }
                }, {
                    icon: 'images/icons/draw/text.png',
                    tooltip : 'Text',
                    scale : 'medium',
                    enableToggle : true,
                    toggleHandler : function(btn, state) {
                        if (state) {
                            view.unToggleOthers(this);
                            sketchpad.mode("drawing");
                            sketchpad.pen("text");
                        } else {
                            sketchpad.mode(false);
                        }
                    }
                }, {
                    icon : 'images/icons/draw/select.png',
                    tooltip : 'Select',
                    scale : 'medium',
                    enableToggle : true,
                    toggleHandler : function(btn, state) {
                        if (state) {
                            view.unToggleOthers(this);
                            sketchpad.mode("select");
                        } else {
                            sketchpad.mode(false);
                        }
                    }
                }, {
                    icon : 'images/icons/delete.png',
                    tooltip : 'Erase',
                    scale : 'medium',
                    handler : function() {
                        sketchpad.erase();
                    }
                }]
            }, {
                xtype : 'toolbar',
                items : [{
                    icon : 'images/icons/draw/undo.png',
                    tooltip : 'Undo',
                    scale : 'medium',
                    handler : function() {
                        sketchpad.undo();
                    }
                }, {
                   icon : 'images/icons/draw/redo.png',
                   tooltip : 'Redo',
                   scale : 'medium',
                   handler : function() {
                        sketchpad.redo();
                   }
                }, {
                    tooltip : 'Choose Color',
                    style : {
                        "background-color" : sketchpad.pen().color(),
                        "marginRight" : '10px',
                        "paddingRight" : '10px'
                    },
                    border : 3,
                    scale : 'medium',
                    handler : function(button, e) {
                        var win = Ext.create('Ext.window.Window', {
                            title : 'Color Picker',
                            width : 90,
                            height : 100,
                            items : [{
                                xtype : 'colorpicker',
                                value : sketchpad.pen().color(),
                                listeners : {
                                    select : function(picker, selcolor) {
                                        console.log("Selected color: " + selcolor);
                                        sketchpad.color('#'+selcolor);
                                        button.getEl().setStyle('background-color', '#'+selcolor);
                                        picker.up().close();
                                    }
                                }
                            }]
                        });
                        win.showAt(e.getXY());
                    }
                }, {
                    xtype : 'numberfield',
                    fieldLabel : 'Width',
                    labelAlign : 'top',
                    minValue : 1,
                    maxValue : 30,
                    value : sketchpad.pen().width(),
                    width : 50,
                    listeners : {
                        change : function(field, newValue, oldValue, opts) {
                            sketchpad.width(newValue);
                        }
                    }
                }, /*{
                    xtype : 'numberfield',
                    fieldLabel : 'Opacity',
                    labelAlign : 'top',
                    minValue : 0.1,
                    step : 0.1,
                    allowDecimals : true,
                    maxValue : 1.0,
                    value : sketchpad.pen().opacity(),
                    width : 50,
                    listeners : {
                        change : function(field, newValue, oldValue, opts) {
                            sketchpad.opacity(newValue);
                        }
                    }
                },*/ {
                    xtype : 'numberfield',
                    fieldLabel : 'Font Size',
                    labelAlign : 'top',
                    minValue : 1,
                    allowDecimals : false,
                    maxValue : 50,
                    value : sketchpad.pen().fontSize(),
                    width : 50,
                    listeners : {
                        change : function(field, newValue, oldValue, opts) {
                            sketchpad.fontSize(newValue);
                        }
                    }
                }]
            }],
            buttons : [{
                text : 'Save',
                xtype : 'splitbutton',
                handler : function() {
                    view.saveOverlay();
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
            },{
                text: 'Close',
                handler: function() {
                    view.up().close();
                }
            }]
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
    saveOverlay : function () {
        var overlayName = this.overlayName;
        console.log("Save overlay: " + overlayName);
        if (overlayName !== null && overlayName.length > 0) {
            this.fireEvent('saveOverlay', this.recordid, overlayName, false, false, this.siteNo, sketchpad.json());
        }
    },

    /**
     * Check if the overlay is dirty, if yes, ask the user if he wants to save and do the saving
     */
    checkAndSave : function(cb) {
        var view = this;
        if (sketchpad.isDirty()) {
            Ext.Msg.confirm("Save", "Save unsaved changes?", function(id)  {
                if (id === "yes") {
                    view.saveOverlay();
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
            this.fireEvent('saveOverlay', this.recordid, overlayName, true, false, this.siteNo, sketchpad.json());
        }
    },

    /**
     * Save overlay on all frames
     */
    saveAllFramesOverlay : function() {
        var overlayName = this.overlayName;
        if (overlayName !== null && overlayName.length > 0) {
            this.fireEvent('saveOverlay', this.recordid, overlayName, false, true, this.siteNo, sketchpad.json());
        }
    },

    /**
     * Save overlay on all frames and slices
     */
    saveAllSlicesFramesOverlay : function() {
        var overlayName = this.overlayName;
        if (overlayName !== null && overlayName.length > 0) {
            this.fireEvent('saveOverlay', this.recordid, overlayName, true, true, this.siteNo, sketchpad.json());
        }
    }
});
