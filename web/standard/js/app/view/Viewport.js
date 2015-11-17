Ext
        .define(
                'Manage.view.Viewport',
                {
                    extend: 'Ext.container.Viewport',
                    
                    requires: [
                            'Manage.view.Headers',
                            'Manage.view.settings.ChangePassword',
                            'Manage.view.settings.ViewTokens',
                            'Manage.view.settings.ProjectUsersList',
                            'Manage.view.settings.ProjectUsers',
                            'Manage.view.dialogs.ExportDialog',
                            'Manage.view.dialogs.WebClientDialog',
                            'Manage.view.Settings',
                            'Manage.view.dialogs.ShareDialog',
                            'Manage.view.ImageControls',
                            'Manage.view.ImageData', 'Manage.view.ImageView',
                            'Manage.view.Navigator',
                            'Manage.view.SummaryTable',
                            'Manage.view.Thumbnails', 'Ext.draw.Text',
                            'Ext.layout.container.Border', 'Ext.slider.*',
                            'Ext.toolbar.Spacer',
                            'Manage.view.settings.TokenList',
                            'Manage.view.workflows.Workflows',
                            'Manage.view.workflows.FavouriteWorkflows',
                            'Manage.view.workflows.SelectedWorkflows',
                            'Manage.view.workflows.AvailableWorkflows',
                            'Manage.view.workflows.WebApplications',
                            'Manage.view.params.ParamsDialog',
                            'Manage.view.settings.ProjectFieldChooser',
                            'Manage.view.settings.LegendsFieldChooser',
                            'Manage.view.FavouriteApplicationChooser',
                            'Manage.view.admin.AdminWindow',
                            'Manage.view.VideoSubmitDialog',
                            'Manage.view.ProfileChooser',
                            'Manage.view.SelectionPanel',
                            'Manage.view.OverlayLocation',
                            'Manage.view.admin.AddProfile',
                            'Manage.view.Imports',
                          //added
                            'Manage.model.Attachment',
                            'Manage.model.Bookmark',
                            'Manage.model.Channel',
                            'Manage.model.ChannelContrast',
                     		'Manage.model.Client',
                     		'Manage.model.Comment',
                     		'Manage.model.Download',
                     		'Manage.model.ExportFormat',
                     		'Manage.model.Field',
                     		'Manage.model.History',
                     		'Manage.model.HistoryType',
                     		'Manage.model.ImageMetaData',
                     		'Manage.model.LUT',
                     		'Manage.model.MemberProject',
                     		'Manage.model.Microscope',
                     		'Manage.model.Overlay',
                     	     'Manage.model.Project',
                     		'Manage.model.ProjectName',
                     		'Manage.model.ProjectUser',
                     		'Manage.model.ProfileType',
                     		'Manage.model.Profile',
                     		'Manage.model.ProfileLengthUnit',
                     		'Manage.model.ProfileTimeUnit',
                     		'Manage.model.RecordMetaData',
                     		'Manage.model.RecordThumbnail',
                     		'Manage.model.SearchResult',
                     		'Manage.model.Service',
                     		'Manage.model.Task',
                     		'Manage.model.Thumbnail',
                     		'Manage.model.Token',
                     		'Manage.model.UserAnnotation',
                     		'Manage.model.Workflow',
                     		'Manage.model.WorkflowCategory',
                     		'Manage.model.WebApplication',
                     		'Manage.model.WebApplicationTree',
                     		'Manage.model.Suggest',
                     		'Manage.model.OverlayLocation',
                     		'Manage.store.AllClients',                                  		
                     		'Manage.store.Bookmarks',
                     		'Manage.store.BookmarksInSelection',
                     		'Manage.store.BookmarksThumbnails',
                     		'Manage.store.ChannelContrasts',
                     		'Manage.store.Channels',
                     		'Manage.store.Comments',
                     		'Manage.store.Downloads',
                     		'Manage.store.DownloadsThumbnails',
                     		'Manage.store.ExportFormats',
                     		'Manage.store.Historys',
                     		'Manage.store.HistoryTypes',
                     		'Manage.store.ImageMetaDatas',
                     		'Manage.store.LUTs',
                     		'Manage.store.LegendAvailableFieldStore',
                     		'Manage.store.LegendSelectedFieldStore',
                     		'Manage.store.LegendFieldStore',
                     		'Manage.store.MemberProjects',
                     		'Manage.store.Memberships',
                     		'Manage.store.MicroscopeStore',
                     		'Manage.store.NavigatorAvailableFields',
                     		'Manage.store.NavigatorFields',
                     		'Manage.store.Navigators',
                     		'Manage.store.NavigatorSelectedFields',
                     		'Manage.store.NavigatorThumbnails',
                     		'Manage.store.Overlays',
                     		'Manage.store.PriorityStore',
                     		'Manage.store.ProjectAvailableFields',
                     		'Manage.store.ProjectFieldChoosers',
                     		'Manage.store.ProjectFields',
                     		'Manage.store.ProjectNames',
                     		'Manage.store.Projects',
                     		'Manage.store.ProjectSelectedFields',
                     		'Manage.store.ProjectUsers',
                     		'Manage.store.ProfileStore',
                     		'Manage.store.ProfileTypeStore',
                     		'Manage.store.ProfileTimeUnitStore',
                     		'Manage.store.ProfileLengthUnitStore',
                     		'Manage.store.RecentProjects',
                     		'Manage.store.RecordMetaDatas',
                     		'Manage.store.RecordThumbnails',
                     		'Manage.store.SearchResults',
                     		'Manage.store.SearchThumbnails',
                     		'Manage.store.SelectionRecords',
                     		'Manage.store.Services',
                     		'Manage.store.SystemAttachments',
                     		'Manage.store.TaskInspectors',
                     		'Manage.store.TaskMonitors',
                     		'Manage.store.Tasks',
                     		'Manage.store.TaskStates',
                     		'Manage.store.TaskThumbnails',
                     		'Manage.store.Tokens',
                     		'Manage.store.TokenUsers',
                     		'Manage.store.UserAnnotations',
                     		'Manage.store.UserAttachments',
                     		'Manage.store.UserFields',
                     		'Manage.store.UserFieldTypes',
                     		'Manage.store.WorkflowClients',
                     		'Manage.store.Workflows',
                     		'Manage.store.WorkflowStore',
                     		'Manage.store.WorkflowTree',
                     		'Manage.store.FavouriteWorkflowStore',
                     		'Manage.store.FavouriteWorkflowTree',
                     		'Manage.store.AvailableWorkflowStore',
                     		'Manage.store.AvailableWorkflowTree',
                     		'Manage.store.SelectedWorkflowStore',
                     		'Manage.store.SelectedWorkflowTree',
                     		'Manage.store.admin.Microscopes',
                     		'Manage.store.Suggest',
                     		'Manage.store.TagSearch',                                		
                     		'Manage.store.WebApplications',
                     		'Manage.store.WebApplicationsTree',
                     		'Manage.store.OverlayLocation',
                     		'Manage.controller.AuthTokens',
                     		'Manage.controller.Bookmarks',
                     		'Manage.controller.Downloads',
                     		'Manage.controller.Headers',
                     		'Manage.controller.ImageControls',
                     		'Manage.controller.ImageView',
                     		'Manage.controller.Movie',
                     		'Manage.controller.Navigator',
                     		'Manage.controller.ProjectSettings',
                     		'Manage.controller.RecordController',
                     		'Manage.controller.Search',
                     		'Manage.controller.Selections',
                     		'Manage.controller.Tasks',
                     		'Manage.controller.Thumbnails',
                     		'Manage.controller.WorkflowController',
                     		'Manage.controller.Zoom',
                     		'Manage.controller.Overlays',
                     		'Manage.controller.admin.Logs',
                     		'Manage.controller.admin.Memberships',
                     		'Manage.controller.admin.MicroscopeProfiles',
                     		'Manage.controller.admin.Microscopes',
                     		'Manage.controller.admin.Projects',
                     		'Manage.controller.admin.Units',
                     		'Manage.controller.admin.UserAnnotations',
                     		'Manage.controller.admin.Users',
                     		'Manage.controller.admin.Utils',
                     		'Manage.controller.admin.Usage'                                		
                    ],
                    
                    layout: 'border',
                    
                    items: [
                            {
                                xtype: 'headers',
                                region: 'north'
                            },
                            {
                                xtype: 'panel',
                                
                                region: 'center',
                                layout: 'border',
                                split: true,
                                
                                items: [
                                        {
                                            xtype: 'imagecontrols',
                                            
                                            region: 'west',
                                            title: 'Image Controls',
                                            
                                            collapsible: true,
                                            header: false,
                                            split: true,
                                            width: 300,
                                            minWidth: 300,
                                            maxWidth: 300
                                        }, {
                                            xtype: 'imageview',
                                            
                                            header: false,
                                            region: 'center',
                                            title: 'Image'
                                        }, {
                                            xtype: 'imagedata',
                                            
                                            header: false,
                                            region: 'east',
                                            split: true,
                                            title: 'Image Data',
                                            
                                            collapsible: true,
                                            width: 300
                                        }
                                ]
                            
                            },
                            {
                                xtype: 'panel',
                                
                                region: 'west',
                                
                                header: false,
                                split: true,
                                title: 'Thumbnails',
                                width: 265,
                                minWidth: 265,
                                
                                layout: 'fit',
                                collapsible: true,
                                
                                tbar: {
                                    style: {
                                        backgroundColor: '#444',
                                        paddingRight: '6px'
                                    },
                                    items: [
                                            {
                                                xtype: 'textfield',
                                                // height : 50,
                                                width: 200,
                                                id: 'projectDisplay',
                                                
                                                // style : 'color:white;
                                                // padding-left:3em;
                                                // padding-top: 0.5em;
                                                // padding-bottom: 0.5em;
                                                // border-radius:12px; border:
                                                // 10px;'
                                                style: {
	                                                color: 'white',
                                                // marginRight : '50px'
                                                }
                                            },
                                            '->',
                                            {
                                                xtype: 'button',
                                                iconCls: "refresh",
                                                tooltip: 'Refresh',
                                                id: 'refreshButton',
                                                style: {
                                                // float : 'right'
                                                }
                                            }
                                    ]
                                },
                                // combo box to sort thumbnails according to
                                // different fields
                                bbar: {
                                    style: {
                                        backgroundColor: '#444',
                                        paddingRight: '6px'
                                    },
                                    
                                    items: [
                                            {
                                                xtype: 'combobox',
                                                id: 'sortThumbnailCombo',
                                                
                                                fieldLabel: 'Sort by',
                                                
                                                labelWidth: 50,
                                                displayField: 'name',
                                                valueField: 'name',
                                                
                                                autoSelect: true,
                                                queryMode: 'local',
                                                forceSelection: true,
                                                
                                                width: 215,
                                                
                                                // style : {
                                                // width : '80%',
                                                // },
                                                
                                                store: 'ProjectFields',
                                                
                                                labelStyle: 'color: white'
                                            },
                                            // '->',
                                            {
                                                xtype: 'button',
                                                id: 'sortThumbnailsButton',
                                                
                                                iconCls: 'uparrow',
                                                enableToggle: true,
                                                // next sort will be in this
                                                // order
                                                tooltip: 'Sort Descending',
                                                
                                                // current sorting order
                                                order: 'ASC',
                                                
                                                toggleHandler: function (
                                                        button, state) {
	                                                if (this.order === 'ASC') {
		                                                this.order = 'DESC';
		                                                this
		                                                        .setTooltip('Sort Ascending');
		                                                this
		                                                        .setIconCls('downarrow');
	                                                } else {
		                                                this.order = 'ASC';
		                                                this
		                                                        .setTooltip('Sort Descending');
		                                                this
		                                                        .setIconCls('uparrow');
	                                                }
	                                                this.fireEvent(
	                                                        'changeSort',
	                                                        this.order);
                                                },
                                                
                                                style: {
                                                    "background-image": "none",
                                                    "border": "none",
                                                    "background-color": "#444"
                                                }
                                            }
                                    ]
                                },
                                
                                items: [
	                                {
	                                    xtype: 'panel',
	                                    
	                                    layout: 'fit',
	                                    
	                                    header: false,
	                                    region: 'center',
	                                    title: 'Thumbnails',
	                                    
	                                    autoScroll: true,
	                                    split: true,
	                                    
	                                    items: [
		                                    {
		                                        xtype: 'thumbnails',
		                                        id: 'imageThumbnails',	
		                                        
		                                        // this is a required field
		                                        store: 'RecordThumbnails',
		                                        
		                                        style: {
			                                        padding: '2px'
		                                        },
		                                        
		                                        autoScroll: true
		                                    }
	                                    ],
	                                    
	                                    bbar: {
	                                        style: {
	                                            paddingRight: '8px',
	                                            borderColor: 'black'
	                                        },
	                                        
	                                        items: [
		                                        {
		                                            xtype: 'slider',
		                                            id: 'thumbslider',
		                                            
		                                            minValue: 1,
		                                            maxValue: 6,
		                                            value: 2,
		                                            
		                                            hideLabel: true,
		                                            style: {
			                                            width: '100%'
		                                            }
		                                        }
	                                        ]
	                                    }
	                                }
                                ]
                            }
                    ]
                });
