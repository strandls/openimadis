/**
 * Panel to display inside grid cell in task monitor or task inspector 
 */

Ext.define('Manage.view.TaskMonitorCell', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.taskMonitorCell',
	
	initComponent : function(){
		var record=this.record;
		var view=this.view;
		
		var me=this;
		var closeScope={
			'record' : record,
			'view' : view
		};
		
		var close={
            xtype : 'button',
	    width: 25,
            tooltip : 'Clear',
            icon : 'images/icons/close_dimmed.png',
			'record' : record,
			'view' : view,	
            handler : function() {
				this.view.panel.fireEvent('clearCell',this.record);
            }
        };
		
        // right now not used, keeping it if need
        // FIXME remove this
		var parameters={
            xtype : 'button',
	    width: 25,
            tooltip : 'Show parameters',
            icon : 'images/icons/parameters.gif',
			'record' : record,
			'view' : view,	
            handler : function() {
				this.view.panel.fireEvent('showParams',this.record);
            }
	    };
		
		var buttons=[/*parameters,*/close];
		
		var state=record.get('state');
		
		if( record.data.terminatePermission == true &&
			( state === "SCHEDULED" 
				|| state === "WAITING" 
				|| state === "PAUSED"
				|| state === "ALLOCATED"
				|| state === "EXECUTING"))	
		{
			var terminate={
		            xtype : 'button',
			    width: 25,
		            tooltip : 'Terminate',
		            icon : 'images/icons/stop.png',
					'record' : record,
					'view' : view,	
		            handler : function() {
						this.view.panel.fireEvent('terminate',this.record);
		            }
			};
			buttons.unshift(terminate);
		}
		
		if(state === "ERROR" || state === "SUCCESS")	
		{
			var showLog={
		            xtype : 'button',
		            tooltip : 'Download Log',
		            icon : 'images/icons/download.png',
					'record' : record,
					'view' : view,	
		            handler : function() {
						this.view.panel.fireEvent('downloadLogs',this.record);
		            }
			};
			buttons.unshift(showLog);
		}
		
		var buttonsPanel={
			xtype: 'panel',
			border:false,
			width : buttons.length*22,
			height : 35,
			margin:'0 0 1 0',
			items:buttons,
			region:'east'
		};
		

		var iconMap={
				'SCHEDULED':'images/icons/scheduled.png',
				'WAITING':'images/icons/scheduled.png',
				'ALLOCATED':'images/icons/allocated.gif',
				'EXECUTING':'images/icons/executing.png',
				'TERMINATING':'images/icons/cancelled.png',
				
				'SUCCESS':'images/icons/success.png',
				'ERROR':'images/icons/warning.png',
				'TERMINATED':'images/icons/cancelled.png',
				'DELETED':'images/icons/dustbin.gif'
			};
			
		var statusIcon={
			xtype:'image',
			src : iconMap[state],
			width : 16,
			height : 16,
			region:'west',
			margin: '0 5 0 0'
		};
		
		var status={
			xtype : 'label',
			style:{
				"font-size":"11px"
			},
			margin:'0 0 0 0',
			//region:'center',
			height:16,
			width:80,
			text : state
		};
		
		var statusPanelItems=[statusIcon, status];
		var percentageComplete=record.get('progress');
		
		if(state === 'EXECUTING'){
			var progress={	
				xtype: 'progressbar',
				text: percentageComplete+'%',
				height:16,
				width:64,
				value:percentageComplete/100.0,
				componentCls:'task-progressbar',
				listeners:{
					'refresh':function(){
						//HACK:to bring text to the center
						//x-progress-text div has initial width of zero since it is not 
						//visible when loaded.
						//Following line will force to make width to 64px.
						this.setWidth(64);
					}
				}
			};
			statusPanelItems.push(progress);
		}
		
		var statusPanel={
			xtype : 'panel',
			region:'center',
			layout:'hbox',
			height:16,
			border:false,
			items : statusPanelItems
		};
		
		var appHeader={
			xtype:'label',
			region:'center',
			height:18,
			style:{
				"font-size":"11px",
				"font-weight":"bold"
			},
			text: record.data.appName +' ' +record.data.appVersion
		};
		
		
		
//		var headerPanel={
//			xtype : 'panel',
//			region: 'center',
//			border:false,
//			bodyStyle: {
//			    background: '#ffffff'
//			},
//			layout: 'border',
//			height: 32,
//			items : [
//			  appHeader,       
//			  statusPanel
//			]
//		};
		
		var date={
			xtype: 'label',
			style:{
				"font-size":"11px"
			},
			text:'Scheduled on: ' + record.data.scheduledTime,
			anchor: '100% 20%'
		};
		
//		var infoHtml=''<>
//		var info1={
//			tag:'div',
//			border:false,
//			region: 'center',
//			html:'<p>info</p>'
//		};
//		
		
		
		var headerWithButtons={
			xtype:'panel',
			region:'north',
			layout:'border',
			border:false,
			height:24,
			bodyStyle: {
			    background: '#FFFFFF'
			},
			items:[
			  appHeader,
			  buttonsPanel
			  //statusPanel
			]	
		};
		

		var infoPanel={
			xtype:'panel',
			region:'center',
			layout:'border',
			border:false,
			height:36,
			items:[
			  headerWithButtons,
			  statusPanel
			]
		};
		
		var process={
	            xtype : 'image',
	            src : 'images/icons/process.png',
	            width : 24,
	            height : 24,
	            margin : '5 5 5 5' 
		};
		
		var processPanel={
			xtype: 'panel',
            region:'west',
            width:36,
            border:false,
			items: [process]
		};
		
		var topPanel={
			xtype : 'panel',
			height:42,
			border:false,
			layout:'border',
			items:[
			  processPanel,
			  infoPanel
			],
			anchor: '100% 80%'
		};
		
		
		var config={
			padding: 5,
			border: false,	
			layout: 'anchor',
			items:[topPanel,date]
		};
		
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		this.callParent();
	}
});
