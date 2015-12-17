/**
 * Canvas to render Image
 * */

Ext.define('Manage.view.imageview.ImagePanel', {
	extend:'Ext.panel.Panel',
	alias:'widget.imagePanel',
	
	initComponent:function() {
		var me=this;
		this.blankImage='images/panojs/blank.gif';
		this.tileSize=256;
		this.tileSizePower=Math.log(this.tileSize)/Math.LN2;
		this.activeImageReaderMap={};
		this.legendLocation = 'top';
		this.displayLegends = false;
		
		var config={
			height:'100%',
			width:'100%',
			bodyCls:'image_overlay',
			border:false,	
			html : '<div id="imageeditor" style="position:absolute;z-index:1;top:0;left:0;" class="editor"></div>',
			items:[
				{
					xtype: 'image',
					id:"imageCanvas",
					src: me.blankImage,
					cls : 'zoom_image',
					flex:1,
					listeners:{
						'load':{
							element : 'el',
							fn:function(){
//									$("<img/>").attr("src", this.getAttribute("src")).load(function() {})
//									.each( function() {
//			                        if(this.complete) 
//			                            $(this).trigger("load");
//			                    	});
								me.adjustClipping();
								$('#loadingText').remove();
								
								me.imageReaderPolling=null;
								me.activeImageReaderMap={};
			                    me.fireEvent("imageLoaded");
			                    me.addLegends();
							},
							scope:this
						},
						'error':{
							//this means etag is not matching and imagereader initialization has been requested.
							//client should poll for initialization completion and then make getImage call
							element : 'el',
							fn:function(){
								var imagePanel=this;
								var callBackSpec={
									fn: imagePanel.setImage,
									scope:imagePanel,
									args:[imagePanel.imageConfig, imagePanel.record, imagePanel.isFullRes,this.imageCoordinates]
								};
								console.log(imagePanel.activeImageReaderMap);
								imagePanel.checkImageReaderAvailability(callBackSpec //callback specification
										, 0 //call count
										, 200 //initial timerInterval 200 ms
										);
								console.log('error in loading image');
								me.addLegends();
							},
							scope:this
						},
						'mousedown':{
							element:'el',
							fn:function(e){
								e.preventDefault();
								console.log('dragstart img');
							},
							scope:this
						}
					}
				},
				{
					xtype: 'image',
					id:"overlayCanvas",
					src: me.blankImage,
					cls : 'overlay_image',
					flex:1
				}
			],
		
		listeners:{
			'afterrender':{
				fn: function(component, opts){
				},
				scope:this
			},
			
			'afterlayout':{
				fn: function(component, opts){
					this.adjustResizing();
				},
				scope:this
			}
		}
	};
	Ext.apply(this, Ext.apply(this.initialConfig, config));
	this.callParent();
},

initSketchPad:function(){
		
	// Image overlay
    /**
     * Defining some constants
     */
    WIDTH = this.scaledSize.width;
    HEIGHT = this.scaledSize.height;
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

setImage:function(imageConfig, record,isFullRes, imageCoordinates){
	
	this.imageCoordinates = imageCoordinates;
	
	this.activeImageReaderMap={};
	this.activeImageReaderMap[record["Record ID"]] = 0;
	
	this.imageConfig=imageConfig;
	
	this.record=record;
	
	this.isFullRes=isFullRes;
	
	$('#imageeditor').html('');
	
	this.setSize("100%","100%");
	this.imgRatio= imageConfig.visible.ratio;
	this.scaledSize= this.getScaledSize();
	
	this.initSketchPad();
	var text = 'Loading...';
	// remove if loading text is already there 
	// otherwise it will append more than one loading texts
	$('#loadingText').remove();
	$('#imageeditor').append('<div id="loadingText" >'+text+'</div>');
	
	this.changeCanvasSize(this.scaledSize.width,this.scaledSize.height);
	
	this.down('image[id="imageCanvas"]').setWidth(this.scaledSize.width);
	this.down('image[id="imageCanvas"]').setHeight(this.scaledSize.height);
	
	this.down('image[id="overlayCanvas"]').setWidth(this.scaledSize.width);
	this.down('image[id="overlayCanvas"]').setHeight(this.scaledSize.height);
	
	if (!isFullRes){
		imageConfig.url += '&height=512';
		//+this.down('image').getHeight();
		//Math.floor(this.down('image').getHeight()/256.0)*256;
 	}
//	else{
//		imageURL += '&height='+this.getImagePanel().getHeight();
//	}
	//this.imageConfig=imageConfig;
	this.down('image[id="imageCanvas"]').setSrc(imageConfig.url);
	this.adjustClipResizing();
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
        		recordid : callBackSpec.scope.record["Record ID"],
        		requestImageReader: requestImageReader
            },
            success : function(result, request) {
            	var response=Ext.JSON.decode(result.responseText);
            	if(page.activeImageReaderMap[page.record["Record ID"]] == null){
            		//return silently if abort polling flag is raised
            		console.log('found abort polling flag raised: stopping image reader polling.');
            		return;
            	}
            	if(response.available){
            		callBackSpec.fn.apply(callBackSpec.scope,callBackSpec.args);
            	}
            	else{
            		if(callCount ==0){
            			page.activeImageReaderMap[callBackSpec.scope.record["Record ID"]] = response.trackerId;
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

adjustClipResizing: function(){
	if(this.imageConfig == null){
		return;
	}
	if(this.imgRatio<1){
		this.fitScale =this.scaledSize.height/this.imageConfig.visible.height;
		//this.overlayScale=this.imageConfig.visible.height/this.imageConfig.record.height;
	}
	else{
		this.fitScale =this.scaledSize.width/this.imageConfig.visible.width;
		//this.overlayScale=this.scaledSize.width/this.imageConfig.visible.width;
		//this.overlayScale=this.imageConfig.visible.width/this.imageConfig.record.width;
	}
	
	//console.log(this.imageConfig);
	//console.log('this.overlayScale'+this.overlayScale);
	
	this.down('image[id="imageCanvas"]').setWidth(this.imageConfig.requested.width*this.fitScale);
	this.down('image[id="imageCanvas"]').setHeight(this.imageConfig.requested.height*this.fitScale);
	
	this.down('image[id="overlayCanvas"]').setWidth(this.imageConfig.record.width*this.fitScale);
	//console.log('overlayCanvas.width'+this.down('image[id="overlayCanvas"]').getWidth());
	this.down('image[id="overlayCanvas"]').setHeight(this.imageConfig.record.height*this.fitScale);
	//console.log('overlayCanvas.height'+this.down('image[id="overlayCanvas"]').getHeight());
	console.log('image resizing done');
},

displayLegend: function(displayLegends){
	this.displayLegends = displayLegends;
	this.addLegends();
},

addLegends: function(){
	if(!this.displayLegends)
	{
		$('#legendText1').remove();
		return;
	}
		
	var imageCoordinates = this.imageCoordinates;
	
	if(this.legendLocation == "")
		this.legendLocation = 'top';
		
	console.log('legend location');
	console.log(this.legendLocation);
	this.adjustClipResizing();
	
	console.log(this.legendLocation);
	
	var location = this.legendLocation;
	
	Ext.Ajax.request({
            method : 'GET',
            url : '../project/getLegends',
            params : {
				recordid : imageCoordinates.recordid,
				sliceNumber : imageCoordinates.sliceNumber,
				frameNumber : imageCoordinates.frameNumber,
				siteNumber : imageCoordinates.siteNumber
            },
            success : function(result, request) {
            	var legends = Ext.decode(result.responseText);
            	console.log(legends.legend);
				$('#imageeditor').append('<div id="legendText1" style="background-color:#B0B0B0;color:black;position:absolute;'+location+':0px;z-index:3;">'+legends.legend+'</div>');
            },
            failure : function(result, request) {
            	console.log('addLegends failed');
            }
    });
    
    this.adjustClipResizing();
},

changeLegendsLocation: function(location) {
	if(this.legendLocation == location)
	{
		console.log('location unchanged');
		console.log(this.legendLocation);
		return;
	}
	
	console.log('location changed');
	console.log(this.legendLocation);
	
	this.legendLocation = location;
	$('#legendText1').remove();
	$('#legendText1').remove();
	this.addLegends();
},

adjustClipping: function(){
	if(this.imageConfig == null){
		return;
	}
	var fitScale=this.fitScale;
	var clip= this.imageConfig.clip;
	
	$('.zoom_image').css('left',''+(-1*clip.left*fitScale));
	
	$('.zoom_image').css('top',''+(-1*clip.top*fitScale));
	
	var clipRect='rect('
			+clip.top*fitScale+','
			+clip.right*fitScale+','
			+clip.bottom*fitScale+','
			+clip.left*fitScale+')';
	$('.zoom_image').css('clip',clipRect);
	
	var overlayScale=this.fitScale;
	var overlayClip= this.imageConfig.overlayClip;
	
	$('.overlay_image').css('left',''+(-1*overlayClip.left*overlayScale));
	$('.overlay_image').css('top',''+(-1*overlayClip.top*overlayScale));
	
	var overlayClipRect='rect('
			+overlayClip.top*overlayScale+','
			+overlayClip.right*overlayScale+','
			+overlayClip.bottom*overlayScale+','
			+overlayClip.left*overlayScale+')';
	$('.overlay_image').css('clip',overlayClipRect);
},

clearOverlayTransparancy:function(){
	this.down('image[id="overlayCanvas"]').setSrc(this.blankImage);
},

setOverlayTransparancy:function(imageUrl){
	imageUrl += '&height='+this.getHeight();
	console.log(this.getHeight());
	console.log(imageUrl);
	this.down('image[id="overlayCanvas"]').setSrc(imageUrl);
},

getScaledSize: function(){
	var panelWidth = this.getWidth();
	var panelHeight = this.getHeight();
	var panelRatio = panelWidth/panelHeight;
	if (panelRatio <= this.imgRatio) {
		return {width : panelWidth, height : parseInt(panelWidth/this.imgRatio)};
	} else {
		return {width : parseInt(this.imgRatio*panelHeight), height : panelHeight};
	}
},

adjustResizing: function(){
	this.scaledSize= this.getScaledSize();
	if(this.sketchpad){
		this.sketchpad.paper().setSize(this.scaledSize.width,this.scaledSize.height);
		this.changeCanvasSize(this.scaledSize.width,this.scaledSize.height);
	}
	this.down('image[id="imageCanvas"]').setWidth(this.scaledSize.width);
	this.down('image[id="imageCanvas"]').setHeight(this.scaledSize.height);
	
	this.down('image[id="overlayCanvas"]').setWidth(this.scaledSize.width);
	this.down('image[id="overlayCanvas"]').setHeight(this.scaledSize.height);
	
	this.adjustClipResizing();
	this.adjustClipping();
},

setViewBox: function(x,y,width,height){
	this.sketchpad.paper().setViewBox(x,y,width,height,false);
},

abortImageReaderPolling:function(){
	if(this.imageReaderPolling !=null){
		//For some reason Ext.Ajax.abort is not aborting the request.
		//Using abortPollingFlag to maintain abort request.
		Ext.Ajax.abort(this.imageReaderPolling);
		//this.imageReaderPolling.abort();
		console.log(this.imageReaderPolling);
		console.log('raising abort polling flag');
		//this.abortPollingFlag=true;
		var requestTrackerIDs =Ext.encode(_.values(this.activeImageReaderMap));
        console.log(_.values(this.activeImageReaderMap));
		
		Ext.Ajax.request({
            method : 'GET',
            url : '../project/cancelImageReaderRequest',
            params : {
				requestTrackerIDs : requestTrackerIDs
            },
            success : function(result, request) {
            	console.log('cancelImageReaderRequest successful');
            },
            failure : function(result, request) {
            	console.log('cancelImageReaderRequest failed');
            }
        });
		this.activeImageReaderMap={};
		console.log(this.activeImageReaderMap);
	}
},

changeRecord:function(record){
	var recordWidth = parseInt(record['Image Width']);
    var recordHeight = parseInt(record['Image Height']);
	this.changeCanvasSize(recordWidth, recordHeight);
},

changeCanvasSize:function(width,height){
	this.sketchpad.paper().changeSize(width,height);
},

fitCanvasSize:function(){
	this.scaledSize= this.getScaledSize();
	this.changeCanvasSize(this.scaledSize.width,this.scaledSize.height);
}
});