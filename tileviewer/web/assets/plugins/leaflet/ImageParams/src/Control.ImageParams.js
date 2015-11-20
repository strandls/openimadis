L.Control.ImageParams = L.Control.extend({
	options: {
		position:'bottomleft',
		data: null
	},
	
	initialize: function (options) {
		L.Util.setOptions(this, options);
		this.options.data = options.data;
	},
	
	onAdd: function (map) {
		this._map = map;
		
		var container = L.DomUtil.create('div', 'image-controls');
		
		var checkControls = L.DomUtil.create('div', 'controlUI check-controls', container);
		this._createCheckbox('Gray Scale', checkControls);
		this._createCheckbox('Z Stacked', checkControls);
		L.DomUtil.create('div', 'clear', checkControls);
		
		this._createSlider('slice', container);
		this._createSlider('frame', container);
		
		return container;
	},
	
	updateData: function (data) {
		this.options.data = data;
		this._map.fireEvent('layerchanged');
	},
	
	_createSlider: function (name, container) {
		var holder = L.DomUtil.create('div', 'controlUI', container);
		var label = L.DomUtil.create('label', 'slider-label', holder);
		var slider = L.DomUtil.create('div', 'slider', holder);
		L.DomUtil.create('div', 'clear', holder);
		
		var control = this;
		
		var initVal = 1;
		if (name === "slice") initVal = control._getData().sliceNumber + 1;
		else if (name === "frame") initVal = control._getData().frameNumber + 1;
		
		var maxVal = 1;
		if (name === "slice") maxVal = control._getData().sliceCount;
		else if (name === "frame") maxVal = control._getData().frameCount;
		
		$(slider).slider({
			value: initVal,
			min: 1,
			max: maxVal,
			step: 1,
			slide: function (event, ui) {
				if (name === "slice") label.innerHTML = "<b>Slice ( " + ui.value + " / " + maxVal + " ) :</b>";
				else if (name === "frame") label.innerHTML = "<b>Frame ( " + ui.value + " / " + maxVal + " ) :</b>";
			},
			stop: function (event, ui) {
				if (name === "slice") control._getData().sliceNumber = ui.value - 1;
				else if (name === "frame") control._getData().frameNumber = ui.value - 1;
				control._map.fireEvent('updatelayer');
			}
		});
		
		var updateSlider = function () {
			var maxVal = 1;
			if (name === "slice") maxVal = control._getData().sliceCount;
			else if (name === "frame") maxVal = control._getData().frameCount;
			
			$(slider).slider("option", "max", maxVal);
			
			if (maxVal > 1) {
				holder.setAttribute('style', 'display:block;');
				if (name === "slice") {
					$(slider).slider("value", control._getData().sliceNumber + 1);
					label.innerHTML = "<b>Slice ( " + (control._getData().sliceNumber + 1) + " / " + maxVal + " ) :</b>";
				} else if (name === "frame") {
					$(slider).slider("value", control._getData().frameNumber + 1);
					label.innerHTML = "<b>Frame ( " + (control._getData().frameNumber + 1) + " / " + maxVal + " ) :</b>";
				}
			} else {
				holder.setAttribute('style', 'display:none;');
			}
		};
		
		updateSlider();
		
		control._map.on('layerchanged', updateSlider);
	},
	
	_createCheckbox: function (name, container) {
		var holder = L.DomUtil.create('div', 'image-control', container);
		var checkbox = L.DomUtil.create('input', 'checkbox', holder);
		checkbox.setAttribute('type', 'checkbox');
		var label = L.DomUtil.create('label', 'checkbox-label', holder);
		label.innerHTML = name;
		
		var control = this;
		
		L.DomEvent.addListener(checkbox, 'click', function () {
			if (name === "Gray Scale") control._getData().isGrayScale = checkbox.checked ? 1 : 0;
			else if (name === "Z Stacked") control._getData().isZStacked = checkbox.checked ? 1 : 0;
			control._map.fireEvent('updatelayer');
		});
		
		control._map.on('layerchanged', function () {
			if (name === "Gray Scale") checkbox.checked = control._getData().isGrayScale==1;
			else if (name === "Z Stacked") checkbox.checked = control._getData().isZStacked==1;
		});
	},
	
	_getData: function () {
		return this.options.data;
	}
});

L.Map.addInitHook(function () {
	if (this.options.imageParams) {
		this.imageParams = (new L.Control.ImageParams()).addTo(this);
	}
});

L.control.imageparams = function (options) {
	return new L.Control.ImageParams(options);
};

