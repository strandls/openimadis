/**
 * Workflow browser. Arranges the workflow items into
 * categories and puts them in an accordion.
 */
Ext.define('Manage.view.Workflows', {
    extend : 'Ext.tree.Panel',
    xtype : 'workflows',
    alias : 'widget.workflows',
    store : 'Manage.store.WorkflowTreeStore',
    rootVisible : false,
    lines : false,
    bodyCls : 'workflowbrowser',
    listeners : {
        itemclick : function (view, record, item, index, e, opts) {
            var childCount = record.childNodes.length;
            if (childCount > 0) {
                if (record.isExpanded())
                    record.collapse();
                else
                    record.expand();
            }
        },
        beforeitemdblclick : function() {
            return false;
        }
    }
});
