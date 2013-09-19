package echo;

import echo.output.EchoOutput;
import echo.output.Logger;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author bjorn
 * @since 2013-09-09
 */
public class TestLevel {

    private Logger logger = mock(Logger.class);;
    private final EchoOutput echoOutput = mock(EchoOutput.class);

    @Test
    public void infoLevelShouldBeDefault() {
        EchoPlugin echoPlugin = new EchoPlugin();

        PluginParameters parameters = new PluginParametersBuilder().setMessage("Björn").createPluginParameters();
        echoPlugin.setup(logger, parameters, echoOutput);
        echoPlugin.echo();
        
        verify(echoOutput).info("Björn");
    }
    
    @Test
    public void errorLevelShouldOutputOnErrorLevel() throws Exception {
        EchoPlugin echoPlugin = new EchoPlugin();

        PluginParameters parameters = new PluginParametersBuilder().setLevel("ErRoR").setMessage("Björn").createPluginParameters();
        echoPlugin.setup(logger, parameters, echoOutput);
        echoPlugin.echo();
        
        verify(echoOutput).error("Björn");
    }
    
    @Test
    public void warnLevelShouldOutputOnWarningLevel() throws Exception {
        EchoPlugin echoPlugin = new EchoPlugin();

        PluginParameters parameters = new PluginParametersBuilder().setLevel("WARNiNg").setMessage("Björn").createPluginParameters();
        echoPlugin.setup(logger, parameters, echoOutput);
        echoPlugin.echo();
        
        verify(echoOutput).warning("Björn");
    }
    
    
    @Test
    public void infoLevelShouldOutputOnInfoLevel() throws Exception {
        EchoPlugin echoPlugin = new EchoPlugin();

        PluginParameters parameters = new PluginParametersBuilder().setLevel("infO").setMessage("Björn").createPluginParameters();
        echoPlugin.setup(logger, parameters, echoOutput);
        echoPlugin.echo();
        
        verify(echoOutput).info("Björn");
    }
    
    
    @Test
    public void debugLevelShouldOutputOnDebugLevel() throws Exception {
        EchoPlugin echoPlugin = new EchoPlugin();

        PluginParameters parameters = new PluginParametersBuilder().setLevel("deBug").setMessage("Björn").createPluginParameters();
        echoPlugin.setup(logger, parameters, echoOutput);
        echoPlugin.echo();
        
        verify(echoOutput).debug("Björn");
    }
    
    
    @Test
    public void verboseLevelShouldOutputOnVerbosesLevel() throws Exception {
        EchoPlugin echoPlugin = new EchoPlugin();

        PluginParameters parameters = new PluginParametersBuilder().setLevel("verBOSE").setMessage("Björn").createPluginParameters();
        echoPlugin.setup(logger, parameters, echoOutput);
        echoPlugin.echo();
        
        verify(echoOutput).verbose("Björn");
    }
    
    
    
}
