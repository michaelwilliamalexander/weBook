/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.Home;

import com.mycompany.rplproject.Bookmark;
import com.mycompany.rplproject.Tag;
import com.mycompany.rplproject.UrlTag;
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
    private List<ComboBox> newTag= new ArrayList<>();
    
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
    private ComboBox namaTag1;
    
    @FXML
    private Button insertUrlButton;
    
    @FXML
     Button isBack;
    
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
    
    public void setChildComboBox(final List<Tag> tagLists,int o) throws SQLException, ClassNotFoundException{
        final int count = o;
        plusTag.setOnMouseClicked(new EventHandler<MouseEvent>(){
            List<Tag> list = new ArrayList<>();
            ObservableList<Tag> listData = FXCollections.observableArrayList();
            ComboBox combo = new ComboBox();
            @Override
            public void handle(MouseEvent event) {
                list.clear();
                list.addAll(tagLists);
                if(list.size()>=0 && count<1){
                    contentTag.getChildren().clear();
                    try {
                        for(int i=0;i<list.size();i++){
                            if(count==0){
                                if(namaTag.getSelectionModel().isSelected(i)==true){
                                    System.out.println("List data: "+list.get(i).getNamaTag());
                                    list.remove(list.get(i));
                                    listData.addAll(list);
                                    namaTag.setDisable(true);
                                }
                            }else{
                                System.out.println("Size tag baru: "+newTag.size());
                                for(int j=0;j<newTag.size();j++){
                                    if(newTag.get(j).getSelectionModel().isSelected(i)==true){
                                        System.out.println("List data: "+list.get(i).getNamaTag());
                                        list.remove(list.get(i));
                                        listData.addAll(list);
                                    }
                                }
                            }
                        }
                        combo.setItems(listData);
                        newTag.add(combo);
                        int datas = count+1;
                        contentTag.getChildren().addAll(newTag);
                        setChildComboBox(listData,datas);
                    } catch (SQLException | ClassNotFoundException ex) {
                        Logger.getLogger(NewURLController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    plusTag.setDisable(true);
                    System.out.println("Ini data Terakhit: "+newTag.size());
                    for(int i=0;i<newTag.size();i++){
                        System.out.println("Ini data ke "+i+newTag.get(i).getValue());
                    }
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
                                 if(namaTag.getSelectionModel().isSelected(i)==true){
                                    int idTag = now.getTag().get(i).getIdTag();
                                    MultiTagDAO.putData(idUrl, idTag, now);
                                    now.getTag().remove(now.getTag().get(i));
                                    System.out.println("Berhasil");
                                 }
                             }for(int i=0;i<now.getTag().size();i++){
                               System.out.println("Ini jumlah tag barunya"+newTag.size());
                                for(int j=0;j<newTag.size();j++){
                                    System.out.println(newTag.size());
                                    if(newTag.get(j).getSelectionModel().isSelected(i)==true){
                                        int idTag = now.getTag().get(i).getIdTag();
                                        System.out.println("Ini data tag yang ke ambil: "+newTag.get(j).getValue());
                                        MultiTagDAO.putData(idUrl, idTag, now);
                                        now.getTag().remove(now.getTag().get(i));
                                        System.out.println("Berhasil");
                                    }
                                } 
                             }
                            newTag.clear();
                            now.setUrlTag(MultiTagDAO.urlTag(now));
                            now.setTag(TagDAO.getAllData(now));
                        }else{
                            BookmarkDAO.tambahUrl_InsideFolder(namaUrl.getText(),linkUrl.getText(), location, now);
                            int idUrl = 0;
                            if(namaUrl.getText().isEmpty()){
                                idUrl = BookmarkDAO.idUrl_withLinkURL(linkUrl.getText(), now);
                            }else{
                                idUrl = BookmarkDAO.idUrl_withNamaURL(namaUrl.getText(), now);
                            }
                            for(int i=0;i<now.getTag().size();i++){
                                 if(namaTag.getSelectionModel().isSelected(i)==true){
                                    int idTag = now.getTag().get(i).getIdTag();
                                    MultiTagDAO.putData(idUrl, idTag, now);
                                    now.getTag().remove(now.getTag().get(i));
                                    System.out.println("Berhasil");
                                 }
                             }for(int i=0;i<now.getTag().size();i++){
                               System.out.println("Ini jumlah tag barunya"+newTag.size());
                                for(int j=0;j<newTag.size();j++){
                                    System.out.println(newTag.size());
                                    if(newTag.get(j).getSelectionModel().isSelected(i)==true){
                                        int idTag = now.getTag().get(i).getIdTag();
                                        System.out.println("Ini data tag yang ke ambil: "+newTag.get(j).getValue());
                                        MultiTagDAO.putData(idUrl, idTag, now);
                                        now.getTag().remove(now.getTag().get(i));
                                        System.out.println("Berhasil");
                                    }
                                } 
                             }
                             newTag.clear();
                             now.setUrlTag(MultiTagDAO.urlTag(now));
                             now.setTag(TagDAO.getAllData(now));
                        }
                        now.setUrlTag(MultiTagDAO.urlTag(now));
                        System.out.println("Darta Url Tag : "+now.getUrlTag().size());
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
    
    public void editBookmark(int location ,String nama, String link, final int id,  List<Integer> parent) throws SQLException, ClassNotFoundException{
        folderTree = parent;
        insertUrlButton.setText("edit");
        namaUrl.setText(nama);
        linkUrl.setText(link);
        now.setUrlTag(MultiTagDAO.urlTag(now));
        List<Integer> tampt = new ArrayList<>();
        final List<Bookmark> count = new ArrayList<>();
        
        if(nama.equals("")){
            tampt = MultiTagDAO.getTag_withUrlLink(link, now);
        }else{
            tampt =  MultiTagDAO.getTag_withNamaLink(nama, now);
        }
        for(int i=0;i<tampt.size();i++){
            count.add(MultiTagDAO.TagData(now,tampt.get(i)));
        }
        System.out.println(count.size());
        String data = null;
        System.out.println(now.getUrlTag().size());
        System.out.println(tampt.size());
        System.out.println(now.getTag().size());
        if(tampt.size()<2){
                data = TagDAO.getStringData(tampt.get(0));
                namaTag.setValue(data);
                setChildComboBox(now.getTag(),0);
        }else{
            for(int i=0;i<now.getUrlTag().size();i++){
                System.out.println("Ini data urlTagnya : "+now.getUrlTag().get(i).getIdTag());
                    for(int j=0;j<tampt.size();j++){
                        if(now.getUrlTag().get(i).getIdTag()== tampt.get(j)){
                            if(j==0){
                                    data = TagDAO.getStringData(tampt.get(0));
                                    namaTag.setValue(data);
                                    for(int k=0;k<now.getTag().size();k++){
                                       if(now.getTag().get(k).getIdTag() == tampt.get(0)){
                                           now.getTag().remove(now.getTag().get(k));
                                       }
                                    }
                                    plusTag.setDisable(true);
                            }else{
                                contentTag.getChildren().clear();
                                ObservableList<Tag> listData = FXCollections.observableArrayList();
                                ComboBox combo = new ComboBox();
                                listData.addAll(now.getTag());
                                combo.setItems(listData);
                                data = TagDAO.getStringData(tampt.get(j));
                                combo.setValue(data);
                                newTag.add(combo);
                                contentTag.getChildren().addAll(newTag);
                                break;
                        } 
                    }
                }  
            }
        }

        insertUrlButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Datas : "+now.getUrlTag().size());
                if(!namaUrl.getText().isEmpty()||!linkUrl.getText().isEmpty()){
                    try {
                        now.setTag(TagDAO.getAllData(now));
                        for(int j=0;j<now.getUrlTag().size();j++){
                            for(int k=0;k<count.size();k++){
                                for(int i=0;i<now.getTag().size();i++){
                                    if(namaTag.getSelectionModel().isSelected(i)==true){
                                        int idUrl = count.get(k).getId();
                                        int idTag = now.getTag().get(i).getIdTag();
                                        if(now.getUrlTag().get(j).getIdTag()==idTag &&now.getUrlTag().get(j).getIdUrl()==idUrl){
                                            MultiTagDAO.updateData(now.getTag().get(i).getIdTag(), idUrl);
                                        }
                                        now.getTag().remove(now.getTag().get(i));
                                        System.out.println("Berhasil");
                                    }
                                     }for(int i=0;i<now.getTag().size();i++){
                                       System.out.println("Ini jumlah tag barunya"+newTag.size());
                                        for(int l=0;l<newTag.size();l++){
                                            System.out.println(newTag.size());
                                            if(newTag.get(l).getSelectionModel().isSelected(i)==true){
                                                int idUrl = count.get(k).getId();
                                                int idTag = now.getTag().get(i).getIdTag();
                                                System.out.println("Ini data tag yang ke ambil: "+newTag.get(j).getValue());
                                                if(now.getUrlTag().get(j).getIdTag()==idTag &&now.getUrlTag().get(j).getIdUrl()==idUrl){
                                                    MultiTagDAO.updateData(now.getTag().get(i).getIdTag(), idUrl);
                                                }
                                                now.getTag().remove(now.getTag().get(i));
                                                System.out.println("Berhasil");
                                        }
                                    } 
                                }
                            }
                        }
                        newTag.clear();
                        now.setUrlTag(MultiTagDAO.urlTag(now));
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
      
        
    }    
    
    
}
