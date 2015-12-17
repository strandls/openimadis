Ext.define('Manage.view.ProjectUsersList', {
    extend : 'Ext.grid.Panel',
    xtype : 'projectUsersList',
    alias : 'widget.projectUsersList',
    store : 'Manage.store.ProjectUserStore',
    title : 'Users',

    initComponent : function() {
        Ext.apply (this, {
            columns : [
                {header : 'Name', dataIndex : 'user', flex : 1},
                {header : 'Role', dataIndex : 'role', flex : 1}
            ],
            height: 400,
            width: 300
        });
        this.callParent();
    }
});
