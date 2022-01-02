package PhoneBook;

import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * this class responsible file utilization
 */
public class FileHandler {
    private File file;
    private String filePath;
    enum FileMode {SAVE ,LOAD} //custom enum for file chooser mode

    /**
     * constructor to set the initial loading of the last used file
     */
    public FileHandler() {
        setInitialFilePath();
        this.file = new File(filePath);
    }

    /**
     * the current file getter
     * @return the file that we currently using
     */
    public File getFile() {
        return file;
    }

    /**
     * selecting a file to load from/save to
     * @param mode the file chooser mode
     * @return selected file
     */
    public File fileSelection (FileMode mode) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please select a file");
        fileChooser.setInitialDirectory(new File("."));
        if (mode.equals(FileMode.SAVE)) {
            return fileChooser.showSaveDialog(null);
        }
        return fileChooser.showOpenDialog(null);
    }

    /**
     * setting the current working file
     * @param file to set
     */
    public void setFile(File file) {
        if (file == null) {
            return;
        }
        this.file = file;
    }

    /**
     * the initial load
     * reads the last used file path to load from
     */
    private void setInitialFilePath() {
        try {
            Scanner s = new Scanner(new File(".\\util\\filePath.txt"));
            this.filePath = s.next();
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * saving the latest used file path
     */
    public void setFilePath()  {
        try {
            FileWriter fileWriter = new FileWriter(new File(".\\util\\filePath.txt"));
            fileWriter.write(file.getPath());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
