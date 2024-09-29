import procesadoFicheros.CreaLogs;
import Algoritmos.Greedy;
import procesadoFicheros.LectorTSP;
import Algoritmos.BusquedaLocal;
import java.util.Random;


import java.lang.reflect.Array;

public class Main
{
    public static void main(String[] args)
    {
        // Archivos de problemas .tsp
        String[] archivosTSP =
        {
                "a280.tsp",
                "pr144.tsp",
                "ch130.tsp",
                "d18512.tsp",
                "u1060.tsp"
        };

        // Ruta base para los archivos .tsp
        String rutaBase = "..\\METAHEURISTICAS\\PRACTICA_1\\";

        // Ruta para la carpeta de logs
        String rutaLogs = rutaBase + "log\\";

        // DNI base
        String seed = "20622008";
        int nIteraciones = 5;
        int k = 5;

        //Configuracion Busqueda local
        int num_iteraciones = 5000;
        double tam_entorno = 0.08;
        double dism_entorno = 0.1;

        // Bucle para cada archivo .tsp
        for (int i = 0; i < archivosTSP.length; i++)
        {
            String archivoTSP = archivosTSP[i];
            String rutaArchivo = rutaBase + archivoTSP;

            // Crear un objeto LectorTSP para leer las ciudades desde el archivo
            LectorTSP lector = new LectorTSP(rutaArchivo);
            double[][] distancias = lector.getDistancias();

            // Instancia de los algoritmos
            Greedy greedy = new Greedy();
            BusquedaLocal bLocal = new BusquedaLocal(num_iteraciones, tam_entorno, dism_entorno, lector);

            // Bucle para las iteraciones con diferentes semillas
            String currentSeed = seed;
            for (int iteracion = 0; iteracion < nIteraciones; iteracion++)
            {
                // Tiempo de inicio de la iteración
                long startTime = System.currentTimeMillis();

                // Desplazar el primer dígito al final
                currentSeed = currentSeed.substring(1) + currentSeed.charAt(0);

                // Convertir la cadena de DNI a número
                long dniNumerico = Long.parseLong(currentSeed);

                // Ejecutar el algoritmo de Busqueda Local
                bLocal.ejecutarBusquedaLocal(greedy.RealizarGreedy(k, dniNumerico, lector), new Random(dniNumerico));

                // Nombre del archivo de log basado en el archivo .tsp y la semilla ( creacion del txt incluida )
                String rutaLog = rutaLogs + "log_" + archivoTSP.replace(".tsp", "") + "_" + currentSeed + ".txt";
                CreaLogs log = new CreaLogs(rutaLog);

                // Generar mensaje de log y consola
                String mensaje = String.format("Iteración %d (Seed: %s) - Busqueda Local: %f", iteracion + 1, currentSeed, bLocal.getMejorCoste());
                logAndPrint(log, mensaje);

                // Tiempo de finalización de la iteración
                long endTime = System.currentTimeMillis();
                long duracion = endTime - startTime;

                // Mostrar y registrar el tiempo de ejecución
                logAndPrint(log, "Tiempo de ejecución: " + duracion + " milisegundos");

                // Cerrar el archivo de log para esta iteración
                log.cerrarLog();
            }
        }
    }

    /**
     * Función para escribir el mensaje en el log y en la consola.
     * @param log El objeto CreaLogs que se encargará de escribir en el archivo.
     * @param mensaje El mensaje que será mostrado en consola y escrito en el log.
     */
    public static void logAndPrint(CreaLogs log, String mensaje) {
        System.out.println(mensaje); // Mostrar en consola
        log.escribirLog(mensaje);    // Escribir en el archivo de log
    }
}
