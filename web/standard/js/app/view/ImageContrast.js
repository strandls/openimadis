/**
 * View for image contrast selection.
 */
Ext.define('Manage.view.ImageContrast', {
    extend : 'Ext.panel.Panel',
    xtype : 'imagecontrast',
    alias : 'widget.imagecontrast',
    layout : 'border',
    initComponent : function() {
        this.imageHeight = 300;
        // Image width important - tied up with slider
        if (!this.imageWidth) {
            this.imageWidth = 200;
	}
        
        // Current values of min and max in the sliders
        this.currentMin = this.minIntensity;
        this.currentMax = this.maxIntensity;
        this.currentGamma = this.userGamma;

        //Create components
        this.createImageCmp();
        this.createMinSlider();
        this.createMaxSlider();
        this.createGammaLabel();

        var _view = this;
        
        
        Ext.apply (this, {
            items : [{
                xtype : 'panel',
                region : 'center',
                layout : 'border',
                width : this.imageWidth + 10,
                border : false,
                items : [this.maxSlider, {
                    xtype : 'panel',
                    region : 'center',
                    bodyCls : 'image_overlay',
                    border : false,
                    width : this.imageWidth,
                    height : this.imageHeight,
                    html : '<div id="contrastDraw" class="editor">' + 
                           '</div>',
                    items : [this.imageCmp]
                }, this.minSlider
                ]
            }, {
                xtype : 'panel',
                region : 'east',
                width : 5,
                border : false
            }, {
                xtype : 'panel',
                region : 'west',
                width : 100,
                border : false,
                height : 110,
                layout : 'border',
                items : [this.minSliderLabel, {
                    xtype : 'panel',
                    border : false,
                    region : 'center',
                    height : 30
                }, this.maxSliderLabel]
            }, {
                xtype : 'panel',
                region : 'south',
                layout : 'border',
                height : 30,
                items : [this.gammaLabel, {
                    xtype : 'slider',
                    region : 'center',
                    minValue : 0.1,
                    maxValue : 3.0,
                    value : this.userGamma,
                    increment : 0.1,
                    decimalPrecision : 1,
                    padding : 5,
                    listeners : {
                        changecomplete : function(slider, newValue) {
                            _view.changeGamma(newValue);
                        },
                        change : function(slider, newValue) {
                            _view.gammaLabel.setText("Gamma : " +newValue);
                        }
                    }
                }]
            }],
            width : this.imageWidth + 120,
            height : this.imageHeight + 100,
            buttons : [{
                text : 'Auto',
                handler : function() {
                    _view.doAuto();
                }
            },{
                text : 'Reset',
                handler : function() {
                    _view.doReset();
                }
            }]
        });
        this.callParent();

   },

    /**
     * Create image component
     */
    createImageCmp : function() {
       this.imageCmp = Ext.create('Ext.Img', {
            src : this.imageSrc,
            width : this.imageWidth,
            height : this.imageHeight,
            region : 'center'
        });
    },

    /**
     * Create max slider
     */
    createMaxSlider : function() {
        var _this = this;
        var stepSize = Math.round((this.maxIntensity - this.minIntensity)/(this.imageWidth));
        if (stepSize === 0) { stepSize = 1;}
        this.maxSlider = Ext.create('Ext.slider.Single', {
            region : 'north',
            minValue : this.minIntensity,
            maxValue : this.maxIntensity,
            value : this.userMax,
            increment : stepSize,
            width : this.imageWidth + 5,
            border : false,
            height : 20,
            listeners : {
                change : function(slider, newValue, thumb, opts) {
                    _this.maxChange(newValue);
                },
                changecomplete : function(slider, newValue, thumb, opts) {
                    _this.maxChangeComplete(newValue);
                },
                beforechange : function(slider, newValue, oldValue, opts) {
                    return _this.canChangeMax(newValue);
                }
            }
        });
        
        this.maxSliderLabel = Ext.create('Ext.form.NumberField', {
            region : 'north',
            height : 40,
            fieldLabel : 'Max Intensity',
            labelAlign : 'top',
            value : this.userMax,
            allowDecimals : false,
            minValue : 0,
            maxValue : Math.pow(2, this.pixelDepth*8)-1,
            listeners : {
                change : function(field, newValue, oldValue, opts) {
                    _this.onMaxSliderLabelChange(newValue, oldValue);
                }
            }
        });

    },
    
    /**
     * Create min slider
     */
    createMinSlider : function() {
        var _this = this;
        var stepSize = Math.round((this.maxIntensity - this.minIntensity)/(this.imageWidth));
        if (stepSize === 0) { stepSize = 1;}
        this.minSlider = Ext.create('Ext.slider.Single', {
            region : 'south',
            minValue : this.minIntensity,
            maxValue : this.maxIntensity,
            value : this.userMin,
            increment : stepSize,
            border : false,
            height : 20,
            listeners : {
                change : function(slider, newValue, thumb, opts) {
                    _this.minChange(newValue);
                },
                changecomplete : function(slider, newValue, thumb, opts) {
                    _this.minChangeComplete(newValue);
                },
                beforechange : function(slider, newValue, oldValue, opts) {
                    return _this.canChangeMin(newValue);
                }
            }
        });

        this.minSliderLabel = Ext.create('Ext.form.NumberField', {
            region : 'south',
            height : 40,
            fieldLabel : 'Min Intensity',
            labelAlign : 'top',
            value : this.userMin,
            allowDecimals : false,
            minValue : 0,
            maxValue : Math.pow(2, this.pixelDepth*8),
            listeners : {
                change : function(field, newValue, oldValue, opts) {
                    _this.onMinSliderLabelChange(newValue, oldValue);
                }
            }
        });
    },
    
    /**
     * Label for indicating changing gamma value
     */
    createGammaLabel : function() {
        this.gammaLabel = Ext.create('Ext.form.Label', {
            region : 'west',
            padding : 5,
            width : 90,
            text : 'Gamma : ' + this.userGamma
        });
    },

    listeners : {
        afterrender : function(comp, opts) {
            comp.paper = Raphael("contrastDraw", comp.imageWidth, comp.imageHeight);
            var startX = ((comp.userMin-comp.minIntensity)/(comp.maxIntensity-comp.minIntensity))*comp.imageWidth;
            var width = ((comp.userMax-comp.userMin)/(comp.maxIntensity-comp.minIntensity))*comp.imageWidth;
            comp.contrastRange = comp.paper.rect(startX, 0, width, comp.imageHeight);
            comp.contrastRange.attr({fill:"yellow", "fill-opacity":0.2});
            
            // Validate values
            var min = false;
            var max = false;
            if (this.userMin < this.minIntensity || this.userMin > this.maxIntensity)
                min = true;
            if (this.userMax < this.minIntensity || this.userMax > this.maxIntensity)
                max = true;
            if(min || max)
            	this.disableSliders(min, max);
        }
    },

    /**
     * when max slider changes
     */
    maxChange : function (value) {
        if (!this.contrastRange)
            return;
        var newWidth = ((value-this.currentMin)/(this.currentMax-this.currentMin)) * this.imageWidth - this.contrastRange.attr("x");
        this.contrastRange.attr({width: newWidth});
    },

    /**
     * when max slider change is complete
     */
    maxChangeComplete : function (value) {
        this.maxSliderLabel.setValue(value);
    },

    /**
     * when min slider changes
     */
    minChange : function (value) {
        if (!this.contrastRange)
            return;
        var newX  = ((value-this.currentMin)/(this.currentMax-this.currentMin)) * this.imageWidth;
        var oldX = this.contrastRange.attr('x');
        var oldWidth = this.contrastRange.attr('width');
        var width = oldWidth - (newX-oldX) ;
        this.contrastRange.attr({x : newX});
        this.contrastRange.attr({width : width});
    },

    /**
     * when min slider change is complete
     */
    minChangeComplete : function (value) {
        this.minSliderLabel.setValue(value);
    },

    /**
     * Save the current settings to server and refetch image
     */
    saveSettings : function() {
        var minIntensity = this.minSliderLabel.getValue();
        var maxIntensity = this.maxSliderLabel.getValue();
        if (minIntensity > maxIntensity)
            return;
        var recordid = this.recordid;
        var channelNo = this.channelNo;
        this.fireEvent('saveSettings', recordid, channelNo, minIntensity, maxIntensity, this.currentGamma);
    },

    /**
     * Refetch the image contrast image
     */
    refetchImage : function(minValue, maxValue) {
        var newURL = this.imageSrc + "&minIntensity=" + minValue + "&maxIntensity=" + maxValue;
        this.imageCmp.setSrc(newURL);
    },

    /**
     * Set new values for the sliders
     */
    adjustSliders : function() {

    },

    /**
     * Can the min slider be changed to the value provided
     */
    canChangeMin : function (value) {
        if (value >= this.getMaxSliderValue())
            return false;
        return true;
    },

    /**
     * Can the max slider be changed to the value provided
     */
    canChangeMax : function (value) {
        if (value <= this.getMinSliderValue())
            return false;
        return true;
    },

    /**
     * Perform zoom on the selected area
     */
    doZoom : function() {
        // Append 10% of the chosen range on either side
        var minSliderValue = this.getMinSliderValue();
        var maxSliderValue = this.getMaxSliderValue();

        var delta = Math.round((maxSliderValue - minSliderValue)/10);
        this.currentMin =  minSliderValue - delta;
        this.currentMin = (this.currentMin <  this.minIntensity) ? this.minIntensity : this.currentMin;
        this.currentMax =  maxSliderValue + delta;
        this.currentMax = (this.currentMax >  this.maxIntensity) ? this.maxIntensity : this.currentMax;
        
        this.doRefresh(minSliderValue, maxSliderValue);
    },

    /**
     * Get the current min slider value
     */
    getMinSliderValue : function() {
        return this.minSlider.getValue();
    },

    /**
     * Get the current max slider value
     */
    getMaxSliderValue : function() {
        return this.maxSlider.getValue();
    },

    /**
     * Reset to previous user selected values of min and max
     */
    doReset : function() {
        this.doRefresh(this.userMin, this.userMax);
    },

    /**
     * Reset to the original values of min and max
     */
    doAuto : function() {
        this.doRefresh(this.currentMin, this.currentMax);
    },

    /**
     * Refresh sliders and image based on current values of currentMin and currentMax.
     * Set the slider values to minSliderValue and maxSliderValue
     */
    doRefresh : function(minSliderValue, maxSliderValue) {
        this.minSlider.setMinValue(this.currentMin);
        this.minSlider.setMaxValue(this.currentMax);
        this.minSlider.setValue(minSliderValue);
        this.minSliderLabel.setValue(minSliderValue);

        this.maxSlider.setMinValue(this.currentMin);
        this.maxSlider.setMaxValue(this.currentMax);
        this.maxSlider.setValue(maxSliderValue);
        this.maxSliderLabel.setValue(maxSliderValue);
        
        var newX = ((minSliderValue-this.currentMin)/(this.currentMax - this.currentMin))*this.imageWidth;
        var newWidth = ((maxSliderValue - minSliderValue)/(this.currentMax - this.currentMin))*this.imageWidth;
        this.contrastRange.attr({x: newX, width: newWidth });
        
        this.saveSettings();
    },

    changeGamma : function(value) {
        this.currentGamma = value;
        this.saveSettings();
    },

    /**
     * When user manually enters a value for min slider.
     * If the value lies between the min slider value, change that too.
     * Else leave at min
     */
    onMinSliderLabelChange : function (value, oldValue) {
        if (this.maxSliderLabel.getValue() < value) {
            // Invalid value for min slider
            return;
        }
        if (value < this.minSliderLabel.minValue || value > this.minSliderLabel.maxValue)
            return;
        if (value >= this.minIntensity && value <= this.maxIntensity) {
            this.enableSliders(true, false);
            this.minSlider.setValue(value);
        } else
            // Outside range. Disable sliders
            this.disableSliders(true, false);
        this.saveSettings();
    },

    /**
     * When user manually enters a value for max slider.
     * If the value lies between the max slider value, change that too.
     * Else leave at max
     */
    onMaxSliderLabelChange : function (value, oldValue) {
        if (this.minSliderLabel.getValue() > value) {
            // Invalid value for min slider.
            return;
        }
        if (value > this.maxSliderLabel.maxValue || value < this.maxSliderLabel.minValue)
            return;
        if (value <= this.maxIntensity && value >= this.minIntensity) {
            this.enableSliders(false, true);
            this.maxSlider.setValue(value);
        } else {
            // Outside range. Disable sliders
            this.disableSliders(false, true);
        }
        this.saveSettings();
    },

    /**
     * Disable min and max sliders
     */
    disableSliders : function(min, max) {
    
    	if(min)
    	{
    		this.minSlider.setValue(this.minIntensity);
    		this.minSlider.setDisabled(true);
    	}
        
        if(max)
        {
        	this.maxSlider.setValue(this.maxIntensity);
        	this.maxSlider.setDisabled(true);
        }
        
        
        if(min && max)
        	this.contrastRange.attr({fill: 'grey'});
    },

    /**
     * Enable min and max sliders
     */
    enableSliders : function(min, max) {
        this.contrastRange.attr({fill: 'yellow'});
        if(min)
        	this.minSlider.setDisabled(false);
        if(max)
        	this.maxSlider.setDisabled(false);
    }
});

