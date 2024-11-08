import Algoritmos.EvolutivoGeneracional;
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
            for (int ite = 0; ite < LeerConfig.nIteraciones; ite++)
            {
                // Desplazar el primer dígito al final
                currentSeed = currentSeed.substring(1) + currentSeed.charAt(0);
                long dniNumerico = Long.parseLong(currentSeed);

                // Nombre del archivo de log basado en el archivo .tsp y la semilla ( creacion del txt incluida )
                String rutaLog = rutaLogs + "log_" + archivoTSP.replace(".tsp", "") + "_" + currentSeed + ".txt";
                CreaLogs log = new CreaLogs(rutaLog);

                //Ejecución Evolutivo Generacional
                //ejecutarEvolutivoGeneracional(dniNumerico, lector);

                log.cerrarLog();
            }
        }
    }

    private static void ejecutarEvolutivoGeneracional(long seed, LectorTSP lector)
    {
        EvolutivoGeneracional generacional = new EvolutivoGeneracional(LeerConfig.tamPoblacion, LeerConfig.porcientoGeneracion, LeerConfig.tamCandidatosGreedy, LeerConfig.cantidadElites
                , LeerConfig.kBest, LeerConfig.kWorst, LeerConfig.probCruce, LeerConfig.prob2opt, LeerConfig.maxEvaluaciones, LeerConfig.maxTiempo, seed, lector);

        generacional.ejecutarGeneracional();
    }
}