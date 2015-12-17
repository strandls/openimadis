/**
 * Thumbnail for zoom
 * */

Ext.define('Manage.view.imageview.ZoomThumbnail', {
	extend:'Ext.panel.Panel',
	alias:'widget.zoomThumbnail',
	
	initComponent:function() {
		var me=this;
		this.normalizedWindow={
			'from':{'x':0,'y':0},
			'to':{'x':1,'y':1}
		};
		
		var config={	
			border:true,
			html : '<div align="center"> <div id="zoomThumbnailCanvas" class=""> '+'</div></div>',
			
			listeners:{
				'afterrender':{
					fn: function(component, opts){
						console.log('afterrender');
						this.initRaphael();
					},
					scope:this
				},
				'afterlayout':{
					fn: function(component, opts){
						console.log('afterlayout');
						this.adjustRaphael();	
					},
					scope:this
				}
			}
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		this.callParent();
	},
	
	initRaphael:function(){
		this.paper = Raphael('zoomThumbnailCanvas', this.getWidth(), this.getHeight());
		this.thumbnail = this.paper.image('images/panojs/blank.gif', 0, 0,100,100);
	},

	
	adjustRaphael:function(){
		if(this.paper){
			this.paper.setSize(this.getWidth(), this.getHeight());
		}
	},
	
	setImage: function(srcpath, imgRatio){
		//console.log('zoom thumbnail setimage');
		this.imgRatio= imgRatio;
		var scaledSize = this.getScaledSize();
		this.thumbnail.attr({
			'src':srcpath,
			'width':scaledSize.width,
			'height':scaledSize.height
		});
		this.paper.setSize(scaledSize.width, scaledSize.height);
		//console.log('scaledSize.width:'+scaledSize.width+'scaledSize.height:'+scaledSize.height);
		this.initSpectrum(scaledSize.width, scaledSize.height);
	},
	
	getScaledSize: function(){
		var panelWidth = this.getWidth();
		var panelHeight = this.getHeight();
		var panelRatio = panelWidth/panelHeight;
		if (panelRatio <= this.imgRatio) {
			return {width : panelWidth, height : parseInt(panelWidth/this.imgRatio)};
		} else {
			return {width : parseInt(this.imgRatio*panelHeight), height : panelHeight};
		}
	},
	
	resetSpectrum:function(){
		this.Spectrum.rect.remove();
		this.Spectrum.resizer.remove();
		this.Spectrum.resizerBlanket.remove();
        this.Spectrum = null;
	},
	
	initSpectrum:function(pwidth,pheight) {
        var scale = 1,
            canvasW = pwidth,
            canvasH = pheight;
            
         var width = pwidth/2,
            height = pheight/2,
            //x0 = (canvasW*scale/2) + (width/2),
            //y0 = (canvasH*scale/2) - (height/2);
            x0 = 0,
            y0 = 0;
            
        var resizerBlanketRadius = 25,
            resizerRadius = 5;
                           
        if(this.Spectrum !=null){
        	
        	return;
        }
        
    	var Spectrum = {};
    	this.Spectrum=Spectrum;
        Spectrum.boundaryLimit = {};
        Spectrum.boundaryLimit.endx = pwidth ;
        Spectrum.boundaryLimit.endy = pheight ;
        
        //Spectrum.paper                 = Raphael('spectrum', canvasW*scale, canvasH*scale);
        Spectrum.paper                 = this.paper;
        
        Spectrum.rect                 = Spectrum.paper.rect(x0, y0, width, height);
        Spectrum.resizer             = Spectrum.paper.circle(x0 + width, y0 + height, resizerRadius);
        Spectrum.resizerBlanket     = Spectrum.paper.circle(x0 + width, y0 + height, resizerBlanketRadius);
        
        Spectrum.rect.attr({fill: 'white', opacity:0.3});
        Spectrum.resizerBlanket.attr({fill: 'yellow', opacity:0});
        Spectrum.resizer.attr({fill:'yellow', opacity:1});
        
        var zoomThumb=this;
        
        Spectrum.rect.drag(
        		/********** bugfix here for checking n not allowing ther rect to be dragged outside the zoomthumbnail 
        		 * and into a non overlapping region
        		 * 
        		 */
            function(dx, dy, x, y) {

            	if(((this.rox + dx) > Spectrum.boundaryLimit.endx)||
            	   ((this.roy + dy) > Spectrum.boundaryLimit.endy)||
            	   ((this.ox + dx ) < 0)||
            	   ((this.oy + dy ) < 0))
            	{
            			console.log("SpectrumBox out of Bounds : move")
            			
            	}else{
            		this.attr({x: this.ox + dx, y: this.oy + dy});
            		Spectrum.resizerBlanket.attr({cx: this.rbox + dx, cy: this.rboy + dy});
            		Spectrum.resizer.attr({cx: this.rox + dx, cy: this.roy + dy});  
            	}
            },
            function() {
            			this.ox = this.attr("x");
            			this.oy = this.attr("y");
            			this.rbox = Spectrum.resizerBlanket.attr('cx');
            			this.rboy = Spectrum.resizerBlanket.attr('cy');
            			this.rox = Spectrum.resizer.attr('cx');
            			this.roy = Spectrum.resizer.attr('cy');
            },
            function(){
            	zoomThumb.windowFinalized();
            }
        );
        
        
        Spectrum.resizerBlanket.drag(
/********** bugfix here for the spectrumRect not being visible when the blanket is dragged 
 * to the left of or above point (thumbnailWIndow.x , thumbnailWindow.y)
 * 
 */
        	function(dx, dy) {
                this.attr({cx: this.ox + dx, cy: this.oy + dy});
                Spectrum.resizer.attr({cx: this.rox + dx, cy: this.roy + dy});  
                Spectrum.rect.attr({width: this.rwidth + dx, height: this.rheight + dy});
            },
            function() {
                this.ox = this.attr('cx');
                this.oy = this.attr('cy');
                this.rox = Spectrum.resizer.attr('cx');
                this.roy = Spectrum.resizer.attr('cy');
                this.rwidth = Spectrum.rect.attr('width');
                this.rheight = Spectrum.rect.attr('height');
            },
            function(){
            	zoomThumb.windowFinalized();
            }
        );
    },
    
    windowFinalized:function(){
    	//console.log('windowFinalized');
    	var thumbnailWindow = this.Spectrum.rect.attr();
    	this.normalizedWindow.from.x=(thumbnailWindow.x/this.paper.width);
    	this.normalizedWindow.from.y=(thumbnailWindow.y/this.paper.height);
    	
    	this.normalizedWindow.to.x=((thumbnailWindow.x+thumbnailWindow.width)/this.paper.width);
    	this.normalizedWindow.to.y=((thumbnailWindow.y+thumbnailWindow.height)/this.paper.height);
    	
    	this.fireEvent("zoomWindowChanged",this.normalizedWindow);
    },	
    
    setFullWindow: function(){
    	this.normalizedWindow.from.x=0;
    	this.normalizedWindow.from.y=0;
    	
    	this.normalizedWindow.to.x=1;
    	this.normalizedWindow.to.y=1;
    },
    
    getNormalizedWindow: function(){
    	return this.normalizedWindow;
    }
});