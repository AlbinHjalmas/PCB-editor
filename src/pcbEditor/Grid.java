package pcbEditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * This class defines a grid. can be used to resolve discrete points based on a
 * grid. also has methods to paint the grid onto a surface.
 *
 * @author Albin Hjalmas.
 */
public class Grid implements Serializable {

    private final int width, height; // The width and height of this grid.
    private final double spacing; // The distance between grid lines.
    private final Color background, lineColor; // Colors of the grid.
    private final Point2D.Double o; // Origin relative to parent.
    private transient Stroke defStroke;

    /**
     * Constructor.
     *
     * @param spacing the space between grid lines.
     * @param background the background color.
     * @param lineColor the color of the grid lines.
     * @param width
     * @param height
     */
    public Grid(double spacing, Color background,
            Color lineColor, int width, int height) {
        this.spacing = spacing;
        this.background = background;
        this.lineColor = lineColor;
        this.width = width;
        this.height = height;

        // Set origin
        o = new Point2D.Double(25, height - 25);
    }

    public void paint(Graphics2D g, Rectangle view) {
        g.setBackground(background);

        // Clear background
        g.clearRect(view.x, view.y, view.width, view.height);

        // Get the lower left corner of the view with regards to the discrete
        // gridpoints. 
        Point2D.Double wp = getClosestGP(view.x, view.y + view.height);
        wp = p2g(wp);

        // draw grid line markers
        g.setColor(lineColor);
        for (double x = wp.x; x < (wp.x + view.width); x += (spacing * 50.0)) {
            for (double y = wp.y; y < (wp.y + view.height); y += (spacing * 50.0)) {
                g.draw(new Line2D.Double(g2p(x - 2, y), g2p(x + 2, y)));
                g.draw(new Line2D.Double(g2p(x, y - 2), g2p(x, y + 2)));
            }
        }

        // Draw origin
        defStroke = g.getStroke();
        BasicStroke stroke = new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g.setStroke(stroke);
        g.setColor(Color.WHITE);
        g.draw(new Line2D.Double(g2p(-5, 0), g2p(5, 0)));
        g.draw(new Line2D.Double(g2p(0, -5), g2p(0, 5)));
        g.setStroke(defStroke);
    }

    /**
     * Returns the closest grid point to p. Returns a point on the parent that
     * corresponds to the grid point closest to p.
     *
     * @param p a point on the parent.
     * @return a point on the parent that corresponds to a discrete point in the
     * grid.
     */
    public Point2D.Double getClosestGP(Point2D.Double p) {
        Point2D.Double pg = p2g(p);

        int x = (int) (Math.round(pg.x / (spacing * 50)) * 50 * spacing);
        int y = (int) (Math.round(pg.y / (spacing * 50)) * 50 * spacing);

        return g2p(x, y);
    }

    /**
     * Returns the closest grid point to p. Returns a point on the parent that
     * corresponds to the grid point closest to p.
     *
     * @param p a point on the parent.
     * @return a point on the parent that corresponds to a discrete point in the
     * grid.
     */
    public Point2D.Double getClosestGP(double x, double y) {
        Point2D.Double pg = p2g(x, y);

        int nx = (int) (Math.round(pg.x / (spacing * 50)) * 50 * spacing);
        int ny = (int) (Math.round(pg.y / (spacing * 50)) * 50 * spacing);

        return g2p(nx, ny);
    }

    /**
     * Transform from point on grid CS to point on parent CS.
     *
     * @param p Point in the grid coordinate system.
     * @return corresponding point on parent coordinate system.
     */
    public Point2D.Double g2p(Point2D.Double p) {
        return new Point2D.Double(p.x + o.x, o.y - p.y);
    }

    /**
     * Transform from point on grid CS to point on parent CS.
     *
     * @param x grid x coordinate
     * @param y grid y coordinate
     * @return corresponding point on parent coordinate system.
     */
    public Point2D.Double g2p(double x, double y) {
        return new Point2D.Double(x + o.x, o.y - y);
    }

    /**
     * Transform from point in parent CS to grid CS.
     *
     * @param p point in parent coordinate system.
     * @return corresponding point in grid coordinate system.
     */
    public Point2D.Double p2gC(Point2D.Double p) {
        return new Point2D.Double((p.x - o.x) / 50.0, (o.y - p.y) / 50.0);
    }

    /**
     * Transform from point in parent CS to grid CS.
     *
     * @param p point in parent coordinate system.
     * @return corresponding point in grid coordinate system.
     */
    public Point2D.Double p2g(Point2D.Double p) {
        return new Point2D.Double(p.x - o.x, o.y - p.y);
    }

    /**
     * Transform from point in parent CS to grid CS.
     *
     * @param p point in parent coordinate system.
     * @return corresponding point in grid coordinate system.
     */
    public Point2D.Double p2g(double x, double y) {
        return new Point2D.Double(x - o.x, o.y - y);
    }
}
