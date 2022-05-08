
package graph;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author root
 */
public class STTest {
    
    String key;
    String val;
    ST<String, String> instance;
    
    public STTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        key = "Item1";
        val = "Value1";
        instance = new ST<>();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class ST.
     */
    @Test
    public void testAdd() {
        System.out.println("add");

        instance.add(key, val);
        
        assertEquals(instance.size(), 1);
    }

    /**
     * Test of get method, of class ST.
     */
    @Test
    public void testGet() {
        System.out.println("get");

        instance.add(key, val);
        
        // Get the actual object
        assertEquals(val, instance.get(key));
        
        // Try to get non-existing object.
        assertEquals(null, instance.get("N/A"));
    }

    /**
     * Test of remove method, of class ST.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        
        instance.add(key, val);
        
        // Try to remove actual object
        assertEquals(val, instance.remove(key));
        assertEquals(0, instance.size());
        
        // Try to remove non-existing object
        instance.add(key, val);
        assertEquals(null, instance.remove("N/A"));
        assertEquals(1, instance.size());
    }
    
}
