package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContactsList {
    private ObservableList<Contact> contactsList = FXCollections.observableArrayList();
    File file;
    enum fileMode{SAVE , LOAD}

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

    public String exportListToFile() throws IOException {
        File file = getFile(fileMode.SAVE);

        FileOutputStream fileOutput = new FileOutputStream(file);
        ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
        objectOutput.writeObject(new ArrayList<Contact>(contactsList));
        objectOutput.close();
        fileOutput.close();
        return file.getAbsolutePath();
    }

    public String loadContactListFromFile () {
        File file = getFile(fileMode.LOAD);

        if (file == null) {
            return null;
        }
        try {
            FileInputStream fileInput = new FileInputStream(file);
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);
            List<Contact> list = (List<Contact>) objectInput.readObject();
            contactsList = FXCollections.observableList(list);
            return file.getAbsolutePath();
        }catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private File getFile(fileMode mode) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please select a file");
        fileChooser.setInitialDirectory(new File("."));
        if (mode == fileMode.SAVE) {
            return fileChooser.showSaveDialog(null);
        }
        return fileChooser.showOpenDialog(null);
    }

}
