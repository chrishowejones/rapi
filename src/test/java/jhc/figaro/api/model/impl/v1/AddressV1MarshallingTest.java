package jhc.figaro.api.model.impl.v1;

import java.io.StringReader;
import java.io.StringWriter;

import jhc.figaro.api.model.impl.v1.AddressV1;
import jhc.figaro.api.model.impl.v1.PersonV1;
import jhc.figaro.api.resource.framework.AbstractMashallingTest;

import org.junit.Test;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONMarshaller;
import com.sun.jersey.api.json.JSONUnmarshaller;

public class AddressV1MarshallingTest extends AbstractMashallingTest {

    @Test
    public void testMarshallingPerson() throws Exception {
        AddressV1 address = new AddressV1();
        address.setId(1l);
        address.setPostcode("postcode");
        Class<?>[] types = { AddressV1.class };
        JSONJAXBContext contextj = new JSONJAXBContext(JSONConfiguration.natural().build(), types);
        JSONMarshaller marshaller = contextj.createJSONMarshaller();
        StringWriter sw = new StringWriter();
        marshaller.marshallToJSON(address, sw);
        System.out.println(sw.toString());
    }

    @Test
    public void testUnmarshallPerson() throws Exception {
        Class<?>[] types = { AddressV1.class};
        JSONJAXBContext contextj = new JSONJAXBContext(JSONConfiguration.natural().build(), types);
        AddressV1 address = new AddressV1();
        address.setId(1l);
        address.setPostcode("postcode");
        JSONMarshaller marshaller = contextj.createJSONMarshaller();
        StringWriter sw = new StringWriter();
        marshaller.marshallToJSON(address, sw);
        String jsonAddress = sw.toString();
        // Person person = readJson(jsonPerson, AbstractPersonImpl.class);
        JSONUnmarshaller um = contextj.createJSONUnmarshaller();
        AddressV1 person2 = um.unmarshalFromJSON(new StringReader(jsonAddress), AddressV1.class);
        System.out.println(person2);
    }

}
