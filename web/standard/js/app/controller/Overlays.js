/**
 * All controls related to the Image changes
 */
Ext.define('Manage.controller.Overlays', {
	extend: 'Ext.app.Controller',
	
	refs: [{
		ref: 'siteControl',
		selector: '#sitecontrol'
	}, {
		ref: 'imagePanel',
		selector: 'imagepanel'
	},{
		ref: 'sliceSlider',
		selector: 'imagesliders #sliceslider'
	}, { 
		ref:'frameSlider',
		selector:'imagesliders #frameslider'
	}
	],
	
	controllers : ['RecordController', 'ImageView', 'ImageControls','Zoom'],
	
	stores: ['OverlayLocation'],
	
	init: function() {
		this.control({
			'imageEditToolbar' : {
                loadOverlay : this.onLoadOverlay,
                saveOverlay : this.onSaveOverlay,
                addEllipse	: this.onAddEllipse,
                addRect	: this.onAddRect,
                addLine	: this.onAddLine,
                addFreeHand	: this.onAddFreeHand,
                addPolygon	: this.onAddPolygon,
                addText     : this.onAddText,
                addArrow     : this.onAddArrow
            },
			'overlays': {
				addOverlays: this.onAddOverlays,
				deleteOverlays: this.onDeleteOverlays,
				editOverlays: this.onEditOverlays,
				locateOverlays: this.onLocateOverlays
			},
			'imagepanel':{
				layoutChange:this.loadShapes
			}
		});
	},
	
	saveInProgress:false,
	
	/**
	 * JSON of shapes to be loaded to canvas
	 */
	shapes:'',
	
	/**
	 * last scale
	 */
	previousScale:'',
	
	/**
	 * last zoom offset
	 */
	previousZoomOffset:'',
	
	/**
	 * Called to set canvas and load shapes to canvas
	 * On changing layout first save the current objects 
	 * on canvas and then loads it again to resized canvas  
	 */
	loadShapes:function(){
		if(this.shapes==='')
			return;
		
		if(this.previousScale!=='')
			this.shapes=JSON.stringify(Save(this.previousScale,this.previousZoomOffset));
		
        var imagePanel = this.getImagePanel();
        var rc=this.getRecordControllerController();
        
        
        var width,height;
        
        var x= imagePanel.getActiveWidth()/rc.getRecordWidth();
        var y= imagePanel.getActiveHeight()/rc.getRecordHeight();
        
        if(this.getImageViewController().viewingMode==='zoom'){
        	width=this.getZoomController().winWidth*this.getZoomController().slider;
        	height=this.getZoomController().winHeight*this.getZoomController().slider;
        	x=1;
        	y=1;
        }else{
        	width=imagePanel.getActiveWidth();
        	height=imagePanel.getActiveHeight();
        }
       
        imagePanel.removeCanvas();
        imagePanel.initCanvas();
        imagePanel.setCanvas(width,height);        
        
        console.log(x+" "+y);

        var zoomOffset={'x':0,'y':0};
        if(this.getImageViewController().viewingMode==='zoom'){
        	x*=this.getZoomController().slider;
        	y*=this.getZoomController().slider;
        	zoomOffset['x']=this.getZoomController().winX;
        	zoomOffset['y']=this.getZoomController().winY;                	
        }
        var scale={'x':x,'y':y};
        
        setDrawingScale(scale);
        setDrawingOffset(zoomOffset);
        
        this.previousScale=scale;
        this.previousZoomOffset=zoomOffset;
        
        LoadShapes(this.shapes,scale,zoomOffset);
		
	},
	
	/**
	 * render visual objects for current overlay in editing mode
	 */
	renderVisualObjects:function(overlayname,scope,callback,args){
		
        var currentSlice = this.getSliceSlider().getValue();
        var currentFrame = this.getFrameSlider().getValue();
        if(this.getSiteControl().items.items[0]===undefined){
        	return;
        }
        var currentSite = this.getSiteControl().items.items[0].getValue().site;

        var recordid = this.getCurrentRecordId();
        if(recordid === null){
		return;
        }
        
        showConsoleLog("OverlaysControoler","renderVisualObjects","recordid="+recordid+" slice="+currentSlice+" frame="+currentFrame+" site="+currentSite+" overlay="+overlayname);
        
        var _this=this;
		Ext.Ajax.request({
            method : 'GET',
            url : '../record/getVisualObjects',
            params : {
                recordid : recordid,
                siteNumber : currentSite,
                frameNumber : currentFrame,
                sliceNumber : currentSlice, 
                overlay : overlayname,
                drawType : 'kinetic'
            },
            success : function(result, request) { 
            	_this.shapes=result.responseText;
            	setFirstLoadFlag(true);
            	_this.loadShapes();
            	setFirstLoadFlag(false);
                callback.apply(scope,args);
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to load visual overlay");
            }
        });
    },
	
	/**
     * Create toolbar for image overlay editing
     */
    onEditOverlays : function(view, overlayName) {

    	showConsoleLog("OverlaysController","onEditOverlays","overlayName="+overlayName);
    	
    	this.shapes='';
    	this.previousScale='';
        this.previousZoomOffset=''; 
        
    	this.getImagePanel().clearOverlayTransparancy();
    	this.renderVisualObjects(overlayName, this,this.doShowToolBar,[view, overlayName]);
    },
    
    /**
     * Save the overlay with the given name and dimensions and objects
     * NOTE: Save can be both a new overlay insert or an update to an existing overlay.
     * allSlices : should the overlay be saved on all slices
     * allFrames : should the overaly be saved on all frames
     */
    onSaveOverlay : function(recordid, overlayName, allSlices, allFrames, siteNo,isToolClosed) {
        var sliceNos = null, frameNos = null, i=0;
        if (allSlices) {
            var sliceCount = this.getSliceSlider().maxValue + 1;
            sliceNos = new Array();
            for (i=0 ; i < sliceCount; ++i)
                sliceNos.push(i);
        } else {
            sliceNos = [this.getSliceSlider().getValue()];
        }
        if (allFrames) {
            var frameCount = this.getFrameSlider().maxValue + 1;
            frameNos = new Array();
            for (i=0; i < frameCount; ++i)
                frameNos.push(i);
        } else {
            frameNos = [this.getFrameSlider().getValue()];
        }
        
        this.saveInProgress=true;  
        
        var imagePanel = _this.getImagePanel();
        var rc=_this.getRecordControllerController();        
        var x= imagePanel.getActiveWidth()/rc.getRecordWidth();
        var y= imagePanel.getActiveHeight()/rc.getRecordHeight();
        
        var zoomOffset={'x':0,'y':0};
        if(_this.getImageViewController().viewingMode==='zoom'){
        	x=1;
        	y=1;
        	x*=_this.getZoomController().slider;
        	y*=_this.getZoomController().slider;
        	zoomOffset['x']=_this.getZoomController().winX;
        	zoomOffset['y']=_this.getZoomController().winY;
        }
                     
        var scale={'x':x,'y':y};
               
    	visualObjects=JSON.stringify(Save(scale,zoomOffset));
    	
        var win = Ext.create('Ext.window.Window',{
        	modal:true,
        	closable:false,
        	width:200,
        	height:100,

		    layout: {
		        align: 'middle',
		        pack: 'center',
		        type: 'hbox'
		    },
		    
            items: [{
                xtype: 'label',
                text:'Saving...'
            }]
        });
        
        win.show();
    	
    	var me=this; 
        Ext.Ajax.request({
            method : 'POST',
            url : '../record/saveOverlay',
            params : {
                recordid : recordid,
                siteNumber : siteNo,
                frameNumbers : Ext.encode(frameNos),
                sliceNumbers : Ext.encode(sliceNos),
                overlay : overlayName,
                visualObjects : visualObjects,
                drawType : 'kinetic'
            },
            success : function(result, request) {
                // DO NOTHING
            	setDirty(false);
                me.saveInProgress=false;
                if(isToolClosed){
                	me.getRecordControllerController().onOverlayChange();
                }
                win.destroy();
            },
            failure : function(result, request) {
            	me.saveInProgress=false;
                showErrorMessage(result.responseText, "Failed to save overlay");
            }
        }); 
    },

    
    /**
     * Create and show image edit toolbar
     */
    doShowToolBar : function(view, overlayName){
    	_this=this;
        view.setDisabled(true);
        var currentRecord = this.getCurrentRecordId();
        var currentSite = this.getSiteControl().items.items[0].getValue().site;

        var toolbar = Ext.create('Manage.view.ImageEditToolBar', {
            xtype : 'imageEditToolbar',
            overlayName : overlayName,
            recordid : currentRecord,
            siteNo : currentSite,
            
            title : 'Edit '+overlayName,
            width : 300,
            closable:true,
            listeners : {
                beforeclose : {fn : function(panel, opts){ _this.closeEditOverlay(panel, opts, view)}, scope: this},
                beforedestroy : {fn : function(panel, opts){ _this.closeEditOverlay(panel, opts, view)}, scope: this}
            }
        });
        
        this.getImageControlsController().add(toolbar);        
    },
    
    closeEditOverlay: function(panel, opts, view) {
    	var _this = this;
		console.log('beforeclose');
        var callback = function() {
            setDirty(false);
            _this.getRecordControllerController().onOverlayChange();
            view.setDisabled(false);
			view.editor = null;
			_this.getImagePanel().removeCanvas();
        };
        panel.checkAndSave(callback,this.saveInProgress);
        this.shapes='';
    },
	
	/**
	 * delete specified overlay from current record and current site
	 */
	onDeleteOverlays: function(view, name)
	{
		var currentRecord = this.getCurrentRecordId();
        var currentSite = this.getSiteControl().items.items[0].getValue().site;
        var overlayName = name;
        
        showConsoleLog("OverlaysController","onDeleteOverlays","currentSite="+currentSite+" overlayName="+overlayName);
        
        Ext.Msg.confirm("Delete", "Are you sure you want to delete the overlay?", function(id)  {
            if (id === "yes") {
                Ext.Ajax.request({
                    method : 'POST',
                    url : '../record/deleteOverlay',
                    params : {
                        recordid : currentRecord,
                        siteNumber : currentSite,
                        overlay : overlayName 
                    },
                    success : function (result, response){
                        view.fireEvent("refreshOverlayList", currentRecord);
                    },
                    failure : function(result, request) {
                        showErrorMessage(result.responseText, "Failed to delete overlay");
                    }
                });
            }
        });
	},
	
	/**
	 * function creates new overlay on a record
	 */
	onAddOverlays: function(view)
	{
        var currentRecord = this.getCurrentRecordId();
        var currentSite = this.getSiteControl().items.items[0].getValue().site;
        
        showConsoleLog("OverlaysController","onAddOverlays","currentSite="+currentSite);

        var addOverlay = Ext.create('Ext.panel.Panel', {
            title : 'Overlay Name',
            height : 120,
            width : 300,
            closable : true,
            layout : 'fit',
            items : [{
                xtype : 'form',
                url : '../record/addOverlay',
                bodyPadding : 10,
                items : [{
                    xtype : 'textfield',
                    fieldLabel : 'Overlay Name',
                    name : 'overlay',
                    allowBlank : false
                }],
                buttons : [{
                    text : 'OK',
                    formbind : true,
                    handler : function() {
                        var form = this.up('form').getForm(); 
                        var _this = this;
                        if (form.isValid()) {
                            form.submit({
                                params : {
                                    recordid : currentRecord,
                                    siteNumber : currentSite
                                },
                                success : function(form, action) {
                                    // Refresh overlay listing 
                                    view.fireEvent("refreshOverlayList", currentRecord);
                                    addOverlay.close();
                                },
                                failure : function(form, action) {
                                    showErrorMessage(action.response.responseText, "Failed to add overlay");
                                }
                            });
                        }
                    }
                }]
            }]

        });
        
        this.getImageControlsController().add(addOverlay);
	},
	
	onLocateOverlays: function(name){
		var currentRecord = this.getCurrentRecordId();
        var currentSite = this.getSiteControl().items.items[0].getValue().site;
        var overlayName = name;
        
        console.log(overlayName);
        this.getOverlayLocationStore().load({params:{siteNumber:currentSite,overlay:overlayName,recordid:currentRecord}});
        
        var overlaylocations = Ext.create('Ext.panel.Panel', {	   		
	   		 title:'Overlay Locations',
			 id:'overlaysummary',
			 closable:true,
			 autoScroll: true,
			 flex: 1,
			 hideHeaders: true,
			 layout:'fit',
				
			 items:[{
				  xtype:'overlaylocation',
			 }]
        });
        
        this.getImageControlsController().add(overlaylocations);
	},
	
	/**
	 * return the current selected record
	 */
	getCurrentRecordId: function() {
		return this.getRecordControllerController().getCurrentRecordId();
	},
	
	onAddEllipse: function(){
		drawEllipse();
	},
	
	onAddRect: function(){
		drawRectangle();
	},
	
	onAddLine: function(){
		drawLine();
	},
	
	onAddFreeHand: function(){
		drawFreeHand();
	},
	
	onAddPolygon: function(){
		drawPolygon();
	},

	onAddText: function(){
		
        var win = Ext.create('Ext.window.Window',{
        	modal:true,
        	width:200,
        	height:150,

		    layout: {
		        align: 'middle',
		        pack: 'stretch',
		        type: 'hbox'
		    },
		    
            items: [{
                xtype: 'textfield',
                fieldLabel:'Text',
                name:'Text'
            }],
            
		    buttons:[{
		    	text:'Draw',
                listeners:{
                	click:function(){
                		var text=this.up().up().down('textfield').getValue();
                		if(text!==''){
                			setText(text);
                			drawText();
                		}
                		this.up().up().close();
                	}
                }		    	
		    }]
        });
        
        win.show();

	},
	
	onAddArrow: function(){
		drawArrow();
	}	
});
