/**
 * Store for all the clients available
 */
Ext.define('Manage.store.task.WorkflowClientStore', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.Client',    
    model: 'Manage.model.Client',
    autoLoad : true,
    proxy: {
        type: 'ajax',
        url: '../compute/getClients',
        extraParams:{
			isWorkflow:false
		},
        reader: 'json'
    },
    listeners:{
    	'load':function(store,records){
    		for(var i=0;i<records.length;i++){
    			var recData=records[i].data;
    			recData['text'] = recData['name']+' '+recData['version'];
    		}
    	}
    }
});
