package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.util.Optional;

public class Controller implements Serializable {

    @FXML private TableColumn<Contact, String> lastName;
    @FXML private TableColumn<Contact, String> firstName;
    @FXML private TableColumn<Contact, String > phoneNumber;
    @FXML private TextField searchField;
    @FXML private TableView <Contact> contactsTable;
    @FXML private Button addButton;
    @FXML private Button editButton;

    private final ObservableList<Contact> dataList = FXCollections.observableArrayList();
    private Contact contact;

    enum DialogMode{ADD , UPDATE}


    @FXML
    public void initialize() {
        init(); //debug
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        search();

    }

    @FXML
    void deleteContact() {
        Contact contact = contactsTable.getSelectionModel().getSelectedItem();
        if (contact == null) {
            return;
        }
        Optional<ButtonType> selected = showConfimationDialog("Deleting Contact",null,"Are you sure you want to delete " + contact);
        if (selected.get() == ButtonType.OK) {
            dataList.remove(contact);
        }
    }

    @FXML
    void contactHandler(ActionEvent event) {
        Contact contact = null;
        DialogMode mode;
        String dialogTitle;
        if (event.getSource().equals(editButton)) {
            mode = DialogMode.UPDATE;
            contact = contactsTable.getSelectionModel().getSelectedItem();
            dialogTitle = "Update contact";
            if (contact == null) {
                return;
            }
        } else {
                mode = DialogMode.ADD;
                dialogTitle = "adding new Student";
        }


        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ContactEdiot.fxml"));
            DialogPane contactDialogPane = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(contactDialogPane);
            dialog.setTitle(dialogTitle);
            Optional<ButtonType> userChoise = dialog.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }


        Dialog<ButtonType> dialog = new Dialog<>();
    }


    private void search() {
        FilteredList<Contact> filteredData = new FilteredList<Contact>(dataList, b -> true);
        searchField.textProperty().addListener((observable ,oldValue,newValue ) -> {
            filteredData.setPredicate(contact -> {
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
            });
        });

        SortedList<Contact> sortedList = new SortedList<Contact>(filteredData);
        sortedList.comparatorProperty().bind(contactsTable.comparatorProperty());
        contactsTable.getSortOrder().add(firstName);
        contactsTable.sort();
        contactsTable.setItems(sortedList);

    }

    @FXML
    private void SaveFile() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
    }

    /*  NOT FINISHED */
    public static void onExit(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType buttonSave = new ButtonType("Save and Quit");
        ButtonType buttonQuit = new ButtonType("Quit");


    }

    private void setContact(Contact contact){
        this.contact = contact;
    }


    /* DEBUG */
    private void init() {
        Contact c1 = new Contact("Ben","Barness","1");
        Contact c2 = new Contact("David","D","2");
        Contact c3 = new Contact("Gamma","G","3");
        Contact c4 = new Contact("Delta","D","4");

        dataList.add(c1);
        dataList.add(c2);
        dataList.add(c3);
        dataList.add(c4);
        dataList.add(new Contact("aa","aa","1"));
    }


    private boolean saveFile() {
        File file = getFile();

        try (FileOutputStream fileOutput = new FileOutputStream(file)) {
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeObject(dataList);
            objectOutput.close();
            fileOutput.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private File getFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        return fileChooser.showOpenDialog(null);
    }

    private Optional<ButtonType> showConfimationDialog(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        return alert.showAndWait();
    }


}
