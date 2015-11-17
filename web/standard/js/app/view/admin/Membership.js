/**
 * View for modifying memberships. Functionalities,
 * 1. View members of a particular project in a table
 * 2. Add members to a project
 * 3. Edit membership of a member one by one.
 */
Ext.define('Manage.view.admin.Membership', {
    extend : 'Ext.panel.Panel',
    xtype : 'membership',
    alias : 'widget.membership',
    title : 'Memberships',
    border : false,
    initComponent : function() {
        this.projectChooser = Ext.create('Ext.form.field.ComboBox', {
            fieldLabel : 'Choose Project',       
            name : 'project',
            queryMode : 'local',
            displayField : 'name',
            valueField : 'name',
            width : 400,
            editable : true,
	        typeAhead:true,
	        store:Ext.create('Manage.store.admin.Projects'),
            padding : '10 10 10 20',
            listeners : {
        		
                select : {
        			fn:function(field, records,opts) {
                        this.onProjectChange(field.getValue());
                    },
                    scope:this
        		},
        		change : {
        			fn:function(field, newValue, oldValue, opts) {
                    	if(newValue == '' || newValue == null ){
                    		this.onProjectChange(newValue);
                    	}
                    },
                    scope:this
        		} 
            }
        });

        this.membershipList = Ext.create('Manage.view.admin.MembershipList', {
            region : 'center'
        });       
        
        Ext.apply (this, {
            items : [{
                xtype : 'panel',
                region : 'north',
                items : [this.projectChooser]
            }, this.membershipList],
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
                projectName : project,
                userLogin : view.userLogin
            },
            callback : function() {
                view.projectName = project;
             }
        });
    } , 
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
