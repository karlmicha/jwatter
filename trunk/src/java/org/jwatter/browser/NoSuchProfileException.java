package org.jwatter.browser;

/**
 * Thrown when a specific browser profile is requested but the profile does not exist.
 */
public class NoSuchProfileException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new exception instance.
     * 
     * @param profileName
     *            the name of the profile that was not found
     */
    public NoSuchProfileException(String profileName) {
        super("browser profile does not exist: " + profileName);
    }
}
