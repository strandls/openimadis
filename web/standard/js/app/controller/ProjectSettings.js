/**
 * All controls related to Project settings
 */
Ext.define('Manage.controller.ProjectSettings', {
	extend: 'Ext.app.Controller',

	controllers: [
		'Thumbnails'
	],

	stores: [
		'ProjectFieldChoosers', 'ProjectSelectedFields', 'LegendFieldStore'
	],
	
	refs: [{
		ref: 'legendYLocation',
		selector: 'settings #yLocationRadio'
	},{
		ref: 'legendXLocation',
		selector: 'settings #xLocationRadio'
	}],

	/**
	 * name of the currently active project
	 */
	projectName: '',

	init: function() {
		this.control({ 
			'projectFieldChooser': {
				okclicked: this.onProjectFieldsChange,
				resetClicked: this.onRefreshProjectFields
			},
			'legendsFieldChooser': {
				okclicked: this.onLegendsFieldsChange,
				resetClicked: this.onRefreshLegendsFields
			}
		});

		this.application.on({
			changeProject: this.onChangeProject,
			scope: this
		});
	},

	/**
	 * gets the name of the currently active project
	 */
	getCurrentProject: function() {
		return this.projectName;
	},

	/**
	 * change the currently active project and 
	 *  reload the field selection store
	 */
	onChangeProject: function(projectName) {
		this.projectName = projectName;
		this.loadProjectFieldChooser();
	},

	/**
	 * reset the field selector fields to the 
	 *  original values
	 */
	onRefreshProjectFields: function() {
		this.loadProjectFieldChooser();
	},
	
	/**
	 * reset the field selector
	 */
	onRefreshLegendsFields: function() {
		this.loadProjectFieldChooser();
	},

	/**
	 * change project fields and refresh the project
	 */
	onProjectFieldsChange: function(view, selectedFields, available) {
		
		//if same, do nothing
		var oldSelected = this.getProjectSelectedFieldsStore();
		if(this.hasFieldsChanged(oldSelected, selectedFields ) === false) {
			return;
		}

		var chosenFields = [];
		var i;
		for (i=0; i < selectedFields.length; ++i ) {
			chosenFields.push(selectedFields[i].data.name);
		}

		// Write to server
		var me = this;
		Ext.Ajax.request({
			method : 'POST',
			url : '../annotation/setFieldsChosen',
			params : {
				projectName : this.projectName,
				chosenFields : Ext.encode(chosenFields)
			},
			success : function(result, req) {
				//refresh the project
				me.getThumbnailsController().onRefresh();
				
				Ext.Msg.alert("Status", "Project fields saved successfully");
			},
			failure : function(result, req) {
				showErrorMessage(action.result.responseText, "Navigator fields saving to server failed");
			}
		});

	},
	
	onYLocationChange: function(location) {
		updateLegendLocation(xLoaction, yLocation);
	},
	
	onXLocationChange: function(location) {
		updateLegendLocation(xLoaction, yLocation);
	},
	
	updateLegendLocation: function(x,y)
	{
		Ext.Ajax.request({
			method : 'POST',
			url : '../annotation/setLegendLocation',
			params : {
				projectName : this.projectName,
				xLocation : x,
				yLocation : y
			},
			success : function(result, req) {
				//refresh the project
				me.getThumbnailsController().onRefresh();
			},
			failure : function(result, req) {
				showErrorMessage(action.result.responseText, "Navigator fields saving to server failed");
			}
		});
	},
	
	onLegendsFieldsChange: function(view, selectedFields, available) {
		
		//if same, do nothing
		var oldSelected = this.getProjectSelectedFieldsStore();
		if(this.hasFieldsChanged(oldSelected, selectedFields ) === false) {
			return;
		}

		var chosenFields = [];
		var i;
		for (i=0; i < selectedFields.length; ++i ) {
			chosenFields.push(selectedFields[i].data.name);
		}

		// Write to server
		var me = this;
        Ext.Ajax.request({
            method : 'POST',
            url : '../annotation/setLegendsChosen',
            params : {
                projectName : me.projectName,
                chosenFields : Ext.encode(chosenFields)
            },
            success : function(result, req) {
            	me.getThumbnailsController().onRefresh();
            	Ext.Msg.alert("Status", "Legend fields saved successfully");
            },
            failure : function(result, req) {
                showErrorMessage(action.result.responseText, "Legends fields saving to server failed");
            }
        });
        
        // write the location
        Ext.Ajax.request({
            method : 'POST',
            url : '../annotation/setLegendLocation',
            params : {
                projectName : me.projectName,
                xLocation : this.getLegendXLocation().getValue().lr, 
                yLocation : this.getLegendYLocation().getValue().tb
            },
            success : function(result, req) {
            	me.getThumbnailsController().onRefresh();
            },
            failure : function(result, req) {
                showErrorMessage(action.result.responseText, "Legends fields saving to server failed");
            }
        });
        
	},
	
	/**
	 * check if fields have changed 
	 */
	hasFieldsChanged : function(selectedFields, newSelectedFields) {
		var fieldsChanged = false;
		// Compare the arrays and set the flag fieldsChanged if there is any change
		if (selectedFields.length == newSelectedFields.length) {
			for (var i=0; i < selectedFields.length ; ++i) {
				if (selectedFields[i] != newSelectedFields[i]) {
					fieldsChanged = true;
					break;
				}
			}
		} else {
			fieldsChanged = true;
		}
		return fieldsChanged;
	},

	/**
	 * load the fields store
	 */
	loadProjectFieldChooser: function() {
		var projectName = this.getCurrentProject();
		var store = this.getProjectFieldChoosersStore();
		store.load({
			params: {
				projectName: projectName
			},
			callback : function(records, operation, success) {
				if (!success) 
					Ext.Msg.Alert("Warning", "Cannot load project fields");
			}
		});
		
		var legendStore = this.getLegendFieldStoreStore();
        var loadParams = {
            params: {
                projectName : projectName
            }
        };
        legendStore.load(loadParams);
	},
});


