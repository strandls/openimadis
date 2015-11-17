/**
 * Two arrow buttons to move items from a left panel to right
 * and the other way
 */
Ext.define('Manage.view.ArrowButtons', {
    extend : 'Ext.panel.Panel',
    xtype : 'arrowButtons',
    alias : 'widget.arrowButtons',
    layout : {
        type : 'hbox',
        pack : 'center',
        align : 'middle'
    },
    items : [{
        xtype : 'panel',
        border : false,
        items : [{
            xtype : 'button',
            icon : 'images/icons/uparrow.png',
            disabled : false,
            handler : function() {
                var view = this.up().up();
                view.fireEvent("moveUp");
            }
        }, {
            // empty panel
            xtype : 'panel',
            height : 10,
            border :false
        },{
            xtype : 'button',
            icon : 'images/icons/rightarrow.png',
            disabled : true,
            handler : function() {
                var view = this.up().up();
                view.fireEvent("moveRight");
            }
        }, {
            // empty panel
            xtype : 'panel',
            height : 10,
            border :false
        },{
            xtype : 'button',
            icon : 'images/icons/leftarrow.png',
            disabled : true,
            handler : function() {
                var view = this.up().up();
                view.fireEvent("moveLeft");
            }
        }, {
            // empty panel
            xtype : 'panel',
            height : 10,
            border :false
        },{
            xtype : 'button',
            icon : 'images/icons/downarrow.png',
            disabled : false,
            handler : function() {
                var view = this.up().up();
                view.fireEvent("moveDown");
            }
        }]
    }],
    /**
     * Enable the left button and disable the right
     */
    enableLeft : function() {
        this.getRightButton().setDisabled(true);
        this.getLeftButton().setDisabled(false);
    },
    /**
     * Enable the right button and disable the left
     */
    enableRight : function() {
        this.getRightButton().setDisabled(false);
        this.getLeftButton().setDisabled(true);
    }, /**
     * Enable the left button and disable the right
     */
    enableUp : function() {
        this.getUpButton().setDisabled(false);
        //this.getLeftButton().setDisabled(false);
    },
    /**
     * Enable the right button and disable the left
     */
    enableDown : function() {
        this.getDownButton().setDisabled(false);
        //this.getLeftButton().setDisabled(true);
    },
    /**
     * Get right button
     */
    getRightButton : function() {
        return this.items.items[0].items.items[2];
    },
    /**
     * Get left button
     */
    getLeftButton : function() {
        return this.items.items[0].items.items[4];
    }, 
    /*
    * Get up button
    */
   getUpButton : function() {
       return this.items.items[0].items.items[0];
   },
   /**
    * Get Down button
    */
   getDownButton : function() {
       return this.items.items[0].items.items[6];
   }
});

/**
 * View which has two columns, both backed by their own stores.
 * One can drag and drop from one to another, reorder within 
 * one column and move an item from one column to another via
 * arrow buttons.
 * TODO: docs
 */
Ext.define('Manage.view.SplitView', {
    extend : 'Ext.panel.Panel',
    layout : {
        type: 'hbox',
        align: 'stretch'
    },
    defaults : { flex : 1 },
    initComponent : function() {
        // FIXME : following two lines assume leftView and rightView come as objects
        Ext.applyIf ( this.leftView, {listeners : {}} );
        Ext.applyIf ( this.rightView, {listeners : {}} );
        var view = this;
        this.leftView.listeners.selectionchange = function() {
            var rightView = view.getRightView();
            rightView.getSelectionModel().deselectAll(true);
            var buttonPanel = view.getArrowsPanel();
            buttonPanel.enableRight();
        };
        this.rightView.listeners.selectionchange = function() {
            var leftView = view.getLeftView();
            leftView.getSelectionModel().deselectAll(true);
            var buttonPanel = view.getArrowsPanel();
            buttonPanel.enableLeft();
        };
        var arrow = Ext.create('Manage.view.ArrowButtons', {
            listeners : {
                moveLeft : this.onMoveLeft(),
                moveRight : this.onMoveRight(),
                moveUp : this.onMoveUp(),
                moveDown : this.onMoveDown()
            },
            border : false,
            width : 40,
            flex : 0
        });
        Ext.apply (this, {
            arrow : arrow,
            items : [
                this.leftView,
                arrow,
                this.rightView
            ],
            buttons : [{
                text : 'Ok',
                handler : function() {
                    var view = this.up().up();
                    
                    var selectedItems = view.getRightView().getStore().data.items;
                    var availableItems = view.getLeftView().getStore().data.items;

                    view.fireEvent("okclicked", view, selectedItems, availableItems);
                }
            }, {
                text : 'Reset',
                handler : function() {
			var view = this.up().up();
			view.fireEvent("resetClicked");
                }
            }]
        });
        this.callParent();
    },
    /**
     * Called when the left move button is clicked
     */
    onMoveLeft : function() {
        var view = this;
        return function() {
            var rightView = view.getRightView();
            var leftView = view.getLeftView();
            var selected = rightView.getSelectionModel().getSelection();
            for (var i=0; i < selected.length; ++i) {
                leftView.getStore().add(selected[i]);    
                rightView.getStore().remove(selected[i]);    
            }
            leftView.getSelectionModel().select(selected);
        };
    },
    /**
     * Called when the right move button is clicked
     */
    onMoveRight : function() {
        var view = this;
        return function() {
            var rightView = view.getRightView();
            var leftView = view.getLeftView();
            var selected = leftView.getSelectionModel().getSelection();
            for (var i=0; i < selected.length; ++i) {
                rightView.getStore().add(selected[i]);    
                leftView.getStore().remove(selected[i]);    
            }
            rightView.getSelectionModel().select(selected);
        };
    },/**
     * Called when the left move button is clicked
     */
    onMoveUp : function() {
        var view = this;
        return function() {
            var rightView = view.getRightView();
            var leftView = view.getLeftView();
            var selectedLeft = leftView.getSelectionModel().getSelection();
            var selectedRight = rightView.getSelectionModel().getSelection();
            var selIndex = [] ; 
            console.log("Selected :")
            console.log(selectedLeft);
        	console.log(selectedRight);
        	if(leftView.getSelectionModel().hasSelection){
        		for (var i=0; i < selectedLeft.length; ++i) {
        			console.log("left selected index :")
        			selIndex[i] = leftView.getStore().find("name" , selectedLeft[i].data.name);
        			if(selIndex[i] == 0 ) return ;
        			selectedLeft[i].index = selIndex[i];
        		}
        		selectedLeft.sort(function (a , b) {return a.index -b.index } );
        		
        		for (var i=0; i < selectedLeft.length; ++i) {
            		console.log("left selected :")
         			leftView.getStore().remove(selectedLeft[i]);
        			leftView.getStore().insert(selectedLeft[i].index -1 , selectedLeft[i]);
         		}
        		leftView.getSelectionModel().select(selectedLeft); 	
        	}
            
            if(rightView.getSelectionModel().hasSelection){
         		for (var i=0; i < selectedRight.length; ++i) {
        			console.log("left selected index :")
        			selIndex[i] = rightView.getStore().find("name" , selectedRight[i].data.name);
        			if(selIndex[i] == 0 ) return ;
        			selectedRight[i].index = selIndex[i];
        		}
         		selectedRight.sort(function (a , b) {return a.index -b.index } );
        	 	for (var j=0; j < selectedRight.length; ++j) {
            		console.log("right selected :")
            		rightView.getStore().remove(selectedRight[j]);
            		rightView.getStore().insert(selectedRight[j].index-1 , selectedRight[j]);              
            	}
            	rightView.getSelectionModel().select(selectedRight); 	
            }
                   
        };
    },
    /**
     * Called when the down move button is clicked
     */
    onMoveDown : function() {
        var view = this;
        return function() {
            var rightView = view.getRightView();
            var leftView = view.getLeftView();
            var selectedLeft = leftView.getSelectionModel().getSelection();
            var selectedRight = rightView.getSelectionModel().getSelection();
            console.log("Selected :")
            var selIndex = [] ; 

            console.log(selectedLeft);
        	console.log(selectedRight);
        	
        	
        	if(leftView.getSelectionModel().hasSelection){
         		for (var i=0; i < selectedLeft.length; ++i) {
        			console.log("left selected index :")
        			selIndex[i] = leftView.getStore().find("name" , selectedLeft[i].data.name);
        			if(selIndex[i] == leftView.getStore().getCount() - 1 ) return ;
         			selectedLeft[i].index = selIndex[i];
        		}	
        		selectedLeft.sort(function (a , b) { return a.index -b.index } );
        		selectedLeft.reverse();
  
        		for (var i=0; i < selectedLeft.length; ++i) {
        			console.log("left selected :")
         			leftView.getStore().remove(selectedLeft[i]);
                	leftView.getStore().insert(selectedLeft[i].index + 1 , selectedLeft[i]);
         		}
        		leftView.getSelectionModel().select(selectedLeft); 	
        	}
        	
            if(rightView.getSelectionModel().hasSelection){
        		for (var i=0; i < selectedRight.length; ++i) {
        			console.log("left selected index :")
        			selIndex[i] = rightView.getStore().find("name" , selectedRight[i].data.name);
        			if(selIndex[i] == rightView.getStore().getCount() - 1 ) return ;
        			selectedRight[i].index = selIndex[i];
        		}
        		selectedRight.sort(function (a , b) {return a.index -b.index } );
        		selectedRight.reverse();
        		for (var j=0; j < selectedRight.length; ++j) {
        			console.log("right selected :")
        			rightView.getStore().remove(selectedRight[j]);
        			rightView.getStore().insert(selectedRight[j].index + 1 , selectedRight[j]);
        		}
            	rightView.getSelectionModel().select(selectedRight); 	
            }           
                     
        };
    },
    /**
     * Get the grid on the left side
     */
    getLeftView : function() {
        return this.items.items[0];
    },
    /**
     * Get the grid on the right side
     */
    getRightView : function() {
        return this.items.items[2];
    },
    /**
     * Get the panel which holds the arrows panel. Instance of Manage.view.ArrowButtons
     */
    getArrowsPanel : function() {
        return this.items.items[1];
    }
});

/**
 * Test implementation of the split view
 */
Ext.define('Manage.view.TestSplitView', {
    extend : 'Manage.view.SplitView',
    leftView : {
        xtype : 'gridpanel',
        multiSelect: true,
        layout : 'fit',
        viewConfig: {
            plugins: {
                ptype: 'gridviewdragdrop'
            }
        },
        store : {
            proxy : 'memory',
            fields : ['name', 'type'],
            data : [
                {"name" : "test1", "type" : "testType1"},
                {"name" : "test2", "type" : "testType2"},
                {"name" : "test3", "type" : "testType3"}
            ]
        },
        columns : [
                    {text: "Name", flex : 1, sortable : true, dataIndex : 'name'},
                    {text: "Type", flex : 1, sortable : true, dataIndex : 'type'}
                ],
        stripeRows : true,
        title : 'Available Fields',
        margins : '0 2 0 0'
    },
    rightView : {
        xtype : 'gridpanel',
        multiSelect: true,
        layout : 'fit',
        viewConfig: {
            plugins: {
                ptype: 'gridviewdragdrop'
            }
        },
        store : {
            proxy : 'memory',
            fields : ['name', 'type'],
            data : [
                {"name" : "test4", "type" : "testType4"},
                {"name" : "test5", "type" : "testType5"},
                {"name" : "test6", "type" : "testType6"}
            ]
        },
        columns : [
                    {text: "Name", flex : 1, sortable : true, dataIndex : 'name'},
                    {text: "Type", flex : 1, sortable : true, dataIndex : 'type'}
                ],
        stripeRows : true,
        title : 'Available Fields',
        margins : '0 2 0 0'
    },
    listeners : {
        okclicked : function(view, selected, available) {
            console.log("ok clicked. make the changes");
            console.log("selected " + selected);
            console.log("available " + available);
        }
    }
});
