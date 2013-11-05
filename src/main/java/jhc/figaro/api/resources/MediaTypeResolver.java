package jhc.figaro.api.resources;

import javax.ws.rs.core.MediaType;

public interface MediaTypeResolver {

	/**
	 * Array of preferred media types in order. First being most preferred etc.
	 */
	public static final String[] PREFERRED_MEDIA_TYPES = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML };

	/**
	 * Resolves the list of media types presented (in a comma separated list) to
	 * a preferred media type for the request links.
	 * 
	 * @param mediaTypes
	 *            - String of comma separated media types.
	 * @return preferred media type.
	 */
	public abstract String resolveMediaType(String mediaTypes);

}