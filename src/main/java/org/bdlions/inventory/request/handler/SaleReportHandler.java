package org.bdlions.inventory.request.handler;

import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.dto.DTOProduct;
import org.bdlions.inventory.dto.DTOSaleOrder;
import org.bdlions.inventory.dto.DTOSaleOrderPayment;
import org.bdlions.inventory.dto.ListDTOProduct;
import org.bdlions.inventory.dto.ListSaleOrder;
import org.bdlions.inventory.dto.ListSaleOrderPayment;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.inventory.entity.EntitySaleOrder;
import org.bdlions.inventory.entity.EntitySaleOrderPayment;
import org.bdlions.inventory.entity.EntitySaleOrderProduct;
import org.bdlions.inventory.entity.EntitySaleOrderReturnProduct;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrder;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrderPayment;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrderProduct;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrderReturnProduct;
import org.bdlions.inventory.entity.manager.EntityManagerShowRoomStock;
import org.bdlions.inventory.util.Constants;
import org.bdlions.inventory.util.StringUtils;
import org.bdlions.inventory.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import org.apache.shiro.authc.UnknownAccountException;

/**
 *
 * @author nazmul hasan
 */
public class SaleReportHandler {
    private final Logger logger = LoggerFactory.getLogger(SaleReportHandler.class);
    private final ISessionManager sessionManager;

    public SaleReportHandler(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @ClientRequest(action = ACTION.FETCH_SALE_ORDER_SUMMARY)
    public ClientResponse getSaleOrderSummary(ISession session, IPacket packet) throws Exception 
    {
        ListSaleOrder listSaleOrder = new ListSaleOrder();
        JsonObject jsonObject = new Gson().fromJson(packet.getPacketBody(), JsonObject.class);        
        String startDate = jsonObject.get("startDate").getAsString();
        String endDate = jsonObject.get("endDate").getAsString();
        if(StringUtils.isNullOrEmpty(startDate))
        {
            startDate = TimeUtils.getCurrentDate("", "");
        }
        if(StringUtils.isNullOrEmpty(endDate))
        {
            endDate = TimeUtils.getCurrentDate("", "");
        }
        long startTime = 0;
        long endTime = 0;
        startTime = TimeUtils.convertHumanToUnix(startDate, "", "");
        endTime = TimeUtils.convertHumanToUnix(endDate, "", "") + 86400;
        
        String offsetString = jsonObject.get("offset").getAsString();
        String limitString = jsonObject.get("limit").getAsString();
        String userIdString = jsonObject.get("userId").getAsString();
        int offset = 0;
        int limit = 0;
        int userId = -1;
        try
        {
            offset = Integer.parseInt(offsetString);
            limit = Integer.parseInt(limitString);    
            userId = Integer.parseInt(userIdString); 
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid request to get sale list.");
            return generalResponse;
        }
        
        List<DTOSaleOrder> saleOrders = new ArrayList<>();
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder(packet.getPacketHeader().getAppId());
        List<EntitySaleOrder> entitySaleOrders =  entityManagerSaleOrder.getSaleOrdersDQ(startTime, endTime, userId, offset, limit);
        if(entitySaleOrders != null)
        {
            
            for (int counter = 0; counter < entitySaleOrders.size(); counter++) 
            {
                EntitySaleOrder entitySaleOrder = entitySaleOrders.get(counter);
                DTOSaleOrder dtoSO = new DTOSaleOrder();
                dtoSO.setEntitySaleOrder(entitySaleOrder);
                dtoSO.setOrderDate(TimeUtils.convertUnixToHuman(entitySaleOrder.getCreatedOn(), "", ""));
                saleOrders.add(dtoSO);
            }
        }        
        listSaleOrder.setSaleOrders(saleOrders);
        listSaleOrder.setTotalSaleOrders(entityManagerSaleOrder.getTotalSaleOrdersDQ(startTime, endTime, userId));
        listSaleOrder.setTotalSaleAmount(entityManagerSaleOrder.getTotalSaleAmountDQ(startTime, endTime, userId));
        listSaleOrder.setSuccess(true);
        return listSaleOrder;
    }
    
    @ClientRequest(action = ACTION.FETCH_SALE_ORDER_PAYMENT_SUMMARY)
    public ClientResponse getSaleOrderPaymentSummary(ISession session, IPacket packet) throws Exception 
    {
        ListSaleOrderPayment listSaleOrderPayment = new ListSaleOrderPayment();
        JsonObject jsonObject = new Gson().fromJson(packet.getPacketBody(), JsonObject.class);  
        int customerUserId = jsonObject.get("customerUserId").getAsInt();
        String strPaymentTypeIds = jsonObject.get("paymentTypeIds").getAsString();
        List<Integer> paymentTypeIds = new ArrayList<>();
        if(!StringUtils.isNullOrEmpty(strPaymentTypeIds))
        {
            String[] paymentTypeIdList = strPaymentTypeIds.split(",");
            for(String paymentTypeId: paymentTypeIdList)
            {
                paymentTypeId = paymentTypeId.trim();
                try
                {
                    paymentTypeIds.add(Integer.parseInt(paymentTypeId));
                }
                catch(Exception ex)
                {
                    logger.debug("Invalid paymentTypeIds : " + strPaymentTypeIds);
                    logger.debug("Exception : " + ex.toString());
                    paymentTypeIds = new ArrayList<>();
                    break;
                }
            }
        }
        //handle start time, end time, unix payment start time and unix payment end time later
        int offset = jsonObject.get("offset").getAsInt();
        int limit = jsonObject.get("limit").getAsInt();

        List<DTOSaleOrderPayment> saleOrderPayments = new ArrayList<>();
        EntityManagerSaleOrderPayment entityManagerSaleOrderPayment = new EntityManagerSaleOrderPayment(packet.getPacketHeader().getAppId());
        List<EntitySaleOrderPayment> entitySaleOrderPayments =  entityManagerSaleOrderPayment.getSaleOrderPaymentsDQ(paymentTypeIds, customerUserId, 0, 0, 0, 0, offset, limit);
        if(entitySaleOrderPayments != null)
        {
            
            for (int counter = 0; counter < entitySaleOrderPayments.size(); counter++) 
            {
                EntitySaleOrderPayment entitySaleOrderPayment = entitySaleOrderPayments.get(counter);
                DTOSaleOrderPayment dtoSOP = new DTOSaleOrderPayment();
                dtoSOP.setEntitySaleOrderPayment(entitySaleOrderPayment);
                dtoSOP.setCreatedDate(TimeUtils.convertUnixToHuman(entitySaleOrderPayment.getCreatedOn(), "", ""));
                saleOrderPayments.add(dtoSOP);
            }
        }        
        listSaleOrderPayment.setSaleOrderPayments(saleOrderPayments);
        listSaleOrderPayment.setTotalSaleOrderPayments(entityManagerSaleOrderPayment.getTotalSaleOrderPaymentsDQ(paymentTypeIds, customerUserId, 0, 0, 0, 0));
        listSaleOrderPayment.setTotalPaymentAmount(entityManagerSaleOrderPayment.getTotalPaymentAmountDQ(paymentTypeIds, customerUserId, 0, 0, 0, 0));
        listSaleOrderPayment.setSuccess(true);
        
        return listSaleOrderPayment;
    }
    
    @ClientRequest(action = ACTION.FETCH_SALE_BY_PRODUCT_SUMMARY)
    public ClientResponse getSaleByProductSummary(ISession session, IPacket packet) throws Exception 
    {
        ListDTOProduct listDTOProduct = new ListDTOProduct();
        JsonObject jsonObject = new Gson().fromJson(packet.getPacketBody(), JsonObject.class);        
        String startDate = jsonObject.get("startDate").getAsString();
        String endDate = jsonObject.get("endDate").getAsString();
        if(StringUtils.isNullOrEmpty(startDate))
        {
            startDate = TimeUtils.getCurrentDate("", "");
        }
        if(StringUtils.isNullOrEmpty(endDate))
        {
            endDate = TimeUtils.getCurrentDate("", "");
        }
        long startTime = 0;
        long endTime = 0;
        startTime = TimeUtils.convertHumanToUnix(startDate, "", "");
        endTime = TimeUtils.convertHumanToUnix(endDate, "", "") + 86400;
        
        String offsetString = jsonObject.get("offset").getAsString();
        String limitString = jsonObject.get("limit").getAsString();
        String productIdString = jsonObject.get("productId").getAsString();
        int offset = 0;
        int limit = 0;
        int productId = 0;
        try
        {
            offset = Integer.parseInt(offsetString);
            limit = Integer.parseInt(limitString);    
            productId = Integer.parseInt(productIdString); 
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid request to get sale order product list.");
            return generalResponse;
        }
        
        List<Integer> transactionCategoryIds = new ArrayList<>();
        transactionCategoryIds.add(Constants.SS_TRANSACTION_CATEGORY_ID_SALE_ORDER_FULFILLED);
        transactionCategoryIds.add(Constants.SS_TRANSACTION_CATEGORY_ID_SALE_ORDER_RESTOCK);
        EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock(packet.getPacketHeader().getAppId());
        List<EntityShowRoomStock> entityShowRoomStockList = entityManagerShowRoomStock.getShowRoomStockProductByTransactionCategoryIdsInTimeRange(productId, transactionCategoryIds, startTime, endTime, offset, limit);
        EntityManagerSaleOrderProduct entityManagerSaleOrderProduct = new EntityManagerSaleOrderProduct(packet.getPacketHeader().getAppId());
        EntityManagerSaleOrderReturnProduct entityManagerSaleOrderReturnProduct = new EntityManagerSaleOrderReturnProduct(packet.getPacketHeader().getAppId());
        for(EntityShowRoomStock entityShowRoomStock : entityShowRoomStockList)
        {
            DTOProduct dtoProduct = new DTOProduct();
            EntityProduct entityProduct = new EntityProduct();
            entityProduct.setName(entityShowRoomStock.getProductName());
            if(entityShowRoomStock.getTransactionCategoryId() == Constants.SS_TRANSACTION_CATEGORY_ID_SALE_ORDER_FULFILLED)
            {
                EntitySaleOrderProduct entitySaleOrderProduct = entityManagerSaleOrderProduct.getSaleOrderProductByProductIdAndSaleOrderNo(entityShowRoomStock.getProductId(), entityShowRoomStock.getOrderNo());
                if(entitySaleOrderProduct != null)
                {
                    entityProduct.setUnitPrice(entitySaleOrderProduct.getUnitPrice());
                    dtoProduct.setDiscount(entitySaleOrderProduct.getDiscount());
                    dtoProduct.setQuantity(entitySaleOrderProduct.getQuantity());
                    dtoProduct.setTotal(entitySaleOrderProduct.getSubtotal());
                }
            }
            else if(entityShowRoomStock.getTransactionCategoryId() == Constants.SS_TRANSACTION_CATEGORY_ID_SALE_ORDER_RESTOCK)
            {
                EntitySaleOrderReturnProduct entitySaleOrderReturnProduct = entityManagerSaleOrderReturnProduct.getSaleOrderReturnProductByProductIdAndSaleOrderNo(entityShowRoomStock.getProductId(), entityShowRoomStock.getOrderNo());
                if(entitySaleOrderReturnProduct != null)
                {
                    entityProduct.setUnitPrice(entitySaleOrderReturnProduct.getUnitPrice());
                    dtoProduct.setDiscount(entitySaleOrderReturnProduct.getDiscount());
                    dtoProduct.setQuantity(-entitySaleOrderReturnProduct.getQuantity());
                    dtoProduct.setTotal(-entitySaleOrderReturnProduct.getSubtotal());
                }
            }
            dtoProduct.setEntityProduct(entityProduct);
            dtoProduct.setDescription(entityShowRoomStock.getTransactionCategoryTitle());
            dtoProduct.setCreatedOn(TimeUtils.convertUnixToHuman(entityShowRoomStock.getCreatedOn(), "", ""));
            listDTOProduct.getProducts().add(dtoProduct);
        }
        listDTOProduct.setTotalProducts(entityManagerShowRoomStock.getTotalShowRoomStockProductByTransactionCategoryIdsInTimeRange(productId, transactionCategoryIds, startTime, endTime));
        listDTOProduct.setTotalAmount(entityManagerSaleOrderProduct.getSubtotalSaleOrderProductByProductIdInTimeRange(productId, startTime, endTime) - entityManagerSaleOrderReturnProduct.getSubtotalSaleOrderReturnProductByProductIdInTimeRange(productId, startTime, endTime));
        
        listDTOProduct.setSuccess(true);
        return listDTOProduct;
    }
}
