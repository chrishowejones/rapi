package jhc.figaro.data.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * 
 * @author Howe-JonesC
 *
 * @param <T>
 * @param <S>
 * @param <ID>
 */
public class CommandQueryRepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID extends Serializable> extends
		JpaRepositoryFactoryBean<T, S, ID> {
	
	/**
	 * Creates a new CommandQueryRepositoryFactory.
	 * @param entityManager - entity manager to use for Jpa.
	 * @return support class for the CommandQueryRepositoryFactory.
	 */
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new CommandQueryRepositoryFactory<S, ID>(entityManager);
	}
	
	
	private static class CommandQueryRepositoryFactory<S, ID extends Serializable> extends JpaRepositoryFactory {

		private EntityManager entityManager;

		public CommandQueryRepositoryFactory(EntityManager em) {
			super(em);
			this.entityManager = em;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Object getTargetRepository(RepositoryMetadata metadata) {
			return new CommandQueryRepositoryImpl<S, Serializable>((Class<S>)metadata.getDomainType(), entityManager);
		}

		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return CommandQueryRepositoryImpl.class;
		}
		
		
	}

}
