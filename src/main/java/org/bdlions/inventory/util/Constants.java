package org.bdlions.inventory.util;

/**
 *
 * @author nazmul hasan
 */
public class Constants {
    public static final int ACCOUNT_STATUS_ID_ACTIVE = 1;
    public static final int ACCOUNT_STATUS_ID_INACTIVE = 2;
    
    public static final int ROLE_ID_ADMIN = 1;
    public static final int ROLE_ID_STAFF = 2;
    public static final int ROLE_ID_SUPPLIER = 3;
    public static final int ROLE_ID_CUSTOMER = 4;  
    
    public static final int PURCHASE_ORDER_PAYMENT_TYPE_ID_ADD_PURCHASE_IN = 1;
    public static final int PURCHASE_ORDER_PAYMENT_TYPE_ID_PURCHASE_PAYMENT_OUT = 2;
    public static final int PURCHASE_ORDER_PAYMENT_TYPE_ID_ADD_NEW_PAYMENT_OUT = 3;
    public static final int PURCHASE_ORDER_PAYMENT_TYPE_ID_ADD_PREVIOUS_DUE_IN = 4;
    
    public static final int SALE_ORDER_PAYMENT_TYPE_ID_ADD_SALE_IN = 1;
    public static final int SALE_ORDER_PAYMENT_TYPE_ID_SALE_PAYMENT_OUT = 2;
    public static final int SALE_ORDER_PAYMENT_TYPE_ID_ADD_NEW_PAYMENT_OUT = 3;
    public static final int SALE_ORDER_PAYMENT_TYPE_ID_ADD_PREVIOUS_DUE_IN = 4;
    
    public static final String PURCHASE_ORDER_PAYMENT_TYPE_ADD_PURCHASE_IN_DESCRIPTION = "New Purchase";
    public static final String PURCHASE_ORDER_PAYMENT_TYPE_PURCHASE_PAYMENT_OUT_DESCRIPTION = "Purchase Payment";
    public static final String PURCHASE_ORDER_PAYMENT_TYPE_ADD_NEW_PAYMENT_OUT_DESCRIPTION = "New Payment";
    public static final String PURCHASE_ORDER_PAYMENT_TYPE_ADD_ADD_PREVIOUS_DUE_IN_DESCRIPTION = "Previous Due";
    
    public static final String SALE_ORDER_PAYMENT_TYPE_ADD_SALE_IN_DESCRIPTION = "New Sale";
    public static final String SALE_ORDER_PAYMENT_TYPE_SALE_PAYMENT_OUT_DESCRIPTION = "Sale Payment";
    public static final String SALE_ORDER_PAYMENT_TYPE_ADD_NEW_PAYMENT_OUT_DESCRIPTION = "New Payment";
    public static final String SALE_ORDER_PAYMENT_TYPE_ADD_ADD_PREVIOUS_DUE_IN_DESCRIPTION = "Previous Due";
    
    public static final int SS_TRANSACTION_CATEGORY_ID_PURCASE_ORDER_RECEIVE = 1;
    public static final int SS_TRANSACTION_CATEGORY_ID_PURCASE_ORDER_UNSTOCK = 3;
    public static final int SS_TRANSACTION_CATEGORY_ID_SALE_ORDER_FULFILLED = 5;
    public static final int SS_TRANSACTION_CATEGORY_ID_SALE_ORDER_RESTOCK = 6;
    public static final int SS_TRANSACTION_CATEGORY_ID_STOCK_ADJUSTMENT = 8;
    public static final String SS_TRANSACTION_CATEGORY_TITLE_PURCASE_ORDER_RECEIVE = "Purchase Order Receive";
    public static final String SS_TRANSACTION_CATEGORY_TITLE_PURCASE_ORDER_UNSTOCK = "Purchase Order Unstock";
    public static final String SS_TRANSACTION_CATEGORY_TITLE_SALE_ORDER_FULFILLED = "Sale Order Fulfilled";
    public static final String SS_TRANSACTION_CATEGORY_TITLE_SALE_ORDER_RESTOCK = "Sale Order Restock";
    public static final String SS_TRANSACTION_CATEGORY_TITLE_STOCK_ADJUSTMENT = "Stock Adjustment";
    
    public static final int DEFAULT_ENDING_STOCK_LIMIT = 10;
    
    
    public static final int CURRENCY_UNIT_DEFAULT = 1;    
   
    public static final String SERVER_ROOT_DIR = "../../";
    public static final String IMAGE_UPLOAD_PATH = "uploads/";
    
    public static final int IMG_PROFILE_PIC_WIDTH  = 150;
    public static final int IMG_PROFILE_PIC_HEIGHT = 150;
    public static final String PROFILE_PIC_PATH = "resources/images/profile/";
    public static final String IMG_PROFILE_PIC_PATH_150_150 = "resources/images/profile/150_150";
    
    public static final String USER_DOCUMENT_PATH = "resources/images/profile/document/";
    
    public static final int IMG_PRODUCT_LIST_WIDTH  = 328;
    public static final int IMG_PRODUCT_LIST_HEIGHT = 212;
    public static final int IMG_PRODUCT_INFO_WIDTH  = 656;
    public static final int IMG_PRODUCT_INFO_HEIGHT = 424;
    public static final String PRODUCT_IMAGE_PATH = "resources/images/product/";
    public static final String IMG_PRODUCT_PATH_328_212 = "resources/images/product/328_212";
    public static final String IMG_PRODUCT_PATH_656_424 = "resources/images/product/656_424";
    
}
