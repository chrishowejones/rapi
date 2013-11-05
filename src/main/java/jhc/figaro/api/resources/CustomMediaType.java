package jhc.figaro.api.resources;

/**
 * Class detailing the custom media types for the Figaro RESTful API.
 * 
 * @author Chris Howe-Jones
 * 
 */
public final class CustomMediaType {

    /**
     * Custom media type String value for People + JSON.
     */
    public static final String APPLICATION_PEOPLE = "application/vnd.figaro.people+json";

    /**
     * Custom media type for People + JSON.
     */
    public static final CustomMediaType APPLICATION_PEOPLE_TYPE = new CustomMediaType(APPLICATION_PEOPLE);

    /**
     * Custom media type String value for People + XML.
     */
    public static final String APPLICATION_PEOPLE_XML = "application/vnd.figaro.people+xml";

    /**
     * Custom media type for People + XML.
     */
    public static final CustomMediaType APPLICATION_PEOPLE_XML_TYPE = new CustomMediaType(APPLICATION_PEOPLE_XML);

    private final String mediaTypeValue; // media type string representation

    /**
     * Returns a CustomMediaType for the specified string value.
     * 
     * @param mediaTypeStringValue
     *            - String value of the CustomMediaType
     * @return CustomMediaType for the value above.
     * @throws IllegalArgumentException
     *             if the String value doesn't match a valid CustomMediaType.
     */
    public static CustomMediaType valueOf(final String mediaTypeStringValue) throws IllegalArgumentException {
        if (APPLICATION_PEOPLE.equalsIgnoreCase(mediaTypeStringValue)) {
            return CustomMediaType.APPLICATION_PEOPLE_TYPE;
        }
        if (APPLICATION_PEOPLE_XML.equalsIgnoreCase(mediaTypeStringValue)) {
            return CustomMediaType.APPLICATION_PEOPLE_XML_TYPE;
        }
        throw new IllegalArgumentException(mediaTypeStringValue + " is not a valid CustomMediaType for Figaro API.");
    }

    /**
     * CustomMediaType constructor taking the String value of the type.
     * @param mediaTypeStringValue
     *            - types String value.
     */
    CustomMediaType(final String mediaTypeStringValue) {
        this.mediaTypeValue = mediaTypeStringValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return this.mediaTypeValue;
    }

}
