import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Juan Carlos Gonzalez Martinez
 * @author Jose Antonio Mayoral Luna
 */

public class LectorTSP
{
    private final String ruta;
    private double ciudades[][]; //Almacena las coordenadas de las ciudades
    private double distancias[][]; //Almacena las distancias entre ciudades

    // Función auxiliar para calcular el coste de una solución
    private double calcularCoste(List<Integer> sol, double[][] dist, int n)
    {
        double coste = 0.0;

        // Sumar las distancias entre las ciudades consecutivas en la solución
        for (int i = 0; i < n - 1; i++)
        {
            coste += dist[sol.get(i)][sol.get(i + 1)];
        }

        // Añadir la distancia desde la última ciudad hasta la primera para cerrar el ciclo
        coste += dist[sol.get(0)][sol.get(n - 1)];
        return coste;
    }

    // Función auxiliar para cargar la ciudad inicial en la solución
    private void cargarInicial(int c, List<Integer> sol, List<Boolean> marcada)
    {
        sol.add(c);
        marcada.set(c, true);
    }

    // Función Greedy para resolver el problema
    public greedy(double[][] dist, int n, List<Integer> sol)
    {
        
    }

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
                        distancias[i][j] = distancias[j][i] = Math.sqrt(
                                Math.pow(ciudades[i][0] - ciudades[j][0], 2) +
                                        Math.pow(ciudades[i][1] - ciudades[j][1], 2)
                        );
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
}