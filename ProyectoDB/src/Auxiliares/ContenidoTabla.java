/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Auxiliares;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author user
 */
public class ContenidoTabla {
    private ArrayList<HashMap> lista;
    private Table tabla;

    public Table getTabla() {
        return tabla;
    }

    public void setTabla(Table tabla) {
        this.tabla = tabla;
    }
    
    

    public ContenidoTabla() {
        this.lista = new ArrayList();
    }
    
    public ArrayList<HashMap> getLista() {
        return lista;
    }

    public void setLista(ArrayList<HashMap> lista) {
        this.lista = lista;
        
        
    }
    
    public void removeColumna (String nombre){
        for (HashMap tabla : lista) {
            tabla.remove(nombre);
        }
    }
    
    public void agregarColumna (String nombre){
        for (HashMap tabla : lista) {
            tabla.put(nombre,null);
        }
    }
    
    public void addRegistro(HashMap entrada){
        this.lista.add(entrada);
    }

    @Override
    public String toString() {
        String t="";
        for (HashMap mapa : lista) {
            for (Iterator it = mapa.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                String key = entry.getKey();
                String value = entry.getValue();
                t+=("key, " + key + " value " + value)+"\n";
            }
        }
        return "ContenidoTabla{" + t+'}';
    }
    
    
}
