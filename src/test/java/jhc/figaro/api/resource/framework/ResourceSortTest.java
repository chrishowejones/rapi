package jhc.figaro.api.resource.framework;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.domain.Sort.Direction;

public class ResourceSortTest {

	private List<String> properties;

	@Before
	public void setUp() {
		properties = new ArrayList<String>();
	}

	@Test
	public void testResourceSortResourceSortDirectionListOfString() {
		String property = "name";
		properties.add(property);
		ResourceSort sort = new ResourceSort(ResourceSortDirection.ASC, properties);
		assertThat(sort.getOrderFor(property), notNullValue());
		assertThat(sort.getOrderFor(property).getDirection(), equalTo(Direction.ASC));
		assertThat(sort.getOrderFor(property).getProperty(), equalTo(property));
		assertThat(sort.getResourceOrderFor(property).getResourceDirection(), equalTo(ResourceSortDirection.ASC));
		assertThat(sort.getResourceOrderFor(property).getProperty(), equalTo(property));
	}

	@Ignore @Test
	public void testResourceSortResourceSortDirectionStringArray() {
		String property = "name";
		ResourceSort sort = new ResourceSort(ResourceSortDirection.ASC, properties);
		assertThat(sort.getOrderFor(property), notNullValue());
		assertThat(sort.getOrderFor(property).getDirection(), equalTo(Direction.ASC));
		assertThat(sort.getOrderFor(property).getProperty(), equalTo(property));
		assertThat(sort.getResourceOrderFor(property).getResourceDirection(), equalTo(ResourceSortDirection.ASC));
		assertThat(sort.getResourceOrderFor(property).getProperty(), equalTo(property));
	}

	@Ignore @Test
	public void testResourceSortListOfResourceOrder() {
		fail("Not yet implemented");
	}

	@Ignore @Test
	public void testResourceSortOrderArray() {
		fail("Not yet implemented");
	}

	@Ignore @Test
	public void testResourceSortStringArray() {
		fail("Not yet implemented");
	}

}
