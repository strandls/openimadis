Ext.define('Manage.controller.OverlayController', {
	extend : 'Ext.app.Controller',
	refs : [
	],

	init : function() {
		// listen to the change in navigator node selection ..
		this.control( {
			'navigator' : {
				fieldOverlaySelector : this.onFieldOverlaySelector,
				overlayFieldsChanged: this.onOverlayFieldsChange
			}
		});
	},
	
	stores : ['settings.FieldStore'],
	
	onFieldSelector : function () {
	    var fieldStore = this.getSettingsFieldStoreStore();
	    var _this = this;
	    var projectName = this.activeProject;
	    fieldStore.load ({
	        params : {
	            projectName : projectName
	        },
	        callback : function(records, operation, success) {
	            if (!success)
	                Ext.Msg.Alert("Warning", "Cannot load project fields");
	            else
	                _this.showOverlayFieldSelector();
	        }
	    });
	},
	
	showOverlayFieldSelector : function() {
	    
	    var overlayFields = Ext.data.StoreManager.get("Manage.store.settings.OverlayFieldStore");
	    var availableFields = Ext.data.StoreManager.get("Manage.store.settings.AvailableFieldStore");
	
	    // Old data
	    var overlayFieldsOld = overlayFields.data.items.slice();
	    var availableFieldsOld = availableFields.data.items.slice();
	
	    var navigator = this.getNavigator();
	    var _this = this;
	    Ext.create('Ext.window.Window', {
	        title : 'Choose overlay fields',
	        layout : 'fit',
	        items : [{
	            xtype : 'fieldChooser',
	            listeners : {
	                'okclicked' : function(view, selected, available) {
	                    if (_this.hasFieldsChanged(selectedFieldsOld, selected)) 
	                        navigator.fireEvent("overlayFieldsChanged", selected);
	                }
	            }
	        }],
	        width : 500,
	        height : 400
	    }).show(); 
	},
	
	hasFieldsChanged : function(selectedFields, newSelectedFields) {
	    var fieldsChanged = false;
	    // Compare the arrays and set the flag fieldsChanged if there is any
		// change
	    if (selectedFields.length == newSelectedFields.length) {
	        for (var i=0; i < selectedFields.length ; ++i) {
	            if (selectedFields[i] != newSelectedFields[i]) {
	                fieldsChanged = true;
	                break;
	            }
	        }
	    } else
	        fieldsChanged = true;
	    return fieldsChanged;
	},
	
	onOverlayFieldsChange : function(selectedFields) {
	    console.log("Overlay fields changed");
	    var projectName = this.activeProject;
	    var chosenFields = new Array();
	    var i;
	    
	    for (i=0; i < selectedFields.length; ++i ) {
	        chosenFields.push(selectedFields[i].data.name);
	    }
	    // Write to server
	    Ext.Ajax.request({
	        method : 'POST',
	        url : '../project/setOverlayFieldsChosen',
	        params : {
	            projectName : projectName,
	            chosenFields : Ext.encode(chosenFields)
	        },
	        success : function(result, req) {
	        // Reset node for project
	            
	        },
	        failure : function(result, req) {
	            showErrorMessage(result.responseText, "Field saving to server failed");
	        }
	    });
	}
});