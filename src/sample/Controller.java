package sample;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class Controller {

    /*      table columns      */
    @FXML private TableColumn<Contact, String> lastName;
    @FXML private TableColumn<Contact, String> firstName;
    @FXML private TableColumn<Contact, String > phoneNumber;
    /*      table view      */
    @FXML private TableView <Contact> contactsTable;
    @FXML private TextField searchField;    //search bar
    @FXML private Button addButton;         //add new contact button
    @FXML private BorderPane mainPanel;
    @FXML private MenuItem saveAsMenuItem;  //save as menu button used to save to a new file
    @FXML private MenuItem loadItemMenu;    //load menu button

    private boolean isChanged;              //flag that keeps up if changes were made to the contact list
    private ContactsList contactsList;      //Data structure


    @FXML
    public void initialize() {
        contactsList = new ContactsList();
        contactsList.addContact("Ilay", "Barness", "0524700"); //DEBUG
        /*      binding contacts to the table columns      */
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        isChanged = false;
        search();       //search contact
    }

    /**
     * handels deletion of contact from the table/list
     * validate user choice by getting confirmation
     */
    @FXML
    void deleteContact() {
        Contact contact = contactsTable.getSelectionModel().getSelectedItem();
        Optional<ButtonType> selected = showAlert(Alert.AlertType.CONFIRMATION,"Deleting Contact",null,"Are you sure you want to delete " + contact);
        if (selected.get() == ButtonType.OK) {
            if(contactsList.removeContact(contact)) {
                isChanged = true;
            }
        }
    }

    /**
     * handels editing or adding contact in a new window
     */
    @FXML
    void contactHandler(ActionEvent event) throws IOException {
        Contact contact;
        String title;       //the title of the dialog
        /* new contact */
        if (event.getSource().equals(addButton) ) {
            contact = null;
            title = "Adding new Contact";
        }
        /* editing contact  */
        else {
            contact = contactsTable.getSelectionModel().getSelectedItem();
            if (contact == null) { //no contact was selected
                return;
            }
            title = "Update Contact";
        }
        /* add/edit window */
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle(title);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ContactEditor.fxml"));
        dialog.getDialogPane().setContent(loader.load());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        ContactEditorController contactEditor = loader.getController();
        contactEditor.setDialog(contact);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        /* values validation */
        okButton.addEventFilter(ActionEvent.ACTION, event1 -> {
            if (contactEditor.getContact() == null) {
                showAlert(Alert.AlertType.ERROR, "Illegal Values",null,"invalid first name/last name/phone number ");
                event1.consume();
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK ) {
            isChanged = true;
            if (contact == null) {
                contactsList.addContact(contactEditor.getContact());
            }
            contactsTable.refresh();
        }
    }

    /**
     * searching contact by his first or last name in contact table
     */
    private void search() {
        FilteredList<Contact> filteredData = new FilteredList<Contact>(contactsList.getContactsList(), b -> true);
        searchField.textProperty().addListener((observable ,oldValue,newValue ) -> filteredData.setPredicate(contact -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();

            return contact.getFirstName().toLowerCase().contains(lowerCaseFilter) || contact.getLastName().toLowerCase().contains(lowerCaseFilter);
        }));

        SortedList<Contact> sortedList = new SortedList<Contact>(filteredData);
        sortedList.comparatorProperty().bind(contactsTable.comparatorProperty());
        contactsTable.getSortOrder().add(firstName);
        contactsTable.setItems(sortedList);
    }

    /**
     * saving the the current contact list either in current exist file or a new file
     */
    @FXML
    private void saveContactsList(ActionEvent event) {
        boolean saveToNewFile = event.getSource().equals(saveAsMenuItem);
        try {
            String path = contactsList.saveList(saveToNewFile);
            showAlert(Alert.AlertType.INFORMATION, "File Save","Contacts list saved successfully",path);
            isChanged = false;
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "ERROR","Could not open or save the file","file is not saved");
        } catch (NullPointerException e) {
        }
    }

    /**
     * used for closing the app from the menu
     */
    @FXML
    private void exitApplication() {
        if(onExit()) {
            Stage stage = (Stage)mainPanel.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * loading new contact list or add contacts from a file
     */
    @FXML
    private void loadFile(ActionEvent event) {
        boolean loadNewList= event.getSource().equals(loadItemMenu); //load new list or adding from a file
        try {
            contactsList.loadContactListFromFile(loadNewList);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error While Loading Occurred",null,"failed or interrupted I/O operations");
        } catch (ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR,"Error While Loading Occurred",null,"loading the class failure");
        } catch (NullPointerException e) {
        }
    }

    /**
     * if there is unsaved changes displays a warning to the user
     * so the user could either cancel/Quit/Save and quit
     *
     * @return true if user chose quit the app
     */
    public boolean onExit() {
        if (!isChanged) {
            return true;  //not changes
        }
        /* new alert */
        Alert alert = new Alert(Alert.AlertType.WARNING);
        ButtonType buttonSave = new ButtonType("Save and Quit");
        ButtonType buttonQuit = new ButtonType("Quit", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType buttonCancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(buttonSave,buttonQuit,buttonCancel);
        /*      ADD TEXT HERE DONT FORGET     */
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(buttonSave)) {
            try {
                contactsList.saveList(false);
                return true;
            } catch (IOException e) {
            } catch (NullPointerException e) {}
        }
        else return !result.get().equals(buttonCancel);
        return true;
    }

    /**
     * the method used for creating and displaying message to the user
     * @param alertType     dialog type
     * @param title         dialog title
     * @param headerText    header text
     * @param contentText   content
     * @return              user selection
     */
    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        return alert.showAndWait();
    }
}
