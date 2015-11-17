/**
 * Controls image loading on zoom.
 * Also clips the image accordingly.
 */
Ext.define('Manage.controller.Zoom', {
	extend: 'Ext.app.Controller',

	/**
	 * tile size for zoom requests
	 */
	tileSize: 256,

	/**
	 * zoom slider value
	 */
	slider: 1,
	
	/**
	 * URL used to fetch overlays
	 */
	overlayURL:'',
	/**
	 * URL used to fetch scalebar
	 */
	scalebarURL:'',
	/**
	 * x,y,width and height of zoom window
	 */
	winX:'',
	winY:'',
	winWidth:'',
	winHeight:'',
	
	controllers: ['ImageView', 'RecordController'],

	refs: [{
		ref: 'imagePanel',
		selector: 'imagepanel'
	}, {
		ref: 'zoom',
		selector: 'zoom'
	}],


	init: function() {
		this.control({
			'zoom': {
				'zoomWindowChanged': this.onZoomWindowChanged,
				'toInit' : this.toInit,
				'showScalebar':this.getScalebar
			}, 
			'#zoomslider': {
				change: this.onZoomSlide
			}
				
		});
		console.log('zoom controls inited');
	},
	
	toInit: function() {
		currRecordId = this.getRecordControllerController().getCurrentRecordId();
		if (typeof this.prevRecordId == 'undefined' || currRecordId != this.prevRecordId) {
			var scaledSize = this.getZoom().getScaledSize();
			this.getZoom().initSpectrum(scaledSize.width,
					scaledSize.height);
			
			this.getZoom().normalizedWindow.from.x = 0;
			this.getZoom().normalizedWindow.from.y = 0;

			this.getZoom().normalizedWindow.to.x = 0.5;
			this.getZoom().normalizedWindow.to.y = 0.5;
			
			this.winX=0;
			this.winY=0;
			this.winWidth=scaledSize.width/2;
			this.winHeight=scaledSize.height/2;
			
		}
		this.prevRecordId = this.getRecordControllerController().getCurrentRecordId();
	},

	/**
	 * zoom slider changed, change image TODO complete this solution
	 */
	onZoomSlide: function(slider, x) {
		slider.setFieldLabel(x + "X");
		
		this.slider = x;
		this.getScalebar();
		this.onZoomWindowChanged();
	},


	/**
	 * entry point function to load zoom image
	 */
	onZoomWindowChanged: function() {
		console.log('controller/zoom onZoomWindowChanged');

		var zoom = this.getZoom();
		var recordWidth = zoom.width;
		var recordHeight = zoom.height;
		var url = zoom.url;

		//get normalized window
		var normalizedWindow = this.getZoom().getNormalizedWindow();
		
		//calculate window from, to, width and height
		var windowFromX = parseInt(normalizedWindow.from.x * recordWidth, 10);
		if(windowFromX < 0){
			windowFromX =0;
		}
		
		var windowFromY = parseInt(normalizedWindow.from.y * recordHeight, 10);
		if(windowFromY < 0){
			windowFromY =0;
		}	
		
		var windowToX = parseInt(normalizedWindow.to.x * recordWidth, 10);
		if(windowToX < 0){
			windowToX =0;
		}
		
		var windowToY = parseInt(normalizedWindow.to.y * recordHeight, 10);
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
		

		//get the requested image and clipping parameters
		var win = this.getWindows(recordHeight, recordWidth, windowFromX, windowFromY, windowWidth, windowHeight);
		var clip = win.clip;
		var req = win.requested;

		console.log('requested ',req);
		console.log('clip', clip);
		
		this.winX=windowFromX;
		this.winY= windowFromY;
		this.winWidth=windowToX-windowFromX;
		this.winHeight=windowToY-windowFromY;

		//add requested parameters to url
		url += '&window_x=' + req.x1;
		url += '&window_y='+ req.y1;
		url += '&window_width='+ req.width;
		url += '&window_height='+ req.height;

		//set image size, clipping params and set mode of image
		var fitScale = this.slider;
		this.getImageViewController().setZoomParameters(clip, req, fitScale);

		// removed already existing loading node, if any
		$('#loadingText').remove();
		// add loading node on zoom change
		console.log("adding loading text... ");
		var text = 'Loading...';
		$('#imageeditor').append('<div id="loadingText" style="background-color: #C0C0C0;color: black;position:absolute;top: 0px;left: 0px;z-index: 3;">'+text+'</div>');
		
		//set image, on load will take car of clipping
		$('#imageCanvas').attr('src', url); 
			
	},
	
	/**
	 * function to show scale bar
	 */
	getScalebar : function(){		
		var me=this;
		var rc= this.getRecordControllerController();
		
		//return if scalebar not choosen
		var showScalebar=rc.getCheckboxScalebar().getValue();
		if(!showScalebar)
			return;
		
		var scalebarSize=100.0;
		var recordWidth= rc.record['Image Width'];
		var slider=this.slider;
		var viewWidth=this.getImageViewController().getViewWidth();
		
		if(rc.isZoomed){
			viewWidth=recordWidth;
		}

		Ext.Ajax.request({
            method : 'GET',
            url : '../project/getXPixelSize',
            params : {
				recordid : rc.activeRecordID				
            },
            success : function(result, request) {
            	var resp = Ext.decode(result.responseText);
            	var xPixelSize=resp['xPixelSize'];
            	var unit=resp['unit'];
            	           	
            	var scale=(recordWidth/viewWidth)*xPixelSize*scalebarSize;
            	           	
            	if(rc.isZoomed){
            		scale=scale/slider;	
            	}

            	if(unit === "micron")
            		unit="&#956;m";
            	me.getImagePanel().setScalebarText((Math.round(scale))+" "+unit);
            	me.getImageViewController().addScalebar();
            },
            failure : function(result, request) {
            	console.log('scalebar failed');
            }
		});
	},

	/**
	 * gets the window_x, window_y, window_width, window_height
	 * TODO change this docs
	 */
	getWindows: function(recordWidth, recordHeight, x, y, width, height){
		var win = {};

		//calculate visible window parameters
		var visible={};
		visible.x1 = x;
		visible.y1 = y;
		visible.width = width;
		visible.height = height;
		visible.x2 = visible.x1 + visible.width;
		visible.y2 = visible.y1 + visible.height;
		visible.ratio= visible.width/visible.height;

		//calculate requested window parameters
		var requested={};
		requested.x1 = visible.x1 - visible.x1%this.ceilPowerOf2(visible.width);
		requested.x2 = this.ceilPowerOf2(visible.x2);
		requested.width = requested.x2 - requested.x1;

		requested.y1 = visible.y1 - visible.y1%this.ceilPowerOf2(visible.height);
		requested.y2 = this.ceilPowerOf2(visible.y2);
		requested.height = requested.y2 - requested.y1;

		var clip = {};
		clip.left = visible.x1 - requested.x1;
		clip.right = visible.width+clip.left;
		clip.top = visible.y1 - requested.y1;
		clip.bottom = visible.height+clip.top;

		win['requested'] = requested;
		win['clip'] = clip;
		return win;
	},

	/**
	 * TODO change this function or change its usage
	 * It is unclear
	 */
	ceilPowerOf2: function(n){
		var p = 0;
		var tileSizePower = Math.log(this.tileSize) / Math.LN2;

		if(n !== 0){
			p = Math.ceil(Math.log(n)/Math.LN2);
		}
		p = ( p >= tileSizePower) ? p : tileSizePower;

		return Math.pow(2,p);
	},
	
	setOverlayURL: function(url)
	{
		this.overlayURL = url;
	},
	
	setScalebarURL: function(url)
	{
		this.scalebarURL = url;
	}

});
