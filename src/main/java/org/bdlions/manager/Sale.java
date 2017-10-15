package org.bdlions.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.db.HibernateUtil;
import org.bdlions.dto.DTOProduct;
import org.bdlions.dto.DTOPurchaseOrder;
import org.bdlions.dto.DTOSaleOrder;
import org.bdlions.dto.EntityPOShowRoomProduct;
import org.bdlions.dto.EntityProduct;
import org.bdlions.dto.EntityPurchaseOrder;
import org.bdlions.dto.EntitySaleOrder;
import org.bdlions.dto.EntitySaleOrderProduct;
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
public class Sale {
    private final Logger logger = LoggerFactory.getLogger(Sale.class);
    public boolean addSaleOrderInfo(DTOSaleOrder dtoSaleOrder)
    {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        try {
            tx.begin();
            if (dtoSaleOrder != null && dtoSaleOrder.getEntitySaleOrder() != null) {
                EntitySaleOrder entitySaleOrder = dtoSaleOrder.getEntitySaleOrder();
                session.save(entitySaleOrder);
                List<DTOProduct> products = dtoSaleOrder.getProducts();
                int totalProducts = products.size();
                for(int counter = 0; counter < totalProducts; counter++)
                {
                    DTOProduct dtoProduct = products.get(counter);
                    EntitySaleOrderProduct entitySaleOrderProduct = new EntitySaleOrderProduct();
                    entitySaleOrderProduct.setSaleOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                    entitySaleOrderProduct.setProductId(dtoProduct.getEntityProduct().getId());
                    entitySaleOrderProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());                    
                    session.save(entitySaleOrderProduct);
                    
                    EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                    entityShowRoomStock.setSaleOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                    entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                    entityShowRoomStock.setStockIn(0);
                    entityShowRoomStock.setStockOut(dtoProduct.getQuantity());
                    entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_SALE_OUT);
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
    
    public DTOSaleOrder getSaleOrderInfo(DTOSaleOrder dtoSaleOrder)
    {
        Session session = HibernateUtil.getSession();
        try {
            Query<EntitySaleOrder> query = session.getNamedQuery("getSaleOrderByOrderNo");
            query.setParameter("orderNo", dtoSaleOrder.getEntitySaleOrder().getOrderNo());
            EntitySaleOrder entitySaleOrder =  query.getSingleResult();
            dtoSaleOrder.setEntitySaleOrder(entitySaleOrder);
            
            Query<EntitySaleOrderProduct> queryShowRoomProducts = session.getNamedQuery("getSaleOrderProductsByOrderNo");
            queryShowRoomProducts.setParameter("saleOrderNo", dtoSaleOrder.getEntitySaleOrder().getOrderNo());
            List<EntitySaleOrderProduct> showRoomProducts =  queryShowRoomProducts.getResultList();
            if(showRoomProducts != null)
            {
                for(int counter = 0; counter < showRoomProducts.size(); counter++)
                {
                    EntitySaleOrderProduct entitySaleOrderProduct = showRoomProducts.get(counter);
                    Query<EntityShowRoomStock> queryStockProducts = session.getNamedQuery("getSaleOrderProductByOrderNoAndCategoryId");
                    queryStockProducts.setParameter("saleOrderNo", dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                    queryStockProducts.setParameter("transactionCategoryId", Constants.SS_TRANSACTION_CATEGORY_ID_SALE_OUT);
                    queryStockProducts.setParameter("productId", entitySaleOrderProduct.getProductId());
                    EntityShowRoomStock stockProduct =  queryStockProducts.getSingleResult();
                    
                    Query<EntityProduct> queryEntityEntityProduct = session.getNamedQuery("getProductByProductId");
                    queryEntityEntityProduct.setParameter("productId", stockProduct.getProductId());
                    EntityProduct entityProduct =  queryEntityEntityProduct.getSingleResult();
                    
                    DTOProduct dtoProduct = new DTOProduct();
                    dtoProduct.setQuantity(stockProduct.getStockOut());
                    dtoProduct.setEntityProduct(entityProduct);
                    dtoProduct.getEntityProduct().setUnitPrice(entitySaleOrderProduct.getUnitPrice());
                    
                    dtoSaleOrder.getProducts().add(dtoProduct);
                }
            }
        } finally {
            session.close();
        }
        return dtoSaleOrder;
    }
    
    public List<DTOSaleOrder> getSaleOrders(DTOSaleOrder dtoSaleOrder)
    {
        List<DTOSaleOrder> saleOrders = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try {
            Query<EntitySaleOrder> query = session.getNamedQuery("getAllSaleOrders");
            List<EntitySaleOrder> entitySaleOrders =  query.getResultList();
            for(int counter = 0; counter < entitySaleOrders.size(); counter++)
            {
                EntitySaleOrder entitySaleOrder = entitySaleOrders.get(counter);
                DTOSaleOrder dtoSO = new DTOSaleOrder();
                dtoSO.setEntitySaleOrder(entitySaleOrder);
                saleOrders.add(dtoSO);
            }
        } finally {
            session.close();
        }
        return saleOrders;
    }
}
