/**
 * View for listing all the publishers supporting compute infrastructure
 */
Ext.define('Manage.view.workflow.ViewPublishers', {
    extend : 'Ext.grid.Panel',
    xtype : 'viewPublishers',
    alias : 'widget.viewPublishers',
    bodyPadding: 5,
    store : 'Manage.store.PublisherStore',

    initComponent : function() {
			Ext.apply(this, {
	    		tbar : [
			        	{
		    				icon : 'images/icons/add.png',
		                    tooltip : 'Add Publisher',
		                    handler : function() {
		                        this.up().up().fireEvent("addPublisher");
		                    }
		    		    }
	    		]
	        });
        this.callParent();
    },

    columns : [{
        header : 'Name', dataIndex : 'name', flex : 1
    }, {
        header : 'Description', dataIndex : 'description', flex : 2
    }, {
        header : 'Publisher Code',
        dataIndex : 'publisherCode',
        xtype : 'componentcolumn',
        dontSort : true,
        renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
            return {
                xtype : 'panel',
                border : false,
                layout : {
                    type : 'hbox',
                    pack : 'center',
                    align : 'middle'
                },
                items : [
                    {
                        xtype : 'button',
                        text : 'Show',
                        handler : function() {
                            Ext.Msg.alert("Publisher Code", value);
                        }
                    }
                ]
            }; 
        }
    }]
});
