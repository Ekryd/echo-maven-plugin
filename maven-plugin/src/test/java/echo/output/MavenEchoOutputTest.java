package echo.output;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
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
    private ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        mavenEchoOutput = new MavenEchoOutput(logMock);
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

    @Test(expected = MojoFailureException.class)
    public void errorLevelShouldThrowException() throws Exception {
        expectedException.expect(MojoFailureException.class);
        expectedException.expectMessage("Gurkas");

        mavenEchoOutput.error("Gurkas");
    }
}
