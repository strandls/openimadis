/**
 * Model for the bookmarks
 */
Ext.define('Manage.model.Bookmark', {
    extend: 'Ext.data.Model',

    fields: [
             { name: 'text',  type: 'string'},
             { name: 'leaf',  type: 'boolean'},
             {name:'bookmarkName', type:'string'},
             {name:'recordCount', type:'string'}
    ]
});

