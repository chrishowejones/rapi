package jhc.figaro.api.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class VersionServiceImplTest {

    @Test
    public void testSetSupportedVersions() {
        VersionServiceImpl versionService = new VersionServiceImpl();
        assertNull(versionService.getSupportedVersions());
        // set supported versions
        String[] testVersions = new String[] { "v1", "v1.1", "v2", "v2.2.2" };
        versionService.setSupportVersions(testVersions);
        assertThat(versionService.getSupportedVersions(), equalTo(testVersions));
    }

    @Test
    public void testSetSupportedVersionsUpperCase() {
        VersionServiceImpl versionService = new VersionServiceImpl();
        assertNull(versionService.getSupportedVersions());
        // set supported versions
        String[] testVersions = new String[] { "v1", "v1.1", "V2", "v2.2.2" };
        String[] testVersionsLowerCase = new String[] { "v1", "v1.1", "v2", "v2.2.2" };
        versionService.setSupportVersions(testVersions);
        assertThat(versionService.getSupportedVersions(), equalTo(testVersionsLowerCase));
    }

    @Test(expected = NullPointerException.class)
    public void testSetSupportedVersionsNull() {
        VersionServiceImpl versionService = new VersionServiceImpl();
        assertNull(versionService.getSupportedVersions());
        // set supported versions null
        String[] testVersions = null;
        versionService.setSupportVersions(testVersions);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testsetSupportedVersionsInvalidStart() {
        VersionServiceImpl versionService = new VersionServiceImpl();
        assertNull(versionService.getSupportedVersions());
        // set supported versions invalid
        String[] testVersions = { "1" };
        versionService.setSupportVersions(testVersions);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testsetSupportedVersionsInvalidStartChar() {
        VersionServiceImpl versionService = new VersionServiceImpl();
        assertNull(versionService.getSupportedVersions());
        // set supported versions invalid
        String[] testVersions = { "x1" };
        versionService.setSupportVersions(testVersions);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testsetSupportedVersionsInvalidVersionChars() {
        VersionServiceImpl versionService = new VersionServiceImpl();
        assertNull(versionService.getSupportedVersions());
        // set supported versions invalid
        String[] testVersions = { "vv1" };
        versionService.setSupportVersions(testVersions);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSupportedVersionsInvalidMajorVersion() {
        VersionServiceImpl versionService = new VersionServiceImpl();
        assertNull(versionService.getSupportedVersions());
        // set supported versions invalid
        String[] testVersions = { "v10000" };
        versionService.setSupportVersions(testVersions);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSupportedVersionsInvalidMinorVersion() {
        VersionServiceImpl versionService = new VersionServiceImpl();
        assertNull(versionService.getSupportedVersions());
        // set supported versions invalid
        String[] testVersions = { "v1.10000" };
        versionService.setSupportVersions(testVersions);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSupportedVersionsInvalidFixVersion() {
        VersionServiceImpl versionService = new VersionServiceImpl();
        assertNull(versionService.getSupportedVersions());
        // set supported versions invalid
        String[] testVersions = { "v1.1.10000" };
        versionService.setSupportVersions(testVersions);
    }

    @Test
    public void testSetSupportedVersionsValidPatterns() {
        VersionServiceImpl versionService = new VersionServiceImpl();
        assertNull(versionService.getSupportedVersions());
        String[] testVersions = { "v1.0.0", "v999", "v999.999.999", "v999.999", "v1.0", "v1.1.0" };
        versionService.setSupportVersions(testVersions);
    }

    /*
     * Test getting the current version. Current version is assumed to be the highest number. Ensure this works for
     * version numbers with sub versions and sub-sub versions
     */
    @Test
    public void testGetCurrentVersionWholeVersions() {
        // set supported versions]
        String[] testVersions = new String[] { "v1", "v2" };
        VersionServiceImpl versionService = new VersionServiceImpl();
        versionService.setSupportVersions(testVersions);
        assertThat(versionService.getCurrentVersion(), equalTo("v2"));
    }

    /*
     * Test getting the current version. Current version is assumed to be the highest number. Ensure this works for
     * version numbers with sub versions and sub-sub versions
     */
    @Test
    public void testGetCurrentVersionMinorVersions() {
        // set supported versions]
        String[] testVersions = new String[] { "v1.1", "v2" };
        VersionServiceImpl versionService = new VersionServiceImpl();
        versionService.setSupportVersions(testVersions);
        assertThat(versionService.getCurrentVersion(), equalTo("v2"));
        testVersions = new String[] { "v1.1", "v2.1" };
        versionService.setSupportVersions(testVersions);
        assertThat(versionService.getCurrentVersion(), equalTo("v2.1"));
        testVersions = new String[] { "v2.1.13", "v2.2", "v2.1.12" };
        versionService.setSupportVersions(testVersions);
        assertThat(versionService.getCurrentVersion(), equalTo("v2.2"));
        testVersions = new String[] { "v2.1.13", "v2.2.99", "v2.1.12" };
        versionService.setSupportVersions(testVersions);
        assertThat(versionService.getCurrentVersion(), equalTo("v2.2.99"));
        testVersions = new String[] { "v13", "v2.2.99", "v2.1.12" };
        versionService.setSupportVersions(testVersions);
        assertThat(versionService.getCurrentVersion(), equalTo("v13"));
    }

    @Test
    public void testCompareToLessThan() {
        String s1 = VersionServiceImpl.normaliseVersionForComparison("1.0");
        String s2 = VersionServiceImpl.normaliseVersionForComparison("1.1");
        assertThat(s1.compareTo(s2), lessThan(0));
        s2 = "1.0.1";
        assertThat(s1.compareTo(s2), lessThan(0));
        s2 = "1.10.10";
        assertThat(s1.compareTo(s2), lessThan(0));
        s1 = "1";
        assertThat(s1.compareTo(s2), lessThan(0));
        s2 = "10";
        assertThat(s1.compareTo(s2), lessThan(0));
        s1 = "0.1";
        s2 = "0.10";
        assertThat(s1.compareTo(s2), lessThan(0));
        s1 = "0.0.1";
        s2 = "0.0.10";
        assertThat(s1.compareTo(s2), lessThan(0));
        s1 = "0.0.1";
        s2 = "0.1.0";
        assertThat(s1.compareTo(s2), lessThan(0));
        s1 = "0.0.1";
        s2 = "0.1";
        assertThat(s1.compareTo(s2), lessThan(0));
        s1 = "0.0.1";
        s2 = "1";
        assertThat(s1.compareTo(s2), lessThan(0));
    }

    @Test
    public void testCompareEqual() {
        VersionServiceImpl.VersionComparable comparable = new VersionServiceImpl().new VersionComparable();
        String s1 = "1.0";
        String s2 = "1";
        assertThat(comparable.compare(s1, s2), equalTo(0));
        s2 = "1.0.0";
        assertThat(comparable.compare(s1, s2), equalTo(0));
        s2 = "1.000.00";
        assertThat(comparable.compare(s1, s2), equalTo(0));
        s1 = "1";
        assertThat(comparable.compare(s1, s2), equalTo(0));
        s2 = "01";
        assertThat(comparable.compare(s1, s2), equalTo(0));
    }

    @Test
    public void testNormaliseVersionForComparison() {
        String version = "1/2/1";
        String normalisedVersion = VersionServiceImpl.normaliseVersionForComparison(version, "/", 4, 3);
        assertThat(normalisedVersion, equalTo("   1   2   1"));
        version = "1000_2_9999";
        normalisedVersion = VersionServiceImpl.normaliseVersionForComparison(version, "_", 4, 3);
        assertThat(normalisedVersion, equalTo("1000   29999"));
        version = "1000";
        normalisedVersion = VersionServiceImpl.normaliseVersionForComparison(version, "_", 4, 3);
        assertThat(normalisedVersion, equalTo("1000        "));
        version = "1000_0";
        normalisedVersion = VersionServiceImpl.normaliseVersionForComparison(version, "_", 4, 3);
        assertThat(normalisedVersion, equalTo("1000        "));

        version = "_1000";
        normalisedVersion = VersionServiceImpl.normaliseVersionForComparison(version, "_", 4, 3);
        assertThat(normalisedVersion, equalTo("    1000    "));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormaliseVersionToManyLevels() {
        // cannot support more than 3 levels
        String version = "1/2/1/2";
        VersionServiceImpl.normaliseVersionForComparison(version, "/", 4, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormaliseVersionForComparisonTooDeep() {
        String version = "0_10000";
        VersionServiceImpl.normaliseVersionForComparison(version, "_", 4, 3);
    }

    @Test
    public void testIsValidVersion() {
        VersionService service = new VersionServiceImpl();
        assertThat(service.isValidVersion("v1"), is(true));
        assertThat(service.isValidVersion("V1"), is(true));
        assertThat(service.isValidVersion("v1.1"), is(true));
        assertThat(service.isValidVersion("v1.0.1"), is(true));
        assertThat(service.isValidVersion("V1111"), is(true));
        assertThat(service.isValidVersion("v1.111"), is(true));
        assertThat(service.isValidVersion("v1.0.111"), is(true));
        assertThat(service.isValidVersion("v0.1"), is(true));
    }

    @Test
    public void testIsInvalidVersion() {
        VersionService service = new VersionServiceImpl();
        assertThat(service.isValidVersion("v1."), is(false));
        assertThat(service.isValidVersion("v1.1."), is(false));
        assertThat(service.isValidVersion("v1.0.1."), is(false));
        assertThat(service.isValidVersion("v1.0.a"), is(false));
        assertThat(service.isValidVersion("v1.0.1.1"), is(false));
        assertThat(service.isValidVersion("v11111"), is(false));
        assertThat(service.isValidVersion("1"), is(false));
        assertThat(service.isValidVersion("X1"), is(false));
        assertThat(service.isValidVersion("XX"), is(false));
        assertThat(service.isValidVersion("vv"), is(false));
    }

    @Test
    public void testNormaliseVersion() {
        VersionService service = new VersionServiceImpl();
        assertThat(service.normaliseVersion("V1.0"), equalTo("v1"));
        assertThat(service.normaliseVersion("V1.0.0"), equalTo("v1"));
        assertThat(service.normaliseVersion("v1.0.1"), equalTo("v1.0.1"));
    }

    @Test
    public void testIsSupportedVersion() {
        VersionServiceImpl service = new VersionServiceImpl();
        String[] testVersions = { "v1.0.0", "v999", "v999.999.999", "v999.999", "v1.0", "v1.1.0" };
        service.setSupportVersions(testVersions);
        assertThat(service.isSupportedVersion("V1"), equalTo(true));
        assertThat(service.isSupportedVersion("v1.0.0"), equalTo(true));
        assertThat(service.isSupportedVersion("v999"), equalTo(true));
        assertThat(service.isSupportedVersion("v999.0"), equalTo(true));
        assertThat(service.isSupportedVersion("v999.999"), equalTo(true));
        assertThat(service.isSupportedVersion("v999.999.999"), equalTo(true));
    }
}
