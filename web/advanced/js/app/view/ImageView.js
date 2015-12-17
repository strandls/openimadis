/**
 * The main image view. This shows the image and has an overlay
 * div on which overlay is drawn. The size of the image and the size 
 * of the overlay div are always the same

Ext.define('Manage.view.ImageView', {
	extend:'Ext.form.Panel',
	xtype:'imageview',
	alias:'widget.imageview',
	requires: [
		'Manage.view.ImageSliders',
		'Manage.view.ImageMetaData',
		'Manage.view.RecordMetaData',
		'Manage.view.Attachments',
		'Manage.view.Comments',
		'Manage.view.History',
		'Manage.view.Legends',
		'Manage.view.ImageControls',
		'Manage.view.ImageToolbar',
        'Manage.view.HorizontalRuler',
        'Manage.view.VerticalRuler'
	],
	initComponent:function() {
		var page=this;
		this.hRulerHeight=25;
		this.vRulerWidth=40;
		
        this.filler=this.createFiller();        
        this.hruler=this.createHruler();
        this.vruler = this.createVruler();
        this.tabpanel=this.createTabPanel();
        
        this.sliders=Ext.create('Manage.view.ImageSliders',{
        	border:false,
        	region:'south'
        });
        
        this.imagePlusRulerContainer=this.createImagePlusRulerContainer();

		var config = {
			id:'imageViewPanel',	
			xtype: 'panel',
	        region:'center',
	        layout: 'border',
	        border : false,
	        items:[
	         {
	        	xtype: 'panel',
	 	        region:'center',
	 	        layout: 'border',
	 	        border : false,
	 	        items:[
	 	           this.imagePlusRulerContainer ,
	 	           this.sliders
                ]
	         },
	         this.tabpanel
	        ]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		Manage.view.ImageView.superclass.initComponent.apply(this, arguments);
	},
	
	
	getScaledSize: function(imgHeight, imgWidth){    	  
    	  var panelHeight=this.imagePlusRulerContainer.getHeight()-this.hruler.getHeight();
          var panelWidth=this.imagePlusRulerContainer.getWidth()-this.vruler.getWidth();
          var panelRatio = panelWidth/panelHeight;
          //var imgRatio = imgWidth/imgHeight;
          var imgRatio = 2;
          if (panelRatio <= imgRatio) {
              return {width : panelWidth, height : panelWidth/imgRatio};
          } else {
              return {width : imgRatio*panelHeight, height : panelHeight};
          }
    },
	
	hideRulers: function(){
    	this.hruler.setHeight(0);
    	this.filler.setHeight(0);
    	this.vruler.setWidth(0);
    	this.filler.setWidth(0);    	
	},
	
	showRulers:function(){
    	this.hruler.setHeight(this.hRulerHeight);
    	this.filler.setHeight(this.hRulerHeight);
    	this.vruler.setWidth(this.vRulerWidth);
    	this.filler.setWidth(this.vRulerWidth);
	},
	
    handleImage:function(){
	   console.log("handle image Size");
    },
    

	createFiller:function() {
		var page=this;
		return Ext.create('Ext.panel.Panel',{
		    id:'filler',
		    height: page.hRulerHeight,
		    width: page.vRulerWidth,
		    border:false,
		    html: '<div id="filler" style="background-color: #cccccc; width: "+vRulerWidth+"px; height: "+"rulerSize"+"px;"></div>'			
		});
	},
	
	createHruler: function(){
		var page=this;
		return Ext.create('Manage.view.HorizontalRuler',{
            id:'hRuler',
            height: page.hRulerHeight,
            border:true,
            listeners : {
              afterrender: {
                 fn: function() {
                     this.initRender();
                 }
              }
            }
        });
	},
	
	createVruler: function(){
		return Ext.create('Manage.view.VerticalRuler', {
			id : 'vRuler',
			bodyStyle : {
				'text-align' : 'left',
				'vertical-align' : 'top'
			},
			width : this.vRulerWidth,
			rulerHeight : 600,
			border : true,
			listeners : {
				afterrender : {
					fn : function() {
						this.initRender();
					}
				}
			}
		});
	},
	
	createImagePlusRulerContainer: function(){
		var imagedisplaypanel=
		{
		        xtype : 'panel',
		    //region:'center',
		    //align:'left',            	
		    border : false,
		    id : 'imagedisplaypanel',
		    bodyStyle : {
		        'text-align' : 'left',
		        'vertical-align' : 'top'
		    },
		    layout:'fit',
		
		    items : [{
		        xtype : 'panel',
		        bodyCls : 'image_overlay',
		        layout:'fit',
		        //flex:1,
		        id : 'imageholder',
		        border:false,
		        bodyStyle : {
		            'text-align' : 'left',
		            'vertical-align' : 'top'
		        },
		        html : '<div id="imageeditor" class="editor">' +
		               '</div>',
		        items : [{
		            xtype: 'image',
		            id : 'imagedisplay',
		            cls : 'zoom_image',
		            flex:1,
		            listeners : {
		                load : {
		                    element : 'el',
		                    fn : function() {
		                        
		                        var imageDisplay = Ext.getCmp('imagedisplay');
		                        var imagePlusRulerContainer = Ext.getCmp('imagePlusRulerContainer');        
		                        var imageDisplayPanel = Ext.getCmp('imagedisplaypanel');
		                        var imageHolder = Ext.getCmp('imageholder');
		                        var imageeditor = Ext.get('imageeditor');
		                        
		                        var imageViewPanel = Ext.getCmp('imageViewPanel');
		                        
		                        $("<img/>").attr("src", this.getAttribute("src")).load(function() {
		                            var imgHeight = this.height; 
		                            var imgWidth = this.width;
		                            imageDisplay.saveImageSize(imgWidth,imgHeight);
		                            var scalingFactor = imageViewPanel.getScaledSize(imgHeight, imgWidth);
		                            
		                            imageeditor.setHeight(scalingFactor.height);
		                            imageeditor.setWidth(scalingFactor.width);
		                            
		                            imageDisplayPanel.setHeight(scalingFactor.height);
		                            imageDisplayPanel.setWidth(scalingFactor.width);
		                            imageDisplay.setHeight(scalingFactor.height);
		                            imageDisplay.setWidth(scalingFactor.width);
		                            imageHolder.setHeight(scalingFactor.height);
		                            imageHolder.setWidth(scalingFactor.width);
		                            
		                           
		                        }).each( function() {
		                            if(this.complete) 
		                                $(this).trigger("load");
		                        });
		                        var parentCmp = Ext.getCmp('imagedisplay');
		                        parentCmp.fireEvent("imageLoaded");
		                    }
		                }
		            },
		            // Fit the current image to the space available. height and width are the dimensions of the image
		            fitImage : function (width, height) {
		            	
		                var imageDisplayPanel = Ext.getCmp('imagedisplaypanel');
		                var imageHolder = Ext.getCmp('imageholder');
		                var imageeditor = Ext.get('imageeditor');
		                var imageViewPanel = Ext.getCmp('imageViewPanel');
		                var scalingFactor = imageViewPanel.getScaledSize(height, width);                       
		
		                this.setHeight(scalingFactor.height);
		                this.setWidth(scalingFactor.width);
		                
		                imageDisplayPanel.setHeight(scalingFactor.height);
		                imageDisplayPanel.setWidth(scalingFactor.width);	 					                        
		                imageHolder.setHeight(scalingFactor.height);
		                imageHolder.setWidth(scalingFactor.width);
		                imageeditor.setHeight(scalingFactor.height);
		                imageeditor.setWidth(scalingFactor.width);
		                sketchpad.setSize(scalingFactor.width, scalingFactor.height);
		                sketchpad.setViewBox(0, 0, width, height);
		                
		                return scalingFactor;
		            },
		            
		            saveImageSize: function(imageWidth, imageHeight){
		            	this.imageWidth=imageWidth;
		            	this.imageHeight=imageHeight;
		            }
		        }]
		    }]
		};
		
		return Ext.create('Ext.panel.Panel',{
         	xtype:'panel',
         	region:'center',
         	layout: {
    	        type: 'table',	        
    	        columns: 2
 	     	}, 	     	
 	     	
 	     	id : 'imagePlusRulerContainer',
 	     	border:false,
 	     	listeners:{
             	resize:{
             		fn:function(el){
             			//page.handleImage();
             			this.up().up().fireEvent('adjustImage');

             		}
             	}
             },
         	items:[
				this.filler,
				this.hruler,
				this.vruler,
				imagedisplaypanel
         	]
         });
	},
	
    createTabPanel:function(){
    	return Ext.create('Ext.tab.Panel',{
			region:'east',
				
		    collapsible:'true',
			tabPosition: 'bottom',
			split: 'true',
            layoutOnTabChange : true,
			defaults: {autoScroll: true},
        	width: 250,
       		//     minSize: 100,
         	//   maxSize: 500,
			//	flex: 1,
            items:[
                {
                    xtype : 'imagecontrols',  
                    tabConfig : {
                        title : ' ',
                        iconCls : 'imagecontrols',
                        tooltip : 'Image Controls'
                    }
                },
                {
                    xtype : 'comments',
                    tabConfig : {
                        title : ' ',
                        iconCls : 'comments',
                        tooltip : 'Comments'
                    }
                },
                {
                    xtype : 'history',
                    tabConfig : {
                        title : ' ',
                        iconCls : 'history',
                        tooltip : 'History'
                    }
                },
                {
                    xtype : 'userAnnotations',
                    tabConfig : {
                        title : ' ',
                        iconCls : 'userannotations',
                        tooltip : 'User Annnotations'
                    }
                },
                {
                    xtype : 'legends',
                    tabConfig : {
                        title : ' ',
                        iconCls : 'userannotations',
                        tooltip : 'User Legends'
                    }
                },
                {
                    xtype : 'attachments',
                    tabConfig : {
                        title : ' ',
                        iconCls : 'attachments',
                        tooltip : 'Attachments'
                    }
                },
                {
                    xtype : 'recordmetadata',
                    tabConfig: {
                        title: ' ',
                        iconCls: 'recordmetadata',
                        tooltip: 'Record Metadata'
                    }
                },
                {
                    xtype : 'imagemetadata',  
                    tabConfig : {
                        title : ' ',
                        iconCls : 'imagemetadata',
                        tooltip : 'Image MetaData'
                    }
                }
            ]
		});
	}	
	
});*/
