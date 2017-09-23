package org.bdlions.email;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author nazmul hasan
 */
public class MailUtil {
    //Method to replace the values for keys

    public static String readEmailFromHtml(String filePath, Map<String, String> input) {
        String msg = readContentFromFile(filePath);
        try {
            Set<Map.Entry<String, String>> entries = input.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                msg = msg.replace("$[" + entry.getKey().trim() + "]", entry.getValue().trim());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return msg;
    }

//Method to read HTML file as a String 
    private static String readContentFromFile(String fileName) {
        StringBuffer contents = new StringBuffer();

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();;
            InputStream input = classLoader.getResourceAsStream(fileName);
            //use buffering, reading one line at a time
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } finally {
                reader.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return contents.toString();
    }
}
