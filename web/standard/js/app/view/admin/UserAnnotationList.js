/**
 * View for managing user annotations for a project
 */
Ext.define('Manage.view.admin.UserAnnotationList', {
    extend : 'Ext.grid.Panel',
    xtype : 'userAnnotationList',
    alias : 'widget.userAnnotationList',
    title : 'User Annotations',

    store: 'admin.UserAnnotations',

    initComponent : function() {
        Ext.apply (this, {
            columns : [
                {header : 'Name', dataIndex : 'name', flex : 1},
                {header : 'Type', dataIndex : 'type', flex : 1}
            ],
            dockedItems : [{
                xtype : 'toolbar',
                items : [{
                    icon : 'images/icons/delete.png',
                    text : 'Remove',
                    tooltip : 'Remove Selected Annotations',
                    scope : this,
                    handler : this.onRemoveClick
                }]
            }]
        });
        this.callParent();
    },

    /**
     * Handler called when remove is clicked
     */
    onRemoveClick : function() {
        var selected = this.getSelectionModel().getSelection();
        if (selected && selected !== null && selected.length === 1) {
            this.fireEvent('removeAnnotation', selected[0]);
        } else {
            Ext.Msg.alert("Warning", "Select entry to remove from the table");
        }
    }
});
