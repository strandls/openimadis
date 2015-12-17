/**
 * Summary table view
 * Lists all the information about records of a project in a spreadsheet
 */
Ext.require(['Manage.view.summarytable.AddFieldView', 'Ext.grid.plugin.CellEditing', 'Ext.toolbar.Paging']);

Ext.define('Manage.view.summarytable.SummaryTable', {
	extend:'Ext.grid.Panel',
	xtype:'summarytable',
	alias:'widget.summarytable',
	store:'Manage.store.summarytable.SummaryTablePagingStore',
    autoScroll : true,
    selModel : {
        xtype : 'rowmodel',
        mode : 'MULTI'
    },
    plugins: [
        'headertooltip'
    ],
    initComponent : function() {
        var dockButtons = [{
            xtype : 'button',
            text : 'Select All',
            enableToggle : true,
            id : 'selectAllRecords',
            listeners : {
                toggle : function(button, pressed, opts)  {
                    var table = button.up().up();
                    var count = table.getStore().getProxy().data.length;
                    if (pressed) {
                        button.setText("Selected " + count + " records across all pages");
                        table.fireEvent("selectAllOn", table);
                    } else {
                        button.setText("Select All Records");
                        table.fireEvent("selectAllOff", table);
                    }
                }
            }
        }];
        
        dockButtons.push({
            xtype : 'button',
            text : 'Download',
            tooltip: 'Create Download Link',
            icon : 'images/icons/download.png',
            enableToggle : false,
            id : 'downloadAllRecords',
            handler : function() {
                console.log ("create download link for selected records");
                var table = this.up('summarytable');
                table.fireEvent("downloadRecords", table);
            }
        });
        
        dockButtons.push({
                xtype : 'button',
                text : 'Share',
                tooltip: 'Share Selected Records',
                icon : 'images/icons/allocated.gif',
                id : 'shareRecords',
                handler : function() {
                    var table = this.up('summarytable');
                    table.fireEvent("shareRecords", table);
                }
            });
        
        // Only administrator can delete a record. check and show button based on that
        if (canDeleteRecord) {
            dockButtons.push({
                xtype : 'button',
                text : 'Delete',
                tooltip: 'Delete Selected Records',
                icon : 'images/icons/delete.png',
                id : 'deleteRecords',
                handler : function() {
                    console.log ("delete selected records");
                    var table = this.up('summarytable');
                    table.fireEvent("deleteRecords", table);
                }
            });
            
            dockButtons.push({
                xtype : 'button',
                text : 'Transfer',
                tooltip: 'Transfer Selected Records',
                icon : 'images/icons/allocated.gif',
                id : 'transferRecords',
                handler : function() {
                    var table = this.up('summarytable');
                    table.fireEvent("transferRecords", table);
                }
            });
        }
        Ext.apply (this, {
            dockedItems : [{
                xtype : 'pagingOptions',
                store       : 'Manage.store.summarytable.SummaryTablePagingStore',
                displayInfo : true,
                dock: 'bottom',
                displayMsg  : 'Displaying records {0} - {1} of {2}',
                pageSize : 20,
                emptyMsg    : "No records to display",
                pageSizeOptions : [{pagesize : 20}, {pagesize: 50}, {pagesize: 100}, {pagesize: 200}],
                listeners   : {
                    pagesizeselect: function(size) {
                        var store = this.up().getStore();
                        store.pageSize = size;
                        store.loadPage(1);
                    }
                },
                items : dockButtons
            }]    
        });
        this.callParent();
    },
    
	columns: {
        items : [{
        	 header : 'Input/Output', dataIndex : 'Input/Output', flex:1
        },
        {
            header : '+',
            dataIndex : 'id',
            sortable : false,
            dontSort : true,
            width : 30,
            tooltip : 'Add Property',
            renderer : function(value, metaData) { return ""; }
        }],
        listeners : {
            headerclick : function(g, index, ev) {
                // The last column is the add column by convention.
                // Handle event only for that
                if (index.getIndex() == g.getColumnCount()-1) {
                    g.up().fireEvent("showMenu", index);
                }
            }
        },
        defaults : {
            menuDisabled : true 
        }
    },
   
    listeners : {
        selectAllOn : function() {
            this.doSelectAllOn();
        },
        selectAllOff : function() {
            this.doSelectAllOff();
        },
        select : function() {
            if (this.selectAllOn) {
                // Change in selection has happended when selectAll was on.
                // Disable select all now
                var selectAll = Ext.ComponentQuery.query('#selectAllRecords')[0];
                selectAll.toggle();
            }
        }
    },

    /**
     * Get the list of all selected records. This needs some special handling because of the
     * presence of "Select All" operation. If the "Select All" is on, the selection is across
     * pages. Else, the selection is the current selection from the table
     */
    getRecordIDs : function() {
        var records = new Array(), selected = null;
        if (this.selectAllOn) {
            // Explicitly get all the records from the main store and create list
            selected = this.getStore().getProxy().data;
            if (selected !== null && selected.length > 0) {
	            _.each(selected, function(item) {
	                records.push(item["Record ID"]);    
	            });
	        }
            console.log("select all");
            console.log(selected);
            console.log(selected.length);
        } else {
            // Return from the actual selection.
            selected = this.getSelectionModel().getSelection();
            if (selected !== null && selected.length > 0) {
	            _.each(selected, function(item) {
	                records.push(item.data["Record ID"]);    
	            });
	        }
            console.log("manual selection");
            console.log(selected);
        }
        console.log("selected records");
        console.log(records);
        return records;
    },
    
    selectAllOn : false,

    /**
     * Turn on select all toggle
     */
    doSelectAllOn : function() {
        this.getSelectionModel().selectAll();
        this.selectAllOn = true;
    },

    /**
     * Turn off select all toggle
     */
    doSelectAllOff : function() {
        this.selectAllOn = false;
    }
    
});
