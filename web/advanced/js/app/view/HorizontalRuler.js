/**
 * This shows horizontal ruler on the image view using raphael
*/
Ext.define('Manage.view.HorizontalRuler', {
	extend: 'Ext.panel.Panel',
	xtype: 'horizontalruler',
	alias: 'widget.horizontalruler',
	requires: [
		//'Manage.view.ImageSliders'
	],
	initComponent: function () {
        var page=this;
        var config = {
            border: false,
            width: this.initialConfig.rulerWidth,
            items: [
                {
                    tag: 'div',
                    border: false,
                    height: this.initialConfig.height,
                    html: '<div id="horizontalRulerCanvas"></div>'
                }
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
		this.callParent();
    },

    initRender: function () {
        if (! this.paper) {
            this.paper = Raphael('horizontalRulerCanvas', this.rulerWidth, this.height);
        }
    },

    reload: function (rulerWidth, xmax) {
        this.paper.setSize(rulerWidth, this.height);
        this.rulerWidth = rulerWidth;
        this.setWidth(rulerWidth);
        this.xmax = xmax;
        this.paper.clear();
        this.drawRuler();
    },

    drawRuler: function () {
    	var path="M0,0 L0,22";
        this.mouseMarker=this.paper.path(path).attr({'stroke':'#0000FF'});
    	
        var xImageUnit = this.calculateUnit(this.xmax);
        var xRulerUnit = ((this.rulerWidth + 0.0) / this.xmax) * xImageUnit;

        var textModCount = Math.round(50 / xRulerUnit);
        if (textModCount < 1) {
            textModCount = 1;
        }
        
        for (var x = 0, measure = 0, unitCnt = 0;x < this.rulerWidth;x += xRulerUnit, measure += xImageUnit, unitCnt++) {

            this.drawUnitMark(x, measure);
            this.drawSubMarks(x, xRulerUnit);

            if (unitCnt % textModCount === 0) {
                this.drawText(x, measure);
            }
        }
    },

    calculateUnit: function (maxlength) {
        var n = maxlength;
        var logn = Math.round(Math.log(n) / Math.log(10));
        return Math.pow(10, logn) / 10;
    },



    drawUnitMark: function (x, m) {
        var path = "M" + x + ", 0 L" + x + ",14";
        this.paper.path(path).attr('stroke-width', 1);
    },

    drawSubMarks: function (start, xRulerUnit) {
        var x, path;
        if (xRulerUnit >= 40) {
            for (var i = 1, dx = xRulerUnit / 10;i < 10;dx += xRulerUnit / 10, i++) {
                if(i!=5){
                    x=start+dx;
                    path="M"+x+", 0 L"+x+",4";
                    this.paper.path(path);
                }
            }
        }
        if(xRulerUnit>=8){
            x=start+xRulerUnit/2;
            path="M"+x+", 0 L"+x+",10";
            this.paper.path(path);
        }
    },

    drawText: function(x,m){

        var text=this.paper.text(x,15,m);
        var bBox=text.getBBox();
        if(bBox.x<=0){
        	text.attr({'x':bBox.x+bBox.width});
        }
        text.attr({'y':bBox.y+bBox.height-2});
        
        var bBox=text.getBBox();
        if(bBox.x+bBox.width+3>this.rulerWidth){
            text.hide();
        }
    },
    
    showMousePosition: function(xPosition){    	
    	var x=xPosition-(this.getPosition()[0]);
    	var path="M"+x+", 0 L"+x+",25";
    	this.mouseMarker.attr({'path':path});
    }
});
