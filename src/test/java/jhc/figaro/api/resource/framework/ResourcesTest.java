package jhc.figaro.api.resource.framework;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

public class ResourcesTest {

    @Test
    public void testHashCodeEmptyContent() {
        List<Resource> list = new ArrayList<Resource>();
        Resources<Resource> resources = new Resources<Resource>(list);
        assertThat(resources.hashCode(), equalTo(System.identityHashCode(resources) + 17));
    }

    @Test
    public void testHashCodeContent() {
        List<Resource> list = new ArrayList<Resource>();
        MockResource resource = new MockResource();
        list.add(resource);
        Resources<Resource> resources = new Resources<Resource>(list);
        assertThat(resources.hashCode(), equalTo(System.identityHashCode(resources) + 17 * list.hashCode()));
    }

    @Test
    public void testEqualsWrongClass() {
        List<Resource> list = new ArrayList<Resource>();
        Resource resource = Mockito.mock(Resource.class);
        list.add(resource);
        Resources<Resource> resources = new Resources<Resource>(list);
        assertThat(resources.equals("string"), equalTo(false));
    }

    @Test
    public void testEqualsDifferentContent() {
        List<Resource> list = new ArrayList<Resource>();
        List<Resource> list2 = new ArrayList<Resource>();
        MockResource resource = new MockResource();
        MockResource resource2 = new MockResource();
        // prepare mock to return equal false
        resource.setEquals(false);
        list.add(resource);
        list2.add(resource2);
        Resources<Resource> resources = new Resources<Resource>(list);
        Resources<Resource> resources2 = new Resources<Resource>(list2);
        assertThat(resources.equals(resources2), equalTo(false));
    }

    @Test
    public void testEqualsSameContent() {
        List<Resource> list = new ArrayList<Resource>();
        MockResource resource = new MockResource();
        // prepare mock to return equal true
        resource.setEquals(true);
        list.add(resource);
        Resources<Resource> resources = new Resources<Resource>(list);
        Resources<Resource> resources2 = new Resources<Resource>(list);
        assertThat(resources.equals(resources2), equalTo(true));
    }

    @Test
    public void testEqualsEqualContent() {
        List<Resource> list = new ArrayList<Resource>();
        List<Resource> list2 = new ArrayList<Resource>();
        MockResource resource = new MockResource();
        MockResource resource2 = new MockResource();
        // prepare mock to return equal true
        resource.setEquals(true);
        resource2.setEquals(true);
        list.add(resource);
        list2.add(resource2);
        Resources<Resource> resources = new Resources<Resource>(list);
        Resources<Resource> resources2 = new Resources<Resource>(list2);
        assertThat(resources.equals(resources2), equalTo(true));
    }

    @Test
    public void testEqualsSameObject() {
        List<Resource> list = new ArrayList<Resource>();
        MockResource resource = new MockResource();
        // prepare mock to return equal true
        resource.setEquals(true);
        list.add(resource);
        Resources<Resource> resources = new Resources<Resource>(list);
        assertThat(resources.equals(resources), equalTo(true));
    }

    @Test
    public void testEqualsNull() {
        List<Resource> list = new ArrayList<Resource>();
        Resources<Resource> resources = new Resources<Resource>(list);
        assertThat(resources.equals(null), equalTo(false));
    }

    private class MockResource extends Resource {

        private boolean isEqual;

        public void setEquals(boolean equals) {
            isEqual = equals;
        }

        @Override
        public boolean equals(Object object) {
            return isEqual;
        }

    }

}
