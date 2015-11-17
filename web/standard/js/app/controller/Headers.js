Ext.define('Manage.controller.Headers', {
	extend: 'Ext.app.Controller',

	controllers: [
		'Navigator', 'Thumbnails'
	],

	stores: [
		'NavigatorThumbnails', 'RecentProjects', 'Projects', 'Downloads','AllClients',
		'NavigatorFields','ProjectNames','Imports', 'TileViewerStore'
	],

	requires: [
		'Manage.view.Navigator', 'Manage.view.NavigatorWindow', 'Manage.view.ProjectListing'
	],

	refs: [{
		ref: 'navigatorButton',
		selector: '#navigatorButton'
	}, {
		ref: 'searchButton',
		selector: '#searchButton'
	}, {
		ref: 'headers',
		selector: 'headers'
	}, {
		ref: 'deleteButton',
		selector: '#headerDeleteButton'
	}, {
		ref: 'transferButton',
		selector: '#headerTransferButton'
	}, {
		ref: 'downloadButton',
		selector: '#headerDownloadButton'
	}, {
		ref: 'annotateButton',
		selector: '#headerAnnotateButton'
	}, {
		ref: 'shareButton',
		selector: '#headerShareButton'
	},{
		ref: 'settingsButton',
		selector: '#headerSettingsButton'
	},{
		ref: 'profileButton',
		selector: '#headerProfileButton'
	}, {
		ref: 'linkButton',
		selector: '#downloadsButton'
	}, {
		ref: 'taskButton',
		selector: '#taskButton'
	}, {
		ref: 'changeButton',
		selector: '#changeButton'
	}, {
		ref: 'adminButton',
		selector: '#headerAdminButton'
	}, {
		ref: 'ownTaskMonitor',
		selector: 'ownTaskMonitor'
	}, {
		ref: 'taskInspector',
		selector: 'taskInspector'
	}, {
		ref: 'bookmarksButton',
		selector: '#bookmarksButton'
	}, {
		ref: 'settingsPanel',
		selector: 'settings'
	},{
		ref: 'legendYLocation',
		selector: 'settings #yLocationRadio'
	},{
		ref: 'legendXLocation',
		selector: 'settings #xLocationRadio'
	},{
		ref: 'searchTextField',
		selector: '#searchTextField'
	},{
		ref : 'searchTerm',
		selector : '#searchterm'
	}, {
		ref : 'searchSubmitButton',
		selector : '#searchSubmitButton'
	}, {
		ref : 'user',
		selector : '#user'
	}, {
		ref : 'projectfieldchooser',
		selector : '#projectfieldchooser'
	}, {
		ref : 'importButton',
		selector : '#importButton'
	},{
		ref : 'completedImportsPanel',
		selector : '#completedImportsPanel'
	},{
		ref : 'ongoingImportsPanel',
		selector : '#ongoingImportsPanel'
	},{
		ref : 'tileViewerTaskButton',
		selector : '#tileViewerTaskButton'
	},{
		ref : 'tileViewerWindow',
		selector : 'tileViewerWindow'
	}
	],
	
	////////////////////////////////////////////
	///////// ACTIVE PROJECT NAME    ///////////
	////////////////////////////////////////////
	projectName: '',

	init: function() {
		this.control({ 
			'#navigatorButton': {
				click: this.onNavClick
			},
			'headers' : {
				onSettingsClick : this.onSettingsClick,
				adminClick: this.onAdminClick,
				searchButtonClicked: this.onSearchClick
			},
			'#searchButton': {
				click: this.onSearchClick
			},
			'#changeButton': {
				click: this.onChangeProjectClick
			},
			'#downloadButton': {
				click: this.onDownloadClick
			}, 
			'#downloadsButton': {
				click: this.onLinkClick
			}, 
			'#taskButton': {
				click: this.onTaskClick
			},
			'#bookmarksButton': {
				click: this.onBookmarksClick
			},
			'navigatorwindow': {
				beforeclose: this.onNavClick
			}, 
			'searchwindow' : {
				beforeclose: this.onSearchClick
			},
			'taskwindow': {
				beforeclose: this.onTaskClick
			},
			'downloadwindow': {
				beforeclose: this.onLinkClick,
				show: this.refreshDownloads
			},
			'projectwindow': {
				beforeclose: this.onChangeProjectClick
			},
			'adminwindow': {
				beforeclose: this.onAdminClick
			},
			'settingswindow': {
				beforeclose: this.onSettingsClick
			},
			'bookmarkswindow': {
				beforeclose: this.onBookmarksClick
			},
			'#importButton':{
				click: this.onImportClick
			},
			'#tileViewerTaskButton':{
				click: this.onTileViewerTaskClick
			},
			'tileViewerWindow':{
				refreshList : this.onRefreshTilingLists
			}
		});
		
		this.application.on({
			changeProject: this.onChangeProject,
			scope: this
		});
	},
	
	onRefreshTilingLists: function(){
		//load store for tile viewer tasks
		var tileviewerstore = this.getTileViewerStoreStore();
		tileviewerstore.load();
	},
	
	onTileViewerTaskClick: function(){
		var tileViewerTaskButton = this.getTileViewerTaskButton();
		
		//load store for tile viewer tasks
		var tileviewerstore = this.getTileViewerStoreStore();
		tileviewerstore.load();
		
		var headers = this.getHeaders();
		var win = headers.tileViewerWindow;
		var timer;
		if(win.isVisible()) {
			clearInterval(timer);
			win.hide(tileViewerTaskButton);
		} else {
			me = this;
			timer = setInterval(function(){
				var tileviewerstore = me.getTileViewerStoreStore();
				tileviewerstore.load();
	        }, 2000)
			win.show(tileViewerTaskButton);
		}
	},
	
	onChangeProject: function(projectName)
	{
		this.projectName = projectName;
		
		this.configureHeaderButtons();
	},
	
	configureHeaderButtons: function()
	{
		var projectName = this.projectName;
		
		var _this = this;
		var role;
		Ext.Ajax.request({
            method : 'GET',
            url : '../admin/getUserPermission',
            params : {
                'projectName' : projectName
            },
            success : function (result, response) {
            	var resp = Ext.decode(result.responseText)[0].value;            	            	
            	
            	_this.getProfileButton().show();
        		_this.getTransferButton().show();
        		_this.getDownloadButton().show();
        		_this.getAnnotateButton().show();
        		_this.getDeleteButton().show();
        		
            	if(resp > 2)
            	{
            		console.log('hide delete button');
            		_this.getDeleteButton().hide();
            		_this.getTransferButton().hide();
            	}
            	if(resp > 4)
            	{
            		// hide annotate
            		console.log('hide annotate');
            		_this.getAnnotateButton().hide();
            		_this.getProfileButton().hide();
            	}
            	if(resp > 5)
            	{
            		// hide export
            		_this.getDownloadButton().hide();
            	}
            	
        		Ext.Ajax.request({
                    method : 'GET',
                    url : '../admin/getCurrentUser',
                    success : function (result, response) {
                    	console.log(Ext.decode(result.responseText)[0].name);
                    	text=Ext.decode(result.responseText)[0].name;
                    	_this.getUser().setText(text);
                    }
        		});
            },
            failure : function (result, response) {}
        });		
		
		// legend settings
		Ext.Ajax.request({
            method : 'GET',
            url : '../annotation/getLegendLocation',
            params : {
                'projectName' : projectName
            },
            success : function (result, response) {
            	var location = Ext.decode(result.responseText);
            	console.log(location.ylocation.toLowerCase());
            	
            	_this.getLegendYLocation().setValue({tb:location.ylocation.toLowerCase()});
            	_this.getLegendXLocation().setValue({lr:location.xlocation.toLowerCase()});
            }
		});
	},
	
	/**
	 * toggles between showing and hiding the administrator window.
	 * Is called whenever the administrator window needs to be shown/hidden
	 */
	onAdminClick: function() {
		var adminButton = this.getAdminButton();
		var headers = this.getHeaders();
		var win = headers.adminWindow;
		if(win.isVisible()) {
			win.hide(adminButton);
		} else {
			win.show(adminButton);
		}
	},
	/**
	 * toggles between showing and hiding the settings window.
	 * Is called whenever the settings window needs to be shown/hidden
	 */
	onSettingsClick: function() {
		showConsoleLog('Headers', 'onSettingsClick', '');

		var activeProject = this.getNavigatorController().getActiveProject();
		console.log(activeProject);
		
		var settingsPanel = this.getSettingsPanel();
		settingsPanel.projectName = activeProject;
		
		this.getProjectfieldchooser().setText("Project - "+activeProject);
		
		//load store for all clients
		var allclientstore=this.getAllClientsStore();
		allclientstore.load();
		
		var projectstore=this.getProjectNamesStore();
		projectstore.reload();
		
		var settingsButton = this.getSettingsButton();
		var headers = this.getHeaders();
		var win = headers.settingsWindow; 
		if(win.isVisible()) {
			win.hide(settingsButton);
		} else {
			win.show(settingsButton);
		}
	},
	
	/**
	 * toggles between showing and hiding the navigator window.
	 * Is called whenever the navigator window needs to be shown/hidden
	 */
	onNavClick: function() {
		var navigatorButton = this.getNavigatorButton();
		var headers = this.getHeaders();	
		
		var navstore=this.getNavigatorFieldsStore();
		navstore.load({params: {projectName: this.projectName}});
		
		var win = headers.navigatorWindow;
		if(win.isVisible()) {
			win.hide(navigatorButton);
		} else {
			win.show(navigatorButton);
		}
	},


	/**
	 * toggles between showing and hiding the search  window.
	 * Is called whenever the search  window needs to be shown/hidden
	 */
	onSearchClick: function() {
		var searchButton = this.getSearchButton();
		var headers = this.getHeaders();
		var win = headers.searchWindow;
		
		var searchTerm = this.getSearchTextField().getValue();

		if(searchTerm)
		{
			this.getSearchTerm().setValue(searchTerm);
			
			var submit = this.getSearchSubmitButton();
			submit.handler();
		}
		
		if(win.isVisible()) {
			win.hide(searchButton);
		} else {
			win.show(searchButton);
		}
	},

	/**
	 * refresh the downloads store on downloadwindow pop up
	 */
	refreshDownloads: function() {
		var store = this.getDownloadsStore();
		store.load();
	},

	/**
	 * toggles between showing and hiding the link window.
	 * Is called whenever the link window needs to be shown/hidden
	 */
	onLinkClick: function() {
		var downloadButton = this.getLinkButton();
		var headers = this.getHeaders();
		var win = headers.downloadWindow;
		if(win.isVisible()) {
			win.hide(downloadButton);
		} else {
			win.show(downloadButton);
		}
	
	},

	/**
	 * toggles between showing and hiding the tasks window.
	 * Is called whenever the tasks window needs to be shown/hidden
	 */
	onTaskClick: function() {
		var taskButton = this.getTaskButton();
		var headers = this.getHeaders();
		var win = headers.taskWindow;
		if(win.isVisible()) {
			win.hide(taskButton);
		} else {
			win.show(taskButton);
		}
	},

	/**
	 * toggles between showing and hiding the project window.
	 * Is called whenever the project window needs to be shown/hidden
	 */
	onChangeProjectClick: function() {
		//Update stores
		this.getRecentProjectsStore().load();
		this.getProjectsStore().load();

		var projButton = this.getChangeButton();
		var headers = this.getHeaders();
		var win = headers.projectWindow;
		if(win.isVisible()) {
			win.hide(projButton);
		} else {
			win.show(projButton);
		}
	},

	/**
	 * toggles between showing and hiding the bookmarks window
	 * Is called whenever the bookmrk window needs to be shown/hidden
	 */
	onBookmarksClick: function() {
		var boomarkButton = this.getBookmarksButton();
		var headers = this.getHeaders();
		var win = headers.bookmarksWindow;
		if(win.isVisible()) {
			win.hide(bookmarksButton);
		} else {
			win.show(bookmarksButton);
		}
	},
	
	/**
	 * toggles between showing and hiding the import window
	 */
	onImportClick: function(){
		this.getCompletedImportsPanel().getStore().load({params: {projectName: this.projectName, status : "complete"}});
		this.getOngoingImportsPanel().getStore().load({params: {projectName: this.projectName, status : "pending"}});
		var importButton = this.getImportButton();
		var headers = this.getHeaders();
		var win = headers.importWindow;
		if(win.isVisible()) {
			win.hide(importButton);
		} else {
			win.show(importButton);
		}
	}

});
			
