$(document).ready(function () {
   var socket = new WebSocket('ws://127.0.0.1:8000/server/');
   socket.onopen = websocket_welcome;
   socket.onmessage = websocket_message_show;

});