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
Ext.define('Manage.view.Navigator', {
    extend: 'Ext.tree.Panel',
    alias: 'widget.navigator',

    store: 'Navigators',

    bodyPadding : 5,
    useArrows: true,

    rootVisible : false,

    /**
     * Gather pre-conditions for this node by traversing upwards
     */
    getPreConditions : function(node) {
        var conditions = [];
        // Keep looping till the project node is reached
        if (!node.get('isProjectNode')) {
            var nextNode = node;
            while (nextNode !== null && !(nextNode.get('isProjectNode'))) {
                var nextProperties = {
                    fieldName : nextNode.get('fieldName'),
                    fieldType : nextNode.get('fieldType')
                };
                if (nextNode.get('min'))
                    nextProperties["min"] = nextNode.get('min') + "";
                if (nextNode.get('max'))
                    nextProperties["max"] = nextNode.get('max') + "";
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
        var conditions = [];
        // Keep looping till the project node is reached
        if (!node.get('isProjectNode')) {
            var nextNode = node.parentNode;
            while (nextNode !== null && !(nextNode.get('isProjectNode'))) {
                var nextProperties = {
                    fieldName : nextNode.get('fieldName'),
                    fieldType : nextNode.get('fieldType')
                };
                if (nextNode.get('min'))
                    nextProperties["min"] = nextNode.get('min') + "";
                if (nextNode.get('max'))
                    nextProperties["max"] = nextNode.get('max') + "";
                conditions.push(nextProperties);
                nextNode = nextNode.parentNode;
            }
        }
        return conditions;  
    }

});

