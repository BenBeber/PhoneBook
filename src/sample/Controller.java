package sample;

import javafx.application.Platform;
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

    @FXML private TableColumn<Contact, String> lastName;
    @FXML private TableColumn<Contact, String> firstName;
    @FXML private TableColumn<Contact, String > phoneNumber;
    @FXML private TextField searchField;
    @FXML private TableView <Contact> contactsTable;
    @FXML private Button addButton;
    @FXML private BorderPane mainPanel;
    @FXML private MenuItem saveAsMenuItem;
    @FXML private MenuItem loadItemMenu;

    private boolean isChanged;
    private ContactsList contactsList;

    @FXML
    public void initialize() {
        contactsList = new ContactsList();
        contactsList.addContact("Ilay", "Barness", "0524700");
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        isChanged = false;
        search();

    }

    @FXML
    void deleteContact() {
        Contact contact = contactsTable.getSelectionModel().getSelectedItem();
        if (contact == null) {
            return;
        }
        Optional<ButtonType> selected = showAlert(Alert.AlertType.CONFIRMATION,"Deleting Contact",null,"Are you sure you want to delete " + contact);
        if (selected.get() == ButtonType.OK) {
            contactsList.removeContact(contact);
            isChanged = true;
        }
    }

    @FXML
    void contactHandler(ActionEvent event) throws IOException {
        Contact contact;
        String title;
        if (event.getSource().equals(addButton) ) {
            contact = null;
            title = "Adding new Contact";
        }
        else {
            contact = contactsTable.getSelectionModel().getSelectedItem();
            if (contact == null) {
                return;
            }
            title = "Update Contact";
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle(title);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ContactEdiot.fxml"));
        dialog.getDialogPane().setContent(loader.load());

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        ContactEditorController contactEditor = loader.getController();
        contactEditor.setDialog(contact);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
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


    private void search() {
        FilteredList<Contact> filteredData = new FilteredList<Contact>(contactsList.getContactsList(), b -> true);
        searchField.textProperty().addListener((observable ,oldValue,newValue ) -> filteredData.setPredicate(contact -> {
            if(newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();

            if (contact.getFirstName().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                return true;
            }else if (contact.getLastName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true;
            }else {
                return false;
            }
        }));

        SortedList<Contact> sortedList = new SortedList<Contact>(filteredData);
        sortedList.comparatorProperty().bind(contactsTable.comparatorProperty());
        contactsTable.getSortOrder().add(firstName);
        contactsTable.setItems(sortedList);

    }

    @FXML
    private void saveContactsList(ActionEvent event) {
        boolean saveToNewFile = event.getSource().equals(saveAsMenuItem) ? true:false;
        try {
            String path = contactsList.saveList(saveToNewFile);
            showAlert(Alert.AlertType.INFORMATION, "File Save","Contacts list saved successfully","file saved: "+path);
            isChanged = false;
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "ERROR","Could not open or save the file","file is not saved");
        } catch (NullPointerException e) {
        }
    }

    @FXML
    private void close() {
        Stage stage = (Stage)mainPanel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void loadFile(ActionEvent event) {
        boolean loadNewList= event.getSource().equals(loadItemMenu) ? true:false;
        try {
            contactsList.loadContactListFromFile(loadNewList);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Loading Error Occurred",null,"failed or interrupted I/O operations");
        } catch (ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR,"Loading Error Occurred",null,"loading the class failure");
        } catch (NullPointerException e) {

        }
    }


    /*  NOT FINISHED */
    public void onExit() {
        if (!isChanged) {
            Platform.exit();
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        ButtonType buttonSave = new ButtonType("Save and Quit");
        ButtonType buttonQuit = new ButtonType("Quit");
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonSave,buttonQuit,buttonCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(buttonSave)) {
            try {
                contactsList.saveList(false);
            } catch (IOException e) {
            } catch (NullPointerException e) {}


        } else if (result.get().equals(buttonQuit)) {
            close();
        }

    }


    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        return alert.showAndWait();
    }
}
