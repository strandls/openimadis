/**
 * View for publisher manipulation. This view will have the following,
 * List of publishers and some properties of publishers in a table
 */
Ext.define('Admin.view.PublisherList', {
    extend : 'Ext.grid.Panel',
    xtype : 'publisherList',
    alias : 'widget.publisherList',
    store : 'Admin.store.PublisherStore',
    title : 'Publishers',

    initComponent : function() {
        Ext.apply (this, {
            columns : [
                {header : 'Name', dataIndex : 'name', flex : 1},
                {header : 'Description', dataIndex : 'description', flex : 1},
                {
    				text : "Publisher Code",
    				dataIndex : 'publisherCode',
    				flex : 1,
    				xtype : 'componentcolumn',
					renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
						return {
							xtype : 'panel',
							border : false,
							items : [{
				        				xtype : 'button',
				        	 			tooltip : 'Show',
				        	 			text : 'Show Code',
				        	 			//icon : 'images/icons/show.png',
				        	 			handler : function() {
				        		 						var downloadMessage = 'Publisher Code is : ' + record.data.publisherCode ;
							    			    		Ext.Msg.alert("Publisher Code",  downloadMessage , function() {});
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
