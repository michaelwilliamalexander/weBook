/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject;

public class UrlTag {
    private int id;
    private int idUrl;
    private int idTag;
    private String email;
   
    public UrlTag(int id,int idUrl,int idTag,String email){
        this.id = id;
        this.idUrl = idUrl;
        this.idTag = idTag;
        this.email = email;
    }
    
    public int getIdurlTag() {
        return id;
    }

    public void setIdurlTag(int idurlTag) {
        this.id = idurlTag;
    }

    public int getIdUrl() {
        return idUrl;
    }

    public void setIdUrl(int idUrl) {
        this.idUrl = idUrl;
    }


    public int getIdTag() {
        return idTag;
    }

    public void setIdTag(int idTag) {
        this.idTag = idTag;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
    
}
