package echo;

import org.junit.Test;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * @author bjorn
 * @since 2013-08-09
 */
public class EchoMojoTest {
    @Test
    public void testChars() throws UnsupportedEncodingException {
        System.out.println("Björn");
        System.out.println(new CharacterOutput("å").getOutput());
        PrintStream out = new PrintStream(System.out, true, "UTF-8");
        out.println("Björn");
    }
}
