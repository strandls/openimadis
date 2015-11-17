/*
 * Displays filters to search tasks
 */
Ext.define('Manage.view.admin.UsageLogSearch',{
	extend : 'Ext.form.Panel',
	alias : 'widget.usageLogSearch',


	layout: {
		type: 'hbox',
		align: 'stretch'
	},

	defaults: {
		margin: '10px',
		flex: 1
	},
	
	initComponent:function(){
		
		this.userCombo = Ext.create('Ext.form.field.ComboBox',{
			fieldLabel : 'Username',
		labelAlign: 'top',
	        name : 'user',
	        queryMode : 'local',
	        displayField : 'login',
	        valueField : 'login',
	        width : 300,
	        editable : true,
	        typeAhead:true,
	        store : 'admin.Users'
		});
		
		this.appCombo=Ext.create('Ext.form.field.ComboBox',{
			fieldLabel : 'Application',
		labelAlign: 'top',
	        name : 'app',
	        queryMode : 'local',
	        displayField : 'text',
	        valueField : 'clientID',
	        width : 300,
	        editable : true,
	        typeAhead:true,
	        store : 'AllClients'
		});
		
		this.fromDate=Ext.widget('datefield',{
			fieldLabel : 'From date',
		labelAlign: 'top',
	        name : 'fromDate'
		});
		
		this.toDate=Ext.widget('datefield',{
			fieldLabel : 'To date',
		labelAlign: 'top',
	        name : 'toDate'
		});
		
		Ext.apply(this, {
			items:[
				this.userCombo,
				this.appCombo,
				this.fromDate,
				this.toDate
		       ],
			buttons: [{
				text : 'Search',
				handler : function() {
					var view = this.up('form');
					view.fireEvent('onUsageLogSearch');
				}
			}, {
				text : 'Reset',
				handler : function() {
					this.up('form').getForm().reset();
				}
			}]
		});
		this.callParent();
	},

    validate: function(){
    	var form=this.getForm();
    	if(form.isValid()==false){
    		return false;
    	}
    	return true;
    }
});
