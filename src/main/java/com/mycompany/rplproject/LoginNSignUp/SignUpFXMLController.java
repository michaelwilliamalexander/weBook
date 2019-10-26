/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.LoginNSignUp;

import com.mycompany.rplproject.db.DBUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class SignUpFXMLController implements Initializable {
   @FXML
   private TextField newEmailInput;
   @FXML
   private PasswordField newPasswordInput;
   @FXML
   private PasswordField rePasswordInput;
   @FXML
   private Button backLogin;
   @FXML
   private Button signUpConfirm;
   
   @FXML
     public void signUpConfirmation(ActionEvent event) throws IOException, SQLException, ClassNotFoundException{
        String insertStmt = "Insert into Account values('"+newEmailInput.getText()+"','"+newPasswordInput.getText()+"')";
        System.out.println(insertStmt);
        String selectStmt = "Select email from Account";
        if(newEmailInput.getText().isEmpty() || newPasswordInput.getText().isEmpty() || rePasswordInput.getText().isEmpty()){
            //alert masukin semua field
            System.out.println("masukan semua field");
        }
        else{
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(selectStmt);
            //check email sudah terdaftar/belum (BELUM DITEST)
            while(rs.next()){
                String email = rs.getString("EMAIL");
                if(newEmailInput.getText().equals(email)){
                    //alert email sudah terdaftar
                    newEmailInput.getText().replaceAll("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", null);
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
            }else{
                //alert password tidak sesuai
                System.out.println("password tidak sesuai");
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
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
