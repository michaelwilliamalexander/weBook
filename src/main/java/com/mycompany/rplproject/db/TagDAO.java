/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.db;

import com.mycompany.rplproject.Tag;
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
public class TagDAO {
    
    public static Tag showAllTag(String email) throws SQLException, ClassNotFoundException{
        String query = " Select * from Tag where email = '"+email+"' or id_Tag =0";
        try {
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(query);
            Tag tag = TagDAO.getAllTag(rs);
            return tag;
        } catch (SQLException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
    
    public static Tag getAllTag(ResultSet rs){
        Tag tag = null;
        try {
            while(rs.next()){
                tag = new Tag(rs.getInt("id_tag"), rs.getString("nama_tag"));
                System.out.println(tag.getNamaTag());
            }
        } catch (SQLException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tag;
        
    }
    
     public static List<Tag> showTagList(String email) throws SQLException, ClassNotFoundException {
        String selectStmt = "select * from tag where email ='"+email+"' or id_Tag =0";
        try {
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(selectStmt);
            List<Tag> mhsList = getTagList(rs);
            return mhsList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e); //Return exception
            throw e;
        }
    }

    private static List<Tag> getTagList(ResultSet rs) throws SQLException, ClassNotFoundException {
        List<Tag> list = FXCollections.observableArrayList();
        while (rs.next()) {
           Tag tag = new Tag(rs.getInt("id_tag"), rs.getString("nama_tag")); 
            list.add(tag);
        }
        return list;
    }
    
    public static void deleteTag(int id, String email){
        try {
            String query = "delete from tag where id_tag = '"+id+"' && email = '"+email+"'";
            DBUtil.getInstance().dbExecuteUpdate(query);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void updateTag(int id,String email, String dataUpdate){
        try {
            String query =" Update from Tag set nama_tag = '"+dataUpdate+"' where id_tag = '"+id+"' && email = '"+email+"'";
            DBUtil.getInstance().dbExecuteUpdate(query);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
