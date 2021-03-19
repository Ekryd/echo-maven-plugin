package echo.output;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author bjorn
 * @since 2013-09-19
 */
class MavenEchoOutputTest {
    private final Log logMock = mock(Log.class);
    private MavenEchoOutput mavenEchoOutput;

    @BeforeEach
    void setUp() {
        mavenEchoOutput = new MavenEchoOutput(logMock);
    }

    @Test
    void failLevelShouldThrowException() {

        final Executable testMethod = () -> mavenEchoOutput.fail("Gurkas");

        final MojoFailureException thrown = assertThrows(MojoFailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("Gurkas")));
    }

    @Test
    void errorShouldOutputErrorLevel() {
        mavenEchoOutput.error("Gurka");

        verify(logMock).error("Gurka");
        verifyNoMoreInteractions(logMock);
    }

    @Test
    void warnShouldOutputWarnLevel() {
        mavenEchoOutput.warning("Gurka");

        verify(logMock).warn("Gurka");
        verifyNoMoreInteractions(logMock);
    }

    @Test
    void infoShouldOutputInfoLevel() {
        mavenEchoOutput.info("Gurka");

        verify(logMock).info("Gurka");
        verifyNoMoreInteractions(logMock);
    }

    @Test
    void debugShouldOutputDebugLevel() {
        mavenEchoOutput.debug("Gurka");

        verify(logMock).debug("Gurka");
        verifyNoMoreInteractions(logMock);
    }
}
