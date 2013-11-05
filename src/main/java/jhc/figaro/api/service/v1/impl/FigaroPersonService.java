package jhc.figaro.api.service.v1.impl;

import java.util.List;

import jhc.figaro.api.RapiMapper;
import jhc.figaro.api.model.People;
import jhc.figaro.api.model.Person;
import jhc.figaro.api.model.impl.v1.PeopleV1;
import jhc.figaro.api.model.impl.v1.PersonV1;
import jhc.figaro.api.persistence.AddressRepository;
import jhc.figaro.api.persistence.PersonRepository;
import jhc.figaro.api.persistence.domain.PersonDetails;
import jhc.figaro.api.service.PersonService;
import jhc.figaro.data.jpa.CommandService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * Acts as the service for co-ordinating the fetching, updating, creating and
 * deleting of Figaro Person resources for V1 of the API.
 * 
 * @author Chris Howe-Jones
 * 
 */
@Service("v1.personService")
public class FigaroPersonService implements PersonService {

	private static final Logger LOG = LoggerFactory.getLogger(FigaroPersonService.class);

	/**
	 * Default page size to use when not set specifically in Spring config.
	 */
	public static final int DEFAULT_PAGE_SIZE = 300;

	private PersonRepository personRepository;

	private RapiMapper mapper;

	private CommandService<PersonDetails> commandService;

	private int pageSize = DEFAULT_PAGE_SIZE;

	private AddressRepository addressRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jhc.figaro.api.service.PersonService#getPersonByCode(java.lang.String)
	 */
	@Override
	public final PersonV1 getPersonByCode(final String personCode) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("getPersonByCode(" + personCode + ")");
		}
		PersonDetails details = personRepository.findOne(personCode);
		// Map a Person
		PersonV1 person = mapper.map(details, PersonV1.class);
		checkAddressesExist(person);
		if (LOG.isDebugEnabled()) {
			LOG.debug("returning Person = " + person);
		}
		return person;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jhc.figaro.api.service.PersonService#getPersonBySearch(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public final People<? extends Person> getPersonBySearch(final String searchTerm) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("getPersonBySearch(" + searchTerm + ")");
		}
		PageRequest pageRequest = new PageRequest(0, getPageSize(), Direction.ASC, "name");
		People people = getPersonBySearch(searchTerm, pageRequest);
		return people;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jhc.figaro.api.service.PersonService#getPersonBySearch(java.lang.String,
	 * int)
	 */
	@Override
	public final PeopleV1 getPersonBySearch(final String searchTerm, final int page) {
		PageRequest pageRequest = new PageRequest(page, getPageSize(), Direction.ASC, "name");
		PeopleV1 people = getPersonBySearch(searchTerm, pageRequest);
		return people;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jhc.figaro.api.service.PersonService#getPersonBySearch(java.lang.String,
	 * org.springframework.data.domain.PageRequest)
	 */
	@Override
	public final PeopleV1 getPersonBySearch(final String searchTerm, final Pageable pageable) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("getPersonBySearch(" + searchTerm + ", " + pageable + ")");
		}
		PeopleV1 people = null;
		Page<PersonDetails> detailsPage = personRepository.findByNameLike(searchTerm, pageable);
		if (detailsPage != null) {
			// Map a Person for each details instance
			List<PersonV1> peopleList = mapper.mapAsList(detailsPage, PersonV1.class);
			people = new PeopleV1(peopleList);
			people.setNumberOfElements(detailsPage.getNumberOfElements());
			people.setPageSize(detailsPage.getSize());
			people.setPageNumber(detailsPage.getNumber());
			people.setTotalElements(detailsPage.getTotalElements());
			checkAddressesExist(people.getPersonList());
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("returning Person page for search term '" + searchTerm + "' with details = " + people);
		}
		return people;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jhc.figaro.api.service.PersonService#getPeople()
	 */
	@Override
	public final PeopleV1 getPeople() {
		if (LOG.isTraceEnabled()) {
			LOG.trace("getPeople()");
		}
		Pageable pageRequest = new PageRequest(0, getPageSize(), Direction.ASC, "personCode");
		return getPeople(pageRequest);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jhc.figaro.api.service.PersonService#getPeople(int)
	 */
	@Override
	public final PeopleV1 getPeople(final int page) {
		Pageable pageRequest = new PageRequest(page, getPageSize(), Direction.ASC, "personCode");
		return getPeople(pageRequest);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jhc.figaro.api.service.PersonService#getPeople(org.springframework.data
	 * .domain.Pageable)
	 */
	@Override
	public final PeopleV1 getPeople(final Pageable pageable) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("getPeople(" + pageable + ")");
		}
		Page<PersonDetails> detailsPage = personRepository.findAll(pageable);
		List<PersonV1> peopleList = mapper.mapAsList(detailsPage, PersonV1.class);
		checkAddressesExist(peopleList);
		PeopleV1 people = new PeopleV1(peopleList);
		people.setPageSize(pageable.getPageSize());
		people.setNumberOfElements(detailsPage.getNumberOfElements());
		people.setPageNumber(detailsPage.getNumber());
		people.setTotalElements(detailsPage.getTotalElements());
		if (LOG.isDebugEnabled()) {
			LOG.debug("returning Person page for getPerson(" + pageable + ") with details = " + people);
		}
		return people;
	}

	@Override
	public final Person update(final Person person) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("update(" + person + ")");
		}
		// Map person to PersonDetails
		PersonDetails details = mapper.map((PersonV1) person, PersonDetails.class);
		// persist
		details = personRepository.save(details);
		Person personReturned = mapper.map(details, PersonV1.class);
		if (LOG.isDebugEnabled()) {
			LOG.debug("update(" + person + ") returned = " + personReturned);
		}
		return personReturned;
	}

	@Override
	public final Person newPerson(final Person person) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("newPerson(" + person + ")");
		}
		// TODO Auto-generated method stub
		if (LOG.isInfoEnabled()) {
			LOG.info("newPerson(" + person + ") NOT IMPLEMENTED!!!!!!");
		}
		return null;
	}

	/*
	 * TODO Make this more efficient!
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * jhc.figaro.api.service.PersonService#isModified(jhc.figaro.api.model.
	 * Person)
	 */
	@Override
	public final boolean isModified(final Person person) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("isModified(" + person + ")");
		}
		boolean isModified = false;
		PersonDetails details = personRepository.findOne(person.getCode());
		PersonV1 persistentPerson = mapper.map(details, PersonV1.class);
		if (!person.deepEquals(persistentPerson)) {
			// dirty
			isModified = true;
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("isModified(" + person + ") returned = " + isModified);
		}
		return isModified;
	}

	/**
	 * Set the persistence repository for Person.
	 * 
	 * @param repository
	 *            - the persistence repository for PersonDetails in Figaro.
	 * 
	 */
	@Autowired
	public final void setPersonRepository(final PersonRepository repository) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("setPersonRepository(" + repository + ")");
		}
		personRepository = repository;
		personRepository.setCommandService(getCommandService());
	}

	/**
	 * Gets the persistence repository for Person.
	 * 
	 * @return person repository.
	 */
	public final PersonRepository getPersonRepository() {
		return personRepository;
	}

	/**
	 * Get a customised Orika mapper to map persistent classes to resources.
	 * 
	 * @return mapper for resources.
	 */
	public final RapiMapper getRapiMapper() {
		return mapper;
	}

	/**
	 * Set a customised Orika mapper for the mapping persistent classes to
	 * resources.
	 * 
	 * @param rapiMapper
	 *            - maps PersonV1 and PersonDetails.
	 */
	@Autowired
	public final void setRapiMapper(final RapiMapper rapiMapper) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("setRapiMapper(" + rapiMapper + ")");
		}
		mapper = rapiMapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jhc.figaro.api.service.v1.impl.PersonService#setCommandService(jhc.figaro
	 * .data.jpa.CommandService)
	 */
	@Override
	@Autowired(required = false)
	public final void setCommandService(final CommandService<PersonDetails> service) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("setCommandService(" + service + ")");
		}
		commandService = service;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jhc.figaro.api.service.v1.impl.PersonService#getCommandService()
	 */
	@Override
	public final CommandService<PersonDetails> getCommandService() {
		return commandService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jhc.figaro.api.service.v1.impl.PersonService#getPageSize()
	 */
	@Override
	public final int getPageSize() {
		if (pageSize == 0) {
			return DEFAULT_PAGE_SIZE;
		}
		return pageSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jhc.figaro.api.service.v1.impl.PersonService#setPageSize(int)
	 */
	@Autowired(required = false)
	@Qualifier("resource.pageSize")
	@Override
	public final void setPageSize(final int pageSizeValue) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Setting resource page size to " + pageSizeValue);
		}
		pageSize = pageSizeValue;
	}

	/**
	 * Set Address Repository.
	 * 
	 * @param addressRepository
	 *            - address repository.
	 */
	@Autowired
	public final void setAddressRepository(final AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	/**
	 * Get Address Repository.
	 * 
	 * @return address repository.
	 */
	public final AddressRepository getAddressRepository() {
		return addressRepository;
	}

	private void checkAddressesExist(PersonV1 person) {
		if (person != null) {
			long addressesCount = getAddressRepository().countByPersonPersonCode(person.getCode());
			if (addressesCount > 0) {
				person.setHasAddresses(true);
			}
		}
	}

	private void checkAddressesExist(List<PersonV1> peopleList) {
		for (PersonV1 person : peopleList) {
			checkAddressesExist(person);
		}
	}

}
