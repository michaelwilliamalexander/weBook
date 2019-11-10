/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.Home;

import com.mycompany.rplproject.Tag;
import com.mycompany.rplproject.db.DBUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private Vector<String> v = new Vector();

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
        controller.data(data);
        controller.show(v,data);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(backHome);
        app_stage.show();
    }
     public void data(String s){
        data = s;
        namaAkun.setText(s);
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
        controller.data(data);
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
        controller.data(data);
        controller.show(null, data);
        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(tagList);
        app_stage.show();
    }
    
    //fungsi menampilkan list folder dan tag yang ada ke combo box
    public void setComboBoxValue() throws SQLException{
        String queryTag = "select * from tag where email = '"+data+"' or id_tag=0";
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
    
    public void tambahURL(Vector<String> parent,String s){
        v = parent;
        insertUrlButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                 Tag t = (Tag) namaTag.getSelectionModel().getSelectedItem();
                String SqlQuery;
                if(v.size()==0){
                    SqlQuery = "insert into URL (nama_url, link_url, id_tag, email) values ('"+namaUrl.getText()+"','"+linkUrl.getText()+"',"+t.getIdTag()+",'"+data+"')";
                }else{
                    SqlQuery = "insert into URL (nama_url, link_url, id_tag,id_folder, email) values ('"+namaUrl.getText()+"','"+linkUrl.getText()+"',"+t.getIdTag()+",'"+v.get(v.size()-1)+"','"+data+"')";
                }
                try{
                    DBUtil.getInstance().dbExecuteUpdate(SqlQuery);
                    System.out.println("input berhasil heheheheee");

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
                        Parent backHomePage = loader.load();
                        Scene backHome = new Scene(backHomePage);
                        HomeController controller = loader.getController();
                        controller.data(data);
                        controller.show(v,data);
                        if(v.size()!=0){
                                controller.getBack(v,data);
                        }
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
                        controller.data(data);
                        controller.show(v,data);
                        if(!v.isEmpty()){
                            controller.getBack(v,data);
                        }
                        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                        app_stage.setScene(backHome);
                        app_stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(NewURLController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
       
    }
    
    public void editBookmark(Vector<String>parent,String nama, String link, final int id, final int tag){
        try {
            v=parent;
            insertUrlButton.setText("edit");
            namaUrl.setText(nama);
            linkUrl.setText(link);
            String sql = "select * from tag where id_tag = "+tag;
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(sql);
            if(rs.next()){
                namaTag.setValue(new Tag(rs.getInt("id_tag"), rs.getString("nama_tag")));
                setComboBoxValue(); 
            }
            //ketika button diklik akan melakukan update
            insertUrlButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    try {
                        Tag t = (Tag) namaTag.getSelectionModel().getSelectedItem();
                        String sql = "update url set nama_url='"+namaUrl.getText()+"', link_url='"+linkUrl.getText()+"', id_tag ="+t.getIdTag()+" where id_url = "+id;
                        DBUtil.getInstance().dbExecuteUpdate(sql);
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/fxml/Home.fxml"));
                        Parent backHomePage = loader.load();
                        Scene backHome = new Scene(backHomePage);
                        HomeController controller = loader.getController();
                        controller.data(data);
                        controller.show(v,data);
                        if(!v.isEmpty()){
                                controller.getBack(v,data);
                        }
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
                        controller.data(data);
                        controller.show(v,data);
                        if(!v.isEmpty()){
                            controller.getBack(v,data);
                        }
                        Stage app_stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                        app_stage.setScene(backHome);
                        app_stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(NewURLController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(NewURLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        
    }    
    
}
