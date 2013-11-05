package jhc.figaro.api.model.impl.v1;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import jhc.figaro.api.resource.framework.AtomLink;

import org.junit.Test;

public class AddressesV1Test {

	@SuppressWarnings("unchecked")
	@Test
	public void testIterator() {
		AddressesV1 addresses = new AddressesV1();
		List<AddressV1> addressList = new ArrayList<AddressV1>();
		addressList.add(new AddressV1());
		addresses.setAddressList(addressList);
		assertThat(addresses.iterator().next(), equalTo(addressList.iterator().next()));

	}

	@Test
	public void testSetAddressList() {
		AddressesV1 addresses = new AddressesV1();
		assertThat(addresses.getAddressList(), nullValue());
		List<AddressV1> addressList = new ArrayList<AddressV1>();
		addresses.setAddressList(addressList);
		assertThat(addresses.getAddressList(), equalTo(addressList));
	}
	
	@Test
	public void testSetLinks() {
		AddressesV1 addresses = new AddressesV1();
		assertThat(addresses.getLinks(), nullValue());
		// set links
		List<AtomLink> links = new ArrayList<AtomLink>();
		addresses.setLinks(links);
		assertThat(addresses.getLinks(), equalTo(links));
	}
	
	@Test
	public void testClone() {
		AddressesV1 addresses = new AddressesV1();
		int pageNumber = 10;
		addresses.setPageNumber(pageNumber );
		int elements = 10;
		addresses.setNumberOfElements(elements);
		int size = 100;
		addresses.setPageSize(size);
		long total = 910l;
		addresses.setTotalElements(total);
		List<AddressV1> addressList = new ArrayList<AddressV1>();
		AddressV1 addressV1 = new AddressV1();
		addressList.add(addressV1);
		Long id = 100l;
		addressV1.setId(id );
		addresses.setAddressList(addressList);
		AtomLink link = new AtomLink();
		List<AtomLink> links = new ArrayList<AtomLink>();
		links.add(link);
		addresses.setLinks(links);
		
		// clone
		AddressesV1 clone = addresses.clone();
		assertThat(clone, notNullValue());
		assertThat(clone.getAddressList(), equalTo(addressList));
		assertThat(clone.getAddressList().get(0), equalTo(addressV1));
		assertThat(clone.getNumberOfElements(), equalTo(elements));
		assertThat(clone.getTotalElements(), equalTo(total));
		assertThat(clone.getPageNumber(), equalTo(pageNumber));
		assertThat(clone.getPageSize(), equalTo(size));
		assertThat(clone.getLinks(), equalTo(links));
		assertThat(clone.getLinks().get(0), equalTo(link));
	}

}
