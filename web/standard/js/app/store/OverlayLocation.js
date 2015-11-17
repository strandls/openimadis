/**
 * Store for overlays locations for a particular record and overlay
 */
Ext.define('Manage.store.OverlayLocation', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.OverlayLocation',    
    model: 'Manage.model.OverlayLocation',
	autoLoad: false,
	
	sorters: [{
	     property: 'frameNo',
	     direction: 'ASC'
	},{
	     property: 'sliceNo',
	     direction: 'ASC'
	}],
	   
	proxy: {
		type: 'ajax',
		url: '../record/getVisualOverlayLocations',
		reader: {
            type: 'json',
            root: 'items',
            totalProperty: 'total'
        }
	}
});
