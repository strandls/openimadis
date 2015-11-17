/*
 * Displays filters to search tasks
 */
Ext.define('Manage.view.TaskSearchPanel',{
	extend : 'Ext.form.Panel',
	alias : 'widget.taskSearchPanel',

	initComponent:function(){

		this.projectCombo=Ext.create('Ext.form.field.ComboBox',{
			fieldLabel : 'Project',
			name : 'projectName',
			queryMode : 'local',
			displayField : 'name',
			valueField : 'name',
			allowBlank : false,
			width : 300,
			editable : true,
			typeAhead:true,
			store : 'Projects',
			listeners : {
				scope : this,
				change : function(field, newValue, oldValue, opts) {
					this.onProjectChange(newValue);
				}
			}
		});

		this.ownerCombo = Ext.create('Ext.form.field.ComboBox',{
			fieldLabel : 'Task Owner',
			name : 'owner',
			queryMode : 'local',
			displayField : 'user',
			valueField : 'user',
			width : 300,
			editable : true,
			typeAhead:true,
			store : 'Memberships'
		});

		this.priorityCombo=Ext.create('Ext.form.field.ComboBox',{
			fieldLabel : 'Task Priority',
			name : 'priority',
			queryMode : 'local',
			displayField : 'text',
			valueField : 'value',
			width : 300,
			editable : true,
			typeAhead:true,
			store : 'PriorityStore'
		});

		this.statusCombo=Ext.create('Ext.form.field.ComboBox',{
			fieldLabel : 'Task Status',
			name : 'taskState',
			queryMode : 'local',
			displayField : 'text',
			valueField : 'value',
			width : 300,
			editable : true,
			validateOnBlur:true,
			validateOnChange:true, 
			typeAhead:true,
			store : 'TaskStates'

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
			store : 'WorkflowClients'
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
			bodyPadding : 10,
			margin: 5,
			layout: {
				type: 'hbox',
				align: 'stretch'
			},
			defaults: {
				flex: 1
			},

			items:[{
				xtype:'fieldset',
				style: {
					border: 0,
					padding: 0
				},
				items:[
					this.projectCombo,
					this.appCombo,
					this.fromDate,
					this.toDate
				]
			}, {
				xtype:'fieldset',
				style: {
					border: 0,
					padding: 0
				},
				items:[
					this.ownerCombo,
					this.priorityCombo,
					this.statusCombo
				]
			}],
			buttons: [{
				text : 'Search',
				handler : function() {
					var view = this.up('form');
					view.fireEvent('searchtask');
				}
			}]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		this.callParent();
	},

	/**
	* Handler called when project is changed from project chooser
	*/
	onProjectChange : function(project) {
		this.ownerCombo.store.load({
		params : {
			projectName : project
		}
		});
	},

	validate: function(){
		var form=this.getForm();
		if(form.isValid()==false){
			return false;
		}
		//console.log(form.getRecord());
		return true;
	}
});
