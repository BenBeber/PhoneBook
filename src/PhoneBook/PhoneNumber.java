package PhoneBook;

import java.io.Serializable;

/**
 * this class used for phone number
 */
public class PhoneNumber implements Serializable {
    private String phoneNumber;

    /**
     * constructor for phone number
     * @param phoneNumber the phone number to add
     */
    public PhoneNumber(String phoneNumber) {
        setPhoneNumber(phoneNumber.trim());
    }

    /**
     * getter for phone number
     * @return this phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * sets the phone number
     * @param phoneNumber the phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null)
            throw new IllegalArgumentException("null is not accepted");
        if (isValidPhoneNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        }else throw new IllegalArgumentException("Not valid Phone number");
    }

    /**
     * checks if the given phone number is valid
     * @param phoneNumber the phone number to check
     * @return true if valid phone number
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\+?(?:\\s*[\\d -]){3,12}\\s*");
    }

    @Override
    public String toString() {
        return phoneNumber;
    }
}
