/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.manager;

import org.bdlions.db.HibernateUtil;
import org.bdlions.dto.EntityProfile;
import org.bdlions.dto.EntityUser;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author alamgir
 */
public class Profile {

    /**
     * This method will return profile info by user id
     *
     * @param userId user id
     * @return Profile profile info
     * @author nazmul hasan on 2nd August 2017
     */
    public EntityProfile getUserProfileById(int userId) {
        Session session = HibernateUtil.getSession();
        try {
            Query<EntityProfile> query = session.getNamedQuery("getProfileByUserId");
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }

    /**
     * This method will update user profile info
     *
     * @param profile user profile
     * @return boolean whether profile is updated or not
     * @author nazmul hasan on 2nd August 2017
     */
    public boolean updateProfile(EntityProfile profile) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            if (profile != null && profile.getUser() != null) {
                EntityUser user = profile.getUser();
                if (user.getId() > 0 && profile.getId() > 0) {
                    session.update(user);
                    session.update(profile);
                    tx.commit();
                    return true;
                }
                return false;
            }
        }
        catch(Exception ex){
            tx.rollback();
        }
        finally {
            session.close();
        }
        return false;
    }

    /**
     * This method will create a new user profile
     *
     * @param profile user profile
     * @return boolean whether profile is created or not
     * @author nazmul hasan on 2nd August 2017
     */
    public boolean createProfile(EntityProfile profile) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        
        try {
            tx.begin();
            if (profile != null && profile.getUser() != null) {
                EntityUser user = profile.getUser();
                session.save(user);
                profile.setUserId(user.getId());
                session.save(profile);
                tx.commit();
                return true;
            }
        }
        catch(Exception ex){
            tx.rollback();
        }
        finally {
            session.close();
        }
        return false;
    }

}
