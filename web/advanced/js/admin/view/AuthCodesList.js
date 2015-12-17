/**
 * View for AuthCodes manipulation. This view will have the following,
 * List of AuthCodes and 
 * properties like whether the authcode has been already used or not and 
 * operations like enabling or disabling an authcode 
 * are provided in a table
 */
Ext.define('Admin.view.AuthCodesList', {
    extend : 'Ext.grid.Panel',
    xtype : 'authCodesList',
    alias : 'widget.authCodesList',
    store : 'Admin.store.AuthCodesStore',
    title : 'Auth Codes',

    initComponent : function() {
        Ext.apply (this, {
     
        });
        this.callParent();
    },
    columns : [{
        header : 'User', dataIndex : 'userLogin', flex : 1
    }, {
        header : 'Client', dataIndex : 'client', flex : 1
    }, {
        header : 'Created On', dataIndex : 'creationTime', flex : 1,
        renderer : function(value) {
            return Ext.util.Format.date(new Date(value), 'Y-m-d g:i a');
        }
    }, {
        header : 'Expiry', dataIndex : 'expiryTime', flex : 1,
        renderer : function(value) {
            return Ext.util.Format.date(new Date(value), 'Y-m-d g:i a');
        }
    }, {
        header : 'Last Access', dataIndex : 'accessTime', flex : 1,
        renderer : function(value) {
            return Ext.util.Format.date(new Date(value), 'Y-m-d g:i a');
        }
    }, {
        header : 'Valid', dataIndex : 'validity', flex : 1
    },{
    	text : "Auth Code",
    	dataIndex : 'id',
    	flex : 1,
    	xtype : 'componentcolumn',
		renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
			return {
				xtype : 'panel',
				border : false,
				items : [
				         {
				        	 xtype : 'button',
				        	 tooltip : 'Show',
				        	 text: 'Show Code',
				        	 handler : function() {
				        		 var downloadMessage = 'Auth Code is : ' + record.data.id ;
							
				        		 Ext.Msg.alert("Authorization Code",  downloadMessage , function() {});
				        	 }
				         }
				         ]
 			};
		}
	},
	    {
        
    	header: 'Disable',
        align: 'center',
        dataIndex: 'validity',
        flex : 1,
		xtype : 'componentcolumn',
		renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
			return {
				xtype : 'panel',
				border : false,
				items : [
				         {
				         	xtype : 'button',
							tooltip : 'Disable',
							icon : 'images/icons/cancelled.png',
							handler : function() {
								var disableMessage = 'Disable token : ' + record.data.id + 'for user : ' + record.data.userLogin;
								this.authCodeId = record.data.id ; 
								Ext.Msg.alert("Disable",  disableMessage , function () {
									//var url = "../admin/disableAuthCode?id="  + this.authCodeId ;
									//window.location = url;
									var authid = this.authCodeId ;
									Ext.Ajax.request( {
										method : 'GET',
										url : '../admin/disableAuthCode',
										params : {
											'id' : authid
										}
									});
								} , this);
							}
				         }
						]
				
			};
		}
	}]
}); 
