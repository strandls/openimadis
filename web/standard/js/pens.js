/**
 * The default Pen object.
 */

Pen = function() {
	var that = this;
	that._color = "#000000";
	that._opacity = 1.0;
	that._width = 1;
	that._fontSize = 10;
	that._offset = null;
	
	that._pen;
	
	that.width = function (value) {
		if (value === undefined) {
			return that._pen.width();
		}
		that._pen.width(value);
		that._width = that._pen.width();
		return that;
	};
	
	that.color = function (value) {
		if (value === undefined) {
			return that._pen.color();
		}
		that._pen.color(value);
		that._color = that._pen.color();
		return that;
	};
	
	that.opacity = function (value) {
		if (value === undefined) {
			return that._pen.opacity();
		}
		that._pen.opacity(value);
		that._opacity = that._pen.opacity();
		return that;
	};
	
	that.fontSize = function (value) {
		if (value === undefined) {
			return that._pen.fontSize();
		}
		that._pen.fontSize(value);
		that._fontSize = that._pen.fontSize();
		return that;
	};
	
	that.start = function(){};
	that.move = function(){};
	that.finish = function(){};
	
	that.pen = function(value, options) {
		if (value === undefined)
			return that._pen;
                if (options !== undefined) {
			that.width(options['line-width']);
			that.color(options['color']);
			that.opacity(options['opacity']);
		}
		switch(value) {
		case "pencil":
			that._pen = new MousePen();
			that._pen.color(that._color).opacity(that._opacity).width(that._width).fontSize(that._fontSize);
			break;
		case "circle":
			that._pen = new CirclePen();
			that._pen.color(that._color).opacity(that._opacity).width(that._width).fontSize(that._fontSize);
			break;
		case "line":
			that._pen = new LinePen();
			that._pen.color(that._color).opacity(that._opacity).width(that._width).fontSize(that._fontSize);
			break;
		case "ellipse":
			that._pen = new EllipsePen();
			that._pen.color(that._color).opacity(that._opacity).width(that._width).fontSize(that._fontSize);
			break;
		case "rectangle":
			that._pen = new RectPen();
			that._pen.color(that._color).opacity(that._opacity).width(that._width).fontSize(that._fontSize);
			break;
		case "selection":
			var spen = new RectPen();
			spen.color(that._color).opacity(1).width(1);
			return spen;
			break;
		case "text":
			that._pen = new TextPen();
			that._pen.color(that._color).opacity(that._opacity).width(that._width).fontSize(that._fontSize);
			break;
		case "arrow":
			that._pen = new ArrowPen();
			that._pen.color(that._color).opacity(that._opacity).width(that._width).fontSize(that._fontSize);
			break;
		default:
			// pen unknown. value should be the pen object
			if (value !== undefined)
				that._pen = value;
            break;
		}
		that.start = that._pen.start;
		that.move = that._pen.move;
		that.finish = that._pen.finish;

		return that;
	};
	
	var MousePen = function () {
		this._color = "#000000";
		this._opacity = 1.0;
		this._width = 1;
		this._fontSize = 10;
		this._offset = null;
		
		// Drawing state
		this._drawing = false;
//		this._points = [];
	};
	MousePen.prototype.constructor = MousePen;
	MousePen.prototype._c = null;
	MousePen.prototype._points = [];
	MousePen.prototype.color = function(value) {
		if (value === undefined){
			return MousePen.prototype._color;
		}
		
		MousePen.prototype._color = value;
		
		return MousePen.prototype;
	};
	
	MousePen.prototype.width = function(value) {
		if (value === undefined) {
			return MousePen.prototype._width;
		} 
		
		if (value < MousePen.MIN_WIDTH) {
			value = MousePen.MIN_WIDTH;
		} else if (value > MousePen.MAX_WIDTH) {
			value = MousePen.MAX_WIDTH;
		}
		
		MousePen.prototype._width = value;
		
		return MousePen.prototype;
	};
	
	MousePen.prototype.opacity = function(value) {
		if (value === undefined) {
			return MousePen.prototype._opacity;
		} 
		
		if (value < 0) {
			value = 0;
		} else if (value > 1) {
			value = 1;
		}
		
		MousePen.prototype._opacity = value;
		
		return MousePen.prototype;
	};
	
	MousePen.prototype.fontSize = function(value) {
		if (value === undefined) {
			return MousePen.prototype._fontSize;
		} 
		
		if (value < 1) {
			value = 1;
		}
		
		MousePen.prototype._fontSize = value;
		
		return MousePen.prototype;
	};
	
	MousePen.prototype.start = function(e, sketchpad) {
		MousePen.prototype._drawing = true;
		
		MousePen.prototype._offset = $(sketchpad.container()).offset();
		var viewCoords = sketchpad.getViewCoordinates(e.pageX - MousePen.prototype._offset.left,
				e.pageY - MousePen.prototype._offset.top);
		MousePen.prototype._points.push([viewCoords.x, viewCoords.y]);
		
		MousePen.prototype._c = sketchpad.paper().path();
		
		MousePen.prototype._c.attr({ 
			stroke: MousePen.prototype._color,
			"stroke-opacity": MousePen.prototype._opacity,
			"stroke-width": MousePen.prototype._width,
			"stroke-linecap": "round",
			"stroke-linejoin": "round"
		});
	};
	MousePen.prototype.finish = function(e, sketchpad) {
		if (!MousePen.prototype._drawing)
			return "";
		var path = null;
		
		if (MousePen.prototype._c !== null) {
			if (MousePen.prototype.isinvalid()) {
				MousePen.prototype._c.remove();
			} else {
				path = MousePen.prototype._c;
			}
		}
		
		MousePen.prototype.reset();
		
		return path;
	};
	MousePen.prototype.reset = function() {
		MousePen.prototype._drawing = false;
		MousePen.prototype._c = null;
		MousePen.prototype._points = [];
	};
	MousePen.prototype.isinvalid = function() {
		return MousePen.prototype._points.length <= 1 ||
		       MousePen.prototype._c === null ||
		       MousePen.prototype._c === undefined ||
		       MousePen.prototype._c.attr().path === null ||
		       MousePen.prototype._c.attr().path === undefined ||
		       MousePen.prototype._c.attr().path.length === 0;
	};
	MousePen.prototype.move = function(e, sketchpad) {
		if (MousePen.prototype._drawing === true) {
			var viewCoords = sketchpad.getViewCoordinates(e.pageX - MousePen.prototype._offset.left,
					e.pageY - MousePen.prototype._offset.top);
			MousePen.prototype._points.push([viewCoords.x, viewCoords.y]);
			MousePen.prototype._c.attr({ path: MousePen.prototype.points_to_svg() });
		}
	};
	MousePen.prototype.points_to_svg = function() {
		if (MousePen.prototype._points !== null && MousePen.prototype._points.length > 1) {
			var p = MousePen.prototype._points[0];
			var path = "M" + p[0] + "," + p[1];
			for (var i = 1, n = MousePen.prototype._points.length; i < n; i++) {
				p = MousePen.prototype._points[i];
				path += "L" + p[0] + "," + p[1]; 
			} 
			return path;
		} else {
			return "";
		}
	};
	
	MousePen.MAX_WIDTH = 1000;
	MousePen.MIN_WIDTH = 0.5;
	
	/**
	 * Default circle pen : CirclePen
	 */
	var CirclePen = function () {
		MousePen.call(this);
	};
	CirclePen.prototype = new MousePen();
	CirclePen.prototype.constructor = CirclePen;
	CirclePen.prototype.start = function(e, sketchpad) {
		CirclePen.prototype._drawing = true;
		
		CirclePen.prototype._offset = $(sketchpad.container()).offset();
		var viewCoords = sketchpad.getViewCoordinates(e.pageX - CirclePen.prototype._offset.left,
				e.pageY - CirclePen.prototype._offset.top);
		CirclePen.prototype._x = viewCoords.x;
		CirclePen.prototype._y = viewCoords.y;
		
		CirclePen.prototype._c = sketchpad.paper().circle(CirclePen.prototype._x, CirclePen.prototype._y, 0);
		
		CirclePen.prototype._c.attr({ 
			stroke: MousePen.prototype._color,
			"stroke-opacity": MousePen.prototype._opacity,
			"stroke-width": MousePen.prototype._width,
			"stroke-linecap": "round",
			"stroke-linejoin": "round"
		});
	};
	CirclePen.prototype.finish = function(e, sketchpad) {
		if (!CirclePen.prototype._drawing)
			return "";
		var path = null;
		
		if (CirclePen.prototype._c !== null) {
			if (CirclePen.prototype.isinvalid()) {
				CirclePen.prototype._c.remove();
			} else {
				path = CirclePen.prototype._c;
			}
		}
		
		CirclePen.prototype.reset();
		
		return path;
	};
	CirclePen.prototype.reset = function() {
		CirclePen.prototype._drawing = false;
		CirclePen.prototype._c = null;
		CirclePen.prototype._x = 0;
		CirclePen.prototype._y = 0;
		CirclePen.prototype._r = 0;
	};
	CirclePen.prototype.isinvalid = function() {
		return CirclePen.prototype._r === 0;
	};
	CirclePen.prototype.move = function(e, sketchpad) {
		if (CirclePen.prototype._drawing === true) {
			
			var viewCoords = sketchpad.getViewCoordinates(e.pageX - CirclePen.prototype._offset.left,
					e.pageY - CirclePen.prototype._offset.top);
			
			var x = viewCoords.x;
			var y = viewCoords.y;
			CirclePen.prototype._r = Math.sqrt(Math.pow(CirclePen.prototype._x - x, 2) + Math.pow(CirclePen.prototype._y - y, 2));
			CirclePen.prototype._c.attr({r: CirclePen.prototype._r});
		}
	};
	
	
	/**
	 * Default arrow pen : ArrowPen
	 */
	var ArrowPen = function () {
		MousePen.call(this);
	};
	ArrowPen.prototype = new MousePen();
	ArrowPen.prototype.constructor = LinePen;
	ArrowPen.prototype.start = function(e, sketchpad) {
		ArrowPen.prototype._drawing = true;
		
		ArrowPen.prototype._offset = $(sketchpad.container()).offset();
		
		var viewCoords = sketchpad.getViewCoordinates(e.pageX - ArrowPen.prototype._offset.left,
				e.pageY - ArrowPen.prototype._offset.top);
		
		ArrowPen.prototype._x1 = viewCoords.x;
		ArrowPen.prototype._y1 = viewCoords.y;
		
		ArrowPen.prototype._c = sketchpad.paper().path();
		ArrowPen.prototype._a = sketchpad.paper().circle(ArrowPen.prototype._x1, ArrowPen.prototype._y1, 2);
		
		ArrowPen.prototype._c.attr({ 
			stroke: MousePen.prototype._color,
			"stroke-opacity": MousePen.prototype._opacity,
			"stroke-width": MousePen.prototype._width,
			"stroke-linecap": "round",
			"stroke-linejoin": "round"
		});
		
		ArrowPen.prototype._a.attr({ 
			stroke: MousePen.prototype._color,
			"fill": MousePen.prototype._color,
			"stroke-opacity": MousePen.prototype._opacity,
			"stroke-width": MousePen.prototype._width,
			"stroke-linecap": "round",
			"stroke-linejoin": "round"
		});
	};
	ArrowPen.prototype.finish = function(e, sketchpad) {
		if (!ArrowPen.prototype._drawing)
			return "";
		var path = null;
		
		if (ArrowPen.prototype._c !== null) {
			if (ArrowPen.prototype.isinvalid()) {
				ArrowPen.prototype._c.remove();
			} else {
				path = [ArrowPen.prototype._c,ArrowPen.prototype._a];
			}
		}
		
		ArrowPen.prototype.reset();
		
		return path;
	};
	ArrowPen.prototype.reset = function() {
		ArrowPen.prototype._drawing = false;
		ArrowPen.prototype._c = null;
		ArrowPen.prototype._x1 = 0;
		ArrowPen.prototype._y1 = 0;
		ArrowPen.prototype._x2 = 0;
		ArrowPen.prototype._y2 = 0;
	};
	ArrowPen.prototype.isinvalid = function() {
		return (	(ArrowPen.prototype._x1 == ArrowPen.prototype._x2) &&	(ArrowPen.prototype._y1 == ArrowPen.prototype._y2));
	};
	ArrowPen.prototype.move = function(e, sketchpad) {
		if (ArrowPen.prototype._drawing === true) {
			var viewCoords = sketchpad.getViewCoordinates(e.pageX - ArrowPen.prototype._offset.left,
					e.pageY - ArrowPen.prototype._offset.top);
			
			ArrowPen.prototype._x2 = viewCoords.x;
			ArrowPen.prototype._y2 = viewCoords.y;			
			ArrowPen.prototype._c.attr({ path: ArrowPen.prototype.create_line()});
			ArrowPen.prototype._a.remove();
			ArrowPen.prototype._a = sketchpad.paper().circle(ArrowPen.prototype._x2, ArrowPen.prototype._y2, 2);
			ArrowPen.prototype._a.attr({ 
			 stroke: MousePen.prototype._color,
			"fill": MousePen.prototype._color,
			"stroke-opacity": MousePen.prototype._opacity,
			"stroke-width": MousePen.prototype._width,
			"stroke-linecap": "round",
			"stroke-linejoin": "round"
		});
		}
	};
	
	ArrowPen.prototype.create_line = function() {
		if (!ArrowPen.prototype.isinvalid()) {			
			var x1 = ArrowPen.prototype._x1;
			var y1 = ArrowPen.prototype._y1;
			var x2 = ArrowPen.prototype._x2;
			var y2 = ArrowPen.prototype._y2;
			var p = MousePen.prototype._points[0];
    			  			
			var linePath = "M" + x1 + "," + y1;
			linePath += "L" + x2 + "," + y2;
			return linePath;
		} else {
			return "";
		}
	};
	
	
	/**
	 * Default line pen : LinePen
	 */
	var LinePen = function () {
		MousePen.call(this);
	};
	LinePen.prototype = new MousePen();
	LinePen.prototype.constructor = LinePen;
	LinePen.prototype.start = function(e, sketchpad) {
		LinePen.prototype._drawing = true;
		
		LinePen.prototype._offset = $(sketchpad.container()).offset();
		
		var viewCoords = sketchpad.getViewCoordinates(e.pageX - LinePen.prototype._offset.left,
				e.pageY - LinePen.prototype._offset.top);
		
		LinePen.prototype._x1 = viewCoords.x;
		LinePen.prototype._y1 = viewCoords.y;
		
		LinePen.prototype._c = sketchpad.paper().path();
		
		LinePen.prototype._c.attr({ 
			stroke: MousePen.prototype._color,
			"stroke-opacity": MousePen.prototype._opacity,
			"stroke-width": MousePen.prototype._width,
			"stroke-linecap": "round",
			"stroke-linejoin": "round"
		});
	};
	LinePen.prototype.finish = function(e, sketchpad) {
		if (!LinePen.prototype._drawing)
			return "";
		var path = null;
		
		if (LinePen.prototype._c !== null) {
			if (LinePen.prototype.isinvalid()) {
				LinePen.prototype._c.remove();
			} else {
				path = LinePen.prototype._c;
			}
		}
		
		LinePen.prototype.reset();
		
		return path;
	};
	LinePen.prototype.reset = function() {
		LinePen.prototype._drawing = false;
		LinePen.prototype._c = null;
		LinePen.prototype._x1 = 0;
		LinePen.prototype._y1 = 0;
		LinePen.prototype._x2 = 0;
		LinePen.prototype._y2 = 0;
	};
	LinePen.prototype.isinvalid = function() {
		return (	(LinePen.prototype._x1 == LinePen.prototype._x2) &&	(LinePen.prototype._y1 == LinePen.prototype._y2));
	};
	LinePen.prototype.move = function(e, sketchpad) {
		if (LinePen.prototype._drawing === true) {
			var viewCoords = sketchpad.getViewCoordinates(e.pageX - LinePen.prototype._offset.left,
					e.pageY - LinePen.prototype._offset.top);
			
			LinePen.prototype._x2 = viewCoords.x;
			LinePen.prototype._y2 = viewCoords.y;
			LinePen.prototype._c.attr({ path: LinePen.prototype.create_line() });
		}
	};
	LinePen.prototype.create_line = function() {
		if (!LinePen.prototype.isinvalid()) {
			var x1 = LinePen.prototype._x1;
			var y1 = LinePen.prototype._y1;
			var x2 = LinePen.prototype._x2;
			var y2 = LinePen.prototype._y2;
			var p = MousePen.prototype._points[0];
			var path = "M" + x1 + "," + y1;
			path += "L" + x2 + "," + y2;
			return path;
		} else {
			return "";
		}
	};
	
	
	/**
	 * Default ellipse pen : EllipsePen
	 */
	var EllipsePen = function () {
		MousePen.call(this);
	};
	EllipsePen.prototype = new MousePen();
	EllipsePen.prototype.constructor = EllipsePen;
	EllipsePen.prototype.start = function(e, sketchpad) {
		EllipsePen.prototype._drawing = true;
		
		EllipsePen.prototype._offset = $(sketchpad.container()).offset();
		var viewCoords = sketchpad.getViewCoordinates(e.pageX - EllipsePen.prototype._offset.left,
				e.pageY - EllipsePen.prototype._offset.top);
		
		EllipsePen.prototype._x = viewCoords.x;
		EllipsePen.prototype._y = viewCoords.y;
		
		EllipsePen.prototype._c = sketchpad.paper().ellipse(EllipsePen.prototype._x, EllipsePen.prototype._y, 0, 0);
		
		EllipsePen.prototype._c.attr({ 
			stroke: MousePen.prototype._color,
			"stroke-opacity": MousePen.prototype._opacity,
			"stroke-width": MousePen.prototype._width,
			"stroke-linecap": "round",
			"stroke-linejoin": "round"
		});
	};
	EllipsePen.prototype.finish = function(e, sketchpad) {
		if (!EllipsePen.prototype._drawing)
			return "";
		var path = null;
		
		if (EllipsePen.prototype._c !== null) {
			if (EllipsePen.prototype.isinvalid()) {
				EllipsePen.prototype._c.remove();
			} else {
				path = EllipsePen.prototype._c;
			}
		}
		
		EllipsePen.prototype.reset();
		
		return path;
	};
	EllipsePen.prototype.reset = function() {
		EllipsePen.prototype._drawing = false;
		EllipsePen.prototype._c = null;
		EllipsePen.prototype._x = 0;
		EllipsePen.prototype._y = 0;
		EllipsePen.prototype._rx = 0;
		EllipsePen.prototype._ry = 0;
	};
	EllipsePen.prototype.isinvalid = function() {
		return EllipsePen.prototype._rx === 0 && EllipsePen.prototype._ry === 0;
	};
	EllipsePen.prototype.move = function(e, sketchpad) {
		if (EllipsePen.prototype._drawing === true) {
			var viewCoords = sketchpad.getViewCoordinates(e.pageX - EllipsePen.prototype._offset.left,
					e.pageY - EllipsePen.prototype._offset.top);
			
			var x = viewCoords.x;
			var y = viewCoords.y;
			EllipsePen.prototype._rx = Math.abs(x - EllipsePen.prototype._x);
			EllipsePen.prototype._ry = Math.abs(y - EllipsePen.prototype._y);
			EllipsePen.prototype._c.attr({rx: EllipsePen.prototype._rx});
			EllipsePen.prototype._c.attr({ry: EllipsePen.prototype._ry});
		}
	};
	
	
	/**
	 * Default rectangle pen : RectPen
	 * @returns {RectPen}
	 */
	var RectPen = function () {
		MousePen.call(this);
	};
	RectPen.prototype = new MousePen();
	RectPen.prototype.constructor = RectPen;
	RectPen.prototype.start = function(e, sketchpad) {
		RectPen.prototype._drawing = true;
		
		RectPen.prototype._offset = $(sketchpad.container()).offset();
		
		var viewCoords = sketchpad.getViewCoordinates(e.pageX - RectPen.prototype._offset.left,
				e.pageY - RectPen.prototype._offset.top);
		
		RectPen.prototype._x = viewCoords.x;
		RectPen.prototype._y = viewCoords.y;
		
		RectPen.prototype._c = sketchpad.paper().rect(RectPen.prototype._x, RectPen.prototype._y, 0, 0, 0);
		
		RectPen.prototype._c.attr({ 
			stroke: MousePen.prototype._color,
			"stroke-opacity": MousePen.prototype._opacity,
			"stroke-width": MousePen.prototype._width,
			"stroke-linecap": "round",
			"stroke-linejoin": "round"
		});
	};
	RectPen.prototype.finish = function(e, sketchpad) {
		if (!RectPen.prototype._drawing)
			return "";
		var path = null;
		
		if (RectPen.prototype._c !== null) {
			if (RectPen.prototype.isinvalid()) {
				RectPen.prototype._c.remove();
			} else {
				path = RectPen.prototype._c;
			}
		}
		
		RectPen.prototype.reset();
		
		return path;
	};
	RectPen.prototype.reset = function() {
		RectPen.prototype._drawing = false;
		RectPen.prototype._c = null;
		RectPen.prototype._x = 0;
		RectPen.prototype._y = 0;
		RectPen.prototype._rectwidth = 0;
		RectPen.prototype._rectheight = 0;
		RectPen.prototype._r = 0;
	};
	RectPen.prototype.isinvalid = function() {
		var invalid = false;
		if (RectPen.prototype._rectwidth < 0 && RectPen.prototype._rectheight < 0)
			invalid = true;
		if (RectPen.prototype._r < 0)
			invalid = true;
		return invalid;
	};
	RectPen.prototype.move = function(e, sketchpad) {
		if (RectPen.prototype._drawing === true) {
			
			var viewCoords = sketchpad.getViewCoordinates(e.pageX - RectPen.prototype._offset.left,
					e.pageY - RectPen.prototype._offset.top);
			
			var x = viewCoords.x;
			var y = viewCoords.y;
			RectPen.prototype._rectwidth = Math.abs(x - RectPen.prototype._x);
			RectPen.prototype._rectheight = Math.abs(y - RectPen.prototype._y);
			if (x < RectPen.prototype._x)
				RectPen.prototype._c.attr({x: x});
			else
				RectPen.prototype._c.attr({x: RectPen.prototype._x});
			if (y < RectPen.prototype._y)
				RectPen.prototype._c.attr({y: y});
			else
				RectPen.prototype._c.attr({y: RectPen.prototype._y});
			RectPen.prototype._c.attr({width: RectPen.prototype._rectwidth});
			RectPen.prototype._c.attr({height: RectPen.prototype._rectheight});
		}
	};
	
	
	var TextPen = function () {
		RectPen.call(this);
	};
	TextPen.prototype = new RectPen();
	TextPen.prototype.constructor = TextPen;
	TextPen.prototype._text = "";
	TextPen.prototype.finish = function(e, sketchpad, callback) {
		if (!RectPen.prototype._drawing)
			return;
		var left = RectPen.prototype._offset.left + RectPen.prototype._c.attr("x");
		var top = RectPen.prototype._offset.top + RectPen.prototype._c.attr("y");
		var right = 0, bottom = 0;
        if (RectPen.prototype._rectwidth)
		    right = RectPen.prototype._offset.left + RectPen.prototype._c.attr("x") + RectPen.prototype._rectwidth;
        else
            right = RectPen.prototype._offset.left + RectPen.prototype._c.attr("x");
        if (RectPen.prototype._rectheight)
		    bottom = RectPen.prototype._offset.top + RectPen.prototype._c.attr("y") + RectPen.prototype._rectheight;
        else
            bottom = RectPen.prototype._offset.top + RectPen.prototype._c.attr("y");

		var offset_x = RectPen.prototype._offset.left;
		var offset_y = RectPen.prototype._offset.top;
		
		var path = RectPen.prototype.finish(e, sketchpad);
		path.remove();
		
        // Ext dependency!
        Ext.create('Ext.window.Window', {
            title : 'Enter Text',
            width : 300,
            height : 120,
            items : [{
                xtype : 'form',
                bodyPadding : 10,
                items : [{
                    xtype : 'textfield',
                    fieldLabel : 'Text',
                    name : 'text',
                    allowBlank : false
                }],
                buttons : [{
                    text : 'OK',
                    handler : function() {
                        var view = this.up().up();
                        var text = view.items.items[0].getValue();
                        view.up().close();
                        enterText((left + right)/2 - offset_x, (top + bottom)/2 - offset_y, text, callback);
                    }
                }]
            }]
        }).show();
		return;
    };
	
	function enterText(x, y, text, callback) {
		var path = sketchpad.paper().text(x, y, text);
		path.attr({ 
			stroke: MousePen.prototype._color,
			"stroke-opacity": MousePen.prototype._opacity,
			"stroke-width": MousePen.prototype._width/5,
			"font-size": MousePen.prototype._fontSize,
            "text-anchor" : "start",
			"stroke-linecap": "round",
			"stroke-linejoin": "round",
            "fill" : MousePen.prototype._color
		});
		callback(path);
	}
};

