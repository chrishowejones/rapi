package integration.jhc.figaro.api.persistence;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import jhc.figaro.api.persistence.AddressRepository;
import jhc.figaro.api.persistence.domain.Address;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class AddressRepositoryITCase extends AbstractTransactionalDataSetTestCase {

	private static final Logger LOG = LoggerFactory.getLogger(AddressRepositoryITCase.class);
	
	@Autowired
	private AddressRepository addressRepo;
	
	@Test
	public void testFindByPersonPersonCode() {
		LOG.debug("testFindByPersonPersonCode");
		Address address1 = new Address();
		Address address2 = new Address();
		address1.setId(1l);
		address2.setId(2l);
		int pageSize = 10;
		String personCode = "A00001";
		Pageable pageable = new PageRequest(0, pageSize, Direction.ASC, "id");
		Page<Address> addressPage = addressRepo.findByPersonPersonCode(personCode, pageable);
		assertThat(addressPage, notNullValue());
		assertThat(addressPage.hasContent(), is(true));
		assertThat(addressPage.getContent(), hasSize(2));
		assertThat(addressPage.getContent(), hasItems(address1, address2));
		assertThat(addressPage.getSize(), equalTo(pageSize));
		assertThat(addressPage.getNumber(), equalTo(0));
		assertThat(addressPage.getNumberOfElements(), equalTo(2));
		assertThat(addressPage.getTotalPages(), equalTo(1));
	}
	
	@Test
	public void testCountByPersonCode() {
		LOG.debug("testCountByPersonCode()");
		String personCode = "A00001";
		long addressCount = addressRepo.countByPersonPersonCode(personCode);
		assertThat(addressCount, equalTo(2l));
	}

	@Test
	public void testFindById() {
		Address address1 = new Address();
		address1.setId(1l);
		
		Address addressReturned = addressRepo.findOne(1l);
		assertThat(addressReturned, notNullValue());
		assertThat(addressReturned, equalTo(address1));
	}
}
