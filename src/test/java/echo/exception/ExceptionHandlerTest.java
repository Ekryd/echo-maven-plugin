package echo.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class ExceptionHandlerTest {

  @Test
  void failureExceptionShouldThrowMojoFailureException() {

    var failureException = new FailureException("Gurka");

    Executable testMethod =
        () -> new ExceptionHandler(failureException).throwMojoFailureException();

    var thrown = assertThrows(MojoFailureException.class, testMethod);

    assertThat(thrown.getMessage(), is(equalTo("Gurka")));
  }

  @Test
  void failureExceptionShouldKeepCause() {

    var cause = new IllegalArgumentException("not valid");
    var failureException = new FailureException("Gurka", cause);

    Executable testMethod =
        () -> new ExceptionHandler(failureException).throwMojoFailureException();

    var thrown = assertThrows(MojoFailureException.class, testMethod);

    assertAll(
        () -> assertThat(thrown.getMessage(), is(equalTo("Gurka"))),
        () -> assertThat(thrown.getCause(), is(cause)));
  }
}
