import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Represents a line with vector coordinates
 */
public class VectorLine implements VectorShape {

    private double x1;
    private double y1;
    private double x2;
    private double y2;

    /**
     * Constructs a new VectorLine
     * @param x1 The X coordinate for the starting position
     * @param y1 The Y coordinate for the starting position
     * @param x2 The X coordinate for the ending position
     * @param y2 The Y coordinate for the ending position
     */

    VectorLine(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Converts the VectorLine to its command equivalent
     * @return A string representation of the VectorLine
     */
    @Override
    public String toString() { return String.format("LINE %f %f %f %f", x1, y1, x2, y2); }

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
        double nowX = now.getX() / width;
        double nowY = now.getY() / height;
        x2 = nowX;
        y2 = nowY;
    }


    /**
     * Given the current Dimension of the Canvas, returns a scaled Line2D.Double
     *
     * @param windowSize Dimension of the Canvas
     * @return A Line2D.Double object
     */
    @Override
    public Shape toShape(Dimension windowSize) {
        double screenHeight = windowSize.getHeight()-1;
        double screenWidth = windowSize.getWidth()-1;

        double _x1 = x1 * screenWidth;
        double _y1 = y1 * screenHeight;
        double _x2 = x2 * screenWidth;
        double _y2 = y2 * screenHeight;

        return (new Line2D.Double(_x1, _y1, _x2, _y2));
    }
}
