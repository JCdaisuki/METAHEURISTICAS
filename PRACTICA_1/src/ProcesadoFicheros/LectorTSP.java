package ProcesadoFicheros;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LectorTSP
{

    private String ruta = "..\\METAHEURISTICAS\\PRACTICA_1\\a280.tsp";
    private double ciudades[][]; //Almacena las coordenadas de las ciudades
    private double distancias[][]; //Almacena las distancias entre ciudades

    public LectorTSP(String ruta)
    {
        // Se guarda la ruta sin la extensión.
        this.ruta = ruta.split("\\.")[0];
        FileReader f = null;
        BufferedReader b = null;

        try
        {
            // Se intenta abrir el archivo especificado por la ruta.
            f = new FileReader(ruta);
            b = new BufferedReader(f);

            String linea;
            // Se busca la línea que define el número de ciudades (DIMENSION).
            while ((linea = b.readLine()) != null)
            {
                if (linea.startsWith("DIMENSION"))
                {
                    // Se obtiene el número de ciudades desde la línea DIMENSION.
                    int tam = Integer.parseInt(linea.split(":")[1].trim());
                    ciudades = new double[tam][2];  // Se inicializa la matriz de coordenadas.
                    break;
                }
            }

            // Procesa las líneas que contienen las coordenadas de las ciudades.
            while ((linea = b.readLine()) != null)
            {
                if (linea.equals("EOF"))
                {
                    break; // Termina la lectura si encuentra la línea EOF.
                }

                // Se ignoran las líneas vacías o irrelevantes.
                if (linea.trim().isEmpty() || linea.startsWith("EDGE_WEIGHT_TYPE") || linea.startsWith("TYPE"))
                {
                    continue;
                }

                String[] split = linea.split(" ");

                // Si la línea tiene tres partes (número de ciudad y dos coordenadas).
                if (split.length == 3)
                {
                    try
                    {
                        // Convierte el índice de la ciudad a entero.
                        int index = Integer.parseInt(split[0]) - 1;
                        // Asegura que el índice sea válido.

                        if (index >= 0 && index < ciudades.length)
                        {
                            // Almacena las coordenadas (x, y) en la matriz de ciudades.
                            ciudades[index][0] = Double.parseDouble(split[1]);
                            ciudades[index][1] = Double.parseDouble(split[2]);
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        // En caso de error, se registra un aviso.
                        Logger.getLogger(LectorTSP.class.getName()).log(Level.WARNING, "Error en el formato de los números: " + linea, e);
                    }
                }
            }

            // Inicializa la matriz de distancias entre las ciudades.
            distancias = new double[ciudades.length][ciudades.length];

            for (int i = 0; i < ciudades.length; i++)
            {
                for (int j = i; j < ciudades.length; j++)
                {
                    if (i != j)
                    {
                        // Calcula la distancia euclidiana entre las ciudades (i, j).
                        distancias[i][j] = distancias[j][i] = Math.sqrt(Math.pow(ciudades[i][0] - ciudades[j][0], 2) + Math.pow(ciudades[i][1] - ciudades[j][1], 2));
                    }
                    else
                    {
                        // La distancia a sí misma es infinita.
                        distancias[i][j] = Double.POSITIVE_INFINITY;
                    }
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            // Se registra un error si el archivo no se encuentra.
            Logger.getLogger(LectorTSP.class.getName()).log(Level.SEVERE, "Archivo no encontrado", ex);
        }
        catch (IOException ex)
        {
            // Se registra un error en caso de problemas de lectura.
            Logger.getLogger(LectorTSP.class.getName()).log(Level.SEVERE, "Error al leer el archivo", ex);
        }
        finally
        {
            try
            {
                // Se cierran los recursos abiertos (BufferedReader y FileReader).
                if (b != null) b.close();
                if (f != null) f.close();
            }
            catch (IOException ex)
            {
                // Se registra un error si hay problemas al cerrar el archivo.
                Logger.getLogger(LectorTSP.class.getName()).log(Level.SEVERE, "Error al cerrar el archivo", ex);
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

    // Obtener la distancia entre dos ciudades específicas
    public double getDistancia(int ciudad1, int ciudad2)
    {
        // Verifica que los índices sean válidos dentro del rango
        if (ciudad1 >= 0 && ciudad1 < distancias.length && ciudad2 >= 0 && ciudad2 < distancias.length) {
            return distancias[ciudad1][ciudad2];
        } else {
            throw new IndexOutOfBoundsException("Índices de las ciudades fuera de rango: " + ciudad1 + ", " + ciudad2);
        }
    }
}
