
######## Risabh Mishra ##########

from django.shortcuts import render
from django.http import HttpResponse
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
    url_fetch = "http://"+str(cam.cam_ip)+"/jpg/1/image.jpg"
    html = '<img src='+url_fetch+' alt="Image Not Found" />'

    #htmls  = "<script>"+' var url = "https://www.google.co.in/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"; '+"window.open(purl,'Image','width=320px,height=240px,resizable=1');}</script>"

    return HttpResponse(html)

def getbattery(request):
    battery = psutil.sensors_battery()
    plugged = battery.power_plugged
    percent = str(battery.percent)

    #Create Pusher Client
    pusher = Pusher(app_id='547621',
             key='f71a3074a9310d38216a',
             secret='890b98c79f1fcfaa36cd',
             cluster='ap2',
            ssl=True)

    #Send the message
    pusher.trigger('battery','show_plugged',{
        'plugged':plugged
    })

    return render(request,'battery.html',{'plugged':plugged,'percent':percent})

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
