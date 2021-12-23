import com.calculator.files.LinearFileReader;
import com.calculator.files.exceptions.FileNotFoundException;
import com.calculator.files.exceptions.FileUnreadableException;
import org.junit.Ignore;
import org.junit.Test;

public class LinearFileReaderTest {
    public static final String VALID_FILEPATH = "src/test/resources/testFileForReadingInParts.txt";
    public static final String UNREADABLE_FILEPATH = "src/test/resources/testUnreadableFile.txt";
    public static final String NONE_EXISTED_FILEPATH = "src/test/resources/oops.txt";

    @Test
    public void whenCreateNewLinearFileReader_shouldCreateNewInstance() {
        new LinearFileReader(VALID_FILEPATH);
    }

    @Test(expected = FileNotFoundException.class)
    public void whenCreateNewLinearFileReader_shouldThrowNullArgumentException() {
        new LinearFileReader(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void whenCreateNewLinearFileReader_shouldThrowFileNotFoundException() {
        new LinearFileReader(NONE_EXISTED_FILEPATH);
    }

    @Ignore
    @Test(expected = FileUnreadableException.class)
    public void whenCreateNewLinearFileReader_shouldThrowFileUnreadableException() {
        new LinearFileReader(UNREADABLE_FILEPATH);
    }
}
