/*
 * ScaleRaphael 0.8 by Zevan Rosser 2010 
 * For use with Raphael library : www.raphaeljs.com
 * Licensed under the MIT license.
 *
 * www.shapevent.com/scaleraphael/
 */
(function(){
  window.ScaleRaphael = function(container, width, height){
    var wrapper = document.getElementById(container);
    if (!wrapper.style.position) wrapper.style.position = "relative";
    wrapper.style.width = width + "px";
    wrapper.style.height = height + "px";
    wrapper.style.overflow = "hidden";
    
    var nestedWrapper;
      
    if (Raphael.type == "VML"){
      wrapper.innerHTML = "<rvml:group style='position : absolute; width: 1000px; height: 1000px; top: 0px; left: 0px' coordsize='1000,1000' class='rvml' id='vmlgroup'><\/rvml:group>";
      nestedWrapper = document.getElementById("vmlgroup");
    }else{
      wrapper.innerHTML = "<div id='svggroup' style='position:absolute;z-index:2;top:0;left:0;'><\/div>";
      nestedWrapper = document.getElementById("svggroup");
    }
 
    var paper = new Raphael(nestedWrapper, width, height);
    var vmlDiv;
    
    if (Raphael.type == "SVG"){
      paper.canvas.setAttribute("viewBox", "0 0 "+width+" "+height);
    }else{
      vmlDiv = wrapper.getElementsByTagName("div")[0];
    }
    
    paper.changeSize = function(w, h, center, clipping){
      clipping = !clipping;
      
      var ratioW = w / width;
      var ratioH = h / height;
      var scale = ratioW < ratioH ? ratioW : ratioH;
      
      var newHeight = parseInt(height * scale);
      var newWidth = parseInt(width * scale);
      
      if (Raphael.type == "VML"){
         // scale the textpaths
    	 //using jquery to scale textpaths inside raphael canvas. 
       $('#'+this.container).find('textpath').each(function(){
    	   var fontSize = parseInt(this.css("font-size"));
    	   fontSize = fontSize * scale + "px";
    	   this.css({'font-size':fontSize});
       });
       //original code is rescaling all textpaths.
//       var txt = document.getElementsByTagName("textpath");
//        for (var i=0; i<txt.length; i++){
//          var curr = txt[i];
//          if (curr.style){
//            if(!curr._fontSize){
//              var mod = curr.style.font.split(" ");
//              curr._fontStyle = mod[0];
//              curr._fontSize = parseInt(mod[1]);
//              curr._font = mod[3];
//            }
//            
//            //curr.style.font = curr._fontStyle + " "+ curr._fontSize * scale + " px " + curr._font;
//          }
//        }
        var newSize; 
        if (newWidth < newHeight){
         newSize = newWidth * 1000 / width;
        }else{
         newSize = newHeight * 1000 / height;
        }
        newSize = parseInt(newSize);
        nestedWrapper.style.width = newSize + "px";
        nestedWrapper.style.height = newSize + "px";
        if (clipping){
          nestedWrapper.style.left = parseInt((w - newWidth) / 2) + "px";
          nestedWrapper.style.top = parseInt((h - newHeight) / 2) + "px";
        }
        vmlDiv.style.overflow = "visible";
      }
      
      if (clipping){
        newWidth = w;
        newHeight = h;
      }
      
      wrapper.style.width = newWidth + "px";
      wrapper.style.height = newHeight + "px";
      paper.setSize(newWidth, newHeight);
      
      if (center){
        wrapper.style.position = "absolute";
        wrapper.style.left = parseInt((w - newWidth) / 2) + "px";
        wrapper.style.top = parseInt((h - newHeight) / 2) + "px";
      }
    }
    
    paper.scaleAll = function(amount){
      paper.changeSize(width * amount, height * amount);
    }
    
    paper.changeSize(width, height);
    
    paper.w = width;
    paper.h = height;
    paper.container=container;
    
    return paper;
  }
})();