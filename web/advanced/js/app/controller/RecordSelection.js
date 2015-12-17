/**
 * Events related to selection of record
 * Selection of a record can be done using
 * 1. Select thumbnail(s) for the record
 * 2. Select row(s) from the summary table
 * Related actions are:
 * 1. Get the record's image and load it in the image viewer
 * 2. Change the image controls as per the record's attributes
 */
 Ext.require(['Manage.view.imageview.LegendsFieldChooser']);
             
Ext.define('Manage.controller.RecordSelection', {
    extend: 'Ext.app.Controller',
    refs: [{
        ref: 'thumbnails',
        selector: 'thumbnails'
    }, {
        ref: 'summaryTable',
        selector: 'summarytable'
    },{
        ref: 'sliceSlider',
        selector: 'imagesliders #sliceslider'
    },{ 
        ref:'frameSlider',
        selector:'imagesliders #frameslider'
    },{ 
        ref:'channelControl',
        selector:'imagecontrols #channelcontrol'
    },{
        ref:'siteControl',
        selector:'imagecontrols #sitecontrol'
    },{
        ref:'legendLocationControl',
        selector:'legends #legendlocation > radiogroup'
    },{ 
        ref:'overlayControl',
        selector:'imagecontrols #overlaycontrol'
    },{
        ref : 'imageView',
        selector : 'imageview'
    },{
        ref : 'imagePanel',
        selector : 'imagePanel'
    },
	{
		ref:'zoomThumbnail',
		selector:'zoomThumbnail'
	},{
        ref : 'zoomButton',
        selector : 'imagetoolbar #button_panzoom'
    }, {
        ref : 'imagedisplay',
        selector : 'imageview #imagedisplay'
    }, {
       ref:'imagedisplaypanel', 
       selector:'imageview #imagedisplaypanel'
    }, {
        ref:'navigator',
        selector: 'navigator'
    }, {
        ref : 'channelcontrol',
        selector : 'imagecontrols #channelcontrol'
    }, {
        ref : 'recordmetadata',
        selector : 'recordmetadata'
    }, {
        ref : 'imagemetadata',
        selector : 'imagemetadata'
    }, {
        ref : 'attachments',
        selector : 'attachments'
    }, {
        ref : 'comments',
        selector : 'comments'
    },
    {
        ref : 'history',
        selector : 'history'
    },
    {
        ref: 'sliceField',
        selector: 'imagesliders #slicefield'
    }, {
        ref : 'frameField',
        selector : 'imagesliders #framefield'
    }, {
        ref : 'slicePanel',
        selector : 'imagesliders #slicepanel'
    }, {
        ref : 'framePanel',
        selector : 'imagesliders #framepanel'
    }, {
        ref : 'zproject',
        selector : 'imagetoolbar #button_zproject'
    }, {
        ref : 'greyScale',
        selector : 'imagetoolbar #button_colorimage'
    }, {
        ref : 'mosaic',
        selector : 'imagetoolbar #button_allchannel'
    }, {
        ref : 'rulerControl',
        selector : 'imagetoolbar #button_rulers'
    }, {
        ref : 'bookmarkButton',
        selector : 'imagetoolbar #button_bookmark'
    }, {
        ref : 'legendButton',
        selector : 'legends #button_legends'
    }, {
        ref : 'scalebarButton',
        selector : 'imagetoolbar #button_scalebar'
    }, {
        ref : 'fullres',
        selector : 'imagetoolbar #button_fullres'
    }, {
        ref : 'sliceButton1',
        selector : 'imagesliders #button_previous_1'
    }, {
        ref : 'sliceButton2',
        selector : 'imagesliders #button_forward_1'
    }, {
        ref : 'imageDownload',
        selector : 'imagetoolbar #button_imagedownload'
    }, {
        ref : 'userAnnotations',
        selector : 'userAnnotations'
    }, {
        ref : 'sliceMovie',
        selector : 'imagesliders #button_movie1'
    }, {
        ref : 'frameMovie',
        selector : 'imagesliders #button_movie2'
    }],
    
    init: function() {
        this.control({
            'thumbnails': {
                selectionchange: this.onThumbnailRecordSelect
            }, 'summarytable':{
                selectionchange: this.onSummaryRecordSelect
            }, 'imagesliders #sliceslider' : {
                changecomplete : this.onImageControlChange
            }, 'imagesliders #frameslider' : {
                changecomplete : this.onImageControlChange
            }, 'imagecontrols #channelcontrol' : {
                select : this.onChannelControlChange,
                deselect : this.onChannelControlChange
            }, 'imagecontrols #sitecontrol > radiogroup' : {
                change : this.onSiteControlChange
            }, 'attachments' : {
                addattachment: this.onAddAttachment
            }, 'history' : {
                filterHistory: this.onFilterHistory
            }, 'legends' : {
                chooseColumns: this.onChooseLegends
            }, 'legends #legendlocation > radiogroup' : {
                change : this.onLegendLocationChange
            }, 'legends #legendFields > gridpanel' : {
                change : this.onLegendFieldChange
            },'legendsFieldChooser' : {
                okclicked : this.onLegendFieldViewOkClicked
            }, 'legends #button_legends' : {
                click : this.onLegendButtonClick
            }, 'imagetoolbar #button_scalebar' : {
                click : this.onScalebarButtonClick
            }, 'imagecontrols #overlaycontrol': {
                selectionchange : this.onOverlayChange,
                refreshOverlayList : this.setOverlays
            }, 'imagetoolbar #button_zproject' : {
                toggle : this.onZStackToggle
            }, 'imagetoolbar #button_colorimage' : {
                toggle : this.onImageToolbarToggle
            }, 'imagetoolbar #button_allchannel' : {
                toggle : this.onChannelTileToggle
            }, 'imagetoolbar #button_rulers' : {
                toggle : this.onRulersToggle
            }, 'imagetoolbar #button_thumbnail' : {
                click : this.onThumbnailButtonClick
            }, 'imagetoolbar #button_fullres' : {
                toggle : this.onImageToolbarToggle
            }, 'imagetoolbar #button_imagedownload' : {
                click : this.onImageDownload
            }
        });
        
        this.application.on({
        	overlayChanged  : {
        		fn:this.onOverlayChange,
        		scope : this	
        	}        
        });
    },
    
    /**
     * called when either slice or frame slider is changed. Refreshes the image
     */
    onImageControlChange : function() {
    	// If there is an active image overlay editor, close it
    	var recordid = this.getThumbnails().getSelectionModel().getLastSelected().data.id;
        if (recordid) {
        	this.setImage(recordid);
        	this.onOverlayChange();
        }
        
    },

    /**
     * Channel control change should not change the overlay panel. Treating it separately
     */
    onChannelControlChange : function() {
        var recordid = this.getThumbnails().getSelectionModel().getLastSelected().data.id;
        if (recordid) {
            this.setImage(recordid);
        }
    },

    /**
     * When any of the toggles in the image toolbar are flipped
     */
    onImageToolbarToggle : function(button, pressed, opts) {
        var recordid = this.getThumbnails().getSelectionModel().getLastSelected().data.id;
        // Get the image fresh
        this.setImage(recordid);
    },

    /**
     * When z stack button is toggled. slice slider should be disabled/enabled accordingly
     */
    onZStackToggle : function(button, pressed, opts) {
        this.getSliceSlider().setDisabled(pressed); 
        this.getSliceButton1().setDisabled(pressed);
        this.getSliceButton2().setDisabled(pressed);
        if (pressed)
            this.hideMovieControls();
        else
            this.showMovieControls();
        
        var recordid = this.getThumbnails().getSelectionModel().getLastSelected().data.id;
        console.log("selected record id is");
        console.log(recordid);
        this.loadContrastSettings(recordid,this.onImageToolbarToggle, this);
    },
    
    /**
     * When channel tile toggle is pressed. If it is enabled, channel selection should
     * be disabled
     */
    onChannelTileToggle : function(button, pressed, opts) {
        this.getChannelcontrol().getSelectionModel().setLocked(pressed);
        if (pressed) {
            this.getOverlayControl().getSelectionModel().deselectAll();
            this.hideMovieControls();
        } else {
            this.showMovieControls();
        }
        this.getOverlayControl().setDisabled(pressed);
        this.onImageControlChange(button, pressed, opts);
    },
    
    onRulersToggle: function(button, pressed, opts){
    	var recordid = this.getThumbnails().getSelectionModel().getLastSelected().data.id;
		if (recordid) {
			if (pressed) {
				this.getImageView().showRulers();
			}
			else {
				this.getImageView().hideRulers();
			}
			this.setImage(recordid);
		}
    },

    /**
     * Called when site control is changed. The radio button change is actually two events
     * with two selected as first step and first deselected in the second step.
     * Ignoring the first of these events
     */
    onSiteControlChange : function(view,  newValue, oldValue, opts) {
        if (newValue.site.length > 1)
            return;
        this.onImageControlChange();
    },

    /**
     * Thumbnail selection. Also triggers summaryTable selection without event
     */
    onThumbnailRecordSelect: function(selModel, selection) {
        // If no selection don't do anything
        if (selection === null || selection.length <= 0)
            return;
        
        var node = this.getThumbnails().getNode(selection[selection.length-1]);
        if (node && node !== null)
            this.getThumbnails().focusNode(node);

        // Select the same record in the summarytable view also
        var itemsToSelect = new Array();
        for (var i=0; i < selection.length; ++i) {
            var recordID = selection[i].data.id;
            var record = this.getRecordItem(recordID);
            if (record)
                itemsToSelect.push(record);
        }
        this.getSummaryTable().getSelectionModel().select(itemsToSelect, false, true);
        this.getSummaryTable().getView().focusRow(itemsToSelect[itemsToSelect.length-1]);

        // If more than one selected don't do anything further
        if (selection.length > 1)
            return;

      //Check and Save VO editor state call onRecordSelect in callback    	
        this.checkAndSaveVOEditor(this.onRecordSelect,selection[0].get('id'));
    },
   
    /**
     * Summary table selection. Also triggers thumbnail selection
     */ 
    onSummaryRecordSelect: function(selModel, selection) {
        // If no selection don't do anything
        if (selection === null || selection.length <= 0)
            return;
        
        // Select the same record in the thumbnails view also
        var itemsToSelect = new Array();
        for (var i=0; i < selection.length; ++i) {
            var recordID = selection[i].data["Record ID"];
            var item = this.getThumbnails().store.getById(recordID);
            if (item)
                itemsToSelect.push(item);
    	}
        this.getThumbnails().getSelectionModel().select(itemsToSelect, false, true);
        var node = this.getThumbnails().getNode(itemsToSelect[itemsToSelect.length-1]);
        if (node && node !== null) {
            this.getThumbnails().focusNode(node);
        }
        //this.getThumbnails().getView().focusRow(itemsToSelect[itemsToSelect.length-1]);

        // If more than one selected don't do anything further
        if (selection.length > 1)
            return;


    	//Check and Save VO editor state call onRecordSelect in callback    	
        this.checkAndSaveVOEditor(this.onRecordSelect,selection[0].get('Record ID'));
    },
   
   /**
     * Record selection final call. Both summary table and thumbnail selection call this method
     */ 
    onRecordSelect: function(recordid){
    	//page = this;
        this.activeRecordID = recordid;
        // Summary table store has all the information about a record. Get it from there
        // Get more information about the record from a call
        var recordItem = this.getRecordItem(recordid);
        if (recordItem === null) {
            return;
        }
        this.application.fireEvent('recordSelectionChanged',recordid);
        var _this = this;
        Ext.Ajax.request({
            method : 'GET',
            url : '../record/getRecordData',
            params : {
                recordid : recordid
            },
            success : function (result, request) {
                var data = Ext.decode(result.responseText);
                _this.loadRecordData(recordid, data);
            },
            failure : function (result, request) {
                showErrorMessage(result.responseText, "Failed to load record data");
            }
        });
    },
 
    /**
     * Load record data
     */
    loadRecordData : function (recordid, record) {
        // Set image controls
        this.setImageControls(record["Slice Count"], record["Frame Count"], record["Channels"], record["Sites"]);
        
     	// Set record meta data
        this.setRecordMetaData(record);
        
        // for .ND files disable movie
        var store = this.getRecordmetadata().getStore();
        var rec = store.findRecord("name", "Source File");
        /*if(rec !=null )
        {
        	if(rec.data.value.toLowerCase().lastIndexOf(".nd") > 0)
        	{
        		this.disbleMovieControls();
        	}
        	else
        	{
        		this.enableMovieControls();
        	}
        }*/

        //load constrast settings
        //setimage() is called as callback for load 
        this.loadContrastSettings(recordid,this.changeRecordAndsetImage,this,recordid);
    	
    	// Set overlays for the record
        this.setOverlays(recordid);
        
    	// Disable zoom on record change
    	this.getZoomButton().toggle(false);

        // set up attachments for this record
        this.setAttachments(recordid);

        // set up comments for this record
        this.setComments(recordid);
        
        // set up history for this record
        this.setHistory(recordid, null, null, null);

        // set bookmark state
        this.setBookmarkState(recordid);
        
        // set user annotations
        this.setUserAnnotations(recordid);
        
        
    },
    
    changeRecordAndsetImage:function(recordid){
    	var record = this.getRecordItem(recordid).data;
    	this.getImagePanel().abortImageReaderPolling();
    	this.setImage(recordid);
        this.getImagePanel().changeRecord(record);
    },
    
    loadContrastSettings:function(recordid,fn,scope,args){
    	var isZStacked = this.getZproject().pressed;
    	var store = Ext.StoreManager.get('Manage.store.ChannelContrastStore');
    	store.load({
    		params : {
				'recordid':recordid,
				'isZStacked':isZStacked
			},
			callback : function(){
				fn.call(scope,args);
			}
    	});
    },
    
    checkAndSaveVOEditor: function(pcallback,args){
    	var me=this;
    	var overlayPanel = this.getOverlayControl();
        // If there is an active image overlay editor, close it
        if (overlayPanel.editor && overlayPanel.editor !== null) {
            var editor = overlayPanel.editor;
            overlayPanel.editor = null;
            
            var toolbar= editor.down('imageEditToolbar');
            var callback = function() {
                sketchpad.purge();
                sketchpad.setDirty(false);
                sketchpad.mode(false);
                overlayPanel.setDisabled(false);
                pcallback.call(me,args);
                editor.close();
            };
            toolbar.checkAndSave(callback);
        }
        else{
        	pcallback.call(me,args);
        }
    },

    /**
     * Set overlays for this record
     */
    setOverlays : function(recordid, overlayName) {
    	// Get the latest values for slice, frame and site
        var sliceNumber = this.getSliceSlider().getValue();
        var frameNumber = this.getFrameSlider().getValue();
        var siteNumber = this.getSiteControl().items.items[0].getValue().site;
        var _this = this;
        Ext.Ajax.request({
            method : 'GET',
            url : '../record/getVisualOverlays',
            params : {
                recordid : recordid,
                siteNumber : siteNumber,
                frameNumber : frameNumber,
                sliceNumber : sliceNumber
            },
            success : function(result, request) {
                var overlays = Ext.decode(result.responseText);
                if (overlays !== null)
                    _this.loadOverlays(overlays, overlayName);
            },
            failure : function(result, request){
                showErrorMessage(result.responseText, "Failed to fetch visual overlays");
            }
        });
    },
    

    /**
     * Load the overlays to the panel
     */
    loadOverlays : function(values, overlayName) {
        var overlayStore = Ext.StoreManager.get('Manage.store.OverlayStore');
        overlayStore.loadData(values);
        if (overlayName && (overlayName !== null) ) {
            // set this overlayname as selected
            var overlay = overlayStore.getById(overlayName);
            if (overlay !== null)
                this.getOverlayControl().getSelectionModel().select(overlay);
        }
        // Call to cleanup any old state on the sketchpad
        this.onOverlayChange();
    },
    
    
    drawHandCreatedOverlays: function(){

    	//var record = this.getRecordItem(recordid).data;
        //this.getImagePanel().changeRecord(record);
        
    	var checkBoxGrid = Ext.ComponentQuery.query('imagecontrols #overlaycontrol')[0];
        var selected = checkBoxGrid.getSelectionModel().getSelection();
        var overlays = new Array();
        for (var i=0; i< selected.length; ++i) {
            overlays.push(selected[i].data);
        }
        sketchpad.purge();
        
        
        var currentSlice = this.getSliceSlider().getValue();
        var currentFrame = this.getFrameSlider().getValue();
        if(this.getSiteControl().items.items[0]===undefined){
        	return;
        }
        var currentSite = this.getSiteControl().items.items[0].getValue().site;

        var recordid = this.activeRecordID;
        if(recordid == null){
        	return;
        }
        
        for (var j=0; j < overlays.length; ++j) {
        	var next = overlays[j];
        	if(next.handCreated){
        		Ext.Ajax.request({
                    method : 'GET',
                    url : '../record/getVisualObjects',
                    params : {
                        recordid : recordid,
                        siteNumber : currentSite,
                        frameNumber : currentFrame,
                        sliceNumber : currentSlice, 
                        overlay : next.name
                    },
                    success : function(result, request) {
                        sketchpad.json(result.responseText, true);
                    },
                    failure : function(result, request) {
                        showErrorMessage(result.responseText, "Failed to load visual overlay");
                    }
                });
        	}
        }
    },
    
    renderVisualObjects:function(overlayname,scope,callback,args){
		
        var currentSlice = this.getSliceSlider().getValue();
        var currentFrame = this.getFrameSlider().getValue();
        if(this.getSiteControl().items.items[0]===undefined){
        	return;
        }
        var currentSite = this.getSiteControl().items.items[0].getValue().site;

        var recordid = this.activeRecordID;
        if(recordid == null){
        	return;
        }
        
        var _this=this;
    	Ext.Ajax.request({
            method : 'GET',
            url : '../record/getVisualObjects',
            params : {
                recordid : recordid,
                siteNumber : currentSite,
                frameNumber : currentFrame,
                sliceNumber : currentSlice, 
                overlay : overlayname
            },
            success : function(result, request) {
            	
                var visibleBox = _this.getImagePanel().imageConfig.visible;
        		var imagePanel = _this.getImagePanel();
        		imagePanel.setViewBox(visibleBox.x1,visibleBox.y1,
        				visibleBox.width,visibleBox.height);		
        		
        		sketchpad.purge();
                sketchpad.json(result.responseText, true);
                
                imagePanel.fitCanvasSize();
        		
        		callback.apply(scope,args);
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to load visual overlay");
            }
        });
    },
    
    /**
     * Load overlays based on the change made
     */
    onOverlayChange : function() {
		
        var recordid = this.activeRecordID;
        console.log("on overlay change");
        console.log(recordid);
        if(recordid != null){
        	//this.setImage(recordid);
        	
        	//var imageViewController=this.getController('Manage.controller.ImageViewController');
        	//imageViewController.refreshOverlays();
        	
        	var url=this.getOverlayTransparencyUrl();
        	console.log(url);
        	this.getImagePanel().setOverlayTransparancy(url);
        }
    },
    
    /**
     * set up attachments for this record
     */
    setAttachments : function(recordid) {
        var attachments = this.getAttachments();
        attachments.recordid = recordid;
        
        attachments.down('[id=userattachments]').store.load({
            params : {
                recordid : recordid
            }
        });
        
        attachments.down('[id=systemattachments]').store.load({
            params : {
                recordid : recordid
            }
        });
    },
    
    /**
     * add attachment to this record
     */
    onAddAttachment : function() {
        var recordid = this.activeRecordID;
        var _this = this;

    	// Get the recordid
    	Ext.create('Ext.window.Window', {
            title : 'Add Attachment',
            /*layout : { 
                type: 'vbox',
		        align: 'stretch'
            },*/
            layout : 'fit',
            width : 300,
            height : 200,
            items : [{
                xtype : 'form',
                bodyPadding : 10,
                items : [{
                    xtype : 'filefield',
                    name : 'attachmentFile',
                    fieldLabel : 'Attachment File',
                    allowBlank : false
                },{
                    xtype : 'textfield',
                    fieldLabel : 'Notes',
                    name : 'notes',
                    allowBlank : false
                }, {
                    xtype : 'hidden',
                    name : 'recordid',
                    value : recordid
                }],
                url : '../project/addRecordAttachments',
                buttons : [{
                    text : 'Add',
                    handler : function() {
                        // add attachment to the recor
                        var view = this.up().up().up();
                        var form = this.up('form').getForm();
                        if (form.isValid()) {
                            form.submit({
                                success: function(form, action) {
                                    view.close();
                                    _this.setAttachments(recordid);
                                },
                                failure: function(form, action) {
                                    showErrorMessage(action.response.responseText, "Failed to add attachment");
                                }
                            }); 
                        }
                    }
                }]
            }]
        }).show(); 
    	// Add attachment to the record id
    },
    
    /**
    * choose legend fields
    */
    onChooseLegends: function() {
    	console.log("on choose legends");
    	
    	var projectName = this.getController('Manage.controller.NavigatorState').activeProject;
    	
    	var columnStore = Ext.StoreManager.get('Manage.store.LegendFieldStore');
        var loadParams = {
            params: {
                projectName : projectName
            }
        };
        columnStore.load(loadParams);
        
    	Ext.create('Ext.window.Window', {
                        title : 'Choose Columns',
                        layout : 'fit',
                        width : 500,
                        height : 500,
                        items : [{
                            xtype : 'legendsFieldChooser'
                        }] 
           			}).show();
    },
    
    /**
    * choose legend location
    */
    onLegendLocationChange: function() {
    	var legendLocation = 'top';
    	if(this.getLegendLocationControl().items.items[0].checked)
    		legendLocation = 'top';
    	else
    		legendLocation = 'bottom';
    	
    	this.getImagePanel().changeLegendsLocation(legendLocation);
    },
    
    /**
    * choose legend fields
    */
    onLegendFieldChange: function() {
    	console.log("legend field change");
    },
    
    /**
    * choose legend fields
    */
    onLegendFieldViewOkClicked: function(view, selected, available) {
    	console.log("legend field ok pressed");
    	var chosenFields = new Array();
        var i;
        
        var projectName = this.getController('Manage.controller.NavigatorState').activeProject;
        
        for (i=0; i < selected.length; ++i ) {
            chosenFields.push(selected[i].data.name);
        }
        console.log(chosenFields);
        
        var me = this;
        // Write to server
        Ext.Ajax.request({
            method : 'POST',
            url : '../annotation/setLegendsChosen',
            params : {
                projectName : projectName,
                chosenFields : Ext.encode(chosenFields)
            },
            success : function(result, req) {
            	me.onLegendButtonClick();
            },
            failure : function(result, req) {
                showErrorMessage(action.result.responseText, "Legends fields saving to server failed");
            }
        });
    },
    
    /**
    * choose legend fields
    */
    onLegendButtonClick: function() {
    	console.log("legend button click");
    	var legendButton = this.getLegendButton();
    	console.log(legendButton.pressed);
    	this.getImagePanel().displayLegend(legendButton.pressed);
    },
    
    /**
    * choose legend fields
    */
    onScalebarButtonClick: function() {
    	console.log("scalebar button click");
    	this.onOverlayChange();
    },
    
    /**
     * add attachment to this record
     */
    onFilterHistory : function() {
        var recordid = this.activeRecordID;
        var _this = this;

    	// Get the recordid
    	Ext.create('Ext.window.Window', {
            title : 'Filter History',
            /*layout : { 
                type: 'vbox',
		        align: 'stretch'
            },*/
            layout : 'fit',
            width : 350,
            height : 200,
            items : [{
                xtype : 'form',
                bodyPadding : 10,
                items : [
                         {
                 			fieldLabel : 'History Type',
                			xtype : 'combo',
                	        name : 'historytype',
                	        queryMode : 'local',
                	        displayField : 'name',
                	        valueField : 'value',
                	        width : 300,
                	        editable : true,
                	        typeAhead: true,
                	        store : 'Manage.store.HistoryTypeStore'
                         },
                         {
                        	 fieldLabel : 'From date',
                        	 name : 'fromDate',
                        	 xtype : 'datefield'
                         }, {
                        	 fieldLabel : 'To date',
                        	 name : 'toDate',
                        	 xtype : 'datefield'
                         }, {
                             xtype : 'hidden',
                             name : 'recordid',
                             value : recordid
                         }
                         ],
                url : '../record/getHistory',
                buttons : [{
                    text : 'Filter',
                    handler : function() {
                        // filter history
                        var view = this.up().up().up();
                        var form = this.up('form').getForm();
                        if (form.isValid()) {
                        	var fromDateTime = null;
                        	if(form.getFieldValues()['fromDate']!==null)
                        		fromDateTime = form.getFieldValues()['fromDate'].getTime();
                        	
                        	var toDateTime = null;
                        	if(form.getFieldValues()['toDate']!==null)
                        		form.getFieldValues()['toDate'].getTime();
                        	_this.setHistory(recordid, form.getFieldValues()['historytype'], fromDateTime, toDateTime);
                        }
                    }
                }]
            }]
        }).show(); 
    },
    

    /**
     * set up comments for this record
     */
    setComments : function(recordid) {
        var commentsView = this.getComments();
        commentsView.updateComments(recordid);
    },
    
    /**
     * set up history for this record
     */
    setHistory : function(recordid, historytype, fromDate, toDate) {
        var historyView = this.getHistory();
        historyView.updateHistory(recordid, historytype, fromDate, toDate);
    },

    /**
     * Set image controls
     */ 
    setImageControls : function(sliceCount, frameCount, channels, sites){
    	
    	this.getSliceField().setText("Z: 1/" + (sliceCount));
    	this.getFrameField().setText("T: 1/" + (frameCount));
    	
        this.getSliceSlider().setMaxValue(sliceCount-1);
        this.getFrameSlider().setMaxValue(frameCount-1);
        
        this.getSliceSlider().setValue(0);
        this.getFrameSlider().setValue(0);
        
        if (sliceCount <= 1) {
            this.getSlicePanel().hide();
        } else {
            this.getSlicePanel().show();
        }

        if (frameCount <= 1) {
            this.getFramePanel().hide();
        } else {
            this.getFramePanel().show();
        }

        var channelStore = Ext.StoreManager.get('Manage.store.ChannelStore');
        channelStore.loadData(channels);

        this.setSiteControls(sites);
        var lockState = this.getChannelcontrol().getSelectionModel().isLocked() || false;
        this.getChannelcontrol().getSelectionModel().setLocked(false);
        this.getChannelcontrol().getSelectionModel().selectAll(true);
        this.getChannelcontrol().getSelectionModel().setLocked(lockState);
    },

    /**
     * construct image url 
     * */
    constructImageViewUrl : function(recordid){
//    	var sliceCount = this.getSliceSlider().getValue();
//        var frameCount = this.getFrameSlider().getValue();
//        var maxSlices =  this.getSliceSlider().maxValue + 1;
//        var maxFrames = this.getFrameSlider().maxValue + 1;
//       
//        var isGreyScale = this.getGreyScale().pressed;
//        var isZStacked = this.getZproject().pressed;
//        var isMosaic = this.getMosaic().pressed;
//        this.getSliceField().setText("Z: " + (sliceCount + 1) + "/" + maxSlices);
//        this.getFrameField().setText("T: " + (frameCount + 1)  + "/" + maxFrames);
//        
//        var siteNumber = this.getSiteControl().items.items[0].getValue().site;
//        var checkBoxes = this.getChannelcontrol().getSelectionModel().getSelection();
//        var channelNumbers = new Array();
//        for (var i=0; i<checkBoxes.length; ++i) {
//            if (checkBoxes[i].data)
//                channelNumbers.push(checkBoxes[i].data.channelNumber);
//        }
//        var channelNumbersURI = encodeURIComponent(Ext.encode(channelNumbers));
//        
//        var overlayNames = new Array();
//        var checkBoxGrid = Ext.ComponentQuery.query('imagecontrols #overlaycontrol')[0];
//        var selected = checkBoxGrid.getSelectionModel().getSelection();
//        for (var j=0; j< selected.length; ++j) {
//        	if(!selected[j].data.handCreated)
//        		overlayNames.push(selected[j].data.name);
//        }
//        var overlayNamesURI = encodeURIComponent(Ext.encode(overlayNames));
//        
//        
//        var imageURL = '../project/getImage?recordid='+recordid+'&sliceNumber='+sliceCount+'&frameNumber='+frameCount+'&channelNumbers='+channelNumbersURI+'&siteNumber='+siteNumber;
//        imageURL += '&isGreyScale='+isGreyScale + '&isZStacked=' + isZStacked + '&isMosaic=' + isMosaic;
//        imageURL += '&overlays=' + overlayNamesURI;
//        if (!this.getFullres().pressed){
//        	imageURL += '&height=512';
//    	}
//            
//        imageURL += '&t='+(new Date()).getTime();
//        return imageURL;
    },
    
    
    /**
     * Set image in the image viewer
     */
    setImage : function(recordid){
    	
    	var sliceCount = this.getSliceSlider().getValue();
        var frameCount = this.getFrameSlider().getValue();
        
        var maxSlices =  this.getSliceSlider().maxValue + 1;
        var maxFrames = this.getFrameSlider().maxValue + 1;
        this.getSliceField().setText("Z: " + (sliceCount + 1) + "/" + maxSlices);
        this.getFrameField().setText("T: " + (frameCount + 1)  + "/" + maxFrames);
        
        var siteNumber = this.getSiteControl().items.items[0].getValue().site;
        
        var checkBoxes = this.getChannelcontrol().getSelectionModel().getSelection();
        var channelNumbers = new Array();
        for (var i=0; i<checkBoxes.length; ++i) {
            if (checkBoxes[i].data)
                channelNumbers.push(checkBoxes[i].data.channelNumber);
        }
    	this.setImageMetaData(recordid, sliceCount, frameCount, siteNumber, channelNumbers);
    	
    	var imageURL=this.getImageURL(recordid,false);
    	
    	var record = this.getRecordItem(recordid).data;
    	var imageViewController=this.getController('Manage.controller.ImageViewController');
    	imageViewController.setImage(imageURL, record);
    },
 
    /**
     * Set image metadata table for the given record and record attributes
     */
    setImageMetaData : function (recordid, sliceCount, frameCount, siteNumber, channelNumbers) {
        var imageMetaData = this.getImagemetadata();
        var fields = new Array();
        imageMetaData.columns = new Array();
        fields.push('field');
        imageMetaData.columns.push({header: 'Field', dataIndex: 'field', flex : 1});
        imageMetaData.headerCt.removeAll();
        imageMetaData.headerCt.add(Ext.create('Ext.grid.column.Column', {text: 'Field', dataIndex: 'field', flex : 1}));
        for (var i=0; i < channelNumbers.length; ++i) { 
            fields.push(channelNumbers[i]+"");
            imageMetaData.columns.push({header: channelNumbers[i]+"", dataIndex: channelNumbers[i]+""});
            imageMetaData.headerCt.add(Ext.create('Ext.grid.column.Column', {text: channelNumbers[i]+"", dataIndex: channelNumbers[i]+"", flex : 1}));
        }
        imageMetaData.getView().refresh();
        imageMetaData.store.fields = fields;
        Ext.Ajax.request( {
            method : 'GET',
            url : '../project/getImageMetaData',
            params : {
                recordid : recordid,
                sliceNumber : sliceCount,
                frameNumber : frameCount,
                siteNumber : siteNumber
            },
            success : function(result, response) {
                var data = Ext.decode(result.responseText);
                imageMetaData.store.loadData(data);
            },
            failure : function(result, response) {
                showErrorMessage(result.responseText, "Failed to load image meta data");
            }
        });
    },
    
    /**
     * Set record metadata table with values from the selected record
     */ 
    setRecordMetaData : function(record) {
        // Convert data to "Name", "Value" map
        var recordMetaData = new Array();
        for (var key in record) {
            value = record[key];
            if (!(value instanceof Object))
                recordMetaData.push({"name": key, "value": value});
        }
        // Load data to RecordMetaDataStore
        var store = this.getRecordmetadata().getStore();
        // store.loadData({metadata: recordMetaData});
        store.loadData(recordMetaData);
    },

    /**
     * Utility to find a record item given record id
     */
    getRecordItem : function(recordid) {
        var summaryTableItems = this.getSummaryTable().store.data.items;
        for (var index=0; index < summaryTableItems.length; ++index) {
            var nextItem = summaryTableItems[index];
            if (nextItem.data["Record ID"] === recordid) {
                return nextItem;
            }
        }
        return null;
    },

 
    /**
     * Utility to set checkbox values
     */
    setCheckBoxes:function(group,values){
       group.removeAll();
        for(var i = 0; i<values.length; i++){
            //alert(sites[i]);
             var checkbox = Ext.create('Ext.form.Checkbox', {'boxLabel':values[i],'inputValue':values[i], 'value' : true, 'checked' : true});
             //var col = group.panel.items.get(group.items.getCount() % group.panel.items.getCount());
             group.items.add(checkbox);
             //col.add(checkbox);
         }
        group.doLayout();
    },

    /**
     * Set radio buttons for the site control
     */
    setSiteControls : function(sites) {
        var sitePanel = this.getSiteControl();
        sitePanel.removeAll();
        var items = new Array();
        var checked = true;
        for (var i=0; i < sites.length; ++i) {
            var radio = Ext.create('Ext.form.field.Radio', {
                boxLabel : sites[i].name,
                name : 'site',
                checked : checked,
                inputValue : i,
                id : i+""
            });
            items.push(radio);
            if (i === 0)
                checked = false;
        }
        if (items.length > 0)
            items[0].checked = true;

        var group = Ext.create('Ext.form.RadioGroup', {
            hideLabel : true,
            items : items,
            vertical : true,
            layout : 'anchor'
        });
        sitePanel.items.add(group);
        sitePanel.doLayout();
        //group.doLayout();
    },
    
    /**
     * Bookmark button indicates if the record is bookmarked or not. Set the state of the button accordingly
     */
    setBookmarkState : function(recordid) {
        var bmButton = this.getBookmarkButton();
        Ext.Ajax.request({
            method : 'GET',
            url : '../project/isBookmark',
            params : {
                recordid : recordid
            },
            success : function(result, request) {
                var response = Ext.decode(result.responseText);
                if (response.isBookmark) {
                    bmButton.toggle(true);
                    bmButton.setTooltip("Click to remove bookmark");
                } else {
                    bmButton.toggle(false);
                    bmButton.setTooltip("Click to add bookmark");
                }
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to set the bookmark button state");
            }
        });
    },
    
    /**
     * Set the current image as the thumbnail for the current record
     */
    onThumbnailButtonClick : function() {
        var recordid = this.activeRecordID;
        var sliceCount = this.getSliceSlider().getValue();
        var frameCount = this.getFrameSlider().getValue();
       
        var isGreyScale = this.getGreyScale().pressed;
        var isZStacked = this.getZproject().pressed;
        var isMosaic = this.getMosaic().pressed;
        
        var siteNumber = this.getSiteControl().items.items[0].getValue().site;
        var checkBoxes = this.getChannelcontrol().getSelectionModel().getSelection();
        var channelNumbers = new Array();
        for (var i=0; i<checkBoxes.length; ++i) {
            if (checkBoxes[i].data)
                channelNumbers.push(checkBoxes[i].data.channelNumber);
        }
        var thumbnailView = this.getThumbnails();
        Ext.Ajax.request({
            method : 'POST',
            url : '../record/setThumbnail',
            params : {
                recordid : recordid,
                sliceNumber : sliceCount,
                frameNumber : frameCount,
                siteNumber : siteNumber,
                isGreyScale : isGreyScale,
                isZStacked : isZStacked,
                isMosaic : isMosaic,
                channelNumbers : Ext.encode(channelNumbers)
            },
            success : function(result, request) {
            	thumbnailView.getStore().findRecord('id',recordid).set('imagesource',
            			'../project/getThumbnail?recordid=' + recordid + '&t='+Math.random());
                thumbnailView.refresh();
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to save image as thumbnail");
            }
        });
   
    },
    
    /**
     * Get image for the current record with the current settings
     */
    getImageURL : function(recordid, includeHandcreatedOverlays) {
        if (!recordid  || (recordid === null))
            recordid = this.activeRecordID;
        var sliceCount = this.getSliceSlider().getValue();
        var frameCount = this.getFrameSlider().getValue();
       
        var isGreyScale = this.getGreyScale().pressed;
        var isZStacked = this.getZproject().pressed;
        var isMosaic = this.getMosaic().pressed;
        
        var siteNumber = this.getSiteControl().items.items[0].getValue().site;
        var checkBoxes = this.getChannelcontrol().getSelectionModel().getSelection();
        var channelNumbers = new Array();
        var contrastStore = Ext.StoreManager.get('Manage.store.ChannelContrastStore');
        var channelDetails={};
        
        for (var i=0; i<checkBoxes.length; ++i) {
            if (checkBoxes[i].data){
            	var channelNo=checkBoxes[i].data.channelNumber;
            	channelNumbers.push(channelNo);
            	
            	var contrast = contrastStore.getById(channelNo+"");
            	
            	var details=[
            	    //min,max,gamma,lut in this order
            		contrast.get('min'), 
            		contrast.get('max'),
            		contrast.get('gamma'),
            		checkBoxes[i].data.lut
                ];
            	channelDetails[channelNo]=details;
            }
        }
        
        var channelDetailsURI = encodeURIComponent(Ext.encode(channelDetails));
        
        var channelNumbersURI = encodeURIComponent(Ext.encode(channelNumbers));
        
        /*Overlays are fetched seperately
         * var overlayNames = new Array();
        var checkBoxGrid = Ext.ComponentQuery.query('imagecontrols #overlaycontrol')[0];
        var selected = checkBoxGrid.getSelectionModel().getSelection();
        for (var j=0; j< selected.length; ++j) {
        	if(includeHandcreatedOverlays == false && selected[j].data.handCreated == true){
        		continue;
        	}
            overlayNames.push(selected[j].data.name);
        }
        var overlayNamesURI = encodeURIComponent(Ext.encode(overlayNames));*/
        
        var record = this.getRecordItem(recordid).data;
        var imageURL = '../project/getImage?recordid='+recordid+'&sliceNumber='+sliceCount+'&frameNumber='+frameCount+'&channelNumbers='+channelNumbersURI+'&siteNumber='+siteNumber;
        imageURL += '&channelDetails='+channelDetailsURI;
        imageURL += '&isGreyScale=' + isGreyScale + '&isZStacked=' + isZStacked + '&isMosaic=' + isMosaic;
        //imageURL += '&overlays=' + overlayNamesURI;

        return imageURL;
        
    },
    
    /**
     * Get overlay transparency for the current record with the current settings
     */
    getOverlayTransparencyUrl : function(recordid) {
        if (!recordid  || (recordid === null))
            recordid = this.activeRecordID;
        var sliceCount = this.getSliceSlider().getValue();
        var frameCount = this.getFrameSlider().getValue();
        
        var siteNumber = this.getSiteControl().items.items[0].getValue().site;
        
        //Overlays are fetched seperately
        var overlayNames = new Array();
        var checkBoxGrid = Ext.ComponentQuery.query('imagecontrols #overlaycontrol')[0];
        var selected = checkBoxGrid.getSelectionModel().getSelection();
        for (var j=0; j< selected.length; ++j) {
            overlayNames.push(selected[j].data.name);
        }
        var overlayNamesURI = encodeURIComponent(Ext.encode(overlayNames));
        
        var record = this.getRecordItem(recordid).data;
        var imageURL = '../project/getOverlayTransparency?recordid='+recordid+'&sliceNumber='+sliceCount+'&frameNumber='+frameCount+'&siteNumber='+siteNumber;
        imageURL += '&overlays=' + overlayNamesURI;
        
        var scalebarValue = this.getScalebarButton().pressed;
        imageURL += '&scalebar=' + scalebarValue;

        return imageURL;
        
    },
    
    /**
     * Get selected co-ordinates for legends
     */
    getLegendCoordinates : function(recordid) {
        if (!recordid  || (recordid === null))
            recordid = this.activeRecordID;
        var sliceCount = this.getSliceSlider().getValue();
        var frameCount = this.getFrameSlider().getValue();
        
        var siteNumber = this.getSiteControl().items.items[0].getValue().site;
        
        coordinate = {};
        coordinate.sliceNumber = sliceCount;
        coordinate.frameNumber = frameCount;
        coordinate.siteNumber = siteNumber;
        coordinate.recordid = recordid;
        
        return coordinate;
    },
    
    /**
     * Download current image with the current settings
     */
    onImageDownload : function() {
    	console.log("image download pressed");
    	
        var imageURL = this.getImageURL(this.activeRecordID,true);
        var scalebarValue = this.getScalebarButton().pressed;
        imageURL += '&scalebar=' + scalebarValue;
        imageURL += '&mode=download';
        Ext.create('Ext.window.Window', {
            title : 'Image Screenshot',
            layout : 'fit',
            width : 200,
            height : 70,
            items : [{
                xtype : 'panel',
                layout : {
                    type : 'hbox',
                    pack : 'center',
                    align : 'middle'
                },
                items : [{
                    layout : 'fit',
                    width : 80,
                    height : 20,
                    border : false,
                    html : '<a href="' + imageURL + '" target="_blank">Download</a>'
                }]
            }],
            buttons : [{
                text : 'Close',
                handler : function() {
                    this.up('window').close();
                }
            }]
        }).show();
    },
    
    /**
     * Update the user annotations tab with data from this record
     */
    setUserAnnotations : function(recordid) {
        var store = this.getUserAnnotations().getStore();
        store.load({
            params : {
                recordid : recordid
            }
        });
    },
    
    /**
     * Disable movie related controls
     */
    disbleMovieControls : function() {
        this.getSliceMovie().disable();
        this.getFrameMovie().disable();
    },
    
    /**
     * Enable movie related controls
     */
    enableMovieControls : function() {
        this.getSliceMovie().enable();
        this.getFrameMovie().enable();
    },
    
    /**
     * Hide movie related controls
     */
    hideMovieControls : function() {
        this.getSliceMovie().hide();
        this.getFrameMovie().hide();
    },

    /**
     * Show movie related controls
     */
    showMovieControls : function() {
        this.getSliceMovie().show();
        this.getFrameMovie().show();
    }
});

//
//Ext.define('OverlayFlag', {
//    singleton: true,
//    flag : false,
//    prevState : false
//});