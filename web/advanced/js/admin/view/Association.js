/**
 * View for modifying memberships. Functionalities,
 * 1. View members of a particular project in a table
 * 2. Add members to a project
 * 3. Edit membership of a member one by one.
 */
Ext.require(['Admin.view.AssociationList']);

Ext.define('Admin.view.Association', {
    extend : 'Ext.panel.Panel',
    xtype : 'association',
    alias : 'widget.association',
    title : 'Team-Project Association',
    border : false,
    initComponent : function() {
        this.projectChooser = Ext.create('Ext.form.field.ComboBox', {
            fieldLabel : 'Choose Project',
            name : 'project',
            queryMode : 'local',
            displayField : 'name',
            valueField : 'name',
            allowBlank : true,
            width : 400,
            editable : true,
            store : 'Admin.store.ProjectStore',
            padding : '10 10 10 20',
            listeners : {
                scope : this,
                change : function(field, newValue, oldValue, opts) {
                    this.onProjectChange(newValue);
                }
            }
        });
        this.membershipList = Ext.create('Admin.view.AssociationList', {
            region : 'center'
        });
        Ext.apply (this, {
            items : [{
                xtype : 'panel',
                region : 'north',
                items : [this.projectChooser
                         //, this.unitChooser
                        ]
            }, this.membershipList],
            layout : 'border'
        });
        this.callParent();
    },

   /**
     * Handler called when project is changed from project chooser
     */
    onProjectChange : function(project) {
    	//var unitName = this.down('combobox[name=unit]').getValue();
    	
        var view = this.membershipList;
        this.membershipList.getStore().load({
            params : {
                projectName : project
                //,
                //unitName : unitName
            },
            callback : function() {
                view.projectName = project;
            }
        });
    }
});
