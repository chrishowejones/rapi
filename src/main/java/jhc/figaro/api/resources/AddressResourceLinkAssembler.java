package jhc.figaro.api.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import jhc.figaro.api.model.Address;
import jhc.figaro.api.model.Addresses;
import jhc.figaro.api.resource.framework.AtomLink;
import jhc.figaro.api.resource.framework.AtomRelationship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Responsible for assembling links for the Address resource.
 * 
 * @author Chris Howe-Jones
 * 
 */
@Component
public class AddressResourceLinkAssembler {

	private static final Logger LOG = LoggerFactory.getLogger(AddressResourceLinkAssembler.class);
	private MediaTypeResolver mediaTypeResolver;
	private UriInfo uriInfo;
	

	/**
	 * Assemble an Address resource with associated links populated.
	 * @param parameterObject TODO
	 * 
	 * @return address resource populated with appropriate links.
	 */
	public final Address assembleAddress(AssembleAddressParameter parameterObject) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("assembleAddress(" + parameterObject.address + ", " + ", " + parameterObject.uriInfo + ", " + parameterObject.mediaTypes + ")");
		}
		UriBuilder uriBuilder = parameterObject.uriInfo.getRequestUriBuilder();
		String href = null;
		if (parameterObject.uriInfo.getPath().endsWith("addresses")) {
			href = uriBuilder.path("{id}").build(Long.toString(parameterObject.address.getId())).toString();
		} else {
			href = uriBuilder.build().toString();
		}
		ArrayList<AtomLink> links = new ArrayList<AtomLink>();
		AtomLink self = new AtomLink(AtomRelationship.SELF, href, mediaTypeResolver.resolveMediaType(parameterObject.mediaTypes));
		links.add(self);
		parameterObject.address.setLinks(links);
		// TODO add links for parent person
		return parameterObject.address;
	}

	/**
	 * Assemble all Address resources in list under Addresses.
	 * 
	 * @param addresses
	 *            - containing Address resources.
	 * @param uriInfo
	 *            - used to determine path.
	 * @param mediaTypes
	 *            - media types requested.
	 * @param <T>
	 *            - type of Address this Addresses wraps.
	 * @return addresses resource with all associated addresses links populated
	 *         plus links for itself.
	 */
	public final <T extends Address> Addresses<T> assembleAddresses(final Addresses<T> addresses,
			final UriInfo uriInfo, final String mediaTypes) {
		List<Address> addressList = new ArrayList<Address>();
		Addresses<T> addressesToReturn = addresses.clone();
		for (Address address : addressesToReturn.getAddressList()) {
			Address assembledAddress = assembleAddress(new AssembleAddressParameter(address, uriInfo, mediaTypes));
			addressList.add(assembledAddress);
		}
		addressesToReturn.setAddressList((List<T>) addressList);
		addLinks(addressesToReturn, uriInfo, mediaTypeResolver.resolveMediaType(mediaTypes));
		return addressesToReturn;
	}

	private void addLinks(final Addresses<? extends Address> addresses, final UriInfo uriInfo, final String mediaTypes) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("addLinks(" + addresses + ", " + uriInfo + ", " + mediaTypes + ")");
		}
		List<AtomLink> links = new ArrayList<AtomLink>();
		String rootHref = uriInfo.getRequestUriBuilder().build(addresses.getAddressList().get(0).getParentId())
				.toString();
		if (addresses.hasNextPage()) {
			// create link for next page.
			StringBuilder hrefBuilder = new StringBuilder(rootHref);
			hrefBuilder.append("?page=");
			hrefBuilder.append(addresses.getPageNumber() + 1);
			AtomLink next = new AtomLink(AtomRelationship.NEXT, hrefBuilder.toString(), mediaTypes);
			links.add(next);
		}
		if (addresses.hasPreviousPage()) {
			// create link for next page.
			StringBuilder hrefBuilder = new StringBuilder(rootHref);
			hrefBuilder.append("?page=");
			hrefBuilder.append(addresses.getPageNumber() - 1);
			AtomLink prev = new AtomLink(AtomRelationship.PREV, hrefBuilder.toString(), mediaTypes);
			links.add(prev);
		}
		if (!links.isEmpty()) {
			addresses.setLinks(links);
		}
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

	/**
	 * Get the MediaTypeResolver.
	 * 
	 * @return media type resolver.
	 */
	public final MediaTypeResolver getMediaTypeResolver() {
		return mediaTypeResolver;
	}

}
