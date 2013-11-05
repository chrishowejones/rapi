package jhc.figaro.api.resources;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import jhc.figaro.api.service.VersionServiceImpl;

import org.junit.Test;

public class VersionControllerTest {

    @Test
    public void testVersionController() {
        VersionController controller = new VersionController();
        assertThat(controller, notNullValue());
    }

    @Test
    public void testSetPeopleController() {
        PeopleResourceController peopleController = new PeopleResourceController();
        VersionController controller = new VersionController();
        assertThat(controller.getPeopleController(), nullValue());
        controller.setPeopleController(peopleController);
        assertThat(controller.getPeopleController(), equalTo(peopleController));
    }

    /*
     * Test setting the version service to use for looking up current version.
     */
    @Test
    public void testSetVersionService() {
        VersionController controller = new VersionController();
        assertThat(controller.getVersionService(), nullValue());
        VersionServiceImpl versionService = new VersionServiceImpl();
        controller.setVersionService(versionService);
        assertThat(controller.getVersionService(), equalTo(versionService));
    }

}
