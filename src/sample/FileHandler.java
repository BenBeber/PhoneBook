package sample;

import javafx.stage.FileChooser;

import java.io.File;

public class FileHandler {


    public File getFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please select file");
        fileChooser.setInitialDirectory(new File("."));
        return fileChooser.showOpenDialog(null);
    }
}
