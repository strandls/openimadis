/**
 * View for adding a new publisher
 */
Ext.define('Manage.view.VideoSubmitDialog', {
    extend : 'Ext.form.Panel',
    xtype : 'videoSubmitDialog',
    alias : 'widget.videoSubmitDialog',
    
    bodyPadding: 5,

    // Fields will be arranged vertically, stretched to full width
    layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
    
    // The fields
    defaultType: 'textfield',
    initComponent : function() {
    	var guid = this.guid;
        var siteNumber = this.siteNumber;
        var frameNumber = this.frameNumber;
        var sliceNumber = this.sliceNumber; 
        var frame       = this.frame;
        var useChannelColor = this.useChannelColor;
        var useZStack = this.useZStack;
        var channelNumbers = Ext.encode(this.channelNumbers);
        var overlayNames = Ext.encode(this.overlayNames);
        Ext.apply (this, {
        	items: [
        	        {
        	        	xtype: 'hidden',
	        	   	  	name: 'guid',
	        	   	  	value: guid
        	   	    },{
        	        	xtype: 'hidden',
	        	   	  	name: 'siteNumber',
	        	   	  	value: siteNumber
        	   	    },{
        	        	xtype: 'hidden',
	        	   	  	name: 'frameNumber',
	        	   	  	value: frameNumber
        	   	    },{
        	        	xtype: 'hidden',
	        	   	  	name: 'sliceNumber',
	        	   	  	value: sliceNumber
        	   	    },{
        	        	xtype: 'hidden',
	        	   	  	name: 'frame',
	        	   	  	value: frame
        	   	    },{
        	        	xtype: 'hidden',
	        	   	  	name: 'useChannelColor',
	        	   	  	value: useChannelColor
        	   	    },{
        	        	xtype: 'hidden',
	        	   	  	name: 'useZStack',
	        	   	  	value: useZStack
        	   	    },{
        	        	xtype: 'hidden',
	        	   	  	name: 'channelNumbers',
	        	   	  	value: channelNumbers
        	   	    },{
        	        	xtype: 'hidden',
	        	   	  	name: 'overlayNames',
	        	   	  	value: overlayNames
        	   	    },{
        	           xtype : 'datefield',
        	           name : 'validity',
        	           fieldLabel : 'Expiry Date',
        	           allowBlank : false,
        	           value : this.expiry,
        	           minValue : new Date()
        	       },{
			        xtype: 'radiogroup',
			        fieldLabel: 'FPS Option',
			        columns: 2,
			        vertical: true,
			        items: [
			            { boxLabel: 'Use Frame Rate', name: 'frameRate', inputValue: 'frameRate', checked: true },
			            { boxLabel: 'Use Elapsed Time', name: 'frameRate', inputValue: 'elapsedTime'}
			        ]
        	       },{
        	           xtype : 'numberfield',
        	           fieldLabel : 'Frame Rate',
        	           name : 'fps',
        	           minValue : 0.1,
        	           maxValue : 30,
        	           allowDecimal : true,
        	           decimalPrecision : 1,
        	           step : 0.1,
        	           value : 10,
        	           allowBlank : true
        	       }
        	]
        });
    	
    	
        // Bug in extjs - see http://stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
        this.on('afterrender', function(me) {
            delete me.form._boundItems;
        });
        this.callParent();
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
    }, {
        text: 'Submit',
        formBind: true, //only enabled once the form is valid
        disabled: true,
        handler: function() {
            var view = this.up('form');
            var form = view.getForm();
            if (form.isValid()) {
                var values = form.getFieldValues();
                console.log(values);
                var date=values.validity.getTime();

                view.up().close();
                
                console.log(values.fps);
                
                Ext.Ajax.request({
                    method : 'GET',
                    url : '../movie/createVideoMovie',
                    params : {
                        guid : values.guid,
                        siteNumber  :  values.siteNumber,
                        frameNumber : values.frameNumber,
                        sliceNumber : values.sliceNumber, 
                        frame       : values.frame,
                        useChannelColor: values.useChannelColor,
                        zstacked: values.useZStack,
                        channelNumbers : values.channelNumbers,
                        overlayNames : values.overlayNames,
                        validity : date,
                        fps : values.fps
                    },
                    success : function(result, request) {
                    	Ext.Msg.alert("Message", "Movie is successfully submitted. Check Downloads Section.");                    	
                    },
                    failure : function(result, request) {
                        showErrorMessage(result.responseText, "Failed to submit Movie");
                    }
                });
            }
        }
    }]
});