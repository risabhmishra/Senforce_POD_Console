
######## Risabh Mishra ##########

from django.shortcuts import render
from django.http import HttpResponse,JsonResponse
import psutil
import urllib,os
from .models import Camera,Gps,User
from django.shortcuts import render
from django.template import loader
from SenForceWeb import settings
from django.core.mail import send_mail
# Create your views here.

def index(request):
    template = loader.get_template('index.html')  #loading the template
    return HttpResponse(template.render()) #rendering the template

def camera(request):
    cam = Camera.objects.all()
    ping()
    return render(request,'camera.html',{'camobjects':cam})

def server(request):
    ###### CPU #################
    percent = psutil.cpu_percent()

    times = str(psutil.cpu_times())
    time = times[10:-1]
    cputime = [i.rstrip().lstrip().rsplit('=') for i in time.split(',')]
    #cputime = [x[1] for x in cputimes]
    '''for x in s:
        print(x[1])
    '''
    serv_stats = str(psutil.cpu_stats())
    stats = serv_stats[10:-1]
    cpustats = [i.rstrip().lstrip().rsplit('=') for i in stats.split(',')]

    ####################  MEMORY  ###########
    svmem = str(psutil.virtual_memory())[7:-1]
    memory = [i.rstrip().lstrip().rsplit('=') for i in svmem.split(',')]

    ################  DISKS  ############
    dusage = str(psutil.disk_usage('/'))[11:-1]
    diskusage = [i.rstrip().lstrip().rsplit('=') for i in dusage.split(',')]

    ######### USERS #########

    users = str(psutil.users())[7:-2]
    user = [i.rstrip().lstrip().rsplit('=') for i in users.split(',')]

    ############ Running Processes #########

    runprocess = [p.name() for p in psutil.process_iter()]

    return render(request,'server.html',{'percent':percent,'cputime':cputime,'cpustats':cpustats,'memory':memory,'diskusage':diskusage,'user':user,'runprocess':runprocess})

def buttonlink(request,button_num):

    cam = Camera.objects.get(cam_num=button_num)

    ############# Use this URL For Axis Cameras ONLY ############s

    url_fetch_snap='http://'+str(cam.cam_username)+':'+str(cam.cam_password)+'@'+str(cam.cam_ip)+"/axis-cgi/jpg/image.cgi"
    url_fetch_video='http://'+str(cam.cam_username)+':'+str(cam.cam_password)+'@'+str(cam.cam_ip)+'/axis-cgi/mjpg/video.cgi?resolution=320x240&fps=10'

    return HttpResponse(url_fetch_snap+'%'+url_fetch_video)

def getbattery(request):
    battery = psutil.sensors_battery()
    plugged = battery.power_plugged
    percent = battery.percent
    secs = battery.secsleft

    return render(request,'battery.html',{'plugged':plugged,'percent':percent,'secs':secs})

def gps(request):
    gpsid = Gps.objects.get(pk=0)
    gps_url = "http://Asterx-U-"+str(gpsid.gps_id)
    try:
        resp = urllib.urlopen(gps_url)
        #resp.read()
    except IOError as e:
        print ("Error: ", e)
    return render(request,'gps.html',{'response':resp.read})

def radar(request):
    return render(request,'radar.html')

def ping():
    import os
    for i in Camera.objects.all():
        hostname = str(i.cam_ip)  # example
        response = os.system("ping -n 1 " + hostname)
    #### IMPORTANT ######
    ###### change -n to -c when using linux or mac server in response field######
    # and then check the response...
        if response == 0:
            i.cam_active=True
            i.save()
        else:
            i.cam_active=False
            i.save()

def mail():
    subject = "SenForce Pod Debugging"
    msg = "Congratulations for your success in Life."
    ##to = User.user_email##
    to = "pranav@sensennetworks.com"
    res = send_mail(subject, msg, settings.EMAIL_HOST_USER, [to])
    if (res == 1):
        msg = "Mail Sent Successfuly"
    else:
        msg = "Mail could not sent"