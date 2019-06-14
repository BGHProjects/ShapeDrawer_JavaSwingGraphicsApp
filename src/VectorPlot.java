import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Represents a dot in vector coordinates
 */
public class VectorPlot implements VectorShape {

    private double x;
    private double y;

    /**
     * Constructs a new VectorPlot
     * @param x X coordinate
     * @param y Y coordinate
     */
    VectorPlot(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Converts the VectorPlot to its command equivalent
     * @return A string representation of the VectorPlot
     */
    @Override
    public String toString() { return String.format("PLOT %f %f", x, y); }

    // The VectorPlot is not resizable, this is an oversight.
    @Override
    public void resize(Point start, Point now, Dimension windowSize) {

    }

    /**
     * Given the current Dimension of the Canvas, returns a scaled Line2D.Double
     * that ends where it starts, creating a dot.
     *
     * @param windowSize Dimension of the Canvas
     * @return A Line2D.Double object
     */
    @Override
    public Shape toShape(Dimension windowSize) {
        double screenHeight = windowSize.getHeight()-1;
        double screenWidth = windowSize.getWidth()-1;

        double x = this.x * screenWidth;
        double y = this.y * screenHeight;

        return (new Line2D.Double(x, y, x, y));
    }
}
