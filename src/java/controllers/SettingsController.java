package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import models.Client;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class SettingsController {

    MainController mainController;
    @FXML private TextField saveAddress;
    @FXML private TextField fileName;
    @FXML private Label errorMessage;
    @FXML private PasswordField oldPassword;
    @FXML private PasswordField newPassword;
    @FXML private PasswordField newPassword2;
    @FXML private Label msgLabel;

    File selectedDirectory;
    Client client =Client.getInstance();

    public SettingsController() throws IOException {
    }

    public void darkButton() throws IOException {
        mainController.setTheme("dark");
        saveSettings("dark");
    }

    public void lightButton() throws IOException {
        mainController.setTheme("light");
        saveSettings("light");

    }
    public void saveSettings(String theme) throws IOException {
        Path themeFile = Path.of("settings/theme_setting.txt");
        if(!Files.exists(themeFile)){
            File file = themeFile.toFile();
            file.createNewFile();
        }
        Files.writeString(themeFile, theme);
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

    public void saveBackup() throws IOException {

        if (saveAddress.getText().trim().equals("") || fileName.getText().trim().equals("")){
            errorMessage.setText("Enter a name and file path");
        }

        var source = new File("./././backups/backup_" + client.getUser().getUsername() + ".cuppa");
        var dest = new File(saveAddress.getText() + "\\" + fileName.getText() + ".cuppa");

        if(source.equals("") || dest.equals("")){
            errorMessage.setText("Please choose a location and filename.");
            return;
        }

        try {
            if (dest.createNewFile()) {
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
        catch(IOException ex){
            errorMessage.setText(ex.getMessage());
        }
    }


    public void changePassword() throws IOException {
        String oldPass = oldPassword.getText();
        String newPass = newPassword.getText();
        String newPass2 = newPassword2.getText();
        if (newPassword.getText().length() < 8) {
            msgLabel.setText("Password must be at least 8 characters.");
            return;
        }
        else{
            msgLabel.setText("");
        }

        if (!newPass.equals(newPass2)) {
            msgLabel.setText("Passwords do not match.");
        } else {
            msgLabel.setText("");
            client.changePassword(oldPass, newPass);
        }
    }

    public void setPasswordChangeResult(String changed){
        if(changed.equals("success")){
            msgLabel.setText("Password successfully changed.");
            oldPassword.setText("");
            newPassword.setText("");
            newPassword2.setText("");
        }
        else{
            msgLabel.setText("Old password incorrect.");
        }
    }

}
