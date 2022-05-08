package pcbEditor;

import graphPrimitives.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JViewport;
import pcbPart.Part;
import pcbPrimitives.Pad;
import pcbPrimitives.Segment;
import pcbPrimitives.ThPad;
import ui.DefaultSettingsDialog;
import ui.NetManager;
import ui.SelectedSettingsDialog;
import ui.BomSaver;

/**
 *
 * @author Albin Hjalmas.
 */
public final class DesignArea extends JComponent implements KeyListener, Serializable {

    // Observers that observe the current coordinate that the cursor is located at.
    private transient ArrayList<CoordinateListener> coordListeners;
    
    private String projName;
    private Grid grid; // The grid on this designarea.
    private ArrayList<Part> parts; // The parts on this designarea.
    private ArrayList<Net> nets;

    // Routing and selection variables
    private ArrayList<Pad> selectedPads; // Currently selected pads.
    private ArrayList<Segment> selectedSegments; // Currently selected segments.
    private ArrayList<Part> selectedParts; // Currently selected components.
    private Pad prevPad; // Previously attached pad
    private Pad currPad; // Currently attached pad
    private boolean isPadAttached; // Only true when currPad contains a viable pad
    private Point2D.Double moveOrigin; // Point pressed when initiating a drag
    private Component currPart; // Currently attached component, used when placing component

    // Cursor states
    public static final int CURSOR_NORMAL = 0; // Cursor can select and edit settings
    public static final int CURSOR_MOVE = 1; // Cursor cam move part around by pressing and dragging
    public static final int CURSOR_ROUTE = 2; // Cursor can connect pads belonging to the same net
    private int cursorState; // Currently selected cursor state.

    // Keycodes
    public static final int KEY_CTRL = 17;
    public static final int KEY_ESC = 27;
    public static final int KEY_DEL = 127;

    // Key press state variables
    private boolean ctrlPressed;

    // Default settings
    private double traceWidth, ringDiam, holeDiam, compSpacing;

    /**
     * Constructor.
     *
     * @param width The width of this design area in pixels.
     * @param height The height of this design area in pixels.
     */
    public DesignArea(String projName, int width, int height) {
        grid = new Grid(0.254, Color.BLACK, Color.GRAY, width, height);
        cursorState = 0;
        coordListeners = new ArrayList<>();
        selectedPads = new ArrayList<>();
        selectedSegments = new ArrayList<>();
        selectedParts = new ArrayList<>();
        isPadAttached = false;
        ctrlPressed = false;
        moveOrigin = new Point2D.Double(0, 0);
        parts = new ArrayList<>();
        nets = new ArrayList<>();
        traceWidth = 0.254;
        ringDiam = 1.5;
        holeDiam = 0.8;
        compSpacing = 0.254;
        this.projName = projName;

        // Set the size of this component
        super.setPreferredSize(new Dimension(width, height));

        // Add mouseListeners
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePressedImpl(e);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                mouseReleasedImpl(e);
            }
        });
        
        super.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseDraggedImpl(e);
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseMovedImpl(e);
            }
        });
    }

    /**
     * Add a CoordinateListener. A CoordinateListener listens for changes in the
     * mouseCoordinate relative to a coordinate system.
     *
     * @param c the coordinateListener to add.
     */
    public void addCoordinateListener(CoordinateListener c) {
        if (coordListeners == null) {
            coordListeners = new ArrayList<>();
        }
        
        coordListeners.add(c);
    }

    /**
     * Removes one CoordinateListener from this DesignArea.
     *
     * @param c the CoordinateListener to remove.
     */
    public void removeCoordinateListener(CoordinateListener c) {
        coordListeners.remove(c);
    }

    /**
     * Set the cursor action state of the design area. Based on what state is
     * currently selected the design area will perform different tasks when
     * pressing the mouse buttons over the design area.
     *
     * @param state can be: CURSOR_NORMAL, CURSOR_MOVE or CURSOR_ROUTE.
     */
    public void setCursorState(int state) {

        // Make shure that the current pad attached to the mouse
        // gets removed if we are changing the cursor state
        // While routing pcb traces.
        if (isPadAttached && state == CURSOR_ROUTE) {
            currPad.getNet().removeSegment(prevPad, currPad);
            isPadAttached = false;
            repaint();
        }
        
        cursorState = state;
    }

    /**
     * Returns the current cursor state.
     *
     * @return
     */
    public int getCursorState() {
        return cursorState;
    }

    /**
     * Places a part onto this designarea.
     *
     * @param part the part to place.
     */
    public void placePart(Part part) {
        if (cursorState != CURSOR_NORMAL) {
            return;
        }
        
        currPart = part;
        parts.add(part);
    }

    /**
     * Shows the default settings associated with this designArea.
     */
    public void showDefaultSettingsDialog() {
        DefaultSettingsDialog defaultSettings = new DefaultSettingsDialog(null, true,
                traceWidth, ringDiam, holeDiam, compSpacing);
        defaultSettings.setLocationRelativeTo(null);
        defaultSettings.setVisible(true);
        traceWidth = defaultSettings.getTraceWidth();
        ringDiam = defaultSettings.getRingDiam();
        holeDiam = defaultSettings.getHoleDiam();
        compSpacing = defaultSettings.getCompSpacing();
    }

    /**
     * Shows the selected settings dialog.
     */
    public void showSelectedSettingsDialog() {
        SelectedSettingsDialog selectedSettings = new SelectedSettingsDialog(null, true);
        selectedSettings.setLocationRelativeTo(null);
        selectedSettings.setVisible(true);
        
        if (selectedSettings.wasCancelled()) {
            return;
        }
        
        if (selectedSettings.getRingDiam() > 0
                && selectedSettings.getHoleDiam() > 0) {
            for (Pad pad : selectedPads) {
                if (pad instanceof ThPad) {
                    ((ThPad) pad).setRingDiam(selectedSettings.getRingDiam() * 50);
                    ((ThPad) pad).setHoleDiam(selectedSettings.getHoleDiam() * 50);
                }
            }
        }
        
        if (selectedSettings.getTraceWidth() > 0) {
            for (Segment seg : selectedSegments) {
                seg.setWidth(selectedSettings.getTraceWidth() * 50);
            }
        }
        
        repaint();
    }

    /**
     * Displays the netmanager.
     */
    public void showNetManager() {
        NetManager nm = new NetManager(parts, nets);
        nm.setLocationRelativeTo(null);
        nm.setVisible(true);
    }

    /**
     * Gives the user a choice to save a BOM - "Bill Of Materials".
     */
    public void saveBOM() {
        BomSaver bs = new BomSaver();
        bs.createBom(parts, projName);
        bs.setLocationRelativeTo(null);
        bs.setVisible(true);
    }

    /**
     * Checks the specified component distance spacing. P.S. Very nasty looking
     * code, did not have the time to "Divide and conquer"
     */
    public void runDesignRuleCheck() {
        
        boolean wasError = false;

        // Look for parts that are to close together
        ArrayList<Part> currParts = new ArrayList<>(parts);
        while (!currParts.isEmpty()) {
            Part currPart = currParts.remove(0);
            for (Part part : currParts) {
                if (isOverlapping(currPart.getPaddedOutline(compSpacing * 50),
                        part.getPaddedOutline(0))) {
                    wasError = true;
                    part.setSelected(true);
                    currPart.setSelected(true);
                    selectedParts.add(part);
                    selectedParts.add(currPart);
                    repaint();
                    JOptionPane.showMessageDialog(null, "Design rule violation detected!\n"
                            + "Part: " + currPart.toString() + " And Part: " + part.toString(),
                            " is to close together", JOptionPane.WARNING_MESSAGE);
                    part.setSelected(false);
                    currPart.setSelected(false);
                    selectedParts.remove(part);
                    selectedParts.remove(currPart);
                }
            }
        }

        // Look for segments and pads that are spaced to close together
        ArrayList<Net> currNets = new ArrayList<>(nets);
        while (!currNets.isEmpty()) {
            Net currNet = currNets.remove(0);
            ArrayList<Pad> currPads = new ArrayList<>(currNet.getPads());
            ArrayList<Segment> currSegments = new ArrayList<>(currNet.getSegments());
            while (!currSegments.isEmpty()) {
                Segment currSeg = currSegments.remove(0);
                for (Net net : currNets) {
                    for (Pad pad : net.getPads()) {
                        if (!pad.getVisible()) {
                            continue;
                        }
                        if (isOverlapping(currSeg.getPaddedOutline(compSpacing * 50),
                                pad.getPaddedOutline(0.0))) {
                            wasError = true;
                            pad.setSelected(true);
                            currSeg.setSelected(true);
                            selectedPads.add(pad);
                            selectedSegments.add(currSeg);
                            repaint();
                            JOptionPane.showMessageDialog(null, "Design rule violation detected!\n"
                                    + "Segment: " + currSeg.toString() + " And Pad: " + pad.toString(),
                                    " is to close together", JOptionPane.WARNING_MESSAGE);
                            pad.setSelected(false);
                            currSeg.setSelected(false);
                            selectedPads.remove(pad);
                            selectedSegments.remove(currSeg);
                        }
                    }
                    
                    for (Segment seg : net.getSegments()) {
                        if (isOverlapping(currSeg.getPaddedOutline(compSpacing * 50),
                                seg.getPaddedOutline(0.0))) {
                            wasError = true;
                            seg.setSelected(true);
                            currSeg.setSelected(true);
                            selectedSegments.add(seg);
                            selectedSegments.add(currSeg);
                            repaint();
                            JOptionPane.showMessageDialog(null, "Design rule violation detected!\n"
                                    + "Segment: " + currSeg.toString() + " And Segment: " + seg.toString(),
                                    " is to close together", JOptionPane.WARNING_MESSAGE);
                            seg.setSelected(false);
                            currSeg.setSelected(false);
                            selectedSegments.remove(seg);
                            selectedSegments.remove(currSeg);
                        }
                    }
                }
            }
            
            while (!currPads.isEmpty()) {
                Pad currPad = currPads.remove(0);
                if (!currPad.getVisible()) {
                    continue;
                }
                
                for (Net net : currNets) {
                    for (Pad pad : net.getPads()) {
                        if (isOverlapping(currPad.getPaddedOutline(compSpacing * 50),
                                pad.getPaddedOutline(0.0))) {
                            wasError = true;
                            pad.setSelected(true);
                            currPad.setSelected(true);
                            selectedPads.add(pad);
                            selectedPads.add(currPad);
                            repaint();
                            JOptionPane.showMessageDialog(null, "Design rule violation detected!\n"
                                    + "Pad: " + currPad.toString() + " And Pad: " + pad.toString(),
                                    " is to close together", JOptionPane.WARNING_MESSAGE);
                            pad.setSelected(false);
                            currPad.setSelected(false);
                            selectedPads.remove(pad);
                            selectedPads.remove(currPad);
                        }
                    }
                    
                    for (Segment seg : net.getSegments()) {
                        if (isOverlapping(currPad.getPaddedOutline(compSpacing * 50),
                                seg.getPaddedOutline(0.0))) {
                            wasError = true;
                            seg.setSelected(true);
                            currPad.setSelected(true);
                            selectedSegments.add(seg);
                            selectedPads.add(currPad);
                            repaint();
                            JOptionPane.showMessageDialog(null, "Design rule violation detected!\n"
                                    + "Pad: " + currPad.toString() + " And Segment: " + seg.toString(),
                                    " is to close together", JOptionPane.WARNING_MESSAGE);
                            seg.setSelected(false);
                            currPad.setSelected(false);
                            selectedSegments.remove(seg);
                            selectedPads.remove(currPad);
                        }
                    }
                }
            }
        }
        
        if (!wasError) {
            JOptionPane.showMessageDialog(null, "No design rule violations found!!",
                    "DRC Check Done", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Checks if the specified shapes a and b is overlapping eachother.
     *
     * @param a shape a
     * @param b shape b
     * @return true if a and ab overlaps else false.
     */
    private boolean isOverlapping(Shape a, Shape b) {
        Area A = new Area(a);
        Area B = new Area(b);
        A.intersect(B);
        return !A.isEmpty();
    }

    /**
     * Sets the project name.
     *
     * @param name the new name.
     */
    public void setProjectName(String name) {
        projName = name;
    }
    
    @Override
    public void paint(Graphics g) {
        paintComponent(g);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // First paint the grid
        grid.paint(g2, ((JViewport) getParent().getParent()).getViewRect());

        // Draw connection nets.
        for (Net n : nets) {
            if (n.isSelected()) {
                n.drawSelected(g2);
            } else {
                n.draw(g2);
            }
        }

        // Draw components
        for (Component comp : parts) {
            comp.draw(g2);
        }

        // Draw selected segments
        for (Segment seg : selectedSegments) {
            seg.drawSelected(g2);
        }

        // Draw selected components
        for (Part part : selectedParts) {
            part.drawSelected(g2);
        }

        // Draw selected pads
        for (Pad p : selectedPads) {
            p.drawSelected(g2);
        }
        
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        
        int keyCode = e.getExtendedKeyCode();
        
        switch (keyCode) {
            case KEY_CTRL:
                ctrlPressed = true;
                break;
            case KEY_DEL:
                for (Pad pad : selectedPads) {
                    pad.getNet().removePad(pad);
                }
                selectedPads.clear();
                for (Segment seg : selectedSegments) {
                    seg.getNet().removeSegment(seg);
                }
                selectedSegments.clear();
                for (Component comp : selectedParts) {
                    // Remove all pads from the net
                    for (Pad pad : ((Part) comp).getFootprint().getPads()) {
                        if (pad.getNet() != null) {
                            pad.getNet().removePad(pad);
                        }
                        
                    }
                    parts.remove(comp);
                }
                selectedParts.clear();
                break;
            case KEY_ESC:
                if (isPadAttached && cursorState == CURSOR_ROUTE) { // Abort current route.
                    currPad.getNet().removeSegment(prevPad, currPad);
                    currPad.getNet().setSelected(false);
                    prevPad = null;
                    currPad = null;
                    isPadAttached = false;
                }
                break;
        }
        
        repaint();
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getExtendedKeyCode() == 17) { // Ctrl was released
            ctrlPressed = false;
        }
    }

    /**
     *
     * @param e
     */
    public void mousePressedImpl(MouseEvent e) {
        Point2D.Double p = new Point2D.Double(e.getPoint().x, e.getPoint().y);

        // If right click was detected
        if (e.getButton() == MouseEvent.BUTTON3) {
            for (Component comp : parts) {
                if (comp.contains(new Point2D.Double(e.getX(), e.getY()))) {
                    ((Part) comp).showContextMenu(e);
                    ActionListener al = new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            repaint();
                            ((Part) comp).removeActionListener(this);
                        }
                    };
                    ((Part) comp).addActionListener(al);
                }
            }
        }
        
        switch (cursorState) {
            case CURSOR_ROUTE:
                if (isPadAttached) {
                    currPad.getNet().removeSegment(prevPad, currPad);
                    if (currPad.getNet().contains(p)) {
                        Component c = currPad.getNet().getUnderlyingComponent(p);
                        if (c instanceof Pad) {
                            currPad.getNet().addSegment(prevPad, (Pad) c,
                                    traceWidth * 50, Color.GREEN);
                            currPad.getNet().setSelected(false);
                            isPadAttached = false;
                        }
                    } else {
                        currPad.getNet().addSegment(prevPad, currPad,
                                traceWidth * 50, Color.GREEN);
                        prevPad = currPad;

                        // Begin routing new segment
                        currPad = new ThPad(currPad.getP(), traceWidth * 50, 0, Color.GREEN, false);
                        prevPad.getNet().addSegment(prevPad, currPad,
                                traceWidth * 50, Color.GREEN);
                    }
                    
                } else {
                    for (Net net : nets) {
                        if (net.contains(p)) {
                            Component c = net.getUnderlyingComponent(p);
                            if (c instanceof Pad) {
                                prevPad = (Pad) c;
                                currPad = ((Pad) c).clone();
                                currPad.setVisible(false);
                                net.addSegment(prevPad, currPad, traceWidth * 50, Color.GREEN);
                                net.setSelected(true);
                                isPadAttached = true;
                            }
                        }
                    }
                }
                break;
            case CURSOR_MOVE:
                moveOrigin.x = p.x;
                moveOrigin.y = p.y;
            case CURSOR_NORMAL:

                // Place component
                if (currPart != null) {
                    currPart = null;
                    return;
                }

                // Run through connection net
                for (Net net : nets) {
                    Component c = net.getUnderlyingComponent(p);
                    
                    if (c instanceof Pad && ((Pad) c).getPart() == null) {
                        if (!ctrlPressed && cursorState != CURSOR_MOVE) {
                            for (Pad pad : selectedPads) { // Unselect selected pads
                                pad.setSelected(false);
                            }
                            selectedPads.clear();
                        }
                        c.setSelected(true);
                        if (selectedPads.contains((Pad) c)) { // Dont add the same pad twice
                            selectedPads.remove((Pad) c);
                        }
                        selectedPads.add((Pad) c);
                        repaint();
                        return;
                    } else if (c instanceof Segment) {
                        if (!ctrlPressed && cursorState != CURSOR_MOVE) {
                            for (Segment seg : selectedSegments) { // Unselect selected pads
                                seg.setSelected(false);
                            }
                            selectedSegments.clear();
                        }
                        c.setSelected(true);
                        if (selectedSegments.contains((Segment) c)) {
                            selectedSegments.remove((Segment) c);
                        }
                        selectedSegments.add((Segment) c);
                        repaint();
                        return;
                    }
                }

                // Go through all parts (not pads or segments)
                for (Part part : parts) {
                    // Skip if component does not contain the mouse point.
                    if (!part.contains(p)) {
                        continue;
                    }
                    
                    if (!ctrlPressed && cursorState != CURSOR_MOVE) {
                        for (Component part1 : selectedParts) {
                            part1.setSelected(false);
                        }
                        selectedParts.clear();
                    }
                    part.setSelected(true);
                    if (selectedParts.contains(part)) {
                        selectedParts.remove(part);
                    }
                    selectedParts.add(part);
                    repaint();
                    return;
                }

                // This is only reached if the mouse was pressed outside of any components
                for (Segment seg : selectedSegments) { // Unselect selected segments
                    seg.setSelected(false);
                }
                selectedSegments.clear();
                for (Part part : selectedParts) { // Unselect selected parts
                    part.setSelected(false);
                }
                selectedParts.clear();
                for (Pad pad : selectedPads) { // Unselect selected pads
                    pad.setSelected(false);
                }
                selectedPads.clear();
                
                break;
            
            default:
                break;
        }
        
        repaint();
    }

    /**
     *
     * @param e
     */
    public void mouseReleasedImpl(MouseEvent e) {
        switch (cursorState) {
            case CURSOR_ROUTE:
                break;
            case CURSOR_MOVE:
                if (isPadAttached) {
                    isPadAttached = false;
                    currPad.getNet().setSelected(false);
                    currPad = null;
                    prevPad = null;
                }
                break;
            case CURSOR_NORMAL:
                break;
            default:
                break;
        }
        
        repaint();
    }

    /**
     *
     * @param e
     */
    public void mouseDraggedImpl(MouseEvent e) {
        Point p = e.getPoint();
        Point2D.Double mousePos = new Point2D.Double(p.x, p.y);

        // Notify all CoordinateListeners
        for (CoordinateListener c : coordListeners) {
            c.CoordinateChanged(grid.p2gC(mousePos));
        }
        
        switch (cursorState) {
            case CURSOR_ROUTE:
                break;
            case CURSOR_MOVE:
                Point2D.Double delta = grid.getClosestGP(mousePos);
                Point2D.Double currPos = grid.getClosestGP(moveOrigin);
                delta.x -= currPos.x;
                delta.y -= currPos.y;
                moveOrigin.x += delta.x;
                moveOrigin.y += delta.y;

                // Keeps track of pads that have been translated.
                // in case that a selected pad belongs to a segment.
                ArrayList<Pad> translated = new ArrayList<>();

                // Translate selected pads.
                for (Pad pad : selectedPads) {
                    pad.translate(delta);
                    translated.add(pad);
                }

                // Translate selected segments.
                for (Segment seg : selectedSegments) {

                    // Should not translate a pad that belongs to a component
                    if ((((Pad) seg.getP0()).getPart() != null
                            || ((Pad) seg.getP1()).getPart() != null)) {
                        return;
                    }
                    
                    if (translated.contains(seg.getP0())
                            && !translated.contains(seg.getP1())) {
                        seg.getP1().translate(delta);
                        translated.add((Pad) seg.getP1());
                        
                    } else if (translated.contains(seg.getP1())
                            && !translated.contains(seg.getP0())) {
                        seg.getP0().translate(delta);
                        translated.add((Pad) seg.getP0());
                        
                    } else if (!(translated.contains(seg.getP1())
                            || translated.contains(seg.getP0()))) {
                        seg.translate(delta);
                        translated.add((Pad) seg.getP0());
                        translated.add((Pad) seg.getP1());
                    }
                }

                // Translate selected components
                for (Component comp : selectedParts) {
                    comp.translate(delta);
                }
                
                break;
            case CURSOR_NORMAL:
                break;
            default:
                break;
        }
        
        repaint();
    }

    /**
     *
     * @param e
     */
    public void mouseMovedImpl(MouseEvent e) {
        Point p = e.getPoint();
        Point2D.Double mousePos = new Point2D.Double(p.x, p.y);

        // Notify all CoordinateListeners
        for (CoordinateListener c : coordListeners) {
            c.CoordinateChanged(grid.p2gC(grid.getClosestGP(mousePos)));
        }
        
        switch (cursorState) {
            case CURSOR_ROUTE:
                if (isPadAttached) {
                    currPad.setP(grid.getClosestGP(mousePos));
                }
                break;
            case CURSOR_MOVE:
                break;
            case CURSOR_NORMAL:
                if (currPart != null) {
                    currPart.setP(grid.getClosestGP(mousePos));
                }
                break;
            default:
                break;
        }
        
        repaint();
    }
}
