/**
 * Profile manipulation related actions
 */
Ext.define ('Manage.controller.admin.MicroscopeProfiles', {
	extend : 'Ext.app.Controller',

	refs : [{
		ref : 'profileList',
		selector : 'profileList'
	}, {
		ref: 'adminSpace',
		selector: '#adminSpace'
	}],

	init : function() {
		this.control({
			'profileList' : {
				onAddProfile : this.onAddProfile
			},
			'addProfile' : {
				refreshList : this.onRefreshList
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
	* Add new profile
	*/
	onAddProfile : function(microscopeName) {
		var comp = Ext.create ('Manage.view.admin.AddProfile', {
				microscopeName : microscopeName   
		});
		this.addToAdminSpace(comp, 'Add New Profile');
		this.getAdminSpace().setHeight(190);
	},

	/**
	* Refresh the profile listing
	*/
	onRefreshList : function(microscope) {
		var profileList = this.getProfileList();
		profileList.store.load({
			params : {
				microscopeName : microscope
			}
		}); 
	}
});
