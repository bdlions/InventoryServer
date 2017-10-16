/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.db.HibernateUtil;
import org.bdlions.dto.DTOProduct;
import org.bdlions.dto.EntityProduct;
import org.bdlions.dto.EntitySaleOrderProduct;
import org.bdlions.dto.EntityShowRoomStock;
import org.bdlions.util.Constants;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul
 */
public class Stock {
    public List<DTOProduct> getCurrentStock()
    {
        List<DTOProduct> products = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try
        {
            Query<Object[]> queryStockProducts = session.getNamedQuery("getCurrentStock");
            List<Object[]> showRoomProducts = queryStockProducts.getResultList();
            for(Object[] entityShowRoomStock : showRoomProducts)
            {
                int productId = (int)entityShowRoomStock[0];
                double quantity = (double)entityShowRoomStock[1];
                
                Query<EntityProduct> queryEntityEntityProduct = session.getNamedQuery("getProductByProductId");
                queryEntityEntityProduct.setParameter("productId", productId);
                EntityProduct entityProduct =  queryEntityEntityProduct.getSingleResult();
                DTOProduct dtoProduct = new DTOProduct();
                dtoProduct.setQuantity(quantity);
                dtoProduct.setEntityProduct(entityProduct);
                products.add(dtoProduct);
            }
        }
        finally {
            session.close();
        }
        return products;
    }
}
