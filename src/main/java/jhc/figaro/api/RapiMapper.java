package jhc.figaro.api;

import jhc.figaro.api.model.impl.v1.AddressV1;
import jhc.figaro.api.model.impl.v1.PersonV1;
import jhc.figaro.api.persistence.domain.Address;
import jhc.figaro.api.persistence.domain.PersonDetails;
import ma.glasnost.orika.DefaultFieldMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Customisable mapper to map Person and PersonDetails.
 * 
 * @author Chris Howe-Jones
 * 
 */
@Component
public class RapiMapper extends ConfigurableMapper {

	private static final Logger LOG = LoggerFactory.getLogger(RapiMapper.class);

	private static final String PROPERTY_PREFIX = "person";

	/**
	 * Configure a customer mapper.
	 * 
	 * @param factory
	 *            - Mapper factory to configure.
	 */
	/*
	 * TODO need to investigate effect of mapping Address.person.personCode to
	 * AddressV1.personCode as this action on a managed persistent Address
	 * object causes a fetch of the owning Person.
	 */
	@Override
	protected final void configure(final MapperFactory factory) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("configure(" + factory + ")");
		}
		ClassMapBuilder<PersonV1, PersonDetails> builder = factory.classMap(PersonV1.class, PersonDetails.class);
		DefaultFieldMapper personDefaultMapper = new DefaultFieldMapper() {

			public String suggestMappedField(final String propertyName, final Type<?> fromPropertyType) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("suggest propertyName starting with Person");
				}
				if (propertyName.startsWith("person")) {
					// Remove the "person" prefix and adjust camel-case
					return propertyName.substring(PROPERTY_PREFIX.length(), 1).toLowerCase()
							+ propertyName.substring(PROPERTY_PREFIX.length() + 1);
				} else {
					// Add a "person" prefix and adjust camel-case
					return "person" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
				}
			}
		};
		factory.registerClassMap(builder.byDefault(personDefaultMapper).toClassMap());
		// set class map for persistent Address to AddressV1 to map personCode
		// from owning Person - unidirectionally so Person not set on Address by
		// mapper.
		factory.registerClassMap(factory.classMap(Address.class, AddressV1.class)
				.fieldAToB("person.personCode", "personCode").byDefault().toClassMap());
	}

}
