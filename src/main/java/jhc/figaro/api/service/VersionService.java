package jhc.figaro.api.service;

/**
 * Version service that lists the supported versions of the RESTful API.
 * 
 * @author Chris Howe-Jones
 * 
 */
public interface VersionService {

    /**
     * Indicates if the version is supported.
     * 
     * @param version
     *            - to be checked
     * @return true if supported, false otherwise.
     */
    boolean isSupportedVersion(String version);

    /**
     * Returns a normalised String representing the version number. Makes leading 'V' lowercase and strips off
     * superfluous zeroes and dot separators:
     * 
     * <p>
     * <ul>
     * 'V1.', 'v1.0' and 'v01.0.00' all become 'v1'
     * </ul>
     * </p>
     * 
     * @param version
     *            - un-normalised version.
     * @return normalised version.
     */
    String normaliseVersion(String version);

    /**
     * Return an array of the supported REST versions.
     * 
     * @return versions supported.
     */
    String[] getSupportedVersions();

    /**
     * Indicates if the version passed as an argument is valid. Valid versions follow the format:
     * <p>
     * <ul>
     * First character is 'v'
     * </ul>
     * <ul>
     * Up to 3 version levels separated by a '.'
     * </ul>
     * <ul>
     * Each version level is indicated by a number with higher numbers indicating more current versions
     * </ul>
     * 
     * The regular expression for this pattern is - [Vv][1-9]{1,4}([\\.]([0-9]){1,4}){0,2}
     * 
     * </p>
     * <p>
     * For example, the following are valid versions:
     * <ul>
     * v1
     * </ul>
     * <ul>
     * v1.1
     * </ul>
     * <ul>
     * v1.0.1
     * </ul>
     * </p>
     * 
     * @param version
     *            - version to validate.
     * @return true if version is valid, false otherwise.
     */
    boolean isValidVersion(String version);

    /**
     * Returns the identifier for the current version assuming that the current version is the highest version number
     * supported. Versions can support sub version and sub sub version.
     * 
     * Copes with versions up 999.999.999.
     * 
     * @return current version.
     */
    String getCurrentVersion();

}