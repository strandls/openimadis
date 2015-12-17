/**
 * @class Ext.ux.grid.HeaderToolTip
 * @namespace ux.grid
 *
 *  Text tooltips should be stored in the grid column definition
 *
 *  Sencha forum url:
 *  http://www.sencha.com/forum/showthread.php?132637-Ext.ux.grid.HeaderToolTip
 */
Ext.define('Ext.ux.grid.HeaderToolTip', {
    alias: 'plugin.headertooltip',
    init : function(grid) {
        var headerCt = grid.headerCt;
        grid.headerCt.on("afterrender", function(g) {
            grid.tip = Ext.create('Ext.tip.ToolTip', {
                target: headerCt.el,
                delegate: ".x-column-header",
                trackMouse: true,
                renderTo: Ext.getBody(),
                listeners: {
                    beforeshow: function(tip) {
                        var c = headerCt.down('gridcolumn[id=' + tip.triggerElement.id  +']');
                        if (c  && c.tooltip)
                            tip.update(c.tooltip);
                        else
                            return false;
                    }
                }
            });
        });
    }
});
