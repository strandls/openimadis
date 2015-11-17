/**
 * List of tokens with actions to manage them (add, delete, edit)
 */
Ext.define('Manage.view.settings.TokenList', {
    extend : 'Ext.grid.Panel',
    store : 'Tokens',
    xtype : 'tokenList',
    alias : 'widget.tokenList',
    region : 'center',
    layout : 'fit',
    initComponent : function() {
        var _this = this;
        Ext.apply(this, {
            tbar : [{
                icon : 'images/icons/add.png',
                tooltip : 'Add Token',
                handler : function() {
                	this.up('tokenList').fireEvent("addToken");
                }
            }, {
                icon : 'images/icons/wrench.png',
                tooltip : 'Edit Selected Token',
                handler : function() {
                    var selected = _this.getSelectionModel().getSelection();
                    if (selected && selected !== null && selected.length === 1) {
                    	this.up('tokenList').fireEvent('editToken', _this, selected[0]);
                    } else {
                        Ext.Msg.alert("Warning", "Select token to edit from the table");
                    } 
                }
            }, {
                icon : 'images/icons/delete.png',
                tooltip : 'Delete Selected Token',
                handler : function() {
                    var selected = _this.getSelectionModel().getSelection();
                    if (selected && selected !== null && selected.length === 1) {
                    	this.up('tokenList').fireEvent('removeToken', _this, selected[0]);
                    } else {
                        Ext.Msg.alert("Warning", "Select token to delete from the table");
                    } 
                }
            }],
            listeners : {
                comboselected : function(newValue, oldValue) {
                    _this.activeUser = newValue;
                    var store = _this.getStore();
                    store.load({
                        params : {
                            user : newValue
                        }
                    });
                }
            }
        });
        this.callParent();
    },

    columns : [{
        header : 'Client', dataIndex : 'client', flex : 1
    }, {
        header : 'Created On', dataIndex : 'creationTime', flex : 1,
        renderer : function(value) {
            return Ext.util.Format.date(new Date(value), 'Y-m-d g:i a');
        }
    }, {
        header : 'Expiry', dataIndex : 'expiryTime', flex : 1,
        renderer : function(value) {
            return Ext.util.Format.date(new Date(value), 'Y-m-d g:i a');
        }
    }, {
        header : 'Last Access', dataIndex : 'accessTime', flex : 1,
        renderer : function(value) {
            return Ext.util.Format.date(new Date(value), 'Y-m-d g:i a');
        }
    }]

});
