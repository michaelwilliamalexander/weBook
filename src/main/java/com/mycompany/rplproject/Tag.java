/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rplproject;

import com.mycompany.rplproject.db.DBUtil;
import java.sql.ResultSet;

/**
 *
 * @author Ananda
 */
public class Tag {
    private int idTag;
    private String namaTag;
    
    public Tag(int id, String nama){
        idTag = id;
        namaTag = nama;
    }
    
  
    @Override
    public String toString() {
        return this.getNamaTag();
    }

    public int getIdTag() {
        return idTag;
    }

    public void setIdTag(int idTag) {
        this.idTag = idTag;
    }

    public String getNamaTag() {
        return namaTag;
    }

   
    public void setNamaTag(String namaTag) {
        this.namaTag = namaTag;
    }
    
    
}
