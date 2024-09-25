package Algoritmos;
import procesadoFicheros.LectorTSP;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Greedy
{
    //Clase auxiliar para el ordenamiento del vector
    private class CiudadesPair
    {
        //TODO : eliminar atributo ciudad
        private int indice;
        private double[] ciudad; //Contiene las coordenadas de la ciudad
        private double distTotal; //Distancia total de la ciudad al resto de ciudades

        CiudadesPair(double[] c, double d)
        {
            ciudad = c;
            distTotal = d;
        }

        public double[] GetCiudad()
        {
            return ciudad;
        }

        public double GetDistTotal()
        {
            return distTotal;
        }
        public void setIndice(int _indice){
            indice = _indice;
        }
        public int getIndice(){return indice;}
    }

    private Greedy.CiudadesPair sol_greedy_act ;


    //Función que realiza el algoritmo Greedy Aleatorio
    public double RealizarGreedy(int k, long seed, LectorTSP lector)
    {
        //Obtenemos un  vector que contiene las ciudades ordenadas de menor a mayor en base a su distancia total
        List<Greedy.CiudadesPair> ordenado =  OrdenarCiudades(lector);

        //Si k supera al número de ciudades reducimos k al mismo
        if(k > ordenado.size())
        {
            k = ordenado.size();
        }

        //Vector solución que contiene el index de las ciudades seleccionadas
        List<Greedy.CiudadesPair> solucion = new ArrayList<>();
        double distTotal = 0; //Distancia total de la solución
        boolean visitada[] = new boolean[ordenado.size()]; //Contiene las ciudades ya visitadas para evitar repeticiones

        //Establecemos que aún no se ha visitado ninguna ciudad
        for(int i = 0; i < visitada.length; i++)
        {
            visitada[i] = false;
        }

        //Se obtienen aleatoriamente ciudades para la solución entre 0 y k
        Random random = new Random(seed);

        //Se añade la primera ciudad
        int ciudad =  random.nextInt(k);
        int tam_ordenado = ordenado.size();
        int indice = ordenado.get(ciudad).indice;
        int indice_ini = indice;
        int indice_ant= indice;
        ordenado.remove(ciudad);
        for(int i = 0; i < tam_ordenado-1; i++)
        {


            if(ordenado.size()==0){
                ciudad = 0;
            }else{
                do{
                    ciudad = random.nextInt(k);
                }while(ciudad >= ordenado.size() );
            }

                solucion.add(ordenado.get(ciudad));
                indice=ordenado.get(ciudad).getIndice();
                distTotal += lector.getDistancias()[indice][indice_ant];
                indice_ant=indice;
                ordenado.remove(ciudad);

        }
        //suma de la distancia para volver al inicio
        distTotal += lector.getDistancias()[indice_ini][indice];

        //Calcula la distancia
        System.out.printf("\nTam : %d\n", solucion.size());
        return distTotal;
    }
    private void Desplazamiento (List<Greedy.CiudadesPair> sol, int k ){
        for(int i = k; i < sol.size()-1; i ++){
            sol.add(i,sol.get(i+1));
        }
}

    //Función auxiliar para ordenar el vector de ciudades en orden de menor a mayor distancia total al resto de ciudades
    private List<Greedy.CiudadesPair> OrdenarCiudades(LectorTSP lector)
    {
        List<Greedy.CiudadesPair> ciudadesPairs = new ArrayList<>();

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

            //Crear un CiudadesPair para la ciudad 'i' con su distancia total
            Greedy.CiudadesPair cp = new Greedy.CiudadesPair(lector.getCiudades()[i], distTotal);
            cp.setIndice(i);
            ciudadesPairs.add(cp);
        }

        // Ordenar la lista de ciudades por la distancia total, de menor a mayor
        ciudadesPairs.sort((c1, c2) -> Double.compare(c1.GetDistTotal(), c2.GetDistTotal()));

        return ciudadesPairs;
    }


    // Función auxiliar para calcular el coste de una solución
    private double CalcularCoste(List<Integer> sol, double[][] dist, int n)
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

    public Greedy() {
        this.sol_greedy_act = null;
    }
}
