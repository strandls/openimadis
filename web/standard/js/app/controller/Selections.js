/**
 * All controls related to the Image changes
 */
Ext.define('Manage.controller.Selections', {
	extend: 'Ext.app.Controller',
	
	requires: [
		'Manage.view.dialogs.FieldEditor','Manage.view.workflows.FavouriteWorkflows','Manage.view.admin.DownloadsList'
	],

	refs: [{
		ref: 'thumbnails',
		selector: '#imageThumbnails'
	}, {
		ref: 'header',
		selector: 'header'
	}, {
		ref: 'userAnnotations',
		selector: 'userannotations'
	}, {
		ref: 'fieldEditor',
		selector: 'fieldEditor'
	}, {
		ref: 'summaryGrid',
		selector: '#summaryGrid'
	}, {
		ref: 'selectionThumbnails',
		selector: '#selectionThumbnails'
	}, {
		ref: 'summaryButton',
		selector: '#summaryButton'
	}, {
		ref: 'formPanel',
		selector: '#formPanel'
	}, {
		ref: 'headers',
		selector: 'headers'
	}, {
		ref: 'downloadButton',
		selector: '#headerDownloadButton'
	}, {
		ref: 'shareButton',
		selector: '#headerShareButton'
	}, {
		ref: 'transferButton',
		selector: '#headerTransferButton'
	}, {
		ref: 'deleteButton',
		selector: '#headerDeleteButton'
	}, {
		ref: 'selectionWindow',
		selector: 'selectionwindow'
	}, {
		ref: 'annotateButton',
		selector: '#headerAnnotateButton'
	}, {
		ref: 'profileButton',
		selector: '#headerProfileButton'
	}, {
		ref: 'workflowButton',
		selector: '#headerWorkflowButton'
	}, {
		ref: 'selectPanel',
		selector: '#selectPanel'
	}, {
		ref: 'addBookmarkButton',
		selector: '#addBookmarkButton'
	},{
		ref: 'exportName',
		selector: '#exportname'
	}, {
		ref: 'bookmarks',
		selector: '#bookmarks'
	},{
		ref: 'recordLinkPanel',
		selector: 'recordlinkpanel'
	},{
		ref: 'actionsButton',
		selector: '#headerActionButton'
	},{
		ref: 'favouriteWorkflows',
		selector: 'favouriteworkflows'
	},{
		ref: 'bookmarksInSelection',
		selector: '#bookmarksInSelection'
	},{
		ref: 'downloadsList',
		selector: 'downloadsList'
	},{
		ref: 'dynamicPanel',
		selector: '#dynamicPanel'
	},{
		ref: 'fieldValues',
		selector: '#fieldvalues'
	}],
	
	stores : [
		'UserFields', 'UserAnnotations', 'SelectionRecords', 'WorkflowStore',
		'Bookmarks', 'BookmarksInSelection','FavouriteWorkflowStore','MemberProjects',
		'Comments','ProfileStore','WebApplicationsTree', 'WebApplications',
		'SelectedWorkflowStore','AvailableWorkflowStore','UserFieldValues','Imports','CompletedImports'
	],
	
	controllers : ['RecordController', 'Thumbnails', 'ProjectSettings', 'Bookmarks'],
	
	/**
	 * name of the currently active project
	 */
	projectName : '', 
	
	/*
	 * active button from which to animate Selection Panel
	 * Could be one of share, transfer, delete, download, annotate
	 */
	activeButton: '',
	
	init: function() {
		this.control({
			'#headerActionButton': {
				annotateButtonClicked: this.onAnnotateClick,
				deleteButtonClicked: this.onDeleteClick,
				downloadButtonClicked: this.onDownloadClick,
				profileButtonClicked: this.onProfileClick,
				shareButtonClicked: this.onShareClick,
				transferButtonClicked: this.onTransferClick,
				workflowButtonClicked: this.onWorkflowClick,
				addbookmarkclicked: this.onAddBookmarkClick,
				invokeUrlButtonClicked: this.onInvokeUrlClick
			},
			'userannotations' : {
				annotateSelected: this.onUserAnnotateSelected
			},
			'fieldEditor': {
				fieldbeforequery: this.onAnnotationFieldBeforeQuery,
				fieldselect: this.onAnnotateFieldSelect,
				typeselect: this.onAnnotationTypeSelect
			},
			'userannotations fieldEditor': {
				submitproperty: this.onRecordSubmitProperty,
				close: this.onAnnotationClose
			},
			'selectionwindow fieldEditor': {
				submitproperty: this.onWindowSubmitProperty,
				close: this.onSelectionClick
			},
			'#summaryGrid': {
				selectionchange: this.summarySelected,
				sortchange: this.focusThumbnail
			},
			'#selectionThumbnails': {
				selectionchange: this.thumbsSelected
			},
			'selectionwindow': {
				beforeclose: this.onSelectionClick,
				show: this.onRecordSelect
			},
			'selectionwindow exportDialog': {
				downloadselected: this.onDownloadSelected,
				close: this.onSelectionClick
			},
			'selectionwindow shareDialog': {
				shareSelected: this.onShareSelected,
				transferSelected: this.onTransferSelected,
				close: this.onSelectionClick
			},
			'selectionwindow profileChooser': {
				setProfile: this.onSetProfile,
				saveProfile: this.onSaveProfile,
				close: this.onSelectionClick
			},
			'#confirmTransfer':{
				confirmTransferSelected:this.onConfirmTransferSelected
			},
			'#comments':{
				deleteComment:this.onDeleteComment
			},
			'imports':{
				refresh:this.onRefreshImports,
				setParams:this.onSetParams
			}
		});
		
		this.application.on({
			changeProject : this.onChangeProject, 
			scope : this
		});
	},
	
	
	onChangeProject: function(projectName)
	{
		console.log("Project Changed To: ");
		console.log(projectName);
		this.projectName = projectName;
	},
	
	getActiveProject: function() {
		return this.projectName;
	},
	
	/**
	 * Show the selection window with the workflow options
	 */
	onInvokeUrlClick: function() {
		console.log("on invoke url clicked");
		// set the title of the window
		this.getSelectionWindow().setTitle('Invoke Url');
		var webAppStore=this.getWebApplicationsStore();
		webAppStore.reload();
		// set the panel to the workflows
		var panel = this.getFormPanel();
		panel.removeAll();
		panel.add(Ext.create('Ext.form.Panel', {
			id: 'urlArea',
	        layout: {
	            type: 'vbox',
	            align: 'stretch' 
	        },
			items:[{
				    title: 'Web Applications',
				    xtype: 'webApplications'
			}]
		}));
		
		// show the window
		this.setActiveButton(this.getActionsButton());
		this.onSelectionClick();
	},
	
	/**
	 * Show the selection window with delete option
	 */
	onDeleteClick: function() {
		//set the title of the window
		this.getSelectionWindow().setTitle('Delete');

		//set the panel to the share form
		var panel = this.getFormPanel();
		panel.removeAll();
		var me = this;
		panel.add(Ext.create('Ext.form.Panel', {
				buttons: [{
					text: 'Cancel',
					handler: function() {
						me.onSelectionClick();
					}
				}, {
					text: 'Delete',
					handler: function() {
						me.deleteSelected();
					}
				}]
			})
		);

		// show the window
		this.setActiveButton(this.getActionsButton()); //set button for animation
		this.onSelectionClick();
	},

	/**
	 * Submit the selected records for deletion
	 */
	deleteSelected: function() {
		this.onSelectionClick();
	
		//get the array of record id's selected
		var guids = this.getRecordsSelected();

		var me = this;
		Ext.Msg.confirm("Delete", "Are you sure you want to delete the selected " + guids.length + " records?", 
			function(id)  {
				if (id === "yes") {
					Ext.Ajax.request({
						method : 'POST',
						url : '../admin/deleteRecords',
						params : {
							recordids : Ext.encode(guids)
						},
						success : function (result, response) {
							Ext.Msg.alert("Success", "Deleted " + guids.length + " records");
							// Reload
							me.getThumbnailsController().onRefresh();
						},
						failure : function (result, response) {
							showErrorMessage(result.responseText, "Failed to delete records"); 
						}
					});
				}
			}
		);
	},
	
	/**
	 * Show the selection window with the download
	 */
	onDownloadClick: function() {
		//set the title of the window
		this.getSelectionWindow().setTitle('Downloads');

		//set the panel to the download form
		var panel = this.getFormPanel();
		panel.removeAll();
		panel.add(Ext.create('widget.exportDialog'));

		// show the window
		this.setActiveButton(this.getActionsButton()); //set button for animation
		this.onSelectionClick();
	},

	/**
	 * Submit the selected records for download
	 * @param {Ext.panel.form} form the form to submit
	 */
	onDownloadSelected: function(form) {
		this.onSelectionClick();
		var values = form.getFieldValues();
		var date = values.validity.getTime();

		//get the array of record id's selected
		var guids = this.getRecordsSelected();
		var me=this;

		Ext.Ajax.request({
			method : 'POST',
			url : '../project/submitDownload',
			params : {
				name : values.name,
				format : values.format,
				validity: date,
				guids: Ext.JSON.encode(guids)
			},
			success : function (result, response){
				Ext.Msg.alert("Success", "Record download request is submitted successfully.");
				me.getDownloadsList().store.reload();
			},
			failure : function(result, request) {
				showErrorMessage(result.responseText, "Failed to sumbit download request");
			} 
		});
	
	},

	/**
	 * show the selection window with bookmarks tree
	 */
	onAddBookmarkClick: function() {
		//set the title of the window
		this.getSelectionWindow().setTitle('Add Bookmark');

		//set up the form with bookmark
		
		var me = this;
		var form = Ext.create('Ext.form.Panel', {
				layout:  {
					type: 'vbox',
					align: 'stretch'
				},	
				
				id:'addBookmarksPanel',

				tbar: [{
					icon : "images/icons/add.png",
					tooltip: 'Add Bookmark',
					handler:function(){
						this.up().up().fireEvent('addFolder');
					}
				}, {
					icon : "images/icons/delete.png",
					tooltip: 'Remove Bookmark',
					handler:function(){
						this.up().up().fireEvent('removeFolder');
					}
				}],
				
				buttons: [{
					text: 'Add',
					handler: function() {
						console.log("selection model = ");
						console.log(me.getBookmarksInSelection().getSelectionModel().getLastSelected());
						me.addToBookmarks(me.getBookmarksInSelection().getSelectionModel().getLastSelected());
					}
				}, {
					text: 'Cancel',
					handler: function() {
						me.onSelectionClick();
					}
				}],
				
				items: [{		
						xtype: 'bookmarks',
						name:'bookmarks',
						id: 'bookmarksInSelection',
						store: 'BookmarksInSelection',
						flex:1
				},{
						xtype:'panel',	
						id: 'addFolderPanel',
				}]
		});

		//set the panel to the Bookmark form
		var panel = this.getFormPanel();
		panel.removeAll();
		panel.add(form);

		//when displayed, select the root
		var win = this.getSelectionWindow();
		win.on('show',
			function() {
				me.getBookmarksInSelection().getSelectionModel().select(0);
			},
			this,
			{single: true}
		);

		// show the window
		this.setActiveButton(this.getActionsButton()); //set button for animation
		this.onSelectionClick();
	},

	/**
	 * add selected record to the bookmark
	 * @param {Ext.data.NodeInterface} node the folder to add the records to
	 * TODO - modify server call to accept multiple records
	 */
	addToBookmarks: function(node) {
		var path = node.getPath('bookmarkName');
		var recordid = this.getSummaryGrid().getSelectionModel().getLastSelected().get('Record ID');
		
		var records = this.getSummaryGrid().getSelectionModel().getSelection();
		var recordids = new Array();
		for(var i=0;i<records.length;i++)
		{
			recordids[i] = records[i].get('Record ID');
		}
		console.log(recordids);
		
		var node = this.getBookmarksInSelection().getSelectionModel().getLastSelected();
		var parentNode = node.parentNode;
		
		var projectName = this.getActiveProject();

		var me = this;
		Ext.Ajax.request({
			url: '../project/addBookmarks', 
			method: 'POST', 
			params: {
				path: path,
				recordids: Ext.encode(recordids)
			},
			success: function(response, options) {
				
				if(parentNode!==null){
					parentNode.collapse();
					parentNode.expand();
				}
				else{
					Ext.Ajax.request({
						url : '../project/getBookmarkFolders',
						method : 'GET',
						params : {
							projectName: projectName,
							bookmarkPath: projectName
						},
						success : function (result, request) {
							var resp = Ext.JSON.decode(result.responseText);
							me.getBookmarksController().initializeBookmarkWindow(resp[0]);
							me.getBookmarksController().initializeBookmarksInSelection(resp[0]);
							
							Ext.Msg.alert("Success", "Bookmark was added successfully.");
						},
						failure : function (result, request) {
							Ext.Msg.alert("Error" , "Failed to load Bookmark for project: " + projectName);
						}
					});
				}
			},
			failure: function(response, options) {
				me.onSelectionClick();
				Ext.Msg.alert('Error', 'Could not add record to Bookmark');
			}
		});
	},

	/**
	 * show the selection window with share
	 */
	onShareClick: function() {
		//set the title of the window
		this.getSelectionWindow().setTitle('Share');
				
		var projectstore=this.getMemberProjectsStore();
		projectstore.reload();
		
		//set the panel to the share form
		var panel = this.getFormPanel();
		panel.removeAll();
		panel.add(Ext.create('widget.shareDialog', {
				flag: false
			})
		);

		// show the window
		this.setActiveButton(this.getActionsButton()); //set button for animation
		this.onSelectionClick();
	},

	/**
	 * Submit the selected records to share
	 * @param {Ext.panel.form} form the form to submit
	 */
	onShareSelected: function(form) {
		this.onSelectionClick();
		var values = form.getFieldValues();

		//get the array of record id's selected
		var guids = this.getRecordsSelected();

		Ext.Ajax.request({
			method : 'POST',
			url : '../project/shareRecords',
			params : {
					targetProject : values.targetProject,
					guids: Ext.JSON.encode(guids)
			},
			success : function (result, response){
				Ext.Msg.alert("Success", "Records are shared successfully.");
			},
			failure : function(result, request) {
				showErrorMessage(result.responseText, "Failed to share records");
			} 
		});
	},
	
	/**
	 * Show the selection window with the transfer
	 */
	onTransferClick: function() {
		//set the title of the window
		this.getSelectionWindow().setTitle('Transfer');

		var projectstore=this.getMemberProjectsStore();
		projectstore.reload();
		
		//set the panel to the transfer form
		var panel = this.getFormPanel();
		panel.removeAll();
		panel.add(Ext.create('widget.shareDialog', {
				flag: true
			})
		);

		// show the window
		this.setActiveButton(this.getActionsButton()); //set button for animation
		this.onSelectionClick();
	},

	/**
	 * 
	 */
	onSetProfile: function(form) {
		var values = form.getFieldValues();
		
		var recordIds = Ext.JSON.encode(this.getRecordsSelected());
		
		var projectName = this.getActiveProject();
        var me=this;
        var x,y,z;
        
        //x
        if(!(values.xPixelSize instanceof Array)){
        	x=[];
        	x.push(values.xPixelSize);
        }
        else{
        	x=values.xPixelSize;
        }
        

        //y
        if(!(values.yPixelSize instanceof Array)){
        	y=[];
        	y.push(values.yPixelSize);
        }
        else{
        	y=values.yPixelSize;
        }
        
        //z
        if(!(values.zPixelSize instanceof Array)){
        	z=[];
        	z.push(values.zPixelSize);
        }
        else{
        	z=values.zPixelSize;
        }
        
		Ext.Ajax.request({
            method : 'POST',
            url : '../admin/setAcqProfile',
            params : {
                profileName : Ext.encode(values.profileName),
                microscopeName : values.microscopeName,
                xPixelSize: Ext.encode(x),
                xType : Ext.encode(values.xType),
                yPixelSize: Ext.encode(y),
                yType : Ext.encode(values.yType),
                zPixelSize: Ext.encode(z),
                zType : Ext.encode(values.zType),
                timeUnit : Ext.encode(values.timeUnit),
                exposureTimeUnit : Ext.encode(values.exposureTimeUnit),
                lengthUnit : Ext.encode(values.lengthUnit),
                guids: recordIds
            },
            success : function (result, response){  
                Ext.Msg.alert("Success", "Profile successfully applied to selected records.");
        		// get the record details
        		Ext.Ajax.request({
        			method : 'GET',
        			url : '../project/getRecordsForProject',
        			params : {
        				projectName : projectName
        			},
        			success : function(result, response)
        			{
        				if (result.responseText && result.responseText.length > 0)
        				{
        					me.getThumbnailsController().setRecords(result.responseText);
        				}
        			},
        			failure : function(result, request)
        			{
        				showErrorMessage(result.responseText, 'Loading link results failed');
        			}
        		});
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to apply selected profile");
            } 
        });
	},
	
	/**
	 * 
	 */
	onSaveProfile: function(form) {
		var values = form.getFieldValues();
        
		var recordIds = Ext.JSON.encode(this.getRecordsSelected());
		
		var me=this;
		
        Ext.Msg.prompt('Name', 'Please enter profile name:', function(btn, text){
            if (btn == 'ok'){
            	Ext.Ajax.request({
                    method : 'POST',
                    url : '../admin/addAcquisitionProfile',
                    params : {
                        profileName : text,
                        microscopeName : values.microscopeName,
                        xPixelSize: values.xPixelSize,
                        xType : values.xType,
                        yPixelSize: values.yPixelSize,
                        yType : values.yType,
                        zPixelSize: values.zPixelSize,
                        zType : values.zType,
                        timeUnit : values.timeUnit,
                        lengthUnit : values.lengthUnit,
                        guids: recordIds
                    },
                    success : function (result, response){
                        Ext.Msg.alert("Success", "Profile successfully saved.");
                        me.getProfileStoreStore().reload();
                    },
                    failure : function(result, request) {
                        showErrorMessage(result.responseText, "Failed to save the profile");
                    } 
                });
            }
        });
	},
	
	/**
	 * Submit the selected records for transfer
	 * @param {Ext.panel.form} form the form to submit
	 */
	onTransferSelected: function(form) {
		
		this.onSelectionClick();
		
		var confirm=Ext.create('Ext.window.Window', {
		    title: 'Confirm Transfer',
		    id:'confirmTransfer',
		    height: 150,
		    width: 300,
		    layout: {
		        align: 'middle',
		        pack: 'center',
		        type: 'hbox'
		    },
		    modal:true,
		    items:[ {  // Let's put an empty grid in just to illustrate fit layout
		        xtype: 'text',
		        text:'Do you want to transfer the records'
		    }],
		    buttons:[
			    {
			    	text:'OK',
			        handler : function() {
			            var view = this.up().up();			            
			            view.fireEvent('confirmTransferSelected',form);	
			            view.close();
			        }
			    },
			    {
			    	text:'Cancel',
			        handler : function() {
			            var view = this.up().up();
			            view.close();
			        }
			    }		             
		    ]
		});
		
		confirm.show();

	},
	
	onConfirmTransferSelected: function(form){
		var values = form.getFieldValues();

		//get the array of record id's selected
		var guids = this.getRecordsSelected();
		
		var projectName=this.projectName;
		
		var me = this;
		Ext.Ajax.request({
			method : 'POST',
			url : '../project/transferRecords',
			params : {
				targetProject : values.targetProject,
				guids: Ext.JSON.encode(guids)
			},
			success : function (result, response){
				Ext.Msg.alert("Success", "Records are transfered successfully.");
				//refrsh the whole view
				me.getThumbnailsController().onRefresh();
				
				//get number of records in project
				Ext.Ajax.request({
					method : 'GET',
					url : '../project/getRecordsForProject',
					params : {
						projectName : projectName
					},
					success : function(result, response)
					{
						var count = Ext.JSON.decode(result.responseText).count;
						
						//if project is empty switch to target project
						if(count===0){
							Ext.Msg.alert(" Current Project is Empty", "Project "+projectName+" is now empty.</br> " +
									" Switching to project "+values.targetProject);
							me.getThumbnailsController().onChangeProject(values.targetProject);
						}
						
					},
					failure : function(result, response)
					{
						showErrorMessage(result.responseText, 'Loading project failed');
					}
				});
			},
			failure : function(result, request) {
				showErrorMessage(result.responseText, "Failed to transfer records");
			} 
		});
	},

	/**
	 * keep the last selected thumbnail in focus
	 * NOTE - sortchange is fired after the grid has been sorted
	 */
	focusThumbnail: function() {
		var thumbs = this.getSelectionThumbnails();
		var selectionModel = thumbs.getSelectionModel();
		var last = selectionModel.getLastSelected();

		if(last) {
			var node = thumbs.getNode(last);
			thumbs.focusNode(node);
		}
	},
		
	/**
	 * select corresponding thumbnails to the records selected in the summarytable
	 */
	summarySelected: function(selModel, selection) {
		var itemsToSelect = [];
		if(selection !== null || selection.length > 0) {
			//Select the same thumbnails as rows
			var i;
			for(i = 0; i < selection.length; i++) {
				var item = selection[i];
				itemsToSelect.push(item);
			}
		}

		var thumbs = this.getSelectionThumbnails();

		//select the thumbnails
		var selectionModel = thumbs.getSelectionModel();
		selectionModel.select(itemsToSelect, false, true);

		//put the last selected thumbnail into focus
		var len = itemsToSelect.length;
		if(len > 0) {
			var node = thumbs.getNode(itemsToSelect[len - 1]);
			thumbs.focusNode(node);
		}
		
		this.updateTitle(len);
	},

	/**
	 * select the corresponding rows in the summarytable to the thumbnails selected
	 */
	thumbsSelected: function(selModel, selection) {
		var itemsToSelect = [];
		if(selection !== null || selection.length > 0) {
			//Select the same rows as thumbnails
			var i;
			for(i = 0; i < selection.length; i++) {
				var item = selection[i];
				itemsToSelect.push(item);
			}
		}
		
		var grid = this.getSummaryGrid();	

		//select the rows
		var selectionModel = grid.getSelectionModel();
		selectionModel.select(itemsToSelect, false, true);

		//put the last selected row into focus
		var len = itemsToSelect.length;
		if(len > 0) {
			var view = grid.getView();
			view.focusRow(itemsToSelect[len - 1]);
		}
		
		this.updateTitle(len);
	},

	/**
	 * updates the title with the number of records selected
	 * @param {Number} n the number of records selected
	 */
	updateTitle: function(n) {
		var panel = this.getSelectPanel();
		panel.setTitle(n + ' records selected');
	},

	/**
	 * gets the current active control button
	 * Could be one of share, transfer, delete, download, annotate
	 */
	getActiveButton: function() {
		return this.activeButton;
	},

	/**
	 * sets the current active control button
	 * Could be one of share, transfer, delete, download, annotate
	 */
	setActiveButton: function(button) {
		this.activeButton = button;
	},

	/**
	 * toggles between showing and hiding the selection window.
	 * Is called whenever the selection window needs to be shown/hidden
	 */
	onSelectionClick: function() {
		var button = this.getActiveButton();
		var headers = this.getHeaders();
		var win = headers.selectionWindow;
		if(win.isVisible()) {
			var noOfTabs=this.getDynamicPanel().items.length;
			for(var i=1;i<noOfTabs;i++)
				this.getDynamicPanel().remove(i);
			win.hide(button);
		} else {
			var panel = this.getFormPanel();
			if(button.getItemId() === 'headerWorkflowButton') {	
				panel.setWidth(400);
			} else {
				panel.setWidth(300);
			}
			win.show(button);
		}
	},

	/**
	 * get the records selected in the SelectionWindow as an array of guids
	 * @return {Array} an array of selected Record ID's
	 */
	getRecordsSelected: function() {
		var selection = this.getSummaryGrid().getSelectionModel().getSelection();
		var arr = [];
		
		var i;
		for( i = 0; i < selection.length; i++) {
			arr.push(selection[i].get('Record ID'));
		}

		return arr;
	},

	/**
	 * Select the record selected in the image view.
	 */
	onRecordSelect: function() {
		var thumbs = this.getThumbnails();
		var selectedItems = thumbs.getSelectionModel().selected;

		var selectionThumbnails = this.getSelectionThumbnails();
		
		//select the thumbnails
		var selectionModel = selectionThumbnails.getSelectionModel();
		selectionModel.select(selectedItems.items, false, false);
	},

	/**
	 * return the current selected record
	 */
	getCurrentRecordId: function() {
		return this.getRecordControllerController().getCurrentRecordId();
	},

	/**
	 * Based on the text typed in, fill the type form field
	 */
	onAnnotationFieldBeforeQuery: function(queryPlan) {
		var name = queryPlan.query;
		var form = queryPlan.combo.up('form');

		var type = form.items.items[1]; //FIXME
		var text = form.items.items[2]; //FIXME
		var field = this.getUserFieldsStore().getById(name);

		if(field) {
			//set new value field
			this.changeAnnotationValueField(form, form.items.items[2], field.get('type'));

			type.setValue(field.get('type'));
			type.setDisabled(true);

			//the text combo box will be changed
			text = form.items.items[2]; //FIXME
			this.setAnnotationValueField(text , field);
		} else {
			//set new value field
			this.changeAnnotationValueField(form, form.items.items[2], 'Text');

			type.setValue('Text');
			type.setDisabled(false);
			var store=this.getUserFieldValuesStore();
			store.loadData([]);
		}
	},

	/**
	 * based on the Annoation Field change value field
	 */
	onAnnotateFieldSelect: function(combo, records) {
		var field = records[0];
		var form = combo.up('form');
		var type = form.items.items[1]; //FIXME
		var text = form.items.items[2]; //FIXME

		//set new value field
		this.changeAnnotationValueField(form, text, field.get('type'));
		type.setValue(field.get('type'));
		type.setDisabled(true);

		//the text combo box will be changed
		text = form.items.items[2]; //FIXME
		this.setAnnotationValueField(text , field);
	},

	/**
	 * based on the Annotation type fill change value field
	 */
	onAnnotationTypeSelect: function(combo, records) {
		var type = records[0];
		var form = combo.up('form');
		var text = form.items.items[2]; //FIXME

		//set new value field
		this.changeAnnotationValueField(form, text, type.get('name'));
	},

	/**
	 * Sets the annotation value field
	 * @param {Ext.form.field.Text} text the combo box to set the value of
	 * @param {Manage.model.Field} field the field whose value to set
	 */
	setAnnotationValueField: function(text, field) {
		var projectName = this.getActiveProject();
		var store=this.getUserFieldValuesStore();
		
		store.load({
			params : {
				fieldName: field.get('name'),
				fieldType: field.get('type'),
				projectName: projectName
			}});

	},

	/**
	 * Change form value field based on type
	 * @param {Ext.form.Panel} form the form to add the value field to
	 * @param {Ext.form.field.Base} field the current field
	 * @param {String} type the type of the new field
	 */
	changeAnnotationValueField: function(form, field, type) {
		console.log(field.inputType);
		form.remove(field);
		var inputType;
		switch(type) {
			case 'Text':
				inputType = 'text';
				form.add(Ext.create('Ext.form.ComboBox',{
					store : 'UserFieldValues',

					allowBlank : false,
					emptyText: '<EnterNew>',
					fieldLabel : 'Value',
					labelAlign: 'top',

					displayField : 'value',
					valueField : 'value',
					inputType : inputType,
					queryMode : 'local'
				}));
				break;
			case 'Time':
			case 'Date':
				form.add(Ext.create('Ext.form.field.Date', {
					allowBlank : false,
					fieldLabel: 'Value',
					labelAlign: 'top'
				}));
				break;
			case 'Real':
			case 'Integer':
			case 'Number':
				inputType = 'number';
				form.add(Ext.create('Ext.form.ComboBox',{
					store : 'UserFieldValues',

					allowBlank : false,
					emptyText: '<EnterNew>',
					fieldLabel : 'Value',
					labelAlign: 'top',

					displayField : 'value',
					valueField : 'value',
					inputType : inputType,
					queryMode : 'local'
				}));
				break;
			default:
				showErrorMessage(result.responseText, "Type unknown " + type);
				break;
			//TODO add some more
		}
		
		console.log(field.inputType);
	},

	
	/**
	 * Add/Update the annotation for the records selected
	 */
	onWindowSubmitProperty: function(form) {
		var guids = this.getRecordsSelected();

		var name = form.items.items[0].getValue();
		var type = form.items.items[1].getValue();
		var value = form.items.items[2].getValue();

		var field = form.items.items[1];
		if(field.isDisabled()) {
			//field type disabled means the property already existed
			//need to update it
			this.updateProperty(name, type, value, guids);
		} else {
			this.addProperty(name, type, value, guids);
		}
	},

	/**
	 * Add/Update the annotation for the record selected
	 */
	onRecordSubmitProperty: function(form) {
		var record = this.getCurrentRecordId();

		var name = form.items.items[0].getValue();
		var type = form.items.items[1].getValue();
		var value = form.items.items[2].getValue();

		var field = form.items.items[1];
		if(field.isDisabled()) {
			//field type disabled means the property already existed
			//need to update it
			this.updateProperty(name, type, value, [record]);
		} else {
			this.addProperty(name, type, value, [record]);
		}

		this.onAnnotationClose();
	},
	
	/**
	 * show the selection window with profile options
	 */
	onProfileClick: function() {
		//set the title of the window
		this.getSelectionWindow().setTitle('Profile');

		//set the panel to the download form
		//set the panel to the transfer form
		var panel = this.getFormPanel();
		panel.removeAll();
		panel.add(Ext.create('widget.profileChooser', {
				flag: true
			})
		);

		// show the window
		this.setActiveButton(this.getActionsButton()); //set button for animation
		this.onSelectionClick();
	},

	/**
	 * Show the selection window with annoation options
	 */
	onAnnotateClick: function() {
		//set the title of the window
		this.getSelectionWindow().setTitle('Annotate');

		//load user field store for this project
		var userFieldStore = this.getUserFieldsStore();
		var projectName = this.getActiveProject();
		userFieldStore.load({
			params : {
				projectName : projectName
			},
			callback : function(records, operation, success) {
				if (!success) {
					Ext.Msg.alert("Warning", "Unable to load field data from server");
				}
			}
		});

		//set the panel to the download form
		var panel = this.getFormPanel();
		panel.removeAll();
		panel.add(Ext.create('Manage.view.dialogs.FieldEditor'));

		// show the window
		this.setActiveButton(this.getActionsButton()); //set button for animation
		this.onSelectionClick();
	},

	/** 
	 * Load user annotations on the same panel, no pop up
	 */
	onUserAnnotateSelected: function() {
		showConsoleLog('Selections','onAnnotateSelected', 'entry');

		var userFieldStore = this.getUserFieldsStore();
		var projectName = this.getActiveProject();
		userFieldStore.load({
			params : {
				projectName : projectName
			},
			callback : function(records, operation, success) {
				if (!success) {
					Ext.Msg.alert("Warning", "Unable to load field data from server");
				}
			}
		});


		var user = this.getUserAnnotations();
		user.add({
			xtype: 'fieldEditor',
			title: 'Add/Edit Property',
			closable: false
		});

		var layout = this.getUserAnnotations().getLayout();
		layout.setActiveItem(1);
	},
	
	/**
	 * change the user annotations panel
	 */
	onAnnotationClose: function() {
		var panel = this.getUserAnnotations();
		var layout = panel.getLayout();
		layout.setActiveItem(0);

		panel.remove(panel.down('fieldEditor'));
	},


	/**
	* Add a new property
	* @param {Array} records the records to update the property of
	*/
	addProperty : function(name, type, value, records) {
		var me = this;
		if (type == "Time")
			value = value.getTime();
		Ext.Ajax.request({
			method : 'POST',
			url : '../annotation/addAnnotation',
			params : {
				records : Ext.encode(records),
				name : name,
				type : type,
				value : value
			},
			success : function(result, request) {
				// On success
				me.refreshAnnotationStore();
				Ext.Msg.alert("Success","Annotation added successfully");
			},
			failure : function(result, request) {
				showErrorMessage(result.responseText, "Adding annotation "+name+" to server failed");
			}
		});
	},
    
	/**
	 * update the properties of the given records
	 * @param {Array} records the records to update the property of
	 */
	updateProperty : function(name, type, value, records) {
		if (type == "Time")
		{
			value = value.getTime();
		}

		var me = this;
		Ext.Ajax.request({
			method : 'POST',
			url : '../annotation/updateAnnotation',
			params : {
				records : Ext.encode(records),
				name : name,
				type : type,
				value : value
			},
			success : function(result, request) {
				// on success refresh navigator
				me.refreshAnnotationStore();
				Ext.Msg.alert("Success","Annotation updated successfully");
			},
			failure : function(result, request) {
				showErrorMessage(result.responseText, "Updating annotation "+name+" to server failed");
			}
		});
	},

	/**
	 * refresh the annotation store
	 */
	refreshAnnotationStore : function() {
		var recordid = this.getCurrentRecordId();
		
		// refresh annotation store
		var store = this.getUserAnnotationsStore();
		store.load({
			params: {
				recordid: recordid
			}
		});
		
		// refresh project available fields store
		this.getProjectSettingsController().onRefreshProjectFields();
	},

	/**
	 * Show the selection window with the workflow options
	 */
	onWorkflowClick: function() {
						
		//set the title of the window
		this.getSelectionWindow().setTitle('Run Tasks');
		var favPanel=Ext.create('Ext.panel.Panel',{
			title : 'Applications Chooser',

			layout : {
				type : 'vbox',
				align : 'stretch'
			},

			items : [
					{
						xtype : 'favouriteapplicationchooser',
						flex : 1
					} 
					]
		});
		
		//load store for available applications
		var availablestore=this.getAvailableWorkflowStoreStore();
		availablestore.load({params: {projectName: this.projectName}});
		
		//load store for favourite applications
		var selstore=this.getSelectedWorkflowStoreStore();
		selstore.load({params: {projectName: this.projectName}});
		
		this.getDynamicPanel().add(favPanel);	
		
		//set the panel to the workflows 
		var panel = this.getFormPanel();
		panel.removeAll();									
		
		panel.add(Ext.create('Ext.form.Panel', {
			id: 'workflowArea',
			
	        layout: {
	            type: 'vbox',
	            align: 'stretch' 
	        },
			items:[{xtype:'favouriteworkflows'}]
		}));
		
		var favstore=this.getFavouriteWorkflowStoreStore();
		favstore.load({params: {projectName: this.projectName}});

		// show the window
		this.setActiveButton(this.getActionsButton());
		this.onSelectionClick();
	},
	
	onDeleteComment: function(record){
		var projectName = this.getActiveProject();
		var recordid = this.getCurrentRecordId();
		
		var me = this;
		Ext.Ajax.request({
			method : 'POST',
			url : '../record/deleteComments',
			params : {
				commentid: record.data.commentid,
				projectName: projectName,
				recordid: recordid
			},
			success : function(result, request) {
				var deleted=Ext.JSON.decode(result.responseText).deleted;
				if(deleted)
					me.getCommentsStore().reload();
				else
					Ext.Msg.alert("Permission Denied", "You don't have permissions to delete comments");
			},
			failure : function(result, request) {
				showErrorMessage(result.responseText, "Deleting comment failed");
			}
		});
	},
	
	onRefreshImports:function(){
		this.getImportsStore().reload({params: {projectName: this.projectName,status:'pending'}});
		this.getCompletedImportsStore().reload({params: {projectName: this.projectName,status:'complete'}});
	},
	
	onSetParams:function(){
		this.getCompletedImportsStore().getProxy().extraParams={"projectName": this.projectName,"status":'complete'};
		this.getImportsStore().getProxy().extraParams={"projectName": this.projectName,"status":'pending'};
	}
			
		
});
