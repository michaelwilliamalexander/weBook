/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.Home;


import com.mycompany.rplproject.Home.SettingController;
import com.mycompany.rplproject.Home.HomeController;
import com.mycompany.rplproject.User;
import com.mycompany.rplproject.db.DBUtil;
import com.mycompany.rplproject.db.UserDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Michael William
 */
public class InsertNewPasswordController implements Initializable {
    private double x,y;
    private String data;
    private String email = null, password = null;
    List<Integer> folderTree = new ArrayList<>();
    private User now;
    @FXML
    private PasswordField inPas;
    
    @FXML
    private PasswordField inPasConf;

    @FXML
    private Button btnConfirm;
    
    @FXML
    private Text namaAkun;
    
   @FXML
    void dragged(MouseEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }
    
    @FXML
    void tagList(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/TagList.fxml"));
        Parent tagListPage = loader.load();
        Scene tagList = new Scene(tagListPage);
        TagListController controller = loader.getController();
        controller.data(now);
        controller.show(false);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(tagList);
        app_stage.show();
    }
    
    @FXML
    public void move(MouseEvent event){
        x = event.getSceneX();
        y = event.getSceneY();
    }
    
    @FXML
    public void close(MouseEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void minimize(MouseEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
    
    public void backHome(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
        Parent backHomePage = loader.load();
        Scene backHome = new Scene(backHomePage);
        HomeController controller = loader.getController();
        controller.data(now);
        folderTree.add(0);
        controller.show(false,folderTree);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(backHome);
        app_stage.show();
    }
     public void data(User s){
        now = s;
        namaAkun.setText(now.getEmail());
    }
     @FXML
    public void logOut(MouseEvent event) throws IOException{
        Parent signOutPage = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene signOut = new Scene(signOutPage);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(signOut);
        app_stage.show();
    }
    public void backSetting(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Setting.fxml"));
        Parent backSettingPage = loader.load();
        Scene backSetting = new Scene(backSettingPage);
        SettingController controller = loader.getController();
        controller.data(now);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(backSetting);
        app_stage.show();
    }
    //Confirm Ganti password
    public void checkPass(MouseEvent event) throws ClassNotFoundException, IOException, SQLException{
        String pass = inPas.getText();
        String passConf = inPasConf.getText();
        if(!inPas.getText().equalsIgnoreCase(inPasConf.getText())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Konfirmasi Password Salah");
            alert.showAndWait();
            inPasConf.clear();
            inPas.clear();
        }
        else if(passwordLength(inPas.getText())==false && passwordLength(inPasConf.getText())==false){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Password min 8 digit");
            alert.showAndWait();
            inPasConf.clear();
            inPas.clear();
        }
        else if(checkPassword(inPas.getText())==false && checkPassword(inPasConf.getText())==false){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Password harus mengandung huruf besar ,kecil dan angka");
            alert.showAndWait();
            inPasConf.clear();
            inPas.clear();
        }
        else if(passConf.equals(pass)){
            UserDAO.updatePassword(pass, now);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/Setting.fxml"));
            Parent passwordConfirmedPage = loader.load();
            Scene passwordConfirmed = new Scene(passwordConfirmedPage);
            SettingController controller = loader.getController();
            controller.data(now);
            Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(passwordConfirmed);
            app_stage.show();
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Password dan Confirmation Password not match");
            alert.showAndWait();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public static boolean checkPassword(String password){
       boolean checkNum = false;
       boolean checkUp = false;
       boolean checkLow =false;
       char tempt;
       for(int i=0; i<password.length();i++){
           tempt = password.charAt(i);
           if(Character.isDigit(tempt)){
               checkNum = true;
           }
           else if(Character.isUpperCase(tempt)){
               checkUp = true;
           }
           else if(Character.isLowerCase(tempt)){
               checkLow = true;
           }
           if(checkNum==true&&checkUp==true&&checkLow ==true){
               return true;
            }
       }
       return false;
   }
    
    public static boolean passwordLength(String password){
       if(password.length()>7){
           return true;
       }
       return false;
   }
}
