package jhc.figaro.api.model;

import java.io.Serializable;

import jhc.figaro.api.resource.framework.Identifiable;
import jhc.figaro.api.resource.framework.Linkable;

/**
 * Address resource.
 * 
 * @author Chris Howe-Jones
 * 
 */
public interface Address extends Identifiable<Long>, Linkable, Cloneable {

	/**
	 * Returns a clone of this address.
	 * 
	 * @return cloned address.
	 */
	Object clone();

	/**
	 * Retrieve the parent id of this Address.
	 * 
	 * @return parent id of the Address.
	 */
	Serializable getParentId();

}
