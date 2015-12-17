/**
 * @class Ext.ux.toolbar.PagingOptions
 * @author Arthur Kay (http://www.akawebdesign.com)
 * @namespace Ext.ux.toolbar
 * @extends Ext.toolbar.Paging
 * @constructor
 * @param {object} configObj
 */
Ext.define('Ext.ux.toolbar.PagingOptions', {
    extend : 'Ext.toolbar.Paging',
    xtype : 'pagingOptions',
    alias : 'widget.pagingOptions',
    getPagingItems : function() {
        var me = this,
            pagingButtons = me.callParent();

        if (!Ext.ModelManager.getModel('PageSize')) {
            Ext.define('PageSize', {
                extend : 'Ext.data.Model',
                fields : [{ name : 'pagesize' , type : 'int'}]
            });
        }

        if (!me.pageSizeOptions) {
            me.pageSizeOptions = [
                { pagesize : 10 },
                { pagesize : 25 },
                { pagesize : 50 },
                { pagesize : 100 },
                { pagesize : 250 },
                { pagesize : 500 }
            ];
        }

        pagingButtons.push({
            xtype           : 'combobox',
            queryMode       : 'local',
            triggerAction   : 'all',
            displayField    : 'pagesize',
            valueField      : 'pagesize',
            width           : 100,
            lazyRender      : true,
            enableKeyEvents : true,
            value           : me.pageSize,
            forceSelection  : me.forceSelection || false,
            editable : false,

            store : new Ext.data.Store({
                model : 'PageSize',
                data  : me.pageSizeOptions
            }),

            listeners : {
                select : function(thisField, value) {
                    me.fireEvent('pagesizeselect', value[0].get('pagesize'));
                },
                keypress : function(thisField, eventObj) {
                    if (eventObj.getKey() !== eventObj.ENTER) { return false; }
                    me.fireEvent('pagesizeselect', thisField.getValue());
                }
            }
        });

        return pagingButtons;
    },

    initComponent : function() {
        var me = this;

        me.callParent();

        me.addEvents(
            'pagesizeselect'
        );
    }
});
