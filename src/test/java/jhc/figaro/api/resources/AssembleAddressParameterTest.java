package jhc.figaro.api.resources;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.ws.rs.core.UriInfo;

import jhc.figaro.api.model.Address;
import jhc.figaro.api.model.impl.v1.AddressV1;

import org.junit.Test;
import org.mockito.Mockito;

public class AssembleAddressParameterTest {

	@Test
	public void testConstructor() {
		Address address = new AddressV1();
		UriInfo uriInfo = Mockito.mock(UriInfo.class);
		String mediaTypes = "mediaTypes";
		AssembleAddressParameter parameter = new AssembleAddressParameter(address, uriInfo, mediaTypes);
		assertThat(parameter.address, is(address));
		assertThat(parameter.uriInfo, is(uriInfo));
		assertThat(parameter.mediaTypes, is(mediaTypes));
	}

}
