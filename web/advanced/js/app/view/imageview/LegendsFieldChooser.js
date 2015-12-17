/**
 * this is legend field chooser. the selected fields will be applied as legends.
 */
Ext.define('Manage.view.imageview.LegendsFieldChooser', {
    extend : 'Manage.view.SplitView',
    xtype : 'legendsFieldChooser',
    alias : 'widget.legendsFieldChooser',
    leftView : {
        xtype : 'gridpanel',
        multiSelect: true,
        layout : 'fit',
        viewConfig: {
            plugins: {
                ptype: 'gridviewdragdrop'
            }
        },
        store : 'Manage.store.LegendAvailableFieldStore',
        columns : [
                    {text: "Name", flex : 1, sortable : true, dataIndex : 'name'}
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
        store : 'Manage.store.LegendSelectedFieldStore',
        columns : [
                    {text: "Name", flex : 1, sortable : true, dataIndex : 'name'}
                ],
        stripeRows : true,
        title : 'Selected Fields',
        margins : '0 2 0 0'
    }
});
