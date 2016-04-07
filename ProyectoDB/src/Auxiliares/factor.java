/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Auxiliares;

/**
 *
 * @author juankboix1309
 */
public class factor 
{
    String value = "";
        
    public factor(String value)
    {
        this.value = value;
    }
    public Date stringToDate(String date)
    {
        date = date.substring(1, date.length()-1);
        String[] fecha = date.split("-");
        Date aret = new Date(Integer.parseInt(fecha[0]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[2]));
        return aret;
    }
    public int getInt()
    {
        return Integer.parseInt(value);
    }
    public String getChar()
    {
        return value;
    }
    public float getFloat()
    {
        return Float.parseFloat(value);
    }
    public Date getDate()
    {
        return stringToDate(this.value);
    }
    
    
    
}
