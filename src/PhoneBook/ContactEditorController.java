package PhoneBook;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ContactEditorController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneNumberField;

    private Contact contact;

    /**
     * setting the dialog parameters
     * @param contact if editing contact sets the fields with the contact info
     */
    public void setDialog (Contact contact) {
        this.contact = contact;
        setTextFields();
    }

    /**
     * setting the contact information in case of editing
     * in case of adding leaving fields empty
     */
    private void setTextFields() {
        if (contact != null) {
            firstNameField.setText(contact.getFirstName());
            lastNameField.setText(contact.getLastName());
            phoneNumberField.setText(contact.getPhoneNumber());
        }
    }

    /**
     * edit/add contact if valid returns the new contact
     * @return the new contact to add or after edit
     */
    public Contact getContact() {
        try {
            if (contact == null) {
                contact = new Contact(firstNameField.getText(), lastNameField.getText(), phoneNumberField.getText());
            }else {
                contact.setPhoneNumber(phoneNumberField.getText());
                contact.setFirstName(firstNameField.getText());
                contact.setLastName(lastNameField.getText());
            }
        }catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
        return contact; //valid
    }
}
