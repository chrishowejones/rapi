package jhc.figaro.api.model.impl.v1;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import jhc.figaro.api.model.Person;
import jhc.figaro.api.resource.framework.AtomLink;
import jhc.figaro.api.resources.PeopleResourceController;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.server.linking.Binding;
import com.sun.jersey.server.linking.Link;
import com.sun.jersey.server.linking.Links;
import com.sun.jersey.server.linking.Ref;
import com.sun.jersey.server.linking.Ref.Style;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "person")
@Links({ @Link(value = @Ref(resource = PeopleResourceController.class, method = "getPersonByCode", style = Style.ABSOLUTE, bindings = @Binding(name = "code", value = "${instance.code}")), rel = "self") })
public class PersonV1 implements Person {

	private static final Logger LOG = LoggerFactory.getLogger(PersonV1.class);

	@XmlElement
	private String code;
	@XmlElement
	private String name;
	@XmlElement
	private String email;

	@XmlElement
	private List<AtomLink> links = new ArrayList<AtomLink>();

	@XmlElement(nillable = true)
	@JsonIgnore
	private List<AddressV1> addresses;

	private boolean hasAddresses;

	/**
	 * Default no arg constructor. Required for JAXB and cos this class is
	 * following a Java bean pattern.
	 */
	public PersonV1() {
		if (LOG.isTraceEnabled()) {
			LOG.trace("Created PersonV1");
		}
	}

	@Override
	public final String getCode() {
		return code;
	}

	@Override
	public final void setCode(final String personCode) {
		this.code = personCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jhc.figaro.api.resource.framework.Identifiable#setId(java.io.Serializable
	 * )
	 */
	@Override
	public final void setId(final String id) {
		setCode(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jhc.figaro.api.resource.framework.Identifiable#getId()
	 */
	@Override
	@XmlTransient
	public final String getId() {
		return getCode();
	}

	/**
	 * Get the name of the Person.
	 * 
	 * @return persons name.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Set the name of the Person.
	 * 
	 * @param personName
	 *            of the Person.
	 */
	public final void setName(final String personName) {
		this.name = personName;
	}

	/**
	 * Get the email address of the Person.
	 * 
	 * @return email address.
	 */
	public final String getEmail() {
		return email;
	}

	/**
	 * Set the email address of the Person.
	 * 
	 * @param email
	 *            - email address of the Person.
	 */
	public final void setEmail(final String emailAddress) {
		this.email = emailAddress;
	}

	@Override
	public final List<AtomLink> getLinks() {
		return links;
	}

	@Override
	public final void setLinks(final List<AtomLink> link) {
		this.links = link;
	}

	@Override
	public final int hashCode() {
		int result = System.identityHashCode(this);
		if (code != null) {
			final int prime = 31;
			result = 1;
			result = prime * result + code.hashCode();
		}
		return result;
	}

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
		PersonV1 other = (PersonV1) obj;
		if (this.hashCode() != other.hashCode()) {
			return false;
		}
		return true;
	}

	@Override
	public final String toString() {
		return "PersonV1 [code=" + code + ", name=" + name + ", email=" + email + "]";
	}

	@Override
	public final boolean deepEquals(final Person person) {
		if (person != null && person.getClass().equals(this.getClass())) {
			PersonV1 other = (PersonV1) person;
			// check all fields with public getters
			if ((code == null && other.getCode() != null) || code != null && !code.equals(other.getCode())) {
				return false;
			}
			if ((name == null && other.getName() != null) || name != null && !name.equals(other.getName())) {
				return false;
			}
			if ((email == null && other.getEmail() != null) || email != null && !email.equals(other.getEmail())) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Person#clone()
	 */
	@Override
	public final Object clone() {
		PersonV1 clone = new PersonV1();
		clone.setCode(this.getCode());
		clone.setEmail(this.getEmail());
		clone.setName(this.getName());
		List<AtomLink> clonedLinks = new ArrayList<AtomLink>(getLinks().size());
		for (AtomLink link : getLinks()) {
			AtomLink clonedLink = (AtomLink) link.clone();
			clonedLinks.add(clonedLink);
		}
		clone.setLinks(clonedLinks);
		return clone;
	}

	/**
	 * Get addresses for this Person.
	 * 
	 * @return address list.
	 */
	public final List<AddressV1> getAddresses() {
		return addresses;
	}

	/**
	 * Set addresses for this Person.
	 * 
	 * @param addresses
	 *            - list of addresses.
	 */
	public final void setAddresses(final List<AddressV1> addresses) {
		this.addresses = addresses;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jhc.figaro.api.model.Person#hasAddresses()
	 */
	@Override
	public final boolean hasAddresses() {
		return hasAddresses;
	}

	/**
	 * Set indicator to show if Person has addresses.
	 * 
	 * @param hasAddresses
	 *            - true if addresses exist, false otherwise.
	 */
	public final void setHasAddresses(final boolean hasAddresses) {
		this.hasAddresses = hasAddresses;
	}

}
