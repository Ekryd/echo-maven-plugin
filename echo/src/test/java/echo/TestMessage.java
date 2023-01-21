package echo;

import static org.mockito.Mockito.*;

import echo.output.EchoOutput;
import echo.output.PluginLog;
import echo.parameter.PluginParametersBuilder;
import org.junit.jupiter.api.Test;

class TestMessage {
  private final PluginLog pluginLog = mock(PluginLog.class);

  @Test
  void stringWithSpecialCharactersShouldBeOutput() {
    var echoOutput = mock(EchoOutput.class);

    var parameters =
        new PluginParametersBuilder().setMessage("Björn", null).createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
    echoPlugin.echo();

    verify(echoOutput).info("Björn");
  }

  @Test
  void emptyMessageShouldOutputNothing() {
    var echoOutput = mock(EchoOutput.class);

    var parameters = new PluginParametersBuilder().setMessage("", null).createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
    echoPlugin.echo();

    verifyNoInteractions(echoOutput);
  }
}
