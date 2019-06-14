import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.*;


class InvalidCommandsTests {

    private Canvas canvas;
    private File ellipseMissingArgs = new File("src/test_vec_files/ellipse_missing_args.txt");
    private File ellipseSpeltWrong = new File("src/test_vec_files/ellipse_spelt_wrong.txt");
    private File ellipseXGreaterThan1 = new File("src/test_vec_files/ellipse_x_greater_than_1.txt");
    private File ellipseXLessThan0 = new File("src/test_vec_files/ellipse_x_less_than_0.txt");
    private File ellipseYGreaterThan1 = new File("src/test_vec_files/ellipse_y_greater_than_1.txt");
    private File ellipseYLessThan0 = new File("src/test_vec_files/ellipse_y_less_than_0.txt");

    private File lineMissingArgs = new File("src/test_vec_files/line_missing_args.txt");
    private File lineSpeltWrong = new File("src/test_vec_files/line_spelt_wrong.txt");
    private File lineXGreaterThan1 = new File("src/test_vec_files/line_x_greater_than_1.txt");
    private File lineXLessThan0 = new File("src/test_vec_files/line_x_less_than_0.txt");
    private File lineYGreaterThan1 = new File("src/test_vec_files/line_y_greater_than_1.txt");
    private File lineYLessThan0 = new File("src/test_vec_files/line_y_less_than_0.txt");

    private File plotMissingArgs = new File("src/test_vec_files/plot_missing_args.txt");
    private File plotSpeltWrong = new File("src/test_vec_files/plot_spelt_wrong.txt");
    private File plotXGreaterThan1 = new File("src/test_vec_files/plot_x_greater_than_1.txt");
    private File plotXLessThan0 = new File("src/test_vec_files/plot_x_less_than_0.txt");
    private File plotYGreaterThan1 = new File("src/test_vec_files/plot_y_greater_than_1.txt");
    private File plotYLessThan0 = new File("src/test_vec_files/plot_y_less_than_0.txt");

    private File rectangleMissingArgs = new File("src/test_vec_files/rectangle_missing_args.txt");
    private File rectangleSpeltWrong = new File("src/test_vec_files/rectangle_spelt_wrong.txt");
    private File rectangleXGreaterThan1 = new File("src/test_vec_files/rectangle_x_greater_than_1.txt");
    private File rectangleXLessThan0 = new File("src/test_vec_files/rectangle_x_less_than_0.txt");
    private File rectangleYGreaterThan1 = new File("src/test_vec_files/rectangle_y_greater_than_1.txt");
    private File rectangleYLessThan0 = new File("src/test_vec_files/rectangle_y_less_than_0.txt");

    private File polygonSpeltWrong = new File("src/test_vec_files/polygon_spelt_wrong.txt");

    @BeforeEach
    void beforeEach() {
        canvas = new Canvas();
    }

    /**
     * Test for ELLIPSE command
     */

    @Test
    void testExceptionThrownReadingEllipseCommandWithMissingArgs() { assertThrows(IndexOutOfBoundsException.class, () -> canvas.loadVecFile(ellipseMissingArgs)); }

    @Test
    void textExceptionThrownReadingEllipseCommandSpeltWrong() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(ellipseSpeltWrong)); }

    @Test
    void testExceptionThrownReadingEllipseCommandWhereXGreaterThan1() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(ellipseXGreaterThan1)); }

    @Test
    void testExceptionThrownReadingEllipseCommandWhereXLessThan0() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(ellipseXLessThan0)); }

    @Test
    void testExceptionThrownReadingEllipseCommandWhereYGreaterThan1() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(ellipseYGreaterThan1)); }

    @Test
    void testExceptionThrownReadingEllipseCommandWhereYLessThan0() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(ellipseYLessThan0)); }

    /**
     * Test for LINE command
     */

    @Test
    void testExceptionThrownReadingLineCommandWithMissingArgs() { assertThrows(IndexOutOfBoundsException.class, () -> canvas.loadVecFile(lineMissingArgs)); }

    @Test
    void textExceptionThrownReadingLineCommandSpeltWrong() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(lineSpeltWrong)); }

    @Test
    void testExceptionThrownReadingLineCommandWhereXGreaterThan1() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(lineXGreaterThan1)); }

    @Test
    void testExceptionThrownReadingLineCommandWhereXLessThan0() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(lineXLessThan0)); }

    @Test
    void testExceptionThrownReadingLineCommandWhereYGreaterThan1() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(lineYGreaterThan1)); }

    @Test
    void testExceptionThrownReadingLineCommandWhereYLessThan0() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(lineYLessThan0)); }


    /**
     * Tests for PLOT command
     */

    @Test
    void testExceptionThrownReadingPlotCommandWithMissingArgs() { assertThrows(IndexOutOfBoundsException.class, () -> canvas.loadVecFile(plotMissingArgs)); }

    @Test
    void textExceptionThrownReadingPlotCommandSpeltWrong() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(plotSpeltWrong)); }

    @Test
    void testExceptionThrownReadingPlotCommandWhereXGreaterThan1() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(plotXGreaterThan1)); }

    @Test
    void testExceptionThrownReadingPlotCommandWhereXLessThan0() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(plotXLessThan0)); }

    @Test
    void testExceptionThrownReadingPlotCommandWhereYGreaterThan1() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(plotYGreaterThan1)); }

    @Test
    void testExceptionThrownReadingPlotCommandWhereYLessThan0() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(plotYLessThan0)); }

    /**
     * Tests for RECTANGLE command
     */

    @Test
    void testExceptionThrownReadingRectangleCommandWithMissingArgs() { assertThrows(IndexOutOfBoundsException.class, () -> canvas.loadVecFile(rectangleMissingArgs)); }

    @Test
    void textExceptionThrownReadingRectangleCommandSpeltWrong() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(rectangleSpeltWrong)); }

    @Test
    void testExceptionThrownReadingRectangleCommandWhereXGreaterThan1() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(rectangleXGreaterThan1)); }

    @Test
    void testExceptionThrownReadingRectangleCommandWhereXLessThan0() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(rectangleXLessThan0)); }

    @Test
    void testExceptionThrownReadingRectangleCommandWhereYGreaterThan1() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(rectangleYGreaterThan1)); }

    @Test
    void testExceptionThrownReadingRectangleCommandWhereYLessThan0() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(rectangleYLessThan0)); }

    /**
     * Tests for POLYGON command
     */

    @Test
    void textExceptionThrownReadingPolygonCommandSpeltWrong() { assertThrows(IllegalArgumentException.class, () -> canvas.loadVecFile(polygonSpeltWrong)); }
}