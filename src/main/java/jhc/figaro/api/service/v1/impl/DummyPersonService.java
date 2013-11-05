package jhc.figaro.api.service.v1.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jhc.figaro.api.model.Page;
import jhc.figaro.api.model.People;
import jhc.figaro.api.model.Person;
import jhc.figaro.api.model.impl.v1.PeopleV1;
import jhc.figaro.api.model.impl.v1.PersonV1;
import jhc.figaro.api.persistence.domain.PersonDetails;
import jhc.figaro.api.service.PersonService;
import jhc.figaro.data.jpa.CommandService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.NotFoundException;

@Service("dummy.personService")
public class DummyPersonService implements PersonService {

    private static final Logger LOG = LoggerFactory.getLogger(DummyPersonService.class);

    private final Map<String, PersonV1> peopleMap = new HashMap<String, PersonV1>();

    /**
     * Creates an instance of the DummyPersonService.
     */
    public DummyPersonService() {
        // init
        initialise();
    }

    private void initialise() {
        LOG.trace("Initialise");
        PersonV1 person = new PersonV1();
        person.setCode("1");
        person.setName("Fred Bloggs");
        person.setEmail("Fred.Bloggs@jhc.co.uk");
        peopleMap.put(person.getCode(), person);
        PersonV1 person2 = new PersonV1();
        person2.setCode("2");
        person2.setName("Bob J Bloggs");
        person2.setEmail("Bob.Bloggs@jhc.co.uk");
        peopleMap.put(person2.getCode(), person2);
        PersonV1 person3 = new PersonV1();
        person3.setCode("3");
        person3.setName("Fred Smith");
        person3.setEmail("Fred.Smith@jhc.co.uk");
        peopleMap.put(person3.getCode(), person3);
    }

    @Override
    public final PersonV1 getPersonByCode(final String personCode) {
        LOG.trace("getPersonById(" + personCode + ")");
        PersonV1 person = peopleMap.get(personCode);
        return person;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public final Page getPersonBySearch(final String searchTerm) {
        LOG.trace("getPersonBySearch(" + searchTerm + ")");
        List<PersonV1> people = new ArrayList<PersonV1>();
        for (PersonV1 person : peopleMap.values()) {
            if (person.getName().toLowerCase().contains(searchTerm.toLowerCase())
                    || person.getEmail().toLowerCase().contains(searchTerm.toLowerCase())) {
                people.add(person);
            }
        }
        Page personsPage = new PeopleV1(people);
        return personsPage;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final PeopleV1 getPeople() {
        LOG.trace("getPeople()");
        List<PersonV1> peopleList = (List<PersonV1>) (List<? extends Person>) new ArrayList<PersonV1>(
                peopleMap.values());
        PeopleV1 people = new PeopleV1(peopleList);
        people.setNumberOfElements(peopleList.size());
        return people;
    }

    @Override
    public final Person update(final Person person) {
        LOG.trace("update(" + person + ")");
        // assert(person instanceof PersonV1);
        PersonV1 personV1 = (PersonV1) person;
        // update person
        if (peopleMap.containsKey(personV1.getCode())) {
            if (isModified(personV1)) {
                LOG.debug("Going to modify current person");
                PersonV1 currentPerson = peopleMap.get(personV1.getCode());
                // check fields and only update different ones
                if (!currentPerson.getName().equals(personV1.getName())) {
                    LOG.debug("Updating name to " + personV1.getName());
                    currentPerson.setName(personV1.getName());
                }
                if (!currentPerson.getEmail().equals(personV1.getEmail())) {
                    LOG.debug("Updating email to " + personV1.getEmail());
                    currentPerson.setEmail(personV1.getEmail());
                }
            }
        } else {
            throw new NotFoundException("No matching person to update. Person " + person.getCode());
        }
        return personV1;
    }

    @Override
    public final boolean isModified(final Person person) {
        LOG.trace("isPersonModified(" + person + ")");
        boolean isModified = false;
        if (peopleMap.containsKey(person.getCode())) {
            PersonV1 currentPerson = peopleMap.get(person.getCode());
            // check to ensure current person doesn't have all the same public
            // properties
            if (!currentPerson.deepEquals(person)) {
                isModified = true;
            }
        }
        LOG.debug("isPersonModified = " + isModified);
        return isModified;
    }

    @Override
    public final Person newPerson(final Person person) {
        LOG.trace("newPerson(" + person + ")");
        // store
        peopleMap.put(person.getCode(), (PersonV1) person);
        return person;
    }

    @Override
    public final void setCommandService(final CommandService<PersonDetails> service) {
        // TODO Auto-generated method stub

    }

    @Override
    public final CommandService<PersonDetails> getCommandService() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public final int getPageSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public final void setPageSize(final int pageSize) {
        // TODO Auto-generated method stub

    }

    @Override
    public final Page getPersonBySearch(final String searchTerm, final Pageable pageRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page getPeople(Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public People getPersonBySearch(String searchTerm, int page) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public People getPeople(int page) {
        // TODO Auto-generated method stub
        return null;
    }

}
