/**
 * List of recent projects. Provides a summarized visual view of the projects
 * All projects tab will have detailed information about the project
 *
 * NOTE:  Hack in the listeners section where the column number is hardcoded!
 * Should be taken care of when adding/removing columns
 */
Ext.define('Manage.view.RecentProjects', {
    extend : 'Ext.grid.Panel',
    xtype : 'recentProjects',
    alias : 'widget.recentProjects',
    title : 'Recent Projects',
    store : 'RecentProjects',
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
            width : 220,
            // Custom renderer to show a thumbnail for every project
            renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {  
            	
            	var url="";
                var URLs=store.proxy.reader.rawData.thumbnails;
                var projectNames=store.proxy.reader.rawData.projectnames;             
                var projectName=record.data.name;
                
                for(var name in projectNames){
                	if(projectNames[name]=== projectName){
                		url=URLs[projectNames.indexOf(projectName)];
                	}
                }               
                
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
                var html = '<div style="overflow: auto;" ><p><b>'+record.data.name+'</b>';
                html += '<br>' + record.data.notes + '<br>';
                html += '<br>Staus: ' + record.data.status ;
                html += '<br>Record count: ' + record.data.noOfRecords ;
                html += '<br>Disk Usage : ' + Ext.util.Format.number(usage, "0.00")  ;
                html += '</p></div>';
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
                
             // check if project is empty
                if(record.data.noOfRecords!=0){
	                if (enabled) {
	                    button = { 
	                        xtype : 'button',
	                        text : ' Launch ',
	                        icon : 'images/launch.png',
	                        handler : function() {
	                            // Fire addProject event when clicked
	                            view.up().fireEvent("addProject",  record.data.name, record.data.noOfRecords);
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
                }
                else{
                	button={
                			xtype : 'label',
	                        text : ' Project Empty '	
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