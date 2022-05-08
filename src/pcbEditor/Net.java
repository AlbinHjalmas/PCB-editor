package pcbEditor;

import pcbPrimitives.*;
import graph.*;
import graphPrimitives.Component;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author Albin Hjalmas.
 */
public final class Net implements graphPrimitives.Component {

    private Graph<Pad> graph; // Graph for storing connections.
    private ArrayList<Segment> segs; // Copper trace segments.
    private String name;
    private boolean selected; // The selection state of this net.

    /**
     * Constructor.
     *
     * @param name the name of this net.
     * @param pads the pads to construct this net from.
     */
    public Net(String name, Pad... pads) {
        graph = new Graph();
        segs = new ArrayList<>();
        this.name = name;

        for (Pad v : pads) {
            v.setNet(this);
            graph.addV(v);
        }
    }
    
    /**
     * Constructor.
     *
     * @param name the name of this net.
     */
    public Net(String name) {
        graph = new Graph();
        segs = new ArrayList<>();
        this.name = name;
    }

    /**
     * Get the name of this net.
     *
     * @return the name of this net.
     */
    public String getName() {
        return new String(name);
    }

    /**
     * Set the name of this net.
     *
     * @param name the new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Add a trace segment between to pads.
     *
     * @param p0 first pad.
     * @param p1 second pad.
     * @param width the desired width of this segment.
     * @param color the desired color of this segment.
     * @return true if successful else false.
     */
    public boolean addSegment(Pad p0, Pad p1, double width, Color color) {

        Net p0Net = p0.getNet();
        Net p1Net = p1.getNet();

        if (p0Net == null) {
            p0.setNet(this);
        }

        if (p1Net == null) {
            p1.setNet(this);
            p1Net = this;
        }

        // Check if the pads belongs to this net.
        if (!(p0Net.equals(this) && p1Net.equals(this))) {
            return false;
        }

        // Graph contains p0 but not p1.
        if (graph.containsV(p0) && !graph.containsV(p1)) {
            graph.addV(p1);

            // Graph contains p1 but not p0.    
        } else if (graph.containsV(p1) && !graph.containsV(p0)) {
            graph.addV(p1);

            // Graph contains neither p1 nor p0. 
        } else if (!graph.containsV(p0) && !graph.containsV(p1)) {
            graph.addV(p1);
            graph.addV(p0);
        }

        // Add edge between p0 and p1.
        graph.addE(p0, p1);

        // Add segment between p0 and p1.
        Segment seg = new Segment(p0, p1, width, color);
        seg.setNet(this);

        // Let segment listen for changes in p0 and p1
        p0.addListener(seg);
        p1.addListener(seg);

        // Add segment to this net.
        segs.add(seg);

        return true;
    }

    /**
     * Remove a segment between two pads.
     *
     * @param p0 first pad.
     * @param p1 second pad.
     * @return true if successful else false.
     */
    public boolean removeSegment(Pad p0, Pad p1) {
        for (Segment seg : segs) {
            if (seg.getP0().equals(p0) && seg.getP1().equals(p1)
                    || seg.getP0().equals(p1) && seg.getP1().equals(p0)) {
                removeSegment(seg);
                return true;
            }
        }

        return false;
    }

    /**
     * Remove the specified segment from this net.
     *
     * @param seg the segment to remove.
     * @return true if successful else false.
     */
    public boolean removeSegment(Segment seg) {

        Pad p0 = (Pad) seg.getP0();
        Pad p1 = (Pad) seg.getP1();

        if (!graph.removeE(p0, p1) || !seg.getNet().equals(this)) {
            return false;
        }

        // Remove listeners.
        p0.removeListener(seg);
        p1.removeListener(seg);

        // Check if p0 is invisible pad and not connected to anything
        if (!p0.getVisible() && graph.getAdj(p0).isEmpty()) {
            graph.removeV(p0);
        }

        // Check if p0 is invisible pad and not connected to anything
        if (!p1.getVisible() && graph.getAdj(p1).isEmpty()) {
            graph.removeV(p1);
        }

        segs.remove(seg);
        return true;
    }

    /**
     * Returns a reference to the segments that constitute the connections 
     * in this net.
     * @return the segments.
     */
    public ArrayList<Segment> getSegments() {
        return segs;
    }
    
    /**
     * Adds a pad to this net.
     *
     * @param pad
     */
    public void addPad(Pad pad) {
        pad.setNet(this);
        graph.addV(pad);
    }

    /**
     * Removes the specified pad from this net.
     *
     * @param pad the pad to remove.
     * @return true if successful else false.
     */
    public boolean removePad(Pad pad) {
        if (!graph.containsV(pad)) {
            return false;
        }

        ArrayList<Pad> adj = (ArrayList) graph.getAdj(pad).clone();
        for (Pad cPad : adj) {
            removeSegment(pad, cPad);
        }
        graph.removeV(pad);
        pad.setNet(null);
        return true;
    }

    /**
     * Returns all the pads connected to this net.
     *
     * @return pads connected to this net.
     */
    public ArrayList<Pad> getPads() {
        return graph.getVertices();
    }

    @Override
    public void setSelected(boolean state) {
        selected = state;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void draw(Graphics2D g) {
        // Draw segments first
        for (Segment seg : segs) {
            seg.draw(g);
        }

        // Draw all pads that are visible
        for (Pad pad : graph.getVertices()) {
            if (pad.getVisible()) {
                pad.draw(g);
            }
        }
    }

    @Override
    public void drawSelected(Graphics2D g) {
        // Draw segments first
        for (Segment seg : segs) {
            seg.drawSelected(g);
        }

        // Draw all pads that are visible
        for (Pad pad : graph.getVertices()) {
            if (pad.getVisible()) {
                pad.drawSelected(g);
            }
        }
    }

    @Override
    public void translate(Point2D.Double delta) {
        // Translate pads
        // Will also translate segments due to observer pattern.
        new DFO<Pad>(graph) {
            @Override
            public void operation(Pad pad) {
                pad.translate(delta);
            }

        }.run();
    }

    @Override
    public boolean contains(Point2D.Double p) {
        // Check segments first.
        for (Segment seg : segs) {
            if (seg.contains(p)) {
                return true;
            }
        }

        // Check all pads.
        for (Pad pad : graph.getVertices()) {
            if (pad.contains(p)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void setColor(Color color) {
        for (Segment seg : segs) {
            seg.setColor(color);
        }
    }

    @Override
    public Color getColor() {
        return segs.get(0).getColor();
    }

    @Override
    public graphPrimitives.Component getUnderlyingComponent(Point2D.Double p) {

        for (Pad pad : graph.getVertices()) {
            Component c = (Component) pad.getUnderlyingComponent(p);
            if (c != null) {
                return (graphPrimitives.Component) c;
            }
        }

        for (Segment seg : segs) {
            Component c = (Component) seg.getUnderlyingComponent(p);
            if (c != null) {
                return (graphPrimitives.Component) c;
            }
        }

        // There were no components belonging to this net at
        // The location specified by p.
        return null;
    }

    @Override
    public Shape getPaddedOutline(double padding) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Point2D.Double getP() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setP(Point2D.Double p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return name;
    }

}
