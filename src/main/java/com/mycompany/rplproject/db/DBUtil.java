package com.mycompany.rplproject.db;

import com.mycompany.rplproject.Home.SettingController;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

/**
 *
 * @author budsus
 */
public class DBUtil {

    private static DBUtil dbutil = null;
    private static Connection conn = null;

    public DBUtil() {
    }

    public static DBUtil getInstance() {
        if (dbutil == null) {
            dbutil = new DBUtil();
        }
        return dbutil;
    }

    public String getJDBC() {
        String urlDB = getUrlDB();
        String JDBC_DRIVER = null;
        if (urlDB != null) {
            JDBC_DRIVER = "jdbc:sqlite:" + urlDB;
        }
        return JDBC_DRIVER;
    }

    private String getUrlDB() {
        try {
            Properties prop = new Properties();
            prop.load(getClass().getClassLoader().getResourceAsStream("aplikasi.properties"));
            return prop.getProperty("data.dir");
        } catch (IOException ex) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void dbConnect() throws SQLException, ClassNotFoundException {
        try {
            conn = DriverManager.getConnection(getJDBC());
        } catch (SQLException e) {
            throw e;
        }
    }

    public void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public ResultSet dbExecuteQuery(String queryStmt) throws SQLException,
           ClassNotFoundException {
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSet crs = null;

        try {
            dbConnect();
            //stmt = conn.createStatement();
            resultSet = conn.createStatement().executeQuery(queryStmt);
            RowSetFactory factory = RowSetProvider.newFactory();
            crs = factory.createCachedRowSet();
            crs.populate(resultSet);
            System.out.println("berhasil query");
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null && stmt!=null) {
                //Close resultSet
                System.out.println("resultset berisi");
                stmt.close();
//                return resultSet;
                resultSet.close();
            }
            //Close connection
            dbDisconnect();
        }
        return crs;
    }

    public void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        try {
            dbConnect();
            stmt = conn.createStatement();
            stmt.executeUpdate(sqlStmt);
            
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            dbDisconnect();
        }
    }
    
    public void dbExecute(String del) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        try{
            dbConnect();
            stmt = conn.createStatement();
            stmt.execute(del);
        } catch(SQLException e){
            throw e;
        } finally {
            if(stmt !=null){
                stmt.close();
            }
            dbDisconnect();
        } 
    }
    
}
