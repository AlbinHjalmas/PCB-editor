package pcbEditor;

import java.awt.geom.Point2D;

/**
 * An interface to enable classes to be notified upon the
 * change of mousePosition in an DesignArea.
 * @author Albin Hjalmas.
 */
public interface CoordinateListener {
    public void CoordinateChanged(Point2D.Double p);
}
