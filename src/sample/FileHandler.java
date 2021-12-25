package sample;

import javafx.stage.FileChooser;

import java.io.File;

public  class FileHandler {

    public static File getFile(ContactsList.FileMode mode) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please select a file");
        fileChooser.setInitialDirectory(new File("."));
        if (mode.equals(ContactsList.FileMode.SAVE)) {
            return fileChooser.showSaveDialog(null);
        }
        return fileChooser.showOpenDialog(null);
    }
}
