/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Auxiliares;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Table {
    
    private ArrayList<String> IDs,Tipos;
    private String nombre;

    public Table(String name,ArrayList<String> IDs, ArrayList<String> Tipos) {
        this.nombre=name;
        this.IDs = IDs;
        this.Tipos = Tipos;
    }
    
    public ArrayList<String> getIDs() {
        return IDs;
    }

    public void setIDs(ArrayList<String> IDs) {
        this.IDs = IDs;
    }

    public ArrayList<String> getTipos() {
        return Tipos;
    }

    public void setTipos(ArrayList<String> Tipos) {
        this.Tipos = Tipos;
    }
    
    
}
