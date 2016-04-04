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
public class FkConstraint 
{
    public ArrayList<String> FkColumnsSource = new ArrayList<String>();
    public ArrayList<String> FkColumnsDestiny = new ArrayList<String>();
    public ArrayList<Table> references = new ArrayList<Table>();
    public String name = "";
    
    public FkConstraint(){}
    public FkConstraint(ArrayList<String> FkColumnsSource, ArrayList<String> FkColumnsDestiny, ArrayList<Table> references, String name)
    {
        
        this.FkColumnsSource = FkColumnsSource;
        this.references = references;
        this.name = name;
    }
}
