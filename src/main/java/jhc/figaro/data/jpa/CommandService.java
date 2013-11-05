package jhc.figaro.data.jpa;

/**
 * Responsible for handling any custom commands that are required to carry out
 * data maintenance rather than Queries. For example: an implementation of this
 * class may delegate the update action to an API calling on to the AS400 code.
 * 
 * @author Chris Howe-Jones
 * 
 * @param <T>
 *            The type of persistence entity that this command service handles.
 */
public interface CommandService<T> {

	/**
	 * Update the entity using a custom method. For example: call an API to
	 * carry out the update.
	 * 
	 * @param <S> - entity type of returned entity that must extend type T.
	 * @param entity - entity to be updated
	 * @return updated entity
	 */
	<S extends T> S update(T entity);

}
