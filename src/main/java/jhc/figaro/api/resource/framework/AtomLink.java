package jhc.figaro.api.resource.framework;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents an Atom link and can be extended to add application specific
 * information as required.
 * 
 * @author Chris Howe-Jones
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "link", namespace = AtomLink.ATOM_NAMESPACE)
public class AtomLink implements Cloneable {

	/**
	 * Atom namespace - See {@link http://www.w3.org/2005/Atom}.
	 */
	public static final String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";

	/**
	 * Default media type.
	 */
	public static final String DEFAULT_MEDIA_TYPE = "application/json";

	@XmlElement
	private String rel;
	@XmlElement
	private String href;
	@XmlElement
	private String type;

	/**
	 * Creates a link entity.
	 */
	public AtomLink() {
	}

	/**
	 * Creates a link entity for the specified rel, href and type.
	 * 
	 * @param relValue
	 *            - the relationship of the link.
	 * @param hrefValue
	 *            - href of the URI for the linked resource.
	 * @param typeValue
	 *            - the media type of the linked resource.
	 */
	public AtomLink(final String relValue, final String hrefValue,
			final String typeValue) {
		rel = relValue;
		href = hrefValue;
		type = typeValue;
	}

	/**
	 * Create a link entity for the specified rel and href.
	 * 
	 * @param relType
	 *            - the relationship of the link.
	 * @param hrefValue
	 *            - href of the URI for the linked resource.
	 */
	public AtomLink(final String relType, final String hrefValue) {
		this(relType, hrefValue, DEFAULT_MEDIA_TYPE);
	}

	/**
	 * Creates a link entity for the specified rel, href and type.
	 * 
	 * @param relValue
	 *            - the relationship of the link.
	 * @param hrefValue
	 *            - href of the URI for the linked resource.
	 * @param typeValue
	 *            - the media type of the linked resource.
	 */
	public AtomLink(final AtomRelationship relValue, final String hrefValue,
			final String typeValue) {
		this(relValue.toString(), hrefValue, typeValue);
	}

	/**
	 * Create a link entity for the specified rel and href.
	 * 
	 * @param relType
	 *            - the relationship of the link.
	 * @param hrefValue
	 *            - href of the URI for the linked resource.
	 */
	public AtomLink(final AtomRelationship relType, final String hrefValue) {
		this(relType.toString(), hrefValue);
	}

	/**
	 * Gets the relationship of the link.
	 * 
	 * @return relationship.
	 */
	public final String getRel() {
		return rel;
	}

	/**
	 * Sets the relationship of the link.
	 * 
	 * @param relValue
	 *            - link relationship.
	 */
	public final void setRel(final String relValue) {
		rel = relValue;
	}

	/**
	 * Gets the href for link.
	 * 
	 * @return href to the linked resource.
	 */
	public final String getHref() {
		return href;
	}

	/**
	 * Sets the href for the link.
	 * 
	 * @param hrefValue
	 *            - to the linked resource.
	 */
	public final void setHref(final String hrefValue) {
		href = hrefValue;
	}

	/**
	 * Get the media type for the linked resource.
	 * 
	 * @return type of media.
	 */
	public final String getType() {
		return type;
	}

	/**
	 * Set the media type for the linked resource.
	 * 
	 * @param typeValue
	 *            - media type for the linked resource.
	 */
	public final void setType(final String typeValue) {
		type = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final Object clone() {
		AtomLink clone = new AtomLink(this.rel, this.href, this.type);
		return clone;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		int hrefHash = 0;
		if (href != null) {
			hrefHash = href.hashCode();
		}
		result = prime * result + hrefHash;
		int relHash = 0;
		if (rel != null) {
			rel.hashCode();
		}
		result = prime * result + relHash;
		int typeHash = 0;
		if (type != null) {
			type.hashCode();
		}
		result = prime * result + typeHash;
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
		AtomLink other = (AtomLink) obj;
		if (href == null) {
			if (other.href != null) {
				return false;
			}
		} else if (!href.equals(other.href)) {
			return false;
		}
		if (rel == null) {
			if (other.rel != null) {
				return false;
			}
		} else if (!rel.equals(other.rel)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		return true;
	}

}
