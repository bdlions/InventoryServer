
import org.bdlions.dto.EntityCompany;
import org.bdlions.dto.EntityUser;
import org.bdlions.manager.Profile;
import org.bdlions.manager.User;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alamgir
 */
public class ProfileDAOTest {
    
    @Test
    public void getProfileById(){
        Profile profile = new Profile();
        EntityCompany c = profile.getUserCompanyById(1, 1);
        
        System.out.println("Company Name: " + c.getTitle());
    }
}
