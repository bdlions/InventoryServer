package org.bdlions.inventory.util;

import java.io.IOException;

/**
 *
 * @author nazmul hasan
 */
public class ServerConfig extends PropertyProvider{
    private static ServerConfig instance;
    public static final String SERVER_BASE_ABS_PATH      = "SERVER_BASE_ABS_PATH";
    public static final String REPORT_PATH               = "REPORT_PATH";
    public static final String COMPANY_NAME              = "COMPANY_NAME";
    public static final String COMPANY_ADDRESS           = "COMPANY_ADDRESS";
    public static final String COMPANY_CELL              = "COMPANY_CELL";
    
    private ServerConfig(String fileName) throws IOException {
        super(fileName);
    }
    
    public static ServerConfig getInstance(){
        try{
            if(instance == null){
                instance = new ServerConfig("server.properties");
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        return instance;
    }
    
    @Override
    public String get(String key){
        return super.get(key);
    }
}
