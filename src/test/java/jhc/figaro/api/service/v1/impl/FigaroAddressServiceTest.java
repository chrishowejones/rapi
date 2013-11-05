package jhc.figaro.api.service.v1.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import jhc.figaro.api.RapiMapper;
import jhc.figaro.api.model.impl.v1.AddressV1;
import jhc.figaro.api.model.impl.v1.AddressesV1;
import jhc.figaro.api.persistence.AddressRepository;
import jhc.figaro.api.persistence.domain.Address;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

@RunWith(MockitoJUnitRunner.class)
public class FigaroAddressServiceTest {

	@Mock
	private AddressRepository addressRepo;
	@Mock
	private RapiMapper mapper;

	@Test
	public void testGetAddressByPerson() {
		String personCode = "code";
		int pageSize = 10;
		int pageNumber = 0;
		long total = 1;
		// set up mocks
		Address address = Mockito.mock(Address.class);
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);
		// mock resulting resource addresses
		List<AddressV1> resourceAddresses = new ArrayList<AddressV1>();
		AddressV1 addressV1 = new AddressV1();
		resourceAddresses.add(addressV1);

		// set up page
		Pageable pageRequest = new PageRequest(0, pageSize, Direction.ASC, "id");
		Page<Address> addressesPage = new PageImpl<Address>(addresses, pageRequest, total);

		// set mocks to return as required
		when(addressRepo.findByPersonPersonCode(personCode, pageRequest)).thenReturn(addressesPage);
		when(mapper.mapAsList(addresses, AddressV1.class)).thenReturn(resourceAddresses);

		// get model address
		FigaroAddressService service = new FigaroAddressService();
		service.setAddressRepository(addressRepo);
		service.setRapiMapper(mapper);
		service.setPageSize(pageSize);
		AddressesV1 addressesReturned = (AddressesV1) service.getAddressesByPerson(personCode, 0);
		assertThat(addressesReturned, is(notNullValue()));
		assertThat(addressesReturned.getAddressList(), hasSize(1));
		assertThat(addressesReturned.getAddressList().get(0), is(equalTo(addressV1)));
		assertThat(addressesReturned.getNumberOfElements(), equalTo(1));
		assertThat(addressesReturned.getPageNumber(), equalTo(pageNumber));
		assertThat(addressesReturned.getPageSize(), equalTo(pageSize));
		assertThat(addressesReturned.getTotalElements(), equalTo(1l));
		assertThat(addressesReturned.getTotalPages(), equalTo(1));
	}

	@Test
	public void testGetAddressCountByPerson() {
		String personCode = "code";
		long count = 10;
		// set up mocks
		when(addressRepo.countByPersonPersonCode(personCode)).thenReturn(count);

		// get model address
		FigaroAddressService service = new FigaroAddressService();
		service.setAddressRepository(addressRepo);
		long countReturned = service.getAddressCountByPerson(personCode);
		assertThat(countReturned, is(equalTo(count)));
	}

	@Test
	public void testSetPageSize() {
		int pageSize = 100;
		FigaroAddressService service = new FigaroAddressService();
		service.setPageSize(pageSize);
		assertThat(service.getPageSize(), equalTo(pageSize));
	}

	@Test
	public void testSetMapper() {
		RapiMapper mapper = new RapiMapper();
		FigaroAddressService service = new FigaroAddressService();
		service.setRapiMapper(mapper);
		assertThat(service.getRapiMapper(), equalTo(mapper));
	}

	@Test
	public void testGetAddressById() {
		// set up mocks
		Address address = Mockito.mock(Address.class);
		long id = 10l;
		AddressV1 addressV1 = new AddressV1();
		addressV1.setId(id);
		// set mocks to return as required
		when(addressRepo.findOne(id)).thenReturn(address);
		when(mapper.map(address, AddressV1.class)).thenReturn(addressV1);

		// test get address by id.
		FigaroAddressService service = new FigaroAddressService();
		service.setAddressRepository(addressRepo);
		service.setRapiMapper(mapper);
		AddressV1 addressReturned = (AddressV1) service.getAddressById(id);

		assertThat(addressReturned, notNullValue());
		assertThat(addressReturned.getId(), equalTo(id));
	}

}
