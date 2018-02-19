package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntityProductCategory;
import org.bdlions.inventory.entity.EntityProductSupplier;
import org.bdlions.inventory.entity.EntitySaleOrder;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerProduct 
{
    private int appId;
    public EntityManagerProduct(int appId)
    {
        this.appId = appId;
    }
    
    /**
     * This method will return product info by name
     * @param name, product name
     * @return EntityProduct, product info
     */
    public EntityProduct getProductByName(String name)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try {
            
            Query<EntityProduct> query = session.getNamedQuery("getProductByName");
            query.setParameter("name", name);            
            //getSingleResult will throw NoResultException if query has no result
            //return query.getSingleResult();
            List<EntityProduct> productList = query.getResultList();
            if(productList == null || productList.isEmpty())
            {
                return null;
            }
            else
            {
                return productList.get(0);
            }           
        }
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return product info by code
     * @param code, product code
     * @return EntityProduct, product info
     */
    public EntityProduct getProductByCode(String code)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try {
            
            Query<EntityProduct> query = session.getNamedQuery("getProductByCode");
            query.setParameter("code", code);            
            List<EntityProduct> productList = query.getResultList();
            if(productList == null || productList.isEmpty())
            {
                return null;
            }
            else
            {
                return productList.get(0);
            }           
        }
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return product info by product id
     * @param productId, product id
     * @return EntityProduct, product info
     */
    public EntityProduct getProductByProductId(int productId) 
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityProduct> query = session.getNamedQuery("getProductByProductId");
            query.setParameter("productId", productId);
            List<EntityProduct> productList = query.getResultList();
            if(productList == null || productList.isEmpty())
            {
                return null;
            }
            else
            {
                return productList.get(0);
            } 
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return product list based on product id list
     * @param productIds, product id list
     * @return List, product list
     */
    public List<EntityProduct> getProductsByProductIds(List<Integer> productIds) 
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityProduct> query = session.getNamedQuery("getProductsByProductIds");
            query.setParameter("productIds", productIds);
            return query.getResultList();

        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will create a new product
     * @param entityProduct, product info
     * @return EntityProduct, product info assigning id
     */
    public EntityProduct createProduct(EntityProduct entityProduct) 
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            session.save(entityProduct);
            return entityProduct;
        }
        finally 
        {
            session.close();
        }
    }
    
    public EntityProduct createProduct(EntityProduct entityProduct, List<EntityProductSupplier> productSuppliers) 
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();
        try 
        {
            session.save(entityProduct);
            if(entityProduct.getId() > 0 && productSuppliers != null && !productSuppliers.isEmpty())
            {
                EntityManagerProductSupplier entityManagerProductSupplier = new EntityManagerProductSupplier(this.appId);
                for(EntityProductSupplier entityProductSupplier: productSuppliers)
                {
                    entityProductSupplier.setProductId(entityProduct.getId());
                    entityProductSupplier.setProductName(entityProduct.getName());
                    entityManagerProductSupplier.addProductSupplier(entityProductSupplier, session);
                }
            } 
            tx.commit();
            return entityProduct;
        }
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will update product info
     * @param entityProduct, product info
     * @return boolean whether product info is updated or not
     */
    public boolean updateProduct(EntityProduct entityProduct) 
    {
        if(entityProduct == null || entityProduct.getId() <= 0)
        {
            return false;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();
        try 
        {
            session.update(entityProduct);
            tx.commit();
            return true;
        }
        finally 
        {
            session.close();
        }
    }
    
    public boolean updateProduct(EntityProduct entityProduct, List<EntityProductSupplier> productSuppliers, List<Integer> supplierUserIds) 
    {
        if(entityProduct == null || entityProduct.getId() <= 0)
        {
            return false;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();
        try 
        {
            session.update(entityProduct);
            if(productSuppliers != null && !productSuppliers.isEmpty())
            {
                EntityManagerProductSupplier entityManagerProductSupplier = new EntityManagerProductSupplier(this.appId);
                if(supplierUserIds != null && !supplierUserIds.isEmpty())
                {
                    entityManagerProductSupplier.deleteProductSuppliersBySupplierUserIds(entityProduct.getId(), supplierUserIds, session);
                }                
                for(EntityProductSupplier entityProductSupplier: productSuppliers)
                {
                    entityProductSupplier.setProductId(entityProduct.getId());
                    entityProductSupplier.setProductName(entityProduct.getName());
                    entityManagerProductSupplier.addProductSupplier(entityProductSupplier, session);
                }
            } 
            //updating product name at show room stock table
            EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock(this.appId);
            entityManagerShowRoomStock.updateShowRoomStockProductInfo(entityProduct, session);
            tx.commit();
            return true;
        }
        finally 
        {
            session.close();
        }
    }
    
    public int updateProductCategoryInfo(EntityProductCategory entityProductCategory, Session session) 
    {
        Query<EntityShowRoomStock> query = session.getNamedQuery("updateProductCategoryInfo");
        query.setParameter("categoryTitle", entityProductCategory.getTitle());
        query.setParameter("vat", entityProductCategory.getVat());
        query.setParameter("categoryId", entityProductCategory.getId());
        return query.executeUpdate();
    }
    
    /**
     * This method will return product list
     * @param offset, offset
     * @param limit, limit
     * @return List, product info list
     */
    public List<EntityProduct> getProducts(int offset, int limit) 
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityProduct> query = session.getNamedQuery("getProducts");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return total number of products
     * @return Integer total number products
     */
    public int getTotalProducts() 
    {
        // use count agreegate method here
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityProduct> query = session.getNamedQuery("getProducts");
            return query.getResultList().size();
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return product list search by name, case insensitive
     * @param name product name
     * @param offset, offset
     * @param limit, limit
     * @return List, product info list
     */
    public List<EntityProduct> searchProductByName(String name, int offset, int limit) 
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityProduct> query = session.getNamedQuery("searchProductByName");
            query.setParameter("name", "%" + name.toLowerCase() + "%");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return total number of products searched by name
     * @param name product name
     * @return Integer total number products
     */
    public int searchTotalProductByName(String name) 
    {
        // use count agreegate method here
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityProduct> query = session.getNamedQuery("searchProductByName");
            query.setParameter("name", "%" + name.toLowerCase() + "%");
            return query.getResultList().size();
        } 
        finally 
        {
            session.close();
        }
    }
    
    public List<EntityProduct> searchProduct(String name, int typeId, int categoryId, int offset, int limit) 
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            String where = "";
            String strLimit = "";
            String strOffset = "";
            if(!StringUtils.isNullOrEmpty(name))
            {
                String lowerName = name.toLowerCase();
                if(where.equals(""))
                {
                    where = " where lower(name) like '%" + lowerName + "%'";
                }
                else 
                {
                    where += " and lower((name) like '%" + lowerName + "%'";
                }
            }
            if(typeId > 0)
            {
                if(where.equals(""))
                {
                    where = " where type_id = " + typeId;
                }
                else 
                {
                    where += " and type_id = " + typeId;
                }
            }
            if(categoryId > 0)
            {
                if(where.equals(""))
                {
                    where = " where category_id = " + categoryId;
                }
                else 
                {
                    where += " and category_id = " + categoryId;
                }
            }
            if(limit > 0)
            {
                strOffset = " offset " + offset;
                strLimit = " limit " + limit;
            }
            
            Query query = session.createSQLQuery("select {ep.*} from products ep " + where + strLimit + strOffset )
                    .addEntity("ep",EntityProduct.class);
            return query.list();           
        } 
        finally 
        {
            session.close();
        }
    }
    public int searchTotalProducts(String name, int typeId, int categoryId) 
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            String where = "";
            if(!StringUtils.isNullOrEmpty(name))
            {
                String lowerName = name.toLowerCase();
                if(where.equals(""))
                {
                    where = " where lower(name) like '%" + lowerName + "%'";
                }
                else 
                {
                    where += " and lower((name) like '%" + lowerName + "%'";
                }
            }
            if(typeId > 0)
            {
                if(where.equals(""))
                {
                    where = " where type_id = " + typeId;
                }
                else 
                {
                    where += " and type_id = " + typeId;
                }
            }
            if(categoryId > 0)
            {
                if(where.equals(""))
                {
                    where = " where category_id = " + categoryId;
                }
                else 
                {
                    where += " and category_id = " + categoryId;
                }
            }
            Query query = session.createSQLQuery("select {ep.*} from products ep " + where)
                    .addEntity("ep",EntityProduct.class);
            return query.list().size();           
        } 
        finally 
        {
            session.close();
        }
    }
}
