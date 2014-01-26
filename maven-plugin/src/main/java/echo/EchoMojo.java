package echo;

import echo.exception.ExceptionHandler;
import echo.exception.FailureException;
import echo.output.MavenEchoOutput;
import echo.output.MavenLogger;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;

import java.util.Map;

/**
 * Mojo (Maven plugin) that outputs messages during Maven build.
 *
 * @author Bjorn Ekryd
 * @goal echo
 * @threadSafe true
 */
@SuppressWarnings({"UnusedDeclaration", "JavaDoc"})
public class EchoMojo extends AbstractMojo {
    /**
     * The message text that should be echoed
     *
     * @parameter expression="${echo.message}"
     */
    private String message;

    /**
     * If the message fetched from a file instead of message tag
     *
     * @parameter expression="${echo.fromFile}"
     */
    private String fromFile;

    /**
     * If the message should be sent to a file instead of standard output
     *
     * @parameter expression="${echo.toFile}"
     */
    private String toFile;

    /**
     * If the message should be appended to the toFile instead of opening a new file/overwrite an existing file
     *
     * @parameter expression="${echo.fromFile}" default-value="false"
     */
    private boolean appended;

    /** Overwrite read-only destination files */
    private boolean force;

    /**
     * Which output level the message should have. The following values are available 'ERROR',  'WARNING', 'INFO', 'VERBOSE' and 'DEBUG'
     *
     * @parameter expression="${echo.fromFile}" default-value="INFO"
     */
    private String level;

    /**
     * Encoding for the messages.
     *
     * @parameter expression="${echo.encoding}" default-value="UTF-8"
     */
    private String encoding;

    /**
     * Line separator messages. Can be either \n, \r or \r\n
     *
     * @parameter expression="${echo.lineSeparator}"
     * default-value="${line.separator}"
     */
    private String lineSeparator;


    /**
     * Debug flag that outputs the message as a character list
     *
     * @parameter expression="${echo.characterOutput}" default-value="false"
     */
    private boolean characterOutput;

    private EchoPlugin echoPlugin;

    public EchoMojo() {
    }

    /**
     * Execute plugin.
     *
     * @throws org.apache.maven.plugin.MojoFailureException
     *          exception that will be handled by plugin framework
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoFailureException {
        setup();
        echo();
        print(getPluginContext());
    }

    private void print(Map pluginContext) {
        System.out.println("hepp");
        for (Object key : pluginContext.keySet()) {
            System.out.println(key + " : " + pluginContext.get(key));
        }

    }

    void setup() throws MojoFailureException {
        try {
            PluginParameters pluginParameters = new PluginParametersBuilder()
                    .setMessage(message, fromFile)
                    .setFile(toFile, appended, force)
                    .setLevel(level)
                    .setFormatting(encoding, lineSeparator)
                    .setDebug(characterOutput)
                    .createPluginParameters();

            echoPlugin = new EchoPlugin(new MavenLogger(getLog()), pluginParameters, new MavenEchoOutput(getLog()));
        } catch (FailureException fex) {
            new ExceptionHandler(fex).throwMojoFailureException();
        }
    }

    void echo() throws MojoFailureException {
        try {
            echoPlugin.echo();
        } catch (FailureException fex) {
            new ExceptionHandler(fex).throwMojoFailureException();
        }
    }

}
