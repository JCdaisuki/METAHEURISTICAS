import Algoritmos.AlgTabu_Clase03_Grupo04;
import ProcesadoFicheros.LectorTSP;
import ProcesadoFicheros.CreaLogs;
import Algoritmos.AlgGreedy_Clase03_Grupo04;
import Algoritmos.AlgBusquedaLocal_Clase03_Grupo04;
import java.util.Random;

public class Main{

    //Lista de nombres de los archivos a leer
    static String[] archivosTSP =
    {
        "a280.tsp",
        "ch130.tsp",
        "d18512.tsp",
        "pr144.tsp",
        "u1060.tsp"
    };

    public static void main(String[] args)
    {
        // Ruta base para los archivos .tsp
        String rutaBase = "..\\PRACTICA_1\\";

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

        //Configuracion Tabu
        int maxite = 1000;
        double empeoramientoPermitido = 0.08;

        // Bucle para cada archivo .tsp
        for (int i = 0; i < archivosTSP.length; i++)
        {
            String archivoTSP = archivosTSP[i];
            String rutaArchivo = rutaBase + archivoTSP;

            // Crear un objeto LectorTSP para leer las ciudades desde el archivo
            LectorTSP lector = new LectorTSP(rutaArchivo);
            double[][] distancias = lector.getDistancias();

            // Instancia de los algoritmos
            AlgGreedy_Clase03_Grupo04 greedy = new AlgGreedy_Clase03_Grupo04();
            AlgBusquedaLocal_Clase03_Grupo04 bLocal = new AlgBusquedaLocal_Clase03_Grupo04(num_iteraciones, tam_entorno, dism_entorno, lector);
            AlgTabu_Clase03_Grupo04 tabu = new AlgTabu_Clase03_Grupo04(lector,5000,0.5,0.08,0.1,10,10,50);

            // Bucle para las iteraciones con diferentes semillas
            String currentSeed = seed;
            for (int ite = 0; ite < nIteraciones; ite++)
            {
                // Desplazar el primer dígito al final
                currentSeed = currentSeed.substring(1) + currentSeed.charAt(0);

                // Convertir la cadena de DNI a número
                long dniNumerico = Long.parseLong(currentSeed);

                // Nombre del archivo de log basado en el archivo .tsp y la semilla ( creacion del txt incluida )
                String rutaLog = rutaLogs + "log_" + archivoTSP.replace(".tsp", "") + "_" + currentSeed + ".txt";
                CreaLogs log = new CreaLogs(rutaLog);

            //Para ejecutar cada algoritmo, emplear la llamada comentada a continuación:

                //Ejecución Greedy
                //AlgGreedy(greedy, k, dniNumerico, lector, ite, i, currentSeed, log);

                //Ejecución Búsqueda Local
                //AlgBusqLocal(bLocal, greedy, k, dniNumerico, lector, ite, i, currentSeed, log);

                //Ejecución Tabú
                AlgTabu(tabu, greedy, k, dniNumerico, lector, ite, i, currentSeed, log);

                // Cerrar el archivo de log para esta iteración
                log.cerrarLog();

            //Los resultados de cada ejecución se almacenarán en los archivos log de la carpeta log
            }
        }
    }

    /**
     * Función para escribir el mensaje en el log y en la consola.
     * @param log El objeto CreaLogs que se encargará de escribir en el archivo.
     * @param mensaje El mensaje que será mostrado en consola y escrito en el log.
     */
    public static void logAndPrint(CreaLogs log, String mensaje, String mensaje2) {
        System.out.println(mensaje); // Mostrar en consola
        log.escribirLog(mensaje);    // Escribir en el archivo de log
        log.escribirLog(mensaje2);
        log.escribirMejoresLocales();
    }

    static void AlgGreedy(AlgGreedy_Clase03_Grupo04 greedy, int k, long dniNumerico, LectorTSP lector, int ite, int i, String currentSeed, CreaLogs log)
    {
        long startTime = System.currentTimeMillis();

        greedy.RealizarGreedy(k, dniNumerico, lector);

        String mensaje = String.format("Ejecucion %d %s(Seed: %s) - Greedy: %f", ite + 1, archivosTSP[i], currentSeed, greedy.GetMejorCoste());
        long endTime = System.currentTimeMillis();
        long duracion = endTime - startTime;
        logAndPrint(log, mensaje,"Tiempo de ejecución: " + duracion + " milisegundos");
    }

    static private void AlgBusqLocal(AlgBusquedaLocal_Clase03_Grupo04 bLocal, AlgGreedy_Clase03_Grupo04 greedy, int k, long dniNumerico, LectorTSP lector, int ite, int i, String currentSeed, CreaLogs log)
    {
        long startTime = System.currentTimeMillis();

        bLocal.ejecutarBusquedaLocal(greedy.RealizarGreedy(k, dniNumerico, lector), new Random(dniNumerico));

        // Generar mensaje de log y consola de blocal
        String mensaje = String.format("Ejecucion %d %s(Seed: %s) - Busqueda Local: %f", ite + 1, archivosTSP[i], currentSeed, bLocal.getMejorCoste());
        long endTime = System.currentTimeMillis();
        long duracion = endTime - startTime;
        logAndPrint(log, mensaje,"Tiempo de ejecución: " + duracion + " milisegundos");
    }

    static private void AlgTabu(AlgTabu_Clase03_Grupo04 tabu, AlgGreedy_Clase03_Grupo04 greedy, int k, long dniNumerico, LectorTSP lector, int ite, int i, String currentSeed, CreaLogs log)
    {
        long startTime = System.currentTimeMillis();

        tabu.SetSemilla(dniNumerico);
        tabu.SetLog(log);

        tabu.ejecutarTabu(greedy.RealizarGreedy(k,dniNumerico,lector));

        String mensaje2 = String.format("Ejecucion %d %s(Seed: %s) - Busqueda Tabu: %f", ite + 1, archivosTSP[i], currentSeed, tabu.GetMejorSolucion());

        // Mostrar y registrar el tiempo de ejecución
        long endTime = System.currentTimeMillis();
        long duracion = endTime - startTime;
        logAndPrint(log, mensaje2,"Tiempo de ejecución: " + duracion + " milisegundos");
    }
}
