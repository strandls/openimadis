/**
 * Utility functions used by all other controllers
 */
Ext.define ('Manage.controller.admin.Utils', {
	extend : 'Ext.app.Controller',

	refs : [{
		ref: 'adminSpace',
		selector: '#adminSpace'
	}],


	/**
	 * adds the given Component to adminSpace
	 * @param {Ext.Component} comp the component to add
	 * @param {String} title the title to set the component
	 */
	addToAdminSpace: function(comp, title) {
		var space = this.getAdminSpace();
		space.removeAll();

		space.add(comp);
		space.setTitle(title);

		space.show();
		
		space.doLayout();// sometimes the height is not correctly calculated
	}
});
