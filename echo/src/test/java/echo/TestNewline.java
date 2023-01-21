package echo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.parameter.PluginParametersBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class TestNewline {

  @Test
  void illegalNewlineShouldThrowException() {

    Executable testMethod =
        () -> new PluginParametersBuilder().setFormatting(null, "\t").createPluginParameters();

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        thrown.getMessage(),
        is(
            equalTo(
                "LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [9]")));
  }

  @Test
  void newlineShouldBeReplacedWithLineSeparator1() {
    var echoOutput = mock(EchoOutput.class);

    var parameters =
        new PluginParametersBuilder()
            .setMessage("Hex\nover\rthe\r\nphone", null)
            .setFormatting(null, "\n")
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(null, parameters, echoOutput);
    echoPlugin.echo();

    verify(echoOutput).info("Hex\nover\nthe\nphone");
  }

  @Test
  void newlineShouldBeReplacedWithLineSeparator2() {
    var echoOutput = mock(EchoOutput.class);

    var parameters =
        new PluginParametersBuilder()
            .setMessage("Hex\nover\rthe\r\nphone", null)
            .setFormatting(null, "\r")
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(null, parameters, echoOutput);
    echoPlugin.echo();

    verify(echoOutput).info("Hex\rover\rthe\rphone");
  }

  @Test
  void newlineShouldBeReplacedWithLineSeparator3() {
    var echoOutput = mock(EchoOutput.class);

    var parameters =
        new PluginParametersBuilder()
            .setMessage("Hex\nover\rthe\r\nphone", null)
            .setFormatting(null, "\r\n")
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(null, parameters, echoOutput);
    echoPlugin.echo();

    verify(echoOutput).info("Hex\r\nover\r\nthe\r\nphone");
  }
}
