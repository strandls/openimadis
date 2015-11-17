/**
 * Workflow browser. Arranges the workflow items into
 * categories and puts them in a tree with two levels.
 * The first level has the categories while the second,
 * the workflows.
 */
Ext.define('Manage.view.workflows.SelectedWorkflows', {
	extend : 'Ext.panel.Panel',
	xtype : 'selectedworkflows',
	alias : 'widget.selectedworkflows',
	
    dockedItems : [{
        xtype : 'toolbar',
        items : [{
            icon : 'images/icons/add.png',
            text : 'Add',
            tooltip : 'Add Favourite Folder',
            listeners:{
            	click: function(view){
            		view.up().up().fireEvent("addFolder");
            	}
            }
        }, ' ' , '-', ' ', {
            icon : 'images/icons/delete.png',
            text : 'Remove',
            tooltip : 'Remove Favourite Folder',
            listeners:{
            	click: function(view){
            		view.up().up().fireEvent("removeFolder");
            	}
            }
        }]
    }],
    
    items:[{
    	xtype:'treepanel',
    	store : 'SelectedWorkflowTree',
    	
    	id:'selectedworkflowsgrid',
    	
        useArrows: true,
        rootVisible: false,
        hideHeaders: true,			
    	lines : false,
    	height: 200,
    	autoscroll:true,
    	
	    //the 'columns' property is now 'headers'
	    columns: [{
		        xtype: 'treecolumn', //this is so we know which column will show the tree
		        text: 'Category',
		        flex: 1,
		        sortable: true,
		        dataIndex: 'name'
		    },{
		    	text: 'Remove',
		        width: 55,
		        menuDisabled: true,
		        xtype: 'actioncolumn',
		        tooltip: 'Remove from favourite',
		        align: 'center',
		        handler: function(view, rowIndex, colIndex, actionItem, event, record, row) {
		        	view.up().up().fireEvent('removeFromFavourite',record); 
		        },
		        items:[
		               {
		                  icon: 'images/icons/delete.png',
		                  id: 'addToFavourite',
		
		                 getClass: function(Value, metaData, record){
		                            if(!record.data.leaf)
		                                metaData.css = 'x-hide-display';
		                            else
		                                metaData.css = 'x-grid-icon'
		                 }
		              }
		         ]
		    },{
		        text: 'appName',
		        flex: 1,
		        hidden: true,
		        dataIndex: 'appName'
	    }]
    
    }]

    
});


