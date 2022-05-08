/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.util.ArrayList;
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
public class GraphTest {
    
    Graph<String> instance;
    
    public GraphTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new Graph<>();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addV method, of class Graph.
     */
    @Test
    public void testAddV() {
        System.out.println("addV");
        String v = "v1";
        instance.addV(v);
        assertEquals(1, instance.getV());
    }

    /**
     * Test of removeV method, of class Graph.
     */
    @Test
    public void testRemoveV() {
        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        instance.addV(v1);
        instance.addV(v2);
        instance.addV(v3);
        
        instance.addE(v1, v2);
        instance.addE(v1, v3);
        assertEquals(2, instance.getE());
        
        // Try to remove non existing vertex.
        assertEquals(false, instance.removeV("v5"));
        assertEquals(2, instance.getE());
        
        // Now remove existing vertex.
        assertEquals(true, instance.removeV(v1));
        assertEquals(0, instance.getE());
    }

    /**
     * Test of addE method, of class Graph.
     */
    @Test
    public void testAddE() {
        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        instance.addV(v1);
        instance.addV(v2);
        instance.addV(v3);
        
        instance.addE(v1, v2);
        instance.addE(v1, v3);
        instance.addE(v2, v3);
        assertEquals(3, instance.getE());
        
        // Check edges
        ArrayList<String> a = instance.getAdj(v1);
        assertEquals(true, a.contains(v2));
        assertEquals(true, a.contains(v3));
        
        a = instance.getAdj(v2);
        assertEquals(true, a.contains(v1));
        assertEquals(true, a.contains(v3));
        
        a = instance.getAdj(v3);
        assertEquals(true, a.contains(v1));
        assertEquals(true, a.contains(v2));
        
        // Fail on purpose
        assertEquals(false, instance.addE(v1, "v4"));
        
        // Add parallell edge (not allowed)
        assertEquals(false, instance.addE(v1, v2));
    }

    /**
     * Test of removeE method, of class Graph.
     */
    @Test
    public void testRemoveE() {
        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        instance.addV(v1);
        instance.addV(v2);
        instance.addV(v3);
        
        instance.addE(v1, v2);
        instance.addE(v1, v3);
        instance.addE(v2, v3);
        assertEquals(3, instance.getE());
        
        // Remove edge between v1 and v2
        assertEquals(true, instance.removeE(v1, v2));
        
        // Check if edge was truly removed
        ArrayList<String> a1 = instance.getAdj(v1);
        ArrayList<String> a2 = instance.getAdj(v2);
        assertEquals(false, a1.contains(v2));
        assertEquals(false, a2.contains(v1));
        assertEquals(2, instance.getE());
        
        // Remove none existing edge
        assertEquals(false, instance.removeE(v1, v2));
    }

    /**
     * Test of containsE method, of class Graph.
     */
    @Test
    public void testContainsE() {
        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        instance.addV(v1);
        instance.addV(v2);
        instance.addV(v3);
        
        instance.addE(v1, v2);
        instance.addE(v1, v3);
        instance.addE(v2, v3);
        assertEquals(3, instance.getE());
        
        // Check if it contains actual edge
        assertEquals(true, instance.containsE(v1, v2));
        
        // Check if it returns false if called
        // with a non existing edge
        assertEquals(false, instance.containsE(v1, "v4"));
    }
}
