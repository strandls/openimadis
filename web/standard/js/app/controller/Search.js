/**
 * controls for all the search related activity
 */
Ext.define('Manage.controller.Search', {
	extend: 'Ext.app.Controller',

	stores: [
		'SearchResults', 'SearchThumbnails'
	],

	refs: [{
		ref: 'searchPanel',
		selector: 'searchPanel'
	}, {
		ref: 'searchResults',
		selector: 'searchResults'
	}, {
		ref: 'searchButton',
		selector: '#searchButton'
	}, {
		ref: 'searchTerm',
		selector: '#searchterm'
	},{
		ref: 'searchTextField',
		selector: '#searchTextField'		
	},{
		ref: 'searchThumbnails',
		selector: '#searchthumbnails'		
	}],

	init: function() {
		this.control({
			'searchPanel': {
				'updateSearchResults': this.onUpdateSearchResults,
				'suggestResults': this.onSuggestResults
			},
			'searchResults': {
				itemclick: this.onNodeClick
			},
			'#searchViewButton': {
				click: this.onChange
			},
			'headers': {
				suggestResults: this.onSuggestResultsHeader
			}
		});
	},

	/**
	 * 
	 */
	onSuggestResults : function(searchText){
		var combo=this.getSearchTerm();
		var store=combo.getStore();
		
		Ext.Ajax.request({
            method : 'GET',
            url : '../project/suggest',
            params : {
                'q' : searchText
            },
            success : function (result, response) {
            	var suggestions = Ext.decode(result.responseText);

            	var data=[];           	
            	if(suggestions.results!=null){
                	for ( var i = 0, c = suggestions.results.length; i < c; i++ ) {
                	    data[i] = [suggestions.results[i]];
                	}               	
            	}
            	store.loadData(data);
            }
		});
	},
	
	/**
	 * 
	 */
	onSuggestResultsHeader : function(searchText){
		var combo=this.getSearchTextField();
		var store=combo.getStore();
		
		Ext.Ajax.request({
            method : 'GET',
            url : '../project/suggest',
            params : {
                'q' : searchText
            },
            success : function (result, response) {
            	var suggestions = Ext.decode(result.responseText);

            	var data=[];           	
            	if(suggestions.results!=null){
                	for ( var i = 0, c = suggestions.results.length; i < c; i++ ) {
                	    data[i] = [suggestions.results[i]];
                	}               	
            	}
            	store.loadData(data);
            }
		});
	},
	
	/**
	* Update the search results tree with the results found
	* @param {JSON} results results of the search in JSON
	*/
	onUpdateSearchResults : function(results) {
		var searchTree = this.getSearchResultsStore();
		var root = searchTree.getRootNode();
		var thumbStore = this.getSearchThumbnailsStore();
		
		// remove the previous search results
		thumbStore.removeAll();
		root.removeAll();
		
		var parentNode = null;

		//load current search results
		if (results && results !== null)
		{
			var key;
			for (key in results)
			{
				if (key.indexOf("Record Metadata") != -1 || key.indexOf("User Annotations") != -1 || key.indexOf("History") != -1 || key.indexOf("Misc") != -1)
				{
					var nodeName = key.substring(parentNode.data.projectName.length+1,key.length);
					parentNode.appendChild(this.constructSearchNode(nodeName, parentNode.data.projectName, results[key]), true);
					console.log(parentNode);
				}
				else
				{
					parentNode = this.constructSearchNode(key, key, results[key], false);
					root.appendChild(parentNode);
				}
			}
		}

		//to help functions that need the store to be loaded 
		searchTree.fireEvent('load');

		//select the first search result
		if(root.hasChildNodes()) {
			var node = root.getChildAt(0);
			var searchPanel = this.getSearchResults();
			var selModel = searchPanel.getSelectionModel();

			selModel.select(node);
			searchPanel.fireEvent('itemclick', searchPanel.getView(), node);
		} 
	},

	/**
	* Construct search result node
	*/
	constructSearchNode : function(nodeText, projectName, records, isLeaf) {
		var projectNode = Ext.create('Manage.model.SearchResult', {
			leaf: isLeaf,
			text : nodeText + " ("+records.length+")",
			projectName : projectName,
			recordids : records
		});
		return projectNode;
	},

	/**
	 * change the thumbnails in the Search Panel
	 */
	onNodeClick: function(view, node) {
		var i = 0;
		var tdata = [];
		var next = '';
		var guid = '';
		var store = this.getSearchThumbnailsStore();
		var records = node.get('recordids');

		for(i = 0; i < records.length; i++) {
			guid = records[i];
			next = {
				id: guid,
				imagesource: '../project/getThumbnail?recordid=' + guid
			};
			tdata.push(next);
		}
		store.loadData(tdata);
	},

	/**
	 * change the viewed thumbnails
	 */
	onChange: function() {
		var search = this.getSearchResults();
		var node = search.getSelectionModel().getLastSelected();
		
		var selectedRecords = this.getSearchThumbnails().getSelectionModel().getSelection();
		var selectedrecordids =[];
		for(var k=0;k<selectedRecords.length;k++)
			selectedrecordids.push(selectedRecords[k].data.id);
		
		var recordids = node.get('recordids');
		
		if(selectedRecords.length!==0){
			recordids=selectedrecordids;
		}
		//if no node selected do nothing
		if(typeof(node) !== 'undefined') {
			search.fireEvent('loadRecords', node.get('projectName'), recordids);
		}

		//close the window
		var button = this.getSearchButton();
		button.fireEvent('click');
	}


});
