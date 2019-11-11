package com.mycompany.rplproject.Home;

import com.mycompany.rplproject.Folder;
import com.mycompany.rplproject.User;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;


public class ChangeFolderController implements Initializable {
    @FXML
    private TreeView folderList; 
    private TreeItem<String> user;
    private User now;
    
    @FXML
    private Text namaAkun;
    Map<Integer, TreeItem<String>> folderById = new HashMap<>();
    Map<Integer, Integer> parentsFolder = new HashMap<>();
    List<Folder> folder = now.getFolder();
    
    public void data(User u){
        now = u;
        namaAkun.setText(now.getEmail());
        
        user = new TreeItem<>(now.getEmail());
        folder = now.getFolder();
        user.setExpanded(true);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        for(int i=0;i<folder.size();i++){
            TreeItem<String> namaFolder = new TreeItem<>(folder.get(i).getNama());
            folderById.put(folder.get(i).getId(), namaFolder);
            parentsFolder.put(folder.get(i).getId(), folder.get(i).getId_parent());
        }
        for(Map.Entry<Integer, TreeItem<String>> entry : folderById.entrySet()){
            TreeItem<String> folders = new TreeItem<>();
            TreeItem<String> child = new TreeItem<>();
            if(parentsFolder.get(entry.getKey()).equals(entry.getKey())){
                folders = entry.getValue();
                //folders.setGraphic(new ImageView(folderIcon));
                user.getChildren().add(folders);
            }else{
                TreeItem<String> parentItem = folderById.get(parentsFolder.get(entry.getKey()));
                if(parentItem==null){
                    folders = entry.getValue();
                    //folders.setGraphic(new ImageView(folderIcon));
                    user.getChildren().add(folders);
                }else{
                    child = entry.getValue();
                    //child.setGraphic(new ImageView(folderIcon));
                    parentItem.getChildren().add(child);
                }
            }
        }
        //url tidak didalam folder
        folderList.setRoot(user); 
    }    
    
}
