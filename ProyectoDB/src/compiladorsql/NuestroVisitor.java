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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.List;
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
    private ArrayList<String> errores,
            metaDataLOCALTBnames;
    public Gson Gsoneador = new Gson(); 
            
    public DataBase DBactual;
    private ArrayList metaDataGENERALDBnumTablas,
            metaDataLOCALTBelementosNum;
    
    private ArrayList<ArrayList<String>> metaDataLOCALTBcolumnas,
            metaDataLOCALTBtipos;
    
    private boolean verbose=false,
            bUse=false;
    private String hb="";
    private String TablaActual;
    private ArrayList<Table> metaDataActual;
    private HashMap<String,Table>tablasActuales;
    public TableMaker elCreador = new TableMaker();
    
    //tabla y constraints se inicializan y el objse arma al final
    public Table NewlyCreatedTable;
    public PkConstraint PkC;
    public FkConstraint FkC;
    public ChConstraint ChC;
    
    private String nameTablaActual="";
    private HashMap<String,ContenidoTabla> registrosTablasActuales;
    
/****************************************************************************************************
                                        NO SE OLVIDEN DE
                                        AGREGAR EL VERBOSE CON EL 
                                        METODO revVerb()
                                        MIREN COMO LO USE YO
                                        EN EL CREATEDB
****************************************************************************************************/

    @Override
    public T visitSqlProgram(GramaticaParser.SqlProgramContext ctx) {
        this.errores=new ArrayList();
        this.metaDataActual=new ArrayList();
        this.tablasActuales=new HashMap();
        this.registrosTablasActuales=new HashMap();
        this.metaDataLOCALTBnames=new ArrayList();
        this.metaDataGENERALDBnumTablas=new ArrayList();
        this.metaDataLOCALTBelementosNum=new ArrayList();
        this.metaDataLOCALTBcolumnas=new ArrayList();
        this.metaDataLOCALTBtipos=new ArrayList();
        for (ParseTree child : ctx.children) 
        {
            visit(child);
            try {
            WriteJSon();
            } catch (IOException ex) {
            //Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
            }
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
                try {
                    this.addToMDGeneral(nueva);
                    revVerb("Creada la entrada de la DB "+nombre+" a la metadata General");
                } catch (IOException ex) {
                    System.err.println("ERROR AL AGREGAR "+nombre+" A LA METADA GENERAL");
                    
                }
                
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
            try {
                this.renameFromMDGeneral(nombreViejo, nombreNuevo);
            } catch (IOException ex) {
                System.err.println("error al renombrar la db "+ctx.ID(0).getText() +" en la metada general");
                //Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            File vj = new File("src\\Auxiliares\\clases\\"+nombreViejo);
            File nv2 = new File("src\\Auxiliares\\clases\\"+nombreNuevo);
            vj.renameTo(nv2);
            
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
        
        /********************************
         * falta la parte de guardar la metada que estoy modificando
         * 
        ********************************/
        this.bUse=true;
        String nombre=ctx.ID().getText();
        //una cosa nueva
        File nuevo = new File(dirBase+nombre);

        revVerb("Revisando que la DB "+nombre+" exista para ser usada");
        if(!nuevo.isDirectory()){
            revVerb("La DB buscada no existe");
            this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no se puede usar la DB:"+nombre+" porque no existe");
            return(T)"error buscando la DB para uso";
        }

        revVerb("La DB buscada si existe");
        this.tablasActuales.clear();
        List<Table> infoMDLocal=null;
        try {
            infoMDLocal=(getInfoMDLocal(dirBase+nombre));
        } catch (IOException ex) {
            Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if( !(infoMDLocal!=null) || infoMDLocal.size()<1 ){
            
            this.dirActual=dirBase+nombre;
        }else{

            //ESTA PARTE CARGA LA NUEVA METADATA QUE VOY A USAR

    //        cargar las tablas existentes en la tabla

            if(infoMDLocal!=null){
                for (Table tabla : infoMDLocal) {

        //            agregar al mapa que le sirve a Angel
                    this.tablasActuales.put(tabla.getNombre(), tabla);

        //          AGREGAR AL ARRAY QUE LE SIRVE A CANTEO
                    this.metaDataActual.add(tabla);
                }
            }
            System.out.println(Arrays.toString(nuevo.list()));
    //        CARGAR EL CONTENIDO DE LOS REGISTROS DE LAS TABLAS 
            System.out.println("USANDO: "+nuevo.getAbsolutePath());
            this.dirActual=dirBase+nombre;
        }
        return(T)""; //To change body of generated methods, choose Tools | Templates.
    }
    
    private HashMap<String,ContenidoTabla> cargarRegistros(File folder){
        
        File[] tablas = folder.listFiles();
        HashMap<String,ContenidoTabla> dataTablas=new HashMap();
        final Type tipoListaHashMap = new TypeToken< List <HashMap<String,String> > >(){}.getType();
        final Gson gson = new Gson();
        List<HashMap<String,String>> datos=null;
        String desGsoneado="";
        
        
        
        for (File tabla : tablas) {
            
//            crear el nuevo objeto para almacenar todos los registros de la tabla actual
            ContenidoTabla readingTable=new ContenidoTabla();
            
            /*********************
            * cargar en un string
            * los registros de la tabla que voy leyendo
           **********************/
           try {
               desGsoneado=getDatos(folder);
           } catch (IOException ex) {
               Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
           }

           /*********************
            * convertir el string anterior
            * en un mapa si tiene al menos un registro
           **********************/
           if(desGsoneado.equals("[]")){
               
               
               
           }else{
               
               try{
               datos= gson.fromJson(desGsoneado, tipoListaHashMap);
               }catch(Exception e){}
               
               //agregar los registros al atributo de la lista
               readingTable.setLista(new ArrayList<HashMap>(datos));
               
               
           }
           
           
//           AGREGAR EL LA TABLA CON SUS DATOS CARGADOS AL HASHMAP CREADO
           
        }
        
        
        
        return null;
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
                try {
                //            ABRIR EL ARCHIVO DE LA METADATA GENERAL Y EDITARLO
                    eliminarFromMDGeneral(nombre);
                } catch (IOException ex) {
                    revVerb("No se puede eliminar la DB especificada");
                    Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
                }
                revVerb("DB->"+nombre+" eliminada de la metadata general");
                deleteDir(toDelete);
                deleteDir(new File("src\\Auxiliares\\clases\\"+nombre));
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
            List<DataBase> infoMDGeneral = new ArrayList<DataBase>();
            infoMDGeneral = this.getInfoMDGeneral();
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
        
        } catch (IOException ex) {
            Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*
        try {
            List<DataBase> infoMDGeneral = this.getInfoMDGeneral();
            if(infoMDGeneral!=null){
                for (DataBase base : infoMDGeneral) {
                    System.out.println(base.toString());
                }
            }else{
                System.out.println("NO HAY DB's PARA MOSTRAR");
            }
        } catch (IOException ex) {
            Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        
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
        ArrayList<String> columnNames = new ArrayList<String>();
        ArrayList<String> columnTypes = new ArrayList<String>();
        this.revVerb("revisar si hay una DB en uso para crear la tabla");
        try {
            crearArchivo(this.dirActual + "\\" + newTBName+".json");
        } catch (IOException ex) {
            Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
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
        NewlyCreatedTable = new Table();
        NewlyCreatedTable.setIDs(columnNames);
        NewlyCreatedTable.setTipos(columnTypes);
        NewlyCreatedTable.setNombre(newTBName);
        NewlyCreatedTable.llenarMapa();
        //si hay constraints se agregan a la tabla nueva
        //agrga tabla al map
        this.tablasActuales.put(newTBName, NewlyCreatedTable);
        //crear JSON vacio
        //String Jsoneado = Gsoneador.toJson(classMaker);
        //Jsoneado = Jsoneado +"\n";
        //try {
        //    Files.write(Paths.get(this.dirActual+"\\METADATA.json"), Jsoneado.getBytes(), StandardOpenOption.APPEND);
        //} catch (IOException ex) {
        //    Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
        //}
        //SE ASIGNA ESTA VARIBLE PARA QUE FUNCIONE BIEN LA BUSQUEDA
        hb=dirActual.substring(dirActual.indexOf("\\")+1);
        
        return (T)"";
    }

    //visitas para crear constraints
    @Override
    public Object visitConstraintPK(GramaticaParser.ConstraintPKContext ctx) 
    {
        return (T)"";
    }

    @Override
    public Object visitConstraintFK(GramaticaParser.ConstraintFKContext ctx) 
    {
        return (T)"";
    }

    @Override
    public Object visitConstraintCheck(GramaticaParser.ConstraintCheckContext ctx) 
    {
        return (T)"";
    }
    

    /******************
     *elimina una tabla ya existente 
     *
    *******************/
    @Override
    public Object visitEliminarTB(GramaticaParser.EliminarTBContext ctx) 
    {
        String tableName = ctx.ID().getText();
        //revisar que si exista la tabla
        if(this.tablasActuales.containsKey(tableName) == false)
        {
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la tabla " + tableName + " que no existe" );
            return (T)"error al eliminar tabla que no existe";
        }
        //eliminar la tabla
        Table aRemover = this.tablasActuales.get(tableName);
        this.tablasActuales.remove(tableName);
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
        hb=ctx.ID().getText();
        System.out.println("voy a alterar la TB:"+hb);
        revVerb("voy a alterar la TB:"+hb);
        return (T)visitChildren(ctx);
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
        
        int ind=this.metaDataLOCALTBnames.indexOf(hb);
        
        String data=(String)visit(ctx.columna());
        // retriebe table object from map and verify table 
        if(this.tablasActuales.containsKey(hb) == false)
        {
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la tabla " + hb + " que no existe" );
            return (T)"error al alterar tabla que no existe";
        }
        Table tabla = this.tablasActuales.get(hb);
        //separar el id pos 0 y el tipo pos 1 para agregarlos a las distintas listas
        String[] split = data.split(",");
        if(tabla.IDs.contains(split[0]))
        {
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "agrega la columna " + split[0] + " que ya existe" );
            return (T)"error al agregar columna que ya existe";
        }
        tabla.IDs.add(split[0]);
        tabla.Tipos.add(split[1]);
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
        /******************************
            FALTA HACER LAS VALIDACIONES CON EL ARCHIVO XML
        ******************************/
        if(this.tablasActuales.containsKey(hb) == false)
        {
            for(int i=0; i<this.tablasActuales.size(); i++)
            {
                System.out.println(this.tablasActuales.toString());
            }
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la tabla " + hb + " que no existe" );
            return (T)"error al renombrar bla bla tabla que no existe";
        }
        if(this.tablasActuales.containsKey(ctx.ID().getText()))
        {
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "renombra a " + ctx.ID().getText() + " que ya existe" );
            return (T)"error al renombrar tabla con un nombre existente";
        }
        
        Table tabla = this.tablasActuales.get(hb);
        this.tablasActuales.remove(hb);
        tabla.nombre = ctx.ID().getText();
        this.tablasActuales.put(ctx.ID().getText(), tabla);
        
        return (T)""; 
        
    }
     /******************
     *elimina una columna para una tabla dada 
     *
    *******************/
    @Override
    public T visitDropColumnTB(GramaticaParser.DropColumnTBContext ctx) 
    {
        String tableName = hb;
        String columnName = ctx.ID().getText();
        //verificar si la tabla existe
        if(this.tablasActuales.containsKey(tableName) == false)
        {
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la tabla " + tableName + " que no existe" );
            return (T)"error al alterar tabla que no existe";
        }
        //verificar si la columna existe
        Table tabla = this.tablasActuales.get(tableName);
        if(tabla.IDs.contains(columnName) == false)
        {
            this.errores.add("La linea: " + ctx.start.getLine() + ", (" + ctx.getText() +  ")" + "referencia a la columna " + columnName + " que ya existe" );
            return (T)"error al eliminar columna que existe";
        }
        //eliminar la columna
        int indice = tabla.IDs.indexOf(columnName);
        tabla.IDs.remove(indice);
        tabla.Tipos.remove(indice);
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
    public T visitInsert(GramaticaParser.InsertContext ctx) {
//        try {
//            WriteJSon();
//        } catch (IOException ex) {
//            Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
//        }
        if (this.bUse){
            for (Table table : tablasActuales.values()) {
                System.out.println(table.toString());
            }
            System.out.println(this.tablasActuales.toString());
            String NombreTabla = ctx.ID().getText(); 
            System.out.println(ctx.ID().getText());//Obtengo Nombre de tabla, Columnas y Values.
            List<TerminalNode> ids = ctx.insertColumns().ID();
            List<GramaticaParser.ValueContext> values = ctx.insertValues().value();
            if (ids.size() > values.size())                                                 
                this.errores.add("Existen mas columnas de destino que expresiones.");              
            else if ((ids.size() < values.size()) && (ids.size() > 0))
                this.errores.add("Existen mas expresiones que columnas de destino.");
            else {
                if (tablasActuales.keySet().contains(NombreTabla)){                 //HACE FALTA AUMENTAR CANTIDAD DE REGISTROS
                    Table TablaActual = tablasActuales.get(NombreTabla);
                    if (ids.size() == 0){                                            // El caso en el cual no se ingrese ninguna columna, solamente Values (Predefinido)
                        ArrayList<String> IDS = TablaActual.getIDs();
                        if (values.size() > IDS.size()){
                            this.errores.add("La cantidad de Valores a ingresar es mayor a la cantidad de columnas de la tabla: " + NombreTabla);
                        } else{
                            ArrayList<String> TiposDeTabla = TablaActual.getTipos();
                            for (GramaticaParser.ValueContext value : values) {
                                if (values.indexOf(value) < TiposDeTabla.size()-1){
//                                    if (TiposDeTabla.contains(value.)) 
                                    //INSERTAR EN TABLA
                                } else{
                                    //INSERTAR NULL                                    
                                }
                            }
                        }
                    }
                }else{
                    this.errores.add("No existe la relación: " + ctx.ID().getText());
                }
            }
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
    public T visitValue(GramaticaParser.ValueContext ctx) {
        
        return (T)visitChildren(ctx);
    }

    @Override
    public T visitEntero(GramaticaParser.EnteroContext ctx) {
        return(T) ctx.getText();
    }
    
    @Override
    public T visitDecimal(GramaticaParser.DecimalContext ctx) {
        return (T)ctx.getText(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visitFecha(GramaticaParser.FechaContext ctx) {
        System.out.println("FECHA VISITADA->"+ctx.getText());
        
       
//        OBTENER LOS NUMEROS DE LA FECHA INGRESADA
//        SE USA UNSIGNEDINT PORQUE LOS NUMEROS SON TODOS POSITIVOS 

        int year=Integer.parseInt(ctx.getText().substring(1, 5));
        int month=Integer.parseInt(ctx.getText().substring(6, 8));
        int day=Integer.parseInt(ctx.getText().substring(9,11));
        //REVISAR QUE EL MES SEA VALIDO
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
        
        return (T)ctx.getText(); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public T visitCharacter(GramaticaParser.CharacterContext ctx) {
        return (T)("CHAR" + String.valueOf(ctx.getText().length()));
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
    public void getMDglob(){
        /*
        String tN="DB's->[",tT="# Tablas->[";
        for (int i = 0; i < this.metaDataGENERALDBnames.size(); i++) {
            tN+=this.metaDataGENERALDBnames.get(i)+",";
            tT+=this.metaDataGENERALDBnumTablas.get(i)+",";
        }
        tN+="]";
        tT+="]";
        
        System.out.println(tN);
        System.out.println(tT);
        */
        //return tN;
    }

    public void getMDloc(){
        String tTabla="",tColumnas="",tTipos="";
        
        tTabla=this.metaDataLOCALTBnames.toString();
        tColumnas=this.metaDataLOCALTBcolumnas.toString();
        tTipos=this.metaDataLOCALTBtipos.toString();
        
        System.out.println("TABLAS->"+tTabla);
        System.out.println("COLUMNAS->"+tColumnas);
        System.out.println("TIPOS->"+tTipos);
    }
    
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
        Gson gson = new Gson();
        String json = gson.toJson(db);
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
    
    
    /*************************
     * METODO QUE CARGA LOS REGISTROS DE LAS TABLAS EN MEMORIA PARA SU 
     * USO POSTERIOR
     * 
    *************************/
    
    private void llenarRegistros(File archivo){
        
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
     */
    
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
            String Jsoneado = Gsoneador.toJson(TableActual);
            Jsoneado = Jsoneado +"\n";
            try {
                Files.write(Paths.get(this.dirActual+"\\METADATA.json"), Jsoneado.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException ex) {
                Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
