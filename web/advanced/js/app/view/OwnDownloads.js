/**
 * Component shows downloads for logged in user
 */
Ext.define('Manage.view.OwnDownloads', {
    extend : 'Ext.grid.Panel',
    alias : 'widget.ownDownloads',
    
    initComponent:function(){
		var me=this;
		this.callParent();
	}
});
