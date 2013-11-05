package jhc.figaro.api.resource.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

/**
 * TODO Complete implementation and use as super class for ResourcePages (collections of Resource)
 * 
 * A page object used by pageable collection resources.
 * 
 * @author Chris Howe-Jones
 * 
 */
public class ResourcePageRequest implements Pageable {

    private static final Logger LOG = LoggerFactory.getLogger(ResourcePageRequest.class);

    private int page;

    private int pageSize;

    private long totalElements;

    private int numberOfElements;

    /**
     * Constructs an instance of ResourcePageRequest that uses the specified page and page size.
     * 
     * @param pageNumber
     *            - the index number of the page @see {@link #getPageNumber()}.
     * @param size
     *            - the page size.
     */
    public ResourcePageRequest(final int pageNumber, final int size) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("ResourcePageRequest(" + pageNumber + ", " + size + ")");
        }

    }

    /**
     * Constructs an instance of ResourcePageRequest that uses the specified page, page size, sort direction and
     * properties to be used in the sort.
     * 
     * @param pageNumber
     *            - the index number of the page @see {@link #getPageNumber()}.
     * @param size
     *            - the page size.
     * @param direction
     *            - the sort direction.
     * @param properties
     *            - the properties to sort on.
     */
    public ResourcePageRequest(final int pageNumber, final int size, final ResourceSortDirection direction,
            final String... properties) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("ResourcePageRequest(" + pageNumber + ", " + size + ", " + direction + ", properties)");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.domain.Pageable#getPageNumber()
     */
    @Override
    public int getPageNumber() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.domain.Pageable#getPageSize()
     */
    @Override
    public int getPageSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getOffset() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Sort getSort() {
        // TODO Auto-generated method stub
        return null;
    }

}
