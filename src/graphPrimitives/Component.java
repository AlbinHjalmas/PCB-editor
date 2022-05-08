
package graphPrimitives;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 *
 * @author Albin Hjalmas.
 */
public interface Component extends Serializable {
    
    /**
     * Set the selection state of this Component.
     * @param state new state.
     */
    void setSelected(boolean state);
    
    /**
     * Test if this component is selected.
     * @return true if selected else false.
     */
    boolean isSelected();
    
    /**
     * Draw this component.
     * @param g 
     */
    void draw(Graphics2D g);
    
    /**
     * Draws the selected version of this component.
     * @param g 
     */
    void drawSelected(Graphics2D g);
    
    /**
     * Translate this component the specified amount.
     * @param delta translation vector.
     */
    void translate(Point2D.Double delta);
    
    /**
     * Test if this component contains the specified point.
     * @param p the point to test with.
     * @return true if this component contains the specified point else false.
     */
    boolean contains(Point2D.Double p);
    
    /**
     * If the composit pattern is used in the application then it can be useful
     * to get the subcomponent to this at the specified point.
     * @param p the point to test with.
     * @return a component at the specified point that is a child to this 
     * component.
     */
    Component getUnderlyingComponent(Point2D.Double p);
    
    /**
     * Sets the color of this component.
     * @param color the new color.
     */
    void setColor(Color color);
    
    /**
     * Gets the color of this component.
     * @return the color.
     */
    Color getColor();
    
    /**
     * Get the current position of this component.
     * @return current position.
     */
    Point2D.Double getP(); 
    
    /**
     * Set the location of this component.
     * @param p the new location.
     */
    void setP(Point2D.Double p);
    
    /**
     * Gets the outline of this component with the added padding.
     * @param padding the amount of padding in perpendicular direction to
     * the component outline.
     * @return a padded version of this components outline.
     */
    Shape getPaddedOutline(double padding);
}
