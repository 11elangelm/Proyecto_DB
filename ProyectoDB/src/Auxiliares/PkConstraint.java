/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Auxiliares;

import java.util.ArrayList;

/**
 *
 * @author juankboix1309
 */
public class PkConstraint 
{
    public ArrayList<String> PkColumns = new ArrayList<String>();
    public String name = "";
    
    public PkConstraint(){}
    public PkConstraint(ArrayList<String> PkColumns, String name)
    {
        this.PkColumns = PkColumns;
        this.name = name;
    }
}
