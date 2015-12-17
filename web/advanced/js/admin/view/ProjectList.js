/**
 * View for project manipulation. This view will have the following,
 * 1. List of projects and some properties of projects in a table
 * 2. Ability to add a new project 
 * 3. Ability to edit a project's attributes
 */
Ext.define('Admin.view.ProjectList', {
    extend : 'Ext.grid.Panel',
    xtype : 'projectList',
    alias : 'widget.projectList',
    store : 'Admin.store.ProjectStore',
    title : 'Projects',

    initComponent : function() {
        Ext.apply (this, {
            columns : [
                {header : 'Name', dataIndex : 'name', flex : 1},
                {header : 'Notes', dataIndex : 'notes', flex : 1, hidden : true},
                {header : 'Status', dataIndex : 'status', flex : 1},
                {header : 'Created By', dataIndex : 'createdBy', flex : 1, hidden : true},
                {header : 'Creation Date', dataIndex : 'creationDate', flex : 1, hidden : true},
                {header : 'Record Count', dataIndex : 'noOfRecords', flex : 1},
                {header : 'Space Usage (in GB)', dataIndex : 'spaceUsage', flex : 1, renderer : Ext.util.Format.numberRenderer("0.0")},
                {header : 'Storage Quota (in GB)', dataIndex : 'storageQuota', flex : 1, renderer : Ext.util.Format.numberRenderer("0.0")},
				{
					header : 'Archive/Restore', dataIndex : 'storageQuota', flex : 1, xtype : 'componentcolumn',
					renderer : function(value, metaData, record, rowIndex, colIndex, store, view) 
					{
						var iconValue = 'images/icons/download.png';
						var disabledValue = false;
						var tooltipValue = '';
						var urlValue = '';
						var locationValue = '';
						var _this = this.up('projectList');
						var textValue = '';
						if(_this.dirtyFlg == true)
							_this.dirtyFlg = true;
						else
							_this.dirtyFlg = false;
							
						if(record.data.status == 'Active')
						{
							iconValue = 'images/icons/leftarrow.png'
							tooltipValue = 'Archive Project';
							urlValue = '../project/archiveProject';
							textValue = 'Archive';
							disabled = false;
							_this.dirtyFlg = _this.dirtyFlg || false;
						}
						else if(record.data.status == 'Queued For Archiving')
						{
							iconValue = 'images/icons/stop.png'
							tooltipValue = 'Stop Project Archiving';
							urlValue = '../project/cancelArchiveProject';
							textValue = 'Stop Archiving';
							_this.dirtyFlg = _this.dirtyFlg || true;
							disabled = true;
						}
						else if(record.data.status == 'Archiving In Progress')
						{
							iconValue = 'images/icons/stop.png'
							tooltipValue = 'Stop Project Archiving';
							urlValue = '../project/cancelArchiveProject';
							textValue = 'Stop Archiving';
							disabled = true;
							_this.dirtyFlg = _this.dirtyFlg || true;
						}
						else if(record.data.status == 'Archived')
						{
							iconValue = 'images/icons/rightarrow.png'
							tooltipValue = 'Restore Project';
							urlValue = '../project/restoreProject';
							textValue = 'Restore';
							disabled = false;
							_this.dirtyFlg = _this.dirtyFlg || false;
						}
						else if(record.data.status == 'Restoration In Progress')
						{
							iconValue = 'images/icons/stop.png'
							tooltipValue = 'Restore Project';
							urlValue = '../project/cancelRestoreProject';
							textValue = 'Stop Restoration';
							disabled = true;
							_this.dirtyFlg = _this.dirtyFlg || true;
						}
						else if(record.data.status == 'Queued For Restoration')
						{
							iconValue = 'images/icons/stop.png'
							tooltipValue = 'Restore Project';
							urlValue = '../project/cancelRestoreProject';
							textValue = 'Stop Restoration';
							disabled = true;
							_this.dirtyFlg = _this.dirtyFlg || true;
						}
						else
						{
							iconValue = 'images/icons/terminated.png'
							tooltipValue = 'Project Unavailable';
							disabledValue = true;
							textValue = 'Unavailable';
						}
						
						return {
							xtype : 'panel',
							border : false,
							items : [
							         {
							         	xtype : 'button',
										tooltip : tooltipValue,
										disabled : disabledValue,
										icon : iconValue,
										text : textValue,
										handler : function() 
										{
											_this.fireEvent('onArchiveRestoreClick', record.data.name, locationValue, urlValue);
										}
							         }
									]
							
								};
					}
				}
            ],
            dockedItems : [{
                xtype : 'toolbar',
                items : [{
                    icon : 'images/icons/add.png',
                    text : 'Add',
                    tooltip : 'Add Project',
                    scope : this,
                    handler : this.onAddClick
                }, ' ' , '-', ' ', {
                    icon : 'images/icons/wrench.png',
                    text : 'Edit',
                    tooltip : 'Edit Project',
                    scope : this,
                    handler : this.onEditClick
                }, ' ' , '-', ' ', 
                /*{
                    icon : 'images/icons/database_go.png',
                    text : 'Add Unit',
                    tooltip : 'Add Unit',
                    scope : this,
                    handler : this.onUnitClick
                }, ' ' , '-', ' ',*/ 
                {
                	icon : 'images/icons/download.png',
                    text : 'Download As CSV',
                    tooltip : 'Download As CSV',
                    scope : this,
                    handler : this.onDownloadProjectList
                }, ' ', '-', ' ', {
                	xtype: 'textfield',
                	name: 'searchQuery',
                	listeners:{
	                	specialkey: function(field, e){
	                    // e.HOME, e.END, e.PAGE_UP, e.PAGE_DOWN,
	                    // e.TAB, e.ESC, arrow keys: e.LEFT, e.RIGHT, e.UP, e.DOWN
		                    if (e.getKey() == e.ENTER) {
		                        var form = field.up('projectList').onSearch();
		                    }
	                	}
                	}
                },{
                	text : 'Search',
                    scope : this,
                    handler : this.onSearch
                }]
            }]
        });
        
        var me = this;
		var config = {
			task : {
				run : function() {
					me.updateProjectList();
				},
				interval : 60000
			}
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		Ext.TaskManager.start(this.task);
		this.callParent();
		this.updateProjectList();
    },
    
    updateProjectList : function() {
    	console.log("updating project list");
    	console.log(this.dirtyFlg);
		if(this.dirtyFlg)
		{
			var store = this.store;
			store.load();
			this.dirtyFlg = false;
		}
	},

    /**
     * Handler called when add is clicked
     */
    onAddClick : function() {
        this.fireEvent('onAddProject'); 
    },
    
    /**
     * Handler called when search is clicked
     */
    onSearch : function() {
        this.fireEvent('searchProject'); 
    },
    
//    /**
//     * Handler called when add is clicked
//     */
//    onUnitClick : function() {
//    	var selected = this.getSelectionModel().getSelection();
//        if (selected && selected !== null && selected.length === 1) {
//            this.fireEvent('onAddUnit', selected[0].data.name);
//        } else {
//            Ext.Msg.alert("Warning", "Select project to edit from the table");
//        }
//    },
    
    /**
     * Handler called when add is clicked
     */
    onDownloadProjectList : function() {
        this.fireEvent('onDownloadProjectList');
    },

    /**
     * Handler called when edit is clicked
     */
    onEditClick : function() {
        var selected = this.getSelectionModel().getSelection();
        if (selected && selected !== null && selected.length === 1) {
            this.fireEvent('onEditProject', selected[0]);
        } else {
            Ext.Msg.alert("Warning", "Select project to edit from the table");
        }
    }
});
