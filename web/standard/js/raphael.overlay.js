/*
 * Raphael Overlay
 *  - Based on Raphael Sketchpad.
 * Following is a comment from Raphael Sketchpad
 * 
 * Modification to the Raphael Sketchpad 0.5.1
 *  - Adding different pens : Normal Pen, Rectangle Pen, Ellipse Pen, Circle Pen
 *  - Selection of the objects.
 *     - A bounding box is drawn to show the selection.
 *     - The box has a thick but less opaque border.
 *     - The box can be used to move the object.
 *     - Double clicking the text box will allow to edit the text.
 *     - (??) Cursors on the corners and edges can be added to allow resize the object.
 *  - Clicking on the paper, highlight the objects which will be selected if
 *    user clicks there.
 *    
 *  - Using scale.raphael for scaling  
 */

/**
 * We use this wrapper to control global variables.
 * The only global variable we expose is Raphael.sketchpad.
 */
(function(Raphael) {
	// Miscellaneous
	// -------------

	// Convert an SVG path into a string, so that it's smaller when JSONified.
	// This function is used by json().
	function svg_path_to_string(path) {
		if (typeof path != "object")
			return path;
		var str = "";
		for (var i = 0, n = path.length; i < n; i++) {
			var point = path[i];
			str += point[0] + point[1] + "," + point[2];
		}
		return str;
	}
	
	// Convert a string into an SVG path. This reverses the above code.
	function string_to_svg_path(str) {
		if (typeof str == "object")
			return str;
		var path = [];
		var tokens = str.split("L");
		
		if (tokens.length > 0) {
			var token = tokens[0].replace("M", "");
			var points = token.split(",");
			path.push(["M", parseInt(points[0], 10), parseInt(points[1], 10)]);
			
			for (var i = 1, n = tokens.length; i < n; i++) {
				token = tokens[i];
				points = token.split(",");
				path.push(["L", parseInt(points[0], 10), parseInt(points[1], 10)]);
			}
		}
		
		return path;
	}
	
	/**
	 * Function to create SketchPad object.
	 */
	Raphael.sketchpad = function(paper, options) {
		return new SketchPad(paper, options);
	};
	
	// Current version.
	Raphael.sketchpad.VERSION = '0.5.1';
	
	/**
	 * The Sketchpad object.
	 */
	var SketchPad = function (paper, options) {
		// Use self to reduce confusion about this.
		var self = this;
		
		var _options = {
			width: 100,
			height: 100,
			strokes: [],
			mode: true
		};
		jQuery.extend(_options, options);
		
		// The Raphael context to draw on.
		var _paper_x = null;

		var _paper_y = null;

		var _paper_width = null;

		var _paper_height = null;

		var _paper;
		if (paper.raphael && paper.raphael.constructor == Raphael.constructor) {
			_paper = paper;
		} else if (typeof paper == "string") {
			_paper = ScaleRaphael(paper, _options.width, _options.height);
			_paper_x = 0;
			_paper_y = 0;
			_paper_width = _options.width;
			_paper_height = _options.height;
		} else {
			throw "first argument must be a Raphael object, an element ID, an array with 3 elements";
		}
		
		// Miscellaneous
		// -------------

		function _redraw_strokes() {
			if (_selection !== undefined) {
				_selection.clear(); // this is done because when we clear paper(), selection is also removed.
			}
			_paper.clear();
			_elements = [];
			
			for (var i = 0, n = _strokes.length; i < n; i++) {
				var stroke = _strokes[i];
				var type = stroke.type;
                                if (stroke.type === "path" && stroke.path !== undefined && typeof stroke.path != "object") {
                                        stroke.path = string_to_svg_path(stroke.path);
				}
				var _ele = _paper[type]().attr(stroke);
				
				for (var a in _ele.attr()) {
					stroke[a] = _ele.attr(a);
				}
				_ele.data("overlay_id", stroke.overlay_id);
				if (stroke.custom !== undefined) {
					_ele.data("custom", stroke.custom);
				}
				_elements.push(_ele);
			}
		}
		
		// Setup
		//--------
		
		var _action_history = new ActionHistory(self);
		
		// Path data
		var _elements = [];
		/*
		 * _viewbox is an array containing two objects.
		 * _viewbox[0] is the scale which is ["scale", sx, sy]
		 * _viewbox[1] is the transform which is ["translate", dx, dy]
		 * every stroke on the current paper, should be a transform with the above 2 operations applied sequenctially.
		 * when a new stroke is added, apply reverse transform to it and then store in the history
		 * when getting strokes from the history, apply this transform to them
		 */
		var _viewbox = [];
		var _strokes = _options.strokes;
		if (jQuery.isArray(_strokes) && _strokes.length > 0) {
			_action_history.add({
				type: "init",
				strokes: jQuery.merge([], _strokes)	// Make a clone.
			});
			_redraw_strokes();
		} else {
			_strokes = [];
			_redraw_strokes();
		}
		
		var _selection = new Selection(self, _fire_change, svg_transform_to_std, _action_history);

		// The Raphael SVG canvas.
		var _canvas = _paper.canvas;

		// The HTML element that contains the canvas.
		var _container = $(_canvas).parent();

		// The default pen.
		var _pen = new Pen().pen("pencil");
		var _selection_pen = new Pen().pen("selection");
		
		var _is_dirty = false;
		
		function _init_parameters(width, height) {
			_paper_x = 0;
			_paper_y = 0;
			_paper_width = width;
			_paper_height = height;
			_is_dirty = false;

			_action_history.reset();

			while (_elements.length > 0) {
				_elements.pop();
			}
			while (_strokes.length > 0) {
				_strokes.pop();
			}

			_redraw_strokes();
			_selection.clear();
		}

		// Global variable
		// ---------------
		var overlay_id = 0;

		// Public Methods
		//-----------------
		
		self.isDirty = function() {
			return _is_dirty;
		};

		self.setDirty = function(dirty) {
			_is_dirty = dirty;
		};

		/*
		 * represent this method as applying the transforms on the current _viewbox
		 * and then change the _viewbox variable
		 */
		self.setViewBox = function(x, y, width, height) {
			// since this is with respect to default
			_action_history.viewBoxShift("");
			var cx = 0;
			var cy = 0;
			var cw = _paper_width;
			var ch = _paper_height;

			var dx = - (x + (width - _paper_width)/2);
			var dy = - (y + (height - _paper_height)/2);
			var scale = Math.min(_paper_width / width, _paper_height / height);
			_action_history.viewBoxShift("translate", dx, dy);
			_action_history.viewBoxShift("scale", scale, scale);
			_strokes = _action_history.current_strokes();
			_redraw_strokes();
            self.prevBox = {x:x, y:y, width:width, height:height};
		};

		// only do a setSize for a clean slate.
		self.setSize = function(width, height) {
			_init_parameters(width, height);
			_paper.setSize(width, height);
			self.setViewBox(0, 0, width, height);
		};

		self.test = function(x) {
			self.purge();
			self.setSize(400, 300);
			self.setViewBox(0, 0, 800, 600);
			self.json(x);
		};

		self.getSize = function() {
			return {
				width: _paper_width,
				height: _paper_height
			};
		};

		// leaving the pen unchanged
		self.purge = function() {
			_action_history.reset();
			_init_parameters(_paper_width, _paper_height);
            if (self.prevBox && self.prevBox !== null) {
                self.setViewBox(self.prevBox.x, self.prevBox.y, self.prevBox.width, self.prevBox.height);
            }
		};

                self.erase = function() {
                        _selection.erase();
                        return self;
                };

                self.width = function(value) {
                        if (value === undefined) {
                                return _pen.width();
                        }
                        _pen.width(value);
                        _selection.width(value);
                        return self;
                };

                self.color = function(value) {
                        if (value === undefined) {
                                return _pen.color();
                        }
                        _pen.color(value);
			_selection.color(value);
                        return self;
                };

                self.opacity = function(value) {
                        if (value === undefined) {
                                return _pen.opacity();
                        }
                        _pen.opacity(value);
                        _selection.opacity(value);
                        return self;
                };

                self.fontSize = function(value) {
                        if (value === undefined) {
                                return _pen.fontSize();
                        }
                        _pen.fontSize(value);
                        _selection.fontSize(value);
                        return self;
                };

		self.paper = function() {
			return _paper;
		};
		
		self.canvas = function() {
			return _canvas;
		};
		
		self.container = function() {
			return _container;
		};

                self.reset_strokes = function(draw) {
			while (_strokes.length > 0) {
				_strokes.pop();
			}
			_strokes = jQuery.merge(_strokes, _action_history.current_strokes("force"));
			if (draw === "redraw") {
				_redraw_strokes();
			}
                };
		
		self.zoom = function(factor) {
			if (factor === undefined) {
				return self;
			}
			if (typeof factor != "number" || factor <= 0) {
				throw "Zoom factor should be a positive number.";
			}
			//var _width = _view_width * (1/factor);
			//var _height = _view_height * (1/factor);
			//var _x = _view_x + (_view_width - _width)/2;
			//var _y = _view_y + (_view_height - _height)/2;
			
			//self.setViewBox(_x, _y, _width, _height);
			_action_history.viewBoxShift("scale", factor, factor);
                        _strokes = _action_history.current_strokes();
			_redraw_strokes();
			return self;	// function-chaining
		};

		self.pen = function(value, options) {
			if (value === undefined) {
				return _pen;
			}
			_pen.pen(value, options);
			return self; // function-chaining
		};
		
                function do_scale(attrtype, attr, sx, sy, cx, cy) {
			switch (attrtype) {
			case "path":
				var path = attr.path;
				var type = typeof attr.path;
				if (type == "string")
					path = string_to_svg_path(path);
				
				var str = "";
				for (var i = 0, n = path.length; i < n; i++) {
					var point = path[i];
                                        var px = Math.round(parseFloat(point[1]) - (cx - parseFloat(point[1]))*(sx - 1));
                                        var py = Math.round(parseFloat(point[2]) - (cy - parseFloat(point[2]))*(sy - 1));
					str += point[0] + px + "," + py;
				}
				attr.path = str;
				break;
			case "circle":
                                /* no implementation for circle */
                                break;
			case "ellipse":
                                /* scaled from the center */
                                attr.rx *= sx;
                                attr.ry *= sy;
				attr.rx = Math.round(attr.rx);
				attr.ry = Math.round(attr.ry);
				break;
			case "rect":
                                /* scaled from the center */
                                attr.x -= (attr.width/2) * (sx - 1);
                                attr.y -= (attr.height/2) * (sy - 1);
                                attr.width *= sx;
                                attr.height *= sy;
				attr.x = Math.round(attr.x);
				attr.y = Math.round(attr.y);
				attr.width = Math.round(attr.width);
				attr.height = Math.round(attr.height);
                                break;
			case "text":
                                /* scaled from the center */
				attr["font-size"] *= (sx > sy ? sy : sx);
				attr["font-size"] = Math.round(attr["font-size"]);
				break;
            default:
                break;
			}
                        return attr;
                }
		
		function do_transform(attrtype, attr, dx, dy) {
			switch (attrtype) {
			case "path":
				var path = attr.path;
				var type = typeof attr.path;
				if (type == "string")
					path = string_to_svg_path(path);
				
				var str = "";
				for (var i = 0, n = path.length; i < n; i++) {
					var point = path[i];
					str += point[0] + (parseFloat(point[1]) + dx) + "," + (parseFloat(point[2]) + dy);
				}
				attr.path = str;
				break;
			case "circle":
			case "ellipse":
				attr.cx = attr.cx + dx;
				attr.cy = attr.cy + dy;
				break;
			case "rect":
			case "text":
				attr.x = attr.x + dx;
				attr.y = attr.y + dy;
				break;
            default:
                break;
			}
			return attr;
		}
		
		function svg_transform_to_std(type, attr) {
			if (! (attr.transform === undefined)) {
				var transform = attr.transform;
				
				if (jQuery.isArray(transform) && transform.length > 0) {
					var tran = null;
					var dx = null;
					var dy = null;
					var sx = null;
					var sy = null;
					var cx = null;
					var cy = null;
					for (var i = 0, n = transform.length; i < n; i++) {
						tran = transform[i];
                                                switch(tran[0]) {
                                                case "t":
							dx = tran[1];
							dy = tran[2];
							attr = do_transform(type, attr, dx, dy);
                                                        break;
                                                case "s":
                                                        sx = tran[1];
                                                        sy = tran[2];
                                                        cx = tran[3] + (dx === null ? 0 : dx);
                                                        cy = tran[4] + (dy === null ? 0 : dy);
                                                        attr = do_scale(type, attr, sx, sy, cx, cy);
                                                        break;
                                                default:
                                                    break;
                                                }
					}
					delete attr.transform;
				}
			}
			
			return attr;
		}
		
		self.json = function(value, append) {
			if (value === undefined) {
				var _orig_strokes = _action_history.current_strokes("original");
				console.log(_orig_strokes);
				for (var i = 0, n = _orig_strokes.length; i < n; i++) {
					var stroke = _orig_strokes[i];
					delete stroke.overlay_id;
					console.log(stroke.type);
					if (stroke.type === "rect") {
						delete stroke.path;
					}
					if (typeof stroke.path == "object") {
						stroke.path = svg_path_to_string(stroke.path);
					}
				}
				return JSON.stringify(_orig_strokes);
			}
			return self.strokes(JSON.parse(value), append);
		};
		
		/**
		 * This method always appends
		 */
		self.strokes = function(value, append) {
			if (value === undefined) {
				return _strokes;
			}
			if (jQuery.isArray(value)) {
				var _old_strokes = _strokes;
				_strokes = value;
				
				for (var i = 0, n = _strokes.length; i < n; i++) {
					var stroke = _strokes[i];
					if (typeof stroke.path == "string") {
						stroke.path = string_to_svg_path(stroke.path);
					}
					stroke.overlay_id = overlay_id;
					overlay_id += 1;
				}
				
				_action_history.add({
					type: "batch",
					strokes: jQuery.merge([], _strokes) // Make a copy.
				});_action_history
				//if (append) {
				//	jQuery.merge(_strokes, _old_strokes);
				//}
				_strokes = _action_history.current_strokes();
				_redraw_strokes();
				_fire_change(false);
			}
			return self; // function-chaining
		};

		self.elements = function() {
			return _elements;
		};
		
		self.freeze_history = function() {
			_action_history.freeze();
		};
		
		self.undoable = function() {
			return _action_history.undoable();
		};

		self.undo = function() {
			if (_action_history.undoable()) {
				_action_history.undo();
				_strokes = _action_history.current_strokes();
				_redraw_strokes();
				_fire_change();
			}
			return self; // function-chaining
		};

		self.redoable = function() {
			return _action_history.redoable();
		};

		self.redo = function() {
			if (_action_history.redoable()) {
				_action_history.redo();
				_strokes = _action_history.current_strokes();
				_redraw_strokes();
				_fire_change();
			}
			return self; // function-chaining
		};
		
		self.clear = function() {
			_action_history.add({
				type: "clear"
			});
			
			_strokes = [];
			_redraw_strokes();
			_fire_change();
			
			return self; // function-chaining
		};
		
		self.animate = function(ms) {
			if (ms === undefined) {
				ms = 500;
			}
			
			if (_selection !== undefined) {
				_selection.clear(); // this is done because when we clear paper(), selection is also removed.
			}
			_paper.clear();
			
			if (_strokes.length > 0) {
				var i = 0;

				function animate() {
					var stroke = _strokes[i];
					var type = stroke.type;
					var _ele = _paper[type]().attr(stroke);
					_ele.data("overlay_id", stroke.overlay_id);
					if (stroke.custom !== undefined) {
						_ele.data("custom", stroke.custom);
					}
					_elements.push(_ele);

					i++;
					if (i < _strokes.length) {
						setTimeout(animate, ms);
					}
				}

				animate();
			}
			
			return self; // function-chaining
		};
		
		self.mode = function(mode) {
			if (mode === undefined) {
				return _options.mode;
			}
			
			_options.mode = mode;
			if (_options.mode != "select")
				_selection.clear();
			if (_options.mode) {
				if (_options.mode == "select") {
					_deregister_mouse_events("crosshair", _pan_mousedown, 
							_pan_mousemove, _pan_mouseup, 
							_pan_touchstart, _pan_touchmove, 
							_pan_touchend);
					_deregister_mouse_events("crosshair", _mousedown, _mousemove,
							_mouseup, _touchstart, _touchmove, _touchend);
					_register_mouse_events("crosshair", _selection_mousedown, 
							_selection_mousemove, _selection_mouseup, 
							_selection_touchstart, _selection_touchmove, 
							_selection_touchend);
				} else if (_options.mode == "pan") {
					_deregister_mouse_events("crosshair", _selection_mousedown, 
							_selection_mousemove, _selection_mouseup, 
							_selection_touchstart, _selection_touchmove, 
							_selection_touchend);
					_deregister_mouse_events("crosshair", _mousedown, _mousemove,
							_mouseup, _touchstart, _touchmove, _touchend);
					_register_mouse_events("crosshair", _pan_mousedown, 
							_pan_mousemove, _pan_mouseup, 
							_pan_touchstart, _pan_touchmove, 
							_pan_touchend);
				} else {
					_deregister_mouse_events("crosshair", _pan_mousedown, 
							_pan_mousemove, _pan_mouseup, 
							_pan_touchstart, _pan_touchmove, 
							_pan_touchend);
					_deregister_mouse_events("crosshair", _selection_mousedown, 
							_selection_mousemove, _selection_mouseup, 
							_selection_touchstart, _selection_touchmove, 
							_selection_touchend);
					_register_mouse_events("crosshair", _mousedown, _mousemove,
							_mouseup, _touchstart, _touchmove, _touchend);
				}
			} else {
				_deregister_mouse_events("crosshair", _pan_mousedown, 
						_pan_mousemove, _pan_mouseup, 
						_pan_touchstart, _pan_touchmove, 
						_pan_touchend);
				_deregister_mouse_events("crosshair", _selection_mousedown, 
						_selection_mousemove, _selection_mouseup, 
						_selection_touchstart, _selection_touchmove, 
						_selection_touchend);
				_deregister_mouse_events("crosshair", _mousedown, _mousemove,
						_mouseup, _touchstart, _touchmove, _touchend);
			}
			
			return self; // function-chaining
		};

		function _register_mouse_events(cursor, mdown, mmove, mup, tstart, tmove, tend) {
			$(_container).css("cursor", cursor);

			$(_container).mousedown(mdown);
			$(_container).mousemove(mmove);
			$(_container).mouseup(mup);

			// Handle the case when the mouse is released outside the canvas.
			$(document).mousemove(mmove);
			$(document).mouseup(mup);

			// iPhone Events
			var agent = navigator.userAgent;
			if (agent.indexOf("iPhone") > 0 || agent.indexOf("iPod") > 0) {
				$(_container).bind("touchstart", tstart);
				$(_container).bind("touchmove", tmove);
				$(_container).bind("touchend", tend);
			}
		}
		
		function _deregister_mouse_events(cursor, mdown, mmove, mup, tstart, tmove, tend) {
			$(_container).css("cursor", cursor);
			$(_container).unbind("mousedown", mdown);
			$(_container).unbind("mousemove", mmove);
			$(_container).unbind("mouseup", mup);
			
			$(document).unbind("mousemove", mmove);
			$(document).unbind("mouseup", mup);

			// iPhone Events
			var agent = navigator.userAgent;
			if (agent.indexOf("iPhone") > 0 || agent.indexOf("iPod") > 0) {
				$(_container).unbind("touchstart", tstart);
				$(_container).unbind("touchmove", tmove);
				$(_container).unbind("touchend", tend);
			}
		}
		
		// Change events
		//----------------
		
		var _change_fn = function() {};
		self.change = function(fn) {
			if (fn === null || fn === undefined) {
				_change_fn = function() {};
			} else if (typeof fn == "function") {
				_change_fn = fn;
			}
		};
		
		function _fire_change(isdirty) {
			if (isdirty === undefined)
				_is_dirty = true;
			else
				_is_dirty = isdirty;
			_change_fn();
		}
		
		// Miscellaneous methods
		//------------------
		
		function _disable_user_select() {
			$("*").css("-webkit-user-select", "none");
			$("*").css("-moz-user-select", "none");
			if (jQuery.browser.msie) {
				$("body").attr("onselectstart", "return false;");
			}
		}
		
		function _enable_user_select() {
			$("*").css("-webkit-user-select", "text");
			$("*").css("-moz-user-select", "text");
			if (jQuery.browser.msie) {
				$("body").removeAttr("onselectstart");
			}
		}
		
		function _find_intersection(x, y, width, height) {
			var _intersect = [];
			var s = null;
                        var rect_points = {
                                0: [x, y],
                                1: [x+width, y],
                                2: [x+width, y+height],
                                3: [x, y+height]
                        };
			for (var i=0, n=_strokes.length; i<n; i++) {
				s = _strokes[i];
                var shape;
				switch(s.type) {
				case "path":
					var path = s.path;
					var _x1 = null;
					var _y1 = null;
					var _x2 = null;
					var _y2 = null;
					if (typeof path == "string")
						path = string_to_svg_path(path);
					if (path.length === 0)
                                                break;

                                        var point = path[0];
                                        _x1 = parseFloat(point[1]);
                                        _y1 = parseFloat(point[2]);
                                        if (_x1 >= x && _x1 <= x+width && _y1 >= y && _y1 <= y+height) {
                                                _intersect.push(s);
                                                break;
                                        }
                                        for (var j = 1, m = path.length; j < m; j++) {
						point = path[j];
						_x2 = parseFloat(point[1]);
						_y2 = parseFloat(point[2]);
						if (_x2 >= x && _x2 <= x+width && _y2 >= y && _y2 <= y+height) {
							_intersect.push(s);
							break;
						}
                                                
                                                if (_test_intersect("path", rect_points, [[_x1, _y1], [_x2, _y2]])) {
                                                        _intersect.push(s);
                                                        break;
                                                }
                                                _x1 = _x2;
                                                _y1 = _y2;
					}
					break;
				case "text":
                                        shape = _paper.text(s.x, s.y, s.text);
                                        if (_test_intersect("text", rect_points, shape.getBBox())) {
                                                _intersect.push(s);
                                        }
                                        shape.remove();
					break;
				case "circle":
                                        shape = _paper.circle(s.cx, s.cy, s.r);
                                        if (_test_intersect("circle", rect_points, shape.getBBox())) {
                                                _intersect.push(s);
                                        }
                                        shape.remove();
					break;
				case "ellipse":
                                        shape = _paper.ellipse(s.cx, s.cy, s.rx, s.ry);
                                        if (_test_intersect("ellipse", rect_points, shape.getBBox())) {
                                                _intersect.push(s);
                                        }
                                        shape.remove();
					break;
				case "rect":
                                        shape = _paper.rect(s.x, s.y, s.width, s.height);
                                        if (_test_intersect("rect", rect_points, shape.getBBox())) {
                                                _intersect.push(s);
                                        }
                                        shape.remove();
					break;
                default:
                    break;
				}
			}
			return _intersect;
		}

                function _test_intersect(type, rect_points, loc) {
                        var c, d, i;
                        switch(type) {
                        case "circle":
                        case "ellipse":
                        case "rect":
                        case "text":
                                for (i=0; i<4; i++) {
                                        c = rect_points[i];
                                        if (c[0] >= loc.x && c[0] <= loc.x+loc.width &&
                                                c[1] >= loc.y && c[1] <= loc.y+loc.height)
                                                return true;
                                }
                                var orig_rect_points = {
                                        0: [loc.x, loc.y],
                                        1: [loc.x+loc.width, loc.y],
                                        2: [loc.x+loc.width, loc.y+loc.height],
                                        3: [loc.x, loc.y+loc.height]
                                };

                                for (i=0; i<4; i++) {
                                        c = orig_rect_points[i];
                                        if (c[0] >= rect_points[0][0] && c[0] <= rect_points[1][0] &&
                                                c[1] >= rect_points[0][1] && c[1] <= rect_points[3][1])
                                                return true;
                                }

                                if (   (   rect_points[0][0] <= orig_rect_points[0][0] &&
                                           rect_points[1][0] >= orig_rect_points[1][0] &&
                                           rect_points[0][1] >= orig_rect_points[0][1] &&
                                           rect_points[3][1] <= orig_rect_points[3][1]) ||
                                       (   rect_points[0][0] >= orig_rect_points[0][0] &&
                                           rect_points[1][0] <= orig_rect_points[1][0] &&
                                           rect_points[0][1] <= orig_rect_points[0][1] &&
                                           rect_points[3][1] >= orig_rect_points[3][1]))
                                        return true;
                                break;
                        case "path":
                                var a = loc[0];
                                var b = loc[1];
                                var alpha1 = (b[1]-a[1])/(b[0]-a[0]);
                                var beta1 = a[1] - alpha1*a[0];
                                var alpha2 = null;
                                var beta2 = null;
                                var x, y;
                                var test1, test2;
                                for (i=0; i<4; i++) {
                                        c = rect_points[i%4];
                                        d = rect_points[(i+1)%4];

                                        if (d[0] == c[0]) {
                                                x = c[0];
                                                y = alpha1 * x + beta1;
                                        } else {
                                                alpha2 = (d[1]-c[1])/(d[0]-c[0]);
                                                beta2 = c[1] - alpha2*c[0];
                                                if (a[0] == b[0]) {
                                                        x = a[0];
                                                        y = alpha2 * x + beta2;
                                                } else if (alpha1 != alpha2) {
                                                        x = (beta1 - beta2)/(alpha2 - alpha1);
                                                        y = alpha1 * x + beta1;
                                                }
                                        }

                                        if (x !== null && y !== null) {
                                                test1 = ((x-a[0])*(x-b[0]) <= 0) && ((y-a[1])*(y-b[1]) <= 0);
                                                test2 = ((x-c[0])*(x-d[0]) <= 0) && ((y-c[1])*(y-d[1]) <= 0);
                                                if (test1 && test2)
                                                        return true;
                                        }
                                        x = null;
                                        y = null;
                                }
                                break;
                        default:
                            break;
                        }
                        return false;
                }
		
		// Event handlers
		//-----------------
		// We can only attach events to the container, so do it.
		
		function _mousedown(e) {
			_disable_user_select();

			_pen.start(e, self);
		}

		function _mousemove(e) {
			_pen.move(e, self);
		}

		function _mouseup(e) {
			_enable_user_select();
			
			var path = _pen.finish(e, self, createPath);
			
			createPath(path);
		}
		
		function createPath(paths) {
			if (paths !== null && paths !== undefined && paths !== "") {
				// Add event when clicked.
				// Save the stroke.
				
				if(paths.length>0){
					for(var i=0;i<paths.length;i++){
						var path=paths[i];
						var stroke = path.attr();
						stroke.type = path.type;
				
						_strokes.push(stroke);
				
						stroke.overlay_id = overlay_id;
						if (stroke.custom !== undefined) {
							path.data("custom", stroke.custom);
						}
						path.data("overlay_id", overlay_id);
						overlay_id += 1;
				
						_elements.push(path);
				
						_action_history.add({
							type: "stroke",
							stroke: stroke
						});
				
						_fire_change();
					}
				}
				else {
					var path=paths;
					var stroke = path.attr();
					stroke.type = path.type;
				
					_strokes.push(stroke);
				
					stroke.overlay_id = overlay_id;
					if (stroke.custom !== undefined) {
						path.data("custom", stroke.custom);
					}
					path.data("overlay_id", overlay_id);
					overlay_id += 1;
				
					_elements.push(path);
				
					_action_history.add({
						type: "stroke",
						stroke: stroke
					});
				
					_fire_change();
				}
			}
		}
		
		function _touchstart(e) {
			e = e.originalEvent;
			e.preventDefault();

			if (e.touches.length == 1) {
				var touch = e.touches[0];
				_mousedown(touch);
			}
		}
		
		function _touchmove(e) {
			e = e.originalEvent;
			e.preventDefault();

			if (e.touches.length == 1) {
				var touch = e.touches[0];
				_mousemove(touch);
			}
		}
		
		function _touchend(e) {
			e = e.originalEvent;
			e.preventDefault();

			_mouseup(e);
		}

		function _selection_mousedown(e) {
			if (!_selection.is_moving()) {
				_disable_user_select();
				
				if (!e.ctrlKey)
                                        _selection.clear();
                                        
                                _selection_pen.start(e, self);
			}
		}

		function _selection_mousemove(e) {
			if (!_selection.is_moving()) {
				_selection_pen.move(e, self);
			}
		}

		function _selection_mouseup(e) {
			if (!_selection.is_moving()) {
				_enable_user_select();
				
				var rect = _selection_pen.finish(e, self, createPath);
				if (rect === undefined || rect === "")
					return;
				var _attr = rect.attr();
				var _intersect = _find_intersection(_attr.x, _attr.y, _attr.width, _attr.height);
				rect.remove();
				
				_intersect = _get_elements_from_strokes(_intersect);
				_selection.select(_intersect);
			}
		}
		
		function _get_elements_from_strokes(_intersect){
			var temp = [];
			for (var i=0, n=_elements.length; i<n; i++) {
				var _a = _elements[i].attr();
				_a.type = _elements[i].type;
				_a.overlay_id = _elements[i].data("overlay_id");
				if (_elements[i].data("custom") !== undefined)
					_a.custom = _elements[i].data("custom");
                                var path_type_object = (typeof _a.path == "object");
				for (var j=0, m=_intersect.length; j<m; j++) {
					if (_intersect[j].type === "path") {
						if (path_type_object) {
							if (_intersect[j].path !== undefined)
								_intersect[j].path = string_to_svg_path(_intersect[j].path);
						} else {
							if (_intersect[j].path !== undefined)
								_intersect[j].path = svg_path_to_string(_intersect[j].path);
						}
					}
                                        if (_intersect[j].overlay_id == _a.overlay_id)
						temp.push(_elements[i]);
				}
			}
			return temp;
		}
		
		function _selection_touchstart(e) {
			if (!_selection.is_moving()) {
				e = e.originalEvent;
				e.preventDefault();
				
				if (e.touches.length == 1) {
					var touch = e.touches[0];
					_selection_mousedown(touch);
				}
			}
		}
		
		function _selection_touchmove(e) {
			if (!_selection.is_moving()) {
				e = e.originalEvent;
				e.preventDefault();
				
				if (e.touches.length == 1) {
					var touch = e.touches[0];
					_selection_mousemove(touch);
				}
			}
		}
		
		function _selection_touchend(e) {
			if (!_selection.is_moving()) {
				e = e.originalEvent;
				e.preventDefault();
				
				_selection_mouseup(e);
			}
		}

		var _panning = false;
		var _panning_startX = null;
		var _panning_startY = null;
		var _panning_X = null;
		var _panning_Y = null;

		function _pan_mousedown(e) {
			_disable_user_select();
			_panning = true;
			_panning_startX = e.pageX;
			_panning_startY = e.pageY;
		}

		function _pan_mousemove(e) {
			if (!_panning) {
				return;
			}
			_panning_X = e.pageX;
			_panning_Y = e.pageY;

			var dx = _panning_X - _panning_startX;
			var dy = _panning_Y - _panning_startY;
			for (var i in _elements) {
				var _ele = _elements[i];
				_ele.transform("");
				_ele.translate(dx, dy);
			}
		}

		function _pan_mouseup(e) {
			_enable_user_select();
			if (!_panning) {
				return;
			}
			_panning = false;
			_panning_X = e.pageX;
			_panning_Y = e.pageY;

			var dx = _panning_X - _panning_startX;
			var dy = _panning_Y - _panning_startY;
			_action_history.viewBoxShift("translate", dx, dy);
                        _strokes = _action_history.current_strokes();
                        _redraw_strokes();
			
			_panning_X = null;
			_panning_Y = null;
			_panning_startX = null;
			_panning_startY = null;
		}

		function _pan_touchstart(e) {
			e = e.originalEvent;
			e.preventDefault();
			
			if (e.touches.length == 1) {
				var touch = e.touches[0];
				_pan_mousedown(touch);
			}
		}
		
		function _pan_touchmove(e) {
			e = e.originalEvent;
			e.preventDefault();
			
			if (e.touches.length == 1) {
				var touch = e.touches[0];
				_pan_mousemove(touch);
			}
		}
		
		function _pan_touchend(e) {
			e = e.originalEvent;
			e.preventDefault();
			
			_pan_mouseup(e);
		}
		
		self.mode(_options.mode);
	};
		
	var ActionHistory = function(spad) {
		var self = this;
		
		var _history = [];

		var _vb_history = [];
		
		// Index of the last state.
		var _current_state = -1;
		
		// Index of the freeze state.
		// The freeze state is the state where actions cannot be undone.
		var _freeze_state = -1;
		
		// The current set of strokes if strokes were to be rebuilt from history.
		// Set to null to force refresh.
		var _current_strokes = null;
		
		self.reset = function() {
			_history = [];
			_vb_history = [];
			_current_state = -1;
			_freeze_state = -1;
			_current_strokes = null;
		};
		
		self.add = function(action) {
			if (_current_state + 1 < _history.length) {
				_history.splice(_current_state + 1, _history.length - (_current_state + 1));
			}
			
			if (action.type !== "batch") {
				if (action.strokes !== undefined) {
					action.strokes = untransform(action.strokes);
				}
				if (action.stroke !== undefined) {
					action.stroke = untransform(action.stroke);
				}
				if (action.old_stroke !== undefined) {
					action.old_stroke = untransform(action.old_stroke);
				}
			}
			_history.push(action);
			_current_state = _history.length - 1;
			
			// Reset current strokes.
			_current_strokes = null;
		};
		
		self.freeze = function(index) {
			if (index === undefined) {
				_freeze_state = _current_state;
			} else {
				_freeze_state = index;
			}
		};
		
		self.undoable = function() {
			return (_current_state > -1 && _current_state > _freeze_state);
		};
		
		self.undo = function() {
			if (self.undoable()) {
				_current_state--;
				
				// Reset current strokes.
				_current_strokes = null;
			}
		};
		
		self.redoable = function() {
			return _current_state < _history.length - 1;
		};
		
		self.redo = function() {
			if (self.redoable()) {
				_current_state++;
				
				// Reset current strokes.
				_current_strokes = null;
			}
		};

		self.viewBoxShift = function(shift, arg_x, arg_y) {
			if (shift === "") {
				_vb_history = [];
				return;
			}
                        if (shift === "translate") {
                                _vb_history.push({
                                        type: shift,
                                        dx: arg_x,
                                        dy: arg_y
                                });
                        } else if (shift === "scale") {
                                _vb_history.push({
                                        type: shift,
                                        sx: arg_x,
                                        sy: arg_y
                                });
                        }
		};

		// Rebuild the strokes from history.
		self.current_strokes = function(force) {
			if (force === "force") {
				_current_strokes = null;
			}
			var _old_strokes, si, s, stroke;
			if (_current_strokes === null) {
				var strokes = [];
				for (var i = 0; i <= _current_state; i++) {
					var action = _history[i];
					switch(action.type) {
						case "init":
						case "json":
						case "strokes":
						case "batch":
							jQuery.merge(strokes, action.strokes);
							break;
						case "stroke":
							strokes.push(action.stroke);
							break;
						case "transform":
							_old_strokes = action.old_stroke;
                                                        var _new_strokes = action.stroke;
                                                        if (!jQuery.isArray(_old_strokes))
                                                                _old_strokes = [_old_strokes];
                                                        if (!jQuery.isArray(_new_strokes))
                                                                _new_strokes = [_new_strokes];

                                                        for (si = 0; si < _old_strokes.length; si++) {
                                                                for (s = 0, n = strokes.length; s < n; s++) {
                                                                        stroke = strokes[s];
                                                                        if (stroke.type === "path" && stroke !== undefined && stroke !== null && _old_strokes[si] !== undefined && _old_strokes[si] !== null && stroke.path !== undefined && _old_strokes[si].path !== undefined && stroke.path !== null && _old_strokes[si].path !== null) {
                                                                                if (typeof _old_strokes[si].path == "object")
                                                                                        stroke.path = string_to_svg_path(stroke.path);
                                                                                else
                                                                                        stroke.path = svg_path_to_string(stroke.path);
                                                                        }
                                                                        if (stroke.overlay_id == _old_strokes[si].overlay_id) {
                                                                                strokes.splice(s, 1);
                                                                                break;
                                                                        }
                                                                }
                                                                strokes.push(_new_strokes[si]);
                                                        }
							break;
						case "erase":
							_old_strokes = action.stroke;
                                                        if (!jQuery.isArray(_old_strokes))
                                                                _old_strokes = [_old_strokes];

                                                        for (si = 0; si < _old_strokes.length; si++) {
                                                                for (s = 0, n = strokes.length; s < n; s++) {
                                                                        stroke = strokes[s];
                                                                        if (stroke.type === "path" && stroke.path !== undefined && _old_strokes[si].path !== undefined) {
                                                                                if (typeof _old_strokes[si].path == "object")
                                                                                        stroke.path = string_to_svg_path(stroke.path);
                                                                                else
                                                                                        stroke.path = svg_path_to_string(stroke.path);
                                                                        }
                                                                        if (stroke.overlay_id == _old_strokes[si].overlay_id) {
                                                                                strokes.splice(s, 1);
                                                                                break;
                                                                        }
                                                                }
                                                        }
							break;
						case "clear":
							strokes = [];
							break;
                        default:
                            break;
					}
				}
				
				_current_strokes = strokes;
			}
			if (force === "original") {
				return _current_strokes;
			}
			return transform(_current_strokes);
		};

		function clone(stroke, type) {
			var s = {};
			for (var i in stroke) {
				if (i === "path") {
					var x = string_to_svg_path(svg_path_to_string(stroke[i]));
					s[i] = x;
					continue;
				}
				s[i] = stroke[i];
			}
			return s;
		}

		function scale_point(val, ref, scale) {
			val -= ref;
			val *= scale;
			val += ref;
			return val;
		}

		function transform(strokes) {
			if (_vb_history.length === 0) {
				return strokes;
			}
			var i, dx, dy, sx, sy;
			if (jQuery.isArray(strokes)) {
				var t_strokes = [];
				for (i = 0; i < strokes.length; i++) {
					t_strokes.push(transform(strokes[i]));
				}
				return t_strokes;
			}
			// strokes is a object
			var t_stroke = clone(strokes, "object");
                        for (i = 0; i < _vb_history.length; i++) {
                                if (_vb_history[i].type === "translate") {
                                        dx = _vb_history[i].dx;
                                        dy = _vb_history[i].dy;

                                        if (t_stroke.cx !== undefined) {
                                                t_stroke.cx += dx;
                                        }
                                        if (t_stroke.cy !== undefined) {
                                                t_stroke.cy += dy;
                                        }
                                        if (t_stroke.x !== undefined) {
                                                t_stroke.x += dx;
                                        }
                                        if (t_stroke.y !== undefined) {
                                                t_stroke.y += dy;
                                        }
                                        if (t_stroke.path !== undefined) {
                                                t_stroke.path = string_to_svg_path(t_stroke.path);
                                                var path = t_stroke.path;
                                                for (var j = 0; j < path.length; j++) {
                                                        if (path[j][1] !== undefined) {
                                                                path[j][1] = parseFloat(path[j][1], 10) + dx;
                                                        }
                                                        if (path[j][2] !== undefined) {
                                                                path[j][2] = parseFloat(path[j][2], 10) + dy;
                                                        }
                                                }
                                                t_stroke.path = path;
                                        }
                                } else if (_vb_history[i].type === "scale") {
                                        sx = _vb_history[i].sx;
                                        sy = _vb_history[i].sy;
					var scale = Math.min(sx, sy);

					var size = spad.getSize();
					var width = size.width;
					var height = size.height;
                                        
					if (t_stroke.cx !== undefined) {
						t_stroke.cx = scale_point(t_stroke.cx, width/2, scale);
                                        }
                                        if (t_stroke.cy !== undefined) {
                                                t_stroke.cy = scale_point(t_stroke.cy, height/2, scale);
                                        }
                                        if (t_stroke.x !== undefined) {
                                                t_stroke.x = scale_point(t_stroke.x, width/2, scale);
                                        }
                                        if (t_stroke.y !== undefined) {
                                                t_stroke.y = scale_point(t_stroke.y, height/2, scale);
                                        }
					if (t_stroke.r !== undefined) {
						t_stroke.r *= scale;
					}
					if (t_stroke.rx !== undefined) {
						t_stroke.rx *= scale;
					}
					if (t_stroke.ry !== undefined) {
						t_stroke.ry *= scale;
					}
					if (t_stroke["font-size"] !== undefined) {
						t_stroke["font-size"] *= scale;
					}
					if (t_stroke.width !== undefined) {
						t_stroke.width *= scale;
					}
					if (t_stroke.height !== undefined) {
						t_stroke.height *= scale;
					}
					//if (t_stroke
                                        if (t_stroke.path !== undefined) {
                                                t_stroke.path = string_to_svg_path(t_stroke.path);
                                                var path = t_stroke.path;
                                                for (var j = 0; j < path.length; j++) {
                                                        if (path[j][1] !== undefined) {
                                                                path[j][1] = scale_point(parseFloat(path[j][1], 10), width/2, scale);
                                                        }
                                                        if (path[j][2] !== undefined) {
                                                                path[j][2] = scale_point(parseFloat(path[j][2], 10), height/2, scale);
                                                        }
                                                }
                                                t_stroke.path = path;
                                        }
                                }
                        }
			switch(t_stroke.type) {
			case "path":
				break;
			case "circle":
				break;
			case "ellipse":
				break;
			case "rect":
				break;
			case "text":
				break;
			default:
				break;
			}
			return t_stroke;
		}

		function untransform(strokes) {
			if (_vb_history.length === 0) {
				return strokes;
			}
			var i, dx, dy, sx, sy;
			if (jQuery.isArray(strokes)) {
				var t_strokes = [];
				for (i = 0; i < strokes.length; i++) {
					t_strokes.push(untransform(strokes[i]));
				}
				return t_strokes;
			}
			// strokes is a object
			var t_stroke = clone(strokes, "object");
                        for (i = _vb_history.length-1; i >= 0; i--) {
                                if (_vb_history[i].type === "translate") {
                                        dx = -_vb_history[i].dx;
                                        dy = -_vb_history[i].dy;

                                        if (t_stroke.cx !== undefined) {
                                                t_stroke.cx += dx;
                                        }
                                        if (t_stroke.cy !== undefined) {
                                                t_stroke.cy += dy;
                                        }
                                        if (t_stroke.x !== undefined) {
                                                t_stroke.x += dx;
                                        }
                                        if (t_stroke.y !== undefined) {
                                                t_stroke.y += dy;
                                        }
                                        if (t_stroke.path !== undefined) {
                                                t_stroke.path = string_to_svg_path(t_stroke.path);
                                                var path = t_stroke.path;
                                                for (var j = 0; j < path.length; j++) {
                                                        if (path[j][1] !== undefined) {
                                                                path[j][1] = parseFloat(path[j][1], 10) + dx;
                                                        }
                                                        if (path[j][2] !== undefined) {
                                                                path[j][2] = parseFloat(path[j][2], 10) + dy;
                                                        }
                                                }
                                                t_stroke.path = path;
                                        }
                                } else if (_vb_history[i].type === "scale") {
                                        sx = 1/_vb_history[i].sx;
                                        sy = 1/_vb_history[i].sy;
					var scale = Math.min(sx, sy);

					var size = spad.getSize();
					var width = size.width;
					var height = size.height;
                                        
					if (t_stroke.cx !== undefined) {
						t_stroke.cx = scale_point(t_stroke.cx, width/2, scale);
                                        }
                                        if (t_stroke.cy !== undefined) {
                                                t_stroke.cy = scale_point(t_stroke.cy, height/2, scale);
                                        }
                                        if (t_stroke.x !== undefined) {
                                                t_stroke.x = scale_point(t_stroke.x, width/2, scale);
                                        }
                                        if (t_stroke.y !== undefined) {
                                                t_stroke.y = scale_point(t_stroke.y, height/2, scale);
                                        }
					if (t_stroke.r !== undefined) {
						t_stroke.r *= scale;
					}
					if (t_stroke.rx !== undefined) {
						t_stroke.rx *= scale;
					}
					if (t_stroke.ry !== undefined) {
						t_stroke.ry *= scale;
					}
					if (t_stroke["font-size"] !== undefined) {
						t_stroke["font-size"] *= scale;
					}
					if (t_stroke.width !== undefined) {
						t_stroke.width *= scale;
					}
					if (t_stroke.height !== undefined) {
						t_stroke.height *= scale;
					}
					//if (t_stroke
                                        if (t_stroke.path !== undefined) {
                                                t_stroke.path = string_to_svg_path(t_stroke.path);
                                                var path = t_stroke.path;
                                                for (var j = 0; j < path.length; j++) {
                                                        if (path[j][1] !== undefined) {
                                                                path[j][1] = scale_point(parseFloat(path[j][1], 10), width/2, scale);
                                                        }
                                                        if (path[j][2] !== undefined) {
                                                                path[j][2] = scale_point(parseFloat(path[j][2], 10), height/2, scale);
                                                        }
                                                }
                                                t_stroke.path = path;
                                        }
                                }
                        }
			switch(t_stroke.type) {
			case "path":
				break;
			case "circle":
				break;
			case "ellipse":
				break;
			case "rect":
				break;
			case "text":
				break;
			default:
				break;
			}
			return t_stroke;
		}
	};

	var Selection = function(spad, _fire_change, svg_transform_to_std, _action_history) {
		var self = this;
		
		var _selected = [];
		
		var _select_box = [];

		var _is_moving = false;

		self.is_moving = function(moving) {
			if (moving === undefined) {
				return _is_moving;
			}
			_is_moving = moving;
		};
		
		self.isEmpty = function() {
                        if (_selected.length === 0)
                                return true;
                        return false;
                };

                self.erase = function() {
                        var old = [];
                        for (var i=0; i<_selected.length; i++) {
                                var strk = _selected[i].attr();
                                strk.type = _selected[i].type;
				strk.overlay_id = _selected[i].data("overlay_id");
				if (_selected[i].data("custom") !== undefined)
					strk.custom = _selected[i].data("custom");
                                old.push(strk);
                                _selected[i].remove();
                        }
                        _add_erase_history(old);
                        self.clear();
                        spad.reset_strokes("redraw");
                };

                self.color = function(value) {
                        var old = [];
                        for (var i=0; i<_selected.length; i++) {
                                var strk = _selected[i].attr();
                                strk.type = _selected[i].type;
				strk.overlay_id = _selected[i].data("overlay_id");
				if (_selected[i].data("custom") !== undefined)
					strk.custom = _selected[i].data("custom");
                                old.push(strk);
                                _selected[i].attr("stroke", value);
                        }
                        _add_history(old);
                        spad.reset_strokes();
                };

                self.width = function(value) {
                        var old = [];
                        for (var i=0; i<_selected.length; i++) {
                                var strk = _selected[i].attr();
                                strk.type = _selected[i].type;
				strk.overlay_id = _selected[i].data("overlay_id");
				if (_selected[i].data("custom") !== undefined)
					strk.custom = _selected[i].data("custom");
                                old.push(strk);
                                _selected[i].attr("stroke-width", value);
                        }
                        _add_history(old);
                        spad.reset_strokes();
                };

                self.opacity = function(value) {
                        var old = [];
                        for (var i=0; i<_selected.length; i++) {
                                var strk = _selected[i].attr();
                                strk.type = _selected[i].type;
				strk.overlay_id = _selected[i].data("overlay_id");
				if (_selected[i].data("custom") !== undefined)
					strk.custom = _selected[i].data("custom");
                                old.push(strk);
                                _selected[i].attr("stroke-opacity", value);
                        }
                        _add_history(old);
                        spad.reset_strokes();
                };

                self.fontSize = function(value) {
                        var old = [];
                        for (var i=0; i<_selected.length; i++) {
                                var strk = _selected[i].attr();
                                strk.type = _selected[i].type;
				strk.overlay_id = _selected[i].data("overlay_id");
				if (_selected[i].data("custom") !== undefined)
					strk.custom = _selected[i].data("custom");
                                old.push(strk);
                                _selected[i].attr("font-size", value);
                        }
                        _add_history(old);
                        spad.reset_strokes();
                };
                
                self.contains = function(stroke) {
			if (stroke === undefined || stroke === null)
				return false;
			for (var i = 0, n = _selected.length; i < n; i++) {
				var s = _selected[i];
				if (s.data("overlay_id") == stroke.overlay_id) {
					return true;
				}
			}
                        return false;
		};
		
		self.clear = function() {
			_selected = [];
			for (var i=0, n = _select_box.length; i < n; i++) {
				_select_box[i].clear();
			}
			_select_box = [];
		};
		
		self.select = function(eles) {
			if (eles === undefined) {
				return _selected;
			}
			
			if (jQuery.isArray(eles)) {
				for (var i=0, n=eles.length; i<n; i++) {
					if (eles[i] !== undefined && eles[i] !== null && !self.contains(eles[i])) {
						_select(eles[i]);
					}
				}
			} else {
				if (eles !== undefined && eles !== null && !self.contains(eles)) {
					_select(eles);
				}
			}
            return "";
		};

                function move_start(x, y, e) {
                        for (var i=0; i<_select_box.length; i++) {
                                _select_box[i]._box_drag_onstart(x, y, e);
                        }
                }

                function move_drag(dx, dy, x, y) {
                        for (var i=0; i<_select_box.length; i++) {
                                _select_box[i]._box_drag_onmove(dx, dy, x, y);
                        }
                }

                function move_end(e) {
                        var old_strokes = [];
			var new_strokes = [];
                        for (var i=0; i<_select_box.length; i++) {
                                var strk_object = _select_box[i]._box_drag_onend(e);
                                old_strokes.push(strk_object.old_stroke);
				new_strokes.push(strk_object.stroke);
                        }
                        _add_history(old_strokes, new_strokes);
			spad.reset_strokes();
                }
                
                function _add_erase_history(old_strokes) {
                        _action_history.add({
                                type: "erase",
                                stroke: old_strokes
                        });
                }

                function _add_history(old_strokes, new_strokes) {
                        if (new_strokes === undefined) {
				new_strokes = [];
				for (var i=0; i<_selected.length; i++) {
					var strk = _selected[i].attr();
					strk.type = _selected[i].type;
					strk.overlay_id = _selected[i].data("overlay_id");
					if (_selected[i].data("custom") !== undefined)
						strk.custom = _selected[i].data("custom");
					new_strokes.push(strk);
				}
			}

                        _action_history.add({
                                type: "transform",
                                stroke: new_strokes,
                                old_stroke: old_strokes
                        });
                }
		
		function _select(ele) {
			_selected.push(ele);
			var box = new SelectionBox(ele, self, spad, _fire_change, svg_transform_to_std, _action_history);
                        box.drag(move_drag, move_start, move_end);
			_select_box.push(box);
		}
	};
	
	var SelectionBox = function(element, _parent, spad, _fire_change, svg_transform_to_std, _action_history) {
		var self = this;
		
		var BOX_WIDTH = 1;
                var SELECTION_COLOR = "#ff0000";
                var RESIZE_HANDLER_SIZE = 6;
		
		var _ele = element;
		
		var box = _draw_selection();
                var resize_handlers = _draw_resize_handlers();

		self.clear = function() {
			box.remove();
                        for (var i in resize_handlers) {
                                resize_handlers[i].remove();
                        }
		};

                self.drag = function(move_drag, move_start, move_end) {
			box.hover(f_in_box, f_out_box).drag(move_drag, move_start, move_end);
                };

		function f_in_box () {
                        var _container = spad.container();
			$(_container).css("cursor", "move");
		}
		
                function f_out_box () {
                        var _container = spad.container();
			$(_container).css("cursor", "crosshair");
		}
		
		self._box_drag_onstart = function(x, y, e) {
			_parent.is_moving(true);
			var _old = _ele.clone();
			var _old_stroke = _old.attr();
			_old_stroke.overlay_id = _ele.data("overlay_id");
			if (_ele.data("custom") !== undefined)
				_old_stroke.custom = _ele.data("custom");
			box.data("old_stroke", _old_stroke);
			_old_stroke.type = _ele.type;
			if (_old_stroke.type === "path" && typeof _old_stroke.path == "object") {
				_old_stroke.path = svg_path_to_string(_old_stroke.path);
			}
			_old.remove();
		};
		
		self._box_drag_onmove = function(dx, dy, x, y) {
                        _ele.transform("");
			_ele.translate(dx, dy);
                        box.transform("");
			box.translate(dx, dy);
                        for (var i in resize_handlers) {
                                resize_handlers[i].transform("");
                                resize_handlers[i].translate(dx, dy);
                        }
		};
		
	        self._box_drag_onend = function(e) {
			var _old_stroke = box.data("old_stroke");
			
			var temp = _ele.clone();
			var strk = svg_transform_to_std(temp.type, temp.attr());
			temp.remove();
			_ele.attr(strk);
			_ele.transform("");
			strk = _ele.attr();
			strk.type = _ele.type;
			strk.overlay_id = _ele.data("overlay_id");
			if (_ele.data("custom") !== undefined)
				strk.custom = _ele.data("custom");
            var i;
			
                        var box_strk = svg_transform_to_std(box.type, box.attr());
                        box.attr(box_strk);
                        box.transform("");
                        
                        for (i in resize_handlers) {
                                var rh_strk = svg_transform_to_std(resize_handlers[i].type, resize_handlers[i].attr());
                                resize_handlers[i].attr(rh_strk);
                                resize_handlers[i].transform("");
                        }
			
			box.removeData("old_stroke");
			
                        var _strokes = spad.strokes();
			for (i = 0, n = _strokes.length; i < n; i++) {
				var s = _strokes[i];
				if (s.type === "path" && typeof s.path == "object") {
					s.path = svg_path_to_string(s.path);
				}
				if (s.overlay_id == _old_stroke.overlay_id) {
					_strokes.splice(i, 1);
					break;
				}
			}
			_strokes.push(strk);
			
			_fire_change();
			_parent.is_moving(false);
                        return {
                                stroke: strk,
                                old_stroke: _old_stroke
                        };
		};
		
		function _box_dimensions(path) {
			return path.getBBox();
		}
		
		function _draw_selection() {
			var dim = _box_dimensions(_ele);
			
			var _x = dim.x;
			var _y = dim.y;
			var _width = dim.width;
			var _height = dim.height;
			var _box = spad.paper().rect(_x, _y, _width, _height);
			
			_box.attr({
				stroke: SELECTION_COLOR,
				"stroke-linecap": "round",
				"stroke-linejoin": "round",
                                "fill": SELECTION_COLOR,
                                "fill-opacity": 0.5
			});
			return _box;
		}

                function _draw_resize_handlers() {
                        var dim = _box_dimensions(_ele);
			
			var _x = dim.x;
			var _y = dim.y;
			var _width = dim.width;
			var _height = dim.height;

                        var rh_offset_x = RESIZE_HANDLER_SIZE/2;
                        var rh_offset_y = RESIZE_HANDLER_SIZE/2;
                        var rh_size_x = RESIZE_HANDLER_SIZE;
                        var rh_size_y = RESIZE_HANDLER_SIZE;
                        var nw_box = spad.paper().rect(_x - rh_offset_x, _y - rh_offset_y, rh_size_x, rh_size_y);
                        var ne_box = spad.paper().rect(_x + _width - rh_offset_x, _y - rh_offset_y, rh_size_x, rh_size_y);
                        var sw_box = spad.paper().rect(_x - rh_offset_x, _y + _height - rh_offset_y, rh_size_x, rh_size_y);
                        var se_box = spad.paper().rect(_x + _width - rh_offset_x, _y + _height - rh_offset_y, rh_size_x, rh_size_y);

                        var att = {
                                stroke: "#ffff00",
                                "fill": "#ffff00"
                        };
                        nw_box.attr(att).data("direction","nw");
                        ne_box.attr(att).data("direction","ne");
                        sw_box.attr(att).data("direction","sw");
                        se_box.attr(att).data("direction","se");

                        var handlers = {
                                nw: nw_box,
                                ne: ne_box,
                                sw: sw_box,
                                se: se_box
                        };

                        _set_resize_events(handlers);

                        return handlers;
                }

                function _set_resize_events(handlers) {
                        for (var i in handlers) {
                                handlers[i].drag(resize_drag_onmove, resize_drag_onstart, resize_drag_onend);
                                var in_cursor = null;
                                var out_cursor = null;
                                switch(i) {
                                case "nw":
                                        handlers[i].hover(
                                                function () {
                                                        $(spad.container()).css('cursor', "nw-resize");
                                                },
                                                function () {
                                                        $(spad.container()).css('cursor', "crosshair");
                                                }
                                        );
                                        break;
                                case "ne":
                                        handlers[i].hover(
                                                function () {
                                                        $(spad.container()).css('cursor', "ne-resize");
                                                },
                                                function () {
                                                        $(spad.container()).css('cursor', "crosshair");
                                                }
                                        );
                                        break;
                                case "sw":
                                        handlers[i].hover(
                                                function () {
                                                        $(spad.container()).css('cursor', "sw-resize");
                                                },
                                                function () {
                                                        $(spad.container()).css('cursor', "crosshair");
                                                }
                                        );
                                        break;
                                case "se":
                                        handlers[i].hover(
                                                function () {
                                                        $(spad.container()).css('cursor', "se-resize");
                                                },
                                                function () {
                                                        $(spad.container()).css('cursor', "crosshair");
                                                }
                                        );
                                        break;
                                default:
                                    break;
                                }
                        }
                }

                function resize_drag_onstart(x, y, e) {
			_parent.is_moving(true);
			var _old = _ele.clone();
			var _old_stroke = _old.attr();
			_old_stroke.overlay_id = _ele.data("overlay_id");
			if (_ele.data("custom") !== undefined)
				_old_stroke.custom = _ele.data("custom");
			box.data("old_stroke", _old_stroke);
			_old_stroke.type = _ele.type;
			if (_old_stroke.type === "path" && typeof _old_stroke.path == "object") {
				_old_stroke.path = svg_path_to_string(_old_stroke.path);
			}
			_old.remove();
                }

                function resize_drag_onmove(dx, dy, x, y) {
                        var dir = this.data("direction");

                        var x_sig = 1;
                        var y_sig = 1;
                        switch(dir) {
                        case "nw":
                                x_sig = -1; y_sig = -1; break;
                        case "ne":
                                x_sig = 1; y_sig = -1; break;
                        case "sw":
                                x_sig = -1; y_sig = 1; break;
                        case "se":
                                x_sig = 1; y_sig = 1; break;
                        default:
                            break;
                        }
                        
                        var scale_x = (box.attr("width") + dx*x_sig) / box.attr("width");
                        var scale_y = (box.attr("height") + dy*y_sig) / box.attr("height");

                        switch(dir) {
                        case "nw":
                                resize_handlers["nw"].transform("");
                                resize_handlers["nw"].translate(dx, dy);
                                resize_handlers["ne"].transform("");
                                resize_handlers["ne"].translate(0, dy);
                                resize_handlers["sw"].transform("");
                                resize_handlers["sw"].translate(dx, 0);
                                box.transform("");
                                box.translate(dx/2, dy/2);
                                box.scale(scale_x, scale_y);
                                _ele.transform("");
                                _ele.translate(dx/2, dy/2);
                                _ele.scale(scale_x, scale_y);
                                break;
                        case "ne":
                                resize_handlers["ne"].transform("");
                                resize_handlers["ne"].translate(dx, dy);
                                resize_handlers["nw"].transform("");
                                resize_handlers["nw"].translate(0, dy);
                                resize_handlers["se"].transform("");
                                resize_handlers["se"].translate(dx, 0);
                                box.transform("");
                                box.translate(dx/2, dy/2);
                                box.scale(scale_x, scale_y);
                                _ele.transform("");
                                _ele.translate(dx/2, dy/2);
                                _ele.scale(scale_x, scale_y);
                                break;
                        case "sw":
                                resize_handlers["sw"].transform("");
                                resize_handlers["sw"].translate(dx, dy);
                                resize_handlers["nw"].transform("");
                                resize_handlers["nw"].translate(dx, 0);
                                resize_handlers["se"].transform("");
                                resize_handlers["se"].translate(0, dy);
                                box.transform("");
                                box.translate(dx/2, dy/2);
                                box.scale(scale_x, scale_y);
                                _ele.transform("");
                                _ele.translate(dx/2, dy/2);
                                _ele.scale(scale_x, scale_y);
                                break;
                        case "se":
                                resize_handlers["se"].transform("");
                                resize_handlers["se"].translate(dx, dy);
                                resize_handlers["ne"].transform("");
                                resize_handlers["ne"].translate(dx, 0);
                                resize_handlers["sw"].transform("");
                                resize_handlers["sw"].translate(0, dy);
                                box.transform("");
                                box.translate(dx/2, dy/2);
                                box.scale(scale_x, scale_y);
                                _ele.transform("");
                                _ele.translate(dx/2, dy/2);
                                _ele.scale(scale_x, scale_y);
                                break;
                        default:
                            break;
                        }
                }

                function resize_drag_onend(e) {
			var _old_stroke = box.data("old_stroke");
			
			var temp = _ele.clone();
			var strk = svg_transform_to_std(temp.type, temp.attr());
			temp.remove();
			_ele.transform("");     // HACK : if this is not given then after scaling the width is inacurrate
			_ele.attr(strk);
			_ele.transform("");
			strk = _ele.attr();
			strk.type = _ele.type;
			strk.overlay_id = _ele.data("overlay_id");
			if (_ele.data("custom") !== undefined)
				strk.custom = _ele.data("custom");
            var i;
			
                        var box_strk = svg_transform_to_std(box.type, box.attr());
                        box.transform("");      // HACK : if this is not given then after scaling the width is inacurrate
                        box.attr(box_strk);
                        box.transform("");
                        
                        for (i in resize_handlers) {
                                var rh_strk = svg_transform_to_std(resize_handlers[i].type, resize_handlers[i].attr());
                                resize_handlers[i].attr(rh_strk);
                                resize_handlers[i].transform("");
                        }
			
			box.removeData("old_stroke");
			
                        var _strokes = spad.strokes();
			for (i = 0, n = _strokes.length; i < n; i++) {
				var s = _strokes[i];
				if (s.type === "path" && typeof s.path == "object") {
					s.path = svg_path_to_string(s.path);
				}
				if (s.overlay_id == _old_stroke.overlay_id) {
					_strokes.splice(i, 1);
					break;
				}
			}
			_strokes.push(strk);
			
			_fire_change();
			_parent.is_moving(false);
                        
                        _action_history.add({
                                type: "transform",
                                stroke: strk,
                                old_stroke: _old_stroke
                        });
			spad.reset_strokes();
                }
	};
	
	/**
	 * Utility to generate string representation of an object.
	 */
	function inspect(obj) {
		var str = "";
		for (var i in obj) {
			str += i + "=" + obj[i] + "\n";
		}
		return str;
	}
	
})(window.Raphael);

Raphael.fn.display = function(elements) {
	for (var i = 0, n = elements.length; i < n; i++) {
		var e = elements[i];
		var type = e.type;
		this[type]().attr(e);
	}
};


/**
 * Utility functions to compare objects by Phil Rathe.
 * http://philrathe.com/projects/equiv
 */

// Determine what is o.
function hoozit(o) {
    if (o.constructor === String) {
        return "string";
        
    } else if (o.constructor === Boolean) {
        return "boolean";

    } else if (o.constructor === Number) {

        if (isNaN(o)) {
            return "nan";
        } else {
            return "number";
        }

    } else if (typeof o === "undefined") {
        return "undefined";

    // consider: typeof null === object
    } else if (o === null) {
        return "null";

    // consider: typeof [] === object
    } else if (o instanceof Array) {
        return "array";
    
    // consider: typeof new Date() === object
    } else if (o instanceof Date) {
        return "date";

    // consider: /./ instanceof Object;
    //           /./ instanceof RegExp;
    //          typeof /./ === "function"; // => false in IE and Opera,
    //                                          true in FF and Safari
    } else if (o instanceof RegExp) {
        return "regexp";

    } else if (typeof o === "object") {
        return "object";

    } else if (o instanceof Function) {
        return "function";
    } else {
        return undefined;
    }
}

// Call the o related callback with the given arguments.
function bindCallbacks(o, callbacks, args) {
    var prop = hoozit(o);
    if (prop) {
        if (hoozit(callbacks[prop]) === "function") {
            return callbacks[prop].apply(callbacks, args);
        } else {
            return callbacks[prop]; // or undefined
        }
    }
    return;
}

// Test for equality any JavaScript type.
// Discussions and reference: http://philrathe.com/articles/equiv
// Test suites: http://philrathe.com/tests/equiv
// Author: Philippe RathÃƒÂ© <prathe@gmail.com>

var equiv = function () {

    var innerEquiv; // the real equiv function
    var callers = []; // stack to decide between skip/abort functions

    
    var callbacks = function () {

        // for string, boolean, number and null
        function useStrictEquality(b, a) {
            if (b instanceof a.constructor || a instanceof b.constructor) {
                // to catch short annotaion VS 'new' annotation of a declaration
                // e.g. var i = 1;
                //      var j = new Number(1);
                return a == b;
            } else {
                return a === b;
            }
        }

        return {
            "string": useStrictEquality,
            "boolean": useStrictEquality,
            "number": useStrictEquality,
            "null": useStrictEquality,
            "undefined": useStrictEquality,

            "nan": function (b) {
                return isNaN(b);
            },

            "date": function (b, a) {
                return hoozit(b) === "date" && a.valueOf() === b.valueOf();
            },

            "regexp": function (b, a) {
                return hoozit(b) === "regexp" &&
                    a.source === b.source && // the regex itself
                    a.global === b.global && // and its modifers (gmi) ...
                    a.ignoreCase === b.ignoreCase &&
                    a.multiline === b.multiline;
            },

            // - skip when the property is a method of an instance (OOP)
            // - abort otherwise,
            //   initial === would have catch identical references anyway
            "function": function () {
                var caller = callers[callers.length - 1];
                return caller !== Object &&
                        typeof caller !== "undefined";
            },

            "array": function (b, a) {
                var i;
                var len;

                // b could be an object literal here
                if ( ! (hoozit(b) === "array")) {
                    return false;
                }

                len = a.length;
                if (len !== b.length) { // safe and faster
                    return false;
                }
                for (i = 0; i < len; i++) {
                    if( ! innerEquiv(a[i], b[i])) {
                        return false;
                    }
                }
                return true;
            },

            "object": function (b, a) {
                var i;
                var eq = true; // unless we can proove it
                var aProperties = [], bProperties = []; // collection of strings

                // comparing constructors is more strict than using instanceof
                if ( a.constructor !== b.constructor) {
                    return false;
                }

                // stack constructor before traversing properties
                callers.push(a.constructor);

                for (i in a) { // be strict: don't ensures hasOwnProperty and go deep

                    aProperties.push(i); // collect a's properties

                    if ( ! innerEquiv(a[i], b[i])) {
                        eq = false;
                    }
                }

                callers.pop(); // unstack, we are done

                for (i in b) {
                    bProperties.push(i); // collect b's properties
                }

                // Ensures identical properties name
                return eq && innerEquiv(aProperties.sort(), bProperties.sort());
            }
        };
    }();

    innerEquiv = function () { // can take multiple arguments
        var args = Array.prototype.slice.apply(arguments);
        if (args.length < 2) {
            return true; // end transition
        }

        return (function (a, b) {
            if (a === b) {
                return true; // catch the most you can
            } else if (a === null || b === null || typeof a === "undefined" || typeof b === "undefined" || hoozit(a) !== hoozit(b)) {
                return false; // don't lose time with error prone cases
            } else {
                return bindCallbacks(a, callbacks, [b, a]);
            }

        // apply transition with (1..n) arguments
        })(args[0], args[1]) && arguments.callee.apply(this, args.splice(1, args.length -1));
    };

    return innerEquiv;

}();
