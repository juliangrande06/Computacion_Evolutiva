package Evolutiva;
import javax.swing.*;
import java.io.File;

public class FileChooser {
    File selectedFile;
    JFileChooser chooser;

    public FileChooser(){
        chooser = new JFileChooser();
    }
    public String run(){
        chooser.showOpenDialog(null);
        selectedFile = chooser.getSelectedFile();
        System.out.println(selectedFile);
        return selectedFile.getAbsolutePath();
    }
}