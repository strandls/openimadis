/**
 * View for AuthCodes manipulation. This view will have the following,
 * List of AuthCodes and 
 * properties like whether the authcode has been already used or not and 
 * operations like enabling or disabling an authcode 
 * are provided in a table
 */
Ext.define('Admin.view.LicenseList', {
    extend : 'Ext.grid.Panel',
    xtype : 'licenseList',
    alias : 'widget.licenseList',
    store : 'Admin.store.LicenseStore',
    title : 'Acquisition Licenses',

    initComponent : function() {
        Ext.apply (this, {
        	
        });
        this.callParent();
    },
    
    dockedItems : [{
        xtype : 'toolbar',
        items : [{
			icon : "js/extjs/resources/themes/images/default/grid/refresh.gif",
			tooltip : 'Refresh',
			handler : function() {
				this.up('licenseList').refresh();
			}
		},'->',{
			xtype: 'label',
			text: '',
			itemId:'countLabel',
		    listeners: {
		        'beforerender': function(){
		        	this.up('licenseList').refresh();
		        }
		    }
		}
        ]
    }],
    
    /**
     * refresh the store
     */
    refresh : function() {
		var store = this.items.items[0].store;
		store.load();
		
		var label = this.down('label[itemId="countLabel"]');
		Ext.Ajax.request({
            method : 'GET',
            url : '../admin/getLicenseCounts',
            success : function(result, request) {
                var text = Ext.decode(result.responseText);
                console.log(text);
                label.setText("License Usage: Fixed:"+text.fixed+" Floating:"+text.floating+" Total:"+text.total);
            },
            failure : function(result, request) {
            }
        });
	},
    
    columns : [{
        header : 'User', dataIndex : 'user', flex : 1
    },  {
        header : 'Created On', dataIndex : 'creationTime', flex : 1,
        renderer : function(value) {
            return Ext.util.Format.date(new Date(value), 'Y-m-d g:i a');
        }
    }, {
    	text : "Auth Code",
    	dataIndex : 'accessToken',
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
				        		 var downloadMessage = 'Auth Code is : ' + record.data.accessToken ;
							
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
								var disableMessage = 'Disable token : ' + record.data.accessToken + 'for user : ' + record.data.user;
								this.authCodeId = record.data.accessToken ; 
								Ext.Msg.alert("Disable",  disableMessage , function () {
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
