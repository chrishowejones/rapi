package jhc.figaro.data.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Person repository that uses JETS-person to carry out persistence (inserts and
 * updates).
 * 
 * @param <T>
 *            - entity type.
 * @param <ID>
 *            - serializable type representing the id of the entity.
 * 
 * @author Chris Howe-Jones
 * 
 */
public class CommandQueryRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements
		CommandQueryRepositoryBean<T, ID> {

	private EntityManager entityManager;

	/**
	 * Creates a CommandQueryRepositoryImpl that delegates to a CommandService
	 * to implement the persistence of entities but uses the SimpleJpaRepository
	 * for all queries. If the command service has not been set then the
	 * CommandQueryRepositoryImpl will delegate all persistence to the
	 * SimpleJpaRepository.
	 * 
	 * @param domainClass
	 *            = class of the domain entity.
	 * @param em
	 *            - EntityManager used for JpaPersistence.
	 */
	public CommandQueryRepositoryImpl(final Class<T> domainClass, final EntityManager em) {
		super(domainClass, em);
		this.entityManager = em;
	}

	private static final Logger LOG = LoggerFactory.getLogger(CommandQueryRepositoryImpl.class);

	/*
	 * Reference to command service responsible for updates.
	 */
	private CommandService<T> commandService;

	@Override
	@Transactional
	public <S extends T> S save(S entity) {
		if (commandService == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Calling Jpa save(entity)");
			}
			return super.save(entity);
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Calling CommandService.update(entity)");
			}
			return commandService.update(entity);
		}
	}

	/**
	 * Gets the Command Service.
	 * 
	 * @return service for commands.
	 */
	public final CommandService<T> getCommandService() {
		return commandService;
	}

	/**
	 * Sets the Command Service.
	 * 
	 * @param service
	 *            - Service for commands.
	 */
	public final void setCommandService(final CommandService<T> service) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Setting JETSPersonService(" + service + ")");
		}
		this.commandService = service;
	}

}
