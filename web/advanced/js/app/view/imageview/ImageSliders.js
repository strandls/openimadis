Ext.require([
    'Ext.slider.*', 
    'Ext.form.*',
    'Ext.window.MessageBox'
]);

/**
 * Slice and frame sliders view
 */
Ext.define('Manage.view.imageview.ImageSliders', {
    extend:'Ext.form.Panel',
    xtype:'imagesliders',
    alias:'widget.imagesliders',
    initComponent:function() {
        var page =this;
        this.menu = this.createMenu();
        var config = {
            width: 400,
            items: [{
                    layout : {
                        type : 'hbox'
                    },
                    border:false,
                    id : 'slicepanel',
                    bodyPadding: 5,
                    items:[
                        {
                            xtype:'label',
                            name:'slicefield',
                            id:'slicefield',
                            html:'Z: ',
                            flex: 1
                        },{
                            xtype:'button',
                            id: 'button_previous_1',
                            iconCls: 'back',
                            tooltip : 'Previous',
                            handler : function(button, e) {
                                button.up().up().fireEvent('previousSlice', button);
                            },
                            style : {
                                "background-image" : "none",
                                "border" : "none"
                            }
                        },
                        {
                            xtype: 'sliderfield',
                            value: 0,
                            name: 'slices',
                            id: 'sliceslider',
                            minValue: 0,
                            maxValue: 1,
                            flex:8,
                            tipText: function(thumb){
                                return String(thumb.value + 1);
                            }
                        },{
                            xtype:'button',
                            id: 'button_forward_1',
                            iconCls: 'forward',
                            tooltip : 'Next',
                            handler : function(button, e) {
                                button.up().up().fireEvent('nextSlice', button);
                            },
                            style : {
                                "background-image" : "none",
                                "border" : "none"
                            }
                        }
                        , {
                            xtype:'button',
                            id:'button_movie1',
                            iconCls:'playMovie',
                            enableToggle: 'true',
                            tooltip: 'Play Movie',
                            toggleHandler:function(btn,state){
                                if (!state) {
                                    btn.setIconCls('playMovie'); 
                                    this.setTooltip("Play Movie");
                                    btn.up().up().fireEvent('stopMovie', btn);
                                } else {
                                    this.setTooltip("Pause Movie");
                                    btn.up().up().fireEvent('playSliceMovie', btn);
                                    btn.setIconCls('pauseMovie');
                                }
                            },
                            style : {
                                 "background-image" : "none",
                                 "border" : "none"
                            }
                        },{
                            xtype:'button',
                            id: 'slice_video_submit',
                            iconCls: 'movie',
                            tooltip : 'Submit Video on Slice',
                            handler : function(button, e) {
                                button.up().up().fireEvent('submitSliceVideo', button);
                            },
                            style : {
                                "background-image" : "none",
                                "border" : "none"
                            }
                        }, {
                            xtype : 'button',
                            id : 'button_movie1_speed',
                            hidden : true,
                            tooltip : 'Choose Speed',
                            menu : this.menu,
                            iconCls : 'zoom',
                            style : {
                                 "background-image" : "none",
                                 "border" : "none"
                            }
                        }
                        ]
                },{
                    layout : {
                        type : 'hbox'
                    }, 
                    bodyPadding: 5,
                    id : 'framepanel',
                    items:[{
                            xtype: 'label',
                            name:'framefield',
                            flex: 1,
                            html: 'T: ',
                            id:'framefield'
                        },{
                            xtype:'button',
                            id: 'button_previous_2',
                            iconCls: 'back',
                            tooltip : 'Previous',
                            handler : function(button, e) {
                                button.up().up().fireEvent('previousFrame', button);
                            },
                            style : {
                                "background-image" : "none",
                                "border" : "none"
                            }
                        },{
                            xtype: 'sliderfield',
                            value: 0,
                            name: 'frames',
                            id: 'frameslider',
                            minValue: 0,
                            maxValue: 1,
                            flex:8,
                            tipText: function(thumb){
                                return String(thumb.value + 1);
                            } 
                        },{
                            xtype:'button',
                            id: 'button_forward_2',
                            iconCls: 'forward',
                            tooltip : 'Next',
                            handler : function(button, e) {
                                button.up().up().fireEvent('nextFrame', button);
                            },
                            style : {
                                "background-image" : "none",
                                "border" : "none"
                            }
                        }
                        ,
                        {
                            xtype:'button',
                            id:'button_movie2',
                            iconCls:'playMovie',
                            enableToggle: 'true',
                            tooltip: 'Play Movie',
                            toggleHandler:function(btn,state){
                                if (!state) {
                                    btn.setIconCls('playMovie'); 
                                    this.setTooltip("Play Movie");
                                    btn.up().up().fireEvent('stopMovie', btn);
                                } else {
                                    this.setTooltip("Pause Movie");
                                    btn.up().up().fireEvent('playFrameMovie', btn);
                                    btn.setIconCls('pauseMovie');
                                }
                            },
                            style : {
                                "background-image" : "none",
                                "border" : "none"
                            }
                        },{
                            xtype:'button',
                            id: 'frame_video_submit',
                            iconCls: 'movie',
                            tooltip : 'Submit Video on Frames',
                            handler : function(button, e) {
                                button.up().up().fireEvent('submitFrameVideo', button);
                            },
                            style : {
                                "background-image" : "none",
                                "border" : "none"
                            }
                        }, {
                            xtype : 'button',
                            id : 'button_movie2_speed',
                            tooltip : 'Choose Speed',
                            hidden : true,
                            menu : this.menu,
                            iconCls : 'zoom',
                            style : {
                                 "background-image" : "none",
                                 "border" : "none"
                            }
                        }
                        ]
                }]

        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        this.callParent();
    },
    
    createMenu:function(){
        var page =this;
        var menu = Ext.create('Ext.menu.Menu', {
            id: 'mainMenu',
            style: {
                overflow: 'visible'     // For the Combo popup
            },
            items: [{
                text : 'Slow',
                checked : false,
                group : 'speed',
                listeners : {
                    click : function() {
                        page.onChooseSpeed(2000);
                    }
                }
            }, {
                text : 'Medium',
                checked : true,
                group : 'speed',
                listeners : {
                    click : function() {
                        page.onChooseSpeed(1000);
                    }
                }
            }, {
                text : 'Fast',
                checked :false,
                group : 'speed',
                listeners : {
                    click : function() {
                        page.onChooseSpeed(500);
                    }
                }
            }]
        });
        return menu;
    },
    onChooseSpeed : function(delay){
        this.fireEvent("chooseSpeed", delay);        	
    }
});
