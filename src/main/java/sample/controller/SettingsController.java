package sample.controller;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Client;
import sample.controller.Conversation.ConversationsController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SettingsController {

    MainController mainController;
    @FXML private TextField saveAddress;
    @FXML private TextField restoreAddress;
    @FXML private TextField fileName;
    @FXML private Label errorMessage;
    File selectedDirectory;
    File selectedFile;
    Client client =Client.getInstance();

    public SettingsController() throws IOException {
    }

    public void darkButton() throws IOException {
        mainController.setTheme("dark");
    }

    public void lightButton() throws IOException {
        mainController.setTheme("light");
    }

    public void setMainController(MainController controller){
        mainController = controller;
    }

    public void setSaveLocation() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = new Stage();
        selectedDirectory = directoryChooser.showDialog(stage);
        saveAddress.setText(selectedDirectory.getAbsolutePath());
    }
    public void setRestoreLocation() {

        FileChooser directoryChooser = new FileChooser();
        Stage stage = new Stage();
        selectedFile = directoryChooser.showOpenDialog(stage);
        restoreAddress.setText(selectedFile.getAbsolutePath());
    }

    public void saveBackup() throws IOException {

        if (saveAddress.getText().trim().equals("") || fileName.getText().trim().equals("")){
            errorMessage.setText("Enter a name and file path");
        }

        var source = new File("./././backups/backup_" + client.getUser().getUsername() + ".cuppa");
        var dest = new File(saveAddress.getText() + "\\" + fileName.getText() + ".cuppa");

        if (dest.createNewFile())
        {
            try (var fis = new FileInputStream(source);
                 var fos = new FileOutputStream(dest)) {

                byte[] buffer = new byte[1024];
                int length;

                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            errorMessage.setText("Backup created at " + saveAddress.getText() + "\\" + fileName.getText() + ".cuppa");
        } else {
            errorMessage.setText("File already exists.");
        }

    }

    public void restoreBackup() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainPage/conversations/conversations.fxml"));
        loader.load();
        ConversationsController controller = loader.getController();
        controller.loadConvoFromFile(restoreAddress.getText());

        FXMLLoader loader1 = new FXMLLoader();
        loader1.setLocation(getClass().getResource("/mainPage/mainPage.fxml"));
        loader1.load();
        MainController controller1 = loader1.getController();

    }
}
