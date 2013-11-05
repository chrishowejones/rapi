package jhc.figaro.api.model.impl.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import jhc.figaro.api.model.Address;
import jhc.figaro.api.resource.framework.AtomLink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Address resource for version 1 of the API.
 * 
 * @author chris howe-jones
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "address")
public class AddressV1 implements Address {

	private static final Logger LOG = LoggerFactory.getLogger(AddressV1.class);

	@XmlElement
	private long id;

	@XmlElement
	private List<AtomLink> links;

	@XmlElement
	private String addressType;

	@XmlElement
	private String line1;

	@XmlElement
	private String line2;

	@XmlElement
	private String city;

	@XmlElement
	private String postcode;

	/*
	 * Person code of parent stored for mapping to parent link
	 */
	private String personCode;

	@Override
	public final void setId(final Long id) {
		this.id = id;

	}

	@Override
	public final Long getId() {
		return new Long(id);
	}

	@Override
	public final List<AtomLink> getLinks() {
		if (links != null) {
			return new ArrayList<AtomLink>(links);
		} else {
			return null;
		}
	}

	@Override
	public final void setLinks(final List<AtomLink> links) {
		this.links = new ArrayList<AtomLink>(links);
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
	 * 
	 * @return parent person code.
	 */
	public final String getPersonCode() {
		return personCode;
	}

	/**
	 * 
	 * @param personCode
	 *            - parent person code.
	 */
	public void setPersonCode(String personCode) {
		this.personCode = personCode;
	}

	@Override
	public Serializable getParentId() {
		return getPersonCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final AddressV1 clone() {
		AddressV1 clone = null;
		try {
			clone = (AddressV1) super.clone();
		} catch (CloneNotSupportedException e) {
			LOG.error("Clone not implemented.", e);
		}
		return clone;
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
		result = prime * result + (int) (id ^ (id >>> 32));
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
		AddressV1 other = (AddressV1) obj;
		if (id == 0 && other.id == 0) {
			return super.equals(other);
		}
		if (id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "AddressV1 [id=" + id + "]";
	}

}
