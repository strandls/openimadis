/**
 * View for adding new members to a project
 */
Ext.require(['Admin.view.SplitView']);

Ext.define('Admin.view.AddAssociations', {
    extend : 'Ext.panel.Panel',
    xtype : 'addMembers',
    alias : 'widget.addAssociations',
    
    bodyPadding: 5,

    // Fields will be arranged vertically, stretched to full width
    layout : 'border',
    items : [{
        xtype : 'splitview',
        region : 'center',
        leftView : {
            xtype : 'gridpanel',
            multiSelect: true,
            layout : 'fit',
            hideHeaders : false,
            viewConfig: {
                plugins: {
                    ptype: 'gridviewdragdrop'
                }
            },
            store : 'Admin.store.AvailableUnitsStore',
            columns : [
                {text: "Name", flex : 1, sortable : true, dataIndex : 'name'},
                {text: "Available Space (GB)", flex : 1, sortable : true, dataIndex : 'availableSpace'}
            ],
            stripeRows : true,
            title : 'Available Teams',
            margins : '0 2 0 0'  
        },
        rightView : {
            xtype : 'gridpanel',
            multiSelect: true,
            hideHeaders : false,
            layout : 'fit',
            viewConfig: {
                plugins: {
                    ptype: 'gridviewdragdrop'
                }
            },
            store : 'Admin.store.SelectedUnitsStore',
            columns : [
                {text: "Name", flex : 1, sortable : true, dataIndex : 'name'},
                {text: "Available Space (GB)", flex : 1, sortable : true, dataIndex : 'availableSpace'}
            ],
            stripeRows : true,
            title : 'Selected Teams',
            margins : '0 2 0 0' 
        }
    }, {
        xtype : 'numberfield',
        fieldLabel : 'Storage Contribution By Each Team (in GB)',
        name : 'globalStorage',
        region : 'south',
        minValue : 1,
        maxValue : 1000000,
        allowDecimal : true,
        decimalPrecision : 1,
        step : 0.1,
        value : 0,
        allowBlank : false
    }],

    /**
     * Get selected data from the split view
     */
    getSelectedData : function() {
        var splitView = this.items.items[0];
        var storageQuota = this.items.items[1];
        return {selected : splitView.getSelectedData(), quota : storageQuota.getValue()};
    },

    buttons : [{
        text : 'Cancel',
        handler : function() {
            var view = this.up().up();
            view.up().close();
        }
    }, {
        text : 'Submit',
        handler : function() {
            var view = this.up().up();
            var data = view.getSelectedData();
            var names = new Array();
            _.each(data.selected, function(item) {
                names.push(item.data.name);
            });
            view.fireEvent('manageAssociations', view.up(), view.projectName, names, data.quota);
        }
    }]
    
});
