/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.Home;

import com.mycompany.rplproject.Tag;
import com.mycompany.rplproject.User;
import com.mycompany.rplproject.db.DBUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class InputTagListController implements Initializable {
    private double x,y;
    private String data;
    List<Integer> folderTree = new ArrayList<>();
    private User now;
    @FXML
    private Text namaAkun;
   
    @FXML
    private Button btnTag;

    @FXML
    private TextField inTag;
    
    @FXML
    private HBox backBox;
    
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
    
    public void data(User s){
        now = s;
        namaAkun.setText(now.getEmail());
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
    
     @FXML
    public void logOut(MouseEvent event) throws IOException{
        Parent signOutPage = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene signOut = new Scene(signOutPage);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(signOut);
        app_stage.show();
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
    public void setting(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Setting.fxml"));
        Parent settingPage = loader.load();
        SettingController controller = loader.getController();
        controller.data(now);
        Scene setting = new Scene(settingPage);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setX(event.getScreenX() - x);
        app_stage.setY(event.getScreenY() - y);
        app_stage.setScene(setting);
        app_stage.show();
    }
    
       @FXML
    void tambahTag(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
        String sql = "Select * from tag where nama_tag = '"+inTag.getText().toString()+"' and email = '"+now.getEmail()+"'";
        String dataBaru = "Insert into Tag(nama_tag,email) values('"+inTag.getText().toString()+"','"+now.getEmail()+"')";
        String temp = "Select * from Tag where email = '"+now.getEmail()+"' and nama_tag = '"+inTag.getText()+"'";
        String tempt = null; 
        int id = 0;
        ResultSet rs = DBUtil.getInstance().dbExecuteQuery(sql);
        while(rs.next()){
            tempt = rs.getString("nama_tag");
        }
        if(inTag.getText().toString().equals(tempt)){
            Alert alert = new Alert(AlertType.ERROR);
           alert.setContentText("Nama Tag telah dibuat");
           alert.showAndWait();
        }
        else{
            DBUtil.getInstance().dbExecuteUpdate(dataBaru);
            
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
    }
    
    public void editTag(final int idTag) throws SQLException, ClassNotFoundException{
        String sqmt = "Select * from Tag where id_tag = '"+idTag+"'";
        ResultSet rs = DBUtil.getInstance().dbExecuteQuery(sqmt);
        String tamp = null;
        String name = null;
        while(rs.next()){
            tamp = rs.getString("nama_tag");
        }
        inTag.setText(tamp);
        btnTag.setText("Edit");
        btnTag.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
               String sql = "Update Tag set nama_tag = '"+inTag.getText()+"' where id_tag = '"+idTag+"' and email = '"+now.getEmail()+"' " ;
                try {
                    DBUtil.getInstance().dbExecuteUpdate(sql);
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/fxml/TagList.fxml"));
                    String queryTag = "select * from tag where email='"+now.getEmail()+"' or id_tag=0";
                    ResultSet rsTag = DBUtil.getInstance().dbExecuteQuery(queryTag);
                    List<Tag> tag = new ArrayList<>();
                    while(rsTag.next()){
                        tag.add(new Tag(rsTag.getInt("id_tag"), rsTag.getString("nama_tag")));
                    }
                    now.setTag(tag);
                    Parent tagListPage = loader.load();
                    Scene tagList = new Scene(tagListPage);
                    TagListController controller = loader.getController();
                    controller.data(now);
                    controller.show(false);
                    Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    app_stage.setScene(tagList);
                    app_stage.show();
                }  catch (IOException ex) {
                    Logger.getLogger(InputTagListController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(InputTagListController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(InputTagListController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
