/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.manager;

import org.bdlions.db.HibernateUtil;
import org.bdlions.dto.EntityCompany;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author alamgir
 */
public class Company {
    public EntityCompany getCompanyById(int id){
        Session session = HibernateUtil.getSession();
        try {
            return getCompanyById(session, id);
        } finally {
            session.close();
        }
    }
    
    public EntityCompany getCompanyById(Session session, int id){
        Query<EntityCompany> query = session.getNamedQuery("getCompanyById");
        query.setParameter("companyId", id);

        return query.getSingleResult();
    }
}
