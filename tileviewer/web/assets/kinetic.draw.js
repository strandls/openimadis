
var stage;
var layer;

var drawLinemode;
var drawFreehandMode;
var drawPolygonMode;
var drawArrowMode;
var drawComplete=false;

var dragOption=false;
var editable;

//the scale at which shapes are drawn
var drawingScale;

//offset at which shapes are drawn
var drawingOffset;

//Text element to be displayed while drawing
var message='';

//Text to be drawn
var textinput;

//flag to check if a shape is added, deleted or modified on stage
var dirtyFlag=false;

//Intial font size is 30
var fontSize=30;

//Intial stroke width is 4
var strokewidth=4;

//Intial colour as Red
var strokecolour='#FF0000';

//Intial opacity is 1
var strokeopacity=1;

//List of selected elements
var selectedShapes=[];

function setDraggable(bool){
	dragOption = bool;
}

function setEditable(bool){
	editable = bool;
}

function update(activeAnchor) {
	dirtyFlag=true;

    var group = activeAnchor.getParent();
    var topLeft = group.get('.topLeft')[0];
    var topRight = group.get('.topRight')[0];
    var bottomRight = group.get('.bottomRight')[0];
    var bottomLeft = group.get('.bottomLeft')[0];
    var leftEnd =  group.get('.leftEnd')[0];
    var rightEnd =  group.get('.rightEnd')[0];
    var rotator = group.get('.rotator')[0];
    var shape = group.get('.shape')[0];
    var shapeType = shape.className;
    
    var anchorX = activeAnchor.getX();
    var anchorY = activeAnchor.getY();   
    
    var anchorName=activeAnchor.getName();
    var polyIndex;
    
    if(typeof anchorName === 'number'){
    	polyIndex=anchorName;
    	anchorName='poly';
    } 
    
    // update anchor positions
    switch (anchorName) {
        case 'topLeft':
            topRight.setY(anchorY);
            bottomLeft.setX(anchorX);
            if (anchorX > topRight.getX() - 10) {          	        
                activeAnchor.setX(topRight.getX() - 10);
                bottomLeft.setX(topRight.getX() - 10);
            }
            if (anchorY > bottomLeft.getY() - 10) {
                activeAnchor.setY(bottomLeft.getY() - 10);
                topRight.setY(bottomLeft.getY() - 10);
            }
            break;
        case 'topRight':
            topLeft.setY(anchorY);
            bottomRight.setX(anchorX);
            if (anchorX < topLeft.getX() + 10) {
                activeAnchor.setX(topLeft.getX() + 10);
                bottomRight.setX(topLeft.getX() + 10);
            }
            if (anchorY > bottomLeft.getY() - 10) {
                activeAnchor.setY(bottomLeft.getY() - 10);
                topLeft.setY(bottomLeft.getY() - 10);
            }
            break;
        case 'bottomRight':
            bottomLeft.setY(anchorY);
            topRight.setX(anchorX);
            if (anchorX < bottomLeft.getX() + 10) {
                activeAnchor.setX(bottomLeft.getX() + 10);
                topRight.setX(bottomLeft.getX() + 10);
            }
            if (anchorY < topLeft.getY() + 10) {
                activeAnchor.setY(topLeft.getY() + 10);
                bottomLeft.setY(topLeft.getY() + 10);
            }
            break;
        case 'bottomLeft':
            bottomRight.setY(anchorY);
            topLeft.setX(anchorX);
            if (anchorX > topRight.getX() - 10) {
                activeAnchor.setX(topRight.getX() - 10);
                topLeft.setX(topRight.getX() - 10);
            }
            if (anchorY < topLeft.getY() + 10) {
                activeAnchor.setY(topLeft.getY() + 10);
                bottomRight.setY(topLeft.getY() + 10);
            }
            break; 
        case 'leftEnd':
             	shape.getPoints()[0].x = anchorX;
                shape.getPoints()[0].y = anchorY;  		
            break; 
        case 'rightEnd':
             	shape.getPoints()[1].x = anchorX;
                shape.getPoints()[1].y = anchorY;                 		
            break;      
        case 'rotator':
                
	    	var topabs=topLeft.getAbsolutePosition();
	    	var bottomabs=bottomRight.getAbsolutePosition();
	    	
		var centerX=(topabs.x+bottomabs.x)/2;
		var centerY=(topabs.y+bottomabs.y)/2;
		
		var mouse=stage.getMousePosition();
		var mouse_x = mouse.x; var mouse_y = mouse.y;
		
		var radians = Math.atan2( (400-mouse_y) - (400-centerY), mouse_x - centerX);

		var godDegree=Math.PI/2-radians;
			    	
		if(shapeType==="Rect"){
			
			var x=(centerX-shape.getWidth()/2);
			var y=(centerY-shape.getHeight()/2);
	
			if(group.getOffset().x!==shape.getWidth()/2){
				var pos={'x':x,'y':y};
				for(var c=0;c<group['children'].length;c++){
					if(group['children'][c]['attrs']['name']==='rotator'){
						var r=group['children'][c];
						break;
					}
				}
				var attributes=shape;
				var id=group._id;
			
				group.removeChildren();
				group.destroy();
				group=drawRectangle(attributes,id,true,pos);
				group.add(r);		
				group.setRotation(godDegree);
			}			
			else{
				group.setRotation(godDegree);
			}
		}
		
		if(shapeType==='Ellipse'){
			var x=centerX;
			var y=centerY;	
			var pos={'x':x,'y':y};
			
			if(group.getX()!==pos.x){
				for(var c=0;c<group['children'].length;c++){
					if(group['children'][c]['attrs']['name']==='rotator'){
						var r=group['children'][c];
						break;
					}
				}
				
				var attributes=shape;
				var id=group._id;
				
				group.removeChildren();
				group.destroy();
				group=drawEllipse(attributes,id,true,pos);
				group.add(r);		
				group.setRotation(godDegree);
			}			
			else{
				group.setRotation(godDegree);
			}				
		}
			
		    var d= shape.getHeight()/2+50;

		    var slope=-1*((topRight.getX() - topLeft.getX())/(topRight.getY() - topLeft.getY()));
		    var theta=Math.atan(slope);	 
		
		    var centerX=(topLeft.getX()+bottomRight.getX())/2;
		    var centerY=(topLeft.getY()+bottomRight.getY())/2;	       
		    
		    if(rotator.getY()<centerY){
		    	rotator.setX(centerX+d*Math.cos(theta));
		    	rotator.setY(centerY+d*Math.sin(theta));
		    }
		    else{
		    	rotator.setX(centerX-d*Math.cos(theta));
		    	rotator.setY(centerY-d*Math.sin(theta));
		    } 
	    		
		return;
	case 'poly':
		shape.getPoints()[polyIndex].x=anchorX;
		shape.getPoints()[polyIndex].y=anchorY;
		break;
    }
    
    if(shapeType==='Rect'){
	    shape.setX(topLeft.getPosition().x);
	    shape.setY(topLeft.getPosition().y);

	    var width = topRight.getX() - topLeft.getX();
	    var height = bottomLeft.getY() - topLeft.getY();
	    if (width && height) {	        
		shape.setSize(width, height);
	    } 
	    
	    
	    var d= height/2+50;

	    var slope=-1*((topRight.getX() - topLeft.getX())/(topRight.getY() - topLeft.getY()));
	    var theta=Math.atan(slope);	 
	    
	    var centerX=(topLeft.getX()+bottomRight.getX())/2;
	    var centerY=(topLeft.getY()+bottomRight.getY())/2;	       
	    
	    if(rotator.getY()<centerY){
	    	rotator.setX(centerX+d*Math.cos(theta));
	    	rotator.setY(centerY+d*Math.sin(theta));
	    }
	    else{
	    	rotator.setX(centerX-d*Math.cos(theta));
	    	rotator.setY(centerY-d*Math.sin(theta));
	    } 
    }
    if(shapeType==='Ellipse'){
	    var centerX=(topLeft.getX()+bottomRight.getX())/2;
	    var centerY=(topLeft.getY()+bottomRight.getY())/2; 
	        
	    var offset=shape.getOffset();
	    shape.setX(centerX);
	    shape.setY(centerY);

	    var width = topRight.getX() - topLeft.getX();
	    var height = bottomLeft.getY() - topLeft.getY();
	    if (width && height) {
		shape.setSize(width, height);
	    }
	    
	    var slope=-1*((topRight.getX() - topLeft.getX())/(topRight.getY() - topLeft.getY()));
	    var theta=Math.atan(slope);
	    

	    var d= height/2+50;
	    
	    if(rotator.getY()<centerY){
	    	rotator.setX(centerX+d*Math.cos(theta));
	    	rotator.setY(centerY+d*Math.sin(theta));
	    }
	    else{
	    	rotator.setX(centerX-d*Math.cos(theta));
	    	rotator.setY(centerY-d*Math.sin(theta));
	    } 
    }
    if(shapeType==='Arrow'){
		fromy=shape.getPoints()[0].y;    
		fromx=shape.getPoints()[0].x;
		toy=shape.getPoints()[1].y;
		tox=shape.getPoints()[1].x;
	
		var headlen = 10;   // how long you want the head of the arrow to be, you could calculate this as a fraction of the distance between the points as well.
	    	var angle = Math.atan2(toy-fromy,tox-fromx);
	    	
	    	shape.setPoints([]);
		shape.setPoints([fromx, fromy, tox, toy, tox-headlen*Math.cos(angle-Math.PI/6),toy-headlen*Math.sin(angle-Math.PI/6),tox, toy, tox-headlen*Math.cos(angle+Math.PI/6),toy-headlen*Math.sin(angle+Math.PI/6)]);     	
    }
}

function addAnchor(group, x, y, name, isExpandable) {
	if(!editable)
		return;
    var stage = group.getStage();
    var layer = group.getLayer();
    
    if(typeof(isExpandable)==='undefined'){
    	isDraggable=true;
    }
    else{
    	isDraggable=false;
    }
     
    var anchor = new Kinetic.Circle({
        x: x,
        y: y,
        stroke: '#666',
        fill: '#ddd',
        strokeWidth: 2,
        radius: 8,
        name: name,
        draggable: isDraggable,
        dragOnTop: false
    });

    anchor.on('dragmove', function () {
        update(this);
        layer.draw();
    });
    anchor.on('mousedown touchstart', function () {
        //group.setDraggable(false);
        this.moveToTop();
    });
    anchor.on('dragend', function (evt) {
    	group.setDraggable(true);
        layer.draw();
   });
    // add hover styling
    anchor.on('mouseover', function () {
    	var layer = this.getLayer();
        document.body.style.cursor = 'pointer';
        this.setStrokeWidth(4);
        layer.draw();
    });
    anchor.on('mouseout', function () {
    	var layer = this.getLayer();
        document.body.style.cursor = 'default';
        this.setStrokeWidth(2);
        layer.draw();
    });

    group.add(anchor);
}

function addRotator(group,x,y,name){
	if(!editable)
		return;
    var stage = group.getStage();
    var layer = group.getLayer();

    var anchor = new Kinetic.Circle({
        x: x,
        y: y,
        stroke: '#666',
        fill: '#ddd',
        strokeWidth: 2,
        radius: 8,
        name: name,
        draggable: true,
        dragOnTop: false
    });
     
    anchor.on('dragmove', function () {
        update(this);
        layer.draw();
    });
    
        // add hover styling
    anchor.on('mouseover', function () {
        var layer = this.getLayer();
        document.body.style.cursor = 'pointer';
        this.setStrokeWidth(4);
        layer.draw();
    });
    anchor.on('mouseout', function () {
        var layer = this.getLayer();
        document.body.style.cursor = 'default';
        this.setStrokeWidth(2);
        layer.draw();
    });
    
    group.add(anchor);
}

function getStrokewidth(){
	return strokewidth;
}

function getStrokecolour(){
	return strokecolour;
}

function getStrokeopacity(){
	return strokeopacity;
}

function getFontSize(){
	return fontSize;
}

function setStrokewidth(width){
	strokewidth=width;
	for(var i=0;i<selectedShapes.length;i++){
		for(var j=0;j<layer["children"].length;j++){
			if(layer["children"][j]["_id"]===selectedShapes[i]){
				layer["children"][j]["children"][0].setStrokeWidth(strokewidth);
				
					//handling for text objects
					if(layer["children"][j]["children"][0]["className"]==="Text"){
						layer["children"][j]["children"][0].setStrokeWidth(0);
					}		
							
				dirtyFlag=true;
				undoArray.push({'id':layer["children"][j]._id,'shapeString':saveShape(layer["children"][j]),'action':'modified'});
			}
		}
	}
	layer.draw();	
}

function setStrokecolour(colour){
	strokecolour=colour;
	for(var i=0;i<selectedShapes.length;i++){
		for(var j=0;j<layer["children"].length;j++){
			if(layer["children"][j]["_id"]===selectedShapes[i]){
				layer["children"][j]["children"][0].setStroke(strokecolour);
				
				//handling for text objects
				if(layer["children"][j]["children"][0]["className"]==="Text"){
					layer["children"][j]["children"][0].setFill(strokecolour);
				}
				
				dirtyFlag=true;
				undoArray.push({'id':layer["children"][j]._id,'shapeString':saveShape(layer["children"][j]),'action':'modified'});
			}
		}
	}
	layer.draw();
}

function setStrokeopacity(opacity){
	strokeopacity=opacity;
	for(var i=0;i<selectedShapes.length;i++){
		for(var j=0;j<layer["children"].length;j++){
			if(layer["children"][j]["_id"]===selectedShapes[i]){
				layer["children"][j]["children"][0].setOpacity(strokeopacity);
				
				dirtyFlag=true;
				undoArray.push({'id':layer["children"][j]._id,'shapeString':saveShape(layer["children"][j]),'action':'modified'});
			}
		}
	}
	layer.draw();		
}

function setFontSize(size){
	fontSize=size;
	for(var i=0;i<selectedShapes.length;i++){
		for(var j=0;j<layer["children"].length;j++){
			if(layer["children"][j]["children"][0]["className"]==="Text"&&layer["children"][j]["_id"]===selectedShapes[i]){
				layer["children"][j]["children"][0].setFontSize(fontSize);
				
				var textGroup=layer["children"][j];
				var text=layer["children"][j]["children"][0];
				
				textGroup.removeChildren();
				textGroup.add(text);
				
				addAnchor(textGroup,  text.getX(), text.getY(), 'topLeft',false);
				addAnchor(textGroup,  text.getX()+text.getWidth(), text.getY(), 'topRight',false);
				addAnchor(textGroup,  text.getX(), text.getY()+text.getHeight(), 'bottomLeft',false);
				addAnchor(textGroup,  text.getX()+text.getWidth(), text.getY()+text.getHeight(), 'bottomRight',false);	
				
				dirtyFlag=true;
				undoArray.push({'id':layer["children"][j]._id,'shapeString':saveShape(layer["children"][j]),'action':'modified'});
			}
		}
	}
	layer.draw();		
}

function isDirty(){
	return dirtyFlag;
}

function setDirty(flag){
	dirtyFlag=flag;
}

function setText(inputText){
	textinput=inputText;
}

function setDrawingScale(scale){
	drawingScale=scale;
}

function setDrawingOffset(offset){
	drawingOffset=offset;
}

function removeStage(){
	if(typeof stage !== 'undefined')
		stage.destroy();
}

function initStage(width,height) {
	stage = new Kinetic.Stage({
		container: 'container',
		width: width,
		height: height
	});
	
    
	layer=new Kinetic.Layer();
	
	stage.add(layer);
    
	//LINE DRAWING HANDLERS
	moving = false;
    
	stage.on("mousedown", function(){
	    if (moving){
		moving = false;
		layer.draw();
	    } else if(drawLinemode){

		line.setAttrs({
		    points: [0, 0, 0, 0],
		    stroke: strokecolour,
		    strokeWidth: strokewidth, 
		    opacity: strokeopacity,
		    zoom_level: getZoomLevel()
		});
			    
		var mousePos = stage.getMousePosition();
		//start point and end point are the same
		line.getPoints()[0].x = mousePos.x;
		line.getPoints()[0].y = mousePos.y;
		line.getPoints()[1].x = mousePos.x;
		line.getPoints()[1].y = mousePos.y;
		moving = true; 

	    	lineGroup.add(line); 
	    	layer.add(lineGroup);		   
		layer.draw();            
	    }

	});

	stage.on("mousemove", function(){
	    if (moving) {
		var mousePos = stage.getMousePosition();
		var x = mousePos.x;
		var y = mousePos.y;
		line.getPoints()[1].x = mousePos.x;
		line.getPoints()[1].y = mousePos.y;                        
		moving = true;
		layer.draw();
	    }
	});

	stage.on("mouseup", function(){
	   if(drawLinemode){
		    moving = false; 
		    drawLinemode=false;
		    stage.fire('click');		   
		    
		    addAnchor(lineGroup,  line.getPoints()[0].x,  line.getPoints()[0].y, 'leftEnd');
		    addAnchor(lineGroup,  line.getPoints()[1].x,  line.getPoints()[1].y, 'rightEnd');

		    for(var i=1;i<lineGroup.children.length;i++){
		      lineGroup.children[i].show();
		    }	
			undoArray.push({'id':lineGroup._id,'shapeString':saveShape(lineGroup),'action':'added'}); 
			redoArray=[]; 
			selectedShapes.push(lineGroup._id);
					    	    
		    layer.draw();
		    	   
		    drawComplete=true;
	    }
	}); 


	//FREE HAND HANDLERS
	movingFreehand = false;

	var maxX,minX,maxY,minY;

	stage.on("mousedown", function(){
	    if (movingFreehand){
		movingFreehand = false;
		layer.draw();
	    } else if(drawFreehandMode){
	    
	    	path.setAttrs({
		  x: 0,
		  y: 0,
		  stroke: strokecolour,
		  strokeWidth: strokewidth,
		  opacity: strokeopacity,
		  scale: 1,
		  zoom_level: getZoomLevel()
		});
			    
		var mousePos = stage.getMousePosition();
	
		pathString+='M'+mousePos.x+','+mousePos.y;
	
		maxX=mousePos.x;
		maxY=mousePos.y;
		minX=mousePos.x;
		minY=mousePos.y;

		path.setData(pathString);
	
		movingFreehand = true;   
		
	    	pathGroup.add(path); 
	    	layer.add(pathGroup);	
	    		 
		layer.drawScene();            
	    }

	});

	stage.on("mousemove", function(){
	    if (movingFreehand) {
		var mousePos = stage.getMousePosition();
	
		pathString+='L'+mousePos.x+','+mousePos.y;
	
		maxX=Math.max(maxX,mousePos.x);
		maxY=Math.max(maxY,mousePos.y);
		minX=Math.min(minX,mousePos.x);
		minY=Math.min(minY,mousePos.y);
			
		path.setData(pathString);   
			        
		movingFreehand = true;
		layer.drawScene();
	    }
	});

	stage.on("mouseup", function(evt){
	   if(drawFreehandMode){
		    movingFreehand = false; 
		    drawFreehandMode=false;
		    stage.fire('click');
		    
		    addAnchor(pathGroup,  minX, minY, 'topLeft',false);
		    addAnchor(pathGroup,  maxX, minY, 'topRight',false);
		    addAnchor(pathGroup,  minX, maxY, 'bottomLeft',false);
		    addAnchor(pathGroup,  maxX, maxY, 'bottomRight',false);	
		    //addRotator(pathGroup,  (maxX+minX)/2, minY-50, 'rotator');  //-50 for rotator
		    
		    for(var i=1;i<pathGroup.children.length;i++){
		      pathGroup.children[i].show();
		    }	   
			undoArray.push({'id':pathGroup._id,'shapeString':saveShape(pathGroup),'action':'added'}); 
			redoArray=[]; 
			selectedShapes.push(pathGroup._id);
					     	    
		    layer.draw();
		    pathGroup.setDraggable(true);
		    	    
		    drawComplete=true;	    
	    }
	});   



	//POLYGON HANDLERS
	movingPolygon = false;

	stage.on("click", function(){
		if(drawPolygonMode){
			var mousePos = stage.getMousePosition();	
	
			if(firstClick){
				polygon.setAttrs({
				    points: [0,0],
				    stroke: strokecolour,
				    strokeWidth: strokewidth,
				    opacity: strokeopacity,
				    closed: true,
				    zoom_level: getZoomLevel()
				});
				
				polyGroup.add(polygon);
				layer.add(polyGroup); 	
				polygon.setPoints([]);
				polygon.setPoints(polygon.getPoints().concat([{x:mousePos.x,y:mousePos.y}]));	
				index++;
				addAnchor(polyGroup, mousePos.x , mousePos.y, index);
			
				polygon.setPoints(polygon.getPoints().concat([{x:mousePos.x,y:mousePos.y}]));
				index++;
												
				firstClick=false;
				maxX=mousePos.x;
				maxY=mousePos.y;
				minX=mousePos.x;
				minY=mousePos.y;
				
				message= new Kinetic.Text({
					    x: mousePos.x,
					    y: mousePos.y,
					    fontFamily: "Arial",
					    fontSize: 12,
					    fill: strokecolour,
					    text:'Double click to end',
					    stroke: null
					});
					
				polyGroup.add(message);				
			}
			else{
				polygon.setPoints(polygon.getPoints().concat([{x:mousePos.x,y:mousePos.y}]));
				index++;
				addAnchor(polyGroup, polygon.getPoints()[index-1].x , polygon.getPoints()[index-1].y , index-1);
			}
			
			maxX=Math.max(maxX,mousePos.x);
			maxY=Math.max(maxY,mousePos.y);
			minX=Math.min(minX,mousePos.x);
			minY=Math.min(minY,mousePos.y);
	
			movingPolygon = true;			
		    					    
			layer.drawScene();            
	    }

	});	

	stage.on("mousemove", function(){
	    if (movingPolygon) {
		var mousePos = stage.getMousePosition();
	
		polygon.getPoints()[index].x=mousePos.x;
		polygon.getPoints()[index].y=mousePos.y;
		
		message.setX(mousePos.x);
		message.setY(mousePos.y);
		
		maxX=Math.max(maxX,mousePos.x);
		maxY=Math.max(maxY,mousePos.y);
		minX=Math.min(minX,mousePos.x);
		minY=Math.min(minY,mousePos.y);	  
			        
		movingPolygon = true;
		layer.drawScene();
	    }
	});

	stage.on("dblclick", function(){
	   if(drawPolygonMode){
		    movingPolygon = false; 
		    drawPolygonMode=false;
		    
		    polyGroup.children[index+1].remove();		   
		    
		    var array=polygon.getPoints();
		    array.pop();
		    array.pop();
		    polygon.setPoints(array);	   		    
			undoArray.push({'id':polyGroup._id,'shapeString':saveShape(polyGroup),'action':'added'}); 
			redoArray=[]; 
			selectedShapes.push(polyGroup._id);
					    
		    message.remove();    
		    layer.draw();
		    polyGroup.setDraggable(true);		    
	    }
	});	    	

	//Anchor handler
	stage.on('click',function(){
		if(drawComplete){
			drawComplete=false;
			dirtyFlag=true;
			return;
		}
		if(drawPolygonMode)
			return;
			
		for(var i=0;i<layer.children.length;i++){
			for(var j=1;j<layer.children[i].children.length;j++){
	    			layer.children[i].children[j].hide();
	    		}  
		}
		
		selectedShapes=[];
		
		layer.draw();
	});
 	
 	
 	//ARROW HANDLERS
 	
 	movingArrow=false;
 	
	stage.on("mousedown", function(){
	    if (movingArrow){
		movingArrow = false;
		layer.draw();
	    } else if(drawArrowMode){
	    
		arrow.setAttrs({
		    points: [0, 0, 0, 0],
		    stroke: strokecolour,
		    strokeWidth: strokewidth, 
		    opacity: strokeopacity,
		    zoom_level: getZoomLevel()
		});
			    
		var mousePos = stage.getMousePosition();
		//start point and end point are the same
		arrow.getPoints()[0].x = mousePos.x;
		arrow.getPoints()[0].y = mousePos.y;
		arrow.getPoints()[1].x = mousePos.x;
		arrow.getPoints()[1].y = mousePos.y;
		movingArrow = true;  
		
		arrowGroup.add(arrow);
		layer.add(arrowGroup);
		  
		layer.draw();            
	    }

	});

	stage.on("mousemove", function(){
	    if (movingArrow) {
		var mousePos = stage.getMousePosition();
		var x = mousePos.x;
		var y = mousePos.y;
		arrow.getPoints()[1].x = mousePos.x;
		arrow.getPoints()[1].y = mousePos.y;                        
		movingArrow = true;
		  
		fromy=arrow.getPoints()[0].y;    
		fromx=arrow.getPoints()[0].x;
		toy=mousePos.y;
		tox=mousePos.x;
	
		var headlen = 10;   // how long you want the head of the arrow to be, you could calculate this as a fraction of the distance between the points as well.
	    	var angle = Math.atan2(toy-fromy,tox-fromx);
	    	
	    	arrow.setPoints([]);
		arrow.setPoints([fromx, fromy, tox, toy, tox-headlen*Math.cos(angle-Math.PI/6),toy-headlen*Math.sin(angle-Math.PI/6), tox, toy, tox-headlen*Math.cos(angle+Math.PI/6),toy-headlen*Math.sin(angle+Math.PI/6)]);       
		layer.draw();
	    }
	});

	stage.on("mouseup", function(){
	   if(drawArrowMode){
		    movingArrow = false; 
		    drawArrowMode=false;
		     stage.fire('click');
		     
		    addAnchor(arrowGroup,  arrow.getPoints()[0].x,  arrow.getPoints()[0].y, 'leftEnd');
		    addAnchor(arrowGroup,  arrow.getPoints()[1].x,  arrow.getPoints()[1].y, 'rightEnd');

		    for(var i=1;i<arrowGroup.children.length;i++){
		      arrowGroup.children[i].show();
		    }	
		    
			undoArray.push({'id':arrowGroup._id,'shapeString':saveShape(arrowGroup),'action':'added'}); 
			redoArray=[];
			selectedShapes.push(arrowGroup._id); 	
				    	    
		    layer.draw();
		    	   
		    drawComplete=true;
	    }
	});
}


function drawRectangle(attributes,id,isRotation,pos){

    	// DRAW RECTANGLE
    	
    	stopDrawingMode();
    	
	rect = new Kinetic.Rect({
		name: 'shape'
	});
	

    	var rectGroup = new Kinetic.Group({
        	draggable: dragOption
    	});
    	
    	var rotation=0;
    		 
	if(typeof attributes !== 'undefined'){
		if(typeof isRotation !== 'undefined'){
			rect=attributes;
			rectGroup.setX(pos.x);
			rectGroup.setY(pos.y);
		}
		else{
			setAttributes(rect,attributes);
			rect.setRotation(0);
			rectGroup.setX(attributes['x']);
			rectGroup.setY(attributes['y']);
			rotation=attributes['rotation'];
		}
		rect.setX(0);
		rect.setY(0);	
	}    
	
	else{
	    	rect.setAttrs({
			x: 0,
			y: 0,
			width: 100,
			height: 100,
			stroke: strokecolour,
			strokeWidth: strokewidth,
			opacity: strokeopacity,
			zoom_level: getZoomLevel(),
			rotation: 0
		});
		
		rectGroup.setX(stage.getWidth()/2);
		rectGroup.setY(stage.getHeight()/2);	
		
		dirtyFlag=true;
		selectedShapes.push(rectGroup._id);
	}

	rectGroup.setOffset(rect.getWidth()/2,rect.getHeight()/2);
	rectGroup.move(rect.getWidth()/2,rect.getHeight()/2);	

    	layer.add(rectGroup);
    	rectGroup.add(rect);
    	
    	addAnchor(rectGroup, rect.getPosition().x, rect.getPosition().y, 'topLeft');
    	addAnchor(rectGroup, rect.getPosition().x+rect.getWidth(), rect.getPosition().y, 'topRight');
    	addAnchor(rectGroup, rect.getPosition().x, rect.getPosition().y+rect.getHeight(), 'bottomLeft');
    	addAnchor(rectGroup, rect.getPosition().x+rect.getWidth(), rect.getPosition().y+rect.getHeight(), 'bottomRight');    	   	
    	
    	if(typeof isRotation === 'undefined')
    		addRotator(rectGroup, rect.getPosition().x+(rect.getWidth()/2), rect.getPosition().y-50, 'rotator');
	 
	rectGroup.setRotation(rotation);  
	 
    rectGroup.on('dragstart', function () {
        this.moveToTop();
    });
 
    rectGroup.on('mousedown',function(evt){
    
    	if (!evt.ctrlKey){
       		stage.fire('click');
        }
        
    	for(var i=1;i<this.children.length;i++){
    		this.children[i].show();
    	}
    	
    	if(selectedShapes.indexOf(this._id)<0)
    		selectedShapes.push(this._id);
    		
    	layer.draw();
    	evt.cancelBubble = true;
    });
    
    rectGroup.on('click',function(evt){
    	evt.cancelBubble = true;
    });
    
    rectGroup.on('dragmove', function() {
    	dirtyFlag=true;
	document.body.style.cursor = 'pointer';
    });
    
    rectGroup.on('mouseover', function() {
	document.body.style.cursor = 'pointer';
    });

    rectGroup.on('mouseout', function() {
	document.body.style.cursor = 'default';
    });

    rectGroup.on('dragend', function() {
	 undoArray.push({'id':rectGroup._id,'shapeString':saveShape(rectGroup),'action':'modified'});
    });
    
    if(typeof id !== 'undefined'){
    	rectGroup['_id']=id;
    }
    else{
	   undoArray.push({'id':rectGroup._id,'shapeString':saveShape(rectGroup),'action':'added'}); 
	   redoArray=[]; 
    }
    	 
    layer.draw();
    
    return rectGroup;
}
    
function drawEllipse(attributes,id,isRotation,pos){
	
	//Drawing ellipse
	
	stopDrawingMode();
	 
	ellipse = new Kinetic.Ellipse({
		name: 'shape'
	});
	
	var ellipseGroup = new Kinetic.Group({
		draggable: dragOption
	});
	
	var rotation=0;
		 
	if(typeof attributes !== 'undefined'){
		if(typeof isRotation !== 'undefined'){
			ellipse=attributes;
			ellipseGroup.setX(pos.x);
			ellipseGroup.setY(pos.y);
		}
		else{
			setAttributes(ellipse,attributes);
			ellipse.setRotation(0);
			ellipseGroup.setX(attributes['x']);
			ellipseGroup.setY(attributes['y']);
			rotation=attributes['rotation'];
		}
		ellipse.setX(0);
		ellipse.setY(0);
	}
	else{
		ellipse.setAttrs({
			x: 0,
			y: 0,
			radius:{
				x: 50,
				y: 50
			},
			stroke: strokecolour,
			strokeWidth: strokewidth,
			opacity: strokeopacity,
			zoom_level: getZoomLevel(),
			rotation: 0
		});
		
		ellipseGroup.setX(stage.getWidth()/2);
		ellipseGroup.setY(stage.getHeight()/2);
		
		dirtyFlag=true;
		selectedShapes.push(ellipseGroup._id);
	}
	
	ellipseGroup.setOffset(0,0);
    	layer.add(ellipseGroup);
    	ellipseGroup.add(ellipse);
    
	addAnchor(ellipseGroup, ellipse.getPosition().x-ellipse.getWidth()/2, ellipse.getPosition().y-ellipse.getHeight()/2, 'topLeft');
	addAnchor(ellipseGroup, ellipse.getPosition().x+ellipse.getWidth()/2, ellipse.getPosition().y-ellipse.getHeight()/2, 'topRight');
	addAnchor(ellipseGroup, ellipse.getPosition().x-ellipse.getWidth()/2, ellipse.getPosition().y+ellipse.getHeight()/2, 'bottomLeft');
	addAnchor(ellipseGroup, ellipse.getPosition().x+ellipse.getWidth()/2, ellipse.getPosition().y+ellipse.getHeight()/2, 'bottomRight');
	
	if(typeof isRotation === 'undefined')
		addRotator(ellipseGroup, ellipse.getPosition().x, ellipse.getPosition().y-ellipse.getHeight()/2-50, 'rotator'); //-50 for rotator

	ellipseGroup.setRotation(rotation); 
	    
	ellipseGroup.on('dragstart', function () {
		this.moveToTop();
	});

    ellipseGroup.on('mousedown',function(evt){
    	if (!evt.ctrlKey){
       		stage.fire('click');
        }
    	for(var i=1;i<this.children.length;i++){
    		this.children[i].show();
    	}
    	
    	if(selectedShapes.indexOf(this._id)<0)
    		selectedShapes.push(this._id);
    		
    	layer.draw();
    	evt.cancelBubble = true;
    });

    ellipseGroup.on('click',function(evt){
    	evt.cancelBubble = true;
    });
        
    ellipseGroup.on('dragmove', function() {
    	dirtyFlag=true;
	document.body.style.cursor = 'pointer';
    });
    
    ellipseGroup.on('mouseover', function() {
	document.body.style.cursor = 'pointer';
    });

    ellipseGroup.on('mouseout', function() {
	document.body.style.cursor = 'default';
    });

    ellipseGroup.on('dragend', function() {
	 undoArray.push({'id':ellipseGroup._id,'shapeString':saveShape(ellipseGroup),'action':'modified'});
    });
    if(typeof id !== 'undefined'){
    	ellipseGroup['_id']=id;
    }   
    else{        
    	undoArray.push({'id':ellipseGroup._id,'shapeString':saveShape(ellipseGroup),'action':'added'}); 
    	redoArray=[]; 
    }
    	        
    layer.draw();
    
    return ellipseGroup;
}
  
function drawLine(attributes,id){   
    
	//DRAW LINE
	stopDrawingMode();
	drawLinemode=true;

	line = new Kinetic.Line({
		name: 'shape'
	});
	
	lineGroup = new Kinetic.Group({
		draggable: dragOption
	});	

	if(typeof attributes !== 'undefined'){
		drawLinemode=false;
		setAttributes(line,attributes);	
		lineGroup.add(line);
		layer.add(lineGroup);
	}            	
	if(typeof attributes !== 'undefined'){
		addAnchor(lineGroup,  line.getPoints()[0].x,  line.getPoints()[0].y, 'leftEnd');
		addAnchor(lineGroup,  line.getPoints()[1].x,  line.getPoints()[1].y, 'rightEnd');
		layer.draw();
	}	
        
    lineGroup.on('mousedown',function(evt){
    	if (!evt.ctrlKey){
       		stage.fire('click');
        }
    	for(var i=1;i<this.children.length;i++){
    		this.children[i].show();
    	}
    	
    	if(selectedShapes.indexOf(this._id)<0)
    		selectedShapes.push(this._id);
    		
    	layer.draw();
    });
    
    lineGroup.on('click',function(evt){
    	evt.cancelBubble = true;
    });

    lineGroup.on('dragmove', function() {
    	dirtyFlag=true;
	document.body.style.cursor = 'pointer';
    });
    
    lineGroup.on('mouseover', function() {
	document.body.style.cursor = 'pointer';
    });

    lineGroup.on('mouseout', function() {
	document.body.style.cursor = 'default';
    });

    lineGroup.on('dragend', function() {
	 undoArray.push({'id':lineGroup._id,'shapeString':saveShape(lineGroup),'action':'modified'});
    });
    
    if(typeof id !== 'undefined'){
    	lineGroup['_id']=id;
    }   
    else{
    	   if(typeof attributes !== 'undefined') {    
	    	undoArray.push({'id':lineGroup._id,'shapeString':saveShape(lineGroup),'action':'added'}); 
	    	redoArray=[]; 
	    }
    	
    }
                    
}        

function drawFreeHand(attributes,id){
 
	//DRAW FREE HAND
	stopDrawingMode();
	drawFreehandMode=true;
	pathString='';  

	path = new Kinetic.Path({
		name: 'shape'
	});
	
	pathGroup = new Kinetic.Group({
		draggable: dragOption
	});	
	 
	if(typeof attributes !== 'undefined'){
		drawFreehandMode=false;
		setAttributes(path,attributes);
		// setData parses the SVG string to create data array
		path.setData(attributes['data']);
		
		pathGroup.add(path);
		layer.add(pathGroup);		
	}
	
	if(typeof attributes!=='undefined'){
	    	/*addAnchor(pathGroup, path.getX(), path.getY(), 'topLeft');
	    	addAnchor(pathGroup, path.getX()+path.getWidth(), path.getY(), 'topRight');
	    	addAnchor(pathGroup, path.getX(), path.getY()+path.getHeight(), 'bottomLeft');
	    	addAnchor(pathGroup, path.getX()+path.getWidth(), path.getY()+path.getHeight(), 'bottomRight');
	    	addRotator(pathGroup, ellipse.getPosition().x-(ellipse.getWidth()/2), ellipse.getPosition().y-ellipse.getHeight()-50, 'rotator'); //-50 for rotator */ 		
	}

    pathGroup.on('mousedown',function(evt){
    	if (!evt.ctrlKey){
       		stage.fire('click');
        }
    	for(var i=1;i<this.children.length;i++){
    		this.children[i].show();
    	}
    	
    	if(selectedShapes.indexOf(this._id)<0)
    		selectedShapes.push(this._id);
    		
    	layer.draw();
    	evt.cancelBubble = true;
    });	

    pathGroup.on('click',function(evt){
    	evt.cancelBubble = true;
    });
    
    pathGroup.on('dragmove', function() {
    	dirtyFlag=true;
	document.body.style.cursor = 'pointer';
    });
    
    pathGroup.on('mouseover', function() {
	document.body.style.cursor = 'pointer';
    });

    pathGroup.on('mouseout', function() {
	document.body.style.cursor = 'default';
    });
 
    pathGroup.on('dragend', function() {
	 undoArray.push({'id':pathGroup._id,'shapeString':saveShape(pathGroup),'action':'modified'});
    });
    
    if(typeof id !== 'undefined'){
    	pathGroup['_id']=id;
    }   
    else{
    	  if(typeof attributes !== 'undefined') {       
	    	undoArray.push({'id':pathGroup._id,'shapeString':saveShape(pathGroup),'action':'added'}); 
	    	redoArray=[]; 
    	}
    }   
    
    layer.draw();
}


  
  
function drawPolygon(attributes,id){
    	//DRAW POLYGON
    	stopDrawingMode();
	drawPolygonMode=true;
	index=-1;
	firstClick=true;
	  
	polygon = new Kinetic.Polygon({
		name: 'shape'
	});

	polyGroup = new Kinetic.Group({
		draggable: dragOption
	});
	 
	if(typeof attributes !== 'undefined'){
		drawPolygonMode=false;
		setAttributes(polygon,attributes);
		
		polyGroup.add(polygon);
		layer.add(polyGroup); 				
	}      	 
	if(typeof attributes !== 'undefined'){
		for(var k=0;k<polygon.getPoints().length;k++){
			addAnchor(polyGroup,polygon.getPoints()[k]['x'],polygon.getPoints()[k]['y'],k);
		}
	}		
	
    polyGroup.on('mousedown',function(evt){
    	if (!evt.ctrlKey){
       		stage.fire('click');
        }
    	for(var i=1;i<this.children.length;i++){
    		this.children[i].show();
    	}
    	
    	if(selectedShapes.indexOf(this._id)<0)
    		selectedShapes.push(this._id);
    	layer.draw();
    });	
    
    polyGroup.on('click', function(evt) {
	evt.cancelBubble=true;
    });

    polyGroup.on('dragmove', function() {
    	dirtyFlag=true;
	document.body.style.cursor = 'pointer';
    });
    
    polyGroup.on('mouseover', function() {
	document.body.style.cursor = 'pointer';
    });

    polyGroup.on('mouseout', function() {
	document.body.style.cursor = 'default';
    });

    polyGroup.on('dragend', function() {
	 undoArray.push({'id':polyGroup._id,'shapeString':saveShape(polyGroup),'action':'modified'});
    });
    
    if(typeof id !== 'undefined'){
    	polyGroup['_id']=id;
    }   
    else{  
        if(typeof attributes !== 'undefined') {  
	    	undoArray.push({'id':polyGroup._id,'shapeString':saveShape(polyGroup),'action':'added'}); 
	    	redoArray=[]; 
    	}
    }
        
    layer.draw();
}

function drawText(attributes,id){
	stopDrawingMode();
	
	text = new Kinetic.Text({
		name: 'shape'
	});
	
	var textGroup = new Kinetic.Group({
		draggable: dragOption
	});		
	 
	if(typeof attributes !== 'undefined'){
		text['textWidth']=attributes['width'];
		text['textHeight']=attributes['height'];
		attributes['width']="auto";
		attributes['height']="auto";
		text['textArr']=[];
		text['textArr'].push({'text':attributes['text'],'width':text['textWidth']});
		setAttributes(text,attributes);
		text.setFontSize(text['textHeight']);
	}
	else{
		text.setAttrs({
			x:stage.getWidth()/2,
			y:stage.getHeight()/2,
			text: textinput,
			fontSize: fontSize,
			fontFamily: 'Arial',
			fill: strokecolour,
			strokeWidth:0,
			stroke:strokecolour,
			opacity:strokeopacity,
			zoom_level:getZoomLevel()
		});
		
		dirtyFlag=true;
		selectedShapes.push(textGroup._id);
        }       	 
	
	textGroup.add(text);	
	layer.add(textGroup);
	
	addAnchor(textGroup,  text.getX(), text.getY(), 'topLeft',false);
	addAnchor(textGroup,  text.getX()+text.getWidth(), text.getY(), 'topRight',false);
	addAnchor(textGroup,  text.getX(), text.getY()+text.getHeight(), 'bottomLeft',false);
	addAnchor(textGroup,  text.getX()+text.getWidth(), text.getY()+text.getHeight(), 'bottomRight',false);	
	//addRotator(textGroup,  (text.getX()+text.getWidth())/2, text.getY()-50, 'rotator');  //-50 for rotator		
	
	textGroup.on('mousedown',function(evt){
	    	if (!evt.ctrlKey){
	       		stage.fire('click');
		}
		for(var i=1;i<this.children.length;i++){
			this.children[i].show();
		}
		
    		if(selectedShapes.indexOf(this._id)<0)
    			selectedShapes.push(this._id);	
    				
		layer.draw();
	});	

	textGroup.on('click', function(evt) {
		evt.cancelBubble=true;
	});

	textGroup.on('dragmove', function() {
		dirtyFlag=true;
		document.body.style.cursor = 'pointer';
	});

	textGroup.on('mouseover', function() {
		document.body.style.cursor = 'pointer';
	});

	textGroup.on('mouseout', function() {
		document.body.style.cursor = 'default';
	});

	textGroup.on('dragend', function() {
		undoArray.push({'id':textGroup._id,'shapeString':saveShape(textGroup),'action':'modified'});
	});

	if(typeof id !== 'undefined'){
		textGroup['_id']=id;
	}   
	else{  
		undoArray.push({'id':textGroup._id,'shapeString':saveShape(textGroup),'action':'added'}); 
		redoArray=[]; 
	}
	layer.draw();	
}

function drawArrow(attributes,id){

	stopDrawingMode();
	drawArrowMode=true;

	arrow = new Kinetic.Line({
		name: 'shape'
	});
	
	arrowGroup = new Kinetic.Group({
		draggable: dragOption
	});	
	 
	if(typeof attributes !== 'undefined'){
		drawArrowMode=false;
		setAttributes(arrow,attributes);
		
		arrowGroup.add(arrow);	
		layer.add(arrowGroup);	
	}	
        arrow['className']='Arrow';      	

        if(typeof attributes !== 'undefined'){
		addAnchor(arrowGroup,  arrow.getPoints()[0].x,  arrow.getPoints()[0].y, 'leftEnd');
		addAnchor(arrowGroup,  arrow.getPoints()[1].x,  arrow.getPoints()[1].y, 'rightEnd');	
	}
	
	arrowGroup.on('mousedown',function(evt){
		if (!evt.ctrlKey){
			stage.fire('click');
		}
		for(var i=1;i<this.children.length;i++){
			this.children[i].show();
		}

		if(selectedShapes.indexOf(this._id)<0)
		selectedShapes.push(this._id);
		layer.draw();
	});	

	arrowGroup.on('click', function(evt) {
		evt.cancelBubble=true;
	});

	arrowGroup.on('dragmove', function() {
		dirtyFlag=true;
		document.body.style.cursor = 'pointer';
	});

	arrowGroup.on('mouseover', function() {
		document.body.style.cursor = 'pointer';
	});

	arrowGroup.on('mouseout', function() {
		document.body.style.cursor = 'default';
	});
 
	arrowGroup.on('dragend', function() {
		 undoArray.push({'id':arrowGroup._id,'shapeString':saveShape(arrowGroup),'action':'modified'});
	});

	if(typeof id !== 'undefined'){
		arrowGroup['_id']=id;
	}   
	else{ 
		if(typeof attributes !== 'undefined') {        
			undoArray.push({'id':arrowGroup._id,'shapeString':saveShape(arrowGroup),'action':'added'}); 
			redoArray=[]; 
		}
	}      
	layer.draw();
}

/*stage.on('mousedown',function(){
	if(isDrawingMode())
		return;
	
	
})*/

function isDrawingMode(){
	return drawLinemode||drawFreehandMode||drawPolygonMode;
}

function stopDrawingMode(){
	stage.fire('click');
	drawLinemode=false;
	drawFreehandMode=false;
	drawPolygonMode=false;
	drawArrowMode=false;
}


function setAttributes(shape, attributes){
	for(var key in attributes){
		shape["attrs"][key]=attributes[key];
	}
}

function removeShapes(){
	var obj = stage;
    	var layerObj=obj["children"][0];
    	var groupsObj=layerObj["children"];	
    	
    	for(var i=0;i<selectedShapes.length;i++){
		for(var j=0;j<groupsObj.length;j++){
			if(groupsObj[j]._id===selectedShapes[i]){
				undoArray.push({'id':groupsObj[j]._id,'shapeString':saveShape(groupsObj[j]),'action':'deleted'});
				groupsObj[j].destroy();
				break;
			}
		}
    	}
    	
    	selectedShapes=[];
    	layer.draw();
    	
    	dirtyFlag=true;
}

function Save(scale,offset,zoom){
		var obj = stage;
    	var layerObj=obj["children"][0];
    	var groupsObj=layerObj["children"];
    	
    	var scaleX=scale.x;
    	var scaleY=scale.y;
    	
    	var visualObjects=[];
    	for(var i=0;i<groupsObj.length;i++){
    	
    		var shapes=groupsObj[i]["children"];
    		var shape= shapes[0]["className"];
		
    		var pos= shapes[0].getPosition();
				
    		var params=JSON.parse(JSON.stringify(shapes[0]["attrs"]));
    		var abspos=JSON.parse(JSON.stringify(shapes[0].getAbsolutePosition()));
    		params['type']=shape;
    		params['x']=abspos.x*(1/scaleX)+offset.x;
    		params['y']=abspos.y*(1/scaleY)+offset.y;
    		params['rotation']=0;
    		
    		
    		if(groupsObj[i]['attrs']['rotation']!==0 && (shape==="Rect"||shape==="Ellipse")){
			var topLeft = groupsObj[i].get('.topLeft')[0];
			var bottomRight = groupsObj[i].get('.bottomRight')[0];
			
			var topabs=topLeft.getAbsolutePosition();
			var bottomabs=bottomRight.getAbsolutePosition();

			var centerX=(topabs.x+bottomabs.x)/2;
			var centerY=(topabs.y+bottomabs.y)/2;
			
			if(shape==="Rect"){
    				params['x']=(centerX-params['width']/2)*(1/scaleX)+offset.x;
    				params['y']=(centerY-params['height']/2)*(1/scaleY)+offset.y;
    			}
			if(shape==="Ellipse"){
    				params['x']=(centerX)*(1/scaleX)+offset.x;
    				params['y']=(centerY)*(1/scaleY)+offset.y;
    			}    			
    			params['rotation']=groupsObj[i]['attrs']['rotation'];
    		}
    		
    		if('width' in params)
    			params['width']=params['width']*(1/scaleX);
    		
    		if('height' in params)
    			params['height']=params['height']*(1/scaleY);

    		if('radiusX' in params)
    			params['radiusX']=params['radiusX']*(1/scaleX);
    		
    		if('radiusY' in params)
    			params['radiusY']=params['radiusY']*(1/scaleY);	
    		
    		if('points' in params){
    			if(params['type']==='Line'){
	    			var temp=[];
	    			for(var j=0;j<params['points'].length;j++){
	    				temp.push(((params['points'][j]['x']*(1/scaleX))+params['x']).toString());
	    				temp.push(((params['points'][j]['y']*(1/scaleY))+params['y']).toString());
	    			}
	    			params['points']=temp;
    			}
    			else{
	    			var temp=[];
	    			for(var j=0;j<params['points'].length;j++){
	    				temp.push(((params['points'][j]['x']*(1/scaleX))+params['x']).toString());
	    				temp.push(((params['points'][j]['y']*(1/scaleY))+params['y']).toString());
	    			}
	    			
					pointStrings='M'+temp[0]+','+temp[1];
					for (var j=2;j<temp.length;j+=2) {			
						pointStrings+='L'+temp[j]+','+temp[j+1];
					}			
	    			params['points']=pointStrings;   			
    			}
    		}
    			
    		if('data' in params && params['data']!==''){
			var ret = [];
			var pointStrings = params['data'].substring(1).split("L");
			for (var j=0;j<pointStrings.length;j++) {			
				var str = pointStrings[j].split(",");
				ret.push(parseFloat(str[0])/scaleX+params['x']);
				ret.push(parseFloat(str[1])/scaleY+params['y']);
			}
			
			pointStrings='M'+ret[0]+','+ret[1];
			for (var j=2;j<ret.length;j+=2) {				
				pointStrings+='L'+ret[j]+','+ret[j+1];
			}
			params['data']=pointStrings;	
    		}
    		
    		//handling for text objects
    		if(shape==="Text"){    			
    			params['width']=JSON.parse(JSON.stringify(shapes[0]['textWidth']))*(1/scaleX);
    			params['height']=JSON.parse(JSON.stringify(shapes[0]['textHeight']))*(1/scaleY);
    			params['fontSize']=params['fontSize'];
    		}	
    				
    		shapes[0].setX(pos.x);
    		shapes[0].setY(pos.y); 
    		
    		params['id']=groupsObj[i]['_id'];
    		params['zoom_level']=params['zoom_level'];
    		
    		visualObjects.push(params);
    	}
	return visualObjects;
}
function getShapePoints(shape,scale,offset){
	
	var scaleX=scale.x;
	var scaleY=scale.y;
    
	shape = JSON.parse(shape);
	
    shape['x']=(shape['x']-offset.x)*(scaleX);
	shape['y']=(shape['y']-offset.y)*(scaleY);

	if('width' in shape)
		shape['width']=shape['width']*(scaleX);

	if('height' in shape)
		shape['height']=shape['height']*(scaleY);

	if('radiusX' in shape)
		shape['radiusX']=shape['radiusX']*(scaleX);

	if('radiusY' in shape)
		shape['radiusY']=shape['radiusY']*(scaleY);   			

	if(shape["type"] === 'Ellipse' || shape["type"] === 'Rect'){
		return JSON.stringify(shape);
	}
	if('points' in shape){
		if(shape["type"]==='Line'){
			var temp=[];
			for(var j=0;j<shape['points'].length;j+=2){
				var point={};
				point['x']=(shape['points'][j]-offset.x)*scaleX;
				point['y']=(shape['points'][j+1]-offset.y)*scaleY;
				temp.push(point);
			}
			shape['points']=temp;
			
			shape['x']=(shape['points'][0]['x']+shape['points'][0]['x'])/2;
			shape['y']=(shape['points'][0]['y']+shape['points'][0]['y'])/2;
		}
		else{
			var ret = [];
			var pointStrings = shape['points'].substring(1).split("L");
			for (var j=0;j<pointStrings.length;j++) {		
				var str = pointStrings[j].split(",");
				ret.push((parseFloat(str[0])-offset.x)*scaleX);
				ret.push((parseFloat(str[1])-offset.y)*scaleY);
			}
		
			var temp=[];
			for(var j=0;j<ret.length;j+=2){
				var point={};
				point['x']=ret[j];
				point['y']=ret[j+1];
				temp.push(point);
			}	
					
			shape['points']=temp;
		
			shape['x']=(shape['points'][0]['x']+shape['points'][0]['x'])/2;
			shape['y']=(shape['points'][0]['y']+shape['points'][0]['y'])/2; 			
		}
	}
	if('data' in shape){
		var ret = [];
		var pointStrings = shape['data'].substring(1).split("L");
		for (var j=0;j<pointStrings.length;j++) {		
			var str = pointStrings[j].split(",");
			ret.push((parseFloat(str[0])-offset.x)*scaleX);
			ret.push((parseFloat(str[1])-offset.y)*scaleY);
		}
		
		pointStrings='M'+ret[0]+','+ret[1];
		for (var j=2;j<ret.length;j+=2) {			
			pointStrings+='L'+ret[j]+','+ret[j+1];
		}
		shape['data']=pointStrings;	
		
		shape['x']=ret[0];
		shape['y']=ret[1];		
	}	
	return JSON.stringify(shape);
}
function LoadShapes(shapes,scale,offset){
	
    	var scaleX=scale.x;
    	var scaleY=scale.y;
 	
	shapes= JSON.parse(shapes);
	
	if(firtLoadFlag){
		undoBound=shapes.length;
 		undoArray=[];
 		redoArray=[];		
	}	
	
	for(var i=0; i<shapes.length;i++){
    		
    		shapes[i]['x']=(shapes[i]['x']-offset.x)*(scaleX);
    		shapes[i]['y']=(shapes[i]['y']-offset.y)*(scaleY);
    		
    		if('width' in shapes[i])
    			shapes[i]['width']=shapes[i]['width']*(scaleX);
    		
    		if('height' in shapes[i])
    			shapes[i]['height']=shapes[i]['height']*(scaleY);

    		if('radiusX' in shapes[i])
    			shapes[i]['radiusX']=shapes[i]['radiusX']*(scaleX);
    		
    		if('radiusY' in shapes[i])
    			shapes[i]['radiusY']=shapes[i]['radiusY']*(scaleY);   			
    		
    		if('points' in shapes[i]){
    			if(shapes[i]["type"]==='Line'){
	    			var temp=[];
	    			for(var j=0;j<shapes[i]['points'].length;j+=2){
	    				var point={};
	    				point['x']=(shapes[i]['points'][j]-offset.x)*scaleX;
	    				point['y']=(shapes[i]['points'][j+1]-offset.y)*scaleY;
	    				temp.push(point);
	    			}
	    			shapes[i]['points']=temp;
	    			
	    			shapes[i]['x']=0;
	    			shapes[i]['y']=0;
    			}
    			else{
				var ret = [];
				var pointStrings = shapes[i]['points'].substring(1).split("L");
				for (var j=0;j<pointStrings.length;j++) {		
					var str = pointStrings[j].split(",");
					ret.push((parseFloat(str[0])-offset.x)*scaleX);
					ret.push((parseFloat(str[1])-offset.y)*scaleY);
				}
				
	    			var temp=[];
	    			for(var j=0;j<ret.length;j+=2){
	    				var point={};
	    				point['x']=ret[j];
	    				point['y']=ret[j+1];
	    				temp.push(point);
	    			}	
	    						
	    			shapes[i]['points']=temp;
	    			
	    			shapes[i]['x']=0;
	    			shapes[i]['y']=0;  			
    			
    			}
    		}
    		
    		if('data' in shapes[i]){
			var ret = [];
			var pointStrings = shapes[i]['data'].substring(1).split("L");
			for (var j=0;j<pointStrings.length;j++) {		
				var str = pointStrings[j].split(",");
				ret.push((parseFloat(str[0])-offset.x)*scaleX);
				ret.push((parseFloat(str[1])-offset.y)*scaleY);
			}
			
			pointStrings='M'+ret[0]+','+ret[1];
			for (var j=2;j<ret.length;j+=2) {			
				pointStrings+='L'+ret[j]+','+ret[j+1];
			}
			shapes[i]['data']=pointStrings;	
			
			shapes[i]['x']=0;
			shapes[i]['y']=0;		
    		}	
    		
		var type= shapes[i]["type"];
		
		var id=shapes[i]['id'];
	
		switch(type){
		
			case 'Rect': drawRectangle(shapes[i],id);
				
					break;
			case 'Ellipse': drawEllipse(shapes[i],id);
					break;
			case 'Line': drawLine(shapes[i],id);
					break;
			case 'Path': drawFreeHand(shapes[i],id);
					break;
			case 'Polygon': drawPolygon(shapes[i],id);
					break;
			case 'Arrow': drawArrow(shapes[i],id);
					break;
			case 'Text': drawText(shapes[i],id);
					break;				
		}
	
	}
	
	stage.fire('click');
}


function saveShape(group){
    var offset=drawingOffset;
	var scaleX=drawingScale.x;
	var scaleY=drawingScale.y;
	
	var shapes = group["children"];
	var shape= shapes[0]["className"];

	var pos= shapes[0].getPosition();
		
	//var params=JSON.parse(JSON.stringify(shapes[0]["attrs"]));
	//var abspos=JSON.parse(JSON.stringify(shapes[0].getAbsolutePosition()));
	//params['type']=shape;
	
	//params['x']=abspos.x*(1/scaleX)+offset.x;
	//params['y']=abspos.y*(1/scaleY)+offset.y;

	if(shapes[0]['attrs']['rotation']!==0 && (shape==="Rect"||shape==="Ellipse")){
		var topLeft = shapes[0].get('.topLeft')[0];
		var bottomRight = shapes[0].get('.bottomRight')[0];
		
		var topabs=topLeft.getAbsolutePosition();
		var bottomabs=bottomRight.getAbsolutePosition();

		var centerX=(topabs.x+bottomabs.x)/2;
		var centerY=(topabs.y+bottomabs.y)/2;
		
		if(shape==="Rect"){
			params['x']=(centerX-params['width']/2)*(1/scaleX)+offset.x;
			params['y']=(centerY-params['height']/2)*(1/scaleY)+offset.y;
		}
		if(shape==="Ellipse"){
			params['x']=(centerX)*(1/scaleX)+offset.x;
			params['y']=(centerY)*(1/scaleY)+offset.y;
		}    			
		params['rotation']=shapes[0]['attrs']['rotation'];
		params['type']=shape;
	}else{
		var params=JSON.parse(JSON.stringify(shapes[0]["attrs"]));
		var abspos=JSON.parse(JSON.stringify(shapes[0].getAbsolutePosition()));
		
		params['type']=shape;
		
		params['x']=abspos.x*(1/scaleX)+offset.x;
		params['y']=abspos.y*(1/scaleY)+offset.y;
	}
    		
	if('width' in params)
		params['width']=params['width']*(1/scaleX);

	if('height' in params)
		params['height']=params['height']*(1/scaleY);

	if('radiusX' in params)
		params['radiusX']=params['radiusX']*(1/scaleX);

	if('radiusY' in params)
		params['radiusY']=params['radiusY']*(1/scaleY);

	if('points' in params){
		if(params['type']==='Line'){
			var temp=[];
			for(var j=0;j<params['points'].length;j++){
				temp.push(((params['points'][j]['x']*(1/scaleX))+params['x']).toString());
				temp.push(((params['points'][j]['y']*(1/scaleY))+params['y']).toString());
			}
			params['points']=temp;
		}
		else{
			var temp=[];
			for(var j=0;j<params['points'].length;j++){
				temp.push(((params['points'][j]['x']*(1/scaleX))+params['x']).toString());
				temp.push(((params['points'][j]['y']*(1/scaleY))+params['y']).toString());
			}
		
			pointStrings='M'+temp[0]+','+temp[1];
			for (var j=2;j<temp.length;j+=2) {			
				pointStrings+='L'+temp[j]+','+temp[j+1];
			}			
			params['points']=pointStrings;			
		}
	}
	
	if('data' in params && params['data']!==''){
		var ret = [];
		var pointStrings = params['data'].substring(1).split("L");
		for (var j=0;j<pointStrings.length;j++) {			
			var str = pointStrings[j].split(",");
			ret.push(parseFloat(str[0])/scaleX+params['x']);
			ret.push(parseFloat(str[1])/scaleY+params['y']);
		}
	
		pointStrings='M'+ret[0]+','+ret[1];
		for (var j=2;j<ret.length;j+=2) {				
			pointStrings+='L'+ret[j]+','+ret[j+1];
		}
		params['data']=pointStrings;	
	}

	//handling for text objects
	if(shape==="Text"){
		params['width']=JSON.parse(JSON.stringify(shapes[0]['textWidth']))*(1/scaleX);
		params['height']=JSON.parse(JSON.stringify(shapes[0]['textHeight']))*(1/scaleY);
		params['fontSize']=params['fontSize']/scaleY;
	}	
		
	shapes[0].setX(pos.x);
	shapes[0].setY(pos.y); 
	
	params['zoom_level'] = getZoomLevel();
	shapes[0]['attrs']['zoom_level'] = getZoomLevel();
	return params;   		
}

function loadShape(shapeString,id){

    var offset=drawingOffset;
	var scaleX=drawingScale.x;
	var scaleY=drawingScale.y;	
    	
	var shape=JSON.parse(JSON.stringify(shapeString));
	
    		
	shape['x']=(shape['x']-offset.x)*(scaleX);
	shape['y']=(shape['y']-offset.y)*(scaleY);

	if('width' in shape)
		shape['width']=shape['width']*(scaleX);

	if('height' in shape)
		shape['height']=shape['height']*(scaleY);

	if('radiusX' in shape)
		shape['radiusX']=shape['radiusX']*(scaleX);

	if('radiusY' in shape)
		shape['radiusY']=shape['radiusY']*(scaleY);   			

	if('points' in shape){
		if(shape["type"]==='Line'){
			var temp=[];
			for(var j=0;j<shape['points'].length;j+=2){
				var point={};
				point['x']=(shape['points'][j]-offset.x)*scaleX;
				point['y']=(shape['points'][j+1]-offset.y)*scaleY;
				temp.push(point);
			}
			shape['points']=temp;
		
			shape['x']=0;
			shape['y']=0;
		}
		else{
			var ret = [];
			var pointStrings = shape['points'].substring(1).split("L");
			for (var j=0;j<pointStrings.length;j++) {		
				var str = pointStrings[j].split(",");
				ret.push((parseFloat(str[0])-offset.x)*scaleX);
				ret.push((parseFloat(str[1])-offset.y)*scaleY);
			}
		
			var temp=[];
			for(var j=0;j<ret.length;j+=2){
				var point={};
				point['x']=ret[j];
				point['y']=ret[j+1];
				temp.push(point);
			}	
					
			shape['points']=temp;
		    
			shape['x']=0;
			shape['y']=0;  			
			
		}
	}

	if('data' in shape){
		var ret = [];
		var pointStrings = shape['data'].substring(1).split("L");
		for (var j=0;j<pointStrings.length;j++) {		
			var str = pointStrings[j].split(",");
			ret.push((parseFloat(str[0])-offset.x)*scaleX);
			ret.push((parseFloat(str[1])-offset.y)*scaleY);
		}
	
		pointStrings='M'+ret[0]+','+ret[1];
		for (var j=2;j<ret.length;j+=2) {			
			pointStrings+='L'+ret[j]+','+ret[j+1];
		}
		shape['data']=pointStrings;	
	
		shape['x']=0;
		shape['y']=0;		
	}
	    					
	var type= shape["type"];

	switch(type){

		case 'Rect': group=drawRectangle(shape,id);
				break;
		case 'Ellipse': group=drawEllipse(shape,id);
				break;
		case 'Line': drawLine(shape,id);
				break;
		case 'Path': drawFreeHand(shape,id);
				break;
		case 'Polygon': drawPolygon(shape,id);
				break;
		case 'Arrow': drawArrow(shape,id);
				break;	
		case 'Text': drawText(shape,id);
				break;						
	}
	
	stage.fire('click');
}

//to keep check of very first loading of objects
var firtLoadFlag=true;

function setFirstLoadFlag(flag){
	firtLoadFlag=flag;
}

//lowe bound on undoArray
var undoBound;
var undoArray=[];
var redoArray=[];

var statePointer=-1;

function undo(){
	
	if(undoArray.length===undoBound)
		return;

		
	var currentShape=undoArray.pop();
	
	var action=currentShape['action'];
	var id=currentShape['id'];
	var shapeString=currentShape['shapeString'];	

	var obj = stage;
    	var layerObj=obj["children"][0];
    	var groupsObj=layerObj["children"];
    		
	for(var child=0;child<groupsObj.length;child++){
		if(groupsObj[child]['_id']===id){
			groupsObj[child].destroy();
			break;
		}
	}
	
	if(action==='added'){
		redoArray.push({'id':id,'shapeString':shapeString,'action':'add'});
	}
	
	if(action==='modified'){
		for(var k=undoArray.length-1;k>=0;k--){
			if(undoArray[k]['id']===id){
				loadShape(undoArray[k]['shapeString'],id);
				break;
			}
		}
		redoArray.push({'id':id,'shapeString':shapeString,'action':'modify'});
	}
	
	if(action==='deleted'){
		loadShape(shapeString,id);
		redoArray.push({'id':id,'shapeString':shapeString,'action':'delete'});
	}
	
	if(undoArray.length===undoBound)
		dirtyFlag=false;
		
	layer.draw();
}

function redo(){
	if(redoArray.length===0)
		return;
	
	dirtyFlag=true;		
	var currentShape=redoArray.pop();
	
	var action=currentShape['action'];
	var id=currentShape['id'];
	var shapeString=currentShape['shapeString'];	

	var obj = stage;
    	var layerObj=obj["children"][0];
    	var groupsObj=layerObj["children"];
    		
	for(var child=0;child<groupsObj.length;child++){
		if(groupsObj[child]['_id']===id){
			groupsObj[child].destroy();
			break;
		}
	}
	
	if(action==='add'){	
		loadShape(shapeString,id);
		undoArray.push({'id':id,'shapeString':shapeString,'action':'added'});
	}
	
	if(action==='modify'){	
		loadShape(shapeString,id);
		undoArray.push({'id':id,'shapeString':shapeString,'action':'modified'});
	}
	
	if(action==='delete'){
		undoArray.push({'id':id,'shapeString':shapeString,'action':'deleted'});
	}	
	
	layer.draw();	
}

