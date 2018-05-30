package com.sensennetworks.vehicles.detection.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.sensennetworks.commons.dto.Notification;
import com.sensennetworks.commons.io.api.Sender;
import com.sensennetworks.commons.io.jms.core.JMSDestinationType;
import com.sensennetworks.commons.utils.SnEventsStatusesUtils;
import com.sensennetworks.commons.utils.SnPropsUtils;
import com.sensennetworks.sndb.dao.EventsDAOImpl;
import com.sensennetworks.sndb.dao.EventsErrorsDAOImpl;
import com.sensennetworks.sndb.dao.EventsFramesDAOImpl;
import com.sensennetworks.sndb.dao.EventsFramesPropertiesDAOImpl;
import com.sensennetworks.sndb.dao.EventsFramesPropertiesTypesDAOImpl;
import com.sensennetworks.sndb.dao.EventsFramesTypesDAOImpl;
import com.sensennetworks.sndb.dao.EventsFusionsDAOImpl;
import com.sensennetworks.sndb.dao.EventsPropertiesDAOImpl;
import com.sensennetworks.sndb.dao.EventsSamplesDAOImpl;
import com.sensennetworks.sndb.dao.EventsSamplesTypesDAOImpl;
import com.sensennetworks.sndb.dao.EventsStatusesDAOImpl;
import com.sensennetworks.sndb.dao.PropertiesDAOImpl;
import com.sensennetworks.sndb.dao.VariablesDAOImpl;
import com.sensennetworks.sndb.pojo.SnEvents;
import com.sensennetworks.sndb.pojo.SnEventsErrors;
import com.sensennetworks.sndb.pojo.SnEventsFrames;
import com.sensennetworks.sndb.pojo.SnEventsFramesTypes;
import com.sensennetworks.sndb.pojo.SnEventsFusions;
import com.sensennetworks.sndb.pojo.SnEventsProperties;
import com.sensennetworks.sndb.pojo.SnEventsSamples;
import com.sensennetworks.sndb.pojo.SnEventsSamplesTypes;
import com.sensennetworks.sndb.pojo.SnEventsStatuses;
import com.sensennetworks.sndb.pojo.SnVariables;
import com.sensennetworks.utils.SsnUtilities;
import com.sensennetworks.vehicles.detection.client.constants.ServerResponseCode;
import com.sensennetworks.vehicles.detection.client.pojo.ANPR;
import com.sensennetworks.vehicles.detection.client.pojo.Classification;
import com.sensennetworks.vehicles.detection.client.pojo.ClassificationPacket;
import com.sensennetworks.vehicles.detection.client.pojo.EventFrames;
import com.sensennetworks.vehicles.detection.client.pojo.EventFramesPacket;
import com.sensennetworks.vehicles.detection.client.pojo.Headers;
import com.sensennetworks.vehicles.detection.client.pojo.Metadata;
import com.sensennetworks.vehicles.detection.client.pojo.RanksByPattern;
import com.sensennetworks.vehicles.detection.client.pojo.RequestModeFlags;
import com.sensennetworks.vehicles.detection.client.pojo.Response;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class VehiclesHandler implements Runnable {

    public static final int QUERY_LIMIT = 1000;
    public static final int DEFAULT_DB_CHECK_INTERVAL_MS = 1000;
    public static final String DB_CHECK_INTERVAL_MS = "vehicles-detection-client-db-check-interval-ms";
    /**
     * Events with these statuses will be processed. Comma separated list.
     */
    public static final String INPUT_EVENTS_STATUSES_INCLUDE = "vehicles-detection-client-input-events-statuses-include";
    /**
     * Events with these statuses will not be processed. Comma separated list.
     */
    public static final String INPUT_EVENTS_STATUSES_EXCLUDE = "vehicles-detection-client-input-events-statuses-exclude";
    /**
     * Events with these rules will be processed. Comma separated list.
     */
    public static final String INPUT_EVENTS_RULES_INCLUDE = "vehicles-detection-client-input-events-rules-include";
    /**
     * Events with these rules will not be processed. Comma separated list.
     */
    public static final String INPUT_EVENTS_RULES_EXCLUDE = "vehicles-detection-client-input-events-rules-exclude";
    /**
     * Events containing these error types will be sent to ML server for further
     * processing. Comma separated list.
     */
    public static final String INPUT_EVENTS_ERROR_TYPES = "vehicles-detection-client-input-events-error-types";
    /**
     * Needs to process the all events irrespective of events error check.
     */
    public static final String SEND_ALL_EVENTS = "vehicles-detection-client-send-all-events";
    /**
     * Region code for ANPR(Ex : AU, KSZ etc,...)
     */
    public static final String ANPR_REGION_CODE = "vehicles-detection-client-anpr-region-code";

    public static final String REST_URL = "vehicles-detection-client-rest-url";
    public static final String CONNECTION_TIMEOUT = "vehicles-detection-client-response-timeout-msec";
    public static final String JSON_KEY = "vehicles-detection-client-json-key";
    public static final String TRUE_CAR = "vehicles-detection-client-true-car";
    public static final String TRUE_PLATE = "vehicles-detection-client-true-plate";
    public static final String FALSE_CAR = "vehicles-detection-client-false-car";
    public static final String FALSE_PLATE = "vehicles-detection-client-false-plate";
    public static final String MLSERVERRESPONSE = "vehicle-detection-server-response";
    public static final String VEHICLE_PRESENCE = "vehicle-present-in-frame";
    public static final String ML_RESPONSE_EVENT_PROP = "ml-server-response-json";
    public static final String JSON_FIELD_KEY = "key";
    public static final String JSON_FIELD_IMG = "img";
    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String EVENT_STATUS_PASSED = "passed";
    final String LINKED_EVENT = "linked-event";
    final String NOTIFICATION_DESTINATION_NAME = "sn.topic.events.notif";
    public static final String VEHICLE_PRESENT_EVENT_STATUS = "vehicles-detection-client-vehicle-present-event-status";
    public static final String REJECTED_EVENT_STATUS = "vehicles-detection-client-rejected-event-status";
    public static final String DB_CHECK_TIMESTAMP = "vehicles-detection-client-db-check-timestamp";
    public static final String RESULT_STATUS = "result-status";
    public static final String FUSION_RANK = "fusion-rank";
    public static final String VOTING_RANK = "voting-rank";
    public static final String TRACK_RESULTS = "track-results";
    public static final String RANK_BY_PATTERN = "ranks-by-pattern";
    public static final String RANK_BY_SCORE = "ranks-by-score";
    public static final String OCR_SCORE_MAX = "ocr-score-max";
    public static final String OCR_SCORE_AVG = "ocr-score-average";
    public static final String OCR_SCORE_MIN = "ocr-score-min";
    public static final String TRACK_LENGTH = "track-length";
    public static final String MANUAL_OCR_VALUE = "MANUAL";
    
    // for request mode flags           
    public static final String IS_ANPR = "isANPR";
    public static final String IS_CLASSIFICATION = "isClassification";
    
    /**
     * Event frame type(plate)
     */
    public static final String PLATE_EVENT_FRAME_TYPE = "plate";

    Logger logger = Logger.getLogger(getClass());
    @Autowired
    PropertiesDAOImpl propsDao;
    @Autowired
    EventsPropertiesDAOImpl eventsPropertiesDao;
    @Autowired
    EventsStatusesDAOImpl eventStatusDao;
    @Autowired
    EventsDAOImpl eventsDao;
    @Autowired
    Sender<Notification> sender;
    @Autowired
    EventsFramesPropertiesTypesDAOImpl eventFramesPropsTypesDao;
    @Autowired
    EventsFramesPropertiesDAOImpl eventFramesPropsDao;
    @Autowired
    VariablesDAOImpl varsDao;
    @Autowired
    EventsErrorsDAOImpl eventsErrorsDao;
    @Autowired
    EventsSamplesTypesDAOImpl eventsSamplesTypesDao;
    @Autowired
    EventsSamplesDAOImpl eventsSamplesDao;
    @Autowired
    EventsFusionsDAOImpl eventsFusionsDao;
    @Autowired
    EventsFramesTypesDAOImpl eventsFramesTypesDao;
    @Autowired
    EventsFramesDAOImpl eventsFramesDao;

    Integer dbCheckIntervalMS;
    List<String> inputEventsStatusesInclude;
    List<String> inputEventsStatusesExclude;
    List<String> inputEventsRulesInclude;
    List<String> inputEventsRulesExclude;
    List<String> inputEventsErrorTypes;
    String restUrl;
    String jsonKey;
    String trueCar;
    String truePlate;
    String falseCar;
    String falsePlate;
    String regionCode;
    String dbFilesPath;
    Boolean eventsErrorsCheck;
    Integer connectionTimeout;
    Thread vehiclesHandler;
    Base64.Encoder base64Encoder = Base64.getEncoder();
    Base64.Decoder base64Decoder = Base64.getDecoder();
    Gson gson;
    SnVariables dbCheckTimestamp;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
    SnEventsStatuses vehiclePresentEventStatus;
    SnEventsStatuses rejectedEventStatus;
    
    // for reqquestModeFlags in request message
    boolean isANPR;
    boolean isClassification;

    /**
     * On Spring startup, initialize user defined values, and start thread.
     *
     * @throws java.io.IOException
     * @throws javax.jms.JMSException
     */
    @PostConstruct
    public void init() throws IOException, JMSException {

        gson = new GsonBuilder().disableHtmlEscaping().create();

        String val = propsDao.getValueByKey(DB_CHECK_INTERVAL_MS);
        dbCheckIntervalMS = (val != null && !val.isEmpty()) ? Integer.valueOf(val) : DEFAULT_DB_CHECK_INTERVAL_MS;

        // These properties are optional.
        inputEventsStatusesInclude = SnPropsUtils.getValueAsList(INPUT_EVENTS_STATUSES_INCLUDE, false, propsDao);
        inputEventsStatusesExclude = SnPropsUtils.getValueAsList(INPUT_EVENTS_STATUSES_EXCLUDE, false, propsDao);
        inputEventsRulesInclude = SnPropsUtils.getValueAsList(INPUT_EVENTS_RULES_INCLUDE, false, propsDao);
        inputEventsRulesExclude = SnPropsUtils.getValueAsList(INPUT_EVENTS_RULES_EXCLUDE, false, propsDao);
        inputEventsErrorTypes = SnPropsUtils.getValueAsList(INPUT_EVENTS_ERROR_TYPES, false, propsDao);

        // These props are compulsory
        vehiclePresentEventStatus = SnEventsStatusesUtils.getEventStatusByPropertyKey(VEHICLE_PRESENT_EVENT_STATUS, true, eventStatusDao, propsDao);
        rejectedEventStatus = SnEventsStatusesUtils.getEventStatusByPropertyKey(REJECTED_EVENT_STATUS, true, eventStatusDao, propsDao);
        eventsErrorsCheck = Boolean.valueOf(SnPropsUtils.getValue(SEND_ALL_EVENTS, true, propsDao));
        regionCode = SnPropsUtils.getValue(ANPR_REGION_CODE, true, propsDao);

        val = propsDao.getValueByKey(REST_URL);
        if (val != null && !val.isEmpty()) {
            restUrl = val;
        } else {
            logger.error(REST_URL + " not set");
            System.exit(1);
        }

        val = propsDao.getValueByKey(JSON_KEY);
        if (val != null && !val.isEmpty()) {
            jsonKey = val;
        } else {
            logger.error(JSON_KEY + " not set");
            System.exit(1);
        }

        val = propsDao.getValueByKey(TRUE_CAR);
        if (val != null && !val.isEmpty()) {
            trueCar = val;
        } else {
            logger.error(TRUE_CAR + " not set");
            System.exit(1);
        }

        val = propsDao.getValueByKey(TRUE_PLATE);
        if (val != null && !val.isEmpty()) {
            truePlate = val;
        } else {
            logger.error(TRUE_PLATE + " not set");
            System.exit(1);
        }

        val = propsDao.getValueByKey(FALSE_CAR);
        if (val != null && !val.isEmpty()) {
            falseCar = val;
        } else {
            logger.error(FALSE_CAR + " not set");
            System.exit(1);
        }

        val = propsDao.getValueByKey(FALSE_PLATE);
        if (val != null && !val.isEmpty()) {
            falsePlate = val;
        } else {
            logger.error(FALSE_PLATE + " not set");
            System.exit(1);
        }

        val = propsDao.getValueByKey(CONNECTION_TIMEOUT);
        if (val != null && !val.isEmpty()) {
            connectionTimeout = Integer.valueOf(val);
        } else {
            logger.error(CONNECTION_TIMEOUT + " not set");
            System.exit(1);
        }
        
        // for requestModeFlags 
        isANPR = propsDao.getValueByKey(IS_ANPR);
        isClassification = propsDao.getValueByKey(IS_CLASSIFICATION);

        // Set db check timestamp to Unix epoch, if it doesn't exist.
        dbCheckTimestamp = varsDao.addIfNonExistent(DB_CHECK_TIMESTAMP, dateFormat.format(new Date(0L)));

        sender.init(NOTIFICATION_DESTINATION_NAME, JMSDestinationType.TOPIC, DeliveryMode.NON_PERSISTENT);

        vehiclesHandler = new Thread(this, getClass().getSimpleName());
        vehiclesHandler.start();
    }

    /**
     * Checks db at regular intervals for events to process and sends
     * notification.
     */
    @Override
    public void run() {
        try {
            logger.info("Started VehiclesHandler");

            while (!Thread.interrupted()) {
                try {
                    dbCheckTimestamp = varsDao.getByKey(DB_CHECK_TIMESTAMP);
                    Date from = dateFormat.parse(dbCheckTimestamp.getValue());
                    Date to = new Date();
                    int offset = 0;
                    int eventsCount = 0;

                    // Get events in groups of events up to query limit.
                    do {
                        List<SnEvents> events = eventsDao.getByStatusesUpdatedRules(inputEventsStatusesInclude, inputEventsStatusesExclude,
                                from, to, inputEventsRulesInclude, inputEventsRulesExclude, Order.asc("updated"), QUERY_LIMIT, offset);

                        eventsCount = events.size();
                        logger.info("Events to process count: " + eventsCount);

                        for (SnEvents event : events) {
                            processEvent(event);
                            sender.send(new Notification(Notification.Type.EVENT, event.getPkEvent(), Notification.Action.UPDATED));
                            logger.info("Notified pkEvent : " + event.getPkEvent() + " action : updated");
                        }

                        offset += eventsCount;
                    } while (eventsCount == QUERY_LIMIT);

                    varsDao.setValueByKey(DB_CHECK_TIMESTAMP, dateFormat.format(to));

                    Thread.sleep(dbCheckIntervalMS);
                } catch (InterruptedException iex) {
                    throw iex;
                } catch (Exception ex) {
                    logger.error(SsnUtilities.getStackTrace(ex));
                }
            }
        } catch (InterruptedException ex) {
            logger.error("Interrupted thread: " + vehiclesHandler.getName());
        }
    }

    /**
     * Process event and update it based on ML server response.
     *
     * @param event
     */
    void processEvent(SnEvents event) {
        try {
            logger.info("Processing pkEvent: " + event.getPkEvent());

            if (!isServerProcessingRequired(event)) {
                setStatusAndUpdateEvent(event, vehiclePresentEventStatus);
                return;
            }

            List<String> eventFrames = getBase64FramesForEvent(event);
            // If event doesn't have any frames, mark it as rejected.
            if (eventFrames.isEmpty()) {
                setStatusAndUpdateEvent(event, rejectedEventStatus);
                return;
            }

            // Send frames to ML server and process response.
            detectVehicle(event, eventFrames);

        } catch (JsonParseException jpe) {
            /**
             * ML server JSON response :
             * {"ClassificationPacket":{"responseCode": "200", "classification":
             * {"car": "false_car", "plateDetectionUIDs": [], "metadata": "",
             * "numberPlate": "false_plates", "plateImg": ""},
             * "responseMessage": ""}}
             *
             * If the response message is getting above format or giving any
             * exception related to above format we are making the event status
             * as rejected. It means the response packet is not having the
             * correct data related to event.
             */

            // Set event status = rejected.
            setStatusAndUpdateEvent(event, rejectedEventStatus);
            logger.info(event.getPkEvent() + " event is rejected because of " + jpe.getMessage());
            //logger.error(SsnUtilities.getStackTrace(jpe));
        } catch (Exception ex) {
            logger.error(SsnUtilities.getStackTrace(ex));
        }
    }

    /**
     * If event contains error records of given types then further processing by
     * ML server is required so return true, else false.
     *
     * @param event
     * @return
     */
    boolean isServerProcessingRequired(SnEvents event) {

        /**
         * Overriding/bypassing the events errors checking to process all
         * events,Even events does or does not contain error records.
         */
        if (eventsErrorsCheck) {
            return true;
        }

        if (inputEventsErrorTypes == null) {
            // No checks required.
            logger.info("pkEvent: " + event.getPkEvent() + ". No error types defined so no further processing by ML server will be done");
            return false;
        }

        List<SnEventsErrors> eventsErrors = eventsErrorsDao.getByEventAndErrorTypes(event, inputEventsErrorTypes);
        if (eventsErrors.isEmpty()) {
            // Event does not contain error records of given types, so no further processing required.
            logger.info("pkEvent: " + event.getPkEvent() + " doesn't contain error records of given types so no further processing by ML server will be done");
            return false;
        } else {
            logger.info("pkEvent: " + event.getPkEvent() + " contains error records of given types so further processing by ML server will be done");
            return true;
        }
    }

    /**
     * Send event images to ML server for processing and update event attributes
     * based on server response.
     *
     * @param event
     * @param eventFrames Base64 encoded frame of given event.
     */
    void detectVehicle(SnEvents event, List<String> eventFrames) throws IOException {

        String responseJSON = null;
        Response response = null;

        // Post event frames to server.
        // If server responds with 'Internal Server Error' then keep re-sending the request
        // until server responds with some other response.
        do {
            responseJSON = performPostCall(restUrl, eventFrames);
            response = gson.fromJson(responseJSON, Response.class);
        } while (response.getClassificationPacket().getResponseCode() == ServerResponseCode.INTERNAL_SERVER_ERROR.getVal());

        onServerResponse(event, responseJSON, response);
    }

    /**
     * Gets the base64 representation of the image
     *
     * @param eventFrames
     * @return
     */
    String getBase64File(SnEventsFrames eventFrames) {
        try {
            String filePath = eventFrames.getUrl();
            File imageFile = new File(filePath);
            byte[] fileContents = FileUtils.readFileToByteArray(imageFile);
            String b64 = base64Encoder.encodeToString(fileContents);
            logger.info("Converting image to base 64 format.");
            //This will be using the other method to store the plate image
            dbFilesPath = imageFile.getParent();
            return b64;
        } catch (IOException ioe) {
            logger.error(SsnUtilities.getStackTrace(ioe));
            return null;
        }
    }

    /**
     * Adds an event property to the db
     *
     * @param event
     * @param key
     * @param data
     */
    public void insertEventProperty(SnEvents event, String key, String data) {
        SnEventsProperties eventProp = eventsPropertiesDao.getByEventAndKey(event, key);
        if (eventProp != null) {
            //DO NOT DELETE THIS BLOCK
            //No operation to perform
            //We are not overrding the event properties if already exist.
            /**
             * eventProp.setValue(data); eventProp =
             * eventsPropertiesDao.update(eventProp); logger.info("Updated
             * pkEventProp: " + eventProp.getPkEventProperty() + " key: " +
             * eventProp.getKey() + " val: " + eventProp.getValue());
             *
             */

            logger.info("Not Updated pkEventProp: " + eventProp.getPkEventProperty() + " key: " + eventProp.getKey() + " val: " + eventProp.getValue());
        } else {
            eventProp = new SnEventsProperties();
            eventProp.setSnEvents(event);
            eventProp.setKey(key);
            eventProp.setValue(data);
            eventProp = eventsPropertiesDao.add(eventProp);
            logger.info("Added pkEventProp: " + eventProp.getPkEventProperty() + " key: " + eventProp.getKey() + " val: " + eventProp.getValue());
        }
    }

    /**
     * Performs a post call and returns the response
     *
     * @param requestURL : URL to which connection is to be made
     * @param eventFrameList :Base64 Image data List
     * @return
     * @throws IOException
     */
    public String performPostCall(String requestURL, List<String> eventFrameList) throws IOException {

        URL url;
        String response = "";

        url = new URL(requestURL);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        logger.info("Opening the HttpURLConnection");
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(connectionTimeout);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(eventFrameList));
        logger.info("Sending data to the server.");
        writer.flush();
        writer.close();
        os.close();
        int responseCode = conn.getResponseCode();
        logger.info("getting response from the server");

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = br.readLine()) != null) {
                response += line;
            }
            logger.info("Response code is valid.");
        } else {
            response = "Error.";
            logger.info("response code is not valid");
        }

        return response;
    }

    /**
     * Gets the POST data string to append it to the URL
     *
     * @param imgData : Base64 Image Data
     * @return: The POST data String
     * @throws UnsupportedEncodingException
     */
    private String getPostDataString(List<String> eventFrameList) {

        int count = 0;
        List<EventFrames> eventFramesList = new ArrayList<>();
        EventFramesPacket eventFramesPacket = new EventFramesPacket();
        Headers headers = new Headers();
        RequestModeFlags requestModeFlags = new RequestModeFlags();
        
        for (String imageData : eventFrameList) {
            EventFrames eventFrames = new EventFrames();
            eventFrames.setBase64image(imageData);
            eventFrames.setFrameType("source");
            eventFrames.setUid("im_" + Integer.toString(count));
            eventFramesList.add(eventFrames);
            ++count;
        }

        headers.setContentType(JSON_CONTENT_TYPE);
        requestModeFlags.setIsANPR(isANPR);
        requestModeFlags.setIsClassification(isClassification);

        eventFramesPacket.setHeaders(headers);
        eventFramesPacket.setKey(jsonKey);
        eventFramesPacket.setRequestModeFlags(requestModeFlags);
//        eventFramesPacket.setRegionCode(regionCode);
        eventFramesPacket.setEventFrames(eventFramesList);

        //logger.info(gson.toJson(eventFramesPacket));
        return gson.toJson(eventFramesPacket);
    }

    /**
     * Sets given event status. Sets event.updated to current timestamp
     *
     * @param event
     * @param eventStatus
     */
    void setStatusAndUpdateEvent(SnEvents event, SnEventsStatuses eventStatus) {
        event.setSnEventsStatuses(eventStatus);
        logger.info("pkEvent: " + event.getPkEvent() + " set status: " + event.getSnEventsStatuses().getKey());
        event.setUpdated(new Date());
        event = eventsDao.update(event);
        logger.info("Updated pkEvent: " + event.getPkEvent());
    }

    /**
     * Gets list of frames for given event, in base64 format.
     *
     * @param event
     * @return
     */
    List<String> getBase64FramesForEvent(SnEvents event) {
        List<String> base64Images = new ArrayList<>();
        logger.info("Processing pkEvent : " + event.getPkEvent());

        for (SnEventsFrames eventFrame : event.getSnEventsFrameses()) {
            logger.info("Processing eventFrame : " + eventFrame.getPkEventFrame());
            String content = getBase64File(eventFrame);
            if (content != null) {
                base64Images.add(content);
            }
        }

        return base64Images;
    }

    /**
     * Process ML server response. Update event associations and status.
     *
     * @param event
     * @param responseJSON
     */
    void onServerResponse(SnEvents event, String responseJSON, Response response) throws IOException {

        //Classification packet from response
        ClassificationPacket classificationPacket = response.getClassificationPacket();

        //Response code of server
        Integer responseCode = classificationPacket.getResponseCode();

        // If server response contains error code.
        if (responseCode != ServerResponseCode.OK.getVal()) {
            logger.error("Server response code: " + responseCode + " message: " + classificationPacket.getResponseMessage());
            // Add whole server response as event prop.
            insertEventProperty(event, ML_RESPONSE_EVENT_PROP, responseJSON);
            // Set event status = rejected.
            setStatusAndUpdateEvent(event, rejectedEventStatus);
            return;
        }

        // Process the server response.
        Classification classification = classificationPacket.getClassification();
        
        ANPR anpr = classificationPacket.getAnpr();
        
        insertEventProperty(event, ML_RESPONSE_EVENT_PROP, responseJSON);

        // checks for all use cases...
        // if isANPR == true and isClassification = true
        if (anpr!=null && isVehiclePresent(anpr) && classification != null) {
            insertEventProperty(event, RESULT_STATUS, (trueCar + ", " + truePlate));

            //Currently not supporting the below methods because of the response packet is updated in server.
            //Ref TK.NO:CSRO-216.
            //addMetadataProps(event, classification.getMetadata);
            //addMetadataSamples(event, classification.getMetadata());
            //addMetadataFusions(event, classification.getMetadata());
            //As per new response packet it has only meta data as OCR's.
            addMetadataFusions(event, anpr.getMetadata());

            // Set event status = vehicle present.
            setStatusAndUpdateEvent(event, vehiclePresentEventStatus);

            //Create image in local system with given file path.
            createImageFile(dbFilesPath + File.separator + "plate-1.JPEG", anpr.getPlateImg());

            //Adding the event frame to associated event.
            addEventFrames(event, dbFilesPath + File.separator + "plate-1.JPEG");
            
            
            //  todo for classification object
            
        } 
        // if  isANPR = true  and isClassification = false 
        else if(anpr!=null && isVehiclePresent(anpr)){
            insertEventProperty(event, RESULT_STATUS, (trueCar + ", " + truePlate));

            //Currently not supporting the below methods because of the response packet is updated in server.
            //Ref TK.NO:CSRO-216.
            //addMetadataProps(event, classification.getMetadata);
            //addMetadataSamples(event, classification.getMetadata());
            //addMetadataFusions(event, classification.getMetadata());
            //As per new response packet it has only meta data as OCR's.
            addMetadataFusions(event, anpr.getMetadata());

            // Set event status = vehicle present.
            setStatusAndUpdateEvent(event, vehiclePresentEventStatus);

            //Create image in local system with given file path.
            createImageFile(dbFilesPath + File.separator + "plate-1.JPEG", anpr.getPlateImg());

            //Adding the event frame to associated event.
            addEventFrames(event, dbFilesPath + File.separator + "plate-1.JPEG");
            
        }
        // if isANPR = false and isClassification = true
        else if(classification !=null){
            // todo  for classification object
        }
        // if isANPR = false and isClassification = false
        else{
            insertEventProperty(event, RESULT_STATUS, (falseCar + ", " + falsePlate));
            // Set event status = rejected.
            setStatusAndUpdateEvent(event, rejectedEventStatus);
        }
    }

    /**
     * Checks anpr to see if vehicle is present or not.
     *
     * parameter  changed from classification to ANPR   ---- updated
     * @param anpr
     * @return
     */
    boolean isVehiclePresent(ANPR anpr) {
        return anpr.getPlateImg() != null && !anpr.getPlateImg().isEmpty();
    }

    /**
     * Adds given meta data as event props.
     *
     * @param event
     * @param metadata
     */
    void addMetadataProps(SnEvents event, Metadata metadata) {
        insertEventProperty(event, FUSION_RANK, metadata.getFusionRank());
        insertEventProperty(event, VOTING_RANK, metadata.getVotingRank());
        insertEventProperty(event, TRACK_RESULTS, new Gson().toJson(metadata.getTrackResultsList()));
        insertEventProperty(event, RANK_BY_PATTERN, new Gson().toJson(metadata.getRanksByPatternList()));
        insertEventProperty(event, RANK_BY_SCORE, new Gson().toJson(metadata.getRanksByScoresList()));
    }

    /**
     * Adds given meta data as event samples.
     *
     * @param event
     * @param metadata
     */
    void addMetadataSamples(SnEvents event, Metadata metadata) {
        insertEventsSamples(event, OCR_SCORE_MAX, metadata.getOcrScoreMax());
        insertEventsSamples(event, OCR_SCORE_MIN, metadata.getOcrScoreMin());
        insertEventsSamples(event, OCR_SCORE_AVG, metadata.getOcrScoreAverage());
        insertEventsSamples(event, TRACK_LENGTH, metadata.getTrackLength());
    }

    /**
     * Inserts the eventsSample into the db.
     *
     * @param event
     * @param eventSampleTypeKey
     * @param value
     */
    public void insertEventsSamples(SnEvents event, String eventSampleTypeKey, double value) {
        SnEventsSamplesTypes eventSampleType = eventsSamplesTypesDao.getByKey(eventSampleTypeKey);
        if (eventSampleType == null) {
            throw new RuntimeException("Sample property type " + eventSampleTypeKey + " is not present");
        }

        SnEventsSamples eventSample = eventsSamplesDao.getEventSamplesByEventAndEventSampleType(event, eventSampleType);
        if (eventSample != null) {
            //DO NOT DELETE THIS BLOCK
            //No operation to perform
            //We are not overriding the event samples if already exist.
            /**
             * eventSample.setValue((float) value); eventSample =
             * eventsSamplesDao.add(eventSample); logger.info("Updated
             * pkEventSample: " + eventSample.getPkEventSample() + " sample
             * type: " + eventSampleTypeKey + " value: " + value);
             *
             */
            logger.info("Not Updated pkEventSample: " + eventSample.getPkEventSample() + " sample type: " + eventSampleTypeKey + " value: " + value);
        } else {
            eventSample = new SnEventsSamples();
            eventSample.setSnEvents(event);
            eventSample.setSnEventsSamplesTypes(eventSampleType);
            eventSample.setValue((float) value);
            eventSample = eventsSamplesDao.add(eventSample);
            logger.info("Added pkEventSample: " + eventSample.getPkEventSample() + " sample type: " + eventSampleTypeKey + " value: " + value);
        }
    }

    /**
     * Adds given meta data as event fusions.
     *
     * @param event
     * @param metadata
     */
    void addMetadataFusions(SnEvents event, Metadata metadata) {
        List<RanksByPattern> ranksByPatternsList = metadata.getRanksByPatternList();

        if (!ranksByPatternsList.isEmpty()) {
            //Incrementing and Inserting the rank based on the order of OCR getting from the ML server.
            int rank = 1;
            for (RanksByPattern ranksByPattern : ranksByPatternsList) {
                /**
                 * Date : 15-08-2017 Backend sending rank #1 OCR as "Vehicle
                 * Detection" When ML server gives the result as
                 * rank1,rank2,rank3 etc We are overriding the OCR with
                 * rank1,rank2,rank3 etc.
                 */
                SnEventsFusions eventsFusions = eventsFusionsDao.getByEventAndRank(event, rank);
                if (eventsFusions != null) {
                    eventsFusions.setOcr(ranksByPattern.getOcr());
                    eventsFusions = eventsFusionsDao.update(eventsFusions);
                    logger.info("updated pkEventFusion: " + eventsFusions.getPkEventFusion() + " rank: " + rank + " ocr: " + ranksByPattern.getOcr());
                    rank++;
                } else {
                    SnEventsFusions fusion = new SnEventsFusions();
                    fusion.setSnEvents(event);
                    fusion.setRank(rank);
                    fusion.setOcr(ranksByPattern.getOcr());
                    fusion = eventsFusionsDao.update(fusion);
                    logger.info("Added pkEventFusion: " + fusion.getPkEventFusion() + " rank: " + rank + " ocr: " + ranksByPattern.getOcr());
                    rank++;
                }
            }
        } else {
            //DO NOT DELETE THIS BLOCK
            //Not performing any operations in else block.

            /**
             * Date : 15-08-2017 Here we are not inserting the manual OCR if the
             * ML server not giving any event fusions. Because already the
             * backend giving the OCR with rank 1 with OCR string as "Vehicle
             * Detection".
             */
            /**
             * //adding the manual event if OCR not present SnEventsFusions
             * fusion = new SnEventsFusions(); fusion.setSnEvents(event);
             * fusion.setOcr(MANUAL_OCR_VALUE); fusion.setRank(0); fusion =
             * eventsFusionsDao.update(fusion); logger.info("Added
             * pkEventFusion: " + fusion.getPkEventFusion() + " rank: " + 0 + "
             * ocr: " + MANUAL_OCR_VALUE);
             */
        }
    }

    /**
     * Adding the event frames for corresponding event
     *
     * @param event
     * @param imagePath
     */
    public void addEventFrames(SnEvents event, String imagePath) {

        SnEventsFramesTypes framesTypes = eventsFramesTypesDao.getByKey(PLATE_EVENT_FRAME_TYPE);
        if (framesTypes == null) {
            throw new RuntimeException("Event frame type " + PLATE_EVENT_FRAME_TYPE + " is not present");
        }

        SnEventsFrames eventsFrames = eventsFramesDao.getEventsFramesByEventAndFrameType(event.getPkEvent(), PLATE_EVENT_FRAME_TYPE);
        if (eventsFrames != null) {
            /**
             * we are relying on the cropping accuracy of secondary OCR engine
             * and the cropped plate image already there we are overriding plate
             * image, If not we are adding new event frame in else block.
             */
            eventsFrames.setUrl(imagePath);
            eventsFrames = eventsFramesDao.update(eventsFrames);
            logger.info("Updated pkEventFrame : " + eventsFrames.getPkEventFrame());
        } else {
            eventsFrames = new SnEventsFrames();
            eventsFrames.setSnEvents(event);
            eventsFrames.setSnCameras(event.getSnCameras());
            eventsFrames.setSnEventsFramesTypes(framesTypes);
            eventsFrames.setUrl(imagePath);
            eventsFrames = eventsFramesDao.update(eventsFrames);
            logger.info("Added pkEventFrame : " + eventsFrames.getPkEventFrame());
        }
    }

    /**
     * Creates an image file based on the Base64 Image.
     *
     * @param filePath
     * @param data
     * @throws IOException
     */
    public void createImageFile(String filePath, String data) throws IOException {
        File file = new File(filePath);
        byte[] contents = base64Decoder.decode(data);
        logger.info("Writing to image data to file :  " + filePath);
        FileUtils.writeByteArrayToFile(file, contents);
    }

    /**
     * Adds given meta data as event fusions. Method has been overloaded with
     * list meta data.The meta data is only having OCR's.
     *
     * @param event
     * @param metadata
     */
    void addMetadataFusions(SnEvents event, List<String> metadata) {

        //The list meta data contains the list of OCR's.
        if (!metadata.isEmpty()) {
            //Incrementing and Inserting the rank based on the order of OCR getting from the ML server.
            int rank = 1;
            for (String ocr : metadata) {
                /**
                 * Updated Date : 01-11-2017 Backend sending rank #1 OCR as
                 * "Vehicle Detection" When ML server gives the result as
                 * rank1,rank2,rank3 etc We are overriding the OCR with
                 * rank1,rank2,rank3 etc.
                 */
                SnEventsFusions eventsFusions = eventsFusionsDao.getByEventAndRank(event, rank);
                if (eventsFusions != null) {
                    eventsFusions.setOcr(ocr);
                    eventsFusions = eventsFusionsDao.update(eventsFusions);
                    logger.info("updated pkEventFusion: " + eventsFusions.getPkEventFusion() + " rank: " + rank + " ocr: " + ocr);
                    rank++;
                } else {
                    SnEventsFusions fusion = new SnEventsFusions();
                    fusion.setSnEvents(event);
                    fusion.setRank(rank);
                    fusion.setOcr(ocr);
                    fusion = eventsFusionsDao.update(fusion);
                    logger.info("Added pkEventFusion: " + fusion.getPkEventFusion() + " rank: " + rank + " ocr: " + ocr);
                    rank++;
                }
            }
        } else {
            //DO NOT DELETE THIS BLOCK
            //Not performing any operations in else block.

            /**
             * Updated Date : 01-11-2017 Here we are not inserting the manual
             * OCR if the ML server not giving any event fusions. Because
             * already the backend giving the OCR with rank 1 with OCR string as
             * "Vehicle Detection".
             */
            /**
             * //adding the manual event if OCR not present SnEventsFusions
             * fusion = new SnEventsFusions(); fusion.setSnEvents(event);
             * fusion.setOcr(MANUAL_OCR_VALUE); fusion.setRank(0); fusion =
             * eventsFusionsDao.update(fusion); logger.info("Added
             * pkEventFusion: " + fusion.getPkEventFusion() + " rank: " + 0 + "
             * ocr: " + MANUAL_OCR_VALUE);
             */
        }
    }

    /**
     * On Spring shutdown.
     *
     * @throws java.lang.Exception
     */
    @PreDestroy
    public void destroy() throws Exception {
        if (vehiclesHandler != null && vehiclesHandler.isAlive()) {
            vehiclesHandler.interrupt();
            sender.close();
        }
    }
}
