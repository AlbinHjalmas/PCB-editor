
package graphPrimitives;

import java.util.ArrayList;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Albin Hjalmas.
 */
public abstract class Vertex implements Component {
    private ArrayList<ChangeListener> listeners; // observers
    private boolean selectionState;
    
    /**
     * Constructor.
     */
    public Vertex() {
        listeners = new ArrayList<>();
        selectionState = false;
    }
    
    /**
     * Add a listener to this vertex.
     * @param l 
     */
    public void addListener(ChangeListener l) {
        listeners.add(l);
    }
    
    /**
     * Remove the specified listener from this vertex.
     * @param l the listener to remove.
     * @return true if successful else false.
     */
    public boolean removeListener(ChangeListener l) {
        return listeners.remove(l);
    }
    
    /**
     * Returns a list of listeners.
     * @return list of listeners.
     */
    public ArrayList<ChangeListener> getListeners() {
        return listeners;
    }
    
    @Override
    public void setSelected(boolean state) {
        selectionState = state;
    }
    
    @Override
    public boolean isSelected() {
        return selectionState;
    }
}
