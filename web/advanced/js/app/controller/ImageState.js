Ext.require(['Manage.view.ImageEditToolBar']);

Ext.define('Manage.controller.ImageState', {
    extend: 'Ext.app.Controller',

    refs :[{
        ref: 'sliceSlider',
        selector: 'imagesliders #sliceslider'
    },{ 
        ref:'frameSlider',
        selector:'imagesliders #frameslider'
    },{ 
        ref:'channelControl',
        selector:'imagecontrols #channelcontrol'
    },{ 
        ref:'siteControl',
        selector:'imagecontrols #sitecontrol'
    },{ 
        ref:'overlayControl',
        selector:'imagecontrols #overlaycontrol'
    },{
       ref:'imagedisplaypanel', 
       selector:'imageview #imagedisplaypanel'
    }, {
        ref : 'imageview',
        selector : 'imageview'
    }, {
        ref : 'imagePanel',
        selector : 'imagePanel'
    }, {
        ref: 'thumbnails',
        selector: 'thumbnails'
    }, {
        ref : 'imagetoolbar',
        selector : 'imagetoolbar'
    },{
        ref : 'imagesliders',
        selector : 'imagesliders'
    },{
        ref : 'verticalruler',
        selector : 'verticalruler'
    },{
        ref : 'horizontalruler',
        selector : 'horizontalruler'
    },{
        ref : 'imageContrast',
        selector : 'imageContrast'
    }, {
        ref : 'zproject',
        selector : 'imagetoolbar #button_zproject'
    },{
        ref : 'rulercontrol',
        selector : 'imagetoolbar #button_rulers'
    }, {
        ref: 'summaryTable',
        selector: 'summarytable'
    }],

    init: function() {
        this.control({
            "imageEditToolbar" : {
                loadOverlay : this.onLoadOverlay,
                saveOverlay : this.onSaveOverlay
            }, "imagecontrols #overlaycontrol" : {
                editOverlays : this.onEditOverlays,
                addOverlays : this.onAddOverlays,
                deleteOverlays: this.onDeleteOverlays
            }, "imagecontrols #channelcontrol" : {
                chooseChannelLUT : this.onChooseChannelLUT,
                chooseContrast : this.onChooseContrast,
                changeChannelName : this.onChangeChannelName
            }, 'imagetoolbar' : {
                show3D : this.onShow3D,
                playMovie:this.onPlayMovie
            },'imagesliders':{
                previousSlice : this.onPreviousSlice,
                nextSlice: this.onNextSlice,
                previousFrame: this.onPreviousFrame,
                nextFrame: this.onNextFrame
            }, 'imageContrast' : {
                saveSettings : this.saveContrastSettings
            },'imageview' : {
		        adjustImage:this.onAdjustImage
			},'#imagedisplay' : {
                imageLoaded : this.onImageLoad
            }
        });
    },

    /**
     * Called when image has finished loading 
     */
    onImageLoad : function() {
    	 
    	 var imagedisplay = Ext.getCmp('imagedisplay');
    	 imagedisplay.setVisible(true);
    	 
    	 //var imageeditor=Ext.getCmp('imageeditor');
    	 var imageHolder = Ext.getCmp('imageholder');
    	 var recordid=this.getActiveRecordID();
    	 if(recordid === null){
    		return null;
    	 }
    	 var record=this.getRecordItem(recordid).data;
    	 
    	 var hruler= this.getHorizontalruler();
		 var xmax=record['Image Width']*record['Pixel Size X'];
	     hruler.reload(imageHolder.getWidth(),xmax);
         
         var vruler= this.getVerticalruler();
         var ymax=record['Image Height']*record['Pixel Size Y'];
         vruler.reload(imageHolder.getHeight(),ymax);
         
        var imageDisplayPanel = Ext.getCmp('imagedisplaypanel');
        imageDisplayPanel.setLoading(false);
        
        sketchpad.setSize(imageHolder.getWidth(), imageHolder.getHeight());
        sketchpad.setViewBox(0, 0, record['Image Width'], record['Image Height']);
        
        // Send refresh event to overlays panel
        var overlay = Ext.ComponentQuery.query('imagecontrols #overlaycontrol')[0];
        if (overlay && overlay !== null)
            overlay.fireEvent("selectionchange");
        // Fire an application-wide event that image loading has finished
        
        
        var imageholder=Ext.get('imageholder');
        
        imageholder.on('mousemove',function(event){
        	this.getHorizontalruler().showMousePosition(event.getX());
        	this.getVerticalruler().showMousePosition(event.getY());
        },this);
       
	 var showRulers = this.getRulercontrol().pressed;
	 if(!showRulers)
		 this.getImageview().hideRulers();

        this.application.fireEvent('imageLoaded');
    },

    /**
     * Create toolbar for image overlay editing
     */
    onEditOverlays : function(view) {
        var overlayName = this.getOverlayControl().getSelectionModel().getLastSelected().data["name"];
        this.getImagePanel().clearOverlayTransparancy();
        this.getController('Manage.controller.RecordSelection').renderVisualObjects(
        		overlayName, this,this.doShowToolBar,[view, overlayName]);
    },
    
    onPreviousFrame : function(view) {
    	var me=this;
    	var value = this.getFrameSlider().getValue();
    	if(value > 0){
    		this.checkAndSaveOverlays(
    			function(){
    				me.getFrameSlider().setValue(value - 1);
    				me.getFrameSlider().fireEvent('changecomplete');
    			}
    		);
    	}
    },
    
    onPreviousSlice : function(view) {
    	var me=this;
    	var value = this.getSliceSlider().getValue();
        if(value > 0){
    		this.checkAndSaveOverlays(
       			function(){
       				me.getSliceSlider().setValue(value - 1);
       				me.getSliceSlider().fireEvent('changecomplete');
       			}
    		);
        }
    },
    
    onNextSlice : function(view) {
    	var me=this;
    	var value = this.getSliceSlider().getValue();
    	if(value < this.getSliceSlider().maxValue){
    		this.checkAndSaveOverlays(
           		function(){
           			me.getSliceSlider().setValue( value + 1);
           			me.getSliceSlider().fireEvent('changecomplete');
           		}
    		);
    	}
    },
    
    onNextFrame : function(view) {
    	var me=this;
    	var value = this.getFrameSlider().getValue();
    	if(value < this.getFrameSlider().maxValue){
    		this.checkAndSaveOverlays(
               function(){
            	   me.getFrameSlider().setValue(value + 1);
            	   me.getFrameSlider().fireEvent('changecomplete');
               }
    		);
    	}
    },
    
      /**
     * Create toolbar for image overlay addition
     */
    onAddOverlays : function(view) {
        var currentRecord = this.getActiveRecordID();
        var currentSite = this.getSiteControl().items.items[0].getValue().site;

        Ext.create('Ext.window.Window', {
            title : 'Overlay Name',
            height : 120,
            width : 300,
            layout : 'fit',
            items : [{
                xtype : 'form',
                url : '../record/addOverlay',
                bodyPadding : 10,
                items : [{
                    xtype : 'textfield',
                    fieldLabel : 'Overlay Name',
                    name : 'overlay',
                    allowBlank : false
                }],
                buttons : [{
                    text : 'OK',
                    formbind : true,
                    handler : function() {
                        var form = this.up('form').getForm(); 
                        var _this = this;
                        if (form.isValid()) {
                            form.submit({
                                params : {
                                    recordid : currentRecord,
                                    siteNumber : currentSite
                                },
                                success : function(form, action) {
                                    // Refresh overlay listing 
                                    view.fireEvent("refreshOverlayList", currentRecord);
                                    _this.up().up().up().close();
                                },
                                failure : function(form, action) {
                                    showErrorMessage(action.response.responseText, "Failed to add overlay");
                                }
                            });
                        }
                    }
                }, {
                    text : 'Cancel',
                    handler : function() {
                        this.up().up().up().close();
                    }
                }]
            }]

        }).show();
    },
  
    /**
     * Create and show image edit toolbar
     */
    doShowToolBar : function(view, overlayName){
    	_this=this;
        view.setDisabled(true);
        var currentRecord = this.getActiveRecordID();
        var currentSite = this.getSiteControl().items.items[0].getValue().site;

        var toolbar = Ext.create('Manage.view.ImageEditToolBar', {
            xtype : 'imageEditToolbar',
            overlayName : overlayName,
            recordid : currentRecord,
            siteNo : currentSite
        });
        var editor = Ext.create('Ext.window.Window', {
            title : 'Edit '+overlayName,
            width : 300,
            items : [toolbar],
            listeners : {
                beforeclose : function(panel, opts) {
        			console.log('beforeclose');
                    var callback = function() {
                        sketchpad.purge();
                        sketchpad.setDirty(false);
                        sketchpad.mode(false);
                        //view.fireEvent("refreshOverlays", currentRecord, overlayName);
                        _this.getController('Manage.controller.RecordSelection').onOverlayChange();
                        view.setDisabled(false);
            			view.editor = null;
                    };
                    toolbar.checkAndSave(callback);
                }
            }
        });
        editor.show();
        // When something in the overlay changes, close the editor from there
        view.editor = editor;
    },

    /**
     * Delete the chosen overlay 
     */
    onDeleteOverlays : function(view) {
        var recordid = this.getActiveRecordID();
        var siteNo = this.getSiteControl().items.items[0].getValue().site;
        var overlayName = this.getOverlayControl().getSelectionModel().getLastSelected().data["name"];
        
        Ext.Msg.confirm("Delete", "Are you sure you want to delete the overlay?", function(id)  {
            if (id === "yes") {
                Ext.Ajax.request({
                    method : 'POST',
                    url : '../record/deleteOverlay',
                    params : {
                        recordid : recordid,
                        siteNumber : siteNo,
                        overlay : overlayName 
                    },
                    success : function (result, response){
                        view.fireEvent("refreshOverlayList", recordid);
                    },
                    failure : function(result, request) {
                        showErrorMessage(result.responseText, "Failed to delete overlay");
                    }
                });
            }
        }); 
        
    },
    
    /**
     * Load the overlay with the specified name
     * TODO: duplicate code in RecordSelection.js. Whose responsibility is it?
     * Need to refactor
     */
    onLoadOverlay : function(recordid, overlayName, sliceNo, frameNo, siteNo) {
        Ext.Ajax.request({
            method : 'GET',
            url : '../record/getVisualObjects',
            params : {
                recordid : recordid,
                siteNumber : siteNo,
                frameNumber : frameNo,
                sliceNumber : sliceNo,
                overlay : overlayName 
            },
            success : function(result, request) {
                sketchpad.json(result.responseText, true);
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to load overlay");
            }
        });           
    },
    
    

    /**
     * Save the overlay with the given name and dimensions and objects
     * NOTE: Save can be both a new overlay insert or an update to an existing overlay.
     * allSlices : should the overlay be saved on all slices
     * allFrames : should the overaly be saved on all frames
     */
    onSaveOverlay : function(recordid, overlayName, allSlices, allFrames, siteNo, visualObjects) {
        var sliceNos = null, frameNos = null, i=0;
        if (allSlices) {
            var sliceCount = this.getSliceSlider().maxValue + 1;
            sliceNos = new Array();
            for (i=0 ; i < sliceCount; ++i)
                sliceNos.push(i);
        } else {
            sliceNos = [this.getSliceSlider().getValue()];
        }
        if (allFrames) {
            var frameCount = this.getFrameSlider().maxValue + 1;
            frameNos = new Array();
            for (i=0; i < frameCount; ++i)
                frameNos.push(i);
        } else {
            frameNos = [this.getFrameSlider().getValue()];
        }

        Ext.Ajax.request({
            method : 'POST',
            url : '../record/saveOverlay',
            params : {
                recordid : recordid,
                siteNumber : siteNo,
                frameNumbers : Ext.encode(frameNos),
                sliceNumbers : Ext.encode(sliceNos),
                overlay : overlayName,
                visualObjects : visualObjects
            },
            success : function(result, request) {
                // DO NOTHING
                sketchpad.setDirty(false);
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to save overlay");
            }
        }); 
    },

    getActiveRecordID : function() {
        var lastSelected = this.getThumbnails().getSelectionModel().getLastSelected();
        if (lastSelected === null)
            return null;
        return lastSelected.data.id; 
    },

    /**
     * Called when user clicks on a channel colour button.
     * Shows a color picker and passes on the chosen color to the server followed
     * by image refresh. 
     */
    onChooseChannelLUT : function(view, channelNo, button, e) {
        console.log("on choose color" + view + channelNo);
        var oldData = view.getStore().getAt(channelNo).data;
        var _this = this;

        Ext.Ajax.request({
            method : 'GET',
            url : '../record/getAvailableLUTs',
            success : function(result, request) {
                var luts = Ext.decode(result.responseText);
                _this.showLUTMenu(luts, view, channelNo, oldData.lut);
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to fetch available LUTs from server");
            }
        });
        //win.showAt(view.getEl().getXY());
    },

    /**
     * Show a menu of LUTs
     */
    showLUTMenu : function(luts, view, channelNo, oldName) {
        var _this = this;
        var lutStore = Ext.StoreManager.get('Manage.store.LUTStore');
        var oldLUT = lutStore.getById(oldName);
        var win = Ext.create('Ext.window.Window', {
            height : 250,
            width : 120,
            title : 'Choose LUT',
            layout : 'fit',
            items : [{
                xtype : 'gridpanel',
                store : lutStore,
                autoScroll : true,
                hideHeaders : true,
                columns : [{
                    text : 'Image',
                    dataIndex : 'url',
                    renderer : function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return '<img src="' + record.data.url + '" width=88 height=11/>';
                    }
                }, {
                    text : 'Name',
                    dataIndex : 'name'
                }],
                listeners : {
                    afterrender : function(comp, opts) {
                        comp.getSelectionModel().select(oldLUT, false, true);
                    },
                    selectionchange : function(model, selected, opts) {
                        if (selected.length !== 1)
                            return;
                        var record = selected[0];
                        _this.setLUTSelection(record.data, channelNo);
                    }
                }
            }],
            buttons : [{
                text : 'Close',
                handler : function(){
                    this.up().up().close();
                }
            }]
        });
        win.showAt(view.getEl().getXY());
    },

    /**
     * Save the selected LUT as the channel's LUT for the user
     */
    setLUTSelection : function(lut, channelNo) {
        var recordid = this.getActiveRecordID();
        var _this = this;
        Ext.Ajax.request({
            method : 'POST',
            url : '../record/setChannelLUT',
            params : {
                recordid : recordid,
                channelNumber : channelNo,
                lut : lut.name
            },
            success : function(result, request) {
                // Refresh image and channel store
                _this.refreshChannels(recordid, channelNo, lut.name);
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to set channel LUT"); 
            }
        });
    },
    
    /**
     * LUT of a channel has changed. Refresh image and channels
     */
    refreshChannels : function(recordid, channelNumber, lut) {
        var channelStore = Ext.StoreManager.get('Manage.store.ChannelStore');
        var channelView = this.getChannelControl();
        var _this = this;
        Ext.Ajax.request({
            method : 'GET',
            url : '../record/getChannels',
            params : {
                recordid : recordid
            },
            success : function(result, request) {
                var channels = Ext.decode(result.responseText);
                channelStore.loadData(channels);
                var oldState = channelView.getSelectionModel().isLocked();
                channelView.getSelectionModel().setLocked(false);
                channelView.getSelectionModel().selectAll(true);
                channelView.getSelectionModel().setLocked(oldState);
                _this.getController('Manage.controller.RecordSelection').setImage(recordid);
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to load record channels"); 
            }
        });
    },

    /**
     * Set the colour chosen for the given channel
     */
    setChosenColor : function(colour, channelNo, button) {
        var recordid = this.getActiveRecordID();
        var _this = this;
        Ext.Ajax.request({
            url : '../record/setChannelColour',
            method : 'POST',
            params : {
                recordid : recordid,
                channelNumber : channelNo,
                colour : "#" + colour
            },
            success : function(result, request) {
                // If success, get the image again
                _this.getController('Manage.controller.RecordSelection').setImage(recordid);
                console.log("change button color: " + button);
                var buttonDiv = Ext.get(button).child('div').child('div');
                buttonDiv.setStyle('background-color', '#' + colour);
                //button.getEl().setStyle('background-color', '#' + colour);
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to save channel color change"); 
            }
        });
    },

    /**
     * Show the 3d view
     */
    onShow3D : function (button) {
        var recordid = this.getActiveRecordID() + '';
        var currentFrame = this.getFrameSlider().getValue() + '';
        var currentSite = this.getSiteControl().items.items[0].getValue().site + '';
        var userName = Ext.get("userName").dom.innerHTML;
        window.open('../project/launch3dView?recordid=' + recordid + '&frameNumber=' + currentFrame + '&siteNumber=' + currentSite, '_blank', 'fullscreen=yes');
    },

    /**
     * When change contrast button is clicked
     */
    onChooseContrast : function(view, channelNo, button, e) {
        var recordid = this.getActiveRecordID();
        var currentFrame = this.getFrameSlider().getValue() + '';
        var currentSlice = this.getSliceSlider().getValue();
        var currentSite = this.getSiteControl().items.items[0].getValue().site + '';
        var isZStacked = this.getZproject().pressed;
        var _this = this;
        Ext.Ajax.request ({
            method : 'GET',
            url : '../record/getContrastHistogram',
            params : {
                recordid : recordid,
                frameNumber : currentFrame,
                siteNumber : currentSite,
                sliceNumber : currentSlice,
                channelNumber : channelNo,
                isZStacked : isZStacked
            },
            success : function (result, request) {
                var response= Ext.decode(result.responseText);
                _this.loadContrastData(response, recordid, channelNo, view);
            },
            failure : function (result, response) {
                showErrorMessage(result.responseText, "Failed to fetch contrast settings for image");
            }
        });
    },

    /**
     * Load contrast data and show the contrast view
     */
    loadContrastData : function(data, recordid, channelNo, view) {
        var win = Ext.create('Ext.window.Window', {
            title : 'Image Contrast Settings',
            height : 350,
            width : 320,
            resizable : false,
            layout : 'fit',
            items : [{
                xtype : 'imageContrast',
                recordid : recordid,
                channelNo : channelNo,
                minIntensity : data.minIntensity,
                maxIntensity : data.maxIntensity,
                maxFrequency : data.maxFrequency,
                userMin : data.currentMin,
                userMax : data.currentMax,
                userGamma : data.currentGamma,
                imageSrc : data.imageSrc,
                pixelDepth : data.pixelDepth
            }]
        });
        win.showAt([view.getEl().getX()-80, view.getEl().getY()]);
    },

    /**
     * Change name of the chosen channel
     */
    onChangeChannelName : function(record, cell) {
        var _this = this;
        var linkEl = Ext.query('#' + cell.id + ' a')[0];
        var oldName = record.data.name;
        var wavelength = record.data.wavelength;
        var items = [{
            xtype : 'textfield',
            fieldLabel : 'New Name',
            name : 'text',
            value : oldName,
            allowBlank : false
        }];
        var height = 120;
        if (wavelength > 0) {
            items.push({
                xtype : 'textfield',
                fieldLabel : 'Emission Wavelength',
                name : 'text',
                value : wavelength,
                disabled : true
            });
            height = 150;
        }
        var cellID = cell.id;
        Ext.create('Ext.window.Window', {
            title : 'Change Channel Name',
            width : 300,
            height : height,
            items : [{
                xtype : 'form',
                bodyPadding : 10,
                items : items,
                buttons : [{
                    text : 'OK',
                    handler : function() {
                        var view = this.up().up();
                        var text = view.items.items[0].getValue();
                        view.up().close();
                        _this.setChannelName(text, record, cellID);
                    }
                }]
            }]
        }).show(); 
    },

    /**
     * Set new name for channel
     */
    setChannelName : function(text, record, cellID) {
       Ext.Ajax.request({
            method : 'POST',
            url : '../record/setChannelName',
            params : {
                recordid : record.data.recordid,
                channelNumber : record.data.channelNumber,
                channelName : text 
            },
            success : function(result, request) {
                var linkEl = Ext.query('#' + cellID + ' a')[0];
                var cell = Ext.query('#' + cellID)[0];
                var displayName = text;
                if (record.data.wavelength > 0)
                    displayName = displayName + ' (' + record.data.wavelength + ')';
                linkEl.innerHTML = displayName;
                cell.setAttribute('data-qtip', displayName);
                record.data.name = text;
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to set channel name");
            }
        }); 
    },

    /**
     * Save contrast settings to server
     */
    saveContrastSettings : function(recordid, channelNo, minIntensity, maxIntensity, gamma) {
        var _this = this;
        var isZStacked = this.getZproject().pressed;
        Ext.Ajax.request({ 
            method : 'POST',
            url : '../record/saveContrastSettings',
            params : {
                recordid : recordid,
                channelNumber : channelNo,
                minIntensity : minIntensity,
                maxIntensity : maxIntensity,
                gamma : gamma,
                isZStacked : isZStacked
            },
            success : function(result, request) {
                // Refetch image
            	var store = Ext.StoreManager.get('Manage.store.ChannelContrastStore');
            	var contrastRecord=store.getById(channelNo+'');
            	contrastRecord.set('min',minIntensity+'');
            	contrastRecord.set('max',maxIntensity+'');
            	contrastRecord.set('gamma',gamma+'');
            	
                _this.getController('Manage.controller.RecordSelection').setImage(recordid);
            },  
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to set set custom contrast settings");
            }   
        });  
    },
	
    /**
     * Utility to find a record item given record id
     */
    getRecordItem : function(recordid) {
        var summaryTableItems = this.getSummaryTable().store.data.items;
        for (var index=0; index < summaryTableItems.length; ++index) {
            var nextItem = summaryTableItems[index];
            if (nextItem.data["Record ID"] === recordid) {
                return nextItem;
            }
        }
        return null;
    },

    /**
	 *  change the image size when the panel is resized 
	 */
	onAdjustImage:function(){
		var recordid = this.getActiveRecordID();
		if(recordid !== null ){
			var record = this.getRecordItem(recordid).data;
	        var imageDisplay = Ext.getCmp('imagedisplay');	        
            var scalingFactor=imageDisplay.fitImage(imageDisplay.imageWidth, imageDisplay.imageHeight);
            
            if(this.getRulercontrol().pressed){
            	var hruler=Ext.getCmp('hRuler');
                var xmax=record['Image Width']*record['Pixel Size X'];
                hruler.reload(scalingFactor.width,xmax);

                var vruler=Ext.getCmp('vRuler');
                var ymax=record['Image Height']*record['Pixel Size Y'];
                vruler.reload(scalingFactor.height,ymax);	
            }
            this.application.fireEvent('overlayChanged',recordid);
		}
	},
	
	checkAndSaveOverlays:function(callback){
		var overlayPanel = this.getOverlayControl();
        if (overlayPanel.editor && overlayPanel.editor !== null) {
            var editor = overlayPanel.editor;
            overlayPanel.editor = null;
            
            var toolbar= editor.down('imageEditToolbar');
            var callbck = function() {
                sketchpad.purge();
                sketchpad.setDirty(false);
                sketchpad.mode(false);
                overlayPanel.setDisabled(false);
                callback();
                editor.close();
            };
            
            toolbar.checkAndSave(callbck);
        }
        else{
        	callback();
        }
	}
});
