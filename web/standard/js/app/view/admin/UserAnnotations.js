/**
 * View for managing user annotations.
 */
Ext.define('Manage.view.admin.UserAnnotations', {
    extend : 'Ext.panel.Panel',
    xtype : 'userAnnotation',
    alias : 'widget.userAnnotation',
    title : 'User Annotations',
    border : false,
    initComponent : function() {
        this.projectChooser = Ext.create('Ext.form.field.ComboBox', {
            fieldLabel : 'Choose Project',
            name : 'project',
            queryMode : 'local',
            displayField : 'name',
            valueField : 'name',
            allowBlank : false,
            width : 400,
            editable : false,
            store : 'admin.Projects',
            padding : '10 10 10 20',
            listeners : {
                scope : this,
                change : function(field, newValue, oldValue, opts) {
                    this.onProjectChange(newValue);
                }
            }
        });
        this.userAnnotationList = Ext.create('Manage.view.admin.UserAnnotationList', {
            region : 'center'
        });
        Ext.apply (this, {
            items : [{
                xtype : 'panel',
                region : 'north',
                items : [this.projectChooser]
            }, this.userAnnotationList],
            layout : 'border'
        });
        this.callParent();
    },

   /**
     * Handler called when project is changed from project chooser
     */
    onProjectChange : function(project) {
        var view = this.userAnnotationList;
        this.userAnnotationList.getStore().load({
            params : {
                projectName : project
            },
            callback : function() {
                view.projectName = project;
            }
        });
    }
});
