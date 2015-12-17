(function() {

	/**
	 * Image class facilitating the zooming and panning of the image,
	 * also keeps the sync with sketchpad mode
	 */
	ImageHandle = function(image, par, ozoom, izoom, sketchpad) {
		var self = this;

		var _offset = null;
		var _panning = false;
		var _top = null;
		var _left = null;

		var _div_width = $(par).width();
		var _div_height = $(par).height();

		$(image).css('width', _div_width);
		$(image).css('height', _div_height);

		function mdown(e) {
			e.preventDefault();
			_offset = $(image).offset();
			_panning = true;
			_left = e.pageX;
			_top = e.pageY;
		}

		function mmove(e) {
			if (!_panning)
				return;
			e.preventDefault();
			var dx = e.pageX - _left;
			var dy = e.pageY - _top;

			_left = e.pageX;
			_top = e.pageY;

			$(image).css('margin-left', '+='+dx);
			$(image).css('margin-top', '+='+dy);

		}

		function mup(e) {
			if (!_panning)
				return;
			e.preventDefault();
			_offset = null;
			_top = null;
			_left = null;
			_panning = false;
		}

		function zoomout() {
			var margin = $(image).margin();
			var left = margin.left;
			var top = margin.top;
			// Zoom out by a factor of 2
			$(image).css('width', ($(image).width()/2));
			$(image).css('height', ($(image).height()/2));
			$(image).css('margin-left', ((_div_width/2 + left)/2));
			$(image).css('margin-top', ((_div_height/2 + top)/2));
		}

		function zoomin() {
			var margin = $(image).margin();
			var left = margin.left;
			var top = margin.top;
			// Zoom in by a factor of 2
			$(image).css('width', (2*$(image).width()));
			$(image).css('height', (2*$(image).height()));
			$(image).css('margin-left', (2*left - _div_width/2));
			$(image).css('margin-top', (2*top - _div_height/2));
		}

		function setPanListeners(condition) {
			if (condition) {
				$(par).mousedown(mdown);
				$(par).mousemove(mmove);
				$(par).mouseup(mup);
				$(image).mousedown(mdown);
				$(image).mousemove(mmove);
				$(image).mouseup(mup);
				$(document).mousemove(mmove);
				$(document).mouseup(mup);
			} else {
				$(par).unbind("mousedown", mdown);
				$(par).unbind("mousemove", mmove);
				$(par).unbind("mouseup", mup);
				$(image).unbind("mousedown", mdown);
				$(image).unbind("mousemove", mmove);
				$(image).unbind("mouseup", mup);
				$(document).unbind("mousemove", mmove);
				$(document).unbind("mouseup", mup);
			}
		}

		self.mode = function(option) {
			switch (option) {
			case "pan":
				setPanListeners(true);
				break;
			default:
				setPanListeners(false);
                break;
			}
			sketchpad.mode(option);
		};

		$(izoom).click(zoomin);
		$(ozoom).click(zoomout);
		$(izoom).click(function() {
			sketchpad.zoom(2);
		});
		$(ozoom).click(function() {
			sketchpad.zoom(0.5);
		});
	};


})();
