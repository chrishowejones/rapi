package jhc.figaro.api.service;

/**
 * Responsible for providing the appropriate address service based on version.
 * 
 * @author Chris Howe-Jones
 * 
 */
public interface AddressServiceResolver {

	/**
	 * Get appropriate address service for the specified version.
	 * 
	 * @param version
	 *            - specified version - null or "current' will return the latest
	 *            version.
	 * @return address service.
	 */
	AddressService getAddressService(String version);

}
