/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.LoginNSignUp;


import com.mycompany.rplproject.Home.HomeController;
import com.mycompany.rplproject.User;
import com.mycompany.rplproject.db.DBUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Michael William
 */
public class LoginController implements Initializable {
    private String email=null,password=null;
    private Vector<String> v = new Vector();
    private User user = new User();
    
    @FXML
    private TextField emailInput;
    
    @FXML
    private PasswordField passwordInput;
    
    @FXML
    private Button signUp;
    
    @FXML
    private Button loginBtn;
    
    @FXML
     public void SignIn(ActionEvent event) throws IOException, ClassNotFoundException, SQLException{
       
        String selectStmt;
        String inpemail = emailInput.getText();
        String inppass = passwordInput.getText();
        selectStmt = "SELECT * FROM Account WHERE email ='"+inpemail+"' and password='"+inppass+"'";
        try {
            System.out.println(selectStmt);
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(selectStmt);
            if(rs.next()){
                email = rs.getString("email");
                password = rs.getString("password");
                System.out.println(email+" - "+password);
                if(email.equals(inpemail) && password.equals(inppass)){
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
                    Parent signInPage = loader.load();
                    Scene signIn = new Scene(signInPage);
                    HomeController controller = loader.getController();
                    User logged = new User(email,password);
                    controller.data(logged);
                    controller.show(logged,false);
                    Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    app_stage.close();
                    app_stage.setScene(signIn);
                    app_stage.show();
                }
            }
            else{
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Error");
                alert.setContentText("Email atau password salah");
                alert.showAndWait();
                emailInput.clear();
                passwordInput.clear();
                
            }
        } catch (SQLException e) {
            System.out.println("Sedang mencari mahasiswa dengan nim " + emailInput.getText() + ", error terjadi: " + e);
            throw e;
        }
        
        
    }
    @FXML
    public void SignUp(ActionEvent event) throws IOException{
        Parent signUpPage = FXMLLoader.load(getClass().getResource("/fxml/SignUpFXML.fxml"));
        Scene signUp = new Scene(signUpPage);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(signUp);
        app_stage.show();
        
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
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
