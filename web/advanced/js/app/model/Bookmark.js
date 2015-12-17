Ext.define('Manage.model.Bookmark', {
    extend: 'Ext.data.Model',
    proxy: {
        type: 'memory'
    },
    fields: [
             
             { name: 'text',  type: 'string'},
             { name: 'leaf',  type: 'boolean'},
             {name :'recordId',type:'string'},
             {name:'projectName', type:'string'},
             {name:'bookmarkName', type:'string'},
             {name:'recordCount', type:'string'}

    ]
});