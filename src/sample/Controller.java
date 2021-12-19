package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.Serializable;

public class Controller implements Serializable {

    @FXML private TableColumn<Contact, String> lastName;
    @FXML private TableColumn<Contact, String> firstName;
    @FXML private TableColumn<Contact, String > phoneNumber;
    @FXML private TextField searchField;
    @FXML private TableView <Contact> contactsTable;

    private final ObservableList<Contact> dataList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        init();
        firstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        search();

    }

    @FXML
    void deleteContact() {
        Contact contact = contactsTable.getSelectionModel().getSelectedItem();
        dataList.remove(contact);
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
        contactsTable.setItems(sortedList);
    }

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

}
