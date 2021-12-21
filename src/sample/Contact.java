package sample;

import java.io.Serializable;

public class Contact implements Serializable, Comparable {
    private String firstName;
    private String lastName;
    private PhoneNumber phoneNumber;

    public Contact(String firstName, String lastName, String phoneNumber) throws IllegalArgumentException {
        if ( firstName.isEmpty() && lastName.isEmpty() ) {
            throw new IllegalArgumentException("New Contact must have first or last name.");
        }
        if ( firstName == null || lastName == null ) {
            throw new IllegalArgumentException("Name or ");
        }
        try {
            this.phoneNumber = new PhoneNumber(phoneNumber);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (NullPointerException e) {
            throw e;
        }

        this.firstName = firstName;
        this.lastName = lastName;

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber.getPhoneNumber();
    }

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
        if (this.firstName.compareTo(other.firstName) == 0) {
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
        if (this.firstName.equals(other.firstName) && this.lastName.equals(other.lastName) && this.getPhoneNumber().equals(other.getPhoneNumber())) {
            return true;
        }
        return false;
    }
}