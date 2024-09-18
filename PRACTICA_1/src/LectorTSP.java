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
    public double greedy(double[][] dist, int n, List<Integer> sol)
    {
        // Vector de ciudades marcadas (Se inicializa con ninguna ciudad marcada como visitada  al inicio)
        List<Boolean> visitada = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
        {
            visitada.add(false);
        }

        //Se establece una solución temporal
        List<Integer> tempSol = new ArrayList<Integer>();
        double menorCoste = Double.MAX_VALUE; //Menor distancia posible

        for(int c = 0; c < n; c++)
        {
            // Inicializar la solución y marcar la ciudad inicial
            cargarInicial(c, tempSol, visitada);

            //Coste de la solución actual
            double costeActual = 0.0;

            //Vaciar la lista de visitadas para evitar conflicto con soluciones anteriores
            visitada.clear();
            for (int i = 0; i < n; i++) {
                visitada.add(false);
            }

            // Búsqueda greedy
            for (int i = 0; i < n - 1; i++)
            {
                double menorDist = Double.MAX_VALUE; //Menor distancia posible
                int posMenor = 0; //Indice de la ciudad más cercana no visitada

                // Busca la ciudad más cercana que no esté marcada
                for (int j = 0; j < n; j++)
                {
                    //Se encuentra la ciudad más cercana no visitada
                    if (!visitada.get(j) && dist[sol.get(i)][j] < menorDist) {
                        menorDist = dist[sol.get(i)][j];
                        posMenor = j;
                    }
                }

                // Agregar la ciudad más cercana a la solución y marcarla
                tempSol.set(i + 1, posMenor);
                visitada.set(posMenor, true);

                //Si el coste de esta solución ya supera a una anterior, descartamos la solución actual
                if(calcularCoste(sol, dist, n) >= costeActual)
                {
                    continue;
                }
            }

            //Comparación de soluciones
            costeActual = calcularCoste(sol, dist, n);
            if(costeActual < menorCoste)
            {
                sol = tempSol;
                menorCoste = costeActual;
            }
        }

        // Devolver el coste de la solución obtenida
        return menorCoste;
    }

    public LectorTSP(String ruta)
    {
        this.ruta = ruta.split("\\.")[0];

        FileReader f = null;
        BufferedReader b = null;

        try {
            f = new FileReader(ruta);
            b = new BufferedReader(f);

            String linea;
            // Busca la línea que define el número de ciudades
            while ((linea = b.readLine()) != null) {
                if (linea.startsWith("DIMENSION")) {
                    int tam = Integer.parseInt(linea.split(":")[1].trim());
                    ciudades = new double[tam][2];
                    break;
                }
            }

            // Procesa las líneas que contienen las coordenadas
            while ((linea = b.readLine()) != null) {
                if (linea.equals("EOF")) {
                    break; // Termina si se encuentra EOF
                }
                if (linea.trim().isEmpty() || linea.startsWith("EDGE_WEIGHT_TYPE") || linea.startsWith("TYPE")) {
                    continue; // Ignora líneas irrelevantes
                }
                String[] split = linea.split(" ");
                if (split.length == 3) {
                    try {
                        int index = Integer.parseInt(split[0]) - 1;
                        if (index >= 0 && index < ciudades.length) {
                            ciudades[index][0] = Double.parseDouble(split[1]);
                            ciudades[index][1] = Double.parseDouble(split[2]);
                        }
                    } catch (NumberFormatException e) {
                        Logger.getLogger(LectorTSP.class.getName()).log(Level.WARNING, "Error en el formato de los números: " + linea, e);
                    }
                }
            }

            // Inicializa la matriz de distancias
            distancias = new double[ciudades.length][ciudades.length];
            for (int i = 0; i < ciudades.length; i++) {
                for (int j = i; j < ciudades.length; j++) {
                    if (i != j) {
                        distancias[i][j] = distancias[j][i] = Math.sqrt(
                                Math.pow(ciudades[i][0] - ciudades[j][0], 2) +
                                        Math.pow(ciudades[i][1] - ciudades[j][1], 2)
                        );
                    } else {
                        distancias[i][j] = Double.POSITIVE_INFINITY;
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LectorTSP.class.getName()).log(Level.SEVERE, "Archivo no encontrado", ex);
        } catch (IOException ex) {
            Logger.getLogger(LectorTSP.class.getName()).log(Level.SEVERE, "Error al leer el archivo", ex);
        } finally {
            try {
                if (b != null) b.close();
                if (f != null) f.close();
            } catch (IOException ex) {
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