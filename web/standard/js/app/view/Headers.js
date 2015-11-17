Ext.define('Manage.view.Headers', {
	extend: 'Ext.panel.Panel',
	xtype: 'headers',
	alias: 'widget.headers',

	/**
	 * All the windows that appear on button click
	 */
	adminWindow: '',
	bookmarksWindow: '',
	downloadWindow: '',
	navigatorWindow: '',
	projectWindow: '',
	searchWindow: '',
	selectionWindow: '',
	taskWindow: '',
	settingsWindow: '',
	importWindow: '',
	tileViewerWindow: '',

	/**
	 * Set up all the windows
	 */
	initComponent: function() {
		this.bookmarksWindow = Ext.create('Manage.view.BookmarksWindow');
		this.downloadWindow = Ext.create('Manage.view.DownloadWindow');
		this.navigatorWindow = Ext.create('Manage.view.NavigatorWindow');
		this.projectWindow = Ext.create('Manage.view.ProjectWindow');
		this.searchWindow = Ext.create('Manage.view.SearchWindow');
		this.selectionWindow = Ext.create('Manage.view.SelectionWindow');
		this.taskWindow = Ext.create('Manage.view.TaskWindow');
		this.settingsWindow = Ext.create('Manage.view.settings.SettingsWindow');
		this.importWindow = Ext.create('Manage.view.ImportWindow');
		this.tileViewerWindow = Ext.create('Manage.view.TileViewerWindow');
		
		if(showAdminLink) {
			this.adminWindow = Ext.create('Manage.view.admin.AdminWindow');
		}
		this.callParent();
	},
	
	tbar: {
		style: 'background-color:#444',
		ui: 'footer',

		items: [
			{ 
				xtype: 'image',
				width: 143,
				height: 24,
				src: 'images/icons/avadis_imanage.png'
			}, ' ', ' ', ' ', ' ', //space for logical separation
			{
				xtype: 'label',
				id: 'user',
				height: 20,
				width: 300,
				style: 'color:white; padding-left:3em; padding-top: 0.5em; padding-bottom: 0.5em; border-radius:12px; border: 10px;'
			},' ', ' ', ' ', ' ', //space for logical separation
			'->',  
			{ 
				xtype : 'button',
				hidden : !showAcqLink,
				text : 'Launch Acquisition',
				handler : function() {
						window.location.href='../project/launchAcqClient';
				}
			}, ' ', ' ', ' ', ' ', //space for logical separation
			{
				xtype: 'button',
				id: 'headerAdminButton',
				hidden: !showAdminLink,
				text: 'Administrator',
				handler: function() {
					var headers = this.up().up();
					headers.fireEvent("adminClick");
				}
			},
			{
				xtype: 'button',
				id: 'headerSettingsButton',
				text: 'Settings',
				handler : function() {
					var headers = this.up().up();
					headers.fireEvent("onSettingsClick");
				}
			}, { 
				xtype : 'button',
				text : 'Help',
				handler : function() {
					window.open('../help.html');
				}
			}, { 
				xtype: 'button', 
				text : 'Logout',
				handler : function() {
					window.location.href='../auth/logout?success=standard';
				}
			}
		]
	},

	bbar: {
		style: 'background-color:#444',
		ui: 'footer',

		items: [
			{ xtype: 'button', text: 'Projects' , id: 'changeButton', tooltip:'Change Project'},
			{ xtype: 'button', text: 'Navigator' , id: 'navigatorButton', tooltip:'Open Navigator'},
			'->',
			{ xtype: 'button', text:'Reset Zoom', id:'buttonReset', tooltip: 'Reset Zoom'},						
			{ xtype: 'button', text: 'Set Thumbnail', id: 'setThumbnailButton', tooltip: 'Set current image as thumbnail for current record'},
			{ 
				xtype: 'button', 
				tooltip: 'Download current image',
				text: 'Screenshot',
				id:'screenshotButton',
				href : '#'
			},
			' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', //space for logical separation
			{
				xtype : 'splitbutton',
				id : 'headerResultButton',
				text : 'Results',
				menu: {
				xtype: 'menu',
					items: [
					    {
							text : 'Bookmarks',
							id : 'bookmarksButton',
							tooltip : 'List all the bookmarked records'
						},
		 				{
							text : 'Tasks',
							id : 'taskButton',
							tooltip : 'List/Search task set to execute on publisher'
						}, {
							text : 'Links',
							id : 'downloadsButton',
							tooltip : 'Download records'
						}, {
							text : 'Imports',
							id : 'importButton',
							tooltip : 'Import status'
						}, {
							text : 'Tile Viewer Tasks',
							id : 'tileViewerTaskButton',
							tooltip : 'List all the Tile Viewer Tasks'
						}
					]
				}
			},
			{
				xtype : 'splitbutton',
				id : 'headerActionButton',
				text : 'Actions',
				menu: {
					xtype: 'menu',
			        items: [
			            // these will render as dropdown menu items when the arrow is clicked:
			            	{
								text: 'Add Bookmark',
								tooltip : 'Add bookmark',
								id : 'addBookmarkButton',
								handler : function()
								{
									var headers = this.up().up();
									headers.fireEvent("addbookmarkclicked");
								}
							},
				            {
								text : 'Run Task',
								tooltip : 'Execute task published by some publisher',
								id : 'headerWorkflowButton',
								handler : function()
								{
									var headers = this.up().up();
									headers.fireEvent("workflowButtonClicked");
								}
							},
							{
								text : 'Invoke URL',
								tooltip : 'Invoke web url',
								id : 'headerInvokeUrlButton',
								handler : function()
								{
									var headers = this.up().up();
									headers.fireEvent("invokeUrlButtonClicked");
								}
							},
				            {
								text : 'Apply Profile',
								tooltip : 'Apply microscope profiles to records',
								hidden : !showAcqLink,
								id : 'headerProfileButton',
								handler : function()
								{
									var headers = this.up().up();
									headers.fireEvent("profileButtonClicked");
								}
							},
							{ 
								text: 'Annotate',
								tooltip: 'Add user annotations to records',
								id : 'headerAnnotateButton',
								handler : function() {
				                    var headers = this.up().up();
				                    headers.fireEvent("annotateButtonClicked");
				                }
							}, 
							{
								text : 'Download',
								tooltip: 'Download selected records',
								id : 'headerDownloadButton',
								handler : function() {
				                    var headers = this.up().up();
				                    headers.fireEvent("downloadButtonClicked");
				                }
							},
							{ 
								text: 'Share',
								tooltip: 'Share selected records with other project',
								hidden : !showAcqLink,
								id : 'headerShareButton',
								handler : function() {
				                    var headers = this.up().up();
				                    headers.fireEvent("shareButtonClicked");
				                }
							},
							{ 
								text: 'Transfer',
								tooltip: 'Transfer selected records to other project',
								id : 'headerTransferButton',
								handler : function() {
				                    var headers = this.up().up();
				                    headers.fireEvent("transferButtonClicked");
				                }
							},
							{ 
								id : 'headerDeleteButton',
								text: 'Delete',
								tooltip: 'Delete selected records from current project',
								handler : function() {
				                    var headers = this.up().up();
				                    headers.fireEvent("deleteButtonClicked");
				                }
							}
			        ]
			    }	
			},
			{				
	            xtype : 'combobox',
	            queryMode : 'local',
            	name: 'searchQuery',
            	id:'searchTextField',
				emptyText : 'Enter Query',
				enableKeyEvents: true,
				displayField : 'suggestions',
	            valueField : 'suggestions',
	            allowBlank : false,
	            hideTrigger: true,
	            typeAhead: true,
	            store: {type:'suggest'},
				listeners: {
				      keypress : function(textfield,eventObject){
				          if (eventObject.getCharCode() == Ext.EventObject.ENTER) {
				        	  
				               //enter is pressed call the search handler function here.
				        	  var headers=this.up().up();
				        	  headers.fireEvent("searchButtonClicked");
				          }
				      },
				      change : function(field, newValue, oldValue, opts){
			        	  var headers=this.up().up();
			        	  headers.fireEvent("suggestResults",newValue);
				      }
				  }
            },
 			{
				xtype : 'button',
				text : 'Search',
				name :'Search',
				id : 'searchButton',
				tooltip : 'Search'
			}
		]
	}
});
	
