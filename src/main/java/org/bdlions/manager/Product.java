package org.bdlions.manager;

import java.util.List;
import org.bdlions.db.HibernateUtil;
import org.bdlions.dto.EntityProductCategory;
import org.bdlions.dto.EntityProductType;
import org.bdlions.dto.EntityUOM;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul
 */
public class Product {
    public List<EntityProductCategory> getAllProductCategories(){
        Session session = HibernateUtil.getSession();
        try {
            Query<EntityProductCategory> query = session.getNamedQuery("getAllProductCategories");
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    public List<EntityProductType> getAllProductTypes(){
        Session session = HibernateUtil.getSession();
        try {
            Query<EntityProductType> query = session.getNamedQuery("getAllProductTypes");
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    public List<EntityUOM> getAllUOMs(){
        Session session = HibernateUtil.getSession();
        try {
            Query<EntityUOM> query = session.getNamedQuery("getAllUOMs");
            return query.getResultList();
        } finally {
            session.close();
        }
    }
}
