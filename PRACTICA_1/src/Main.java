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
        //Tiempo de inicio del programa
        long startTime = System.currentTimeMillis();

        // Define la ruta del archivo
        String rutaArchivo = "C:\\Githubs de clase\\Meta\\METAHEURISTICAS\\PRACTICA_1\\u1060.tsp";
        
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
        double distancia = greedy.RealizarGreedy(5,20622008,lector);
        System.out.printf("Greedy: %f\n", distancia);

        //Tiempo de finalización del programa
        long endTime = System.currentTimeMillis();

        //Mostrar el tiempo de ejecución del programa
        System.out.println("Tiempo de ejecución: " + (endTime - startTime) + " milisegundos");
    }
}

