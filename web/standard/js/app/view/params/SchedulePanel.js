Ext.define('Manage.view.params.SchedulePanel', {
    extend : 'Ext.form.Panel',
    xtype:'schedulePanel',
    alias:'widget.schedulePanel',
    bodyPadding : 10,
    initComponent : function() {
	
		var config={
        	items:[
				{
				    xtype: 'combo',
					fieldLabel: 'Task Priority',
				    store: 'PriorityStore',
				    queryMode: 'local',
				    displayField: 'text',
				    valueField: 'value',
				    value:'HIGH',
				    name:'priority',
				    anchor: '70%'
				} ,
				{
			        xtype: 'radiogroup',
			        fieldLabel: 'Schedule option',
			        columns: 2,
			        vertical: true,
			        items: [
			            { boxLabel: 'Execute now', name: 'when', inputValue: 'now', checked: true },
			            { boxLabel: 'Schedule later', name: 'when', inputValue: 'later'}
			        ]
			    },
				{
			        xtype: 'datefield',
			        anchor: '70%',
			        fieldLabel: 'Scheduled date',
			        name: 'scheduledDate',
			        allowBlank:false,
			        disabled:true
			    },	
    			{
				    xtype: 'timefield',
				    name: 'scheduledTime',
				    fieldLabel: 'Scheduled time',
				    format: 'h:i A',
				    minValue: '12:00 AM',
				    maxValue: '11:30 PM',
				    disabled:true,
				    allowBlank:false,
				    increment: 30,
				    anchor: '70%'
				},
				{
					xtype: 'checkbox',
					boxLabel  : 'Monitor this task in task monitor',
                    name      : 'isMonitored',
                    checked: true
				}
        	]	
        };
        
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        this.callParent();
    },
    
    markAsReadOnly: function(taskDetails){
    	this.down('radiogroup[fieldLabel="Schedule option"]').hide();
    	//query with checkbox wont work so use checkboxfield
    	this.down('checkboxfield[name="isMonitored"]').hide();
    	
    	var priorityCombo=this.down('combobox[name="priority"]');
    	priorityCombo.setValue(taskDetails.priority);
    	priorityCombo.setReadOnly(true);
    	priorityCombo.setDisabled(true);
    	
    	var scheduledtime=new Date(taskDetails.scheduledtime);
    	var dateField=this.down('datefield[name="scheduledDate"]');
    	dateField.setValue(scheduledtime);
    	dateField.setDisabled(true);
    	
    	var timeField=this.down('timefield[name="scheduledTime"]');
    	timeField.setValue(scheduledtime);
    	timeField.setReadOnly(true);
    }
    
    
});