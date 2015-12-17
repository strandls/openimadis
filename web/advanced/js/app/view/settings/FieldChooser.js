/**
 * View to choose fields from a list of available fields
 * The view allows to drag and drop from a 'availableFields' panel
 * to 'selectedFields' panel. Also, within a panel, the order can be
 * changed again using drag and drop
 */
Ext.define('Manage.view.settings.FieldChooser', {
    extend : 'Manage.view.SplitView',
    xtype : 'fieldChooser',
    alias : 'widget.fieldChooser',
    leftView : {
        xtype : 'gridpanel',
        multiSelect: true,
        layout : 'fit',
        viewConfig: {
            plugins: {
                ptype: 'gridviewdragdrop'
            }
        },
        store : 'Manage.store.settings.AvailableFieldStore',
        columns : [
                    {text: "Name", flex : 1, sortable : true, dataIndex : 'name'},
                    {text: "Type", flex : 1, sortable : true, dataIndex : 'type'}
                ],
        stripeRows : true,
        title : 'Available Fields',
        margins : '0 2 0 0'
    },
    rightView : {
        xtype : 'gridpanel',
        multiSelect: true,
        layout : 'fit',
        viewConfig: {
            plugins: {
                ptype: 'gridviewdragdrop'
            }
        },
        store : 'Manage.store.settings.SelectedFieldStore',
        columns : [
                    {text: "Name", flex : 1, sortable : true, dataIndex : 'name'},
                    {text: "Type", flex : 1, sortable : true, dataIndex : 'type'}
                ],
        stripeRows : true,
        title : 'Selected Fields',
        margins : '0 2 0 0'
    }
});
