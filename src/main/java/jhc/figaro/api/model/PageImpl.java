package jhc.figaro.api.model;

import java.io.Serializable;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import jhc.figaro.api.resource.framework.Identifiable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of Page.
 * 
 * @author Chris Howe-Jones
 * 
 * @param <T>
 *            - identifiable type to page over.
 */
public abstract class PageImpl<T extends Identifiable<? extends Serializable>> implements Page<T> {

	private static final Logger LOG = LoggerFactory.getLogger(PageImpl.class);
	private int page;
	private int pageSize;
	private long totalElements;
	private int numberOfElements;

	@Override
	@XmlElement
	public final int getPageNumber() {
		return page;
	}

	/**
	 * Sets the page number for the People resource. Pages are indexed from
	 * zero.
	 * 
	 * @param pageNumber
	 *            - number of the page.
	 */
	public final void setPageNumber(final int pageNumber) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("setPersonList(" + pageNumber + ")");
		}
		if (pageNumber < 0) {
			throw new IllegalArgumentException("Page number cannot be negative.");
		}
		page = pageNumber;
	}

	@Override
	@XmlElement
	public final long getTotalElements() {
		return totalElements;
	}

	/**
	 * Sets the total elements for the People resource.
	 * 
	 * @param elements
	 *            - total number of elements in People resource.
	 */
	public final void setTotalElements(final long elements) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("setPersonList(" + elements + ")");
		}
		if (elements < 0) {
			throw new IllegalArgumentException("Total elements cannot be negative.");
		}
		totalElements = elements;
	}

	@Override
	@XmlElement
	public final int getNumberOfElements() {
		return numberOfElements;
	}

	/**
	 * Sets the number of elements for the current page of the People resource.
	 * 
	 * @param elements
	 *            - number of elements in current page of People resource.
	 */
	public final void setNumberOfElements(final int elements) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("setNumberOfElements(" + elements + ")");
		}
		if (elements < 0) {
			throw new IllegalArgumentException("Number of elements cannot be negative.");
		}
		numberOfElements = elements;
	}

	@Override
	public final void setPageSize(final int size) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("setPageSize(" + size + ")");
		}
		if (size <= 0) {
			throw new IllegalArgumentException("Page size cannot be negative or zero.");
		}
		pageSize = size;
	}

	@Override
	@XmlElement
	public final int getPageSize() {
		return pageSize;
	}

	@Override
	@XmlElement
	public final int getTotalPages() {
		int totalPages = 0;
		if (getTotalElements() != 0) {
			totalPages = (int) Math.ceil((double) getTotalElements() / (double) getPageSize());
		}
		return totalPages;
	}

	@Override
	public final boolean hasPreviousPage() {
		boolean hasPreviousPage = false;
		if (getPageNumber() > 0) {
			hasPreviousPage = true;
		}
		return hasPreviousPage;
	}

	@Override
	public final boolean isFirstPage() {
		if (page == 0) {
			return true;
		}
		return false;
	}

	@Override
	public final boolean hasNextPage() {
		boolean hasNextPage = false;
		if (getPageNumber() + 1 < getTotalPages()) {
			hasNextPage = true;
		}
		return hasNextPage;
	}

	@Override
	public final boolean isLastPage() {
		boolean isLastPage = false;
		if (getPageNumber() + 1 == getTotalPages()) {
			isLastPage = true;
		}
		return isLastPage;
	}

	@Override
	public abstract Iterator<T> iterator();

}