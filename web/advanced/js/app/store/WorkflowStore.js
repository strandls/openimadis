/**
 * Store for workflows.
 */
Ext.define('Manage.store.WorkflowStore', {
    extend: 'Ext.data.Store',
    requires: ['Manage.model.WorkflowCategory', 'Manage.model.Workflow'],
    model: 'Manage.model.WorkflowCategory',
    autoLoad : true,
    proxy: {
        type: 'ajax',
        url: '../compute/getWorkflows',
        reader: 'json'
    },
    autoScroll : true,
    listeners : {
        /**
         * On receiving data about the workflows, the store pushes the same to the
         * equivaled tree store (WorkflowTreeStore.js). This way the logic of converting
         * data into extjs-specific map is kept in js instead of servlet
         */
        load : function (store, records, options) {
            if (records === null || records.length === 0)
                return;
            var rootNode = Ext.StoreManager.get('Manage.store.WorkflowTreeStore').getRootNode();
            rootNode.removeAll();
            for (var i=0; i < records.length; ++i) {
                var next = records[i];
                var categoryData = next.data;
                var category = {};
                category["leaf"] = false;
                category["text"] = categoryData["name"];
                category["id"] = categoryData["name"];
                category["iconCls"] = "workflowcategoryicon";
                //category["cls"] = "x-panel-header-default workflowcategory";
                category["cls"] = "workflowcategory";
                var nextNode = rootNode.appendChild(category);
                for (var j=0; j < next.workflows().getCount(); ++j) {
                    var childData = next.workflows().getAt(j).data;
                    var child = {};
                    child["leaf"] = true;
                    child["text"] = childData["name"]+' '+childData["version"];
                    child["name"] = childData["name"];
                    child["id"] = childData["name"]+' '+childData["version"];
                    child["version"] = childData["version"];
                    child["description"] = childData["description"];
                    child["iconCls"] = "workflowicon";
                    child["cls"] = "workflowitem";
                    nextNode.appendChild(child);
                }
                nextNode.expand();
            }
        }
    }
});
