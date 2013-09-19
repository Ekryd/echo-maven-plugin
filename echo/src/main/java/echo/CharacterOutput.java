package echo;

/**
 * @author bjorn
 * @since 2013-08-09
 */
public class CharacterOutput {
    private final char[] messageChars;
    private String output;

    public CharacterOutput(String message) {
        this.messageChars = message.toCharArray();
        generateOutput();
    }

    private void generateOutput() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("[");
        for (char messageChar : messageChars) {
            sb.append("['").append(messageChar).append("' , ").append((int) messageChar).append(" ").append("],");
        }
        sb.append("]");

        output = sb.toString();
    }

    public String getOutput() {
        return output;
    }
}
