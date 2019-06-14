import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Represents an ellipse with vector coordinates
 */
public class VectorEllipse implements VectorShape {

    private double x1;
    private double y1;
    private double x2;
    private double y2;

    /**
     * Constructs a new VectorEllipse
     * @param x1 The X coordinate of the starting position
     * @param y1 The Y coordinate of the starting position
     * @param x2 The X coordinate of the ending position
     * @param y2 The Y coordinate of the ending position
     */

    VectorEllipse(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Converts the VectorEllipse to its command equivalent
     * @return A string representation of the VectorEllipse
     */
    @Override
    public String toString() { return String.format("ELLIPSE %f %f %f %f", x1, y1, x2, y2); }

    /**
     * Given the parameters, resizes the Shape.
     *
     * @param start - The point where the user first started drawing
     * @param now - The point where the users mouse is currently
     * @param windowSize - The current Dimension of the Canvas
     */
    @Override
    public void resize(Point start, Point now, Dimension windowSize) {
        double width = windowSize.getWidth()-1;
        double height = windowSize.getHeight()-1;
        double startX = start.getX() / width;
        double startY = start.getY() / height;
        double nowX = now.getX() / width;
        double nowY = now.getY() / height;
        x1 = Math.min(startX, nowX);
        y1 = Math.min(startY, nowY);
        x2 = Math.max(startX, nowX);
        y2 = Math.max(startY, nowY);
    }


    /**
     * Given the current Dimension of the Canvas, returns a scaled Ellipse2D.Double
     *
     * @param windowSize Dimension of the Canvas
     * @return An Ellipse2D.Double object
     */
    @Override
    public Shape toShape(Dimension windowSize) {
        double screenHeight = windowSize.getHeight()-1;
        double screenWidth = windowSize.getWidth()-1;

        double _x1 = x1 * screenWidth;
        double _y1 = y1 * screenHeight;
        double w = (x2 - x1) * screenWidth;
        double h = (y2 - y1) * screenHeight;

        return (new Ellipse2D.Double(_x1, _y1, w, h));
    }
}
