package echo;

import echo.exception.ExceptionHandler;
import echo.exception.FailureException;
import echo.output.MavenEchoOutput;
import echo.output.MavenLogger;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;

/**
 * Mojo (Maven plugin) that outputs messages during Maven build.
 *
 * @author Bjorn Ekryd
 * @goal echo
 * @threadSafe true
 * @requiresProject false
 * @inheritByDefault false
 * @phase initialize
 */
@SuppressWarnings({"UnusedDeclaration", "JavaDoc"})
class EchoMojo extends AbstractMojo {
    /**
     * The message text that should be echoed
     *
     * @parameter property="echo.message"
     */
    private String message;

    /**
     * If the message fetched from a file (or URL) instead of message tag
     *
     * @parameter property="echo.fromFile"
     */
    private String fromFile;

    /**
     * The default path for fromFile and toFile. 
     * The fromFile will be read relative to this path. 
     * The toFile will be created relative to this path. READ-ONLY
     *
     * @parameter default-value="${basedir}"
     * @readonly
     */
    private File basePath;

    /**
     * If the message should be sent to a file instead of standard output
     *
     * @parameter property="echo.toFile"
     */
    private String toFile;

    /**
     * If the message should be appended to the toFile instead of opening a new file/overwrite an existing file
     *
     * @parameter property="echo.append" default-value="false"
     */
    private boolean append;

    /**
     * Overwrite read-only destination files
     *
     * @parameter property="echo.force" default-value="false"
     */
    private boolean force;

    /**
     * Which output level the message should have. The following values are available 'FAIL', 'ERROR',  'WARNING', 'INFO', and 'DEBUG'
     *
     * @parameter property="echo.level" default-value="INFO"
     */
    private String level;

    /**
     * Encoding for the messages.
     *
     * @parameter property="echo.encoding" default-value="UTF-8"
     */
    private String encoding;

    /**
     * Line separator messages. Can be either \n, \r or \r\n
     *
     * @parameter property="echo.lineSeparator" default-value="${line.separator}"
     */
    private String lineSeparator;


    /**
     * Debug flag that outputs the message as a character list
     *
     * @parameter property="echo.characterOutput" default-value="false"
     */
    private boolean characterOutput;

    /**
     * Set this to 'true' to bypass echo plugin
     *
     * @parameter property="sort.skip" default-value="false"
     */
    private boolean skip;

    private MavenLogger mavenLogger;
    private MavenEchoOutput echoOutput;
    private EchoPlugin echoPlugin;

    public EchoMojo() {
    }

    /**
     * Execute plugin.
     *
     * @throws org.apache.maven.plugin.MojoFailureException exception that will be handled by plugin framework
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoFailureException {
        initLoggers();
        if (skip) {
            mavenLogger.info("Skipping echo-plugin");
        } else {
            setup();
            echo();
        }
    }

    private void initLoggers() {
        mavenLogger = new MavenLogger(getLog());
        echoOutput = new MavenEchoOutput(getLog());
    }

    void setup() throws MojoFailureException {

        try {
            PluginParameters pluginParameters = new PluginParametersBuilder()
                    .setMessage(message, fromFile)
                    .setFile(basePath, toFile, append, force)
                    .setLevel(level)
                    .setFormatting(encoding, lineSeparator)
                    .setDebug(characterOutput)
                    .createPluginParameters();

            echoPlugin = new EchoPlugin(mavenLogger, pluginParameters, echoOutput);
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
