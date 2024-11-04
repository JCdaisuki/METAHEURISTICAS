package ProcesadoFicheros;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeerConfig
{
    public static String[] archivosTSP;
    public static String rutaBase;
    public static String rutaLogs;
    public static String seed;
    public static int nIteraciones;
    public static int tamPoblacion;
    public static double porcientoGeneracion;
    public static int tamCandidatosGreedy;
    public static int cantidadElites;
    public static int kBest;
    public static double probCruce;
    public static double prob2opt;
    public static int maxEvaluaciones;
    public static long maxTiempo;

    public static void leerConfiguracion(String rutaConfig)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaConfig)))
        {
            List<String> archivosList = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null)
            {
                line = line.trim();
                if (line.startsWith("static String[] archivosTSP"))
                {
                    // Leer la lista de archivos TSP
                    while ((line = br.readLine()) != null && !line.contains("};"))
                    {
                        line = line.trim().replace("\"", "").replace(",", "");
                        if (!line.isEmpty())
                        {
                            archivosList.add(line);
                        }
                    }
                    archivosTSP = archivosList.toArray(new String[0]);
                } else if (line.startsWith("String rutaBase")) {
                    rutaBase = line.split("=")[1].trim().replace("\"", "").replace(";", "");
                } else if (line.startsWith("String rutaLogs")) {
                    rutaLogs = line.split("=")[1].trim().replace("\"", "").replace(";", "");
                } else if (line.startsWith("String seed")) {
                    seed = line.split("=")[1].trim().replace("\"", "").replace(";", "");
                } else if (line.startsWith("int nIteraciones")) {
                    nIteraciones = Integer.parseInt(line.split("=")[1].trim().replace(";", ""));
                } else if (line.startsWith("int tamPoblacion")) {
                    tamPoblacion = Integer.parseInt(line.split("=")[1].trim().replace(";", ""));
                } else if (line.startsWith("double porcientoGeneracion")) {
                    porcientoGeneracion = Double.parseDouble(line.split("=")[1].trim().replace(";", ""));
                } else if (line.startsWith("int tamCandidatosGreedy")) {
                    tamCandidatosGreedy = Integer.parseInt(line.split("=")[1].trim().replace(";", ""));
                } else if (line.startsWith("int cantidadElites")) {
                    cantidadElites = Integer.parseInt(line.split("=")[1].trim().replace(";", ""));
                } else if (line.startsWith("int kBest")) {
                    kBest = Integer.parseInt(line.split("=")[1].trim().replace(";", ""));
                } else if (line.startsWith("double probCruce")) {
                    probCruce = Double.parseDouble(line.split("=")[1].trim().replace(";", ""));
                } else if (line.startsWith("double prob2opt")) {
                    prob2opt = Double.parseDouble(line.split("=")[1].trim().replace(";", ""));
                } else if (line.startsWith("int maxEvaluaciones")) {
                    maxEvaluaciones = Integer.parseInt(line.split("=")[1].trim().replace(";", ""));
                } else if (line.startsWith("long maxTiempo")) {
                    maxTiempo = Long.parseLong(line.split("=")[1].trim().replace(";", ""));
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("Error al leer el archivo de configuración.");
        }
    }
}