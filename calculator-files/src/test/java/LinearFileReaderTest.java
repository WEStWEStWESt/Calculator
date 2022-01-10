import com.calculator.files.LinearFileReader;
import com.calculator.files.exceptions.EmptyPageException;
import com.calculator.files.exceptions.FileNotFoundException;
import com.calculator.files.exceptions.FileUnreadableException;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;

import static org.junit.Assert.*;

public class LinearFileReaderTest {
    public static final String VALID_FILEPATH = "src/test/resources/testFileForReadingInParts.txt";
    public static final String UNREADABLE_FILEPATH = "src/test/resources/testUnreadableFile.txt";
    public static final String NONE_EXISTED_FILEPATH = "src/test/resources/oops.txt";
    public static final String EMPTY_DIRECTORY_FILEPATH = "src/test/resources/root/empty";
    public static final String EMPTY_FILE_FILEPATH = "src/test/resources/root/empty.txt";
    public static final String ONE_FILE_FILEPATH = "src/test/resources/root/0.txt";
    public static final String ONE_FILE_IN_FOLDER_FILEPATH = "src/test/resources/root/level1/level2/level3/level4/level5/5.txt";

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

    private void assertAllLines(String path, int fileNumber) throws IOException, EmptyPageException {
        LinearFileReader reader = new LinearFileReader(path);
        assertTrue(reader.hasNext());
        var lines = new HashSet<>();
        //        while (reader.hasNext()) {
//            lines.add(reader.next());
//        }
        reader.forEachRemaining(lines::add);
        assertEquals(4, lines.size());
        assertTrue(lines.contains("file %s - line1".formatted(fileNumber)));
        assertTrue(lines.contains("file %s - line2".formatted(fileNumber)));
        assertTrue(lines.contains("file %s - line3".formatted(fileNumber)));
        assertTrue(lines.contains("file %s - line4".formatted(fileNumber)));
    }

    @Test
    public void whenReadOneFolderWithOneFile_shouldReturnAllTheFileLines() throws IOException, EmptyPageException {
        assertAllLines(ONE_FILE_IN_FOLDER_FILEPATH, 5);
    }

    @Test
    public void whenReadOneFile_shouldReturnAllTheFileLines() throws IOException, EmptyPageException {
        assertAllLines(ONE_FILE_FILEPATH, 0);
    }

    @Test
    public void whenReadEmptyDirectory_hasNextReturnsFalse() throws IOException, EmptyPageException {
        assertFalse(new LinearFileReader(EMPTY_FILE_FILEPATH).hasNext());
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