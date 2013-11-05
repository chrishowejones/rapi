package jhc.figaro.api.resources;

import javax.ws.rs.OPTIONS;

import org.hibernate.internal.jaxb.mapping.orm.JaxbElementCollection;

public class RapiResource {
	
	@OPTIONS
	public JaxbElementCollection getOptions() {
		return null;
	}

}
