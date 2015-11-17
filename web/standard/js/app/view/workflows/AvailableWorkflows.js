/**
 * Workflow browser. Arranges the workflow items into
 * categories and puts them in a tree with two levels.
 * The first level has the categories while the second,
 * the workflows.
 */
Ext.define('Manage.view.workflows.AvailableWorkflows', {
	extend : 'Ext.panel.Panel',
	xtype : 'availableworkflows',
	alias : 'widget.availableworkflows',
	
    dockedItems : [{
        xtype : 'form',
        layout: {
        	type: 'hbox'
        },

        defaults: {
        	margin: '6px'
        },
        
        items : [{
        	xtype:'textfield',
        	name: 'tag',
			listeners: {
				specialkey : function(textfield,eventObject){
			          if (eventObject.getCharCode() == Ext.EventObject.ENTER) {
			        	  this.up().up().fireEvent("tagSearch",this.up().getForm().findField('tag').getSubmitValue());
			          }
			      }
			}
        	
        },  {
        	xtype:'button',
        	text:'Search',
            listeners:{
            	click: function(view){
            		view.up().up().fireEvent("tagSearch",this.up().getForm().findField('tag').getSubmitValue());
            	}
            }
        	
        }, {
        	xtype:'button',
        	text:'All Apps',  
            listeners:{
            	click: function(view){
            		view.up().up().fireEvent("resetSearch");
            	}
            }
        }]
    }],
    
    
    items:[{  
    	xtype:'panel',
        layout:'card',
        activeItem:0,
        id:'availablelayout',
       
        items:[{    	
        	xtype:'treepanel',

        	store : 'AvailableWorkflowTree',
        	id:'availableworkflowsgrid',

            useArrows: true,
            rootVisible: false,
            hideHeaders: true,			
        	lines : false,
        	height: 200,
        	autoscroll:true,
    	    //the 'columns' property is now 'headers'
    	    columns: [{
    		        xtype: 'treecolumn', //this is so we know which column will show the tree
    		        text: 'Category',
    		        flex: 1,
    		        sortable: true,
    		        dataIndex: 'name'
    		    },{
    		    	text: 'Add',
    		        width: 55,
    		        menuDisabled: true,
    		        xtype: 'actioncolumn',
    		        tooltip: 'Add to favourite',
    		        align: 'center',
    		        handler: function(view, rowIndex, colIndex, actionItem, event, record, row) {
    		        	view.up().up().up().fireEvent('addToFavourite',record); 
    		        },
    		        items:[
    		               {
    		                  icon: 'images/icons/add.png',
    		                  id: 'addToFavourite',
    		
    		                 getClass: function(Value, metaData, record){
    		                            if(record.data.selected)
    		                                metaData.css = 'x-hide-display';
    		                            else
    		                                metaData.css = 'x-grid-icon'
    		                 }
    		              }
    		         ]
    		    },{
    		        text: 'appName',
    		        hidden: true,
    		        dataIndex: 'appName'
    	    }] 
        },{
        	xtype:'gridpanel',
        	store:'TagSearch',
        	hideHeaders:true,
        		
            columns: [{
    		        flex: 1,
    		        sortable: true,
    		        dataIndex: 'name'
    		    },{
    		        flex: 1,
    		        sortable: true,
    		        dataIndex: 'version'
    		    },{
    		        width: 55,
    		        menuDisabled: true,
    		        xtype: 'actioncolumn',
    		        tooltip: 'Add to favourite',
    		        align: 'center',
    		        handler: function(view, rowIndex, colIndex, actionItem, event, record, row) {
    		        	view.up().up().up().fireEvent('addToFavourite',record); 
    		        },
    		        items:[
    		               {
    		                  icon: 'images/icons/add.png',
    		                  id: 'addToFavourite',
     		                  getClass: function(Value, metaData, record){
		                            if(record.data.selected)
		                                metaData.css = 'x-hide-display';
		                            else
		                                metaData.css = 'x-grid-icon'
     		                  }
    		              }
    		         ]
    		    },{
    		        text: 'appName',
    		        hidden: true,
    		        dataIndex: 'appName'
    	    }]	
        }]      
    }]
    
});


