/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntityProductCategory;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Nazmul
 */
public class ProductDAOTest {
    
    public ProductDAOTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    //@Test
    public void getAllProductCategories() 
    {
        
    }
    
    @Test
    public void getEntityProductByNameTest() 
    {
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
        EntityProduct resultEntityProduct = entityManagerProduct.getProductByName("tshirt");
        System.out.println(resultEntityProduct.getName());
    }
}
