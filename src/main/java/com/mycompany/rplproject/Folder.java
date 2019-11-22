/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Folder {
    private SimpleIntegerProperty id;
    private SimpleStringProperty nama;
    private SimpleIntegerProperty id_parent;
    
    public Folder(int id){
        this.id = new SimpleIntegerProperty(id);
    }
    
    public Folder(int id, String nama, int id_parent){
        this.id = new SimpleIntegerProperty(id);
        this.nama = new SimpleStringProperty(nama);
        this.id_parent = new SimpleIntegerProperty(id_parent);
    }
    /**
     * @return the id
     */
    public int getId() {
        return id.get();
    }

    /**
     * @return the nama
     */
    public String getNama() {
        return nama.get();
    }

    /**
     * @return the id_parent
     */
    public int getId_parent() {
        return id_parent.get();
    }
//    @Override
//    public String toString(){
//        return getNama();
//    }
    
}
