package org.codehaus.groovy.grails.io.support;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

/**
 * {@link org.springframework.core.io.Resource} implementation for <code>java.net.URL</code> locators.
 * Obviously supports resolution as URL, and also as File in case of
 * the "file:" protocol.
 *
 * @author Juergen Hoeller
 * @since 28.12.2003
 * @see java.net.URL
 */
public class UrlResource extends AbstractFileResolvingResource {

    /**
     * Original URL, used for actual access.
     */
    private final URL url;

    /**
     * Cleaned URL (with normalized path), used for comparisons.
     */
    private final URL cleanedUrl;

    /**
     * Original URI, if available; used for URI and File access.
     */
    private final URI uri;


    /**
     * Create a new UrlResource.
     * @param url a URL
     */
    public UrlResource(URL url) {
        this.url = url;
        this.cleanedUrl = getCleanedUrl(this.url, url.toString());
        this.uri = null;
    }

    /**
     * Create a new UrlResource.
     * @param uri a URI
     * @throws java.net.MalformedURLException if the given URL path is not valid
     */
    public UrlResource(URI uri) throws MalformedURLException {
        this.url = uri.toURL();
        this.cleanedUrl = getCleanedUrl(this.url, uri.toString());
        this.uri = uri;
    }

    /**
     * Create a new UrlResource.
     * @param path a URL path
     * @throws MalformedURLException if the given URL path is not valid
     */
    public UrlResource(String path) throws MalformedURLException {
        this.url = new URL(path);
        this.cleanedUrl = getCleanedUrl(this.url, path);
        this.uri = null;
    }

    /**
     * Determine a cleaned URL for the given original URL.
     * @param originalUrl the original URL
     * @param originalPath the original URL path
     * @return the cleaned URL
     * @see org.springframework.util.StringUtils#cleanPath
     */
    private URL getCleanedUrl(URL originalUrl, String originalPath) {
        try {
            return new URL(GrailsResourceUtils.cleanPath(originalPath));
        }
        catch (MalformedURLException ex) {
            // Cleaned URL path cannot be converted to URL
            // -> take original URL.
            return originalUrl;
        }
    }


    /**
     * This implementation opens an InputStream for the given URL.
     * It sets the "UseCaches" flag to <code>false</code>,
     * mainly to avoid jar file locking on Windows.
     * @see java.net.URL#openConnection()
     * @see java.net.URLConnection#setUseCaches(boolean)
     * @see java.net.URLConnection#getInputStream()
     */
    public InputStream getInputStream() throws IOException {
        URLConnection con = this.url.openConnection();
        useCachesIfNecessary(con);
        try {
            return con.getInputStream();
        }
        catch (IOException ex) {
            // Close the HTTP connection (if applicable).
            if (con instanceof HttpURLConnection) {
                ((HttpURLConnection) con).disconnect();
            }
            throw ex;
        }
    }

    private static void useCachesIfNecessary(URLConnection con) {
        con.setUseCaches(con.getClass().getName().startsWith("JNLP"));
    }

    /**
     * This implementation returns the underlying URL reference.
     */
    public URL getURL() throws IOException {
        return this.url;
    }

    /**
     * This implementation returns the underlying URI directly,
     * if possible.
     */
    public URI getURI() throws IOException {
        if (this.uri != null) {
            return this.uri;
        }
        else {
            return getFile().toURI();
        }
    }

    /**
     * This implementation returns a File reference for the underlying URL/URI,
     * provided that it refers to a file in the file system.
     * @see org.springframework.util.ResourceUtils#getFile(java.net.URL, String)
     */
    @Override
    public File getFile() throws IOException {
        if (this.uri != null) {
            return super.getFile(this.uri);
        }
        else {
            return super.getFile();
        }
    }

    /**
     * This implementation creates a UrlResource, applying the given path
     * relative to the path of the underlying URL of this resource descriptor.
     * @see java.net.URL#URL(java.net.URL, String)
     */
    public Resource createRelative(String relativePath)  {
        if (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }
        try {
            return new UrlResource(new URL(this.url, relativePath));
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * This implementation returns the name of the file that this URL refers to.
     * @see java.net.URL#getFile()
     * @see java.io.File#getName()
     */
    public String getFilename() {
        return new File(this.url.getFile()).getName();
    }

    /**
     * This implementation returns a description that includes the URL.
     */
    public String getDescription() {
        return "URL [" + this.url + "]";
    }


    /**
     * This implementation compares the underlying URL references.
     */
    @Override
    public boolean equals(Object obj) {
        return (obj == this ||
                (obj instanceof UrlResource && this.cleanedUrl.equals(((UrlResource) obj).cleanedUrl)));
    }

    /**
     * This implementation returns the hash code of the underlying URL reference.
     */
    @Override
    public int hashCode() {
        return this.cleanedUrl.hashCode();
    }

}
