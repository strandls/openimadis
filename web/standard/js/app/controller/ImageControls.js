/**
 * This handles only those events where some
 * changes have to be made on the controls
 *	Example: change contrast settings
 */

Ext.define('Manage.controller.ImageControls', {
	extend: 'Ext.app.Controller',

	requires: [
		'Manage.view.ImageContrast'
	],

	refs: [{
		ref: 'channels',
		selector: 'channels'
	}, {
		ref: 'ControlArea',
		selector: '#controlArea'
	}, {
		ref: 'InnerControlArea',
		selector: '#innerControlArea'
	}, {
		ref: 'zoom',
		selector: 'zoom'
	}, {
		ref: 'thumbnails',
		selector: '#imageThumbnails'
	}, { 
		ref:'frameSlider',
		selector:'imagesliders #frameslider' 
	}, {
		ref: 'sliceSlider',
		selector: 'imagesliders #sliceslider'
	}, {
		ref: 'siteControl',
		selector: '#sitecontrol'
	}, {
		ref: 'checkboxZProjection',
		selector: '#checkboxZProjection'
	}, {
		ref: 'checkboxChannels',
		selector: '#checkboxChannels'
	}, {
		ref: 'checkboxScalebar',
		selector: '#checkboxScalebar'
	}],
	

	controllers: ['RecordController'], //creates getter
	stores: [ //creates getter
		'Channels', 'ChannelContrasts'
	], 
	views: ['Thumbnails'], //creates getter named getThumbnailsView()

	init: function() {
		this.control({
			'channels': {
				'chooseChannelLUT': this.onChooseChannelLUT,
				'chooseContrast': this.onChooseContrast,
				'changeChannelName':this.onChangeChannelName,
				select: this.remove ,
				deselect: this.remove
			},
			'#buttonChannels': {
				click: this.onChannelsClicked
			},
			'#buttonSites': {
				click: this.onSitesClicked
			},
			'#buttonOverlays': {
				click: this.onOverlaysClicked
			}, 
			'#buttonZoom': {
				click: this.onZoomClicked
			}, 
			'#buttonReset': {
				click: this.onResetClicked
			}, 
			'thumbnails': {
				select: this.remove //set the ControlArea
			},
			'imagecontrast' : {
				saveSettings : this.saveContrastSettings
			}
		});
		console.log('image controls inited');
	},

	/**
	 * reset button clicked, load channels, luts and contrast dialog
	 */
	onResetClicked: function() {
		console.log('imagecontrosl [onresetclicked]');
		this.setActiveItem(0);
		
		var rc = this.getRecordControllerController();
		rc.isZoomed=false;
		rc.onImageChange();
		
		//fire event to show scale bar
		var showScalebar=this.getCheckboxScalebar().getValue();
		
		var zoom = this.getZoom();
		if(showScalebar)
			zoom.fireEvent('showScalebar');
		
	},
	
	/**
	 * channel button clicked, load channels, luts and contrast dialog
	 */
	onChannelsClicked: function() {
		console.log('imagecontrosl [onchannelsclicked]');
		this.setActiveItem(0);
	},

	/**
	 * sites button clicked, change viewing area
	 */
	onSitesClicked: function() {
		console.log('imagecontrols [onsitesclicked]');
		this.setActiveItem(1);
	},
	
	/**
	 * overlays button clicked, change viewing area
	 */
	onOverlaysClicked: function() {
		console.log('imagecontrols onOverlaysClicked');
		this.setActiveItem(2);
	},

	/**
	 * zoom buttoon clicked, switch to pan-zoom viewing area
	 */
	onZoomClicked: function() {
		console.log('imagecontrols onZoomClicked');
		this.setActiveItem(3);
		
		this.getRecordControllerController().isZoomed=true;

		//disable the All Channels checkbox
		this.getCheckboxChannels().disable();

		//initialize zoom image and fire the zoom event
		var zoom = this.getZoom();
		zoom.fireEvent('zoomWindowChanged');	
		
		//fire event to show scale bar
		var showScalebar=this.getCheckboxScalebar().getValue();
		
		//update the zoom thumbnail
		this.getRecordControllerController().setZoomThumbnail();
		
		if(showScalebar)
			zoom.fireEvent('showScalebar');
	},
	
	/**
	 * set active item
	 *
	 * @params {int} num active item to set to
	 */
	setActiveItem: function(num) {
		var layout = this.getInnerControlArea().getLayout();
		var zoom = false;
		
		//if zoom is current active item, then on change reload image
		if(layout.getActiveItem().xtype === 'zoom') {
			zoom = true;

			// if number of channel greater that one enable Channels checkbox
			if(this.getRecordControllerController().record['Channel Count'] > 1) {
				this.getCheckboxChannels().enable();
			}
		}
		
		this.remove();

		/*
		 * order has to be maintained
		 * First we have to change active item and then call set image, since
		 * the set image is dependent on active item
		 */
		layout.setActiveItem(num);
		if(zoom) {
			//TODO make this more elegant
			//will have to change the adjustclipping function here, 
			//instead set flags in imagePanel
			var rc = this.getRecordControllerController();
			rc.onImageChange();
		}

	},
	
	
	/**
     * Change name of the chosen channel
     * Called when user clicks on channel name
     */
    onChangeChannelName : function(record, cell) {
        var _this = this;
        var linkEl = Ext.query('#' + record.internalId)[0];
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
       
               
	    var editChannelName = Ext.create('Ext.panel.Panel', {
	    	title: 'Change Channel Name',
			autoScroll: true,
			flex: 2,
			hideHeaders: true,
			layout: 'fit',
			closable: true,
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
                        _this.setChannelName(text, record, cell);
                    }
                }]
            }]
		});
	
		this.add(editChannelName);


    },

    /**
     * Set new name for channel
     */
    setChannelName : function(text, record, cell) {
	   var rc = this.getRecordControllerController();
	   var recordid = rc.getCurrentRecordId();
	   
	   var me=this;
       Ext.Ajax.request({
            method : 'POST',
            url : '../record/setChannelName',
            params : {
                recordid : recordid,
                channelNumber : record.data.channelNumber,
                channelName : text 
            },
            success : function(result, request) {            	
                Ext.Ajax.request({
                    method : 'POST',
                    url : '../record/getChannels',
                    params : {
                        recordid : recordid,
                        channelNumber : record.data.channelNumber,
                        channelName : text 
                    },
                    success : function(result, request) {
                    	var resp=Ext.JSON.decode(result.responseText);
                    	me.getChannelsStore().loadData(resp);
                    	me.getChannels().getSelectionModel().selectAll(true);
                    },
                    failure : function(result, request) {
                        showErrorMessage(result.responseText, "Failed to set channel name");
                    }
                });
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to set channel name");
            }
        }); 
    },

		

	/**
	 * Called when user clicks on a channel contrast button.
	 * Show a contrast histogram of present slice and 
	 * the min, max intensities across the record
	 * TODO apply the min and max values
	 */
	onChooseContrast: function(view, channelNo, channelName) {
		console.log('ImageControls <onChooseContrast>');
		
		var _this = this;

		var rc = this.getRecordControllerController();
		var recordid = rc.getCurrentRecordId();

		var currentFrame = this.getFrameSlider().getValue();
		var currentSlice = this.getSliceSlider().getValue();
		var currentSite = this.getSiteControl().items.items[0].getValue().site; //TODO make this elegant
		var isZStacked = this.getCheckboxZProjection().getValue(); 

		Ext.Ajax.request({
			method: 'GET',
			url: '../record/getContrastHistogram',
			params: {
				channelNumber: channelNo,
				recordid: recordid,
				frameNumber: currentFrame,
				sliceNumber: currentSlice,
				siteNumber: currentSite,
				isZStacked: isZStacked
			},
			success: function(result, request) {
				var response = Ext.JSON.decode(result.responseText);
				_this.loadContrastData(response, recordid, channelNo, channelName );
			},
			failure: function(result, response) {
				//TODO
			}
		});
	},

	/**
	 * Load the contrast data
	 */
	loadContrastData: function(data, recordid, channelNo, channelName) {

		var contrast = Ext.create('Manage.view.ImageContrast', {
			closable: true,
			title: 'Image Contrast for ' + channelName,
			channelNo: channelNo,
			imageSrc: data.imageSrc,
			maxFrequency: data.maxFrequency,
			maxIntensity: data.maxIntensity,
			minIntensity: data.minIntensity,
			pixelDepth: data.pixelDepth,
			recordid: recordid,
			userGamma: data.currentGamma,
			userMax: data.currentMax,
			userMin: data.currentMin,
			flex: 2,

			//setting width to fixed, else histogram not displaying properly
			maxWidth: 300,
			minWidth: 300
		});

		this.add(contrast);	
	},

	/**
	 * Called when user clicks on a channel color button.
	 * Shows a color picker and passes on the chosen color to the server
	 * followed by refresh
	 * TODO send to server and image refresh
	 */
	onChooseChannelLUT: function(view, channelNo, channelName) {
		console.log('luted <>');
		//get control panel and add luts
		var _this = this;
		var oldData = view.getStore().getAt(channelNo).data;
		var lutStore = Ext.StoreManager.get('LUTs');
		var oldLut = lutStore.getById(oldData.lut);

		var lutPanel = Ext.create('Ext.grid.Panel', {
				store: lutStore,

				autoScroll: true,
				flex: 2,
				hideHeaders: true,
				title: 'Select LUT for ' + channelName,

				closable: true,

				columns: [{
					text: 'Image',
					flex: 1,
					dataIndex: 'url',
					renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
						return '<img src="' + record.data.url + '" width=88 height=11/>';
					}
				}, {
					text: 'Name',
					flex: 1,
					dataIndex: 'name'
				}],

				listeners: {
					viewready: function(comp, opts) {
						comp.getSelectionModel().select(oldLut, false, true);

						//put selected LUT in focus
						var view = comp.getView();
						var item = comp.getSelectionModel().getLastSelected();
						view.focusNode(item);
					},
					selectionchange: function(model, selected, opts) { 
						if (selected.length !== 1) {
						    return;
						}
						var record = selected[0];
						_this.setLUTSelection(record.data, channelNo);
					}
				}
		});

		this.add(lutPanel);
	},

	/**
	 * Save the selected LUT as the channel LUT for the user
	 */
	setLUTSelection: function(lut, channelNo) {
		var rc = this.getRecordControllerController();
		var recordid = rc.getCurrentRecordId();
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
		var recordController = this.getRecordControllerController();
		var channelStore = this.getChannelsStore();
		var channelView = this.getChannels();
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
			recordController.onImageChange();
		    },
		    failure : function(result, request) {
			showErrorMessage(result.responseText, "Failed to load record channels"); 
		    }
		});
	},
	/**
	* Save contrast settings to server
	*/
	saveContrastSettings : function(recordid, channelNo, minIntensity, maxIntensity, gamma) {
		var _this = this;
		var isZStacked = this.getCheckboxZProjection().getValue();
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
			var store = _this.getChannelContrastsStore(); 
			var contrastRecord = store.getById(channelNo+'');
			contrastRecord.set('min',minIntensity+'');
			contrastRecord.set('max',maxIntensity+'');
			contrastRecord.set('gamma',gamma+'');
			
			var rc = _this.getRecordControllerController();
			rc.onImageChange();
		    },  
		    failure : function(result, request) {
			showErrorMessage(result.responseText, "Failed to set set custom contrast settings");
		    }   
		});  
	},

	/**
	 * add panel to controlArea
	 *
	 * @params {panel} pan panel to add
	 */
	add: function(pan) {
		var cnt = this.getControlArea();
		this.remove();
		cnt.add(pan);
	},

	/**
	 * function to remove last added item if any
	 * TODO make this more elegant
	 */
	remove: function() { 
		var cnt = this.getControlArea();
		if(cnt.items.length > 1) {
			cnt.remove(cnt.items.items[1]); //TODO make this code cleaner
		}
	}
});


