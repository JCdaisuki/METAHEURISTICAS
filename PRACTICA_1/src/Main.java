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
        String rutaArchivo = "";
        
        // Crear un objeto LectorTSP para leer las ciudades desde el archivo
        LectorTSP lector = new LectorTSP(rutaArchivo);

        double[][] distancias = lector.getDistancias();

/*
        //Mostrar matriz de distancias
        System.out.println("Matriz de distancias:");
        for (int i = 0; i < distancias.length; i++)
        {
            for (int j = 0; j < distancias[i].length; j++)
            {
                System.out.printf("%.2f ", distancias[i][j]);
            }
            System.out.println(); // Nueva línea
        }
*/
        Greedy greedy = new Greedy();
        // DNI base
        String seed = "20622008";
        int nIteraciones = 5;

        // Realizar 5 iteraciones desplazando los números del DNI
        for (int iteracion = 0; iteracion < nIteraciones; iteracion++)
        {
            //Tiempo de inicio de la iteración
            long startTime = System.currentTimeMillis();

            // Desplazar el primer dígito al final
            seed = seed.substring(1) + seed.charAt(0);

            // Convertir la cadena de DNI a número
            long dniNumerico = Long.parseLong(seed);

            // Ejecutar el algoritmo Greedy con el nuevo DNI desplazado
            double distancia = greedy.RealizarGreedy(5, dniNumerico, lector);
            System.out.printf("Iteración %d (Seed: %s) - Greedy: %f\n", iteracion + 1, seed, distancia);

            //Tiempo de finalización de la iteración
            long endTime = System.currentTimeMillis();

            //Mostrar el tiempo de ejecución
            System.out.println("Tiempo de ejecución: " + (endTime - startTime) + " milisegundos");
        }
    }
}

