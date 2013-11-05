package jhc.figaro.api.persistence.domain;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class AddressTest {

    @Test
    public void testHashCodeInitial() {
        Address address = new Address();
        assertThat(address.hashCode(), is(System.identityHashCode(address)));
    }

    @Test
    public void testHashCode() {
        Address address = new Address();
        address.setId(1l);
        assertThat(address.hashCode(), is(1 + 31));
        address.setId(12345l);
        assertThat(address.hashCode(), is(12345 + 31));
    }

    @Test
    public void testSetId() {
        // Id not set (null)
        Address address = new Address();
        assertThat(address.getId(), equalTo(0l));
        // set id and get
        long id = 10l;
        address.setId(id);
        assertThat(address.getId(), equalTo(id));
    }

    @Test
    public void testSetAddressType() {
        // type not set (null)
        Address address = new Address();
        assertThat(address.getAddressType(), nullValue());
        // set type and get
        String type = "type";
        address.setAddressType(type);
        assertThat(address.getAddressType(), equalTo(type));
    }

    @Test
    public void testSetLine1() {
        // line1 not set (null)
        Address address = new Address();
        assertThat(address.getLine1(), nullValue());
        // set line1 and get
        String line1 = "line1";
        address.setLine1(line1);
        assertThat(address.getLine1(), equalTo(line1));
    }

    @Test
    public void testSetLine2() {
        // line2 not set (null)
        Address address = new Address();
        assertThat(address.getLine2(), nullValue());
        // set line2 and get
        String line2 = "line2";
        address.setLine2(line2);
        assertThat(address.getLine2(), equalTo(line2));
    }

    @Test
    public void testSetCity() {
        // city not set (null)
        Address address = new Address();
        assertThat(address.getCity(), nullValue());
        // set city and get
        String city = "city";
        address.setCity(city);
        assertThat(address.getCity(), equalTo(city));
    }

    @Test
    public void testSetPostcode() {
        // postcode not set (null)
        Address address = new Address();
        assertThat(address.getPostcode(), nullValue());
        // set postcode and get
        String postcode = "postcode";
        address.setPostcode(postcode);
        assertThat(address.getPostcode(), equalTo(postcode));
    }

    @Test
    public void testEqualsNull() {
        // test equals with null
        Address address = new Address();
        assertThat(address.equals(null), is(false));
    }

    @Test
    public void testEqualsDifferentType() {
        Address address = new Address();// test equals with different type
        assertThat(address.equals(new Integer(10)), is(false));
    }

    @Test
    public void testEqualsIdsUnset() {
        Address address = new Address();
        // test equals with two Address - ids unset
        Address address2 = new Address();
        assertThat(address.equals(address2), is(false));
    }

    @Test
    public void testEqualsSame() {
        Address address = new Address();
        // check same person address is equal
        assertThat(address.equals(address), is(true));
    }

    @Test
    public void testEqualsIdsSetDifferent() {
        Address address = new Address();
        Address address2 = new Address();
        // set id on one
        long id = 10l;
        address.setId(id);
        assertThat(address.equals(address2), is(false));
        // set id to different value
        address2.setId(11l);
        assertThat(address.equals(address2), is(false));
    }

    @Test
    public void testEqualsIdsSetsame() {
        Address address = new Address();
        Address address2 = new Address();
        // set id to the same
        long id = 10l;
        address.setId(id);
        address2.setId(id);
        assertThat(address.equals(address2), is(true));
    }

}
