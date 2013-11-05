package integration.jhc.figaro.api.persistence;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import jhc.figaro.api.persistence.JetsPersonCommandService;
import jhc.figaro.api.persistence.PersonRepository;
import jhc.figaro.api.persistence.domain.PersonDetails;
import jhc.figaro.data.jpa.CommandQueryRepositoryBean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.googlecode.test.spring.dbunit.DataSet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/application-context.xml",
        "file:src/main/webapp/WEB-INF/spring/transaction-context.xml",
        "file:src/main/webapp/WEB-INF/spring/spring-data-context.xml" })
@DataSet("dbunit-test-data/PersonDataset.xml")
// set active profile to production to get the Jets implementation.
@ActiveProfiles("production")
public class JETSPersonRepositoryITCase {

    @Autowired
    private PersonRepository repository;

    // TODO complete implementation
    @Test(expected = UnsupportedOperationException.class)
    public void testSave() {
        assertThat(((CommandQueryRepositoryBean<PersonDetails, String>) repository).getCommandService(),
                instanceOf(JetsPersonCommandService.class));
        String personCode = "code1";
        String name = "name";
        String email = "email";
        PersonDetails personDetails = new PersonDetails();
        personDetails.setName(name);
        personDetails.setPersonCode(personCode);
        personDetails.setEmail(email);
        PersonDetails managedPersonDetails = repository.save(personDetails);
        assertThat(managedPersonDetails, equalTo(personDetails));

        // fetch details again to check
        PersonDetails fetched = repository.findOne(personCode);
        assertThat(managedPersonDetails, equalTo(fetched));
    }
}
