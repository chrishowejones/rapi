package jhc.figaro.api;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

/**
 * JAXB Context provider to map JSON.
 * 
 * @author Chris Howe-Jones
 * 
 */
@Provider
public class FigaroJAXBContextProvider implements ContextResolver<JAXBContext> {

    private static final Logger LOG = LoggerFactory.getLogger(FigaroJAXBContextProvider.class);

    private final JSONJAXBContext context;

    /**
     * Create JAXB context provider for JSON mapping.
     * 
     * @throws JAXBException
     *             - if there is a problem creating the context.
     */
    public FigaroJAXBContextProvider() throws JAXBException {
        this.context = new JSONJAXBContext(JSONConfiguration.natural().build(),
                "jhc.figaro.api.model.impl.v1:jhc.figaro.api.resource.framework");
         if (LOG.isDebugEnabled()) {
            LOG.debug("Created JSONJAXBContext");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.ws.rs.ext.ContextResolver#getContext(java.lang.Class)
     */
    @Override
    public JAXBContext getContext(final Class<?> type) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Getting context for type: " + type.getName());
        }
        return context;
    }

}
