
package com.mycompany.rplproject.db;

import com.mycompany.rplproject.Folder;
import com.mycompany.rplproject.User;
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
    
    public static String seeData(String namaFolder, int idParent){
        String tempt = null;
        try {
            String stmt = "Select * From Folder where nama_folder = '"+namaFolder+"' and Parent_Folder = '"+idParent+"'";
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(stmt);
            tempt = FolderDAO.getData(rs);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(FolderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tempt;
    }
    
    private static String getData(ResultSet rs){
        String tempt = null;
        try {
            while(rs.next()){
                tempt= rs.getString("nama_folder");
                System.out.println(tempt);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(FolderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tempt;
    }
    
    public static List<Folder> showFolderList(String email) throws SQLException, ClassNotFoundException {
        String sqrt = "select * from folder where email ='"+email+"'";
        try {
            ResultSet rs = DBUtil.getInstance().dbExecuteQuery(sqrt);
            List<Folder> folderList =  FolderDAO.getFolderList(rs);
            return folderList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e); //Return exception
            throw e;
        }
    }

    private static List<Folder> getFolderList(ResultSet rs) throws SQLException, ClassNotFoundException {
        List<Folder> fdList = FXCollections.observableArrayList();
        while (rs.next()) {
           Folder folder =new Folder(rs.getInt("id_folder"), rs.getString("nama_folder"), rs.getInt("Parent_folder")); 
           fdList.add(folder);
        }
        return fdList;
    }
    
    public static void tambahFolder_WithParent(String namaFolder,int parent,String email){
        try {
            String newFolder = "Insert into Folder (nama_folder,Parent_folder,email) values('"+namaFolder+"','"+parent+"','"+email+"')";
            DBUtil.getInstance().dbExecuteUpdate(newFolder);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(FolderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void tambahFolder(String namaFolder, String email){
        try {
            String  newFolder = "Insert into Folder (nama_folder,email) values('"+namaFolder+"','"+email+"')";
            DBUtil.getInstance().dbExecuteUpdate(newFolder);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(FolderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void deleteFolder(int id){
         try {
            String query = "delete from folder where id_folder = '"+id+"'";
            DBUtil.getInstance().dbExecuteUpdate(query);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void updateFolder(int id, String dataUpdate, User user){
        try {
            String query =" Update Folder set nama_folder = '"+dataUpdate+"' "
                    + "where id_folder = '"+id+"'";
            DBUtil.getInstance().dbExecuteUpdate(query);
            for(int i=0;i<user.getFolder().size();i++){
                if(user.getFolder().get(i).getId()==id){
                    user.getFolder().get(i).setNama(dataUpdate);
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TagDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static List<Integer> searchFolder(String search, String email) throws ClassNotFoundException, SQLException{
        String sqlFolder = "Select * from Folder where nama_folder like '%"+search+"%' and email = '"+email+"'";
        ResultSet rs = DBUtil.getInstance().dbExecuteQuery(sqlFolder);
        List<Integer> data = searchFolderData(rs);
        return data;
    }
    
    private static List<Integer> searchFolderData(ResultSet rs){
        List<Integer>data = FXCollections.observableArrayList();
        try {
            while (rs.next()){
                data.add(rs.getInt("id_folder"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FolderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }
}
