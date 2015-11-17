/**
 * Manages the thumbnails shown in the View. 1. Loads new thumbnails from -
 * navigator selection - search results - project change 2. Increases/decreases
 * size of thumbnails 3. "project change" events from all sources are caught
 * here, and then an application wide "project change" is fired. 4. Sorts the
 * images based on different fields
 */
Ext.define('Manage.controller.Thumbnails', {
	extend : 'Ext.app.Controller',

	refs : [ {
		ref : 'navigator',
		selector : 'navigator'
	}, {
		ref : 'thumbnails',
		selector : '#imageThumbnails'
	}, {
		ref : 'projectListing',
		selector : 'projectListing'
	}, {
		ref : 'searchResults',
		selector : 'searchResults'
	}, {
		ref : 'searchPanel',
		selector : 'searchPanel'
	}, {
		ref : 'searchTerm',
		selector : '#searchterm'
	}, {
		ref : 'searchSubmitButton',
		selector : '#searchSubmitButton'
	}, {
		ref : 'advanceQuery',
		selector : '#advanceQuery'
	}, {
		ref : 'sortThumbnailsButton',
		selector : '#sortThumbnailsButton'
	}, {
		ref : 'sortThumbnailCombo',
		selector : '#sortThumbnailCombo'
	}, {
		ref : 'thumbslider',
		selector : '#thumbslider'
	}, {
		ref : 'summaryGrid',
		selector : '#summaryGrid'
	}, {
		ref: 'searchButton',
		selector: '#searchButton'
	}, {
		ref: 'headers',
		selector: 'headers'
	}, {
		ref: 'imageCanvas',
		selector: '#imageCanvas'
	}, {
		ref: 'imagePanel',
		selector: 'imagepanel'
	}, {
		ref: 'siteControl',
		selector: '#sitecontrol'
	}],

	controllers : [ 'Navigator','RecordController' ],

	stores : [ 'SearchResults', 'RecordThumbnails', 'ProjectFields', 'SelectionRecords', 'Comments', 'Channels', 'ChannelContrasts', 'Historys', 
	   		'ImageMetaDatas', 'RecordMetaDatas', 'SystemAttachments', 'UserAttachments',
			'UserAnnotations', 'Overlays','RecordMetaDatas', 'ImageMetaDatas'],

	models : [ 'RecordThumbnail' ],

	/**
	 * Dynamically set function for refresh. Is assigned to the
	 * function to call on refresh which can be refreshSearch,
	 * or onChangeProject
	 * 
	 * @private
	 */
	fn : '',

	/**
	 * arguments for the dynamically set refresh function
	 * 
	 * @private
	 */
	args : '',

	init : function()
	{
		this.control({
			'#thumbslider' : {
				change : this.onThumbSlide
			},
			'#refreshButton' : {
				click : this.onRefresh
			},
			'navigator' : {
				loadBin : this.onLoadBin,
				loadProject : this.onLoadProject,
				loadRecords: this.onLoadNavigatorRecords
			},
			'projectListing' : {
				changeProject : this.onChangeProject
			},
			'searchResults' : {
				loadRecords : this.onLoadRecords
			},
			'#sortThumbnailCombo' : {
				select : this.sortThumbnails
			},
			'#sortThumbnailsButton' : {
				changeSort : this.changeSort
			},
			'bookmarks' : {
				loadrecords : this.onLoadBookmark
			},
			'comments' : {
				recordLinkClicked : this.onLinkClicked
			}
			
		});
	},

	/**
	 * called when record link is clicked
	 */
	onLinkClicked : function(recordids){		

		var searchButton = this.getSearchButton();
		var headers = this.getHeaders();
		var win = headers.searchWindow;
		if(win.isVisible()) {
			win.hide(searchButton);
		} else {
			win.show(searchButton);
		}
		
		var searchPanel = this.getSearchPanel();
		var submit = this.getSearchSubmitButton();
		var store = this.getSearchResultsStore();

		var searchTerm="";
		for(id in recordids){
			searchTerm=searchTerm+"guid:"+recordids[id]+" OR ";
		}
		
		searchTerm=searchTerm.substring(0,searchTerm.length-4);
		
		// set "search query" and "advance option" 
		this.getSearchTerm().setValue(searchTerm);
		this.getAdvanceQuery().setValue(true);

		//submit search
		submit.handler();
	},
	
	/**
	 * on login load previous worked on project
	 */
	onLaunch : function()
	{
		// Change user name displayed
		var userName = $('#welcomeMessage').html();
		var user = Ext.ComponentQuery.query('#user')[0];
		user.update(userName);

		this.activeUser = userName;

		console.log('onLaunched');

		var me = this;
		// get the recently accessed project
		Ext.Ajax.request({
			method : 'GET',
			url : '../project/recentList',
			success : function(result, response)
			{
				var projectName = Ext.decode(result.responseText).items[0].name;

				me.onChangeProject(projectName);
				me.application.fireEvent('changeProject', projectName);
			}
		});
	},

	/**
	 * Refreshes the thumbnails
	 */
	onRefresh : function()
	{
		this.fn.apply(this, this.args);
	},

	/**
	 * set the function and args
	 */
	setRefresh : function(fn, args)
	{
		this.fn = fn;
		this.args = args;
	},

	/**
	 * sets the project name that is displayed
	 */
	setProject : function(projectName, num, from)
	{
		// Change project name displayed
		var projectDisplay = Ext.ComponentQuery.query('#projectDisplay')[0];
		var text = '<span title="' + projectName + '" style="font-weight:bold; overflow:hidden; white-space:nowrap;">Project: ' + projectName
				+ '</span><br />' + '<sub>' + num + '  ' + 'records';
		if (typeof (from) !== 'undefined' && from)
		{
			text += ' from ' + from;
		}
		text += '</sub>';
		
		text+='<br /> <br />Role:';
		
		var role;
		Ext.Ajax.request({
            method : 'GET',
            url : '../admin/getUserPermission',
            params : {
                'projectName' : projectName
            },
            success : function (result, response) {
            	role=Ext.decode(result.responseText)[0].name;
            	text+=role;
            	projectDisplay.update(text);
            },
            failure: function (result, response){
            	projectDisplay.update(text);
            }
		});
	},

	/**
	 * load project thumbnails and fire application wide event
	 */
	onChangeProject : function(projectName)
	{
		showConsoleLog('Thumbnails', 'onChangeProject', projectName);

		// send projectName to server, so that the recently
		// accessed list can be updated
		Ext.Ajax.request({
			method : 'POST',
			url : '../project/projectAccessed',
			params : {
				projectName : projectName
			},
			failure : function(result, request)
			{
				showErrorMessage(result.responseText, 'Saving project selection to server failed');
			}
		});

		this.onLoadProject(projectName);
		this.application.fireEvent('changeProject', projectName);
	},

	/**
	 * load the records and fire the application wide event
	 * 
	 * @param {Array}
	 *            recordids array of recordids to load
	 */
	onLoadRecords : function(projectName, recordids)
	{
		// set the project name that is displayed
		this.setProject(projectName, recordids.length, 'Search');

		// set up the refresh function
		// NOTE: refresh function is not onLoadRecords function
		// but refreshSearch
		this.setRefresh(this.refreshSearch, [ projectName ]);

		var me = this;
		// get the record details
		Ext.Ajax.request({
			method : 'GET',
			url : '../project/getRecords',
			params : {
				projectName : projectName,
				recordids : Ext.encode(recordids)
			},
			success : function(result, response)
			{
				if (result.responseText && result.responseText.length > 0)
				{
					me.setRecords(result.responseText);
				}
			},
			failure : function(result, request)
			{
				showErrorMessage(result.responseText, 'Loading link results failed');
			}
		});

		this.application.fireEvent('changeProject', projectName);

	},


	/**
	 * Refresh the search results by resending the search query.
	 * Also reload the thumbnails
	 * 
	 * @param {String}
	 *            projectName the name of the project to search
	 *            for in Search Tree
	 */
	refreshSearch : function(projectName)
	{
		var searchPanel = this.getSearchPanel();
		var submit = this.getSearchSubmitButton();
		var store = this.getSearchResultsStore();

		// set "search query" and "advance option" to last
		// submitted search
		this.getSearchTerm().setValue(searchPanel.query);
		this.getAdvanceQuery().setValue(searchPanel.advance);

		// re-submit search
		// BUG: ExtJS
		// When handler is used instead of click listener,
		// handler wont be called on fireEvent('click').
		// Hence calling handler directly.
		submit.handler();

		// once store is loaded, handle loading the recordids
		store.on('load', function()
		{
			// select the correct node
			var root = this.getSearchResultsStore().getRootNode();
			var node = root.findChild('projectName', projectName);

			if (node === null)
			{
				// node not found in search now,
				// because records in the project were altered
				recordids = [];
			}
			else
			{
				recordids = node.get('recordids');
			}

			this.onLoadRecords(projectName, recordids);
		}, this, // scope of the callback function
		{
			single : true
		});
	},

	/*
	 * change size of thumbnails based on slider position
	 */
	onThumbSlide : function(slider, x)
	{
		var dim = 0;
		switch (x)
		{
			case 1:
				dim = 228;
				break;
			case 2:
				dim = 108;
				break;
			case 3:
				dim = 68;
				break;
			case 4:
				dim = 48;
				break;
			case 5:
				dim = 36;
				break;
			case 6:
				dim = 28;
				break;
		}
		$('#imageThumbnails .thumb img').css('width', dim + 'px');
		$('#imageThumbnails .thumb img').css('height', dim + 'px');

		this.setRecordInFocus();
	},

	/**
	 * load the bin thumbnails
	 */
	onLoadBin : function(node)
	{
		var projectName = node.get('projectName');
	
		var conditions = this.getNavigator().getParentPreConditions(node);
		var me = this;

		// NOTE - the refresh function is onChangeProject
		// Navigator state might have changed after share,
		// transfer or delete.
		// So reload the whole project.
		this.setRefresh(this.onChangeProject, [ projectName ]);

		var params = {
			projectName : projectName,
			conditions : Ext.encode(conditions),
			fieldName : node.get('fieldName'),
			fieldType : node.get('fieldType')
		};
		if (node.get('min'))
		{
			params["min"] = node.get('min') + "";
		}
		if (node.get('max'))
		{
			params["max"] = node.get('max') + "";
		}

		Ext.Ajax.request({
			method : 'GET',
			url : '../project/getRecordsForBin',
			params : params,
			success : function(result, response)
			{
				if (result.responseText && result.responseText.length > 0)
				{
					// set the project name that is
					// displayed
					var length = Ext.JSON.decode(result.responseText).data.length;
					me.setProject(projectName, length, 'Navigator');

					me.setRecords(result.responseText);
				}
			},
			failure : function(result, request)
			{
				showErrorMessage(result.responseText, 'Loading bin failed');
			}
		});

	},

	/**
	 * load the project thumbnails
	 */
	onLoadProject : function(projectName)
	{
		// set up the refresh function
		this.setRefresh(this.onChangeProject, [ projectName ]);		

		var me = this;
		Ext.Ajax.request({
			method : 'GET',
			url : '../project/getRecordsForProject',
			params : {
				projectName : projectName
			},
			success : function(result, response)
			{
				// set the project name that is displayed
				var count = Ext.JSON.decode(result.responseText).count;
				me.setProject(projectName, count);
				me.setRecords(result.responseText);				
				
			},
			failure : function(result, response)
			{
				showErrorMessage(result.responseText, 'Loading project failed');
			}
		});
	},
	
	/**
	 * load the records for the tasks
	 * 
	 * @param {Array}
	 *            recordids array of recordids to load
	 */
	onLoadTask : function(projectName, recordids)
	{
		// set the project name that is displayed
		this.setProject(projectName, recordids.length, 'Tasks');

		// set up the refresh function
		// NOTE - refresh the whole project
		this.setRefresh(this.onChangeProject, [ projectName ]);

		var me = this;
		// get the record details
		Ext.Ajax.request({
			method : 'GET',
			url : '../project/getRecords',
			params : {
				projectName : projectName,
				recordids : Ext.encode(recordids)
			},
			success : function(result, response)
			{
				if (result.responseText && result.responseText.length > 0)
				{
					me.setRecords(result.responseText);
				}
			},
			failure : function(result, request)
			{
				Ext.Msg.alert('Error', 'Could nor get records for tasks');
			}
		});
	},

	/**
	 * load the records for the bookmarks
	 * 
	 * @param {Array}
	 *            recordids array of recordids to load
	 */
	onLoadBookmark : function(projectName, recordids)
	{
		// set the project name that is displayed
		this.setProject(projectName, recordids.length, 'Bookmark');

		// set up the refresh function
		// NOTE - refresh the whole project
		this.setRefresh(this.onChangeProject, [ projectName ]);

		var me = this;
		// get the record details
		Ext.Ajax.request({
			method : 'GET',
			url : '../project/getRecords',
			params : {
				projectName : projectName,
				recordids : Ext.encode(recordids)
			},
			success : function(result, response)
			{
				if (result.responseText && result.responseText.length > 0)
				{
					me.setRecords(result.responseText);
				}
			},
			failure : function(result, request)
			{
				Ext.Msg.alert('Error', 'Could nor get records for bookmark');
			}
		});
	},
	
	/**
	 * load the records for the navigator
	 * 
	 * @param {Array}
	 *            recordids array of recordids to load
	 */
	onLoadNavigatorRecords : function(projectName, recordids)
	{
		// set the project name that is displayed
		this.setProject(projectName, recordids.length, 'Navigator');

		// set up the refresh function
		// NOTE - refresh the whole project
		this.setRefresh(this.onChangeProject, [ projectName ]);

		var me = this;
		// get the record details
		Ext.Ajax.request({
			method : 'GET',
			url : '../project/getRecords',
			params : {
				projectName : projectName,
				recordids : Ext.encode(recordids)
			},
			success : function(result, response)
			{
				if (result.responseText && result.responseText.length > 0)
				{
					me.setRecords(result.responseText);
				}
			},
			failure : function(result, request)
			{
				Ext.Msg.alert('Error', 'Could nor get records for navigator');
			}
		});
	},	

	/**
	 * set the records for the thumbnails in the main view Load
	 * new fields for the project
	 */
	setRecords : function(responseText)
	{
		var json = Ext.JSON.decode(responseText);
		var fieldNames = json.fields;
		var fieldTypes = json.types;

		// set summary table columns
		this.setSummaryTable(fieldNames);

		// set new fields in model
		var fields = [];
		var i;
		for (i = 0; i < fieldNames.length; i++)
		{
			fields.push(Ext.create('Ext.data.Field', {
				name : fieldNames[i],
				type : fieldTypes[i]
			}));
		}

		// the field store does not need imagesource
		var fieldsStore = this.getProjectFieldsStore();
		fieldsStore.loadData(fields);

		// since in the returned values imagesource is not
		// present,
		// need to map Record ID -> imagesource
		fields.push(Ext.create('Ext.data.Field', {
			name : 'imagesource',
			convert : function(value, record)
			{
				var guid = record.get('Record ID');
				return '../project/getThumbnail?recordid=' + guid + '&t=' + Math.random();
			}
		}));

		// create an id field as well
		// id property of thumbnail is used to get current
		// record selected
		fields.push(Ext.create('Ext.data.Field', {
			name : 'id',
			convert : function(value, record)
			{
				return record.get('Record ID');
			}
		}));

		var model = this.getRecordThumbnailModel();
		model.setFields(fields);

		// store should be loaded only after model fields have
		// been set
		var records = json.data;
		this.getRecordThumbnailsStore().loadData(records);
		this.getSelectionRecordsStore().loadData(records);

		// select a default image
		var thumbnails = this.getThumbnails();
		thumbnails.getSelectionModel().select(0, false, false);

		// reset the thumbnails
		// has to be called after loading the store
		this.resetThumbnails();
	},

	/**
	 * sort the thumbnails according to the field selected
	 */
	sortThumbnails : function()
	{
		var name = this.getSortThumbnailCombo().getValue();
		var store = this.getRecordThumbnailsStore();
		var order = this.getSortThumbnailsButton().order; // ASC
		// or
		// DESC

		store.sort(name, order);

		// adjust thumbnail size after any store event
		this.adjustThumbnailSize();
	},

	/**
	 * change sort order ASC/DESC of thumbnails
	 */
	changeSort : function(order)
	{
		var name = this.getSortThumbnailCombo().getValue();
		var store = this.getRecordThumbnailsStore();

		if (name === null)
		{
			// no field selected
			return;
		}

		store.sort(name, order);

		// adjust thumbnail size after any store event
		this.adjustThumbnailSize();

	},

	/**
	 * Sets all controls related to thumbnails accordingly
	 */
	resetThumbnails : function()
	{
		var combo = this.getSortThumbnailCombo();

		if (combo.getValue() === null)
		{
			combo.setValue('Record ID');
		}

		this.sortThumbnails();
	},

	/**
	 * adjusts size of the thumbnails according to slider values
	 * FIXME could not register this function for any event
	 * hence has to be called whenever the RecordThumbnails
	 * store changes in any way Tried - associating with store
	 * load event, but the thumbnails view template is applied
	 * later overriding the css values that are set
	 */
	adjustThumbnailSize : function()
	{
		// get slider value and set thumbnail size
		var slider = this.getThumbslider();
		this.onThumbSlide(slider, slider.getValue());
	},

	/**
	 * set the columns in the summary table
	 * 
	 * @arg {Array} fieldNames an array of names of the columns
	 */
	setSummaryTable : function(fieldNames)
	{
		var summary = this.getSummaryGrid();
		var columns = [];

		var i;
		for (i = 0; i < fieldNames.length; i++)
		{
			var hideable = true;
			if (fieldNames[i] === 'Record ID')
			{
				hideable = false;
			}

			var minWidth = fieldNames.length < 12 ? 150 : 200;
			columns.push({
				text : fieldNames[i],
				dataIndex : fieldNames[i],
				hideable : hideable,
				minWidth : minWidth
			});
		}

		summary.reconfigure(undefined, columns);
	},

	/**
	 * sets the selected record into focus
	 */
	setRecordInFocus : function()
	{
		var thumbs = this.getThumbnails();
		var item = thumbs.getSelectionModel().getLastSelected();
		var node = thumbs.getNode(item);

		thumbs.focusNode(item);
	}
});
