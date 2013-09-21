package echo.output;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author bjorn
 * @since 2013-09-19
 */
public class MavenEchoOutputTest {

    @Test(expected = MojoFailureException.class)
    public void errorLevelShouldThrowException() throws Exception {
        try {
            new MavenEchoOutput(null).error("Gurkas");
            fail();
        } catch (Exception ex) {
            assertThat(ex.getMessage(), is("Gurkas"));
            throw ex;
        }

    }
}
