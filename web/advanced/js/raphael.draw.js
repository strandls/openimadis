(function(Raphael) {
    
    Raphael.sketchpad.shapepen = function() {
        return new TextPen();
    };
        
    var CirclePen = function() {
        this._circle = null;
    };

    CirclePen.prototype = new Raphael.sketchpad.pen();

    CirclePen.prototype.start = function(e, sketchpad) {
        this._drawing = true;

        var offset = $(sketchpad.canvas()).offset();
        var centerX = e.pageX - offset.left;
        var centerY = e.pageY - offset.top;
        this._circle = sketchpad.paper().circle(centerX, centerY, 0);
        this._circle.attr({ 
                stroke: this._color,
                "stroke-opacity": this._opacity,
                "stroke-width": this._width,
                "stroke-linecap": "round",
                "stroke-linejoin": "round"
        });

    };

    CirclePen.prototype.finish = function(e, sketchpad) {
        this._drawing = false;
        var circle = this._circle;
        return circle;
    };

    CirclePen.prototype.move = function(e, sketchpad) {
        if (this._drawing) {
            var cx = this._circle.attr("cx");
            var cy = this._circle.attr("cy");
            var radius = Math.sqrt((e.pageX- cx)*(e.pageX-cx) + (e.pageY-cy)*(e.pageY-cy));
            this._circle.attr({r: radius});
        } 
    };

    /**
     * Pen for an ellipse
     */
    var EllipsePen = function() {
        this._ellipse = null;
    }

    EllipsePen.prototype = new Raphael.sketchpad.pen();

    EllipsePen.prototype.start = function(e, sketchpad) {
        this._drawing = true;

        var offset = $(sketchpad.canvas()).offset();
        var centerX = e.pageX - offset.left;
        var centerY = e.pageY - offset.top;
        this._ellipse = sketchpad.paper().ellipse(centerX, centerY, 0, 0);
        this._ellipse.attr({ 
                stroke: this._color,
                "stroke-opacity": this._opacity,
                "stroke-width": this._width,
                "stroke-linecap": "round",
                "stroke-linejoin": "round"
        });

    };

    EllipsePen.prototype.finish = function(e, sketchpad) {
        this._drawing = false;
        var ellipse = this._ellipse;
        return ellipse;
    };

    EllipsePen.prototype.move = function(e, sketchpad) {
        if (this._drawing) {
            var cx = this._ellipse.attr("cx");
            var cy = this._ellipse.attr("cy");
            var rx = Math.abs(e.pageX - cx);
            var ry = Math.abs(e.pageY - cy);
            this._ellipse.attr({rx: rx, ry: ry});
        } 
    };

    /**
     * Pen for a rectangle
     */
    var RectanglePen = function() {
        this._rect = null;
    }

    RectanglePen.prototype = new Raphael.sketchpad.pen();

    RectanglePen.prototype.start = function(e, sketchpad) {
        this._drawing = true;

        var offset = $(sketchpad.canvas()).offset();
        var centerX = e.pageX - offset.left;
        var centerY = e.pageY - offset.top;
        this._rect = sketchpad.paper().rect(centerX, centerY, 0, 0);
        this._rect.attr({ 
                stroke: this._color,
                "stroke-opacity": this._opacity,
                "stroke-width": this._width,
                "stroke-linecap": "round",
                "stroke-linejoin": "round"
        });

    };

    RectanglePen.prototype.finish = function(e, sketchpad) {
        this._drawing = false;
        var rect = this._rect;
        return rect;
    };

    RectanglePen.prototype.move = function(e, sketchpad) {
        if (this._drawing) {
            var x = this._rect.attr("x");
            var y = this._rect.attr("y");
            var width = Math.abs(e.pageX - x);
            var height = Math.abs(e.pageY - y);
            this._rect.attr({width: width, height: height});
        } 
    };

    /**
     * Pen for text 
     */
    var TextPen = function() {
        this._text = null;
    }

    TextPen.prototype = new Raphael.sketchpad.pen();

    TextPen.prototype.start = function(e, sketchpad) {
        this._drawing = true;

        var offset = $(sketchpad.canvas()).offset();
        var centerX = e.pageX - offset.left;
        var centerY = e.pageY - offset.top;
        this._text = sketchpad.paper().text(centerX, centerY, "");
        /*this._text.attr({ 
                stroke: this._color,
                "stroke-opacity": this._opacity,
                "stroke-width": this._width,
                "stroke-linecap": "round",
                "stroke-linejoin": "round"
        });*/
        var text = this._text;
        // Function to call after the text is entered 
        var success = function(newText) {
            text.attr('text', newText);
        }
        getAndSetText("", success);
    };

    TextPen.prototype.finish = function(e, sketchpad) {
        this._drawing = false;
        var text = this._text;
        return text;
    };

    TextPen.prototype.move = function(e, sketchpad) {
    
    };



})(window.Raphael);
