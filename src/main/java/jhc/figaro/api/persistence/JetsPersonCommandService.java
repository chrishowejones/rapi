package jhc.figaro.api.persistence;

import jhc.figaro.api.persistence.domain.PersonDetails;
import jhc.figaro.data.jpa.CommandService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Implementation of a CommandService that provides the implementation of the
 * persistence methods required to call onto JETS.
 */
@Profile("production")
@Component
public class JetsPersonCommandService implements CommandService<PersonDetails> {
	
	private static final Logger LOG = LoggerFactory.getLogger(JetsPersonCommandService.class);

	/*
	 * TODO Implement this to call JETS-Person
	 * 
	 * (non-Javadoc)
	 * @see jhc.figaro.data.jpa.CommandService#update(java.lang.Object)
	 */
	@Override
	public final <S extends PersonDetails> S update(final PersonDetails entity) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("update(" + entity + ")");
		}
		throw new UnsupportedOperationException("The call to Jets Person needs implementing here!");
	}

}
