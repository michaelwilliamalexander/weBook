/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject;

import java.util.List;

/**
 *
 * @author Alvin
 */
public class Bookmark {
    private String nama;
    private int id;
    private String link;
    private int id_folder;
    
    public Bookmark(int id, String nama, String link, int id_folder){
        this.nama = nama;
        this.id = id;
       
        this.link = link;
        this.id_folder = id_folder;
                
    }
    /**
     * @return the nama
     */
    public String getNama() {
        return nama;
    }

    /**
     * @param nama the nama to set
     */
    public void setNama(String nama) {
        this.nama = nama;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return the id_folder
     */
    public int getId_folder() {
        return id_folder;
    }

    /**
     * @param id_folder the id_folder to set
     */
    public void setId_folder(int id_folder) {
        this.id_folder = id_folder;
    }
    
    
}
