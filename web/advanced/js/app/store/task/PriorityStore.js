/*Task priority*/
Ext.define('Manage.store.task.PriorityStore',{
	extend: 'Ext.data.Store', 
    fields: ['text', 'value'],
    data : [
        {"text":"HIGH","value":"HIGH"},
        {"text":"MEDIUM","value":"MEDIUM"},
        {"text":"LOW","value":"LOW"}
    ]
});