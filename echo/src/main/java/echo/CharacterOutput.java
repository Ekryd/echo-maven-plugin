package echo;

import echo.parameter.PluginParameters;

/**
 * @author bjorn
 * @since 2013-08-09
 */
class CharacterOutput {
    private final boolean writeOutput;
    private final String message;

    private boolean firstCharacter = true;
    private final StringBuilder outputStringBuilder = new StringBuilder();
    private String output;

    public CharacterOutput(PluginParameters pluginParameters) {
        writeOutput = pluginParameters.characterOutput;
        message = pluginParameters.message;
    }

    public String getOutput() {
        if (output == null) {
            generateOutput(message.toCharArray());
        }
        return output;
    }

    private void generateOutput(char[] messageChars) {
        outputStringBuilder.append("[");
        for (char messageChar : messageChars) {
            appendOneCharOutput(messageChar);
        }
        outputStringBuilder.append("]");

        output = outputStringBuilder.toString();
    }

    private void appendOneCharOutput(char messageChar) {
        if (firstCharacter) {
            firstCharacter = false;
        } else {
            outputStringBuilder.append(",");
        }
        outputStringBuilder.append("['").append(messageChar).append("' , ").append((int) messageChar).append(" ").append("]");
    }

    public boolean isWriteOutput() {
        return writeOutput;
    }
}
