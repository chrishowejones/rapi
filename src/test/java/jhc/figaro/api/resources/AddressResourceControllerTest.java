package jhc.figaro.api.resources;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import jhc.figaro.api.model.Addresses;
import jhc.figaro.api.model.impl.v1.AddressV1;
import jhc.figaro.api.model.impl.v1.AddressesV1;
import jhc.figaro.api.resource.framework.AtomRelationship;
import jhc.figaro.api.service.AddressService;
import jhc.figaro.api.service.AddressServiceResolver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class AddressResourceControllerTest {

	@Mock
	private AddressService mockAddressService;
	@Mock
	private AddressServiceResolver mockAddressServiceResolver;
	private AddressResourceLinkAssembler assembler;
	@Mock
	private UriInfo mockUriInfo;
	@Mock
	private MediaTypeResolver resolver;
	private String path = "/people/A00001/addresses";

	@Before
	public void setUp() {
		assembler = new AddressResourceLinkAssembler();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testGetAddressesForPerson() {
		List<AddressV1> mockAddressList = new ArrayList<AddressV1>();
		Long id = 10l;
		AddressV1 address = new AddressV1();
		address.setId(id);
		address.setPersonCode("A00001");
		mockAddressList.add(address);
		Addresses mockAddresses = new AddressesV1();
		mockAddresses.setAddressList(mockAddressList);
		((AddressesV1) mockAddresses).setNumberOfElements(1);
		((AddressesV1) mockAddresses).setPageSize(10);

		int defaultPage = 0;
		String personCode = "code1";
		// set up address to be returned from mock service
		when(mockAddressService.getAddressesByPerson(personCode, defaultPage)).thenReturn(mockAddresses);
		// set up address service resolver
		when(mockAddressServiceResolver.getAddressService("v1")).thenReturn(mockAddressService);
		when(mockUriInfo.getRequestUriBuilder()).thenReturn(UriBuilder.fromPath(path), UriBuilder.fromPath(path),
				UriBuilder.fromPath(path));
		when(mockUriInfo.getPath()).thenReturn(path);
		when(resolver.resolveMediaType(any(String.class))).thenReturn(MediaType.APPLICATION_JSON);

		// instantiate AddressResource
		AddressResourceController addressResource = new AddressResourceController();
		// inject mock address resolver
		addressResource.setAddressServiceResolver(mockAddressServiceResolver);
		// set up assembler
		addressResource.setAddressAssembler(assembler);
		assembler.setMediaTypeResolver(resolver);
		// set up mock UriInfo
		addressResource.setUriInfo(mockUriInfo);
		// invoke getAddressForPerson
		Response addressResponse = addressResource.getAddressesForPerson(personCode, MediaType.APPLICATION_JSON,
				defaultPage, "v1");
		assertThat(addressResponse, notNullValue());
		AddressesV1 addresses = (AddressesV1) addressResponse.getEntity();
		List<AddressV1> actualResponseList = (List<AddressV1>) addresses.getAddressList();
		assertThat(actualResponseList, is(equalTo(mockAddressList)));
		assertThat(actualResponseList.size(), is(equalTo(1)));
		// check links for address
		for (AddressV1 addressReceived : actualResponseList) {
			assertThat(addressReceived.getLinks(), hasSize(1));
			assertThat(addressReceived.getLinks().get(0).getRel(), equalTo(AtomRelationship.SELF.toString()));
		}
	}
	
	@Test
	public void testGetAddressesForPersonNoAddresses() {
		int defaultPage = 0;
		String personCode = "code1";
		// set up address to be returned from mock service
		when(mockAddressService.getAddressesByPerson(personCode, defaultPage)).thenReturn(null);
		// set up address service resolver
		when(mockAddressServiceResolver.getAddressService("v1")).thenReturn(mockAddressService);
		when(mockUriInfo.getRequestUriBuilder()).thenReturn(UriBuilder.fromPath(path), UriBuilder.fromPath(path),
				UriBuilder.fromPath(path));
		when(mockUriInfo.getPath()).thenReturn(path);
		when(resolver.resolveMediaType(any(String.class))).thenReturn(MediaType.APPLICATION_JSON);

		// instantiate AddressResource
		AddressResourceController addressResource = new AddressResourceController();
		// inject mock address resolver
		addressResource.setAddressServiceResolver(mockAddressServiceResolver);
		// set up assembler
		addressResource.setAddressAssembler(assembler);
		assembler.setMediaTypeResolver(resolver);
		// set up mock UriInfo
		addressResource.setUriInfo(mockUriInfo);
		// invoke getAddressForPerson
		Response addressResponse = addressResource.getAddressesForPerson(personCode, MediaType.APPLICATION_JSON,
				defaultPage, "v1");
		assertThat(addressResponse, notNullValue());
		assertThat(addressResponse.getStatus(), is(Response.Status.NO_CONTENT.getStatusCode()));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testGetAddressesForPersonNullVersion() {
		List<AddressV1> mockAddressList = new ArrayList<AddressV1>();
		Long id = 10l;
		AddressV1 address = new AddressV1();
		address.setId(id);
		address.setPersonCode("A00001");
		mockAddressList.add(address);
		Addresses mockAddresses = new AddressesV1();
		mockAddresses.setAddressList(mockAddressList);
		((AddressesV1) mockAddresses).setNumberOfElements(1);
		((AddressesV1) mockAddresses).setPageSize(10);

		int defaultPage = 0;
		String personCode = "code1";
		// set up address to be returned from mock service
		when(mockAddressService.getAddressesByPerson(personCode, defaultPage)).thenReturn(mockAddresses);
		// set up address service resolver
		when(mockAddressServiceResolver.getAddressService("v1")).thenReturn(mockAddressService);
		when(mockUriInfo.getRequestUriBuilder()).thenReturn(UriBuilder.fromPath(path), UriBuilder.fromPath(path),
				UriBuilder.fromPath(path));
		when(mockUriInfo.getPath()).thenReturn(path);
		when(resolver.resolveMediaType(any(String.class))).thenReturn(MediaType.APPLICATION_JSON);

		// instantiate AddressResource
		AddressResourceController addressResource = new AddressResourceController();
		// inject mock address resolver
		addressResource.setAddressServiceResolver(mockAddressServiceResolver);
		// set up assembler
		addressResource.setAddressAssembler(assembler);
		assembler.setMediaTypeResolver(resolver);
		// set up mock UriInfo
		addressResource.setUriInfo(mockUriInfo);
		// invoke getAddressForPerson
		Response addressResponse = addressResource.getAddressesForPerson(personCode, MediaType.APPLICATION_JSON,
				defaultPage, null);
		assertThat(addressResponse, notNullValue());
		AddressesV1 addresses = (AddressesV1) addressResponse.getEntity();
		List<AddressV1> actualResponseList = (List<AddressV1>) addresses.getAddressList();
		assertThat(actualResponseList, is(equalTo(mockAddressList)));
		assertThat(actualResponseList.size(), is(equalTo(1)));
		// check links for address
		for (AddressV1 addressReceived : actualResponseList) {
			assertThat(addressReceived.getLinks(), hasSize(1));
			assertThat(addressReceived.getLinks().get(0).getRel(), equalTo(AtomRelationship.SELF.toString()));
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testGetAddressesForPersonCurrentVersion() {
		List<AddressV1> mockAddressList = new ArrayList<AddressV1>();
		Long id = 10l;
		AddressV1 address = new AddressV1();
		address.setId(id);
		address.setPersonCode("A00001");
		mockAddressList.add(address);
		Addresses mockAddresses = new AddressesV1();
		mockAddresses.setAddressList(mockAddressList);
		((AddressesV1) mockAddresses).setNumberOfElements(1);
		((AddressesV1) mockAddresses).setPageSize(10);

		int defaultPage = 0;
		String personCode = "code1";
		// set up address to be returned from mock service
		when(mockAddressService.getAddressesByPerson(personCode, defaultPage)).thenReturn(mockAddresses);
		// set up address service resolver
		when(mockAddressServiceResolver.getAddressService("v1")).thenReturn(mockAddressService);
		when(mockUriInfo.getRequestUriBuilder()).thenReturn(UriBuilder.fromPath(path), UriBuilder.fromPath(path),
				UriBuilder.fromPath(path));
		when(mockUriInfo.getPath()).thenReturn(path);
		when(resolver.resolveMediaType(any(String.class))).thenReturn(MediaType.APPLICATION_JSON);

		// instantiate AddressResource
		AddressResourceController addressResource = new AddressResourceController();
		// inject mock address resolver
		addressResource.setAddressServiceResolver(mockAddressServiceResolver);
		// set up assembler
		addressResource.setAddressAssembler(assembler);
		assembler.setMediaTypeResolver(resolver);
		// set up mock UriInfo
		addressResource.setUriInfo(mockUriInfo);
		// invoke getAddressForPerson
		Response addressResponse = addressResource.getAddressesForPerson(personCode, MediaType.APPLICATION_JSON,
				defaultPage, "current");
		assertThat(addressResponse, notNullValue());
		AddressesV1 addresses = (AddressesV1) addressResponse.getEntity();
		List<AddressV1> actualResponseList = (List<AddressV1>) addresses.getAddressList();
		assertThat(actualResponseList, is(equalTo(mockAddressList)));
		assertThat(actualResponseList.size(), is(equalTo(1)));
		// check links for address
		for (AddressV1 addressReceived : actualResponseList) {
			assertThat(addressReceived.getLinks(), hasSize(1));
			assertThat(addressReceived.getLinks().get(0).getRel(), equalTo(AtomRelationship.SELF.toString()));
		}
	}
	
	@Test
	public void testSetAddressServiceResolver() {
		AddressResourceController controller = new AddressResourceController();
		assertThat(controller.getAddressServiceResolver(), nullValue());
		// set address service
		controller.setAddressServiceResolver(mockAddressServiceResolver);
		assertThat(controller.getAddressServiceResolver(), equalTo(mockAddressServiceResolver));
	}

	@Test
	public void testGetAddress() {
		Long id = 10l;
		AddressV1 address = new AddressV1();
		address.setId(id);
		address.setPersonCode("A00001");
		
		// mock addressService
		when(mockAddressService.getAddressById(id)).thenReturn(address);
		// set up address service resolver
		when(mockAddressServiceResolver.getAddressService("v1")).thenReturn(mockAddressService);
		when(mockUriInfo.getRequestUriBuilder()).thenReturn(UriBuilder.fromPath(path), UriBuilder.fromPath(path),
				UriBuilder.fromPath(path));
		when(mockUriInfo.getPath()).thenReturn(path);
		when(resolver.resolveMediaType(any(String.class))).thenReturn(MediaType.APPLICATION_JSON);

		// instantiate AddressResource
		AddressResourceController addressResource = new AddressResourceController();
		// inject mock address resolver
		addressResource.setAddressServiceResolver(mockAddressServiceResolver);
		// set up assembler
		addressResource.setAddressAssembler(assembler);
		assembler.setMediaTypeResolver(resolver);
		// set up mock UriInfo
		addressResource.setUriInfo(mockUriInfo);
		// invoke getAddressForPerson
		Response addressResponse = addressResource.getAddressById(id, MediaType.APPLICATION_JSON, "v1");
		
		assertThat(addressResponse, notNullValue());
		AddressV1 addressReturned = (AddressV1)addressResponse.getEntity();
		assertThat(addressReturned, equalTo(address));
		assertThat(addressReturned.getId(), equalTo(id));
		assertThat(addressReturned.getLinks(), hasSize(1));
		assertThat(addressReturned.getLinks().get(0).getRel(), equalTo(AtomRelationship.SELF.toString()));
	}

}
