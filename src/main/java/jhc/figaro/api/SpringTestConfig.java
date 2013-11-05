package jhc.figaro.api;

import java.io.InputStream;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

/**
 * Configuration bean used only in the Test classpath. This is used in both the
 * integration tests including the Jetty plugin started from mvn.
 * 
 * Use DBUnit to load the database for testing.
 * 
 * @author Chris Howe-Jones
 * 
 */
@Configuration
@Profile("development")
public class SpringTestConfig {

	private static final Logger LOG = LoggerFactory
			.getLogger(SpringTestConfig.class);

	@Autowired
	private DataSource dataSource;

	@Autowired
	private Resource personDataset;

	/**
	 * Creates a DataSourceDatabaseTester which is not referenced but trigger
	 * DBUnit to load the data into the Hypersonic database instance.
	 * 
	 * @return person database tester
	 * @throws Exception
	 */
	@Bean
	public DataSourceDatabaseTester getPersonDBTester() throws Exception {
		LOG.info("getDBUnitTester");
		InputStream personIS = personDataset.getInputStream();
		DataSourceDatabaseTester tester = new DataSourceDatabaseTester(
				dataSource);
		tester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(personIS);
		tester.setDataSet(dataSet);
		tester.onSetup();
		return tester;
	}

	/**
	 * Sets the page size to be used in the development runs to enable testing
	 * of pages without using ridiculous amounts of data. Used in PersonService
	 * to determine how big a page of resources will be.
	 * 
	 * @return page size.
	 */
	@Bean(name = "resource.pageSize")
	public int getPageSize() {
		LOG.info("getPageSize");
		// set page size
		return 10;
	}

}
