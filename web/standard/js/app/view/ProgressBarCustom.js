Ext.define("Manage.view.ProgressBarCustom", {
    extend: 'Ext.ProgressBar',
    alias: 'widget.ProgressBarCustom',
    max: null,
    ave: null,
    min: null,
    color: null,

    initComponent: function () {
        var me = this;
        me.width = 300;
        me.margin = '5 5 0 5';
        me.callParent(arguments);
    },

    listeners: {
        update: function (obj, val) {
            if (this.max != null && this.ave != null && this.min != null) {
                if (val * 100 <= this.min) {
                    obj.getEl().child(".x-progress-bar", true).style.backgroundColor = "#FF0000";
                    obj.getEl().child(".x-progress-bar", true).style.borderRightColor = "#FF0000";
                    obj.getEl().child(".x-progress-bar", true).style.backgroundImage = "url('images/progress.gif')";
                } else if (val * 100 <= this.ave) {
                    obj.getEl().child(".x-progress-bar", true).style.backgroundColor = "#FFFF00";
                    obj.getEl().child(".x-progress-bar", true).style.borderRightColor = "#FFFF00";
                    obj.getEl().child(".x-progress-bar", true).style.backgroundImage = "url('images/progress.gif')";
                } else {
                    obj.getEl().child(".x-progress-bar", true).style.backgroundColor = "#009900";
                    obj.getEl().child(".x-progress-bar", true).style.borderRightColor = "#009900";
                    obj.getEl().child(".x-progress-bar", true).style.backgroundImage = "url('images/progress.gif')";
                }
            } else if (this.color != null) {
                obj.getEl().child(".x-progress-bar", true).style.backgroundColor = this.color;
                obj.getEl().child(".x-progress-bar", true).style.borderRightColor = this.color;
                obj.getEl().child(".x-progress-bar", true).style.backgroundImage = "images/progress.gif";
            }
        }
    }
});