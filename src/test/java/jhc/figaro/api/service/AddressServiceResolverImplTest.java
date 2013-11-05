package jhc.figaro.api.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AddressServiceResolverImplTest {

	@Mock
	private AddressService service;
	@Mock
	private VersionService versionService;

	@Test
	public void testGetAddressService() {
		AddressServiceResolverImpl resolver = new AddressServiceResolverImpl();
		resolver.setAddressServices(new HashMap<String, AddressService>());
		assertThat(resolver.getAddressService("dummy"), nullValue());
		// set services
		Map<String, AddressService> addressServices = new HashMap<String, AddressService>();
		addressServices.put("v1.addressService", service);
		resolver.setAddressServices(addressServices);
		assertThat(resolver.getAddressService("v1"), equalTo(service));
	}

	@Test
	public void testSetAddressServices() {
		AddressServiceResolverImpl resolver = new AddressServiceResolverImpl();
		assertThat(resolver.getAddressServices(), nullValue());

		Map<String, AddressService> addressServices = new HashMap<String, AddressService>();
		addressServices.put("v1.addressService", service);
		resolver.setAddressServices(addressServices);
		assertThat(resolver.getAddressServices().size(), equalTo(1));
		assertThat(resolver.getAddressServices(), equalTo(addressServices));
	}
	
	@Test
	public void testSetVersionService() {
		AddressServiceResolverImpl resolver = new AddressServiceResolverImpl();
		assertThat(resolver.getVersionService(), nullValue());
		resolver.setVersionService(versionService);
		assertThat(resolver.getVersionService(), equalTo(versionService));		
	}
}
