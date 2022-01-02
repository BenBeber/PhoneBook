package PhoneBook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * @author Ben
 * @version final
 * this is my implementation for phone book in Q2 of maman14
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ContactList.fxml"));
        Parent root = loader.load();
        PhoneBookController controller = loader.getController();
        primaryStage.setTitle("Contact Book");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {   //saving before quiting
            if (!(controller.onExit())) {
                event.consume();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
