package Algoritmos;
import Auxiliares.CiudadInfo;
import ProcesadoFicheros.LectorTSP;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlgGreedy_Clase03_Grupo04
{
    private double mejorCoste;

    public double GetMejorCoste()
    {
        return mejorCoste;
    }

    public AlgGreedy_Clase03_Grupo04() {}

    // Función que realiza el algoritmo Greedy Aleatorio
    public int[] RealizarGreedy(int k, long seed, LectorTSP lector)
    {
        List<CiudadInfo> ordenado = OrdenarCiudades(lector);

        if (k > ordenado.size()) {
            k = ordenado.size();
        }

        // Vector solución que contiene el índice de las ciudades seleccionadas
        List<Integer> solucion = new ArrayList<>();
        double distTotal = 0; // Distancia total de la solución

        Random random = new Random(seed);
        int ciudad = random.nextInt(k);

        // Se añade la primera ciudad
        int tam_ordenado = ordenado.size();
        int indice = ordenado.get(ciudad).getIndice();
        int indice_ini = indice;
        int indice_ant = indice;
        solucion.add(indice); // Cambiamos a añadir el índice en lugar de CiudadInfo
        ordenado.remove(ciudad);

        for (int i = 0; i < tam_ordenado - 1; i++) {
            if (ordenado.isEmpty()) {
                ciudad = 0;
            } else {
                do {
                    ciudad = random.nextInt(k);
                } while (ciudad >= ordenado.size());
            }

            // Se añade la nueva ciudad obtenida a la solución
            indice = ordenado.get(ciudad).getIndice();
            solucion.add(indice); // Añadir el índice
            distTotal += lector.getDistancias()[indice][indice_ant];
            indice_ant = indice;

            ordenado.remove(ciudad);
        }

        // Suma de la distancia para volver al inicio
        distTotal += lector.getDistancias()[indice_ini][indice];

        // Convertir la lista de índices a un arreglo y devolverlo
        int[] resultado = new int[solucion.size()];

        for (int i = 0; i < solucion.size(); i++)
        {
            resultado[i] = solucion.get(i);
        }

        mejorCoste = distTotal;
        return resultado;
        }

    // Función auxiliar para ordenar el vector de ciudades en orden de menor a mayor distancia total al resto de ciudades
    private List<CiudadInfo> OrdenarCiudades(LectorTSP lector)
    {
        List<CiudadInfo> ciudadInfos = new ArrayList<>();

        for (int i = 0; i < lector.getCiudades().length; i++) {
            double distTotal = 0;

            for (int j = 0; j < lector.getCiudades().length; j++) {
                if (i != j) {
                    distTotal += lector.getDistancias()[i][j];
                }
            }

            CiudadInfo cp = new CiudadInfo(i, distTotal);
            ciudadInfos.add(cp);
        }

        // Ordenar la lista de ciudades por la distancia total, de menor a mayor
        ciudadInfos.sort((c1, c2) -> Double.compare(c1.GetDistTotal(), c2.GetDistTotal()));

        return ciudadInfos;
    }
}
