package org.bdlions.inventory.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author nazmul hasan
 */
class PropertyProvider {
    private final Properties properties;
    public PropertyProvider(String fileName) throws IOException {
        properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
	properties.load(inputStream);
    }
    
    protected String get(String key){
        return properties.getProperty(key);
    }
}



