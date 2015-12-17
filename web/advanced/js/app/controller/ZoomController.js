Ext.define('Manage.controller.ZoomController', {
	extend: 'Ext.app.Controller',
	views:[
		'zoom.ZoomPanel'
	],
	refs :[
	       {
	        ref: 'sliceSlider',
	        selector: 'imagesliders #sliceslider'
	    },{ 
	        ref:'frameSlider',
	        selector:'imagesliders #frameslider'
	    },{ 
	        ref:'channelControl',
	        selector:'imagecontrols #channelcontrol'
	    },{ 
	        ref:'overlaycontrol',
	        selector:'imagecontrols #overlaycontrol'
	    },{
	        ref : 'greyScale',
	        selector : 'imagetoolbar #button_colorimage'
	    },{
	        ref:'siteControl',
	        selector:'imagecontrols #sitecontrol'
	    } 
	]

//,
	
//	init: function() {
//		this.tileSize=512;
//		this.control({
//			'imagetoolbar #button_panzoom' : {
//		    	click : this.onZoomRequest
//			}
//		});	
//	},
//	
//	onZoomRequest:function(){
//		
//        var me=this;
//        var RecordSelection=this.getController('Manage.controller.RecordSelection');
//		var recordId=RecordSelection.activeRecordID;
//        var record = RecordSelection.getRecordItem(recordId);
//        
//        var checkBoxes = this.getChannelControl().getSelectionModel().getSelection();
//        var channelNumbers = new Array();
//        for (var i=0; i<checkBoxes.length; ++i) {
//            if (checkBoxes[i].data)
//                channelNumbers.push(checkBoxes[i].data.channelNumber);
//        }
//        
//        var overlaySelection = this.getOverlaycontrol().getSelectionModel().getSelection();
//        var overlayNames = new Array();
//        for (var i=0; i<overlaySelection.length; ++i) {
//            if (overlaySelection[i].data)
//            	overlayNames.push(overlaySelection[i].data.name);
//        }
//        
//		var zoomRequestParams={
//            guid : recordId,
//			sliceNumber:me.getSliceSlider().getValue(),
//			frameNumber:me.getFrameSlider().getValue(),
//			siteNumber:me.getSiteControl().items.items[0].getValue().site,
//			useChannelColor:!(me.getGreyScale().pressed),
//			channelNumbers:Ext.encode(channelNumbers),
//			overlayNames:Ext.encode(overlayNames)
//		};
//		
//        Ext.Ajax.request({
//            method : 'GET',
//            url : '../zoom/start',
//            params : zoomRequestParams,
//            success : function(result, request) {
//        	    var response = Ext.decode(result.responseText);
//                var zoomId = response["zoomId"];
//            	me.showZoomPanel(zoomId, record.get('Image Width'), record.get('Image Height'));
//        	},
//            failure : function(result, request) {
//                showErrorMessage(result.responseText, "Failed to start Zoom");
//            }
//        });
//		
//		
//
//		
//	},
//	
//	showZoomPanel: function(zoomId, recordWidth, recordHeight){
//		Ext.create ('Ext.window.Window', {
//			title : 'Zoom',
//            height : '100%',
//            width : '100%',
//            items : [
//				{
//					xtype : 'zoomPanel',
//					zoomId: zoomId,
//					recordWidth: recordWidth,
//					recordHeight:recordHeight
//				}
//            ]
//        }).show();
//		var viewer=this.createViewer(zoomId, recordWidth, recordHeight);
//	},
//	
//	createViewer: function(zoomId, width, height) {
//		var me=this;
//		var dom_id='zoomViewer_';
//		
//		var zoomViewer=document.getElementById(dom_id);
//		zoomViewer.innerHTML='';
//		//zoomViewer.style.width='100%';
//		//zoomViewer.style.height='100%';
//		
//		var myPyramid = new ZoomifyPyramid( width, height, this.tileSize);
//		var myProvider = new PanoJS.TileUrlProvider('','','');
//		
//		
//		viewer = new PanoJS(dom_id, {
//		    tileUrlProvider : myProvider,
//		    tileSize        : myPyramid.tilesize,
//		    maxZoom         : myPyramid.getMaxLevel(),
//		    imageWidth      : myPyramid.width, 
//		    imageHeight     : myPyramid.height,
//		    initialZoom		: 0,
//		    blankTile       : 'images/panojs/blank.gif',
//		    loadingTile     : 'images/spinner.gif'
//		});
//		
//		viewer.tileExistsMap={};
//
//		myProvider.assembleUrl = function(xIndex, yIndex, zoom) {
//			//project/getTileImage?recordid=1&sliceNumber=0&frameNumber=0&channelNumbers=[0]&siteNumber=0&isGreyScale=false&isZStacked=false&isMosaic=false&t=1337670339551&x=3&y=3&zoom=1.0
//		    //return url + '&x=' + xIndex + '&y=' + yIndex + '&zoom='+((zoom+1)*0.25);
//			//return url + 'r-' +zoom +'-'+ xIndex + '-' + yIndex+'.jpeg';
//			var zoomLevel= myPyramid.getMaxLevel()-zoom;
//			var params={
//					'zoomId':zoomId,
//					'xTile':xIndex,
//					'yTile':yIndex,
//					'zoomLevel':zoomLevel
//		    };
//			var tileKey=zoomLevel+','+xIndex+','+yIndex;
//			if(viewer.tileExistsMap[tileKey] !== true){
//				var reply=Ext.JSON.decode(syncAJAXcall('../zoom/waitForTileImage',params));
//				if(reply.exists == true){
//					viewer.tileExistsMap[tileKey]=true;
//				}
//				else{
//					viewer.tileExistsMap[tileKey] = false;
//					showErrorMessage(reply,'Image generation failed');
//				}
//			}
//			
//			var requestUrl = '../zoom/getTileImage'+ '?zoomId='+zoomId + '&xTile='+xIndex + '&yTile='+yIndex 
//				+ '&zoomLevel='+zoomLevel;
//			console.log(requestUrl);
//			return requestUrl;
//		
//			
//		}
//		
//		myProvider.thumbnailUrl = function() {
//			//project/getTileImage?recordid=1&sliceNumber=0&frameNumber=0&channelNumbers=[0]&siteNumber=0&isGreyScale=false&isZStacked=false&isMosaic=false&t=1337670339551&x=3&y=3&zoom=1.0
//		    //return url + '&x=' + xIndex + '&y=' + yIndex + '&zoom='+((zoom+1)*0.25);
//			//return url + 'r-' +zoom +'-'+ xIndex + '-' + yIndex+'.jpeg';
//			var params={
//					'zoomId':zoomId,
//		    };
//			var tileKey='thumbnail';
//			if(viewer.tileExistsMap[tileKey] !== true){
//				var reply=Ext.JSON.decode(syncAJAXcall('../zoom/waitForThumbnail',params));
//				if(reply.exists == true){
//					viewer.tileExistsMap[tileKey]=true;
//				}
//				else{
//					viewer.tileExistsMap[tileKey] = false;
//					showErrorMessage(reply,'Image generation failed');
//				}
//			}
//			
//			var requestUrl = '../zoom/getThumbnail'+ '?zoomId='+zoomId ;
//			console.log(requestUrl);
//			return requestUrl;	
//		}
//
//		Ext.EventManager.addListener( window, 'resize', callback(viewer, viewer.resize));
//		viewer.init();
//		return viewer;
//	}
});