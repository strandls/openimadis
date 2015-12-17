Ext.define('Manage.controller.ImageViewController', {
	extend: 'Ext.app.Controller',
	views:[
		'imageview.ImageView',
		'imageview.ImageProperties',
		'imageview.ImageSliders',
		'imageview.ImagePanel',
		'imageview.ImageControls',
		'imageview.ImageMetaData',
		'imageview.RecordMetaData',
		'imageview.Attachments',
		'imageview.Comments',
		'imageview.History',
		'imageview.ImageControls',
		'imageview.ImageToolbar',
		'imageview.ImageContrast'
	],
	
	refs :[{
			ref : 'imagePanel',
			selector : 'imagePanel'
		},{
	        ref : 'fullres',
	        selector : 'imagetoolbar #button_fullres'
	    },{
	        ref : 'multiChannel',
	        selector : 'imagetoolbar #button_allchannel'
	    }, {
	        ref : 'zoomButton',
	        selector : 'imagetoolbar #button_panzoom'
	    },
		{
			ref : 'thumbnails',
			selector : 'thumbnails'
		},
		{
			ref:'zoomThumbnail',
			selector:'zoomThumbnail'
		}
	],
	
	init: function() {
		
		this.control({
			'imagetoolbar #button_panzoom' : {
		    	toggle : this.onZoomToggle
			},
			'imagecontrols #overlaycontrol': {
                refreshOverlays : this.refreshOverlays
            },
			'zoomThumbnail':{
				'zoomWindowChanged':this.onzoomWindowChanged,
				'show':this.showZoomThumbnail,
				'hide':this.hideZoomThumbnail
			}
		});	
	},
	
	/**
     * Set image in the image viewer
     */
    setImage : function(imageURL,record){
    	var recordWidth = parseInt(record['Image Width']);
        var recordHeight = parseInt(record['Image Height']);
        
        console.log("set image");
        console.log(record['Channel Count']);
        
        recordWidth = recordHeight * this.getMultichannelScalingFactor(recordWidth, recordHeight, record['Channel Count']);

    	var imageConfig=this.getDefaultImageConfig(imageURL, recordWidth, recordHeight);
    	
		if(this.getZoomButton().pressed){
			var normalizedWindow=this.getZoomThumbnail().getNormalizedWindow();
			imageConfig = this.getTiledImage(imageURL,normalizedWindow,record);
			
			this.getZoomThumbnail().setImage(imageConfig.url,imageConfig.visible.ratio);
		}
		
		var recordSelectionController=this.getController('Manage.controller.RecordSelection');
		var coordinates = recordSelectionController.getLegendCoordinates();
		
		this.getImagePanel().setImage(imageConfig,record,this.getFullres().pressed,coordinates);
    },
    
    onZoomToggle:function(button,pressed){
    	if(pressed){
    		this.getZoomThumbnail().show();
    	}
    	else{
    		this.getZoomThumbnail().hide();
    	}
    },
    
    resetZoom:function(button,pressed){
    	this.getZoomThumbnail().resetSpectrum();
    },
    
    showZoomThumbnail:function(){
		var recordSelectionController=this.getController('Manage.controller.RecordSelection');
		
		var recordid = this.getThumbnails().getSelectionModel().getLastSelected().data.id;
		var record = recordSelectionController.getRecordItem(recordid).data;
		var imageURL = recordSelectionController.getImageURL(recordid);
		imageURL += '&height=128';
		
		var recordWidth = parseInt(record['Image Width']);
        var recordHeight = parseInt(record['Image Height']);
		var imgRatio = recordWidth/recordHeight;
		this.getZoomThumbnail().setImage(imageURL,imgRatio);
		this.getZoomThumbnail().windowFinalized();
    },
    
    hideZoomThumbnail:function(){
    	var zoomThumbnail = this.getZoomThumbnail();
    	zoomThumbnail.setImage('images/panojs/blank.gif',1);
    	zoomThumbnail.setFullWindow();
		this.onzoomWindowChanged(zoomThumbnail.getNormalizedWindow());
		this.resetZoom();
    },
    
    getTiledImage: function(imageURL,normalizedWindow,record){

		var recordWidth = parseInt(record['Image Width']);
		var recordHeight = parseInt(record['Image Height']);
		
		var windowFromX = parseInt(normalizedWindow.from.x * recordWidth);
		if(windowFromX < 0){
			windowFromX =0;
		}
		
		var windowFromY = parseInt(normalizedWindow.from.y * recordHeight);
		if(windowFromY < 0){
			windowFromY =0;
		}	
		
		var windowToX = parseInt(normalizedWindow.to.x * recordWidth);
		if(windowToX < 0){
			windowToX =0;
		}
		
		var windowToY = parseInt(normalizedWindow.to.y * recordHeight);
		if(windowToY < 0){
			windowToY =0;
		}
		
		var windowWidth = windowToX - windowFromX;
		if(windowWidth < 0){
			windowWidth =0;
		}
		
		var windowHeight = windowToY - windowFromY;
		if(windowHeight < 0){
			windowHeight =0;
		}
		
//		var tiledImageConfig={
//				'url':tiledImageURL,
//				'x':windowFromX,
//				'y':windowFromY,
//				'width':windowWidth,
//				'height':windowHeight,
//		};
//		
		//return tiledImageConfig;
		//return this.adjustImageClipping(tiledImageConfig);
		var imageConfig=this.adjustImageClipping(imageURL,recordWidth,recordHeight,windowFromX,windowFromY
				,windowWidth,windowHeight);
		
		var req=imageConfig.requested;
		imageConfig.url += '&window_x='+req.x1;
		imageConfig.url += '&window_y='+req.y1;
		imageConfig.url += '&window_width='+req.width;
		imageConfig.url += '&window_height='+req.height;
		
		return imageConfig;
    },
    
    getMultichannelScalingFactor: function(recordWidth, recordHeight, channelCount) {
    	if(this.getMultiChannel().pressed)
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
    
    getDefaultImageConfig: function(url,width,height){
    
    	var config={};
    	config.url = url; 
    	
    	var record={};
    	config['record']=record;
    	
    	record.height=height;
    	record.width=width;
    	
    	var visible={};
    	config['visible']=visible;
    	
    	visible.x1 = 0;
    	visible.y1 = 0;
    	visible.width = width;
    	visible.height = height;
    	visible.x2 = visible.x1+visible.width;
    	visible.y2 = visible.y1+visible.height;
    	visible.ratio= visible.width/visible.height;
    	
    	var requested={};
    	config['requested']=visible;
    	
    	
    	var clip={};
    	config['clip'] = clip;
    	clip.left = 0;
    	clip.right = visible.width;
    	clip.top = 0;
    	clip.bottom = visible.height;
    	
    	var overlayClip={};
    	config['overlayClip'] = overlayClip;
    	overlayClip.left = 0;
    	overlayClip.right = visible.width;
    	overlayClip.top = 0;
    	overlayClip.bottom = visible.height;
    	
    	return config;
    },
    
    adjustImageClipping: function(url,recordWidth,recordHeight,x1,y1,width,height){
    	
    	var config={};
    	config.url = url; 
    	var visible={};
    	config['visible']=visible;
    	
    	var record={};
    	config['record']=record;
    	
    	record.height=recordHeight;
    	record.width=recordWidth;
    	
    	visible.x1=x1;
    	visible.y1=y1;
    	visible.width=width;
    	visible.height=height;
    	visible.x2=visible.x1+visible.width;
    	visible.y2=visible.y1+visible.height;
    	visible.ratio= visible.width/visible.height;

    	var requested={};
    	config['requested']=requested;
    	
    	requested.x1 = visible.x1 - visible.x1%this.ceilPowerOf2(visible.width);
    	requested.x2 = this.ceilPowerOf2(visible.x2);
    	requested.width = requested.x2 - requested.x1;
    	
    	requested.y1 = visible.y1 - visible.y1%this.ceilPowerOf2(visible.height);
    	requested.y2 = this.ceilPowerOf2(visible.y2);
    	requested.height = requested.y2 - requested.y1;
    	
    	var clip={};
    	config['clip'] = clip;
    	clip.left = visible.x1 - requested.x1;
    	clip.right = visible.width+clip.left;
    	clip.top = visible.y1 - requested.y1;
    	clip.bottom = visible.height+clip.top;
    	
    	var overlayClip={};
    	config['overlayClip'] = overlayClip;
    	overlayClip.left = visible.x1 - 0;
    	overlayClip.right = visible.width + overlayClip.left;
    	overlayClip.top = visible.y1 - 0;
    	overlayClip.bottom = visible.height+overlayClip.top;
    	
    	return config;
    },
    
    ceilPowerOf2: function(n){
    	var p=0;
    	if(n!=0){
    		p= Math.ceil(Math.log(n)/Math.LN2);
    	}
    	var tileSizePower =this.getImagePanel().tileSizePower; 
    	p=(p>=tileSizePower)?p:tileSizePower;
    	return Math.pow(2,p);
    },
	
	onzoomWindowChanged:function(normalizedWindow){
		var recordSelectionController=this.getController('Manage.controller.RecordSelection');
		
		var recordid = this.getThumbnails().getSelectionModel().getLastSelected().data.id;
		var record = recordSelectionController.getRecordItem(recordid).data;
		var imageURL = recordSelectionController.getImageURL(recordid,false);
		var imgConfig=this.getTiledImage(imageURL,normalizedWindow,record);
		
		var coordinates = recordSelectionController.getLegendCoordinates();
		this.getImagePanel().setImage(imgConfig, record,this.getFullres().pressed,coordinates);
		//this.refreshOverlays();
	},
	
	refreshOverlays:function(){
		var recordSelectionController=this.getController('Manage.controller.RecordSelection');
		var visibleBox = this.getImagePanel().imageConfig.visible;
		var imagePanel = this.getImagePanel();
		imagePanel.setViewBox(visibleBox.x1,visibleBox.y1,
				visibleBox.width,visibleBox.height);		
		recordSelectionController.drawHandCreatedOverlays();
		imagePanel.fitCanvasSize();
	}
});