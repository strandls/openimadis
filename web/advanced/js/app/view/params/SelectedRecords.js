Ext.define('Manage.view.params.SelectedRecords', {
	extend:'Ext.grid.Panel',
	xtype:'selectedRecords',
	alias:'widget.selectedRecords',
	
	initComponent : function() {
		
		var store= Ext.create('Ext.data.Store', {
			id:'selectedRecordStore',
    	    fields:['Record ID','Source Folder','Source File','Frame Count','Slice Count'],
    	    data:{'items':[]},
    	    proxy: {
    	        type: 'memory',
    	        reader: {
    	            type: 'json',
    	            root: 'items'
    	        }
    	    }
    	});
			
		var config={
			autoScroll : true,
			height:150,
			title:'Selected Records',
            columns:[{header: 'Source Folder',  dataIndex:'Source Folder'},
                     {header: 'Source File',  dataIndex:'Source File'},
                     {header: 'Frame Count',  dataIndex:'Frame Count'},
                     {header: 'Slice Count',  dataIndex:'Slice Count'}],
            store: store,
            selModel : Ext.create('Ext.selection.CheckboxModel', {
                checkOnly : true
            })
            
		};
		
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		this.callParent();
	},
	
	getSelectedRecordIds: function(){
		var ret = new Array(); 
        var selected = this.getSelectionModel().getSelection();
        if (selected !== null && selected.length > 0) {
            for (var i=0; i<selected.length; ++i) {
                ret.push(selected[i].data["Record ID"]);
            }
        }
        return ret;
	}
});