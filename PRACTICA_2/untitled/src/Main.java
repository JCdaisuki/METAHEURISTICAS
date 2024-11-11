import Algoritmos.AlgEvolutivoGeneracional_Clase03_Grupo04;
import ProcesadoFicheros.CreaLogs;
import ProcesadoFicheros.LectorTSP;
import ProcesadoFicheros.LeerConfig;

public class Main
{
    public static void main(String[] args)
    {
        LeerConfig.leerConfiguracion("config.txt");

        String[] archivosTSP = LeerConfig.archivosTSP;
        String rutaBase = LeerConfig.rutaBase;
        String rutaLogs = LeerConfig.rutaLogs;
        String seed = LeerConfig.seed;

        for (int i = 0; i < archivosTSP.length; i++)
        {
            String archivoTSP = archivosTSP[i];
            String rutaArchivo = rutaBase + archivoTSP;

            LectorTSP lector = new LectorTSP(rutaArchivo);

            String currentSeed = seed;

            AlgEvolutivoGeneracional_Clase03_Grupo04 generacional = new AlgEvolutivoGeneracional_Clase03_Grupo04(LeerConfig.tamPoblacion, LeerConfig.porcientoGeneracion, LeerConfig.tamCandidatosGreedy, LeerConfig.cantidadElites
                    , LeerConfig.kBest, LeerConfig.kWorst, LeerConfig.probCruce, LeerConfig.prob2opt, LeerConfig.maxEvaluaciones, LeerConfig.maxTiempo);

            for (int ite = 0; ite < LeerConfig.nIteraciones; ite++)
            {
                // Desplazar el primer dígito al final
                currentSeed = currentSeed.substring(1) + currentSeed.charAt(0);
                long dniNumerico = Long.parseLong(currentSeed);

                // Nombre del archivo de log basado en el archivo .tsp y la semilla ( creacion del txt incluida )
                String rutaLog = rutaLogs + "log_" + archivoTSP.replace(".tsp", "") + "_" + currentSeed + ".txt";
                CreaLogs log = new CreaLogs(rutaLog);

                //Ejecución Evolutivo Generacional
                ejecutarEvolutivoGeneracional(generacional, dniNumerico, lector, ite, archivoTSP, log);

                log.cerrarLog();
            }
        }
    }

    public static void logAndPrint(CreaLogs log, String mensaje, String mensaje2)
    {
        //Mostrar en consola
        System.out.println(mensaje);

        //Escribir en el archivo de log
        log.escribirLog(mensaje);
        log.escribirLog(mensaje2);
        log.escribirMejoresLocales();
    }

    private static void ejecutarEvolutivoGeneracional(AlgEvolutivoGeneracional_Clase03_Grupo04 generacional, long seed, LectorTSP lector, int ite, String archivo, CreaLogs log)
    {
        long startTime = System.currentTimeMillis();

        generacional.ejecutarGeneracional(seed, lector);

        String mensaje = String.format("Ejecucion %d(Seed: %s) - Evolutivo Generacional: %f", ite + 1, seed, generacional.getMejorCoste());
        long endTime = System.currentTimeMillis();
        long duracion = endTime - startTime;
        logAndPrint(log, mensaje,"Tiempo de ejecución: " + duracion + " milisegundos");
    }
}