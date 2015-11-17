/**
 * All the controls needed to play movie
 */
Ext.define('Manage.controller.Movie', {
	extend: 'Ext.app.Controller',


	refs: [{
		ref: 'imagesliders',
		selector: 'imagesliders'
	}, {
		ref: 'siteControl',
		selector: '#sitecontrol'
	}, {
		ref:'frameSlider',
		selector:'imagesliders #frameslider'
	}, {
		ref: 'sliceSlider',
		selector: 'imagesliders #sliceslider'
	}, { 
		ref: 'channels',
		selector: 'channels'
	}, {
		ref: 'checkboxZStack',
		selector: '#checkboxZProjection'
	},{
		ref: 'checkboxGreyScale',
		selector: '#checkboxGreyScale'
	}, {
		ref: 'overlays',
		selector: 'overlays #checkboxgrid'
	}, {
		ref: 'sliceField',
		selector: 'imagesliders #slicefield'
	}, {
		ref : 'frameField',
		selector : 'imagesliders #framefield'
	}, {
		ref: 'imagepanel',
		selector: 'imagepanel'
	}, {
		ref: 'checkboxFitWidth',
		selector: '#checkboxFitWidth'
	}, {
		ref: 'headers',
		selector: 'headers'
	}, {
		ref: 'imagecontrols',
		selector: 'imagecontrols'
	}, {
		ref: 'westPanel',
		selector: 'viewport > panel[region=west]'
	}, {
		ref : 'slicePanel',
		selector : 'imagesliders #slicepanel'
	}, {
		ref : 'framePanel',
		selector : 'imagesliders #framepanel'
	}, { 
		ref: 'sliceBackward',
		selector: 'imagesliders #button_previous_1'
	}, {
		ref: 'sliceForward',
		selector: 'imagesliders #button_forward_1'
	}, {
		ref: 'frameBackward',
		selector: 'imagesliders #button_previous_2',
	}, {
		ref: 'frameForward',
		selector: 'imagesliders #button_forward_2'
	},{
        ref : 'sliceVideoSubmit',
        selector : 'imagesliders #slice_video_submit'
    }, {
        ref : 'frameVideoSubmit',
        selector : 'imagesliders #frame_video_submit'
    }, {
        ref : 'frameRate',
        selector : 'videoSubmitDialog numberfield'
    }],

	controllers: [ //creates getter
		'RecordController',
		'ImageView'
	],

	init: function() {
		this.control({
			'imagesliders': {
				playFrameMovie: this.startFrameMovie,
				playSliceMovie: this.startSliceMovie,
				submitFrameVideo : this.onSubmitFrameVideo,
                submitSliceVideo : this.onSubmitSliceVideo,
				stopMovie: this.stopMovie,
				chooseSpeed: this.chooseSpeed //TODO implement this
			},
            "videoSubmitDialog radiogroup" : {    
            	change: this.onFrameRateOptionChange
            },
			'imagepanel': {
				imageLoaded: this.continueMovie 
			}
		});
	},

	/**
	 * @property {Boolean} 
	 * play frame/slice movie
	 * @private
	 */
	onFrame: '',

	/**
	 * @property {Number} [delay=1000ms]
	 * delay in between frames in movie
	 * @private
	 */
	delay: 10,

	/**
	 * @property {Array}
	 * Channels of the record
	 * @private
	 */
	recordChannels: '',

	/**
	 * @property 
	 * Current frame
	 * @private
	 */
	currentFrame: '',

	/**
	 * @property
	 * Current slice
	 * @private
	 */
	currentSlice: '',

	/**
	 * @property {String}
	 * Current site
	 * @private
	 */
	currentSite: '',

	/**
	 * @property {Boolean}
	 * State of movie
	 * @private
	 */
	playMovie: '',

	/**
	 * @property {String} TODO string/number
	 * Unique id of the movie to communicate with the server
	 * @private
	 */
	movieId: '',

	/**
	 * Plays the frame movie
	 * Handles the {@link Manage.view.ImageSliders#event-playFrameMovie} event
	 */
	startFrameMovie: function() {
		this.onFrame = true;
		this.startMovie();
	},

	/**
	 * Plays the slice movie
	 * Handles the {@link Manage.view.ImageSliders#event-playSliceMovie} event
	 */
	startSliceMovie: function() {
		this.onFrame = false;
		this.startMovie();
	},

	/**
	 * Stop playing the movie
	 */
	stopMovie:function() {
		this.playMovie = false;
		this.getImagepanel().setMoviePlaying(this.playMovie);
		this.toggleUi(false);

		// useful in case image was zoomed before playing the movie
		// now this ensures that the zoomed is displayed
		this.getRecordControllerController().setImage();
	},

	/**
	 * Set up the server request and on success play movie
	 * @private
	 */
	startMovie: function() {
		//set the global properties
		this.playMovie = true;
		this.getImagepanel().setMoviePlaying(this.playMovie);
		this.currentSite = this.getSiteControl().items.items[0].getValue().site;
		this.currentFrame = this.getFrameSlider().getValue();
		this.currentSlice = this.getSliceSlider().getValue();
		
		//set the global property - record channels
		this.recordChannels = [];
		var checkBoxes = this.getChannels().getSelectionModel().getSelection();
		var i;
		for(i = 0; i < checkBoxes.length; i++) {
			if(checkBoxes[i].data) {
				this.recordChannels.push(checkBoxes[i].data.channelNumber);
			}
		}

		//set local variables
		var isUserColor = !(this.getCheckboxGreyScale().getValue());
		var overlayNames = [];
		var selected = this.getOverlays().getSelectionModel().getSelection();
		if(selected !== null && selected.length > 0) {
			for(i = 0; i < selected.length; i++) {
				overlayNames.push(selected[i].data.name);
			}
		}

		var me = this;
		Ext.Ajax.request({
			method: 'GET',
			url: '../movie/start',
			params: {
				guid: me.getActiveRecordId(),
				siteNumber:  me.currentSite,
				frameNumber: me.currentFrame,
				sliceNumber: me.currentSlice, 
				frame: me.onFrame,
				useChannelColor:isUserColor,
				channelNumbers: Ext.encode(me.recordChannels),
				overlayNames: Ext.encode(overlayNames)
			},
			success: function(result, request) {
				if(me.playMovie) {
					var response = Ext.decode(result.responseText);
					me.movieId = response['movieID'];
					me.runMovie();
				}
			},
			failure: function(result, request) {
				showErrorMessage(result.responseText, 'Failed to start movie');
			}
		});
	},
	
	/**
	 * Run the movie
	 * TODO maybe this function is unneccessary, can be expaned at call point???
	 * @private
	 */
	runMovie: function() {
		if(this.playMovie) {
			this.toggleUi(true);
			this.prefetchImages();
			this.showNextImage();
		}
	},
	
	/**
	 * prefetch next k images for the movie
	 */
	prefetchImages: function() {
		var current = 0;
		
		var frameSlider = this.getFrameSlider();
		var sliceSlider = this.getSliceSlider();

		if(this.onFrame) {
			current = frameSlider.getValue();
			max = frameSlider.maxValue + 1;
		} else {
			current = sliceSlider.getValue();
			max = sliceSlider.maxValue + 1;
		}
		
		if(current + 10 < max)
			max = 10;
		
		for(var i=0;i<max;i++)
		{
			this.prefetchNextImage(current+i);
		}
	},
	
	prefetchNextImage: function(next) {
		if(this.playMovie !== true) {
			return;
		}

		var max = 0; //max movie slider value
		var current = 0; //current movie slider value

		var frameSlider = this.getFrameSlider();
		var sliceSlider = this.getSliceSlider();

		current = next;
		if(this.onFrame) {
			max = frameSlider.maxValue + 1;
		} else {
			max = sliceSlider.maxValue + 1;
		}

		if(current + 1 < max) {
			next = current + 1;
		} else {
			next = 0;
		}

		var url = '../movie/getNextImage?ithImage=' + next + '&movieID=' + this.movieId;

		var me = this;
		var callback = function() {
			$.get(url);// make dummy call to subsequent images
		};
		this.waitForNextImage(this.movieId, next, callback, 0, false);
	},

	/**
	 * Continue the movie
	 */
	continueMovie: function() {
		if(this.playMovie !== true) {
			return;
		}
		var me = this;
		setTimeout(
			function() {
				me.prefetchImages();
				me.showNextImage();
			},
			me.delay
		);
	},

	/**
	 * show the next image in the movie
	 * @private
	 */
	showNextImage: function() {
		if(this.playMovie !== true) {
			return;
		}

		var max = 0; //max movie slider value
		var current = 0; //current movie slider value
		var next = 0; //next slider value, on reaching max circles back to zero

		var frameSlider = this.getFrameSlider();
		var sliceSlider = this.getSliceSlider();

		if(this.onFrame) {
			current = frameSlider.getValue();
			max = frameSlider.maxValue + 1;
		} else {
			current = sliceSlider.getValue();
			max = sliceSlider.maxValue + 1;
		}

		if(current + 1 < max) {
			next = current + 1;
		} else {
			next = 0;
		}

		if(this.onFrame) {
			frameSlider.setValue(next);
			this.getFrameField().setText("T: " + (next + 1)  + "/" + max);
		} else {
			sliceSlider.setValue(next);
			this.getSliceField().setText("Z: " + (next + 1) + "/" + max);
		}

		var url = '../movie/getNextImage?ithImage=' + next + '&movieID=' + this.movieId;

		var me = this;
		var callback = function() {
			var ivc = me.getImageViewController();
			ivc.setViewingMode('fullsize');

			var record = me.getRecordControllerController().record; //TODO record is private
			var imageWidth = record['Image Width'];
			var imageHeight = record['Image Height'];
			var recordid = record['Record ID'];

			var fitWidth = me.getCheckboxFitWidth().getValue();
			if(fitWidth) {
				me.getImageViewController().setFitWidthParameters(imageWidth, imageHeight);
			} else {
				me.getImageViewController().setFullSizeParameters(imageWidth, imageHeight);
			}

			me.getImagepanel().setActiveWidth(imageWidth);
			me.getImagepanel().setActiveHeight(imageHeight);
			me.getImagepanel().setImage(url, recordid, imageWidth, imageHeight);

			var rc = me.getRecordControllerController();
			rc.setImageMetaData(recordid,
					      me.getSliceSlider().getValue(),
					      me.getFrameSlider().getValue(),
					      me.currentSite,
					      me.recordChannels);							
		};
		this.waitForNextImage(this.movieId, next, callback, 0, true);

	},
	

	/**
	 * Wait and load next image for the movie
	 * @private
	 */
	waitForNextImage: function(movieId, imageIndex, callback, callCount, showMsg) {
		var me = this;
		if(callCount > 50 && showMsg){
			showErrorMessage(null, "Movie creation under progress. \nPlease try again after some time.");
			console.log(this.getSliceMovie().getState());
			this.resetMovie();
		}
		else{
			Ext.Ajax.request({
				method : 'GET',
				url : '../movie/isImageExists',
				params : {
					movieID : movieId,
					ithImage  :  imageIndex
				},
				success : function(result, request) {
					var response = Ext.JSON.decode(result.responseText);
					if(me.playMovie === false){
						return;
					}
					if(response.exists){
						callback();
					} else {
						setTimeout(
							function() { 
								me.waitForNextImage(movieId,imageIndex,callback,callCount+1, showMsg); 
							}, 
							200 //200ms
						);
					}
				},
				failure : function(result, request) {
					showErrorMessage(result.responseText, "Movie creation failed");
					me.resetMovie();
				}
			});
		}
    	
	},

	/**
	 * enable/disable all other ui elements when movie is playing
	 * @param {Boolean} disabled true to disable all ui elements
	 * @private
	 */
	toggleUi: function(disabled) {
		this.getHeaders().setDisabled(disabled);
		this.getImagecontrols().setDisabled(disabled);
		this.getWestPanel().setDisabled(disabled);

		if(this.onFrame) {
			this.getSlicePanel().setDisabled(disabled);
			if(disabled) {
				this.getFrameForward().hide();
				this.getFrameBackward().hide();
				this.getFrameVideoSubmit().hide();
		        this.getSliceVideoSubmit().hide();
			} else {
				this.getFrameForward().show();
				this.getFrameBackward().show();
				this.getFrameVideoSubmit().show();
		        this.getSliceVideoSubmit().show();
			}
		} else {
			this.getFramePanel().setDisabled(disabled);
			if(disabled) {
				this.getSliceForward().hide();
				this.getSliceBackward().hide();
				this.getFrameVideoSubmit().hide();
		        this.getSliceVideoSubmit().hide();
			} else {
				this.getSliceForward().show();
				this.getSliceBackward().show();
				this.getFrameVideoSubmit().show();
		        this.getSliceVideoSubmit().show();
			}
		}
	},

	/**
	 * Returns the current active record id
	 * @private
	 */
	getActiveRecordId: function() {
		return this.getRecordControllerController().getCurrentRecordId();
	},
	
	
	/////////////////////////////////////////////////////////////
	///////////// METHODS FOR VIDEO MOVIE //////////////////////
	///////////////////////////////////////////////////////////
	
	
	
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
    
    submitMovieVideo:function(onFrame){
    	
    	console.log("submit movie video");
    	
    	var page = this;
		this.recordid = this.getActiveRecordId();
		var currentFrame = this.getFrameSlider().getValue() + '';
		var currentSlice = this.getSliceSlider().getValue();
		this.currentSite = this.getSiteControl().items.items[0].getValue().site + '';
		
		var sliceCount = this.getSliceSlider().getValue();
        var frameCount = this.getFrameSlider().getValue();
        var maxSlices =  this.getSliceSlider().maxValue + 1;
        var maxFrames = this.getFrameSlider().maxValue + 1;
        
        var channelNumbers = [];
		var checkBoxes = this.getChannels().getSelectionModel().getSelection();
		var i;
		for(i = 0; i < checkBoxes.length; i++) {
			if(checkBoxes[i].data) {
				channelNumbers.push(checkBoxes[i].data.channelNumber);
			}
		}
        
        var allChannels = new Array();
        for (var i=0; i<checkBoxes.length; ++i) {
                allChannels.push(checkBoxes[i].data.channelNumber);
        }
        
        this.recordChannels = allChannels; 
        
        var useZStack = this.getCheckboxZStack().getValue();
        var useChannelColor = !(this.getCheckboxGreyScale().getValue());
                
        var overlayNames = [];
		var selected = this.getOverlays().getSelectionModel().getSelection();
		if(selected !== null && selected.length > 0) {
			for(i = 0; i < selected.length; i++) {
				overlayNames.push(selected[i].data.name);
			}
		}
        
		console.log("useZStack "+useZStack);
		
        var selectedRecord = this.recordid;
        var selectedSite = this.currentSite;
        
        var win = Ext.create ('Ext.window.Window', {
            height : 260,
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
        
        var movieName = 'RecordId-'+selectedRecord;
        win.down().getComponent('movieName').setValue(movieName);
        win.show();
    },
    
    onFrameRateOptionChange: function(field,newValue,oldValue)
    {
    	if(newValue.frameRate === 'elapsedTime'){
    		this.getFrameRate().disable();
    	}
    	else if(newValue.frameRate === 'frameRate'){
    		this.getFrameRate().enable();
    	}
    }
});
