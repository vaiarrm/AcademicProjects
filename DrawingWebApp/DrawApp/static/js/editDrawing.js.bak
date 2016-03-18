var raster = new Raster('canvasImage');
    raster.position = view.center;
    raster.scale(0.5);

var pathDraw;
function onMouseDown(event) {
    pathDraw = new Path();
    pathDraw.strokeColor = 'black';
	pathDraw.add(event.point);
}

function onMouseDrag(event) {
    if(Key.isDown('e')) {
        pathDraw.strokeColor = 'white';
        pathDraw.strokeWidth=10;
        pathDraw.add(event.point);
        pathDraw.smooth();
    }else if(Key.isDown('r')) {
        pathDraw.strokeColor = 'red';
        pathDraw.add(event.point);
        pathDraw.smooth();
    }else if(Key.isDown('g')) {
        pathDraw.strokeColor = 'green';
        pathDraw.add(event.point);
        pathDraw.smooth();
    }else if(Key.isDown('y')) {
        pathDraw.strokeColor = 'yellow';
        pathDraw.add(event.point);
        pathDraw.smooth();
    }else if(Key.isDown('b')) {
        pathDraw.strokeColor = 'blue';
        pathDraw.add(event.point);
        pathDraw.smooth();
    }else if(Key.isDown('2')) {
        pathDraw.strokeWidth = 2;
        pathDraw.add(event.point);
        pathDraw.smooth();
    }else if(Key.isDown('3')) {
        pathDraw.strokeWidth = 3;
        pathDraw.add(event.point);
        pathDraw.smooth();
    }else if(Key.isDown('4')) {
        pathDraw.strokeWidth = 4;
        pathDraw.add(event.point);
        pathDraw.smooth();
    }else if(Key.isDown('5')) {
        pathDraw.strokeWidth = 5;
        pathDraw.add(event.point);
        pathDraw.smooth();
    }else if(Key.isDown('6')) {
        pathDraw.strokeWidth = 6;
        pathDraw.add(event.point);
        pathDraw.smooth();
    }else if(Key.isDown('7')) {
        pathDraw.strokeWidth = 7;
        pathDraw.add(event.point);
        pathDraw.smooth();
    }else if(Key.isDown('8')) {
        pathDraw.strokeWidth = 8;
        pathDraw.add(event.point);
        pathDraw.smooth();
    }else if(Key.isDown('9')) {
        pathDraw.strokeWidth = 9;
        pathDraw.add(event.point);
        pathDraw.smooth();
    }else {
        pathDraw.strokeColor = 'black';
        pathDraw.add(event.point);
        pathDraw.smooth();
    }
}
