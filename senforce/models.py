######## Risabh Mishra ##########


from django.db import models

# Create your models here.
class Camera(models.Model):
    cam_num = models.IntegerField()
    cam_ip = models.CharField(max_length=35)
    cam_port = models.IntegerField()
    cam_username = models.CharField(max_length=35)
    cam_password = models.CharField(max_length=35)
    cam_active = models.BooleanField()

    def __str__(self):
        return 'Camera'


class Gps(models.Model):
    gps_id = models.IntegerField()
    gps_ip = models.CharField(max_length=15)

    def __str__(self):
        return 'Gps'

class User(models.Model):
    user_fname = models.CharField(max_length=10)
    user_lname = models.CharField(max_length=10)
    user_email = models.CharField(max_length=25)
    user_organization = models.CharField(max_length=25)

    def __str__(self):
        return 'User Details'