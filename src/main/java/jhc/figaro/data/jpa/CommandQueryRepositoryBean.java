package jhc.figaro.data.jpa;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import jhc.figaro.api.persistence.domain.PersonDetails;

/**
 * Defines custom methods that override the modifying methods on the
 * JpaRepository interface to call JETS instead of the default implementation.
 * 
 * @param <T> - Type of the persistent object that the repository handles.
 * @param <ID> - Type of the serializable identifier for the persistent type.
 * 
 * @author Chris Howe-Jones
 * 
 */
@NoRepositoryBean
public interface CommandQueryRepositoryBean<T, ID extends Serializable> extends JpaRepository<T, ID> {

	/**
	 * Gets the Command Service.
	 * 
	 * @return service for commands.
	 */
	CommandService<T> getCommandService();
	
	/**
	 * Sets the Command Service.
	 * 
	 * @param service
	 *            - Service for commands.
	 */
    void setCommandService(CommandService<T> service);
	
}
