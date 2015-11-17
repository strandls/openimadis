/**
 * View to choose fields from a list of available fields
 * The view allows to drag and drop from a 'availableFields' panel
 * to 'selectedFields' panel. Also, within a panel, the order can be
 * changed again using drag and drop
 */
Ext.define('Manage.view.NavigatorFieldChooser', {
    extend : 'Manage.view.SplitView',
    xtype : 'navigatorFieldChooser',
    alias : 'widget.navigatorFieldChooser',
    leftView : {
        xtype : 'gridpanel',
        multiSelect: true,
        layout : 'fit',
        viewConfig: {
            plugins: {
                ptype: 'gridviewdragdrop'
            }
        },
        store : 'NavigatorAvailableFields',
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
        store : 'NavigatorSelectedFields',
        columns : [
                    {text: "Name", flex : 1, sortable : true, dataIndex : 'name'},
                    {text: "Type", flex : 1, sortable : true, dataIndex : 'type'}
                ],
        stripeRows : true,
        title : 'Selected Fields',
        margins : '0 2 0 0'
    }
});
