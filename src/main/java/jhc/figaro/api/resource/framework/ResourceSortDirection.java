package jhc.figaro.api.resource.framework;

import java.util.Locale;

import org.springframework.data.domain.Sort.Direction;

/**
 * Direction of sort required for the resources.
 * 
 * @author Chris Howe-Jones
 * 
 */
public enum ResourceSortDirection  {

	ASC, DESC;
	
	/**
	 * Returns the {@link ResourceSortDirection} enum for the given {@link String} value.
	 * 
	 * @param value
	 * @return
	 */
	public static ResourceSortDirection fromString(String value) {

		try {
			return ResourceSortDirection.valueOf(value.toUpperCase());
		} catch (Exception e) {
			throw new IllegalArgumentException(String.format(
					"Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).", value), e);
		}
	}
}
