package echo.parameter;

import java.io.File;

/**
 * Contains all parameters that are sent to the plugin
 */
public class PluginParameters {

    private final String message;
    private final String fromFile;
    private final File defaultOutputPath;
    private final String toFile;
    private final boolean appendToFile;
    private final boolean force;
    private final OutputLevelType level;
    private final String encoding;
    private final LineSeparator lineSeparator;
    private final boolean characterOutput;

    public PluginParameters(String message, String fromFile, File defaultOutputPath, String toFile, boolean appendToFile, boolean force, OutputLevelType level,
                            String encoding, LineSeparator lineSeparator, boolean characterOutput) {
        this.message = message;
        this.fromFile = fromFile;
        this.defaultOutputPath = defaultOutputPath;
        this.toFile = toFile;
        this.appendToFile = appendToFile;
        this.force = force;
        this.level = level;
        this.encoding = encoding;
        this.lineSeparator = lineSeparator;
        this.characterOutput = characterOutput;
    }

    public String getMessage() {
        return message;
    }

    public String getFromFile() {
        return fromFile;
    }

    public File getDefaultOutputPath() {
        return defaultOutputPath;
    }

    public String getToFile() {
        return toFile;
    }

    public boolean isAppendToFile() {
        return appendToFile;
    }

    public boolean isForce() {
        return force;
    }

    public OutputLevelType getLevel() {
        return level;
    }

    public String getEncoding() {
        return encoding;
    }

    public LineSeparator getLineSeparator() {
        return lineSeparator;
    }

    public boolean isCharacterOutput() {
        return characterOutput;
    }

    @Override
    public String toString() {
        return "PluginParameters{" +
                "appendToFile=" + appendToFile +
                ", message='" + message + '\'' +
                ", fromFile=" + fromFile +
                ", defaultOutputPath=" + defaultOutputPath +
                ", toFile=" + toFile +
                ", force=" + force +
                ", level='" + level + '\'' +
                ", encoding='" + encoding + '\'' +
                ", lineSeparator='" + lineSeparator + '\'' +
                ", characterOutput=" + characterOutput +
                '}';
    }

}
