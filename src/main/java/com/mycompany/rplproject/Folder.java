/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject;

/**
 *
 * @author Alvin
 */
public class Folder {
    private int id;
    private String nama;
    private int id_parent;
    
    public Folder(int id){
        this.id = id;
    }
    
    public Folder(int id, String nama, int id_parent){
        this.id = id;
        this.nama = nama;
        this.id_parent = id_parent;
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the nama
     */
    public String getNama() {
        return nama;
    }

    /**
     * @return the id_parent
     */
    public int getId_parent() {
        return id_parent;
    }
    
    
}
