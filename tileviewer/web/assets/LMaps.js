$("head").append("<link type='text/css' rel='stylesheet' href='assets/leaflet-0.6.4/leaflet.css' />");
$("head").append("<link type='text/css' rel='stylesheet' href='assets/plugins/leaflet/FullScreen/src/Control.FullScreen.css' />");
$("head").append("<link type='text/css' rel='stylesheet' href='assets/plugins/leaflet/ImageParams/src/Control.ImageParams.css' />");
$("head").append("<link type='text/css' rel='stylesheet' href='assets/plugins/leaflet/Loading/src/Control.Loading.css' />");
$("head").append("<link type='text/css' rel='stylesheet' href='assets/plugins/leaflet/MiniMap/src/Control.MiniMap.css' />");
$("head").append("<link type='text/css' rel='stylesheet' href='assets/plugins/leaflet/ViewCenter/src/Control.ViewCenter.css' />");
$("head").append("<link type='text/css' rel='stylesheet' href='assets/plugins/leaflet/ZoomSlider/src/L.Control.Zoomslider.css' />");

$.ajax({url:'assets/util.js', async:false, dataType:"script"});
$.ajax({url:'assets/leaflet-0.6.4/leaflet-src.js', async:false, dataType:"script"});
$.ajax({url:'assets/plugins/leaflet/FullScreen/src/Control.FullScreen.js', async:false, dataType:"script"});
$.ajax({url:'assets/plugins/leaflet/ImageParams/src/Control.ImageParams.js', async:false, dataType:"script"});
$.ajax({url:'assets/plugins/leaflet/Loading/src/Control.Loading.js', async:false, dataType:"script"});
$.ajax({url:'assets/plugins/leaflet/MiniMap/src/Control.MiniMap.js', async:false, dataType:"script"});
$.ajax({url:'assets/plugins/leaflet/ViewCenter/src/Control.ViewCenter.js', async:false, dataType:"script"});
$.ajax({url:'assets/plugins/leaflet/ZoomSlider/src/L.Control.Zoomslider.js', async:false, dataType:"script"});
$.ajax({url:'assets/kinetic-v4.6.0.min.js', async:false, dataType:"script"});
$.ajax({url:'assets/kinetic.draw.js', async:false, dataType:"script"});

var LMap = {
	
		// Set up the Map
		initialize: function (container) {
			var map = new L.map(container, {center:[0,0],zoom: 0});	
			//map.setView([-100,-50], 0);
			//var map = L.map(container).setView([51.505, -0.09], 1);
			map.on('zoomstart', LMap.cancelTileFetching);
			map.on('movestart', LMap.cancelTileFetching);
			map.on('dragstart', LMap.cancelTileFetching);
			// Set up the Record Layers
			LMap.setRecords(map, Util.recordids);
			// Add the Image Control
//			var imageParams = new L.Control.ImageParams({data: LMap.currentLayer.getData()});
//			map.addControl(imageParams);
			
			// Add the Full Screen control
//			map.addControl(new L.Control.FullScreen());
			
			// Add the Home control
//			map.addControl(new L.Control.ViewCenter({vcLatLng: Util.initCenter, vcZoom: Util.initZoom(container)}));

			// Add the Loading control
//			map.addControl(new L.Control.loading({separate:true}));

			// Add the Mini Map control
//			var miniLayer = new L.TileLayer(LMap.tileUrl(LMap.currentLayer.getData()), {
//					attribution: LMap.attr,
//					continuousWorld: true,
//					minZoom: 0,
//					maxZoom: Util.maxZoom(LMap.currentLayer.getData())
//				});
//			map.addControl(new L.Control.MiniMap(miniLayer, {autoToggleDisplay: true, toggleDisplay: true}));
//			
//			// Update Controls on Layer change
//			map.on('baselayerchange', function () {
//				miniLayer.setUrl(LMap.tileUrl(LMap.currentLayer.getData()));
//				imageParams.updateData(LMap.currentLayer.getData());
//			});
//			
			// Update Layer on control actions
			map.on('updatelayer', function (e) {
				LMap.currentLayer.update();
			});
			
			var click = document.getElementById('click'),
			mousemove = document.getElementById('mousemove');
			
			return map;
		},
		
		cancelTileFetching: function(){
			url = Util.serverAddr+"cancelTileFetching";
			$.ajax({url:url,success:function(result){
				console.log("submited tasks canceled");
			}});
		},
	
		// Get tile URL with the ZXY and TileSize property
		tileUrl: function (data) {
			return Util.getTileUrl(data) +
					"&d=" + "{tileSize}" +
					"&x=" + "{x}" +
					"&y=" + "{y}" +
					"&z=" + "{z}";
		},
	
		// Create layers for each record
		setRecords: function (map, records) {
			var layers = new L.Control.Layers({}, {}, {collapsed: true});
			for (var i=0; i<records.length; i++) {
				var layer = new LMap.initLayer(records[i]).getLayer();
				layers.addBaseLayer(layer, "<img src='" + Util.getThumbnailUrl(records[i]) + "' class='thumbnail' />");
				if (i==0) layer.addTo(map);
			}
			layers.addTo(map);
		},
		
		// Create the layer holder
		initLayer: function (recordid) {
			var data = Util.getData(recordid);
			var maxZoom = Math.floor(Math.log(Math.max(data.imageWidth, data.imageHeight))/Math.log(2)-8);
			var minZoom = (maxZoom - 5 > 0)? (maxZoom -5) : 0;
			var layer = new L.tileLayer(LMap.tileUrl(data), {
					attribution: LMap.attr,
					continuousWorld: true,
					detectRetina: true,
					zoomReverse: true,
					minZoom: minZoom,
					maxZoom:  maxZoom
				});
	
			var holder = this;

			layer.on('loading', function (e) {
				LMap.currentLayer = holder;
			});
	
			this.getLayer = function () {
				return layer;
			};
	
			this.getData = function () {
				return data;
			};
	
			this.update = function () {
				layer.setUrl(LMap.tileUrl(data));
			};
	
			return this;
		}
};

var overlayNames = [];
var editOverlayName;

// Load the Map
$(document).ready(function () {
	Util.init();
	$.ajax({
		url: Util.getLoginUrl(),
		async: false,
		success: function (result) {
			if (result['login']) {
				Util.token = result['token'];
				MyMap = new LMap.initialize("map");
				getVisualOverlays();
			}
		}
	});
});



function getOverlayNames(){
	return overlayNames;
}
function getVisualOverlays(){
	
	var recordid= Util.recordids[0];
	
	var visualOverlayUrl = Util.getVisualOverlayUrl(recordid);
	
	overlayNames = [];
	
	$.ajax({
		url: visualOverlayUrl,
		success: function (result) {
			var overlayEditor = '<table id="OverlayTable" style="border-spacing: 10px;">';
			overlayEditor = overlayEditor + '</table>';
			$('#overlays').append(overlayEditor);
			if(result.length > 0){
				for(var i=0;i<result.length;i++){
					var checkBox = '<label title="Display overlay '+result[i]+'"><i><input type="checkbox" name="'+result[i]+'" id="'+result[i]+'" onclick="handleCheckBoxChange();" value="'+result[i]+'"';
					checkBox = checkBox +'>'+result[i]+'</i></label>';
					overlayNames.push(result[i]);
					var editButton = '<input type="image" title="Click to edit overlay '+result[i]+'" src="assets/images/va_edit.png" class="editButton" name="'+result[i]+'" onclick="handleEditOverlay(this.name);" value="Edit">';
					var delButton = '<input type="image" title="Click to delete overlay '+result[i]+'" src="assets/images/va_delete.png" class="editButton" name="'+result[i]+'" onclick="handleDeleteOverlay(this.name);" value="Del">';
					var locateButton = '<input type="image" title="Locate Overlay" src="assets/images/search.png" class="editButton" name="'+result[i]+'" onclick="handleSearchOverlay(this.name);">';
					var table = document.getElementById("OverlayTable");
					var row = table.insertRow(table.rows.length);
					var cell1 = row.insertCell(0);
					var cell2 = row.insertCell(1);
					var cell3 = row.insertCell(2);
					var cell4 = row.insertCell(3);
					cell1.innerHTML = checkBox;
					cell2.innerHTML = editButton;
					cell3.innerHTML = delButton;
					cell4.innerHTML = locateButton;
				}
			}
		}
	});
}

function setEditOverlayName(name){
	editOverlayName = name;
	// set checked false for other overlay check boxes
	for(var i=0;i<overlayNames.length;i++){
		if( document.getElementById(overlayNames[i]).value == name ){ 
			document.getElementById(overlayNames[i]).checked = true;
		    continue;
		}
		document.getElementById(overlayNames[i]).checked = false;
	}
}

function deleteRow(name) {
	try {
		var table = document.getElementById("OverlayTable");
		// delete row
		for(var i=0;i<overlayNames.length;i++){
			if( document.getElementById(overlayNames[i]).value == name ){ 
				table.deleteRow(i);
			    break;
			}
		}
	}catch(e) {
		alert(e);
	}
}

function deleteOverlay(name){
	var recordid= Util.recordids[0];
	var overlayUrl = Util.deleteOverlayUrl(recordid)+"&overlayName="+name;
	showDeletingPopup();
	$.ajax({
		url: overlayUrl,
		async: false,
		success: function (result) {
			setDirty(false);
			$('#container').remove();
			removeMapEvent(false);
			$('#mapcontainer').append('<div id="container" style="height:0px;width:0px;"></div>');
			addMapEvent(false);
			deleteRow(name);
			var index = overlayNames.indexOf(name);
			if (index > -1) {
				overlayNames.splice(index, 1);
			}
			closeDeletingPopup();
			displaySelectedOverlays();
		}
	});
}

function addRow(name){
	try {
		var table = document.getElementById("OverlayTable");
		// add Row
		var row = table.insertRow(table.rows.length);

		var cell1 = row.insertCell(0);
		var cell2 = row.insertCell(1);
		var cell3 = row.insertCell(2);
		var cell4 = row.insertCell(3);
        
		var checkBox = '<label title="Display overlay '+name+'"><i><input type="checkbox" name="'+name+'" id="'+name+'" onclick="handleCheckBoxChange();" value="'+name+'"';
		checkBox = checkBox +'>'+name+'</i></label>';
		var editButton = '<input type="image" title="Click to edit overlay '+name+'" src="assets/images/va_edit.png" class="editButton" name="'+name+'" onclick="handleEditOverlay(this.name);" value="Edit">';
		var delButton = '<input type="image" title="Click to delete overlay '+name+'" src="assets/images/va_delete.png" class="editButton" name="'+name+'" onclick="handleDeleteOverlay(this.name);" value="Del">';
		var locateButton = '<input type="image" title="Locate Overlay" src="assets/images/search.png" class="editButton" name="'+name+'" onclick="handleSearchOverlay(this.name);">';
		cell1.innerHTML = checkBox;
		cell2.innerHTML = editButton;
		cell3.innerHTML = delButton;
		cell4.innerHTML = locateButton;
	}catch(e) {
		alert(e);
	}
}

function createOverlay(name){
	var recordid= Util.recordids[0];
	var overlayUrl = Util.createOverlayUrl(recordid)+"&overlayName="+name;
	$.ajax({
		url: overlayUrl,
		async: false,
		success: function (result) {
			addRow(name);
			overlayNames.push(name);
		}
	});
}

function getZoomLevel(){
	return MyMap.getZoom();
}

var paramsArray = [];

function moveToPostion(x,y,level){
	//console.log("x mod:"+x);
	//console.log("y mod:"+y);
	//MyMap.setView(MyMap.unproject([0,0],MyMap.getMinZoom()),MyMap.getMinZoom());
	var latlon = MyMap.unproject([x,y],level);
	//console.log("lat mod:"+latlon.lat);
	//console.log("lon mod:"+latlon.lng);
	MyMap.setView(latlon,level);
}

function onClickVisualObject(index){
	moveToPostion(paramsArray[index].x,paramsArray[index].y,paramsArray[index].level);
	for(var i=0;i<overlayNames.length;i++){
		if( document.getElementById(overlayNames[i]).value == paramsArray[index].name ){ 
			document.getElementById(overlayNames[i]).checked = true;
		    break;
		}
	}
	$('#container').remove();
	removeMapEvent(false);
	$('#mapcontainer').append('<div id="container" style="height:0px;width:0px;"></div>');
	addMapEvent(false);
	setDirty(false);
	displaySelectedOverlays();
	closeSearchOverlayDialog();
}

function searchOverlay(name){
	var recordid= Util.recordids[0];
	var overlayUrl = Util.searchOverlayUrl(recordid)+"&overlayName="+name;
	document.getElementById("searchOverlayDialog").innerHTML = "";
	
	$.ajax({
		url: overlayUrl,
		async: false,
		success: function (result) {
			var visualobjects = JSON.stringify(result);
			var shapes= JSON.parse(visualobjects);
			paramsArray = [];
			for(var i=0; i<shapes.length;i++){
				var level = shapes[i]['zoom_level'];

				//console.log("x:"+shapes[i]['x']+" , "+"y:"+shapes[i]['y']);
				var recordid= Util.recordids[0];
				
				var min = MyMap.getPixelBounds().min;
				var max = MyMap.getPixelBounds().max;
				
				var canvas_x = 0;
				var canvas_y = 0;
				
				var zoom = level;
				var max_zoom = MyMap._layers[22]._map._layersMaxZoom;
				var reverse_zoom = max_zoom-zoom;
				
				var imageWidth = Util.getData(recordid).imageWidth / Math.pow(2,reverse_zoom);
				var imageHeight = Util.getData(recordid).imageHeight / Math.pow(2,reverse_zoom);
				
				var canvas_width = max.x-min.x;
				var canvas_height = max.y-min.y;
				
				if(min.x<0){
					canvas_x = min.x * -1;
					canvas_width = max.x;
				}
				else{
					if(max.x>imageWidth){
						canvas_width = imageWidth-min.x;
					}
				}
				if(min.y<0){
					canvas_y = min.y * -1;
					canvas_height = max.y;
				}
				else{
					if(max.y>imageHeight){
						canvas_height = imageHeight-min.y;
					}
				}
				
				canvas_width = canvas_width < imageWidth ? canvas_width : imageWidth;
				canvas_height = canvas_height < imageHeight ? canvas_height : imageHeight;	
				
				$("#container").css({top: canvas_y, left: canvas_x, position:'absolute'});
				
				var areaX = min.x < 0 ? 0 : min.x;
				var areaY = min.y < 0 ? 0 : min.y;
				
				areaX *= Math.pow(2, reverse_zoom);
				areaY *= Math.pow(2, reverse_zoom);
				
				var scale = { x: 1/Math.pow(2, reverse_zoom), y: 1/Math.pow(2, reverse_zoom) };
				var offset = { x:0, y:0 };
				var tshape=JSON.parse(getShapePoints(JSON.stringify(shapes[i]),scale,offset) );
				var params = {};
				params.name = name;
				params.x = tshape['x'];
				params.y = tshape['y'];
				params.level= level;
				paramsArray.push(params);
				
				var label = '<label align="left" style="padding:2px;" title="click to navigate" onclick="onClickVisualObject('+i+');"><i>';
				switch(shapes[i]["type"]){
					case 'Rect':
							label = label + '<input type="image" src="assets/images/rect.png">';
							break;
					case 'Ellipse': 
						    label = label + '<input type="image" src="assets/images/ellipse.png">';
							break;
					case 'Line':
							label = label + '<input type="image" src="assets/images/line.png">';
							break;
					case 'Path':
							label = label + '<input type="image" src="assets/images/fhpath.png">';
							break;
					case 'Polygon':
							label = label + '<input type="image" src="assets/images/polygon.png">';
							break;
					case 'Arrow':
							label = label + '<input type="image" src="assets/images/arrow.png">';
							break;
					case 'Text':
							label = label + '<input type="image" src="assets/images/text.png">';
							break;				
				}
				label = label +'</i></label>';
				$('#searchOverlayDialog').append(label);
			}
			openSearchOverlayDialog();
		}
	});
}

function getSelectedOverlayNames(){
	var selectedNames = [];
	for(var i=0;i<overlayNames.length;i++){
		if(document.getElementById(overlayNames[i]).checked == true) {
			selectedNames.push(document.getElementById(overlayNames[i]).value);
		}
	}
	return selectedNames;
}

function getOverlays(){
	
	var recordid= Util.recordids[0];
	
	var min = MyMap.getPixelBounds().min;
	var max = MyMap.getPixelBounds().max;
	
	var canvas_x = 0;
	var canvas_y = 0;
	
	var zoom = MyMap._layers[22]._map._zoom;
	var max_zoom = MyMap._layers[22]._map._layersMaxZoom;
	var reverse_zoom = max_zoom-zoom;
	
	var imageWidth = Util.getData(recordid).imageWidth / Math.pow(2,reverse_zoom);
	var imageHeight = Util.getData(recordid).imageHeight / Math.pow(2,reverse_zoom);
	
	var canvas_width = max.x-min.x;
	var canvas_height = max.y-min.y;
	
	if(min.x<0){
		canvas_x = min.x * -1;
		canvas_width = max.x;
	}
	else{
		if(max.x>imageWidth){
			canvas_width = imageWidth-min.x;
		}
	}
	if(min.y<0){
		canvas_y = min.y * -1;
		canvas_height = max.y;
	}
	else{
		if(max.y>imageHeight){
			canvas_height = imageHeight-min.y;
		}
	}
	
	canvas_width = canvas_width < imageWidth ? canvas_width : imageWidth;
	canvas_height = canvas_height < imageHeight ? canvas_height : imageHeight;	
	
	$("#container").css({top: canvas_y, left: canvas_x, position:'absolute'});
	
	var areaX = min.x < 0 ? 0 : min.x;
	var areaY = min.y < 0 ? 0 : min.y;
	
	areaX *= Math.pow(2, reverse_zoom);
	areaY *= Math.pow(2, reverse_zoom);
	
	var areaW = canvas_width * Math.pow(2, reverse_zoom); 
	var areaH = canvas_height * Math.pow(2, reverse_zoom);
	
	var overlayUrl = Util.getOverlayUrl(recordid)+"&areaX="+areaX+"&areaY="+areaY+"&areaW="+areaW+"&areaH="+areaH+"&overlayName="+editOverlayName;
	
	var scale = { x: 1/Math.pow(2, reverse_zoom), y: 1/Math.pow(2, reverse_zoom) };
	var offset = { x:areaX, y:areaY };
	
	initStage(canvas_width,canvas_height);
	setDrawingScale(scale);
	setDrawingOffset(offset);
	
	showFetchingPopup();
	$.ajax({
		url: overlayUrl,
		async: false,
		success: function (result) {
			closeFetchingPopup();
			setDraggable(true);
			setEditable(true);
			var visualobjects = JSON.stringify(result);
			LoadShapes(visualobjects, scale, offset);
			
		}
	});
}

function displaySelectedOverlays(){
	var selected = getSelectedOverlayNames();
	var recordid= Util.recordids[0];
	
	var min = MyMap.getPixelBounds().min;
	var max = MyMap.getPixelBounds().max;
	
	var canvas_x = 0;
	var canvas_y = 0;
	
	var zoom = MyMap._layers[22]._map._zoom;
	var max_zoom = MyMap._layers[22]._map._layersMaxZoom;
	var reverse_zoom = max_zoom-zoom;
	
	var imageWidth = Util.getData(recordid).imageWidth / Math.pow(2,reverse_zoom);
	var imageHeight = Util.getData(recordid).imageHeight / Math.pow(2,reverse_zoom);
	
	var canvas_width = max.x-min.x;
	var canvas_height = max.y-min.y;
	
	if(min.x<0){
		canvas_x = min.x * -1;
		canvas_width = max.x;
	}
	else{
		if(max.x>imageWidth){
			canvas_width = imageWidth-min.x;
		}
	}
	if(min.y<0){
		canvas_y = min.y * -1;
		canvas_height = max.y;
	}
	else{
		if(max.y>imageHeight){
			canvas_height = imageHeight-min.y;
		}
	}
	
	canvas_width = canvas_width < imageWidth ? canvas_width : imageWidth;
	canvas_height = canvas_height < imageHeight ? canvas_height : imageHeight;	
	
	var areaX = min.x < 0 ? 0 : min.x;
	var areaY = min.y < 0 ? 0 : min.y;
	
	areaX *= Math.pow(2, reverse_zoom);
	areaY *= Math.pow(2, reverse_zoom);
	
	var areaW = canvas_width * Math.pow(2, reverse_zoom); 
	var areaH = canvas_height * Math.pow(2, reverse_zoom);
	var scale = { x: 1/Math.pow(2, reverse_zoom), y: 1/Math.pow(2, reverse_zoom) };
	var offset = { x:areaX, y:areaY };
	
	var visualobjects;
	var jsonObj = [];
	showFetchingPopup();
	for(var i=0;i<selected.length;i++){
		var overlayUrl = Util.getOverlayUrl(recordid)+"&areaX="+areaX+"&areaY="+areaY+"&areaW="+areaW+"&areaH="+areaH+"&overlayName="+selected[i];
		$.ajax({
			url: overlayUrl,
			async: false,
			success: function (result) {
				for(var j = 0; j < result.length; j++) {
				    jsonObj.push(result[j]);
				}
			}
		});
	}
	closeFetchingPopup();
	if(jsonObj.length == 0){
		removeStage();
		MyMap.dragging.enable();
		return;
	}
	$("#container").css({top: canvas_y, left: canvas_x, position:'absolute'});
	
	initStage(canvas_width,canvas_height);
	setDrawingScale(scale);
	setDrawingOffset(offset);
	
	visualobjects = JSON.stringify(jsonObj);
	setDraggable(false);
	setEditable(false);
	LoadShapes(visualobjects, scale, offset);
}

function saveOverlays(){
	
	var recordid= Util.recordids[0];
	
	var min = MyMap.getPixelBounds().min;
	var max = MyMap.getPixelBounds().max;
	
	var canvas_x = 0;
	var canvas_y = 0;
	
	var zoom = MyMap._layers[22]._map._zoom;
	var max_zoom = MyMap._layers[22]._map._layersMaxZoom;
	var reverse_zoom = max_zoom-zoom;
	
	var imageWidth = Util.getData(recordid).imageWidth / Math.pow(2,reverse_zoom);
	var imageHeight = Util.getData(recordid).imageHeight / Math.pow(2,reverse_zoom);
	
	var canvas_width = max.x-min.x;
	var canvas_height = max.y-min.y;
	
	if(min.x<0){
		canvas_x = min.x * -1;
		canvas_width = max.x;
	}
	else{
		if(max.x>imageWidth){
			canvas_width = imageWidth-min.x;
		}
	}
	if(min.y<0){
		canvas_y = min.y * -1;
		canvas_height = max.y;
	}
	else{
		if(max.y>imageHeight){
			canvas_height = imageHeight-min.y;
		}
	}
	
	canvas_width = canvas_width < imageWidth ? canvas_width : imageWidth;
	canvas_height = canvas_height < imageHeight ? canvas_height : imageHeight;	
	
	$("#container").css({top: canvas_y, left: canvas_x, position:'absolute'});
	
	var areaX = min.x < 0 ? 0 : min.x;
	var areaY = min.y < 0 ? 0 : min.y;
	
	areaX *= Math.pow(2, reverse_zoom);
	areaY *= Math.pow(2, reverse_zoom);
	
	var areaW = canvas_width * Math.pow(2, reverse_zoom); 
	var areaH = canvas_height * Math.pow(2, reverse_zoom);
	
	var overlayUrl = Util.saveOverlayUrl(recordid)+"&areaX="+areaX+"&areaY="+areaY+"&areaW="+areaW+"&areaH="+areaH+"&overlayName="+editOverlayName;
	
	var scale = { x: 1/Math.pow(2, reverse_zoom), y: 1/Math.pow(2, reverse_zoom) };
	var offset = { x:areaX, y:areaY };	
	
	var visualObjects=JSON.stringify(Save(scale,offset,MyMap.getZoom()));
	
	showSavingPopup();
	var shapes = JSON.parse(visualObjects);
	$.ajax({
		type: "POST",
		url: overlayUrl,
		data:visualObjects,
		contentType: 'application/json',
		success: function (result) {
			
			shapes = JSON.parse(visualObjects);
			closeSavingPopup();
			setDirty(false);
			$('#container').remove();
			removeMapEvent(false);
			$('#mapcontainer').append('<div id="container" style="height:0px;width:0px;"></div>');
			addMapEvent(false);
			$('#dialog').dialog('close');
			displaySelectedOverlays();
		},
		error:function(data,status,er) {
            //alert("error: "+data+" status: "+status+" er:"+er);
			console.log("error: "+data+" status: "+status+" er:"+er);
        }
	});
}

function addMapEvent(editable){
	MyMap.dragging.disable();
	MyMap.on('zoomstart',removeStage);
	if(!editable){
		MyMap.on('zoomend',displaySelectedOverlays);
	}
	else{
		MyMap.on('zoomend',getOverlays);
	}
}

function removeMapEvent(editable){
	MyMap.off('zoomstart',removeStage);
	if(!editable){
		MyMap.off('zoomend',displaySelectedOverlays);
		MyMap.dragging.enable();
	}else{
		MyMap.off('zoomend',getOverlays);
		MyMap.dragging.disable();
	}
}

function showSavingPopup(){
	$( "#saveDialog" ).dialog( "open" );
}

function closeSavingPopup(){
	$( "#saveDialog" ).dialog( "close" );
}

function openSearchOverlayDialog(){
	$( "#searchOverlayDialog" ).dialog( "open" );
}

function closeSearchOverlayDialog(){
	$( "#searchOverlayDialog" ).dialog( "close" );
}

function showDeletingPopup(){
	$( "#deleteDialog" ).dialog( "open" );
}

function closeDeletingPopup(){
	$( "#deleteDialog" ).dialog( "close" );
}

function showFetchingPopup(){
	$( "#fetchDialog" ).dialog( "open" );
}

function closeFetchingPopup(){
	$( "#fetchDialog" ).dialog( "close" );
}
