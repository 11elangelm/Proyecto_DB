/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladorsql.GUI;

import Auxiliares.MakeClass;
import compiladorsql.CompiladorSQL;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import compiladorsql.*;
import java.util.ArrayList;
import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 *
 * @author user
 */
public class Menu extends javax.swing.JFrame {
    
    private boolean bArbol=false,bVerb=false;
    
    /**
     * Creates new form Menu
     */
    private int findLastNonWordChar (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }
    DefaultStyledDocument doc = new DefaultStyledDocument() 
        {
            @Override
            public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offset);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;
                
                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) 
                    {
                        if (text.substring(wordL, wordR).matches("(\\W)*"+regex_reserved_1))
                            setCharacterAttributes(wordL, wordR - wordL, attrBlue, false);
                        else
                            setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                        wordL = wordR;
                    }
                    wordR++;
                }
            }
            @Override
            public void remove (int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offs);

                if (text.substring(before, after).matches("(\\W)*"+regex_reserved_1)) {
                    setCharacterAttributes(before, after - before, attrBlue, false);
                }
                else {
                    setCharacterAttributes(before, after - before, attrBlack, false);
                }
            }
    };
    private final StyleContext cont = StyleContext.getDefaultStyleContext();
    private final String regex_reserved_1="(((C|c)(R|r)(E|e)(A|a)(T|t)(E|e))|((D|d)(A|a)(T|t)(A|a)(B|b)(A|a)(S|s)(E|e))|((D|d)(A|a)(T|t)(A|a)(B|b)(A|a)(S|s)(E|e)(S|s))|((D|d)(R|r)(O|o)(P|p))|((U|u)(S|s)(E|e))|((S|s)(H|h)(O|o)(W|w))|((A|a)(L|l)(T|t)(E|e)(R|r))|((R|r)(E|e)(N|n)(A|a)(M|m)(E|e))|((T|t)(O|o))|((T|t)(A|a)(B|b)(L|l)(E|e))|((T|t)(A|a)(B|b)(L|l)(E|e)(S|s))|((C|c)(O|o)(N|n)(S|s)(T|t)(R|r)(A|a)(I|i)(N|n)(T|t))|((C|c)(O|o)(L|l)(U|u)(M|m)(N|n))|((C|c)(O|o)(L|l)(U|u)(M|m)(N|n)(S|s))|((F|f)(R|r)(O|o)(M|m))|((A|a)(D|d)(D|d))|((S|s)(E|e)(L|l)(E|e)(C|c)(T|t))|((W|w)(H|h)(E|e)(R|r)(E|e))|((I|i)(N|n)(T|t))|((D|d)(A|a)(T|t)(E|e))|((C|c)(H|h)(A|a)(R|r))|((F|f)(L|l)(O|o)(A|a)(T|t))|((P|p)(R|r)(I|i)(M|m)(A|a)(R|r)(Y|y))|((C|c)(H|h)(E|e)(C|c)(K|k))|((R|r)(E|e)(F|f)(E|e)(R|r)(E|e)(N|n)(C|c)(E|e)(S|s))|((F|f)(O|o)(R|r)(E|e)(I|i)(G|g)(N|n))|((K|k)(E|e)(Y|y))|((I|i)(N|n)(S|s)(E|e)(R|r)(T|t))|((I|i)(N|n)(T|t)(O|o))|((V|v)(A|a)(L|l)(U|u)(E|e)(S|s))|((O|o)(R|r)(D|d)(E|e)(R|r))|((S|s)(E|e)(T|t))|((U|u)(P|p)(D|d)(A|a)(T|t)(E|e))|((B|b)(Y|y))|((A|a)(S|s)(C|c))|((D|d)(E|e)(S|s)(C|c))|((A|a)(N|n)(D|d))|((O|o)(R|r))|((N|n)(O|o)(T|t))|((N|n)(U|u)(L|l)(L|l))|((D|d)(E|e)(L|l)(E|e)(T|t)(E|e)))";
//final String regex_reserved_1 = regex_reserved_12.replaceAll("'", "");
    private final AttributeSet attrBlue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLUE);
    private final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
    public Menu() {
        initComponents();
    }

    private String newline=System.getProperty("line.separator"),lastPath="";
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        panelEditor = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaInput = new javax.swing.JTextPane(doc);
        chekArbol = new javax.swing.JCheckBox();
        checkVerb = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        txtAreaErrores = new java.awt.TextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuAbrir = new javax.swing.JMenuItem();
        menuGuardar = new javax.swing.JMenuItem();
        menuEjecutar = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Compilador SQL");

        txtAreaInput.setFont(new java.awt.Font("DialogInput", 0, 18)); // NOI18N
        jScrollPane2.setViewportView(txtAreaInput);

        chekArbol.setText("Mostrar Arbol");
        chekArbol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chekArbolActionPerformed(evt);
            }
        });

        checkVerb.setText("Verbose");
        checkVerb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkVerbActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 896, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(chekArbol, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(checkVerb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(chekArbol)
                .addGap(27, 27, 27)
                .addComponent(checkVerb)
                .addContainerGap(430, Short.MAX_VALUE))
        );

        panelEditor.addTab("Editor", jPanel1);

        txtAreaErrores.setEditable(false);
        txtAreaErrores.setFont(new java.awt.Font("Microsoft Tai Le", 0, 17)); // NOI18N
        txtAreaErrores.setForeground(new java.awt.Color(255, 51, 51));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtAreaErrores, javax.swing.GroupLayout.DEFAULT_SIZE, 1012, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtAreaErrores, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
        );

        panelEditor.addTab("Errores", jPanel2);

        jMenu1.setText("File");

        menuAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        menuAbrir.setText("Abrir");
        menuAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAbrirActionPerformed(evt);
            }
        });
        jMenu1.add(menuAbrir);

        menuGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        menuGuardar.setText("Guardar");
        menuGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuGuardarActionPerformed(evt);
            }
        });
        jMenu1.add(menuGuardar);

        menuEjecutar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        menuEjecutar.setText("Ejecutar");
        menuEjecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEjecutarActionPerformed(evt);
            }
        });
        jMenu1.add(menuEjecutar);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelEditor)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelEditor)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAbrirActionPerformed
        int returnVal = this.jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = this.jFileChooser1.getSelectedFile();
            try {
                BufferedReader br=new BufferedReader(new FileReader(file.getPath()));
                String s="",s1="";
                this.txtAreaInput.setText("");
                while(((s=br.readLine())!=null)){
                    s1+=(s+newline);
                }
                if(br!=null){
                    br.close();
                }
                this.txtAreaInput.setText(s1);
                lastPath=file.getAbsolutePath();
                
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null,"El archivo que intenta abrir no existe","ERROR",JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null,"Hubo un error al leer/escribir el archivo "+file.getAbsolutePath(),"ERROR",JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }//GEN-LAST:event_menuAbrirActionPerformed

    private void menuGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuGuardarActionPerformed
        int returnVal = this.jFileChooser1.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String nuevoT= this.txtAreaInput.getText();
            BufferedWriter writer = null;
            File file = this.jFileChooser1.getSelectedFile();
            try
            {
            writer = new BufferedWriter( new FileWriter(file));
            writer.write(nuevoT);
            writer.close();
            System.out.println(file.getAbsolutePath()+" fue generado exitosamente");
            lastPath=file.getAbsolutePath();
            
            }catch(IOException ex){
                JOptionPane.showMessageDialog(null,"Problema al guardar el archivo "+file.getAbsolutePath(),"ERROR",JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_menuGuardarActionPerformed

    private void menuEjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEjecutarActionPerformed
        System.out.println("");
        CompiladorSQL comp=new CompiladorSQL(bVerb,bArbol);
        this.txtAreaErrores.setText("ESTE ES UN ERROR");
        comp.nombre(txtAreaInput.getText());
        
        ArrayList<String>err=comp.getErr();
//        si hay errores detectados durante el visit se muestra este mensaje
        if(err.size()>0){
            JOptionPane.showMessageDialog(null,"El programa tiene errores semánticos, vaya a la tab ERRORES para mayor info.","Error",JOptionPane.ERROR_MESSAGE);
            String texto="";
            for (String error : err) {
                texto=error+newline;
            }
            this.txtAreaErrores.setText(texto);
        }
    }//GEN-LAST:event_menuEjecutarActionPerformed
    /*
        este metodo sirve para saber si el arbol sintactico debe ser desplegado
    */
    private void chekArbolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chekArbolActionPerformed
        this.bArbol=!this.bArbol;
    }//GEN-LAST:event_chekArbolActionPerformed

    /*
    este metodo sirve para saber si la opcion verbose va a ser activada 
    */
    private void checkVerbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkVerbActionPerformed
        this.bVerb=!this.bVerb;
    }//GEN-LAST:event_checkVerbActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkVerb;
    private javax.swing.JCheckBox chekArbol;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenuItem menuAbrir;
    private javax.swing.JMenuItem menuEjecutar;
    private javax.swing.JMenuItem menuGuardar;
    private javax.swing.JTabbedPane panelEditor;
    private java.awt.TextArea txtAreaErrores;
    private javax.swing.JTextPane txtAreaInput;
    // End of variables declaration//GEN-END:variables
}
