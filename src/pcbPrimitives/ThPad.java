package pcbPrimitives;

import graphPrimitives.Component;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pcbEditor.Net;

/**
 * 
 * @author Albin Hjalmas.
 */
public final class ThPad extends Pad {

    private Net net; // A reference to the net that this pad belongs to.
    private boolean visible; // The visibility state of this component
    private Color color;
    private Ellipse2D.Double ring; // Annular ring
    private Ellipse2D.Double hole; // Hole
    private Point2D.Double p;
    
    /**
     * Constructor.
     * @param p The center point of this pad. 
     * @param ringDiam the annular ring diameter.
     * @param holeDiam the hole diameter.
     * @param color the color of this pad.
     * @param visible
     */
    public ThPad(Point2D.Double p, 
            double ringDiam, double holeDiam, Color color, boolean visible) {
        this.p = p;
        ring = new Ellipse2D.Double(p.x - ringDiam/2, p.y - ringDiam/2, 
                ringDiam, ringDiam);
        
        hole = new Ellipse2D.Double(p.x - holeDiam/2, p.y - holeDiam/2, 
                holeDiam, holeDiam);
        
        this.color = color;
        this.visible = visible;
    }
    
    /**
     * Sets the annular ring diameter of this pad.
     * @param diam the new diameter.
     */
    public void setRingDiam(double diam) {
        ring = new Ellipse2D.Double(p.x - diam/2, p.y - diam/2, 
                diam, diam);
    }
    
    /**
     * Sets the hole diameter of this pad.
     * @param diam the new diameter.
     */
    public void setHoleDiam(double diam) {
        hole = new Ellipse2D.Double(p.x - diam/2, p.y - diam/2, 
                diam, diam);
    }
    
    /**
     * Sets the visibility state of this pad.
     * @param state 
     */
    @Override
    public void setVisible(boolean state) {
        visible = state;
    }
    
    /**
     * Get the visibility state of this component.
     * @return the visibility state.
     */
    @Override
    public boolean getVisible() {
        return visible;
    }
    
    /**
     * Get the net that this pad belong to.
     * @return the net that this pad belong to.
     */
    @Override
    public Net getNet() {
        return net;
    }
    
    /**
     * Set the net that this pad belong to.
     * @param net the net that this pad belong to.
     */
    @Override
    public void setNet(Net net) {
        this.net = net;
    }
    
    @Override
    public void draw(Graphics2D g) {
        if(!visible) return;
        g.setColor(color);
        g.fill(ring);
        g.setColor(Color.BLACK);
        g.fill(hole);
    }

    @Override
    public void drawSelected(Graphics2D g) {
        if(!visible) return;
        g.setColor(color.brighter());
        g.fill(ring);
        g.setColor(Color.BLACK);
        g.fill(hole);
        g.setColor(Color.WHITE);
        g.draw(ring);
    }

    @Override
    public boolean contains(Point2D.Double p) {
        return ring.contains(p);
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void translate(Point2D.Double delta) {
        ring.x += delta.x;
        ring.y += delta.y;
        hole.x += delta.x;
        hole.y += delta.y;
        
        for(ChangeListener l : getListeners()) {
            l.stateChanged(new ChangeEvent(this));
        }
    }
    
    /**
     * Get the center point of this pad. 
     * @return center of pad.
     */
    @Override
    public Point2D.Double getP() {
        return new Point2D.Double(ring.x + ring.width/2, ring.y + ring.width/2);
    }
    
    @Override
    public void setP(Point2D.Double p) {
        ring.x = p.x - ring.getHeight()/2;
        ring.y = p.y - ring.getHeight()/2;
        hole.x = p.x - hole.getHeight()/2;
        hole.y = p.y - hole.getHeight()/2;
        
        for(ChangeListener l : getListeners()) {
            l.stateChanged(new ChangeEvent(this));
        }
    }
    
    @Override
    public Component getUnderlyingComponent(Point2D.Double p) {
        if(this.contains(p)) {
            return this;
        } else {
            return null;
        }
    }
    
    @Override
    public Pad clone() {
        return new ThPad(getP(), ring.getHeight(), hole.getHeight(), 
                color, visible);
    }

    @Override
    public Shape getPaddedOutline(double padding) {
        return new Ellipse2D.Double(ring.x, ring.y, 
                ring.height + padding * 2, ring.width + padding * 2);
    }
}
