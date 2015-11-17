/**
 * Store for workflows.
 */
Ext.define('Manage.store.AvailableWorkflowStore', {
    extend: 'Ext.data.Store',
    model: 'Manage.model.WorkflowCategory',
    autoLoad : true,
    proxy: {
        type: 'ajax',
        url: '../compute/getAvailableWorkflows',
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
        	
            var rootNode = Ext.StoreManager.get('AvailableWorkflowTree').getRootNode();
            rootNode.removeAll();
            
            if (records === null || records.length === 0)
                return;            

            for (var i=0; i < records.length; ++i) {
                var next = records[i];
                var categoryData = next.data;
                var category = {};
                category["leaf"] = false;
                category["name"] = categoryData["name"];
                category["expanded"] = true;
                category["id"] = categoryData["folderID"];
                category["selected"] = true;

                var nextNode = rootNode.appendChild(category);
                
                for (var j=0; j < next.workflows().getCount(); ++j) {
                    var childData = next.workflows().getAt(j).data;
                    var child = {};
                    child["leaf"] = true;
                    child["expanded"] = true;
                    child["name"] = childData["name"]+' '+childData["version"];
                    child["id"] = childData["id"];
                    child["version"] = childData["version"];
                    child["description"] = childData["description"];                   
                    child["selected"] = childData["selected"];
                    if(!childData["selected"]){
                    	child["appName"] = childData["name"];
                    }
                    nextNode.appendChild(child);
                }
                nextNode.expand();
            }
        }
    }
});
