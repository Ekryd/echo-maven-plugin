package echo;

import static org.mockito.Mockito.*;

import echo.output.MavenEchoOutput;
import echo.output.MavenPluginLog;
import echo.parameter.PluginParametersBuilder;
import org.junit.jupiter.api.Test;

class TestMessage {
  private final MavenPluginLog pluginLog = mock(MavenPluginLog.class);

  @Test
  void stringWithSpecialCharactersShouldBeOutput() {
    var echoOutput = mock(MavenEchoOutput.class);

    var parameters =
        new PluginParametersBuilder().setMessage("Björn", null).createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
    echoPlugin.echo();

    verify(echoOutput).info("Björn");
  }

  @Test
  void emptyMessageShouldOutputNothing() {
    var echoOutput = mock(MavenEchoOutput.class);

    var parameters = new PluginParametersBuilder().setMessage("", null).createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
    echoPlugin.echo();

    verifyNoInteractions(echoOutput);
  }
}
