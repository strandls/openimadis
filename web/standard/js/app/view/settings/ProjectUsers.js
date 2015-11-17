Ext.require(['Manage.view.settings.ProjectUsersList']);

Ext.define('Manage.view.settings.ProjectUsers', {
    extend : 'Ext.panel.Panel',
    xtype : 'projectUsers',
    alias : 'widget.projectUsers',
    title : 'Project Chooser',
    width : 300,
    height : 500,
    border : false,
    
    projectName : '',
    
    initComponent : function() {
    	var projectName = this.projectName;
        this.projectChooser = Ext.create('Ext.form.field.ComboBox', {
            fieldLabel : 'Project',
            name : 'name',
            queryMode : 'local',
            displayField : 'name',
            valueField : 'name',
            value: projectName,
            allowBlank : false,
            editable : false,
            height: 50,
            width: 300,
            store : 'ProjectNames',
            padding : '5 5 5 5',
            listeners : {
                scope : this,
                change : function(field, newValue, oldValue, opts) {
                    this.onProjectChange(newValue);
                }
            }
        });
        this.membershipList = Ext.create('Manage.view.settings.ProjectUsersList', {
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
