package echo.output;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.*;

/**
 * @author bjorn
 * @since 2013-09-19
 */
public class MavenEchoOutputTest {
    private Log logMock = mock(Log.class);
    private MavenEchoOutput mavenEchoOutput;
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        mavenEchoOutput = new MavenEchoOutput(logMock);
    }

    @Test
    public void failLevelShouldThrowException() throws Exception {
        expectedException.expect(MojoFailureException.class);
        expectedException.expectMessage("Gurkas");

        mavenEchoOutput.fail("Gurkas");
    }

    @Test
    public void errorShouldOutputErrorLevel() throws Exception {
        mavenEchoOutput.error("Gurka");

        verify(logMock).error("Gurka");
        verifyNoMoreInteractions(logMock);
    }

    @Test
    public void warnShouldOutputWarnLevel() throws Exception {
        mavenEchoOutput.warning("Gurka");

        verify(logMock).warn("Gurka");
        verifyNoMoreInteractions(logMock);
    }

    @Test
    public void infoShouldOutputInfoLevel() throws Exception {
        mavenEchoOutput.info("Gurka");

        verify(logMock).info("Gurka");
        verifyNoMoreInteractions(logMock);
    }

    @Test
    public void debugShouldOutputDebugLevel() throws Exception {
        mavenEchoOutput.debug("Gurka");

        verify(logMock).debug("Gurka");
        verifyNoMoreInteractions(logMock);
    }
}
