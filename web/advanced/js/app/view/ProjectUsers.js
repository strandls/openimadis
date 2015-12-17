Ext.require(['Manage.view.ProjectUsersList']);

Ext.define('Manage.view.ProjectUsers', {
    extend : 'Ext.panel.Panel',
    xtype : 'projectUsers',
    alias : 'widget.projectUsers',
    title : 'Project Chooser',
    width : 300,
    height : 500,
    border : false,
    initComponent : function() {
        this.projectChooser = Ext.create('Ext.form.field.ComboBox', {
            fieldLabel : 'Choose Project',
            name : 'name',
            queryMode : 'local',
            displayField : 'name',
            valueField : 'name',
            allowBlank : false,
            editable : false,
            height: 100,
            width: 300,
            store : 'Manage.store.ProjectNameStore',
            padding : '10 10 10 20',
            listeners : {
                scope : this,
                change : function(field, newValue, oldValue, opts) {
                    this.onProjectChange(newValue);
                }
            }
        });
        this.membershipList = Ext.create('Manage.view.ProjectUsersList', {
            region : 'center'
        });
        Ext.apply (this, {
            items : [
                {
	                xtype : 'panel',
	                region : 'north',
	                items : [this.projectChooser] 
            	}
                , 
            	this.membershipList
            ],
            layout : 'border'
        });
        this.callParent();
    },

   /**
     * Handler called when project is changed from project chooser
     */
    onProjectChange : function(project) {
        var view = this.membershipList;
        this.membershipList.getStore().load({
            params : {
                projectName : project
            },
            callback : function() {
                view.projectName = project;
            }
        });
    }
});
