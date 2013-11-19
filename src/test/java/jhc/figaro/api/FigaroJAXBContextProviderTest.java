package jhc.figaro.api;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.notNullValue;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.junit.Test;

public class FigaroJAXBContextProviderTest {

	@Test
	public void testGetContext() throws JAXBException {
		FigaroJAXBContextProvider provider = new FigaroJAXBContextProvider();
		JAXBContext context = provider.getContext(jhc.figaro.api.model.impl.v1.AddressesV1.class);
		assertThat(context, notNullValue());
	}

}
