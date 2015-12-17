/**
 * Handles events related to the summary table
 * For example: Download of archives
 */
Ext.require(['Manage.view.summarytable.SummaryTableColumnChooser',
             'Manage.view.summarytable.ExportDialog','Manage.view.summarytable.ShareDialog',
             'Manage.view.summarytable.FieldEditor', 'Manage.view.summarytable.ProfileChooser']);

Ext.define('Manage.controller.SummaryTableController', {
    extend: 'Ext.app.Controller',

    refs :[{
        ref: 'navigator',
        selector: 'navigator'
    },{ 
        ref: 'summarytable',
        selector:'summarytable'
    }, {
        ref : 'summaryTableColumnChooser',
        selector : 'summaryTableColumnChooser'
    }, {
        ref : 'searchResults',
        selector : 'searchResults'
    }, {
        ref : 'searchPanel',
        selector : 'searchPanel'
    }, {
        ref : 'bookmarkButton',
        selector : 'imagetoolbar #button_bookmark'
    }, {
        ref : 'bookmarks',
        selector : 'bookmarks'
    }],

    stores : ['summarytable.UserFields', 'summarytable.SummaryTableColumnsStore'],

    init: function() {
        this.control({
            "summarytable" : {
                download: this.onDownloadRequest,
                downloadRecords: this.onDownloadRecords,
                showMenu : this.onShowMenu,
                refreshTable : this.refreshSummaryTable,
                refreshColumns : this.refreshColumns,
                deleteRecords : this.onDeleteRecords,
                shareRecords : this.onShareRecords,
                transferRecords : this.onTransferRecords
            }, "fieldEditor" : {
                "fieldChosen": this.onFieldChosen
            }, "fieldValueChooser" : {
                "updateProperty" : this.onUpdateProperty
            },"summaryTableColumnChooser" : {
                "okclicked" : this.onColumnsSelected
            }, "addField" : {
                "addProperty" : this.onAddProperty
            }, 'searchPanel' : {
                "updateSearchResults" : this.onUpdateSearchResults
            }, 'searchResults' : {
                itemclick : this.onSearchResultClick
            }, 'imagetoolbar #button_bookmark' : {
                click : this.onBookmarkButtonClick
            }, 'bookmarks' : {
                refreshBookmarks : this.onRefreshBookmarks,
                itemclick : this.onBookmarkNodeClick,
                addFolder : this.onAddFolder,
                removeFolder:this.onRemoveFolder,
                beforeitemexpand : this.onBookmarkNodeExpand
            },
            'comments' : {
                recordLinkClicked : this.onRecordLinkClicked
            },
            'shareDialog' : {
                transferSuccessful : this.onTransferSuccessful
            },
            'downloads' : {
                itemclick : this.loadRecordsForDownloads
            },
            'bookmarks #button_bookmark1' : {
                click : this.onBookmarkButtonClick
            },
            'projectListing' : {
                addProject : this.onRefreshBookmarks
            },'navigator': {
                itemclick : this.onRefreshBookmarks
            }
        });
    },
    
    onRecordLinkClicked : function(guid) {
    	var _this = this;
    	Ext.Ajax.request({
            method : 'GET',
            url : '../project/getProjectForRecord',
            params : {
                recordid : guid,
                advanceQuery : 1
            },
            success : function (result, request) {
                console.log(result.responseText);
                var map = Ext.decode(result.responseText);
                console.log(map);
                console.log(map.projectName);
                _this.loadRecordsForLinkClick(guid, map.projectName);
            },
            failure : function (result, request) {
                showErrorMessage(result.responseText, "Failed to load record data");
            }
        });
        
    },
    
    /**
     * Load records for the search result
     */
    loadRecordsForLinkClick : function(guid, projectName) {
    	var recordids = [];
    	recordids.push(parseInt(guid));
    	var project = projectName;
        var controller = this;

        Ext.Ajax.request({
            url : '../project/getRecords',
            method : 'get',
            params : {
                projectName : project,
                recordids : Ext.encode(recordids)
            },
            success : function (result, request) {
                if (result.responseText && result.responseText.length > 0) {
                    var records = Ext.JSON.decode(result.responseText);
                    controller.loadDataToTable(records);
                }                   
            },
            failure : function (result, request) {
                showErrorMessage(action.result.responseText, "Failed to laod the records for the link");
            }
        });
    },
   
    /**
     * Reload summary table data from server and if selections present, select the relevant records
     */
    refreshSummaryTable : function(selections) {
        var navigatorSelections = this.getNavigator().getSelectionModel().getSelection();
        // HACK HACK HACK
        if (navigatorSelections.length > 0) {
            var activeNode = this.getNavigator().getSelectionModel().getLastSelected();
            if (activeNode.data.isProjectNode) {
                this.refreshSummaryTableProject(activeNode, selections);
            } else {
                this.refreshSummaryTableBin(activeNode, selections);
            }
            return;
        } 
        var searchSelections = this.getSearchResults().getSelectionModel().getSelection();
        if (searchSelections.length > 0) {
            // From search
            var record = this.getSearchResults().getSelectionModel().getLastSelected();
            this.loadRecordsForSearch(record);
            return;
        }
        var bookmarkSelections = this.getBookmarks().getSelectionModel().getSelection();
        if (bookmarkSelections.length > 0) {
            // From bookmarks 
            var bookmarkNode = this.getBookmarks().getSelectionModel().getLastSelected();
            this.loadRecordsForBookmark(bookmarkNode);
            return;
        }
    },

    /**
     * Reload summary table for a binned folder
     */
    refreshSummaryTableBin : function(node, selections) {
        var projectName = node.data.projectName;
        var conditions = this.getNavigator().getParentPreConditions(node);
        var summarystore = this.getSummarytable().store;
        var controller = this;
        
        var params = {
            projectName : projectName,
            conditions : Ext.encode(conditions),
            fieldName : node.data.fieldName,
            fieldType : node.data.fieldType
        };
        if (node.data.min)
            params["min"] = node.data.min + "";
        if (node.data.max)
            params["max"] = node.data.max + "";

        Ext.Ajax.request({
            method: 'GET',
            url: '../project/getRecordsForBin',
            params: params,
            success: function(result, response) {
                if (result.responseText && result.responseText.length > 0) {
                    var records = Ext.JSON.decode(result.responseText);
                    controller.loadDataToTable(records);
                }
            }
        });

    },

    /**
     * Refresh summary table for the whole project
     */
    refreshSummaryTableProject : function(node, selections) {
        var projectName = node.data.projectName;
        var _this = this;
        Ext.Ajax.request({
            method: 'GET',
            url: '../project/getRecordsForProject',
            params: {
                projectName : projectName
            },
            success: function(result, response) {
                if (result.responseText && result.responseText.length > 0) {
                    var allData = Ext.JSON.decode(result.responseText);
                    _this.loadDataToTable(allData);                    
                }
            }
        });
    },

    /**
     * Load data to the table
     */
    loadDataToTable : function(allData,mode) {
        var records = allData.data;
        var fieldNames = allData.fields;
        var actualCount = allData.count;

        var recordModel = Ext.ModelManager.getModel('Manage.model.Record');
        recordModel.prototype.fields.removeAll();
        
        var fields = new Array();
        for (var i=0; i < fieldNames.length; ++i) {
            var item = fieldNames[i];
            fields.push(Ext.create('Ext.data.Field', {
                name: item
            }));
        }
        
        recordModel.prototype.fields.addAll(fields);
        var proxy = {
            model : 'Manage.model.Record',
            type : 'pagingmemory',
            data : records
        };
        
        var summarystore = this.getSummarytable().store;
        var controller = this;
        summarystore.setProxy(proxy);
        summarystore.load({
            params : {
                start : 0,
                limit : 20
            }, callback : function(records, operation, success) {
                summarystore.loadPage(1);
                var task  = new Ext.util.DelayedTask(function() {
                    controller.getSummarytable().getSelectionModel().select(0);
                });
                task.delay(500);
            }
        });

        var inputOutputColumn=this.getSummarytable().down("gridcolumn[dataIndex='Input/Output']");
        if(mode === 'taskInspection'){
        	inputOutputColumn.show();
        }
        else{
        	inputOutputColumn.hide();
        }

        var pagingToolbar = Ext.ComponentQuery.query('summarytable pagingtoolbar')[0];
        var columns = this.getSummarytable().columns;
        if (actualCount !== records.length) {
            // The records have been limited. Disable sorting on the summary table
            // and display information that records have been limited
            pagingToolbar.displayMsg = "Displaying records {0} - {1} of {2}";
            _.each(columns, function(item) {
            	if (!item.dontSort)
                	item.sortable = true;
            });
        } else {
            pagingToolbar.displayMsg = "Displaying records {0} - {1} of {2}";
            _.each(columns, function(item) {
                if (!item.dontSort)
                    item.sortable = true;
            });
        }

    },
    /**
     * Show menu for manipulating summary table
     */
    onShowMenu : function(header) {
        // If summary table is empty, menu is not shown
        if (this.getSummarytable().store.getCount() === 0) {
            Ext.Msg.alert("Warning", "Summary table empty. Choose a project first");
            return;
        }
        var menu = Ext.create('Ext.menu.Menu', {
            items: [this.getChooseColumnsMenuItem(), this.getAddPropertyMenuItem(), this.getAddProfileMenuItem()]
        });
        console.log(menu);
        menu.showBy(header); 
    },

    getChooseColumnsMenuItem : function() {
        var _this = this;
        return  {
            text: 'Choose Columns',
            handler : function() {
                _this.refreshColumns(null, false, function() {
                    Ext.create('Ext.window.Window', {
                        title : 'Choose Columns',
                        layout : 'fit',
                        width : 500,
                        height : 500,
                        items : [{
                            xtype : 'summaryTableColumnChooser'
                        }]  
                    }).show();
                });
            }
        };
    },    

    getAddPropertyMenuItem : function() {
        var userFieldStore = this.getSummarytableUserFieldsStore();
        var projectName = this.getActiveProject();
        return {
            text: 'Add Property',
            handler : function() {
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
                Ext.create('Ext.window.Window', {
                    title : 'Add/Edit Property',
                    width : 300,
                    items : [{
                        xtype : 'fieldEditor'
                    }]
                }).show();
                console.log("add property");
            }
        };
    },
    
    getAddProfileMenuItem : function() {
    	var selected = this.getSummarytable().getRecordIDs();
    	if (selected === null || selected.length <= 0) {
            return;
        }
        return {
            text: 'Apply Profile',
            handler : function() {
                Ext.create('Ext.window.Window', {
                    title : 'Apply Profile',
                    width : 500,
                    items : [{
                        xtype : 'profileChooser',
                		guids : Ext.encode(selected)
                    }]
                }).show();
                console.log("apply profile");
            }
        };
    },

    /**
     * Process download request
     */
    onDownloadRequest : function(recordid) {
        console.log("On download request: "+ recordid);
        var projectName = this.getActiveProject();

        // Panel where progress gif will be shown, which later gets converted to download url
        var centerPanel = Ext.create('Ext.panel.Panel', {
            layout : 'fit',
            width : 400,
            height : 40,
            border : false,
            html : '<img width=400 height=15 src="images/progress.gif"/>' 
        });
    
        // Progress window, which will hold the gif/download link
        var progressWindow = Ext.create('Ext.window.Window', {
            title : 'Processing download',
            layout : 'fit',
            width : 500,
            height : 120,
            items : [{
                xtype : 'panel',
                layout : {
                    type : 'hbox',
                    pack : 'center',
                    align : 'middle'
                },
                items : [centerPanel]
            }],
            buttons : [
                {
                    text : 'Close',
                    handler : function() {
                        console.log("Cancel download");
                        this.up('window').close();
                    }
                }
            ]
        });
    
        // Finally ajax call get start preparing download
        // When the call returns, the centerPanel is updated with download link
        Ext.Ajax.request( {
            method : 'GET',
            url : '../project/prepareArchive',
            timeout: 300000,
            params : {
                projectName : projectName,
                recordid : recordid
            },
            success : function (result, request) {
                var fileData = Ext.decode(result.responseText);
                var downloadMessage = 'Download ' + fileData.name + ' ( ' + Ext.util.Format.fileSize(fileData.size) + ' )';
                centerPanel.update('<a href="../project/getArchive?recordid=' + recordid + '">' + downloadMessage + '</a>');
            },
            failure : function (result, request) {
                progressWindow.close();
                showErrorMessage(action.result.responseText, 'Failed to download archive. Please try again later.');
            }
        });
        
        // All set. Show the window
        progressWindow.show(); 
    },
    
    /**
     * show download form
     */
    onDownloadRecords : function(table) {
    	var selected = table.getRecordIDs();
        if (selected === null || selected.length <= 0) {
            return;
        }
        var _this = this;
        
        console.log("launching download dialog");
        
        // show description dialog here
        var win = Ext.create ('Ext.window.Window', {
            height : 170,
            title : 'Create Download Link',
            width : 430,
            items : [{
                xtype : 'exportDialog',
                guids : Ext.encode(selected)
            }]
        });
        win.show();
        
        _this.doDownloadRecords(table, selected);
    },
    
    /**
     * Process download request
     */
    doDownloadRecords : function(table, selected) {
    	console.log("processing download request");
    	console.log(Ext.encode(selected));
    },
    
    /**
     * Based on the selected value of column, add appropriate view to the parentPanel
     */
    onFieldChosen : function(selectedValue, parentPanel){
        // Check if the column is an existing column
        var userFieldStore = this.getSummarytableUserFieldsStore();
        var storeValue = userFieldStore.getById(selectedValue);
        if (storeValue !== null) {
            this.showOldColumnView(storeValue, selectedValue, parentPanel);
        } else {
            this.showNewColumnView(selectedValue, parentPanel);
        }
    },

    /**
     * If the selectedValue is one of existing columns, then a simple type-validated
     * input field with combobox is presented
     */
    showOldColumnView : function(storeValue, selectedValue, parentPanel) {
        var type = storeValue.data.type;
        var name = selectedValue;
        var projectName = this.getActiveProject();

        Ext.Ajax.request({
            method : 'GET',
            url : '../annotation/getFieldValues',
            params : {
                fieldName : name,
                fieldType : type,
                projectName : projectName
            },
            success : function (result, request) {
                var response = Ext.decode(result.responseText);
                if (response && response !== null) {
                     parentPanel.add({
                        xtype : 'fieldValueChooser',
                        fieldType : type,
                        fieldName : name,
                        fieldData : response 
                    });
                }
            },
            failure : function (result, request) {
                showErrorMessage(action.result.responseText, "Failed to get values for the field: " + fieldName);
            }
        });
    },

    /**
     * If the selectedValue is a new column, type and value fields are presented
     */
    showNewColumnView : function(selectedValue, parentPanel) {
        parentPanel.add({
            xtype : 'addField',
            fieldName : selectedValue
        }); 
    },
    
    /**
     * Refresh columns of the summary table with the given name (optional). 
     * @projectName name of project to refresh. default - active project
     * @refreshTable indicates if the table should be refreshed or not (default: true). 
     * @callback optional callback to call if refresh is successful
     */
    refreshColumns : function(projectName, refreshTable, callback) {
        var columnStore = this.getSummarytableSummaryTableColumnsStoreStore();
        if (!projectName || (projectName === null))
            projectName = this.getActiveProject();
        if ((refreshTable === undefined) || (refreshTable === null))
            refreshTable = true;
        var controller = this;
        var summaryTable = this.getSummarytable();
        var loadParams = {
            params: {
                projectName : projectName
            },
            callback : function(records, operation, success) {
                if (success && refreshTable) {
                    if (records !== null && records.length == 1){
                        controller.onColumnsSelected(null, records[0].data.user, records[0].data.available);
                    }
                    summaryTable.fireEvent('refreshTable');
                }
                if (success && callback && callback !== null)
                    callback();
                if (!success)
                    Ext.Msg.alert("Warning", "Unable to load field data from server");
            }
        };
        columnStore.load(loadParams); 
    },

    /**
     * Called after the user has made a choice on what columns to show in summary table
     */
    onColumnsSelected : function(view, selected, available) {
        var projectName = this.getActiveProject();
        var chosenFields = new Array();
        var i;
        
        var table = this.getSummarytable();
        for (i=0; i < selected.length; ++i ) {
            chosenFields.push(selected[i].data.name);
        } 
        // Write to server
        Ext.Ajax.request({
            method : 'POST',
            url : '../annotation/setFieldsChosen',
            params : {
                projectName : projectName,
                chosenFields : Ext.encode(chosenFields)
            },
            success : function(result, req) {
            // Reset node for project
               table.fireEvent('refreshTable');
            },
            failure : function(result, req) {
                showErrorMessage(action.result.responseText, "Navigator fields saving to server failed");
            }
        });
         
        var itemsToRemove = new Array();
        for (i=0; i < table.headerCt.getColumnCount()-2; ++i)
            itemsToRemove.push(table.headerCt.getComponent(i));
        for (i=0; i< itemsToRemove.length; ++i)
            table.headerCt.remove(itemsToRemove[i]); 
        for (i=0; i < selected.length ; ++i) {
            var next = selected[i].data;
            table.headerCt.insert(table.headerCt.getColumnCount()-2, Ext.create('Ext.grid.column.Column', {text: next.name, dataIndex : next.name, flex : 1, menuDisabled : true}));
        }
        table.getView().refresh();
        
    },

    /**
     * Add a new property
     */
    onAddProperty : function(name, type, value) {
        // Post data to server. If successful update summary table
        var records = this.getSelectedRecords();
        var table = this.getSummarytable();
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
                table.fireEvent('refreshColumns', null);       
            },
            failure : function(result, request) {
                showErrorMessage(action.result.responseText, "Adding annotation "+name+" to server failed");
            }
        });
    },

    /**
     * Update an existing property
     */
    onUpdateProperty : function(name, type, value) {
        // Post data to server. If successful update summary table
        var records = this.getSelectedRecords();
        var table = this.getSummarytable();
        if (type == "Time")
            value = value.getTime();
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
                table.fireEvent('refreshColumns', null);
            },
            failure : function(result, request) {
                showErrorMessage(action.result.responseText, "Updating annotation "+name+" to server failed");
            }
        });

    },

    /**
     * Utility to get the record id of all selected records
     */
    getSelectedRecords : function() {
        var ret = new Array(); 
        var selected = this.getSummarytable().getSelectionModel().getSelection();
        if (selected !== null && selected.length > 0) {
            for (var i=0; i<selected.length; ++i) {
                ret.push(selected[i].data["Record ID"]);
            }
        }
        return ret;
    },
    
    getSelectedRecordsData : function() {
        var ret = new Array(); 
        var selected = this.getSummarytable().getSelectionModel().getSelection();
        if (selected !== null && selected.length > 0) {
            for (var i=0; i<selected.length; ++i) {
                ret.push(selected[i]);
            }
        }
        return ret;
    },

    /**
     * Update the search results tree with the results found
     */
    onUpdateSearchResults : function(results) {
        var searchTree = this.getSearchResults();
        var rootNode = searchTree.getRootNode();
        rootNode.removeAll();
        if (results && results !== null) {
            for (var key in results) {
                rootNode.appendChild(this.constructSearchNode(key, results[key]));    
            }
        }
    },

    /**
     * Construct search result node
     */
    constructSearchNode : function(projectName, records) {
        var projectNode = {"leaf":true};
        projectNode["text"] = projectName + " ("+records.length+")";
        projectNode["projectName"] = projectName;
        projectNode["recordids"] = records;
        return projectNode;
    },

    /**
     * Load the summary table with results from the selected project node
     */
    onSearchResultClick : function (view, record, item, index, e, eOpts) {
        var projectName = record.data.projectName;
        this.getNavigator().getSelectionModel().deselectAll();
        this.setActiveProject(projectName);
        this.refreshColumns();
    },
    
    /**
     * Load records for the search result
     */
    loadRecordsForSearch : function(record) {
        var projectName = record.data.projectName;
        var recordids = record.data.recordids;
        var controller = this;

        Ext.Ajax.request({
            url : '../project/getRecords',
            method : 'get',
            params : {
                projectName : projectName,
                recordids : Ext.encode(recordids)
            },
            success : function (result, request) {
                if (result.responseText && result.responseText.length > 0) {
                    var records = Ext.JSON.decode(result.responseText);
                    controller.loadDataToTable(records);
                }                   
            },
            failure : function (result, request) {
                showErrorMessage(action.result.responseText, "Failed to laod the records for the search");
            }
        });
    },
    
    /**
     * Load records for the search result
     */
    loadRecordsForDownloads : function(record) {
    	var recordids = record.data.input_guids;
    	var project = record.data.project;
        var controller = this;

        Ext.Ajax.request({
            url : '../project/getRecords',
            method : 'get',
            params : {
                projectName : project,
                recordids : Ext.encode(recordids)
            },
            success : function (result, request) {
                if (result.responseText && result.responseText.length > 0) {
                    var records = Ext.JSON.decode(result.responseText);
                    controller.loadDataToTable(records);
                }                   
            },
            failure : function (result, request) {
                showErrorMessage(action.result.responseText, "Failed to laod the records for the download");
            }
        });
    },

    /**
     * Get the current active project
     */
    getActiveProject : function() {
        return this.getController('Manage.controller.NavigatorState').activeProject;
    },

    /**
     * Set active project to the given project name
     */
    setActiveProject : function(projectName) {
        this.getController('Manage.controller.NavigatorState').activeProject = projectName;
    },

    /**
     * Get the current active record id
     */
    getActiveRecordID : function() {
        return this.getController('Manage.controller.RecordSelection').activeRecordID;
    },

    /**
     * Refresh the bookmark tree with fresh data from server
     */
    onRefreshBookmarks : function() {
        var _this = this;
        var store = this.getBookmarks().store;
        
        store.getRootNode().data.text = this.getActiveProject() + " ["+store.getRootNode().data.recordCount+"]";
        store.getRootNode().data.bookmarkName = this.getActiveProject();
        
        console.log("on refresh bookmarks");
        console.log(store.getRootNode().data.text);
        console.log(store.getRootNode().data.recordCount);
        
        this.loadFormTree(this.getBookmarks());
        
        Ext.Ajax.request({
            url : '../project/getBookmarkFolders',
            method : 'get',
            params : {
        		projectName: _this.getActiveProject(),
        		bookmarkPath: _this.getActiveProject()
            },
            success : function (result, request) {
                var resp = Ext.JSON.decode(result.responseText);
                if(resp[0] != null)
                {
                	store.getRootNode().data.recordCount = resp[0].recordCount;
                    store.getRootNode().data.text = _this.getActiveProject() + " ["+store.getRootNode().data.recordCount+"]";
                }
                _this.addBookmarkNodes(resp);
            },
            failure : function (result, request) {
                showErrorMessage(action.result.responseText, "Failed to laod the records for the search");
            }
        });
    },

    /**
     * Add bookmark nodes
     */
    addBookmarkNodes : function(nodeData) {
    	var path=null;
    	var bookmarkTree = this.getBookmarks();
    	
    	var page =this;
    	if(nodeData[0]!=null)
    	{
    		page.treeJson = nodeData[0].children;
        	this.loadTree(bookmarkTree,page.treeJson);
    	}
    	
    	var bookmarkFormTree = Ext.getCmp("bookmarkFormTree");
    	if(bookmarkFormTree!=undefined && bookmarkFormTree!=null)
    	{
    		this.loadFormTree(bookmarkFormTree);
    	}

    },
    
    appendBookmarkNodes : function(rootNode, nodeData) {
    	var path=null;
    	var bookmarkTree = this.getBookmarks();
    	var page =this;
    	if(nodeData[0]!=null)
    	{
    		page.treeJson = nodeData[0].children;
    		rootNode.removeAll();
        	rootNode.appendChild(page.treeJson);
    	}
    },

    /**
     * Load records from bookmark node
     */
    loadRecordsForBookmark : function(node) {
        var projectName = this.getActiveProject();
        var selectedPath = this.getPath(node, "text");
        var isLeaf = node.isLeaf();
        var controller = this;
        Ext.Ajax.request({
          url : '../project/getRecordsForBookmarkFolder',
          method : 'get',
          params : {
              projectName : projectName,
              bookmarkPath : selectedPath,
              isLeaf : isLeaf
          },
            success : function (result, request) 
            {
                if (result.responseText && result.responseText.length > 0) 
                {
                    var records = Ext.JSON.decode(result.responseText);
                    controller.loadDataToTable(records);
                }                   
            },
            failure : function (result, request) 
            {
                showErrorMessage(action.result.responseText, "Failed to load the records for the bookmark node");
            }
        });
    },
    
    /**
     * on bookmark node expand
     */
    onBookmarkNodeExpand : function(node, eOpts) 
    {
    	var _this = this;
        
        Ext.Ajax.request({
            url : '../project/getBookmarkFolders',
            method : 'get',
            params : {
        		projectName: _this.getActiveProject(),
        		bookmarkPath: _this.getPath(node,"text")
            },
            success : function (result, request) {
                var resp = Ext.JSON.decode(result.responseText);
                _this.appendBookmarkNodes(node, resp);
            },
            failure : function (result, request) {
                showErrorMessage(action.result.responseText, "Failed to laod the records for the search");
            }
        });
    },

    /**
     * When bookmark node is clicked
     */
    onBookmarkNodeClick : function(view, record, item, index, e, eOpts) {
    	var selectedNode = this.getBookmarks().getSelectionModel().getLastSelected();
    	
    	if(selectedNode.data.recordCount == 0)
    	{
    		Ext.Msg.alert("Failed", "no records to display");
    		this.getBookmarks().getSelectionModel().deselectAll(false);
    		return;
    	}
    	
    	this.loadRecordsForBookmark(selectedNode)
    },

    /**
     * Delete records selected in the summary table with confirmation.
     */
    onDeleteRecords : function(table) {
        var selected = table.getRecordIDs();
        if (selected === null || selected.length <= 0) {
            return;
        }
        var _this = this;
        Ext.Msg.confirm("Delete", "Are you sure you want to delete the selected " + selected.length + " records?", function(id)  {
            if (id === "yes") {
                _this.doDeleteRecords(table, selected);
            }
        });
    },
    
    /**
     * share records selected in the summary table with confirmation.
     */
    onShareRecords : function(table) {
    
    	var selected = table.getRecordIDs();
        if (selected === null || selected.length <= 0) {
            return;
        }
        var _this = this;
        
        console.log("on share records");
        
        // show description dialog here
        var win = Ext.create ('Ext.window.Window', {
            height : 70,
            title : 'Share Records',
            width : 350,
            items : [{
                xtype : 'shareDialog',
                guids : Ext.encode(selected),
                flag : false
            }]
        });
        win.show();
    },
    
    onTransferSuccessful : function(table) {
    	console.log("transfer successful");
    	this.getController('Manage.controller.NavigatorState').onRefreshProjects();
    },
    
    /**
     * transfer records selected in the summary table with confirmation.
     */
    onTransferRecords : function(table) {
    
    	var selected = table.getRecordIDs();
        if (selected === null || selected.length <= 0) {
            return;
        }
        var _this = this;
        var win = Ext.create ('Ext.window.Window', {
        	           height : 70,
        	           title : 'Transfer Records',
        	           width : 350,
        	           items : [{
        	        	   xtype : 'shareDialog',
        	        	   guids : Ext.encode(selected),
        	        	   flag : true
        	           			}]
        });
        Ext.Msg.confirm("Transfer", "Are you sure you want to transfer the selected " + selected.length + " records?", function(id)  {
            if (id === "yes") {
                console.log("on transfer records");
		        win.show();     
            }
        });
        console.log("on transfer records");
    },

    // Actual record deletion action after confirmation    
    doDeleteRecords : function(table, selected) {
        var _this = this;
        Ext.Ajax.request({
            method : 'POST',
            url : '../admin/deleteRecords',
            params : {
                recordids : Ext.encode(selected)
            },
            success : function (result, response) {
                Ext.Msg.alert("Success", "Deleted " + selected.length + " records");
                // Reload
                _this.refreshSummaryTable();
            },
            failure : function (result, response) {
                showErrorMessage(result.responseText, "Failed to delete records"); 
            }
        });
    },
    /**
     *  Adds folder to the selected node in the tree
     *  bookmarkFormTree = true for if the add folder is called from Bookmark Form
     *  bookmarkFormTree = false if the add folder is called form left side bookmark tree
     */
    onAddFolder:function(bookmarkFormTree){
    	var page =this;
    	var node;
        if(bookmarkFormTree == false){
        	node = this.getBookmarks().getSelectionModel().getLastSelected();
        }
        else{
        	var tree = Ext.getCmp("bookmarkFormTree");
        	node = tree.getSelectionModel().getLastSelected();
        	console.log(node);
        }
        page.currentNode = node;
        if(node==null || node.isLeaf()){
   		 Ext.Msg.alert("Error", "Please select some Folder to add Folder to it");
        }
        else{
        	page.showAddFolderForm(bookmarkFormTree);
        }
    	
    	
    },
    /*
     * adds folder
     */
    addChildFolder:function(childFolderName){
    	var page =this;
    	Ext.Ajax.request({
    		method : 'POST',
    		url : '../project/addBookmarkFolder',
    		params : {
    			path:page.getPath(page.currentNode,"text"),
    			folderName:childFolderName,
    			projectName:page.getActiveProject()
    		},
    		success : function (result, response) {
    			var resp = Ext.JSON.decode(result.responseText);
                if(resp.success==true){
             	   Ext.Msg.alert("Success",resp.message );
             	   window.close();
             	   page.onRefreshBookmarks();
                }
                else{
             	   Ext.Msg.alert("Failure",resp.message );
                }
    		},
    		failure : function (result, response) {
    			showErrorMessage(result.responseText, "Failed to add Folder"); 
    		}
    	});
    	
    	
    },
  /**
   *  folder Creation Form
   */
    showAddFolderForm:function(bookmarkFormTree){
    	var page =this;
        Ext.create('Ext.window.Window', {
            title : 'Add Folder',
            height : 120,
            width : 300,
            
            items : [
                
                     {
                xtype : 'form',
                url : '../project/addFolder',
                bodyPadding : 10,
                items : [{
                    xtype : 'textfield',
                    fieldLabel : 'Folder Name',
                    name : 'folderName',
                    allowBlank : false
                }],
             
                buttons : [{
                    text : 'OK',
                    formbind : true,
                    handler : function() {
                        var form = this.up('form').getForm(); 
                        var _this = this;
                        if (form.isValid()) {
                            
                        	var name =form.findField("folderName").getValue();
                        	page.addChildFolder(name);
                        	this.up().up().up().close();
                        }
                    }
                }, {
                    text : 'Cancel',
                    handler : function() {
                        this.up().up().up().close();
                    }
                }]
            }
            ]

        }).show();
    },
    /**
     *  remove selected folder or Record<leaf> from tree
     *  bookmarkFormTree = true for if the remove folder is called from Bookmark Form
     *  bookmarkFormTree = false if the remove folder is called form left side bookmark tree
     */
    onRemoveFolder:function(bookmarkFormTree){
    	var page= this;
    	if(bookmarkFormTree == false){
        	node = this.getBookmarks().getSelectionModel().getLastSelected();
        }
        else{
        	var tree = Ext.getCmp("bookmarkFormTree");
        	node = tree.getSelectionModel().getLastSelected();
        }
   
    	
    	if(node==null || node==undefined){
    	
    		Ext.Msg.alert("Error", "Please select some folder or Record to Delete it");
    	}else{
    		var path;
    		var url;
    		var params={};
    		if(node.isLeaf()){
    			path = page.getPath(node.parentNode,"text");
    			url = "../project/removeBookmark";
    			params.path = path;
    			params.recordid = node.get("recordId");
    			page.currentNode=node.parentNode;
    			
    		}else{
    			path = page.getPath(node,"text");
    			url = "../project/removeBookmarkFolder";
    			params.path = path;
    			params.projectName = this.getActiveProject();
    			page.currentNode=node;
    		}
    		Ext.Ajax.request({
    			method : 'POST',
    			url : url,
    			params : params,
    			success : function (result, response) {
    				Ext.Msg.alert("Success", "Folder '"+node.get("text")+"' deleted Successfully");
    				//Reload
    				page.onRefreshBookmarks();
    				
    			},
    			failure : function (result, response) {
    				showErrorMessage(result.responseText, "Failed to remove Folder"); 
    			}
    		});
    	}

    
    },
    /**
     * retuns the complete path of node in the tree
     */
    getPath:function(node , field){
    	var path = [];
    	while (node) {
    		path.push(node.get("bookmarkName"));
    		node = node.parentNode;
    		}
    		path.reverse();
    		return "/"+ path.join('/');
    	
    },
    /**
     * creates bookmark FormTree
     */
    createTree:function(){
    	var _this = this;
    	var treeStore = Ext.create('Manage.store.BookmarkStore');
    	treeStore.getRootNode().data.text = this.getActiveProject();
    	treeStore.getRootNode().data.bookmarkName = this.getActiveProject();
    	return Ext.create('Ext.tree.Panel', {
    		title: 'Choose Folder',
    		bodyPadding: 5,
    		height:270,
    		margin:5,
    		frame:true,
    		id: 'bookmarkFormTree',
    		store:treeStore,
    		rootVisible: true,
    		viewConfig: {
    			plugins: {
    				ptype: 'treeviewdragdrop'
    			}
    		},
    		listeners: {
    			beforeitemexpand : {
    				fn : _this.onBookmarkNodeExpand,
    				scope : _this
    			} 
    	    }
    	}
    	);
    },
    /**
     * create bookmark Form
     */
    createForm:function(){
    	var form = 	Ext.create('Ext.form.Panel', {
    		title: 'BookMark Name',
    		bodyPadding: 10,
    		frame:true,
    		margin:5,
    		url: '',
    		layout:'auto'
    	});
    	return form;
    },
    /**
     *  Form that comes when you choose new folder in the bookmarks form
     */
    showbookMarkWindow:function(){
    	var page =this;
    	var tree = page.createTree();
    	page.loadFormTree(tree);
    	var bookMarkWindow = Ext.create('Ext.window.Window', {
    		title : ' BookMark',
    		layout : 'fit',
    		height : 400,
    		width :400,

    		items : [{
    			xtype : 'panel',
    			layout : {
    				type : 'vbox',
    				pack : 'start',
    				align : 'stretch'
    			},
    			items : [tree]
    		}],
    		buttons: [{
    			text:'Add Folder',
    			handler:function(){
    				page.onAddFolder(true);
    			}
    		},
    		{
    			text:'Delete',
    			handler:function(){
    				page.onRemoveFolder(true);
    			}
    		},

    		{
    			text: 'Cancel',
    			handler: function() {
    				this.up('window').close();
    			}
    		}, {
    			text: 'Submit',

    			handler: function() {
    				var path;
					var tree = Ext.getCmp("bookmarkFormTree");
					var node = tree.getSelectionModel().getLastSelected();
					if(node==null || node==undefined){
						Ext.Msg.alert("Error", "Please select some folder ");
					}
					else{
						if(node.isLeaf()){
							path= page.getPath(node.parentNode,"text");
						}
						else{
							path=page.getPath(node,"text");
						}
						page.AddBookmark(path,this.up('window'));

					}
    			}
    		}]

    	});
    	bookMarkWindow.show();
    },
    
    /**
     * Loads the json in the tree
     */
    loadTree:function(tree,json){
    	var page =this;
    	var bookmarkTree = tree;
    	var rootNode = bookmarkTree.getRootNode();
    	rootNode.removeAll();
    	rootNode.appendChild(json);
    	var path=null;
    	if(page.currentNode!=undefined || page.currentNode!=null)
    	{
    		path = page.getPath(page.currentNode,"text");
    	}
    	
//    	page.expandTree(tree,path);
    },
    /**
     * load bookmark form tree using json getting from Bookmark Tree
     */
    loadFormTree:function(bookmarkFormTree){
    	var bookmarkTree = this.getBookmarks();
    	var rootNode = bookmarkTree.getRootNode();
    	var json = this.getJson(rootNode);
    	var root = bookmarkFormTree.getRootNode();
    	bookmarkFormTree.setRootNode(json);
//    	bookmarkFormTree.expandAll();

   },
    expandTree:function(tree,path){
    	if(path==null){
    		tree.expandAll();
    	}
    	else{
    		tree.expandPath(path);
    	}
    	tree.expandAll();
    },
    onBookmarkButtonClick:function(){
    	this.showbookMarkform();
    },
    showbookMarkform:function(){
    	var folderComboBox = this.createfolderComboBox();
    	var recordid = this.getActiveRecordID();
    	var page = this;
    	var bookmarkButton = this.getBookmarkButton();
    	Ext.create('Ext.window.Window', {
    		title : 'BookMark',
    		id:'bookmarkWindow',
    		layout:'fit',
    		width : 350,

    		items : [{
    			xtype : 'form',

    			url : '',
    			bodyPadding : 10,
    			items : [folderComboBox],

    			buttons : [{
    				text : 'OK',
    				formbind : true,
    				handler : function() {
    					var form = this.up('form').getForm(); 
    					var _this = this;
    					if (form.isValid()) {

    						var path = Ext.getCmp("folders").getValue();
    						var window = this.up("window");
    						if(path!="New"){
    							page.AddBookmark(path,window);
    						}

    					}
    				}
    			}, {
    				text : 'Cancel',
    				handler : function() {
    					this.up().up().up().close();
    					bookmarkButton.toggle();
    				}
    			}]
    		}]

    	}).show();
    },
    /**
     *  combobox for showing recent bookmark Fields
     */
    createfolderComboBox:function(){
    	var page=this;
    	var store=Ext.create('Ext.data.Store', {
    		fields:['folderName','folderPath'],

    		proxy : {
    			type : 'ajax',
    			url : '../project/getRecentFolders',

    			reader: {
    				type: 'json',
    				root: 'items'
    			},
    			
    			extraParams: {
    				projectName: page.getActiveProject()
    			}
    		},
    		listeners : {
    			exception : function(proxy, response,operation) {
    				alert('Failure',response.responseText);
    			}
    		}
    	});

    	var combobox=Ext.create('Ext.form.field.ComboBox',{
    		id:'folders',
    		fieldLabel: 'Folder',
    		margin:5,
    		name: 'Choose Folder',
    		width:300,
    		displayField: 'folderName',
    		valueField: 'folderPath',
    		emptyText: 'Select Folder',
    		store: store,
    		mode: 'remote',
    		typeAhead: true,
    		allowBlank: false,
    		msgTarget: 'under',
    		
    		listeners:{
    			'select':function(combo,selected){
    				if(combo.getValue()=="New"){
    					page.showbookMarkWindow();
    					Ext.getCmp("bookmarkWindow").close();
    				}
    			}
    		}
    	});


    	return combobox;
    },
    /**
     * this function adds the bookmark with name and given path
     */
    AddBookmark:function(path,window){
    	var recordid = this.getActiveRecordID();
    	var page =this;
    	Ext.Ajax.request({
            url : '../project/addBookmark',
            method : 'POST',
            params : {
                 path: path,
                 recordid:recordid
            },
            success : function (result, request) {
               var resp = Ext.JSON.decode(result.responseText);
               if(resp.success==true){
            	   Ext.Msg.alert("Success",resp.message );
            	   window.close();
            	   page.onRefreshBookmarks();
               }
               else{
            	   Ext.Msg.alert("Failure",resp.message );
               }
               
            },
            failure : function (result, request) {
                showErrorMessage(action.result.responseText, "Failed to laod the records for the search");
            }
        });
    },
    
    
    
    getJson:function(node){
    	var page =this;
    	var json = {};
    	json.text = node.get("text");
    	json.leaf = node.isLeaf();
    	json.bookmarkName = node.get("bookmarkName");
    	if(json.leaf==true){
    		json.projectName = node.get("projectName");
    		json.recordId = node.get("recordId");
    	}
    	if(node.childNodes!=undefined){
    		json.children=[];
    		for (var i=0; i < node.childNodes.length; i++) {
    			json.children.push(page.getJson(node.childNodes[i]) );
    		}
    	}

    	return json;
    }
   
    
});
