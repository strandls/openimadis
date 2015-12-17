/**
 * This shows vertical ruler on the image view using raphael
*/
Ext.define('Manage.view.VerticalRuler', {
	extend:'Ext.panel.Panel',
	xtype:'verticalruler',
	alias:'widget.verticalruler',
	requires: [
		//'Manage.view.ImageSliders'
	],
	initComponent:function() {
        var page=this;
        var config = {
            border:false,
            height:this.initialConfig.rulerHeight,
            items:[
                {
                    xtype:'panel',
                    border:false,
                    width:this.initialConfig.width,
                    height:this.initialConfig.rulerHeight,
                    html:'<div id="verticalRulerCanvas"></div>'
                }
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
		this.callParent();
    },

    initRender: function(){
        if(! this.paper){
            this.paper=Raphael('verticalRulerCanvas', this.width, this.rulerHeight);
        }
    },

    reload:function(rulerHeight,ymax){
        this.paper.setSize(this.width,rulerHeight);
        this.rulerHeight=rulerHeight;
        this.setHeight(rulerHeight);
        this.ymax=ymax;
        this.paper.clear();
        this.drawRuler();
    },

    drawRuler:function(){
    	var path="M0,0 L30,0";
        this.mouseMarker=this.paper.path(path).attr({'stroke':'#0000FF'});
        
        var yImageUnit=this.calculateUnit(this.ymax);
        var yRulerUnit=((this.rulerHeight+0.0)/this.ymax)*yImageUnit;

        var textModCount=Math.round(50/yRulerUnit);
        if(textModCount<1){
            textModCount=1;
        }

        for(var y=0, measure=0,unitCnt=0;y<this.rulerHeight;y+=yRulerUnit,measure+=yImageUnit,unitCnt++){

            this.drawUnitMark(y,measure);
            this.drawSubMarks(y,yRulerUnit);

            if(unitCnt%textModCount==0){
                this.drawText(y,measure);
            }
        }
    },

    calculateUnit:function(maxlength){
        var n=maxlength;
        var logn=Math.round(Math.log(n) / Math.log(10));
        return Math.pow(10,logn)/10;
    },



    drawUnitMark: function(y,m){
        var path="M0,"+y+" L14, "+y;
        this.paper.path(path);
    },

    drawSubMarks:function(start,yRulerUnit){
        if(yRulerUnit>=40){
            for(var i=1,dy=yRulerUnit/10;i<10;dy+=yRulerUnit/10,i++){
                if(i!=5){
                    var y=start+dy;
                    var path="M0, "+y+" L4, "+y;
                    this.paper.path(path);
                }
            }
        }
        if(yRulerUnit>=8){
            var y=start+yRulerUnit/2;
            var path="M0, "+y+" L10, "+y;
            this.paper.path(path);
        }
    },

    drawText: function(y,m){
        //add \n to print it vertically
        /*m=m+'';
        var label='';
        for(var i=0;i<m.length;i++){
            label+=m[i];
            if(i!=m.length-1){
                label+='\n';
            }
        }*/

        var text=this.paper.text(15,y,m);
        var bBox=text.getBBox();        

        if(bBox.y<=0){
            text.attr({'y':bBox.y+bBox.height});
        }
        
        text.attr({'x':bBox.x+bBox.width+1});

        var bBox=text.getBBox();
        if(bBox.x+bBox.height+3>this.rulerHeight){
            text.hide();
        }
    },
    
    showMousePosition: function(yPosition){    	
    	var y=yPosition-(this.getPosition()[1]);
    	
    	var path="M0,"+y+" L30, "+y;
    	this.mouseMarker.attr({'path':path});
    }
});

