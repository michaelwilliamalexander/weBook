/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.Home;

import com.mycompany.rplproject.User;
import com.mycompany.rplproject.db.FolderDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Michael William
 */
public class NewFolderController implements Initializable {

    private double x,y;
    private String data;
    List<Integer> folderTree = new ArrayList<>();
    private User now;
    private int location;
    @FXML
    private Text namaAkun;
    
    @FXML
    private Button isBack;
    @FXML
    private Button folderBtn;
    
    @FXML
    private TextField inFolder;
    
      @FXML
    void cancelNewFolder(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
            Parent backHomePage = loader.load();
            Scene backHome = new Scene(backHomePage);
            HomeController controller = loader.getController();
            controller.data(now);
            controller.show(false,folderTree);
            Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(backHome);
            app_stage.show();
            } catch (IOException ex) {
                Logger.getLogger(NewURLController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void tambahFolder ( List<Integer> locate, User user){
        now = user;
        folderTree = locate;
        this.location = locate.get(locate.size()-1);
        folderBtn.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                try {
                    if(inFolder.getText().isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Error");
                        alert.setContentText("Field Kosong");
                        alert.showAndWait();
                    }
                    else{
                        String tempt = FolderDAO.seeData(inFolder.getText(),location);
                        if(!inFolder.getText().equals(tempt)){
                             if(location==0){
                                 FolderDAO.tambahFolder(inFolder.getText(),now.getEmail());
                             }else{
                                FolderDAO.tambahFolder_WithParent(inFolder.getText(), location,now.getEmail());
                             }
                            now.setFolder(FolderDAO.showFolderList(now.getEmail()));
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
                            Parent backHomePage;
                            try {
                                backHomePage = loader.load();
                                Scene backHome = new Scene(backHomePage);
                                HomeController controller = loader.getController();
                                controller.data(now);
                                controller.show(false,folderTree);
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
        controller.show(false);
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
        folderTree.removeAll(folderTree);
        folderTree.add(0);
        controller.show(false,folderTree);
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
    
    
    public void editFolder(final String nama, final int id,  List<Integer> parent){
        folderTree = parent;
        inFolder.setText(nama);
        folderBtn.setText("Edit");
        folderBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(inFolder.getText().isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Error");
                        alert.setContentText("Field Kosong");
                        alert.showAndWait();
                }else{
                    try {
                        FolderDAO.updateFolder(id,inFolder.getText(), now);
                        now.setFolder(FolderDAO.showFolderList(now.getEmail()));
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
                        Parent backHomePage = loader.load();
                        Scene backHome = new Scene(backHomePage);
                        HomeController controller = loader.getController();
                        controller.data(now);
                        controller.show(false,folderTree);
                        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                        app_stage.setScene(backHome);
                        app_stage.show();
                    } catch (IOException | SQLException | ClassNotFoundException ex) {
                        Logger.getLogger(NewFolderController.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
