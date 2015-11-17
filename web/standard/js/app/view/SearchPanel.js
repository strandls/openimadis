/**
 * Search results view
 */
Ext.define('Manage.view.SearchResults', {
    extend : 'Ext.tree.Panel',
    xtype : 'searchResults',
    alias : 'widget.searchResults',
    rootVisible : false,
    store : 'SearchResults'
});

/**
 * Search panel. Has a search box, along with place for search results
 */
Ext.define('Manage.view.SearchPanel', {
    extend : 'Ext.panel.Panel',
    xtype : 'searchPanel',
    alias : 'widget.searchPanel',
    layout : 'anchor',


    /**
     * the last search query sent to the server
     * @private
     */
    query: '',

    /**
     * @property  the state of the advance query option in the previous query sent to the server
     * @private
     */
    advance: false,

    items : [{
        xtype : 'form',
        title:'',
        bodyPadding : 5,
        anchor: '100% 35%',
        items : [{
            fieldLabel : 'Search text',
            queryMode : 'local',
            xtype : 'combobox',
            labelAlign : 'top',
            id: 'searchterm',
            name : 'q',
            displayField : 'suggestions',
            valueField : 'suggestions',
            allowBlank : false,
            hideTrigger: true,
            typeAhead: true,
            store: {type:'suggest'},
            listeners : {
                change : function(field, newValue, oldValue, opts) {
                	this.up().up().fireEvent("suggestResults", newValue);
                }
            }
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
	dockedItems:[{
		xtype: 'toolbar',
		dock: 'bottom',
		style : 'background-color:white',
		ui: 'footer',
		items: [
			'->', {
			xtype : 'button',
			text : 'Submit',
			id: 'searchSubmitButton',
			formBind : true,
			handler : function() {
				var form = this.up('form').getForm();
				var view = this.up().up().up();
				if (form.isValid()) {
					//setting the last submitted query
					view.query = form.getFields().get('searchterm').getValue();
					view.advance = Ext.getCmp('advanceQuery').getValue();

					form.submit( {
						success : function(form, action) {
							var searchterm = form.getFields().get('searchterm').getValue();
							var advQuery = Ext.getCmp('advanceQuery').getValue();
							var resp = Ext.decode(action.response.responseText);
							var results = resp.results;
							view.items.items[1].update("Found "+resp.count + " records for: <br>" + searchterm);
							view.fireEvent("updateSearchResults", results);
						},
						failure : function(form, action) {
							Ext.Msg.alert("Failed", "Not a valid query string");
						}
					});
				}
			}
		}]           
	}]
    }, {
        xtype : 'panel',
        anchor : '100% 15%',
        bodyPadding : 5,
        border : false,
	autoScroll: true
    }, {
        xtype : 'searchResults',
        border : false,
        //preventHeader : true,
        bodyPadding : 5,
        padding : 5,
        anchor: '100% 50%'
    }]
});
