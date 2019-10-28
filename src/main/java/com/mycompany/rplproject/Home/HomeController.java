/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.Home;

import com.mycompany.rplproject.Home.SettingController;
import com.mycompany.rplproject.db.DBUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;

public class HomeController implements Initializable {
    private List<String> listOfSomething = null;
    private ListView<Button> listview;
    private ObservableList<Button> folders = FXCollections.observableArrayList();
    private ObservableList<String> list = FXCollections.observableArrayList("Tag","Folder");
    private String data;
    
    @FXML
    private Text namaAkun;
    
    @FXML
    private Text settings;
    
    @FXML
    private Button tambahFolder;
    
    @FXML
    private ComboBox<String> ComboName;

    @FXML
    private Text logOut;
    double x,y;
    @FXML
    private VBox contentBox;
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
    //ambil data email
    public void data(String s){
        data = s;
        namaAkun.setText(s);
    }

    public void fillCombo(){
        ComboName.setItems(list);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.fillCombo();
        String sqlQuery = "SELECT * FROM Folder where parent_folder is NULL;";
        //VBox vbox = new VBox();
        //vbox.setPrefWidth(100); digunakan meenyamakan panjang button
        List<Text> textlist = new ArrayList<>(); //our Collection to hold newly created Buttons
        try {
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(sqlQuery);
            while (rs.next()) { //iterate over every row returned
                String folderName = rs.getString("nama_folder"); //extract button text, adapt the String to the columnname that you are interested in
                textlist.add(new Text(folderName));
            }
            contentBox.getChildren().clear();
            contentBox.getChildren().addAll(textlist);
            //vbox.getChildren().addAll(buttonlist); //tadi buat nmenyamakan panjang button
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
