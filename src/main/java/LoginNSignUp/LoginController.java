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
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Michael William
 */
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button signUp;
    
    @FXML
    private Button loginBtn;
    
    @FXML
     public void SignIn(ActionEvent event) throws IOException{
        Parent signInPage = FXMLLoader.load(getClass().getResource("/fxml/Home.fxml"));
        Scene signIn = new Scene(signInPage);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(signIn);
        app_stage.show();
        
    }
    @FXML
    public void SignUp(ActionEvent event) throws IOException{
        Parent signUpPage = FXMLLoader.load(getClass().getResource("/fxml/SignUpFXML.fxml"));
        Scene signUp = new Scene(signUpPage);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(signUp);
        app_stage.show();
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
