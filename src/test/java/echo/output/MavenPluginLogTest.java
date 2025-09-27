package echo.output;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MavenPluginLogTest {
  private final Log logMock = mock(Log.class);
  private MavenPluginLog mavenLogger;

  @BeforeEach
  void setUp() {
    mavenLogger = new MavenPluginLog(logMock);
  }

  @Test
  void infoShouldOutputInfoLevel() {
    mavenLogger.info("Gurka");

    verify(logMock).info("Gurka");
    verifyNoMoreInteractions(logMock);
  }

  @Test
  void debugExceptionLevelShouldLogToDebugIfEnabled() {
    when(logMock.isDebugEnabled()).thenReturn(true);

    mavenLogger.debug(new OutOfMemoryError("Ta daa!"));

    verify(logMock).isDebugEnabled();
    verify(logMock).debug(any(OutOfMemoryError.class));
    verifyNoMoreInteractions(logMock);
  }

  @Test
  void debugExceptionLevelShouldNotLogToDebugIfDisabled() {
    mavenLogger.debug(new OutOfMemoryError("Ta daa!"));

    when(logMock.isDebugEnabled()).thenReturn(false);
    verify(logMock).isDebugEnabled();
    verifyNoMoreInteractions(logMock);
  }

  @Test
  void debugMessageLevelShouldLogToDebugIfEnabled() {
    when(logMock.isDebugEnabled()).thenReturn(true);

    mavenLogger.debug("Ta daa!");

    verify(logMock).isDebugEnabled();
    verify(logMock).debug("Ta daa!");
    verifyNoMoreInteractions(logMock);
  }

  @Test
  void debugMessageLevelShouldNotLogToDebugIfDisabled() {
    mavenLogger.debug("Ta daa!");

    when(logMock.isDebugEnabled()).thenReturn(false);
    verify(logMock).isDebugEnabled();
    verifyNoMoreInteractions(logMock);
  }
}
