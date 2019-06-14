import java.awt.*;

/**
 * Defines a VectorShape interface
 */
public interface VectorShape {
    void resize(Point start, Point now, Dimension windowSize);
    String toString();
    Shape toShape(Dimension windowSize);
}
