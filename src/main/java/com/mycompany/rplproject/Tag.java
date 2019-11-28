package com.mycompany.rplproject;

import com.mycompany.rplproject.db.DBUtil;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Ananda
 */
public class Tag {
    private SimpleIntegerProperty idTag;
    private SimpleStringProperty namaTag;
    
    public Tag(int id, String nama){
        this.idTag = new SimpleIntegerProperty(id);
        this.namaTag =  new SimpleStringProperty(nama);
    }
    
  
    @Override
    public String toString() {
        return this.getNamaTag();
    }

    public int getIdTag() {
        return idTag.get();
    }

    public void setIdTag(int idTag) {
        this.idTag = new SimpleIntegerProperty(idTag);
    }

    public String getNamaTag() {
        return namaTag.get();
    }

   
    public void setNamaTag(String namaTag) {
        this.namaTag = new SimpleStringProperty(namaTag);
    }
    
}
