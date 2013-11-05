package jhc.figaro.api.service.v1.impl;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import jhc.figaro.api.model.People;
import jhc.figaro.api.model.impl.v1.PersonV1;
import jhc.figaro.api.resource.framework.Linkable;

import org.junit.Ignore;
import org.junit.Test;

public class DummyPersonServiceTest {

    @Test
    public void testGetPersonByCode() {
        DummyPersonService personService = new DummyPersonService();
        PersonV1 person1 = personService.getPersonByCode("1");
        assertThat(person1, notNullValue());
        assertThat(person1.getCode(), is(equalTo("1")));
        assertThat(person1.getName(), is(equalTo("Fred Bloggs")));
    }

    @Test
    public void testGetPersonByCodeNotFound() {
        DummyPersonService personService = new DummyPersonService();
        Linkable person1 = personService.getPersonByCode("100");
        assertThat(person1, nullValue());
    }

    @Test
    public void testGetPersonNull() {
        DummyPersonService personService = new DummyPersonService();
        Linkable person1 = personService.getPersonByCode(null);
        assertThat(person1, nullValue());
    }

    @Ignore
    @Test
    public void testGetPersonBySearch() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetPeople() {
        // set up Person1
        PersonV1 person1 = new PersonV1();
        person1.setCode("1");
        PersonV1 person2 = new PersonV1();
        person2.setCode("2");
        PersonV1 person3 = new PersonV1();
        person3.setCode("3");
        DummyPersonService personService = new DummyPersonService();
        People<PersonV1> personList = personService.getPeople();
        assertThat(personList, notNullValue());
        assertThat(personList.getNumberOfElements(), is(equalTo(3)));
        assertThat(personList.getPersonList(), containsInAnyOrder(person1, person2, person3));
    }

    @Test
    public void testUpdateModifiedPerson() {
        PersonV1 person1 = new PersonV1();
        person1.setName("Fred");
        person1.setEmail("New");
        person1.setCode("1");
        // same as current Person
        DummyPersonService personService = new DummyPersonService();
        PersonV1 personUpdated = (PersonV1) personService.update(person1);
        assertTrue(personUpdated.deepEquals(person1));
        // retrieve and verify
        assertTrue(personService.getPersonByCode("1").deepEquals(personUpdated));
        assertTrue(personService.getPersonByCode("1").getEmail().equals("New"));
    }

    @Test
    public void testUpdateNotModifiedPerson() {
        PersonV1 person1 = new PersonV1();
        person1.setName("Fred");
        person1.setEmail("Bloggs");
        person1.setCode("1");
        // same as current Person
        DummyPersonService personService = new DummyPersonService();
        PersonV1 personUpdated = (PersonV1) personService.update(person1);
        assertTrue(personUpdated.deepEquals(person1));
        // retrieve and verify
        assertTrue(personService.getPersonByCode("1").deepEquals(personUpdated));

    }

    /*
     * Test if a person is already in existence but the fields for that person have not been modified then returns
     * false.
     */
    @Test
    public void testIsModifiedPersonFoundAndNotModified() {
        PersonV1 person = new PersonV1();
        person.setCode("1");
        person.setName("Fred Bloggs");
        person.setEmail("Fred.Bloggs@jhc.co.uk");
        DummyPersonService personService = new DummyPersonService();
        assertFalse(personService.isModified(person));
    }

    /*
     * Test if a person is already in existence and a field for that person has been modified then returns true.
     */
    @Test
    public void testIsModifiedPersonFoundAndModified() {
        PersonV1 person = new PersonV1();
        person.setCode("1");
        person.setName("Fred");
        person.setEmail("email");
        DummyPersonService personService = new DummyPersonService();
        assertTrue(personService.isModified(person));
        // change first name
        person.setName("name");
        assertTrue(personService.isModified(person));
        person.setEmail("newemail");
        person.setName("Fred");
        assertTrue(personService.isModified(person));
    }

    @Test
    public void testNewPerson() {
        PersonV1 newPerson = new PersonV1();
        newPerson.setCode("new");
        newPerson.setName("Fred");
        newPerson.setEmail("email");
        // new person
        DummyPersonService personService = new DummyPersonService();
        PersonV1 actualNewPerson = (PersonV1) personService.newPerson(newPerson);
        assertThat(actualNewPerson.getCode(), is("new"));
        assertThat(actualNewPerson.getName(), is(newPerson.getName()));
        assertThat(actualNewPerson.getEmail(), is(newPerson.getEmail()));
    }

    /*
     * Creating a person with a person id that already exists should just create a new person as the POST operation is
     * not idempotent.
     */
    @Test
    public void testNewPersonExists() {
        PersonV1 newPerson = new PersonV1();
        newPerson.setCode("1");
        newPerson.setName("Fred Bloggs");
        newPerson.setEmail("email");
        // new person
        DummyPersonService personService = new DummyPersonService();
        PersonV1 actualNewPerson = (PersonV1) personService.newPerson(newPerson);
        assertThat(actualNewPerson.getCode(), is("1"));
        assertThat(actualNewPerson.getName(), is(newPerson.getName()));
        assertThat(actualNewPerson.getEmail(), is(newPerson.getEmail()));
    }

    @Test(expected = NullPointerException.class)
    public void testNewNull() {
        // null person
        DummyPersonService personService = new DummyPersonService();
        personService.newPerson(null);
    }

}
