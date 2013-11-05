package jhc.figaro.api.resources;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

public class MediaTypeResolverTest {

	  @Test
	    public void testResolveMediaTypeJSON() {
	        MediaTypeResolver resolver = new MediaTypeResolverImpl();
	        // test for JSON
	        assertThat(resolver.resolveMediaType(MediaType.APPLICATION_JSON), equalTo(MediaType.APPLICATION_JSON));
	        // test for XML
	        assertThat(resolver.resolveMediaType(MediaType.APPLICATION_XML), equalTo(MediaType.APPLICATION_XML));
	        // test for multiple media types - including JSON returns JSON
	        StringBuilder inputMediaTypes = new StringBuilder(MediaType.APPLICATION_XML);
	        inputMediaTypes.append(",");
	        inputMediaTypes.append("application/text");
	        inputMediaTypes.append(",");
	        inputMediaTypes.append("application/html");
	        inputMediaTypes.append(",");
	        inputMediaTypes.append(MediaType.APPLICATION_JSON);
	        assertThat(resolver.resolveMediaType(inputMediaTypes.toString()), equalTo(MediaType.APPLICATION_JSON));
	        // test for */* media type returns JSON
	        assertThat(resolver.resolveMediaType("*/*"), equalTo(MediaType.APPLICATION_JSON));
	        // test mixed media types excluding JSON but including XML returns XML
	        inputMediaTypes = new StringBuilder(MediaType.APPLICATION_XML);
	        inputMediaTypes.append(",");
	        inputMediaTypes.append("application/text");
	        inputMediaTypes.append(",");
	        inputMediaTypes.append("application/html");
	        assertThat(resolver.resolveMediaType(inputMediaTypes.toString()), equalTo(MediaType.APPLICATION_XML));
	    }

	    public void testResolvedMediaTypeXML() {
	        MediaTypeResolver resolver = new MediaTypeResolverImpl();
	        // test for XML
	        assertThat(resolver.resolveMediaType(MediaType.APPLICATION_XML), equalTo(MediaType.APPLICATION_XML));
	    }

	    /*
	     * If no specific media type specified then default to JSON.
	     */
	    @Test
	    public void testResolveMediaTypeAsterisk() {
	        MediaTypeResolver resolver = new MediaTypeResolverImpl();
	        // test for */* media type returns JSON
	        assertThat(resolver.resolveMediaType("*/*"), equalTo(MediaType.APPLICATION_JSON));
	    }

	    /*
	     * If multiple media types include JSON then return JSON in preference to others.
	     */
	    @Test
	    public void test_Resolve_Media_Type_Multi_Includes_JSON() {
	        MediaTypeResolver resolver = new MediaTypeResolverImpl();
	        // test for multiple media types - including JSON returns JSON
	        StringBuilder inputMediaTypes = new StringBuilder(MediaType.APPLICATION_XML);
	        inputMediaTypes.append(",");
	        inputMediaTypes.append("application/text");
	        inputMediaTypes.append(",");
	        inputMediaTypes.append("application/html");
	        inputMediaTypes.append(",");
	        inputMediaTypes.append(MediaType.APPLICATION_JSON);
	        assertThat(resolver.resolveMediaType(inputMediaTypes.toString()), equalTo(MediaType.APPLICATION_JSON));
	    }

	    /*
	     * If multiple media types don't include JSON but includes XML then return XML in preference to others.
	     */
	    @Test
	    public void test_Resolve_Media_Type_Multi_Excludes_JSON_Includes_XML() {
	        MediaTypeResolver resolver = new MediaTypeResolverImpl();
	        // test for multiple media types - including JSON returns JSON
	        StringBuilder inputMediaTypes = new StringBuilder(MediaType.APPLICATION_XML);
	        inputMediaTypes.append(",");
	        inputMediaTypes.append("application/text");
	        inputMediaTypes.append(",");
	        inputMediaTypes.append("application/html");
	        inputMediaTypes.append(",");
	        inputMediaTypes.append(MediaType.APPLICATION_JSON);
	        assertThat(resolver.resolveMediaType(inputMediaTypes.toString()), equalTo(MediaType.APPLICATION_JSON));
	    }

}
