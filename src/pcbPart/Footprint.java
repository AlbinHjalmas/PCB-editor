
package pcbPart;

import graphPrimitives.Component;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import pcbPrimitives.Pad;

/**
 *
 * @author Albin Hjalmas
 */
public class Footprint implements Component {
    private ArrayList<Pad> pads; // The pads in this footprint.
    private boolean selected;
    private Point2D.Double center;
    
    /**
     * Constructor.
     * @param center The center point of this footprint.
     * @param pads The pads, positioned relative to center point.
     */
    public Footprint(Point2D.Double center, Pad ... pads) {
        this.center = center;
        this.pads = new ArrayList<>();
        this.pads.addAll(Arrays.asList(pads));
        for(Pad pad : pads) {
            pad.getP().x += center.x;
            pad.getP().y += center.y;
        }
    }
    
    /**
     * Gets the pads belonging to this footprint.
     * @return 
     */
    public ArrayList<Pad> getPads() {
        return pads;
    }
    
    @Override
    public void setSelected(boolean state) {
        selected = state;
        for(Pad pad : pads) {
            pad.setSelected(state);
        }
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void draw(Graphics2D g) {
        for(Pad pad : pads) {
            pad.draw(g);
        }
    }

    @Override
    public void drawSelected(Graphics2D g) {
        for(Pad pad : pads) {
            pad.drawSelected(g);
        }
    }

    @Override
    public void translate(Point2D.Double delta) {
        
        center.x += delta.x;
        center.y += delta.y;
        
        for(Pad pad : pads) {
            pad.translate(delta);
        }
    }

    @Override
    public boolean contains(Point2D.Double p) {
        for(Pad pad : pads) {
            if(pad.contains(p)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public Component getUnderlyingComponent(Point2D.Double p) {
        for(Pad pad : pads) {
            if(pad.contains(p)) {
                return pad;
            }
        }
        
        return null;
    }

    @Override
    public void setColor(Color color) {
        for(Pad pad : pads) {
            pad.setColor(color);
        }
    }

    @Override
    public Color getColor() {
        return pads.get(0).getColor();
    }
    

    
    @Override
    public Shape getPaddedOutline(double padding) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Point2D.Double getP() {
        return (Point2D.Double) center.clone();
    }
    
    @Override
    public void setP(Point2D.Double p) {
        Point2D.Double c = getP();
        translate(new Point2D.Double(p.x - c.x, p.y - c.y));
    }
}
