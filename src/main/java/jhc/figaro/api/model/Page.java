package jhc.figaro.api.model;

import java.io.Serializable;
import java.util.Iterator;

import jhc.figaro.api.resource.framework.Identifiable;
import jhc.figaro.api.resource.framework.Linkable;

/**
 * Page of resources that implement the Identifiable interface.
 * 
 * @author Chris Howe-Jones
 * 
 * @param <T>
 *            - identifiable.
 */
public interface Page<T extends Identifiable<? extends Serializable>> {

	/**
	 * Get the number of the current page.
	 * 
	 * @return current page number.
	 */
	int getPageNumber();

	/**
	 * Get the total elements in the page.
	 * 
	 * @return total elements represented by this Page instance.
	 */
	long getTotalElements();

	/**
	 * The number of elements in this page of the resources.
	 * 
	 * @return number of Elements in the page.
	 */
	int getNumberOfElements();

	/**
	 * Set the maximum number of elements allowed in a page.
	 * 
	 * @param size
	 *            - maximum number of elements allowed in a page.
	 */
	void setPageSize(int size);

	/**
	 * Get the maximum number of elements allowed in a page.
	 * 
	 * @return maximum number of elements allowed in a page.
	 */
	int getPageSize();

	/**
	 * Get the total number of pages for this instance of resources.
	 * 
	 * @return total number of pages.
	 */
	int getTotalPages();

	/**
	 * Indicates if previous page of resource instances.
	 * 
	 * @return true if previous page of resources exists, false otherwise.
	 */
	boolean hasPreviousPage();

	/**
	 * Indicates if this is the first page of resource instances.
	 * 
	 * @return true if first page, false otherwise.
	 */
	boolean isFirstPage();

	/**
	 * Indicates if there is a next page of resource instances.
	 * 
	 * @return true if next page available, false otherwise.
	 */
	boolean hasNextPage();

	/**
	 * Indicates if this is the last page of resource instances.
	 * 
	 * @return true if last page, false otherwise.
	 */
	boolean isLastPage();

	/**
	 * Iterator for element instances.
	 * 
	 * @return element iterator.
	 */
	Iterator<? extends T> iterator();

}