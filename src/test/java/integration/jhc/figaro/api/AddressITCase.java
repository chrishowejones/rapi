package integration.jhc.figaro.api;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jayway.restassured.RestAssured;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AddressITCase {

	private static final Logger LOG = LoggerFactory.getLogger(AddressITCase.class);

	private final static String ADDRESSES_A00001_URI = "people/A00001/addresses";
	private final static String ADDRESSES_ONE_URI = "people/A00001/addresses/1";

	@Before
	public void setUp() {
		RestAssured.port = 9090;
		RestAssured.baseURI = "http://localhost";
		RestAssured.basePath = "/rapi";
	}

	@Test
	public void testGetAddressesForPerson() {
		LOG.debug("testGetAddressesForPerson");
		LOG.debug("response = " + get(ADDRESSES_A00001_URI).asString());
		expect().body("address.get(0).id", equalTo(1)).and().body("address.get(1).id", equalTo(2)).and()
				.body("numberOfElements", equalTo(2)).and().body("totalPages", equalTo(1)).and()
				.body("totalElements", equalTo(2)).and().body("pageNumber", equalTo(0)).and()
				.body("pageSize", equalTo(10)).get(ADDRESSES_A00001_URI);
	}

	@Test
	public void testGetAddressesForPersonLinks() {
		LOG.debug("testGetAddressesForPersonLinks");
		LOG.debug("response = " + get(ADDRESSES_A00001_URI).asString());
		expect().statusCode(Status.OK.getStatusCode()).body("address.get(0).links", hasSize(1))
				.body("address.get(0).links.get(0).rel", equalTo("self")).and()
				.body("address.get(0).links.get(0).href", equalTo(getUri(ADDRESSES_A00001_URI + "/1")))
				.and().body("address.get(0).links.get(0).type", equalTo(MediaType.APPLICATION_JSON))
				.get(ADDRESSES_A00001_URI);
	}
	
	@Test
	public void testGetAddressById() {
		LOG.debug("testGetAddressById");
		LOG.debug("response = " + get(ADDRESSES_ONE_URI).asString());
		expect().body("id", equalTo(1)).and().body("line1", equalTo("St Nicholas Building")).and()
				.body("postcode", equalTo("NE1 1TH")).get(ADDRESSES_ONE_URI);
	}
	
	@Test
	public void testGetAddressByIdLinks() {
		LOG.debug("testGetAddressByIdLinks");
		LOG.debug("response = " + get(ADDRESSES_ONE_URI).asString());
		expect().statusCode(Status.OK.getStatusCode()).body("links", hasSize(1))
				.body("links.get(0).rel", equalTo("self")).and()
				.body("links.get(0).href", equalTo(getUri(ADDRESSES_ONE_URI)))
				.and().body("links.get(0).type", equalTo(MediaType.APPLICATION_JSON))
				.get(ADDRESSES_ONE_URI);
	}

	private String getUri(String addressesOneUri) {
		StringBuilder uri = new StringBuilder(RestAssured.baseURI);
		uri.append(":");
		uri.append(RestAssured.port);
		uri.append(RestAssured.basePath);
		uri.append("/");
		uri.append(addressesOneUri);
		return uri.toString();
	}

}
