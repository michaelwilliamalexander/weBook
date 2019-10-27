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
import javax.swing.JOptionPane;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Michael William
 */
public class LoginController implements Initializable {
    private String email=null,password=null;
    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField emailInput;
    
    @FXML
    private PasswordField passwordInput;
    
    @FXML
    private Button signUp;
    
    @FXML
    private Button loginBtn;
    
    /**
     *
     * @param event
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
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
                    Parent signInPage = FXMLLoader.load(getClass().getResource("/fxml/Home.fxml"));
                    Scene signIn = new Scene(signInPage);
                    Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    app_stage.close();
                    app_stage.setScene(signIn);
                    app_stage.show();
                }
            }
            else{
                JOptionPane.showMessageDialog(null,"Email atau Password salah !");
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
