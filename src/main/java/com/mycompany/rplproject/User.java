/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String password;
    private List<Folder> folder = new ArrayList<>();
    private List<Tag> tag = new ArrayList<>();
    private List<Bookmark> bookmark = new ArrayList<>();
    
    public User(){}
    
    public User(String email, String password){
        this.email = email;
        this.password = password;
    }
    
    public User(String email, String password, List<Folder> f, List<Tag> t, List<Bookmark> b){
        this.email = email;
        this.password = password;
        folder = f;
        tag = t;
        bookmark = b;
    }
    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the folder
     */
    public List<Folder> getFolder() {
        return folder;
    }

    /**
     * @param folder the folder to set
     */
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
