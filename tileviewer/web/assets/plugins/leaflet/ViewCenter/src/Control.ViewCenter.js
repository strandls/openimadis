L.Control.ViewCenter = L.Control.extend({
	options: {
		position: 'topleft',
		title: 'Initial Position',
		forceSeparateButton: false,
		vcLatLng: [0, 0],
		vcZoom: 0
	},

	onAdd: function (map) {
		var className = 'leaflet-control-view-center', container;
		
		if(map.zoomControl && !this.options.forceSeparateButton) {
			container = map.zoomControl._container;
		} else {
			container = L.DomUtil.create('div', 'leaflet-bar');
		}
		
		this._createButton(this.options, className, container, this.setCenterView, map);

		return container;
	},
	
	_createButton: function (opts, className, container, fn, context) {
		var link = L.DomUtil.create('a', className, container);
		link.href = '#';
		link.title = opts.title;

		var zoom = opts.vcZoom || 6;
		
		L.DomEvent
			.addListener(link, 'click', L.DomEvent.stopPropagation)
			.addListener(link, 'click', L.DomEvent.preventDefault)
			.addListener(link, 'click', function(){
			    context.setView(opts.vcLatLng, zoom);
			}, context);
		return link;
	}
});

/*
L.Map.addInitHook(function () {
	if (this.options.fullscreenControl) {
		this.fullscreenControl = L.control.fullscreen(this.options.fullscreenControlOptions);
		this.addControl(this.fullscreenControl);
	}
});
*/

L.control.viewcenter = function (options) {
	return new L.Control.ViewCenter(options);
};

