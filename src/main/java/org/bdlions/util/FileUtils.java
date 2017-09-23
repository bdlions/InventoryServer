package org.bdlions.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author nazmul hasan
 */
public class FileUtils {
    
    /**
     * This method will copy a file
     * @param srcFilePath  source file path
     * @param destFilePath destination file path
     */
    public static void copyFile(String srcFilePath, String destFilePath)
    {
        InputStream is = null;
        OutputStream os = null;
        try
        {
            File srcFile = new File(srcFilePath);
            if(!srcFile.isFile())
            {
                return;
            }
            //creating destination directory if doesn't exist
            File destFile  = new File(destFilePath);
            destFile.getParentFile().mkdirs();            
            //copying file content
            is = new FileInputStream(srcFile);
            os = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
        catch(Exception ex)
        {
        
        }
        finally
        {
            try
            {
                if(is != null)
                {
                    is.close();
                }
                if(os != null)
                {
                    os.close();
                }
            }
            catch(Exception ex2)
            {
            
            }
        }
    }
}
