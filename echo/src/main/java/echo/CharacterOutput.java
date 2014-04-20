package echo;

import echo.parameter.PluginParameters;

/**
 * Converts the message content as an user friendly character string
 *
 * @author bjorn
 * @since 2013-08-09
 */
class CharacterOutput {
    private final boolean writeOutput;

    private boolean firstCharacter = true;
    private StringBuilder outputStringBuilder;

    /**
     * Create a new instance of the CharacterOutput class
     *
     * @param pluginParameters The user-supplied plugin parameters
     */
    public CharacterOutput(PluginParameters pluginParameters) {
        writeOutput = pluginParameters.isCharacterOutput();
    }

    /** Returns message content as a debug string, ready to be output */
    public String getOutput(String message) {
        if (!writeOutput) {
            return "";
        }
        generateOutput(message.toCharArray());

        return outputStringBuilder.toString();
    }

    private void generateOutput(char[] messageChars) {
        outputStringBuilder = new StringBuilder();
        outputStringBuilder.append("[");
        for (char messageChar : messageChars) {
            appendOneCharOutput(messageChar);
        }
        outputStringBuilder.append("]");
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
