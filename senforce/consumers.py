from channels import Group

#connect
def ws_add(message):
    message.reply_channel.send({'accept':True})
    Group('server').add(message.reply_channel)

#receive
def ws_message(message):
    Group('server').send({'text':})

#disconnect
def ws_disconnect(message):
    Group('server').discard(message.reply_channel)
