package org.bdlions.inventory.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author nazmul hasan
 */
public class TimeUtils {
    public long getCurrentTime()
    {
        long currentTime = System.currentTimeMillis() / 1000L;        
        return currentTime;
    }
    
    public String convertUnixToHuman(long unixSeconds, String dateFormat, String reference) {
        if(StringUtils.isNullOrEmpty(dateFormat))
        {
            dateFormat = "yyyy-MM-dd h:mm a";
        }
        Date date = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"+reference)); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
}
