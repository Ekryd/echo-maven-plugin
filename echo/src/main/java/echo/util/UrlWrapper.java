package echo.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Wraps the java URL class so that it exposes methods to determine if entered values are urls or not.
 *
 * @author bjorn
 * @since 2013-08-16
 */
class UrlWrapper {
    private final String spec;

    /** Creates a new UrlWrapper with a supplied url */
    public UrlWrapper(String spec) {
        this.spec = spec;
    }

    /** Returns true if the specified string is an URL */
    public boolean isUrl() {
        try {
            new URL(spec);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    /** Open an input stream to the location of the url */
    public InputStream openStream() throws IOException {
        URL url = new URL(spec);
        return url.openStream();
    }
}
