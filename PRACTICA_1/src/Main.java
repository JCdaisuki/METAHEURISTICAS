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
        // DNI base
        String dni = "20622008";

        // Realizar 5 iteraciones desplazando los números del DNI
        for (int iteracion = 0; iteracion < 5; iteracion++) {
            // Desplazar el primer dígito al final
            dni = dni.substring(1) + dni.charAt(0);

            // Convertir la cadena de DNI a número
            long dniNumerico = Long.parseLong(dni);

            // Ejecutar el algoritmo Greedy con el nuevo DNI desplazado
            double distancia = greedy.RealizarGreedy(5, dniNumerico, lector);
            System.out.printf("Iteración %d (DNI: %s) - Greedy: %f\n", iteracion + 1, dni, distancia);
        }

        //Tiempo de finalización del programa
        long endTime = System.currentTimeMillis();

        //Mostrar el tiempo de ejecución del programa
        System.out.println("Tiempo de ejecución: " + (endTime - startTime) + " milisegundos");
    }
}

