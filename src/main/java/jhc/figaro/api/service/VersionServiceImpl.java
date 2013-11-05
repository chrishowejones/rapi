package jhc.figaro.api.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Version service that lists the supported versions of the RESTful API.
 * 
 * @author Chris Howe-Jones
 * 
 */
@Service
public class VersionServiceImpl implements VersionService {

    private static final Logger LOG = LoggerFactory.getLogger(VersionServiceImpl.class);

    private String[] supportedVersions;

    /*
     * (non-Javadoc)
     * 
     * @see jhc.figaro.api.service.VersionService#getSupportedVersions()
     */
    @Override
    public final String[] getSupportedVersions() {
        return supportedVersions;
    }

    /**
     * Set the supported REST versions. Supported versions array cannot be null and each version must conform to the
     * pattern:
     * 
     * Starts with 'v' or 'V' Must have a major version between 0 and 999. Optionally can have a minor version between 0
     * and 999 separated from the major version by a dot. Optionally can have a 'fix' version between 0 and 999
     * separated from the minor version by a dot.
     * 
     * 
     * @param supportedVersions
     *            - array of supported versions.
     * @throws IllegalArgumentException
     *             - if the any supported version in the array is not a valid version
     * 
     */
    @Value("${rapi.supportedVersions:}")
    public final void setSupportVersions(final String[] supportedVersions) throws IllegalArgumentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("setSupportVersions(" + Arrays.toString(supportedVersions) + ")");
        }
        // check valid version
        String[] workingSupportedVersions = supportedVersions;
        for (int i = 0; i < workingSupportedVersions.length; i++) {
            if (!isValidVersion(workingSupportedVersions[i])) {
                throw new IllegalArgumentException("'" + workingSupportedVersions[i]
                        + "' is not a valid version. Must conform to pattern: v[0-9].[0-9|].[0-9|]");
            }
            workingSupportedVersions[i] = workingSupportedVersions[i].toLowerCase();
        }
        Arrays.sort(workingSupportedVersions, new VersionComparable());
        this.supportedVersions = workingSupportedVersions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jhc.figaro.api.service.VersionService#isValidVersion(java.lang.String)
     */
    @Override
    public final boolean isValidVersion(final String version) {
        boolean isValid = version.matches("[Vv][0-9]{1,4}([\\.]([0-9]){1,4}){0,2}");
        return isValid;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jhc.figaro.api.service.VersionService#getCurrentVersion()
     */
    @Override
    public final String getCurrentVersion() {
        if (LOG.isTraceEnabled()) {
            LOG.trace("getCurrentVersion()");
        }
        String currentVersion = supportedVersions[supportedVersions.length - 1];
        return currentVersion;
    }

    /**
     * Comparator for comparing versions up to 3 levels deep separated by a dot and with a maximum of 4 numeric
     * characters at each level.
     * 
     * @author Chris Howe-Jones
     * 
     */
    public class VersionComparable implements Comparator<String> {

        /**
         * Compare version string removing any leading 'v' character.
         * 
         * @param version1
         *            - first version to compare.
         * @param version2
         *            - second version to compare.
         * @return negative if version1 < version2, 0 if equal to each other and positive if version1 > version 2.
         */
        @Override
        public final int compare(final String version1, final String version2) {
            // normalise to compare
            return normaliseVersionForComparison(version1).compareTo(normaliseVersionForComparison(version2));
        }
    }

    /**
     * Normalises the version String using a default separator of '.', a maximum number of characters between the
     * separator of 4 (maxWidth) and a maximum number of levels in the version of 3. So a valid version string might
     * look like:
     * 
     * 9999.9999.9999
     * 
     * 
     * @param version
     *            - version to normalise.
     * @return normalised version.
     */
    public static final String normaliseVersionForComparison(final String version) {
        return normaliseVersionForComparison(version, ".", 4, 3);
    }

    /**
     * Normalises the version string by removing any leading 'v' character (case insensitive) splitting the string into
     * levels up to the maximum level specified then removing any leading zeroes and padding each level string to the
     * maximum width with spaces.
     * <p>
     * Hence calling with a separator of '.', maxLevels of 3 and maxWidth of 4 will result in a return String of 3
     * spaces + '1' + 7 spaces + '1' for any of the following version strings:
     * </p>
     * <p>
     * <ul>
     * '1.0.1'
     * </ul>
     * <ul>
     * 'v1.0.1'
     * </ul>
     * <ul>
     * '001.0.1'
     * </ul>
     * <ul>
     * '1.0000.1'
     * </ul>
     * <ul>
     * 'V1.00.001'
     * </ul>
     * </p>
     * 
     * @param version
     *            - version string to normalise
     * @param separator
     *            - separating character indicating a new version level.
     * @param maxWidth
     *            - maximum characters allowed per version level.
     * @param maxLevels
     *            - maximum level of sub-versions e.g. 3 would allow 1.1.1 with a separator of '.'.
     * @return normalised version string minus separators, right padded to the maxWidth per level with spaces and with
     *         no leading zeroes. Always returns a String of length maxLevels * maxWidth.
     */
    public static final String normaliseVersionForComparison(final String version, final String separator,
            final int maxWidth, final int maxLevels) {
        // remove any leading 'v'
        String versionMinusV = version.replaceFirst("^[Vv]+", "");
        // split by separator
        String[] split = Pattern.compile(separator, Pattern.LITERAL).split(versionMinusV);
        if (split.length > maxLevels) {
            throw new IllegalArgumentException("Can only support " + maxLevels + " of subversions.");
        }
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            if (s.length() > maxWidth) {
                throw new IllegalArgumentException("Can only support " + maxWidth + " characters per version level.");
            }
            // remove leading zeros (including 0 on it's own)
            s = s.replaceFirst("^0+", " ");
            // pad with leading spaces to maxWidth
            sb.append(String.format("%" + maxWidth + 's', s));
        }
        String normalisedVersion = sb.toString();
        if (normalisedVersion.length() < (maxLevels * maxWidth)) {
            // pad right with trailing spaces
            normalisedVersion = String.format("%1$-" + (maxLevels * maxWidth) + 's', normalisedVersion);
        }
        return normalisedVersion;
    }

    @Override
    public final boolean isSupportedVersion(final String version) {
        boolean isSupported = false;
        for (String supportedVersion : supportedVersions) {
            if (normaliseVersionForComparison(supportedVersion).equals(normaliseVersionForComparison(version))) {
                isSupported = true;
            }
        }
        return isSupported;
    }

    @Override
    public final String normaliseVersion(final String version) {
        String regEx = "(?<=[0-9])(\\.[0]+)(?![\\.[0-9][1-9]])";
        String regEx2 = "(?<=[0-9])(\\.[0]*)$";
        String workingVersion = version.replaceAll(regEx, "");
        workingVersion = workingVersion.replaceAll(regEx2, "");
        workingVersion = workingVersion.toLowerCase();
        return workingVersion;
    }
}
