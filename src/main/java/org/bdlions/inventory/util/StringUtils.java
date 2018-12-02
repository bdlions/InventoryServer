package org.bdlions.inventory.util;

/**
 *
 * @author Nazmul Hasan
 */
public class StringUtils 
{
    public static boolean isNullOrEmpty(String str)
    {
        return str == null || str.equals("");
    }
    
    public static String generatePurchaseOrderNo(int number)
    {
        String numberAsString = String.valueOf(number);
        String paddedNumberAsString = ServerConfig.getInstance().get(ServerConfig.PURCHASE_ORDER_TEMPLATE).substring(numberAsString.length()) + numberAsString;      
        return ServerConfig.getInstance().get(ServerConfig.PURCHASE_ORDER_PREFIX) + paddedNumberAsString;
    }
    
    public static String generateSaleOrderNo(int number)
    {
        String numberAsString = String.valueOf(number);
        String paddedNumberAsString = ServerConfig.getInstance().get(ServerConfig.SALE_ORDER_TEMPLATE).substring(numberAsString.length()) + numberAsString;      
        return ServerConfig.getInstance().get(ServerConfig.SALE_ORDER_PREFIX) + paddedNumberAsString;
    }
    
    public static String generateAdjustStockOrderNo(int number)
    {
        String numberAsString = String.valueOf(number);
        String paddedNumberAsString = ServerConfig.getInstance().get(ServerConfig.ADJUST_STOCK_ORDER_TEMPLATE).substring(numberAsString.length()) + numberAsString;      
        return ServerConfig.getInstance().get(ServerConfig.ADJUST_STOCK_ORDER_PREFIX) + paddedNumberAsString;
    }
}
