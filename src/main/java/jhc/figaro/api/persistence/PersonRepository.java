package jhc.figaro.api.persistence;

import java.util.List;

import jhc.figaro.api.persistence.domain.PersonDetails;
import jhc.figaro.data.jpa.CommandQueryRepositoryBean;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository responsible for handling the persistence of Person information.
 * 
 * 
 * @author Chris Howe-Jones
 * 
 */
public interface PersonRepository extends CommandQueryRepositoryBean<PersonDetails, String> {

	/**
	 * Search for a persons details using a pattern match on name. Will return a
	 * list of PersonDetails that have a name that contains the supplied search
	 * string.
	 * 
	 * @param searchString
	 *            - String that will be used in the name search.
	 * @param pageRequest
	 *            - page request used to determine rows to return.
	 * @return page of PersonDetails whose names include the searchString.
	 */
	@Query(name = "searchByName", countName = "countSearchByName")
	Page<PersonDetails> findByNameLike(@Param("searchTerm") String searchString, Pageable pageRequest);

	List<PersonDetails> getByEmail(String email);
}