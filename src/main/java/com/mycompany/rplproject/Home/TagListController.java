/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.Home;

import com.mycompany.rplproject.db.DBUtil;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TagListController implements Initializable {
    private double x,y;
    private String data;
    private Vector<String> v = new Vector();
    private List<Button> tag = new ArrayList<>();
    private List<Button> edit = new ArrayList<>();
    private List<Button> delete = new ArrayList<>();
    
    @FXML
    private Text namaAkun;
    
    @FXML
    private VBox contentBox;

    @FXML
    private VBox editBox;

    @FXML
    private VBox deleteBox;
    
    @FXML
    private HBox backBox;
    
    @FXML
    private TextField inSearchTag;

    @FXML
    private Button btnSearchTag;
    
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
    
    public void data(String s){
        data = s;
        namaAkun.setText(s);
    }
    
    public void backHome(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
        Parent backHomePage = loader.load();
        Scene backHome = new Scene(backHomePage);
        HomeController controller = loader.getController();
        controller.data(data);
        controller.show(v,data,false);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(backHome);
        app_stage.show();
    }
    
     @FXML
    public void setting(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Setting.fxml"));
        Parent settingPage = loader.load();
        SettingController controller = loader.getController();
        controller.data(data);
        Scene setting = new Scene(settingPage);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setX(event.getScreenX() - x);
        app_stage.setY(event.getScreenY() - y);
        app_stage.setScene(setting);
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
    
    public void displayTag(String sql){
        try {
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(sql);
            System.out.println(sql);
            while(rs.next()){
                Button button = new Button(rs.getString("nama_tag"));
                button.setMinWidth(350);
                button.setId(String.valueOf(rs.getInt("id_tag")));
                tag.add(button);
                Button editButton = new Button("Edit");
                editButton.setId(String.valueOf(rs.getInt("id_tag")));
                edit.add(editButton);
                Button deleteButton = new Button("X");
                deleteButton.setId(String.valueOf(rs.getInt("id_tag")));
                deleteButton.setBackground(Background.EMPTY);
                delete.add(deleteButton);  
                }
            }catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(TagListController.class.getName()).log(Level.SEVERE, null, ex);
            }
        contentBox.getChildren().addAll(tag);
        editBox.getChildren().addAll(edit);
        deleteBox.getChildren().addAll(delete);
    }
        
    public void displayURL(String sql){
        try {
            ResultSet rs= DBUtil.getInstance().dbExecuteQuery(sql);
            System.out.println(sql);
             while(rs.next()){
                 
                final Button button = new Button(rs.getString("nama_url"));
                button.setMinWidth(350);
                button.setId(String.valueOf(rs.getInt("id_url")));
                button.setBackground(Background.EMPTY);
                tag.add(button);
                Button editButton = new Button("Edit");
                editButton.setId(String.valueOf(rs.getInt("id_url")));
                edit.add(editButton);
                Button deleteButton = new Button("X");
                deleteButton.setId(String.valueOf(rs.getInt("id_url")));
                deleteButton.setBackground(Background.EMPTY);
                delete.add(deleteButton);  
                button.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            String selectUrl = "select * from url where id_url = "+button.getId()+" and email = '"+data+"'";
                            Desktop d = Desktop.getDesktop();
                            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(selectUrl);
                            if(rs.next()){
                                d.browse(new URI(rs.getString("link_url")));
                            }
                        } catch (SQLException | ClassNotFoundException | URISyntaxException | IOException ex) {
                            Logger.getLogger(TagListController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
             
             });
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TagListController.class.getName()).log(Level.SEVERE, null, ex);
        }
        contentBox.getChildren().addAll(tag);
        editBox.getChildren().addAll(edit);
        deleteBox.getChildren().addAll(delete);
    }    
    
    public void show(final String idtag, String s,boolean Search){
        
        contentBox.getChildren().clear();
        editBox.getChildren().clear();
        deleteBox.getChildren().clear();
        String sql = "Select * from Tag where email = '"+s+"' or id_tag = 0";
        String sql1= "Select * from URL where id_tag ='"+idtag+"' and email = '"+s+"'";
        String sql2 = "Select * from Tag where nama_tag like '%"+idtag+"%'";
        
        if(idtag == null){
            displayTag(sql);
        }else if(Search==true){
            displayTag(sql2);
        }else{
            displayURL(sql1);
        }
            for(int i=0;i<delete.size();i++){
                final int o = i;
                final String tempt = deleteBox.getChildren().get(i).getId();
                deleteBox.getChildren().get(i).setOnMouseClicked(new EventHandler<MouseEvent>(){
                
                    @Override
                    public void handle(MouseEvent me) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation");
                        alert.setContentText("Apakah anda ingin menghapus folder ini");
                        Optional<ButtonType> option = alert.showAndWait();
                        if(option.get() == ButtonType.OK){
                             try {
                               String selectFolder = "delete from Tag where id_tag = "+tempt+" and email = '"+data+"'";
                               DBUtil.getInstance().dbExecuteUpdate(selectFolder);
                               
                            } catch (SQLException | ClassNotFoundException ex) {
                                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/fxml/TagList.fxml"));
                            Parent TagListPage;
                            try {
                                TagListPage = loader.load();
                                Scene TagList = new Scene(TagListPage);
                                TagListController controller = loader.getController();
                                controller.data(data);
                                controller.show(idtag, data,false);
                                Stage app_stage = (Stage)((Node) me.getSource()).getScene().getWindow();
                                app_stage.setScene(TagList);
                                app_stage.show();
                            } catch (IOException ex) {
                                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                            } 
                        }
                    }
                });
                editBox.getChildren().get(i).setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                            try {
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("/fxml/InputTagList.fxml"));
                                Parent TagListPage = loader.load();
                                Scene TagList = new Scene(TagListPage);
                                InputTagListController controller = loader.getController();
                                controller.data(data);
                                controller.editTag(tempt,data);
                                Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                                app_stage.setScene(TagList);
                                app_stage.show();
                            } catch (IOException ex) {
                                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) { 
                            Logger.getLogger(TagListController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(TagListController.class.getName()).log(Level.SEVERE, null, ex);
                        } 
                    }  
                });
                
                contentBox.getChildren().get(i).setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {     
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/fxml/TagList.fxml"));
                            Parent TagListPage = loader.load();
                            Scene TagList = new Scene(TagListPage);
                            TagListController controller = loader.getController();
                            controller.data(data);
                            controller.getBack(contentBox.getChildren().get(o).getId(), data);
                            controller.show(contentBox.getChildren().get(o).getId(), data,false);
                            Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                            app_stage.setScene(TagList);
                            app_stage.show();
                        } catch (IOException ex) {
                            Logger.getLogger(TagListController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                });
                btnSearchTag.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            System.out.println(inSearchTag.getText());
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/fxml/TagList.fxml"));
                            Parent tagListPage = loader.load();
                            Scene tagList = new Scene(tagListPage);
                            TagListController controller = loader.getController();
                            controller.data(data);
                            controller.inSearchTag.setText(inSearchTag.getText());
                            controller.show(inSearchTag.getText(), data,true);
                            controller.getBack(contentBox.getChildren().get(o).getId(), data);
                            Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                            app_stage.setScene(tagList);
                            app_stage.show();
                        } catch (IOException ex) {
                            Logger.getLogger(TagListController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                });
                
            }
        }
        
    public void getBack(final String idTag,String email){
        Button back = new Button("Back");
        final String tempt = idTag;
        back.setId("back");
        backBox.getChildren().clear();
        backBox.getChildren().add(back);
        backBox.getChildren().get(backBox.getChildren().size()-1).setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/fxml/TagList.fxml"));
                    Parent tagListPage = loader.load();
                    Scene tagList = new Scene(tagListPage);
                    TagListController controller = loader.getController();
                    controller.data(data);
                    controller.show(null,data,false);
                    Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    app_stage.setScene(tagList);
                    app_stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(TagListController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
     @FXML
    void tambahTag(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/InputTagList.fxml"));
        Parent TagListPage = loader.load();
        Scene TagList = new Scene(TagListPage);
        InputTagListController controller = loader.getController();
        controller.data(data);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(TagList);
        app_stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
