import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Juan Carlos Gonzalez Martinez
 * @author Jose Antonio Mayoral Luna
 */

public class LectorTSP
{
    private final String ruta;
    private final double ciudades[][]; //Almacena las coordenadas de las ciudades
    private final double distancias[][]; //Almacena las distancias entre ciudades

    public LectorTSP(String ruta)
    {
        //Se elimina la extensión del archivo de la ruta
        this.ruta = ruta.split("\\.")[0];

        FileReader f = null; //Objeto para leer el archivo
        String linea = null; //Almacena temporalmente cada linea del archivo

        //Se intenta abrir el archivo
        try
        {
            f = new FileReader(ruta);
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(LectorTSP.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Objeto para leer el archivo linea por linea
        BufferedReader b = new BufferedReader(f);

        //Se lee el archivo hasta encontrar la linea que define el número de ciudades
        while(!linea.split(":")[0].equals("DIMENSION"))
        {
            try
            {
                linea = b.readLine();
            }
            catch (IOException ex)
            {
                Logger.getLogger(LectorTSP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


        //Divide la linea en dos partes separadas por ":" donde la segunda contiene el número de ciudades (Que se convierte en entero)
        int tam = Integer.parseInt(linea.split(":")[1].replace(" ", ""));

        // Inicialización del array 'ciudades' y la variable 'linea'
        ciudades = new double[tam][2];


        // Leer la primera línea del archivo
        try {
            linea = b.readLine();
        } catch (IOException ex) {
            Logger.getLogger(LectorTSP.class.getName()).log(Level.SEVERE, "Error al leer la línea inicial", ex);
            return; // Terminar la ejecución si no se puede leer la línea inicial
        }

        // Procesar las líneas hasta encontrar la correcta
        while (linea != null && linea.split(" ").length != 3) {
            try {
                linea = b.readLine();
            } catch (IOException ex) {
                Logger.getLogger(LectorTSP.class.getName()).log(Level.SEVERE, "Error al leer la línea", ex);
                return; // Terminar la ejecución si no se puede leer la línea
            }
        }

        // Procesar las líneas de datos hasta encontrar "EOF"
        while (linea != null && !linea.equals("EOF")) {
            String[] split = linea.split(" ");

            if (split.length == 3) {
                try {
                    int index = Integer.parseInt(split[0]) - 1;

                    // Verificar que el índice esté dentro del rango válido
                    if (index >= 0 && index < tam) {
                        ciudades[index][0] = Double.parseDouble(split[1]);
                        ciudades[index][1] = Double.parseDouble(split[2]);
                    } else {
                        Logger.getLogger(LectorTSP.class.getName()).log(Level.WARNING, "Índice fuera de rango: " + index);
                    }
                } catch (NumberFormatException e) {
                    Logger.getLogger(LectorTSP.class.getName()).log(Level.WARNING, "Error en el formato de los números: " + linea, e);
                }
            } else {
                Logger.getLogger(LectorTSP.class.getName()).log(Level.WARNING, "Formato de línea incorrecto: " + linea);
            }

            try {
                linea = b.readLine();
            } catch (IOException ex) {
                Logger.getLogger(LectorTSP.class.getName()).log(Level.SEVERE, "Error al leer la siguiente línea", ex);
                return; // Terminar la ejecución si no se puede leer la línea
            }
        }

    }


    //Get ruta
    public String getRuta()
    {
        return ruta;
    }

    //Get matriz ciudades
    public double[][] getCiudades()
    {
        return ciudades;
    }

    //Get matriz distancias
    public double[][] getDistancias()
    {
        return distancias;
    }
}