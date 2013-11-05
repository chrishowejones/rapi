package integration.jhc.figaro.api.persistence;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import jhc.figaro.api.persistence.PersonRepository;
import jhc.figaro.api.persistence.domain.PersonDetails;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.googlecode.test.spring.dbunit.AbstractTransactionalDataSetTestCase;
import com.googlecode.test.spring.dbunit.DataSet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/application-context.xml",
        "file:src/main/webapp/WEB-INF/spring/transaction-context.xml",
        "file:src/main/webapp/WEB-INF/spring/spring-data-context.xml" })
@DataSet("dbunit-test-data/PersonDataset.xml")
public class PersonRepositoryITCase extends AbstractTransactionalDataSetTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(PersonRepositoryITCase.class);

    @Autowired
    private PersonRepository repository;

    @Test
    public void testFindPersonDetails() {
    	LOG.debug("testFindPersonDetails");
        String personCode = "A00001";
        PersonDetails detail = repository.findOne(personCode);
        assertThat(detail, not(nullValue()));
        assertThat(detail.getPersonCode().trim(), equalTo(personCode));
        assertThat(detail.getName(), equalTo("MR 'ARIZ ADAMS TEST"));
        assertThat(detail.getEmail(), equalTo("kevin.parker@jhc.co.uk"));
    }

    @Test
    public void testSavePersonDetailsJpa() {
    	LOG.debug("testSavePersonDetailsJpa");
        String personCode = "A01345";
        String email = "test2@jhc.co.uk";
        PersonDetails detailToChange = repository.findOne(personCode);
        assertThat(detailToChange.getEmail(), not(equalTo(email)));
        detailToChange.setEmail(email);
        PersonDetails detail = repository.save(detailToChange);
        assertThat(detail, not(nullValue()));
        assertThat(detail.getPersonCode(), equalTo(personCode));
        assertThat(detail.getName(), equalTo("MR FINTAN BARNES TEST"));
        assertThat(detail.getEmail(), equalTo(email));

        detail = repository.findOne(personCode);
        assertThat(detail, not(nullValue()));
        assertThat(detail.getPersonCode(), equalTo(personCode));
        assertThat(detail.getName(), equalTo("MR FINTAN BARNES TEST"));
        assertThat(detail.getEmail(), equalTo(email));
    }

    @Test
    public void testSearchPersonDetails() {
    	LOG.debug("testSearchPersonDetails");
        String name = "MR FINTAN BARNES TEST";
        PageRequest pageRequest = new PageRequest(0, 10);
        Page<PersonDetails> detailsPage = repository.findByNameLike(name, pageRequest);
        assertThat(detailsPage, notNullValue());
        assertThat(detailsPage.getSize(), is(10));
        assertThat(detailsPage.getNumberOfElements(), is(1));
        assertThat(detailsPage.getContent().get(0).getName(), equalTo(name));
    }

    @Test
    public void testSearchPersonDetailsMultiple() {
    	LOG.debug("testSearchPersonDetailsMultiple");
        String name = "BARNES TEST";
        PageRequest pageRequest = new PageRequest(0, 10);
        Page<PersonDetails> detailsPage = repository.findByNameLike("%" + name + "%", pageRequest);
        assertThat(detailsPage, notNullValue());
        assertThat(detailsPage.getNumberOfElements(), is(3));
        for (PersonDetails details : detailsPage) {
            assertThat(details.getName(), containsString(name));
        }
    }

    @Test
    public void testSearchPersonDetailsPaged() {
    	LOG.debug("testSearchPersonDetailsPaged");
        String name = "DUMMY";
        PageRequest pageRequest = new PageRequest(0, 10);
        Page<PersonDetails> detailsPage = repository.findByNameLike("%" + name + "%", pageRequest);
        assertThat(detailsPage, notNullValue());
        assertThat(detailsPage.getNumberOfElements(), is(10));
        assertThat(detailsPage.getTotalElements(), is(29l));
        assertThat(detailsPage.getNumber(), is(0));
        assertThat(detailsPage.getTotalPages(), is(3));
        for (PersonDetails details : detailsPage) {
            assertThat(details.getName(), containsString(name));
        }

        // fetch next pages
        while (detailsPage.hasNextPage()) {
            int nextPageNumber = detailsPage.getNumber() + 1;
            pageRequest = new PageRequest(nextPageNumber, 10);
            detailsPage = repository.findByNameLike("%" + name + "%", pageRequest);
            assertThat(detailsPage, notNullValue());
            assertThat(detailsPage.getNumberOfElements(), is(nextPageNumber == 2 ? 9 : 10));
            assertThat(detailsPage.getTotalElements(), is(29l));
            assertThat(detailsPage.getNumber(), is(nextPageNumber));
            assertThat(detailsPage.getTotalPages(), is(3));
            for (PersonDetails details : detailsPage) {
                assertThat(details.getName(), containsString(name));
            }
        }
    }

    /*
     * Test paged and sorted implementation of getAll.
     */
    @Test
    public void testGetPeoplePagedExceedsPages() {
    	LOG.debug("testGetPeoplePagedExceedsPages");
        int pageSize = 30;
        PageRequest pageRequest = new PageRequest(2, pageSize, new Sort(Direction.ASC, "personCode"));
        Page<PersonDetails> detailsPage = repository.findAll(pageRequest);
        assertThat(detailsPage, notNullValue());
        assertThat(detailsPage.getNumberOfElements(), equalTo(0));
     }
    
    /*
     * Test paged and sorted implementation of getAll.
     */
    @Test
    public void testGetPeoplePaged() {
    	LOG.debug("testGetPeoplePaged");
        int pageSize = 10;
        PageRequest pageRequest = new PageRequest(0, pageSize, new Sort(Direction.ASC, "personCode"));
        Page<PersonDetails> detailsPage = repository.findAll(pageRequest);
        assertThat(detailsPage, notNullValue());
        assertThat(detailsPage.getNumberOfElements(), equalTo(pageSize));
        // check first page - first and last rows
        assertThat(detailsPage.getContent().get(0).getPersonCode(), equalTo("A00001"));
        assertThat(detailsPage.getContent().get(9).getPersonCode(), equalTo("A00010"));
        // next page
        pageRequest = new PageRequest(1, pageSize, new Sort(Direction.ASC, "personCode"));
        detailsPage = repository.findAll(pageRequest);
        // check second page - first and last rows
        assertThat(detailsPage.getNumberOfElements(), equalTo(pageSize));
        assertThat(detailsPage.getContent().get(0).getPersonCode(), equalTo("A00011"));
        assertThat(detailsPage.getContent().get(9).getPersonCode(), equalTo("A00020"));
        // next page
        pageRequest = new PageRequest(2, pageSize, new Sort(Direction.ASC, "personCode"));
        detailsPage = repository.findAll(pageRequest);
        // check third page - first and last rows
        assertThat(detailsPage.getNumberOfElements(), equalTo(pageSize));
        assertThat(detailsPage.getContent().get(0).getPersonCode(), equalTo("A00021"));
        assertThat(detailsPage.getContent().get(9).getPersonCode(), equalTo("A00030"));
        // next page
        pageRequest = new PageRequest(3, pageSize, new Sort(Direction.ASC, "personCode"));
        detailsPage = repository.findAll(pageRequest);
        // check fourth page - rows
        assertThat(detailsPage.getNumberOfElements(), equalTo(3));
        assertThat(detailsPage.getContent().get(0).getPersonCode(), equalTo("A01345"));
        assertThat(detailsPage.getContent().get(1).getPersonCode(), equalTo("A01346"));
        assertThat(detailsPage.getContent().get(2).getPersonCode(), equalTo("A01347"));

    }

}
