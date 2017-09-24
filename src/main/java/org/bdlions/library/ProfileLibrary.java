package org.bdlions.library;

import org.bdlions.dto.EntityCompany;
import org.bdlions.dto.EntityProfile;
import org.bdlions.dto.EntityUser;
import org.bdlions.manager.Profile;
import org.bdlions.manager.User;

/**
 *
 * @author Nazmul hasan
 */
public class ProfileLibrary 
{
    public ProfileLibrary()
    {
    
    }
    
    /**
     * This method will return profile info by user id including user info and company info
     * @param userId user id
     * @return User user info
     * @author nazmul hasan on 2nd August 2017
     */
    public EntityProfile getProfileInfo(int userId)
    {
        Profile profileManager = new Profile();
        User userManager = new User();
        
        EntityProfile profile = null;
        EntityUser user = null;
        EntityCompany company = null;
        try
        {
            profile = profileManager.getUserProfileById(userId);
            if(profile != null)
            {
                user = userManager.getUserById(userId);
                int companyId = 0;
                if(profile != null && profile.getCompanyId() > 0)
                {
                    companyId = profile.getCompanyId();
                }
                company = profileManager.getUserCompanyById(userId, companyId);
                profile.setUser(user);
                profile.setCompany(company);
            }
        }
        catch(Exception ex)
        {
        
        }        
        return profile;
    }
}
