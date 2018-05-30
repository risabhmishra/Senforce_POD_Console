SHARE_FILES = dist/sn-vehicles-detection-client.jar

PROPERTIES_FILES = dist/log4j.properties

BIN_FILES = dist/run_vehicles_detection_client

SUPERVISOR_FILES = dist/sensen-vehicles-detection-client.conf

.PHONY: all
all:
	mkdir -p ../sn-jaxb/src
	ant -v -Dlibs.jaxb.classpath=../sn-jars/jaxb/jaxb-xjc.jar -Dlibs.CopyLibs.classpath=../sn-jars/ant/org-netbeans-modules-java-j2seproject-copylibstask.jar -Dj2ee.server.home=/opt/sensen/apache-tomcat-8.0.33 -Djavadoc.additionalparam=-Xdoclint:none


.PHONY: install
install: all $(SHARE_FILES) $(PROPERTIES_FILES) $(BIN_FILES) $(SUPERVISOR_FILES)
	mkdir -p -m 0755 $(DESTDIR)/opt/sensen/bin
	mkdir -p -m 0755 $(DESTDIR)/opt/sensen/share/vehicles-detection-client/lib
	mkdir -p -m 0755 $(DESTDIR)/etc/supervisor/conf.d
	install -m 0755 $(BIN_FILES) $(DESTDIR)/opt/sensen/bin
	install -m 0644 $(SHARE_FILES) $(DESTDIR)/opt/sensen/share/vehicles-detection-client
	install -m 0644 dist/lib/*.jar $(DESTDIR)/opt/sensen/share/vehicles-detection-client/lib
	install -m 0644 $(SUPERVISOR_FILES) $(DESTDIR)/etc/supervisor/conf.d
	install -m 0600 $(PROPERTIES_FILES) $(DESTDIR)/opt/sensen/share/vehicles-detection-client/

.PHONY: clean
clean:
	ant clean
