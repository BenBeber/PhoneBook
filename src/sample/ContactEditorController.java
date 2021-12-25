package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ContactEditorController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneNumberField;

    private Contact contact;

    public void setDialog (Contact contact) {
        this.contact = contact;
        setTextFields();
    }

    private void setTextFields() {
        if (contact != null) {
            firstNameField.setText(contact.getFirstName());
            lastNameField.setText(contact.getLastName());
            phoneNumberField.setText(contact.getPhoneNumber());
        }
    }

    public Contact getContact() {
        try {
            if (contact == null) {
                contact = new Contact(firstNameField.getText(), lastNameField.getText(), phoneNumberField.getText());
            }else {
                contact.setFirstName(firstNameField.getText());
                contact.setLastName(lastNameField.getText());
                contact.setPhoneNumber(phoneNumberField.getText());
            }
        }catch (IllegalArgumentException e) {
            return null;
        }catch (NullPointerException e) {
            return null;
        }
        return contact;
    }
}
