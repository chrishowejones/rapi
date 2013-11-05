package jhc.figaro.api.resources;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import jhc.figaro.api.TestGenerator;
import jhc.figaro.api.model.People;
import jhc.figaro.api.model.Person;
import jhc.figaro.api.model.impl.v1.PeopleV1;
import jhc.figaro.api.model.impl.v1.PersonV1;
import jhc.figaro.api.resource.framework.AtomLink;
import jhc.figaro.api.resource.framework.AtomRelationship;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class PeopleResourceLinkAssemblerTest {

    @Mock
    private UriInfo mockUriInfo;
    private TestGenerator generator;
    private MediaTypeResolverImpl resolver;

    @Before
    public void setUp() {
        generator = new TestGenerator();
        resolver = new MediaTypeResolverImpl();
    }

    @Test
    public void testAssemblePerson() {
        String uriRoot = "http://localhost:8080/rapi";
        // set up dummy person
        Person person = generator.generateMockPerson(10);
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri(uriRoot));

        // test assemble Person
        PeopleResourceLinkAssembler assembler = new PeopleResourceLinkAssembler();
        assembler.setMediaTypeResolver(resolver);
        PersonV1 assembledPerson = (PersonV1) assembler
                .assemblePerson(person, mockUriInfo, MediaType.APPLICATION_JSON);
        generator.assertPersonOK(assembledPerson, 10);
        generator.assertPersonLinksOK(assembledPerson, 10, MediaType.APPLICATION_JSON, uriRoot);
        assertThat(assembledPerson.hasAddresses(), equalTo(false));
    }

    @Test
    public void testAssemblePersonHasAddresses() {
        String uriRoot = "http://localhost:8080/rapi";
        // set up dummy person
        Person person = generator.generateMockPerson(10);
        ((PersonV1)person).setHasAddresses(true);
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri(uriRoot),UriBuilder.fromUri(uriRoot));

        // test assemble Person
        PeopleResourceLinkAssembler assembler = new PeopleResourceLinkAssembler();
        assembler.setMediaTypeResolver(resolver);
        PersonV1 assembledPerson = (PersonV1) assembler
                .assemblePerson(person, mockUriInfo, MediaType.APPLICATION_JSON);
        generator.assertPersonOK(assembledPerson, 10);
        generator.assertPersonLinksOK(assembledPerson, 10, MediaType.APPLICATION_JSON, uriRoot);
        assertThat(assembledPerson.hasAddresses(), equalTo(true));
        assertThat(assembledPerson.getLinks().size(), equalTo(2));
        assertThat(assembledPerson.getLinks().get(1).getRel(), equalTo(AtomRelationship.ADDRESSES.toString()));
        assertThat(assembledPerson.getLinks().get(1).getHref(), equalTo(uriRoot + "/people/code10/addresses"));
     
    }

    @Test
    public void testAssemblePeopleList() {
        String uriRoot = "http://localhost:8080/rapi";
        int startIndex = 1;

        PeopleResourceLinkAssembler assembler = new PeopleResourceLinkAssembler();
        assembler.setMediaTypeResolver(resolver);
        List<? extends Person> personsList = generator.generateMockPersonList(2, startIndex);
        // inject mock
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri(uriRoot)).thenReturn(
                UriBuilder.fromUri(uriRoot));

        // test assembling list
        List<? extends Person> assembledPeopleList = assembler.assemblePeopleList(personsList, mockUriInfo,
                MediaType.APPLICATION_JSON);
        assertThat(assembledPeopleList.size(), equalTo(2));
        generator.assertPersonListOK(assembledPeopleList, startIndex);
        generator.assertPersonListLinksOK(assembledPeopleList, startIndex, MediaType.APPLICATION_JSON, uriRoot);
    }

    @Test
    public void testAssemblePeople() {
        // set up data
        String uriRoot = "http://localhost:8080/rapi";
        int startIndex = 1;
        int totalElements = 2;
        int pageSize = 10;
        PeopleResourceLinkAssembler assembler = new PeopleResourceLinkAssembler();
        assembler.setMediaTypeResolver(resolver);
        // test for persons list
        List<PersonV1> personsList = generator.generateMockPersonList(totalElements, startIndex);
        // prepare mock uriInfo
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri(uriRoot)).thenReturn(
                UriBuilder.fromUri(uriRoot));
        // Generate persons list and use that to generate a People object without links
        Pageable pageRequest = new PageRequest(0, pageSize);
        PeopleV1 persons = generator.generateMockPeople(personsList, pageRequest, totalElements);
        // test assembling People object
        People<? extends Person> assembledPeopleObject = assembler.assemblePeople(persons, mockUriInfo,
                MediaType.APPLICATION_JSON);
        generator.assertPeopleOK(assembledPeopleObject, pageSize, 0, totalElements, 1, totalElements, startIndex);
        List<? extends Person> assembledPeopleList = assembledPeopleObject.getPersonList();
        generator.assertPersonListLinksOK(assembledPeopleList, startIndex, MediaType.APPLICATION_JSON, uriRoot);
    }

    @Test
    public void testAssemblePeopleTestPageLinksPageZero() {
        // set up data
        String uriRoot = "http://localhost:8080/rapi";
        int startIndex = 1;
        long totalElements = 6l;
        int elements = 5;
        int pageSize = 5;
        int page = 0;
        PeopleResourceLinkAssembler assembler = new PeopleResourceLinkAssembler();
        assembler.setMediaTypeResolver(resolver);
        // test for persons list
        List<PersonV1> personsList = generator.generateMockPersonList(elements, startIndex);
        // prepare mock uriInfo
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri(uriRoot), UriBuilder.fromUri(uriRoot),
                UriBuilder.fromUri(uriRoot), UriBuilder.fromUri(uriRoot), UriBuilder.fromUri(uriRoot),
                UriBuilder.fromUri(uriRoot));
        // Generate persons list and use that to generate a People object without links
        Pageable pageRequest = new PageRequest(page, pageSize);
        People<? extends Person> persons = generator.generateMockPeople(personsList, pageRequest, totalElements);
        // test assembling People object
        People<? extends Person> assembledPeopleObject = assembler.assemblePeople(persons, mockUriInfo,
                MediaType.APPLICATION_JSON);
        // assert People page links OK
        assertThat(assembledPeopleObject.getLinks(), notNullValue());
        assertThat(assembledPeopleObject.getLinks(), hasSize(1));
        AtomLink nextPageLink = assembledPeopleObject.getLinks().get(0);
        assertThat(nextPageLink.getRel(), equalTo("next"));
        assertThat(nextPageLink.getHref(), equalTo(uriRoot + "/people?page=1"));
    }

    @Test
    public void testAssemblePeopleTestPageLinksTwoPagesLastPage() {
        // set up data
        String uriRoot = "http://localhost:8080/rapi";
        int startIndex = 1;
        long totalElements = 6l;
        int elements = 5;
        int pageSize = 5;
        int page = 1;
        PeopleResourceLinkAssembler assembler = new PeopleResourceLinkAssembler();
        assembler.setMediaTypeResolver(resolver);
        // test for persons list
        List<PersonV1> personsList = generator.generateMockPersonList(elements, startIndex);
        // prepare mock uriInfo
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri(uriRoot), UriBuilder.fromUri(uriRoot),
                UriBuilder.fromUri(uriRoot), UriBuilder.fromUri(uriRoot), UriBuilder.fromUri(uriRoot),
                UriBuilder.fromUri(uriRoot));
        // Generate persons list and use that to generate a People object without links
        Pageable pageRequest = new PageRequest(page, pageSize);
        PeopleV1 persons = generator.generateMockPeople(personsList, pageRequest, totalElements);
        // test assembling People object
        People<? extends Person> assembledPeopleObject = assembler.assemblePeople(persons, mockUriInfo,
                MediaType.APPLICATION_JSON);
        // assert People page links OK
        assertThat(assembledPeopleObject.getLinks(), notNullValue());
        assertThat(assembledPeopleObject.getLinks(), hasSize(1));
        AtomLink prevPageLink = assembledPeopleObject.getLinks().get(0);
        assertThat(prevPageLink.getRel(), equalTo("prev"));
        assertThat(prevPageLink.getHref(), equalTo(uriRoot + "/people?page=0"));
    }

    @Test
    public void testAssemblePeopleTestPageLinksPrevAndNext() {
        // set up data
        String uriRoot = "http://localhost:8080/rapi";
        int startIndex = 1;
        long totalElements = 11l;
        int elements = 5;
        int pageSize = 5;
        int page = 1;
        PeopleResourceLinkAssembler assembler = new PeopleResourceLinkAssembler();
        assembler.setMediaTypeResolver(resolver);
        // test for persons list
        List<PersonV1> personsList = generator.generateMockPersonList(elements, startIndex);
        // prepare mock uriInfo
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri(uriRoot), UriBuilder.fromUri(uriRoot),
                UriBuilder.fromUri(uriRoot), UriBuilder.fromUri(uriRoot), UriBuilder.fromUri(uriRoot),
                UriBuilder.fromUri(uriRoot));
        // Generate persons list and use that to generate a People object without links
        Pageable pageRequest = new PageRequest(page, pageSize);
        People<? extends Person> persons = generator.generateMockPeople(personsList, pageRequest, totalElements);
        // test assembling People object
        People<? extends Person> assembledPeopleObject = assembler.assemblePeople(persons, mockUriInfo,
                MediaType.APPLICATION_JSON);
        // assert People page links OK
        assertThat(assembledPeopleObject.getLinks(), notNullValue());
        assertThat(assembledPeopleObject.getLinks(), hasSize(2));
        AtomLink nextPageLink = assembledPeopleObject.getLinks().get(0);
        assertThat(nextPageLink.getRel(), equalTo("next"));
        assertThat(nextPageLink.getHref(), equalTo(uriRoot + "/people?page=2"));
        AtomLink prevPageLink = assembledPeopleObject.getLinks().get(1);
        assertThat(prevPageLink.getRel(), equalTo("prev"));
        assertThat(prevPageLink.getHref(), equalTo(uriRoot + "/people?page=0"));
    }

    @Test
    public void testAssemblePeopleTestPageLinksOnePage() {
        // set up data
        String uriRoot = "http://localhost:8080/rapi";
        int startIndex = 1;
        long totalElements = 5l;
        int elements = 5;
        int pageSize = 5;
        int page = 0;
        PeopleResourceLinkAssembler assembler = new PeopleResourceLinkAssembler();
        assembler.setMediaTypeResolver(resolver);
        // test for persons list
        List<PersonV1> personsList = generator.generateMockPersonList(elements, startIndex);
        // prepare mock uriInfo
        when(mockUriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri(uriRoot), UriBuilder.fromUri(uriRoot),
                UriBuilder.fromUri(uriRoot), UriBuilder.fromUri(uriRoot), UriBuilder.fromUri(uriRoot),
                UriBuilder.fromUri(uriRoot));
        // Generate persons list and use that to generate a People object without links
        Pageable pageRequest = new PageRequest(page, pageSize);
        People<? extends Person> persons = generator.generateMockPeople(personsList, pageRequest, totalElements);
        // test assembling People object
        People<? extends Person> assembledPeopleObject = assembler.assemblePeople(persons, mockUriInfo,
                MediaType.APPLICATION_JSON);
        // assert People page links OK
        assertThat(assembledPeopleObject.getLinks(), nullValue());
    }
    
    @Test
    public void testSetMediaTypeResolver() {
    	PeopleResourceLinkAssembler assembler = new PeopleResourceLinkAssembler();
    	assertThat(assembler.getMediaTypeResolver(), nullValue());
    	
    	MediaTypeResolver resolver = Mockito.mock(MediaTypeResolver.class);
    	assembler.setMediaTypeResolver(resolver);
    	assertThat(assembler.getMediaTypeResolver(), equalTo(resolver));
    }
}
