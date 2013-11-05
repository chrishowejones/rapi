package jhc.figaro.api.resource.framework;

import java.io.Serializable;

/**
 * Indicates an identifiable Resource.
 * 
 * @param <ID>
 *            the type of the serializable id.
 * 
 * @author Chris Howe-Jones
 * 
 */
public interface Identifiable<ID extends Serializable> {

    /**
     * Set the identifier on a resource.
     * 
     * @param id
     *            - the identifier.
     */
    void setId(ID id);

    /**
     * Get the identifier on a resource.
     * 
     * @return identifier of a resource.
     */
    ID getId();

}
