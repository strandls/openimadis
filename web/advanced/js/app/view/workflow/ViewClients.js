/**
 * View for listing all the clients created by the user
 */
Ext.define('Manage.view.workflow.ViewClients', {
    extend : 'Ext.grid.Panel',
    xtype : 'viewClients',
    alias : 'widget.viewClients',
    bodyPadding: 5,
    store : 'Manage.store.UserClientStore',

    initComponent : function() {
        Ext.apply(this, {
            tbar : [{
                icon : 'images/icons/add.png',
                tooltip : 'Add Client',
                handler : function() {
                    this.up().up().fireEvent("addClient");
                }
            }]
        });
        this.callParent();
    },

    columns : [{
        header : 'Name', dataIndex : 'name', flex : 1
    }, {
        header : 'Version', dataIndex : 'version'
    }, {
        header : 'Description', dataIndex : 'description', flex : 2
    }, {
        header : 'Client ID',
        dataIndex : 'clientID',
        xtype : 'componentcolumn',
        dontSort : true,
        renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
            return {
                xtype : 'panel',
                border : false,
                layout : {
                    type : 'hbox',
                    pack : 'center',
                    align : 'middle'
                },
                items : [
                    {
                        xtype : 'button',
                        text : 'Show',
                        handler : function() {
                            Ext.Msg.alert("Client ID", value);
                        }
                    }
                ]
            }; 
        }
    }]
});
