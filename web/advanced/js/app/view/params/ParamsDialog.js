/**
 * Generate params component based on the json specification.
 * See README for more details
 */
Ext.require(['Manage.view.params.ParamsPanel',
             'Manage.view.params.SchedulePanel',
             'Manage.view.params.SelectedRecords']);

Ext.define('Manage.view.params.ParamsDialog', {
    extend : 'Ext.panel.Panel',
    xtype:'paramsDialog',
    alias : 'widget.paramsDialog',
    layout : 'border',
    
    initComponent : function() {
        var itemConfigs = this.itemConfigs;
        this.paramsPanel= Ext.create('Manage.view.params.ParamsPanel', {
        	border:false,
            itemConfigs : itemConfigs
        });
        
        this.schedulePanel=Ext.create('Manage.view.params.SchedulePanel', {
        	border:false
        });
        
        this.selRecords=Ext.create('Manage.view.params.SelectedRecords', {
        	region : 'south'
        });
        
        var configPanel = Ext.create('Ext.panel.Panel', {
            items : [this.paramsPanel,this.schedulePanel],
            autoScroll :true,
            region : 'center'
        });
        var banner = Ext.create('Ext.panel.Panel', {
            region : 'north',
            html : this.description,
            bodyStyle : {
                background : '#F7F7F7'
            },
            bodyPadding : 5,
            height : 40
        });
        
        
        
        Ext.apply(this, {
        	border:false,
            items : [ configPanel, banner,this.selRecords],
            //title : this.appName,
            buttons: [{
                text : 'Cancel',
                handler : function() {
                    var view = this.up('window');
                    view.close();
                }
            },{
                text: 'Reset',
                handler: function() {
            		var view = this.up().up();
            		view.paramsPanel.getForm().reset();
            		view.schedulePanel.getForm().reset();
                }
            },{
                text : 'Submit',
                handler : function() {
                    var view = this.up().up();
                    if(view.schedulePanel.getForm().isValid()
                    		&& view.paramsPanel.getForm().isValid() ){
                    	var params=view.schedulePanel.getForm().getValues();
                    	params['paramValues']=Ext.encode(view.paramsPanel.getForm().getValues());
                        params['appName']=view.appName;
                        params['appVersion']=view.appVersion;
                        
                        var records=view.selRecords.getSelectedRecordIds();
                        if(records.length < 1){
                        	Ext.Msg.alert("Error", "No records selected");
                        }
                        else{
                        	params['guids']=Ext.encode(records);
                        	view.fireEvent("submitAppExecution",view.up(),params);
                        }
                    }
                }
            }] 
        });
        this.callParent();
    },

    markAsReadOnly: function(taskDetails){
    	this.down('schedulePanel').markAsReadOnly(taskDetails);
    	this.down('paramsPanel').markAsReadOnly(taskDetails);
    	this.down('button[text="Submit"]').hide();
    	this.down('button[text="Reset"]').hide();
    	this.down('button[text="Cancel"]').setText('Close');
    }
    
});
