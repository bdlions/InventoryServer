package org.bdlions.inventory.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.dto.DTOProduct;
import org.bdlions.inventory.dto.DTOSaleOrder;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntitySaleOrder;
import org.bdlions.inventory.entity.EntitySaleOrderProduct;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrder;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrderProduct;
import org.bdlions.inventory.entity.manager.EntityManagerShowRoomStock;
import org.bdlions.inventory.util.Constants;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class Sale 
{

    private final Logger logger = LoggerFactory.getLogger(Sale.class);

    public DTOSaleOrder addSaleOrderInfo(DTOSaleOrder dtoSaleOrder) 
    {
        if (dtoSaleOrder == null || dtoSaleOrder.getEntitySaleOrder() == null) 
        {
            return null;
        }
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction();
        try 
        {       
            tx.begin();                
            EntitySaleOrder entitySaleOrder = dtoSaleOrder.getEntitySaleOrder();
            EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder();
            dtoSaleOrder.setEntitySaleOrder(entityManagerSaleOrder.createSaleOrder(entitySaleOrder, session));

            List<DTOProduct> products = dtoSaleOrder.getProducts();
            int totalProducts = products.size();
            EntityManagerSaleOrderProduct entityManagerSaleOrderProduct = new EntityManagerSaleOrderProduct();
            EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock();
            for (int counter = 0; counter < totalProducts; counter++) {
                DTOProduct dtoProduct = products.get(counter);
                EntitySaleOrderProduct entitySaleOrderProduct = new EntitySaleOrderProduct();
                entitySaleOrderProduct.setSaleOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                entitySaleOrderProduct.setProductId(dtoProduct.getEntityProduct().getId());
                entitySaleOrderProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());   
                entityManagerSaleOrderProduct.addSaleOrderProduct(entitySaleOrderProduct, session);

                EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                entityShowRoomStock.setSaleOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                entityShowRoomStock.setStockIn(0);
                entityShowRoomStock.setStockOut(dtoProduct.getQuantity());
                entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_SALE_OUT);
                entityManagerShowRoomStock.addShowRoomStock(entityShowRoomStock, session);
            }
            tx.commit();
            return dtoSaleOrder;
        } 
        finally {
            session.close();
        }
    }

    public DTOSaleOrder getSaleOrderInfo(DTOSaleOrder dtoSaleOrder) 
    {
        if(dtoSaleOrder == null || dtoSaleOrder.getEntitySaleOrder() == null)
        {
            return null;
        }
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder();
        dtoSaleOrder.setEntitySaleOrder(entityManagerSaleOrder.getSaleOrderByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo()));

        EntityManagerSaleOrderProduct entityManagerSaleOrderProduct = new EntityManagerSaleOrderProduct();
        List<EntitySaleOrderProduct> showRoomProducts = entityManagerSaleOrderProduct.getSaleOrderProductsByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
        if (showRoomProducts != null) {
            EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock();
            for (int counter = 0; counter < showRoomProducts.size(); counter++) {
                EntitySaleOrderProduct entitySaleOrderProduct = showRoomProducts.get(counter);

                EntityShowRoomStock stockProduct = entityManagerShowRoomStock.getSaleOrderProductByOrderNoAndCategoryId(entitySaleOrderProduct.getProductId(), dtoSaleOrder.getEntitySaleOrder().getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_SALE_OUT);

                EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
                EntityProduct entityProduct = entityManagerProduct.getProductByProductId(stockProduct.getProductId());

                DTOProduct dtoProduct = new DTOProduct();
                dtoProduct.setQuantity(stockProduct.getStockOut());
                dtoProduct.setEntityProduct(entityProduct);
                dtoProduct.getEntityProduct().setUnitPrice(entitySaleOrderProduct.getUnitPrice());

                dtoSaleOrder.getProducts().add(dtoProduct);
            }
        }
        return dtoSaleOrder;
    }
    
    public List<DTOSaleOrder> getSaleOrders(DTOSaleOrder dtoSaleOrder) 
    {
        List<DTOSaleOrder> saleOrders = new ArrayList<>();
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder();
        List<EntitySaleOrder> entitySaleOrders =  entityManagerSaleOrder.getSaleOrders(0, 10);
        if(entitySaleOrders != null)
        {
            
            for (int counter = 0; counter < entitySaleOrders.size(); counter++) 
            {
                EntitySaleOrder entitySaleOrder = entitySaleOrders.get(counter);
                DTOSaleOrder dtoSO = new DTOSaleOrder();
                dtoSO.setEntitySaleOrder(entitySaleOrder);
                saleOrders.add(dtoSO);
            }
        }
        return saleOrders;
    }
    
    public EntitySaleOrder getEntitySaleOrderById(int id) 
    {
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder();
        return entityManagerSaleOrder.getEntitySaleOrderById(id);
    }
    
    public EntitySaleOrder getEntitySaleOrderByOrderNo(String orderNo) {
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder();
        return entityManagerSaleOrder.getEntitySaleOrderByOrderNo(orderNo);
    }
    
    public boolean updateSaleOrderInfo(DTOSaleOrder dtoSaleOrder) {
        boolean status = false;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction();
        try {
            if (dtoSaleOrder != null && dtoSaleOrder.getEntitySaleOrder() != null) {
                //getting current entity sale order based on entity sale order id
                EntitySaleOrder currEntitySaleOrder = getEntitySaleOrderById(dtoSaleOrder.getEntitySaleOrder().getId());
                if (currEntitySaleOrder != null && currEntitySaleOrder.getId() > 0) {
                    tx.begin();
                    //updating entity sale order table
                    EntitySaleOrder entitySaleOrder = dtoSaleOrder.getEntitySaleOrder();                    
                    EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder();
                    entityManagerSaleOrder.updateSaleOrder(entitySaleOrder);
                    
                    //we have to follow delete then insert approach and update some tables.
                    //delete entitySaleOrderProduct based on existing order no
                    EntityManagerSaleOrderProduct entityManagerSaleOrderProduct = new EntityManagerSaleOrderProduct();
                    entityManagerSaleOrderProduct.deleteSaleOrderProductsByOrderNo(currEntitySaleOrder.getOrderNo(), session);
                    
                    //delete entityShowRoomStock based on existing order no
                    EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock();
                    entityManagerShowRoomStock.deleteProductsBySaleOrderNoAndTransactionCategoryId(currEntitySaleOrder.getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_SALE_OUT, session);
                    
                    List<DTOProduct> products = dtoSaleOrder.getProducts();
                    int totalProducts = products.size();
                    for (int counter = 0; counter < totalProducts; counter++) {
                        DTOProduct dtoProduct = products.get(counter);
                        EntitySaleOrderProduct entitySaleOrderProduct = new EntitySaleOrderProduct();
                        entitySaleOrderProduct.setSaleOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                        entitySaleOrderProduct.setProductId(dtoProduct.getEntityProduct().getId());
                        entitySaleOrderProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());                        
                        entityManagerSaleOrderProduct.addSaleOrderProduct(entitySaleOrderProduct, session);
                        
                        EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                        entityShowRoomStock.setSaleOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                        entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                        entityShowRoomStock.setStockIn(0);
                        entityShowRoomStock.setStockOut(dtoProduct.getQuantity());
                        entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_SALE_OUT);
                        entityManagerShowRoomStock.addShowRoomStock(entityShowRoomStock, session);
                    }
                    tx.commit();
                    status = true;
                }
            }
        } catch (Exception ex) {
            logger.error(ex.toString());
            tx.rollback();
        } finally {
            session.close();
        }
        return status;
    }
    /*public DTOSaleOrder addSaleOrderInfo(DTOSaleOrder dtoSaleOrder) 
    {
        boolean status = false;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction();
        try 
        {
            if (dtoSaleOrder != null && dtoSaleOrder.getEntitySaleOrder() != null) 
            {
                tx.begin();                
                EntitySaleOrder entitySaleOrder = dtoSaleOrder.getEntitySaleOrder();
                EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder();
                entityManagerSaleOrder.createSaleOrder(entitySaleOrder, session);
                //session.save(entitySaleOrder);
                List<DTOProduct> products = dtoSaleOrder.getProducts();
                int totalProducts = products.size();
                EntityManagerSaleOrderProduct entityManagerSaleOrderProduct = new EntityManagerSaleOrderProduct();
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock();
                for (int counter = 0; counter < totalProducts; counter++) {
                    DTOProduct dtoProduct = products.get(counter);
                    EntitySaleOrderProduct entitySaleOrderProduct = new EntitySaleOrderProduct();
                    entitySaleOrderProduct.setSaleOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                    entitySaleOrderProduct.setProductId(dtoProduct.getEntityProduct().getId());
                    entitySaleOrderProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());   
                    entityManagerSaleOrderProduct.addSaleOrderProduct(entitySaleOrderProduct, session);
                    //session.save(entitySaleOrderProduct);

                    EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                    entityShowRoomStock.setSaleOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                    entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                    entityShowRoomStock.setStockIn(0);
                    entityShowRoomStock.setStockOut(dtoProduct.getQuantity());
                    entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_SALE_OUT);
                    entityManagerShowRoomStock.addShowRoomStock(entityShowRoomStock, session);
                    //session.save(entityShowRoomStock);
                }
                tx.commit();
                status = true;
            }
        } catch (Exception ex) {
            logger.error(ex.toString());
            tx.rollback();
        } finally {
            session.close();
        }
        if(status)
        {
            return dtoSaleOrder;
        }
        else
        {
            return null;
        }
    }*/

    /*public DTOSaleOrder getSaleOrderInfo(DTOSaleOrder dtoSaleOrder) 
    {
        //--------------if order no is null or empty then return from here
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntitySaleOrder> query = session.getNamedQuery("getSaleOrderByOrderNo");
            query.setParameter("orderNo", dtoSaleOrder.getEntitySaleOrder().getOrderNo());
            EntitySaleOrder entitySaleOrder = query.getSingleResult();
            dtoSaleOrder.setEntitySaleOrder(entitySaleOrder);

            Query<EntitySaleOrderProduct> queryShowRoomProducts = session.getNamedQuery("getSaleOrderProductsByOrderNo");
            queryShowRoomProducts.setParameter("saleOrderNo", dtoSaleOrder.getEntitySaleOrder().getOrderNo());
            List<EntitySaleOrderProduct> showRoomProducts = queryShowRoomProducts.getResultList();
            if (showRoomProducts != null) {
                for (int counter = 0; counter < showRoomProducts.size(); counter++) {
                    EntitySaleOrderProduct entitySaleOrderProduct = showRoomProducts.get(counter);
                    Query<EntityShowRoomStock> queryStockProducts = session.getNamedQuery("getSaleOrderProductByOrderNoAndCategoryId");
                    queryStockProducts.setParameter("saleOrderNo", dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                    queryStockProducts.setParameter("transactionCategoryId", Constants.SS_TRANSACTION_CATEGORY_ID_SALE_OUT);
                    queryStockProducts.setParameter("productId", entitySaleOrderProduct.getProductId());
                    EntityShowRoomStock stockProduct = queryStockProducts.getSingleResult();

                    Query<EntityProduct> queryEntityEntityProduct = session.getNamedQuery("getProductByProductId");
                    queryEntityEntityProduct.setParameter("productId", stockProduct.getProductId());
                    EntityProduct entityProduct = queryEntityEntityProduct.getSingleResult();

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
    }*/

    /*public List<DTOSaleOrder> getSaleOrders(DTOSaleOrder dtoSaleOrder) {
        List<DTOSaleOrder> saleOrders = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try {
            Query<EntitySaleOrder> query = session.getNamedQuery("getAllSaleOrders");
            List<EntitySaleOrder> entitySaleOrders = query.getResultList();
            for (int counter = 0; counter < entitySaleOrders.size(); counter++) {
                EntitySaleOrder entitySaleOrder = entitySaleOrders.get(counter);
                DTOSaleOrder dtoSO = new DTOSaleOrder();
                dtoSO.setEntitySaleOrder(entitySaleOrder);
                saleOrders.add(dtoSO);
            }
        } finally {
            session.close();
        }
        return saleOrders;
    }*/

    /*public EntitySaleOrder getEntitySaleOrder(EntitySaleOrder entitySaleOrder) {
        EntitySaleOrder resultEntitySaleOrder = null;
        Session session = HibernateUtil.getSession();
        try {
            Query<EntitySaleOrder> query = session.getNamedQuery("getSaleOrderById");
            query.setParameter("id", entitySaleOrder.getId());
            resultEntitySaleOrder = query.getSingleResult();

        } finally {
            session.close();
        }
        return resultEntitySaleOrder;
    }*/

    /*public EntitySaleOrder getEntitySaleOrderByOrderNo(EntitySaleOrder entitySaleOrder) {
        EntitySaleOrder resultEntitySaleOrder = null;
        Session session = HibernateUtil.getSession();
        try {
            Query<EntitySaleOrder> query = session.getNamedQuery("getSaleOrderByOrderNo");
            query.setParameter("orderNo", entitySaleOrder.getOrderNo());
            resultEntitySaleOrder = query.getSingleResult();

        } catch (Exception ex) {

        } finally {
            session.close();
        }
        return resultEntitySaleOrder;
    }*/

    /*public boolean updateSaleOrderInfo(DTOSaleOrder dtoSaleOrder) {
        boolean status = false;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction();
        try {
            if (dtoSaleOrder != null && dtoSaleOrder.getEntitySaleOrder() != null) {
                //getting current entity sale order based on entity sale order id
                EntitySaleOrder currEntitySaleOrder = getEntitySaleOrderById(dtoSaleOrder.getEntitySaleOrder().getId());
                if (currEntitySaleOrder != null && currEntitySaleOrder.getId() > 0) {
                    tx.begin();
                    //updating entity sale order table
                    EntitySaleOrder entitySaleOrder = dtoSaleOrder.getEntitySaleOrder();
                    session.update(entitySaleOrder);

                    //we have to follow delete then insert approach and update some tables.
                    //delete entitySaleOrderProduct based on existing order no
                    Query<EntitySaleOrderProduct> querySaleOrderProducts = session.getNamedQuery("deleteSaleOrderProductsByOrderNo");
                    querySaleOrderProducts.setParameter("saleOrderNo", currEntitySaleOrder.getOrderNo());
                    int code = querySaleOrderProducts.executeUpdate();

                    //delete entityShowRoomStock based on existing order no
                    Query<EntityShowRoomStock> queryStockProducts = session.getNamedQuery("deleteSaleOrderShowRoomProductsByOrderNo");
                    queryStockProducts.setParameter("saleOrderNo", currEntitySaleOrder.getOrderNo());
                    queryStockProducts.setParameter("transactionCategoryId", Constants.SS_TRANSACTION_CATEGORY_ID_SALE_OUT);
                    code = queryStockProducts.executeUpdate();

                    List<DTOProduct> products = dtoSaleOrder.getProducts();
                    int totalProducts = products.size();
                    for (int counter = 0; counter < totalProducts; counter++) {
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
                    status = true;
                }
            }
        } catch (Exception ex) {
            logger.error(ex.toString());
            tx.rollback();
        } finally {
            session.close();
        }
        return status;
    }*/
}
