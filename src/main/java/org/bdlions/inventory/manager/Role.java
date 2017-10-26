/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.inventory.manager;

import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityRole;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author alamgir
 */
public class Role {
    public EntityRole getRoleByRoleId(int roleId){
        Session session = HibernateUtil.getSession();
        try{
            return getRoleByRoleId(session, roleId);
        }
        finally{
            session.close();
        }
    }
    public EntityRole getRoleByRoleId(Session session, int roleId){
        Query<EntityRole> query = session.getNamedQuery("getRoleById");
        query.setParameter("roleId", roleId);
        return query.uniqueResult();

    }
}
