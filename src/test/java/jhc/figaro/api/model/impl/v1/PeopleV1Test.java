package jhc.figaro.api.model.impl.v1;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import jhc.figaro.api.resource.framework.AtomLink;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PeopleV1Test {

    @Mock
    private List<PersonV1> personList;
    @InjectMocks
    private PeopleV1 personsV1;

    @Test
    public void testGetPersonList() {
        assertThat(personsV1.getPersonList(), notNullValue());
    }

    @Test
    public void testSetPersonList() {
        // set a new Person List as null
        personsV1.setPersonList(null);
        assertThat(personsV1.getPersonList(), nullValue());
        // set a new Person list with empty list
        List<PersonV1> personList = new ArrayList<PersonV1>();
        personsV1.setPersonList(personList);
        assertThat(personsV1.getPersonList(), equalTo(personList));
        assertThat(personsV1.getPersonList().isEmpty(), is(true));

        // set values in Person list.
        PersonV1 person1 = new PersonV1();
        personList.add(person1);
        assertThat(personsV1.getPersonList(), sameInstance(personList));
    }

    @Test
    public void testSetPage() {
        personsV1.setPageNumber(0);
        assertThat(personsV1.getPageNumber(), equalTo(0));
        personsV1.setPageNumber(100);
        assertThat(personsV1.getPageNumber(), equalTo(100));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegative() {
        personsV1.setPageNumber(-1);
    }

    @Test
    public void testSetPageSize() {
        personsV1.setPageSize(1);
        assertThat(personsV1.getPageSize(), equalTo(1));
        personsV1.setPageSize(100);
        assertThat(personsV1.getPageSize(), equalTo(100));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageSizeNegative() {
        personsV1.setPageSize(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageSizeZero() {
        personsV1.setPageSize(0);
    }

    @Test
    public void testSetTotalElements() {
        personsV1.setTotalElements(0l);
        assertThat(personsV1.getTotalElements(), equalTo(0l));
        personsV1.setTotalElements(100l);
        assertThat(personsV1.getTotalElements(), equalTo(100l));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTotalElementsNegative() {
        personsV1.setTotalElements(-1);
    }

    @Test
    public void testSetNumberOfElements() {
        personsV1.setNumberOfElements(0);
        assertThat(personsV1.getNumberOfElements(), equalTo(0));
        personsV1.setNumberOfElements(100);
        assertThat(personsV1.getNumberOfElements(), equalTo(100));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumberOfElementsNegative() {
        personsV1.setNumberOfElements(-1);
    }

    @Test
    public void testSetTotalPages() {
        assertThat(personsV1.getTotalPages(), equalTo(0));
        // simulate populated list
        personsV1.setTotalElements(300l);
        personsV1.setPageSize(300);
        assertThat(personsV1.getTotalPages(), equalTo(1));

        personsV1.setTotalElements(299l);
        personsV1.setPageSize(300);
        assertThat(personsV1.getTotalPages(), equalTo(1));

        personsV1.setTotalElements(1l);
        personsV1.setPageSize(300);
        assertThat(personsV1.getTotalPages(), equalTo(1));

        personsV1.setTotalElements(301l);
        personsV1.setPageSize(300);
        assertThat(personsV1.getTotalPages(), equalTo(2));

        personsV1.setTotalElements(3000l);
        personsV1.setPageSize(300);
        assertThat(personsV1.getTotalPages(), equalTo(10));

        personsV1.setTotalElements(2999l);
        personsV1.setPageSize(300);
        assertThat(personsV1.getTotalPages(), equalTo(10));

        personsV1.setTotalElements(3001l);
        personsV1.setPageSize(300);
        assertThat(personsV1.getTotalPages(), equalTo(11));
    }

    @Test
    public void testHasNextPageTrue() {
        assertThat(personsV1.hasNextPage(), is(false));
        // set up details for total elements, page number and page size
        personsV1.setTotalElements(20l);
        personsV1.setPageSize(10);
        personsV1.setPageNumber(0);
        assertThat(personsV1.hasNextPage(), is(true));
    }

    @Test
    public void testHasNextPageTrueNotFullPage() {
        assertThat(personsV1.hasNextPage(), is(false));
        // set up details for total elements, page number and page size
        personsV1.setTotalElements(11l);
        personsV1.setPageSize(10);
        personsV1.setPageNumber(0);
        assertThat(personsV1.hasNextPage(), is(true));
    }

    @Test
    public void testHasNextPageFalse() {
        assertThat(personsV1.hasNextPage(), is(false));
        // set up details for total elements, page number and page size
        personsV1.setTotalElements(10l);
        personsV1.setPageSize(10);
        personsV1.setPageNumber(0);
        assertThat(personsV1.hasNextPage(), is(false));
    }

    @Test
    public void testHasNextPageFalseNoElements() {
        assertThat(personsV1.hasNextPage(), is(false));
        // set up details for total elements, page number and page size
        personsV1.setTotalElements(0l);
        personsV1.setPageSize(10);
        personsV1.setPageNumber(0);
        assertThat(personsV1.hasNextPage(), is(false));
    }

    @Test
    public void testHasPrevPageTrue() {
        assertThat(personsV1.hasPreviousPage(), is(false));
        // set up details for total elements, page number and page size
        personsV1.setTotalElements(20l);
        personsV1.setPageSize(10);
        personsV1.setPageNumber(1);
        assertThat(personsV1.hasPreviousPage(), is(true));
    }

    @Test
    public void testHasPrevPageTrueNotFullPage() {
        assertThat(personsV1.hasPreviousPage(), is(false));
        // set up details for total elements, page number and page size
        personsV1.setTotalElements(19l);
        personsV1.setPageSize(10);
        personsV1.setPageNumber(1);
        assertThat(personsV1.hasPreviousPage(), is(true));
    }

    @Test
    public void testHasPrevPageFalse() {
        assertThat(personsV1.hasPreviousPage(), is(false));
        // set up details for total elements, page number and page size
        personsV1.setTotalElements(20l);
        personsV1.setPageSize(10);
        personsV1.setPageNumber(0);
        assertThat(personsV1.hasPreviousPage(), is(false));
    }

    @Test
    public void testHasPrevPageFalseOnePage() {
        assertThat(personsV1.hasPreviousPage(), is(false));
        // set up details for total elements, page number and page size
        personsV1.setTotalElements(1l);
        personsV1.setPageSize(10);
        personsV1.setPageNumber(0);
        assertThat(personsV1.hasPreviousPage(), is(false));
    }

    @Test
    public void testSetPageNumber() {
        assertThat(personsV1.getPageNumber(), is(0));
        // set page number
        personsV1.setPageNumber(1);
        assertThat(personsV1.getPageNumber(), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNumberNegative() {
        // set page number -ve
        personsV1.setPageNumber(-1);
    }

    @Test
    public void testIsFirstPage() {
        // set up for first page
        personsV1.setPageNumber(0);
        assertThat(personsV1.isFirstPage(), is(true));
    }

    @Test
    public void testIsNotFirstPage() {
        // set up for first page
        personsV1.setPageNumber(1);
        assertThat(personsV1.isFirstPage(), is(false));
    }

    @Test
    public void testIsLastPage() {
        // set up for last page.
        personsV1.setTotalElements(20l);
        personsV1.setPageSize(10);
        personsV1.setPageNumber(1);
        assertThat(personsV1.isLastPage(), is(true));
    }

    @Test
    public void testIsLastPageNotFullPage() {
        // set up for last page.
        personsV1.setTotalElements(19l);
        personsV1.setPageSize(10);
        personsV1.setPageNumber(1);
        assertThat(personsV1.isLastPage(), is(true));
    }

    @Test
    public void testIsNotLastPage() {
        // set up for last page.
        personsV1.setTotalElements(20l);
        personsV1.setPageSize(10);
        personsV1.setPageNumber(0);
        assertThat(personsV1.isLastPage(), is(false));
    }

    @Test
    public void testLinks() {
        assertThat(personsV1.getLinks(), nullValue());
        // add links
        List<AtomLink> links = new ArrayList<AtomLink>();
        personsV1.setLinks(links);
        assertThat(personsV1.getLinks().isEmpty(), is(true));
        assertThat(personsV1.getLinks(), equalTo(links));
        AtomLink link = new AtomLink("self", "href");
        links.add(link);
        assertThat(personsV1.getLinks().isEmpty(), is(true));
        // add links to personsV1
        personsV1.setLinks(links);
        assertThat(personsV1.getLinks().isEmpty(), is(false));
        assertThat(personsV1.getLinks(), hasSize(1));
    }

    /*
     * Test no args constructor.
     */
    @Test
    public void testPeopleV1NoArgs() {
        PeopleV1 persons = new PeopleV1();
        assertThat(persons, notNullValue());
        assertThat(persons.getLinks(), nullValue());
    }

    @Test
    public void testIterator() {
        assertThat(personsV1.iterator(), nullValue());
        // set up list
        List<PersonV1> personList = new ArrayList<PersonV1>();
        PersonV1 person = new PersonV1();
        personList.add(person);
        personsV1.setPersonList(personList);
        assertThat(personsV1.iterator(), notNullValue());
        assertThat(personsV1.iterator().hasNext(), equalTo(true));
        assertThat(personsV1.iterator().next(), equalTo(person));
    }
}
