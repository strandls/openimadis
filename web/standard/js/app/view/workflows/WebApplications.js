/**
 * Workflow browser. Arranges the workflow items into
 * categories and puts them in a tree with two levels.
 * The first level has the categories while the second,
 * the workflows.
 */
Ext.define('Manage.view.workflows.WebApplications', {
	extend : 'Ext.tree.Panel',
	xtype : 'webApplications',
	alias : 'widget.webApplications',

	store : 'WebApplicationsTree',

    useArrows: true,
    singleExpand: true,	
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
	        text: 'name',
	        flex: 1,
	        hidden: true,
	        dataIndex: 'name'
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
				console.log(record);
				view.up().fireEvent("requestInvokeLink", record.data.id, record.data.name, record.data.description, record.data.url);
			}
		},
		beforeitemdblclick : function() {
			return false;
		}
	}        

    
});


