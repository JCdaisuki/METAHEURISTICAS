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
    public static int k;
    public static int num_iteraciones;
    public static double tam_entorno;
    public static double dism_entorno;
    public static int maxite;
    public static double empeoramientoPermitido;

    public static void leerConfiguracion(String rutaConfig)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaConfig)))
        {
            List<String> archivosList = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null)
            {
                line = line.trim();
                if (line.startsWith("static String[] archivosTSP")) {
                    // Leer la lista de archivos TSP
                    while ((line = br.readLine()) != null && !line.contains("};")) {
                        line = line.trim().replace("\"", "").replace(",", "");
                        if (!line.isEmpty()) {
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
                } else if (line.startsWith("int k")) {
                    k = Integer.parseInt(line.split("=")[1].trim().replace(";", ""));
                } else if (line.startsWith("int num_iteraciones")) {
                    num_iteraciones = Integer.parseInt(line.split("=")[1].trim().replace(";", ""));
                } else if (line.startsWith("double tam_entorno")) {
                    tam_entorno = Double.parseDouble(line.split("=")[1].trim().replace(";", ""));
                } else if (line.startsWith("double dism_entorno")) {
                    dism_entorno = Double.parseDouble(line.split("=")[1].trim().replace(";", ""));
                } else if (line.startsWith("int maxite")) {
                    maxite = Integer.parseInt(line.split("=")[1].trim().replace(";", ""));
                } else if (line.startsWith("double empeoramientoPermitido")) {
                    empeoramientoPermitido = Double.parseDouble(line.split("=")[1].trim().replace(";", ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al leer el archivo de configuración.");
        }
    }
}
