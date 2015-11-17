/**
 * View for adding profile information to the images
 */
Ext.define('Manage.view.ProfileChooser', {
    extend : 'Ext.form.Panel',
    xtype : 'profileChooser',
    alias : 'widget.profileChooser',
    autoScroll:true,
    
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
        	           fieldLabel : 'Microscope Name',
        	           labelAlign : 'top',
        	           name : 'microscopeName',
        	           queryMode : 'local',
        	           displayField : 'microscopeName',
        	           valueField : 'microscopeName',
        	           allowBlank : false,
        	           editable : false,
        	           store : 'admin.Microscopes',
        	           listeners : {
						   'select': function(combo, row, index) {
							   _this.microscopeSelected(combo.getValue());
				            }
				    }
        	       }, {
        	           xtype : 'combobox',
        	           fieldLabel : 'Profile',
        	           labelAlign : 'top',
        	           name : 'profileName',
        	           queryMode : 'local',
        	           displayField : 'profileName',
        	           valueField : 'profileName',
        	           allowBlank : false,
        	           editable : false,
        	           multiSelect : true,
        	           enabled : false,
        	           store : 'ProfileStore',
					   listeners : {
						   'select' : function(combo, rows, index) {
							   _this.profileSelected(rows);
						   }
					   }
		      }, {
	                   xtype : 'combo',
	                   fieldLabel : 'x Pixel Size',
	                   labelAlign : 'top',
	                   name : 'xPixelSize',
					   minValue : 0,
					   decimalPrecision : 10,
					   hideTrigger : true,
					   keyNavEnabled : false,
					   mouseWheelEnabled : false,
					   multiSelect : true,
			           allowBlank : true,
			           validator: function(value)
			              {
			            	  var view = this.up('form');
			                  var form = view.getForm();
			                  var values = form.getFieldValues();
			                  if(values.xPixelSize == null && values.xType == null)
			                  {
			                	  form.findField('xType').clearInvalid();
			                	  return true;
			                  }
			                  if(values.xPixelSize != null && values.xType != null)
			                  {
			                	  form.findField('xType').clearInvalid();
			                	  return true;
			                  }
			                  
			                  form.findField('xType').markInvalid("Conflicting Type");
			                  return false;
			              }
		       }, {
		              xtype : 'combo',
		              fieldLabel : 'X Type',
		              labelAlign : 'top',
		              queryMode : 'local',
		              name : 'xType',
		              valueField : 'name',
		              displayField : 'value',
		              multiSelect : true,
		              store : 'ProfileTypeStore',
		              validator: function(value)
		              {
		            	  var view = this.up('form');
		                  var form = view.getForm();
		                  var values = form.getFieldValues();
		                  if(values.xPixelSize == null && values.xType == null)
		                  {
		                	  form.findField('xPixelSize').clearInvalid();
		                	  return true;
		                  }
		                  if(values.xPixelSize != null && values.xType != null)
		                  {
		                	  form.findField('xPixelSize').clearInvalid();
		                	  return true;
		                  }
		                  
		                  form.findField('xPixelSize').markInvalid("Conflicting Value");
		                  return false;
		              }
			  }, {
	                   xtype : 'combo',
	                   fieldLabel : 'y Pixel Size',
	                   labelAlign : 'top',
	                   name : 'yPixelSize',
					   minValue : 0,
					   decimalPrecision : 10,
					   hideTrigger : true,
					   keyNavEnabled : false,
					   mouseWheelEnabled : false,
					   multiSelect : true,
			           allowBlank : true,
			           validator: function(value)
			              {
			            	  var view = this.up('form');
			                  var form = view.getForm();
			                  var values = form.getFieldValues();
			                  if(values.yPixelSize == null && values.yType == null)
			                  {
			                	  form.findField('yType').clearInvalid();
			                	  return true;
			                  }
			                  if(values.yPixelSize != null && values.yType != null)
			                  {
			                	  form.findField('yType').clearInvalid();
			                	  return true;
			                  }
			                  
			                  form.findField('yType').markInvalid("Conflicting Type");
			                  return false;
			              }
		       },{
		              xtype : 'combo',
		              fieldLabel : 'Y Type',
		              labelAlign : 'top',
		              queryMode : 'local',
		              name : 'yType',
		              valueField : 'name',
		              displayField : 'value',
		              multiSelect : true,
		              store : 'ProfileTypeStore',
		              validator: function(value)
		              {
		            	  var view = this.up('form');
		                  var form = view.getForm();
		                  var values = form.getFieldValues();
		                  if(values.yPixelSize == null && values.yType == null)
		                  {
		                	  form.findField('yPixelSize').clearInvalid();
		                	  return true;
		                  }
		                  if(values.yPixelSize != null && values.yType != null)
		                  {
		                	  form.findField('yPixelSize').clearInvalid();
		                	  return true;
		                  }
		                  
		                  form.findField('yPixelSize').markInvalid("Conflicting Value");
		                  return false;
		              }
			  }, {
	                   xtype : 'combo',
	                   fieldLabel : 'z Pixel Size',
	                   labelAlign : 'top',
	                   name : 'zPixelSize',
					   inValue : 0,
					   decimalPrecision : 10,
					   hideTrigger : true,
					   keyNavEnabled : false,
					   mouseWheelEnabled : false,
					   multiSelect : true,
	                   allowBlank : true,
	                   validator: function(value)
	                   {
	                 	  var view = this.up('form');
	                       var form = view.getForm();
	                       var values = form.getFieldValues();
	                       if(values.zPixelSize == null && values.zType == null)
	                       {
			                	  form.findField('zType').clearInvalid();
			                	  return true;
			               }
	                       if(values.zPixelSize != null && values.zType != null)
	                       {
			                	  form.findField('zType').clearInvalid();
			                	  return true;
			               }
			                  
			               form.findField('zType').markInvalid("Conflicting Type");
	                       
	                       return false;
	                   }
		       },{
		              xtype : 'combo',
		              fieldLabel : 'Z Type',
		              labelAlign : 'top',
		              queryMode : 'local',
		              name : 'zType',
		              valueField : 'name',
		              displayField : 'value',
		              multiSelect : true,
		              store : 'ProfileTypeStore',
		              validator: function(value)
		              {
		            	  var view = this.up('form');
		                  var form = view.getForm();
		                  var values = form.getFieldValues();
		                  if(values.zPixelSize == null && values.zType == null)
		                  {
		                	  form.findField('zPixelSize').clearInvalid();
		                	  return true;
		                  }
		                  if(values.zPixelSize != null && values.zType != null)
		                  {
		                	  form.findField('zPixelSize').clearInvalid();
		                	  return true;
		                  }
		                  
		                  form.findField('zPixelSize').markInvalid("Conflicting Value");
		                  return false;
		              }
			  },  {
	                   xtype : 'combo',
	                   fieldLabel : 'Elapsed Time Unit',
	                   labelAlign : 'top',
	                   queryMode : 'local',
	                   name : 'timeUnit',
	                   valueField : 'name',
	                   displayField : 'value',
	                   multiSelect : true,
	                   store : 'ProfileTimeUnitStore',
	                   allowBlank : false,
	                   forceSelection : true	
		       }, {
                   xtype : 'combo',
                   fieldLabel : 'Exposure Time Unit',
                   labelAlign : 'top',
                   queryMode : 'local',
                   name : 'exposureTimeUnit',
                   valueField : 'name',
                   displayField : 'value',
                   multiSelect : true,
                   store : 'ProfileTimeUnitStore',
                   allowBlank : false,
                   forceSelection : true	
	           }, {
	                   xtype : 'combo',
	                   fieldLabel : 'Length Unit',
	                   labelAlign : 'top',
	                   queryMode : 'local',
	                   name : 'lengthUnit',
	                   valueField : 'name',
	                   displayField : 'value',
	                   multiSelect : true,
	                   store : 'ProfileLengthUnitStore',
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
		
		var fields = [ 'xPixelSize', 'xType', 'yPixelSize', 'yType', 'zPixelSize', 'zType', 'timeUnit', 'exposureTimeUnit', 'lengthUnit'];
		var fieldValues=new Array(fields.length);
		
		for(var i = 0; i < fieldValues.length; i++){
			fieldValues[i]=[];
		}
		
		for(var cnt=0;cnt<profiles.length;cnt++)
		{
			console.log(cnt);
			console.log("applying profile "+profiles[cnt].data.profileName);
			var record = store.findRecord('profileName', profiles[cnt].data.profileName);	
			
			//fill the form fields with the values from profileName
			//TODO change fields to automatically load from the store
			
			for(var i = 0; i < fields.length; i++) {
				var field = fields[i];
				var obj = form.findField(field);
				if(record.data[field]!="")
				{
					console.log(record.data[field]);
					fieldValues[i].push(record.data[field]);
					obj.setValue(fieldValues[i]);
				}
			}
		}	    
    },

    // Reset and Submit buttons
    buttons: [ {
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
            var view = this.up('form');
            var form = view.getForm();
            
            if (form.isValid()) {
            	view.fireEvent('saveProfile', form);
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
                
                view.fireEvent('setProfile', form);
            }
        }
    }]
});
