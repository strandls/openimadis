/**
 * View for microscope manipulation. This view will have the following,
 * 1. List of microscopes and some properties of microscopes in a table
 * 2. Ability to add a new microscope 
 * 3. Ability to edit a microscope's attributes
 */
Ext.define('Manage.view.admin.MicroscopeList', {
    extend : 'Ext.grid.Panel',
    xtype : 'microscopeList',
    alias : 'widget.microscopeList',
    title : 'Microscopes',

    store: 'admin.Microscopes',

    initComponent : function() {
        Ext.apply (this, {
            columns : [
                {header : 'Name', dataIndex : 'microscopeName', flex : 1},
                {header : 'IP Address', dataIndex : 'ipAddress', flex : 1},
                {header : 'MAC Address', dataIndex : 'macAddress', flex : 1},
		{header : 'Licenses', dataIndex : 'licenses', flex : 1}
            ],
            dockedItems : [{
                xtype : 'toolbar',
                items : [{
                    icon : 'images/icons/add.png',
                    text : 'Add',
                    tooltip : 'Add Microscope',
                    scope : this,
                    handler : this.onAddClick
                }, ' ' , '-', ' ', {
                    icon : 'images/icons/wrench.png',
                    text : 'Edit',
                    tooltip : 'Edit Microscope',
                    scope : this,
                    handler : this.onEditClick
                }, ' ', '-', ' ', {
                    icon : 'images/icons/delete.png',
                    text : 'Remove',
                    tooltip : 'Remove Microscope',
                    scope : this,
                    handler : this.onRemoveClick
                }]
            }]
        });
        this.callParent();
    },

    /**
     * Handler called when add is clicked
     */
    onAddClick : function() {
    	console.log("on add microscope");
        this.fireEvent('onAddMicroscope'); 
    },
    
    /**
     * Handler called when remove is clicked
     */
    onRemoveClick : function() {
	var _this = this;
        var selected = this.getSelectionModel().getSelection();
        if (selected && selected !== null && selected.length === 1) {
	    Ext.MessageBox.confirm("Confirmation", "Are you sure you want to delete microscope " + selected[0].data.microscopeName, 
		function(btn) {
			if(btn === "yes") {
		        	_this.fireEvent('onRemoveMicroscope', selected[0]);
			}
		});
		
        } else {
            Ext.Msg.alert("Warning", "Select microscope to remove from the table");
        }
    },

    /**
     * Handler called when edit is clicked
     */
    onEditClick : function() {
        var selected = this.getSelectionModel().getSelection();
        if (selected && selected !== null && selected.length === 1) {
            this.fireEvent('onEditMicroscope', selected[0]);
        } else {
            Ext.Msg.alert("Warning", "Select microscope to edit from the table");
        }
    }
});
