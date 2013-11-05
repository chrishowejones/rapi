package jhc.figaro.api.model;

import java.util.List;

import jhc.figaro.api.resource.framework.AtomLink;

/**
 * Representation of a collection of Person resources. Has the concept of pages of Person data.
 * 
 * @param <T>
 *            Type of the Person class that this class wraps.
 * 
 * @author Chris Howe-Jones
 * 
 */
public interface People<T extends Person> extends Cloneable, Page<T> {

    /**
     * Gets the Person instances contained by this People instance.
     * 
     * @return list of Person instances.
     */
    List<? extends T> getPersonList();

    /**
     * 
     * Sets a list of Person instances contained by this People instance.
     * 
     * @param personList
     *            - list of Person instances.
     */
    void setPersonList(List<T> personList);

    /**
     * Return a list of Hypermedia links to describe the actions related to this People resource.
     * 
     * @return list of links
     */
    List<AtomLink> getLinks();

    /**
     * Set a list of Hypermedia links to describe the actions related to this People resource.
     * 
     * @param links
     *            - list of links for the People.
     */
    void setLinks(List<AtomLink> links);

    /**
     * Clone this People object.
     * 
     * @return cloned People.
     * 
     */
    Object clone();

}