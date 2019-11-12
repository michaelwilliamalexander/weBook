/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.LoginNSignUp;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import com.mycompany.rplproject.db.DBUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import javafx.scene.input.MouseEvent;



public class SignUpFXMLController implements Initializable {
   @FXML
   private TextField newEmailInput = new TextField();
   @FXML
   private PasswordField newPasswordInput;
   @FXML
   private PasswordField rePasswordInput;
   @FXML
   private Button backLogin;
   @FXML
   private Button signUpConfirm;
   
   public static boolean passwordLength(String password){
       if(password.length()>7){
           return true;
       }
       return false;
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
   @FXML
     public void signUpConfirmation(ActionEvent event) throws IOException, SQLException, ClassNotFoundException{
        String insertStmt = "Insert into Account values('"+newEmailInput.getText()+"','"+newPasswordInput.getText()+"')";
        System.out.println(insertStmt);
        String selectStmt = "Select email from Account";
        if(newEmailInput.getText().isEmpty() || newPasswordInput.getText().isEmpty() || rePasswordInput.getText().isEmpty()){
            //alert masukin semua field
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Pastikan semua data terisi");
            alert.showAndWait();
            newPasswordInput.clear();
            rePasswordInput.clear();
            newEmailInput.clear();
        }
        else if(!(Pattern.matches("^[a-zA-Z0-9]+[@]{1}+[ti][1]+[.]{1}+[ukdw][1]+[.]{1}+[ac][1]+[.]{1}+[id][1]+$",newEmailInput.getText()))){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Format Email Salah");
            alert.showAndWait();
            newPasswordInput.clear();
            rePasswordInput.clear();
            newEmailInput.clear();
        }
        else if(!newPasswordInput.getText().toString().equalsIgnoreCase(rePasswordInput.getText().toString())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Konfirmasi Password Salah");
            alert.showAndWait();
            newPasswordInput.clear();
            rePasswordInput.clear();
            newEmailInput.clear();
        }
        else if(passwordLength(newPasswordInput.getText().toString())==false && passwordLength(rePasswordInput.getText().toString())==false){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Password min 8 digit");
            alert.showAndWait();
            newPasswordInput.clear();
            rePasswordInput.clear();
            newEmailInput.clear();
        }
        else if(checkPassword(newPasswordInput.getText().toString())==false && checkPassword(rePasswordInput.getText().toString())==false){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Password harus mengandung huruf besar ,kecil dan angka");
            alert.showAndWait();
            newPasswordInput.clear();
            rePasswordInput.clear();
            newEmailInput.clear();
        }
        else{
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(selectStmt);
            //check email sudah terdaftar/belum (BELUM DITEST)
            while(rs.next()){
                String email = rs.getString("EMAIL");
                if(newEmailInput.getText().equals(email)){
                    //alert email sudah terdaftar
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setContentText("Email sudah terdaftar");
                    alert.showAndWait();
                }
            }
            //check password memenuhi syarat
            //...
            
            //jika sesuai
            if(newPasswordInput.getText().equals(rePasswordInput.getText())){
                //kalo bisa masukin kelas DBUtil 
                DBUtil.getInstance().dbExecuteUpdate(insertStmt);
                //alert daftar berhasil
                //alert silahkan melakukan login
                Parent signUpPage = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
                Scene signUp = new Scene(signUpPage);
                Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                app_stage.setScene(signUp);
                app_stage.show();
            }
        }
        
        
    }
     
    @FXML
    public void backLogin(ActionEvent event) throws IOException{
        Parent back = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene backLogin = new Scene(back);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.close();
        app_stage.setScene(backLogin);
        app_stage.show();
        
    }
   @FXML
    void close(MouseEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void minimize(MouseEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
    
    double x,y;
      @FXML
    void dragged(MouseEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }
    
    @FXML
    public void move(MouseEvent event){
        x = event.getSceneX();
        y = event.getSceneY();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
