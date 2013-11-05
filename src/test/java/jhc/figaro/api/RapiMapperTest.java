package jhc.figaro.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import jhc.figaro.api.model.impl.v1.AddressV1;
import jhc.figaro.api.model.impl.v1.PersonV1;
import jhc.figaro.api.persistence.domain.Address;
import jhc.figaro.api.persistence.domain.PersonDetails;

import org.junit.Test;

public class RapiMapperTest {

	@Test
	public void testPersonV1ToPersonDetails() {
		String personCode = "code";
		String name = "name";
		String email = "email";
		PersonV1 person = new PersonV1();
		person.setCode(personCode);
		person.setName(name);
		person.setEmail(email);
		// test mapper
		PersonDetails personDetails = new RapiMapper().map(person, PersonDetails.class);
		assertThat(personDetails, notNullValue());
		assertThat(personDetails.getPersonCode(), equalTo(personCode));
		assertThat(personDetails.getName(), equalTo(name));
		assertThat(personDetails.getEmail(), equalTo(email));

	}

	@Test
	public void testPersonDetailsToPersonV1() {
		String personCode = "code";
		String name = "name";
		String email = "email";
		PersonDetails personDetails = new PersonDetails();
		personDetails.setPersonCode(personCode);
		personDetails.setName(name);
		personDetails.setEmail(email);
		// test mapper
		PersonV1 person = new RapiMapper().map(personDetails, PersonV1.class);
		assertThat(person, notNullValue());
		assertThat(person.getCode(), equalTo(personCode));
		assertThat(person.getName(), equalTo(name));
		assertThat(person.getEmail(), equalTo(email));

	}

	@Test
	public void testMapPersonNullDetails() {
		// test mapper null value
		PersonV1 person = new RapiMapper().map(null, PersonV1.class);
		assertThat(person, is(nullValue()));
	}

	@Test
	public void testMapPersonDetailsNullPerson() {
		// test mapper null value
		PersonDetails details = new RapiMapper().map(null, PersonDetails.class);
		assertThat(details, is(nullValue()));
	}

	@Test
	public void testMapPersonList() {
		String personCode = "code";
		String name = "name";
		String email = "email";
		PersonDetails personDetails1 = new PersonDetails();
		personDetails1.setPersonCode(personCode);
		personDetails1.setName(name);
		personDetails1.setEmail(email);
		String personCode2 = "code2";
		String name2 = "name2";
		String email2 = "email2";
		PersonDetails personDetails2 = new PersonDetails();
		personDetails2.setPersonCode(personCode2);
		personDetails2.setName(name2);
		personDetails2.setEmail(email2);
		List<PersonDetails> detailsList = new ArrayList<PersonDetails>();
		detailsList.add(personDetails1);
		detailsList.add(personDetails2);
		// test mapper
		RapiMapper mapper = new RapiMapper();
		List<PersonV1> personList = mapper.mapAsList(detailsList, PersonV1.class);
		assertThat(personList, notNullValue());
		assertThat(personList.size(), equalTo(2));
		assertThat(personList.get(0).getCode(), equalTo(personCode));
		assertThat(personList.get(0).getName(), equalTo(name));
		assertThat(personList.get(0).getEmail(), equalTo(email));
		assertThat(personList.get(1).getCode(), equalTo(personCode2));
		assertThat(personList.get(1).getName(), equalTo(name2));
		assertThat(personList.get(1).getEmail(), equalTo(email2));
	}

	@Test
	public void testMapPersonListNull() {
		List<PersonDetails> detailsList = null;
		// test mapper
		RapiMapper mapper = new RapiMapper();
		List<PersonV1> personList = mapper.mapAsList(detailsList, PersonV1.class);
		assertThat(personList, nullValue());
	}

	@Test
	public void testMapPersonListNoDetails() {
		List<PersonDetails> detailsList = new ArrayList<PersonDetails>();
		// test mapper
		RapiMapper mapper = new RapiMapper();
		List<PersonV1> personList = mapper.mapAsList(detailsList, PersonV1.class);
		assertThat(personList, notNullValue());
		assertThat(personList.size(), equalTo(0));
	}

	@Test(expected = NullPointerException.class)
	public void testMapPersonListNullDetails() {
		List<PersonDetails> detailsList = new ArrayList<PersonDetails>();
		detailsList.add(null);
		detailsList.add(null);
		// test mapper
		RapiMapper mapper = new RapiMapper();
		mapper.mapAsList(detailsList, PersonV1.class);
	}

	@Test
	public void testMapDetailsList() {
		String personCode = "code";
		String name = "name";
		String email = "email";
		PersonV1 person1 = new PersonV1();
		person1.setCode(personCode);
		person1.setName(name);
		person1.setEmail(email);
		String personCode2 = "code2";
		String name2 = "name2";
		String email2 = "email2";
		PersonV1 person2 = new PersonV1();
		person2.setCode(personCode2);
		person2.setName(name2);
		person2.setEmail(email2);
		List<PersonV1> personList = new ArrayList<PersonV1>();
		personList.add(person1);
		personList.add(person2);
		// test mapper
		RapiMapper mapper = new RapiMapper();
		List<PersonDetails> personDetailsList = mapper.mapAsList(personList, PersonDetails.class);
		assertThat(personDetailsList, notNullValue());
		assertThat(personDetailsList.size(), equalTo(2));
		assertThat(personDetailsList.get(0).getPersonCode(), equalTo(personCode));
		assertThat(personDetailsList.get(0).getName(), equalTo(name));
		assertThat(personDetailsList.get(0).getEmail(), equalTo(email));
		assertThat(personDetailsList.get(1).getPersonCode(), equalTo(personCode2));
		assertThat(personDetailsList.get(1).getName(), equalTo(name2));
		assertThat(personDetailsList.get(1).getEmail(), equalTo(email2));
	}

	@Test
	public void testMapDetailsListNull() {
		List<PersonV1> personList = null;
		// test mapper
		RapiMapper mapper = new RapiMapper();
		List<PersonDetails> detailsList = mapper.mapAsList(personList, PersonDetails.class);
		assertThat(detailsList, nullValue());
	}

	@Test
	public void testMapDetailsListNoDetails() {
		List<PersonV1> personList = new ArrayList<PersonV1>();
		// test mapper
		RapiMapper mapper = new RapiMapper();
		List<PersonDetails> detailsList = mapper.mapAsList(personList, PersonDetails.class);
		assertThat(detailsList, notNullValue());
		assertThat(detailsList.size(), equalTo(0));
	}

	@Test(expected = NullPointerException.class)
	public void testMapDetailsListNullDetails() {
		List<PersonV1> personList = new ArrayList<PersonV1>();
		personList.add(null);
		personList.add(null);
		// test mapper
		RapiMapper mapper = new RapiMapper();
		mapper.mapAsList(personList, PersonDetails.class);
	}

	@Test
	public void testAddressV1ToAddress() {
		long id = 10l;
		String line1 = "line1";
		String line2 = "line2";
		String city = "city";
		String postcode = "postcode";
		String type = "type1";
		PersonV1 person = new PersonV1();
		String personCode = "code1";
		person.setCode(personCode);
		AddressV1 address = new AddressV1();
		address.setId(id);
		address.setLine1(line1);
		address.setLine2(line2);
		address.setCity(city);
		address.setAddressType(type);
		address.setPostcode(postcode);
		address.setPersonCode(personCode);
		// test mapper
		jhc.figaro.api.persistence.domain.Address persistentAddress = new RapiMapper().map(address, jhc.figaro.api.persistence.domain.Address.class);
		assertThat(persistentAddress, notNullValue());
		assertThat(persistentAddress.getId(), equalTo(id));
		assertThat(persistentAddress.getLine1(), equalTo(line1));
		assertThat(persistentAddress.getLine2(), equalTo(line2));
		assertThat(persistentAddress.getCity(), equalTo(city));
		assertThat(persistentAddress.getPostcode(), equalTo(postcode));
		assertThat(persistentAddress.getAddressType(), equalTo(type));
		assertThat(persistentAddress.getPerson(), nullValue());
	}
	
	@Test
	public void testAddressToAddressV1() {
		long id = 10l;
		String line1 = "line1";
		String line2 = "line2";
		String city = "city";
		String postcode = "postcode";
		String type = "type1";
		PersonDetails person = new PersonDetails();
		String personCode = "code1";
		person.setPersonCode(personCode);
		Address address = new Address();
		address.setId(id);
		address.setLine1(line1);
		address.setLine2(line2);
		address.setCity(city);
		address.setAddressType(type);
		address.setPostcode(postcode);
		address.setPerson(person);
		// test mapper
		AddressV1 resourceAddress = new RapiMapper().map(address, AddressV1.class);
		assertThat(resourceAddress, notNullValue());
		assertThat(resourceAddress.getId(), equalTo(id));
		assertThat(resourceAddress.getLine1(), equalTo(line1));
		assertThat(resourceAddress.getLine2(), equalTo(line2));
		assertThat(resourceAddress.getCity(), equalTo(city));
		assertThat(resourceAddress.getPostcode(), equalTo(postcode));
		assertThat(resourceAddress.getAddressType(), equalTo(type));
		assertThat(resourceAddress.getPersonCode(), equalTo(personCode));
	}
}
