/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

/**
 *
 * @author Albin Hjalmas.
 */
public class DFOTest {
    public static void main(String[] args) {
        Graph<String> g = new Graph();
        g.addV("v1");
        g.addV("v2");
        g.addV("v3");
        g.addV("v4");
        g.addV("v5");
        
        g.addE("v1", "v5");
        g.addE("v2", "v5");
        g.addE("v3", "v5");
        g.addE("v4", "v5");
        g.addE("v1", "v2");
        g.addE("v3", "v2");
        g.addE("v4", "v2");
        g.addE("v5", "v2");
        
        new DFO<String>(g) {
            @Override
            public void operation(String vertex) {
                System.out.println(vertex);
            }  
        }.run();
    }
}
