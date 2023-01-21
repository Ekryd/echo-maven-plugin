package echo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import echo.output.EchoOutput;
import echo.output.PluginLog;
import echo.parameter.PluginParametersBuilder;
import org.junit.jupiter.api.Test;

class TestCharacterOutput {

  private final PluginLog pluginLog = mock(PluginLog.class);
  private final EchoOutput echoOutput = mock(EchoOutput.class);

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
