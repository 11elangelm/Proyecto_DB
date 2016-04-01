/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Auxiliares;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class MakeClass {
    
    ArrayList<String> nombres,tipos;
    String name;

    public MakeClass(String n,ArrayList<String> nombres, ArrayList<String> tipos) {
        this.name=n;
        this.nombres = nombres;
        this.tipos = tipos;
    }
    
    
    public void crear() throws IOException{
        File nuevo=new File("src\\Auxiliares\\clases\\"+this.name+".java");
        if(!nuevo.exists()){
            
            nuevo.createNewFile();
            
            BufferedWriter writer = null;
            
            writer = new BufferedWriter(new FileWriter(nuevo));
            
            writer.write("package Auxiliares.clases;\n" +
                            "\n" +
                            "/**\n" +
                            " *\n" +
                            " * @author user\n" +
                            " */\n" +
                            "public class "+this.name +"{\n" +
                            "    \n");
            
            for (int i = 0; i < nombres.size(); i++) {
                if(tipos.get(i).contains("char")){
                    writer.write("String " + nombres.get(i) + ";\n" );
                }else{
                    writer.write(tipos.get(i)+" " +nombres.get(i)+";\n" );
                }
            }
            
            writer.write("}");
            writer.close();
        }else{
            throw new IOException("EL archivo de clase ("+ this.name+") que trata de crear ya existe");
        }
    }
    
}
