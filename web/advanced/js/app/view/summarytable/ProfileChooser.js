/**
 * View for adding profile information to the images
 */
Ext.define('Manage.view.summarytable.ProfileChooser', {
    extend : 'Ext.form.Panel',
    xtype : 'profileChooser',
    alias : 'widget.profileChooser',
    
    bodyPadding: 5,

    // Fields will be arranged vertically, stretched to full width
    layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
    
    // The fields
    defaultType: 'textfield',
    initComponent : function() {
    	var guids = this.guids;
    	var _this = this;
        Ext.apply (this, {
        	items: [
        	        {
        	        	xtype: 'hidden',
	        	   	  	name: 'guids',
	        	   	  	value: guids
        	   	    },
        	   	    {
        	           xtype : 'combobox',
        	           fieldLabel : 'Microscope',
        	           name : 'microscopeName',
        	           queryMode : 'local',
        	           displayField : 'microscopeName',
        	           valueField : 'microscopeName',
        	           allowBlank : false,
        	           editable : false,
        	           store : 'Manage.store.MicroscopeStore',
        	           listeners : {
				   'select': function(combo, row, index) {
					   _this.microscopeSelected(combo.getValue());
				            }
				    }
        	       }, {
        	           xtype : 'combobox',
        	           fieldLabel : 'Profile',
        	           name : 'profileName',
        	           queryMode : 'local',
        	           displayField : 'profileName',
        	           valueField : 'profileName',
        	           allowBlank : false,
        	           editable : false,
        	           multiSelect : true,
        	           enabled : false,
        	           store : 'Manage.store.ProfileStore',
					   listeners : {
						   'select' : function(combo, rows, index) {
							   _this.profileSelected(rows);
						   }
					   }
		      }, {
	                   xtype : 'numberfield',
	                   fieldLabel : 'x Pixel Size',
	                   name : 'xPixelSize',
					   minValue : 0,
					   decimalPrecision : 10,
					   hideTrigger : true,
					   keyNavEnabled : false,
					   mouseWheelEnabled : false,
			           allowBlank : true,
			           validator: function(value)
			              {
			            	  var view = this.up('form');
			                  var form = view.getForm();
			                  var values = form.getFieldValues();
			                  if(values.xPixelSize == null && values.xType == null)
			                	  return true;
			                  if(values.xPixelSize != null && values.xType != null)
			                	  return true;
			                  
			                  return false;
			              }
		       }, {
		              xtype : 'combo',
		              fieldLabel : 'X Type',
		              queryMode : 'local',
		              name : 'xType',
		              valueField : 'name',
		              displayField : 'value',
		              store : 'Manage.store.ProfileTypeStore',
		              validator: function(value)
		              {
		            	  var view = this.up('form');
		                  var form = view.getForm();
		                  var values = form.getFieldValues();
		                  if(values.xPixelSize == null && values.xType == null)
		                	  return true;
		                  if(values.xPixelSize != null && values.xType != null)
		                	  return true;
		                  
		                  return false;
		              }
			  }, {
	                   xtype : 'numberfield',
	                   fieldLabel : 'y Pixel Size',
	                   name : 'yPixelSize',
					   minValue : 0,
					   decimalPrecision : 10,
					   hideTrigger : true,
					   keyNavEnabled : false,
					   mouseWheelEnabled : false,
			           allowBlank : true,
			           validator: function(value)
			              {
			            	  var view = this.up('form');
			                  var form = view.getForm();
			                  var values = form.getFieldValues();
			                  if(values.yPixelSize == null && values.yType == null)
			                	  return true;
			                  if(values.yPixelSize != null && values.yType != null)
			                	  return true;
			                  
			                  return false;
			              }
		       },{
		              xtype : 'combo',
		              fieldLabel : 'Y Type',
		              queryMode : 'local',
		              name : 'yType',
		              valueField : 'name',
		              displayField : 'value',
		              store : 'Manage.store.ProfileTypeStore',
		              validator: function(value)
		              {
		            	  var view = this.up('form');
		                  var form = view.getForm();
		                  var values = form.getFieldValues();
		                  if(values.yPixelSize == null && values.yType == null)
		                	  return true;
		                  if(values.yPixelSize != null && values.yType != null)
		                	  return true;
		                  
		                  return false;
		              }
			  }, {
	                   xtype : 'numberfield',
	                   fieldLabel : 'z Pixel Size',
	                   name : 'zPixelSize',
					   inValue : 0,
					   decimalPrecision : 10,
					   hideTrigger : true,
					   keyNavEnabled : false,
					   mouseWheelEnabled : false,
	                   allowBlank : true,
	                   validator: function(value)
	                   {
	                 	  var view = this.up('form');
	                       var form = view.getForm();
	                       var values = form.getFieldValues();
	                       if(values.zPixelSize == null && values.zType == null)
	                     	  return true;
	                       if(values.zPixelSize != null && values.zType != null)
	                     	  return true;
	                       
	                       return false;
	                   }
		       },{
		              xtype : 'combo',
		              fieldLabel : 'Z Type',
		              queryMode : 'local',
		              name : 'zType',
		              valueField : 'name',
		              displayField : 'value',
		              store : 'Manage.store.ProfileTypeStore',
		              validator: function(value)
		              {
		            	  var view = this.up('form');
		                  var form = view.getForm();
		                  var values = form.getFieldValues();
		                  if(values.zPixelSize == null && values.zType == null)
		                	  return true;
		                  if(values.zPixelSize != null && values.zType != null)
		                	  return true;
		                  
		                  return false;
		              }
			  },  {
	                   xtype : 'combo',
	                   fieldLabel : 'Time Unit',
	                   queryMode : 'local',
	                   name : 'timeUnit',
	                   valueField : 'name',
	                   displayField : 'value',
	                   store : 'Manage.store.ProfileTimeUnitStore',
	                   allowBlank : false,
	                   forceSelection : true	
		       }, {
	                   xtype : 'combo',
	                   fieldLabel : 'Length Unit',
	                   queryMode : 'local',
	                   name : 'lengthUnit',
	                   valueField : 'name',
	                   displayField : 'value',
	                   store : 'Manage.store.ProfileLengthUnitStore',
	                   allowBlank : false,
	                   forceSelection : true			 
		       }
		]
        });
    	
    	
        // Bug in extjs - see http://stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
        this.on('afterrender', function(me) {
            delete me.form._boundItems;
        });
        this.callParent();
    },
    
    microscopeSelected : function(value) {
		var nameField = this['form'].findField("profileName");
		var profileStore = nameField.store;
		var microscope = value;
		console.log(microscope);
	    	
		nameField.enabled = true;
		profileStore.load({
	            params : {
	                microscopeName : microscope
	            }
	        });
    },

    profileSelected : function(profiles) {
		var form = this['form'];
		var store = form.findField('profileName').store;
		for(var cnt=0;cnt<profiles.length;cnt++)
		{
			console.log(cnt);
			console.log("applying profile "+profiles[cnt].data.profileName);
			var record = store.findRecord('profileName', profiles[cnt].data.profileName);	
			
			//fill the form fields with the values from profileName
			//TODO change fields to automatically load from the store
			var fields = [ 'xPixelSize', 'xType', 'yPixelSize', 'yType', 'zPixelSize', 'zType', 'timeUnit', 'lengthUnit'];
			for(var i = 0; i < fields.length; i++) {
				var field = fields[i];
				var obj = form.findField(field);
				if(record.data[field]!="")
				{
					console.log(record.data[field]);
					obj.setValue(record.data[field]);
				}
			}
		}	    
    },

    // Reset and Submit buttons
    buttons: [{
        text : 'Cancel',
        handler : function() {
            var view = this.up('form');
            view.up().close();
        }
    }, {
        text: 'Reset',
        handler: function() {
            this.up('form').getForm().reset();
        }
    },{
        text: 'Save',
        tooltip: 'Save Current Profile',
        formBind: true, //only enabled once the form is valid
        disabled: true,
        handler: function() {
            console.log("save pressed");
            var view = this.up('form');
            var form = view.getForm();
            
            if (form.isValid()) {
                var values = form.getFieldValues();
                
                Ext.Msg.prompt('Name', 'Please enter profile name:', function(btn, text){
                    if (btn == 'ok'){
                    	Ext.Ajax.request({
                            method : 'POST',
                            url : '../admin/addAcquisitionProfile',
                            params : {
                                profileName : text,
                                microscopeName : values.microscopeName,
                                xPixelSize: values.xPixelSize,
                                xType : values.xType,
                                yPixelSize: values.yPixelSize,
                                yType : values.yType,
                                zPixelSize: values.zPixelSize,
                                zType : values.zType,
                                timeUnit : values.timeUnit,
                                lengthUnit : values.lengthUnit,
                                guids: values.guids
                            },
                            success : function (result, response){
                                Ext.Msg.alert("Success", "Profile successfully saved.");
                            },
                            failure : function(result, request) {
                                showErrorMessage(result.responseText, "Failed to save the profile");
                            } 
                        });
                    }
                });
            }
        }
    }, {
        text: 'Apply',
        formBind: true, //only enabled once the form is valid
        disabled: true,
        handler: function() {
            var view = this.up('form');
            var form = view.getForm();
            
            if (form.isValid()) {
                var values = form.getFieldValues();
                
                Ext.Ajax.request({
                    method : 'POST',
                    url : '../admin/setAcqProfile',
                    params : {
                        profileName : values.profileName,
                        microscopeName : values.microscopeName,
                        xPixelSize: values.xPixelSize,
                        xType : values.xType,
                        yPixelSize: values.yPixelSize,
                        yType : values.yType,
                        zPixelSize: values.zPixelSize,
                        zType : values.zType,
                        timeUnit : values.timeUnit,
                        lengthUnit : values.lengthUnit,
                        guids: values.guids
                    },
                    success : function (result, response){
                        Ext.Msg.alert("Success", "Profile successfully applied to selected records.");
                        view.up().close();
                    },
                    failure : function(result, request) {
                        showErrorMessage(result.responseText, "Failed to apply selected profile");
                    } 
                });
            }
        }
    }]
});
