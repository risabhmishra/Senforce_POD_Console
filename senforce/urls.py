
######## Risabh Mishra ##########


from django.conf.urls import url
from . import views

app_name = 'senforce_app'

urlpatterns = [
    url(r'^index/$', views.index, name='index'),
    url(r'^$', views.index, name='index'),
    url(r'^camera/$', views.camera, name='camera'),
    url(r'^battery/$', views.getbattery, name='battery'),
    url(r'^server/$', views.server, name='server'),
    url(r'^buttonlink/(?P<button_num>[0-9]+)/$', views.buttonlink, name='buttonlink'),
    url(r'^gps/$', views.gps, name='gps'),
    url(r'^radar/$', views.radar, name='radar'),

]

