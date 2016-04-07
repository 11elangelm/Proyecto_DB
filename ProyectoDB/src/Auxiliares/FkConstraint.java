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
    public String name = "";
    public ArrayList<String> FkColumnsSource = new ArrayList<String>();
    public ArrayList<String> FkColumnsDestiny = new ArrayList<String>();
    public Table references = new Table();
    
    
    public FkConstraint(){}
    public FkConstraint(ArrayList<String> FkColumnsSource, ArrayList<String> FkColumnsDestiny, Table references, String name)
    {
        
        this.FkColumnsSource = FkColumnsSource;
        this.FkColumnsDestiny=FkColumnsDestiny;
        this.references = references;
        this.name = name;
    }

    @Override
    public String toString() {
        return "FkConstraint{" + "name=" + name + ", FkColumnsSource=" + FkColumnsSource + ", FkColumnsDestiny=" + FkColumnsDestiny + '}';
    }

    
    
    
}
