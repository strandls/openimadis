/**
 * Store for the fields in the summary table.
 * The fields are dependent on project
 * This store has list of fields for every project
 * Initially the store is empty. When a project is added
 * to the navigator, a list of default fields are added
 * to the store. This can be further manipulated by the user.
 */
Ext.define('Manage.store.summarytable.SummaryTableFields', {
    extend: 'Ext.data.Store',
    proxy : 'memory'
});
