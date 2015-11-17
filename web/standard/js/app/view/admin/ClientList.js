/**
 * View for client manipulation. This view will have the following,
 * List of clients and some properties of clients in a table
 */
Ext.define('Manage.view.admin.ClientList', {
    extend : 'Ext.grid.Panel',
    xtype : 'clientList',
    alias : 'widget.clientList',
    store : 'admin.Clients',
    title : 'Clients',

    initComponent : function() {
        Ext.apply (this, {
            columns : [
                {header : 'Name', dataIndex : 'name', flex : 1},
                {header : 'Version', dataIndex : 'version', flex : 1},
                {header : 'Description', dataIndex : 'description', flex : 1},
                {header : 'Link', dataIndex : 'url', flex : 1},
                {
    				text : "Client ID",
    				dataIndex : 'clientId',
    				flex : 1,
    				xtype : 'componentcolumn',
					renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
						return {
							xtype : 'panel',
							border : false,
							items : [{
				        				xtype : 'button',
				        	 			tooltip : 'Show',
				        	 			text : 'Show ID',
				        	 			//icon : 'images/icons/show.png',
				        	 			handler : function() {
				        		 						var downloadMessage = 'Client id is : ' + record.data.clientId ;
							    			    		Ext.Msg.alert("Client Id",  downloadMessage , function() {});
				        	 					  }
				         	}]
 						};
					}
				},
				{
    				text : "Remove",
    				dataIndex : 'clientId',
    				flex : 1,
    				xtype : 'componentcolumn',
					renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
						return {
							xtype : 'panel',
							border : false,
							items : [{
				        				xtype : 'button',
				        	 			tooltip : 'Remove',
				        	 			text : 'Remove',
				        	 			//icon : 'images/icons/show.png',
				        	 			handler : function() {
				        	 				var _this = this;
				        	 				Ext.Msg.confirm("Delete", " All the authcodes related to this client will not be functional after deletion. Are you sure you want to delete the selected " + record.data.clientId + " client?", 
				        	 					function(id)  {
				        	 						if (id === "yes") {
				        	 							var id = record.data.clientId ;
								        	 			var pan = _this.up('clientList');
								        	 		   	pan.fireEvent('removeClient', id);
				        	 						}
				        	 					}
				        	 				);
				        	 			}
				         	}]
 						};
					}
				},
            ]
        });
        this.callParent();
    },
    
    tbar: [{
	    icon : 'images/icons/add.png',
	    text : 'Add',
	    tooltip : 'Add Client',
	    handler : function() {
	    	var pan = this.up('clientList');
	    	pan.fireEvent('addClient');
	    }
    }]
});
