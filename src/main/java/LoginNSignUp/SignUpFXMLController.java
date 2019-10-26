/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoginNSignUp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;



public class SignUpFXMLController implements Initializable {

   @FXML
   private Button backLogin;
   @FXML
   private Button signUpConfirm;
   
   @FXML
     public void signUpConfirmation(ActionEvent event) throws IOException{
        Parent signUpPage = FXMLLoader.load(getClass().getResource("/fxml/Home.fxml"));
        Scene signUp = new Scene(signUpPage);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(signUp);
        app_stage.show();
        
    }
     
    @FXML
    public void backLogin(ActionEvent event) throws IOException{
        Parent back = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene backLogin = new Scene(back);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(backLogin);
        app_stage.show();
        
    }
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
