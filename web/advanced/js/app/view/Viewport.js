Ext.define('Manage.view.Viewport', {
    extend : 'Ext.container.Viewport',

    requires : [
            'Manage.view.SearchBar', 'Manage.view.Navigator', 'Manage.view.Bookmarks', 'Manage.view.Downloads',
            'Manage.view.task.TaskNavigator','Manage.view.Thumbnails', 'Manage.view.summarytable.SummaryTable',
            'Manage.view.imageview.ImageView', 'Manage.view.Header'
    ],

    initComponent : function() {
    	// init the quicktips
    	Ext.QuickTips.init();
    	
        Ext.apply(this, {
            layout : 'border',
            items : [{
                        xtype : 'imgHeader',
                        region : 'north',
                        split : 'true'
                    }, {
                        xtype : 'panel',
                        region : 'center',
                        split : 'true',
                        layout : {
                            type : 'border'
                            // align: 'stretch'
                        },
                        items : [{
                                    xtype : 'panel',
                                    region : 'west',
                                    layout:'accordion',
                                    split : 'true',
                                    collapsible: 'true',
                                    /*defaults: {
                                        // applied to each contained panel
                                        bodyStyle: 'padding:15px'
                                    },*/
                                    layoutConfig: {
                                        // layout-specific configs go here
                                        titleCollapse: false,
                                        animate: true,
                                        activeOnTop: true
                                    },
                                    width : 250,
                                    items : [{
                                                xtype : 'navigator',
                                                title : 'Navigator',
                                                iconCls : 'navigator'
                                            }, {
                                                xtype : 'searchPanel',
                                                title : 'Search',
                                                iconCls : 'search'
                                            },{
                                                xtype : 'bookmarks',
                                                title : 'Bookmarks',
                                                iconCls : 'bookmark'
                                            },{
                                                xtype : 'taskNavigator',
                                                title : 'Tasks',
                                                iconCls : 'task'
                                            },{
                                                xtype : 'downloads',
                                                title : 'Downloads',
                                                iconCls : 'download'
                                            }]
                                }, {
                                    xtype : 'panel',
                                    region : 'center',
                                    split : 'true',
                                    layout : {
                                        type : 'border'
                                    },
                                    items : [{
                                                xtype : 'panel',
                                                region : 'center',
                                                split : 'true',
                                                height : 400,
                                                layout : {
                                                    type : 'border'
                                                },
                                                /*dockedItems : [{
                                                            dock : 'top',
                                                            xtype : 'searchbar'
                                                        }],*/

                                                items : [{
                                                            xtype : 'panel',
                                                            region : 'west',
                                                            width : 150,
                                                            split : true,
                                                            autoScroll : true,
                                                            collapsible : true,
                                                            items : [{
                                                                xtype : 'thumbnails',
                                                                autoScroll : false
                                                            }]
                                                        }, {
                                                            xtype : 'imageview',
                                                            region : 'center'
                                                        }, {
                                                            xtype : 'workflows',
                                                            region : 'east',
                                                            title : 'Workflows',
                                                            collapsible : true,
                                                            collapsed : true,
                                                            width : 200
                                                        }]
                                            }, {
                                                xtype : 'summarytable',
                                                region : 'south',
                                                split : 'true',
                                                height : 100,
                                                autoScroll : true,
                                                collapsible: 'true'
                                            }]
                                }]
                    }]
        });

        this.callParent();
    }
});
