/**
 * List of all projects. Provides detailed information about each project 
 * All projects tab will have detailed information about the project
 *
 * NOTE:  Hack in the listeners section where the column number is hardcoded!
 * Should be taken care of when adding/removing columns
 */
Ext.define('Manage.view.AllProjects', {
    extend : 'Ext.grid.Panel',
    xtype : 'allProjects',
    alias : 'widget.allProjects',
    title : 'All Projects',
    store : 'Projects',
    layout : 'fit',
    autoScroll : true,
    listeners: {
        // HACKS to change the panel's background on mouse over
        // Get the panel element (assuming the position of element in order) and then change its style
        beforeitemmouseenter: function(view, record, item, index, e, options) {
            var panelElement = item.children[5].children[0].children[0].children[0];
            var panelID = panelElement.id;
            Ext.fly(panelID).addCls("x-panel-over");
        },
        beforeitemmouseleave: function(view, record, item, index, e, options) {
            var panelElement = item.children[5].children[0].children[0].children[0];
            var panelID = panelElement.id;
            Ext.fly(panelID).removeCls("x-panel-over");
        }
    },
    columns : [
        {
            header: 'Name',
            dataIndex : 'name',
            // renderer for word wrapping
            renderer : function (val){
                return '<div style="white-space:normal !important;">'+ val +'</div>';
            },
            flex : 1
        },
        {
            header: 'Notes',
            dataIndex : 'notes',
            // renderer for word wrapping
            renderer : function (val){
                return '<div style="white-space:normal !important;">'+ val +'</div>';
            },
            flex : 1
        },
        {
            header: 'Record Count',
            dataIndex : 'noOfRecords',
            flex : 1
        },
        {
            header: 'Space Usage (GB)',
            dataIndex : 'spaceUsage',
            renderer : Ext.util.Format.numberRenderer("0.00"),
	    width: 150
        },
        {
            header: 'Storage Quota (GB)',
            dataIndex : 'storageQuota',
	    width: 150
        },
        {
            header: 'Launch',
            dataIndex : 'projectID',
            xtype : 'componentcolumn',
            // Custom renderer to show a launch button. Uses an extjs plugin 'componentcolumn'
            renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
            	                               
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
                    layout : 'fit',
                    items : [button]
                };
            }
        }
    ]
});
