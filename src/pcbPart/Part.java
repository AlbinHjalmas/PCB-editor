package pcbPart;

import graphPrimitives.Component;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import pcbPrimitives.Pad;

/**
 *
 * @author Albin Hjalmas.
 */
public final class Part implements Component, Cloneable {

    // Listeners
    private ArrayList<ActionListener> actionListeners;
    
    private Footprint fp;
    private Rectangle2D.Double ol; // The outline
    private boolean selected;
    private transient Stroke defaultStroke;
    private String name;
    private String partId;
    private double width, height;

    Shape s;

    /**
     * Super constructor.
     *
     * @param name
     * @param footprint the footprint of this part.
     * @param width
     * @param height
     */
    public Part(String id, String name, Footprint footprint, double width, double height) {
        this.name = name;
        partId = id;
        fp = footprint;
        actionListeners = new ArrayList<>();

        // Set reference to this part.
        for (Pad pad : fp.getPads()) {
            pad.setPart(this);
        }

        // Construct the outline
        ol = new Rectangle2D.Double(
                fp.getP().x - width / 2,
                fp.getP().y - height / 2,
                width, height
        );

        this.width = width;
        this.height = height;
    }

    /**
     * Sets the name of this part.
     *
     * @param name the new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name of this part.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the id for this part.
     *
     * @return the id for this part.
     */
    public String getId() {
        return partId;
    }

    /**
     * Gets the footprint.
     *
     * @return the current footprint.
     */
    public Footprint getFootprint() {
        return fp;
    }
    
    /**
     * Add an actionlistener.
     * 
     * @param listener 
     */
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }
    
    /**
     * Removes the specified actionListener from the list of ActionListeners.
     * 
     * @param listener 
     */
    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }
    
    /**
     * Rotate part 90 degrees.
     *
     * @param clockwise if true: the part will be rotated 90 degrees clockwise
     * else it will be rotated 90 degrees counterclockwise.
     */
    public void rot90(boolean clockwise) {
        if (clockwise) {
            for (Pad pad : fp.getPads()) {
                pad.translate(new Point2D.Double(
                        fp.getP().x - fp.getP().y + pad.getP().y - pad.getP().x,
                        fp.getP().x + fp.getP().y - pad.getP().x - pad.getP().y
                ));
            }
        } else {
            for (Pad pad : fp.getPads()) {
                pad.translate(new Point2D.Double(
                        fp.getP().x + fp.getP().y - pad.getP().x - pad.getP().y,
                        fp.getP().y - fp.getP().x + pad.getP().x - pad.getP().y
                ));
            }
        }

        // Change the outline
        double width1 = ol.width;
        ol.width = ol.height;
        ol.height = width1;

        ol = new Rectangle2D.Double(
                fp.getP().x - ol.width / 2,
                fp.getP().y - ol.height / 2,
                ol.width, ol.height
        );
    }

    /**
     * Show the contextmenu related to this part.
     */
    public void showContextMenu(MouseEvent evt) {
        JPopupMenu m = new JPopupMenu(getName());
        JMenuItem cw = new JMenuItem("Rotate Clockwise");
        
        cw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rot90(true);
                for(ActionListener listener : actionListeners) {
                    if (listener == null) {
                        continue;
                    }
                    
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            listener.actionPerformed(e);
                        }
                    });
                }
            }
        });
        
        JMenuItem ccw = new JMenuItem("Rotate Counterclockwise");
        
        ccw.addActionListener((ActionEvent e) -> {
            rot90(false);
            for(ActionListener listener : actionListeners) {
                if (listener == null) {
                        continue;
                    }
                    
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            listener.actionPerformed(e);
                        }
                    });
            }
        });
        
        m.add(cw);
        m.add(ccw);

        // Show the context menu
        m.show(evt.getComponent(), evt.getX(), evt.getY());
    }

    @Override
    public void setSelected(boolean state) {
        selected = state;
        fp.setSelected(state);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void draw(Graphics2D g) {
        fp.draw(g);
        g.setColor(Color.yellow);
        g.draw(ol);
    }

    @Override
    public void drawSelected(Graphics2D g) {
        fp.drawSelected(g);

        // Draw a fattened version of the outline
        g.setColor(Color.yellow);
        defaultStroke = g.getStroke();
        g.setStroke(new BasicStroke(
                4f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        ));
        g.draw(ol);

        g.setStroke(defaultStroke);
    }

    @Override
    public void translate(Point2D.Double delta) {
        ol.x += delta.x;
        ol.y += delta.y;
        fp.translate(delta);
    }

    @Override
    public boolean contains(Point2D.Double p) {
        return ol.contains(p);
    }

    @Override
    public Component getUnderlyingComponent(Point2D.Double p) {
        return fp.getUnderlyingComponent(p);
    }

    @Override
    public void setColor(Color color) {

    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public Shape getPaddedOutline(double padding) {
        BasicStroke stroke = new BasicStroke(
                (float) (padding * 2 + 1),
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        );

        return stroke.createStrokedShape(ol);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Point2D.Double getP() {
        return (Point2D.Double) fp.getP().clone();
    }

    @Override
    public void setP(Point2D.Double p) {
        Point2D.Double c = getP();
        translate(new Point2D.Double(p.x - c.x, p.y - c.y));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ArrayList<Pad> pads = new ArrayList<>();
        for (Pad pad : fp.getPads()) {
            pads.add(pad.clone());
        }

        Pad[] padAr = new Pad[pads.size()];

        for (int i = 0; i < pads.size(); i++) {
            padAr[i] = pads.get(i);
        }

        return new Part(partId, name,
                new Footprint(new Point2D.Double(fp.getP().x, fp.getP().y), padAr),
                width, height);
    }
}
