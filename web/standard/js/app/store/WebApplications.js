/**
 * Store for workflows.
 */
Ext.define('Manage.store.WebApplications', {
    extend: 'Ext.data.Store',
    model: 'Manage.model.WebApplicationTree',
    autoLoad : true,
    proxy: {
        type: 'ajax',
        url: '../compute/getAvailableWebClients',
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
            
            var rootNode = Ext.StoreManager.get('WebApplicationsTree').getRootNode();
            rootNode.removeAll();
            
            console.log("[WebApplications] records"+records);
            
            for (var i=0; i < records.length; ++i) {
                var next = records[i];
                var categoryData = next.data;
                var category = {};
                category["leaf"] = false;
                category["name"] = categoryData["name"];
                category["expanded"] = true;

                var nextNode = rootNode.appendChild(category);
                
                for (var j=0; j < next.webApplication().getCount(); ++j) {
                    var childData = next.webApplication().getAt(j).data;
                    
                    var child = {};
                    child["leaf"] = true;
                    child["expanded"] = true;
                    child["name"] = childData["name"]+' '+childData["version"];
                    child["id"] = childData["id"];
                    child["version"] = childData["version"];
                    child["description"] = childData["description"];
                    child["url"] = childData["url"];
                    nextNode.appendChild(child);
                }
                nextNode.expand();
            }
        }
    }
});
