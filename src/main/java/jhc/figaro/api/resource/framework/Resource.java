package jhc.figaro.api.resource.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.util.Assert;

/**
 * A simple {@link Resource} wrapping a domain object and adding links to it.
 * 
 * @author Chris Howe-Jones
 */
public abstract class Resource {

    @XmlElement(name = "link", namespace = AtomLink.ATOM_NAMESPACE, required = false)
    @JsonProperty("links")
    private final List<AtomLink> links;

    /**
     * Constructor to be invoked by sub class.
     */
    public Resource() {
        this.links = new ArrayList<AtomLink>();
    }

    /**
     * Adds the given link to the resource.
     * 
     * @param link
     *            - link for a resource.
     */
    public final void add(final AtomLink link) {
        Assert.notNull(link, "Link must not be null!");
        Assert.isTrue(!hasLink(link.getRel()), "Link already added.");
        this.links.add(link);
    }

    /**
     * Adds all given {@link AtomLink}s to the resource.
     * 
     * @param linksValue
     *            - collection of links.
     */
    public final void add(final Iterable<AtomLink> linksValue) {
        Assert.notNull(linksValue, "Given links must not be null!");
        for (AtomLink candidate : linksValue) {
            add(candidate);
        }
    }

    /**
     * Returns whether the resource contains {@link AtomLink}s at all.
     * 
     * @return true if links
     */
    public final boolean hasLinks() {
        return !this.links.isEmpty();
    }

    /**
     * Returns whether the resource contains a {@link AtomLink} with the given rel.
     * 
     * @param rel
     *            - relationship to search for.
     * @return true if link for matching rel contained in resource, false otherwise.
     */
    public final boolean hasLink(final String rel) {
        return getLink(rel) != null;
    }

    /**
     * Returns all {@link AtomLink}s contained in this resource.
     * 
     * @return all links for this resource.
     */
    public final List<AtomLink> getLinks() {
        return Collections.unmodifiableList(links);
    }

    /**
     * Returns the link with the given rel.
     * 
     * @param rel
     *            - relationship to search for.
     * @return the link with the given rel or {@literal null} if none found.
     */
    public final AtomLink getLink(final String rel) {

        for (AtomLink link : links) {
            if (link.getRel().equals(rel)) {
                return link;
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return String.format("links: %s", links.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }

        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }

        Resource that = (Resource) obj;

        return this.links.equals(that.links);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        return this.links.hashCode();
    }
}
