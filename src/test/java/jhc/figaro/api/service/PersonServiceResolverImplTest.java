package jhc.figaro.api.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

public class PersonServiceResolverImplTest {

    @Test
    public void testSetGetPersonServices() {
        // Test not set
        PersonServiceResolver resolver = new PersonServiceResolverImpl();
        assertTrue("PersonServices not set", resolver.getPersonServices().isEmpty());

        // Set map
        Map<String, PersonService> personServices = new HashMap<String, PersonService>();
        resolver.setPersonServices(personServices);
        assertThat(resolver.getPersonServices(), not(nullValue()));
        assertThat(resolver.getPersonServices(), is(personServices));
    }

    @Test
    public void testGetPersonService() {
        // set up map of PersonServices
        Map<String, PersonService> personServices = new HashMap<String, PersonService>();
        PersonService personServiceV1 = Mockito.mock(PersonService.class);
        PersonService personServiceV2 = Mockito.mock(PersonService.class);
        personServices.put("v1.personService", personServiceV1);
        personServices.put("v2.personService", personServiceV2);

        PersonServiceResolver resolver = new PersonServiceResolverImpl();
        resolver.setPersonServices(personServices);
        assertThat(resolver.getPersonService("v1"), is(personServiceV1));
        assertThat(resolver.getPersonService("V1"), is(personServiceV1));
        assertThat(resolver.getPersonService("V2"), is(personServiceV2));
    }

    /*
     * Test the version service with a null version argument.
     */
    @Test
    public void testGetPersonServiceNullVersionArg() {
        PersonServiceResolverImpl resolver = new PersonServiceResolverImpl();
        VersionService versionService = Mockito.mock(VersionService.class);
        resolver.setVersionService(versionService);
        // test this returns the current version.
        resolver.getPersonService(null);
        verify(versionService).getCurrentVersion();
    }

    /*
     * Test setting and getting the VersionService.
     */
    @Test
    public void testSetVersionService() {
        PersonServiceResolverImpl resolver = new PersonServiceResolverImpl();
        assertThat(resolver.getVersionService(), nullValue());
        VersionService versionService = Mockito.mock(VersionService.class);
        resolver.setVersionService(versionService);
        assertThat(resolver.getVersionService(), equalTo(versionService));
    }

}
