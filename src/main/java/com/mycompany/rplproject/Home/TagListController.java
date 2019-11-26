/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.Home;

import com.mycompany.rplproject.User;
import com.mycompany.rplproject.db.DBUtil;
import com.mycompany.rplproject.db.TagDAO;
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
     List<Integer> folderTree = new ArrayList<>();
    private List<Button> tag = new ArrayList<>();
    private List<Button> edit = new ArrayList<>();
    private List<Button> delete = new ArrayList<>();
    private User now;
    private int location=-1;
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
    private Button tambahTag;
    
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
        folderTree.removeAll(folderTree);
        folderTree.add(0);
        controller.show(false,folderTree);
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
        controller.data(now);
        Scene setting = new Scene(settingPage);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setX(event.getScreenX() - x);
        app_stage.setY(event.getScreenY() - y);
        app_stage.setScene(setting);
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
    public void logOut(MouseEvent event) throws IOException{
        Parent signOutPage = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene signOut = new Scene(signOutPage);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(signOut);
        app_stage.show();
    }

        
    public void displayURL(int id_tag){
        try {
            contentBox.getChildren().clear();
            editBox.getChildren().clear();
            deleteBox.getChildren().clear();
            tag.clear();
            delete.clear();
            edit.clear();
            String sql = "select u.* from url u inner join urltag mg on u.id_url=mg.id_url inner join tag g on g.id_tag=mg.id_tag where g.id_tag ="+id_tag+" and email ='"+now.getEmail()+"' group by u.id_url;";
            ResultSet rs= DBUtil.getInstance().dbExecuteQuery(sql);
            System.out.println(sql);
            while(rs.next()){
                final Button urlTag;
                if(rs.getString("nama_url")==null){
                    urlTag = new Button(rs.getString("link_url"));
                }else{
                    urlTag = new Button(rs.getString("nama_url"));
                }
                urlTag.setMinWidth(400);
                urlTag.setId(String.valueOf(rs.getInt("id_url")));
                urlTag.setBackground(Background.EMPTY);
                
                
                Button editButton = new Button("Edit");
                editButton.setBackground(Background.EMPTY);
                editButton.setId(String.valueOf(rs.getInt("id_url")));
                
                
                final Button deleteButton = new Button("X");
                deleteButton.setId(String.valueOf(rs.getInt("id_url")));
                deleteButton.setBackground(Background.EMPTY);
                deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            String deleteUrl = "delete * from url where id_url = "+deleteButton.getId();
                            DBUtil.getInstance().dbExecuteUpdate(deleteUrl);
                            for(int j=0;j<now.getBookmark().size();j++){
                                if(now.getBookmark().get(j).getId() == Integer.parseInt(deleteButton.getId())){
                                    now.getBookmark().remove(j);
                                }
                            }
                        } catch (SQLException | ClassNotFoundException ex) {
                            Logger.getLogger(TagListController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                
                urlTag.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            String selectUrl = "select * from url where id_url = "+urlTag.getId()+" and email = '"+now.getEmail()+"'";
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
            tag.add(urlTag);
            edit.add(editButton);
            delete.add(deleteButton);
            }
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TagListController.class.getName()).log(Level.SEVERE, null, ex);
        }
        contentBox.getChildren().addAll(tag);
        editBox.getChildren().addAll(edit);
        deleteBox.getChildren().addAll(delete);
    }    
    
    public void addTag(){
        for(int i=0;i<now.getTag().size();i++){
            final int o = i;
            final Button Tag = new Button(now.getTag().get(i).getNamaTag());
            Tag.setId(String.valueOf(now.getTag().get(i).getIdTag()));
            Tag.setMinWidth(400);
            Tag.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    location = Integer.parseInt(Tag.getId());
                    show(false);
                }
            });
            
            final Button Edit = new Button("Edit");
            Edit.setId(String.valueOf(now.getTag().get(i).getIdTag()));
            Edit.setBackground(Background.EMPTY);
            Edit.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/fxml/InputTagList.fxml"));
                        Parent TagListPage = loader.load();
                        Scene TagList = new Scene(TagListPage);
                        InputTagListController controller = loader.getController();
                        controller.data(now);
                        controller.editTag(Integer.parseInt(Edit.getId()));
                        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                        app_stage.setScene(TagList);
                        app_stage.show();
                    } catch (IOException | SQLException | ClassNotFoundException ex) {
                        Logger.getLogger(TagListController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            final Button Delete = new Button("X");
            Delete.setId(String.valueOf(now.getTag().get(i).getIdTag()));
            Delete.setBackground(Background.EMPTY);
            Delete.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete Folder");
                    alert.setHeaderText("Apakah anda ingin menghapus tag ini?");
                    Optional<ButtonType> option = alert.showAndWait();
                    if(option.get() == ButtonType.OK){
                        TagDAO.deleteTag(now.getTag().get(o).getIdTag());
                        for(int j=0;j<now.getTag().size();j++){
                            if(now.getTag().get(j).getIdTag() == Integer.parseInt(Delete.getId())){
                                now.getTag().remove(j);
                                show(false);
                            }
                        }
                    }
                }
            });
            delete.add(Delete);
            edit.add(Edit);
            tag.add(Tag);
        }
        contentBox.getChildren().addAll(tag);
        editBox.getChildren().addAll(edit);
        deleteBox.getChildren().addAll(delete);
    }
    
    public void show(boolean Search){
        tag.clear();
        delete.clear();
        edit.clear();
        contentBox.getChildren().clear();
        editBox.getChildren().clear();
        deleteBox.getChildren().clear();
       if(Search==false){
            if(location < 0){
            //menambah tag kedalam layout
                addTag();
            }else{
                //menampilkan isi tag
                this.displayURL(location);
            }
       }
       SearchTag();
          
    }
        
    public void getBack(final String idTag,String email){
        Button back = new Button("<");
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
                    controller.data(now);
                    controller.show(false);
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
        controller.data(now);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(TagList);
        app_stage.show();
    }
    
    public void SearchTag(){
        tambahTag.setDisable(false);
        btnSearchTag.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                try {
                    tag.clear();
                    contentBox.getChildren().clear();
                    editBox.getChildren().clear();
                    deleteBox.getChildren().clear();
                    String temp= inSearchTag.getText();
                    List<Integer> tamp =  new ArrayList<>();
                    tamp.addAll(TagDAO.searchData(temp, now));
                    for(int i=0;i<now.getTag().size();i++){
                        for(int j=0;j<tamp.size();j++){
                            if(now.getTag().get(i).getIdTag()==tamp.get(j)){
                                final Button Tag = new Button(now.getTag().get(i).getNamaTag());
                                Tag.setId(String.valueOf(now.getTag().get(i).getIdTag()));
                                Tag.setMinWidth(400);
                                Tag.setOnMouseClicked(new EventHandler<MouseEvent>(){
                                   @Override
                                   public void handle(MouseEvent event) {
                                       location = Integer.parseInt(Tag.getId());
                                       show(false);
                                   }
                                });
                                tag.add(Tag);
                           }
                        }
                        
                    }
                    contentBox.getChildren().addAll(tag);
                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(TagListController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
