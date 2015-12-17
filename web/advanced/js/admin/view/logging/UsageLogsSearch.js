/*
 * Displays filters to search tasks
 */


Ext.define('Admin.view.logging.UsageLogsSearch',{
	extend : 'Ext.form.Panel',
	alias : 'widget.usageLogsSearch',
	
	initComponent:function(){
		
		this.userCombo = Ext.create('Ext.form.field.ComboBox',{
			fieldLabel : 'Username',
	        name : 'user',
	        queryMode : 'local',
	        displayField : 'login',
	        valueField : 'login',
	        width : 300,
	        editable : true,
	        typeAhead:true,
	        store : 'Admin.store.UserStore'
		});
		
		this.appCombo=Ext.create('Ext.form.field.ComboBox',{
			fieldLabel : 'Application',
	        name : 'app',
	        queryMode : 'local',
	        displayField : 'text',
	        valueField : 'clientID',
	        width : 300,
	        editable : true,
	        typeAhead:true,
	        store : 'Manage.store.AllClientStore'
		});
		
		this.fromDate=Ext.widget('datefield',{
			fieldLabel : 'From date',
	        name : 'fromDate'
		});
		
		this.toDate=Ext.widget('datefield',{
			fieldLabel : 'To date',
	        name : 'toDate'
		});
		
		var config={
			frame:true,
			border:true,
			bodyPadding : 10,
			margin: 5,
			layout: 'column',
			defaults: {
				columnWidth: 0.5
		    },
			items:[
			       {
			    	   xtype:'fieldset',
			    	   columnWidth: 0.5,
			    	   style: {
			    		    border: 0,
			    		    padding: 0
			    		},
			    	   items:[
			    	        this.userCombo,
							this.fromDate
			    	   ]
			       },
			       {
			    	   xtype:'fieldset',
			    	   columnWidth: 0.5,
			    	   style: {
			    		    border: 0,
			    		    padding: 0
			    		},
			    	   items:[
			    	       this.appCombo,
			   		       this.toDate
			    	   ]
			       }
			],
			buttons: [{
		        text : 'Search',
		        handler : function() {
		            var view = this.up('form');
		            view.fireEvent('onUsageLogsSearch');
		        }
		    	},
		    	{
			        text : 'Reset',
			        handler : function() {
			            var view = this.up('form');
			            view.userCombo.setValue(null);
			            view.appCombo.setValue(null);
			            view.fromDate.setValue(null);
			            view.toDate.setValue(null);
			        }
			    }
			]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
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