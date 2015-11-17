Ext.define('Manage.store.MemberProjects', {
    extend : 'Ext.data.Store',
    requires : 'Manage.model.MemberProject',
    model : 'Manage.model.MemberProject',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../project/getMemberProjectNames',
        reader : {
            type : 'json'
        }
    }
});