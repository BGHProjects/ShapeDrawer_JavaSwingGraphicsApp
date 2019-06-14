import java.awt.*;

/**
 * Represents a VectorShape and its associated stroke colour and fill color
 */
class ColouredVectorShape {
    private VectorShape shape;
    private Color stroke;
    private boolean fill;
    private Color fillColor;

    /**
     * Constructs a ColouredVectorShape
     * @param shape - Any class implementing VectorShape
     * @param stroke - The stroke colour
     * @param fill - Boolean determines whether the shape should be filled when drawn
     * @param fillColor - The fill colour
     */
    ColouredVectorShape(VectorShape shape, Color stroke, boolean fill, Color fillColor) {
        this.shape = shape;
        this.stroke = stroke;
        this.fill = fill;
        this.fillColor = fillColor;
    }

    /**
     * Sets this.VectorShape to the supplied VectorShape
     * @param vectorShape - The new VectorShape
     */
    void setVectorShape(VectorShape vectorShape) { this.shape = vectorShape; }

    // Getters
    VectorShape getVectorShape() { return shape; }
    Color getStroke() { return stroke; }
    boolean fill() { return fill; }
    Color getFillColor() { return fillColor; }

    /**
     * Returns the PEN command for the shapes fill color
     * @return
     */
    String getPenCmd () { return String.format("PEN %s", colorToHex(stroke)); }

    /**
     * Returns the FILL command for the shapes fill color
     * @return String
     */
    String getFillCmd() { return String.format("FILL %s", colorToHex(fillColor)); }


    /**
     * Returns the ColouredVectorShapes stroke colour as a hex string
     * @return String
     */
    String getStrokeHex() { return colorToHex(stroke); }

    /**
     * Returns the ColouredVectorShapes fill colour as a hex string
     * @return String
     */
    String getFillHex() { return colorToHex(fillColor); }

    /**
     * Helper function that converts a Color object to a HEX string
     * @param color - The Color to convert to a HEX string
     * @return String
     */
    static String colorToHex(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        return String.format("#%02x%02x%02x", r, g, b);
    }
}
