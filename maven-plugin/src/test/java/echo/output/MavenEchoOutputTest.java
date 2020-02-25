package echo.output;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author bjorn
 * @since 2013-09-19
 */
public class MavenEchoOutputTest {
    private final Log logMock = mock(Log.class);
    private MavenEchoOutput mavenEchoOutput;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        mavenEchoOutput = new MavenEchoOutput(logMock);
    }

    @Test
    public void failLevelShouldThrowException() {
        expectedException.expect(MojoFailureException.class);
        expectedException.expectMessage("Gurkas");

        mavenEchoOutput.fail("Gurkas");
    }

    @Test
    public void errorShouldOutputErrorLevel() {
        mavenEchoOutput.error("Gurka");

        verify(logMock).error("Gurka");
        verifyNoMoreInteractions(logMock);
    }

    @Test
    public void warnShouldOutputWarnLevel() {
        mavenEchoOutput.warning("Gurka");

        verify(logMock).warn("Gurka");
        verifyNoMoreInteractions(logMock);
    }

    @Test
    public void infoShouldOutputInfoLevel() {
        mavenEchoOutput.info("Gurka");

        verify(logMock).info("Gurka");
        verifyNoMoreInteractions(logMock);
    }

    @Test
    public void debugShouldOutputDebugLevel() {
        mavenEchoOutput.debug("Gurka");

        verify(logMock).debug("Gurka");
        verifyNoMoreInteractions(logMock);
    }
}
