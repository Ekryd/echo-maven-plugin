package echo.output;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * @author bjorn
 * @since 2013-10-19
 */
public class MavenLoggerTest {
    private Log logMock = mock(Log.class);
    private MavenLogger mavenLogger;

    @Before
    public void setUp() throws Exception {
        mavenLogger = new MavenLogger(logMock);
    }

    @Test
    public void infoShouldOutputInfoLevel() throws Exception {
        mavenLogger.info("Gurka");

        verify(logMock).info("Gurka");
        verifyNoMoreInteractions(logMock);
    }
}
