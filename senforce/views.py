
######## Risabh Mishra ##########

from django.shortcuts import render
from django.http import HttpResponse,JsonResponse
import psutil
from pusher import Pusher
import urllib
from .models import Camera,Gps
from django.shortcuts import render
from django.template import loader
# Create your views here.

def index(request):
    template = loader.get_template('index.html')  #loading the template
    return HttpResponse(template.render()) #rendering the template

def camera(request):
    cam = Camera.objects.all()
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
    #url_fetch = "http://"+str(cam.cam_ip)+"/jpg/1/image.jpg"
    url_fetch_snap="http://"+str(cam.cam_ip)+"/axis-cgi/jpg/image.cgi?resolution=320x240"
    url_fetch_video='http://'+str(cam.cam_ip)+'/axis-cgi/mjpg/video.cgi?resolution=320x240&fps=10'
    #html = '<img src='+url_fetch+' alt="Image Not Found" />'
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
