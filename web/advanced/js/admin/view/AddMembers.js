/**
 * View for adding new members to a project
 */
Ext.require(['Admin.view.SplitView']);

Ext.define('Admin.view.AddMembers', {
    extend : 'Ext.panel.Panel',
    xtype : 'addMembers',
    alias : 'widget.addMembers',
    
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
            hideHeaders : true,
            viewConfig: {
                plugins: {
                    ptype: 'gridviewdragdrop'
                }
            },
            store : 'Admin.store.AvailableStore',
            columns : [
                {text: "Name", flex : 1, sortable : true, dataIndex : 'name'}
            ],
            stripeRows : true,
            title : 'Available Members',
            margins : '0 2 0 0'  
        },
        rightView : {
            xtype : 'gridpanel',
            multiSelect: true,
            hideHeaders : true,
            layout : 'fit',
            viewConfig: {
                plugins: {
                    ptype: 'gridviewdragdrop'
                }
            },
            store : 'Admin.store.SelectedStore',
            columns : [
                {text: "Name", flex : 1, sortable : true, dataIndex : 'name'}
            ],
            stripeRows : true,
            title : 'Selected Members',
            margins : '0 2 0 0' 
        }
    }, {
        xtype : 'combobox',
        fieldLabel : 'Role',
        name : 'role',
        queryMode : 'local',
        displayField : 'name',
        valueField : 'value',
        allowBlank : false,
        value : 'Write',
        editable : false,
        region : 'south',
        padding : '20 10 10 10',
        store : 'Admin.store.UserRoleStore'
    }],

    /**
     * Get selected data from the split view
     */
    getSelectedData : function() {
        var splitView = this.items.items[0];
        var roleChooser = this.items.items[1];
        return {selected : splitView.getSelectedData(), role : roleChooser.getValue()};
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
            view.fireEvent('manageMembers', view.up(),view.projectName , view.userLogin , names, data.role);
        }
    }]
    
});
