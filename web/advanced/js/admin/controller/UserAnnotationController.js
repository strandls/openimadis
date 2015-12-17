/**
 * User annotation manipulation related actions
 */
Ext.define ('Admin.controller.UserAnnotationController', {
    extend : 'Ext.app.Controller',

    refs : [{
        ref : 'userAnnotationList',
        selector : 'userAnnotationList'
    }],
    
    init : function() {
        this.control({
            'userAnnotationList' : {
                removeAnnotation : this.onRemoveAnnotation
            }
        });
    },

   /**
     * Remove user annotation
     */
    onRemoveAnnotation : function(annotation) {
        var view = this.getUserAnnotationList();
        Ext.Msg.confirm("Remove", "Are you sure you want to remove " + annotation.data.name + " from the project " + annotation.data.projectName + " ?", function(id) {
            if (id === "yes") {
                Ext.Ajax.request({
                    method : 'POST',
                    url : '../admin/removeUserAnnotation',
                    params : {
                        name : annotation.data.name,
                        projectName : annotation.data.projectName
                    },
                    success : function (result, response){
                        Ext.Msg.alert("Removed", "Removed annotation " + annotation.data.name + " from project " + annotation.data.projectName);
                        view.store.load({
                            params : {
                                projectName : annotation.data.projectName        
                            }
                        });
                    },
                    failure : function(result, request) {
                        showErrorMessage(result.responseText, "Failed to remove annotation");
                    } 
                });
            }
        });
    }
});
