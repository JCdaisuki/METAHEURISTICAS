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