/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Auxiliares;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author juankboix1309
 */
public class TableMaker 
{
    public TableMaker(){}
    
    public JTable ShowDatabases(List<DataBase> databases)
    {
        JTable aret = new JTable();
        
        
        Object rowNames[] = {"Nombre","No. Tablas"}; 
        Object rowData[][] = new Object[databases.size()][2];
        for(int i=0;i<databases.size(); i++)
        {
            rowData[i][0] = databases.get(i).getName();
            rowData[i][1] = databases.get(i).getNumTablas();        
        }
        aret = new JTable(rowData, rowNames);
        
        
        return aret;
    }
    public JTable ShowTables(List<Table> tablas)
    {
        JTable aret = new JTable();
        Object rowNames[] = {"Nombre", "registros"};
        Object rowData[][] = new Object[tablas.size()][2];
        for(int i=0; i<tablas.size(); i++)
        {
            rowData[i][0] = tablas.get(i).nombre;
            rowData[i][1] = tablas.get(i).getCantRegistros();
        }
        aret = new JTable(rowData, rowNames);
        return aret;
    }
    
    public JTable ShowColumnsFrom(Table tabla)
    {
        JTable aret = new JTable();
        Object rowNames[] = {"Nombre","Tipo"};
        Object[][] rowData = new Object[tabla.IDs.size()][2];
        for(int i=0; i<tabla.IDs.size(); i++)
        {
            rowData[i][0] = tabla.IDs.get(i);
            rowData[i][1] = tabla.Tipos.get(i);
        }
        aret = new JTable(rowData, rowNames);
        return aret;
    }
}
