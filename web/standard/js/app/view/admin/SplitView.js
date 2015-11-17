/**
 * Two arrow buttons to move items from a left panel to right
 * and the other way
 */
Ext.define('Manage.view.admin.ArrowButtons', {
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
        layout : 'fit',
        border : false,
        items : [{
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
    },
    /**
     * Get right button
     */
    getRightButton : function() {
        return this.items.items[0].items.items[0];
    },
    /**
     * Get left button
     */
    getLeftButton : function() {
        return this.items.items[0].items.items[2];
    }
});

/**
 * View which has two columns, both backed by their own stores.
 * One can drag and drop from one to another, reorder within 
 * one column and move an item from one column to another via
 * arrow buttons.
 * TODO: docs
 */
Ext.define('Manage.view.admin.SplitView', {
    extend : 'Ext.panel.Panel',
    xtype : 'splitview',
    alias : 'widget.splitview',
    layout : {
        type: 'hbox',
        align: 'stretch'
    },
    width : "100%",
    height : "100%",
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
        var arrow = Ext.create('Manage.view.admin.ArrowButtons', {
            listeners : {
                moveLeft : this.onMoveLeft(),
                moveRight : this.onMoveRight() 
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
            ]
        });
        this.callParent();
    },

    /**
     * Get selected data
     */
    getSelectedData : function() {
        var selectedItems = this.getRightView().getStore().data.items; 
        return selectedItems;
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
     * Get the panel which holds the arrows panel. Instance of Admin.view.ArrowButtons
     */
    getArrowsPanel : function() {
        return this.items.items[1];
    }
});
