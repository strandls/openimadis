/**
 * This handles only those events where some
 * changes have to be made on the controls
 *	Example: change contrast settings
 */

Ext.define('Manage.controller.ImageView', {
	extend: 'Ext.app.Controller',

	refs: [{
		ref: 'imagepanel',
		selector: 'imagepanel'
	},{
		ref: 'legendYLocation',
		selector: 'settings #yLocationRadio'
	},{
		ref: 'legendXLocation',
		selector: 'settings #xLocationRadio'
	}, {
		ref: 'zoom',
		selector: 'zoom'
	}],

	init: function() {
		this.control({
			'imagepanel': {
				'imageLoaded': this.onImageLoad,
				'layoutChange': this.onImageLoad
			}
		});
		showConsoleLog("ImageView", "init", "controller loaded");
	},
	
	///////////////////////////////////////////////////////////
	////////////// STATE VARIABLES ////////////////////////////
	///////////////////////////////////////////////////////////
	viewingMode: 'fullsize', // viewingMode will be either 'zoom', 'fit', 'fullsize'
	fitScale: '',
	clip: '',

	/**
	 * height and width of image requested from server
	 */
	imageWidth: '',
	imageHeight: '',
	recordWidth: '',
	
	/**
	 * actions to be done after the image is loaded
	 */
	onImageLoad: function()
	{

		var zoom = this.getZoom();
		zoom.fireEvent('showScalebar');
		
		showConsoleLog("ImageView", "onImageLoad", "entry");
		if(this.viewingMode === 'zoom')
		{
			showConsoleLog("ImageView", "onImageLoad", "viewingMode="+this.viewingMode);
			
			var fitScale = this.fitScale;
			var clip = this.clip;
			var height = this.imageHeight;
			var width = this.imageWidth;

			console.log('fitScale ', fitScale);
			console.log('clip', clip);
			console.log('requested ', this.requested);
			console.log('width ' + width + ' height ' + height);
			
			//expand image according to fitScale and clip it accordingly
			$('.zoom_image').css('position','absolute');
			$('.zoom_image').css('width', width * fitScale);
			$('.zoom_image').css('height', height * fitScale);
			$('.zoom_image').css('left',''+(-1*clip.left*fitScale)+'px');
			$('.zoom_image').css('top',''+(-1*clip.top*fitScale)+'px');

			var clipRect='rect(' + clip.top*fitScale +'px,' +clip.right*fitScale+'px,' +clip.bottom*fitScale+'px,' +clip.left*fitScale+'px)';
			$('.zoom_image').css('clip',clipRect);
			
			console.log('record width ' + this.recordWidth + ' record height ' + this.recordHeight);
			
			$('.overlay_image').css('position','absolute');
			$('.overlay_image').css('width', this.recordWidth * fitScale);
			$('.overlay_image').css('height', this.recordHeight * fitScale);			
			$('.overlay_image').css('left',''+(-1*(clip.left+this.requested.x1)*fitScale)+'px');
			$('.overlay_image').css('top',''+(-1*(clip.top+this.requested.y1)*fitScale)+'px');
			
			var clipOverlayRect='rect(' + (clip.top+this.requested.y1)*fitScale +'px,' +(clip.right+this.requested.x1)*fitScale+'px,' +(clip.bottom+this.requested.y1)*fitScale+'px,' +(clip.left+this.requested.x1)*fitScale+'px)';
			$('.overlay_image').css('clip',clipOverlayRect);
			
			$('.scalebar_image').css('position','absolute');
			$('.scalebar_image').css('width', this.recordWidth * fitScale);
			$('.scalebar_image').css('height', (clip.bottom - clip.top) * fitScale);
			$('.scalebar_image').css('left',''+'0px');
			$('.scalebar_image').css('bottom',''+(clip.bottom*fitScale - 10)+'px');
			showConsoleLog("ImageView", "onImageLoad", (clip.bottom*fitScale)-(clip.top*fitScale));
		}
		
		else if(this.viewingMode === 'fullsize')
		{
			showConsoleLog("ImageView", "onImageLoad", "viewingMode="+this.viewingMode);
			
			var localWidth = this.imageWidth;
			var localHeight = this.imageHeight;
			
			localWidth = localHeight * this.getMultichannelScalingFactor(localWidth, localHeight);
			
			var width = localWidth;
			var height = localHeight;
			
			$('.zoom_image').css('position','absolute');
			$('.zoom_image').css('left',''+'0px');
			$('.zoom_image').css('top','0px');
			$('.zoom_image').css('width',width+'px');
			$('.zoom_image').css('height',height+'px');
			$('.zoom_image').css('clip','');
			
			$('.overlay_image').css('position','absolute');
			$('.overlay_image').css('left','0px');
			$('.overlay_image').css('top','0px');
			$('.overlay_image').css('width',width+'px');
			$('.overlay_image').css('height',height+'px');
			$('.overlay_image').css('clip','');
			
			$('.scalebar_image').css('position','absolute');
			$('.scalebar_image').css('left','0px');
			$('.scalebar_image').css('top','0px');
			$('.scalebar_image').css('width',width+'px');
			$('.scalebar_image').css('height',height+'px');
			$('.scalebar_image').css('clip','');
		}
		
		else if(this.viewingMode === 'fit')
		{
			showConsoleLog("ImageView", "onImageLoad", "viewingMode="+this.viewingMode);
			
			var panelWidth = this.getImagepanel().getWidth();
			var panelHeight = this.getImagepanel().getHeight();
			
			var localWidth = this.imageWidth;
			var localHeight = this.imageHeight;
			
			localWidth = localHeight * this.getMultichannelScalingFactor(localWidth, localHeight);
			var ratio = localWidth/localHeight;
			var panelRatio = panelWidth/panelHeight;

			var width;
			var height;
			
			if (panelRatio <= ratio) 
			{
				width = panelWidth;
				height = panelWidth/ratio;
			} 
			else 
			{
				width = ratio*panelHeight;
				height = panelHeight;
			}
			
			$('.zoom_image').css('position','absolute');
			$('.zoom_image').css('left',''+'0px');
			$('.zoom_image').css('top','0px');
			$('.zoom_image').css('width',width+'px');
			$('.zoom_image').css('height',height+'px');
			$('.zoom_image').css('clip','');
			
			$('.overlay_image').css('position','absolute');
			$('.overlay_image').css('left','0px');
			$('.overlay_image').css('top','0px');
			$('.overlay_image').css('width',width+'px');
			$('.overlay_image').css('height',height+'px');
			$('.overlay_image').css('clip','');
			
			$('.scalebar_image').css('position','absolute');
			$('.scalebar_image').css('left','0px');
			$('.scalebar_image').css('top','0px');
			$('.scalebar_image').css('width',width+'px');
			$('.scalebar_image').css('height',height+'px');
			$('.scalebar_image').css('clip','');
		}
		
		this.getImagepanel().setActiveWidth(width);
		this.getImagepanel().setActiveHeight(height);
		
		this.addLegends();
		this.addScalebar();
		
		$('#loadingText').remove();
	},
	
	addLegends: function()
	{
		$('#legendText1').remove();
		
		var width = $('#imageCanvas').width();
		var height = $('#imageCanvas').height();
		var panelWidth = this.getImagepanel().getWidth();
		var panelHeight = this.getImagepanel().getHeight();
		console.log("panel width = "+panelWidth);
		console.log("panel height = "+panelHeight);
		
		if(panelWidth < width)
			width = panelWidth;
		if(panelHeight < height)
			height = panelHeight;
		
		console.log("width = "+width);
		console.log("height = "+height);
		
		$('#imageeditor').width(width);
		$('#imageeditor').height(height);
		if(!this.getImagepanel().isLegendsOn())
		{
			return;
		}
		
		var color = 'black';
		var xLocation = this.getLegendXLocation().getValue().lr;
		var yLocation = this.getLegendYLocation().getValue().tb;
		var text = this.getImagepanel().getLegendText();
		
		$('#imageeditor').append('<div id="legendText1" style="background-color:#B0B0B0;color:'+color+';position:absolute;'+xLocation+':0px;'+yLocation+':0px;z-index:3;">'+text+'</div>');
	},
	
	addScalebar: function()
	{
		$('#scalebarText').remove();
		
		var width = $('#imageCanvas').width();
		var height = $('#imageCanvas').height();
		var panelWidth = this.getImagepanel().getWidth();
		var panelHeight = this.getImagepanel().getHeight();
		console.log("panel width = "+panelWidth);
		console.log("panel height = "+panelHeight);
		
		if(panelWidth < width)
			width = panelWidth;
		if(panelHeight < height)
			height = panelHeight;
		
		console.log("width = "+width);
		console.log("height = "+height);
		
		$('#imageeditor').width(width);
		$('#imageeditor').height(height);
		if(!this.getImagepanel().isScalebarOn())
		{
			return;
		}
		
		var color = 'black';
		var xLocation = this.getImagepanel().getScalebarXLocation();
		var yLocation = this.getImagepanel().getScalebarYLocation();
		var text = this.getImagepanel().getScalebarText();
		
		$('#imageeditor').append('<div id="scalebarText" align="center" style="background-color:#B0B0B0;color:'+color+';position:absolute;'+xLocation+':0px;'+yLocation+':0px;z-index:3;background-color : transparent;">  &nbsp&nbsp<img src="images/redBar.jpg"  height="5" width="100"><br/><font size="4" color="red">'+text+'</font></div>');
	},
	
	getMultichannelScalingFactor: function(recordWidth, recordHeight) 
	{
		var channelCount = this.channelCount;
    	if(this.isMosaic)
    	{
    		console.log("multichannel pressed");
    		var sqrt = Math.floor(Math.sqrt(channelCount));
		
			var lowerSqr =  (sqrt * sqrt);
			var heigherSqr = (sqrt+1)*(sqrt+1);
			
			var gridX = 0;
			var gridY = 0;
			
			if ((channelCount - lowerSqr) > (heigherSqr - channelCount)) 
			{
				gridX = sqrt + 1;
				gridY = sqrt + 1;
			} 
			else if ((channelCount - lowerSqr) < (heigherSqr - channelCount))
			{
				gridY = sqrt;
				if(channelCount!=lowerSqr)
					gridX = sqrt + 1;
				else
					gridX = sqrt;
			}
    		return recordWidth/recordHeight * (gridX/gridY);
    	}
    	return recordWidth/recordHeight;
    },
	
	/**
	 * returns effective width of the image based on viewing mode
	 */
	getViewWidth : function()
	{
		if(this.viewingMode === 'zoom')
		{
			return this.getImagepanel().getActiveWidth();
		}
		if(this.viewingMode === 'fit')
		{
			var panelWidth = this.getImagepanel().getWidth();
			var panelHeight = this.getImagepanel().getHeight();
			
			var ratio = this.imageWidth/this.imageHeight;
			var panelRatio = panelWidth/panelHeight;

			var width;
			
			if (panelRatio <= ratio) 
			{
				width = panelWidth;
			} 
			else 
			{
				width = ratio*panelHeight;
			}
			
			return width;
		}
		return this.imageWidth;
	},
	
	/**
	 * returns effective height of the image based on viewing mode
	 */
	getViewHeight : function()
	{
		if(this.viewingMode === 'zoom')
		{
			return this.getImagepanel().getActiveHeight();
		}
		if(this.viewingMode === 'fit')
		{
			var panelWidth = this.getImagepanel().getWidth();
			var panelHeight = this.getImagepanel().getHeight();
			
			var ratio = this.imageWidth/this.imageHeight;
			var panelRatio = panelWidth/panelHeight;

			var height;
			
			if (panelRatio <= ratio) 
			{
				height = panelWidth/ratio;
			} 
			else 
			{
				height = panelHeight;
			}
			return height;
		}
		return this.imageHeight;
	},
	
	/**
	 * set parameters required for zoom, also sets viewing mode to zoom
	 */
	setZoomParameters: function(clip, win, fitScale)
	{
		this.requested = win;
		this.setFitScale(fitScale);
		this.setClip(clip);
		this.setImageSize(win.width, win.height);
		this.setViewingMode('zoom');
	},
	
	/**
	 * set parameters required for fitwidth, also sets viewing mode to fitwidth
	 */
	setFitWidthParameters: function(imageWidth, imageHeight)
	{
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.setViewingMode('fit');
	},
	
	/**
	 * set parameters required for fullsize, also sets viewing mode to fullsize
	 */
	setFullSizeParameters: function(imageWidth, imageHeight)
	{
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.setViewingMode('fullsize');
	},
	
	/**
	 * set the zoom clip 
	 */
	setClip: function(clip)
	{
		this.clip = clip;
	},
	
	/**
	 * set the viewing mode: 'zoom', 'fit', 'fullsize'
	 */
	setViewingMode:function (mode)
	{
		this.viewingMode = mode;
	},
	
	/**
	 * set the fit scale
	 */
	setFitScale: function(scale)
	{
		this.fitScale = scale;
	},

	/**
	 * set the width, height of the image requested from the server
	 */
	setImageSize: function(width, height) {
		this.imageWidth = width;
		this.imageHeight = height;
	},
	
	/**
	 * set the width of the record
	 */
	setRecordWidth: function(width) {
		this.recordWidth = width;
	},
	
	/**
	 * set the height of the record
	 */
	setRecordHeight: function(height) {
		this.recordHeight = height;
	},
	
	/**
	 * sets the number of channels in this record
	 */
	setChannelCount: function(count) {
		this.channelCount = count;
	},
	
	setMosaic: function(isMosaic) {
		this.isMosaic = isMosaic;
	}
});


