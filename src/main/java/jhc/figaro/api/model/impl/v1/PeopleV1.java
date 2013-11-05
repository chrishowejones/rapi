package jhc.figaro.api.model.impl.v1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import jhc.figaro.api.model.PageImpl;
import jhc.figaro.api.model.People;
import jhc.figaro.api.resource.framework.AtomLink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation of People interface for version 1 of the API.
 * 
 * @author Chris Howe-Jones
 * 
 */
@XmlRootElement(name = "people")
public class PeopleV1 extends PageImpl<PersonV1> implements People<PersonV1> {
	
	private static final Logger LOG = LoggerFactory.getLogger(PeopleV1.class);

    private List<PersonV1> listOfPeople = new ArrayList<PersonV1>();

    private List<AtomLink> links;

    /**
     * Default no argument constructor for PeopleV1 required for JAXB marshaling.
     */
    public PeopleV1() {
        if (LOG.isTraceEnabled()) {
            LOG.trace("PeopleV1()");
        }
    }

    /**
     * Instantiate a PeopleV1 instance for a given PersonList.
     * 
     * @param personList
     *            - list of Person instances.
     */
    public PeopleV1(final List<PersonV1> personList) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("PeopleV1(" + personList + ")");
        }
        setPersonList(personList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jhc.figaro.api.model.impl.v1.People#getPersonList()
     */
    @Override
    @XmlElementRef(name = "person")
    public final List<PersonV1> getPersonList() {
        return listOfPeople;
    }

    /**
     * 
     * Sets a list of Person instances contained by this People instance.
     * 
     * @param personList
     *            - list of Person instances.
     */
    @Override
    public final void setPersonList(final List<PersonV1> personList) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("setPersonList(" + personList + ")");
        }
        this.listOfPeople = personList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jhc.figaro.api.model.People#getLinks()
     */
    @Override
    public final List<AtomLink> getLinks() {
        return links;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jhc.figaro.api.model.People#setLinks(java.util.List)
     */
    @Override
    public final void setLinks(final List<AtomLink> links) {
        this.links = new ArrayList<AtomLink>(links);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public final Object clone() {
        List<PersonV1> clonedList = new ArrayList<PersonV1>();
        for (PersonV1 person : getPersonList()) {
            clonedList.add((PersonV1) person.clone());
        }
        PeopleV1 clone = new PeopleV1(clonedList);
        clone.setNumberOfElements(getNumberOfElements());
        clone.setPageNumber(getPageNumber());
        clone.setPageSize(getPageSize());
        clone.setTotalElements(getTotalElements());
        return clone;
    }

	@Override
	public Iterator<PersonV1> iterator() {
		return listOfPeople.iterator();
	}

}
