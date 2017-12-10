/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.GsonBuilder;
import org.bdlions.inventory.packet.PacketHeaderImpl;
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
public class ProductHandlerTest extends HTTPRequestHelper{
    
    public ProductHandlerTest() {
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
    // @Test
    // public void hello() {}
    
    //@Test
    public void getAllProductCategoriesTest() {
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.FETCH_ALL_PRODUCT_CATEGORIES);
        mockPacketHeader.setRequestType(REQUEST_TYPE.REQUEST);
        mockPacketHeader.setSessionId(getSessionId());

        String packetHeader = new GsonBuilder().create().toJson(mockPacketHeader);
        System.out.println(packetHeader);

        String packetBody = "{}";
        System.out.println(packetBody);

        String result = getResult(packetHeader, packetBody);
        System.out.println("Result : " + result);
    }
    
    //@Test
    public void getAllProductTypesTest() {
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.FETCH_ALL_PRODUCT_TYPES);
        mockPacketHeader.setRequestType(REQUEST_TYPE.REQUEST);
        mockPacketHeader.setSessionId(getSessionId());

        String packetHeader = new GsonBuilder().create().toJson(mockPacketHeader);
        System.out.println(packetHeader);

        String packetBody = "{}";
        System.out.println(packetBody);

        String result = getResult(packetHeader, packetBody);
        System.out.println("Result : " + result);
    }
    
    //@Test
    public void getAllUOMsTest() {
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.FETCH_ALL_UOMS);
        mockPacketHeader.setRequestType(REQUEST_TYPE.REQUEST);
        mockPacketHeader.setSessionId(getSessionId());

        String packetHeader = new GsonBuilder().create().toJson(mockPacketHeader);
        System.out.println(packetHeader);

        String packetBody = "{}";
        System.out.println(packetBody);

        String result = getResult(packetHeader, packetBody);
        System.out.println("Result : " + result);
    }
    
    //@Test
    public void addProductInfoTest() {
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.ADD_PRODUCT_INFO);
        mockPacketHeader.setRequestType(REQUEST_TYPE.UPDATE);
        mockPacketHeader.setSessionId(getSessionId());

        String packetHeader = new GsonBuilder().create().toJson(mockPacketHeader);
        System.out.println(packetHeader);

        String packetBody = "{\"name\":\"samsung mobile\", \"code\":\"code1\", \"categoryId\":1, \"categoryTitle\":\"Product category1\", \"typeId\":1, \"typeTitle\":\"Product type1\", \"purchase_uom_id\":1, \"sale_uom_id\":1, \"standard_uom_id\":1}";
        System.out.println(packetBody);

        String result = getResult(packetHeader, packetBody);
        System.out.println("Result : " + result);
    }
    
    //@Test
    public void updateProductInfoTest() {
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.UPDATE_PRODUCT_INFO);
        mockPacketHeader.setRequestType(REQUEST_TYPE.UPDATE);
        mockPacketHeader.setSessionId(getSessionId());

        String packetHeader = new GsonBuilder().create().toJson(mockPacketHeader);
        System.out.println(packetHeader);

        String packetBody = "{\"id\":1, \"name\":\"jeansu\", \"code\":\"code11\", \"categoryId\":1, \"categoryTitle\":\"Product category1\", \"typeId\":1, \"typeTitle\":\"Product type1\", \"purchase_uom_id\":1, \"sale_uom_id\":1, \"standard_uom_id\":1}";
        System.out.println(packetBody);

        String result = getResult(packetHeader, packetBody);
        System.out.println("Result : " + result);
    }
    
    //@Test
    public void getProductInfoTest() {
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.FETCH_PRODUCT_INFO);
        mockPacketHeader.setRequestType(REQUEST_TYPE.REQUEST);
        mockPacketHeader.setSessionId(getSessionId());

        String packetHeader = new GsonBuilder().create().toJson(mockPacketHeader);
        System.out.println(packetHeader);

        String packetBody = "{\"id\":1}";
        System.out.println(packetBody);

        String result = getResult(packetHeader, packetBody);
        System.out.println("Result : " + result);
    }
    
    //@Test
    public void getProductInfoByCodeTest() {
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.FETCH_PRODUCT_BY_CODE);
        mockPacketHeader.setRequestType(REQUEST_TYPE.REQUEST);
        mockPacketHeader.setSessionId(getSessionId());

        String packetHeader = new GsonBuilder().create().toJson(mockPacketHeader);
        System.out.println(packetHeader);

        String packetBody = "{\"code\":\"pant\"}";
        System.out.println(packetBody);

        String result = getResult(packetHeader, packetBody);
        System.out.println("Result : " + result);
    }
    
    //@Test
    public void getAllProductsTest() {
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.FETCH_PRODUCTS);
        mockPacketHeader.setRequestType(REQUEST_TYPE.REQUEST);
        mockPacketHeader.setSessionId(getSessionId());

        String packetHeader = new GsonBuilder().create().toJson(mockPacketHeader);
        System.out.println(packetHeader);

        String packetBody = "{\"limit\":10, \"offset\":0}";
        System.out.println(packetBody);

        String result = getResult(packetHeader, packetBody);
        System.out.println("Result : " + result);
    }
    
    //@Test
    public void getProductsByNameTest() {
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.FETCH_PRODUCTS_BY_NAME);
        mockPacketHeader.setRequestType(REQUEST_TYPE.REQUEST);
        mockPacketHeader.setSessionId(getSessionId());

        String packetHeader = new GsonBuilder().create().toJson(mockPacketHeader);
        System.out.println(packetHeader);

        String packetBody = "{\"limit\":10, \"offset\":0, \"entityProduct\":{\"name\":\"jeans\"}}";
        System.out.println(packetBody);

        String result = getResult(packetHeader, packetBody);
        System.out.println("Result : " + result);
    }
}
