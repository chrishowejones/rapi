package jhc.figaro.api.resources;

import javax.ws.rs.Path;

import jhc.figaro.api.service.VersionServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Version controller.
 * 
 * @see {@link RootController} for how this is mapped to a path.
 * 
 * @author Chris Howe-Jones
 * 
 */
// TODO - return the options for resources in the body of the response.
@Path("/{version: [vV][0-9]}")
@Component
public class VersionController {

    private PeopleResourceController peopleController;
    private VersionServiceImpl versionService;

    /**
     * Default no argument constructor for the version controller. TODO will use version service to resolve current
     * version.
     */
    public VersionController() {
    }

    /**
     * Get the PeopleController.
     * 
     * @return controller for Person resources.
     */
    @Path("/people")
    public final PeopleResourceController getPeopleController() {
        return peopleController;
    }

    /**
     * Set the people controller.
     * 
     * @param peopleController
     *            - controller for Person resources.
     */
    @Autowired
    public final void setPeopleController(final PeopleResourceController peopleController) {
        this.peopleController = peopleController;
    }

    /**
     * Sets the VersionService used to determine current and valid versions of the API.
     * 
     * @param versionService
     *            - Version service.
     */
    public final void setVersionService(final VersionServiceImpl versionService) {
        this.versionService = versionService;
    }

    /**
     * Gets the VersionService used to determine current and valid versions.
     * 
     * @return version service.
     */
    public final VersionServiceImpl getVersionService() {
        return versionService;
    }

}
