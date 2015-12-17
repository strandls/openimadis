/**
 * List of recent projects. Provides a summarized visual view of the projects
 * All projects tab will have detailed information about the project
 *
 * NOTE:  Hack in the listeners section where the column number is hardcoded!
 * Should be taken care of when adding/removing columns
 */
Ext.define('Manage.view.RecentProjectsPanel', {
    extend : 'Ext.grid.Panel',
    xtype : 'recentProjects',
    alias : 'widget.recentProjects',
    title : 'Recent Projects',
    store : 'Manage.store.RecentProjectStore',
    layout : 'fit',
//    hideHeaders : true,
    autoScroll : true,
    disableSelection: true,
    listeners: {
        // HACKS to change the panel's background on mouse over
        // Get the panel element (assuming the position of element in order) and then change its style
        beforeitemmouseenter: function(view, record, item, index, e, options) {
            var panelElement = item.children[2].children[0].children[0].children[0];
            var panelID = panelElement.id;
            Ext.fly(panelID).addCls("x-panel-over");
        },
        beforeitemmouseleave: function(view, record, item, index, e, options) {
            var panelElement = item.children[2].children[0].children[0].children[0];
            var panelID = panelElement.id;
            Ext.fly(panelID).removeCls("x-panel-over");
        }
    },
    columns : [
        {
            header: 'Thumbnail',
            dataIndex: 'projectID',
            width : 100,
            // Custom renderer to show a thumbnail for every project
            renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
                var url = store.proxy.reader.rawData.thumbnails[rowIndex];
                if (url === "") {
                    return "<h2>Project Empty<h2>";
                }
                return '<img src="' +  url + '" height=80 width=80/>';
            }
        },
        {
            header: 'Description',
            dataIndex : 'name',
            flex : 1,
            // Custom renderer to show name, notes and disk usage
            renderer : function(value, metaData, record) {
                var usage = (record.data.spaceUsage/record.data.storageQuota) * 100;
                var html = '<h2>'+record.data.name+'</h2>';
                html += '<p>' + record.data.notes + '</p>';
                html += '<br/>';
                html += '<p>Staus: ' + record.data.status + ' </p>';
                html += '<p>Record count: ' + record.data.noOfRecords + ' </p>';
                html += '<p>Disk Usage : ' + Ext.util.Format.number(usage, "0.00")  + ' %</p>';
                return html;
            }
        },
        {
            header: 'Launch',
            dataIndex : 'notes',
            xtype : 'componentcolumn',
            // Custom renderer to show a launch button. Uses an extjs plugin 'componentcolumn'
            renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
                // Already loaded projects are a parameter in projectListing
                var loadedProjects = view.up().up().loadedProjects;
                var enabled = true;
                if (loadedProjects && (record.data.name in loadedProjects))
                    enabled = false;
                var button = null;
                if (enabled) {
                    button = { 
                        xtype : 'button',
                        text : ' Launch ',
                        icon : 'images/launch.png',
                        handler : function() {
                            // Fire addProject event when clicked
                            view.up().fireEvent("addProject", record.data.projectID, record.data.name, record.data.noOfRecords);
                            this.setText("Added");
                            this.setDisabled(true);
                            view.up().up().up().close();
                        }  
                    }; 
                } else {
                    button = { 
                        xtype : 'button',
                        text : ' Added ',
                        icon : 'images/launch.png',
                        disabled : true
                    };
                }
                return {
                    xtype : 'panel',
                    border : false,
                    height : 80,
                    width : 100,
                    layout : {
                        type : 'hbox',
                        pack : 'center',
                        align : 'middle'
                    },
                    items : [button]
                };
            }
        }
    ]
});
