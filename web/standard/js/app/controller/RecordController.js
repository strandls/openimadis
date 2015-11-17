/**
 * All controls related to the Image changes
 */
Ext.define('Manage.controller.RecordController', {
	extend: 'Ext.app.Controller',

	controllers: [
		//creates getters
		'ImageView'
	],

	models: [
		//creates getters example: getImageMetaDataModel()
		'ImageMetaData'
	],

	stores: [ 
		//creates getters example: getCommentsStore()
		'Comments', 'Channels', 'ChannelContrasts', 'Historys', 
		'ImageMetaDatas', 'RecordMetaDatas', 'SystemAttachments', 'UserAttachments',
		'UserAnnotations', 'Overlays', 'RecordThumbnails'
	],
	
	refs: [{
		ref: 'thumbnails',
		selector: '#imageThumbnails'
	},{
		ref: 'history',
		selector: 'history'
	}, {
		ref: 'siteControl',
		selector: '#sitecontrol'
	},{
		ref: 'checkboxChannels',
		selector: '#checkboxChannels'
	},{
		ref: 'checkboxLegends',
		selector: '#checkboxLegends'
	},{
		ref: 'checkboxScalebar',
		selector: '#checkboxScalebar'
	}, {
		ref: 'checkboxZProjection',
		selector: '#checkboxZProjection'
	}, {
		ref: 'checkboxGreyScale',
		selector: '#checkboxGreyScale'
	},{
		ref: 'checkboxFitWidth',
		selector: '#checkboxFitWidth'
	}, {
		ref: 'checkboxFullResolution',
		selector: '#checkboxFullResolution'
	}, {
		ref: 'sliceSlider',
		selector: 'imagesliders #sliceslider'
	}, { 
		ref:'frameSlider',
		selector:'imagesliders #frameslider'
	}, {
		ref: 'sliceField',
		selector: 'imagesliders #slicefield'
	}, {
		ref : 'frameField',
		selector : 'imagesliders #framefield'
	}, {
		ref : 'slicePanel',
		selector : 'imagesliders #slicepanel'
	}, {
		ref : 'framePanel',
		selector : 'imagesliders #framepanel'
	}, {
		ref: 'attachments',
		selector: 'attachments'
	}, {
		ref: 'imageMetaData',
		selector: 'imagemetadata'
	}, {
		ref: 'channels',
		selector: 'channels'
	}, {
		ref: 'overlays',
		selector: 'overlays #checkboxgrid'
	}, {
		ref: 'imagePanel',
		selector: 'imagepanel'
	}, {
		ref: 'zoom',
		selector: 'zoom'
	}, {
		ref: 'innerControlArea',
		selector: '#innerControlArea'
	}, {
		ref: 'imageview',
		selector: 'imageView'
	}, {
		ref: 'comments',
		selector: 'comments'
	}, {
		ref: 'screenshotButton',
		selector: '#screenshotButton'
	}, {
		ref: 'zoomButton',
		selector: '#buttonZoom'
	
	}],
	
	isZoomed: false,

	/**
	 * data pertaining to current selected record.
	 *  Value is set with data from server call
	 */
	record: '',

	init: function() {
		this.control({
			'channels': {
				select: this.onChannelChange,
				deselect: this.onChannelChange
			},'overlays #checkboxgrid': {
				selectionchange: this.onOverlayChange
			},'overlays':{
				refreshOverlayList: this.setOverlays
			},'#imageThumbnails': {
				select: this.onChange
			}, 'imagesliders #sliceslider': {
				changecomplete : this.onImageControlChange
			}, 'imagesliders #frameslider': {
				changecomplete : this.onImageControlChange
			}, '#controlChecks': {
				change: this.onControlChecks
			}, 'imagesliders': {
				'previousSlice': this.onPreviousSlice,
				'nextSlice': this.onNextSlice,
				'previousFrame': this.onPreviousFrame,
				'nextFrame': this.onNextFrame
			}, '#sitecontrol > radiogroup': {
				change: this.onSitesControlChanged
			}, 'headers #setThumbnailButton': {
				click: this.onThumbnailButtonClick
			},'imagepanel': {
				'imageLoaded': this.onImageLoad
			}
		});
	},


	/*
	 *--------------------------------------------------------------------------------
	 * 
	 * Entry Point functions for all the events handled by this controller
	 *
	 *--------------------------------------------------------------------------------
	 */

	/**
	 * channel has changed
	 */
	onChannelChange: function() {
		var recordid = this.getCurrentRecordId();

		console.log('RecordController [onChannelsChange]');
		this.onImageChange();
	},

	/**
	 * On any control check change call this function
	 * Control checks are Z Projection, Grey Scale, etc
	 */
	onControlChecks: function(){
		var recordid = this.getCurrentRecordId();
		if(!recordid)
		{
			console.log("RecordController onControlChecks recordid="+recordid);
			return;
		}
		console.log('RecordController [oncontrolchecks]');

		//if z stacked disbale slice frame
		if(this.getCheckboxZProjection().getValue()) {
			this.getSlicePanel().setDisabled(true);
		} else {
			this.getSlicePanel().setDisabled(false);
		}

		// if all channels then disable pan and zoom
		if(this.getCheckboxChannels().getValue()) {
			this.getZoomButton().disable();
		} else {
			this.getZoomButton().enable();
		}

		this.onImageChange();
	},
		
	/**
	 * sliders have been moved, reload image
	 *
	 */
	onImageControlChange: function() {
		var recordid = this.getCurrentRecordId();

		console.log('RecordController image changed');
		this.setImageSliders();
		this.onImageChange();
	},

	/*
	 * a thumbnail has been selected
	 * @param {Manage.model.Thumbnails} record The instance of the record clicked
	 */
	onChange: function(comp, record) {
		console.log('on images changed');
		var me = this;
		
		this.activeRecordID = record.get('id');
		this.isZoomed=false;
		
		//get record data and on success load record data
		Ext.Ajax.request({
			method: 'GET',
			url: '../record/getRecordData',
			params: {
				recordid: record.get('id')
			},
			success: function(result, request) {
				var record = Ext.JSON.decode(result.responseText);
				me.record = record;
				me.loadRecordData(record);
			},
			failure: function(result, request) {
				showErrorMessage(result.responseText, "Loading record data failed");
			}
		});

	},
	
	/**
	 * slice value changed
	 */
	onPreviousSlice: function() {
		var slider = this.getSliceSlider();
		var value = slider.getValue();
		if(value > 0) {
			slider.setValue(value - 1); 
		}
		this.onImageControlChange();
	},

	/**
	 * slice value changed
	 */
	onNextSlice: function() {
		var slider = this.getSliceSlider();
		var value = slider.getValue();
		if(value < slider.maxValue) {
			slider.setValue(value + 1); 
		}
		this.onImageControlChange();
	},

	/**
	 * frame value changed
	 */
	onPreviousFrame: function() {
		var slider = this.getFrameSlider();
		var value = slider.getValue();
		if(value > 0) {
			slider.setValue(value - 1); 
		}
		this.onImageControlChange();
	},

	/**
	 * frame value changed
	 */
	onNextFrame: function() {
		var slider = this.getFrameSlider();
		var value = slider.getValue();
		if(value < slider.maxValue) {
			slider.setValue(value + 1); 
		}
		this.onImageControlChange();
	},
		
	/**
	 * sites have changed, reload image with that site
	 */
	onSitesControlChanged: function() {
		// on sites change just reload the same record
		// inside set image function value of current site will be read
		this.onImageChange();
	},
	
	/*
	 *--------------------------------------------------------------------------------
	 *
	 * functions related to loading the record data
	 *
	 *--------------------------------------------------------------------------------
	 */

	/**
	 * handler function that loads all the image metadata and controls
	 */
	loadRecordData: function(record) {
		var recordid = record['Record ID'];

		//set the initial control as Channels 
		var layout = this.getInnerControlArea().getLayout();
		layout.setActiveItem(0);

		//load contrast settings 
		//and call onImageChange when loaded
		this.loadContrastSettings(recordid, this.onImageChange, this);

		//set the image controls
		this.setImageControls(record);

		//set attachments for this record
		this.setAttachments(recordid);

		//set comments for this record
		this.setComments(recordid);

		//set history for this record
		this.setHistory(recordid);

		//set metadata for this record
		this.setRecordMetadata(record);

		//set user annotations
		this.setUserAnnotations(recordid);

		//set image sliders
		this.setImageSliders();
	},
	 
	/**
	 *load the contrast settings and call fn as callback 
	 */
	loadContrastSettings: function(recordid, fn, scope) {
		var store = this.getChannelContrastsStore();
		var isZStacked = this.getCheckboxZProjection().getValue(); 

		var me = this;
		store.load({
			params: {
				'recordid': recordid,
				'isZStacked': isZStacked 
			},
			callback : function(){
				fn.call(scope);
			}
		});

	},

	/**
	 * set attachments
	 */
	setAttachments: function(recordid) {
		console.log('RecordController <setAttachments>');
		var userStore = this.getUserAttachmentsStore();
		var sysStore = this.getSystemAttachmentsStore();

		this.getAttachments().setRecordId(recordid);
		
		var params = { recordid: recordid};

		userStore.load({
			params: params
		});

		sysStore.load({
			params: params
		});

	},

	/**
	 * set the comments for this record
	 */
	setComments: function(recordid) {
		console.log('RecordController <setComments>');
		var comments = this.getComments();
		comments.updateComments(recordid);
		this.getCommentsStore().getProxy().extraParams={"recordid":recordid};
	},

	/**
	 * set history for this record
	 */
	setHistory: function(recordid) {
		console.log('RecordController <setHistory>');
		var store = this.getHistorysStore();

		store.load({
			params: {
				recordid: recordid
			}
		});
		
		this.getHistory().setRecordID(recordid);
		this.getHistorysStore().getProxy().extraParams={"recordid":recordid};
	},

	/**
	 * set the image metadata
	 */
	setImageMetaData : function () {
		var recordid = this.getCurrentRecordId();
		var sliceCount = this.getSliceSlider().getValue();
		var frameCount = this.getFrameSlider().getValue();

		var isGreyScale = this.getCheckboxGreyScale().getValue(); 
		var isZStacked = this.getCheckboxZProjection().getValue(); 
		var isMosaic = this.getCheckboxChannels().getValue(); 
		var isFullRes = this.getCheckboxFullResolution().getValue();

		var siteNumber = this.getSiteControl().items.items[0].getValue().site; //TODO make this elegant
		var channelNumbers = [];
		var checkBoxes = this.getChannels().getSelectionModel().getSelection();

		var i;
		for (i = 0; i < checkBoxes.length; ++i) {
			if (checkBoxes[i].data) {
				var channelNo = checkBoxes[i].data.channelNumber;
				channelNumbers.push(channelNo);
			}
		}

		var imageMetaData = this.getImageMetaData();
		var model = this.getImageMetaDataModel();
		var store = this.getImageMetaDatasStore(); //model to change

		var fields = []; //new fields to add to model

		imageMetaData.columns = [];
		fields.push('field');
		imageMetaData.columns.push({header: 'Field', dataIndex: 'field', flex : 1});
		imageMetaData.headerCt.removeAll();
		imageMetaData.headerCt.add(Ext.create('Ext.grid.column.Column', {text: 'Field', dataIndex: 'field', flex : 1}));
		for (i = 0; i < channelNumbers.length; ++i) { 
		    fields.push(channelNumbers[i]+"");
		    imageMetaData.columns.push({header: channelNumbers[i]+"", dataIndex: channelNumbers[i]+""});
		    imageMetaData.headerCt.add(Ext.create('Ext.grid.column.Column', {text: channelNumbers[i]+"", dataIndex: channelNumbers[i]+"", flex : 1}));
		}
		imageMetaData.getView().refresh();
		
		model.setFields(fields);
		
		Ext.Ajax.request( {
		    method : 'GET',
		    url : '../project/getImageMetaData',
		    params : {
			recordid : recordid,
			sliceNumber : sliceCount,
			frameNumber : frameCount,
			siteNumber : siteNumber
		    },
		    success : function(result, response) {
		    	var data = Ext.decode(result.responseText);
				store.loadData(data);
		    },
		    failure : function(result, response) {
			showErrorMessage(result.responseText, "Failed to load image meta data");
		    }
		});
	},


	/**
	 * set the image slider values
	 */
	setImageSliders: function() {
		var sliceCount = this.getSliceSlider().getValue();
		var frameCount = this.getFrameSlider().getValue();

		var maxSlices =  this.getSliceSlider().maxValue + 1;
		var maxFrames = this.getFrameSlider().maxValue + 1;
		this.getSliceField().setText("Z: " + (sliceCount + 1) + "/" + maxSlices);
		this.getFrameField().setText("T: " + (frameCount + 1)  + "/" + maxFrames);
	},

	/**
	 * set record metadata for this record
	 *
	 * @params {../record/getRecordData} record all record data
	 */
	setRecordMetadata: function(record) {
		var store = this.getRecordMetaDatasStore();
		var data = [];
		var key;
		var value;

		//create name, value map
		for(key in record) {
			if(record.hasOwnProperty(key) && key!='Archive Signature') {
				value = record[key];
				if (!(value instanceof Object))
					data.push({'name': key, 'value': value});
			}
		}
		
		store.loadData(data);
	},


	/**
	 * set user annotations for this record
	 */
	setUserAnnotations: function(recordid) {
		var store = this.getUserAnnotationsStore();

		store.load({
			params: {
				recordid: recordid
			}
		});
	},


	/*
	 *--------------------------------------------------------------------------------
	 *
	 * functions related to loading controls of the record
	 *
	 *--------------------------------------------------------------------------------
	 */

	/*
	 * set up the image controls
	 */
	setImageControls: function(record) {
		console.log('RecordController setImageControls');
		
		var sliceCount = record['Slice Count'];
		var frameCount = record['Frame Count'];
		
		var channelStore = this.getChannelsStore();
		var channels = this.getChannels();
		
		//load channel store
		channelStore.loadData(record['Channels']);
		
		//select all channels
		channels.getSelectionModel().selectAll(true);
		
		this.getSliceField().setText("Z: 1/" + (sliceCount));
		this.getFrameField().setText("T: 1/" + (frameCount));
		
		this.getSliceSlider().setMaxValue(sliceCount-1);
		this.getFrameSlider().setMaxValue(frameCount-1);
		
		this.getSliceSlider().setValue(0);
		this.getFrameSlider().setValue(0);
		
		if (sliceCount <= 1) {
			// dont show slice panel
		    this.getSlicePanel().hide();
		    // disable z projection
		    this.getCheckboxZProjection().disable();
		} else {
			// show slice panel
		    this.getSlicePanel().show();
		    // enable z projection
		    this.getCheckboxZProjection().enable();
		}
		
		var channelCount = record['Channel Count'];
		if(channelCount <= 1)
		{
			// disable multichannel
			this.getCheckboxChannels().disable();
		} else {
			// enable multichannel
			this.getCheckboxChannels().enable();
		}

		if (frameCount <= 1) {
		    this.getFramePanel().hide();
		} else {
		    this.getFramePanel().show();
		}

		this.setSiteControls(record['Sites']);
		
		this.setOverlays(record['Record ID']);
	},
	
	/**
     * Set overlays for this record
     */
    setOverlays : function(recordid, overlayName) {
    	console.log('setOverlays '+recordid);
        var sliceNumber = this.getSliceSlider().getValue();
        var frameNumber = this.getFrameSlider().getValue();
        var siteNumber = this.getSiteControl().items.items[0].getValue().site;
        
        var _this = this;
        Ext.Ajax.request({
            method : 'GET',
            url : '../record/getVisualOverlays',
            params : {
                recordid : recordid,
                siteNumber : siteNumber,
                frameNumber : frameNumber,
                sliceNumber : sliceNumber
            },
            success : function(result, request) {
                var overlays = Ext.decode(result.responseText);
                console.log(overlays);
                if (overlays !== null)
                    _this.loadOverlays(overlays, overlayName);
            },
            failure : function(result, request){
                showErrorMessage(result.responseText, "Failed to fetch visual overlays");
            }
        });
    },
    
    /**
     * Load the overlays to the panel
     */
    loadOverlays : function(values, overlayName) {
        var overlayStore = this.getOverlaysStore();
        overlayStore.loadData(values);
        
        this.getOverlays().getSelectionModel().deselectAll();
        
        console.log("RecordController loadOverlays"+values);
        
        // Call to cleanup any old state on the sketchpad
        this.onOverlayChange();
    },
    
    /**
     * Load overlays based on the change made
     */
    onOverlayChange : function() {
        var recordid = this.getCurrentRecordId();
        console.log("RecordController onOverlayChange");
        console.log(recordid);
        if(recordid !== null){
        	var url=this.getOverlayTransparencyUrl(recordid);
        	console.log(url);
        	this.getImagePanel().setOverlayTransparancy(url);
        }
    },
    
    /**
     * Get overlay transparency for the current record with the current settings
     */
    getOverlayTransparencyUrl : function(recordid) {
        if (!recordid  || (recordid === null))
            recordid = this.activeRecordID;
        var sliceCount = this.getSliceSlider().getValue();
        var frameCount = this.getFrameSlider().getValue();
        
        var siteNumber = this.getSiteControl().items.items[0].getValue().site;
        
        console.log("recordController getOverlayTransparencyUrl slice="+sliceCount+" frame="+frameCount+" site="+siteNumber);
        
        //Overlays are fetched seperately
        var overlayNames = new Array();
        var checkBoxGrid = this.getOverlays();

        var selected = checkBoxGrid.getSelectionModel().getSelection();
        console.log(selected);
        for (var j=0; j< selected.length; ++j) {
            overlayNames.push(selected[j].data.name);
        }
        var overlayNamesURI = encodeURIComponent(Ext.encode(overlayNames));
        
        var imageURL = '../project/getOverlayTransparency?recordid='+recordid+'&sliceNumber='+sliceCount+'&frameNumber='+frameCount+'&siteNumber='+siteNumber;
        imageURL += '&overlays=' + overlayNamesURI;
        imageURL += '&height=' + Math.round(this.getImageViewController().getViewHeight());
        imageURL += '&timestamp=' +new Date().getTime();// for preventing browserside url caching

        return imageURL;
    },
    
    /**
     * Get overlay transparency for the current record with the current settings
     */
    getScalebarTransparencyUrl : function(recordid) {
        if (!recordid  || (recordid === null))
            recordid = this.activeRecordID;
        var sliceCount = this.getSliceSlider().getValue();
        var frameCount = this.getFrameSlider().getValue();
        
        var siteNumber = this.getSiteControl().items.items[0].getValue().site;
        
        console.log("recordController getScalebarTransparencyUrl slice="+sliceCount+" frame="+frameCount+" site="+siteNumber);
        
        //Overlays are fetched seperately
        var overlayNames = new Array();
        var overlayNamesURI = encodeURIComponent(Ext.encode(overlayNames));
        
        var imageURL = '../project/getOverlayTransparency?recordid='+recordid+'&sliceNumber='+sliceCount+'&frameNumber='+frameCount+'&siteNumber='+siteNumber;
        imageURL += '&overlays=' + overlayNamesURI;
        
//        var scalebarValue = this.getCheckboxScalebar().getValue();
        var scalebarValue = false;
        imageURL += '&scalebar=' + scalebarValue;

        return imageURL;
    },

	/**
	 * set site controls
	 */
	setSiteControls: function(sites) {
		console.log('RecordController setSiteControls');
		var sitePanel = this.getSiteControl();
		var items = [];
		var checked = true;
		var i;

		sitePanel.removeAll();
		
		for (i = 0; i < sites.length; ++i) {
		    var radio = Ext.create('Ext.form.field.Radio', {
			boxLabel : sites[i].name,
			labelAlign: 'right',
			name : 'site',
			checked : checked,
			inputValue : i,
			id : i + ""
		    });
		    items.push(radio);
		    if (i === 0)
			checked = false;
		}
		if (items.length > 0)
		    items[0].checked = true;

		var group = Ext.create('Ext.form.RadioGroup', {
		    hideLabel : true,
		    items : items,
		    vertical : true,
		    layout : 'anchor'
		});
		sitePanel.items.add(group);
		sitePanel.doLayout();
	},

	/*
	 *--------------------------------------------------------------------------------
	 * functions related to setting the image and the overlay
	 *
	 *--------------------------------------------------------------------------------
	 */

	/**
	 * set the zoom thumbnail
	 */
	setZoomThumbnail: function()
	{
		this.getZoom().setParams(this.getImageURL(), this.record);
		this.setImage();
	},
	
	/**
	 * Set the image as per the current record and the viewwing mode
	 * @private
	 * NOTE - when image changes DO NOT call this function, 
	 *   instead call onImageChange
	 */
	setImage: function() {
		var record = this.record;
		var recordid = record['Record ID'];
		
		// get the record properties 
		this.getImageViewController().setRecordWidth(record['Image Width']);
		this.getImageViewController().setRecordHeight(record['Image Height']);
		
		this.getImageViewController().setChannelCount(record['Channel Count']);
		
		var isMosaic = this.getCheckboxChannels().getValue();
		this.getImageViewController().setMosaic(isMosaic);
		
		// legend block starts
		var showLegends = this.getCheckboxLegends().getValue();
		this.getImagePanel().setLegendsOn(showLegends);
		
		//scalebar
		var showScalebar=this.getCheckboxScalebar().getValue();
		this.getImagePanel().setScalebarOn(showScalebar);		
		
		if(showScalebar){
			var zoom = this.getZoom();
			zoom.fireEvent('showScalebar');
		}
		else{
			this.getImageViewController().addScalebar();
		}
		
		var text = this.getLegendText();
		
		// get the image URL
		var imageURL = this.getImageURL();

		showConsoleLog('RecordController', 'setImage', 'entry');
		console.log(imageURL);

		var layout = this.getInnerControlArea().getLayout();
		var item = layout.getActiveItem();

		/**
		 * IF ZOOM is pressed flow of loading image is different
		 */
		if (item.xtype === 'zoom')
		{
			//set mode to zoom
			var ivc = this.getImageViewController();
			ivc.setViewingMode('zoom');//TODO see if this should be shifted to onchange event

			// we are in pan and zoom panel
			var zoom = this.getZoom();
			zoom.setZoomMode(true);
			zoom.fireEvent('zoomWindowChanged', url);
			
			var url=this.getOverlayTransparencyUrl(recordid);
			this.getImagePanel().setOverlayTransparancy(url);			
			
		}
		else  
		{
			var zoom = this.getZoom();
			zoom.setZoomMode(false);
			
			var ivc = this.getImageViewController();
			ivc.setViewingMode('fullsize');//TODO see if this should be shifted to onchange event

			var fitWidth = this.getCheckboxFitWidth().getValue();
			showConsoleLog("RecordController", "setImage", "fitWidth="+fitWidth);
			var imageWidth = record['Image Width'];
			var imageHeight = record['Image Height'];
			if(fitWidth)
			{
				this.getImageViewController().setFitWidthParameters(imageWidth, imageHeight);
			}
			else
			{
				this.getImageViewController().setFullSizeParameters(imageWidth, imageHeight);
			}
			
			this.getImagePanel().setActiveWidth(imageWidth);
			this.getImagePanel().setActiveHeight(imageHeight);
			this.getImagePanel().setImage(imageURL, recordid, imageWidth, imageHeight);

			var url=this.getOverlayTransparencyUrl(recordid);
			this.getImagePanel().setOverlayTransparancy(url);			
		}
		
		if(this.isZoomed){
			ivc.setViewingMode('zoom');//TODO see if this should be shifted to onchange event

			// we are in pan and zoom panel
			var zoom = this.getZoom();
			zoom.fireEvent('zoomWindowChanged', url);
			
			var url=this.getOverlayTransparencyUrl(recordid);
			this.getImagePanel().setOverlayTransparancy(url);
		}
	},
	
	getLegendText : function() 
	{
		var recordid = this.activeRecordID;
		var sliceNumber = this.getSliceSlider().getValue();
		var frameNumber = this.getFrameSlider().getValue();
		var siteNumber = this.getSiteControl().items.items[0].getValue().site;
		
		var _this = this;
		
		var settingsMessage = "Select legend fields from Settings menu";
		
		Ext.Ajax.request({
            method : 'GET',
            url : '../project/getLegends',
            params : {
				recordid : recordid,
				sliceNumber : sliceNumber,
				frameNumber : frameNumber,
				siteNumber : siteNumber
            },
            success : function(result, request) {
            	var legends = Ext.decode(result.responseText);
            	console.log(legends.legend);
            	var legendMessage = legends.legend;
            	if(legendMessage == "")
            		legendMessage = settingsMessage;
            	
            	_this.getImagePanel().setLegendText(legendMessage);
				return legends.legend;
            },
            failure : function(result, request) {
            	console.log('addLegends failed');
            	_this.getImagePanel().setLegendText("unavailable");
            	return "unavailable";
            }
		});
	},
	
	
	/**
	* Get image url for the current record with the current settings
	*/
	getImageURL : function() {
		var recordid = this.getCurrentRecordId();
		var sliceCount = this.getSliceSlider().getValue();
		var frameCount = this.getFrameSlider().getValue();

		var isGreyScale = this.getCheckboxGreyScale().getValue(); 
		var isZStacked = this.getCheckboxZProjection().getValue(); 
		var isMosaic = this.getCheckboxChannels().getValue(); 
		var isFullRes = this.getCheckboxFullResolution().getValue();

		var siteNumber = this.getSiteControl().items.items[0].getValue().site; //TODO make this elegant
		var channelNumbers = [];
		var channelDetails = {};

		var contrastStore = this.getChannelContrastsStore();
		var channelStore = this.getChannelsStore();
		
		var channelDetails={};
		var checkBoxes = this.getChannels().getSelectionModel().getSelection();
		
		if(isMosaic){
			checkBoxes=channelStore.data.items;
		}

		var i;
		for (i = 0; i < checkBoxes.length; ++i)
		{
			if (checkBoxes[i].data)
			{
				var channelNo = checkBoxes[i].data.channelNumber;
				channelNumbers.push(channelNo);
				
				/**
				 * for the sake of LUTs we need the channel details to be part of URL
				 * though this is NOT used on server side for image generation
				 */
				var contrast = contrastStore.getById(channelNo+"");
				var min = '';
				var max = '';
				var gamma = '';
				if(contrast!=null)
				{
					min = contrast.get('min');
					max = contrast.get('max');
					gamma = contrast.get('gamma');
				}
				
				var details = [min, max, gamma, channelStore.getAt(channelNo).get('lut')];
				channelDetails[channelNo]= details;
			}
		}
		
		var channelNumbersURI = encodeURIComponent(Ext.encode(channelNumbers));
		var channelDetailsURI = encodeURIComponent(Ext.encode(channelDetails));

		var imageURL = '../project/getImage?recordid='+recordid+'&sliceNumber='+sliceCount+'&frameNumber='+frameCount+'&channelNumbers='+channelNumbersURI+'&siteNumber='+siteNumber;
		imageURL += '&isGreyScale=' + isGreyScale + '&isZStacked=' + isZStacked + '&isMosaic=' + isMosaic;
		imageURL += '&channelDetails='+channelDetailsURI;
		if(isFullRes === false) {
			imageURL += '&height=512';
		}

		return imageURL;
	},

	/**
	* Set the current image as the thumbnail for the current record
	*/
	onThumbnailButtonClick: function() {
		var recordid = this.getCurrentRecordId();;
		var sliceCount = this.getSliceSlider().getValue();
		var frameCount = this.getFrameSlider().getValue();

		var isGreyScale = this.getCheckboxGreyScale().getValue(); 
		var isZStacked = this.getCheckboxZProjection().getValue(); 
		var isMosaic = this.getCheckboxChannels().getValue(); 
		var isFullRes = this.getCheckboxFullResolution().getValue();

		var siteNumber = this.getSiteControl().items.items[0].getValue().site; //TODO make this elegant
		var channelNumbers = [];
		var checkBoxes = this.getChannels().getSelectionModel().getSelection();

		var i;
		for (i = 0; i < checkBoxes.length; ++i) {
			if (checkBoxes[i].data) {
				var channelNo = checkBoxes[i].data.channelNumber;
				channelNumbers.push(channelNo);
			}
		}
		var store = this.getRecordThumbnailsStore();
		var me = this;
		Ext.Ajax.request({
			method: 'POST',
			url: '../record/setThumbnail',
			params: {
				recordid: recordid,
				sliceNumber: sliceCount,
				frameNumber: frameCount,
				siteNumber: siteNumber,
				isGreyScale: isGreyScale,
				isZStacked: isZStacked,
				isMosaic: isMosaic,
				channelNumbers: Ext.encode(channelNumbers)
			},
			success: function(result, request) {
				var record = store.findRecord('id',recordid);

				// set the image source with a random attribute to force browser to reload the image
				// NOTE - record.set() function not used, because 'imagesource' field has a convert function.
				//   The convert function will override the parameter passed and will not set the random parameter.
				//   SEE - controller/Thumbnails.js:setRecords() where the imagesource field is created. 
				record.data['imagesource'] =  '../project/getThumbnail?recordid=' + recordid + '&t=' + Math.random();

				// reload the record
				var i = store.indexOf(record);
				me.getThumbnails().refreshNode(i);

				// node reload puts its css back to original size of height: 50px, width: 50px
				//  To adjust for that resize thumbnails
				var cnt = me.getController('Thumbnails');
				cnt.adjustThumbnailSize();
				
				Ext.Msg.alert("Status", "Current image is successfully set as thumbnail on current record");
			},
			failure: function(result, request) {
				showErrorMessage(result.responseText, "Failed to save image as thumbnail");
			}
		});

	},

	/**
	 * set the screenshot url
	 * @param {String} url
	 */
	setScreenShotUrl: function() {
		var btn = this.getScreenshotButton();
		var url = this.getImageURL();
		url += '&mode=download';
		btn.setHref(url);
	},

	/**
	 * Set the appropriate parameters on image change
	 * Set
	 * 	- image
	 * 	- image metadata
	 * 	- screenshot url
	 * 	- zoom parameters
	 */
	onImageChange: function() {
		// order is important 
		//  1. setParams: sets the url of the zoom window
		//  2. setImage: fires zoomWindowChanged, whose handler uses the url set in setParams 
		this.getZoom().setParams(this.getImageURL(), this.record);
		this.setImage();

		this.setScreenShotUrl();
		this.setImageMetaData();
	},
	
	/**
	 * update the zoom thumbnail only when image has loaded
	 */
	onImageLoad: function() {
		var rand = '&t=' + Math.random();
		this.getZoom().setParams(this.getImageURL()+rand, this.record);
		this.getZoom().setImage();
	},

	
	/*
	 *--------------------------------------------------------------------------------
	 * utility functions
	 *
	 *--------------------------------------------------------------------------------
	 */

	/**
	 * gets the current selected record id
	 */
	getCurrentRecordId: function() {
		return this.record['Record ID'];
	},
	
	/**
	 * gets the height of current record
	 */
	getRecordHeight: function() {
		return this.record['Image Height'];
	},
	
	/**gets the width of current record
	 * 
	 */
	getRecordWidth: function() {
		return this.record['Image Width'];
	}
});
		
