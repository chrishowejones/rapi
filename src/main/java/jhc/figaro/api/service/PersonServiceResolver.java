package jhc.figaro.api.service;

import java.util.Map;

/**
 * Person Service Resolver responsible for determining the Person Service that will carry out the processing of RESTful
 * requests.
 * 
 * @author Chris Howe-Jones
 * 
 */
public interface PersonServiceResolver {

    /**
     * Returns a Map of the available PersonService instances keyed by there Spring bean id.
     * 
     * @return map of available PersonService instances.
     */
    Map<String, PersonService> getPersonServices();

    /**
     * Sets a Map of the available PersonService instances keyed by there Spring bean id.
     * 
     * @param services
     *            - map of available PersonService instances.
     */
    void setPersonServices(Map<String, PersonService> services);

    /**
     * Gets a Person Service based on the version passed in the argument. If the version argument is null then delegates
     * to the VersionService to look up current version.
     * 
     * @param version
     *            - of the API.
     * @return person service that provides version.
     */
    PersonService getPersonService(String version);

}