/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.Home;

import com.mycompany.rplproject.db.DBUtil;
import java.io.IOException;
import java.net.URL;
import static java.sql.JDBCType.NULL;
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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Michael William
 */
public class NewFolderController implements Initializable {

    private double x,y;
    private String data;

    @FXML
    private Text namaAkun;
    
    @FXML
    private Button folderBtn;
    
    @FXML
    private TextField inFolder;
    
    public void tambahFolder (MouseEvent event) throws IOException, SQLException, ClassNotFoundException{
        String stmt = "Select * From Folder where nama_folder = '"+inFolder.getText()+"'";
        String insertStmt = "Insert into Folder (nama_folder,email) values('"+inFolder.getText()+"','"+data+"')";
        ResultSet rs = DBUtil.getInstance().dbExecuteQuery(stmt);
        String tempt = null;
        while(rs.next()){
            tempt= rs.getString("nama_folder");
            System.out.println(tempt);
        }
        if(inFolder.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setContentText("Field Kosong");
                    alert.showAndWait();
        }
        else if(!inFolder.getText().equals(tempt)){
                DBUtil.getInstance().dbExecuteUpdate(insertStmt);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
                Parent backHomePage = loader.load();
                Scene backHome = new Scene(backHomePage);
                HomeController controller = loader.getController();
                controller.data(data);
                controller.fillCombo();
                controller.show(data);
                Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                app_stage.setScene(backHome);
                app_stage.show();
            }
         else {
                 Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setContentText("Folder sudah terdaftar");
                    alert.showAndWait();
                    inFolder.clear();
            }
 
    }
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
    
    public void backHome(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
        Parent backHomePage = loader.load();
        Scene backHome = new Scene(backHomePage);
        HomeController controller = loader.getController();
        controller.data(data);
        controller.fillCombo();
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
