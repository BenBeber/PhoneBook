package sample;

import java.util.ArrayList;
import java.util.Iterator;

public class ContactsList {
    private ArrayList<Contact> contactsList;

    public ContactsList(ArrayList<Contact> contactsList) {
        this.contactsList = contactsList;
    }

    public boolean addContact(Contact contact) {
        return true;
    }

    public boolean removeContact(Contact contact) {
        return contactsList.remove(contact);
    }

    public boolean updateContact(Contact contact) {
        return true;
    }

    public Iterator getIterator() {
        return contactsList.iterator();
    }
}
