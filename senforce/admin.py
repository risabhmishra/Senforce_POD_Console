
######## Risabh Mishra ##########

from django.contrib import admin

# Register your models here.

from .models import Camera #importing Camera Model
from .models import Gps  #importing Gps Model

admin.site.register(Camera) #Registering Camera Model in Admin Panel
admin.site.register(Gps)  #Registering Gps Model in Admin Panel
