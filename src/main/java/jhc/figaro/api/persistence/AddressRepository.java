package jhc.figaro.api.persistence;

import jhc.figaro.api.persistence.domain.Address;
import jhc.figaro.data.jpa.CommandQueryRepositoryBean;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository responsible for handling the persistence of Address information.
 * 
 * @author Chris Howe-Jones
 * 
 */
public interface AddressRepository extends CommandQueryRepositoryBean<Address, Long> {

	/**
	 * Find Addresses by the owning Person.
	 * 
	 * @param personCode
	 *            - identifying code of the owning Person.
	 * @param pageable
	 *            - page request used to determine rows to return.
	 * @return page of addresses.
	 */
	Page<Address> findByPersonPersonCode(String personCode, Pageable pageable);

	/**
	 * Count the number of Address instances that have a Person as owner whose
	 * personCode matches the parameter.
	 * 
	 * @param personCode
	 *            - person code.
	 * @return count of addresses for the specified person.
	 */
	@Query(value = "SELECT count(*) FROM Address WHERE person.personCode = :personCode")
	long countByPersonPersonCode(@Param("personCode") String personCode);

}
