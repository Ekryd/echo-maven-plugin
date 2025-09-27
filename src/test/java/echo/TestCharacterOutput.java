package echo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import echo.output.MavenEchoOutput;
import echo.output.MavenPluginLog;
import echo.parameter.PluginParametersBuilder;
import org.junit.jupiter.api.Test;

class TestCharacterOutput {

  private final MavenPluginLog pluginLog = mock(MavenPluginLog.class);
  private final MavenEchoOutput echoOutput = mock(MavenEchoOutput.class);

  @Test
  void characterDebugOutputShouldOutputToInfoLevel() {
    var parameters =
        new PluginParametersBuilder()
            .setMessage("Gurka", null)
            .setLevel("DEBUG")
            .setDebug(true)
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

    echoPlugin.echo();

    verify(pluginLog).info("[['G' , 71 ],['u' , 117 ],['r' , 114 ],['k' , 107 ],['a' , 97 ]]");
  }
}
