/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.db;

import com.mycompany.rplproject.Bookmark;
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
    
    public static Bookmark showAllBookmark(String email) throws SQLException, ClassNotFoundException{
        String query = " Select * from url where email = '"+email+"'";
        try {
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(query);
            Bookmark bookmark = BookmarkDAO.getAllBookmark(rs);
            return bookmark;
        } catch (SQLException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
    
    public static Bookmark getAllBookmark(ResultSet rs){
        Bookmark bookmark = null;
        try {
            while(rs.next()){
                bookmark = new Bookmark(rs.getInt("id_url"), rs.getString("nama_url"), rs.getString("link_url"), rs.getInt("id_folder")); 
            }
        }catch (SQLException ex) {
            Logger.getLogger(BookmarkDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bookmark;
    }
    
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
        List<Bookmark> bookmark = FXCollections.observableArrayList();;
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
    
    public static void deleteBookmark(int id, String email){
         try {
            String query = "delete from bookmark where id_url = '"+id+"' && email = '"+email+"'";
            DBUtil.getInstance().dbExecuteUpdate(query);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void updateBookmark(int id,String email, String dataUpdate){
        try {
            String query =" Update from bookmark set nama_url = '"+dataUpdate+"' "
                    + "where id_url = '"+id+"' && email = '"+email+"'";
            DBUtil.getInstance().dbExecuteUpdate(query);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
