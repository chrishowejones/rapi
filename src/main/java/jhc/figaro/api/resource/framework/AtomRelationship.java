package jhc.figaro.api.resource.framework;

/**
 * Enum describing AtomRelationships.
 * @author Chris Howe-Jones
 * 
 */
public enum AtomRelationship {

    /**
     * Atom relationships.
     */
    SELF, NEXT, PREV, ADDRESSES;

    /**
     * Returns the {@link AtomRelationship} enum for the given {@link String} value.
     * 
     * @param value
     * @return AtomRelationship.
     */
    public static AtomRelationship fromString(String value) {

        try {
            return AtomRelationship.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Invalid value '%s' for relationship given!", value), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

}
