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
    private static void createNewDB(User user){
        try {
            String newDB = "jdbc:sqlite:"+user.getBackupPath()+"/"+user.getEmail()+".db";
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
                    "       \"email\"       TEXT NOT NULL,\n"   +
                    "	FOREIGN KEY(\"id_tag\") REFERENCES \"Tag\"(\"id_tag\") On DELETE CASCADE,\n" +
                    "	FOREIGN KEY(\"id_url\") REFERENCES \"URL\"(\"id_url\") On Delete CASCADE\n" +
                    ");";
            
            forBackup.createStatement().execute(tableTag);
            forBackup.createStatement().execute(tableFolder);
            forBackup.createStatement().execute(tableURL);
            forBackup.createStatement().execute(tableURLTag);
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static void insertBackupDataintoBackupDB(User user){
        try {
            String newDB = "jdbc:sqlite:"+user.getBackupPath()+"/"+user.getEmail()+".db";
            System.out.println(newDB);
            Connection forBackup = DriverManager.getConnection(newDB);
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
            
            //insert relasi tag dan url
            String selectRelation = "select * from urltag where email = '"+user.getEmail()+"'";
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(selectRelation);
            while(rs.next()){
                String insertRelation = "insert into urltag values("+rs.getInt("id_urlTag")+","+rs.getInt("id_url")+","+rs.getInt("id_tag")+",'"+user.getEmail()+"')";
                forBackup.createStatement().executeUpdate(insertRelation);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    public static void dbBackUp(User user){
        String newDB = "jdbc:sqlite:"+user.getBackupPath()+"/"+user.getEmail()+".db";
        File dbCheck = new File(user.getBackupPath()+"/"+user.getEmail()+".db");
        if(dbCheck.exists()){
            try{
                String[] tableDataDelete = {"Tag","Folder","URL","urltag"};
                Connection forBackup = DriverManager.getConnection(newDB);
                
                for(int i=0;i<tableDataDelete.length;i++){
                    String delete = "delete from "+tableDataDelete[i]+" where email = '"+user.getEmail()+"'";
                    forBackup.createStatement().execute(delete);
                }
                insertBackupDataintoBackupDB(user);
            }catch(SQLException ex){
                Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("BackUp Successful");
                 alert.setHeaderText("Back up File updated");
                 alert.showAndWait();
            }
        }else{
             try{
                Connection forBackup = DriverManager.getConnection(newDB);
                createNewDB(user);
                insertBackupDataintoBackupDB(user);
            } catch (SQLException ex) {
                Logger.getLogger(SettingController.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("BackUp Successful");
                 alert.setHeaderText("Back up File succesfully created");
                 alert.showAndWait();
             }
        }
    }
    
    //proses insert dari backup ke db utama
    private static User RestoreInsertintoDB(User user){
        try {
            String newDB = "jdbc:sqlite:"+user.getBackupPath()+"/"+user.getEmail()+".db";
            Connection forBackup = DriverManager.getConnection(newDB);
            String[] tableList = {"urltag","url","tag","folder"};
            for(int i=0;i<tableList.length;i++){
                String deleteData = "delete from "+tableList[i]+" where email = '"+user.getEmail()+"'";
                DBUtil.getInstance().dbExecute(deleteData);
            }
        //masukan data hasil backup
            String selectTag = "Select * from tag";
            String selectUrl = "Select * from Url";
            String selectFolder = "Select * from folder";
            String selectRelation = "select * from urltag";

            //insert tag backup ke db utama
            ResultSet rs = forBackup.createStatement().executeQuery(selectTag);
            while(rs.next()){
                String insert = "insert into tag values("+rs.getInt("id_tag")+",'"+rs.getString("nama_tag")+"','"+user.getEmail()+"')";
                DBUtil.getInstance().dbExecuteUpdate(insert);
            }
            //insert folder backup ke db utama
            rs = null; rs = forBackup.createStatement().executeQuery(selectFolder);
            while(rs.next()){
                String insert = "insert into folder values("+rs.getInt("id_folder")+",'"+rs.getString("nama_folder")+"',"+rs.getInt("parent_folder")+",'"+user.getEmail()+"')";
                DBUtil.getInstance().dbExecuteUpdate(insert);
            }
            //insert url backup ke db utama
            rs = null; rs = forBackup.createStatement().executeQuery(selectUrl);
            while(rs.next()){
                String insert = "insert into url values("+rs.getInt("id_url")+",'"+rs.getString("nama_url")+"','"+rs.getString("link_url")+"','"+rs.getString("id_folder")+"','"+user.getEmail()+"')";
                DBUtil.getInstance().dbExecuteUpdate(insert);
            }
            //insert relasi url dan tag
            rs = null; rs = forBackup.createStatement().executeQuery(selectRelation);
            while(rs.next()){
                String insert = "insert into urltag values("+rs.getInt("id_urltag")+","+rs.getInt("id_url")+","+rs.getInt("id_tag")+",'"+user.getEmail()+"')";
                DBUtil.getInstance().dbExecuteUpdate(insert);
            }
            user.setBookmark(BookmarkDAO.showBookmarkList(user.getEmail()));
            user.setFolder(FolderDAO.showFolderList(user.getEmail()));
            user.setTag(TagDAO.showTagList(user.getEmail()));
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return user;
        }
    }
    
    public static User dbRestore(User user){
        String newDB = "jdbc:sqlite:"+user.getBackupPath()+"/"+user.getEmail()+".db";
        //String newDB = "jdbc:sqlite:"+path+"/"+user.getEmail()+".db";
        File dbCheck = new File(user.getBackupPath()+"/"+user.getEmail()+".db");
        if(dbCheck.exists()){
            try {
                Connection forBackup = DriverManager.getConnection(newDB);
                user = RestoreInsertintoDB(user);
            } catch (SQLException ex) {
                Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Restore Successful");
                alert.setHeaderText("Restored your previous data");
                alert.showAndWait();
                return user;
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Restore Failed");
            alert.setHeaderText("No Backup File found!");
            alert.showAndWait();
        }
        return user;
    }
    
    public static String showBackupPath(String email){
        String path = null;
        try {
            String query = "select BackUpPath from account where email = '"+email+"'";
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(query);
            if(rs.next())
                path = rs.getString("BackUpPath");
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            return path;
        }
    }
    
    public static void updatePath(String email, String path){
        try {
            String update = "update account set BackUpPath = '"+path+"' where email = '"+email+"'";
            DBUtil.getInstance().dbExecuteUpdate(update);
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
