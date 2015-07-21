
var wsUri = "ws://"+document.location.host+document.location.pathname+"mysocketendpoint";
var websocket = new WebSocket(wsUri);

websocket.onerror = function(evt){
    onError(evt);
};

function onError(evt){
    writeToScreen('<span style="color: red;">ERROR</span> '+ evt.data);
}
