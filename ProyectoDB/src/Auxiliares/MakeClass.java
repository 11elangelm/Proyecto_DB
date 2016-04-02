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
        //"src\\Auxiliares\\clases\\"+ elimine esto del path para pruebas
        File nuevo=new File("src\\Auxiliares\\clases\\"+this.name+".java");
        //cambie if para pruebas, debe ser if(!nuevo.exists())
        if(true){
            
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
            String constructorParams = "( ";
            
            for (int i = 0; i < nombres.size(); i++) 
            {
                
                if(tipos.get(i).contains("char")){
                    constructorParams = constructorParams + "String " + nombres.get(i);
                    writer.write("    public String " + nombres.get(i) + ";\n" );
                    if(i <nombres.size()-1)
                    {
                        constructorParams = constructorParams + ", ";
                    }
                }else{
                    writer.write("    public " + tipos.get(i)+" " +nombres.get(i)+";\n" );
                    constructorParams = constructorParams + tipos.get(i)+" " +nombres.get(i);
                    if(i <nombres.size()-1)
                    {
                        constructorParams = constructorParams + ", ";
                    }
                }
            }
            writer.write("    public " + this.name + "()" + "\n");
            writer.write("    {"+ "\n");
            writer.write("    }"+ "\n");
            writer.write("    public " + this.name + constructorParams + ")"+ "\n");
            writer.write("    {"+ "\n");
            for (int i = 0; i < nombres.size(); i++) 
            {
                writer.write("        this." + nombres.get(i) + " = " + nombres.get(i)+";"+ "\n");
            }
            writer.write("    }"+ "\n");
            writer.write("}"+ "\n");
            writer.close();
        }else{
            throw new IOException("EL archivo de clase ("+ this.name+") que trata de crear ya existe");
        }
    }
    
}
