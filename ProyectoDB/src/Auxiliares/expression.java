/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Auxiliares;

import compiladorsql.GramaticaParser;
import java.util.HashMap;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 *
 * @author juankboix1309
 */
public class expression 
{
    public ParseTree Barbol;
    public GramaticaParser parsero;
    public Table tablon = new Table();
    factor segundo;
    factor primero;
    public expression(ParseTree exp, GramaticaParser parsero, Table tablon)
    {
        this.tablon = tablon;
        this.Barbol = exp;
        this.parsero = parsero;
    }
    public String NodeName(String PTFormat)
    {
        String[] Rwords = PTFormat.split(" ");
        String Rfirst = Rwords[0];
        String RNodeLabel = Rfirst;
        try
        {
        if(Rfirst.charAt(0) =='(' && Rfirst.charAt(1) != ' ')
        {
            RNodeLabel = RNodeLabel + ")";
        }
        }
        catch(StringIndexOutOfBoundsException e)
                {
                    
                }
        return RNodeLabel;
    }
    
    public boolean checkValue(HashMap<String, String> registro)//hashmap
    {
        //primer nivel
        return FinalExpression(parsero, Barbol, registro);
    }
    public boolean FinalExpression(GramaticaParser parsero, ParseTree Barbol, HashMap<String, String> registro)
    {
        if(Barbol.getChildCount() == 1)
        {
            return FullExpression(parsero, Barbol.getChild(0), registro);
        }
        else
        {
            if(FullExpression(parsero, Barbol.getChild(1), registro) == true)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }
    
    public boolean FullExpression(GramaticaParser parsero, ParseTree Barbol, HashMap<String, String> registro)
    {
        return true;
    }
    public boolean GeneralExpression(GramaticaParser parsero, ParseTree Barbol, HashMap<String, String> registro)
    {
        if(NodeName(Barbol.getChild(0).toStringTree(parsero)).equals("(booleanExpression)"))
        {
            return BooleanExpression(parsero, Barbol.getChild(0), registro);
        }
        if(NodeName(Barbol.getChild(0).toStringTree(parsero)).equals("(normalExpression)"))
        {
            return NormalExpression(parsero, Barbol.getChild(0), registro);
        }
        return true;
    }
    public boolean BooleanExpression(GramaticaParser parsero, ParseTree Barbol, HashMap<String, String> registro)
    {
        if(NodeName(Barbol.getChild(3).toStringTree(parsero)).equals("(andExp)"))
        {
            return GeneralExpression(parsero, Barbol.getChild(1), registro) && GeneralExpression(parsero, Barbol.getChild(5), registro);
        }
        if(NodeName(Barbol.getChild(3).toStringTree(parsero)).equals("(orExp)"))
        {
            return GeneralExpression(parsero, Barbol.getChild(1), registro) || GeneralExpression(parsero, Barbol.getChild(5), registro);
        }
        return true;
    }
    public boolean NormalExpression(GramaticaParser parsero, ParseTree Barbol, HashMap<String, String> registro)
    {
        String type1 = "NULO";
        String type2 = "NULO";
        //revisa el primer valor
        if(Barbol.getChild(0).getChildCount() == 1)
        {
            //literal
            String valueType = NodeName(Barbol.getChild(0).getChild(0).getChild(0).toStringTree(parsero));
            String value = NodeName(Barbol.getChild(0).getChild(0).getChild(0).getChild(0).toStringTree(parsero));
            primero = new factor(value);
            if(valueType.equals("(entero)"))
            {
                type1 = "int";
            }
            else if(valueType.equals("(decimal)"))
            {
                type1 = "float";
            }
            else if(valueType.equals("(fecha)"))
            {
                type1 = "date";
            }
            else if(valueType.equals("(caracter)"))
            {
                type1 = "char";
            }
            else if(valueType.equals("(nulo)"))
            {
                type1 = "null";
            }
        }
        else
        {
            //location
            //tabla + _ + columna
            String TName = NodeName(Barbol.getChild(0).getChild(0).toStringTree(parsero));
            TName = TName.substring(1, TName.length()-1);//sin parentesis
            String CName = NodeName(Barbol.getChild(0).getChild(2).toStringTree(parsero));
            CName = CName.substring(1, CName.length()-1);//sin parentesis
            String FinalName = TName + "_" + CName;
            type1 = tablon.columnas.get(FinalName);
        }
        //revisa el segundo valor
        if(Barbol.getChild(2).getChildCount() == 1)
        {
            //literal
            String valueType = NodeName(Barbol.getChild(2).getChild(0).getChild(0).toStringTree(parsero));
            String value = NodeName(Barbol.getChild(2).getChild(0).getChild(0).getChild(0).toStringTree(parsero));
            segundo = new factor(value);
            if(valueType.equals("(entero)"))
            {
                type2 = "int";
            }
            else if(valueType.equals("(decimal)"))
            {
                type2 = "float";
            }
            else if(valueType.equals("(fecha)"))
            {
                type2 = "date";
            }
            else if(valueType.equals("(caracter)"))
            {
                type2 = "char";
            }
            else if(valueType.equals("(nulo)"))
            {
                type2 = "null";
            }
        }
        else
        {
            //location
            //tabla + _ + columna
            String TName = NodeName(Barbol.getChild(2).getChild(0).toStringTree(parsero));
            TName = TName.substring(1, TName.length()-1);//sin parentesis
            String CName = NodeName(Barbol.getChild(2).getChild(2).toStringTree(parsero));
            CName = CName.substring(1, CName.length()-1);//sin parentesis
            String FinalName = TName + "_" + CName;
            type2 = tablon.columnas.get(FinalName);
            if(type2.equals("int"))
            {
            }
            else if(type2.equals("float"))
            {
            }
            else if(type2.equals("null"))
            {
            }
            else if(type2.equals("date"))
            {
            }
            else
            {
                type2="char";
            }
        }
        //revisa que el tipo sea el mismo
        if(type1.equals(type2)==false)
        {
            System.out.println("Los tipos no son iguales");
        }
        //revisa el operando
        
        if(NodeName(Barbol.getChild(1).getChild(0).toStringTree(parsero)).equals("="))
        {
            if(type1.equals("int"))
            {
                if(primero.getInt() == segundo.getInt())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(type1.equals("float"))
            {
                if(primero.getFloat() == segundo.getFloat())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(type1.equals("null"))
            {
                return true;
            }
            else if(type1.equals("date"))
            {
                if(primero.getDate() == segundo.getDate())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(type1.equals("char"))
            {
                if(primero.getChar().equals(segundo.getChar()))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        if(NodeName(Barbol.getChild(1).getChild(0).toStringTree(parsero)).equals("<>"))
        {
            if(type1.equals("int"))
            {
                if(primero.getInt() != segundo.getInt())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(type1.equals("float"))
            {
                if(primero.getFloat() != segundo.getFloat())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(type1.equals("null"))
            {
                return false;
            }
            else if(type1.equals("date"))
            {
                if(primero.getDate() != segundo.getDate())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(type1.equals("char"))
            {
                if(primero.getChar().equals(segundo.getChar()))
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
        }
        
        if(NodeName(Barbol.getChild(1).getChild(0).toStringTree(parsero)).equals("<"))
        {
            if(type1.equals("int"))
            {
                if(primero.getInt() < segundo.getInt())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(type1.equals("float"))
            {
                if(primero.getFloat() < segundo.getFloat())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(type1.equals("null"))
            {
                return false;
            }
            else if(type1.equals("date"))
            {
                if(primero.getDate().compareTo(segundo.getDate()) == -1)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(type1.equals("char"))
            {
                System.out.println("Error al comparar char con < o >");
            }
        }
        if(NodeName(Barbol.getChild(1).getChild(0).toStringTree(parsero)).equals(">"))
        {
            if(type1.equals("int"))
            {
                if(primero.getInt() > segundo.getInt())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(type1.equals("float"))
            {
                if(primero.getFloat() > segundo.getFloat())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(type1.equals("null"))
            {
                return false;
            }
            else if(type1.equals("date"))
            {
                if(primero.getDate().compareTo(segundo.getDate())== 1)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(type1.equals("char"))
            {
                System.out.println("error al comparar chars con < o >");
            }
        }
        if(NodeName(Barbol.getChild(1).getChild(0).toStringTree(parsero)).equals("<="))
        {
            if(type1.equals("int"))
            {
                if(primero.getInt() <= segundo.getInt())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(type1.equals("float"))
            {
                if(primero.getFloat() <= segundo.getFloat())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(type1.equals("null"))
            {
                return false;
            }
            else if(type1.equals("date"))
            {
                if(primero.getDate().compareTo(segundo.getDate()) == 1)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            else if(type1.equals("char"))
            {
                System.out.println("error al comparar chars con <= o >=");
            }
        }
        if(NodeName(Barbol.getChild(1).getChild(0).toStringTree(parsero)).equals(">="))
        {
            if(type1.equals("int"))
            {
                if(primero.getInt() >= segundo.getInt())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(type1.equals("float"))
            {
                if(primero.getFloat() >= segundo.getFloat())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(type1.equals("null"))
            {
                return false;
            }
            else if(type1.equals("date"))
            {
                if(primero.getDate().compareTo(segundo.getDate())==-1)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            else if(type1.equals("char"))
            {
                System.out.println("Error al comparar char con <= o >=");
            }
        }
        //devuelve el booleano
        ////malditaaaa
        
        //perra
        
        return true;
    }
    
    
}
