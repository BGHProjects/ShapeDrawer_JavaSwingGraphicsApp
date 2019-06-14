import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

/**
 * Represents a polygon with vector coordinates
 */
public class VectorPolygon implements VectorShape {

    private ArrayList<double[]> lines;

    /**
     * Constructs a new VectorPolygon
     * @param lines - A list of x,y pairs
     */
    VectorPolygon(ArrayList<double[]> lines) {
        this.lines = lines;
    }

    /**
     * Converts the VectorPolygon to its command equivalent
     * @return A string representation of the VectorPolygon
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("POLYGON ");

        for (double[] xyPair : lines) {
            sb.append(xyPair[0]);
            sb.append(" ");
            sb.append(xyPair[1]);
            sb.append(" ");
        }

        return sb.toString();
    }

    // Drawing with mouse not implemented
    @Override
    public void resize(Point start, Point now, Dimension windowSize) {
    }

    /**
     * Given the current Dimension of the Canvas, returns a scaled Path2D.Double
     * @param windowSize Dimension of the Canvas
     * @return A Path2D.Double
     */
    @Override
    public Shape toShape(Dimension windowSize) {
        double windowWidth = windowSize.getWidth()-1;
        double windowHeight = windowSize.getHeight()-1;
        Path2D.Double path = new Path2D.Double();

        double[] firstXYPair = lines.get(0); // Get first xy pair
        path.moveTo(firstXYPair[0] * windowWidth, firstXYPair[1] * windowHeight); // Move to start of polygon with moveTo()

        for (int i=1; i<lines.size(); i++) { // Draw the rest of the polygon with lineTo()
            double[] xyPair = lines.get(i);
            path.lineTo(xyPair[0] * windowWidth, xyPair[1] * windowHeight);
        }

        path.lineTo(firstXYPair[0] * windowWidth, firstXYPair[1] * windowHeight); // Path back to start

        return path;
    }

}
