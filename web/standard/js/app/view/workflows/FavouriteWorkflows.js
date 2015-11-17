/**
 * Workflow browser. Arranges the workflow items into
 * categories and puts them in a tree with two levels.
 * The first level has the categories while the second,
 * the workflows.
 */
Ext.define('Manage.view.workflows.FavouriteWorkflows', {
	extend : 'Ext.panel.Panel',
	xtype : 'favouriteworkflows',
	alias : 'widget.favouriteworkflows',		
	
	items:[{
		
	xtype :'treepanel',
	store :'FavouriteWorkflowTree',

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
		        text: 'appName',
		        flex: 1,
		        hidden: true,
		        dataIndex: 'appName'
	    }],
	    
		listeners : {
			itemclick : function (view, record, item, index, e, opts) {
				var childCount = record.childNodes.length;
				if (childCount > 0) {
					if (record.isExpanded())
						record.collapse();
					else
						record.expand();
				} else {
					view.up().up().fireEvent("requestAppExecution", record.data.appName,
					record.data.version, record.data.description);
				}
			},
			beforeitemdblclick : function() {
				return false;
			}
		}  
	}]      

    
});


