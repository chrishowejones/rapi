package jhc.figaro.api.service;

import jhc.figaro.api.model.Page;
import jhc.figaro.api.model.People;
import jhc.figaro.api.model.Person;
import jhc.figaro.api.persistence.domain.PersonDetails;
import jhc.figaro.data.jpa.CommandService;

import org.springframework.data.domain.Pageable;

/**
 * Person service responsible for carrying out queries and persistence commands on a Person resource. This service
 * delegates responsibility to the persistence layer to carry out most of the work.
 * 
 * @author Chris Howe-Jones
 * 
 */
public interface PersonService {

    /**
     * Get a Person by its identifier.
     * 
     * @param personCode
     *            - identifier for the person.
     * @return the Person that matches this identifier or null if not found.
     */
    Person getPersonByCode(String personCode);

    /**
     * Get initial People based on the search term.
     * 
     * @param searchTerm
     *            - search term to match to retrieve People
     * @return People that match the search term.
     */
    Page<? extends Person> getPersonBySearch(String searchTerm);

    /**
     * Get initial People based on the search term.
     * 
     * @param searchTerm
     *            - search term to match to retrieve People.
     * @param page
     *            - number of page required.
     * @return People that match the search term.
     */
    People<? extends Person> getPersonBySearch(String searchTerm, int page);

    /**
     * Get page of People based on the search term and the pageRequest.
     * 
     * @param searchTerm
     *            - search term to match to retrieve People
     * @param pageable
     *            - page request specifying page, pagesize and sort criteria.
     * @return People that match the search term.
     */
    Page<? extends Person> getPersonBySearch(String searchTerm, Pageable pageable);

    /**
     * Get initial page of all People.
     * 
     * @return page of people.
     */
    People<? extends Person> getPeople();

    /**
     * Get initial page of all People.
     * 
     * @param page
     *            - number of page required.
     * 
     * @return page of people.
     */
    People<? extends Person> getPeople(int page);

    /**
     * Get People based on pageable.
     * 
     * @param pageable
     *            - info required to create a Page of Person instances.
     * @return people instance containing a page of Person instances.
     */
    Page<? extends Person> getPeople(Pageable pageable);

    /**
     * Update a Person.
     * 
     * @param person
     *            - person to be update.
     * @return updated person.
     */
    Person update(Person person);

    /**
     * Persist a new Person.
     * 
     * @param person
     *            - new person to be persisted.
     * @return newly persisted person.
     */
    Person newPerson(Person person);

    /**
     * Indicate if person is 'dirty' i.e. the person does not match the persistent view of the same person resource.
     * 
     * @param person
     *            - person to be checked
     * @return true if person has been modified, false otherwise.
     */
    boolean isModified(Person person);

    /**
     * Sets a CommandService that is delegated to handle commands (persistence) of PersonDetails.
     * 
     * @param service
     *            - CommandService for PersonDetails, may be null and if so Jpa carries out persistence.
     */
    void setCommandService(CommandService<PersonDetails> service);

    /**
     * Gets a CommandService that is delegated to handle commands (persistence) of PersonDetails.
     * 
     * @return CommandService for PersonDetails, may be null
     */
    CommandService<PersonDetails> getCommandService();

    /**
     * Gets the page size to be used for collections of People.
     * 
     * @return maximum number of rows in a page.
     */
    int getPageSize();

    /**
     * Sets the page size to be used for collections of People.
     * 
     * @param pageSize
     *            - maximum number of rows in a page.
     * 
     */
    void setPageSize(int pageSize);

}
