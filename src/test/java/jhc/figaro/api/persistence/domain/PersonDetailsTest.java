package jhc.figaro.api.persistence.domain;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PersonDetailsTest {

    @Test
    public void testHashCode() {
        // hash code with null personCode
        PersonDetails details = new PersonDetails();
        assertThat(details.hashCode(), equalTo(System.identityHashCode(details) + 31));
        // hash code with personCode set
        String personCode = "TEST";
        details.setPersonCode(personCode);
        assertThat(details.hashCode(), equalTo(personCode.hashCode() + 31));
    }

    @Test
    public void testGetSetPersonCode() {
        // Code not set (null)
        PersonDetails details = new PersonDetails();
        assertThat(details.getPersonCode(), is(nullValue()));
        // set code and get
        String personCode = "TEST";
        details.setPersonCode(personCode);
        assertThat(details.getPersonCode(), equalTo(personCode));
        // set back to null
        details.setPersonCode(null);
        assertThat(details.getPersonCode(), is(nullValue()));
    }

    @Test
    public void testGetSetName() {
        // Name not set (null)
        PersonDetails details = new PersonDetails();
        assertThat(details.getName(), is(nullValue()));
        // set name and get
        String personName = "TEST";
        details.setName(personName);
        assertThat(details.getName(), equalTo(personName));
        // set back to null
        details.setName(null);
        assertThat(details.getName(), is(nullValue()));
        // check trimmed output
        String trailingSpaces = "TEST      ";
        details.setName(trailingSpaces);
        assertThat(details.getName(), equalTo(personName));
    }

    @Test
    public void testGetSetEmail() {
        // Email not set (null)
        PersonDetails details = new PersonDetails();
        assertThat(details.getEmail(), is(nullValue()));
        // set name and get
        String personEmail = "TEST";
        details.setEmail(personEmail);
        assertThat(details.getEmail(), equalTo(personEmail));
        // set back to null
        details.setEmail(null);
        assertThat(details.getEmail(), is(nullValue()));
        // check trimmed output
        String trailingSpaces = "TEST      ";
        details.setEmail(trailingSpaces);
        assertThat(details.getEmail(), equalTo(personEmail));
    }

    @Test
    public void testEqualsObject() {
        // test equals with null
        PersonDetails details = new PersonDetails();
        assertThat(details.equals(null), is(false));
        // test equals with different type
        assertThat(details.equals(new Integer(10)), is(false));
        // test equals with two PersonDetails - codes unset
        PersonDetails details2 = new PersonDetails();
        assertThat(details.equals(details2), is(false));
        // check same person details is equal
        assertThat(details.equals(details), is(true));
        // set code on one
        String personCode = "TEST";
        details.setPersonCode(personCode);
        assertThat(details.equals(details2), is(false));
        // set code to different value
        details2.setPersonCode("different");
        assertThat(details.equals(details2), is(false));
        // set code to the same
        details2.setPersonCode(personCode);
        assertThat(details.equals(details2), is(true));
    }

    @Test
    public void testSetAddress() {
        PersonDetails details = new PersonDetails();
        assertThat(details.getAddresses(), nullValue());
        // set addresses
        List<Address> addresses = new ArrayList<Address>();
        details.setAddresses(addresses);
        assertThat(details.getAddresses(), equalTo(addresses));
    }

    @Test
    public void testToString() {
        PersonDetails details = new PersonDetails();
        String name = "name";
        String personCode = "personCode";
        String email = "email";
        details.setName(name);
        details.setPersonCode(personCode);
        details.setEmail(email);
        assertThat(details.toString(), is("PersonDetails [personCode=personCode, name=name, email=email]"));
    }

    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        PersonDetails details = new PersonDetails();
        String name = "name";
        String personCode = "personCode";
        details.setName(name);
        details.setPersonCode(personCode);
        // serialize person details
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream objout = new ObjectOutputStream(bytesOut);
        // write details
        objout.writeObject(details);
        byte[] detailsBytes = bytesOut.toByteArray();
        // deserialize
        ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(detailsBytes));
        PersonDetails detailsDeserialized = (PersonDetails) objIn.readObject();
        assertThat(details, equalTo(detailsDeserialized));
        assertThat(detailsDeserialized.getName(), equalTo(name));
        assertThat(detailsDeserialized.getPersonCode(), equalTo(personCode));
    }

}
