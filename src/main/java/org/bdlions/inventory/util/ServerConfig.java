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
    public static final String JASPER_FILE_PATH          = "JASPER_FILE_PATH";
    public static final String COMPANY_NAME              = "COMPANY_NAME";
    public static final String PURCHASE_ORDER_PREFIX     = "PURCHASE_ORDER_PREFIX";
    public static final String PURCHASE_ORDER_TEMPLATE   = "PURCHASE_ORDER_TEMPLATE";
    public static final String SALE_ORDER_PREFIX         = "SALE_ORDER_PREFIX";
    public static final String SALE_ORDER_TEMPLATE       = "SALE_ORDER_TEMPLATE";
    public static final String ADJUST_STOCK_ORDER_PREFIX     = "ADJUST_STOCK_ORDER_PREFIX";
    public static final String ADJUST_STOCK_ORDER_TEMPLATE   = "ADJUST_STOCK_ORDER_TEMPLATE";
    public static final String COMPANY_ADDRESS           = "COMPANY_ADDRESS";
    public static final String COMPANY_CELL              = "COMPANY_CELL";
    public static final String COMPANY_LOGO              = "COMPANY_LOGO";
    
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
