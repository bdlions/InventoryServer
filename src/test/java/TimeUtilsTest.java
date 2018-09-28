import org.bdlions.inventory.util.TimeUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class TimeUtilsTest {
    
    public TimeUtilsTest() {
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

    @Test
    public void timeTest() 
    {
        long unixTime = TimeUtils.getCurrentTime();
        System.out.println(unixTime);
        System.out.println(TimeUtils.convertUnixToHuman(unixTime, "", "+0"));
        System.out.println(TimeUtils.convertUnixToHuman(unixTime, "", "+6"));
    }
}
