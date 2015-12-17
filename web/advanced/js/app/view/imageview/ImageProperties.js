/**
 * ImageControls : tab panel on the right hand side of imageview 
 */
Ext.define('Manage.view.imageview.ImageProperties', {
	extend:'Ext.tab.Panel',
	alias:'widget.imageProperties',
	requires: [
		'Manage.view.imageview.ImageControls',
		'Manage.view.imageview.ImageMetaData',
		'Manage.view.imageview.RecordMetaData',
		'Manage.view.imageview.Attachments',
		'Manage.view.imageview.Comments',
		'Manage.view.imageview.History',
		'Manage.view.imageview.Legends',
		'Manage.view.imageview.ImageControls',
		'Manage.view.imageview.ImageToolbar'
	],
	
	initComponent:function() {
		var config={
			collapsible:'true',
			tabPosition: 'bottom',
			split: 'true',
			width:300,
			layoutOnTabChange : true,
			defaults: {autoScroll: true},
			listeners: {
                tabchange: function(tabPanel, newTab, oldTab, index){
                    if(newTab.xtype === 'history')
                    {
                    	newTab.updateHistory();
                    }
                }
            },
			items : [ {
				xtype : 'imagecontrols',
				tabConfig : {
					title : ' ',
					iconCls : 'imagecontrols',
					tooltip : 'Image Controls'
				}
			}, {
				xtype : 'comments',
				tabConfig : {
					title : ' ',
					iconCls : 'comments',
					tooltip : 'Comments'
				}
			}, {
				xtype : 'history',
				tabConfig : {
					title : ' ',
					iconCls : 'history',
					tooltip : 'History'
				}
			}, {
				xtype : 'userAnnotations',
				tabConfig : {
					title : ' ',
					iconCls : 'userannotations',
					tooltip : 'User Annnotations'
				}
			}, 
			{
				xtype : 'legends',
				tabConfig : {
					title : ' ',
					iconCls : 'panzoom-on',
					tooltip : 'User Legends'
				}
			}, 
			{
				xtype : 'attachments',
				tabConfig : {
					title : ' ',
					iconCls : 'attachments',
					tooltip : 'Attachments'
				}
			}, {
				xtype : 'recordmetadata',
				tabConfig : {
					title : ' ',
					iconCls : 'recordmetadata',
					tooltip : 'Record Metadata'
				}
			}, {
				xtype : 'imagemetadata',
				tabConfig : {
					title : ' ',
					iconCls : 'imagemetadata',
					tooltip : 'Image MetaData'
				}
			} ]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		this.callParent();
	}
});