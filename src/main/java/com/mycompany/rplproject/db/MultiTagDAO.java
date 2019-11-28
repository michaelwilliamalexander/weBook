/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.db;

import com.mycompany.rplproject.Bookmark;
import com.mycompany.rplproject.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;

public class MultiTagDAO {
    
    public static List<Bookmark> multiTagData(User user, int data) throws ClassNotFoundException, SQLException{
        String sql = "select u.* from url u inner join urltag mg on u.id_url=mg.id_url inner join tag g on g.id_tag=mg.id_tag where g.id_tag ="+data+" and g.email ='"+user.getEmail()+"' group by u.id_url;";
        ResultSet rs= DBUtil.getInstance().dbExecuteQuery(sql);
        List<Bookmark> bookmark = resultSetTag(rs);
        return bookmark;
    }
    
    public static List<Bookmark> searchUrlByTag(User user, String namaTag) throws SQLException, ClassNotFoundException{
       String sql = "select u.* from url u inner join urltag mg on u.id_url=mg.id_url inner join tag g on g.id_tag=mg.id_tag where g.nama_tag like '%"+namaTag+"%' and g.email ='"+user.getEmail()+"' group by u.id_url;";
       ResultSet rs= DBUtil.getInstance().dbExecuteQuery(sql);
       List<Bookmark> bookmark = resultSetTag(rs);
       return bookmark;
    }
    
    private static List<Bookmark> resultSetTag(ResultSet rs) throws SQLException{
        List<Bookmark> bookmark = FXCollections.observableArrayList();
        while(rs.next()){
            Bookmark data = new Bookmark(rs.getInt("id_url"), rs.getString("nama_url"), rs.getString("link_url"), rs.getInt("id_folder")); 
            bookmark.add(data);
        }
        return bookmark;
    }
    
    public static void deleteMultiTag(int id){
         try {
            String query = "delete from urlTag where id_url = '"+id+"'";
            DBUtil.getInstance().dbExecuteUpdate(query);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void putData(int idUrl, int idTag, User user) throws SQLException, ClassNotFoundException{
        String sql = "Insert into urlTag(id_url,id_tag,email) values('"+idUrl+"','"+idTag+"','"+user.getEmail()+"')";
        DBUtil.getInstance().dbExecuteUpdate(sql);  
    }
    
    
}
