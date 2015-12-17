/**
 * Dynamic navigator. This navigator fetches records arranged into folders
 * whose order can be configured by the user. This implementation uses two
 * api's: 
 * - /navigator/getChildren : To get the children of a node
 * - /navigator/getSiblings : To get siblings of a node.
 * Siblings are rebinned versions of a node. For example, if a bin(folder)
 * contains elements from 1-100 of a field, a rebinned version might return
 * two folders instead with values from say 1-50 and 50-100.
 *
 * This implementation fetches children lazily. This is done via Ext.data.TreeStore.
 * The only field that the TreeStore sends across for an AJAX call is the node's ID.
 * So the node ID here contains all the information about its path from the root.
 */
Ext.require([
    'Ext.tree.*',
    'Manage.view.settings.FieldChooser'
]);

Ext.define('Manage.view.Navigator', {
    extend: 'Ext.tree.Panel',
    alias: 'widget.navigator',
    store: 'Manage.store.NavigatorStore',
    bodyPadding : 5,
    rootVisible : false,
    dockedItems: [{
        xtype: 'toolbar',
        items: [
            {
                icon : "images/icons/add.png",
                tooltip: 'Add Projects',
                handler : function() {
                    this.up().up().fireEvent("projectlisting");
                }
            }, { 
                icon : "images/icons/wrench.png", 
                tooltip: 'Select Fields', 
                handler: function(){ 
                    this.up().up().fireEvent("fieldSelector"); 
                }  
            }, {
                icon : "images/icons/table_refresh.png",
                tooltip : 'Refresh Projects',
                handler : function() {
                    this.up().up().fireEvent("refreshProjects"); 
                }
            }/*, {
                icon : "images/icons/legend-icon.png",
                tooltip : 'Annotation overlays',
                handler : function() {
                    this.up().up().fireEvent("fieldOverlaySelector"); 
                }
            }*/
        ]
    }],

    /**
     * Gather pre-conditions for this node by traversing upwards
     */
    getPreConditions : function(node) {
        var conditions = new Array();
        // Keep looping till the project node is reached
        if (!node.data.isProjectNode) {
            var nextNode = node;
            while (nextNode !== null && !(nextNode.data.isProjectNode)) {
                var nextProperties = {
                    fieldName : nextNode.data.fieldName,
                    fieldType : nextNode.data.fieldType
                };
                if (nextNode.data.min)
                    nextProperties["min"] = nextNode.data.min + "";
                if (nextNode.data.max)
                    nextProperties["max"] = nextNode.data.max + "";
                conditions.push(nextProperties);
                nextNode = nextNode.parentNode;
            }
        }
        return conditions;  
    },

    /**
     * Gather parent pre conditions
     */
    getParentPreConditions : function(node) {
        var conditions = new Array();
        // Keep looping till the project node is reached
        if (!node.data.isProjectNode) {
            var nextNode = node.parentNode;
            while (nextNode !== null && !(nextNode.data.isProjectNode)) {
                var nextProperties = {
                    fieldName : nextNode.data.fieldName,
                    fieldType : nextNode.data.fieldType
                };
                if (nextNode.data.min)
                    nextProperties["min"] = nextNode.data.min + "";
                if (nextNode.data.max)
                    nextProperties["max"] = nextNode.data.max + "";
                conditions.push(nextProperties);
                nextNode = nextNode.parentNode;
            }
        }
        return conditions;  
    }

});
