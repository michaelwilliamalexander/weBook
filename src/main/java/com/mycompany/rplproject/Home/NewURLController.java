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
    
    public void setChildComboBox(final List<Tag> tagLists,int o) throws SQLException, ClassNotFoundException{
        final int count = o;
        plusTag.setOnMouseClicked(new EventHandler<MouseEvent>(){
            List<Tag> list = new ArrayList<>();
            ObservableList<Tag> listData = FXCollections.observableArrayList();
            ComboBox combo = new ComboBox();
            @Override
            public void handle(MouseEvent event) {
                if(list.size()>=0 && count<1){
                    contentTag.getChildren().clear();
                    list.clear();
                    list.addAll(tagLists);
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
                                    now.setUrlTag(MultiTagDAO.urlTag(now));
                                    System.out.println("Berhasil");
                                 }else{
                                     System.out.println("Ini jumlah tag barunya"+newTag.size());
                                     for(int j=0;j<newTag.size();j++){
                                         System.out.println(newTag.size());
                                         if(!newTag.get(j).getSelectionModel().isEmpty()){
                                            int idTag = now.getTag().get(i).getIdTag();
                                            System.out.println("Ini data tag yangkeambil: "+newTag.get(j).getValue());
                                            MultiTagDAO.putData(idUrl, idTag, now);
                                            System.out.println("Berhasil");
                                         }
                                     }newTag.clear();
                                     now.setUrlTag(MultiTagDAO.urlTag(now));
                                 }
                             }
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
                                    System.out.println("Berhasil");
                                 }
                             }
                        }
                        now.setUrlTag(MultiTagDAO.urlTag(now));
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
//        plusTag.setOnMouseClicked(new EventHandler<MouseEvent>(){
//            int count = 0;
//            @Override
//            public void handle(MouseEvent event) {
//                if(count==0){
//                    namaTag.setDisable(true);
//                    namaTag1.setVisible(true);
//                    count++;
//                }else if(count==1){
//                    namaTag1.setDisable(true);
//                    namaTag2.setVisible(true);
//                    count++;
//                }else{
//                    plusTag.setDisable(true);
//                }
//            }
//            
//        });
       
    }
    
    public void editBookmark(int location ,String nama, String link, final int id,  List<Integer> parent) throws SQLException, ClassNotFoundException{
        folderTree = parent;
        insertUrlButton.setText("edit");
        namaUrl.setText(nama);
        linkUrl.setText(link);
        try{
        ObservableList<Tag> isi=TagDAO.showTagList(now.getEmail());
        List<Bookmark> data = BookmarkDAO.showBookmarkList(now.getEmail());
        List<Tag> taglist = TagDAO.showTagList(now.getEmail());
        List<UrlTag> manyTag = MultiTagDAO.urlTag(now);
        List<Integer> counter = new ArrayList<>();
        final int count = counter.size();
        for(int i=0;i<data.size();i++){
            for(int j=0;j<manyTag.size();j++){
                if(data.get(i).getId() == manyTag.get(j).getIdUrl()){
                    counter.add(manyTag.get(j).getIdTag());
                }
            }
        }
        for(int i=0;i<isi.size();i++){
           for(int j=0;j<counter.size();j++){
               if(isi.get(i).getIdTag()== counter.get(i)){
                   if(counter.size()==1){
                       this.namaTag.setValue(isi.get(i).getNamaTag());
                       taglist.remove(isi.get(i).getNamaTag());
                   }
                   else{
                       contentTag.getChildren().clear();
                       
                   }
               }
           }
        }
        now.setUrlTag(MultiTagDAO.urlTag(now));
        }catch(Exception e){
            e.printStackTrace();
        }
        
        insertUrlButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(!namaUrl.getText().isEmpty()||!linkUrl.getText().isEmpty()){
                    try {
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
