/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.inventory.manager;

/*import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityRole;
import org.bdlions.inventory.entity.EntityUserRole;
import org.hibernate.Session;
import org.hibernate.query.Query;*/

/**
 *
 * @author alamgir
 */
public class UserRole {

    /*public ArrayList<EntityRole> getUserRolesByUserId(int userId) {
        Session session = HibernateUtil.getSession();
        try {
            Query<Integer> query = session.getNamedQuery("getUserRolesByUserId");
            query.setParameter("userId", userId);

            List<Integer> roleIds = query.list();

            Role r = new Role();
            ArrayList<EntityRole> roles = new ArrayList<>();

            for (Integer roleId : roleIds) {
                roles.add(r.getRoleByRoleId(session, roleId));
            }
            return roles;
        } finally {
            session.close();
        }
    }*/

    /*public boolean assignUserRole(int userId, int roleId) {
        Session session = HibernateUtil.getSession();
        try {

            EntityUserRole entityUserRole = new EntityUserRole();
            entityUserRole.setRoleId(roleId);
            entityUserRole.setUserId(userId);

            session.save(entityUserRole);
            return true;

        } finally {
            session.close();
        }
    }*/
}
