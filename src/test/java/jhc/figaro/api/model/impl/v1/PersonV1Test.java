package jhc.figaro.api.model.impl.v1;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import jhc.figaro.api.model.Address;
import jhc.figaro.api.model.Person;
import jhc.figaro.api.resource.framework.AtomLink;

import org.junit.Test;

public class PersonV1Test {

    @Test
    public void testHashCodeInitial() {
        PersonV1 person = new PersonV1();
        assertThat(person.hashCode(), is(System.identityHashCode(person)));
    }

    @Test
    public void testHashCode() {
        PersonV1 person = new PersonV1();
        person.setCode("code");
        assertThat(person.hashCode(), is("code".hashCode() + 31));
    }

    @Test
    public void testGetSetCode() {
        PersonV1 person = new PersonV1();
        assertThat(person.getCode(), is(nullValue()));
        // set id
        person.setCode("code");
        assertThat(person.getCode(), is("code"));
    }

    @Test
    public void testGetNameNull() {
        PersonV1 person = new PersonV1();
        assertThat(person.getName(), is(nullValue()));
    }

    @Test
    public void testGetName() {
        PersonV1 person = new PersonV1();
        person.setName("Chris");
        assertThat(person.getName(), is("Chris"));
    }

    @Test
    public void testGetEmail() {
        PersonV1 person = new PersonV1();
        person.setEmail("Jones@jhc.co.uk");
        assertThat(person.getEmail(), is("Jones@jhc.co.uk"));
    }

    @Test
    public void testGetEmailNull() {
        PersonV1 person = new PersonV1();
        assertThat(person.getEmail(), is(nullValue()));
    }

    @Test
    public void testEqualsObject() {
        PersonV1 person = new PersonV1();
        PersonV1 person2 = new PersonV1();
        // test null
        assertThat(person, is(not(equalTo(person2))));
        // test same
        assertThat(person, is(equalTo(person)));
        // test different type
        assertFalse(person.equals("string"));
        // test different persons
        assertThat(person, is(not(equalTo(person2))));
    }

    @Test
    public void testEqualsIdSet() {
        PersonV1 person = new PersonV1();
        PersonV1 person2 = new PersonV1();
        person.setCode("1");
        person2.setCode("1");
        // test equals
        assertThat(person, is(equalTo(person2)));
    }

    @Test
    public void testToString() {
        PersonV1 person = new PersonV1();
        assertThat(person.toString(), is("PersonV1 [code=null, name=null, email=null]"));
        // set values
        person.setName("Chris");
        person.setCode("10");
        person.setEmail("Jones@jhc.co.uk");
        assertThat(person.toString(), is("PersonV1 [code=10, name=Chris, email=Jones@jhc.co.uk]"));
    }

    /*
     * Equality based on all the fields in the class
     */
    @Test
    public void testDeepEquals() {
        PersonV1 person1 = new PersonV1();
        PersonV1 person2 = new PersonV1();
        assertTrue(person1.deepEquals(person2));
        String name = "name";
        // set up first name with differences
        person1.setName(name);
        assertFalse(person1.deepEquals(person2));
        assertFalse(person2.deepEquals(person1));
        person2.setName(name);
        assertTrue(person2.deepEquals(person1));
        person2.setName("different");
        assertFalse(person1.deepEquals(person2));
        person2.setName(name);
        // set up emaile with differences
        String email = "email";
        person1.setEmail(email);
        assertFalse(person1.deepEquals(person2));
        person2.setEmail(email);
        assertTrue(person2.deepEquals(person1));
        person2.setEmail("different");
        assertFalse(person1.deepEquals(person2));
        person2.setEmail(email);
        // set up codes
        String code = "code";
        person1.setCode(code);
        person2.setCode(code);
        assertTrue(person1.deepEquals(person2));
        assertTrue(person2.deepEquals(person1));
        // different codes
        person2.setCode("different");
        assertFalse(person1.deepEquals(person2));
    }

    /*
     * Equality based on all the fields in the class
     */
    @Test
    public void testDeepEqualsNull() {
        PersonV1 person1 = new PersonV1();
        assertFalse(person1.deepEquals(null));
    }

    /*
     * Equality based on all the fields in the class
     */
    @Test
    public void testDeepEqualsDifferentType() {
        PersonV1 person1 = new PersonV1();
        Person person2 = new Person() {

            @Override
            public String getCode() {
                return null;
            }

            @Override
            public void setCode(final String personCode) {
            }

            @Override
            public List<AtomLink> getLinks() {
                return null;
            }

            @Override
            public void setLinks(final List<AtomLink> link) {
            }

            @Override
            public boolean deepEquals(final Person person2) {
                return false;
            }

            @Override
            public Object clone() {
                return null;
            }

            @Override
            public void setId(final String id) {
                // TODO Auto-generated method stub

            }

            @Override
            public String getId() {
                // TODO Auto-generated method stub
                return null;
            }

			@Override
			public boolean hasAddresses() {
				// TODO Auto-generated method stub
				return false;
			}

        };
        assertFalse(person1.deepEquals(person2));
    }

    @Test
    public void testSetAddresses() {
    	PersonV1 person = new PersonV1();
    	assertThat(person.getAddresses(), nullValue());
    	List<AddressV1> addresses = new ArrayList<AddressV1>();
    	person.setAddresses(addresses);
    	assertThat(person.getAddresses(), equalTo(addresses));
    }
    
}
