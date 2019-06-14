import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class SwingApp extends JFrame {
    private Canvas canvas; // reference to current drawing canvas

    /**
     * Adds the Canvas, JToolBar(s) and JMenuBar to SwingApp
     * GridBagLayout is used to layout the components ont he JFrame.
     */

    private SwingApp() {
        super("CAB302 Assignment 2 - Totally Awesome Painting Program ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensures the program terminates when the JFrame is closed
        setLayout(new GridBagLayout()); // Sets JFrame to use GridBagLayout

        // Add JMenuBar to the JFrame
        setJMenuBar(createMenuBar());

        // Set GBC for JToolBar and add to the JFrame
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill remaining space horizontally
        getContentPane().add(createToolBar(), gbc);

        // Set GBC for Canvas and add to the JFrame
        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH; // Fill remaining space vertically and horizontally
        canvas = new Canvas();
        getContentPane().add(canvas, gbc);

        // Set  GBC for Zoom Toolbar and add to the JFrame
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH; // Fill remaining space horizontally
        getContentPane().add(createZoomToolBar(), gbc);

        // Pack and display JFrame
        setPreferredSize(new Dimension(1000, 1000));
        setResizable(false); // Disable user ability to resize window
        pack();
        setVisible(true);
    }

    /**
     * Creates a JToolBar with buttons such as, redo, undo and more.
     * @return A JToolBar with buttons for use with Canvas
     */
    private JToolBar createToolBar() {
        JToolBar toolBar;
        JButton fillButton, strokeColourChooserButton, fillColourChooserButton,
        rectangleButton, lineButton, plotButton, ellipseButton, undoButton, clearAllButton, redoButton;

        // Initialise JToolBar
        toolBar = new JToolBar("Tools");
        toolBar.setFloatable(false); // Disable movement with mouse

        // Initialize JButtons
        lineButton = new JButton("Line");
        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");
        plotButton = new JButton("Plot");
        rectangleButton = new JButton("Rectangle");
        ellipseButton = new JButton("Ellipse");
        fillButton = new JButton("Fill");
        strokeColourChooserButton = new JButton("Stroke Colour");
        fillColourChooserButton = new JButton("Fill colour");
        clearAllButton = new JButton("Clear All");

        // Add action listeners
        redoButton.addActionListener((ActionEvent) -> canvas.redoPainting());
        undoButton.addActionListener((ActionEvent) -> canvas.undoPainting());
        clearAllButton.addActionListener((ActionEvent)-> canvas.clear());
        plotButton.addActionListener((ActionEvent) -> canvas.setSelectedVectorShape(DrawCommands.PLOT));
        lineButton.addActionListener((ActionEvent) -> canvas.setSelectedVectorShape(DrawCommands.LINE));
        rectangleButton.addActionListener((ActionEvent) -> canvas.setSelectedVectorShape(DrawCommands.RECTANGLE));
        ellipseButton.addActionListener((ActionEvent) -> canvas.setSelectedVectorShape(DrawCommands.ELLIPSE));
        strokeColourChooserButton.addActionListener((ActionEvent) -> canvas.setSelectedStrokeColour(JColorChooser.showDialog(null, "Stroke Colour", Color.BLACK)));
        fillColourChooserButton.addActionListener((ActionEvent) -> canvas.setSelectedFillColour(JColorChooser.showDialog(null, "Fill Colour", Color.BLACK)));
        fillButton.addActionListener((ActionEvent) -> {
            if (canvas.getFill()) canvas.setFill(false);
            else canvas.setFill(true);
        });

        // Add JButtons to JToolBar
        toolBar.add(redoButton);
        toolBar.add(undoButton);
        toolBar.add(plotButton);
        toolBar.add(lineButton);
        toolBar.add(rectangleButton);
        toolBar.add(ellipseButton);
        toolBar.add(fillButton);
        toolBar.add(strokeColourChooserButton);
        toolBar.add(fillColourChooserButton);
        toolBar.add(clearAllButton);

        // Center buttons in toolbar
        toolBar.setLayout(new FlowLayout(FlowLayout.CENTER));

        return toolBar;
    }

    /**
     * Creates a JToolBar with buttons that controls the zoom level of the canvas
     * @return A JToolBar with zoom buttons for use with Canvas
     */
    private JToolBar createZoomToolBar() {
        JToolBar toolBar;
        JButton zoomIn, zoomOut;

        // Initialise JToolBar
        toolBar = new JToolBar("Tools");
        toolBar.setFloatable(false); // Disable ability to be moved with mouse

        // Initialise JButtons
        zoomIn = new JButton("   +   ");
        zoomOut = new JButton("   -   ");

        // Add action listeners
        zoomIn.addActionListener((ActionEvent) -> canvas.adjustScaleIn());
        zoomOut.addActionListener((ActionEvent) -> canvas.adjustScaleOut());

        // Add JButton to JToolBar
        toolBar.add(zoomIn);
        toolBar.add(zoomOut);

        // Center buttons in toolbar
        toolBar.setLayout(new FlowLayout(FlowLayout.CENTER));

        return toolBar;
    }

    /**
     * Creates a JMenuBar, includes all file manipulation features, including 'Save As', 'Open' and 'New'
     * @return JMenuBar for use with Canvas
     */

    private JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu fileMenu;
        JMenuItem fileMenuItem1, fileMenuItem2, fileMenuItem3;

        // Initialise JMenuBar
        menuBar = new JMenuBar();

        // Initialise JMenu
        fileMenu = new JMenu("File");

        // Initialise and add JMenuItems to JMenu
        fileMenuItem1 = new JMenuItem("New");
        fileMenuItem2 = new JMenuItem("Open");
        fileMenuItem3 = new JMenuItem("Save As");

        // Add action listeners
        fileMenuItem1.addActionListener((ActionEvent) -> { canvas.clear(); }); // Clears canvas when 'New' is selected

        // Instantiates a JFileChooser upon selecting "Open"
        fileMenuItem2.addActionListener((ActionEvent) -> {
            JFileChooser fc = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("VEC files", "vec", "VEC"); //filters all files to be VEC files
            fc.setFileFilter(filter);

            int returnValue = fc.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                try {
                    canvas.loadVecFile(fc.getSelectedFile()); // Try load the file
                } catch (IOException | IndexOutOfBoundsException | IllegalArgumentException e) { // Handle any errors gracefully
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Encountered incorrectly formatted draw command"); // Explain to the user something has gone wrong.
                }
            }
        });

        // Instantiates a JFileChooser  upon selecting "Save As"
        fileMenuItem3.addActionListener((ActionEvent) -> {
                    JFileChooser fc = new JFileChooser();
                    int returnValue = fc.showSaveDialog(this);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        // Writes string representation of VectorShapes to file
                        try (BufferedWriter br = new BufferedWriter(new FileWriter(fc.getSelectedFile() + ".vec"))) {
                            String[] lines = canvas.getCommands();

                            for (String str : lines) {
                                br.write(str);
                                br.newLine();
                            }

                            JOptionPane.showMessageDialog(this, "Successfully saved."); // Displays confirmation message if file is saved
                        } catch (IOException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(this, "Error encountered while saving."); // Displays error message if file is unable to be saved
                        }

                    }
                });

        // Add JMenuItems to the JFileMenu
        fileMenu.add(fileMenuItem1);
        fileMenu.add(fileMenuItem2);
        fileMenu.add(fileMenuItem3);

        // Add JMenu to JMenuBar
        menuBar.add(fileMenu);

        return menuBar;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SwingApp());

    }
}



