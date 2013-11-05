package jhc.figaro.api.model.impl.v1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import jhc.figaro.api.model.Addresses;
import jhc.figaro.api.model.PageImpl;
import jhc.figaro.api.resource.framework.AtomLink;

/**
 * Version 1 of Addresses resource container.
 * 
 * @author Chris Howe-Jones
 * 
 */
@XmlRootElement(name = "addresses")
@XmlAccessorType(XmlAccessType.NONE)
public class AddressesV1 extends PageImpl<AddressV1> implements Addresses<AddressV1> {

	@XmlElementRef(name = "address")
	private List<AddressV1> addressList;
	@XmlElement(name = "links")
	private List<AtomLink> links;

	@Override
	public final Iterator<AddressV1> iterator() {
		return addressList.iterator();
	}

	@Override
	public final List<AddressV1> getAddressList() {
		return addressList;
	}

	@Override
	public final void setAddressList(final List<AddressV1> addressList) {
		this.addressList = addressList;
	}

	@Override
	public final List<AtomLink> getLinks() {
		return links;
	}

	@Override
	public final void setLinks(final List<AtomLink> links) {
		this.links = links;
	}
	
	@Override
	public final AddressesV1 clone() {
		List<AddressV1> clonedList = new ArrayList<AddressV1>();
        for (AddressV1 address : getAddressList()) {
            clonedList.add((AddressV1) address.clone());
        }
        AddressesV1 clone = new AddressesV1();
        clone.setAddressList(clonedList);
        clone.setNumberOfElements(getNumberOfElements());
        clone.setPageNumber(getPageNumber());
        clone.setPageSize(getPageSize());
        clone.setTotalElements(getTotalElements());
        List<AtomLink> links = getLinks();
        clone.setLinks(links);
        return clone;
	}

}
