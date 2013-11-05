package integration.jhc.figaro.api;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

public class PeopleCollectionITCase {

    private static final String BASE_PATH = "/rapi";

    private static final String BASE_URI = "http://localhost";

    private static final int PORT = 9090;

    private final Logger LOG = LoggerFactory.getLogger(PeopleCollectionITCase.class);

    private String rootPath;

    private static final int EXPECTED_SIZE_OF_SEARCH_LIST = 3;
    /*
     * Expected page size set in SpringTestConfig for development profile.
     */
    private static final int EXPECTED_SIZE_OF_PAGE = 10;
    /*
     * Expected total elements in PersonDataset.xml
     */
    private static final String EXPECTED_TOTAL_ELEMENTS = "33";
    /*
     * Expected total pages dependent on total elements and page size
     */
    private static final String EXPECTED_TOTAL_PAGES = "4";
    private static final String PERSONS_SEARCH_URI = "people?search=BARNES TEST";
    private static final String PERSONS_SEARCH_NO_MATCH_URI = "people?search=doesnotmatch";
    private static final String PERSONS_GROUP_URI = "people";
    private final static String name = "MR DOODAH BARNES TEST";

    @Before
    public void setUp() {
        RestAssured.port = PORT;
        RestAssured.baseURI = BASE_URI;
        RestAssured.basePath = BASE_PATH;
        rootPath = BASE_URI + ":" + PORT + BASE_PATH + "/";
    }

    /*
     * Test getting a collection of People based on a search
     */
    @Test
    public void testGetsPeopleSearch() {
        LOG.debug("testGetsPeopleSearch");
        LOG.debug("get " + PERSONS_SEARCH_URI + " returned = '" + get(PERSONS_SEARCH_URI).asString() + "'");
        expect().statusCode(200).and().body("person.name", hasItem(containsString(name))).and()
                .body("person.name", hasSize(EXPECTED_SIZE_OF_SEARCH_LIST)).and().contentType(ContentType.JSON).when()
                .get(PERSONS_SEARCH_URI);
    }

    /*
     * Test getting a collection of People based on a search
     */
    @Test
    public void testGetsPeopleSearchNoMatch() {
        LOG.debug("testGetsPeopleSearchNoMatch");
        LOG.debug("get " + PERSONS_SEARCH_NO_MATCH_URI + " returned = '" + get(PERSONS_SEARCH_NO_MATCH_URI).asString()
                + "'");
        expect().statusCode(Status.NO_CONTENT.getStatusCode()).expect().body(equalTo(""))
                .get(PERSONS_SEARCH_NO_MATCH_URI);
    }

    /*
     * Test links on a collection of People based on a search
     */
    @Test
    public void testGetsPeopleSearchLinks() {
        LOG.debug("testGetsPeopleLinks");
        expect().body("person.rel", hasSize(EXPECTED_SIZE_OF_SEARCH_LIST)).and().contentType(ContentType.JSON).and()
                .statusCode(200).when().get(PERSONS_SEARCH_URI);
    }

    /*
     * Test default implementation of get persons returns a sort list (ascending by name) paged to a maximum number of
     * rows of 300.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDefaultGetPeople() {
        LOG.debug("testDefaultGetPeople");
        String response = given().header("Accept", MediaType.APPLICATION_JSON).get(PERSONS_GROUP_URI).asString();
        JsonPath json = JsonPath.from(response);
        assertThat((List<String>) json.get("person.code"), hasSize(EXPECTED_SIZE_OF_PAGE));
        // sort returned list of codes and ensure it's in same order as that returned
        List<String> codesReturned = (List<String>) json.get("person.code");
        List<String> sortedCodes = new ArrayList<String>(codesReturned);
        Collections.sort(sortedCodes);
        assertThat(codesReturned, equalTo(sortedCodes));
    }

    /*
     * Test default implementation of get persons returns a sort list (ascending by name) paged to a maximum number of
     * rows of 10.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPeoplePagedFirstPage() {
        LOG.debug("testGetPeoplePagedFirstPage");
        String response = given().header("Accept", MediaType.APPLICATION_JSON).get(PERSONS_GROUP_URI + "?page=0")
                .asString();
        LOG.debug("get " + PERSONS_GROUP_URI + "?page=0 returned : " + response);
        JsonPath json = JsonPath.from(response);
        assertThat((List<String>) json.get("person.code"), hasSize(EXPECTED_SIZE_OF_PAGE));
        // sort returned list of codes and ensure it's in same order as that returned
        List<String> codesReturned = (List<String>) json.get("person.code");
        List<String> sortedCodes = new ArrayList<String>(codesReturned);
        Collections.sort(sortedCodes);
        assertThat(codesReturned, equalTo(sortedCodes));
        assertThat(json.getString("pageSize"), equalTo(Integer.toString(EXPECTED_SIZE_OF_PAGE)));
        assertThat(json.getString("totalElements"), equalTo(EXPECTED_TOTAL_ELEMENTS));
        assertThat(json.getString("totalPages"), equalTo(EXPECTED_TOTAL_PAGES));
        assertThat(json.getString("numberOfElements"), equalTo("10"));
        assertThat(json.getString("pageNumber"), equalTo("0"));
        assertThat(json.getString("hasNextPage"), nullValue());
        assertThat(json.getString("hasPreviousPage"), nullValue());
        assertThat(json.getString("isFirstPage"), nullValue());
        assertThat(json.getString("isLastPage"), nullValue());
        assertThat(json.getString("iterator"), nullValue());
    }

    /*
     * Test default implementation of get persons returns the expected page links.
     */
    @Test
    public void testGetPeoplePageLinksPage0() {
        LOG.debug("testGetPeoplePageLinksPage0");
        String response = given().header("Accept", MediaType.APPLICATION_JSON).get(PERSONS_GROUP_URI + "?page=0")
                .asString();
        LOG.debug("get " + PERSONS_GROUP_URI + "?page=0 returned : " + response);
        JsonPath json = JsonPath.from(response);
        assertThat(json.getString("links"), notNullValue());
        assertThat(json.getString("links.rel.get(0)"), equalTo("next"));
        assertThat(json.getString("links.href.get(0)"), equalTo(rootPath + PERSONS_GROUP_URI + "?page=1"));
        assertThat(json.getString("links.type.get(0)"), equalTo(MediaType.APPLICATION_JSON));
    }

    /*
     * Test default implementation of get persons returns the expected page links.
     */
    @Test
    public void testGetPeoplePageLinksPage1() {
        LOG.debug("testGetPeoplePageLinksPage1");
        String response = given().header("Accept", MediaType.APPLICATION_JSON).get(PERSONS_GROUP_URI + "?page=1")
                .asString();
        LOG.debug("get " + PERSONS_GROUP_URI + "?page=1 returned : " + response);
        JsonPath json = JsonPath.from(response);
        assertThat(json.getString("links"), notNullValue());
        assertThat(json.getList("links"), hasSize(2));
        assertThat(json.getString("links.get(0).rel"), equalTo("next"));
        assertThat(json.getString("links.get(0).href"), equalTo(rootPath + PERSONS_GROUP_URI + "?page=2"));
        assertThat(json.getString("links.get(0).type"), equalTo(MediaType.APPLICATION_JSON));
        assertThat(json.getString("links.get(1).rel"), equalTo("prev"));
        assertThat(json.getString("links.get(1).href"), equalTo(rootPath + PERSONS_GROUP_URI + "?page=0"));
        assertThat(json.getString("links.get(1).type"), equalTo(MediaType.APPLICATION_JSON));
        assertThat(json.getString("hasNextPage"), nullValue());
        assertThat(json.getString("hasPreviousPage"), nullValue());
        assertThat(json.getString("isFirstPage"), nullValue());
        assertThat(json.getString("isLastPage"), nullValue());
        assertThat(json.getString("iterator"), nullValue());
    }

    /*
     * Test default implementation of get persons returns the expected page links.
     */
    @Test
    public void testGetPeoplePageLinksPage3() {
        LOG.debug("testGetPeoplePageLinksPage3");
        String response = given().header("Accept", MediaType.APPLICATION_JSON).get(PERSONS_GROUP_URI + "?page=3")
                .asString();
        LOG.debug("get " + PERSONS_GROUP_URI + "?page=3 returned : " + response);
        JsonPath json = JsonPath.from(response);
        assertThat(json.getString("links"), notNullValue());
        assertThat(json.getList("links"), hasSize(1));
        assertThat(json.getString("links.get(0).rel"), equalTo("prev"));
        assertThat(json.getString("links.get(0).href"), equalTo(rootPath + PERSONS_GROUP_URI + "?page=2"));
        assertThat(json.getString("links.get(0).type"), equalTo(MediaType.APPLICATION_JSON));
        // check the elements
        assertThat(json.getString("numberOfElements"), equalTo("3"));

    }

    /*
     * Test default implementation of get persons returns a sort list (ascending by name) paged to a maximum number of
     * rows of 10.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPeoplePagedSecondPage() {
        LOG.debug("testGetPeoplePagedSecondPage");
        String response = given().header("Accept", MediaType.APPLICATION_JSON).get(PERSONS_GROUP_URI + "?page=1")
                .asString();
        LOG.debug("get " + PERSONS_GROUP_URI + "?page=1 returned : " + response);
        JsonPath json = JsonPath.from(response);
        assertThat((List<String>) json.get("person.code"), hasSize(EXPECTED_SIZE_OF_PAGE));
        // sort returned list of codes and ensure it's in same order as that returned
        List<String> codesReturned = (List<String>) json.get("person.code");
        List<String> sortedCodes = new ArrayList<String>(codesReturned);
        Collections.sort(sortedCodes);
        assertThat(codesReturned, equalTo(sortedCodes));
        assertThat(json.getString("pageSize"), equalTo(Integer.toString(EXPECTED_SIZE_OF_PAGE)));
        assertThat(json.getString("totalElements"), equalTo(EXPECTED_TOTAL_ELEMENTS));
        assertThat(json.getString("totalPages"), equalTo(EXPECTED_TOTAL_PAGES));
        assertThat(json.getString("numberOfElements"), equalTo("10"));
        assertThat(json.getString("pageNumber"), equalTo("1"));
    }

    /*
     * Test default implementation of get persons returns a sort list (ascending by name) paged to a maximum number of
     * rows of 10.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetPeoplePagedLastPage() {
        LOG.debug("testGetPeoplePagedLastPage");
        String response = given().header("Accept", MediaType.APPLICATION_JSON).get(PERSONS_GROUP_URI + "?page=3")
                .asString();
        LOG.debug("get " + PERSONS_GROUP_URI + "?page=1 returned : " + response);
        JsonPath json = JsonPath.from(response);
        assertThat((List<String>) json.get("person.code"), hasSize(3));
        // sort returned list of codes and ensure it's in same order as that returned
        List<String> codesReturned = (List<String>) json.get("person.code");
        List<String> sortedCodes = new ArrayList<String>(codesReturned);
        Collections.sort(sortedCodes);
        assertThat(codesReturned, equalTo(sortedCodes));
        assertThat(json.getString("pageSize"), equalTo(Integer.toString(EXPECTED_SIZE_OF_PAGE)));
        assertThat(json.getString("totalElements"), equalTo(EXPECTED_TOTAL_ELEMENTS));
        assertThat(json.getString("totalPages"), equalTo(EXPECTED_TOTAL_PAGES));
        assertThat(json.getString("numberOfElements"), equalTo("3"));
        assertThat(json.getString("pageNumber"), equalTo("3"));
    }

    /*
     * Test default implementation of get persons returns a sort list (ascending by name) paged to a maximum number of
     * rows of 10.
     */
    @Test
    public void testGetPeoplePagedBeyondLastPage() {
        LOG.debug("testGetPeoplePagedBeyondLastPage");
        Response response = given().header("Accept", MediaType.APPLICATION_JSON).get(PERSONS_GROUP_URI + "?page=5");
        LOG.debug("get " + PERSONS_GROUP_URI + "?page=5 returned : " + response);
        // No content
        assertThat(response.getStatusCode(), equalTo(Status.NO_CONTENT.getStatusCode()));
        assertThat(response.asString(), equalTo(""));
    }
}
