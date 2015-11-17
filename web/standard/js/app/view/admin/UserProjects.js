/**
 * View for modifying memberships. Functionalities,
 * 1. View members of a particular project in a table
 * 2. Add members to a project
 * 3. Edit membership of a member one by one.
 */
Ext.require(['Manage.view.admin.UserProjectList']);

Ext.define('Manage.view.admin.UserProjects', {
    extend : 'Ext.panel.Panel',
    xtype : 'userProjects',
    alias : 'widget.userProjects',
    title : 'User Projects',
    border : false,
    initComponent : function() {
        this.userChooser = Ext.create('Ext.form.field.ComboBox', {
            fieldLabel : 'Choose User',
            id:'users',
            name : 'userLogin',
            queryMode : 'local',
            displayField : 'login',
            valueField : 'login',
            width : 400,
            editable : true,
	        typeAhead:true,
            store : Ext.create('Manage.store.admin.Users'),
            padding : '10 10 10 20',
            listeners : {
        		
                select : {
        			fn:function(field, records,opts) {
                        this.onUserChange(field.getValue());
                    },
                    scope:this
        		},
        		change : {
        			fn:function(field, newValue, oldValue, opts) {
                    	if(newValue == '' || newValue == null ){
                    		this.onUserChange(newValue);
                    	}
                    },
                    scope:this
        		} 
            }
        });
        this.membershipList = Ext.create('Manage.view.admin.UserProjectList', {
            region : 'center'
        });
        Ext.apply (this, {
            items : [{
                xtype : 'panel',
                region : 'north',
                items : [this.userChooser]
            }, this.membershipList],
            layout : 'border'
        });
        this.callParent();
    },
 
    onUserChange : function(userLogin) {
        var view = this.membershipList;
 
        this.membershipList.getStore().load({
            params : {
            	projectName : view.projectName ,
                userLogin : userLogin 
            },
            callback : function() {
                view.userLogin = userLogin;
            }
        });
    }
});
