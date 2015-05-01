package echo.parameter;

import java.io.File;

/** Creates a PluginParameter instance using the Builder pattern */
public class PluginParametersBuilder {
    private String message;
    private String fromFile;
    private File basePath;
    private String toFile;
    private boolean appendToFile;
    private boolean force;
    private OutputLevelType level = OutputLevelType.INFO;
    private String encoding = "UTF-8";
    private LineSeparator lineSeparator = new LineSeparator("\n");
    private boolean characterOutput;

    /** Sets input to plugin, either from message or file */
    public PluginParametersBuilder setMessage(String message, String fromFile) {
        this.message = message;
        this.fromFile = fromFile;
        return this;
    }

    /** Sets file output for plugin */
    public PluginParametersBuilder setFile(File basePath, String toFile, boolean appendToFile, boolean force) {
        this.basePath = basePath;
        this.toFile = toFile;
        this.appendToFile = appendToFile;
        this.force = force;
        return this;
    }

    /** Sets message level fro plugin */
    public PluginParametersBuilder setLevel(String level) {
        this.level = OutputLevelType.fromString(level);
        return this;
    }

    /** Sets message formatting for plugin */
    public PluginParametersBuilder setFormatting(String encoding, String lineSeparatorString) {
        this.encoding = encoding;
        this.lineSeparator = new LineSeparator(lineSeparatorString);
        return this;
    }

    /** Set message content debug flag for plugin */
    public PluginParametersBuilder setDebug(boolean characterOutput) {
        this.characterOutput = characterOutput;
        return this;
    }

    /** Builds the PluginParameters instance */
    public PluginParameters createPluginParameters() {
        lineSeparator.checkLineSeparator();

        return new PluginParameters(message, fromFile, basePath, toFile, appendToFile, force, level, encoding, lineSeparator, characterOutput);
    }
}
