package jhc.figaro.api.persistence.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Person details entity that maps to the AS400 PERSON table.
 * 
 * @author Chris Howe-Jones
 * 
 */
@Entity
@Table(name = "PERSON")
@NamedQueries({
        @NamedQuery(name = "searchByName", query = "FROM PersonDetails WHERE name like '%' + :searchTerm + '%'"),
        @NamedQuery(name = "countSearchByName",
                query = "SELECT count(p) FROM PersonDetails p WHERE p.name like '%' + :searchTerm + '%'") })
public class PersonDetails implements Serializable {

    /**
     * Column length of the Person Code.
     */
    public static final int PERSON_CODE_LENGTH = 11;

    /**
     * Generated version UID.
     */
    private static final long serialVersionUID = 7289261918421400599L;

    /**
     * Primary key for the Person.
     */
    @Column(name = "UECODE", length = PERSON_CODE_LENGTH, columnDefinition = "CHAR(11)")
    @Id
    private String personCode;

    /**
     * Name of the Person.
     */
    @Column(name = "UENAME")
    private String name;

    /**
     * Email address of Person.
     */
    @Column(name = "UEEML")
    private String email;

    // TODO implement this properly
    @Transient
    private List<Address> addresses;

    /**
     * Gets the identifying person code.
     * 
     * @return identifier of the Person.
     */
    public final String getPersonCode() {
        return personCode;
    }

    /**
     * Sets the identifying person code.
     * 
     * @param personCodeString
     *            - code that identifies this Person.
     */
    public final void setPersonCode(final String personCodeString) {
        personCode = personCodeString;
    }

    /**
     * Get the Person's name.
     * 
     * @return name of the Person.
     */
    public final String getName() {
        String returnedName = name;
        if (returnedName != null) {
            returnedName = returnedName.trim();
        }
        return returnedName;
    }

    /**
     * Set the Person's name.
     * 
     * @param nameString
     *            - name of the person
     */
    public final void setName(final String nameString) {
        name = nameString;
    }

    /**
     * Get the Person's email address.
     * 
     * @return email address of the Person.
     */
    public final String getEmail() {
        String returnedEmail = email;
        if (returnedEmail != null) {
            returnedEmail = returnedEmail.trim();
        }
        return returnedEmail;
    }

    /**
     * Set the email address of the Person.
     * 
     * @param emailString
     *            - Person's email address.
     */
    public final void setEmail(final String emailString) {
        email = emailString;
    }

    /**
     * @return the addresses
     */
    public final List<Address> getAddresses() {
        return addresses;
    }

    /**
     * @param addresses
     *            the addresses to set
     */
    public final void setAddresses(final List<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        int hashCode = super.hashCode();
        if (personCode != null) {
            hashCode = personCode.hashCode();
        }
        result = prime * result + hashCode;
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PersonDetails other = (PersonDetails) obj;
        if (personCode == null) {
            return false;
        } else if (!personCode.equals(other.personCode)) {
            return false;
        }
        return true;
    }

    @Override
    public final String toString() {
        return "PersonDetails [personCode=" + personCode + ", name=" + name + ", email=" + email + "]";
    }

}
