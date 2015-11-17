Ext.define('Manage.view.TileViewerPanel', {
	extend: 'Ext.grid.Panel',
	alias: 'widget.tileViewerPanel',
    requires: 'Manage.view.ProgressBarCustom',
	bodyPadding : 0,
	border: false,
    
	title: 'Tasks',
    store: 'TileViewerStore',
    columns: [{
        header: 'RecordID',
        dataIndex: 'recordId',
        flex: 15
    }, {
        header: 'Estimated Time',
        dataIndex: 'estimatedTime',
        flex: 20
    }, /*{
        header: '% Progress',
        dataIndex: 'progress',
        flex: 40
    }*/
    {
        header: '% Progress',
        dataIndex: 'progress',
        flex: 40,
        renderer: function (v, m, r) {
            var id = Ext.id();
            Ext.defer(function () {
                /*Ext.widget('ProgressBarCustom', {
                    text: v,
                    renderTo: id,
                    value: v,
                    color : "#4D0099",
                    //ui: 'progress1',
                    width: 100
                });*/
            	Ext.widget('progressbar', {
            		text: v,
                    renderTo: id,
                    value: v,
                    cls: 'customprogress'
            	});
            }, 50);
            return Ext.String.format('<div id="{0}"></div>', id);
        }
    }, {
        header: 'Memory (KB)',
        dataIndex: 'size',
        flex: 25  
    }],
    height: 200,
    width: 300
});
