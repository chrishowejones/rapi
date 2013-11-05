package jhc.figaro.api.service.v1.impl;

import java.util.List;

import jhc.figaro.api.RapiMapper;
import jhc.figaro.api.model.Address;
import jhc.figaro.api.model.Addresses;
import jhc.figaro.api.model.impl.v1.AddressV1;
import jhc.figaro.api.model.impl.v1.AddressesV1;
import jhc.figaro.api.persistence.AddressRepository;
import jhc.figaro.api.service.AddressService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

/**
 * Address service responsible for carrying out queries and persistence commands
 * on Address resources for version 1 of the API. This service delegates
 * responsibility to the persistence layer to carry out most of the work.
 * 
 * @author Chris Howe-Jones
 * 
 */
@Component("v1.addressService")
public class FigaroAddressService implements AddressService {
	/**
	 * Default page size to use when not set specifically in Spring config.
	 */
	public static final int DEFAULT_PAGE_SIZE = 300;

	private static final Logger LOG = LoggerFactory.getLogger(FigaroAddressService.class);

	private AddressRepository addressRepository;
	private RapiMapper mapper;
	private int pageSize = DEFAULT_PAGE_SIZE;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jhc.figaro.api.service.AddressService#getAddressesByPerson(java.lang.
	 * String, int)
	 */
	@Override
	public final Addresses<AddressV1> getAddressesByPerson(final String personCode, final int page) {
		List<AddressV1> addressesResource = null;
		PageRequest pageRequest = new PageRequest(page, getPageSize(), Direction.ASC, "id");
		Page<jhc.figaro.api.persistence.domain.Address> addressPage = addressRepository.findByPersonPersonCode(
				personCode, pageRequest);
		int numberOfPageElements = 0;
		long totalElements = 0;
		if (addressPage != null && addressPage.hasContent()) {
			// map persistent addresses to resource addresses.
			addressesResource = mapper.mapAsList(addressPage.getContent(), AddressV1.class);
			numberOfPageElements = addressPage.getNumberOfElements();
			totalElements = addressPage.getTotalElements();
		}
		AddressesV1 addresses = new AddressesV1();
		addresses.setAddressList(addressesResource);
		addresses.setPageSize(getPageSize());
		addresses.setPageNumber(page);
		addresses.setNumberOfElements(addressPage.getNumberOfElements());
		addresses.setTotalElements(totalElements);
		return addresses;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jhc.figaro.api.service.v1.impl.AddressService#getAddressCountByPerson
	 * (java.lang.String)
	 */
	@Override
	public final long getAddressCountByPerson(final String personCode) {
		long addressCount = addressRepository.countByPersonPersonCode(personCode);
		return addressCount;
	}

	/**
	 * Get the page size.
	 * 
	 * @return page size.
	 */
	public final int getPageSize() {
		return pageSize;
	}

	/**
	 * Set Address Repository.
	 * 
	 * @param addressRepository
	 *            - address repository.
	 */
	@Autowired
	public final void setAddressRepository(final AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	/**
	 * Set mapper used to map from persistent to resource address.
	 * 
	 * @param mapper
	 *            - map from persistent to resource address.
	 */
	@Autowired
	public final void setRapiMapper(final RapiMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * Set page size.
	 * 
	 * @param pageSize
	 *            - size of page.
	 */
	@Autowired(required = false)
	@Qualifier("resource.pageSize")
	public final void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * Get used to map from persistent to resource address.
	 * 
	 * @return mapper.
	 */
	public final RapiMapper getRapiMapper() {
		return mapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jhc.figaro.api.service.AddressService#getAddressById(java.lang.Long)
	 */
	@Override
	public final Address getAddressById(final Long id) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("getAddressById(" + id + ")");
		}
		Address address = null;
		jhc.figaro.api.persistence.domain.Address persistentAddress = addressRepository.findOne(id);
		address = mapper.map(persistentAddress, AddressV1.class);
		return address;
	}

}
