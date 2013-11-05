package jhc.figaro.api.resource.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.util.Assert;

/**
 * General helper to easily create a wrapper for a collection of resource entities.
 * 
 * @author Chris Howe-Jones
 */
@XmlRootElement(name = "entities")
public class Resources<T extends Resource> implements Iterable<T> {

    @XmlAnyElement
    @XmlElementWrapper
    @JsonProperty("content")
    private final Collection<T> content;

    /**
     * Creates a {@link Resources} instance with the given content and {@link Link}s (optional).
     * 
     * @param content
     *            must not be {@literal null}.
     * @param links
     *            the links to be added to the {@link Resources}.
     */
    public Resources(Iterable<T> content, AtomLink... links) {
        this(content, Arrays.asList(links));
    }

    /**
     * Creates a {@link Resources} instance with the given content and {@link Link}s.
     * 
     * @param content
     *            must not be {@literal null}.
     * @param links
     *            the links to be added to the {@link Resources}.
     */
    public Resources(Iterable<T> content, Iterable<AtomLink> links) {

        Assert.notNull(content);

        this.content = new ArrayList<T>();

        for (T element : content) {
            this.content.add(element);
        }
        this.add(links);
    }

    public void add(Iterable<AtomLink> links) {
        // TODO Auto-generated method stub

    }

    /**
     * Creates a new {@link Resources} instance by wrapping the given domain class instances into a {@link Resource}.
     * 
     * @param content
     *            must not be {@literal null}.
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Resource> Resources<T> wrap(Iterable<T> content) {

        Assert.notNull(content);
        ArrayList<T> resources = new ArrayList<T>();

        for (T element : content) {
            resources.add(element);
        }

        return new Resources<T>(resources);
    }

    /**
     * Returns the underlying elements.
     * 
     * @return the content will never be {@literal null}.
     */
    public Collection<T> getContent() {
        return Collections.unmodifiableCollection(content);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Resources { content: %s, %s }", getContent(), super.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }

        Resources<?> that = (Resources<?>) obj;

        boolean contentEqual = false;
        if (this.content == null && that.content == null) {
            contentEqual = true;
        } else if (this.content != null && this.content.equals(that.content)) {
            contentEqual = true;
        }
        return contentEqual;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result += 17 * content.hashCode();
        return result;
    }
}
