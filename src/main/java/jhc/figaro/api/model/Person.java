package jhc.figaro.api.model;

import jhc.figaro.api.resource.framework.Identifiable;
import jhc.figaro.api.resource.framework.Linkable;

/**
 * Person resource.
 * 
 * @author Chris Howe-Jones
 * 
 */
public interface Person extends Cloneable, Linkable, Identifiable<String> {

	/**
	 * Get the identifier for the Person resource.
	 * 
	 * @return identifying code for the Person.
	 */
	String getCode();

	/**
	 * Set the identifier for the Person resource.
	 * 
	 * @param code
	 *            - person code identifying person.
	 */
	void setCode(String code);

	/**
	 * Indicates if the person argument passed is equal to this instance of
	 * Person by checking all the properties of the Person not just the
	 * identifier.
	 * 
	 * @param personToCheck
	 *            - person to check against.
	 * @return true if personToCheck's properties are equal to this Person's
	 *         properties.
	 */
	boolean deepEquals(Person personToCheck);

	/**
	 * Return a clone of the Person.
	 * 
	 * @return clone of Person.
	 */
	Object clone();

	/**
	 * Indicates if this person resource has addresses.
	 * 
	 * @return true if addresses present for this Person, false otherwise.
	 */
	boolean hasAddresses();
}
