package jhc.figaro.api.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 * Resolves media types for links to the preferred media type.
 * 
 * @author Chris Howe-Jones
 * 
 */
@Component
public class MediaTypeResolverImpl implements MediaTypeResolver {

	private static final Logger LOG = LoggerFactory.getLogger(MediaTypeResolverImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jhc.figaro.api.resources.MediaTypeResolver#resolveMediaType(java.lang
	 * .String)
	 */
	@Override
	public final String resolveMediaType(final String mediaTypes) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("resolveMediaType(" + mediaTypes + ")");
		}
		String resolvedMediaType = null;
		// try and match most preferred if none match default to first in list.
		for (String mediaType : PREFERRED_MEDIA_TYPES) {
			if (mediaTypes.contains(mediaType)) {
				resolvedMediaType = mediaType;
				break;
			}
		}
		if (resolvedMediaType == null) {
			resolvedMediaType = PREFERRED_MEDIA_TYPES[0];
		}
		return resolvedMediaType;
	}

}
