package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.Iterator;

public class ContactsList {
    private ObservableList<Contact> contactsList;


    public ContactsList() {
        this.contactsList = FXCollections.observableArrayList();
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

    public boolean addContact(String firstName, String lastName, String phoneNumber) {
        try {
            addContact(new Contact(firstName, lastName, phoneNumber));
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean removeContact(Contact contact) {
        return contactsList.remove(contact);
    }

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

    public Iterator getIterator() {
        return contactsList.iterator();
    }

    public boolean exportListToFile() {
        File file = getFile();

        try {
            FileOutputStream fileOutput = new FileOutputStream(file);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeObject(contactsList);
            objectOutput.close();
            fileOutput.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean loadContactListFromFile () {
        File file = getFile1();

        if (file == null) {
            return false;
        }
        try {
            FileInputStream fileInput = new FileInputStream(file);
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);
            this.contactsList = (ObservableList<Contact>)objectInput.readObject();
            return true;
        }catch (IOException e) {
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private File getFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please select a file");
        fileChooser.setInitialDirectory(new File("."));
        return fileChooser.showSaveDialog(null);
    }
    private File getFile1() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please select a file");
        fileChooser.setInitialDirectory(new File("."));
        return fileChooser.showOpenDialog(null);
    }
}
