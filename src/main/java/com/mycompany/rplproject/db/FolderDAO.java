
package com.mycompany.rplproject.db;

import com.mycompany.rplproject.Folder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Michael William
 */
public class FolderDAO {
    
    public static Folder showAllFolder(String email) throws SQLException, ClassNotFoundException{
        String query = "select * from folder where email ='"+email+"'";
        try {
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(query);
            Folder folder = FolderDAO.getAllFolder(rs);
            return folder;
        } catch (SQLException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
    
    public static Folder getAllFolder(ResultSet rs){
        Folder folder = null;
        try {
            while(rs.next()){
                folder = new Folder(rs.getInt("id_folder"), rs.getString("nama_folder"), rs.getInt("parent_folder")); 
            }
        } catch (SQLException ex) {
            Logger.getLogger(FolderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return folder;
    }
    
    public static List<Folder> showFolderList(String email) throws SQLException, ClassNotFoundException {
        String selectStmt = "select * from folder where email ='"+email+"'";
        try {
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(selectStmt);
            List<Folder> mhsList = getFolderList(rs);
            return mhsList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e); //Return exception
            throw e;
        }
    }

    private static List<Folder> getFolderList(ResultSet rs) throws SQLException, ClassNotFoundException {
        ObservableList<Folder> fdList = FXCollections.observableArrayList();
        while (rs.next()) {
           Folder folder =new Folder(rs.getInt("id_folder"), rs.getString("nama_folder"), rs.getInt("parent_folder")); 
            fdList.add(folder);
        }
        return fdList;
    }
    
    
    
    public static void deleteFolder(int id, String email){
         try {
            String query = "delete from folder where id_folder = '"+id+"' && email = '"+email+"'";
            DBUtil.getInstance().dbExecuteUpdate(query);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void updateFolder(int id,String email, String dataUpdate){
        try {
            String query =" Update from Folder set nama_folder = '"+dataUpdate+"' "
                    + "where id_folder = '"+id+"' && email = '"+email+"'";
            DBUtil.getInstance().dbExecuteUpdate(query);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
