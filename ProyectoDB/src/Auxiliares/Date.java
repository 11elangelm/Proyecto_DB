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
public class Date implements Comparable<Date>
{
    public int day = 0;
    public int month = 0;
    public int year = 0;
    
    public Date(int y, int m, int d)
    {
        this.day = d;
        this.month = m;
        this.year = y;
    }

    @Override
    public int compareTo(Date o) 
    {
        if(this.year > o.year)
        {
            return 1;
        }
        if(this.year < o.year)
        {
            return -1;
        }
        if(this.year == o.year)
        {
            if(this.month > o.month)
            {
                return 1;
            }
            if(this.month < o.month)
            {
                return -1;
            }
            if(this.month == o.month)
            {
                if(this.day > o.day)
                {
                    return 1;
                }
                if(this.day < o.day)
                {
                    return -1;
                }
                if(this.day == o.day)
                {
                    return 0;
                }
            }
        }
        return 0;
    }
    
    public Date stringToDate(String date)
    {
        date = date.substring(1, date.length()-1);
        String[] fecha = date.split("-");
        Date aret = new Date(Integer.parseInt(fecha[0]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[2]));
        return aret;
    }
}
