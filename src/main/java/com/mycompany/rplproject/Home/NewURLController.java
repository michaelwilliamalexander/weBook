/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.Home;

import com.mycompany.rplproject.Tag;
import com.mycompany.rplproject.User;
import com.mycompany.rplproject.db.BookmarkDAO;
import com.mycompany.rplproject.db.MultiTagDAO;
import com.mycompany.rplproject.db.TagDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
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
    private ComboBox newTag;
    
     @FXML
    private VBox contentTag;
    
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
    private Button plusTag;
    
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
        try {
            namaTag.setItems(TagDAO.showTagList(now.getEmail()));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NewURLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setChildComboBox(final List<Tag> tagList,int counter) throws SQLException, ClassNotFoundException{
        final int count = counter;
            plusTag.setOnMouseClicked(new EventHandler<MouseEvent>(){
                List<Tag> listTag = new ArrayList<>();
                ObservableList newCombo = FXCollections.observableArrayList();
                    @Override   
                    public void handle(MouseEvent event) {
                        listTag.clear();
                        listTag.addAll(tagList);
                        newTag= new ComboBox();
                        if(count<2){
                            for(int i=0;i<now.getTag().size();i++){
                                 if(count==0){
                                     if(namaTag.getSelectionModel().isSelected(i)==true){
                                        System.out.println("Ini datanya : "+listTag.get(i));
                                        listTag.remove(listTag.get(i));
                                        newCombo.setAll(listTag);
                                     }
                                 }else{
                                    newCombo.setAll(listTag);
                                 }
                           }
                            newTag.setItems(newCombo);
                            System.out.println("masuk");
                            newTag.setMinWidth(namaTag.getWidth());
                            contentTag.getChildren().addAll(newTag);
                            int data = count+1;
                            System.out.println("Ini sizenya : "+ data);
                            try {
                                setChildComboBox(listTag,data);
                            } catch (SQLException | ClassNotFoundException ex) {
                                Logger.getLogger(NewURLController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }else{
                            plusTag.setDisable(true); 
                        }
                    }
                });
    }
    
    public void tambahURL(List<Integer> parent, String s){
        folderTree = parent;
        final int location = parent.get(parent.size()-1);
        //insert url, tapi blm bisa sama tag
        insertUrlButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(!namaUrl.getText().isEmpty()||!linkUrl.getText().isEmpty()){
                    Tag t = (Tag) namaTag.getSelectionModel().getSelectedItem();
                    String SqlQuery;
                    try{
                        if(location==0){
                            BookmarkDAO.tambahUrl(namaUrl.getText(),linkUrl.getText(), now);
                            int idUrl = 0;
                            if(namaUrl.getText().isEmpty()){
                                idUrl = BookmarkDAO.idUrl_withLinkURL(linkUrl.getText(), now);
                            }else{
                                idUrl = BookmarkDAO.idUrl_withNamaURL(namaUrl.getText(), now);
                            }
                            for(int i=0;i<now.getTag().size();i++){
                                 if(namaTag.getSelectionModel().isSelected(i)==true || newTag.getSelectionModel().isSelected(i)==true ){
                                     int idTag = now.getTag().get(i).getIdTag();
                                     MultiTagDAO.putData(idUrl, idTag, now);
                                     System.out.println("Berhasil");
                                 }
                             }
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
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Add Url");
                    alert.setHeaderText("Pastikan nama url atau link url berisi !");
                    alert.showAndWait();
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
        SingleSelectionModel<Integer> count = namaTag.getSelectionModel();
        for(int i=0;i<now.getTag().size();i++){
            if(now.getTag().get(i).getIdTag()== id){
                
            }
        }
        insertUrlButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(!namaUrl.getText().isEmpty()||!linkUrl.getText().isEmpty()){
                    try {
                        Tag t = (Tag) namaTag.getSelectionModel().getSelectedItem();
                        BookmarkDAO.updateBookmark(id, namaUrl.getText(),linkUrl.getText(),now);
                        now.setBookmark(BookmarkDAO.showBookmarkList(now.getEmail()));
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
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Add Url");
                    alert.setHeaderText("Pastikan nama url atau link url berisi !");
                    alert.showAndWait();
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
