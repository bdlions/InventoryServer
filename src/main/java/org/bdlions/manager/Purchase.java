package org.bdlions.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.db.HibernateUtil;
import org.bdlions.dto.DTOProduct;
import org.bdlions.dto.DTOPurchaseOrder;
import org.bdlions.dto.EntityPOShowRoomProduct;
import org.bdlions.dto.EntityPurchaseOrder;
import org.bdlions.dto.EntityShowRoomStock;
import org.bdlions.util.Constants;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class Purchase {
    private final Logger logger = LoggerFactory.getLogger(Purchase.class);
    public boolean addPurchaseOrderInfo(DTOPurchaseOrder dtoPurchaseOrder)
    {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        try {
            tx.begin();
            if (dtoPurchaseOrder != null && dtoPurchaseOrder.getEntityPurchaseOrder() != null) {
                EntityPurchaseOrder entityPurchaseOrder = dtoPurchaseOrder.getEntityPurchaseOrder();
                session.save(entityPurchaseOrder);
                List<DTOProduct> products = dtoPurchaseOrder.getProducts();
                int totalProducts = products.size();
                for(int counter = 0; counter < totalProducts; counter++)
                {
                    DTOProduct dtoProduct = products.get(counter);
                    EntityPOShowRoomProduct entityPOShowRoomProduct = new EntityPOShowRoomProduct();
                    entityPOShowRoomProduct.setOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                    entityPOShowRoomProduct.setProductId(dtoProduct.getEntityProduct().getId());
                    entityPOShowRoomProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());                    
                    session.save(entityPOShowRoomProduct);
                    
                    EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                    entityShowRoomStock.setPurchaseOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                    entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                    entityShowRoomStock.setStockIn(dtoProduct.getQuantity());
                    entityShowRoomStock.setStockOut(0);
                    entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                    session.save(entityShowRoomStock);
                }
                tx.commit();
                return true;
            }
        }
        catch(Exception ex){
            logger.error(ex.toString());
            tx.rollback();
        }
        finally {
            session.close();
        }
        return false;
    }
    
    public DTOPurchaseOrder getPurchaseOrderInfo(DTOPurchaseOrder dtoPurchaseOrder)
    {
        Session session = HibernateUtil.getSession();
        try {
            Query<EntityPurchaseOrder> query = session.getNamedQuery("getPurchaseOrderByOrderNo");
            query.setParameter("orderNo", dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
            EntityPurchaseOrder entityPurchaseOrder =  query.getSingleResult();
            dtoPurchaseOrder.setEntityPurchaseOrder(entityPurchaseOrder);
        } finally {
            session.close();
        }
        return dtoPurchaseOrder;
    }
    
    public List<DTOPurchaseOrder> getPurchaseOrders(DTOPurchaseOrder dtoPurchaseOrder)
    {
        List<DTOPurchaseOrder> purchaseOrders = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try {
            Query<EntityPurchaseOrder> query = session.getNamedQuery("getAllPurchaseOrders");
            List<EntityPurchaseOrder> entityPurchaseOrders =  query.getResultList();
            for(int counter = 0; counter < entityPurchaseOrders.size(); counter++)
            {
                EntityPurchaseOrder entityPurchaseOrder = entityPurchaseOrders.get(counter);
                DTOPurchaseOrder dtoPO = new DTOPurchaseOrder();
                dtoPO.setEntityPurchaseOrder(entityPurchaseOrder);
                purchaseOrders.add(dtoPO);
            }
        } finally {
            session.close();
        }
        return purchaseOrders;
    }
}
