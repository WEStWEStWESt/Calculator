import com.calculator.files.LinearFileReader;
import com.calculator.files.exceptions.EmptyPageException;
import com.calculator.files.exceptions.FileNotFoundException;
import com.calculator.files.exceptions.FileUnreadableException;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

public class LinearFileReaderTest {
    public static final String VALID_FILEPATH = "src/test/resources/testFileForReadingInParts.txt";
    public static final String INVALID_FILEPATH = "src/test/resources/root";
    public static final String UNREADABLE_FILEPATH = "src/test/resources/testUnreadableFile.txt";
    public static final String NONE_EXISTED_FILEPATH = "src/test/resources/oops.txt";
    public static final String EMPTY_DIRECTORY_FILEPATH = "src/test/resources/src";

    /* public static final String EMPTY_DIRECTORY_FILEPATH = "/src";
     private String contextPath;

     @Before
     public void setUp() throws Exception {
         String userPath = System.getProperty("user.dir");
         this.contextPath = userPath.substring(0, userPath.lastIndexOf("\\"));
     }
     @Test(expected = EmptyPageException.class)
     public void whenCreateNewLinearFileReader_shouldThrowEmptyPageException() throws IOException, EmptyPageException {
         new LinearFileReader(contextPath + EMPTY_DIRECTORY_FILEPATH);
     }*/

    //TODO check getPage() functionality which throws an IOException

    @Ignore
    @Test
    public void reservedFutureTests() throws IOException, EmptyPageException {
        new LinearFileReader(INVALID_FILEPATH);
    }

    @Test(expected = EmptyPageException.class)
    public void whenCreateNewLinearFileReader_shouldThrowEmptyPageException() throws IOException, EmptyPageException {
        new LinearFileReader(EMPTY_DIRECTORY_FILEPATH);
    }

    @Test
    public void whenCreateNewLinearFileReader_shouldCreateNewInstance() throws IOException, EmptyPageException {
        new LinearFileReader(VALID_FILEPATH);
    }

    @Test(expected = FileNotFoundException.class)
    public void whenCreateNewLinearFileReader_shouldThrowNullArgumentException() throws IOException, EmptyPageException {
        new LinearFileReader(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void whenCreateNewLinearFileReader_shouldThrowFileNotFoundException() throws IOException, EmptyPageException {
        new LinearFileReader(NONE_EXISTED_FILEPATH);
    }

    @Ignore
    @Test(expected = FileUnreadableException.class)
    public void whenCreateNewLinearFileReader_shouldThrowFileUnreadableException() throws IOException, EmptyPageException {
        new LinearFileReader(UNREADABLE_FILEPATH);
    }
}