package Algoritmos;
import procesadoFicheros.LectorTSP;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Greedy
{
    //Clase auxiliar para el ordenamiento del vector
    private class CiudadInfo
    {
        private int indice;
        private double distTotal; //Distancia total de la ciudad al resto de ciudades

        CiudadInfo(int i, double d)
        {
            indice = i;
            distTotal = d;
        }

        public double GetDistTotal()
        {
            return distTotal;
        }
        public int getIndice(){return indice;}
    }

    public Greedy(){}

    //Función que realiza el algoritmo Greedy Aleatorio
    public double RealizarGreedy(int k, long seed, LectorTSP lector)
    {
        //Obtenemos un  vector que contiene las ciudades ordenadas de menor a mayor en base a su distancia total
        List<CiudadInfo> ordenado =  OrdenarCiudades(lector);

        //Si k supera al número de ciudades reducimos k al mismo
        if(k > ordenado.size())
        {
            k = ordenado.size();
        }

        //Vector solución que contiene el index de las ciudades seleccionadas
        List<CiudadInfo> solucion = new ArrayList<>();
        double distTotal = 0; //Distancia total de la solución

        //Se obtienen aleatoriamente ciudades para la solución entre 0 y k
        Random random = new Random(seed);
        int ciudad =  random.nextInt(k);

        //Se añade la primera ciudad
        int tam_ordenado = ordenado.size();
        int indice = ordenado.get(ciudad).indice;
        int indice_ini = indice;
        int indice_ant= indice;
        ordenado.remove(ciudad);

        for(int i = 0; i < tam_ordenado-1; i++)
        {
            //Si no quedan ciudades por seleccionar se selecciona la primera
            if(ordenado.isEmpty())
            {
                ciudad = 0;
            }
            else
            {
                do
                {
                    ciudad = random.nextInt(k);
                }
                while(ciudad >= ordenado.size()); //No aseguramos que el índice obtenido sea válido
            }

            //Se añade la nueva ciudad obtenida a la solución
            solucion.add(ordenado.get(ciudad));
            indice = ordenado.get(ciudad).getIndice();

            //Se suma la nueva distancia al valor del coste final
            distTotal += lector.getDistancias()[indice][indice_ant];
            indice_ant = indice;

            ordenado.remove(ciudad);
        }

        //Suma de la distancia para volver al inicio
        distTotal += lector.getDistancias()[indice_ini][indice];

        //Número de ciudades recorridas en la solución
        return distTotal;
    }

    //Función auxiliar para ordenar el vector de ciudades en orden de menor a mayor distancia total al resto de ciudades
    private List<CiudadInfo> OrdenarCiudades(LectorTSP lector)
    {
        List<CiudadInfo> ciudadInfos = new ArrayList<>();

        //Calcular la distancia total para cada ciudad
        for (int i = 0; i < lector.getCiudades().length; i++)
        {
            double distTotal = 0;

            //Sumar las distancias de la ciudad 'i' con todas las demás
            for (int j = 0; j < lector.getCiudades().length; j++)
            {
                if (i != j)
                {
                    distTotal += lector.getDistancias()[i][j];
                }
            }

            //Crear un CiudadInfo para la ciudad 'i' con su distancia total
            CiudadInfo cp = new CiudadInfo(i, distTotal);
            ciudadInfos.add(cp);
        }

        // Ordenar la lista de ciudades por la distancia total, de menor a mayor
        ciudadInfos.sort((c1, c2) -> Double.compare(c1.GetDistTotal(), c2.GetDistTotal()));

        return ciudadInfos;
    }
}
