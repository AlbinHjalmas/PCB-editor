
package pcbPrimitives;

import graphPrimitives.Component;
import graphPrimitives.Vertex;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pcbEditor.Net;

/**
 *
 * @author Albin Hjalmas.
 */
public class Segment implements Component, ChangeListener, Serializable {

    private Net net; // A reference to the net to which this segment belongs to.
    private Vertex p0, p1; // The endpoints of this segment.
    double width; // The width of this segment
    private transient Stroke defStroke; // Placeholder for the default stroke.
    private Line2D.Double line;
    private boolean selectionState;
    private Color color;
    
    /**
     * Constructor.
     * @param p0 first endpoint.
     * @param p1 second endpoint.
     * @param width the width of the segment.
     * @param color the color of this segment.
     */
    public Segment(Vertex p0, Vertex p1, double width, Color color) {
        // Listen to changes on on p1 and p2.
        p0.addListener(this);
        p1.addListener(this);
        
        this.width = width;
        
        this.p0 = p0;
        this.p1 = p1;
        
        line = new Line2D.Double(p0.getP(), p1.getP());
        selectionState = false;
        this.color = color;
    }
    
    /**
     * Set the net that this segment is connected to.
     * @param net the net to connect to.
     */
    public void setNet(Net net) {
        this.net = net;
    }
    
    /**
     * Get the net that this segment is connected to.
     * @return the connected net.
     */
    public Net getNet() {
        return net;
    }
    
    /**
     * Check if this segment is connected to the specified net.
     * @param net the specified net.
     * @return true if this segment is connected 
     * to the specified net, else false.
     */
    public boolean isConnectedTo(Net net) {
        if(this.net == null) {
            return false;
        } else {
            return this.net.equals(net);
        }
    }
    
    /**
     * Sets the width of this segment.
     * @param width the new width;
     */
    public void setWidth(double width) {
        this.width = width;
    }
    
    @Override
    public void setSelected(boolean state) {
        selectionState = state;
    }

    @Override
    public boolean isSelected() {
        return selectionState;
    }

    @Override
    public void draw(Graphics2D g) {
        defStroke = g.getStroke();
        g.setStroke(new BasicStroke((float) width, BasicStroke.CAP_ROUND, 
                BasicStroke.JOIN_ROUND));
        g.setColor(color);
        g.draw(line);
        g.setStroke(defStroke);
    }

    @Override
    public void drawSelected(Graphics2D g) {       
        defStroke = g.getStroke();
        g.setStroke(new BasicStroke((float) width, BasicStroke.CAP_ROUND, 
                BasicStroke.JOIN_ROUND));
        g.setColor(color.brighter());
        g.draw(line);
        g.setStroke(defStroke);
        g.setColor(Color.WHITE);
        g.draw(line);
    }

    @Override
    public void translate(Point2D.Double delta) {
        p0.translate(delta);
        p1.translate(delta);
    }

    @Override
    public boolean contains(Point2D.Double p) {
        return new BasicStroke((float)width, BasicStroke.CAP_ROUND, 
                BasicStroke.JOIN_ROUND).createStrokedShape(line).contains(p);
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    /**
     * ChangeListener callback function.
     * @param e the event.
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        line.setLine(p0.getP(), p1.getP());
    }

    @Override
    public Component getUnderlyingComponent(Point2D.Double p) {
        if(this.contains(p)) {
            return this;
        } else {
            return null;
        }
    }
    
    public Vertex getP0() {
        return p0;
    }
    
    public Vertex getP1() {
        return p1;
    }

    @Override
    public Shape getPaddedOutline(double padding) {
        BasicStroke stroke = new BasicStroke((float) (width + padding * 2), 
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        return stroke.createStrokedShape(line);
    }

    @Override
    public Point2D.Double getP() {
        return new Point2D.Double(0.5 * (p1.getP().x + p0.getP().x), 
                0.5 * (p1.getP().y + p0.getP().y));
    }

    @Override
    public void setP(Point2D.Double p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
