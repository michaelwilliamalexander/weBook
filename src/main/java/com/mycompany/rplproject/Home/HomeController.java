/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.Home;

import com.mycompany.rplproject.Folder;
import com.mycompany.rplproject.User;
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
    
    List<Integer> folderTree = new ArrayList<>();
    
    private double x,y;
    List<Button> folderList = new ArrayList<>(); //our Collection to hold newly created Buttons
    List<Button> urlList = new ArrayList<>();
    List<Button> edit = new ArrayList<>();
    List<Button> delete = new ArrayList<>();
    private User now = new User();
    
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

    public HomeController() {
        this.folderTree = new Vector();
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
        controller.data(now, folderTree.get(folderTree.size()-1));
        System.out.println(folderTree.get(folderTree.size()-1));
        controller.tambahFolder(folderTree.get(folderTree.size()-1), now);
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
        controller.data(now);
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
        controller.data(now);
        controller.tambahURL(folderTree.get(folderTree.size()-1), now.getEmail());
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
        controller.data(now);
        controller.show(null,now.getEmail(),false);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(tagList);
        app_stage.show();
    }
    
    //ambil data email
    public void data(User s){
        now = s;
        namaAkun.setText(now.getEmail());
        folderTree.add(0);
        show(false);
    }
    
//    public void displayFolder(String queryFolder){
//        try {
//            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(queryFolder);
//            while (rs.next()) { //iterate over every row returned
//                 //extract button text, adapt the String to the columnname that you are interested in
//                Button button = new Button(rs.getString("nama_folder"));
//                button.setId(String.valueOf(rs.getInt("id_folder")));
//                button.setMinWidth(350);
//                folderList.add(button);
//               
//                
//                Button editButton = new Button("Edit");
//                editButton.setId(String.valueOf(rs.getInt("id_folder")));
//                edit.add(editButton);
//                
//                Button deleteButton = new Button("X");
//                deleteButton.setId(String.valueOf(rs.getInt("id_folder")));
//                deleteButton.setBackground(Background.EMPTY);
//                delete.add(deleteButton);      
//                
//            }
//        }catch (SQLException | ClassNotFoundException ex) {
//            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    } 
//    
//    public void displayUrl(String queryUrl){
//        try {
//            ResultSet rsUrl = DBUtil.getInstance().dbExecuteQuery(queryUrl);
//            while(rsUrl.next()){
//                Button text;
//                if(rsUrl.getString("nama_url")== null){
//                    text = new Button(rsUrl.getString("link_url"));
//                }else{
//                    text = new Button(rsUrl.getString("nama_url"));
//                }     
//                
//                text.setMinHeight(30);
//                text.setMinWidth(350);
//                
//                Button editURL = new Button("Edit");
//                Button delURL = new Button("X");
//                delURL.setBackground(Background.EMPTY);
//                text.setBackground(Background.EMPTY);
//                editURL.setMinHeight(30);
//                delURL.setMinHeight(30);
//                
//                delURL.setId(String.valueOf(rsUrl.getInt("id_url")));
//                editURL.setId(String.valueOf(rsUrl.getInt("id_url")));
//                text.setId(String.valueOf(rsUrl.getInt("id_url")));
//                
//                urlList.add(text);
//                edit.add(editURL);
//                delete.add(delURL); 
//            }
//        } catch (SQLException | ClassNotFoundException ex) {
//            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
      
    //Menampilkan Data di Home
    public void show(boolean Search){
        System.out.println("masuk ke show");
        if(folderTree.size()>1){
           getBack(now);
        }else{
            backBox.getChildren().clear();
        }
        
        //agar dapat digunakan untuk menampilkan subfolder
        contentBox.getChildren().clear();
        editBox.getChildren().clear();
        deleteBox.getChildren().clear();
        folderList.clear();
        urlList.clear();
        delete.clear();
        edit.clear();
        
        //tambah folder ke home
        for(int i=0;i<now.getFolder().size();i++){
            final int o = i;
            System.out.println("perulangan folder");
            if(folderTree.get(folderTree.size()-1)==0){
                System.out.println("jebol");
                if(now.getFolder().get(i).getId_parent() == folderTree.get(folderTree.size()-1)){
                    //tampilkan folder di home
                    System.out.println("masuk yang sini");
                    final Button folder = new Button(now.getFolder().get(i).getNama());
                    folder.setId(String.valueOf(now.getFolder().get(i).getId()));
                    folder.setMinWidth(400);
                    folder.setOnMouseClicked(new EventHandler<MouseEvent>(){
                        @Override
                        public void handle(MouseEvent event) {
                            folderTree.add(Integer.parseInt(folder.getId()));
                            show(false);
                        }
                    });
                    //buat button edit folder
                    Button Edit = new Button("Edit");
                    Edit.setId(String.valueOf(now.getFolder().get(o).getId()));
                    Edit.setBackground(Background.EMPTY);
                    Edit.setOnMouseClicked(new EventHandler<MouseEvent>(){
                        @Override
                        public void handle(MouseEvent event) {
                            //panggil fungsi edit folder . . .
                            //belum dikerjain
                        }
                    });
                    //buat button delete folder
                    final Button Delete = new Button("X");
                    Delete.setId(String.valueOf(now.getFolder().get(o).getId()));
                    Delete.setBackground(Background.EMPTY);
                    Delete.setOnMouseClicked(new EventHandler<MouseEvent>(){
                        @Override
                        public void handle(MouseEvent event) {
                            try {
                                String deleteFolder = "delete from folder where id_folder = "+Delete.getId();
                                DBUtil.getInstance().dbExecuteUpdate(deleteFolder);
                                for(int j=0; j<now.getFolder().size();j++){
                                    if(Integer.parseInt(Delete.getId()) == now.getFolder().get(j).getId()){
                                        now.getFolder().remove(j);
                                        break;
                                    }
                                }
                                show(false);
                            } catch (SQLException | ClassNotFoundException ex) {
                                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    folderList.add(folder); 
                    edit.add(Edit);
                    delete.add(Delete);
                }
            }else{
                //menampilkan folder didalam folder
                System.out.println("masuk kesini gan");
                if(now.getFolder().get(i).getId_parent() == folderTree.get(folderTree.size()-1)){
                    final Button folder = new Button(now.getFolder().get(i).getNama());
                    folder.setId(String.valueOf(now.getFolder().get(i).getId()));
                    folder.setMinWidth(400);
                    folder.setOnMouseClicked(new EventHandler<MouseEvent>(){
                        @Override
                        public void handle(MouseEvent event) {
                            folderTree.add(Integer.parseInt(folder.getId()));
                            show(false);
                        }
                    });
                    folderList.add(folder);
                }
            }
        }
        System.out.println("udah ketambah");
        
        //tambah bookmark ke home
        for(int i=0;i<now.getBookmark().size();i++){
            final int o = i;
            //menampilkan bookmark dalam home
            if(folderTree.get(folderTree.size()-1)==0){
                if(now.getBookmark().get(i).getId_folder()==folderTree.get(i)){
                    Button url;
                    if(now.getBookmark().get(i).getNama().equals(null)){
                        url = new Button(now.getBookmark().get(i).getLink());
                    }else{
                        url = new Button(now.getBookmark().get(i).getNama());
                    }
                    url.setMinWidth(400);
                    url.setBackground(Background.EMPTY);
                    url.setOnMouseClicked(new EventHandler<MouseEvent>(){
                        @Override
                        public void handle(MouseEvent event) {
                            try {
                                Desktop d = Desktop.getDesktop();
                                d.browse(new URI(now.getBookmark().get(o).getLink()));
                            } catch (URISyntaxException | IOException ex) {
                                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    final Button Delete = new Button("X");
                    Delete.setId(String.valueOf(now.getBookmark().get(o).getId()));
                    Delete.setBackground(Background.EMPTY);
                    Delete.setOnMouseClicked(new EventHandler<MouseEvent>(){
                        @Override
                        public void handle(MouseEvent event) {
                            try {
                                String deleteFolder = "delete from Url where id_url = "+Delete.getId();
                                DBUtil.getInstance().dbExecuteUpdate(deleteFolder);
                                for(int j=0; j<now.getBookmark().size();j++){
                                    if(Integer.parseInt(Delete.getId()) == now.getBookmark().get(j).getId()){
                                        now.getBookmark().remove(j);
                                        break;
                                    }
                                }
                                show(false);
                            } catch (SQLException | ClassNotFoundException ex) {
                                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    delete.add(Delete);
                    urlList.add(url);
                }else{
                    //menampilkan bookmark didalam folder
                    if(now.getBookmark().get(i).getId_folder()==folderTree.get(i)){
                        Button url;
                        if(now.getBookmark().get(i).getNama().equals(null)){
                            url = new Button(now.getBookmark().get(i).getLink());
                        }else{
                            url = new Button(now.getBookmark().get(i).getNama());
                        }
                        url.setMinWidth(400);
                        url.setBackground(Background.EMPTY);
                        url.setOnMouseClicked(new EventHandler<MouseEvent>(){
                            @Override
                            public void handle(MouseEvent event) {
                                try {
                                    Desktop d = Desktop.getDesktop();
                                    d.browse(new URI(now.getBookmark().get(o).getLink()));
                                } catch (URISyntaxException | IOException ex) {
                                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                        final Button Delete = new Button("X");
                        Delete.setId(String.valueOf(now.getBookmark().get(o).getId()));
                        Delete.setBackground(Background.EMPTY);
                        Delete.setOnMouseClicked(new EventHandler<MouseEvent>(){
                            @Override
                            public void handle(MouseEvent event) {
                                try {
                                    String deleteFolder = "delete from Url where id_url = "+Delete.getId();
                                    DBUtil.getInstance().dbExecuteUpdate(deleteFolder);
                                    for(int j=0; j<now.getBookmark().size();j++){
                                        if(Integer.parseInt(Delete.getId()) == now.getBookmark().get(j).getId()){
                                            now.getBookmark().remove(j);
                                            break;
                                        }
                                    }
                                    show(false);
                                } catch (SQLException | ClassNotFoundException ex) {
                                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                        delete.add(Delete);
                        urlList.add(url);
                    }
                }
            }
        }
        contentBox.getChildren().addAll(folderList);
        contentBox.getChildren().addAll(urlList);
        editBox.getChildren().addAll(edit);
        deleteBox.getChildren().addAll(delete);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
    }
    
    public void getBack(User u){
        now = u;
        Button button = new Button("<");
        backBox.getChildren().clear();
        backBox.getChildren().add(button);
        backBox.getChildren().get(backBox.getChildren().size()-1).setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Hai");
                folderTree.remove(folderTree.size()-1);
                show(false);
            }
        });
    }
} 
    

   
    
     
   