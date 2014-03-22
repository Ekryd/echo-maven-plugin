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

    @Test
    public void debugExceptionLevelShouldLogToDebugIfEnabled() throws Exception {
        when(logMock.isDebugEnabled()).thenReturn(true);

        mavenLogger.debug(new OutOfMemoryError("Ta daa!"));

        verify(logMock).isDebugEnabled();
        verify(logMock).debug(any(OutOfMemoryError.class));
        verifyNoMoreInteractions(logMock);
    }

    @Test
    public void debugExceptionLevelShouldNotLogToDebugIfDisabled() throws Exception {
        mavenLogger.debug(new OutOfMemoryError("Ta daa!"));

        when(logMock.isDebugEnabled()).thenReturn(false);
        verify(logMock).isDebugEnabled();
        verifyNoMoreInteractions(logMock);
    }

    @Test
    public void debugMessageLevelShouldLogToDebugIfEnabled() throws Exception {
        when(logMock.isDebugEnabled()).thenReturn(true);

        mavenLogger.debug("Ta daa!");

        verify(logMock).isDebugEnabled();
        verify(logMock).debug("Ta daa!");
        verifyNoMoreInteractions(logMock);
    }

    @Test
    public void debugMessageLevelShouldNotLogToDebugIfDisabled() throws Exception {
        mavenLogger.debug("Ta daa!");

        when(logMock.isDebugEnabled()).thenReturn(false);
        verify(logMock).isDebugEnabled();
        verifyNoMoreInteractions(logMock);
    }
}
