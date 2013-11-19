package jhc.figaro.api.resource.framework;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.hibernate.hql.internal.ast.tree.IsNotNullLogicOperatorNode;
import org.junit.Test;

public class AtomLinkTest {

	@Test
	public void testAtomLink() {
		AtomLink atomLink = new AtomLink();
		assertThat(atomLink, notNullValue());
	}

	@Test
	public void testAtomLinkStringStringString() {
		String typeValue = "application/xml";
		String hrefValue =  "/people/person/1";
		String relValue = "self";
		AtomLink atomLink = new AtomLink(relValue, hrefValue, typeValue);
		assertThat(atomLink, notNullValue());
		assertThat(atomLink.getType(), is(typeValue));
		assertThat(atomLink.getHref(), is(hrefValue));
		assertThat(atomLink.getRel(), is(relValue));
	}

	@Test
	public void testAtomLinkStringString() {
		String typeValue = "application/json";
		String hrefValue =  "/people/person/1";
		String relValue = "self";
		AtomLink atomLink = new AtomLink(relValue, hrefValue);
		assertThat(atomLink, notNullValue());
		assertThat(atomLink.getType(), is(typeValue));
		assertThat(atomLink.getHref(), is(hrefValue));
		assertThat(atomLink.getRel(), is(relValue));
	}

	@Test
	public void testAtomLinkAtomRelationshipStringString() {
		String typeValue = "application/json";
		String hrefValue =  "/people/person/1";
		AtomRelationship rel = AtomRelationship.NEXT;
		AtomLink atomLink = new AtomLink(rel, hrefValue, typeValue);
		assertThat(atomLink, notNullValue());
		assertThat(atomLink.getType(), is(typeValue));
		assertThat(atomLink.getHref(), is(hrefValue));
		assertThat(atomLink.getRel(), equalTo(rel.toString()));
	}

	@Test
	public void testAtomLinkAtomRelationshipString() {
		String typeValue = "application/json";
		String hrefValue =  "/people/person/1";
		AtomRelationship rel = AtomRelationship.NEXT;
		AtomLink atomLink = new AtomLink(rel, hrefValue);
		assertThat(atomLink, notNullValue());
		assertThat(atomLink.getType(), is(typeValue));
		assertThat(atomLink.getHref(), is(hrefValue));
		assertThat(atomLink.getRel(), equalTo(rel.toString()));
	}

	@Test
	public void testSetRel() {
		AtomLink atomLink = new AtomLink();
		String relValue = "self";
		atomLink.setRel(relValue);
		assertThat(atomLink.getRel(), is(relValue));
	}

	@Test
	public void testSetHref() {
		AtomLink atomLink = new AtomLink();
		String hrefValue =  "/people/person/1";
		atomLink.setHref(hrefValue);
		assertThat(atomLink.getHref(), is(hrefValue));
	}

	@Test
	public void testSetType() {
		AtomLink atomLink = new AtomLink();
		String typeValue = "application/xml";
		atomLink.setType(typeValue);
		assertThat(atomLink.getType(), is(typeValue));
	}

	@Test
	public void testClone() {
		String typeValue = "application/xml";
		String hrefValue =  "/people/person/1";
		String relValue = "self";
		AtomLink atomLink = new AtomLink(relValue, hrefValue, typeValue);
		AtomLink clone = (AtomLink) atomLink.clone();
		assertThat(clone, notNullValue());
		assertThat(atomLink.getHref(), equalTo(clone.getHref()));
		assertThat(atomLink.getRel(), equalTo(clone.getRel()));
	}

}
