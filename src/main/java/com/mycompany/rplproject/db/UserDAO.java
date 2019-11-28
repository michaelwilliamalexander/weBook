/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.db;

import com.mycompany.rplproject.Bookmark;
import com.mycompany.rplproject.Folder;
import com.mycompany.rplproject.Home.SettingController;
import com.mycompany.rplproject.Tag;
import com.mycompany.rplproject.User;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.swing.JFileChooser;

public class UserDAO {
    
    public static void updatePassword(String password, User user){
        try {
            String sql = "Update Account set password = '"+password+"' where email = '"+user.getEmail()+"'";
            DBUtil.getInstance().dbExecuteUpdate(sql);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void newUser(String email, String password){
        try {
            String sql = "Insert into Account values('"+email+"','"+password+"')";
            DBUtil.getInstance().dbExecuteUpdate(sql);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void dbBackUp(User user){
        String newDB = "jdbc:sqlite:C:/users/natha/documents/"+user.getEmail()+".db";
        //String newDB = "jdbc:sqlite:"+path+"/"+user.getEmail()+".db";
        File dbCheck = new File("C:/users/natha/documents/"+user.getEmail()+".db");
        if(dbCheck.exists()){
            System.out.println("Sdah ada db");
            try{
                String[] tableDataDelete = {"Tag","Folder","URL"};
                Connection forBackup = DriverManager.getConnection(newDB);
                for(int i=0;i<tableDataDelete.length;i++){
                    String delete = "delete from "+tableDataDelete[i]+" where email = '"+user.getEmail()+"'";
                    forBackup.createStatement().execute(delete);
                }
                List<Tag> tag = TagDAO.showTagList(user.getEmail());
                List<Folder> folder = FolderDAO.showFolderList(user.getEmail());
                List<Bookmark> bookmark = BookmarkDAO.showBookmarkList(user.getEmail());
                for(int i=0;i<tableDataDelete.length;i++){
                    String insert;
                    if(tableDataDelete[i]=="Tag")
                        for(int j=0;j<tag.size();j++){
                            insert = "insert into tag values('"+tag.get(j).getIdTag()+"','"+tag.get(j).getNamaTag()+"','"+user.getEmail()+"')";
                            forBackup.createStatement().executeUpdate(insert);
                        }
                    else if(tableDataDelete[i]=="Folder")
                        for(int j=0;j<folder.size();j++){
                            insert = "Insert into folder values('"+folder.get(j).getId()+"','"+folder.get(j).getNama()+"','"+folder.get(j).getId_parent()+"','"+user.getEmail()+"')";
                            forBackup.createStatement().executeUpdate(insert);
                        }
                    else if(tableDataDelete[i]=="URL")
                        for(int j=0;j<bookmark.size();j++){
                            insert = "insert into url values('"+bookmark.get(j).getId()+"','"+bookmark.get(j).getNama()+"','"+bookmark.get(j).getLink()+"','"+bookmark.get(j).getId_folder()+"','"+user.getEmail()+"')";
                            forBackup.createStatement().executeUpdate(insert);
                        }
                    //kurang URL TAG    
                }    
            }catch(SQLException | ClassNotFoundException ex){
                Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("BackUp Successful");
                 alert.setHeaderText("Back up File updated");
                 alert.showAndWait();
            }
        }else{
             try{
                 //pindah ke setting
//                String directory; int returnVal;
//                DirectoryChooser directoryChooser = new DirectoryChooser();
//
//                directoryChooser.setInitialDirectory(new File("C:/"));
//                Stage primaryStage = new Stage();
//                File selectedDirectory = directoryChooser.showDialog(primaryStage);
//
//                String path = selectedDirectory.getAbsolutePath();

                Connection forBackup = DriverManager.getConnection(newDB);
                String tableTag =             "CREATE TABLE \"Tag\" (\n" +
                                                "	\"id_tag\"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                                                "	\"nama_tag\"	TEXT NOT NULL DEFAULT 'General',\n" +
                                                "	\"email\"	TEXT NOT NULL\n" +
                                                ");";

                String tableFolder =            "CREATE TABLE \"Folder\" (\n" +
                                                "	\"id_folder\"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                                                "	\"nama_folder\"	TEXT NOT NULL DEFAULT 'New Folder',\n" +
                                                "	\"Parent_folder\"	INTEGER,\n" +
                                                "	\"email\"	TEXT NOT NULL\n" +
                                                "	" +
                                                ");";
                String tableURL =               "CREATE TABLE \"URL\" (\n" +
                                                "	\"id_url\"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                                                "	\"nama_url\"	TEXT,\n" +
                                                "	\"link_url\"	TEXT NOT NULL,\n" +
                                                "	\"id_folder\"	INTEGER,\n" +
                                                "	\"email\"	TEXT NOT NULL\n" +
                                                "	" +
                                                ");";
                String tableURLTag =             "CREATE TABLE \"UrlTag\" (\n" +
                                                "	\"id_urlTag\"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                                                "	\"id_url\"	INTEGER NOT NULL,\n" +
                                                "	\"id_tag\"	INTEGER NOT NULL,\n" +
                                                "	FOREIGN KEY(\"id_tag\") REFERENCES \"Tag\"(\"id_tag\") On DELETE CASCADE,\n" +
                                                "	FOREIGN KEY(\"id_url\") REFERENCES \"URL\"(\"id_url\") On Delete CASCADE\n" +
                                                ");";

                forBackup.createStatement().execute(tableTag);
                forBackup.createStatement().execute(tableFolder);
                forBackup.createStatement().execute(tableURL);
                forBackup.createStatement().execute(tableURLTag);
                int i;

                //insert data account ke table tag
                List<Tag> tag = TagDAO.showTagList(user.getEmail());
                i=0;
                while(i!=tag.size()){
                    String insert = "Insert into Tag values('"+tag.get(i).getIdTag()+"','"+tag.get(i).getNamaTag()+"','"+user.getEmail()+"')";
                    forBackup.createStatement().executeUpdate(insert);
                    i++;
                }

                //insert data backup ke table folder
                List<Folder> folder = FolderDAO.showFolderList(user.getEmail());
                i=0;
                while(i!=folder.size()){
                    String insert = "Insert into folder values('"+folder.get(i).getId()+"','"+folder.get(i).getNama()+"','"+folder.get(i).getId_parent()+"','"+user.getEmail()+"')";
                    forBackup.createStatement().executeUpdate(insert);
                    i++;
                }

                //insert data backup ke table url
                List<Bookmark> bookmark = BookmarkDAO.showBookmarkList(user.getEmail());
                i=0;
                while(i!=bookmark.size()){
                    String insert = "insert into url values('"+bookmark.get(i).getId()+"','"+bookmark.get(i).getNama()+"','"+bookmark.get(i).getLink()+"','"+bookmark.get(i).getId_folder()+"','"+user.getEmail()+"')";
                    forBackup.createStatement().executeUpdate(insert);
                    i++;
                }


            } catch (SQLException ex) {
                Logger.getLogger(SettingController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("BackUp Successful");
                 alert.setHeaderText("Back up File succesfully created");
                 alert.showAndWait();
             }
        }
       
    }
    public static void dbRestore(User user){
        String newDB = "jdbc:sqlite:C:/users/natha/documents/"+user.getEmail()+".db";
        //String newDB = "jdbc:sqlite:"+path+"/"+user.getEmail()+".db";
        File dbCheck = new File("C:/users/natha/documents/"+user.getEmail()+".db");
        if(dbCheck.exists()){
            
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Restore");
            alert.setHeaderText("No Backup File found!");
            alert.showAndWait();
        }
    }
    
}
