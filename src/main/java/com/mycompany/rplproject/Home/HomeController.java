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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;

public class HomeController implements Initializable {
    private List<String> listOfSomething = null;
    private ListView<Button> listview;
    private ObservableList<Button> folders = FXCollections.observableArrayList();
    private ObservableList<String> list = FXCollections.observableArrayList("Tag","Folder");
    private String data;
    private Vector<String> parent = new Vector<>();
    private double x,y;
    List<Button> folderList = new ArrayList<>(); //our Collection to hold newly created Buttons
    List<Button> urlList = new ArrayList<>();
    List<Button> edit = new ArrayList<>();
    List<Button> delete = new ArrayList<>();
 
    @FXML
    private Text namaAkun;
    
    @FXML
    private Text settings;
    
    @FXML
    private Text logOut;
    
    @FXML
    private TextField inSearch;

    @FXML
    private Button btnSearch;
    
    @FXML
    private Button tambahURL;
    
    @FXML
    private Button btnEdit;
    
    @FXML
    private Button btnDelete;
    
    @FXML
    private Button tambahFolder;

    @FXML
    private VBox contentBox;
    
    @FXML
    private VBox editBox;

    @FXML
    private VBox deleteBox;
    
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
    
    @FXML
    public void logOut(MouseEvent event) throws IOException{
        Parent signOutPage = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene signOut = new Scene(signOutPage);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(signOut);
        app_stage.show();
    }
    
    @FXML
    public void tambahFolder(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/NewFolder.fxml"));
        Parent tambahFolderPage = loader.load();
        NewFolderController controller = loader.getController();
        controller.data(data);
        controller.tambahFolder(parent, data);
        Scene tambahFolder = new Scene(tambahFolderPage);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(tambahFolder);
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
    void tambahURL(MouseEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/NewURL.fxml"));
        Parent tambahURLPage = loader.load();
        NewURLController controller = loader.getController();
        controller.data(data);
        controller.tambahURL(parent, data);
        controller.setComboBoxValue();
        Scene tambahURL = new Scene(tambahURLPage);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(tambahURL);
        app_stage.show();
    }
    
    @FXML
    void tagList(MouseEvent event) throws IOException {
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
    }
    
    //ambil data email
    public void data(String s){
        data = s;
        namaAkun.setText(s);
    }
    
    public void displayFolder(String queryFolder){
        try {
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(queryFolder);
            while (rs.next()) { //iterate over every row returned
                 //extract button text, adapt the String to the columnname that you are interested in
                Button button = new Button(rs.getString("nama_folder"));
                button.setId(String.valueOf(rs.getInt("id_folder")));
                button.setMinWidth(350);
                
                Button editButton = new Button("Edit");
                folderList.add(button);
                editButton.setId(String.valueOf(rs.getInt("id_folder")));
                edit.add(editButton);
                Button deleteButton = new Button("X");
                deleteButton.setId(String.valueOf(rs.getInt("id_folder")));
                deleteButton.setBackground(Background.EMPTY);
                delete.add(deleteButton);           
            }
        }catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    public void displayUrl(String queryUrl){
        try {
            ResultSet rsUrl = DBUtil.getInstance().dbExecuteQuery(queryUrl);
            while(rsUrl.next()){
                Button text;
                if(rsUrl.getString("nama_url")== null){
                    text = new Button(rsUrl.getString("link_url"));
                }else{
                    text = new Button(rsUrl.getString("nama_url"));
                }     
                //text = new Button(rsUrl.getString("link_url"));
                text.setMinHeight(30);
                text.setMinWidth(350);
                
                Button editURL = new Button("Edit");
                Button delURL = new Button("X");
                delURL.setBackground(Background.EMPTY);
                text.setBackground(Background.EMPTY);
                editURL.setMinHeight(30);
                delURL.setMinHeight(30);
                
                delURL.setId(String.valueOf(rsUrl.getInt("id_url")));
                editURL.setId(String.valueOf(rsUrl.getInt("id_url")));
                text.setId(String.valueOf(rsUrl.getInt("id_url")));
                
                urlList.add(text);
                edit.add(editURL);
                delete.add(delURL); 
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
    //Menampilkan Data di Home
    public void show(Vector<String> t,String s,boolean Search){
            String queryFolder,queryUrl;
            parent = t;
            
            System.out.println(inSearch.getText());
             if(parent.size()==0){
                queryFolder = "SELECT * FROM Folder where parent_folder is NULL and email = '"+data+"';";
                queryUrl = "select * from url where email = '"+data+"' and id_folder is NULL";
                backBox.getChildren().clear();
            }else if(Search==true){
                queryFolder = "select * from folder where nama_folder like '%"+parent.get(parent.size()-1)+"%' and email='"+data+"'";
                queryUrl = "Select * from url where email='"+data+"' and nama_url like '%"+parent.get(parent.size()-1)+"%' or link_url like '%"+parent.get(parent.size()-1)+"%'";
            }else {
                queryFolder = "select * from folder where parent_folder = "+parent.get(parent.size()-1)+" and email='"+data+"'";
                queryUrl = "select * from url where email = '"+data+"' and id_folder = "+parent.get(parent.size()-1)+"";
            }
             for(int i=0;i<parent.size();i++){
                 System.out.println(parent.get(i));
             }
            displayFolder(queryFolder);
            displayUrl(queryUrl);
            contentBox.getChildren().clear();
            contentBox.getChildren().addAll(folderList);
            contentBox.getChildren().addAll(urlList);
            editBox.getChildren().clear();
            deleteBox.getChildren().clear();
            editBox.getChildren().addAll(edit);
            deleteBox.getChildren().addAll(delete);
            
            for(int i=0;i<delete.size();i++){
                final int o = i;
                final String tempt = deleteBox.getChildren().get(i).getId();
                deleteBox.getChildren().get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
                    //EventHandler buat Delete
                    public void handle(MouseEvent me) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation");
                        alert.setContentText("Apakah anda ingin menghapus folder ini");
                        Optional<ButtonType> option = alert.showAndWait();
                        if(option.get() == ButtonType.OK){
                             try {
                               String sqmt = "delete from URL where id_url = '"+tempt+"' ";
                               DBUtil.getInstance().dbExecuteUpdate(sqmt);
                               
                               String selectFolder = "delete from Folder where id_folder = "+tempt+" and email = '"+data+"'";
                               DBUtil.getInstance().dbExecuteUpdate(selectFolder);
                               
                            } catch (SQLException | ClassNotFoundException ex) {
                                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
                            Parent backHomePage;
                            try {
                                backHomePage = loader.load();
                                Scene backHome = new Scene(backHomePage);
                                HomeController controller = loader.getController();
                                controller.data(data);
                                controller.show(parent,data,false);
                                if(!parent.isEmpty()){
                                    controller.getBack(parent,data);
                                }
                                Stage app_stage = (Stage)((Node) me.getSource()).getScene().getWindow();
                                app_stage.setScene(backHome);
                                app_stage.show();
                            } catch (IOException ex) {
                                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                            } 
                        }
                    }
                });
                editBox.getChildren().get(i).setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent me){
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/fxml/NewFolder.fxml"));
                            Parent tambahFolderPage = loader.load();
                            NewFolderController controller = loader.getController();
                            controller.data(data);
                            String sql = "select * from folder where id_folder = '"+editBox.getChildren().get(o).getId()+"'";
                            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(sql);
                            if(rs.next()){
                                controller.editFolder(parent,rs.getString("nama_folder"),rs.getInt("id_folder"));
                            }
                            Scene tambahFolder = new Scene(tambahFolderPage);
                            Stage app_stage = (Stage)((Node) me.getSource()).getScene().getWindow();
                            app_stage.setScene(tambahFolder);
                            app_stage.show();
                        } catch (IOException | SQLException | ClassNotFoundException ex) {
                            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
            
            for(int i=0;i<folderList.size();i++){
                final String tempt = contentBox.getChildren().get(i).getId();
                contentBox.getChildren().get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent me) {
                        System.out.println(tempt);
                      
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
                        Parent backHomePage;
                        try {
                            backHomePage = loader.load();
                            Scene backHome = new Scene(backHomePage);
                            HomeController controller = loader.getController();
                            controller.data(data);
                            parent.add(tempt);
                            controller.show(parent,data,false);
                            if(!parent.isEmpty()){
                                controller.getBack(parent,data);
                            }
                            Stage app_stage = (Stage)((Node) me.getSource()).getScene().getWindow();
                            app_stage.setScene(backHome);
                            app_stage.show();
                        } catch (IOException ex) {
                            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        
                    }
                });
            }
            //untuk edit delete Bookmark
            for(int i=folderList.size();i<contentBox.getChildren().size();i++){
                final int o = i;
                contentBox.getChildren().get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent me) {
                        try {
                               String selectUrl = "select * from url where id_url = "+contentBox.getChildren().get(o).getId()+" and email = '"+data+"'";
                               Desktop d = Desktop.getDesktop();
                               ResultSet rs = DBUtil.getInstance().dbExecuteQuery(selectUrl);
                               if(rs.next()){      
                                   d.browse(new URI(rs.getString("link_url")));
                               }
                        } catch (IOException | URISyntaxException | SQLException | ClassNotFoundException ex) {
                            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                });
                editBox.getChildren().get(i).setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/fxml/NewURL.fxml"));
                            Parent tambahURLPage = loader.load();
                            NewURLController controller = loader.getController();
                            String sql = "select * from url where id_url = '"+editBox.getChildren().get(o).getId()+"'";
                            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(sql);
                            if(rs.next()){
                                controller.editBookmark(parent,rs.getString("nama_url"),rs.getString("link_url"),rs.getInt("id_url"),rs.getInt("id_tag"));     
                                controller.data(data);
                                controller.setComboBoxValue();
                                Scene tambahURL = new Scene(tambahURLPage);
                                Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                                app_stage.setScene(tambahURL);
                                app_stage.show();
                            }
                            
                        } catch (IOException | SQLException | ClassNotFoundException ex) {
                            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                });
                //Button untuk search
                btnSearch.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    System.out.println(inSearch.getText());
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
                    Parent backHomePage;
                    try {
                        backHomePage = loader.load();
                        Scene backHome = new Scene(backHomePage);
                        HomeController controller = loader.getController();
                        controller.data(data);
                        parent.add(inSearch.getText());
                        controller.show(parent,data,true);
                        controller.tambahFolder.setVisible(false);
                        controller.tambahURL.setVisible(false);
                        controller.inSearch.setText(inSearch.getText());
                        if(!parent.isEmpty()){
                            controller.getBack(parent,data);
                        }
                        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                        app_stage.setScene(backHome);
                        app_stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                    }   
                }
            });
            }
            //vbox.getChildren().addAll(buttonlist); //tadi buat nmenyamakan panjang button
        }
     
    public void getBack(Vector<String> t,String s){
        parent = t;
        Button button = new Button("<");
        backBox.getChildren().clear();
        backBox.getChildren().add(button);
        backBox.getChildren().get(backBox.getChildren().size()-1).setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Hai");
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
                try {
                    Parent backHomePage = loader.load();
                    Scene backHome = new Scene(backHomePage);
                    HomeController controller = loader.getController();
                    controller.data(data);
                    parent.remove(parent.size()-1);
                    if(parent.size()==0){
                        controller.show(parent,data,false);
                    }else {
                        controller.show(parent,data,true);
                    }
                    if(parent.size()!=0){
                        controller.getBack(parent,data);
                    }
                    Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    app_stage.setScene(backHome);
                    app_stage.show();
                    
                } catch (IOException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       
        
    }   
}
   