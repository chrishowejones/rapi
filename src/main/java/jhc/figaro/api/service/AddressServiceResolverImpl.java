package jhc.figaro.api.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Responsible for getting the relevant address service for the specified
 * version.
 * 
 * 
 * @author Chris Howe-Jones
 * 
 */
@Component
public class AddressServiceResolverImpl implements AddressServiceResolver {

	private static final Logger LOG = LoggerFactory.getLogger(AddressServiceResolverImpl.class);

	private Map<String, AddressService> addressServices;

	private VersionService versionService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jhc.figaro.api.service.AddressServiceResolver#getAddressService(java.
	 * lang.String)
	 */
	@Override
	public final AddressService getAddressService(final String version) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("getAddressService(" + version + ")");
		}
		StringBuffer versionKey = new StringBuffer();
		if (version == null || version.equalsIgnoreCase("current")) {
			versionKey.append(versionService.getCurrentVersion());
		} else {
			versionKey.append(version.toLowerCase());
		}
		versionKey.append(".addressService");
		return addressServices.get(versionKey.toString());
	}

	/**
	 * Get all available address services keyed by bean name.
	 * 
	 * @return address services.
	 */
	public final Map<String, AddressService> getAddressServices() {
		return addressServices;
	}

	/**
	 * Set all available address services.
	 * 
	 * @param services
	 *            - address services keyed by name.
	 */
	@Autowired
	public final void setAddressServices(final Map<String, AddressService> services) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("setAddressServices(" + services + ")");
		}
		this.addressServices = services;
	}

	/**
	 * Get version service.
	 * 
	 * @return version service.
	 */
	public final VersionService getVersionService() {
		return versionService;
	}

	/**
	 * Set version service.
	 * 
	 * @param versionService
	 *            - version service.
	 */
	public final void setVersionService(final VersionService versionService) {
		this.versionService = versionService;
	}

}
