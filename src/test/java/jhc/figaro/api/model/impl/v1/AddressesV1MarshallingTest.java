package jhc.figaro.api.model.impl.v1;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import jhc.figaro.api.resource.framework.AbstractMashallingTest;
import jhc.figaro.api.resource.framework.AtomLink;
import jhc.figaro.api.resource.framework.AtomRelationship;

import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;

public class AddressesV1MarshallingTest extends AbstractMashallingTest {
	
	private AddressesV1 addresses;
	private int pageNumber;
	private int size;
	private int elements;
	private long total;
	private Long id;
	private String postcode;
	private AddressV1 address;
	private List<AddressV1> addressList;
	
	@Before
	public void setUp() {
		super.setUp();
		addresses = new AddressesV1();
		pageNumber = 2;
		size = 100;
		elements = 99;
		total = 199;
		addresses.setPageNumber(pageNumber);
		addresses.setPageSize(size);
		addresses.setNumberOfElements(elements);
		addresses.setTotalElements(total);
	    address = new AddressV1();
		id = 22l;
		postcode = "postcode";
		address.setId(id);
		address.setPostcode(postcode);
		// set up address list
		addressList = new ArrayList<AddressV1>();
		addressList.add(address);
		addresses.setAddressList(addressList);
		// set up links
		List<AtomLink> links = new ArrayList<AtomLink>();
		AtomLink link = new AtomLink(AtomRelationship.SELF, "href", "media");
		links.add(link);
		addresses.setLinks(links);
	}

	@Test
	public void testMarshallingToJson() throws Exception {
		// marshall addresses to json
		String json = writeJson(addresses);
		JsonPath path = new JsonPath(json);
		assertThat(path.getInt("pageNumber"), equalTo(pageNumber));
		assertThat(path.getInt("pageSize"), equalTo(size));
		assertThat(path.getInt("numberOfElements"), equalTo(elements));
		assertThat(path.getLong("totalElements"), equalTo(total));
		assertThat(path.getList("address"), hasSize(1));
		assertThat(path.getLong("address.get(0).id"), equalTo(id));
		assertThat(path.getString("address.get(0).postcode"), equalTo(postcode));
	}

	@Test
	public void testMarshallingToXml() throws Exception {
		// marshall addresses to xml
		String xml = writeXml(addresses);
		XmlPath path = new XmlPath(xml);
		assertThat(path.getInt("addresses.pageNumber"), equalTo(pageNumber));
		assertThat(path.getInt("addresses.pageSize"), equalTo(size));
		assertThat(path.getInt("addresses.numberOfElements"), equalTo(elements));
		assertThat(path.getLong("addresses.totalElements"), equalTo(total));
		assertThat(path.getList("addresses.address"), hasSize(1));
		assertThat(path.getLong("addresses.address[0].id"), equalTo(id));
		assertThat(path.getString("addresses.address[0].postcode"), equalTo(postcode));
	}
}
