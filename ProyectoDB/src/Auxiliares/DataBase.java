/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Auxiliares;

/**
 *
 * @author user
 */
public class DataBase {
    private String name;
    private int numTablas;

    public DataBase(String name, int numTablas) {
        this.name = name;
        this.numTablas = numTablas;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumTablas() {
        return numTablas;
    }

    public void setNumTablas(int numTablas) {
        this.numTablas = numTablas;
    }
}
