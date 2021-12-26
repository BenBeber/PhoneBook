package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContactsList {
    private ObservableList<Contact> contactsList;
    private File file;
    enum FileMode {SAVE ,LOAD}
    //add list has changed boolean

    public ContactsList() {
        contactsList = FXCollections.observableArrayList();
    }

    public ObservableList<Contact> getContactsList() {
        return contactsList;
    }

    public void addContact(Contact contact) {
        if (contactsList.contains(contact)) {
            return;
        }
        contactsList.add(contact);
    }
//delete!!!!!!
    public void addContact(String firstName, String lastName, String phoneNumber) {
        addContact(new Contact(firstName, lastName, phoneNumber));
    }
//change to void
    public boolean removeContact(Contact contact) {
        if (contact == null) {
            return false;
        }
        return contactsList.remove(contact);
    }
//delete!!!
    public boolean updateContact(Contact contact, String firstName, String lastName, String phoneNumber) {
        try {
            contact.setFirstName(firstName);
            contact.setLastName(lastName);
            contact.setPhoneNumber(phoneNumber);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String saveList(boolean saveToNewFile) throws IOException {
        if (file == null || saveToNewFile) {
            file = FileHandler.getFile(FileMode.SAVE);
        }
        FileOutputStream fileOutput = new FileOutputStream(file);
        ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
        objectOutput.writeObject(new ArrayList<>(contactsList));
        objectOutput.close();
        fileOutput.close();
        return file.getAbsolutePath();
    }

    public void loadContactListFromFile (boolean loadNewList) throws IOException, ClassNotFoundException {
        File newFile = FileHandler.getFile(FileMode.LOAD);
        if (loadNewList) {
            file = newFile;
            contactsList.clear();           //clear the list in the end to make sure no data has lost
        }
        if (newFile == null) {
            throw new NullPointerException();
        }
        FileInputStream fileInput = new FileInputStream(newFile);
        ObjectInputStream objectInput = new ObjectInputStream(fileInput);
        List wrapper = (List<Contact>) objectInput.readObject();
        for (Object object : wrapper) { //change if statment to Contact
            if (!(object instanceof Contact)) { //delete!!!! if statment
                throw new IllegalArgumentException();
            }
            addContact((Contact) object);
        }
            objectInput.close();
            fileInput.close();
    }
}
