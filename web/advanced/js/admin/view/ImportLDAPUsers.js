/**
 * View for adding new members to a project
 */
Ext.require(['Admin.view.SplitView']);

Ext.define('Admin.view.ImportLDAPUsers', {
    extend : 'Ext.panel.Panel',
    xtype : 'importLDAPUsers',
    alias : 'widget.importLDAPUsers',
    
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
            store : 'Admin.store.AvailableLDAPUsersStore',
            columns : [
                {text: "Name", flex : 1, sortable : true, dataIndex : 'name'},
                {text: "E-Mail", flex : 1, sortable : true, dataIndex : 'email', hidden : true}
            ],
            stripeRows : true,
            title : 'Available Users',
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
            store : 'Admin.store.SelectedLDAPUsersStore',
            columns : [
                {text: "Name", flex : 1, sortable : true, dataIndex : 'name'},
                {text: "E-Mail", flex : 1, sortable : true, dataIndex : 'email', hidden : true}
            ],
            stripeRows : true,
            title : 'Selected Users',
            margins : '0 2 0 0' 
        }
    }, {
        xtype : 'combobox',
        fieldLabel : 'Rank',
        name : 'rank',
        queryMode : 'local',
        displayField : 'name',
        valueField : 'value',
        allowBlank : false,
        value : 'TeamMember',
        editable : false,
        region : 'south',
        padding : '20 10 10 10',
        store : 'Admin.store.UserRankStore'
    }],

    /**
     * Get selected data from the split view
     */
    getSelectedData : function() {
        var splitView = this.items.items[0];
        var rankChooser = this.items.items[1];
        return {selected : splitView.getSelectedData(), rank : rankChooser.getValue()};
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
            var emails = new Array();
            _.each(data.selected, function(item) {
                names.push(item.data.name);
                emails.push(item.data.email);
            });
            
            view.fireEvent('onAddLDAPUsers', view.up(), names, emails, data.rank);
        }
    }]
    
});
