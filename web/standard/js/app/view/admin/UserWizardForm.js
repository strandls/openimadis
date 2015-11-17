Ext.define('Manage.view.admin.UserWizardForm', {
  extend  : 'Ext.form.Panel',
  alias   : 'widget.UserWizard',
  layout  : 'card',
  itemId  : 'UserWizardForm',
  defaults: {
    bodyPadding: 5
  },
  items: [{
    xtype: 'WIaddUser'
  },{
    xtype: 'WIaddUserToProject'
  }]
});

Ext.define('UserLogin', {
    singleton : true,
    userLogin: "null"

});