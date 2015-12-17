/**
 * Controls events from the navigator
 * 1. double click on a node gets siblings of the node
 * Note: fetching children logic is done automatically by treeStore (NavigatorStore.js)
 */
Ext.define('Manage.controller.NavigatorState', {
    extend: 'Ext.app.Controller',
    
   refs: [{
        ref: 'navigator',
        selector: 'navigator'
    }, {
        ref : 'projectListing',
        selector : 'projectListing'
    }, {
        ref : 'recentProjects',
        selector  : 'recentProjects'
    }, {
        ref : 'allProjects',
        selector : 'allProjects'
    }, {
        ref : 'summaryTable',
        selector : 'summarytable'
    }, {
        ref: 'thumbnails',
        selector: 'thumbnails'
    }],

    stores : ['summarytable.SummaryTableFields', 'settings.FieldStore'],

    init: function() {
        // listen to the change in navigator node selection ..
        this.control({
            'navigator': {
                beforeitemdblclick : this.onNodeDoubleClick,
                fieldSelector : this.onFieldSelector,
                projectlisting : this.onProjectListing,
                fieldsChanged : this.onFieldsChange,
                beforeitemexpand : this.onNodeExpand,
                itemclick : this.onNodeClick,
                refreshProjects : this.onRefreshProjects
            },
            'projectListing' : {
                addProject : this.onAddProject
            }
        });
    },
    
    /**
     * Based on what node is clicked on the navigator, populate the thumbnails and summarytables with records
     */
    onNodeClick: function(view, record, item, index, e, eOpts) {
        //if (this.activeRecord === record)
        //    return;
        this.activeRecord = record;

        // If the project has changed, then column choosers need to be refreshed
        var projectName = record.data.projectName;
        if (this.activeProject !== projectName){
            this.activeProject = projectName;
            this.refreshNavigatorFieldsStore(projectName);
            // Set columns for summary table for this project 
            this.getSummaryTable().fireEvent('refreshColumns', projectName);
        } else {
            this.getSummaryTable().fireEvent('refreshTable');
        }
    },

    refreshNavigatorFieldsStore : function (projectName) {
        var fieldStore = this.getSettingsFieldStoreStore();
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
     * Node expand is done manually
     */
    onNodeExpand : function(node, opts) {
        console.log("before expand: "+ node);
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
                projectName : node.data.projectName,
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
        for (var i=0; i < childNodes.length ; ++i) {
            var nextNode = this.constructNode(childNodes[i], parentNode.data.projectID, parentNode.data.projectName);
            lastAddedNode = parentNode.appendChild(nextNode);
        }
    },

    onAddProject : function(projectID, projectName, recordCount) {
        var navigator = this.getNavigator();
        var rootNode = navigator.store.getRootNode();
        var projectNode = this.constructProjectNode(projectID, projectName, recordCount);
        var addedNode = rootNode.appendChild(projectNode);
        navigator.getSelectionModel().select(addedNode);
        navigator.fireEvent("itemclick", navigator, navigator.getSelectionModel().getLastSelected());
        this.getAllProjects().up().loadedProjects = this.getLoadedProjects();
        Ext.Ajax.request({
            method : 'POST',
            url : '../project/projectAccessed',
            params : {
                projectName : projectName
            },
            success : function(result, req) {
            // Reset node for project
                
            },
            failure : function(result, req) {
                showErrorMessage(result.responseText, "Saving project selection to server failed");
            }
   
        });
    },

    constructProjectNode : function (projectID, projectName, recordCount) {
        var projectNode = {"cls" : "folder", "iconCls":"folder","binned":false,"leaf":false, min:"", max:""};
        projectNode["text"] = projectName + " [" + recordCount +"]";
        projectNode["projectName"] = projectName;
        projectNode["projectID"] = projectID;
        projectNode["id"] = '["' + projectID + '"]';
        projectNode["isProjectNode"] = true;
        return projectNode;
    },

    onProjectListing : function() {
        // Reload the stores to get the latest information from server
        var recentStore = Ext.data.StoreManager.get('Manage.store.RecentProjectStore');
        recentStore.load();
        var allStore = Ext.data.StoreManager.get('Manage.store.ProjectStore');
        allStore.load();
        var loadedProjects = this.getLoadedProjects();
        // Create view now
        Ext.create('Ext.window.Window', {
            title : 'Choose Project',
            layout : 'fit',
            width : 600,
            height : 520,
            items : [{
                xtype : 'projectListing',
                loadedProjects : loadedProjects
            }]
        }).show(); 
    },

    onFieldSelector : function () {
        var fieldStore = this.getSettingsFieldStoreStore();
        var _this = this;
        var projectName = this.activeProject;
        fieldStore.load ({
            params : {
                projectName : projectName
            },
            callback : function(records, operation, success) {
                if (!success)
                    Ext.Msg.Alert("Warning", "Cannot load project fields");
                else
                    _this.showFieldSelector();
            }
        });
    },

    showFieldSelector : function() {
        // The navigator and other views need to be refreshed only when there is a change in the selected fields
        // The selected fields can change either as a set or the order within it can change
        var selectedFields = Ext.data.StoreManager.get("Manage.store.settings.SelectedFieldStore");
        var availableFields = Ext.data.StoreManager.get("Manage.store.settings.AvailableFieldStore");

        // Old data
        var selectedFieldsOld = selectedFields.data.items.slice();
        var availableFieldsOld = availableFields.data.items.slice();

        var navigator = this.getNavigator();
        var _this = this;
        Ext.create('Ext.window.Window', {
            title : 'Choose fields',
            layout : 'fit',
            items : [{
                xtype : 'fieldChooser',
                listeners : {
                    'okclicked' : function(view, selected, available) {
                        if (_this.hasFieldsChanged(selectedFieldsOld, selected)) 
                            navigator.fireEvent("fieldsChanged", selected);
                    }
                }
            }],
            width : 500,
            height : 400
        }).show(); 
    },

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
        } else
            fieldsChanged = true;
        return fieldsChanged;
    },

    onFieldsChange : function(selectedFields) {
        console.log("Navigator: fields changed");
        var projectName = this.activeProject;
        var chosenFields = new Array();
        var i;
        
        for (i=0; i < selectedFields.length; ++i ) {
            chosenFields.push(selectedFields[i].data.name);
        }
        // Write to server
        Ext.Ajax.request({
            method : 'POST',
            url : '../navigator/setFieldsChosen',
            params : {
                projectName : projectName,
                chosenFields : Ext.encode(chosenFields)
            },
            success : function(result, req) {
            // Reset node for project
                
            },
            failure : function(result, req) {
                showErrorMessage(result.responseText, "Field saving to server failed");
            }
        });
       
        var projectNode = this.activeRecord;
        while (projectNode !== null && !projectNode.data.isProjectNode)
            projectNode = projectNode.parentNode;
        projectNode.set('leaf', false);
        projectNode.set('expandable', true);
        projectNode.set('loaded', false);
        projectNode.collapse();
        projectNode.childNodes = new Array();
    },

    // Double click action is by default expand. Replacing that with show siblings
    // wherever siblings are available.
    // Note: return false is important to prevent the default action of expanding node
    onNodeDoubleClick : function(view, node, item, index, e, eOpts) {
        // no siblings for the project node
        if (node.data.isProjectNode)
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
        var controller = this;
        var params = {
            fieldName : node.data.fieldName,
            fieldType : node.data.fieldType,
            projectName : node.data.projectName,
            conditions : Ext.encode(conditions)
        };
        if (node.data.min)
            params["min"] = node.data.min + "";
        if (node.data.max)
            params["max"] = node.data.max + "";
        
        Ext.Ajax.request( {
            method : 'GET',
            url : '../navigator/getSiblings',
            params : params,
            success : function (result, request) {
                var siblings = Ext.decode(result.responseText);
                if (siblings !== null && siblings.length > 0) {
                    controller.addSiblings(siblings, node);
                }
                console.log("Sibling nodes: " + siblings);
            },
            failure : function (result, request) {
                showErrorMessage(result.responseText, "Failed to fetch siblings for node");
            }
        });
    },

    /**
     * Add siblings for a given node. Remove the original node if all goes well.
     */
    addSiblings : function(siblings, node) {
        var parentNode = node.parentNode;
        var myIndex = parentNode.indexOf(node);
        for (var i=0; i < siblings.length ; ++i) {
            var nextNode = this.constructNode(siblings[i], node.data.projectID, node.data.projectName); 
            var addedNode = parentNode.insertChild(myIndex, nextNode);
            myIndex += 1;
        }
        parentNode.removeChild(node);
    },
    
    /**
     * Utility to construct a node from nodeData
     */
    constructNode : function(nodeData, projectID, projectName) {
        var nodeLimits = this.getNodeLimits(nodeData);
        var isLeafNode = this.checkIfLeafNode(nodeData.fieldName);
        var nextNode = {
            id : Ext.id(),
            leaf : isLeafNode,
            fieldName : nodeData.fieldName,
            fieldType : nodeData.fieldType,
            count : nodeData.noOfRecords,
            text : nodeData.fieldName + ' ' + nodeLimits + ':'+nodeData.noOfRecords,
            qtip : "Limits : " + nodeLimits + "<br/>Count : " + nodeData.noOfRecords,
            projectID : projectID,
            projectName : projectName,
            isProjectNode : false
        };
        if (nodeData.lowerLimit)
            nextNode["min"] = nodeData.lowerLimit;
        if (nodeData.upperLimit)
            nextNode["max"] = nodeData.upperLimit;
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
    
    /*
    * Check if the node is leaf node
    */

	checkIfLeafNode : function(fieldName){
	      var selectedFields = Ext.data.StoreManager.get("Manage.store.settings.SelectedFieldStore").data.items.slice();
          var length = selectedFields.length
          if(length > 0){
              var lastField = selectedFields[length - 1].data.name;
              if(fieldName == lastField)
                    return true;
              else
                    return false;
              }
              else
                    return true;
	},
	

    getFormatted : function(value, type) {
        if (!value)
            return "";
        if (type == "Real")
            return Ext.util.Format.number(value, "0.00");
        else
            return value;
    },
    /**
     * Utility to get all the loaded projects from the navigator
     * These projects will be disabled next time project explorer is opened
     */
    getLoadedProjects : function() {
        var projects = {};
        var projectNodes = this.getNavigator().getRootNode().childNodes;
        for (var i=0; i < projectNodes.length; ++i) {
            projects[projectNodes[i].data.projectName] = true;
        }
        return projects;
    },

    /**
     * Refresh all the projects in the navigator
     */
    onRefreshProjects : function() {
        var activeProjectNames = new Array();
        var rootNode = this.getNavigator().getRootNode();
        for (var i=0; i < rootNode.childNodes.length; ++i) {
            var nextProjectNode = rootNode.childNodes[i];
            activeProjectNames.push(nextProjectNode.data.projectName);
        }
        var _this = this;
        var navigator = this.getNavigator();
        Ext.Ajax.request({
            method : 'GET',
            url : '../project/getProjects',
            params : {
                projectNames : Ext.encode(activeProjectNames)
            },
            success : function(result, response) {
                var projects = Ext.decode(result.responseText);
                if (projects && projects !== null && projects.length > 0) {
                    rootNode.removeAll();
                    for (var j=0; j < projects.length; ++j) {
                        var next = projects[j];
                        var projectNode = _this.constructProjectNode(next.projectID, next.name, next.noOfRecords);
                        rootNode.appendChild(projectNode);
                    }
                    navigator.getSelectionModel().select(0);
                    navigator.fireEvent("itemclick", navigator, navigator.getSelectionModel().getLastSelected());
                }
            },
            failure : function(result, response) {
                showErrorMessage(result.responseText, "Failed to refresh projects");
            }
        });
    }
    
});
