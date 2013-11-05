package jhc.figaro.api.model.impl.v1;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import jhc.figaro.api.model.impl.v1.AddressV1;
import jhc.figaro.api.model.impl.v1.PersonV1;
import jhc.figaro.api.resource.framework.AbstractMashallingTest;

import org.junit.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONMarshaller;
import com.sun.jersey.api.json.JSONUnmarshaller;

public class PersonV1MarshallingTest extends AbstractMashallingTest {

	@Test
	public void testMarshallingPerson() throws Exception {
		PersonV1 person = new PersonV1();
		String code = "code1";
		String email = "email@jhc.co.uk";
		long addressId = 10l;
		String addressLine1 = "line1";
		String addressPostcode = "postcode";
		person.setCode(code);
		person.setEmail(email);
		AddressV1 address = new AddressV1();
		address.setId(addressId);
		address.setLine1(addressLine1);
		address.setPostcode(addressPostcode);
		List<AddressV1> addresses = new ArrayList<AddressV1>();
		addresses.add(address);
		person.setAddresses(addresses);
		Class<?>[] types = { PersonV1.class, AddressV1.class };
		JSONJAXBContext contextj = new JSONJAXBContext(JSONConfiguration.natural().build(), types);
		JSONMarshaller marshaller = contextj.createJSONMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshallToJSON(person, sw);
		String personJson = sw.toString();
		System.out.println(personJson);
		assertThat(JsonPath.from(personJson).getString("code"), equalTo(code));
		assertThat(JsonPath.from(personJson).getString("email"), equalTo(email));
		assertThat(JsonPath.from(personJson).getLong("addresses.get(0).id"), equalTo(addressId));
		assertThat(JsonPath.from(personJson).getString("addresses.get(0).line1"), equalTo(addressLine1));
		assertThat(JsonPath.from(personJson).getString("addresses.get(0).postcode"), equalTo(addressPostcode));
		System.out.println(sw.toString());
	}

	@Test
	public void testUnmarshallPerson() throws Exception {
		String code = "code1";
		String email = "email@jhc.co.uk";
		long addressId = 10l;
		String addressLine1 = "line1";
		String addressPostcode = "postcode";
		Class<?>[] types = { PersonV1.class, AddressV1.class };
		JSONJAXBContext contextj = new JSONJAXBContext(JSONConfiguration.natural().build(), types);
		String jsonPerson = "{\"code\":\"code1\",\"email\":\"email@jhc.co.uk\",\"addresses\":[{\"id\":10,\"line1\":\"line1\",\"postcode\":\"postcode\"}]}";
		System.out.println("json = " + jsonPerson);
		JSONUnmarshaller um = contextj.createJSONUnmarshaller();
		PersonV1 person2 = um.unmarshalFromJSON(new StringReader(jsonPerson), PersonV1.class);
		System.out.println(person2);
		assertThat(person2.getCode(), equalTo(code));
		assertThat(person2.getEmail(), equalTo(email));
		assertThat(person2.getAddresses().get(0).getId(), equalTo(addressId));
		assertThat(person2.getAddresses().get(0).getLine1(), equalTo(addressLine1));
		assertThat(person2.getAddresses().get(0).getPostcode(), equalTo(addressPostcode));
		
	}

}
