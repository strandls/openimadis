/**
 * Summary table column chooser. This view is similar to the FieldChooser.js
 * There are two columns 'Available' and 'Selected'. One can drag and drop
 * from one to another and also drag and rearrange within a group. The values
 * in 'Selected' column will be the columns shown in summary table
 */
        /*handler : function() {
            var availableData = this.up().up().items.items[0].store.data.items;
            var selectedData = this.up().up().items.items[1].store.data.items;
            var availableItems = new Array();
            var selectedItems = new Array();
            var i;
            for (i=0; i < availableData.length; ++i)
                availableItems.push(availableData[i].data);
            for (i=0; i < selectedData.length; ++i)
                selectedItems.push(selectedData[i].data);
            this.up().up().fireEvent('columnsSelected', availableItems, selectedItems);
            // Close the window
            this.up().up().up().close();
        }*/

Ext.define('Manage.view.summarytable.SummaryTableColumnChooser', {
    extend : 'Manage.view.SplitView',
    xtype : 'summaryTableColumnChooser',
    alias : 'widget.summaryTableColumnChooser',
    leftView : {
        xtype : 'gridpanel',
        multiSelect: true,
        layout : 'fit',
        viewConfig: {
            plugins: {
                ptype: 'gridviewdragdrop'
            }
        },
        store : 'Manage.store.summarytable.SummaryTableAvailableColumnsStore',
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
        store : 'Manage.store.summarytable.SummaryTableSelectedColumnsStore',
        columns : [
                    {text: "Name", flex : 1, sortable : true, dataIndex : 'name'},
                    {text: "Type", flex : 1, sortable : true, dataIndex : 'type'}
                ],
        stripeRows : true,
        title : 'Selected Fields',
        margins : '0 2 0 0'
    }
});
