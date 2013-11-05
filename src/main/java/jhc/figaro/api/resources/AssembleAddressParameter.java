package jhc.figaro.api.resources;

import javax.ws.rs.core.UriInfo;

import jhc.figaro.api.model.Address;

public class AssembleAddressParameter {
	public final Address address;
	public final UriInfo uriInfo;
	public final String mediaTypes;

	public AssembleAddressParameter(Address address, UriInfo uriInfo, String mediaTypes) {
		this.address = address;
		this.uriInfo = uriInfo;
		this.mediaTypes = mediaTypes;
	}
}