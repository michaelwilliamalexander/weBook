/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject.db;

import com.mycompany.rplproject.User;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
}
