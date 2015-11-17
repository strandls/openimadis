/**
 * Controller for the Usage tab
 */
Ext.define ('Manage.controller.admin.Usage', {
	extend : 'Ext.app.Controller',

	refs: [{
		ref: 'publisherList',
		selector: 'publisherList'
	},{
		ref: 'clientList',
		selector: 'clientList'
	}, {
		ref: 'adminSpace',
		selector: '#adminSpace'
	}],

	init : function() {
		this.control({
			'publisherList' : {
				addpublisher : this.onAddPublisher
			},'clientList' : {
				addClient : this.onAddClient,
				removeClient: this.onRemoveClient
			},
			'addPublisher': {
				refreshlist: this.onRefreshPublisherList
			},
			'addClient': {
				refreshlist: this.onRefreshClientList
			}
		});
	},

	/**
	 * adds the given Component to adminSpace
	 * @param {Ext.Component} comp the component to add
	 * @param {String} title the title to set the component
	 */
	addToAdminSpace: function(comp, title) {
		var ctr = this.getController('admin.Utils');
		ctr.addToAdminSpace(comp, title);
	},

	/**
	* Add new publisher ui
	*/
	onAddPublisher : function() {
		var comp = Ext.create('Manage.view.admin.AddPublisher');
		this.addToAdminSpace(comp, "Add new publisher");
		this.getAdminSpace().setHeight(200);
	},
	
	/**
	 * add new client ui
	 */
	onAddClient : function() {
		var comp = Ext.create('Manage.view.admin.AddClient');
		this.addToAdminSpace(comp, "Add new client");
		this.getAdminSpace().setHeight(200);
	},
	
	/**
	 * remove existing client
	 */
	onRemoveClient : function(id) {
		var _this = this;
		Ext.Ajax.request({
			method : 'POST',
			url : '../compute/removeClient',
			params : {
				clientID : id
			},
			success : function(result, response) {
				Ext.Msg.alert("Client Removed", "Client "+id+" is removed successfully");
				_this.onRefreshClientList();
			},
			failure: function(result, response) {
				Ext.Msg.alert("Failure", "Failed to remove the client "+id);
			}
		});
	},

	/**
	 * refresh the publisher list
	 */
	onRefreshPublisherList: function() {
		var publisherList = this.getPublisherList();
		publisherList.getStore().load();
	},
	
	/**
	 * refresh the client list
	 */
	onRefreshClientList: function() {
		var publisherList = this.getClientList();
		publisherList.store.load();
	}
});
