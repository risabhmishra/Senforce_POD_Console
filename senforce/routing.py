from channels.routing import route
from senforce import consumers

channel_routing = [
    route('websocket.connect',consumers.ws_add,path=r'^/server/$'),
    route('websocket.receive', consumers.ws_message, path=r'^/server/$'),
    route('websocket.disconnect', consumers.ws_disconnect, path=r'^/server/$')

]