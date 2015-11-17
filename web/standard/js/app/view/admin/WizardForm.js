Ext.define('Manage.view.admin.WizardForm', {
  extend  : 'Ext.form.Panel',
  alias   : 'widget.wizard',
  layout  : 'card',
  itemId  : 'wizardForm',
  defaults: {
    bodyPadding: 5
  },
  items: [{
    xtype: 'WIaddProject'
  },
  {
    xtype: 'WIaddTeamProjectAssociations'
  },{
    xtype: 'WIaddMember'

  }]
});

Ext.define('Utils', {
    singleton : true,
    ProjectName: "null"

});
