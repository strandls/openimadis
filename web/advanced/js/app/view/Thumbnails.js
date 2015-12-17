/**
 * Thumnails view
 */
Ext.define('Manage.view.Thumbnails', {
    extend : 'Ext.view.View',
    xtype : 'thumbnails',
    alias : 'widget.thumbnails',

    tpl: [
        '<tpl for=".">',
            '<div class="thumb-wrap">',
                '<div class="thumb">',
                    (!Ext.isIE6? '<img src="{imagesource}" />' : 
                    '<div style="width:120px;height:120px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\'{imagesource}&t={[new Date().getTime()]}\')"></div>'),
                '</div>',
            '</div>',
        '</tpl>'
    ],

    style : {
        'background-color' : 'white'
    },

    itemSelector: 'div.thumb-wrap',
    multiSelect: true,
    singleSelect: false,
    cls: 'x-image-view',
    store : 'Manage.store.ThumbnailRecords',

    focusNode : function(node) {
        var me         = this,
            scroll     = false,
            panel      = me.ownerCt,
            rowRegion,
            elRegion,
            record;

        if (panel && node) {
            elRegion  = panel.el.getRegion();
            rowRegion = Ext.fly(node).getRegion();
            // row is above
            if (rowRegion.top < elRegion.top) {
                scroll = true;
            // row is below
            } else if (rowRegion.bottom > elRegion.bottom) {
                scroll = true;
            }

            if (scroll) {
                // scroll the grid itself, so that all gridview's update.
                //panel.scrollByDeltaY(adjustment);
                node.scrollIntoView();
            }
        }
    },

    listeners : {
        refresh : function(view, opts) {
            var selected = view.getSelectionModel().getSelection();
            if (selected && selected !== null && selected.length > 0) {
                var node = view.getNode(selected[0]);
                var task = new Ext.util.DelayedTask(function() {
                    node.scrollIntoView();
                });
                task.delay(1000);
            }
        }
    }
});
