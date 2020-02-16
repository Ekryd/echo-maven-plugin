package echo;

import echo.exception.ExceptionHandler;
import echo.exception.FailureException;
import echo.output.MavenEchoOutput;
import echo.output.MavenPluginLog;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * Mojo (Maven plugin) that outputs messages during Maven build.
 *
 * @author Bjorn Ekryd
 */
@Mojo(name = "echo", threadSafe = true, defaultPhase = LifecyclePhase.INITIALIZE, inheritByDefault = false, requiresProject = false)
@SuppressWarnings({"UnusedDeclaration"})
class EchoMojo extends AbstractMojo {
    /**
     * The message text that should be echoed
     */
    @Parameter(property = "echo.message")
    private String message;

    /**
     * If the message fetched from a file (or URL) instead of message tag
     */
    @Parameter(property = "echo.fromFile")
    private String fromFile;

    /**
     * The default path for fromFile and toFile.
     * The fromFile will be read relative to this path.
     * The toFile will be created relative to this path. READ-ONLY
     */
    @Parameter(defaultValue = "${basedir}", readonly = true)
    private File basePath;

    /**
     * If the message should be sent to a file instead of standard output
     */
    @Parameter(property = "echo.toFile")
    private String toFile;

    /**
     * If the message should be appended to the toFile instead of opening a new file/overwrite an existing file
     */
    @Parameter(property = "echo.append", defaultValue = "false")
    private boolean append;

    /**
     * Overwrite read-only destination files
     */
    @Parameter(property = "echo.force", defaultValue = "false")
    private boolean force;

    /**
     * Which output level the message should have. The following values are available 'FAIL', 'ERROR',  'WARNING', 'INFO', and 'DEBUG'
     */
    @Parameter(property = "echo.level", defaultValue = "INFO")
    private String level;

    /**
     * Encoding for the messages.
     */
    @Parameter(property = "echo.encoding", defaultValue = "UTF-8")
    private String encoding;

    /**
     * Line separator messages. Can be either \n, \r or \r\n
     */
    @Parameter(property = "echo.lineSeparator", defaultValue = "${line.separator}")
    private String lineSeparator;


    /**
     * Debug flag that outputs the message as a character list
     */
    @Parameter(property = "echo.characterOutput", defaultValue = "false")
    boolean characterOutput;

    /**
     * Set this to 'true' to bypass echo plugin
     */
    @Parameter(property = "echo.skip", defaultValue = "false")
    private boolean skip;

    private MavenPluginLog mavenLogger;
    private MavenEchoOutput echoOutput;
    private EchoPlugin echoPlugin;

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
            mavenLogger.info("Skipping echo-maven-plugin");
        } else {
            setup();
            echo();
        }
    }

    private void initLoggers() {
        mavenLogger = new MavenPluginLog(getLog());
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
