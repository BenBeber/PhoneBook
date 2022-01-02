package PhoneBook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * the data structure of the contacts list
 */
public class ContactsList {
    private final ObservableList<Contact> contactsList;

    /**
     * constructor initial the list
     */
    public ContactsList() {
        contactsList = FXCollections.observableArrayList();
    }

    /**
     * getter for the list
     * @return the contacts list
     */
    public ObservableList<Contact> getContactsList() {
        return contactsList;
    }

    public void addContact(Contact contact) {
        if (contactsList.contains(contact)) {
            return;
        }
        contactsList.add(contact);
    }

    public boolean removeContact(Contact contact) {
        return contactsList.remove(contact);
    }

    public String saveList(File file) throws IOException {
        FileOutputStream fileOutput = new FileOutputStream(file);
        ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
        objectOutput.writeObject(new ArrayList<>(contactsList));
        objectOutput.close();
        fileOutput.close();
        return file.getPath();
    }

    public void loadContactListFromFile (boolean loadNewList, File file) throws IOException, ClassNotFoundException {
        if (file == null) {
            throw new NullPointerException("No file has been Chosen");
        }
        if (loadNewList) {
            contactsList.clear();
        }
        FileInputStream fileInput = new FileInputStream(file);
        ObjectInputStream objectInput = new ObjectInputStream(fileInput);
        List wrapper = (List<Contact>) objectInput.readObject();
        for (Object object : wrapper) {
            if (!(object instanceof Contact)) {
                throw new IllegalArgumentException();
            }
            addContact((Contact) object);
        }
            objectInput.close();
            fileInput.close();
    }

}
