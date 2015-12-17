$(document).ready(function() {

	/**
	 * Image class facilitating the zooming and panning of the image,
	 * also keeps the sync with sketchpad mode
	 */
	var Image = function(image, par, ozoom, izoom, sketchpad) {
		self = this;

		var _img = $(image);
		var _parent = $(par);
		var _zoom_out = $(ozoom);
		var _zoom_in = $(izoom);
		var _offset = null;
		var _panning = false;
		var _top = null;
		var _left = null;

		var _div_width = _parent.width();
		var _div_height = _parent.height();

		_img.css('width', _div_width);
		_img.css('height', _div_height);

		function mdown(e) {
			e.preventDefault();
			_offset = _img.offset();
			_panning = true;
			_left = e.pageX;
			_top = e.pageY;
		};

		function mmove(e) {
			if (!_panning)
				return;
			e.preventDefault();
			var dx = e.pageX - _left;
			var dy = e.pageY - _top;

			_left = e.pageX;
			_top = e.pageY;

			_img.css('margin-left', '+='+dx);
			_img.css('margin-top', '+='+dy);

		};

		function mup(e) {
			if (!_panning)
				return;
			e.preventDefault();
			_offset = null;
			_top = null;
			_left = null;
			_panning = false;
		};

		function zoomout() {
			var margin = _img.margin();
			var left = margin.left;
			var top = margin.top;
			// Zoom out by a factor of 2
			_img.css('width', (_img.width()/2));
			_img.css('height', (_img.height()/2));
			_img.css('margin-left', ((_div_width/2 + left)/2));
			_img.css('margin-top', ((_div_height/2 + top)/2));
		};

		function zoomin() {
			var margin = _img.margin();
			var left = margin.left;
			var top = margin.top;
			// Zoom in by a factor of 2
			_img.css('width', (2*_img.width()));
			_img.css('height', (2*_img.height()));
			_img.css('margin-left', (2*left - _div_width/2));
			_img.css('margin-top', (2*top - _div_height/2));
		};

		function setPanListeners(condition) {
			if (condition) {
				_parent.mousedown(mdown);
				_parent.mousemove(mmove);
				_parent.mouseup(mup);
				_img.mousedown(mdown);
				_img.mousemove(mmove);
				_img.mouseup(mup);
				$(document).mousemove(mmove);
				$(document).mouseup(mup);
			} else {
				_parent.unbind("mousedown", mdown);
				_parent.unbind("mousemove", mmove);
				_parent.unbind("mouseup", mup);
				_img.unbind("mousedown", mdown);
				_img.unbind("mousemove", mmove);
				_img.unbind("mouseup", mup);
				$(document).unbind("mousemove", mmove);
				$(document).unbind("mouseup", mup);
			}
		};

		self.mode = function(option) {
			switch (option) {
			case "pan":
				setPanListeners(true);
				break;
			default:
				setPanListeners(false);
			}
			sketchpad.editing(option);
		};

		_zoom_in.click(zoomin);
		_zoom_out.click(zoomout);
		_zoom_in.click(function() {
			sketchpad.zoom(2);
		});
		_zoom_out.click(function() {
			sketchpad.zoom(0.5);
		});
	};

	/**
	 * Defining some constants
	 */
	WIDTH = $('.image_overlay').width();
	HEIGHT = $('.image_overlay').height();
	pen_color = "#000000";
	pen_width = 5.0;
	pen_opacity = 1.0;
	
	/**
	 * Creating the sketchpad
	 */
	sketchpad = Raphael.sketchpad("imagedisplaypanel", {
		width: WIDTH,
		height: HEIGHT,
		editing: true
	});
	
	sketchpad.pen("pencil");
	sketchpad.pen().color(pen_color).width(pen_width).opacity(pen_opacity);

	/**
	 * Creating the image_content object, which is the instance of the Image class
	 */
	image_content = new Image('.image_overlay #test', '.image_overlay', '.image_overlay #zoom_out_icon', '.image_overlay #zoom_in_icon', sketchpad);
	image_content.mode(true);


	/**
	 * Below is function definitions for the toolbars
	 */
	// FREEHAND
	$('#pencil').click(function() {
		image_content.mode(true);
		sketchpad.pen("pencil");
	});
	// CIRCLE
	$('#circle').click(function() {
		image_content.mode(true);
		sketchpad.pen("circle");
	});
	// ELLIPSE
	$('#ellipse').click(function() {
		image_content.mode(true);
		sketchpad.pen("ellipse");
	});
	// RECTANGLE
	$('#rectangle').click(function() {
		image_content.mode(true);
		sketchpad.pen("rectangle");
	});
	// TEXT
	$('#text').click(function() {
		image_content.mode(true);
		sketchpad.pen("text");
	});
	// UNDO
	$('#undo').click(function() {
		sketchpad.undo();
	});
	// REDO
	$('#redo').click(function() {
		sketchpad.redo();
	});
	// COLOR
	colorSelected = function(hex, rgb) {
		pen_color = hex;
		sketchpad.pen().color(pen_color);
	};
	$('#color').miniColors({
		change: colorSelected
	});
	// LINE WIDTH
	$lineDialog = $('<div></div>').html("<form><input type=\"text\" name=\"linevalue\" /><input type=\"button\" value=\"Submit\" onclick=\"lineSelected(this.form)\" /></form>").dialog({'autoOpen': false, 'title': "Line width chooser"});
	
	lineSelected = function(form) {
		pen_width = form.linevalue.value;
		sketchpad.pen().width(pen_width);
		$lineDialog.dialog('close');
	};
	
	$('#line').click(function() {
		$lineDialog.dialog('open');
	});
	// OPACITY
	$opacityDialog = $('<div></div>').html("<form><input type=\"text\" name=\"opacityvalue\" /><input type=\"button\" value=\"Submit\" onclick=\"opacitySelected(this.form)\" /></form>").dialog({'autoOpen': false, 'title': "Opacity"});
	
	opacitySelected = function(form) {
		pen_opacity = form.opacityvalue.value;
		sketchpad.pen().opacity(pen_opacity);
		$opacityDialog.dialog('close');
	};
	
	$('#opacity').click(function() {
		$opacityDialog.dialog('open');
	});
	// ERASE
	$('#erase').click(function() {
		var mode = sketchpad.editing();
		if (mode != "erase") {
			image_content.mode("erase");
			$('#erase').text("Erase (click to off)");
		} else {
			image_content.mode(true);
			$('#erase').text("Erase");
		}
	});
	// CLEAR
	$('#clear').click(function() {
		sketchpad.clear();
	});
	// SELECT
	$('#select').click(function() {
		var mode = sketchpad.editing();
		if (mode != "select") {
			image_content.mode("select");
			$('#select').text("Select (click to off)");
		} else {
			image_content.mode(true);
			$('#select').text("Select");
		}
	});
	// TESTING FUNCTION on how to export the json content and use it back
	$('#test').click(function() {
		var x = sketchpad.json();
		sketchpad.clear();
		sketchpad.json(x);
	});
	// PAN
	$('#pan').click(function() {
		image_content.mode('pan');
	});

});
