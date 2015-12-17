/**
 * Actions related to movie playing
 */

Ext.require(['Manage.view.VideoSubmitDialog']);

Ext.define('Manage.controller.MovieController', {
    extend: 'Ext.app.Controller',

    refs :[{
        ref: 'sliceSlider',
        selector: 'imagesliders #sliceslider'
    },{
		ref : 'imagePanel',
		selector : 'imagePanel'
	},{
		ref : 'imageCanvas',
		selector : 'imagePanel #imageCanvas'
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
    }, {
        ref: 'thumbnails',
        selector: 'thumbnails'
    }, {
        ref: 'summaryTable',
        selector: 'summarytable'
    }, {
        ref: 'sliceField',
        selector: 'imagesliders #slicefield'
    }, {
        ref : 'frameField',
        selector : 'imagesliders #framefield'
    }, {
        ref : 'sliceSpeedField',
        selector : 'imagesliders #button_movie1_speed'
    }, {
        ref : 'frameSpeedField',
        selector : 'imagesliders #button_movie2_speed'
    }, {
        ref : 'greyScale',
        selector : 'imagetoolbar #button_colorimage'
    }, {
        ref : 'ZStack',
        selector : 'imagetoolbar #button_zproject'
    }, {
        ref : 'sliceForward',
        selector : 'imagesliders #button_forward_1'
    }, {
        ref : 'slicePrevious',
        selector : 'imagesliders #button_previous_1'
    }, {
        ref : 'frameForward',
        selector : 'imagesliders #button_forward_2'
    }, {
        ref : 'framePrevious',
        selector : 'imagesliders #button_previous_2'
    }, {
        ref : 'imageControls',
        selector : 'imagecontrols'
    }, {
        ref : 'sliceMovie',
        selector : 'imagesliders #button_movie1'
    }, {
        ref : 'frameMovie',
        selector : 'imagesliders #button_movie2'
    }, {
        ref : 'sliceVideoSubmit',
        selector : 'imagesliders #slice_video_submit'
    }, {
        ref : 'frameVideoSubmit',
        selector : 'imagesliders #frame_video_submit'
    },{
        ref : 'frameRate',
        selector : 'videoSubmitDialog numberfield'
    }],
    
    init: function() {
        this.control({
            'imagesliders':{
                playFrameMovie : this.onStartFrameMovie,
                playSliceMovie : this.onStartSliceMovie,
                submitFrameVideo : this.onSubmitFrameVideo,
                submitSliceVideo : this.onSubmitSliceVideo,
                stopMovie : this.onStopMovie,
                chooseSpeed : this.chooseSpeed
              },
              "videoSubmitDialog radiogroup" : {    
              	change: this.onFrameRateOptionChange
              },
             'imagePanel' :{
            	'imageLoaded':this.continueMovie
              	
              }
        });
        this.application.on({
            imageLoaded  : {
        		fn:this.continueMovie,
        		scope : this	
        	},
        	recordSelectionChanged: {
        		fn:this.onRecordSelectionChange,
        		scope : this
        	}            
        });
        this.delay = 1000;
    },

    /**
     * Get the current selected record from the thumbnail selection model
     */
    getActiveRecordID : function() {
        var lastSelected = this.getThumbnails().getSelectionModel().getLastSelected();
        if (lastSelected === null)
            return null;
        return lastSelected.data.id;
    },

    // Play movie on all frames on the record 
    onStartFrameMovie:function(){
    	this.onFrame =true;
    	this.startMovie();
    },

    // Play movie on all slices on the record
    onStartSliceMovie:function(){
    	this.onFrame =false;
    	this.startMovie();
    },
    
    // submit video for all frames on the record
    onSubmitFrameVideo:function(){
    	this.onFrame = true;
    	this.submitMovieVideo(this.onFrame);
    },
    
    // submit video for all slices on the record
    onSubmitSliceVideo:function(){
    	this.onFrame = false;
    	this.submitMovieVideo(this.onFrame);
    },
    
    onFrameRateOptionChange: function(field,newValue,oldValue){
    	if(newValue.frameRate === 'elapsedTime'){
    		this.getFrameRate().disable();
    	}
    	else if(newValue.frameRate === 'frameRate'){
    		this.getFrameRate().enable();
    	}
    },
    
    submitMovieVideo:function(onFrame){
    	var page = this;
		this.recordid = this.getActiveRecordID();
		var currentFrame = this.getFrameSlider().getValue() + '';
		var currentSlice = this.getSliceSlider().getValue();
		this.currentSite = this.getSiteControl().items.items[0].getValue().site + '';
		
		var sliceCount = this.getSliceSlider().getValue();
        var frameCount = this.getFrameSlider().getValue();
        var maxSlices =  this.getSliceSlider().maxValue + 1;
        var maxFrames = this.getFrameSlider().maxValue + 1;
        
        var checkBoxes = this.getChannelControl().getSelectionModel().getSelection();
        var channelNumbers = new Array();
        for (var i=0; i<checkBoxes.length; ++i) {
            if (checkBoxes[i].data)
                channelNumbers.push(checkBoxes[i].data.channelNumber);
        }
        
        var allChannels = new Array();
        for (var i=0; i<checkBoxes.length; ++i) {
                allChannels.push(checkBoxes[i].data.channelNumber);
        }
        
        this.recordChannels = allChannels; 
        
        var useZStack = this.getZStack().pressed;
        var useChannelColor = !(this.getGreyScale().pressed);
        var overlayNames = new Array();
        var checkBoxGrid = Ext.ComponentQuery.query('imagecontrols #overlaycontrol')[0];
        var selected = checkBoxGrid.getSelectionModel().getSelection();
        if (selected !== null && selected.length > 0) {
            for (var j=0; j< selected.length; ++j) {
                overlayNames.push(selected[j].data.name);
            }
        }
        
        var selectedRecord = this.recordid;
        var selectedSite = this.currentSite;
        
        var win = Ext.create ('Ext.window.Window', {
            height : 170,
            title : 'Submit Video',
            width : 430,
            items : [{
                xtype : 'videoSubmitDialog',
                guid : selectedRecord,
                siteNumber  :  selectedSite,
                frameNumber : currentFrame,
                sliceNumber : currentSlice, 
                frame       : page.onFrame,
                useChannelColor: useChannelColor,
                useZStack : useZStack,
                channelNumbers : channelNumbers,
                overlayNames : overlayNames
            }]
        });
        win.show();
    },
    
    /**
     * Play movie. if onFrame is true, play on frames
     * Else play on slices
     */
	startMovie:function(onFrame){
		var page = this;
		this.playMovie = true;
		this.recordid = this.getActiveRecordID();
		var currentFrame = this.getFrameSlider().getValue() + '';
		var currentSlice = this.getSliceSlider().getValue();
		this.currentSite = this.getSiteControl().items.items[0].getValue().site + '';
		
		var sliceCount = this.getSliceSlider().getValue();
        var frameCount = this.getFrameSlider().getValue();
        var maxSlices =  this.getSliceSlider().maxValue + 1;
        var maxFrames = this.getFrameSlider().maxValue + 1;
        
        var checkBoxes = this.getChannelControl().getSelectionModel().getSelection();
        var channelNumbers = new Array();
        for (var i=0; i<checkBoxes.length; ++i) {
            if (checkBoxes[i].data)
                channelNumbers.push(checkBoxes[i].data.channelNumber);
        }
        
        var allChannels = new Array();
        for (var i=0; i<checkBoxes.length; ++i) {
                allChannels.push(checkBoxes[i].data.channelNumber);
        }
        
        this.recordChannels = allChannels; 
        
        var useZStack = this.getZStack().pressed;
        var useChannelColor = !(this.getGreyScale().pressed);
        var overlayNames = new Array();
        var checkBoxGrid = Ext.ComponentQuery.query('imagecontrols #overlaycontrol')[0];
        var selected = checkBoxGrid.getSelectionModel().getSelection();
        if (selected !== null && selected.length > 0) {
            for (var j=0; j< selected.length; ++j) {
                overlayNames.push(selected[j].data.name);
            }
        }
        
        var selectedRecord = this.recordid;
        var selectedSite = this.currentSite;
        
        Ext.Ajax.request({
            method : 'GET',
            url : '../movie/start',
            params : {
                guid : selectedRecord,
                siteNumber  :  selectedSite,
                frameNumber : currentFrame,
                sliceNumber : currentSlice, 
                frame       : page.onFrame,
                useChannelColor: useChannelColor,
                channelNumbers : Ext.encode(channelNumbers),
                overlayNames : Ext.encode(overlayNames)
            },
            success : function(result, request) {
            	if(page.playMovie){
                    var response = Ext.decode(result.responseText);
                    var movieID = response["movieID"];
                    page.movieID = movieID;
            		page.runMovie();
            	}
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to start Movie");
            }
        });
	},
    
	runMovie:function(){
		var page =this;
		if(page.playMovie){
			this.getImagePanel().addListener('load', function(){console.log("load complete");});
            page.showSpeedControls();
            page.lockAll();
			page.showNextImage();
		}

	},

    continueMovie : function() {
		if (!this.playMovie)
            return;
        var page = this;
        setTimeout(function() {
            page.showNextImage();
        }, page.delay);
    }, 

    showNextImage : function() {
        var page =this, max, current;
		if(page.playMovie){
            var frameSlider = this.getFrameSlider();
            var sliceSlider = this.getSliceSlider();

			var recordid = this.getActiveRecordID();
			var record = this.getRecordItem(recordid).data;
			if(page.onFrame){
				current = frameSlider.getValue();
                max = frameSlider.maxValue + 1;
			}
			else{
				current = sliceSlider.getValue();
                max = sliceSlider.maxValue + 1; 
			}
            var next;
			if(current+1 < max)
                next = current + 1;
            else
                next = 0;
            
            if(page.onFrame){
                this.getFrameSlider().setValue(next);
                this.getFrameField().setText("T: " + (next+1) + "/" + max);
            }
            else{
                this.getSliceSlider().setValue(next);
                this.getSliceField().setText("Z: " + (next+1) + "/" + max);
            }
            
            var imageURL = '../movie/getNextImage?ithImage='+next+'&movieID=' + page.movieID;
            console.log(imageURL);
            
            var sliceValue = this.getSliceSlider().getValue();
            var frameValue = this.getFrameSlider().getValue();
            var siteValue = this.currentSite;
            var channelValue = this.recordChannels;
            
            var callback=function(){
            	page.getController('Manage.controller.ImageViewController').setImage(imageURL,record);
            	
            	page.getController('Manage.controller.RecordSelection').setImageMetaData(recordid, sliceValue, frameValue, siteValue, channelValue);
            };
            this.waitForNextImage(page.movieID,next,callback,0);
            
		}

    },
    
    resetMovie: function(){
    	if(this.getFrameMovie().getState())
			this.getFrameMovie().toggle(false,false);
		if(this.getSliceMovie().getState())
			this.getSliceMovie().toggle(false,false);
    },
    
    waitForNextImage:function(movieID,imageIndex,callback,callCount){
    	var page = this;
    	if(callCount > 50){
    		showErrorMessage(null, "Movie creation under progress. \nPlease try again after some time.");
    		console.log(this.getSliceMovie().getState());
    		page.resetMovie();
    	}
    	else{
    		Ext.Ajax.request({
                method : 'GET',
                url : '../movie/isImageExists',
                params : {
            		movieID : movieID,
            		ithImage  :  imageIndex
                },
                success : function(result, request) {
                	var response=Ext.JSON.decode(result.responseText);
                	if(page.playMovie ==false){
                		return;
                	}
                	if(response.exists){
                		callback();
                	}
                	else{
                        setTimeout(function() {
                            page.waitForNextImage(movieID,imageIndex,callback,callCount+1);
                        }, 200);//200ms
                	}
                },
                failure : function(result, request) {
                    showErrorMessage(result.responseText, "Movie creation failed");
                    page.resetMovie();
                }
            });
    	}
    	
    },
	
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
    
    onRecordSelectionChange:function(){    	
    	if(this.playMovie){
        	if (this.onFrame) {         
                this.getFrameMovie().toggle();
            }
            else {            
                this.getSliceMovie().toggle();
            }
    	}
    	
    },
    
    onStopMovie:function(){
    	this.playMovie = false;
        this.hideSpeedControls();
        this.releaseAll();
        this.getController('Manage.controller.RecordSelection').setImage(this.getActiveRecordID());
    },

    /**
     * Set speed based on user preference
     */
    chooseSpeed : function(delay) {
        this.delay = delay;
    },
    
    // Show the speed controls
    showSpeedControls : function() {
        this.getSliceForward().hide();
        this.getSlicePrevious().hide();
        this.getFrameForward().hide();
        this.getFramePrevious().hide();
        this.getFrameVideoSubmit().hide();
        this.getSliceVideoSubmit().hide();
        if (this.onFrame) {
            this.getFrameSpeedField().show();
            this.getSliceMovie().hide();
        }
        else {
            this.getSliceSpeedField().show();
            this.getFrameMovie().hide();
        }
    },

    // Hide the speed controls
    hideSpeedControls : function() {
        this.getSliceForward().show();
        this.getSlicePrevious().show();
        this.getFrameForward().show();
        this.getFramePrevious().show();
        this.getFrameMovie().show();
        this.getSliceMovie().show();
        this.getFrameVideoSubmit().show();
        this.getSliceVideoSubmit().show();
        this.getSliceSpeedField().hide();
        this.getFrameSpeedField().hide();
    },

    // Lock all other ui elements when movie is playing
    lockAll : function () {
        if (this.onFrame)
            this.getSliceSlider().setDisabled(true);
        else
            this.getFrameSlider().setDisabled(true);
        this.getImageControls().setDisabled(true);
        this.getChannelControl().setDisabled(true);
        this.getSiteControl().setDisabled(true);
        this.getOverlayControl().setDisabled(true);
        this.getSummaryTable().setDisabled(true);
        this.getThumbnails().setDisabled(true);
    },

    // Release all locked ui elements once movie is done
    releaseAll : function() {
        this.getSliceSlider().setDisabled(false);
        this.getFrameSlider().setDisabled(false);
        this.getImageControls().setDisabled(false);
        this.getChannelControl().setDisabled(false);
        this.getSiteControl().setDisabled(false);
        this.getOverlayControl().setDisabled(false);
        this.getSummaryTable().setDisabled(false);
        this.getThumbnails().setDisabled(false);
    }
});
