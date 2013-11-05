package integration.jhc.figaro.api;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasXPath;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import jhc.figaro.api.model.impl.v1.PersonV1;
import jhc.figaro.api.resources.CustomMediaType;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.Response;
import com.sun.jersey.api.client.ClientResponse.Status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PersonITCase {

	private static final Logger LOG = LoggerFactory.getLogger(PersonITCase.class);

	private final static String PERSON_ONE_URI = "people/A00001";
	private final static String PERSON_TO_UPDATE_URI = "people/A01345";
	private final static String name = "MR 'ARIZ ADAMS TEST";
	private final static String email = "kevin.parker@jhc.co.uk";
	private final static String code = "A00001";

	@Autowired
	protected Resource newPersonJsonResource;

	@Before
	public void setUp() {
		RestAssured.port = 9090;
		RestAssured.baseURI = "http://localhost";
		RestAssured.basePath = "/rapi";
	}

	@Test
	public void testPerson1BodyJSON() {
		LOG.debug("Test getPerson1BodyJSON = " + get(PERSON_ONE_URI).asString());
		expect().body("name", equalTo(name)).and().body("email", equalTo(email)).and().contentType(ContentType.JSON)
				.and().statusCode(200).when().get(PERSON_ONE_URI);
	}

	@Test
	public void testPersonVersion1BodyJSON() {
		expect().body("name", equalTo(name)).and().body("email", equalTo(email)).and().contentType(ContentType.JSON)
				.and().statusCode(200).when().get("/v1/people/A00001");
	}

	@Test
	public void testPerson1LinkRel() {
		LOG.info(get(PERSON_ONE_URI).asString());
		// assert that the link is for rel self
		expect().body("links.get(0).rel", is(equalTo("self"))).get(PERSON_ONE_URI);
	}

	@Test
	public void testPerson1AddressesLinkRel() {
		LOG.info(get(PERSON_ONE_URI).asString());
		// assert that the link is for rel addresses
		expect().body("links.get(1).rel", is(equalTo("addresses"))).get(PERSON_ONE_URI);
	}

	@Test
	public void testPerson1AddressesLinkRelXml() {
		LOG.info(given().header("Accept", ContentType.XML.toString()).get(PERSON_ONE_URI).asString());
		// assert that the link is for rel addresses
		given().header("Accept", ContentType.XML.toString()).expect()
				.body("person.links[1].rel", is(equalTo("addresses"))).get(PERSON_ONE_URI);
	}

	@Test
	public void testPerson1AddressesLinkHref() {
		LOG.info(get(PERSON_ONE_URI).asString());
		// assert that the link is for href addresses
		expect().body("links.get(1).href", containsString(PERSON_ONE_URI + "/addresses")).get(PERSON_ONE_URI);
	}

	@Test
	public void testPerson1AddressesLinkHrefXml() {
		LOG.info(given().header("Accept", ContentType.XML.toString()).get(PERSON_ONE_URI).asString());
		// assert that the link is for href addresses
		given().header("Accept", ContentType.XML.toString()).expect()
				.body("person.links[1].href", containsString(PERSON_ONE_URI + "/addresses")).get(PERSON_ONE_URI);
	}

	@Test
	public void testPerson1AddressesLinkType() {
		LOG.info(get(PERSON_ONE_URI).asString());
		// assert that the link is for type addresses
		expect().body("links.get(1).type", is(ContentType.JSON.toString())).get(PERSON_ONE_URI);
	}

	@Test
	public void testPerson1AddressesLinkTypeXml() {
		LOG.info(given().header("Accept", ContentType.XML.toString()).get(PERSON_ONE_URI).asString());
		// assert that the link is for type addresses
		given().header("Accept", ContentType.XML.toString()).expect().body("person.links[1].type", is(ContentType.XML.toString())).get(PERSON_ONE_URI);
	}

	@Test
	public void testPerson1LinkRelForXml() {
		LOG.info(given().header("Accept", ContentType.XML.toString()).get(PERSON_ONE_URI).asString());
		// assert that the link is for rel self
		given().header("Accept", ContentType.XML.toString()).expect().body("person.links[0].rel", is(equalTo("self")))
				.and().header("Content-Type", equalTo(ContentType.XML.toString())).get(PERSON_ONE_URI);

	}

	@Test
	public void testPerson1LinkHref() {
		// assert that the href contains api/people/1
		expect().body("links.href.get(0)", containsString("api/people/" + code)).get(PERSON_ONE_URI);
	}

	@Test
	public void testPerson1LinkHrefForXml() {
		given().header("Accept", ContentType.XML.toString()).expect()
				.body("person.links[0].href", containsString("api/people/" + code)).and()
				.header("Content-Type", equalTo(ContentType.XML.toString())).get(PERSON_ONE_URI);
	}

	@Test
	public void testPerson1LinkProducesJSON() {
		// assert that this link produces JSON
		expect().body("links.type.get(0)", is(ContentType.JSON.toString())).get(PERSON_ONE_URI);
	}

	@Test
	public void testPerson1LinkProducesXml() {
		// assert that this link produces JSON
		given().header("Accept", ContentType.XML.toString()).expect()
				.body("person.links[0].type", is(ContentType.XML.toString())).get(PERSON_ONE_URI);
	}

	@Test
	public void testPerson1BodyXML() {
		String xmlResponse = given().header("Accept", ContentType.XML.toString()).get(PERSON_ONE_URI).asString();
		assertThat(XmlPath.from(xmlResponse).getString("person.name"), equalTo(name));
		assertThat(XmlPath.from(xmlResponse).getString("person.email"), equalTo(email));
		given().header("Accept", ContentType.XML.toString()).expect().body("person.name", equalTo(name))
				.get(PERSON_ONE_URI);
	}

	@Test
	public void testPerson1HeaderJSON() {
		expect().header("Content-Type", CustomMediaType.APPLICATION_PEOPLE).get(PERSON_ONE_URI);
	}

	@Test
	public void testPerson1HeaderXML() {
		given().header("Accept", ContentType.XML.toString()).expect()
				.header("Content-Type", ContentType.XML.toString()).get(PERSON_ONE_URI);
	}

	@Ignore
	@Test
	public void testPeopleJSONArray() {
		System.out.println(get("/people/JSONArray").asString());
	}

	@Test
	public void testPeopleLinksHeader() {
		// build expected header for Link as
		// "<http://baseURI:port/basePath/PERSON_ONE_URI>;rel=self"
		StringBuilder expectedHeader = new StringBuilder("<");
		expectedHeader.append(RestAssured.baseURI);
		expectedHeader.append(":");
		expectedHeader.append(RestAssured.port);
		expectedHeader.append(RestAssured.basePath);
		expectedHeader.append("/");
		expectedHeader.append(PERSON_ONE_URI);
		expectedHeader.append(">;rel=self");
		// test link header accepting XML
		given().header("Accept", ContentType.XML.toString()).expect().header("Link", notNullValue()).and()
				.header("Link", expectedHeader.toString()).get(PERSON_ONE_URI);
		// test link header accepting JSON
		given().header("Accept", ContentType.JSON.toString()).expect().header("Link", notNullValue()).and()
				.header("Link", expectedHeader.toString()).get(PERSON_ONE_URI);
		LOG.info("Link header = " + get(PERSON_ONE_URI).getHeader("Link"));
	}

	@Ignore
	@Test
	public void testGetPersonHead() {
		// test HEAD on XML
		given().header("Accept", ContentType.XML.toString()).expect()
				.header("Content-Type", ContentType.XML.toString()).and().body(equalTo("")).head(PERSON_ONE_URI);
		// test HEAD on JSON
		expect().header("Content-Type", ContentType.JSON.toString()).and().body(equalTo("")).head(PERSON_ONE_URI);
	}

	@Test
	public void testGetPersonOptions() {
		// Build expected base path 'http://baseURI:port/basePath/'
		StringBuilder expectedPathBuilder = new StringBuilder();
		expectedPathBuilder.append(RestAssured.baseURI);
		expectedPathBuilder.append(":");
		expectedPathBuilder.append(RestAssured.port);
		expectedPathBuilder.append(RestAssured.basePath);
		expectedPathBuilder.append("/");
		String expectedBasePath = expectedPathBuilder.toString();
		// test OPTIONS on XML
		given().header("Accept", ContentType.XML.toString()).expect()
				.header("Content-Type", "application/vnd.sun.wadl+xml").header("Allow", containsString("GET"))
				.header("Allow", containsString("PUT")).header("Allow", containsString("HEAD"))
				.header("Allow", not(containsString("POST"))).and()
				.body(hasXPath("/application/resources[@base='" + expectedBasePath + "']")).and()
				.body(hasXPath("/application/resources/resource[@path='" + PERSON_ONE_URI + "']")).when()
				.options(PERSON_ONE_URI);
		// test OPTIONS on JSON
		given().header("Accept", ContentType.JSON.toString()).expect()
				.header("Content-Type", "application/vnd.sun.wadl+xml").header("Allow", containsString("GET"))
				.header("Allow", containsString("HEAD")).header("Allow", containsString("OPTIONS"))
				.header("Allow", containsString("PUT")).header("Allow", not(containsString("POST")))
				.body(hasXPath("/application/resources[@base='" + expectedBasePath + "']")).and()
				.body(hasXPath("/application/resources/resource[@path='" + PERSON_ONE_URI + "']")).when()
				.options(PERSON_ONE_URI);

	}

	@Ignore
	@Test
	public void testPostNewPersonJSON() throws IOException {
		// set up a new Person resource using POST
		BufferedReader reader = getNewPersonJson();
		StringBuilder jsonForPerson = new StringBuilder();
		String currentLine = null;
		while ((currentLine = reader.readLine()) != null) {
			jsonForPerson.append(currentLine.trim());
		}
		System.out.println("JSON = " + jsonForPerson.toString());
		Response response = given().header("Content-Type", ContentType.JSON.toString()).request()
				.body(jsonForPerson.toString()).when().post("people");
		// System.out.println("POST response = " + response.asString());
		assertThat(response.getStatusCode(), equalTo(Status.CREATED.getStatusCode()));
		JsonPath newPersonJson = response.getBody().jsonPath();
		assertThat(newPersonJson, notNullValue());
		assertThat(newPersonJson.getString("name"), equalTo("Very"));
		assertThat(newPersonJson.getString("initials"), equalTo("V."));
		assertThat(newPersonJson.getString("email"), equalTo("New"));
	}

	@Test
	public void testPutUpdatedPersonNameJSON() {
		// set up updated Person
		LOG.debug("testPutUpdatedPersonNameJSON"
				+ given().header("Accept", ContentType.JSON.toString()).get(PERSON_TO_UPDATE_URI).asString());
		PersonV1 personToUpdate = given().header("Content-Type", ContentType.JSON.toString()).get(PERSON_TO_UPDATE_URI)
				.as(PersonV1.class);
		assertThat(personToUpdate, notNullValue());
		String oldName = personToUpdate.getName();
		// update name
		String newName = "Changed";
		personToUpdate.setName(newName);
		Response response = given().contentType(ContentType.JSON).body(personToUpdate)
				.put("people/" + personToUpdate.getCode());
		assertThat(response.getStatusCode(), equalTo(Status.OK.getStatusCode()));
		assertThat(response.getBody().jsonPath().getString("name"), equalTo(newName));
		// set it back again
		personToUpdate.setName(oldName);
		given().contentType(ContentType.JSON).body(personToUpdate).put("people/" + personToUpdate.getCode());
	}

	@Test
	public void testPutUpdatedPersonEmailJSON() {
		// set up updated Person
		PersonV1 personToUpdate = given().header("Accept", ContentType.JSON.toString()).get(PERSON_TO_UPDATE_URI)
				.as(PersonV1.class);
		assertThat(personToUpdate, notNullValue());
		String oldEmail = personToUpdate.getEmail();
		// update email
		String newEmail = "Changed@email.co.uk";
		personToUpdate.setEmail(newEmail);
		Response response = given().contentType(ContentType.JSON).body(personToUpdate)
				.put("people/" + personToUpdate.getCode());
		assertThat(response.getStatusCode(), equalTo(Status.OK.getStatusCode()));
		assertThat(response.getBody().jsonPath().getString("email"), equalTo(newEmail));
		// set it back again
		personToUpdate.setEmail(oldEmail);
		given().contentType(ContentType.JSON).body(personToUpdate).put("people/" + personToUpdate.getCode());
	}

	@Test
	public void testPutUpdatedPersonNotModifiedJSON() {
		// set up updated Person
		PersonV1 personToUpdate = given().header("Accept", ContentType.JSON.toString()).get(PERSON_ONE_URI)
				.as(PersonV1.class);
		assertThat(personToUpdate, notNullValue());
		// don't update anything
		Response response = given().contentType(ContentType.JSON).body(personToUpdate)
				.put("people/" + personToUpdate.getCode());
		assertThat(response.getStatusCode(), equalTo(Status.NOT_MODIFIED.getStatusCode()));

	}

	@Test
	public void testPersonNotFound() {
		expect().statusCode(equalTo(javax.ws.rs.core.Response.Status.NOT_FOUND.getStatusCode())).when()
				.get("people/notfound");
	}

	// @Test
	// public void testPersonAddressesJSON() {
	// LOG.debug("Get for addressess" + get(PERSON_ONE_URI).asString());
	// expect().body("addresses", notNullValue())
	// .and().statusCode(200)
	// .and().body("addresses.get(0).id", equalTo(10))
	// .and().body("addresses.get(0).line1", equalTo("line1"))
	// .and().body("addresses.get(0).city", equalTo("City"))
	// .and().body("addresses.get(0).postcode", equalTo("postcode"))
	// .when().get(PERSON_ONE_URI);
	// }

	private BufferedReader getNewPersonJson() throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(newPersonJsonResource.getFile()));
		} catch (IOException e) {
			fail(e.getMessage());
		}
		return reader;
	}

}
