/*Task priority*/
Ext.define('Manage.store.TaskStates',{
	extend: 'Ext.data.Store', 
    fields: ['text', 'value'],
    data : [
        {"text":"SCHEDULED","value":"SCHEDULED"},
        {"text":"WAITING","value":"WAITING"},
        {"text":"PAUSED","value":"PAUSED"},
        {"text":"ALLOCATED","value":"ALLOCATED"},
        {"text":"EXECUTING","value":"EXECUTING"},
        {"text":"TERMINATING","value":"TERMINATING"},
        {"text":"DELETED","value":"DELETED"},
        {"text":"ERROR","value":"ERROR"},
        {"text":"SUCCESS","value":"SUCCESS"},
        {"text":"TERMINATED","value":"TERMINATED"}
    ]
});
