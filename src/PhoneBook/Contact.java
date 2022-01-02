package PhoneBook;

import java.io.Serializable;

/**
 * this class represent contact and validate first and last name
 */
public class Contact implements Serializable, Comparable {
    private String firstName;
    private String lastName;
    private final PhoneNumber phoneNumber;

    /**
     * creating new contact
     * @param firstName first name
     * @param lastName last name
     * @param phoneNumber phone number
     */
    public Contact(String firstName, String lastName, String phoneNumber)  {
        this.phoneNumber = new PhoneNumber(phoneNumber);
        setFirstName(firstName.trim());
        setLastName(lastName.trim());
    }

    /**
     * getter for the first name
     * @return contact first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * setter for the first name
     * @param firstName to set
     */
    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.firstName = firstName;
    }

    /**
     * getter for the last name
     * @return contact last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * setter for the last name
     * @param lastName to set
     */
    public void setLastName(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.lastName = lastName;
    }

    /**
     * getter for the phone number
     * @return the contacts first number
     */
    public String getPhoneNumber() {
        return phoneNumber.getPhoneNumber();
    }

    /**
     * setter for the phone number
     * @param phoneNumber to set
     */
    public void setPhoneNumber(String  phoneNumber) {
        this.phoneNumber.setPhoneNumber(phoneNumber);
    }

    @Override
    public String toString() {
        return firstName+" "+lastName+" "+phoneNumber.getPhoneNumber();
    }

    @Override
    public int compareTo(Object object) {
        if (! (object instanceof Contact) ) {
            return 0;
        }
        Contact other = (Contact) object;
        if (this.firstName.compareTo(other.firstName) != 0) {
            return this.firstName.compareTo(other.firstName);
        }
        return this.lastName.compareTo(other.lastName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Contact) ) {
            return false;
        }
        Contact other = (Contact)obj;
        return this.getFirstName().equals(other.getFirstName()) && this.getLastName().equals(other.getLastName());
    }
}