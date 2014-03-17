package echo.mojoharness;

import echo.EchoMojo;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author bjorn
 * @since 2014-03-11
 */
public class EchoMojoHarnessTest 
{
    @Rule
    public MojoRule rule = new MojoRule()
    {
      @Override
      protected void before() throws Throwable 
      {
      }

      @Override
      protected void after()
      {
      }
    };

    @Test
    public void testSomething()
        throws Exception
    {
        //String basedir = System.getProperty("basedir");
        //System.setProperty("basedir", basedir + "/maven-plugin");
        EchoMojo echoMojo = (EchoMojo) rule.lookupMojo("echo", "src/test/resources/pom.xml");
        assertNotNull(echoMojo);
        echoMojo.execute();
    }
}
