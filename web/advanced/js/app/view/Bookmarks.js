
Ext.require([
    'Ext.tree.*'
]);

Ext.define('Manage.view.Bookmarks', {
    extend: 'Ext.tree.Panel',
    xtype : 'bookmarks',
    alias: 'widget.bookmarks',
    store: 'Manage.store.BookmarkStore',
    bodyPadding : 5,
    rootVisible : true,
    fields:[ 'text','recordId'],
    viewConfig: {
        plugins: {
            ptype: 'treeviewdragdrop'
        }
    },
    useArrows: true,
    dockedItems: [{
        xtype: 'toolbar',
        items: [{
        	icon : "images/icons/add.png",
            tooltip: 'Add Bookmark',
            handler:function(){
            	this.up().up().fireEvent('addFolder',false);
            }
        }, {
        	icon : "images/icons/delete.png",
        	tooltip: 'Remove Bookmark',
            handler:function(){
            	this.up().up().fireEvent('removeFolder',false);
            }
        }
        ]
    }],
    listeners: {
        itemclick: function(node, event){
        	
        },
        select:function(view, record, item, index, event){
        	
        }
    }
    
    
});
