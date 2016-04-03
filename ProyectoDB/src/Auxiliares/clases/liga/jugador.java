package Auxiliares.clases;

/**
 *
 * @author user
 */
public class jugador{
    
    public String nombre;
    public int edad;
    public String posicion;
    public float sueldo;
    public jugador()
    {
    }
    public jugador( String nombre, int edad, String posicion, float sueldo)
    {
        this.nombre = nombre;
        this.edad = edad;
        this.posicion = posicion;
        this.sueldo = sueldo;
    }
}
