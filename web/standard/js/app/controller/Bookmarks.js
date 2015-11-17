/**
 * controls for all the Bookmarks related activity
 */
Ext.define('Manage.controller.Bookmarks', {
	extend: 'Ext.app.Controller',

	stores: [
		'Bookmarks', 'BookmarksInSelection', 'BookmarksThumbnails'
	],

	refs: [{
		ref: 'bookmarks',
		selector: 'bookmarks'
	}, {
		ref: 'bookmarksButton',
		selector: '#bookmarksButton'
	}, {
		ref: 'bookmarkPanel',
		selector: '#bookmarkPanel'
	}, {
		ref: 'bookmarksWindow',
		selector: 'bookmarkswindow'
	}, {
		ref: 'bookmarksInSelection',
		selector: '#bookmarksInSelection'
	}, {
		ref: 'addFolderPanel',
		selector: '#addFolderPanel'
	}],

	init: function() {
		this.control({
			'#bookmarks': {
				itemclick : this.onBookmarkNodeClick, 
				beforeitemexpand : this.onBookmarkNodeExpand 
			},
			'#bookmarksInSelection': {
				beforeitemexpand : this.onBookmarkNodeExpand 
			},
			'bookmarkswindow': {
				applyselection: this.onChange
			}, 
			'addBookmarkFolder': {
				addbookmarkfolder: this.onAddBookmarkFolder
			},
			'#addBookmarksPanel':{
				addFolder : this.onAddFolder, 
				removeFolder:this.onRemoveFolder
			}
		});

		this.application.on({
			changeProject : this.onChangeProject, 
			scope : this
		});
	},
	
	/**
	 * name of current project
	 */
	projectName: '',

	/**
	 * records pertaining to the current bookmark selected
	 */
	records: [],

	/**
	 * set the current records
	 * @param {JSON} data the records to load
	 */
	setRecords: function(data) {
		var i;
		this.records = [];

		for(i = 0; i < data.length; i++) {
			var record = data[i];
			this.records.push(record["Record ID"]);
		}
	},

	/**
	 * returns the current records
	 * @return {String[]} record id's
	 */
	getRecords: function() {
		return this.records;
	},

	/**
	 * set the name of the current project
	 * @param {String} projectName name of current project
	 */
	setCurrentProject: function(projectName) {
		this.projectName = projectName;
	},

	/**
	 * get the name of the current project
	 * @return {String} the name of the project
	 */
	getCurrentProject: function() {
		return this.projectName;
	},

	/**
	 * change the project name and load the project root bookmark
	 * @param {String} projectName name of current project
	 */
	onChangeProject: function(projectName) {
		this.setCurrentProject(projectName);

		var me = this;
		Ext.Ajax.request({
			url : '../project/getBookmarkFolders',
			method : 'GET',
			params : {
				projectName: projectName,
				bookmarkPath: projectName
			},
			success : function (result, request) {
				var resp = Ext.JSON.decode(result.responseText);
				me.initializeBookmarkWindow(resp[0]);
				me.initializeBookmarksInSelection(resp[0]);
			},
			failure : function (result, request) {
				Ext.Msg.alert("Error" , "Failed to load Bookmark for project: " + projectName);
			}
		});
	},

	/**
	 * initialize the bookmarks component of BookmarksWindow
	 * @param {Object} data an object with the Manage.model.Bookmark properties
	 */
	initializeBookmarkWindow: function(data) {
		var store = this.getBookmarksStore();
		var root = this.constructNode(data); 

		store.setRootNode(root);
		this.displayNode(root);

		//make the root node selected first time when the window shows up
		var win = this.getBookmarksWindow();
		win.on('show',
			function() {
				this.getBookmarks().getSelectionModel().select(root);
			},
			this,
			{single: true}
		);
	},

	/**
	 * initialize the BookmarksInSelection store
	 */
	initializeBookmarksInSelection: function(data) {
		var store = this.getBookmarksInSelectionStore();
		var root = this.constructNode(data); 

		store.setRootNode(root);
	},

	/**
	 * show the panel for adding a folder
	 */
	onAddFolder: function() {
		var bookmarkPanel = this.getAddFolderPanel();
		var selection = this.getBookmarksInSelection().getSelectionModel().getLastSelected();
		
		console.log(selection.get('bookmarkName'));
		var me = this;
		bookmarkPanel.add(Ext.create('Manage.view.AddBookmarkFolder', {
			title: 'Add folder to ' + selection.get('bookmarkName')
		}));
	},

	/**
	 * Add bookmark to the selected folder
	 * @param {String} name the name of the Bookmark to add
	 */
	onAddBookmarkFolder: function(name) {
		var parentNode = this.getBookmarksInSelection().getSelectionModel().getLastSelected();

		var me = this;
		Ext.Ajax.request({
			url: '../project/addBookmarkFolder',
			method: 'POST',

			params: {
				folderName: name,
				path: parentNode.getPath('bookmarkName'),
				projectName: me.getCurrentProject()
			},
			success: function(response, options) {
				// show the newly added bookmark in its parent node
				// collapse and expand to force the server call for the parent node
				parentNode.collapse();
				parentNode.expand();
			},
			failure: function(response, options) {
				Ext.Msg.alert('Error', 'Could not add folder');
			}
		});
	},

	/**
	 * remove the selected bookmark
	 */
	onRemoveFolder: function() {
		var node = this.getBookmarksInSelection().getSelectionModel().getLastSelected();
		var parentNode = node.parentNode;

		var me = this;
		Ext.Ajax.request({
			url: '../project/removeBookmarkFolder',
			method: 'POST',

			params: {
				path: node.getPath('bookmarkName'),
				projectName: me.getCurrentProject()
			},
			success: function(response, options) {
				if(parentNode === null) {
					// removed root folder
					var store = me.getBookmarksThumbnailsStore();
					store.removeAll();
				} else {
					// show the newly added bookmark in its parent node
					// collapse and expand to force the server call for the parent node
					parentNode.collapse();
					parentNode.expand();

					// display the selected node
					me.displayNode(parentNode);
				}
			},
			failure: function(response, options) {
				Ext.Msg.alert('Error', 'Could not delete folder');
			}
		});
	},

	/**
	 * Get the children of the expanded node from the server
	 * @param {Ext.data.NodeInterface} node The expanding node
	 */
	onBookmarkNodeExpand: function(node) {
		var projectName = this.getCurrentProject();
		var path = node.getPath("bookmarkName"); // get the path from the root of the current node

		var me = this;
		Ext.Ajax.request({
			url : '../project/getBookmarkFolders',
			method : 'GET',
			params : {
				projectName: projectName,
				bookmarkPath: path
			},
			success : function (result, request) {
				var resp = Ext.JSON.decode(result.responseText);
				me.appendChildren(node, resp);
			},
			failure : function (result, request) {
				Ext.Msg.alert("Error" , "Failed to load Bookmark");
			}
		});
	},

	/**
	 * select and display the node
	 * @param {Manage.model.Bookmark} node the node to select and display the thumbnails of
	 */
	displayNode: function(node) {
		var bookmarks = this.getBookmarks();
		
		//select node
		bookmarks.getSelectionModel().select(node);
		
		//show the thumbnails of the bookmark
		this.onBookmarkNodeClick(bookmarks.getView(), node);
	},

	/**
	 * Append the children to the given node
	 * @param {Ext.data.NodeInterface} node
	 * @param {JSON} children to append to the node
	 */
	appendChildren : function(node, json) {
		if(json[0] !== null) {
			var children = json[0].children;
			node.removeAll();
			for(var i = 0; i < children.length; i++) {
				var child = children[i];
				node.appendChild(this.constructNode(child));
			}
		}
	},

	/**
	 * Construct a node from the given data
	 * @param {Object} An object with the Manage.model.Bookmark properties
	 * @return {Manage.model.Bookmark} the node constructed from the data
	 */
	constructNode: function(data) {
		var node = Ext.create('Manage.model.Bookmark', {
			bookmarkName: data.bookmarkName,
			id: Ext.id(),
			leaf: data.leaf,
			recordCount: data.recordCount,
			text: data.text
		});
		return node;
	},

	/**
	 * Show the thumbnails for this bookmark folder
	 * @param {Ext.view.View} view the view which was clicked on
	 * @param {Ext.data.NodeInterface} node the selected node
	 */
	onBookmarkNodeClick: function(view, node) {
		var path = node.getPath("bookmarkName");

		if(node.get('recordCount') === '0') {
			var store = this.getBookmarksThumbnailsStore();
			store.removeAll();
		} else {
			this.loadThumbnailsForPath(path);
		}

		// if add folder panel is open change it 
		var panel = this.getBookmarkPanel();
		var addBookmark = panel.down('addBookmarkFolder');
		if(addBookmark !== null) {
			panel.remove(addBookmark);
			this.getBookmarks().fireEvent('addFolder');
		}
	},

	/**
	 * make a server call for the given path, 
	 *  and load the thumbnails in the BookmarksWindow
	 *  @param {String} path the path of the node to load 
	 */
	loadThumbnailsForPath: function(path) {
		var projectName = this.getCurrentProject();
		var isLeaf = false; // FIXME check this node.isLeaf()

		var me = this;
		Ext.Ajax.request({
			url : '../project/getRecordsForBookmarkFolder',
			method : 'GET',
			params : {
				projectName: projectName,
				bookmarkPath: path,
				isLeaf: isLeaf
			},
			success : function (result, request) {
				if (result.responseText && result.responseText.length > 0) {
					var records = Ext.JSON.decode(result.responseText);
					me.loadThumbnails(records.data);
					me.setRecords(records.data);
				}                   
			},
			failure : function (result, request) {
				Ext.Msg.alert("Error", "Failed to laod the records for the bookmark node");
			}
		});
	},

	/**
	 * load the thumbnails into the BookmarksWindow
	 * @param {JSON} records the records to load
	 */
	loadThumbnails: function(records) {
		var i = 0;
		var tdata = [];
		var next = '';
		var guid = '';
		var record;
		var store = this.getBookmarksThumbnailsStore();

		for(i = 0; i < records.length; i++) {
			record = records[i];
			guid = record["Record ID"];
			next = {
				id: guid,
				imagesource: '../project/getThumbnail?recordid=' + guid
			};
			tdata.push(next);
		}
		store.loadData(tdata);
	},

	/**
	 * change the viewed thumbnails
	 */
	onChange: function() {
		var bookmarks = this.getBookmarks();
		bookmarks.fireEvent('loadrecords', this.getCurrentProject(), this.getRecords());

		//close the window
		var bookmarkBtn = this.getBookmarksButton();
		bookmarkBtn.fireEvent('click');
	}
});
