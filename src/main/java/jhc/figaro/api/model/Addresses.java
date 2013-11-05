package jhc.figaro.api.model;

import java.util.List;

import jhc.figaro.api.resource.framework.Linkable;

/**
 * Address wrapper collection.
 * 
 * @author Chris Howe-Jones
 * 
 * @param <T>
 *            - subtype of Address wrapped by Addresses.
 */
public interface Addresses<T extends Address> extends Page<T>, Linkable, Cloneable {

	/**
	 * Get the address list.
	 * 
	 * @return gets list of Addresses.
	 */
	List<T> getAddressList();

	/**
	 * Set address list.
	 * 
	 * @param addressList
	 *            - list of Addresses.
	 */
	void setAddressList(List<T> addressList);

	/**
	 * Clone the Addresses instance.
	 * 
	 * @return clone of this instance.
	 */
	Addresses<T> clone();

}
