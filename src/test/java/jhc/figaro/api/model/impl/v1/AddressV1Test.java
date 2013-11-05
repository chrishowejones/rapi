package jhc.figaro.api.model.impl.v1;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import jhc.figaro.api.resource.framework.AtomLink;

import org.junit.Test;

public class AddressV1Test {

	@Test
	public void testSetId() {
		// not set
		AddressV1 address = new AddressV1();
		assertThat(address.getId(), equalTo(0l));
		// set
		long id = 100l;
		address.setId(id);
		assertThat(address.getId(), equalTo(id));
	}

	@Test
	public void testSetLinks() {
		// not set
		AddressV1 address = new AddressV1();
		assertThat(address.getLinks(), nullValue());
		// set
		List<AtomLink> links = new ArrayList<AtomLink>();
		address.setLinks(links);
		assertThat(address.getLinks(), equalTo(links));
	}

	@Test
	public void testClone() {
		AddressV1 address = new AddressV1();
		long id = 1l;
		address.setId(id);
		AddressV1 clone = (AddressV1) address.clone();
		assertThat(clone, equalTo(address));
		assertThat(clone.getId(), equalTo(id));
	}

	@Test
	public void testEqualsNull() {
		AddressV1 address = new AddressV1();
		assertThat(address.equals(null), is(false));
	}

	@Test
	public void testEqualsTypeDifferent() {
		AddressV1 address = new AddressV1();
		assertThat(address.equals("type"), is(false));
	}

	@Test
	public void testEqualsIdNotSet() {
		AddressV1 address = new AddressV1();
		AddressV1 address2 = new AddressV1();
		assertThat(address.equals(address2), is(false));
	}

	@Test
	public void testEqualsSame() {
		AddressV1 address = new AddressV1();
		// check same person address is equal
		assertThat(address.equals(address), is(true));
	}

	@Test
	public void testEqualsIdsSetDifferent() {
		AddressV1 address = new AddressV1();
		AddressV1 address2 = new AddressV1();
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
		AddressV1 address = new AddressV1();
		AddressV1 address2 = new AddressV1();
		// set id to the same
		long id = 10l;
		address.setId(id);
		address2.setId(id);
		assertThat(address.equals(address2), is(true));
	}

	@Test
	public void testSetAddressType() {
		// type not set (null)
		AddressV1 address = new AddressV1();
		assertThat(address.getAddressType(), nullValue());
		// set type and get
		String type = "type";
		address.setAddressType(type);
		assertThat(address.getAddressType(), equalTo(type));
	}

	@Test
	public void testSetLine1() {
		// line1 not set (null)
		AddressV1 address = new AddressV1();
		assertThat(address.getLine1(), nullValue());
		// set line1 and get
		String line1 = "line1";
		address.setLine1(line1);
		assertThat(address.getLine1(), equalTo(line1));
	}

	@Test
	public void testSetLine2() {
		// line2 not set (null)
		AddressV1 address = new AddressV1();
		assertThat(address.getLine2(), nullValue());
		// set line2 and get
		String line2 = "line2";
		address.setLine2(line2);
		assertThat(address.getLine2(), equalTo(line2));
	}

	@Test
	public void testSetCity() {
		// city not set (null)
		AddressV1 address = new AddressV1();
		assertThat(address.getCity(), nullValue());
		// set city and get
		String city = "city";
		address.setCity(city);
		assertThat(address.getCity(), equalTo(city));
	}

	@Test
	public void testSetPostcode() {
		// postcode not set (null)
		AddressV1 address = new AddressV1();
		assertThat(address.getPostcode(), nullValue());
		// set postcode and get
		String postcode = "postcode";
		address.setPostcode(postcode);
		assertThat(address.getPostcode(), equalTo(postcode));
	}
}
