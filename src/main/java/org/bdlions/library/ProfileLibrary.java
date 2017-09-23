package org.bdlions.library;

import org.bdlions.dto.Company;
import org.bdlions.dto.Profile;
import org.bdlions.dto.User;
import org.bdlions.manager.UserManager;

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
    public Profile getProfileInfo(int userId)
    {
        UserManager userManager = new UserManager();
        Profile profile = null;
        User user = null;
        Company company = null;
        try
        {
            profile = userManager.getUserProfileById(userId);
            if(profile != null)
            {
                user = userManager.getUserInfoById(userId);
                int companyId = 0;
                if(profile != null && profile.getCompanyId() > 0)
                {
                    companyId = profile.getCompanyId();
                }
                company = userManager.getUserCompanyById(userId, companyId);
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
