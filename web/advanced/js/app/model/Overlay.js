/**
 * Model for an overlay
 */
Ext.define('Manage.model.Overlay', {
    extend: 'Ext.data.Model',
    fields : ['frameNo', 'height', 'name', 'siteNo', 'sliceNo', 'width', 'handCreated'],
    idProperty : 'name'
});
