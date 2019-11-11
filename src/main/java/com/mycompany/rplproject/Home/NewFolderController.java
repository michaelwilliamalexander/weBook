/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.Home;

import com.mycompany.rplproject.Folder;
import com.mycompany.rplproject.User;
import com.mycompany.rplproject.db.DBUtil;
import java.io.IOException;
import java.net.URL;
import static java.sql.JDBCType.NULL;
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
    private Vector<String> v = new Vector();
    private User now;
    private int location;
    @FXML
    private Text namaAkun;
    
    @FXML
    private Button folderBtn;
    
    @FXML
    private TextField inFolder;
    
    public void tambahFolder (int locate, User user){
        now = user;
        this.location = locate;
        folderBtn.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                try {
                    String stmt = "Select * From Folder where nama_folder = '"+inFolder.getText()+"'";
                    String insertStmt;
                    if(location==0){
                        insertStmt = "Insert into Folder (nama_folder,email) values('"+inFolder.getText()+"','"+now.getEmail()+"')";
                    }else{
                        insertStmt = "Insert into Folder (nama_folder,Parent_folder,email) values('"+inFolder.getText()+"','"+location+"','"+now.getEmail()+"')";
                    }
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
                        String queryFolderBaru = "select * from folder where email='"+now.getEmail()+"'";
                        ResultSet rsFolder = DBUtil.getInstance().dbExecuteQuery(queryFolderBaru);
                        List<Folder> folder = new ArrayList<>();
                        while(rsFolder.next()){
                            folder.add(new Folder(rsFolder.getInt("id_folder"), rsFolder.getString("nama_folder"), rsFolder.getInt("parent_folder")));
                        }
                        now.setFolder(folder);
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
                        Parent backHomePage;
                        try {
                            backHomePage = loader.load();
                            Scene backHome = new Scene(backHomePage);
                            HomeController controller = loader.getController();
                            controller.data(now);
                            controller.show(false);
                            if(!now.getFolder().isEmpty()){
                                controller.getBack(now);
                            }
                            Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                            app_stage.setScene(backHome);
                            app_stage.show();
                        } catch (IOException ex) {
                            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Error");
                        alert.setContentText("Folder sudah terdaftar");
                        alert.showAndWait();
                        inFolder.clear();
                    }
                } catch (SQLException | ClassNotFoundException  ex) {
                    Logger.getLogger(NewFolderController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    @FXML
    void tagList(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/TagList.fxml"));
        Parent tagListPage = loader.load();
        Scene tagList = new Scene(tagListPage);
        TagListController controller = loader.getController();
        controller.data(now);
        controller.show(null,data,false);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(tagList);
        app_stage.show();
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
        controller.data(now);
        controller.show(false);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(backHome);
        app_stage.show();
    }
     public void data(User s, int parent){
        now = s;
        namaAkun.setText(now.getEmail());
        location = parent;
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
    
    public void editData(String newName, int id){
        String sql = "update folder set nama_folder = '"+newName+"' where id_folder = "+id;
        try{
            DBUtil.getInstance().dbExecuteUpdate(sql);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(NewFolderController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void editFolder(User user,final String nama, final int id){
        now = user;
        inFolder.setText(nama);
        folderBtn.setText("Edit");
        folderBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    editData(inFolder.getText(),id);
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
                    Parent backHomePage = loader.load();
                    Scene backHome = new Scene(backHomePage);
                    HomeController controller = loader.getController();
                    controller.data(now);
                    controller.show(false);
                    if(v.size()!=0){
                            controller.getBack(now);
                    }
                    Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    app_stage.setScene(backHome);
                    app_stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(NewFolderController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    public void editURL(String nama, String url, int id_tag){
        
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    void data(User now) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
