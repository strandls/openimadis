/**
 * View for client manipulation. This view will have the following,
 * List of clients and some properties of clients in a table
 */
Ext.define('Admin.view.ClientList', {
    extend : 'Ext.grid.Panel',
    xtype : 'clientList',
    alias : 'widget.clientList',
    store : 'Admin.store.ClientStore',
    title : 'Clients',

    initComponent : function() {
        Ext.apply (this, {
            columns : [
                {header : 'Name', dataIndex : 'name', flex : 1},
                {header : 'Version', dataIndex : 'version', flex : 1},
                {header : 'Description', dataIndex : 'description', flex : 1},
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
				}
            ]
        });
        this.callParent();
    }
});
