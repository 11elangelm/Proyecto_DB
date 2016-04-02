/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladorsql;

import Auxiliares.DataBase;
import Auxiliares.MakeClass;
import Auxiliares.clases.equipo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 *
 * @author user
 */
public class NuestroVisitor<T> extends GramaticaBaseVisitor{

    private String dirBase="Bases de Datos\\",dirActual="";
    private ArrayList<String> errores,
            metaDataGENERALDBnames,
            metaDataLOCALTBnames;
    
    private ArrayList metaDataGENERALDBnumTablas,
            metaDataLOCALTBelementosNum;
    
    private ArrayList<ArrayList<String>> metaDataLOCALTBcolumnas,
            metaDataLOCALTBtipos;
    
    private boolean verbose=false,
            bUse=false;
    private String hb="";
    
    
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
        this.metaDataGENERALDBnames=new ArrayList();
        this.metaDataLOCALTBnames=new ArrayList();
        this.metaDataGENERALDBnumTablas=new ArrayList();
        this.metaDataLOCALTBelementosNum=new ArrayList();
        this.metaDataLOCALTBcolumnas=new ArrayList();
        this.metaDataLOCALTBtipos=new ArrayList();
        for (ParseTree child : ctx.children) {
            visit(child);
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
                    this.metaDataGENERALDBnames.add(ctx.ID().getText());
                    this.metaDataGENERALDBnumTablas.add(0);
                    
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
                    this.crearArchivo(nombre,dirBase+nombre+"\\METADATA.json");
                    revVerb("Creado el archivo metadata de la DB "+nombre+" ");
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
        
        this.bUse=true;
        String nombre=ctx.ID().getText();
        File old = new File(dirBase+nombre);
        
        revVerb("Revisando que la DB "+nombre+" exista para ser usada");
        if(!old.isDirectory()){
            revVerb("La DB buscada no existe");
            this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no se puede usar la DB:"+nombre+" porque no existe");
            return(T)"error buscando la DB para uso";
        }
        revVerb("La DB buscada si existe");
        System.out.println("USANDO: "+old.getAbsolutePath());
        this.dirActual=dirBase+nombre;
        
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
                try {
                //            ABRIR EL ARCHIVO DE LA METADATA GENERAL Y EDITARLO
                    this.eliminarFromMDGeneral(nombre);
                } catch (IOException ex) {
                    revVerb("No se puede eliminar la DB especificada");
                    Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
                }
                revVerb("DB->"+nombre+" eliminada de la metadata general");
                deleteDir(toDelete);
                revVerb("DB->"+nombre+" eliminada");
                
            }
            
        }
        
        return (T)"";
    }

    /******************
     * IMPRIME EN LA CONSOLA LAS DB's EXISTENTES,
     * SI NO HAY NIGUNA ENTONCES MUESTRA UN MENSAJE DICIENDOLO
     *
    *******************/
    @Override
    public T visitMostrarDB(GramaticaParser.MostrarDBContext ctx) {
        
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
        
        return (T)"";
    }

    @Override
    public T visitCrearTB(GramaticaParser.CrearTBContext ctx) {
        //Crear variables para usar en la instancia del classmaker
        //nombre
        String newTBName = ctx.ID().getText();
        ArrayList<String> columnNames = new ArrayList<String>();
        ArrayList<String> columnTypes = new ArrayList<String>();
        this.revVerb("revisar si hay una DB en uso para crear la tabla");
        
        if(!this.bUse){
            revVerb("no hay DB seleccionada");
            this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+"), no pueder crear una tabla porque no hay DB seleccionada");
            return (T)"error porque no hay DB elegida";
            
        }
        /*****************************
         * FALTA CRAR EL ARRAYLIST CON LAS CONSTRAINTS
        *****************************/
        
        
        /****************************
            CREADA LA METADATA DE LA TABLA PARA LA DB ACTUAL
            * FALTA CREAR LOS ARCHIVOS PARA ESTA TABLA EN ESPECIFICO
            * FALTA VALIDAR QUE LA TABLA CREADA NO HAYA ESTE SIENDO DUPLICADA
        *****************************/
        
        
        // AGREGO EL NOMBRE DE LA TABLA A LA METADATA DE LA BASE DE DATOS EN USO
        //this.metaDataLOCALTBnames.add(ctx.ID().getText());
        revVerb("agregar la tabla a la metadata");
//        BUSCAR EL INDICE DE LA TABLA RECIEN AGREGADA
        //int ind=this.metaDataLOCALTBnames.indexOf(ctx.ID().getText());
        revVerb("buscar la tabla recien agregada");
        revVerb("llenar las tablas con la data correspondiente");
//  LLENAR LA INFO DE LAS COLUMNAS EN LA METADATA LOCAL
        for (int i = 0; i < ctx.columna().size(); i++) 
        {
            
        //obtener el id y el tipo de la columna para meterlo a la metaDATA local
            String data=(String)visit(ctx.columna(i));
            
            //separar el id y el tipo para agregarlos a las distintas listas
            String[] split = data.split(",");
            
//            AGREGO LAS NUEVAS POSICIONES QUE VOY A NECESITAR MAS ADELANTE
            //this.metaDataLOCALTBcolumnas.add(new ArrayList());//.get(i).add(split[0]);
            //this.metaDataLOCALTBtipos.add(new ArrayList());//.add(split[1]);
            
//            AGREGO EL EL NOMBRE Y TIPO DE LA COLUMNA EN LA POSICION CORRESPONDIENTE A LA TABLA
            //this.metaDataLOCALTBcolumnas.get(ind).add(split[0]);
            columnNames.add(split[0]);
            columnTypes.add(split[1]);
            //this.metaDataLOCALTBtipos.get(ind).add(split[1]);
            
        }
        
        
        
        //crear clase para instanciar objetos en el insert
        MakeClass classMaker = new MakeClass(newTBName, columnNames, columnTypes);
        try {
            classMaker.crear();
        } catch (IOException ex) {
            Logger.getLogger(NuestroVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        revVerb("actualizar metadata global");
        //SE ASIGNA ESTA VARIBLE PARA QUE FUNCIONE BIEN LA BUSQUEDA
        hb=dirActual.substring(dirActual.indexOf("\\")+1);

        /*int indi=this.metaDataGENERALDBnames.indexOf( hb );

        //aumentar el contador de tablas en el metadata
        int cont=(int)this.metaDataGENERALDBnumTablas.get( indi );
        cont++;
        this.metaDataGENERALDBnumTablas.set(indi, cont);
        */
        
        return (T)"";
    }

    @Override
    public T visitAlterarTB(GramaticaParser.AlterarTBContext ctx) {
        hb=ctx.ID().getText();
        revVerb("voy a alterar la DB");
        return (T)visitChildren(ctx);
    }

    @Override
    public T visitAddColumnTB(GramaticaParser.AddColumnTBContext ctx) {
        
        /******************************
            FALTA HACER LAS VALIDACIONES CON EL ARCHIVO XML
        ******************************/
        
        this.revVerb("revisar si la columna que voy a insertar ya existe");
        
        int ind=this.metaDataLOCALTBnames.indexOf(hb);
        
        String data=(String)visit(ctx.columna());

        //separar el id y el tipo para agregarlos a las distintas listas
        String[] split = data.split(",");

//      AGREGO LAS NUEVAS POSICIONES QUE VOY A NECESITAR MAS ADELANTE
        
        this.metaDataLOCALTBcolumnas.add(new ArrayList());//.get(i).add(split[0]);
        this.metaDataLOCALTBtipos.add(new ArrayList());//.add(split[1]);

//            AGREGO EL EL NOMBRE Y TIPO DE LA COLUMNA EN LA POSICION CORRESPONDIENTE A LA TABLA 
//        REVISO QUE NO EXISTA UNA COLUMNA CON EL MISMO NOMBRE
        
        if(!this.metaDataLOCALTBcolumnas.get(ind).contains(split[0])){
            
//            agregar a la tabla correspondiente y aumetar el contadar de tablas en metadata general
            this.metaDataLOCALTBcolumnas.get(ind).add(split[0]);
            this.metaDataLOCALTBtipos.get(ind).add(split[1]);
            

            revVerb("la columna fue insertada con exito");
            
        }else{
            this.errores.add("La linea:"+ctx.start.getLine()+", ("+ctx.getText()+") trata de insertar la columna ("+ctx.columna().getText()+") que ya existe");
            return (T)"error al tratar de insertar columna repetida";
        }
        
        
        return (T)"";
    }

    @Override
    public T visitRenameTB(GramaticaParser.RenameTBContext ctx) {
        revVerb("voy a revisar si la tabla que voy a renombrar si existe");
        /******************************
            FALTA HACER LAS VALIDACIONES CON EL ARCHIVO XML
        ******************************/
        int ind=this.metaDataLOCALTBnames.indexOf(hb);
        if(ind==-1){
            this.errores.add("La linea:"+ctx.getParent().start.getLine()+", ("+ctx.getParent().getText()+") intenta renombra la tabla ("+hb+") que no existe");
            return (T)"error al buscar la tabla para renombrarla";
        }
        if(this.metaDataLOCALTBnames.contains(ctx.ID().getText())){
            this.errores.add("La linea:"+ctx.getParent().start.getLine()+", ("+ctx.getParent().getText()+") intenta renombra la tabla ("+hb+") con el nombre ("+ctx.ID().getText()+") ya existe");
            return (T)"error al renombrar la tabla porque ya existe";
        }
        
        this.metaDataLOCALTBnames.set(ind, ctx.ID().getText());
        revVerb("tabla Renombrada exitosamente");
        return (T)"";
    }
    
    @Override
    public T visitDropColumnTB(GramaticaParser.DropColumnTBContext ctx) {
        return (T)"";
    }
    
    @Override
    public T visitColumna(GramaticaParser.ColumnaContext ctx) {
        String td=ctx.ID().getText()+","+visit(ctx.type());
        return (T)td;
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
        return (T)ctx.getText();
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
        
        String tN="DB's->[",tT="# Tablas->[";
        for (int i = 0; i < this.metaDataGENERALDBnames.size(); i++) {
            tN+=this.metaDataGENERALDBnames.get(i)+",";
            tT+=this.metaDataGENERALDBnumTablas.get(i)+",";
        }
        tN+="]";
        tT+="]";
        
        System.out.println(tN);
        System.out.println(tT);
        
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
    
    private void crearArchivo(String nombre,String dir) throws IOException{
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
        List<DataBase> bases=null;
        try{
            bases= gson.fromJson(arc, tipoListaDB);
        }catch(Exception e){
            return null;
        }
        
        return bases;
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
}
