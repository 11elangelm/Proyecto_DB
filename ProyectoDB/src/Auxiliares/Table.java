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
import java.util.HashMap;

/**
 *
 * @author user
 */
public class Table {
    //array de objs constraint cuando se cree la clase
    public int cantRegistros;
    public ArrayList<String> IDs = new ArrayList<String>();
    public ArrayList<String>Tipos = new ArrayList<String>();
    public String nombre;
    public boolean Pk = false;
    public PkConstraint PkS=new PkConstraint();
    public ArrayList<FkConstraint> FkS = new ArrayList<FkConstraint>();
    public ArrayList<ChConstraint> ChK = new ArrayList<ChConstraint>();
    public ArrayList<expression> ColumnaCheck = new ArrayList<expression>();
    public HashMap<String,String> columnas = new HashMap();
    public HashMap<String, String> getColumnas() {
        return columnas;
    }
    public Table(){}
    public Table(String name,ArrayList<String> IDs, ArrayList<String> Tipos) 
    {
        this.nombre=name;
        this.IDs = IDs;
        this.Tipos = Tipos;
        //llenarMapa();
    }
    public Table(String name, ArrayList<String> IDs, ArrayList<String> Tipos, PkConstraint PkS, ArrayList<FkConstraint> FkS, ArrayList<ChConstraint> ChK)
    {
        this.nombre = name;
        this.IDs = IDs;
        this.Tipos = Tipos;
        this.PkS = PkS;
        this.Pk = true;
        this.FkS = FkS;
        this.ChK = ChK;
        //llenarMapa();
    }
    public void setPkS(PkConstraint PkS)
    {
        this.PkS = PkS;
        this.Pk = true;
    }
    public boolean ColumnExists(String Column)
    {
        return this.IDs.contains(Column);
    }
    public boolean CheckPK()
    {
        return Pk;
    }
    
    public void llenarMapa()
    {
        for (int i = 0; i < IDs.size(); i++) {
            columnas.put(IDs.get(i), Tipos.get(i));
        }
    }
 
    public void crear(String db) throws IOException{
        //llenarMapa();
        //"src\\Auxiliares\\clases\\"+ elimine esto del path para pruebas
        File nuevo=new File("src\\Auxiliares\\clases\\"+db+"\\"+this.nombre+".java");
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
                            "public class "+this.nombre +"{\n" +
                            "    \n");
            String constructorParams = "( ";
            
            for (int i = 0; i < IDs.size(); i++) 
            {
                
                if(Tipos.get(i).contains("char")){
                    constructorParams = constructorParams + "String " + IDs.get(i);
                    writer.write("    public String " + IDs.get(i) + ";\n" );
                    if(i <IDs.size()-1)
                    {
                        constructorParams = constructorParams + ", ";
                    }
                }else{
                    writer.write("    public " + Tipos.get(i)+" " +IDs.get(i)+";\n" );
                    constructorParams = constructorParams + Tipos.get(i)+" " +IDs.get(i);
                    if(i <IDs.size()-1)
                    {
                        constructorParams = constructorParams + ", ";
                    }
                }
            }
            writer.write("    public " + this.nombre + "()" + "\n");
            writer.write("    {"+ "\n");
            writer.write("    }"+ "\n");
            writer.write("    public " + this.nombre + constructorParams + ")"+ "\n");
            writer.write("    {"+ "\n");
            for (int i = 0; i < IDs.size(); i++) 
            {
                writer.write("        this." + IDs.get(i) + " = " + IDs.get(i)+";"+ "\n");
            }
            writer.write("    }"+ "\n");
            writer.write("}"+ "\n");
            writer.close();
        }else{
            throw new IOException("EL archivo de clase ("+ this.nombre+") que trata de crear ya existe");
        }
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

    public int getCantRegistros() {
        return cantRegistros;
    }

    public void setCantRegistros(int cantRegistros) {
        this.cantRegistros = cantRegistros;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Table{" + "cantRegistros=" + cantRegistros + ", nombre=" + nombre + ", Pk=" + Pk + ", PkS=" + PkS.toString() + ", FkS=" + FkS.toString() + ", ChK=" + ChK.toString() + ", columnas=" + columnas + '}';
    }

    
    
    
    
}
