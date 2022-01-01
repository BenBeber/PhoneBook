package sample;

import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileHandler {
    private File file;
    private String filePath;
    enum FileMode {SAVE ,LOAD}

    public FileHandler() {
        setInitialFilePath();
        this.file = new File(filePath);
        System.out.println(file.getPath());
        System.out.println(file.getName());
    }

    public File getFile() {
        return file;
    }

    public File fileSelection (FileMode mode) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please select a file");
        fileChooser.setInitialDirectory(new File("."));
        if (mode.equals(FileMode.SAVE)) {
            return fileChooser.showSaveDialog(null);
        }
        return fileChooser.showOpenDialog(null);
    }

    public void setFile(File file) {
        if (file == null) {
            return;
        }
        this.file = file;
    }

    private void setInitialFilePath() {
        try {
            Scanner s = new Scanner(new File(".\\util\\filePath.txt"));
            this.filePath = s.next();
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setFilePath() {
        try {
            FileWriter fileWriter = new FileWriter(new File(".\\util\\filePath.txt"));
            System.out.println(file.getPath());
            fileWriter.write(file.getPath());
            fileWriter.close();
        } catch (IOException e) {
        }
    }
}
