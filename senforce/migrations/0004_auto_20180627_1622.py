# Generated by Django 2.0.6 on 2018-06-27 10:52

from django.db import migrations, models
import django.utils.timezone


class Migration(migrations.Migration):

    dependencies = [
        ('senforce', '0003_gps'),
    ]

    operations = [
        migrations.AddField(
            model_name='camera',
            name='cam_password',
            field=models.CharField(default=django.utils.timezone.now, max_length=15),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='camera',
            name='cam_username',
            field=models.CharField(default=django.utils.timezone.now, max_length=15),
            preserve_default=False,
        ),
    ]