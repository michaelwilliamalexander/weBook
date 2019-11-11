package com.mycompany.rplproject.Home;

import com.mycompany.rplproject.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;


public class ChangeFolderController implements Initializable {
    @FXML
    private TreeView folderList; 
    private TreeItem<String> user;
    private User now;
    
    public void data(User u){
        
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
