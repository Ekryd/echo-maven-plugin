package echo;

import echo.parameter.PluginParameters;

/** Converts the message content as a user-friendly character string */
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
    for (var messageChar : messageChars) {
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
    outputStringBuilder
        .append("['")
        .append(messageChar)
        .append("' , ")
        .append((int) messageChar)
        .append(" ")
        .append("]");
  }

  public boolean isWriteOutput() {
    return writeOutput;
  }
}
