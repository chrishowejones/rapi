package jhc.figaro.api.persistence.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * Persistent entity class representing an Address.
 * 
 * @author Chris Howe-Jones
 * 
 */
@Entity
@Table(name = "ADDRESS")
public class Address implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7570267642861266991L;

	@Id
	private long id;

	private String addressType;

	private String line1;

	private String line2;

	private String city;

	private String postcode;
	
	@ManyToOne
	@JoinColumn(name = "PERSON_CODE")
	private PersonDetails person;

	/**
	 * 
	 * @return identifier.
	 */
	public final long getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            - identifier of Address.
	 */
	public final void setId(final long id) {
		this.id = id;
	}

	/**
	 * 
	 * @return address type.
	 */
	public final String getAddressType() {
		return addressType;
	}

	/**
	 * 
	 * @param addressType
	 *            - type of Address.
	 */
	public final void setAddressType(final String addressType) {
		this.addressType = addressType;
	}

	/**
	 * Get first line of address.
	 * 
	 * @return first address line.
	 */
	public final String getLine1() {
		return line1;
	}

	/**
	 * Set first line of Address.
	 * 
	 * @param addressLine1
	 *            - first line of Address.
	 */
	public final void setLine1(final String addressLine1) {
		line1 = addressLine1;
	}

	/**
	 * Get second address line.
	 * 
	 * @return second line of address.
	 */
	public final String getLine2() {
		return line2;
	}

	/**
	 * Set second address line.
	 * 
	 * @param addressLine2
	 *            - second line of address.
	 */
	public final void setLine2(final String addressLine2) {
		line2 = addressLine2;
	}

	/**
	 * Get city.
	 * 
	 * @return city.
	 */
	public final String getCity() {
		return city;
	}

	/**
	 * Set city of address.
	 * 
	 * @param city
	 *            - city to set.
	 */
	public final void setCity(final String city) {
		this.city = city;
	}

	/**
	 * Get postcode of address.
	 * 
	 * @return postcode.
	 */
	public final String getPostcode() {
		return postcode;
	}

	/**
	 * Set postcode.
	 * 
	 * @param postcode
	 *            - postcode to set.
	 */
	public final void setPostcode(final String postcode) {
		this.postcode = postcode;
	}
	
	/**
	 * Get owning Person.
	 * 
	 * @return person details
	 */
	public final PersonDetails getPerson() {
		return person;
	}

	/**
	 * Set Person.
	 * 
	 * @param person - person details.
	 */
	public final void setPerson(final PersonDetails person) {
		this.person = person;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		if (id != 0) {
			result = prime * result + (int) (id ^ (id >>> prime + 1));
		} else {
			result = super.hashCode();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Address other = (Address) obj;
		if (id == 0 && other.id == 0) {
			return super.equals(other);
		}
		if (id != other.id) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return "Address [id=" + id + "]";
	}

}
