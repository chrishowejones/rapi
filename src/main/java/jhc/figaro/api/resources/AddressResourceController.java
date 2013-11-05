package jhc.figaro.api.resources;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import jhc.figaro.api.model.Address;
import jhc.figaro.api.model.Addresses;
import jhc.figaro.api.service.AddressServiceResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Address resource controller - responsible for handling requests for Address
 * resources.
 * 
 * @author Chris Howe-Jones
 * 
 */
@Path("/people/{personCode}/addresses")
@Controller
public class AddressResourceController {

	private static final Logger LOG = LoggerFactory.getLogger(AddressResourceController.class);
	private AddressServiceResolver addressServiceResolver;
	private AddressResourceLinkAssembler assembler;
	private UriInfo uriInfo;

	/**
	 * Get addresses for a Person.
	 * 
	 * @param personCode
	 *            - owning person code.
	 * @param mediaTypes
	 *            - media types from accept header.
	 * @param page
	 *            - page number - defaults to zero.
	 * @param version
	 *            - version number.
	 * @return addresses for the person whose code is specified.
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public final Response getAddressesForPerson(@PathParam("personCode") final String personCode,
			@HeaderParam("Accept") final String mediaTypes, @PathParam("page") @DefaultValue("0") final int page,
			@PathParam("version") final String version) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("getAddressForPerson(" + personCode + ", " + mediaTypes + ", " + page + ", " + version + ")");
		}
		Response response = null;
		String versionKey = version;
		if (version == null || version.equalsIgnoreCase("current")) {
			versionKey = "v1";
		}
		Addresses<? extends Address> addresses = getAddressServiceResolver().getAddressService(versionKey)
				.getAddressesByPerson(personCode, page);
		if (addresses == null || addresses.getNumberOfElements() == 0) {
			response = Response.noContent().build();
		} else {
			addresses = assembler.assembleAddresses(addresses, uriInfo, mediaTypes);
			response = Response.ok(addresses).build();
		}
		return response;
	}

	/**
	 * Set the address service resolver responsible for getting Address Service.
	 * 
	 * @param addressServiceResolver
	 *            - address service resolver.
	 */
	@Autowired
	public final void setAddressServiceResolver(final AddressServiceResolver addressServiceResolver) {
		this.addressServiceResolver = addressServiceResolver;
	}

	/**
	 * Get the address service resolver responsible for getting Address Service.
	 * 
	 * @return address service resolver.
	 */
	public final AddressServiceResolver getAddressServiceResolver() {
		return addressServiceResolver;
	}

	/**
	 * Set address link assembler.
	 * 
	 * @param assembler
	 *            - used to assemble addresses and their associated links.
	 */
	@Autowired
	public final void setAddressAssembler(final AddressResourceLinkAssembler assembler) {
		this.assembler = assembler;
	}

	/**
	 * Get address link assembler.
	 * 
	 * @return assembler - used to assemble addresses and their associated
	 *         links.
	 */
	public final AddressResourceLinkAssembler getAddressAssembler() {
		return assembler;
	}

	/**
	 * UriInfo for request.
	 * 
	 * @param uriInfo
	 *            - UriInfo used to get request and URI information.
	 */
	@Context
	public final void setUriInfo(final UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}

	/**
	 * Get an Address resource by it's id.
	 * 
	 * @param id
	 *            - identifier of the address resource.
	 * @param mediaTypes
	 *            - requested media types.
	 * @param version
	 *            - version requested.
	 * @return response containing the Address resource.
	 */
	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public final Response getAddressById(@PathParam("id") final Long id,
			@HeaderParam("Accept") final String mediaTypes, @PathParam("version") final String version) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("getAddressById(" + id + ", " + mediaTypes + ", " + version + ")");
		}
		Response response = null;
		String versionKey = getVersionKey(version);
		Address address = getAddressServiceResolver().getAddressService(versionKey).getAddressById(id);
		if (address == null) {
			response = Response.noContent().build();
		} else {
			address = assembler.assembleAddress(new AssembleAddressParameter(address, uriInfo, mediaTypes));
			response = Response.ok(address).build();
		}
		return response;
	}

	/**
	 * Look up version.
	 * 
	 * @param version
	 *            - initial requested version - may be null.
	 * @return version key to use for service resolver.
	 */
	private String getVersionKey(final String version) {
		String versionKey = version;
		if (version == null || version.equalsIgnoreCase("current")) {
			versionKey = "v1";
		}
		return versionKey;
	}

}
