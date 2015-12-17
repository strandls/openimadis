/**
 * Workflow browser. Arranges the workflow items into
 * categories and puts them in a tree with two levels.
 * The first level has the categories while the second,
 * the workflows.
 */
Ext.define('Manage.view.workflow.Workflows', {
    extend : 'Ext.tree.Panel',
    xtype : 'workflows',
    alias : 'widget.workflows',
    store : 'Manage.store.WorkflowTreeStore',
    rootVisible : false,
    lines : false,
    bodyCls : 'workflowbrowser',
    initComponent : function() {
		var dockButtons = [{
	        icon : "images/icons/images.png",
	        tooltip : 'Manage Clients',
	        handler : function() {
	            this.up().up().fireEvent("viewClients");
	        }
	    }, {
	        icon : "images/icons/password.png",
	        tooltip : 'Manage Auth Tokens',
	        handler : function() {
	            this.up().up().fireEvent("viewTokens");
	        }
	    }];
		
		var publisherButton = {
	        icon : "images/icons/publisher.png",
	        tooltip : 'Manage Publishers',
	        xtype : 'button',
	        handler : function() {
	            this.up().up().fireEvent("viewPublishers");
	        }
	    };
	    
		if(canAddPublisher)
		{
			dockButtons.push(publisherButton);
		}
		
		Ext.apply (this, {
            dockedItems : [{
                xtype : 'toolbar',
                items : dockButtons
            }]    
        });
		
        this.callParent();
	},
    listeners : {
        itemclick : function (view, record, item, index, e, opts) {
            var childCount = record.childNodes.length;
            if (childCount > 0) {
                if (record.isExpanded())
                    record.collapse();
                else
                    record.expand();
            } else {
            	view.up().fireEvent("requestAppExecution", record.data.name,
            			record.data.version, record.data.description);
            }
        },
        beforeitemdblclick : function() {
            return false;
        }
    }
});
