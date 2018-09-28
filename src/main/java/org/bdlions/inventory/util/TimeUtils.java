package org.bdlions.inventory.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author nazmul hasan
 */
public class TimeUtils {
    /**
     * This method returns current UTC unix time
     * @return long current UTC unix time in seconds
     */
    public static long getCurrentTime()
    {
        long currentTime = System.currentTimeMillis() / 1000L;        
        return currentTime;
    }
    
    public static String getCurrentDate(String dateFormat, String reference)
    {
        long currentTime = System.currentTimeMillis() / 1000L;     
        if(StringUtils.isNullOrEmpty(dateFormat))
        {
            dateFormat = "yyyy-MM-dd";
        }
        if(StringUtils.isNullOrEmpty(reference))
        {
            reference = "+6";
        }
        Date date = new Date(currentTime * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"+reference)); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
    
    public static String convertUnixToHuman(long unixSeconds, String dateFormat, String reference) {
        if(StringUtils.isNullOrEmpty(dateFormat))
        {
            dateFormat = "dd-MM-yyyy h:mm a";
        }
        if(StringUtils.isNullOrEmpty(reference))
        {
            reference = "+6";
        }
        Date date = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"+reference)); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
    
    public static long convertHumanToUnix(String humanDate, String dateFormat, String reference)
    {
        if(StringUtils.isNullOrEmpty(dateFormat))
        {
            dateFormat = "yyyy-MM-dd";
        }
        if(StringUtils.isNullOrEmpty(reference))
        {
            reference = "+6";
        }
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT" + reference));
            long ts = sdf.parse(humanDate).getTime()/1000;
            return ts;
        }
        catch(Exception ex)
        {
        
        }        
        return 0;
    }
}
