package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;

public class Controller implements Serializable {

    @FXML private TableColumn<Contact, String> lastName;
    @FXML private TableColumn<Contact, String> firstName;
    @FXML private TableColumn<Contact, String > phoneNumber;
    @FXML private TextField searchField;
    @FXML private TableView <Contact> contactsTable;

    private final ObservableList<Contact> dataList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        init(); //debug
        firstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
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
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setTitle("Deleting Contact");
        deleteAlert.setHeaderText("Are you sure you want to delete " + contact);
        deleteAlert.setHeaderText(null);
        deleteAlert.setContentText("The contact " + contact + "will be permanently deleted");

        if (deleteAlert.showAndWait().get() == ButtonType.OK) {
            dataList.remove(contact);
        }


    }

    @FXML
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

}
