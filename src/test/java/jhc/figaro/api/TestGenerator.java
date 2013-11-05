package jhc.figaro.api;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlTransient;

import jhc.figaro.api.model.People;
import jhc.figaro.api.model.Person;
import jhc.figaro.api.model.impl.v1.PeopleV1;
import jhc.figaro.api.model.impl.v1.PersonV1;
import jhc.figaro.api.persistence.domain.PersonDetails;
import jhc.figaro.api.resource.framework.AtomLink;
import jhc.figaro.api.resource.framework.Linkable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;

/**
 * Generates test data and assert values are as expected. For public String properties the value generated for the
 * property will consist of the property name with the index appended. For example:
 * <p>
 * For the property 'PersonV1.getName()' and an index of 5 the value generated would be "name5".
 * </p>
 * 
 * @author Chris Howe-Jones
 * 
 */
public class TestGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(TestGenerator.class);

    /**
     * Generate a list of PersonDetails with any String property values set to their property names with an index
     * appended. The startIndex indicates the suffix for the first PersonDetails element and is incremented for each
     * subsequent element.
     * 
     * @param number
     *            - the number of PersonDetails to generate in this list.
     * @param startIndex
     *            - the suffix for the first PersonDetails String property values.
     * @return List of generated mock PersonDetails.
     */
    public List<PersonDetails> generateMockDetailsList(final int number, final int startIndex) {
        List<PersonDetails> detailsList = new ArrayList<PersonDetails>();
        for (int i = 0; i < number; i++) {
            PersonDetails details = generateMockDetails(startIndex + i);
            detailsList.add(details);
        }
        return detailsList;
    }

    /**
     * Generate a mock PersonDetails instance with any String property values set to their property names with an index
     * appended.
     * 
     * @param detailsIndex
     *            - index to append to all String property values.
     * @return mock PersonDetails instance generated.
     */
    public PersonDetails generateMockDetails(final int detailsIndex) {
        PersonDetails details = new PersonDetails();
        setStringProperties(details, detailsIndex);
        return details;
    }

    /**
     * Generate a list of Person instances with any String property values set to their property names with an index
     * appended. The startIndex indicates the suffix for the first PersonDetails element and is incremented for each
     * subsequent element.
     * 
     * @param number
     *            - the number of Person instances to generate in this list.
     * @param startIndex
     *            - the suffix for the first PersonDetails String property values.
     * @return List of generated mock PersonDetails.
     */
    public List<PersonV1> generateMockPersonList(final int number, final int startIndex) {
        List<PersonV1> personList = new ArrayList<PersonV1>();
        for (int i = 0; i < number; i++) {
            PersonV1 person = generateMockPerson(startIndex + i);
            personList.add(person);
        }
        return personList;
    }

    /**
     * Generate a mock Person with an index of 1 added to the String property values.
     * 
     * @return generated Person.
     */
    public PersonV1 generateMockPerson() {
        return generateMockPerson(1);
    }

    /**
     * Assert People is as expected.
     * 
     * @param personsReturned
     *            - list of persons.
     * @param pageSize
     *            - size of page expected.
     * @param pageNumber
     *            - page number expected.
     * @param totalElements
     *            - total elements expected.
     * @param totalPages
     *            - total pages expected.
     * @param elements
     *            - number of elements expected in personsReturned
     * @param startIndex
     *            - first index that is expected to be appended to the person property values.
     */
    public void assertPeopleOK(final People personsReturned, final int pageSize, final int pageNumber,
            final long totalElements, final int totalPages, final int elements, final int startIndex) {
        assertThat(personsReturned, is(notNullValue()));
        assertThat(personsReturned.getPersonList().size(), greaterThanOrEqualTo(elements));
        assertThat(personsReturned.getTotalElements(), is(totalElements));
        assertThat(personsReturned.getTotalPages(), is(totalPages));
        assertThat(personsReturned.getNumberOfElements(), is(elements));
        assertThat(personsReturned.getPageNumber(), is(pageNumber));
        assertPersonListOK(personsReturned.getPersonList(), startIndex);
    }

    /**
     * Generate a mock PersonV1. All String properties will have a value of their property name and the personIndex
     * appended, for example the name property will have a value of "name1" assuming the personIndex is 1.
     * 
     * @param personIndex
     *            - to append to String property values.
     * @return mock PersonV1.
     */
    public PersonV1 generateMockPerson(final int personIndex) {
        PersonV1 person = new PersonV1();
        person.setCode("code" + personIndex);
        person.setName("name" + personIndex);
        person.setEmail("email" + personIndex);
        return person;
    }

    /**
     * Assert that Person has the expected values.
     * 
     * @param person
     *            - Person to test.
     * @param personIndex
     *            - index used to derive property values.
     */
    public void assertPersonOK(final Linkable person, final int personIndex) {
        assertStringPropertiesOK(person, personIndex);
    }

    /**
     * Assert that Person Links are OK
     * 
     * @param person
     *            - Person to test.
     * @param personIndex
     *            - index used to derive property values.
     */
    public void assertPersonLinksOK(final Linkable person, final int personIndex) {
        assertPersonLinksOK(person, personIndex, MediaType.APPLICATION_JSON);
    }

    /**
     * Assert that Person Links are OK
     * 
     * @param person
     *            - Person to test.
     * @param personIndex
     *            - index used to derive property values.
     * @param mediaType
     *            - expected media type e.g. application/json
     */
    public void assertPersonLinksOK(final Linkable person, final int personIndex, final String mediaType) {
        assertPersonLinksOK(person, personIndex, mediaType, null);
    }

    /**
     * Assert that Person Links are OK
     * 
     * @param person
     *            - Person to test.
     * @param personIndex
     *            - index used to derive property values.
     * @param mediaType
     *            - expected media type e.g. application/json
     */
    public void assertPersonLinksOK(final Linkable person, final int personIndex, final String mediaType,
            final String uriRoot) {
        StringBuilder hrefBuilder = new StringBuilder();
        // contstruct expected href from uriRoot, routing path and person index.
        if (uriRoot != null && !uriRoot.isEmpty()) {
            hrefBuilder.append(uriRoot);
        }
        hrefBuilder.append("/people/code");
        hrefBuilder.append(personIndex);
        assertThat(person.getLinks(), notNullValue());
        assertThat(person.getLinks().size(), greaterThan(0));
        AtomLink actualLink = person.getLinks().get(0);
        assertThat(actualLink.getRel(), is(equalTo("self")));
        assertThat(actualLink.getHref(), is(equalTo(hrefBuilder.toString())));
        assertThat(actualLink.getType(), containsString(mediaType));
    }

    /*
     * Use introspection to identify public String properties and set them to their property name suffixed by the index.
     */
    private void assertStringPropertiesOK(final Object object, final int index) {
        try {
            Class<?> beanClass = object.getClass();
            BeanInfo info = Introspector.getBeanInfo(beanClass);
            MethodDescriptor[] methodDescriptors = info.getMethodDescriptors();
            for (MethodDescriptor descriptor : methodDescriptors) {
                // Identify if this method is a modifier for a String property.
                if (descriptor.getName().contains("get")
                        && descriptor.getMethod().getAnnotation(XmlTransient.class) == null) {
                    Class<?> returnType = descriptor.getMethod().getReturnType();
                    if (returnType.equals(String.class)) {
                        // build the expected value which is the property name (decapitalised) appended with the index
                        String expectedValue = getPropertyValue(descriptor, index);
                        // set value
                        descriptor.getMethod().setAccessible(true);
                        String actualValue = (String) descriptor.getMethod().invoke(object);
                        assertThat(actualValue, equalTo(expectedValue));
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Problem with introspection", e);
            fail("Problem introspecting a bean.\n" + e.toString());
        }
    }

    /*
     * Use introspection to identify public String properties and set them to their property name suffixed by the index.
     */
    private void setStringProperties(final Object object, final int index) {
        try {
            Class<?> beanClass = object.getClass();
            BeanInfo info = Introspector.getBeanInfo(beanClass);
            MethodDescriptor[] methodDescriptors = info.getMethodDescriptors();
            for (MethodDescriptor descriptor : methodDescriptors) {
                // Identify if this method is a modifier for a String property.
                if (descriptor.getName().contains("set")) {
                    Class<?>[] paramTypes = descriptor.getMethod().getParameterTypes();
                    if (paramTypes.length == 1 && paramTypes[0].equals(String.class)) {
                        // build the value which is the property name (decapitalised) minus 'person' if present and
                        // appended with the index
                        String value = getPropertyValue(descriptor, index);
                        // set value
                        descriptor.getMethod().setAccessible(true);
                        descriptor.getMethod().invoke(object, value);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Problem with introspection", e);
            fail("Problem introspecting a bean.\n" + e.toString());
        }
    }

    private String getPropertyValue(final MethodDescriptor descriptor, final int index) {
        StringBuilder valueBuilder = new StringBuilder();
        // build the value which is the property name (decapitalised) minus 'person' if present appended with the index
        int start = 3;
        if (descriptor.getName().substring(3).startsWith("Person")) {
            start = 9;
        }
        valueBuilder.append(descriptor.getName().substring(start, start + 1).toLowerCase());
        valueBuilder.append(descriptor.getName().substring(start + 1));
        valueBuilder.append(Integer.toString(index));
        return valueBuilder.toString();
    }

    /**
     * Wrap the person list in a People instance for the page request supplied.
     * 
     * @param personList
     * @param pageSize
     * @param pageRequest
     * @return
     */
    public PeopleV1 generateMockPeople(final List<PersonV1> personList, final Pageable pageRequest,
            final long totalElements) {
        // TODO Auto-generated method stub
        PeopleV1 persons = new PeopleV1(personList);
        persons.setNumberOfElements(personList.size());
        persons.setPageSize(pageRequest.getPageSize());
        persons.setPageNumber(pageRequest.getPageNumber());
        persons.setTotalElements(totalElements);
        return persons;
    }

    /**
     * Assert that the person instances for a given list of persons are OK.
     * 
     * @param personList
     *            - list of person instances.
     * @param startIndex
     *            - index to start with as a prefix to String property values.
     */
    public void assertPersonListOK(final List<? extends Person> personList, final int startIndex) {
        int personIndex = startIndex;
        for (Linkable person : personList) {
            assertPersonOK(person, personIndex++);
        }
    }

    /**
     * Assert that the links for a given list of Person instances are OK.
     * 
     * @param personList
     *            - list of person instances.
     * @param startIndex
     *            - index to start with as a prefix to String property values.
     * @param mediaType
     *            - type of media expected for link.
     * @param uriRoot
     *            - root of the URI expected in the link.
     */
    public void assertPersonListLinksOK(final List<? extends Person> personList, final int startIndex,
            final String mediaType, final String uriRoot) {
        int personIndex = startIndex;
        for (Linkable person : personList) {
            assertPersonLinksOK(person, personIndex++, mediaType, uriRoot);
        }
    }

}
