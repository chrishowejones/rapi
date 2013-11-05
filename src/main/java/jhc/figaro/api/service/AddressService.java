package jhc.figaro.api.service;

import jhc.figaro.api.model.Address;
import jhc.figaro.api.model.Addresses;

/**
 * Address service responsible for carrying out queries and persistence commands
 * on Address resources. This service delegates responsibility to the
 * persistence layer to carry out most of the work.
 * 
 * @author Chris Howe-Jones
 * 
 */
public interface AddressService {

	/**
	 * Find the addresses that are owned by the person identified by the Person
	 * code.
	 * 
	 * @param personCode
	 *            - identifier of the Person owning the required addresses.
	 * @param page
	 *            - page number for page of addresses required.
	 * @return list of Address instances.
	 */
	Addresses<? extends Address> getAddressesByPerson(String personCode, int page);

	/**
	 * Get a count of addresses that are owned by a person whose person code is
	 * as supplied.
	 * 
	 * @param personCode
	 *            - owning person code to match.
	 * @return count of Addresses owned by specified person.
	 */
	long getAddressCountByPerson(String personCode);

	/**
	 * Gets an Address by it's id.
	 * 
	 * @param id
	 *            - identifier of the address.
	 * @return address that matches the id.
	 */
	Address getAddressById(Long id);
}
