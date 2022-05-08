
package pcbPrimitives;

import graphPrimitives.Vertex;
import pcbEditor.Net;
import pcbPart.Part;

/**
 * 
 * @author Albin Hjalmas.
 */
public abstract class Pad extends Vertex implements Cloneable {
    
    private String name;
    private Part part; // A back reference to the part that this pad belongs to.
    
    /**
     * Set the name of this pad.
     * @param name the new name.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Get the name of this component.
     * @return the name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set the part that this pad is a part of.
     * @param part the part that this pad is a part of.
     */
    public void setPart(Part part) {
        this.part = part;
    }
    
    /**
     * returns the part that this pad is a part of.
     * @return the part that this pad belong to.
     */
    public Part getPart() {
        return part;
    }
    
    public abstract void setVisible(boolean state);
    public abstract boolean getVisible();
    public abstract void setNet(Net net);
    public abstract Net getNet();
    @Override
    public abstract Pad clone();
    
    @Override
    public String toString() {
        return name;
    }
}
