/**
 * Builds up the AdminWindow based on the user privileges.
 * Based on the views that have to be loaded
 *  - creates the stores
 *  - creates and arranges the views
 *  - creates the front admin page from which to navigate to the other admin views
 *
 *  Stores should not be created when the user does not have permissions. 
 *  So the stores are created based on user privileges.
 *
 *  Views are created based on user privileges.
 *
 *  Most of the logic here is in creating the views and the stores, and the front admin page
 *
 *  NOTE - On adding new views remember to update all the data structures listed below
 *   ------------------------------------------------------------------
 *   Data Structure  |   Key                  |   Value
 *   ------------------------------------------------------------------ 
 *   components          componentName            component
 *   storeOfViews        componentName            [componentStore, ...]
 *   titles		 tabName                  tabTitle
 *   viewTabs            tabName                  [componentName, ...]
 *   ------------------------------------------------------------------
 *
 */
Ext.define('Manage.view.admin.AdminWindow', {
	extend: 'Ext.window.Window',
	xtype: 'adminwindow',
	alias: 'widget.adminwindow',

	closable: true,
	closeAction: 'hide',
	modal: true,
	draggable: false,
	title: 'Administration',

	requires: [
		'Manage.view.admin.MicroscopeProfiles'
	],

	layout: {
		type: 'vbox',
		align: 'stretch'
	},

	/**
	 * mapping of names to components
	 */
	components: {
		"association":         "Manage.view.admin.Association",
		"authCodesList":       "Manage.view.admin.AuthCodesList",
		"clientList":          "Manage.view.admin.ClientList",
		"webClientList":       "Manage.view.admin.WebClientList",
		"downloadsList":       "Manage.view.admin.DownloadsList",
		"licenseList":         "Manage.view.admin.LicenseList",
		"logList":             "Manage.view.admin.LogList",
		"membership":          "Manage.view.admin.Membership",
		"microscopeList":      "Manage.view.admin.MicroscopeList",
		"microscopeProfiles":  "Manage.view.admin.MicroscopeProfiles",
		"projectList":         "Manage.view.admin.ProjectList",
		"publisherList":       "Manage.view.admin.PublisherList",
		"uploadsList":         "Manage.view.admin.UploadsList",
		"unitList":            "Manage.view.admin.UnitList",
		"usageLogs":           "Manage.view.admin.UsageLogs",
		"userAnnotation":      "Manage.view.admin.UserAnnotations",
		"userList":            "Manage.view.admin.UserList",
		"userProjects":        "Manage.view.admin.UserProjects",
		"importLDAPUsers":     "Manage.view.admin.ImportLDAPUsers",
		"cacheStatus":			"Manage.view.admin.CacheStatus",
		"workerStatus":			"Manage.view.admin.WorkerStatus"
	},

	/**
	 * stores that need to be created for the views to work
	 * NOTE - stores can be part of multiple views, 
	 */
	storesOfViews: {
		"association": [
			"Manage.store.admin.Associations",
			"Manage.store.admin.AvailableUnits",
			"Manage.store.admin.Projects"
		],
		"authCodesList": [
			"Manage.store.admin.AuthCodes"
		],
		"clientList": [
			"Manage.store.admin.Clients"
		],
		"webClientList": [
		    "Manage.store.admin.WebClients"
		],
		"downloadsList": [
			"Manage.store.admin.Downloads"
		],
		"licenseList": [
			"Manage.store.admin.Licenses"
		],
		"logList": [
			"Manage.store.admin.Logs"
		],
		"membership": [
			"Manage.store.admin.AvailableUsers",
			"Manage.store.admin.Memberships",
			"Manage.store.admin.Projects",
			"Manage.store.admin.UserRoles"
		],
		"microscopeProfiles": [
			"Manage.store.admin.MicroscopeProfiles"
		],
		"projectList": [
			"Manage.store.admin.Projects"
		], 
		"publisherList": [
			"Manage.store.admin.Publishers"
		],
		"unitList": [
			"Manage.store.admin.Units"
		],
		"uploadsList": [
			"Manage.store.admin.Uploads"
		],
		"usageLogs": [
			"Manage.store.admin.UsageLogs",
			"Manage.store.admin.Users"
		],
		"userAnnotation": [
			"Manage.store.admin.Projects",
			"Manage.store.admin.UserAnnotations"
		],
		"userList": [
			"Manage.store.admin.Users",
			"Manage.store.admin.AvailableLDAPUsersStore",
			"Manage.store.admin.SelectedLDAPUsersStore",
			"Manage.store.admin.UserRanks"
		],
		"userProjects": [
			"Manage.store.admin.UserProjects",
			"Manage.store.admin.Users"
		],
		"importLDAPUsers": [
		    "Manage.store.admin.AvailableLDAPUsersStore",
			"Manage.store.admin.SelectedLDAPUsersStore",
			"Manage.store.admin.UserRanks"
		],
		"cacheStatus": [
			"Manage.store.admin.Cache"
		],
		"workerStatus": [
		    "Manage.store.admin.Worker"
		]
	},
		
	
	/**
	 * titles of the different views
	 * Also used as itemId to change card active item
	 */
	titles: {
		'logs': 'Logs',
		'microscopes': 'Microscopes',
		'projects': 'Projects',
		'unitList': 'Teams',
		'usage': 'Usage',
		'userList': 'Users'
	},

	/**
	 * the organization of the tabs into views
	 */
	viewTabs: {
		'logs': ['usageLogs', 'logList'],
		'microscopes': ['microscopeList', 'microscopeProfiles'],
		'projects': ['projectList', 'membership', 'association', 'userAnnotation'],
		'unitList': ['unitList'],
		'usage': ['licenseList', 'authCodesList', 'downloadsList', 'publisherList', 'uploadsList', 'webClientList', 'clientList', 'cacheStatus', 'workerStatus'],
		'userList': ['userList', 'userProjects', 'importLDAPUsers'],
	},

	/**
	 * itemId of the front page in adminwindow
	 */
	frontId: 'front',

	height: 500,
	width: 1000,

	/**
	 * create a panel containing a row of buttons.
	 * If number of buttons is less than n then create dummy buttons
	 * @param {Number} n the number of buttons to create
	 * @param {Ext.button.Button[]} buttons the buttons to add in this row
	 * @return {Ext.panel.Panel} a pnel containing all the buttons
	 */
	createRow: function(n, buttons) {
		var panel = Ext.create('Ext.panel.Panel', {
				layout: {
					type: 'hbox',
					align: 'stretch'
				},

				defaults: {
					flex: 1
				}
		});

		var i;
		for(i = 0; i < buttons.length; i++) {
			panel.add(buttons[i]);
		}

		for(; i < n; i++) {
			var btn = Ext.create('Ext.button.Button');
			panel.add(btn);
		}
		
		return panel;
	},

	/*
	 * NOTE - have to dynamically create objects based on user privileges.
	 * Hence models, stores are not added in the app.js store, model array.
	 * If the stores were to be added then they would not be loaded, due to
	 * lack of privileges.
	 */
	initComponent: function() {
		var viewsShown = []; // the views that will be shown based on user privileges
		var buttonsShown = []; // the buttons that will be shown based on user privileges

		// create the stores
		// NOTE - order is important
		//  first create stores, then create views that depend on the stores
		//  since the views use the storeId to attach stores
		this.createStores();

		// create all the views and buttons
		var name;
		for(name in this.viewTabs) {
			if(this.canShow(name)) {
				var btn = this.createButton(name);
				buttonsShown.push(btn);

				var view = this.createView(name);
				viewsShown.push(view);
			}
		}

		// create the front admin page that will have buttons to 
		// take the user to the appropriate views
		var len = buttonsShown.length;
		var row1 = this.createRow(3, 
					len <= 0 ? []:
					len >= 3 ? buttonsShown.slice(0, 3) : 
					buttonsShown.slice(0, len));
		var row2 = this.createRow(3, 
					len <= 3 ? [] : 
					len >= 6 ? buttonsShown.slice(3, 6) : 
					buttonsShown.slice(3, len));

		var cards = Ext.create('Ext.panel.Panel', {
			itemId: 'cards',
			
			flex: 1,
			layout: 'card',

			items: [{
				// front admin page
				xtype: 'panel',

				layout: {
					type: 'vbox',
					align: 'stretch'
				},

				defaults: {
					flex: 1
				},

				items: [
					row1,
					row2
				],

				itemId: this.frontId
			}]
		});

		// add all the views that the user has privilege to access
		var i;
		for(i = 0; i < viewsShown.length; i++) {
			cards.add(viewsShown[i]);
		}

		Ext.apply(this, {
			items: [cards, {
				// space that is used to add, edit things
				xtype: 'panel',
				hidden: true,
				id: 'adminSpace',
				height: 150,
				width: '100%'
			}]
		});

		// on show load the front view
		var me = this;
		this.on("show",
			function() {
				var layout = me.getComponent('cards').getLayout();
				layout.setActiveItem(me.frontId);
				me.down('#adminSpace').hide();
			},
			this
		);
		this.callParent();
	},

	/**
	 * create stores based on the views that the user can access
	 */
	createStores: function() {
		var i;
		var allStores = []; //names of stores to create

		// same store may be needed in multiple views
		// Hence intersecting all the required stores to get a set of distinct store names
		for(i = 0; i < viewsToShow.length; i++) {
			//TODO remove this checker ???
			var view = viewsToShow[i];
			if(!(view in this.storesOfViews)) {
				continue;
			}

			var stores = this.storesOfViews[view];
			var j;
			for(j = 0; j < stores.length; j++) {
				//if store not present add it
				var store = stores[j];
				if(allStores.indexOf(store) === -1) {
					allStores.push(store);
				}
			}
		}

		//create the stores
		for(i = 0; i < allStores.length; i++) {
			// stores are created with their storeId's and can be referenced in the views
			Ext.create(allStores[i]);
		}
	},
		
	/**
	 * checks whether the view can be shown
	 * @param {String} view name of the view to check
	 * @return {Boolean} true if view can be shown
	 */
	canShow: function(view) {
		var views = this.viewTabs[view];
		var i;
		for(i = 0; i < views.length; i++) {
			if(viewsToShow.indexOf(views[i]) !== -1) {
				return true;
			}
		}

		return false;
	},

	/**
	 * create the button for the given name
	 * @param {String} name the name for which to create the button
	 * @return {Ext.button.Button} button for the given name
	 */
	createButton: function(view) {
		var me = this;
		var title = this.titles[view];
		var btn = Ext.create('Ext.button.Button', {
				text: title,
				handler: function() {
					me.setActiveItem(title);
				}
		});

		return btn;
	},
	
	/**
	 * create the tab component for the given view
	 * @param {String} view name of view to build component for
	 * @return {Component} the component built for the view
	 */
	createView: function(view) {
		var views = this.viewTabs[view];
		var title = this.titles[view];
		var me = this;
		var tabs = [];
		
		// create the tabs to show in this view
		var i;
		for(i = 0; i < views.length; i++) {
			if(viewsToShow.indexOf(views[i]) !== -1) {
				tabs.push(views[i]);
			}
		}

		if(tabs.length === 1) {
			component = this.createComponent(tabs[0], title);
		} else {
			var items = [];
			for(i = 0; i < tabs.length; i++) {
				var ct = this.createComponent(tabs[i]);
				items.push(ct);
			}

			component = Ext.create('Ext.tab.Panel', {
					title: title,
					layout: 'fit',

					items: items,

					itemId: title, // used to change active item

					closable: true,
					closeAction: 'hide',

					listeners: {
						tabchange: {
							// hide adminSpace on tabchange
							fn: function() {
								me.down('#adminSpace').hide();
							},
							scope: this
						}
					}
			});
		}

		component.on("close", 
			function() {
				me.setActiveItem(me.frontId);
				var pan = me.down('#adminSpace');
				pan.setHeight(150);
				pan.hide();
			},
			this
		);

		return component;
	},

	
	/**
	 * Create a component 
	 * @param {String} xtype the component to be created
	 * @param {String} itemId the itemId of the created component, if "not undefined" signifies that the component is a single item
	 * @return {Component} the created component
	 */
	createComponent: function(xtype, itemId) {
		var components = this.components;

		var comp;
		var me = this;
		if(typeof itemId === 'undefined') {
			comp = Ext.create(components[xtype]);
		} else {
			comp = Ext.create(components[xtype], {
				itemId: itemId, // useful in changing card items
				
				closable: true,
				closeAction: 'hide'
			});
		}

		return comp;
	},

	/**
	 * sets the active item
	 * @param {String} name the component to make active
	 */
	setActiveItem: function(name) {
		var slideInFrom = 'r';
		var slideOutTo = "l";
		if(name === this.frontId) {
			slideInFrom = "l";
			slideOutTo = "r";
		}

		var layout = this.getComponent('cards').getLayout();
		var inItem = this.getComponent('cards').getComponent(name);
		
		layout.setActiveItem(name);
		inItem.getEl().slideIn(slideInFrom, {
			duration: 500
		});
	}
});
