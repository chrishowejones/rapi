package jhc.figaro.api.resources;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CustomMediaTypeTest {

    @Test
    public void testToString() {
        assertThat(CustomMediaType.APPLICATION_PEOPLE_TYPE.toString(), equalTo("application/vnd.figaro.people+json"));
        assertThat(CustomMediaType.APPLICATION_PEOPLE_XML_TYPE.toString(), equalTo("application/vnd.figaro.people+xml"));
    }

    @Test
    public void testValueOfApplicationPeopleJson() {
        // lowercase
        assertThat(CustomMediaType.valueOf("application/vnd.figaro.people+json"),
                equalTo(CustomMediaType.APPLICATION_PEOPLE_TYPE));
        // mixed case
        assertThat(CustomMediaType.valueOf("application/vNd.fIgarO.People+Json"),
                equalTo(CustomMediaType.APPLICATION_PEOPLE_TYPE));

    }

}
