/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject;

import com.mycompany.rplproject.db.BookmarkDAO;
import com.mycompany.rplproject.db.DBUtil;
import com.mycompany.rplproject.db.FolderDAO;
import com.mycompany.rplproject.db.TagDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User {
    private String email;
    private String password;
    private List<Folder> folder = new ArrayList<>();
    private List<Tag> tag = new ArrayList<>();
    private List<Bookmark> bookmark = new ArrayList<>();
    
    public User(){}
    
    public User(String email, String password){
        try {
            this.email = email;
            this.password = password;
            this.folder = FolderDAO.showFolderList(email);
            this.tag = TagDAO.showTagList(email);
            this.bookmark = BookmarkDAO.showBookmarkList(email);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public User(String email, String password, List<Folder> f, List<Tag> t, List<Bookmark> b){
        this.email = email;
        this.password = password;
        folder = f;
        tag = t;
        bookmark = b;
    }
   
    public String getEmail() {
        return email;
    }

    
    public void setEmail(String email) {
        this.email = email;
    }

    
    public void setPassword(String password) {
        this.password = password;
    }

    
    public List<Folder> getFolder() {
        return folder;
    }

    
    public void setFolder(List<Folder> folder) {
        this.folder = folder;
    }

    /**
     * @return the tag
     */
    public List<Tag> getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(List<Tag> tag) {
        this.tag = tag;
    }

    /**
     * @return the bookmark
     */
    public List<Bookmark> getBookmark() {
        return bookmark;
    }

    /**
     * @param bookmark the bookmark to set
     */
    public void setBookmark(List<Bookmark> bookmark) {
        this.bookmark = bookmark;
    }
    
    
}
