/**
 * View for displaying all the user annotations for a record
 */
Ext.define('Manage.view.UserAnnotations', {
	extend : 'Ext.grid.Panel',
	xtype : 'userAnnotations',
	alias : 'widget.userAnnotations',
	store:'Manage.store.UserAnnotationStore',
    title : 'User Annotations',
    columns: [
        { header: 'User Annotation', dataIndex: 'name', flex : 1 },
        { header: 'Value', dataIndex: 'value', flex : 1 }
    ]
});
