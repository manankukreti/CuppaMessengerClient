package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import sample.Client;
import java.io.*;

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

    public void darkButton() {
        mainController.setTheme("dark");
    }

    public void lightButton(){
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
