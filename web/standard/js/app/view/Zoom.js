/**
 * The Pan and Zoom view. Has functionality for handling all the zoom rectangle
 * drag and movement.
 */
Ext.define('Manage.view.Zoom', {
	extend : 'Ext.panel.Panel',
	xtype : 'zoom',
	alias : 'widget.zoom',

	border : true,

	html : '<div id="zoomCanvas"></div>',

	/**
	 * the basic url of the image
	 */
	url : '',

	/**
	 * the image width, height and ratio
	 */
	height : '',
	width : '',
	imgRatio : '',
	zoomMode : false,

	/**
	 * x, y values are normalised i.e x/width, y/height
	 */
	normalizedWindow : {
		'from' : {
			'x' : 0,
			'y' : 0
		},
		'to' : {
			'x' : 0.5,
			'y' : 0.5
		}
	},

	bbar : [ {

		xtype : 'slider',
		id : 'zoomslider',

		minValue : 1,
		maxValue : 5,
		value : 1,

		fieldLabel : '1X',
		labelWidth : 20,
		labelAlign : "right",

		style : {
			paddingRight : '8px',
			width : '100%'
		},

		tipText : function(thumb)
		{
			var val = Ext.ComponentQuery.query('#zoomslider')[0].getValue();
			return val + 'x';
		}
	} ],

	listeners : {
		afterrender : {
			fn : function(component, opts)
			{
				component.initRaphael();
			},
			scope : this
		},
		afterlayout : {
			fn : function(component, layout, opts)
			{
				component.adjustRaphael();
				component.setImage();

				// initialize the spectrum
				component.fireEvent("toInit", component);
			},
			scope : this
		}
	},

	initRaphael : function()
	{
		console.log('view/Zoom initRaphael <afterrender>');
		this.paper = Raphael('zoomCanvas', this.getWidth(), this.getHeight());
		this.thumbnail = this.paper.image('images/panojs/blank.gif', 0, 0, 200, 220);
	},

	adjustRaphael : function()
	{
		console.log('view/Zoom adjustRaphael <afterlayout>');
		if (this.paper)
		{
			this.paper.setSize(this.getWidth(), this.getHeight());
		}
	},

	/**
	 * sets the basic params for this image
	 * 
	 * @params {URL} url basic url of the image
	 * @params {record} record the record object
	 */
	setParams : function(url, record)
	{
		console.log('view/zoom seturl');
		this.url = url;
		this.height = record['Image Height'];
		this.width = record['Image Width'];
		this.imgRatio = this.width / this.height;
	},
	
	/**
	 * zoom mode
	 * @param value
	 */
	setZoomMode : function(value)
	{
		this.zoomMode = value;
	},

	/**
	 * sets the image with the basic url and imgRatio
	 */
	setImage : function()
	{
		console.log('view/zoom setimage');
		var scaledSize = this.getScaledSize();
		if(this.zoomMode)
		{
			this.thumbnail.attr({
				'src' : this.url,
				'width' : scaledSize.width,
				'height' : scaledSize.height
			});
		}
		
		this.paper.setSize(scaledSize.width, scaledSize.height);
		console.log('scaledSize.width:' + scaledSize.width + 'scaledSize.height:' + scaledSize.height);
	},

	/**
	 * get sizes with the aspect ratio maintained
	 */
	getScaledSize : function()
	{
		var panelWidth = this.getWidth();
		var panelHeight = this.getHeight();
		var panelRatio = panelWidth / panelHeight;
		if (panelRatio <= this.imgRatio)
		{
			return {
				width : panelWidth,
				height : parseInt(panelWidth / this.imgRatio)
			};
		}
		else
		{
			return {
				width : parseInt(this.imgRatio * panelHeight),
				height : panelHeight
			};
		}
	},

	resetSpectrum : function()
	{
		this.Spectrum.rect.remove();
		this.Spectrum.resizer.remove();
		this.Spectrum.resizerBlanket.remove();
		this.Spectrum = null;
	},

	initSpectrum : function(pwidth, pheight)
	{
		var scale = 1, canvasW = pwidth, canvasH = pheight;

		var width = pwidth / 2, height = pheight / 2,
		x0 = 0, y0 = 0;

		var resizerBlanketRadius = 25, resizerRadius = 5;

		if (typeof this.Spectrum !== 'undefined' && this.Spectrum !== null)
		{
			this.resetSpectrum();
		}

		var Spectrum = {};
		this.Spectrum = Spectrum;
		Spectrum.boundaryLimit = {};
		Spectrum.boundaryLimit.endx = pwidth;
		Spectrum.boundaryLimit.endy = pheight;

		Spectrum.paper = this.paper;

		Spectrum.rect = Spectrum.paper.rect(x0, y0, width, height);
		Spectrum.resizer = Spectrum.paper.circle(x0 + width, y0 + height, resizerRadius);
		Spectrum.resizerBlanket = Spectrum.paper.circle(x0 + width, y0 + height, resizerBlanketRadius);

		Spectrum.rect.attr({
			fill : 'white',
			opacity : 0.3
		});
		Spectrum.resizerBlanket.attr({
			fill : 'yellow',
			opacity : 0
		});
		Spectrum.resizer.attr({
			fill : 'yellow',
			opacity : 1
		});

		var zoomThumb = this;

		/**
		 * drag function of Raphael
		 */
		Spectrum.rect.drag(
		/**
		 * bugfix here for checking n
		 * not allowing the rect to be dragged
		 * outside the zoom thumbnail and into a
		 * non overlapping region
		 * 
		 */
		function(dx, dy, x, y)
		{

			if (((this.rox + dx) > Spectrum.boundaryLimit.endx) || ((this.roy + dy) > Spectrum.boundaryLimit.endy) || ((this.ox + dx) < 0)
					|| ((this.oy + dy) < 0))
			{
				console.log("SpectrumBox out of Bounds : move")

			}
			else
			{
				this.attr({
					x : this.ox + dx,
					y : this.oy + dy
				});
				Spectrum.resizerBlanket.attr({
					cx : this.rbox + dx,
					cy : this.rboy + dy
				});
				Spectrum.resizer.attr({
					cx : this.rox + dx,
					cy : this.roy + dy
				});
			}
		}, function()
		{
			this.ox = this.attr("x");
			this.oy = this.attr("y");
			this.rbox = Spectrum.resizerBlanket.attr('cx');
			this.rboy = Spectrum.resizerBlanket.attr('cy');
			this.rox = Spectrum.resizer.attr('cx');
			this.roy = Spectrum.resizer.attr('cy');
		}, function()
		{
			zoomThumb.windowFinalized();
		});

		Spectrum.resizerBlanket.drag(
		/**
		 * ******** bugfix here for the spectrumRect not being
		 * visible when the blanket is dragged to the left of or
		 * above point (thumbnailWIndow.x , thumbnailWindow.y)
		 * 
		 */
		function(dx, dy)
		{
			if (((this.rox + dx) > Spectrum.boundaryLimit.endx) || ((this.roy + dy) > Spectrum.boundaryLimit.endy) || ((this.ox + dx) < 0)
					|| ((this.oy + dy) < 0))
			{
				console.log("SpectrumBox out of Bounds : stretch");
				return;
			}

			this.attr({
				cx : this.ox + dx,
				cy : this.oy + dy
			});
			Spectrum.resizer.attr({
				cx : this.rox + dx,
				cy : this.roy + dy
			});
			Spectrum.rect.attr({
				width : this.rwidth + dx,
				height : this.rheight + dy
			});
		}, function()
		{
			this.ox = this.attr('cx');
			this.oy = this.attr('cy');
			this.rox = Spectrum.resizer.attr('cx');
			this.roy = Spectrum.resizer.attr('cy');
			this.rwidth = Spectrum.rect.attr('width');
			this.rheight = Spectrum.rect.attr('height');
		}, function()
		{
			zoomThumb.windowFinalized();
		});
	},

	windowFinalized : function()
	{
		//						console.log('windowFinalized');
		var thumbnailWindow = this.Spectrum.rect.attr();
		this.normalizedWindow.from.x = (thumbnailWindow.x / this.paper.width);
		this.normalizedWindow.from.y = (thumbnailWindow.y / this.paper.height);

		this.normalizedWindow.to.x = ((thumbnailWindow.x + thumbnailWindow.width) / this.paper.width);
		this.normalizedWindow.to.y = ((thumbnailWindow.y + thumbnailWindow.height) / this.paper.height);

		this.fireEvent("zoomWindowChanged", this.normalizedWindow);
	},

	getNormalizedWindow : function()
	{
		return this.normalizedWindow;
	}
});
