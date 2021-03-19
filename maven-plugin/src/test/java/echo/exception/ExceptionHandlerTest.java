package echo.exception;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author bjorn
 * @since 2013-10-19
 */
class ExceptionHandlerTest {

    @Test
    void failureExceptionShouldThrowMojoFailureException() {

        FailureException failureException = new FailureException("Gurka");

        final Executable testMethod = () -> new ExceptionHandler(failureException)
                .throwMojoFailureException();

        final MojoFailureException thrown = assertThrows(MojoFailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("Gurka")));
    }

    @Test
    void failureExceptionShouldKeepCause() {

        IllegalArgumentException cause = new IllegalArgumentException("not valid");
        FailureException failureException = new FailureException("Gurka", cause);

        final Executable testMethod = () -> new ExceptionHandler(failureException)
                .throwMojoFailureException();

        final MojoFailureException thrown = assertThrows(MojoFailureException.class, testMethod);

        assertAll(
                () -> assertThat(thrown.getMessage(), is(equalTo("Gurka"))),
                () -> assertThat(thrown.getCause(), is(cause))
        );
    }
}
