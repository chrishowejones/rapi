package jhc.figaro.api.resource.framework;

import java.util.List;

/**
 * A resource that has links.
 * 
 * @author Chris Howe-Jones
 * 
 */
public interface Linkable {

    /**
     * Return a list of Hypermedia links to describe the actions related to this Person resource.
     * 
     * @return list of links
     */
    List<AtomLink> getLinks();

    /**
     * Set a list of Hypermedia links to describe the actions related to this Person resource.
     * 
     * @param links
     *            - list of links for the Person.
     */
    void setLinks(List<AtomLink> links);

}