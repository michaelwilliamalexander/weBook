/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.Home;

import com.mycompany.rplproject.db.DBUtil;
import com.mycompany.rplproject.Home.HomeController;
import com.mycompany.rplproject.Home.SettingController;
import com.mycompany.rplproject.Home.InsertNewPasswordController;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
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
public class GantiPasswordController implements Initializable {

      private double x,y;
    private String data;
    private String email = null, password = null;
    
      @FXML
    private PasswordField passLama;

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
    
    @FXML
    void tagList(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/TagList.fxml"));
        Parent tagListPage = loader.load();
        Scene tagList = new Scene(tagListPage);
        TagListController controller = loader.getController();
        controller.data(data);
        controller.show(data);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(tagList);
        app_stage.show();
    }
    
    public void backHome(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
        Parent backHomePage = loader.load();
        Scene backHome = new Scene(backHomePage);
        HomeController controller = loader.getController();
        controller.data(data);
        controller.show(data);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(backHome);
        app_stage.show();
    }
     public void data(String s){
        data = s;
        namaAkun.setText(s);
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
        controller.data(data);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(backSetting);
        app_stage.show();
    }
    //check pass lama
    public void checkPass(MouseEvent event) throws ClassNotFoundException, IOException, SQLException{
        String stmt;
        String pass = passLama.getText();
        stmt = "Select * from Account where email = '"+data+"'";
          try {
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(stmt);
            if(rs.next()){
                password = rs.getString("password");
                System.out.println(email+" - "+password);
                if(password.equalsIgnoreCase(pass)){
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/fxml/ChangePassword.fxml"));
                    Parent anotherChangePasswordPage = loader.load();
                    Scene anotherChangePassword = new Scene(anotherChangePasswordPage);
                    InsertNewPasswordController controller = loader.getController();
                    controller.data(data);
                    Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    app_stage.setScene(anotherChangePassword);
                    app_stage.show();
                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setContentText("Password salah !");
                    alert.showAndWait();
                }
            }
            
        } catch (SQLException e) {
            throw e;
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
