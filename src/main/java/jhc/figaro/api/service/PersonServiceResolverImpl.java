package jhc.figaro.api.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Person Service Resolver responsible for determining the Person Service that will carry out the processing of RESTful
 * requests.
 * 
 * @author Chris Howe-Jones
 * 
 */
@Service
public class PersonServiceResolverImpl implements PersonServiceResolver {

    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceResolverImpl.class);

    private Map<String, PersonService> personServices = new HashMap<String, PersonService>();
    private VersionService versionService;

    /*
     * (non-Javadoc)
     * 
     * @see jhc.figaro.api.service.PersonServiceResolver#getPersonServices()
     */
    @Override
    public final Map<String, PersonService> getPersonServices() {
        return personServices;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jhc.figaro.api.service.PersonServiceResolver#setPersonServices(java.util.Map)
     */
    @Override
    @Autowired
    public final void setPersonServices(final Map<String, PersonService> services) {
        this.personServices = services;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jhc.figaro.api.service.PersonServiceResolver#getPersonService(java.lang.String)
     */
    @Override
    public final PersonService getPersonService(final String version) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("getPersonService(" + version + ")");
        }
        StringBuffer versionKey = new StringBuffer();
        if (version == null || version.equalsIgnoreCase("current")) {
            versionKey.append(versionService.getCurrentVersion());
        } else {
            versionKey.append(version.toLowerCase());
        }
        versionKey.append(".personService");
        return personServices.get(versionKey.toString());
    }

    /**
     * Set the VersionService used to determine supported versions.
     * 
     * @param versionService
     *            - service to be set.
     */
    @Autowired
    public final void setVersionService(final VersionService versionService) {
        this.versionService = versionService;
    }

    /**
     * Get the VersionService.
     * 
     * @return version service.
     */
    public final VersionService getVersionService() {
        return versionService;
    }

}
