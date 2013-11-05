package jhc.figaro.api.resources;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import javax.ws.rs.core.UriInfo;

import jhc.figaro.api.TestGenerator;
import jhc.figaro.api.model.People;
import jhc.figaro.api.model.Person;
import jhc.figaro.api.model.impl.v1.PeopleV1;
import jhc.figaro.api.model.impl.v1.PersonV1;
import jhc.figaro.api.resource.framework.Linkable;
import jhc.figaro.api.service.PersonService;
import jhc.figaro.api.service.PersonServiceResolver;

import org.codehaus.jettison.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import com.sun.jersey.api.core.ResourceContext;

@RunWith(MockitoJUnitRunner.class)
public class PeopleResourceControllerTest {

    @Mock
    private PersonService mockPersonService;
    @Mock
    private PersonServiceResolver mockPersonServiceResolver;
    @Mock
    private UriInfo mockUriInfo;
    private UriBuilder mockUriBuilder;
    private PeopleResourceLinkAssembler mockPeopleAssembler;
    private TestGenerator generator;
    private final String defaultPage = "0";
    private MediaTypeResolverImpl resolver;

    @Before
    public void setUp() {
        // set up request context
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("com.sun.jersey.config.property.packages", "jhc.figaro.api.resources</param-value");
        mockUriBuilder = UriBuilder.fromUri("/");
        mockPeopleAssembler = new PeopleResourceLinkAssembler();
        resolver = new MediaTypeResolverImpl();
        mockPeopleAssembler.setMediaTypeResolver(resolver);
        generator = new TestGenerator();
    }

    @Test
    public void testGetPersonById() throws IllegalArgumentException, UriBuilderException, URISyntaxException,
            SecurityException, NoSuchMethodException {
        int personIndex = 10;
        Person mockPerson = generator.generateMockPerson(personIndex);
        // set up person to be returned from mock service
        when(mockPersonService.getPersonByCode(Integer.toString(personIndex))).thenReturn(mockPerson);
        // set up person service resolver
        when(mockPersonServiceResolver.getPersonService("v1")).thenReturn(mockPersonService);
        // set up UriInfo
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(mockUriBuilder);

        // instantiate PersonResource
        PeopleResourceController peopleResource = new PeopleResourceController();
        // inject mock person resolver
        peopleResource.setPersonServiceResolver(mockPersonServiceResolver);
        // inject mock UriInfo
        peopleResource.setUriInfo(mockUriInfo);
        // set up assembler
        peopleResource.setPeopleAssembler(mockPeopleAssembler);
        // invoke getPersonById
        PersonV1 actualPerson = (PersonV1) peopleResource.getPersonByCode("10", MediaType.APPLICATION_JSON.toString(),
                "v1");
        generator.assertPersonOK(actualPerson, personIndex);
        generator.assertPersonLinksOK(actualPerson, personIndex);
    }

    @Test
    public void testGetPersonByIdXML() throws IllegalArgumentException, UriBuilderException, URISyntaxException,
            SecurityException, NoSuchMethodException {
        Person mockPerson = generator.generateMockPerson(10);
        // set up person to be returned from mock service
        when(mockPersonService.getPersonByCode("10")).thenReturn(mockPerson);
        // set up person service resolver
        when(mockPersonServiceResolver.getPersonService("v1")).thenReturn(mockPersonService);
        // set up UriInfo
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(mockUriBuilder);

        // instantiate PersonResource
        PeopleResourceController peopleResource = new PeopleResourceController();
        // inject mock person resolver
        peopleResource.setPersonServiceResolver(mockPersonServiceResolver);
        // set up assembler
        peopleResource.setPeopleAssembler(mockPeopleAssembler);
        // invoke getPersonByIdXML
        peopleResource.setUriInfo(mockUriInfo);
        Linkable actualPerson = peopleResource.getPersonByCode("10", MediaType.APPLICATION_XML.toString(), "v1");
        generator.assertPersonLinksOK(actualPerson, 10, MediaType.APPLICATION_XML);
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testGetPeople() throws URISyntaxException, IllegalArgumentException, SecurityException,
            NoSuchMethodException {
        long totalElements = 2;
        List<PersonV1> mockPersonList = generator.generateMockPersonList((int) totalElements, 1);
        int pageSize = 2;
        PageRequest pageRequest = new PageRequest(0, pageSize);
        People people = generator.generateMockPeople(mockPersonList, pageRequest, totalElements);
        // set up person to be returned from mock service
        when(mockPersonService.getPeople(Integer.parseInt(defaultPage))).thenReturn(people);
        // set up person service resolver
        when(mockPersonServiceResolver.getPersonService("v1")).thenReturn(mockPersonService);
        // set up UriInfo
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri("/"), UriBuilder.fromUri("/"));

        // instantiate PersonResource
        PeopleResourceController peopleResource = new PeopleResourceController();
        // inject mock person resolver
        peopleResource.setPersonServiceResolver(mockPersonServiceResolver);
        // set up assembler
        peopleResource.setPeopleAssembler(mockPeopleAssembler);
        // set up mock UriInfo
        peopleResource.setUriInfo(mockUriInfo);
        // invoke getPersonById
        Response actualResponse = peopleResource.getPeople(null, defaultPage, MediaType.APPLICATION_JSON);
        assertThat(actualResponse, notNullValue());
        PeopleV1 peopleResponse = (PeopleV1) actualResponse.getEntity();
        List<PersonV1> actualResponseList = peopleResponse.getPersonList();
        assertThat(actualResponseList, is(equalTo(mockPersonList)));
        assertThat(actualResponseList.size(), is(equalTo(2)));
        // check links for people
        generator.assertPeopleOK(peopleResponse, 10, 0, 2, 1, 2, 1);
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testGetPeoplePaged() throws URISyntaxException, IllegalArgumentException, SecurityException,
            NoSuchMethodException {
        int pageSize = 3;
        int pageNumber = 0;
        List<PersonV1> mockPersonList = generator.generateMockPersonList(3, 1);
        Pageable pageRequest = new PageRequest(pageNumber, pageSize, Direction.ASC, "personCode");
        People people = generator.generateMockPeople(mockPersonList, pageRequest, 10l);
        // set up person to be returned from mock service
        when(mockPersonService.getPeople(pageNumber)).thenReturn(people);
        // set up person service resolver
        when(mockPersonServiceResolver.getPersonService("v1")).thenReturn(mockPersonService);
        // set up UriInfo
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri("/"), UriBuilder.fromUri("/"),
                UriBuilder.fromUri("/"), UriBuilder.fromUri("/"));

        // instantiate PersonResource
        PeopleResourceController peopleResource = new PeopleResourceController();
        // inject mock person resolver
        peopleResource.setPersonServiceResolver(mockPersonServiceResolver);
        // set up assembler
        peopleResource.setPeopleAssembler(mockPeopleAssembler);
        // set up mock UriInfo
        peopleResource.setUriInfo(mockUriInfo);
        // invoke getPersonById
        Response actualResponse = peopleResource.getPeople(null, Integer.toString(pageNumber),
                MediaType.APPLICATION_JSON);
        PeopleV1 peopleResponse = (PeopleV1) actualResponse.getEntity();
        generator.assertPeopleOK(peopleResponse, pageSize, Integer.valueOf(pageNumber), 10l, 4, 3, 1);
        // check links for people
        int personIndex = 1;
        for (PersonV1 person : peopleResponse.getPersonList()) {
            generator.assertPersonLinksOK(person, personIndex++);
        }
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testGetPeoplePagedSecondPage() throws URISyntaxException, IllegalArgumentException, SecurityException,
            NoSuchMethodException {
        int pageSize = 3;
        int pageNumber = 1;
        List<PersonV1> mockPersonList = generator.generateMockPersonList(3, 4);
        Pageable pageRequest = new PageRequest(pageNumber, pageSize, Direction.ASC, "personCode");
        People people = generator.generateMockPeople(mockPersonList, pageRequest, 10l);
        // set up person to be returned from mock service
        when(mockPersonService.getPeople(pageNumber)).thenReturn(people);
        // set up person service resolver
        when(mockPersonServiceResolver.getPersonService("v1")).thenReturn(mockPersonService);
        // set up UriInfo
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri("/"), UriBuilder.fromUri("/"),
                UriBuilder.fromUri("/"), UriBuilder.fromUri("/"));
        // instantiate PersonResource
        PeopleResourceController peopleResource = new PeopleResourceController();
        // inject mock person resolver
        peopleResource.setPersonServiceResolver(mockPersonServiceResolver);
        // set up assembler
        peopleResource.setPeopleAssembler(mockPeopleAssembler);
        // set up mock UriInfo
        peopleResource.setUriInfo(mockUriInfo);

        // invoke getPersonById
        Response actualResponse = peopleResource.getPeople(null, Integer.toString(pageNumber),
                MediaType.APPLICATION_JSON);
        PeopleV1 peopleResponse = (PeopleV1) actualResponse.getEntity();
        generator.assertPeopleOK(peopleResponse, pageSize, pageNumber, 10l, 4, 3, 4);
        // check links for people
        int personIndex = 4;
        for (PersonV1 person : peopleResponse.getPersonList()) {
            generator.assertPersonLinksOK(person, personIndex++);
        }
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testGetPeoplePagedLastPage() throws URISyntaxException, IllegalArgumentException, SecurityException,
            NoSuchMethodException {
        int pageSize = 3;
        int pageNumber = 2;
        List<PersonV1> mockPersonList = generator.generateMockPersonList(3, 7);
        Pageable pageRequest = new PageRequest(pageNumber, pageSize, Direction.ASC, "personCode");
        People people = generator.generateMockPeople(mockPersonList, pageRequest, 10l);
        // set up person to be returned from mock service
        when(mockPersonService.getPeople(pageNumber)).thenReturn(people);
        // set up person service resolver
        when(mockPersonServiceResolver.getPersonService("v1")).thenReturn(mockPersonService);
        // set up UriInfo
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri("/"), UriBuilder.fromUri("/"),
                UriBuilder.fromUri("/"), UriBuilder.fromUri("/"));
        // instantiate PersonResource
        PeopleResourceController peopleResource = new PeopleResourceController();
        // inject mock person resolver
        peopleResource.setPersonServiceResolver(mockPersonServiceResolver);
        // set up assembler
        peopleResource.setPeopleAssembler(mockPeopleAssembler);
        // set up mock UriInfo
        peopleResource.setUriInfo(mockUriInfo);

        // invoke getPersonById
        Response actualResponse = peopleResource.getPeople(null, Integer.toString(pageNumber),
                MediaType.APPLICATION_JSON);
        PeopleV1 peopleResponse = (PeopleV1) actualResponse.getEntity();
        generator.assertPeopleOK(peopleResponse, pageSize, pageNumber, 10l, 4, 3, 7);
        // check links for people
        int personIndex = 7;
        for (PersonV1 person : peopleResponse.getPersonList()) {
            generator.assertPersonLinksOK(person, personIndex++);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testGetPeoplePagedBeyondPage() throws URISyntaxException, IllegalArgumentException, SecurityException,
            NoSuchMethodException {
        int pageNumber = 25;
        // set up person to be returned from mock service
        when(mockPersonService.getPeople(pageNumber)).thenReturn((People) new PeopleV1(new ArrayList<PersonV1>()));
        // set up person service resolver
        when(mockPersonServiceResolver.getPersonService("v1")).thenReturn(mockPersonService);
        // instantiate PersonResource
        PeopleResourceController peopleResource = new PeopleResourceController();
        // inject mock person resolver
        peopleResource.setPersonServiceResolver(mockPersonServiceResolver);
        peopleResource.setUriInfo(mockUriInfo);

        // invoke getPersonById
        Response actualResponse = peopleResource.getPeople(null, Integer.toString(pageNumber),
                MediaType.APPLICATION_JSON);
        assertThat(actualResponse.getEntity(), nullValue());
        assertThat(actualResponse.getStatus(), equalTo(204));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testSearchPeople() throws URISyntaxException {
        List<PersonV1> mockPersonList = generator.generateMockPersonList(2, 1);
        String searchTerm = "search";
        People people = generator.generateMockPeople(mockPersonList, new PageRequest(0, 10), 2);
        // set up person to be returned from mock service
        when(mockPersonService.getPersonBySearch(searchTerm, Integer.parseInt(defaultPage))).thenReturn(people);
        // set up person service resolver
        when(mockPersonServiceResolver.getPersonService("v1")).thenReturn(mockPersonService);
        // set up UriInfo
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri("/"), UriBuilder.fromUri("/"));

        // instantiate PersonResource
        PeopleResourceController peopleResource = new PeopleResourceController();
        // inject mock person resolver
        peopleResource.setPersonServiceResolver(mockPersonServiceResolver);
        // set up assembler
        peopleResource.setPeopleAssembler(mockPeopleAssembler);
        // set up mock UriInfo
        peopleResource.setUriInfo(mockUriInfo);
        // invoke getPeople
        Response actualResponse = peopleResource.getPeople(searchTerm, defaultPage, MediaType.APPLICATION_JSON);
        PeopleV1 peopleResponse = (PeopleV1) actualResponse.getEntity();
        assertThat(peopleResponse.getPersonList(), is(equalTo(mockPersonList)));

    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testSearchPeopleNotFound() throws URISyntaxException {
        String searchTerm = "search";
        People people = null;
        // set up person to be returned from mock service
        when(mockPersonService.getPersonBySearch(searchTerm, Integer.parseInt(defaultPage))).thenReturn(people);
        // set up person service resolver
        when(mockPersonServiceResolver.getPersonService("v1")).thenReturn(mockPersonService);
        // set up UriInfo
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri("/"), UriBuilder.fromUri("/"));

        // instantiate PersonResource
        PeopleResourceController peopleResource = new PeopleResourceController();
        // inject mock person resolver
        peopleResource.setPersonServiceResolver(mockPersonServiceResolver);
        // set up assembler
        peopleResource.setPeopleAssembler(mockPeopleAssembler);
        // set up mock UriInfo
        peopleResource.setUriInfo(mockUriInfo);
        // invoke getPeople
        Response actualResponse = peopleResource.getPeople(searchTerm, defaultPage, MediaType.APPLICATION_JSON);
        assertThat(actualResponse.getStatus(), equalTo(Response.Status.NO_CONTENT.getStatusCode()));
        assertThat(actualResponse.getEntity(), nullValue());
    }

    @Test
    public void testNewPerson() throws IllegalArgumentException, SecurityException, NoSuchMethodException {
        PersonV1 person1 = generator.generateMockPerson();
        // set up mocks
        // set up person to be returned from mock service
        when(mockPersonService.newPerson(person1)).thenReturn(person1);
        // set up person service resolver
        when(mockPersonServiceResolver.getPersonService("v1")).thenReturn(mockPersonService);
        // set up UriInfo
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri("/"));
        when(mockUriInfo.getRequestUriBuilder()).thenReturn(UriBuilder.fromPath("/people/10"));

        // call update
        PeopleResourceController peopleResource = new PeopleResourceController();
        // inject mock person resolver
        peopleResource.setPersonServiceResolver(mockPersonServiceResolver);
        // inject mock uriInfo
        peopleResource.setUriInfo(mockUriInfo);
        // inject assembler
        peopleResource.setPeopleAssembler(mockPeopleAssembler);

        Response response = peopleResource.newPerson(person1, MediaType.APPLICATION_JSON);
        assertThat(response, notNullValue());
        assertThat(response.getStatus(), is(Status.CREATED.getStatusCode()));
        assertThat(response.getEntity(), notNullValue());
        PersonV1 newPerson = (PersonV1) response.getEntity();
        assertThat(newPerson, is(person1));
        generator.assertPersonLinksOK(newPerson, 1);

    }

    @Test
    public void testUpdatePerson() {
        PersonV1 person1 = generator.generateMockPerson();
        // set up mocks
        // set up person to be returned from mock service
        when(mockPersonService.isModified(person1)).thenReturn(true);
        when(mockPersonService.update(person1)).thenReturn(person1);
        // set up person service resolver
        when(mockPersonServiceResolver.getPersonService("v1")).thenReturn(mockPersonService);
        // set up UriInfo
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri("/"));

        // call update
        PeopleResourceController peopleResource = new PeopleResourceController();
        // inject mock person resolver
        peopleResource.setPersonServiceResolver(mockPersonServiceResolver);
        // inject mock uriInfo
        peopleResource.setUriInfo(mockUriInfo);
        // inject assembler
        peopleResource.setPeopleAssembler(mockPeopleAssembler);

        Response response = peopleResource.updatePerson("code1", person1, MediaType.APPLICATION_JSON);
        assertThat(response, notNullValue());
        assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
        assertThat(response.getEntity(), notNullValue());
        PersonV1 updatedPerson = (PersonV1) response.getEntity();
        assertThat(updatedPerson, is(person1));
        generator.assertPersonLinksOK(updatedPerson, 1);
    }

    @Test
    public void testUpdatePersonNotModified() {
        PersonV1 person1 = generator.generateMockPerson();
        // set up mocks
        // set up person to be returned from mock service
        when(mockPersonService.isModified(person1)).thenReturn(false);
        // set up person service resolver
        when(mockPersonServiceResolver.getPersonService("v1")).thenReturn(mockPersonService);

        // call update
        PeopleResourceController peopleResource = new PeopleResourceController();
        // inject mock person resolver
        peopleResource.setPersonServiceResolver(mockPersonServiceResolver);
        Response response = peopleResource.updatePerson("1", person1, MediaType.APPLICATION_JSON);
        assertThat(response, notNullValue());
        assertThat(response.getStatus(), is(Status.NOT_MODIFIED.getStatusCode()));
        assertThat(response.getEntity(), nullValue());
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testGetPeopleAsJSONArray() throws URISyntaxException {
        List<PersonV1> mockPersonList = generator.generateMockPersonList(2, 1);
        People people = new PeopleV1(mockPersonList);
        // set up person list to be returned from mock service
        when(mockPersonService.getPeople()).thenReturn(people);
        // set up person service resolver
        when(mockPersonServiceResolver.getPersonService("v1")).thenReturn(mockPersonService);
        // set up UriInfo
        when(mockUriInfo.getRequestUriBuilder()).thenReturn(mockUriBuilder);

        // instantiate PersonResource
        PeopleResourceController peopleResource = new PeopleResourceController();
        // inject mock person resolver
        peopleResource.setPersonServiceResolver(mockPersonServiceResolver);
        peopleResource.setUriInfo(mockUriInfo);
        // invoke getPersonById
        JSONArray jsonArray = peopleResource.getPeopleAsJsonArray();
        System.out.println(jsonArray.toString());
    }
    
    @Test
    public void testSetAddressResourceController() {
    	PeopleResourceController controller = new PeopleResourceController();
    	// set AddressResourceController
    	AddressResourceController addressController = new AddressResourceController();
    	controller.setAddressResourceController(addressController);
    	assertThat(controller.getAddressResourceController(), equalTo(addressController));
    	
    }

}
