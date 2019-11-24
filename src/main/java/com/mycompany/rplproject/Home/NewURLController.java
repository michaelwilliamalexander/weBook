/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.Home;

import com.mycompany.rplproject.Bookmark;
import com.mycompany.rplproject.Folder;
import com.mycompany.rplproject.Tag;
import com.mycompany.rplproject.User;
import com.mycompany.rplproject.db.BookmarkDAO;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Michael William
 */
public class NewURLController implements Initializable {

    private double x,y;
    private String data;
    List<Integer> folderTree = new ArrayList<>();
    private User now;
    @FXML
    private Text namaAkun;
    
    @FXML
    private TextField namaUrl;

    @FXML
    private TextField linkUrl;

    @FXML
    private ComboBox namaTag;
    
    @FXML
    private Button insertUrlButton;
    
    @FXML
    private Button isBack;
    
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
    
    public void data(User s){
        now = s;
        namaAkun.setText(now.getEmail());
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
    
    //fungsi menampilkan list folder dan tag yang ada ke combo box
    public void setComboBoxValue() throws SQLException{
        String queryTag = "select * from tag where email = '"+now.getEmail()+"' or id_tag=0";
        ObservableList listTag = FXCollections.observableArrayList(); //to show tag data into tag  
        try{
            ResultSet rsTag = DBUtil.getInstance().dbExecuteQuery(queryTag);
            while(rsTag.next()){
                 listTag.add(new Tag(rsTag.getInt("id_tag"),rsTag.getString("nama_tag")));
            }
            //namaTag.setValue(Default);
            namaTag.setItems(listTag);    
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(NewURLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void tambahURL(List<Integer> parent, String s){
        folderTree = parent;
        final int location = parent.get(parent.size()-1);
        //insert url, tapi blm bisa sama tag
        insertUrlButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                 Tag t = (Tag) namaTag.getSelectionModel().getSelectedItem();
                String SqlQuery;
                try{
                    if(location==0){
                        BookmarkDAO.tambahUrl(namaUrl.getText(),linkUrl.getText(), now);
                    }else{
                        BookmarkDAO.tambahUrl_InsideFolder(namaUrl.getText(),linkUrl.getText(), location, now);
                    }
                    FXMLLoader loader = new FXMLLoader();
                    now.setBookmark(BookmarkDAO.showBookmarkList(now.getEmail()));
                    loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
                    Parent backHomePage = loader.load();
                    Scene backHome = new Scene(backHomePage);
                    HomeController controller = loader.getController();
                    controller.data(now);
                    controller.show(false,folderTree);
                        
                    Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    app_stage.setScene(backHome);
                    app_stage.show();

                }catch (SQLException | ClassNotFoundException | IOException ex) {
                    Logger.getLogger(NewURLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
        isBack.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
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
            });
       
    }
    
    public void editBookmark(int location ,String nama, String link, final int id,  List<Integer> parent){
        folderTree = parent;
        insertUrlButton.setText("edit");
        namaUrl.setText(nama);
        linkUrl.setText(link);
        for(int i=0;i<now.getTag().size();i++){
            if(now.getTag().get(i).getIdTag()== id){
                namaTag.setItems((ObservableList) namaTag.getSelectionModel().getSelectedItem());
            }
        }
        insertUrlButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                try {
                    Tag t = (Tag) namaTag.getSelectionModel().getSelectedItem();
                    BookmarkDAO.updateBookmark(id, namaUrl.getText(),linkUrl.getText(),now);
                    BookmarkDAO.showBookmarkList(now.getEmail());
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

                } catch (SQLException | ClassNotFoundException | IOException ex) {
                    Logger.getLogger(NewURLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        isBack.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
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
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        
    }    
    
}
