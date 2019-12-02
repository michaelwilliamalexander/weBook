/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.db;

import com.mycompany.rplproject.Tag;
import com.mycompany.rplproject.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author Michael William
 */
public class TagDAO {
    
    public static ObservableList showTagList(String email) throws SQLException, ClassNotFoundException {
        String selectStmt = "select * from tag where email ='"+email+"' or id_Tag =0";
        try {
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(selectStmt);
            ObservableList mhsList = getTagList(rs);
            return mhsList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e); //Return exception
            throw e;
        }
    }

    private static ObservableList getTagList(ResultSet rs) throws SQLException, ClassNotFoundException {
        ObservableList list = FXCollections.observableArrayList();
        while (rs.next()) {
           Tag tag = new Tag(rs.getInt("id_tag"), rs.getString("nama_tag")); 
            list.add(tag);
        }
        return list;
    }
    
    public static void deleteTag(int id){
        try {
            String query = "delete from tag where id_tag = '"+id+"'";
            DBUtil.getInstance().dbExecuteUpdate(query);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void updateTag(int id,String dataUpdate){
        try {
            String query =" Update Tag set nama_tag = '"+dataUpdate+"' where id_tag = '"+id+"'";
            DBUtil.getInstance().dbExecuteUpdate(query);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String getStringData(int idTag) throws ClassNotFoundException, SQLException{
        String getData = null;
        String sql ="Select * from Tag where id_tag = '"+idTag+"'";
        ResultSet rs = DBUtil.getInstance().dbExecuteQuery(sql);
        getData = getData(rs);
        return getData;
    }
    
    public static String getStringData(String namaTag,String email) throws SQLException, ClassNotFoundException{
        String tempt = null;
        String sql = "Select * from tag where nama_tag = '"+namaTag+"' and email = '"+email+"'";
        ResultSet rs = DBUtil.getInstance().dbExecuteQuery(sql);
        tempt = getData(rs);
        return tempt;
    }
    
    private static String getData(ResultSet rs) throws SQLException{
        String tempt= null;
        while(rs.next()){
            tempt = rs.getString("nama_tag");
        }
         return tempt;
    }
    
    public static void tambahTag(String namaTag, User user) throws SQLException, ClassNotFoundException{
        String sql = "Insert into Tag(nama_tag,email) values('"+namaTag+"','"+user.getEmail()+"')";
        DBUtil.getInstance().dbExecuteUpdate(sql);
        
    }
    
    public static List<Integer> searchData(String search, User user) throws SQLException, ClassNotFoundException{
        String sql = "Select * from Tag where nama_tag like '%"+search+"%' and email = '"+user.getEmail()+"'";
        ResultSet rs = DBUtil.getInstance().dbExecuteQuery(sql);
        List<Integer> data = search(rs);
        return data;
    }
    
    private static List<Integer> search(ResultSet rs) throws SQLException{
        List<Integer> data =  FXCollections.observableArrayList();
        while(rs.next()){
            data.add(rs.getInt("id_tag"));
        }
        return data;
    }
    
    public static int idTag(String nama, User user) throws SQLException, ClassNotFoundException{
         String sql = "Select * from tag where nama_tag = '"+nama+"' and email = '"+user.getEmail()+"'";
         ResultSet rs = DBUtil.getInstance().dbExecuteQuery(sql);
         int data = getId(rs);
         return data;
     }
    
     private static int getId(ResultSet rs) throws SQLException{
         int data = 0; 
         while(rs.next()){
             data = rs.getInt("id_tags");
         }
        return data;
     }
     
     public static List<Tag> getAllData(User user) throws SQLException, ClassNotFoundException{
        String sql = "Select * from Tag where email = '"+user.getEmail()+"'";
        ResultSet rs = DBUtil.getInstance().dbExecuteQuery(sql);
        List<Tag> data = data(rs);
        return data;
    }
    
    private static List<Tag> data(ResultSet rs) throws SQLException{
        List<Tag> data =  FXCollections.observableArrayList();
        while(rs.next()){
            Tag tag = new Tag(rs.getInt("id_tag"), rs.getString("nama_tag")); 
            data.add(tag);
        }
        return data;
    }
     
}
