/**
 * Canvas to render the image
 */

Ext.define('Manage.view.ImagePanel', {
	extend: 'Ext.panel.Panel',
	xtype: 'imagepanel',
	alias: 'widget.imagepanel',

	border: false,
	height: '100%',
	width: '100%',

	bodyCls: 'image_overlay',
	autoScroll: true,
	
	html : '<div id="imageeditor" style="position:absolute; z-index:1; top:0; left:0;" class="editor"></div>',
	blankImage : 'images/panojs/blank.gif',
	
	controllers: [ 'Movie'],
	
	///////////////////////////////////////////////////////////////////
	// 		DECLARING STATE VARIABLES ////////////////////////////////
	/////////////////////////////////////////////////////////////////
	activeWidth:"",
	activeHeight:"",
	displayLegends:false,
	displayScalebar:false,
	moviePlaying:false,

	items: [{
		xtype: 'image',
		id: "imageCanvas",
		
		cls: 'zoom_image',
		src: this.blankImage,

		autoScroll: true,
		
		flex: 1,
		listeners: {
			'load': {
				element: 'el',
				fn: function() {
					console.log('image loaded <>');
					var imagePanel=Ext.ComponentQuery.query('imagepanel')[0];
					
					imagePanel.imageReaderPolling=null;
					imagePanel.activeImageReaderMap={};
					
					imagePanel.fireEvent("imageLoaded");
				}
			},
			'error': {
				element: 'el',
				fn:function(){
					var imagePanel=Ext.ComponentQuery.query('imagepanel')[0];
					console.log("ImagePanel error imagePanel=");
					console.log(imagePanel);
					var callBackSpec={
						fn: imagePanel.setImage,
						scope:imagePanel,
						args:[imagePanel.imageURL, imagePanel.recordid, imagePanel.imageWidth,imagePanel.imageHeight]
					};
					
					console.log(imagePanel.activeImageReaderMap);
					imagePanel.checkImageReaderAvailability(callBackSpec //callback specification
							, 0 //call count
							, 200 //initial timerInterval 200 ms
							);
					console.log('error in loading image');
				},
				scope:this
			}
		}
		
	},{
		xtype: 'image',
		id: "overlayCanvas",
		src: this.blankImage,
		cls : 'overlay_image',
		flex:1
	},{
		xtype: 'image',
		id: "scalebarCanvas",
		src: this.blankImage,
		cls : 'scalebar_image',
		flex:1
	}],

	listeners: {
		/**
		 * resize the imageCanvas and the overlayCanvas on this event
		 */
		afterLayout: function() {
			$('#loadingText').remove();
			this.fireEvent('layoutChange');
		}
	},
	
	setMoviePlaying: function(moviePlaying)
	{
		this.moviePlaying = moviePlaying;
	},
	
	setImage: function(imageURL, recordid, imageWidth, imageHeight)
	{
		// HACK: reset image
		// so that browser will not consider it as a same request
		// dont do this if movie is playing, because it will flicker
		if(!this.moviePlaying)
		{
			$('#imageCanvas').attr('src', '');
		}
		
		this.imageURL = imageURL;
		this.recordid = recordid;
		
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		
		console.log("ImagePanel setImage url="+imageURL+" imageWidth="+imageWidth+" imageHeight="+imageHeight+" recordid"+recordid);
		
		this.activeImageReaderMap={};
		this.activeImageReaderMap[recordid] = 0;
		
		var text = 'Loading...';
		$('#loadingText').remove();// remove if there is any loading node already present
		$('#imageeditor').append('<div id="loadingText" style="background-color: #C0C0C0;color: black;position:absolute;top: 0px;left: 0px;z-index: 3;">'+text+'</div>');
		
		$('#imageCanvas').attr('src', imageURL);
	},
	
	checkImageReaderAvailability:function(callBackSpec,callCount,timerInterval){
		if(callCount > 50){ //throw error if recursion depth is beyond threshold
			showErrorMessage(null, "Image generation failed.");
		}
		else{
			var page=callBackSpec.scope;
			var requestImageReader = false;
			if(callCount == 0){
				requestImageReader = true;
			}
			page.imageReaderPolling=Ext.Ajax.request({
	            method : 'GET',
	            url : '../project/checkImageReaderAvailability',
	            params : {
	        		recordid : callBackSpec.scope.recordid,
	        		requestImageReader: requestImageReader
	            },
	            success : function(result, request) {
	            	var response=Ext.JSON.decode(result.responseText);
	            	if(page.activeImageReaderMap[page.recordid] == null){
	            		//return silently if abort polling flag is raised
	            		console.log('found abort polling flag raised: stopping image reader polling.');
	            		return;
	            	}
	            	if(response.available){
	            		callBackSpec.fn.apply(callBackSpec.scope,callBackSpec.args);
	            	}
	            	else{
	            		if(callCount ==0){
	            			page.activeImageReaderMap[callBackSpec.scope.recordid] = response.trackerId;
	            		}
	            		var newInterval=timerInterval*2 >5000?5000:timerInterval*2;  //timerInterval increases exponentially limited by 1000ms
	                    setTimeout(function() {
	                        page.checkImageReaderAvailability(callBackSpec,
	                        		callCount+1, //recursion depth
	                        		newInterval
	                        );
	                    }, timerInterval);
	            	}
	            },
	            failure : function(result, request) {
	            	if(result !=null && result.responseText!=null){
	            		//silently ignore the failure if abort is triggerred from javascript
	            		showErrorMessage(result.responseText, "Image generation failed");
	            	}
	                
	            }
	        });
		}
		
	},
	
	/**
	 * clears overlay transperancy, used while going to edit mode inorder to removed server generated transperancy
	 */
	clearOverlayTransparancy:function(){
		this.down('image[id="overlayCanvas"]').setSrc(this.blankImage);
	},
	
	/**
	 * sets overlay transparancy fetched from server to overlayCanvas
	 * @param imageUrl url for fetching overlay image
	 */
	setOverlayTransparancy : function(imageUrl, height)
	{
		showConsoleLog("ImagePanel", "setOverlayTransparancy", imageUrl);

		this.down('image[id="overlayCanvas"]').setSrc("");
		this.down('image[id="overlayCanvas"]').setSrc(imageUrl);
	},
	
	/**
	 * sets scalebar transparancy fetched from server to scalebarCanvas
	 * @param imageUrl url for fetching overlay image
	 */
	setScalebarTransparancy : function(imageUrl, height)
	{
		showConsoleLog("ImagePanel", "setScalebarTransparancy", imageUrl);
		imageUrl += '&height=' + this.getActiveHeight();
		
		this.down('image[id="scalebarCanvas"]').setSrc(imageUrl);
		
	},
	
	//////////////////////////////////////////////////////////////////////
	////////          LEGENDS RELATED FUNCTIONS  ///////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	setLegendsOn: function(value)
	{
		this.displayLegends = value;
	},
	
	isLegendsOn: function()
	{
		return this.displayLegends;
	},
	
	setLegendText : function(text)
	{
		this.legendText = text;
	},
	
	getLegendText : function()
	{
		return this.legendText;
	},
	
	getLegendYLocation : function()
	{
		return 'Top';
	},
	
	getLegendXLocation : function()
	{
		return 'Left';
	},
	
	
	//////////////////////////////////////////////////////////////////////
	////////          SCALEBAR RELATED FUNCTIONS  ///////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	setScalebarOn: function(value)
	{
		this.displayScalebar = value;
	},
	
	isScalebarOn: function()
	{
		return this.displayScalebar;
	},
	
	setScalebarText : function(text)
	{
		this.scalebarText = text;
	},
	
	getScalebarText : function()
	{
		return this.scalebarText;
	},
	
	getScalebarYLocation : function()
	{
		return 'Top';
	},
	
	getScalebarXLocation : function()
	{
		return 'Left';
	},
	
	//////////////////////////////////////////////////////////////////////
	////////          SKETCHPAD RELATED FUNCTIONS  /////////////////////// 
	//////////////////////////////////////////////////////////////////////
	
	initSketchPad:function(){
		
		// Image overlay
	    /**
	     * Defining some constants
	     */
	    WIDTH = this.getActiveWidth();
	    HEIGHT = this.getActiveHeight();
	    pen_color = "#FF0000";
	    pen_width = 3.0;
	    pen_opacity = 1.0;

	    /**
	     * Creating the sketchpad
	     */
	    sketchpad = Raphael.sketchpad('imageeditor', {
	        width: WIDTH,
	        height: HEIGHT,
	        editing: true
	    });
	    
	    sketchpad.pen("pencil");
	    sketchpad.pen().color(pen_color).width(pen_width).opacity(pen_opacity);
	    sketchpad.mode(false);
	    this.sketchpad=sketchpad;
	    
	    sketchpad.getViewCoordinates = function(x,y){
	    	var paper = sketchpad.paper();
	    	var xscale = paper._vbSize;//paper.width/(paper.w * 1.0); 
	    	var yscale = paper._vbSize;//paper.height/(paper.h * 1.0);
	    	
	    	var xOffset=paper.canvas.viewBox.baseVal.x;
	    	var yOffset=paper.canvas.viewBox.baseVal.y;
	    	
	    	return {
	    		'x': x*xscale + xOffset,
	    		'y': y*yscale + yOffset
	    	}
	    };
	    //var c = this.sketchpad.paper().image("js/ov2.svg", 0, 0,WIDTH , HEIGHT);
	},
	
	/**
	 * set sketchpad properties
	 */
	setViewBox: function(x,y,width,height){
		this.sketchpad.paper().setViewBox(x,y,width,height,false);
	},
	
	/**
	 * 
	 */
	setSketchpadSize: function(width, height) {
		this.sketchpad.setSize(width, height);
	},
	
	setSketchpadViewBox: function(x,y,width,height) {
		this.sketchpad.setViewBox(x, y, width, height);
	},
	
	//////////////////////////////////////////////////////////////////////
	///          GETTER-SETTER BLOCK FOR STATE VARIABLES ///////////////// 
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * set the state variable height used for imageclipping etc
	 */
	setActiveHeight: function(height){
		this.activeHeight = height;
	},
	
	/**
	 * returns the value of state variable active height
	 */
	getActiveHeight: function(){
		return this.activeHeight;
	},
	
	/**
	 * returns the value of state variable active width
	 */
	getActiveWidth: function(){
		return this.activeWidth;
	},
	
	/**
	 * set the state variable width used for imageclipping etc
	 */
	setActiveWidth: function(width){
		this.activeWidth = width;
	},	
	
	
	//////////////////////////////////////////////////////////////////////
	////////          KINETIC RELATED FUNCTIONS  /////////////////////// 
	//////////////////////////////////////////////////////////////////////
	canvas:"",
	
	initCanvas: function(){
		$('#imageeditor').append('<div id="container"></div>');
	},
	
	setCanvas: function(width,height){
		initStage(width,height);
	},
	
	removeCanvas: function(){
		removeStage();
		$('#container').remove();
	},
	
});
