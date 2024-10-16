import Algoritmos.AlgTabu_Clase03_Grupo04;
import ProcesadoFicheros.LectorTSP;
import ProcesadoFicheros.CreaLogs;
import Algoritmos.AlgGreedy_Clase03_Grupo04;
import Algoritmos.AlgBusquedaLocal_Clase03_Grupo04;
import java.util.Random;

public class Main{
                                //TODO HACER QUE EN LOS LOGS DE LA BUSQUEDA LOCAL MUESTRE EL RECORRIDO LOCAL

    public static void main(String[] args)
    {
        // Archivos de problemas .tsp
        String[] archivosTSP =
        {
                "a280.tsp",
                "ch130.tsp",
                "d18512.tsp",
                "pr144.tsp",
                "u1060.tsp"
        };

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

            // Bucle para las iteraciones con diferentes semillas
            String currentSeed = seed;
            for (int ite = 0; ite < nIteraciones; ite++)
            {
                // Desplazar el primer dígito al final
                currentSeed = currentSeed.substring(1) + currentSeed.charAt(0);
                // Tiempo de inicio de la iteración
                long startTime = System.currentTimeMillis();
                // Convertir la cadena de DNI a número
                long dniNumerico = Long.parseLong(currentSeed);

                //Ejecutar el algoritmo de Busqueda Local
                bLocal.ejecutarBusquedaLocal(greedy.RealizarGreedy(k, dniNumerico, lector), new Random(dniNumerico));


                // Nombre del archivo de log basado en el archivo .tsp y la semilla ( creacion del txt incluida )
                String rutaLog = rutaLogs + "log_" + archivoTSP.replace(".tsp", "") + "_" + currentSeed + ".txt";
                CreaLogs log = new CreaLogs(rutaLog);
                //Ejecucion de tabu

                // Generar mensaje de log y consola de blocal
                String mensaje = String.format("Ejecucion %d de %s(Seed: %s) - Busqueda Local: %f", ite + 1, archivosTSP[i], currentSeed, bLocal.getMejorCoste());
                long endTime = System.currentTimeMillis();
                long duracion = endTime - startTime;
                logAndPrint(log, mensaje,"Tiempo de ejecución: " + duracion + " milisegundos");

                 startTime = System.currentTimeMillis();
                AlgTabu_Clase03_Grupo04 tabu = new AlgTabu_Clase03_Grupo04(lector,5000,0.5,dniNumerico,log,0.08,0.1,10,10,50);
                double mejorTabu = tabu.ejecutarTabu(greedy.RealizarGreedy(k,dniNumerico,lector));


                String mensaje2 = String.format("Ejecucion %d de %s(Seed: %s) - Busqueda Tabu: %f", ite + 1, archivosTSP[i], currentSeed,mejorTabu );
                // Mostrar y registrar el tiempo de ejecución
                endTime = System.currentTimeMillis();
                duracion = endTime - startTime;
                logAndPrint(log, mensaje2,"Tiempo de ejecución: " + duracion + " milisegundos");

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
    public static void logAndPrint(CreaLogs log, String mensaje, String mensaje2) {
        System.out.println(mensaje); // Mostrar en consola
        log.escribirLog(mensaje);    // Escribir en el archivo de log
        log.escribirLog(mensaje2);
        log.escribirMejoresLocales();
    }
}
