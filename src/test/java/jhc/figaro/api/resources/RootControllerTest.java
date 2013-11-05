package jhc.figaro.api.resources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RootControllerTest {

    @Mock
    private VersionController versionController;

    @Test
    public void testGetRootOptions() {
        RootController root = new RootController();

    }

}
