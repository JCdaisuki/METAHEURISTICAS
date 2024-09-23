import java.util.ArrayList;
import java.util.List;
import procesadoFicheros.*;
import Algoritmos.Greedy;
/**
 * Pareja 11
 * @author Juan Carlos Gonzalez Martinez
 * @author Jose Antonio Mayoral Luna
 */

public class Main
{
    public static void main(String[] args)
    {
        // Define la ruta del archivo
        String rutaArchivo = "C:\\Githubs de clase\\Meta\\METAHEURISTICAS\\PRACTICA_1\\ch130.tsp";
        
        // Crear un objeto LectorTSP para leer las ciudades desde el archivo
       LectorTSP lector = new LectorTSP(rutaArchivo);

        double[][] distancias = lector.getDistancias();
        int numCiudades = distancias.length;

        System.out.println("Matriz de distancias:");
        for (int i = 0; i < distancias.length; i++)
        {
            for (int j = 0; j < distancias[i].length; j++)
            {
                System.out.printf("%.2f ", distancias[i][j]);
            }
            System.out.println(); // Nueva línea
        }
        Greedy greedy = new Greedy();
        System.out.printf("Greedy: " , greedy.realizagreedy(5,20622008,lector) );
    }
}

