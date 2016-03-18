//document.getElementById("save").onclick = function(){
function saveme() {
       var base64BlobData = document.getElementById('myCanvas').toDataURL();
       var base64Blob = document.getElementById("imageFormBlob");
       base64Blob.value = base64BlobData;
       document.getElementById("saveImage").submit();
}
function myCanvas() {
    var c = document.getElementById("myCanvas");
    var ctx = c.getContext("2d");
    var img = document.getElementById("canvasImage");
    ctx.drawImage(img,0,0,300,300);
}
