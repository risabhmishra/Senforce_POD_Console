package com.sensennetworks.vehicles.detection.client;

import com.sensennetworks.commons.utils.SnAppUtils;
import java.io.File;
import java.io.IOException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <pre>
 * Checks the database at regular intervals for ANPR events updated since last check.
 * If event doesn't have some (predefined) errors then nothing is done except update event status.
 * But if event has those errors then its frames are sent to ML (Machine Learning) server to check for presence of vehicle.
 * If vehicle is detected then event is updated, else it is marked as rejected.
 * </pre>
 *
 * @author Pranav
 * @since 2017-03-31
 * @version 2.0 2017-07-04 Faram
 */
public class Main {

    public static final String LOCK_FILE_INSTANCE_NAME = System.getProperty("java.io.tmpdir")
            + File.separator + "vehicles-detection-client.lock";

    public static void main(String[] args) throws IOException {
        //Allow only one instance of this program to run
        SnAppUtils.lockAppInstance(LOCK_FILE_INSTANCE_NAME);

        // Start Spring
        final ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");

        //intercept SIGINT (ctrl+c) from OS to allow spring to gracefully shutdown the threads
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Tell Spring to shutdown all its beans
                ((ConfigurableApplicationContext) ac).close();
            }
        });

    }
}
