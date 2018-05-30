package com.sensennetworks.vehicles.detection.client.utils;

import com.sensennetworks.sndb.dao.PropertiesDAOImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Get the properties based on key
 *
 * @author Sreekanth Reddy B
 * @since 2017-05-29
 */
@Component
public class PropertiesUtils {

    Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    PropertiesDAOImpl propertiesDAO;

    /**
     * Gets given property. Exits application if property not found.
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String propertyValue = propertiesDAO.getValueByKey(key);
        if (propertyValue == null || propertyValue.isEmpty()) {
            logger.error(key + " not set");
            System.exit(1);
        }
        return propertyValue;
    }
}
