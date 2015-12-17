/**
 * Search results view
 */
Ext.define('Manage.view.SearchResults', {
    extend : 'Ext.tree.Panel',
    xtype : 'searchResults',
    alias : 'widget.searchResults',
    rootVisible : false,
    store : 'Manage.store.SearchResultsStore'
});

/**
 * Search panel. Has a search box, along with place for search results
 */
Ext.define('Manage.view.SearchPanel', {
    extend : 'Ext.panel.Panel',
    xtype : 'searchPanel',
    alias : 'widget.searchPanel',
    layout : 'anchor',
    items : [{
        xtype : 'form',
        title:'',
        bodyPadding : 5,
        anchor: '100% 25%',
        items : [{
            fieldLabel : 'Search text',
            xtype : 'textfield',
            labelAlign : 'top',
            id: 'searchterm',
            name : 'q',
            allowBlank : false
        },{
            xtype      : 'fieldcontainer',
            fieldLabel : 'Advance Query',
            defaultType: 'checkboxfield',
            items: [
                {
                    boxLabel  : '',
                    name      : 'advanceQuery',
                    inputValue: '1',
                    id        : 'advanceQuery'
                }
            ]
        },{
        	xtype : 'label',
            html : '<a href="../search-guide.html" target="_blank">Advance Query Syntax Help</a>'
        }
        ],
        url : '../project/search',
        method : 'get',
        border : false,
        buttons : [{
            text : 'Submit',
            formBind : true,
            handler : function() {
                var form = this.up('form').getForm();
                var view = this.up().up().up();
                if (form.isValid()) {
                    form.submit( {
                        success : function(form, action) {
                        	var searchterm = form.getFields().get('searchterm').getValue();
                        	var advQuery = Ext.getCmp('advanceQuery').getValue();
                            var resp = Ext.decode(action.response.responseText);
                            var results = resp.results;
                            view.items.items[1].update("Found "+resp.count + " records for: " + searchterm);
                            view.fireEvent("updateSearchResults", results);
                        },
                        failure : function(form, action) {
                            Ext.Msg.alert("Failed", "Not a valid query string");
                        }
                    });
                }
            }
        }]           
    }, {
        xtype : 'panel',
        anchor : '100% 5%',
        bodyPadding : 5,
        border : false
    }, {
        xtype : 'searchResults',
        border : false,
        //preventHeader : true,
        bodyPadding : 5,
        padding : 5,
        anchor: '100% 70%'
    }]
});
