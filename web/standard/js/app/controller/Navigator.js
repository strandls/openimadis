/**
 * Controls events from the navigator
 * Note: fetching children logic is done automatically by treeStore (NavigatorStore.js)
 */
Ext.define('Manage.controller.Navigator', {
	extend: 'Ext.app.Controller',

	requires: [ 'Manage.view.Navigator', 'Manage.view.settings.ProjectUsers'],

	stores: [
		'Navigators', 'NavigatorThumbnails', 'NavigatorFields', 'NavigatorSelectedFields'
	],

	refs: [{
		ref: 'navigator',
		selector: 'navigator'
	}, {
		ref: 'navigatorButton',
		selector: '#navigatorButton'
	}, {
		ref: 'navigatorWindow',
		selector: 'navigatorwindow'
	}, {
		ref: 'navigatorFieldChooser',
		selector: 'navigatorFieldChooser'
	}, {
		ref: 'navigatorViewArea',
		selector: '#navigatorViewArea'
	}, {
		ref: 'navigatorThumbnails',
		selector: '#navigatorThumbnails'
	}],

	/**
	 * name of the currently active project
	 */
	activeProject: '',
	
	getActiveProject: function() {
		return this.activeProject;
	},

	init: function() {
		// listen to the change in navigator node selection ..
		this.control({
			'navigator': {
				beforeitemdblclick : this.onNodeDoubleClick,
				fieldSelector : this.onFieldSelector,
				projectlisting : this.onProjectListing,
				beforeitemexpand : this.onNodeExpand, //done
				itemclick : this.onNodeClick,
				refreshProjects : this.onRefreshProjects,
				afterrender: this.onNavigatorAfterRender
			},
			'#navigatorChangeButton': {
				click: this.onChange 
			},
			'navigatorFieldChooser': {
				'okclicked': this.onFieldsChange,
				'resetClicked': this.refreshNavigatorFieldsStore
			}
		});
		this.application.on({
			changeProject: this.onAddProject,
			scope: this
		});
	},

	/**
	 * select a node on navigator being rendered
	 * TODO make this work
	 */
	onNavigatorAfterRender: function() {
		var store = this.getNavigatorsStore();
		var root = store.getRootNode();
		var node = root.getChildAt(0);
		var selectionModel = this.getNavigator().getSelectionModel();

		if(selectionModel.getSelection().length === 0) {
			selectionModel.select(node);
			this.loadProject(node);
		}
	},

	/**
	 * navigator fields have changed
	 */
	onFieldsChange: function(view, selectedFields, available) {
		//if same, do nothing
		var oldSelected = this.getNavigatorSelectedFieldsStore();
		if(this.hasFieldsChanged(oldSelected, selectedFields ) === false) {
			return;
		}
		var projectName = this.activeProject;
		var chosenFields = [];
		var i;

		for (i=0; i < selectedFields.length; ++i ) {
			chosenFields.push(selectedFields[i].data.name);
		}
		// Write to server
		var me = this;
		Ext.Ajax.request({
			method : 'POST',
			url : '../navigator/setFieldsChosen',
			params : {
				projectName : projectName,
				chosenFields : Ext.encode(chosenFields)
			},
			success : function(result, req) {
				//reloading the navigator and the thumbnails
				me.onAddProject(me.getActiveProject());

				//changing the view to thumbnails
				me.getNavigatorViewArea().setActiveTab(0);
			},
			failure : function(result, req) {
				showErrorMessage(result.responseText, "Field saving to server failed");
			}
		});

	},

	/**
	 * check if fields have changed 
	 */
	hasFieldsChanged : function(selectedFields, newSelectedFields) {
		var fieldsChanged = false;
		// Compare the arrays and set the flag fieldsChanged if there is any change
		if (selectedFields.length == newSelectedFields.length) {
			for (var i=0; i < selectedFields.length ; ++i) {
				if (selectedFields[i] != newSelectedFields[i]) {
					fieldsChanged = true;
					break;
				}
			}
		} else {
			fieldsChanged = true;
		}
		return fieldsChanged;
	},
	/**
	 * display new project in navigator
	 */
	onAddProject: function(projectName) {
		showConsoleLog('Navigator', 'onAddProject', 'adding project'+projectName);
		var store = this.getNavigatorsStore();
		
		var root = store.getRootNode();
		var node = this.constructProjectNode(projectName);
		
		//remove previous project and add new one
		root.removeAll();
	 	root.appendChild(node);

		//select nodes and render thumbnails
		var nav = this.getNavigator();
		nav.getSelectionModel().select(node);
		this.loadProject(node)

		//order is important
		this.activeProject = projectName;
		this.refreshNavigatorFieldsStore();
	},

	/**
	 * refresh the navigator fields
	 */
	refreshNavigatorFieldsStore : function () {
		var projectName = this.activeProject;
		var fieldStore = this.getNavigatorFieldsStore();
		fieldStore.load ({
			params : {
				projectName : projectName
			},
			callback : function(records, operation, success) {
				if (!success) 
					Ext.Msg.Alert("Warning", "Cannot load project fields");
			}
		});
	},

	/**
	 * construct the project node
	 */
	constructProjectNode : function (projectName) {
		var projectNode = Ext.create('Manage.model.Navigator', {
			binned: false,
			id:  Ext.id(), //used internally by tree
			isProjectNode: true,
			leaf: false, 
			max: '',
			min: '',
			projectName: projectName,
			text: projectName 
		});
		return projectNode;
	},

	/**
	* Node expand is done manually
	*/
	onNodeExpand : function(node, opts) {
		console.log("before expand: "+ node);

		//FIXME bug in our code????
		// problem: somehow the beforeexpand event is being fired on store load which
		//  results in error
		if(node.internalId === 'root') {
			return true;
		}

		//if node already has children, do nothing
		if (node.childNodes && node.childNodes !== null && node.childNodes.length > 0)
			return true;

		var conditions = this.getNavigator().getPreConditions(node); 
		this.fetchChildren(node, conditions);

		return true; 
	},

	/**
	* Fetch children for a node. If successful add the same
	*/
	fetchChildren : function(node, conditions) {
		var controller = this;
		Ext.Ajax.request( {
			method : 'GET',
			url : '../navigator/getChildren',
			params : {
				projectName : node.get('projectName'), 
				conditions : Ext.encode(conditions)
			},
			success : function (result, request) {
				var childNodes = Ext.decode(result.responseText);
				if (childNodes !== null && childNodes.length > 0)
					controller.addChildren(childNodes, node);
				else
					node.set('leaf', true);
			},
			failure : function (result, request) {
				showErrorMessage(result.responseText, "Failed to fetch children");
			}
		});
	},

	/**
	* Add children under the given node
	*/
	addChildren : function(childNodes, parentNode) {
		var lastAddedNode = null;
		var i;
		for (i=0; i < childNodes.length ; ++i) {
			var nextNode = this.constructNode(childNodes[i],  parentNode.get('projectName'));
			lastAddedNode = parentNode.appendChild(nextNode);
		}
	},

	/**
	* Utility to construct a node from nodeData
	*/
	constructNode : function(nodeData, projectName) {
		var nodeLimits = this.getNodeLimits(nodeData);
		var isLeafNode = this.checkIfLeafNode(nodeData.fieldName);
		var min;
		var max;

		if (nodeData.lowerLimit)
			min = nodeData.lowerLimit;
		if (nodeData.upperLimit)
			max = nodeData.upperLimit;

		var nextNode = Ext.create('Manage.model.Navigator', {
			count : nodeData.noOfRecords,
			fieldName : nodeData.fieldName,
			fieldType : nodeData.fieldType,
			id : Ext.id(), //used internally by tree
			isProjectNode : false,
			leaf : isLeafNode,
			max: max,
			min: min,
			projectName : projectName,
			qtip : "Limits : " + nodeLimits + "<br/>Count : " + nodeData.noOfRecords,
			text : nodeData.fieldName + ' ' + nodeLimits + ':'+nodeData.noOfRecords
		});
		return nextNode;
	},

	/**
	* Get the text to show on the node
	*/
	getNodeLimits : function(nodeData) {
		var lowerText = this.getFormatted(nodeData.lowerLimit, nodeData.fieldType);
		var upperText = this.getFormatted(nodeData.upperLimit, nodeData.fieldType);
		var limits = "";
		if (nodeData.lowerLimit === nodeData.upperLimit) {
			if (!nodeData.lowerLimit)
				limits = "()";
			else
				limits = '(' + lowerText + ')';
		}
		else
			limits = '('+lowerText+'-'+upperText+')';
		return limits;
	},

	/**
	 * format the limits
	 */
	getFormatted : function(value, type) {
		if (!value)
			return "";
		if (type === "Real")
			return Ext.util.Format.number(value, "0.00");
		else
			return value;
	},

	/*
	* Check if the node is leaf node
	*/
	checkIfLeafNode : function(fieldName){
		var selectedFields = this.getNavigatorSelectedFieldsStore().data.items.slice();
		var length = selectedFields.length
		if(length > 0){
			var lastField = selectedFields[length - 1].data.name;
		if(fieldName === lastField)
			return true;
		else
			return false;
		}
		else
			return true;
	},

	/**
	 * node clicked
	 */
	onNodeClick: function(view, node, item, index, e, eOpts) {
		var isProjectNode = node.get('isProjectNode');

		//make different ajax calls based on node type
		if(isProjectNode) {
			this.loadProject(node);
		} else {
			this.loadBin(node);
		}
	},

	// Double click action is by default expand. Replacing that with show siblings
	// wherever siblings are available.
	// Note: return false is important to prevent the default action of expanding node
	onNodeDoubleClick : function(view, node, item, index, e, eOpts) {
		// no siblings for the project node
		if (node.get('isProjectNode'))
			return false;
		if (node.isExpanded())
			return false;

		var conditions = this.getNavigator().getPreConditions(node.parentNode);
		this.fetchSiblings(node, conditions);
		return false;
	},

	/**
	* Fetch siblings for a node. if successful add the same
	*/
	fetchSiblings : function (node, conditions) {
		var me = this;
		var params = {
			fieldName : node.get('fieldName'),
			fieldType : node.get('fieldType'),
			projectName : node.get('projectName'),
			conditions : Ext.encode(conditions)
		};
		if (node.get('min'))
			params["min"] = node.get('min') + "";
		if (node.get('max'))
			params["max"] = node.get('max') + "";

		Ext.Ajax.request( {
			method : 'GET',
			url : '../navigator/getSiblings',
			params : params,
			success : function (result, request) {
				if(result.responseText.length <= 0) {
					return;
				}

				var siblings = Ext.decode(result.responseText);
				if (siblings !== null && siblings.length > 0) {
					    me.addSiblings(siblings, node);
				}
				console.log("Sibling nodes: " + siblings);
			},
			failure : function (result, request) {
				// TODO is this an actual error
				//showErrorMessage(result.responseText, "Failed to fetch siblings for node");
			}
		});
	    },

	
	/**
	 * load the bin thumbnails
	 */
	loadBin: function(node) {
		var projectName = node.get('projectName');
		var conditions = this.getNavigator().getParentPreConditions(node);
		var _this = this;

		var params = {
			projectName : projectName,
			conditions : Ext.encode(conditions),
			fieldName : node.get('fieldName'),
			fieldType : node.get('fieldType')
		};
		if (node.get('min')) {
			params["min"] = node.get('min') + "";
		}
		if (node.get('max')) {
			params["max"] = node.get('max') + "";
		}

		Ext.Ajax.request({
			method: 'GET',
			url: '../project/getRecordsForBin',
			params: params,
			success: function(result, response) {
				if (result.responseText && result.responseText.length > 0) {
					_this.loadThumbnails(result.responseText);
				}
			}
		});

	},

	/**
	 * load the  project thumbnails
	 */
	loadProject: function(node) {
		var projectName = node.get('projectName');
		var _this = this;
		Ext.Ajax.request({
			method: 'GET',
			url : '../project/getRecordsForProject',
			params: {
				projectName: projectName
			},
			success: function(result, response) {
				_this.loadThumbnails(result.responseText);
			},
			failure: function(result, response) {
				console.log(result.responseText);
			}
		});
	},

	/**
	 * load thumbnails from the provided responsetext
	 */
	loadThumbnails: function(responseText) {
		var i = 0;
		var tdata = [];
		var next = '';
		var guid = '';
		var store = this.getNavigatorThumbnailsStore();

		allData = Ext.JSON.decode(responseText);
		records = allData.data;

		for(i = 0; i < records.length; i++) {
			guid = records[i]['Record ID'];
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
		var nav = this.getNavigator();
		var node = nav.getSelectionModel().getLastSelected();
		
		var selectedRecords = this.getNavigatorThumbnails().getSelectionModel().getSelection();
		var recordids =[];
		for(var k=0;k<selectedRecords.length;k++)
			recordids.push(selectedRecords[k].data.id);

		if(selectedRecords.length!==0){
			nav.fireEvent('loadRecords', node.get('projectName'), recordids);
		}
		else{
			if(node.get('isProjectNode')) {
				nav.fireEvent('loadProject', node.get('projectName'));
			} else {
				nav.fireEvent('loadBin', node);
			}
		}
		
		//close the window
		var navBtn = this.getNavigatorButton();	
		navBtn.fireEvent('click');
			
	}
});
