import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;


// Extends JPanel to override paintComponent
public class Canvas extends JPanel {
    private DrawCommands selectedVectorShape = DrawCommands.ELLIPSE; // The type of VectorShape to be drawn
    private Color selectedStrokeColour = null;
    private boolean fill = false;
    private Color selectedFillColour = null;
    private ColouredVectorShape currentColouredVectorShape;
    private ArrayList<ColouredVectorShape> undoneShapes = new ArrayList<>(); // Holds shapes removed with undo
    private ArrayList<ColouredVectorShape> colouredVectorShapes = new ArrayList<>();
    private AffineTransform at; // Reference to the AffineTransform used for zooming
    private double scaleFactor = 1.0;

    /**
     * Canvas constructor
     * Adds mouse listeners to the Canvas
     */
    Canvas() {
        CanvasMouseListener canvasMouseListener = new CanvasMouseListener();
        addMouseListener(canvasMouseListener);
        addMouseMotionListener(canvasMouseListener);
    }


    // Setters
    void setSelectedStrokeColour (Color color) { selectedStrokeColour = color; }
    void setFill(boolean fill) { this.fill = fill; }
    void setSelectedFillColour(Color color) { selectedFillColour = color; }
    void setSelectedVectorShape(DrawCommands vectorShape) { selectedVectorShape = vectorShape; }

    // Getters
    boolean getFill() { return fill; }

    /**
     * Removes the last VectorShape in the list of VectorShapes to be drawn.
     * Effectively 'undoing' the last drawn shape.
     */
    void undoPainting() {
        // Avoid IndexOutOfRange exceptions
        if (colouredVectorShapes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nothing to undo.");
        } else {
            int size = colouredVectorShapes.size();
            undoneShapes.add(colouredVectorShapes.get(size-1)); // Add the undone VectorShape to a list for redo
            colouredVectorShapes.remove(size-1);
            repaint();
        }
    }


    /**
     * Adds the last 'undone' VectorShape back to the list of VectorShapes to be drawn
     */
    void redoPainting(){
        // Avoid IndexOutOfRange exceptions
        if (undoneShapes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nothing to redo.");
        } else {
            int size = undoneShapes.size();
            // Add the VectorShape back to the list of shapes to be drawn
            colouredVectorShapes.add(undoneShapes.get(size-1));
            // Then remove it from the list of shapes to be 'redone'
            undoneShapes.remove(size-1);
        }
    }


    /**
     * Clears the list of VectorShapes to be drawn, effectively wiping the canvas.
     */
    void clear() { colouredVectorShapes.clear(); }

    /**
     * Adjusts the zoom scale factor higher.
     */
    void adjustScaleIn(){
        if (scaleFactor + 0.5 <= 3.0){
            scaleFactor += 0.5;
        }
    }

    /**
     * Adjusts the zoom scale factor lower.
     */
    void adjustScaleOut() {
        scaleFactor -= 0.5;
        if (scaleFactor < 1.0) {
            scaleFactor = 1.0;
        }
    }

    /**
     * Overrides JPanel paintComponent method to "draw".
     * Iterates through 'cvshapes' (the list of ColouredVectorShapes to be drawn) and draws them on the canvas.
     * Also draws 'currentColouredVectorShape' which refers to the shape the user is currently drawing with the ouse.
     * @param g The Graphics object passed to the function
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        double width = getWidth();
        double height = getHeight();

        double zoomWidth = width * scaleFactor;
        double zoomHeight = height * scaleFactor;

        //get anchor point in the middle of the canvas for zoom
        double anchorx = (width - zoomWidth) / 2;
        double anchory = (height - zoomHeight) / 2;

        at = new AffineTransform();
        at.translate(anchorx, anchory);

        // configure the user-nominated scale-factor before drawing canvas elements.
        at.scale(scaleFactor, scaleFactor);

        g2d.setTransform(at);

        // configure AA so it looks nice.
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Iterate the list of ColouredVectorShapes and draw the,
        for (ColouredVectorShape cv : colouredVectorShapes) {
            Shape s = cv.getVectorShape().toShape(getSize()); // Shape to draw
            Color stroke = cv.getStroke(); // Stroke colour
            Color fillColor = cv.getFillColor(); // Fill colour
            boolean fill = cv.fill(); // Whether or not the ColouredVectorShape should be filled

            // Fill the shape with the specified fill color
            if (fill) {
                g2d.setPaint(fillColor);
                g2d.fill(s);
            }

            g2d.setPaint(stroke);
            g2d.draw(s);
        }

        // This draws the ColouredVectorShape the user is currently manipulating,
        // it produces a visual indicator of the shapes final size and position.

        if (currentColouredVectorShape != null) {
            // Get teh underlying VectorShape and convert to an awt Shape
            Shape shape = currentColouredVectorShape.getVectorShape().toShape(getSize());
            Color stroke = currentColouredVectorShape.getStroke();
            Color fillColor = currentColouredVectorShape.getFillColor();
            boolean fill = currentColouredVectorShape.fill();

            // Fill the shape with the specified fill color
            if (fill) {
                g2d.setPaint(fillColor);
                g2d.fill(shape);
            }

            g2d.setPaint(stroke);
            g2d.draw(shape);
        }

        repaint();
    }

    /**
     * Iterates the list of ColouredVectorShapes and converts them to a string
     * @return A list of VectorShape's in string form.
     */
    String[] getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        String prevStrokeHex = "#000000";
        String prevFillHex = ""; // default fill color
        boolean fillOff = true;

        for (ColouredVectorShape cvshape : colouredVectorShapes) {
            String strokeHex = cvshape.getStrokeHex(); // Get the shapes stroke color in hex
            String fillHex = cvshape.getFillHex(); // Get the shapes fill color in hex

            // If the shapes stroke colour is different to prevStrokeHex the stroke colour has changed
            if (!strokeHex.equals(prevStrokeHex)) {
                prevStrokeHex = strokeHex;
                commands.add(cvshape.getPenCmd()); // To the list of commands, add a PEN command with the new hex color
            }

            // If the shape should be filled set fillOff to false
            if (cvshape.fill()) {
                fillOff = false;
                // If the shapes fill colour is different to prevFillHex the fill colour has changed
                if (!fillHex.equals(prevFillHex)) {
                    prevFillHex = fillHex;
                    commands.add(cvshape.getFillCmd()); // To the list of commands, add a FILL command with the new fill color
                }
            } else if (!fillOff) { // Otherwise, turn fill off
                fillOff = true;
                commands.add("FILL OFF"); // To the list of commands, add a FILL OFF commaand
            }

            commands.add(cvshape.getVectorShape().toString());
        }
        return commands.toArray(new String[0]);
    }


    /**
     * Given a File chosen from the JMenubar, splits the information into its individual lines,
     * adds an appropriate shape to an ArrayList of commands to be drawn, including the different shapes
     * and the Pen and Fill colour, and alerts the user if there is an invalid command in the file
     * @param file The file that is selected via the menubar in the GUI
     * @return The ArrayList of String arrays that are the commands to be drawn on the canvas
     */

    ArrayList<String[]> loadVecFile(File file) throws IndexOutOfBoundsException, IllegalArgumentException, IOException {
        colouredVectorShapes.clear();
        ArrayList<String[]> commands = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();

        while (line != null) {
            commands.add(line.split(" "));
            line = br.readLine();
        }
        br.close();

        readCommands(commands);

        return commands;
    }

    /**
     * Iterates a list of commands and creates and adds the relevant VectorShape to the list of VectorShapes
     * to be drawn.
     * @param commands - An array list of commands
     * @throws IndexOutOfBoundsException - Thrown by the createX functions.
     * @throws IllegalArgumentException - Thrown when an unsuitable paramater is found, or by Enum.valueOf(), or by Color.decode()
     */
    private void readCommands(ArrayList<String[]> commands) throws IndexOutOfBoundsException, IllegalArgumentException {
        Color stroke = Color.BLACK;
        Color fillColor = Color.BLACK;
        boolean fill = false;

        for (String[] cmd : commands) {
            switch (DrawCommands.valueOf(cmd[0])) {
                case LINE:
                    createLine(cmd, stroke, fill, fillColor);
                    break;
                case PLOT:
                    createPlot(cmd, stroke, fill, fillColor);
                    break;
                case RECTANGLE:
                    createRectangle(cmd, stroke, fill, fillColor);
                    break;
                case ELLIPSE:
                    createEllipse(cmd, stroke, fill, fillColor);
                    break;
                case POLYGON:
                    createPolygon(cmd, stroke, fill, fillColor);
                    break;
                case PEN:
                    stroke = Color.decode(cmd[1]); // decode throws NumberFormatException
                    break;
                case FILL:
                    if (cmd[1].equals("OFF")) fill = false; // Ensure future shapes are not filled
                    else {
                        // Ensure future shapes are filled
                        fill = true;
                        fillColor = Color.decode(cmd[1]);
                    }
                    break;
            }
        }
        repaint();
    }

    /**
     * Instantiates a new ColouredVectorShape(VectorPolygon) and adds it to the list of shapes to be drawn.
     * @param cmd - A VEC command split into a String array
     * @param stroke - The stroke colour
     * @param fill - Boolean determining if the shape should be filled
     * @param fillColor - The colour to fill the shape with
     * @throws IndexOutOfBoundsException - Thrown when there are an incorrect number of parameters
     * @throws NumberFormatException - Thrown when Double.parse() fails
     */
    private void createPolygon(String[] cmd, Color stroke, boolean fill, Color fillColor) throws IndexOutOfBoundsException, NumberFormatException {
        ArrayList<double[]> params = new ArrayList<>();
        for (int i=1; i<cmd.length; i+=2) {
            double x = Double.parseDouble(cmd[i]);
            double y = Double.parseDouble(cmd[i+1]);
            if (x > 1.0 || x < 0.0 || y > 1.0 || y < 0.0) throw new IllegalArgumentException();
            params.add(new double[]{x, y});
        }
        VectorPolygon poly = new VectorPolygon(params);
        colouredVectorShapes.add(new ColouredVectorShape(poly, stroke, fill, fillColor));
    }

    /**
     * Instantiates a new ColouredVectorShape(VectorPlot) and adds it to the list of shapes to be drawn.
     * @param cmd - A VEC command split into a String array
     * @param stroke - The stroke colour
     * @param fill - Boolean determining if the shape should be filled
     * @param fillColor - The colour to fill the shape with
     * @throws IndexOutOfBoundsException - Thrown when there are an incorrect number of parameters
     * @throws NumberFormatException - Thrown when Double.parse() fails
     */
    private void createPlot(String[] cmd, Color stroke, boolean fill, Color fillColor) throws IndexOutOfBoundsException, IllegalArgumentException {
        double x = Double.parseDouble(cmd[1]);
        double y = Double.parseDouble(cmd[2]);

        // Check if any parameters are greater than 1.0 or less than 0.0
        if (x > 1.0 || y > 1.0 || x < 0.0 || y < 0.0) throw new IllegalArgumentException();

        VectorPlot p = new VectorPlot(x, y);
        colouredVectorShapes.add(new ColouredVectorShape(p, stroke, fill, fillColor));
    }

    /**
     * Instantiates a new ColouredVectorShape(VectorRectangle) and adds it to the list of shapes to be drawn.
     * @param cmd - A VEC command split into a String array
     * @param stroke - The stroke colour
     * @param fill - Boolean determining if the shape should be filled
     * @param fillColor - The colour to fill the shape with
     * @throws IndexOutOfBoundsException - Thrown when there are an incorrect number of parameters
     * @throws NumberFormatException - Thrown when Double.parse() fails
     */
    private void createRectangle(String[] cmd, Color stroke, boolean fill, Color fillColor) throws IndexOutOfBoundsException, IllegalArgumentException {
        double x1 = Double.parseDouble(cmd[1]);
        double y1 = Double.parseDouble(cmd[2]);
        double x2 = Double.parseDouble(cmd[3]);
        double y2 = Double.parseDouble(cmd[4]);

        // Check if any parameters are greater than 1.0 or less than 0.0
        if (x1 > 1.0 || y1 > 1.0 || x2 > 1.0 || y2 > 1.0) throw new IllegalArgumentException();
        if (x1 < 0.0 || y1 < 0.0 || x2 < 0.0 || y2 < 0.0) throw new IllegalArgumentException();

        VectorRectangle e = new VectorRectangle(x1, y1, x2, y2);
        colouredVectorShapes.add(new ColouredVectorShape(e, stroke, fill, fillColor));
    }

    /**
     * Instantiates a new ColouredVectorShape(VectorEllipse) and adds it to the list of shapes to be drawn.
     * @param cmd - A VEC command split into a String array
     * @param stroke - The stroke colour
     * @param fill - Boolean determining if the shape should be filled
     * @param fillColor - The colour to fill the shape with
     * @throws IndexOutOfBoundsException - Thrown when there are an incorrect number of parameters
     * @throws NumberFormatException - Thrown when Double.parse() fails
     */
    private void createEllipse(String[] cmd, Color stroke, boolean fill, Color fillColor) throws IndexOutOfBoundsException, IllegalArgumentException {
        double x1 = Double.parseDouble(cmd[1]);
        double y1 = Double.parseDouble(cmd[2]);
        double x2 = Double.parseDouble(cmd[3]);
        double y2 = Double.parseDouble(cmd[4]);

        // Check if any parameters are greater than 1.0 or less than 0.0
        if (x1 > 1.0 || y1 > 1.0 || x2 > 1.0 || y2 > 1.0) throw new IllegalArgumentException();
        if (x1 < 0.0 || y1 < 0.0 || x2 < 0.0 || y2 < 0.0) throw new IllegalArgumentException();

        VectorEllipse r = new VectorEllipse(x1, y1, x2, y2);
        colouredVectorShapes.add(new ColouredVectorShape(r, stroke, fill, fillColor));
    }

    /**
     * Instantiates a new ColouredVectorShape(VectorLine) and adds it to the list of shapes to be drawn.
     * @param cmd - A VEC command split into a String array
     * @param stroke - The stroke colour
     * @param fill - Boolean determining if the shape should be filled
     * @param fillColor - The colour to fill the shape with
     * @throws IndexOutOfBoundsException - Thrown when there are an incorrect number of parameters
     * @throws NumberFormatException - Thrown when Double.parse() fails
     */
    private void createLine(String[] cmd, Color stroke, boolean fill, Color fillColor) throws IndexOutOfBoundsException, IllegalArgumentException{
        double x1 = Double.parseDouble(cmd[1]);
        double y1 = Double.parseDouble(cmd[2]);
        double x2 = Double.parseDouble(cmd[3]);
        double y2 = Double.parseDouble(cmd[4]);

        // Check if any parameters are greater than 1.0 or less than 0.0
        if (x1 > 1.0 || y1 > 1.0 || x2 > 1.0 || y2 > 1.0) throw new IllegalArgumentException();
        if (x1 < 0.0 || y1 < 0.0 || x2 < 0.0 || y2 < 0.0) throw new IllegalArgumentException();

        VectorLine l = new VectorLine(x1, y1, x2, y2);
        colouredVectorShapes.add(new ColouredVectorShape(l, stroke, fill, fillColor));
    }

    /**
     * Handles mouse event actions on the canvas
     */
    private class CanvasMouseListener extends MouseInputAdapter {
        // Point at which the mouse was first pressed.
        private Point start;

        @Override
        public void mousePressed(MouseEvent e) {

            try {
                start = e.getPoint();
                at.inverseTransform(start, start); // Allow accurate drawing when zoomed
            } catch (NoninvertibleTransformException ex) {
                ex.printStackTrace();
            }

            Dimension windowSize = getSize();
            double startX = start.getX() / windowSize.getWidth();
            double startY = start.getY() / windowSize.getHeight();

            // Draw the users desired shape
            switch (selectedVectorShape) {
                case PLOT:
                    Color pFillColour = selectedFillColour;
                    Color pStrokeColour = selectedStrokeColour;
                    if (pStrokeColour == null) { pStrokeColour = Color.BLACK; }
                    if (pFillColour == null){ pFillColour = Color.BLACK; }
                    colouredVectorShapes.add(new ColouredVectorShape(new VectorPlot(startX, startY), pStrokeColour, fill, pFillColour));
                    repaint();
                    break;
                case LINE:
                    Color lFillColour = selectedFillColour;
                    Color lStrokeColour = selectedStrokeColour;
                    if (lStrokeColour == null) { lStrokeColour = Color.BLACK; }
                    if (lFillColour == null){ lFillColour = Color.BLACK; }
                    currentColouredVectorShape = new ColouredVectorShape(new VectorLine(startX, startY, startX, startY), lStrokeColour, fill, lFillColour);
                    break;
                case RECTANGLE:
                    Color rFillColour = selectedFillColour;
                    Color rStrokeColour = selectedStrokeColour;
                    if (rStrokeColour == null) { rStrokeColour = Color.BLACK; }
                    if (rFillColour == null){ rFillColour = Color.BLACK; }
                    currentColouredVectorShape = new ColouredVectorShape(new VectorRectangle(startX, startY, startX, startY), rStrokeColour, fill, rFillColour);
                    break;
                case ELLIPSE:
                    Color eFillColour = selectedFillColour;
                    Color eStrokeColour = selectedStrokeColour;
                    if (eStrokeColour == null) { eStrokeColour = Color.BLACK; }
                    if (eFillColour == null){ eFillColour = Color.BLACK; }
                    currentColouredVectorShape = new ColouredVectorShape(new VectorEllipse(startX, startY, startX, startY), eStrokeColour, fill, eFillColour);
                    break;
                case POLYGON:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {

            // Get the new point to draw the selected shape based on the position of the mouse
            //and the initial point of drawing
            if (selectedVectorShape == DrawCommands.PLOT) { return; } // plot is a special case

            try {
                Point mousePoint = e.getPoint();
                at.inverseTransform(mousePoint, mousePoint);

                double mouseX = mousePoint.getX();
                double mouseY = mousePoint.getY();
                double width = getSize().getWidth();
                double height = getSize().getHeight();

                if ((mouseX >= 0 && mouseX <= width-1) && (mouseY >= 0 && mouseY <= height-1)) {
                    VectorShape v = currentColouredVectorShape.getVectorShape(); // Get underlying VectorShape
                    v.resize(start, mousePoint, getSize()); // Resize VectorShape
                    currentColouredVectorShape.setVectorShape(v);
                    repaint();
                }

            } catch (NoninvertibleTransformException ex) {
                ex.printStackTrace();
            }
        }


        @Override
        public void mouseReleased(MouseEvent e) {
            if (selectedVectorShape == DrawCommands.PLOT) { return; }

            colouredVectorShapes.add(currentColouredVectorShape);
            // Nullify the current shape, otherwise the shape is drawn twice
            // and it takes two undo's to remove the shape
            currentColouredVectorShape = null;
        }

    }//CanvasMouseListener class
}//end Canvas class