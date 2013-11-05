package jhc.figaro.api.resources;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import jhc.figaro.api.model.Address;
import jhc.figaro.api.model.Addresses;
import jhc.figaro.api.model.impl.v1.AddressV1;
import jhc.figaro.api.model.impl.v1.AddressesV1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AddressResourceLinkAssemblerTest {

	@Mock
	private UriInfo uriInfo;
	@Mock
	private MediaTypeResolver mediaTypeResolver;
	private String path = "/people/A00001/addresses";
	
	@Test
	public void testAssembleAddressLinksFromAddressesUri() {
		Long id = new Long(10);
		String personCode = "A00001";
		String mediaTypes = "application/json";
		when(uriInfo.getRequestUriBuilder()).thenReturn(UriBuilder.fromPath(path));
		when(uriInfo.getPath()).thenReturn(path);
		when(mediaTypeResolver.resolveMediaType(MediaType.APPLICATION_JSON)).thenReturn(MediaType.APPLICATION_JSON);

		AddressResourceLinkAssembler assembler = new AddressResourceLinkAssembler();
		assembler.setMediaTypeResolver(mediaTypeResolver);
		AddressV1 addressWithoutLinks = new AddressV1();
		addressWithoutLinks.setId(id);
		addressWithoutLinks.setPersonCode(personCode);
		// assemble AddressV1 links
		AddressV1 addressWithLinks = (AddressV1) assembler.assembleAddress(new AssembleAddressParameter(addressWithoutLinks, uriInfo, mediaTypes));
		assertThat(addressWithLinks.getLinks(), notNullValue());
		assertThat(addressWithLinks.getLinks(), hasSize(1));
		assertThat(addressWithLinks.getLinks().get(0).getRel(), equalTo("self"));
		assertThat(addressWithLinks.getLinks().get(0).getHref(), equalTo("/people/" + personCode + "/addresses/" + id));
		assertThat(addressWithLinks.getLinks().get(0).getType(), equalTo(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testAssembleAddressLinksFromAddressUri() {
		Long id = new Long(10);
		String personCode = "A00001";
		String mediaTypes = "application/json";
		String pathForOne = path + "/" + id;
		when(uriInfo.getRequestUriBuilder()).thenReturn(UriBuilder.fromPath(pathForOne));
		when(uriInfo.getPath()).thenReturn(pathForOne);
		when(mediaTypeResolver.resolveMediaType(MediaType.APPLICATION_JSON)).thenReturn(MediaType.APPLICATION_JSON);

		AddressResourceLinkAssembler assembler = new AddressResourceLinkAssembler();
		assembler.setMediaTypeResolver(mediaTypeResolver);
		AddressV1 addressWithoutLinks = new AddressV1();
		addressWithoutLinks.setId(id);
		addressWithoutLinks.setPersonCode(personCode);
		// assemble AddressV1 links
		AddressV1 addressWithLinks = (AddressV1) assembler.assembleAddress(new AssembleAddressParameter(addressWithoutLinks, uriInfo, mediaTypes));
		assertThat(addressWithLinks.getLinks(), notNullValue());
		assertThat(addressWithLinks.getLinks(), hasSize(1));
		assertThat(addressWithLinks.getLinks().get(0).getRel(), equalTo("self"));
		assertThat(addressWithLinks.getLinks().get(0).getHref(), equalTo("/people/" + personCode + "/addresses/" + id));
		assertThat(addressWithLinks.getLinks().get(0).getType(), equalTo(MediaType.APPLICATION_JSON));
	}


	@Test
	@SuppressWarnings("unchecked")
	public void testAssembleAddressesLinks() {
		Long id = new Long(10);
		String personCode = "A00001";
		String mediaTypes = "application/json";
		Long id2 = new Long(11);
		when(uriInfo.getRequestUriBuilder()).thenReturn(UriBuilder.fromPath(path),
				UriBuilder.fromPath(path),
				UriBuilder.fromPath(path),
				UriBuilder.fromPath(path),
				UriBuilder.fromPath(path));
		when(uriInfo.getPath()).thenReturn(path);
		when(mediaTypeResolver.resolveMediaType(MediaType.APPLICATION_JSON)).thenReturn(MediaType.APPLICATION_JSON);

		AddressResourceLinkAssembler assembler = new AddressResourceLinkAssembler();
		assembler.setMediaTypeResolver(mediaTypeResolver);
		AddressV1 addressWithoutLinks = new AddressV1();
		addressWithoutLinks.setId(id);
		addressWithoutLinks.setPersonCode(personCode);
		AddressV1 addressWithoutLinks2 = new AddressV1();
		addressWithoutLinks2.setId(id2);
		addressWithoutLinks2.setPersonCode(personCode);
		List<AddressV1> addressList = new ArrayList<AddressV1>();
		addressList.add(addressWithoutLinks);
		addressList.add(addressWithoutLinks2);
		AddressesV1 addresses = new AddressesV1();
		addresses.setAddressList(addressList);
		addresses.setPageSize(10);
		addresses.setTotalElements(100);
		addresses.setPageNumber(2);
		// assemble AddressV1 links
		Addresses<? extends Address> addressesWithLinks = assembler.assembleAddresses(addresses, uriInfo, mediaTypes);
		for (AddressV1 addressWithLinks : (List<AddressV1>) addressesWithLinks.getAddressList()) {
			assertThat(addressWithLinks.getLinks(), notNullValue());
			assertThat(addressWithLinks.getLinks(), hasSize(1));
			assertThat(addressWithLinks.getLinks().get(0).getRel(), equalTo("self"));
			assertThat(addressWithLinks.getLinks().get(0).getHref(),
					equalTo("/people/" + addressWithLinks.getPersonCode() + "/addresses/" + addressWithLinks.getId()));
			assertThat(addressWithLinks.getLinks().get(0).getType(), equalTo(MediaType.APPLICATION_JSON));
		}
		// check links for Addresses itself.
		assertThat(addressesWithLinks.getLinks(), notNullValue());
		// check next and prev links
		assertThat(addressesWithLinks.getLinks().get(0).getRel(), equalTo("next"));
		assertThat(addressesWithLinks.getLinks().get(0).getHref(), containsString("/people/A00001/addresses?page=3"));
		assertThat(addressesWithLinks.getLinks().get(0).getType(), equalTo(mediaTypes));
		assertThat(addressesWithLinks.getLinks().get(1).getRel(), equalTo("prev"));
		assertThat(addressesWithLinks.getLinks().get(1).getHref(), containsString("/people/A00001/addresses?page=1"));
		assertThat(addressesWithLinks.getLinks().get(1).getType(), equalTo(mediaTypes));

	}
	
	@Test
	public void testGetMediaTypeResolver() {
		AddressResourceLinkAssembler assembler = new AddressResourceLinkAssembler();
		assembler.setMediaTypeResolver(mediaTypeResolver);
		assertThat(assembler.getMediaTypeResolver(), is(mediaTypeResolver));
	}
	
}
