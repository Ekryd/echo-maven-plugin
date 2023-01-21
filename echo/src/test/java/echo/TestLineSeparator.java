package echo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.output.PluginLog;
import echo.parameter.PluginParametersBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class TestLineSeparator {
  private final PluginLog pluginLog = mock(PluginLog.class);

  @Test
  void formattingTextWithNLShouldResultInLineBreaks() {
    var echoOutput = mock(EchoOutput.class);

    var parameters =
        new PluginParametersBuilder()
            .setMessage("ABC\n\n\nDEF\r\r\rGHI\r\n\r\n\r\n", null)
            .setFormatting("UTF-8", "\n")
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
    echoPlugin.echo();

    verify(echoOutput).info("ABC\n\n\nDEF\n\n\nGHI\n\n\n");
  }

  @Test
  void formattingXmlWithCRShouldResultInOneLineBreaks() {
    var echoOutput = mock(EchoOutput.class);

    var parameters =
        new PluginParametersBuilder()
            .setMessage("ABC\n\n\nDEF\r\r\rGHI\r\n\r\n\r\n", null)
            .setFormatting("UTF-8", "\r")
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
    echoPlugin.echo();

    verify(echoOutput).info("ABC\r\r\rDEF\r\r\rGHI\r\r\r");
  }

  @Test
  void formattingXmlWithCRNLShouldResultInOneLineBreaks() {
    var echoOutput = mock(EchoOutput.class);

    var parameters =
        new PluginParametersBuilder()
            .setMessage("ABC\n\n\nDEF\r\r\rGHI\r\n\r\n\r\n", null)
            .setFormatting("UTF-8", "\r\n")
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
    echoPlugin.echo();

    verify(echoOutput).info("ABC\r\n\r\n\r\nDEF\r\n\r\n\r\nGHI\r\n\r\n\r\n");
  }

  @Test
  void formattingWithIllegalLineBreaksShouldThrowException() {

    Executable testMethod =
        () ->
            new PluginParametersBuilder()
                .setMessage("ABC\n\n\nDEF\r\r\rGHI\r\n\r\n\r\n", null)
                .setFormatting("UTF-8", "\t")
                .createPluginParameters();

    var thrown = assertThrows(FailureException.class, testMethod);

    assertAll(
        () ->
            assertThat(
                thrown.getMessage(),
                is(
                    equalTo(
                        "LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [9]"))),
        () -> verifyNoMoreInteractions(pluginLog));
  }
}
