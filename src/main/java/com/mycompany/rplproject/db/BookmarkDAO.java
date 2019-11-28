/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.db;

import com.mycompany.rplproject.Bookmark;
import com.mycompany.rplproject.User;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;

/**
 *
 * @author Michael William
 */
public class BookmarkDAO {
    
   
    
    public static List<Bookmark> showBookmarkList(String email) throws SQLException, ClassNotFoundException{
        String query = " Select * from url where email = '"+email+"'";
        try {
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(query);
            List<Bookmark> bookmark = BookmarkDAO.getBookmarkList(rs);
            return bookmark;
        } catch (SQLException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
    
     public static List<Bookmark> getBookmarkList(ResultSet rs){
        List<Bookmark> bookmark = FXCollections.observableArrayList();
        try {
            while(rs.next()){
                Bookmark data = new Bookmark(rs.getInt("id_url"), rs.getString("nama_url"), rs.getString("link_url"), rs.getInt("id_folder")); 
                bookmark.add(data);
            }
        }catch (SQLException ex) {
            Logger.getLogger(BookmarkDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bookmark;
    }
    
    public static void tambahUrl_InsideFolder(String namaUrl, String linkUrl, int location, User user){
        try {
            String sql;
            String nama = "NULL";
            if(namaUrl.isEmpty()){
                sql = "insert into URL (link_url, id_folder, email) values ('"+linkUrl+"','"+location+"','"+user.getEmail()+"')";
            }else{
                sql = "insert into URL (nama_url, link_url, id_folder, email) values ('"+namaUrl+"','"+linkUrl+"','"+location+"','"+user.getEmail()+"')"; 
            }
            DBUtil.getInstance().dbExecuteUpdate(sql);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(BookmarkDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void tambahUrl(String namaUrl,String linkUrl, User user){
        try {
            String sqrt;
            if(namaUrl.isEmpty())
                sqrt = "Insert into URL (link_url, email) values ('"+linkUrl+"','"+user.getEmail()+"')";
            else
                sqrt = "Insert into URL (nama_url, link_url, email) values ('"+namaUrl+"','"+linkUrl+"','"+user.getEmail()+"')";
            DBUtil.getInstance().dbExecuteUpdate(sqrt);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(BookmarkDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
    
    public static void deleteBookmark(int id){
         try {
            String query = "delete from url where id_url = '"+id+"'";
            DBUtil.getInstance().dbExecuteUpdate(query);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void updateBookmark(int id,String namaURL, String linkUrl,User user){
        try {
            String query;
            if(namaURL.isEmpty()){
                query = " Update url set nama_url = NULL , link_url = '"+linkUrl+"'"
                    + "where id_url = '"+id+"'";
            }else{
               query = " Update url set nama_url = '"+namaURL+"', link_url = '"+linkUrl+"'"
                    + "where id_url = '"+id+"'";
            }
            
            for(int i=0;i<user.getBookmark().size();i++){
                if(user.getBookmark().get(i).getId()==id){
                    user.getBookmark().get(i).setLink(linkUrl);
                    user.getBookmark().get(i).setNama(namaURL);
                }
            }
            DBUtil.getInstance().dbExecuteUpdate(query);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static List<Integer> searchBookmark(String dataSearch, User user) throws ClassNotFoundException, SQLException{
        String tempt = null;
        for(int i=0;i<user.getBookmark().size();i++){
            if(user.getBookmark().get(i).getNama().equals(null)){
                tempt = "Select * from URL where link_url like '%"+dataSearch+"%' and email = '"+user.getEmail()+"'";
            }else{
                tempt = "Select * from URL where nama_url like '%"+dataSearch+"%'and email = '"+user.getEmail()+"'";
            }
        }
        ResultSet rs = DBUtil.getInstance().dbExecuteQuery(tempt);
        List<Integer> data = searchBookmarkrData(rs);
        return data;
    }
    
     private static List<Integer> searchBookmarkrData(ResultSet rs){
        List<Integer>data = FXCollections.observableArrayList();
        try {
            while (rs.next()){
                data.add(rs.getInt("id_url"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FolderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }
     
     public static void clickToGo(User user, int id) {
        try {
            String selectUrl = "select * from url where id_url = "+id+" and email = '"+user.getEmail()+"'";
            Desktop d = Desktop.getDesktop();
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(selectUrl);
            if(rs.next()){
                d.browse(new URI(rs.getString("link_url")));
            }
        } catch (SQLException | ClassNotFoundException | URISyntaxException | IOException ex) {
            Logger.getLogger(BookmarkDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
     public static int idUrl_withNamaURL(String nama, User user) throws SQLException, ClassNotFoundException{
         String sql = "Select * from url where nama_url = '"+nama+"' and email = '"+user.getEmail()+"'";
         ResultSet rs = DBUtil.getInstance().dbExecuteQuery(sql);
         int data = getId(rs);
         return data;
     }
     
     private static int getId(ResultSet rs) throws SQLException{
         int data = 0; 
         while(rs.next()){
             data = rs.getInt("id_url");
         }
        return data;
     }
      public static int idUrl_withLinkURL(String nama, User user) throws SQLException, ClassNotFoundException{
         String sql = "Select * from url where link_url = '"+nama+"' and email = '"+user.getEmail()+"'";
         ResultSet rs = DBUtil.getInstance().dbExecuteQuery(sql);
         int data = getId(rs);
         return data;
     }
}
