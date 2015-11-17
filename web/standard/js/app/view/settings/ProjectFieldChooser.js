/**
 * View to choose the COLUMNS to show in the SummaryTable 
 *  and the Fields to show in Thumbnail sorting.
 * The view allows to drag and drop from a 'availableFields' panel
 * to 'selectedFields' panel. Also, within a panel, the order can be
 * changed again using drag and drop
 */
Ext.define('Manage.view.settings.ProjectFieldChooser', {
    extend : 'Manage.view.SplitView',
    xtype : 'projectFieldChooser',
    alias : 'widget.projectFieldChooser',
    leftView : {
        xtype : 'gridpanel',
        multiSelect: true,
        layout : 'fit',
        viewConfig: {
            plugins: {
                ptype: 'gridviewdragdrop'
            }
        },
        store : 'ProjectAvailableFields',
        columns : [
                    {text: "Name", flex : 1, sortable : true, dataIndex : 'name'},
                    {text: "Type", flex : 1, sortable : true, dataIndex : 'type'}
                ],
        stripeRows : true,
        title : 'Available Columns',
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
        store : 'ProjectSelectedFields',
        columns : [
                    {text: "Name", flex : 1, sortable : true, dataIndex : 'name'},
                    {text: "Type", flex : 1, sortable : true, dataIndex : 'type'}
                ],
        stripeRows : true,
        title : 'Selected Columns',
        margins : '0 2 0 0'
    }
});

