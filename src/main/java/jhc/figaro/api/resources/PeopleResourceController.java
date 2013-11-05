package jhc.figaro.api.resources;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import jhc.figaro.api.model.People;
import jhc.figaro.api.model.Person;
import jhc.figaro.api.model.impl.v1.PersonV1;
import jhc.figaro.api.resource.framework.Linkable;
import jhc.figaro.api.service.PersonService;
import jhc.figaro.api.service.PersonServiceResolver;

import org.codehaus.jettison.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.api.core.ResourceContext;

/**
 * Responsible for serving Person resources to REST clients.
 * 
 * @author Chris Howe-Jones
 * 
 */
// this is mapped as a 'root' resource and via VersionController
@Path("/people")
@Component
// TODO map the version into this controller
public class PeopleResourceController {

	private static final Logger LOG = LoggerFactory.getLogger(PeopleResourceController.class);

	private PersonServiceResolver resolver;
	private UriInfo uriInfo;
	private PeopleResourceLinkAssembler peopleLinkAssembler;
	private ResourceContext resourceContext;

	private AddressResourceController addressController;

	/**
	 * Get a person by its identifier and return the person in JSON format.
	 * 
	 * @param personCode
	 *            - identifier of the person from the URI path.
	 * @param accept
	 *            - Accept header from HTTP request.
	 * @param version
	 *            - the version from the URI if present.
	 * @return Person that matches the identifier supplied or a response of
	 *         NO_CONTENT (HTTP status 204).
	 */
	@GET
	@Path("/{code}")
	@Produces({ CustomMediaType.APPLICATION_PEOPLE, CustomMediaType.APPLICATION_PEOPLE_XML, MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML })
	public final Linkable getPersonByCode(@PathParam("code") final String personCode,
			@HeaderParam("Accept") final String accept, @PathParam("version") final String version) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("getPersonByCode(" + personCode + ", " + accept + ", " + version + ")");
		}
		PersonV1 person = (PersonV1) resolver.getPersonService(version).getPersonByCode(personCode);
		if (person != null) {
			person = (PersonV1) peopleLinkAssembler.assemblePerson(person, uriInfo, accept);
		} else {
			throw new NotFoundException(uriInfo.getAbsolutePath());
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Returned: " + person);
		}
		return person;
	}

	/**
	 * Search for a person matching on first or last name based on search term
	 * specified or all Person instances if search term not specified. Results
	 * will be paged within result set and pages are numbered from zero upwards.
	 * 
	 * @param search
	 *            - search term to use to retrieve a list of people.
	 * @param page
	 *            - page number required. Defaults to zero (1st page).
	 * @param accept
	 *            - Accept header from HTTP request.
	 * @return People resource that lists Person instances matching search term
	 *         or requested page of Person instances if search term not
	 *         specified. Will return a response of NO_CONTENT (HTTP status 204)
	 *         if no People found or page exceeds total pages.
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public final Response getPeople(@QueryParam("search") final String search,
			@QueryParam("page") @DefaultValue("0") final String page, @HeaderParam("Accept") final String accept) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("getPeople(" + search + ", " + page + ")");
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Accept header : " + accept);
		}
		PersonService personService = resolver.getPersonService("v1");
		People<? extends Person> people = null;
		// determine if search on name required.
		if (search != null && !search.isEmpty()) {
			people = personService.getPersonBySearch(search, Integer.parseInt(page));
		} else {
			people = personService.getPeople(Integer.parseInt(page));
		}
		if (people == null || people.getNumberOfElements() == 0) {
			LOG.info("No content for People request : " + uriInfo.getPath());
			return Response.noContent().build();
		}
		// assemble resources with link
		people = peopleLinkAssembler.assemblePeople(people, uriInfo, accept);
		return Response.ok(people).build();
	}

	/**
	 * Create a new Person resource.
	 * 
	 * @param newPerson
	 *            - person resource to be created.
	 * @param accept
	 *            - accept header.
	 * @return newly created person resource.
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public final Response newPerson(final PersonV1 newPerson, @HeaderParam("Accept") final String accept) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("newPerson(" + newPerson + ")");
		}
		PersonService personService = resolver.getPersonService("v1");
		Person person = personService.newPerson(newPerson);
		// add links to person
		person = peopleLinkAssembler.assemblePerson(person, uriInfo, accept);
		URI createdUri = uriInfo.getRequestUriBuilder().build(person.getCode());
		return Response.created(createdUri).entity(person).build();
	}

	/**
	 * Update a Person resource for the specified person code.
	 * 
	 * @param personCode
	 *            - person code from the URI path.
	 * @param personToUpdate
	 *            - deserialised Person resource (XML or JSON)
	 * @param accept
	 *            - accept header
	 * @return response containing the updated person resource.
	 */
	@PUT
	@Path(("/{code}"))
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public final Response updatePerson(@PathParam("code") final String personCode, final PersonV1 personToUpdate,
			@HeaderParam("Accept") final String accept) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("updatePerson(" + personCode + ", " + personToUpdate + ")");
		}
		PersonService personService = resolver.getPersonService("v1");
		Response response = null;
		Person person = personToUpdate;
		if (personService.isModified(person)) {
			person = personService.update(person);
			// assemble links
			peopleLinkAssembler.assemblePerson(person, uriInfo, accept);
			response = Response.ok().entity(person).build();
		} else {
			response = Response.notModified().build();
		}
		return response;
	}

	/**
	 * Get a JSONArray version of person instances.
	 * 
	 * @return JSON Array.
	 */
	@GET
	@Path("/JSONArray")
	public final JSONArray getPeopleAsJsonArray() {
		JSONArray uriArray = new JSONArray();
		List<? extends Person> personList = resolver.getPersonService("v1").getPeople().getPersonList();
		for (Person person : personList) {
			UriBuilder ub = uriInfo.getRequestUriBuilder();
			URI userUri = ub.path(person.getCode().toString()).build();
			uriArray.put(userUri.toASCIIString());
		}
		return uriArray;
	}

	/**
	 * Sets a Person Service resolver. Used to determine the version of the
	 * Person Service that will be used.
	 * 
	 * @param personServiceResolver
	 *            - resolver for PersonService.
	 */
	@Autowired
	public final void setPersonServiceResolver(final PersonServiceResolver personServiceResolver) {
		this.resolver = personServiceResolver;
	}

	/**
	 * Get Person Service resolver. Used to determine the version of the Person
	 * Service that will be used.
	 * 
	 * @return resolver for Person Service.
	 */
	public final PersonServiceResolver getPersonServiceResolver() {
		return resolver;
	}

	/**
	 * Injects the UriInfo from the Jersey framework.
	 * 
	 * @param info
	 *            - UriInfo from Jersey.
	 */
	@Context
	public final void setUriInfo(final UriInfo info) {
		uriInfo = info;
	}

	/**
	 * Gets the UriInfo from the Jersey framework.
	 * 
	 * @return UriInfo - Uri Info from Jersey.
	 */
	public final UriInfo getUriInfo() {
		return uriInfo;
	}

	/**
	 * Gets the People Assembler used to assemble the Person resource with link
	 * information.
	 * 
	 * @return People Assembler.
	 */
	public final PeopleResourceLinkAssembler getPeopleAssembler() {
		return peopleLinkAssembler;
	}

	/**
	 * Sets the Person Assembler used to assemble the Person resource with Link
	 * information.
	 * 
	 * @param peopleAssembler
	 *            - People Assembler.
	 */
	@Autowired
	public final void setPeopleAssembler(final PeopleResourceLinkAssembler peopleAssembler) {
		this.peopleLinkAssembler = peopleAssembler;
	}

	/**
	 * Get address resource controller.
	 * 
	 * @return address resource controller.
	 */
	public final AddressResourceController getAddressResourceController() {
		if (LOG.isTraceEnabled()) {
			LOG.trace("getAddressResourceController()");
		}
		return addressController;
	}

	/**
	 * Set address resource controller.
	 * 
	 * @param addressController
	 *            - address resource controller.
	 */
    @Autowired
	public final void setAddressResourceController(final AddressResourceController addressController) {
		this.addressController = addressController;
	}

	public ResourceContext getResourceContext() {
		return resourceContext;
	}

	@Context
	public void setResourceContext(ResourceContext resourceContext) {
		this.resourceContext = resourceContext;
	}
}
