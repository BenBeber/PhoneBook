package PhoneBook;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class PhoneBookController {
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
    private FileHandler fileHandler;


    @FXML
    public void initialize() {
        contactsList = new ContactsList();
        /*      binding contacts to the table columns      */
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        fileHandler = new FileHandler();
        try {
            contactsList.loadContactListFromFile(true ,fileHandler.getFile());
        } catch (IOException | ClassNotFoundException ignored) {
        }

        isChanged = false;
        search();  //active search
    }

    /**
     * handel delete of contact from the table/list
     * validate user choice by getting confirmation
     */
    @FXML
    void deleteContact() {
        Contact contact = contactsTable.getSelectionModel().getSelectedItem();
        if (contact == null) {
            return;
        }
        Optional<ButtonType> selected = showAlert(Alert.AlertType.CONFIRMATION,"Deleting Contact",null,"Are you sure you want to delete " + contact);
        if (selected.isPresent() && selected.get() == ButtonType.OK) {
            if(contactsList.removeContact(contact)) {
                isChanged = true;
            }
        }
    }

    /**
     * handel's editing or adding contact in a new window
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
            if (contact == null) {
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
     * creating about window for the application
     */
    @FXML
    void aboutDialog() {
        ButtonType backButton = new ButtonType("Back", ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(Alert.AlertType.NONE, "Phone Book",backButton);
        alert.setTitle("About");
        TextArea textArea = new TextArea("Made by Ben Beberashvili.\nThis application implements phone book. \n" +
                                        "My solution for MAMAN 14. \nHome assignment of Course - Java Advanced Programming in the Open University.\n" +
                                        "Thank you for using the app");

        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        GridPane.setVgrow(textArea , Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane aboutPane = new GridPane();

        aboutPane.setMaxWidth(Double.MAX_VALUE);
        aboutPane.add(textArea, 1,0);

        alert.getDialogPane().setContent(aboutPane);
        alert.showAndWait();
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
        if (event.getSource().equals(saveAsMenuItem)) {
            File newFile = fileHandler.fileSelection(FileHandler.FileMode.SAVE);
            if (newFile == null) {
                return;
            }else {
                fileHandler.setFile(newFile);
            }
        }
        try {
            String path = contactsList.saveList(fileHandler.getFile());
            showAlert(Alert.AlertType.INFORMATION, "File Save","Contacts list saved successfully",path);
            isChanged = false;
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "ERROR","Could not open or save the file","file is not saved");
        } catch (NullPointerException ignored) {
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
     * loading new contact list or adding contacts from a file
     */
    @FXML
    private void loadFile(ActionEvent event) {
        File fileToLoad = fileHandler.fileSelection(FileHandler.FileMode.LOAD);
        boolean loadNewList = false;
        if (event.getSource().equals(loadItemMenu)) {
            loadNewList = true;
            fileHandler.setFile(fileToLoad);
        }
        try {
            contactsList.loadContactListFromFile(loadNewList, fileToLoad);
            isChanged = true;
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error While Loading Occurred",null,"failed or interrupted I/O operations");
        } catch (ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR,"Error While Loading Occurred",null,"loading the class failure");
        } catch (NullPointerException ignored) {
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
            return true;  //no changes
        }
        /* new alert */
        Alert alert = new Alert(Alert.AlertType.WARNING);
        ButtonType buttonSave = new ButtonType("Save and Quit");
        ButtonType buttonQuit = new ButtonType("Quit");
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonSave,buttonQuit,buttonCancel);

        alert.setTitle("Exit");
        alert.setHeaderText("Changes were made to the contacts list, any unsaved changes will be lost.");
        alert.setContentText("are you sure you want to Quit?\n\n");
        Optional<ButtonType> result = alert.showAndWait();
        fileHandler.setFilePath();
        if (result.isPresent() && result.get().equals(buttonSave)) {
            try {
                contactsList.saveList(fileHandler.getFile());
                return true;
            } catch (IOException | NullPointerException e) {
                showAlert(Alert.AlertType.ERROR,"File not Saved",null,"Problem occurred while saving the file");
                return false;
            }
        }
        else return !(result.isPresent() && result.get().equals(buttonCancel));
    }

    /**
     * the method used for creating and displaying message to the user
     * @param alertType     dialog type
     * @param title         dialog title
     * @param headerText    dialog header
     * @param contentText   dialog content
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
