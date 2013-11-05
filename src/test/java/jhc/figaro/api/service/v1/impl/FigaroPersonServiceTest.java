package jhc.figaro.api.service.v1.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import jhc.figaro.api.RapiMapper;
import jhc.figaro.api.TestGenerator;
import jhc.figaro.api.model.People;
import jhc.figaro.api.model.Person;
import jhc.figaro.api.model.impl.v1.PersonV1;
import jhc.figaro.api.persistence.AddressRepository;
import jhc.figaro.api.persistence.PersonRepository;
import jhc.figaro.api.persistence.domain.PersonDetails;
import jhc.figaro.api.resource.framework.Linkable;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@RunWith(MockitoJUnitRunner.class)
public class FigaroPersonServiceTest {

	@Mock
	private PersonRepository personRepo;

	@Mock
	private AddressRepository addressRepo;

	private RapiMapper mapper;

	private TestGenerator generator;

	@Before
	public void setUp() {
		mapper = new RapiMapper();
		generator = new TestGenerator();
	}

	@Test
	public void testGetPersonById() {
		String personCode = "code";
		String name = "name";
		String email = "email";

		// Mock details
		PersonDetails details = new PersonDetails();
		details.setPersonCode(personCode);
		details.setEmail(email);
		details.setName(name);

		// Mock repository
		when(personRepo.findOne(personCode)).thenReturn(details);
		when(addressRepo.countByPersonPersonCode(any(String.class))).thenReturn(0l);

		// Test
		FigaroPersonService service = new FigaroPersonService();
		service.setPersonRepository(personRepo);
		service.setRapiMapper(mapper);
		service.setAddressRepository(addressRepo);
		PersonV1 personReturned = service.getPersonByCode(personCode);
		assertThat(personReturned, is(notNullValue()));
		assertThat(personReturned.getCode(), is(equalTo((personCode))));
		assertThat(personReturned.getName(), is(equalTo(name)));
		assertThat(personReturned.getEmail(), is(equalTo(email)));
	}

	@Test
	public void testGetPersonByIdNull() {
		// Mock repository
		when(personRepo.findOne(null)).thenReturn(null);
		when(addressRepo.countByPersonPersonCode(any(String.class))).thenReturn(0l);

		// Test
		FigaroPersonService service = new FigaroPersonService();
		service.setPersonRepository(personRepo);
		service.setRapiMapper(mapper);
		service.setAddressRepository(addressRepo);
		Linkable personReturned = service.getPersonByCode(null);
		assertThat(personReturned, is(nullValue()));
	}

	@Test
	public void testGetPersonBySearchNoneFound() {
		String name = "fred";
		int pageSize = 10;

		// Mock details
		PageRequest pageRequest = new PageRequest(0, pageSize, new Sort(Direction.ASC, "name"));
		// Mock repository
		when(personRepo.findByNameLike(name, pageRequest)).thenReturn(null);

		// Test
		FigaroPersonService service = new FigaroPersonService();
		service.setPageSize(pageSize);
		service.setPersonRepository(personRepo);
		service.setRapiMapper(mapper);
		jhc.figaro.api.model.Page personsReturned = service.getPersonBySearch(name);
		assertThat(personsReturned, nullValue());
	}

	@Test
	public void testGetPersonBySearch() {
		String name = "name";
		int pageSize = 10;

		// Mock details
		List<PersonDetails> detailsList = generator.generateMockDetailsList(2, 1);
		PageRequest pageRequest = new PageRequest(0, pageSize, new Sort(Direction.ASC, "name"));
		Page<PersonDetails> detailsPage = new PageImpl<PersonDetails>(detailsList, pageRequest, 2);
		assertThat(detailsPage.getNumberOfElements(), is(2));
		// Mock repository
		when(personRepo.findByNameLike(name, pageRequest)).thenReturn(detailsPage);
		when(addressRepo.countByPersonPersonCode(any(String.class))).thenReturn(0l);

		// Test
		FigaroPersonService service = new FigaroPersonService();
		service.setPageSize(pageSize);
		service.setPersonRepository(personRepo);
		service.setRapiMapper(mapper);
		service.setAddressRepository(addressRepo);
		People personsReturned = service.getPersonBySearch(name);
		generator.assertPeopleOK(personsReturned, pageSize, 0, 2, 1, 2, 1);
	}

	@Test
	public void testGetPersonBySearchDefaultPageSize() {
		String name = "name";

		// Mock details
		List<PersonDetails> detailsList = generator.generateMockDetailsList(2, 1);
		PageRequest pageRequest = new PageRequest(0, FigaroPersonService.DEFAULT_PAGE_SIZE, new Sort(Direction.ASC,
				"name"));
		Page<PersonDetails> detailsPage = new PageImpl<PersonDetails>(detailsList, pageRequest, 2);
		// Mock repository
		when(personRepo.findByNameLike(eq(name), eq(pageRequest))).thenReturn(detailsPage);
		when(addressRepo.countByPersonPersonCode(any(String.class))).thenReturn(0l);

		// Test
		FigaroPersonService service = new FigaroPersonService();
		service.setPersonRepository(personRepo);
		service.setRapiMapper(mapper);
		service.setAddressRepository(addressRepo);
		People<? extends Person> personsReturned = service.getPersonBySearch(name);
		generator.assertPeopleOK(personsReturned, FigaroPersonService.DEFAULT_PAGE_SIZE, 0, 2, 1, 2, 1);
	}

	@Test
	public void testGetPersonBySearchPageable() {
		int pageSize = 5;
		String name = "name";

		// Mock details
		List<PersonDetails> detailsList = generator.generateMockDetailsList(5, 1);
		// load page with list
		PageRequest pageRequest = new PageRequest(0, pageSize, Direction.ASC, "name");
		Page<PersonDetails> detailsPage = new PageImpl<PersonDetails>(detailsList, pageRequest, 6);
		// Mock repository
		when(personRepo.findByNameLike(name, pageRequest)).thenReturn(detailsPage);
		when(addressRepo.countByPersonPersonCode(any(String.class))).thenReturn(0l);

		// Test
		FigaroPersonService service = new FigaroPersonService();
		service.setPageSize(pageSize);
		service.setPersonRepository(personRepo);
		service.setRapiMapper(mapper);
		service.setAddressRepository(addressRepo);
		People personsReturned = service.getPersonBySearch(name, pageRequest);
		generator.assertPeopleOK(personsReturned, pageSize, 0, 6l, 2, 5, 1);

		// request next page
		detailsList = generator.generateMockDetailsList(1, 6);
		pageRequest = new PageRequest(1, pageSize, Direction.ASC, "name");
		detailsPage = new PageImpl<PersonDetails>(detailsList, pageRequest, 6);
		// Mock repository
		when(personRepo.findByNameLike(name, pageRequest)).thenReturn(detailsPage);

		// Test page 2
		personsReturned = service.getPersonBySearch(name, pageRequest);
		generator.assertPeopleOK(personsReturned, pageSize, 1, 6l, 2, 1, 6);
	}

	@Test
	public void testGetPersonBySearchPageNumber() {
		int pageSize = 5;
		String name = "name";

		// Mock details
		List<PersonDetails> detailsList = generator.generateMockDetailsList(5, 1);
		// load page with list
		int pageNum = 0;
		PageRequest pageRequest = new PageRequest(0, pageSize, Direction.ASC, "name");
		Page<PersonDetails> detailsPage = new PageImpl<PersonDetails>(detailsList, pageRequest, 6);
		// Mock repository
		when(personRepo.findByNameLike(name, pageRequest)).thenReturn(detailsPage);
		when(addressRepo.countByPersonPersonCode(any(String.class))).thenReturn(0l);

		// Test
		FigaroPersonService service = new FigaroPersonService();
		service.setPageSize(pageSize);
		service.setPersonRepository(personRepo);
		service.setRapiMapper(mapper);
		service.setAddressRepository(addressRepo);
		People personsReturned = service.getPersonBySearch(name, pageNum);
		generator.assertPeopleOK(personsReturned, pageSize, pageNum, 6l, 2, 5, 1);

		// request next page
		pageNum++;
		detailsList = generator.generateMockDetailsList(1, 6);
		pageRequest = new PageRequest(pageNum, pageSize, Direction.ASC, "name");
		detailsPage = new PageImpl<PersonDetails>(detailsList, pageRequest, 6);
		// Mock repository
		when(personRepo.findByNameLike(name, pageRequest)).thenReturn(detailsPage);

		// Test page 2
		personsReturned = service.getPersonBySearch(name, pageRequest);
		generator.assertPeopleOK(personsReturned, pageSize, pageNum, 6l, 2, 1, 6);
	}

	@Test
	public void testGetPeopleLessThanPage() {
		int pageSize = 10;

		// Mock details
		List<PersonDetails> detailsList = generator.generateMockDetailsList(2, 1);
		// load page with list
		PageRequest pageRequest = new PageRequest(0, pageSize, Direction.ASC, "personCode");
		Page<PersonDetails> detailsPage = new PageImpl<PersonDetails>(detailsList, pageRequest, 2);
		// Mock repository
		when(personRepo.findAll(pageRequest)).thenReturn(detailsPage);
		when(addressRepo.countByPersonPersonCode(any(String.class))).thenReturn(0l);

		// Test
		FigaroPersonService service = new FigaroPersonService();
		service.setAddressRepository(addressRepo);
		service.setPageSize(pageSize);
		service.setPersonRepository(personRepo);
		service.setRapiMapper(mapper);
		People personsReturned = service.getPeople();
		generator.assertPeopleOK(personsReturned, pageSize, 0, 2, 1, 2, 1);
		assertThat(personsReturned, is(notNullValue()));
	}

	@Test
	public void testGetPeopleNoneFound() {
		int pageSize = 10;

		// empty details
		PageRequest pageRequest = new PageRequest(0, pageSize, Direction.ASC, "personCode");
		Page<PersonDetails> detailsPage = new PageImpl<PersonDetails>(new ArrayList<PersonDetails>(), pageRequest, 0);
		// Mock repository
		when(personRepo.findAll(pageRequest)).thenReturn(detailsPage);

		// Test
		FigaroPersonService service = new FigaroPersonService();
		service.setPageSize(pageSize);
		service.setPersonRepository(personRepo);
		service.setRapiMapper(mapper);
		jhc.figaro.api.model.Page personsReturned = service.getPeople();
		assertThat(personsReturned.getNumberOfElements(), equalTo(0));
	}

	@Test
	public void testGetPeopleDefaultPageSize() {
		List<PersonDetails> detailsList = generator.generateMockDetailsList(2, 1);
		// load page with list
		PageRequest pageRequest = new PageRequest(0, FigaroPersonService.DEFAULT_PAGE_SIZE, Direction.ASC, "personCode");
		Page<PersonDetails> detailsPage = new PageImpl<PersonDetails>(detailsList, pageRequest, 2);
		// Mock repository
		when(personRepo.findAll(pageRequest)).thenReturn(detailsPage);
		when(addressRepo.countByPersonPersonCode(any(String.class))).thenReturn(0l);

		// Test
		FigaroPersonService service = new FigaroPersonService();
		service.setAddressRepository(addressRepo);
		service.setPersonRepository(personRepo);
		service.setRapiMapper(mapper);
		People personsReturned = service.getPeople();
		generator.assertPeopleOK(personsReturned, FigaroPersonService.DEFAULT_PAGE_SIZE, 0, 2, 1, 2, 1);
	}

	@Test
	public void testGetPeopleMoreThanOnePage() {
		int pageSize = 5;

		List<PersonDetails> detailsList = generator.generateMockDetailsList(5, 1);
		// load page with list
		Sort sort = new Sort(Direction.ASC, "personCode");
		PageRequest pageRequest = new PageRequest(0, pageSize, sort);
		Page<PersonDetails> detailsPage = new PageImpl<PersonDetails>(detailsList, pageRequest, 6l);
		assertEquals(detailsPage.getNumberOfElements(), 5);
		// Mock repository
		when(personRepo.findAll(pageRequest)).thenReturn(detailsPage);
		when(addressRepo.countByPersonPersonCode(any(String.class))).thenReturn(0l);

		// Test
		FigaroPersonService service = new FigaroPersonService();
		service.setAddressRepository(addressRepo);
		service.setPersonRepository(personRepo);
		service.setRapiMapper(mapper);
		service.setPageSize(pageSize);
		People personsReturned = service.getPeople();
		generator.assertPeopleOK(personsReturned, pageSize, 0, 6l, 2, 5, 1);
	}

	@Test
	public void testGetPeoplePageable() {
		int pageSize = 5;

		// Mock details
		List<PersonDetails> detailsList = generator.generateMockDetailsList(5, 1);
		// load page with list
		PageRequest pageRequest = new PageRequest(0, pageSize, Direction.ASC, "personCode");
		Page<PersonDetails> detailsPage = new PageImpl<PersonDetails>(detailsList, pageRequest, 6);
		// Mock repository
		when(personRepo.findAll(pageRequest)).thenReturn(detailsPage);
		when(addressRepo.countByPersonPersonCode(any(String.class))).thenReturn(0l);

		// Test first page - page 0
		FigaroPersonService service = new FigaroPersonService();
		service.setAddressRepository(addressRepo);
		service.setPersonRepository(personRepo);
		service.setRapiMapper(mapper);
		service.setPageSize(pageSize);
		People personsReturned = service.getPeople(pageRequest);
		generator.assertPeopleOK(personsReturned, pageSize, 0, 6l, 2, 5, 1);

		// get next page - page 1
		detailsList = generator.generateMockDetailsList(1, 6);
		pageRequest = new PageRequest(1, pageSize, Direction.ASC, "personCode");
		detailsPage = new PageImpl<PersonDetails>(detailsList, pageRequest, 6);
		// Mock repository
		when(personRepo.findAll(pageRequest)).thenReturn(detailsPage);

		// Test second page
		personsReturned = service.getPeople(pageRequest);
		generator.assertPeopleOK(personsReturned, pageSize, 1, 6l, 2, 1, 6);
	}

	@Test
	public void testGetPeoplePageNumber() {
		int pageSize = 5;

		// Mock details
		List<PersonDetails> detailsList = generator.generateMockDetailsList(5, 1);
		// load page with list
		PageRequest pageRequest = new PageRequest(0, pageSize, Direction.ASC, "personCode");
		Page<PersonDetails> detailsPage = new PageImpl<PersonDetails>(detailsList, pageRequest, 6);
		// Mock repository
		when(personRepo.findAll(pageRequest)).thenReturn(detailsPage);
		when(addressRepo.countByPersonPersonCode(any(String.class))).thenReturn(0l);

		// Test first page - page 0
		FigaroPersonService service = new FigaroPersonService();
		service.setAddressRepository(addressRepo);
		service.setPersonRepository(personRepo);
		service.setRapiMapper(mapper);
		service.setPageSize(pageSize);
		People personsReturned = service.getPeople(0);
		generator.assertPeopleOK(personsReturned, pageSize, 0, 6l, 2, 5, 1);

		// get next page - page 1
		detailsList = generator.generateMockDetailsList(1, 6);
		pageRequest = new PageRequest(1, pageSize, Direction.ASC, "personCode");
		detailsPage = new PageImpl<PersonDetails>(detailsList, pageRequest, 6);
		// Mock repository
		when(personRepo.findAll(pageRequest)).thenReturn(detailsPage);

		// Test second page
		personsReturned = service.getPeople(1);
		generator.assertPeopleOK(personsReturned, pageSize, 1, 6l, 2, 1, 6);
	}

	@Test
	public void testUpdate() {
		// Create Person
		PersonV1 person = generator.generateMockPerson();

		// Mock details
		PersonDetails details = mapper.map(person, PersonDetails.class);
		// Mock repository
		when(personRepo.save(any(PersonDetails.class))).thenReturn(details);

		// Test update
		FigaroPersonService service = new FigaroPersonService();
		service.setPersonRepository(personRepo);
		service.setRapiMapper(mapper);
		Person personReturned = service.update(person);
		assertThat(personReturned, is(notNullValue()));
		assertThat((PersonV1) personReturned, is(equalTo(person)));
		assertThat(personReturned.deepEquals(person), is(true));
	}

	@Test
	public void testUpdateNull() {

		// Test update
		FigaroPersonService service = new FigaroPersonService();
		service.setPersonRepository(personRepo);
		service.setRapiMapper(mapper);
		Linkable personReturned = service.update(null);
		assertThat(personReturned, is(nullValue()));
	}

	@Ignore
	@Test
	public void testNewPerson() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsModified() {
		String code = "personCode";
		String newName = "new name";
		String currentName = "currentName";
		PersonV1 person = new PersonV1();
		PersonDetails currentDetails = new PersonDetails();
		person.setCode(code);
		currentDetails.setPersonCode(code);
		person.setName(newName);
		currentDetails.setName(currentName);
		// Mock Repository
		when(personRepo.findOne(code)).thenReturn(currentDetails);

		FigaroPersonService personService = new FigaroPersonService();
		personService.setPersonRepository(personRepo);
		personService.setRapiMapper(mapper);

		// check person modified
		assertTrue(personService.isModified(person));
		// assert false
		person.setName(currentName);
		assertFalse(personService.isModified(person));
	}


	@Test
	public void testSetPersonRepository() {
		FigaroPersonService personService = new FigaroPersonService();
		assertThat(personService.getPersonRepository(), nullValue());
		// set person repo
		personService.setPersonRepository(personRepo);
		assertThat(personService.getPersonRepository(), equalTo(personRepo));
	}

	@Test
	public void testSetAddressRepository() {
		FigaroPersonService personService = new FigaroPersonService();
		assertThat(personService.getAddressRepository(), nullValue());
		// set address repo
		personService.setAddressRepository(addressRepo);
		assertThat(personService.getAddressRepository(), equalTo(addressRepo));
	}
	
	@Test
	public void testGetPersonHasAddresses() {
		String personCode = "code";
		
		FigaroPersonService personService = new FigaroPersonService();
		personService.setAddressRepository(addressRepo);
		personService.setPersonRepository(personRepo);
		
		// Mock details
		PersonDetails details = new PersonDetails();
		details.setPersonCode(personCode);
		
		// Mock repository
		when(personRepo.findOne(personCode)).thenReturn(details);
		when(addressRepo.countByPersonPersonCode(personCode)).thenReturn(10l);

		// Test
		FigaroPersonService service = new FigaroPersonService();
		service.setPersonRepository(personRepo);
		service.setRapiMapper(mapper);
		service.setAddressRepository(addressRepo);
		PersonV1 personReturned = service.getPersonByCode(personCode);
		assertThat(personReturned, is(notNullValue()));
		assertThat(personReturned.hasAddresses(), is(equalTo(true)));
	}
	
	@Test
	public void testGetPersonHasNoAddresses() {
		String personCode = "code";
		
		FigaroPersonService personService = new FigaroPersonService();
		personService.setAddressRepository(addressRepo);
		personService.setPersonRepository(personRepo);
		
		// Mock details
		PersonDetails details = new PersonDetails();
		details.setPersonCode(personCode);
		
		// Mock repository
		when(personRepo.findOne(personCode)).thenReturn(details);
		when(addressRepo.countByPersonPersonCode(personCode)).thenReturn(0l);

		// Test
		FigaroPersonService service = new FigaroPersonService();
		service.setPersonRepository(personRepo);
		service.setRapiMapper(mapper);
		service.setAddressRepository(addressRepo);
		PersonV1 personReturned = service.getPersonByCode(personCode);
		assertThat(personReturned, is(notNullValue()));
		assertThat(personReturned.hasAddresses(), is(equalTo(false)));
	}
}
