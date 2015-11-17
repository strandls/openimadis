Ext.define('Manage.view.settings.ProjectUsersList', {
    extend : 'Ext.grid.Panel',
    xtype : 'projectUsersList',
    alias : 'widget.projectUsersList',
    store : 'ProjectUsers',
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
