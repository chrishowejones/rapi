package jhc.figaro.api.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import jhc.figaro.api.model.People;
import jhc.figaro.api.model.Person;
import jhc.figaro.api.model.impl.v1.PeopleV1;
import jhc.figaro.api.resource.framework.AtomLink;
import jhc.figaro.api.resource.framework.AtomRelationship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Utility responsible for building links for a Person resource.
 * 
 * TODO Need a generic mechanism for generating Links
 * 
 * @author Chris Howe-Jones
 * 
 */
@Component
public class PeopleResourceLinkAssembler {

	private static final Logger LOG = LoggerFactory.getLogger(PeopleResourceLinkAssembler.class);
	private MediaTypeResolver mediaTypeResolver;

	/**
	 * Assemble a Person with it's appropriate links.
	 * 
	 * @param person
	 *            - person resource to add links to.
	 * @param uriInfo
	 *            - URI info used to get request builder.
	 * @param mediaTypes
	 *            - mediaTypes supported.
	 * @return Person with links populated
	 */
	public final Person assemblePerson(final Person person, final UriInfo uriInfo, final String mediaTypes) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("assemblePerson(" + person + ", " + ", " + uriInfo + ", " + mediaTypes + ")");
		}
		UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path(PeopleResourceController.class);
		String href = uriBuilder.path(person.getCode()).build().toString();
		ArrayList<AtomLink> links = new ArrayList<AtomLink>();
		AtomLink self = new AtomLink(AtomRelationship.SELF, href, mediaTypeResolver.resolveMediaType(mediaTypes));
		links.add(self);
		person.setLinks(links);
		if (person.hasAddresses()) {
			uriBuilder = uriInfo.getBaseUriBuilder().path(PeopleResourceController.class);
			StringBuilder hrefBuilder = new StringBuilder();
			hrefBuilder.append(uriBuilder.path(person.getCode()).build().toString());
			// TODO refactor this to use the path of AddressResourceController
			// replacing person code in path - couldn't get this working.
			hrefBuilder.append("/addresses");
			href = hrefBuilder.toString();
			AtomLink addresses = new AtomLink(AtomRelationship.ADDRESSES, href,
					mediaTypeResolver.resolveMediaType(mediaTypes));
			links.add(addresses);
		}
		return person;
	}

	/**
	 * Assemble a list of People setting their links.
	 * 
	 * @param peopleList
	 *            - list of People to add links to.
	 * @param uriInfo
	 *            - URI Info used to get request builder.
	 * @param mediaTypes
	 *            - string listing media types requested.
	 * @return List of People with links.
	 */
	public final List<? extends Person> assemblePeopleList(final List<? extends Person> peopleList,
			final UriInfo uriInfo, final String mediaTypes) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("assemblePeopleList(" + peopleList + ", " + uriInfo + ", " + mediaTypes + ")");
		}
		List<Person> assembledPeople = new ArrayList<Person>();
		for (Person person : peopleList) {
			assemblePerson(person, uriInfo, mediaTypes);
			assembledPeople.add(person);
		}
		return assembledPeople;
	}

	/**
	 * Assemble a People resource by setting up links per Person.
	 * 
	 * @param people
	 *            - People instance to assemble.
	 * @param uriInfo
	 *            - URI Info used to get request builder.
	 * @param mediaTypes
	 *            - string listing media types requested.
	 * @return People object with links.
	 * 
	 */
	public final People<? extends Person> assemblePeople(final People<? extends Person> people, final UriInfo uriInfo,
			final String mediaTypes) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("assemblePeople(" + people + ", " + uriInfo + ", " + mediaTypes + ")");
		}
		PeopleV1 assembledPeople = null;
		assembledPeople = (PeopleV1) people.clone();
		for (Person person : (List<? extends Person>) assembledPeople.getPersonList()) {
			assemblePerson(person, uriInfo, mediaTypes);
		}
		addLinks(assembledPeople, uriInfo, mediaTypeResolver.resolveMediaType(mediaTypes));
		return assembledPeople;
	}

	/*
	 * Add links to the People object.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addLinks(final People people, final UriInfo uriInfo, final String mediaTypes) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("assemblePeopleList(" + people + ", " + uriInfo + ", " + mediaTypes + ")");
		}
		List<AtomLink> links = new ArrayList<AtomLink>();
		UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path(PeopleResourceController.class);
		String rootHref = uriBuilder.build().toString();
		if (people.hasNextPage()) {
			// create link for next page.
			StringBuilder hrefBuilder = new StringBuilder(rootHref);
			hrefBuilder.append("?page=");
			hrefBuilder.append(people.getPageNumber() + 1);
			AtomLink next = new AtomLink(AtomRelationship.NEXT, hrefBuilder.toString(), mediaTypes);
			links.add(next);
		}
		if (people.hasPreviousPage()) {
			// create link for next page.
			StringBuilder hrefBuilder = new StringBuilder(rootHref);
			hrefBuilder.append("?page=");
			hrefBuilder.append(people.getPageNumber() - 1);
			AtomLink prev = new AtomLink(AtomRelationship.PREV, hrefBuilder.toString(), mediaTypes);
			links.add(prev);
		}
		if (!links.isEmpty()) {
			people.setLinks(links);
		}
	}

	/**
	 * Get the MediaTypeResolver.
	 * 
	 * @return media type resolver.
	 */
	public final MediaTypeResolver getMediaTypeResolver() {
		return mediaTypeResolver;
	}

	/**
	 * Set media type resolver.
	 * 
	 * @param resolver
	 *            - media type resolver.
	 */
	@Autowired
	public final void setMediaTypeResolver(final MediaTypeResolver resolver) {
		this.mediaTypeResolver = resolver;
	}

}
