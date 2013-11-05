package jhc.figaro.api.resource.framework;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Test;

public class ResourceTest extends AbstractMashallingTest {

	@Test
	public void testHashCode() {
		TestResource resource1 = new TestResource();
		TestResource resource2 = new TestResource();
		assertThat(resource1.hashCode(), equalTo(resource2.hashCode()));
		
		resource1.add(new AtomLink("self", "href"));
		assertThat(resource1.hashCode(), not(equalTo(resource2.hashCode())));
		resource2 = resource1;
		assertThat(resource1.hashCode(), equalTo(resource2.hashCode()));
	}

	
	@Test
	public void testResourceJsonSerialisation() throws Exception {
		TestResource resource = new TestResource();

		assertThat(writeJson(resource), equalTo("{\"links\":[]}"));
		// set values
		resource.setSurname("Bloggs");
		assertThat(writeJson(resource), equalTo("{\"links\":[],\"surname\":\"Bloggs\"}"));
		resource.setStuff("Fred");
		assertThat(writeJson(resource), equalTo("{\"links\":[],\"surname\":\"Bloggs\",\"firstName\":\"Fred\"}"));
	}

	@Test
	public void testResourceJaxbSerialisation() throws Exception {
		TestResource resource = new TestResource();

		assertThat(writeXml(resource).replaceAll("\\n", ""), equalTo(XML_HEADER + "<test xmlns:ns2=\""
				+ AtomLink.ATOM_NAMESPACE + "\"/>"));
		// set values
		resource.setSurname("Bloggs");
		assertThat(writeXml(resource).replaceAll("\\n", "").replaceAll("  ", ""), equalTo(XML_HEADER
				+ "<test xmlns:ns2=\"" + AtomLink.ATOM_NAMESPACE + "\"><surname>Bloggs</surname></test>"));
		resource.setStuff("Fred");
		assertThat(writeXml(resource).replaceAll("\\n", "").replaceAll("  ", ""), equalTo(XML_HEADER
				+ "<test xmlns:ns2=\"" + AtomLink.ATOM_NAMESPACE
				+ "\"><surname>Bloggs</surname><firstName>Fred</firstName></test>"));
	}

	@Test
	public void testAddLink() {
		TestResource resource = new TestResource();
		
		AtomLink link = new AtomLink();
		resource.add(link);
		assertThat(resource.getLinks().get(0), equalTo(link));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddLinkTwice() {
		TestResource resource = new TestResource();
		
		AtomLink link1 = new AtomLink("rel", "href");
		resource.add(link1);
		AtomLink link2 = new AtomLink("rel", "href");
		resource.add(link2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddNullLink() {
		TestResource resource = new TestResource();
		AtomLink link = null;
		resource.add(link);
	}
	
	@Test
	public void testGetLinks() {
		TestResource resource = new TestResource();
		assertThat(resource.getLinks(), notNullValue());
		assertThat(resource.getLinks().isEmpty(), is(true));
		AtomLink link = new AtomLink("rel", "href");
		resource.add(link);
		assertThat(resource.getLinks(), notNullValue());
		assertThat(resource.getLinks().isEmpty(), is(false));
		assertThat(resource.getLinks().size(), equalTo(1));
		assertThat(resource.getLinks().get(0), equalTo(link));
		AtomLink link2 = new AtomLink("rel2", "href2");
		resource.add(link2);
		assertThat(resource.getLinks().size(), equalTo(2));
		assertThat(resource.getLinks().get(0), equalTo(link));
		assertThat(resource.getLinks().get(1), equalTo(link2));
	}
	
	@Test
	public void testGetLinkRel() {
		TestResource resource = new TestResource();
		assertThat(resource.getLink("rel"), nullValue());
		AtomLink link = new AtomLink("rel", "href");
		resource.add(link);
		assertThat(resource.getLink("rel"), notNullValue());
		assertThat(resource.getLink("rel"), equalTo(link));
		AtomLink link2 = new AtomLink("rel2", "href2");
		resource.add(link2);
		assertThat(resource.getLink("rel2"), equalTo(link2));
	}
	
	@Test
	public void testAddLinks() {
		TestResource resource = new TestResource();
		List<AtomLink> links = new ArrayList<AtomLink>();
		resource.add(links);
		assertThat(resource.getLinks(), equalTo(links));
		// add link
		AtomLink link = new AtomLink("rel", "href");
		links.add(link);
		resource.add(links);
		assertThat(resource.getLinks(), equalTo(links));
	}

	@Test
	public void testToString() {
		TestResource resource = new TestResource();
		assertThat(resource.toString(), equalTo("links: []"));
		AtomLink selfLink = new AtomLink("self", "href");
		resource.add(selfLink);
		assertThat(resource.toString(), equalTo("links: ["+ selfLink.toString() + "]"));
		AtomLink otherLink = new AtomLink("other", "href2");
		resource.add(otherLink);
		assertThat(resource.toString(), equalTo("links: ["+ selfLink.toString() + ", " + otherLink.toString() + "]"));
	}

	@Test
	public void testEqualsObject() {
		TestResource resource1 = new TestResource();
		TestResource resource2 = new TestResource();
		assertEquals(resource1, resource2);
		assertEquals(resource2, resource1);
	}

	@Test
	public void testEqualsNull() {
		TestResource resource1 = new TestResource();
		assertThat(resource1.equals(null), is(false));
	}

	@Test
	public void testEqualsDifferentClass() {
		TestResource resource1 = new TestResource();
		assertThat(resource1.equals("string"), is(false));
	}
	
	
	@XmlAccessorType(XmlAccessType.NONE)
	@XmlRootElement(name = "test")
	public static class TestResource extends Resource {

		@XmlElement
		private String surname;

		@XmlElement(name = "firstName")
		private String stuff;

		public String getSurname() {
			return surname;
		}

		public void setSurname(String surname) {
			this.surname = surname;
		}

		public String getStuff() {
			return stuff;
		}

		public void setStuff(String stuff) {
			this.stuff = stuff;
		}
	}

}
