package jhc.figaro.api.service;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import jhc.figaro.api.service.v1.impl.DummyPersonService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PersonServiceResolverSpringTest {

	@Autowired
	PersonServiceResolver resolver;
	
	@Test
	public void testGetPersonService() {
		assertThat(resolver.getPersonService("dummy"), instanceOf(DummyPersonService.class));
	}
	
	@Test
	public void testGetPersonServiceNoMatch() {
		assertThat(resolver.getPersonService("noversion"), is(nullValue()));
	}

}
