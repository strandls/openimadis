/**
 * this is favourite application chooser.
 */
Ext.define('Manage.view.settings.FavouriteApplicationChooser', {
    extend : 'Ext.panel.Panel',
    xtype : 'favouriteapplicationchooser',
    alias : 'widget.favouriteapplicationchooser',
    
    layout : {
        type : 'vbox',
        align : 'stretch'
    },  
    
    items:[{
    	xtype:'panel',
    	flex:3,
        layout : {
            type : 'hbox',
            pack : 'center',
            align : 'stretch'
        },
	    items :[ {
	        xtype : 'availableworkflows',
	        title : 'Available Applications',
	        layout: 'fit',
	        flex: 1,
	        margins : '0 2 0 0',
	    },
	    {
	        xtype : 'selectedworkflows',
	        title : 'Favourite Applications',
	        layout: 'fit',
	        flex: 1,
	        margins : '0 2 0 0',
	    }]
    },{
    	xtype:'panel',
    	id:'addfolder',
    	flex:1
    }
    ]
});
