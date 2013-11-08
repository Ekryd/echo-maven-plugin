package echo.parameter;

/**
 * Contains all parameters that are sent to the plugin
 */
public class PluginParameters {

    public final String message;
    public final String fromFile;
    public final String toFile;
    public final boolean appended;
    public final boolean force;
    public final OutputLevelType level;
    public final String encoding;
    public final LineSeparator lineSeparator;
    public final boolean characterOutput;

    public PluginParameters(String message, String fromFile, String toFile, boolean appended, boolean force, OutputLevelType level,
                            String encoding, LineSeparator lineSeparator, boolean characterOutput) {
        this.message = message;
        this.fromFile = fromFile;
        this.toFile = toFile;
        this.appended = appended;
        this.force = force;
        this.level = level;
        this.encoding = encoding;
        this.lineSeparator = lineSeparator;
        this.characterOutput = characterOutput;
    }

    @Override
    public String toString() {
        return "PluginParameters{" +
                "appended=" + appended +
                ", message='" + message + '\'' +
                ", fromFile=" + fromFile +
                ", toFile=" + toFile +
                ", force=" + force +
                ", level='" + level + '\'' +
                ", encoding='" + encoding + '\'' +
                ", lineSeparator='" + lineSeparator + '\'' +
                ", characterOutput=" + characterOutput +
                '}';
    }
}
