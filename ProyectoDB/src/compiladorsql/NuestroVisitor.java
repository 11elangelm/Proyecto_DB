/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladorsql;

import Auxiliares.ChConstraint;
import Auxiliares.ContenidoTabla;
import Auxiliares.DataBase;
import Auxiliares.FkConstraint;
import Auxiliares.MakeClass;
import Auxiliares.PkConstraint;
import Auxiliares.Table;
import Auxiliares.TableMaker;
import Auxiliares.expression;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 *
 * @author user
 */
public class NuestroVisitor<T> extends GramaticaBaseVisitor{

    private String dirBase="Bases de Datos\\",dirActual="";
    private ArrayList<String> errores=new ArrayList();
    public Gson Gsoneador = new Gson(); 
            
    public DataBase DBactual;
    private ArrayList<ArrayList<String>> metaDataLOCALTBcolumnas,
            metaDataLOCALTBtipos;
    
    private boolean verbose=false,
            bUse=false;
    private String tablaActualName="";
    private String TablaActual;
    private ArrayList<Table> metaDataActual;
    private HashMap<String,Table>tablasActuales;
    public TableMaker elCreador = new TableMaker();
    public GramaticaParser parsero;
    //tabla y constraints se inicializan y el objse arma al final
    public Table ActualTable;
    public Table ReferencedTable;
    public PkConstraint PkC;
    public FkConstraint FkC;
    public ChConstraint ChC;
    
    private String nameTablaActual="";
    private HashMap<String,ContenidoTabla> registrosTablasActuales;
    
    private String lastDB="";
    
    private HashMap<String,DataBase> bases;
    private ArrayList<String> destinyColumns= new ArrayList();
    
/****************************************************************************************************
                                        NO SE OLVIDEN DE
                                        AGREGAR EL VERBOSE CON EL 
                                        METODO revVerb()
                                        MIREN COMO LO USE YO
                                        EN EL CREATEDB
****************************************************************************************************/

    @Override
    public T visitSqlProgram(GramaticaParser.SqlProgramContext ctx) {
        this.metaDataActual=new ArrayList();
        this.tablasActuales=new HashMap();
        this.registrosTablasActuales=new HashMap();
        this.bases=new HashMap();
        
        this.cargarBases();
        long startTime = System.nanoTime();

        for (ParseTree child : ctx.statement()) 
        {
            String visit = (String) visit(child);
            if(visit.contains("error")){
                return (T)visit;
            }        
            
        }
        long estimatedTime = System.nanoTime() - startTime;
        estimatedTime=(long) (estimatedTime*0.000001);
        System.out.println("Tiempo total->"+estimatedTime+" ms");
            writeData();
            escribirMD();
        try {
            this.WriteJSon();
        } catch (IOException ex) {
//            Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (T)"";
    }
    
    /******************
     * CREA UN FOLDER PARA ALMACENAR LAS TABLAS
     * AGREGAR EL NOMBRE DE LA DB A LA METADATA GENERAL
    *******************/
    @Override
    public T visitCrearDB(GramaticaParser.CrearDBContext ctx) {
        String nombre=ctx.ID().getText();
        File dir = new File(dirBase+nombre);
        
        revVerb("Revisando que la DB "+nombre+" no exista ya para poder crearla");
        if(dir.isDirectory()){
            this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), genera una DB ya existente ");
            
            return (T)"error al tratar crear DB  ya existente";
            
        }else{
            
            //trata de crear el directorio
            boolean successful = dir.mkdir();
            if (successful){
                
//                    agregar el archivo a la metadata general en memoria
                    
                    revVerb("Folder para dB "+nombre+" creado exitosamente");
                    
//                      AGREGAR LA NUEVA DB A LA METADATA GENERAL PARA SU USO
                    DataBase nueva=new DataBase(nombre,0);
                    if(!this.bases.containsKey(nombre)){
                        this.bases.put(nombre, nueva);
                    }
                    /*
                try {
                    
                    //this.addToMDGeneral(nueva);
                    revVerb("Creada la entrada de la DB "+nombre+" a la metadata General");
                } catch (IOException ex) {
                    System.err.println("ERROR AL AGREGAR "+nombre+" A LA METADA GENERAL");
                    
                }
                */
//                crear el archivo de la metadata local para la DB
                try {
                    this.crearArchivo(dirBase+nombre+"\\METADATA.json");
                    revVerb("Creado el archivo metadata de la DB "+nombre+" ");
                    
                    File nf=new File ("src\\Auxiliares\\clases\\"+nombre);
                    nf.mkdir();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
                
                
                
            }else{
                this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no se puede crear el directorio especificado");
                revVerb("Folder para la DB "+nombre+"  no creado");
                
                return (T)"error al tratar crear DB ";
            }
        }
        
        
        return (T)""; //To change body of generated methods, choose Tools | Templates.
    }

    /******************
     * CAMBIA EL NOMBRE DE LA DB EN LA METADATA GENERAL Y
     * CAMBIA EL NOMBRE DEL FOLDER QUE CONTIENE LAS TABLAS DE LA DB
     *
    *******************/
    @Override
    public T visitAlterarDB(GramaticaParser.AlterarDBContext ctx) {
        
        String nombreViejo=ctx.ID(0).getText();
        String nombreNuevo=ctx.ID(1).getText();
        //VER SI LA DB QUE QUIERO RENOMBRAR YA EXISTE
        File old = new File(dirBase+nombreViejo);
        
        revVerb("revisar que la DB "+nombreViejo+"  ya exista para poder renombrar");
        if(!old.isDirectory()){
            revVerb("DB "+nombreViejo+" no existe para ser renombrada");
            
            this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no se puede renombrar la DB:"+ctx.ID(0)+" que no existe");
            return (T)"error al renombrar una DB que no existe";
        }
        
//        CAMBIAR EL NOMBRE DE LA DB EN LOS LUGARES BASICOS
        
//        cambiado el nombre de la carpeta
        File nuevo = new File(dirBase+nombreNuevo);
        if(old.renameTo(nuevo)){
            
            if(this.dirActual.equals(this.dirBase+nombreViejo)){
                this.dirActual=this.dirBase+nombreNuevo;
            }

//            RENOMBRAR LA DB EN LA METADATA GENERAL
            DataBase get = this.bases.remove(nombreViejo);
            get.setName(nombreNuevo);
            this.bases.put(nombreNuevo, get);
            
            /*
            try {
                this.renameFromMDGeneral(nombreViejo, nombreNuevo);
            } catch (IOException ex) {
                System.err.println("error al renombrar la db "+ctx.ID(0).getText() +" en la metada general");
                //Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            */
            
            //File vj = new File("src\\Auxiliares\\clases\\"+nombreViejo);
            //File nv2 = new File("src\\Auxiliares\\clases\\"+nombreNuevo);
            //vj.renameTo(nv2);
            
            revVerb("DB->"+nombreViejo+" renombrada como ->"+nombreNuevo);
            
//            this.metaDataGENERALDBnames.set(this.metaDataGENERALDBnames.indexOf(ctx.ID(0).getText()), ctx.ID(1).getText());
        }else{
            revVerb("DB->"+nombreViejo+" no pudo ser renombrada");
        }
        
        
        /************************************
            FALTA EL PROCEMIENTO PARA LAS TABLAS
        ************************************/
        
        return (T)"";
    }

    /******************
     * CAMBIA EL DIRECTORIO ACTUAL PARA QUE APUNTE AL DE LA DB ESPECIFICADA
     * 
    *******************/
    
    @Override
    public T visitUsarDB(GramaticaParser.UsarDBContext ctx) {
        
        writeData();
        
        
        this.tablasActuales.clear();

        //System.out.println("no hay que sobreescribir archivos de regreso");
        this.bUse=true;
        String nombre=ctx.ID().getText();
        this.lastDB=dirBase+nombre;

        File nuevo = new File(dirBase+nombre);

        revVerb("Revisando que la DB "+nombre+" exista para ser usada");
        if(!nuevo.isDirectory()){
            revVerb("La DB buscada no existe");
            this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no se puede usar la DB:"+nombre+" porque no existe");
            return(T)"error buscando la DB para uso";
        }

        revVerb("La DB buscada si existe");
//            LIMPIAR LAS VARIABLES QUE VAN A SERVIR EN MEMORIA


//            LLENAR EL ARRAY CON LA DATA PRESENTE EN LAS TABLAS
        List<Table> infoMDLocal=null;
        try {
            infoMDLocal=(getInfoMDLocal(dirBase+nombre));
        } catch (IOException ex) {
            Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }

        if( !(infoMDLocal!=null) || infoMDLocal.size()<1 ){

            this.dirActual=dirBase+nombre;
        }else{
            revVerb("las DB cargada si tiene tablas creadas");
    //        cargar las tablas existentes en la tabla
            if(infoMDLocal!=null){
                for (Table tabla : infoMDLocal) {
        //            agregar al mapa que le sirve a Angel
                    this.tablasActuales.put(tabla.getNombre(), tabla);
        //          AGREGAR AL ARRAY QUE LE SIRVE A CANTEO
                    this.metaDataActual.add(tabla);
                }
            }
            

    //        CARGAR EL CONTENIDO DE LOS REGISTROS DE LAS TABLAS 
            this.registrosTablasActuales=cargarRegistros(nuevo);
            this.dirActual=dirBase+nombre;
            revVerb("USANDO: "+nuevo.getAbsolutePath());
            
        }


        return(T)""; //To change body of generated methods, choose Tools | Templates.
    }
    
    /******************
     * BORRA EL NOMBRE DE LA DB EN LA METADATA GENERAL Y
     * BORRA EL  FOLDER QUE CONTIENE LAS TABLAS DE LA DB
     *
    *******************/
    @Override
    public T visitEliminarDB(GramaticaParser.EliminarDBContext ctx) {
        
        String nombre=ctx.ID().getText();
        File toDelete = new File(dirBase+nombre);
        revVerb("Revisando que la DB "+nombre+" exista para ser eliminada");
        
//        REVISAR QUE LA DB EXISTA
        if(!toDelete.isDirectory()){
            
            revVerb("La DB "+nombre+" buscada no existe");
            this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no se puede eliminar la DB:"+ctx.ID()+" porque no existe");
            return(T)"error buscando la DB para uso";
            
        }else{
            
            int opcion=JOptionPane.showConfirmDialog(null, "Desea Eliminar la DB:\""+ nombre +"\" que tiene "+"\"IR A OBTENER LOS REGISTROS A LA METADATA\" "+"registros","ATENCION",JOptionPane.INFORMATION_MESSAGE);
            if(opcion==JOptionPane.YES_OPTION){
                
                this.bases.remove(nombre);
                /*
                try {
                //            ABRIR EL ARCHIVO DE LA METADATA GENERAL Y EDITARLO
                    eliminarFromMDGeneral(nombre);
                } catch (IOException ex) {
                    revVerb("No se puede eliminar la DB especificada");
                    Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
                }
                */
                revVerb("DB->"+nombre+" eliminada de la metadata general");
                deleteDir(toDelete);
                //deleteDir(new File("src\\Auxiliares\\clases\\"+nombre));
                revVerb("DB->"+nombre+" eliminada");
                
            }
            
        }
        
        return (T)"";
    }

    /******************
     * muestra en una nueva ventana las bases de datos
     * SI NO HAY NIGUNA ENTONCES MUESTRA UN MENSAJE DICIENDOLO
     *
    *******************/
    @Override
    public T visitMostrarDB(GramaticaParser.MostrarDBContext ctx) {
        try {
            List<DataBase> infoMDGeneral = new ArrayList(this.bases.values());
            //infoMDGeneral = this.getInfoMDGeneral();
            if(infoMDGeneral.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Actualmente no hay bases de datos");
            }
            else
            {
            JTable toShow = elCreador.ShowDatabases(infoMDGeneral);
            JFrame frame = new JFrame();
            frame.setTitle("Bases de datos actuales");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JScrollPane scrollPane = new JScrollPane(toShow);
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setSize(500, 150);
            frame.setVisible(true);
            }
        
        } catch (Exception ex) {
            Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return (T)"";
    }

    /******************
     *crea una nueva tabla y su respectivo JSON, vacio. 
     *
    *******************/
    @Override
    public T visitCrearTB(GramaticaParser.CrearTBContext ctx) {
        
//Crear variables para usar en la instancia del classmaker
        //nombre
        String newTBName = ctx.ID().getText();
        ArrayList<String> columnNames = new ArrayList();
        ArrayList<String> columnTypes = new ArrayList();
        this.revVerb("revisar si hay una DB en uso para crear la tabla");
        try {
            crearArchivo(this.dirActual + "\\" + newTBName+".json");
        } catch (IOException ex) {
            Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
            return (T)"error al crear nuevo archivo para la tabla escogida";
        }
        if(!this.bUse){
            revVerb("no hay DB seleccionada");
            this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no pueder crear una tabla porque no hay DB seleccionada");
            return (T)"error porque no hay DB elegida";
            
        }
        
        
//  llenar arraylists para crear objeto tabla
        for (int i = 0; i < ctx.columna().size(); i++) 
        {
        //obtener el id y el tipo de la columna para meterlo a la metaDATA local
            String data=(String)visit(ctx.columna(i));   
            //separar el id y el tipo para agregarlos a las distintas listas
            String[] split = data.split(",");
//            AGREGO EL EL NOMBRE Y TIPO DE LA COLUMNA EN LA POSICION CORRESPONDIENTE A LA TABLA
            columnNames.add(split[0]);
            columnTypes.add(split[1]);
        }
        if(this.tablasActuales.containsKey(newTBName))
        {
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la tabla " + newTBName + " que ya existe" );
            return (T)"error al crear tabla que ya existe";
        }

        //crear obj tabla
        this.ActualTable = new Table();
        this.ActualTable.setIDs(columnNames);
        this.ActualTable.setTipos(columnTypes);
        this.ActualTable.setNombre(newTBName);
        this.ActualTable.llenarMapa();
        //si hay constraints se agregan a la tabla nueva
//        revisar que todas las constraints sean validas
        for (GramaticaParser.ConstraintContext constraint : ctx.constraint()) {
            String visit = (String) visit(constraint);
            if(visit.contains("error")){
                return (T)visit;
            }
        }
        
        //agrga tabla al map
        this.tablasActuales.put(newTBName, ActualTable);
        //crear JSON vacio
        //String Jsoneado = Gsoneador.toJson(classMaker);
        //Jsoneado = Jsoneado +"\n";
        //try {
        //    Files.write(Paths.get(this.dirActual+"\\METADATA.json"), Jsoneado.getBytes(), StandardOpenOption.APPEND);
        //} catch (IOException ex) {
        //    Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
        //}
        
//        System.out.println("***********************->"+this.dirActual);
        String nameDB=this.dirActual.substring(this.dirActual.lastIndexOf("\\")+1);
        System.out.println(nameDB);
        DataBase get = this.bases.get(nameDB);
        get.setNumTablas(get.getNumTablas()+1);
        //SE ASIGNA ESTA VARIBLE PARA QUE FUNCIONE BIEN LA BUSQUEDA
        tablaActualName=dirActual.substring(dirActual.indexOf("\\")+1);
        
        return (T)"";
    }
    

    //visitas para crear constraints
    @Override
    public T visitConstraintPK(GramaticaParser.ConstraintPKContext ctx) 
    {
        //revisar que no exista una primary key en tabla actual
        if(ActualTable.CheckPK())
        {
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la tabla " + ActualTable.nombre + " que ya contiene una Primary key" );
            return (T)"error al agregar primary key si ya existe";
        }
        //nombre Constraint
        String Cname = ctx.ID(0).getText();
        //verificar nombre de columnas
        ArrayList<String> ConCols = new ArrayList<String>();
        for(int i=1; i<ctx.ID().size(); i++)
        {
            String ColName = ctx.ID(i).getText();
            if(ActualTable.ColumnExists(ColName) == false)
            {
                this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la columna " + ColName + " que no existe en la tabla" + ActualTable.nombre );
                return (T)"error al agregar primary key sobre una columna que no existe";
            }
            ConCols.add(ColName);
            
        }
        //crear Constraint
        PkConstraint primary = new PkConstraint();
        primary.name = Cname;
        primary.PkColumns = ConCols;
        ActualTable.setPkS(primary);
        return (T)"";
    }

    @Override
    public T visitConstraintFK(GramaticaParser.ConstraintFKContext ctx) 
    {
        
        //nombre constraint
        String FkName = ctx.ID(0).getText();
        //source columns
        ArrayList<String> SourceColumns = new ArrayList<String>();
        for(int i=1; i<ctx.ID().size(); i++)
        {
            String SourceName = ctx.ID(i).getText();
            //revisar si columna existe en tabla
            if(ActualTable.ColumnExists(SourceName) == false)
            {
                this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la columna " + SourceName + " que no existe en la tabla" + ActualTable.nombre );
                return (T)"error al agregar foreign key sobre una columna que no existe";
            }
            //revisar si columna no forma parte de la primary key de la tabla actual
            if(ActualTable.PkS.PkColumns.contains(SourceName))
            {
                this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la columna " + SourceName + " que forma parte de una Primary Key en la tabla" + ActualTable.nombre );
                return (T)"error al agregar foreign key sobre una columna que es primary key";
            }
            SourceColumns.add(SourceName);
        }
        //mandar a traer las columnas de la tabla a referenciar
        String visitReferences = (String)visitReferences(ctx.references());
        if(visitReferences.contains("error")){
            return (T)visitReferences;
        }
        
        ArrayList<String> DestinyColumns = new ArrayList(this.destinyColumns);
        //revisar que el numero de columnas sea el mismo
        
        if(SourceColumns.size() != DestinyColumns.size())
        {
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia una cantidad de atributos erronea para la llave foranea" );
            return (T)"error al agregar foreign key sin que las columnas sean la misma cantidad";
        }
        //revisar si los tipos son los mismos en las columnas origen y destino
        for(int i=0; i<SourceColumns.size(); i++)
        {
            String Scolumn = SourceColumns.get(i);
            String Dcolumn = DestinyColumns.get(i);
            String TScolumn = this.ActualTable.columnas.get(Scolumn);
            String TDcolumn = this.ReferencedTable.columnas.get(Dcolumn);
            if(TScolumn.equals(TDcolumn) == false)
            {
                this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia una columna que no es del mismo tipo de la columna a la que hace referencia" );
                return (T)"error al agregar foreign key sin que las columnas sean la misma cantidad";
            }
        }
        
        //System.out.println("*****->"+DestinyColumns.toString());
        FkConstraint elExtranjero = new FkConstraint(SourceColumns, DestinyColumns, ReferencedTable, FkName);
        //System.out.println("---->"+elExtranjero.toString());
        ActualTable.FkS.add(elExtranjero);
        return (T)"";
    }
    
    @Override
    public T visitReferences(GramaticaParser.ReferencesContext ctx) 
    {
        
        String RtableName = ctx.ID(0).getText();
        //revisar si la tabla a referenciar existe
        if(this.tablasActuales.containsKey(RtableName) == false)
        {
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la tabla " + RtableName + " que no existe");
            return (T)"error al agregar foreign key referenciando a una tabla que no existe";
        }
        this.ReferencedTable = this.tablasActuales.get(RtableName);
        this.destinyColumns.clear();
        for(int i=1; i<ctx.ID().size(); i++)
        {
            
            //revisar si la tabla referenciada contiene las referencias
            String DestinyName = ctx.ID(i).getText();
            if(ReferencedTable.ColumnExists(DestinyName) == false)
            {
                this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la columna " + DestinyName + " que no existe en la tabla " + ReferencedTable);
                return (T)"error al agregar foreign key referenciando a una columna que no existe";
            }
            
            //revisar si las columnas referenciadas son primary key
            if(ReferencedTable.PkS.PkColumns.contains(DestinyName) == false)
            {
                this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la columna " + DestinyName + " que no es primary key en la tabla " + ReferencedTable);
                return (T)"error al agregar foreign key referenciando a una columna que no es primary key";
            }
            
            this.destinyColumns.add(DestinyName);
            
        }
        return (T)"";
    }
    
    @Override
    public T visitConstraintCheck(GramaticaParser.ConstraintCheckContext ctx) 
    {
        //nombre
        String nombre = ctx.ID().getText();
        ParseTree dekuTree = ctx.getChild(2);
        expression nueva = new expression(dekuTree, this.parsero, this.ActualTable);
        this.ActualTable.ChK.put(nombre, nueva);
        return (T)"";
    }

    /******************
     *elimina una tabla ya existente 
     *
    *******************/
    @Override
    public T visitEliminarTB(GramaticaParser.EliminarTBContext ctx) 
    {
        if(!this.bUse){
            revVerb("no hay DB seleccionada");
            this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no pueder crear una tabla porque no hay DB seleccionada");
            return (T)"error porque no hay DB elegida";
            
        }
        String tableName = ctx.ID().getText();
        //revisar que si exista la tabla
        if(this.tablasActuales.containsKey(tableName) == false)
        {
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la tabla " + tableName + " que no existe" );
            return (T)"error al eliminar tabla que no existe";
        }
        //eliminar la tabla
        Table aRemover = this.tablasActuales.get(tableName);
        
//        ITERAR SOBRE TODAS LAS TABLAS PARA VER SI ALGUNA REFERENCIA A ESTA
        for (Table tablaXrev : this.tablasActuales.values()) {
            if(!tablaXrev.getNombre().equals(tablaActualName))
            //ITERAR SOBRE TODAS LAS FOREIGN KEYS DE LAS TABLAS
            for (FkConstraint listaFKs : tablaXrev.FkS) {
                
                if(listaFKs.references.getNombre().equals(tableName)){
                    this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "intenta borrar la tabla " + tableName + " que es una constraint en la tabla " +tablaXrev.getNombre()+", primero debe eliminar esta constraint");
                    return (T)"error al eliminar tabla que no existe";
                }
                
            }
        }    
        this.tablasActuales.remove(tableName);
        this.registrosTablasActuales.remove(tableName);
        
        File nf=new File(dirActual+"\\"+tableName+".json");
        nf.delete();
        
        String nameDB=dirActual.substring(dirActual.lastIndexOf("\\")+1);
        System.out.println("");
        DataBase get = this.bases.get(nameDB);
        get.setNumTablas(get.getNumTablas()-1);
        return (T)""; 
    }
    
    
    /******************
     *muestra las columnas de una tabla dada y sus tipos
     *
    *******************/
    @Override
    public T visitMostrarColumnasTB(GramaticaParser.MostrarColumnasTBContext ctx) 
    {
        
        String tableName = ctx.ID().getText();
        //revisa que la tabla exista
        if(this.tablasActuales.containsKey(tableName) == false)
        {
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la tabla " + tableName + " que no existe" );
            return (T)"error al mostrar tabla que no existe";
        }
        //muestra la tabla
        Table tabla = this.tablasActuales.get(tableName);
        JTable toShow = elCreador.ShowColumnsFrom(tabla);
        JFrame frame = new JFrame();
        frame.setTitle("Columnas de " + tableName);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane scrollPane = new JScrollPane(toShow);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(500, 150);
        frame.setVisible(true);
        return (T)"";
    }
    /******************
     *muestra las tablas y su numero de registros para la DB seleccionada
     *
    *******************/
    @Override
    public T visitMostrarTablasTB(GramaticaParser.MostrarTablasTBContext ctx) 
    {
        if(this.bUse)
            try {
                List<Table> tablas = this.getInfoMDLocal(dirActual);
                if(tablas.isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Actualmente no hay tablas en esta base de datos");
                }
                else
                {
                JTable toShow = elCreador.ShowTables(tablas);
                JFrame frame = new JFrame();
                frame.setTitle("Tablas de Base de Datos Actual");
    //            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JScrollPane scrollPane = new JScrollPane(toShow);
                frame.add(scrollPane, BorderLayout.CENTER);
                frame.setSize(500, 150);
                frame.setVisible(true);
                }
            } catch (IOException ex) {
                Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        return (T)"";
    }
    
    /******************
     *redirige a las siguientes alter tables 
     *
    *******************/
    @Override
    public T visitAlterarTB(GramaticaParser.AlterarTBContext ctx) {
        if(!this.bUse){
            revVerb("no hay DB seleccionada");
            this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no pueder alterar una tabla porque no hay DB seleccionada");
            return (T)"error porque no hay DB elegida";
            
        }
        this.tablaActualName=ctx.ID().getText();
        if(!this.tablasActuales.containsKey(tablaActualName)){
            revVerb("no existe la tabla que se quiere modificar");
            this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no pueder alterar la tabla "+tablaActualName+" porque no existe");
            return (T)"error porque no hay DB elegida";
        }
        revVerb("voy a alterar la TB:"+tablaActualName);
        this.ActualTable=this.tablasActuales.get(tablaActualName);
//        System.out.println("voy a alterar la TB:"+tablaActualName);
        for (GramaticaParser.TableActionContext tableAction : ctx.tableAction()) {
            String visit = (String) visit(tableAction);
            if(visit.contains("error")){
                return (T)visit;
            }
        }
        return (T)"";
    }
    
    
    @Override
    public T visitDropConstraintTB(GramaticaParser.DropConstraintTBContext ctx) {
        
        String nameConstr=ctx.ID().getText();
        boolean b=false;
        
        if(this.ActualTable.CheckPK()){
            
            if(this.ActualTable.PkS.name.equals(nameConstr)){
                this.ActualTable.Pk=false;
                b=true;
            }
        }
        if(!b)
            for (FkConstraint fk : this.ActualTable.FkS) {
                if(fk.name.equals(nameConstr)){
                    this.ActualTable.FkS.remove(fk);
                    b=true;
                    break;
                }
            }
        
        if(!b){
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la constraint \"" + nameConstr + "\" en la tabla "+this.ActualTable.getNombre()+", pero esta constraint no existe" );
            return (T)"error al borrar constraint que no existe";
        }
        
        return (T)"";
    }
    
    /******************
     *agregar constraint a la tabla especificada
     *
    *******************/
    @Override
    public T visitAddConstraintTB(GramaticaParser.AddConstraintTBContext ctx) {
        String visit = (String) visit(ctx.constraint());
        return (T)visit;
    }
    
     /******************
     *agrega una columna a la tabla
     *
    *******************/
    
    @Override
    public T visitAddColumnTB(GramaticaParser.AddColumnTBContext ctx) {
        
        /******************************
            FALTA HACER LAS VALIDACIONES CON EL ARCHIVO XML
        ******************************/
        
        this.revVerb("revisar si la columna que voy a insertar ya existe");
        
        String data=(String)visit(ctx.columna());
        // retriebe table object from map and verify table 
        if(this.tablasActuales.containsKey(tablaActualName) == false)
        {
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la tabla " + tablaActualName + " que no existe" );
            return (T)"error al alterar tabla que no existe";
        }
        Table tabla = this.tablasActuales.get(tablaActualName);
        
//        System.out.println("****************");
//        System.out.println(tabla.toString());
        
        //separar el id pos 0 y el tipo pos 1 para agregarlos a las distintas listas
        String[] split = data.split(",");
        if(tabla.IDs.contains(split[0]))
        {
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "agrega la columna " + split[0] + " que ya existe" );
            return (T)"error al agregar columna que ya existe";
        }
        tabla.IDs.add(split[0]);
        tabla.Tipos.add(split[1]);
        tabla.columnas.put(split[0], split[1]);
        
//        System.out.println("****************");
//        System.out.println(tabla.toString());
        
        ContenidoTabla tablaXmodificar = this.registrosTablasActuales.get(tablaActualName);
        if(tablaXmodificar!=null){
            for (HashMap registro : tablaXmodificar.getLista()) {
                registro.put(split[0], "");
            }
        }
        //buscar en 
//      AGREGO LAS NUEVAS POSICIONES QUE VOY A NECESITAR MAS ADELANTE
        //this.metaDataLOCALTBcolumnas.add(new ArrayList());//.get(i).add(split[0]);
        //this.metaDataLOCALTBtipos.add(new ArrayList());//.add(split[1]);
//            AGREGO EL EL NOMBRE Y TIPO DE LA COLUMNA EN LA POSICION CORRESPONDIENTE A LA TABLA 
//        REVISO QUE NO EXISTA UNA COLUMNA CON EL MISMO NOMBRE
        
        
        
        
        return (T)"";
    }
     /******************
     *renombra una tabla 
     *
    *******************/
    
    @Override
    public T visitRenameTB(GramaticaParser.RenameTBContext ctx) {
        revVerb("voy a revisar si la tabla que voy a renombrar si existe");
        
        if(this.tablasActuales.containsKey(tablaActualName) == false)
        {
            for(int i=0; i<this.tablasActuales.size(); i++)
            {
                System.out.println(this.tablasActuales.toString());
            }
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la tabla " + tablaActualName + " que no existe" );
            return (T)"error al renombrar bla bla tabla que no existe";
        }
        if(this.tablasActuales.containsKey(ctx.ID().getText()))
        {
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "renombra a " + ctx.ID().getText() + " que ya existe" );
            return (T)"error al renombrar tabla con un nombre existente";
        }
        
        Table tabla = this.tablasActuales.get(tablaActualName);
        
//        System.out.println("****************");
//        System.out.println(tabla.toString());
        
        this.tablasActuales.remove(tablaActualName);
        tabla.nombre = ctx.ID().getText();
        this.tablasActuales.put(ctx.ID().getText(), tabla);
        
//        System.out.println("****************");
//        System.out.println(tabla.toString());
        
        File old=new File(dirActual+"\\"+tablaActualName+".json");
        old.renameTo(new File(dirActual+"\\"+ctx.ID().getText()+".json"));
        
        ContenidoTabla contenido = this.registrosTablasActuales.get(tablaActualName);
        if(contenido!=null){
            this.registrosTablasActuales.remove(tablaActualName);
            contenido.setTabla(tabla);
            this.registrosTablasActuales.put(tabla.getNombre(), contenido);
        }
        return (T)""; 
        
    }
    
     /******************
     *elimina una columna para una tabla dada 
     *
    *******************/
    @Override
    public T visitDropColumnTB(GramaticaParser.DropColumnTBContext ctx) 
    {
        String tableName = tablaActualName;
        String columnName = ctx.ID().getText();
        //verificar si la tabla existe
        if(this.tablasActuales.containsKey(tableName) == false)
        {
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la tabla " + tableName + " que no existe" );
            return (T)"error al alterar tabla que no existe";
        }
        //verificar si la columna existe
        Table tabla = this.tablasActuales.get(tableName);
        
//        System.out.println("****************");
//        System.out.println(tabla.toString());
        
        if(tabla.IDs.contains(columnName) == false)
        {
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la columna " + columnName + " que no existe" );
            return (T)"error al eliminar columna que existe";
        }
        //eliminar la columna
        
//        REVISAR QUE NO SEA UNA FOREIGN KEY
        
        //ITERAR SOBRE TODAS LAS TABLAS ACTUALES
        for (Table tablaXrev : this.tablasActuales.values()) {
            if(!tablaXrev.getNombre().equals(tablaActualName))
            //ITERAR SOBRE TODAS LAS FOREIGN KEYS DE LAS TABLAS
            for (FkConstraint listaFKs : tablaXrev.FkS) {
                //si la tabla a la que referencio es la que voy a alterar
                if(listaFKs.references.getNombre().equals(tablaActualName)){
                    for (String columnaDestinoFK : listaFKs.FkColumnsDestiny) {
                        if(columnaDestinoFK.equals(columnName)){
                            this.errores.add("La linea: " + ctx.start.getLine() + ", " + "intenta borrar la columna " + columnName + " que es foreign key de la tabla "+tablaXrev.getNombre()+", primero debe eliminar esta constraint" );
                            return (T)"error al tratar de eliminar columna que existe";
                        }
                    }
                }
            }
        }
        
        
//        revisar si es primary key de la tabla
//        para cambiar la bandera
        if(tabla.CheckPK()){
            if(tabla.PkS.PkColumns.contains(columnName)){
                tabla.PkS.PkColumns.remove(columnName);
                if(tabla.PkS.PkColumns.size()>0){
                     tabla.Pk=false;
                }
            }
        }
        int indice = tabla.IDs.indexOf(columnName);
        tabla.IDs.remove(indice);
        tabla.Tipos.remove(indice);
        tabla.columnas.remove(columnName);
        
//        System.out.println("****************");
//        System.out.println(tabla.toString());
        
        ContenidoTabla tablaXmodificar= this.registrosTablasActuales.get(tableName);
        if(tablaXmodificar!=null){
            for (HashMap registro : tablaXmodificar.getLista()) {
                registro.remove(columnName);
            }
        }
        
        return (T)"";    
        
    }
     /******************
     *retorna la culomna a agregar 
     *
    *******************/
    @Override
    public T visitColumna(GramaticaParser.ColumnaContext ctx) {
        
        String td=ctx.ID().getText()+","+visit(ctx.type());
        return (T)td;
    }

    @Override
    public T visitInsertValues(GramaticaParser.InsertValuesContext ctx) {
        List<String> nombres=new ArrayList();
        for (GramaticaParser.ValueContext value : ctx.value()) {
            nombres.add((String) visit(value));
        }
        return (T)nombres;
    }

    @Override
    public T visitInsertColumns(GramaticaParser.InsertColumnsContext ctx) {
        List<String> nombres=new ArrayList();
        for (TerminalNode ID : ctx.ID()) {
            nombres.add(ID.getText());
        }
        return (T)nombres;
    }
    
    @Override
    public T visitInsert(GramaticaParser.InsertContext ctx) {
        
        if(!this.bUse){
            revVerb("no hay DB seleccionada");
            this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no puede crear una tabla porque no hay DB seleccionada");
            return (T)"error porque no hay DB elegida";
            
        }
        
        String nameTabla=ctx.ID().getText();
        revVerb("buscando la tabla "+nameTabla);
        Table tabla = this.tablasActuales.get(nameTabla);
        
//        REVISAR QUE LA TABLA SI EXISTA
        if(!(tabla!=null)){
            this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no puede insertar en la tabla ("+nameTabla+") porque esta no existe");
            return (T)"error porque no existe la tabla escogida";
        }
        
//        REVISAR SI LA LISTA DE COLUMNAS FUE PROVISTA POR EL USUARIO O SIMPLEMENTE USO EL ORDEN IMPLÍCITO
        List<String> orden = (List<String>)visit(ctx.insertColumns());
        
        
        List<String> valores;
        HashMap tmp;
        
        if(orden.size()>0){
            
            for (String columna : orden) {
                if(!tabla.IDs.contains(columna)){
                    this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no puede insertar en la tabla ("+nameTabla+") porque una de las columnas definidas no existe");
                    return (T)"error porque no existe la columna escogida";
                }
            }
            
//            System.out.println("si hay lista de colunas!!!!");
//            REVISAR QUE EL NUMERO DE COLUMNAS Y EL DE VALORES COINCIDA
            valores=(List<String>) visit(ctx.insertValues());
            if(valores.size()!=orden.size()){
                this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no puede insertar en la tabla ("+nameTabla+") porque el numero de columnas provista no es el mismo que el numero de valores para insertar");
                return (T)"error porque no coincide el numero de columnas provistas con el de valores";
            }
            revVerb("el numero de columas por insertar si coincide con el numero de valores provistos");
            ContenidoTabla registros = this.registrosTablasActuales.get(nameTabla);            
            
//            REVISAR SI LA TABLA YA TIENE CONTENIDO EXISTENTE
            if(!(registros!=null)){
                
                /*************************
                 * SI LA TABLA ESTA VACIA CREO UNA NUEVO OBJETO PARA ALMACENAR SUS REGISTROS
                 **************************/
                
//                System.out.println("la tabla no tiene datos");
                ContenidoTabla cont = new ContenidoTabla();
                cont.setTabla(tabla);
                this.registrosTablasActuales.put(tabla.getNombre(), cont);
                registros=this.registrosTablasActuales.get(tabla.getNombre());
            }
            /************************
             *          FALTA REVISAR LO DE LAS CONSTRAINTS
             ************************/

            tmp=new HashMap();


            /************************
             * REVISAR QUE EL TIPO DE LAS COLUMNAS COINCIDA
             * SEGUN EL ORDEN QUE EL USUARIO DEFINIÓ
             ************************/
            HashMap<String, String> columnas = tabla.getColumnas();

            for (int i = 0; i < tabla.IDs.size(); i++) {
                String columna=tabla.IDs.get(i);

                if(orden.contains(columna)){
                    int indice=orden.indexOf(columna);
                    String val=valores.get(indice);
//                    System.out.println("val->"+val);
                    String nombreColumnaXinsertar=orden.get(indice);
                    String tipoColumnaXinsertar=val.substring(0,val.indexOf("-"));
                    String valorColumnaXinsertar=val.substring(val.indexOf("-")+1);
                    String tipoColumnaEsperado = columnas.get(nombreColumnaXinsertar);
                    int tamano=0;
                    if(tipoColumnaEsperado.contains("char")){
                        tamano=Integer.parseInt(tipoColumnaEsperado.substring(tipoColumnaEsperado.indexOf("(")+1, tipoColumnaEsperado.indexOf(")")));
                        tipoColumnaEsperado="char";
                        
                    }
                    
//                    REVISAR SI LA COLUMNA QUE VOY A INSERTAR ES PRIMARY KEY
                    if( tabla.CheckPK() ){
                        
                        if(registros!=null){
                            
//                        HACER UNA COMPARACION CORRECTA EN CASO DE QUE SEAN VARIAS COLUMNAS
                            if(tabla.PkS.PkColumns.size()>1){
                                 int numMagico=tabla.PkS.PkColumns.size();
                                for (HashMap fila : registros.getLista()) {
                                    for (Object llaves : fila.keySet()) {
                                        System.out.print(llaves+",");
                                    }
                                    System.out.println("");
                                    int cont=0;
                                    int j=0;
                                    String elFallo="";
                                    for (String columnaPk : tabla.PkS.PkColumns) {
                                        int indixPk=orden.indexOf(columnaPk);
                                        String elValors = valores.get(indixPk).substring(val.indexOf("-")+1);
                                        System.out.println(fila.get(columnaPk)+" <-> "+elValors);
                                        if(fila.get(columnaPk).equals(elValors)){
                                            elFallo+=elValors+",";
                                            cont++;
                                        }else{
                                            break;
                                        }
                                        
                                    }
                                    if(cont==numMagico){
                                        this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no puede insertar en la tabla ("+nameTabla+") porque los valores de la PRIMARY KEY  ("+elFallo+") ya existen ");
                                        return(T)"";
                                    }
                                    
                                }
                            }else{
                                
                                for (HashMap fila : registros.getLista()) {
                                    if(fila.get(columna).equals(valorColumnaXinsertar)){
                                        this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no puede insertar en la tabla ("+nameTabla+") porque el valor:" +columna+"="+valorColumnaXinsertar+" ya existe en la tabla");
                                        return (T)"error al insertar valor duplicado por PK";
                                    }
                                }

                            }
                        }

                        
                    }
                    
                    

//                    REVISAR SI EL TIPO DE LOS VALORES X INSERTAR COINCIDE CON EL ESPERADO

//                    EL SWITCH SE HACE XQ PUEDE EXISTIR LA CONVERSION FORZOSA EN LOS INT Y FLOAT
                    switch(tipoColumnaEsperado){
                        case("int"):
//                            LOS UNICOS TIPOS DE DATOS QUE PUEDO CONVERTIR SON INT Y FLOAT
                            switch(tipoColumnaXinsertar){
                                case "float":
                                    tmp.put(nombreColumnaXinsertar, valorColumnaXinsertar.substring(0, valorColumnaXinsertar.indexOf(".")));
                                    break;
                                case "int":
                                    tmp.put(nombreColumnaXinsertar, valorColumnaXinsertar);
                                    break;
                                case("null"):
                                    tmp.put(nombreColumnaXinsertar,"");
                                    break;
                                default:
                                    if(!tipoColumnaXinsertar.equals(tipoColumnaEsperado)){
                                        this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no puede insertar en la tabla ("+nameTabla+") porque el tipo de "+valorColumnaXinsertar+" es: ("+tipoColumnaXinsertar+") y esperaba: ("+tipoColumnaEsperado+")");
                                        return (T)"error porque no coincide el tipo de columnas provistas para insertar con el tipo de valores para insertar";
                                    }
                                    break;
                            }

                            break;
                        case("float"):

                            switch(tipoColumnaXinsertar){
                                case "float":
                                    tmp.put(nombreColumnaXinsertar, valorColumnaXinsertar);
                                    break;
                                case "int":
                                    tmp.put(nombreColumnaXinsertar, valorColumnaXinsertar+".0");
                                    break;
                                case("null"):
                                    tmp.put(nombreColumnaXinsertar,"");
                                    break;
                                default:
                                    if(!tipoColumnaXinsertar.equals(tipoColumnaEsperado)){
                                        this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no puede insertar en la tabla ("+nameTabla+") porque el tipo de "+valorColumnaXinsertar+" es: ("+tipoColumnaXinsertar+") y esperaba: ("+tipoColumnaEsperado+")");
                                        return (T)"error porque no coincide el tipo de columnas provistas para insertar con el tipo de valores para insertar";
                                    }
                                    break;
                            }

                            break;
                            
                        case("char"):
                            if(valorColumnaXinsertar.length()>tamano){
                                this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no puede insertar en la tabla ("+nameTabla+") porque el tamaño del char encontrado es: ("+valorColumnaXinsertar.length()+") y esperaba como max: ("+tamano+")");
                                return (T)"error porque no el tamano del char es muy grande";
                            }
                            tmp.put(nombreColumnaXinsertar, valorColumnaXinsertar);
                            break;
                        default:
                            if(!tipoColumnaXinsertar.equals(tipoColumnaEsperado)){
                                this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no puede insertar en la tabla ("+nameTabla+") porque el tipo de "+valorColumnaXinsertar+" es: ("+tipoColumnaXinsertar+") y esperaba: ("+tipoColumnaEsperado+")");
                                return (T)"error porque no coincide el tipo de columnas provistas para insertar con el tipo de valores para insertar";
                            }
                            tmp.put(nombreColumnaXinsertar, valorColumnaXinsertar);
                            break;
                    }

                }else{
                    tmp.put(columna, "");
                }
            }
            
            //AQUI ESTABA LO DEL FOR DE ARRIBA ANTES
            
            registros.addRegistro(tmp);
            
        }else{
            revVerb("se va a insertar en el orden predefinido");
            System.out.println("no hay lista jajaja");
            valores=(List<String>) visit(ctx.insertValues());
        }
        
        return (T)""; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visitTipoChar(GramaticaParser.TipoCharContext ctx) {
        String t="char("+ctx.NUM()+")";
        return (T)t;
    }

    @Override
    public T visitTipoEntero(GramaticaParser.TipoEnteroContext ctx) {
        return (T)"int";
    }

    @Override
    public T visitTipoDAte(GramaticaParser.TipoDAteContext ctx) {
        return (T)"date";
    }

    @Override
    public T visitTipoFloat(GramaticaParser.TipoFloatContext ctx) {
        return (T)"float";
    }

    @Override
    public T visitEntero(GramaticaParser.EnteroContext ctx) {
        String t="int-";
        t+=ctx.getText();
        return(T) t;
    }
    
    @Override
    public T visitDecimal(GramaticaParser.DecimalContext ctx) {
        String t="float-";
        t+=ctx.getText();
        return (T)t; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visitFecha(GramaticaParser.FechaContext ctx) {
//        System.out.println("FECHA VISITADA->"+ctx.getText());
        
       
//        OBTENER LOS NUMEROS DE LA FECHA INGRESADA
//        SE USA UNSIGNEDINT PORQUE LOS NUMEROS SON TODOS POSITIVOS 

        int year=Integer.parseInt(ctx.getText().substring(1, 5));
        int month=Integer.parseInt(ctx.getText().substring(6, 8));
        int day=Integer.parseInt(ctx.getText().substring(9,11));
        //REVISAR QUE EL MES SEA VALIDO
        
        switch (month) {
            case 1: case 3: case 5:
            case 7: case 8: case 10:
            case 12:
                if(day>31){
                    this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), Los meses no deben tener mas de 31 dias");
                    return(T)"error en el numero de dia";
                }
                break;
            case 4: case 6:
            case 9: case 11:
                if(day>30){
                    this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), Los meses no deben tener mas de 30 dias");
                    return(T)"error en el numero de dia";
                }
                break;
            case 2:
                if (((year % 4 == 0) && 
                     !(year % 100 == 0))
                     || (year % 400 == 0))
                    if(day>29){
                        this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), Febrero no puede tener mas de 29 dias");
                        return(T)"error en la el dia de febrero";
                    }
                else
                    if(day>28){
                        this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), Febrero no puede tener mas de 28 dias");
                        return(T)"error en la el dia de febrero";
                    }
                break;
            default:
                this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), Los meses no deben ser mayores a 12");
                return(T)"error en el numero de mes";
                
        }
        /*
        if(month>12){
            this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), Los meses no deben ser mayores a 12");
            return(T)"error en el numero de mes";
        }
        
        //REVISAR QUE EL DIA SEA VALIDO
        if(day>31){
            this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), Los meses no deben tener mas de 31 dias");
            return(T)"error en el numero de dia";
        }
        
        //REVISAR QUE EL CASO ESPECIAL DE FEBRERO
        if(month==2 && day>29){
            this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), Febrero no puede tener mas de 29 dias");
            return(T)"error en la el dia de febrero";
        }
        */
        String t="date-";
        t+=ctx.getText();
        return (T)t; //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public T visitCharacter(GramaticaParser.CharacterContext ctx) {
        String t="char-";
        t+=ctx.getText();
        return (T)t;
    }

    @Override
    public T visitNullo(GramaticaParser.NulloContext ctx) {
        String t="null-";
        t+="!!";
        return (T) t;//To change body of generated methods, choose Tools | Templates.
    }
    
    

    
/****************************************************************************************************
                                        METODOS AUXILIARES
****************************************************************************************************/
    
    public ArrayList<String> getErrores() {
        return this.errores;
    }

    
    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    
    /*******************************
     * metodo para imprimir mensajes en consola si la opcion Verbose es activada
    *******************************/
    private void revVerb(String mensaje){
        if(verbose){
            System.out.println(mensaje);
        }
    }
    
    
    /*************************
        IMPRIME LOS DATOS EXISTENTES EN LA METADA GENERAL
    *************************/
    
    private void crearArchivo(String dir) throws IOException{
        File porCrear=new File(dir);
        if(!porCrear.exists()){
            porCrear.createNewFile();
        }else{
            throw new IOException("EL archivo "+dir+" no puede ser creado porque ya existe");
        }
        
    }
    
    /******************************
     *  METODO QUE AGREGA UNA DB EN LA METADA GENERAL    
     * 
    *******************************/
    
    private void addToMDGeneral(DataBase db) throws IOException{
        File gen=new File("Bases de Datos\\metaData_GENERAL.json");
        BufferedWriter bw = new BufferedWriter(new FileWriter(gen, true));
        String json = this.Gsoneador.toJson(db);
        bw.write(json);
        bw.newLine();
        bw.close();
    }
    
    
    
    /************************
     * METODO QUE SIRVE PARA OBTENER LA LISTA DE LAS BASES DE DATOS EXISTENTES PARA SU USO POSTERIOR
     * DEVUELVE NULL SI NO HAY DB's CREADAS
     * ESTE ES UN METODO AUXILIAR
    *************************/
    
    private List<DataBase> getInfoMDGeneral() throws FileNotFoundException, IOException{
        File gen=new File("Bases de Datos\\metaData_GENERAL.json");
        String arc="[";
        BufferedReader br = new BufferedReader(new FileReader(gen));
        String s="";        
        while(((s=br.readLine())!=null)){
            arc+=s+",";
        }
        if(br!=null){
            br.close();
        }
        arc=arc.substring(0, arc.length()-1)+"]";
        //System.out.println("******************->"+arc);
        
        final Type tipoListaDB = new TypeToken<List<DataBase>>(){}.getType();
        final Gson gson = new Gson();
        List<DataBase> bases= new ArrayList<DataBase>();
        try{
            bases= gson.fromJson(arc, tipoListaDB);
        }catch(Exception e){
            return bases;
        }
        
        return bases;
    }
    
    /************************
     * METODO QUE SIRVE PARA OBTENER LA LISTA DE LAS TABLAS EXISTENTES
     * DEVUELVE NULL SI NO HAY DB's CREADAS
     * ESTE ES UN METODO AUXILIAR
    *************************/
    
    private List<Table> getInfoMDLocal(String dir) throws FileNotFoundException, IOException{
        File gen=new File(dir+"\\METADATA.json");
        String arc="[";
        BufferedReader br = new BufferedReader(new FileReader(gen));
        String s="";        
        while(((s=br.readLine())!=null)){
            arc+=s+",";
        }
        if(br!=null){
            br.close();
        }
        arc=arc.substring(0, arc.length()-1)+"]";
        //System.out.println("******************->"+arc);
        
        final Type tipoListaTB = new TypeToken<List<Table>>(){}.getType();
        final Gson gson = new Gson();
        List<Table> tablas=new ArrayList<Table>();
        try{
            tablas= gson.fromJson(arc, tipoListaTB);
        }catch(Exception e){
            return tablas;
        }
        
        return  tablas;
    }
    
    
    private List<HashMap<String,String>> getInfoArchivo(String dir) throws FileNotFoundException, IOException{
        File gen=new File(dir+"\\METADATA.json");
        String arc="[";
        BufferedReader br = new BufferedReader(new FileReader(gen));
        String s="";        
        while(((s=br.readLine())!=null)){
            arc+=s+",";
        }
        if(br!=null){
            br.close();
        }
        arc=arc.substring(0, arc.length()-1)+"]";
        //System.out.println("******************->"+arc);
        
        final Type tipoListaTB = new TypeToken<List<HashMap<String,String>>>(){}.getType();
        final Gson gson = new Gson();
        List<HashMap<String,String>> tablas=new ArrayList();
        try{
            tablas= gson.fromJson(arc, tipoListaTB);
        }catch(Exception e){
            return tablas;
        }
        
        return  tablas;
    }
    
    
    /*************************
     * METODO QUE RENOMBRA UNA DB EN LA METADA GENERAL
     * 
    *************************/
    
    private void renameFromMDGeneral(String oldName,String newName) throws FileNotFoundException, IOException{
        
        List<DataBase> bases=getInfoMDGeneral();
        if(bases!=null){
            File gen=new File("Bases de Datos\\metaData_GENERAL.json");
            gen.delete();

    //        HACER EL NUEVO ARCHIVO DE METADATA
            gen.createNewFile();

            //itero sobre las bases existentes para cambiar el nombre de la que busco y las agrego de nuevo al archivo
            for (DataBase base : bases) {
                if(base.getName().equals(oldName)){

                    base.setName(newName);
                    addToMDGeneral(base);

                }else{
                    addToMDGeneral(base);
                }

            }
        }
        
       
    }
    
    
    private void renamesFromMDGeneral(String oldName,String newName) throws FileNotFoundException, IOException{
        
        List<DataBase> bases=getInfoMDGeneral();
        if(bases!=null){
            File gen=new File("Bases de Datos\\metaData_GENERAL.json");
            gen.delete();

    //        HACER EL NUEVO ARCHIVO DE METADATA
            gen.createNewFile();

            //itero sobre las bases existentes para cambiar el nombre de la que busco y las agrego de nuevo al archivo
            for (DataBase base : bases) {
                if(base.getName().equals(oldName)){

                    base.setName(newName);
                    addToMDGeneral(base);

                }else{
                    addToMDGeneral(base);
                }

            }
        }
        
       
    }
    
    private void cargarBases(){
        List<DataBase> infoMDGeneral=null;
        try {
             infoMDGeneral= this.getInfoMDGeneral();
        } catch (IOException ex) {
            Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(infoMDGeneral!=null){
            for (DataBase DB : infoMDGeneral) {
                this.bases.put(DB.getName(), DB);
            }
        }
    }
    
    /*************************
     * METODO QUE ELIMINA UNA DB EN LA METADA GENERAL 
     * 
    *************************/
    
    private void eliminarFromMDGeneral(String name) throws IOException{
        
        List<DataBase> bases=getInfoMDGeneral();
        if(bases!=null){
            File gen=new File("Bases de Datos\\metaData_GENERAL.json");
            gen.delete();
    //        HACER EL NUEVO ARCHIVO DE METADATA
            gen.createNewFile();


            //itero sobre las bases existentes para cambiar el nombre de la que busco y las agrego de nuevo al archivo
            for (DataBase base : bases) {
                if(base!=null&&base.getName().equals(name)){

                }else{
                    addToMDGeneral(base);
                }

            }
        }
    }
    
     /**************************
      *     METODO QUE ELIMINA UNA DB 
      *************************/
    
    private void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }
    
    /****************************
     *  MÉTODO QUE VERIFICA EL USO DE UNA TABLE YA "ABIERTA" PARA OPTIMIZAR TIEMPO
     ****************************/
    
    public boolean TablaExistente(String NombreTabla){ 
        /******
         * Cambiar de tabla
         */
        
        if ((this.TablaActual.length() == 0) || (TablaActual.equals(NombreTabla))){
            TablaActual = NombreTabla;
            return false;
        }
        else{
            TablaActual = NombreTabla;
            return true;
        }
    }
    
    public void WriteJSon() throws DirectoryNotEmptyException, IOException{
        try {
            boolean success = Files.deleteIfExists(Paths.get(this.dirActual+"\\METADATA.json"));
        } catch (IOException | SecurityException e) {
            System.err.println(e);
        }
        File gen=new File(this.dirActual+"\\METADATA.json");
        gen.delete();
        gen.createNewFile();
        for(Table TableActual: tablasActuales.values()){
            ContenidoTabla content = this.registrosTablasActuales.get(TableActual.getNombre());
            if(content!=null){
                TableActual.setCantRegistros(content.getLista().size());
            }
            String Jsoneado = Gsoneador.toJson(TableActual);
            Jsoneado = Jsoneado +"\n";
            try {
                Files.write(Paths.get(this.dirActual+"\\METADATA.json"), Jsoneado.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException ex) {
                Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private List<String> prepareToAddTable(ArrayList<HashMap> lista){
        /*System.out.println("voy a preparar estos mapas");
                    for (HashMap mapa : lista) {
                        System.out.println("mapa");
                        for (Iterator it = mapa.entrySet().iterator(); it.hasNext();) {
                            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                            String key = entry.getKey();
                            String value = entry.getValue();
                            System.out.println("key, " + key + " value " + value);
                        }
                    }*/
        ArrayList<String> result=new ArrayList();
        for (HashMap registro : lista) {
            result.add(this.Gsoneador.toJson(registro));
        }
//        System.out.println(result.toString());
        return result;
    }
    
    private void escribirTabla(List<String> data,File dir) throws IOException{
        //System.out.println("ESTO es lo que voy a escribir:"+data.toString());
        dir.delete();
        dir.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(dir, true));
        for (String dato : data) {
            bw.write(dato);
            bw.newLine();
        }
        bw.close();
    }
    
    private void writeData(){
        if(!this.lastDB.equals("")){
            File dir= new File(this.lastDB);
            final File[] listFiles = dir.listFiles();
        //              RECORRER TODAS LAS TABLAS EXISTENTES PARA
            if(!(listFiles!=null)||listFiles.length<1){
            }else{
                for (File tabla : listFiles) {

        //                REVISAR QUE EL NOMBRE DE LA TABLA QUE VOY A ESCRIBIR NO SEA EL DE LA METADATA
                    if(!tabla.getName().contains("METADATA")){
                        String nombre=tabla.getName().substring(0, tabla.getName().indexOf("."));
//                        System.out.println("VOY A ESCRIBIR LA TABLA:"+tabla.getName());
//                        System.out.println(nombre);
//                        System.out.println(this.registrosTablasActuales.containsKey(nombre));
                        ContenidoTabla content = this.registrosTablasActuales.get(nombre);

                        if(content!=null){
                            List<String> datos=prepareToAddTable(content.getLista());

                            try {
                                escribirTabla(datos,tabla);
                            } catch (IOException ex) {
                                System.err.println("ERROR AL ESCRIBIR DE REGRESO LOS DATOS DE LA DB"+this.lastDB);
                                //Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
                
            }
            
            try {
                WriteJSon();
            } catch (IOException ex) {
                //Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    /************************
     * ESTE METODO DEVUELVE LOS DATOS DE LA DB
     * CARGAADOS EN LAS ESTRUCTURAS DE MEMORIA
     * PARA SU USO Y MODIFICACION
    *************************/
    private HashMap<String,ContenidoTabla> cargarRegistros(File folder){
//        System.out.println("entro a llenar el hashmap para la db actual");
//        System.out.println("el dir actual es->"+dirActual);
        File[] tablas = folder.listFiles();
        HashMap<String,ContenidoTabla> dataTablas=new HashMap();
        final Type tipoListaHashMap = new TypeToken< List <HashMap<String,String> > >(){}.getType();
        final Gson gson = new Gson();
        List<HashMap<String,String>> datos=null;
        String desGsoneado="";
        
        
        
        for (File tabla : tablas) {
            if(tabla.getName().contains("METADATA")){
                
            }else{
//                System.out.println("FILE SOBRE LA ITERACION " +tabla.getName());
        //            crear el nuevo objeto para almacenar todos los registros de la tabla actual
                    ContenidoTabla readingTable=new ContenidoTabla();
                    readingTable.setTabla(this.tablasActuales.get(tabla.getName()));

                    /*********************
                    * cargar en un string
                    * los registros de la tabla que voy leyendo
                   **********************/
                   try {
                       desGsoneado=getDatos(tabla);
                   } catch (IOException ex) {
                       
//                       System.out.println(tabla.getAbsolutePath());
                       Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
                   }

                   /*********************
                    * convertir el string anterior
                    * en un mapa si tiene al menos un registro
                   **********************/
                   if(desGsoneado.equals("]")){
                       revVerb("la tabla "+tabla.getName()+" no tiene datos ingresados");
                   }else{
                       revVerb("la tabla "+tabla.getName()+" si tiene datos ingresados");
                       try{
                       datos= gson.fromJson(desGsoneado, tipoListaHashMap);
                       }catch(Exception e){

                       }
//                       System.out.println(desGsoneado);
                       /*
                       for (HashMap<String, String> dato : datos) {
                           System.out.println("HASHMAP");
                           for (Map.Entry<String, String> entry : dato.entrySet()) {
                                String key = entry.getKey().toString();
                                String value = entry.getValue();
                                System.out.println("key, " + key + " value " + value);
                           }
                       }*/
                       
                       //agregar los registros al atributo de la lista
                       ArrayList<HashMap> at=new ArrayList();
                       /*
                       if(tabla.getName().contains("carro")){
                        HashMap<String,String> ne=new HashMap();
                        ne.put("marca", "maserati");
                        ne.put("precio", "9876.23");
                        ne.put("id", "14");
                        at.add(ne);
                        
                       }else{
                           if(tabla.getName().contains("local")){
                               HashMap<String,String> ne=new HashMap();
                                ne.put("apertura", "saber77");
                                ne.put("direccion", "zona 8");
                                ne.put("empleados", "5");
                                at.add(ne);
                           }else{
                               HashMap<String,String> ne=new HashMap();
                                ne.put("nombre", "porsche");
                                ne.put("sueldo", "876.23");
                                ne.put("nacimiento", "saber99");
                                at.add(ne);
                           }
                       }
                       */
                       if((datos!=null) && datos.size()>0){
                           at.addAll(datos);
                       }
                       
                       
                       readingTable.setLista(at);
                   }
        //           AGREGAR EL LA TABLA CON SUS DATOS CARGADOS AL HASHMAP CREADO
                   String nombre=tabla.getName();
                   nombre=nombre.substring(0,nombre.indexOf("."));
//                   System.out.println("nombre de la tabla que voy cargando->"+nombre);
                   dataTablas.put(nombre, readingTable);
                }
            
            }
//            System.out.println(dataTablas.toString());
            return dataTablas;
    }
    
    /******************
     * OBTENER EL STRING QUE ESTA ESCRITO EN EL 
     * ARCHIVO.JSON QUE REPRESENTA LA TABLA
     * QUE ESTOY LEYENDO
    *******************/
    
    private String getDatos(File tabla) throws FileNotFoundException, IOException{
        String arc="[";
        
        BufferedReader br = new BufferedReader(new FileReader(tabla));
        String s;
        while(((s=br.readLine())!=null)){
            arc+=s+",";
        }
        br.close();
        arc=arc.substring(0, arc.length()-1)+"]";
        return arc;
    }
    
    private void escribirMD(){
        File file= new File(dirBase+"\\metaData_GENERAL.json");
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (DataBase value : this.bases.values()) {
            try {
                this.addToMDGeneral(value);
            } catch (IOException ex) {
                Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
}
