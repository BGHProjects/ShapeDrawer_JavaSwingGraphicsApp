import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.*;
import java.util.ArrayList;


class FileReadingTests {

    private Canvas canvas;

    // Correct Single Shapes
    private File correctSingleRectangle = new File("src/test_vec_files/CorrectSingleRectangle.txt");
    private File correctSingleEllipse = new File ("src/test_vec_files/CorrectSingleEllipse.txt");
    private File correctSingleLine = new File ("src/test_vec_files/CorrectSingleLine.txt");
    private File correctSinglePlot = new File ("src/test_vec_files/CorrectSinglePlot.txt");

    // Correct Single Colour Commands
    private File correctSinglePen = new File ("src/test_vec_files/CorrectSinglePen.txt");
    private File correctSingleFill = new File ("src/test_vec_files/CorrectSingleFill.txt");

    // Correct Compilations of Commands
    private File correctShapes = new File ("src/test_vec_files/CorrectShapes.txt");

    ///////////////////////////////////////////
    //Tests for CORRECTLY Implemented Commands
    ///////////////////////////////////////////

    @BeforeEach
    void BeforeEach() {
        canvas = new Canvas();
    }

    @Test
    void testCorrectSingleRectangle()
    {
        try {
            ArrayList<String[]> fromFile = canvas.loadVecFile(correctSingleRectangle);
            ArrayList<String[]> shouldMatch = new ArrayList<>();
            String[] correctCommands = {"RECTANGLE", "0.25", "0.25", "0.5", "0.75"};
            shouldMatch.add(correctCommands);
            assertArrayEquals(shouldMatch.get(0), fromFile.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testCorrectSingleEllipse() {
        try {
            ArrayList<String[]> fromFile = canvas.loadVecFile(correctSingleEllipse);
            ArrayList<String[]> shouldMatch = new ArrayList<>();
            String[] correctCommands = {"ELLIPSE", "0.25", "0.25", "0.5", "0.75"};
            shouldMatch.add(correctCommands);
            assertArrayEquals(shouldMatch.get(0), fromFile.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testCorrectSingleLine()
    {
        try {
            ArrayList<String[]> fromFile = canvas.loadVecFile(correctSingleLine);
            ArrayList<String[]> shouldMatch = new ArrayList<>();
            String[] correctCommands = {"LINE", "0.1", "0.1", "0.8", "0.8"};
            shouldMatch.add(correctCommands);
            assertArrayEquals(shouldMatch.get(0), fromFile.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testCorrectSinglePlot()
    {
        try {
            ArrayList<String[]> fromFile = canvas.loadVecFile(correctSinglePlot);
            ArrayList<String[]> shouldMatch = new ArrayList<>();
            String[] correctCommands = {"PLOT", "0.5", "0.9"};
            shouldMatch.add(correctCommands);
            assertArrayEquals(shouldMatch.get(0), fromFile.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testCorrectSinglePen()
    {
        try {
            ArrayList<String[]> fromFile = canvas.loadVecFile(correctSinglePen);
            ArrayList<String[]> shouldMatch = new ArrayList<>();
            String[] correctCommands = {"PEN", "#0000FF"};
            shouldMatch.add(correctCommands);
            assertArrayEquals(shouldMatch.get(0), fromFile.get(0));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testCorrectSingleFill()
    {
        try {
            ArrayList<String[]> fromFile = canvas.loadVecFile(correctSingleFill);
            ArrayList<String[]> shouldMatch = new ArrayList<>();
            String[] correctCommands = {"FILL", "#00FF00"};
            shouldMatch.add(correctCommands);

            assertArrayEquals(shouldMatch.get(0), fromFile.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testCorrectShapeCommands()
    {
        try {
            ArrayList<String[]> fromFile = canvas.loadVecFile(correctShapes);
            ArrayList<String[]> shouldMatch = new ArrayList<>();
            String[] rectangle = {"RECTANGLE", "0.25", "0.25", "0.5", "0.75"};
            String[] ellipse = {"ELLIPSE", "0.25", "0.25", "0.5", "0.75"};
            String[] line = {"LINE", "0.1", "0.1", "0.8", "0.8"};
            String[] plot = {"PLOT", "0.5", "0.9"};

            shouldMatch.add(rectangle);
            shouldMatch.add(ellipse);
            shouldMatch.add(line);
            shouldMatch.add(plot);

            assertArrayEquals(shouldMatch.get(0), fromFile.get(0));
            assertArrayEquals(shouldMatch.get(1), fromFile.get(1));
            assertArrayEquals(shouldMatch.get(2), fromFile.get(2));
            assertArrayEquals(shouldMatch.get(3), fromFile.get(3));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
