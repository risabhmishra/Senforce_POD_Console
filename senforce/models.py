######## Risabh Mishra ##########


from django.db import models

# Create your models here.
class Camera(models.Model):
    cam_num = models.IntegerField()
    cam_ip = models.CharField(max_length=35)
    cam_username = models.CharField(max_length=35)
    cam_password = models.CharField(max_length=35)

    def __str__(self):
        return 'Camera'


class Gps(models.Model):
    gps_id = models.IntegerField()
    gps_ip = models.CharField(max_length=15)

    def __str__(self):
        return 'Gps'