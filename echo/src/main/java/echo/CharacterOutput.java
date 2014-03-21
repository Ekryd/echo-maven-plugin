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
    private final String message;

    private boolean firstCharacter = true;
    private final StringBuilder outputStringBuilder = new StringBuilder();
    private String output;

    /**
     * Create a new instance of the CharacterOutput class
     *
     * @param pluginParameters The user-supplied plugin parameters
     */
    public CharacterOutput(PluginParameters pluginParameters) {
        writeOutput = pluginParameters.isCharacterOutput();
        message = pluginParameters.getMessage();
    }

    /** Returns message content as a debug string, ready to be output */
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
