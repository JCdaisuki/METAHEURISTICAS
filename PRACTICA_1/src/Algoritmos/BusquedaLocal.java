package Algoritmos;

import procesadoFicheros.LectorTSP;
import java.util.ArrayList;
import java.util.Random;

public class BusquedaLocal
{
    private class Vecino {
        private int[] vector_sol;
        private double coste_total;

        public Vecino(int[] solucion)
        {
            this.vector_sol = solucion;
            calcular_coste_total();
        }

        public void calcular_coste_total()
        {
            double posible_nuevo_coste = 0;

            for (int i = 0; i < vector_sol.length; i++)
            {
                if (i != 0)
                {
                    posible_nuevo_coste += lector.getDistancias()[vector_sol[i]][vector_sol[i - 1]];
                }
                else
                {
                    posible_nuevo_coste += lector.getDistancias()[vector_sol[i]][vector_sol[vector_sol.length - 1]];
                }
            }

            coste_total = posible_nuevo_coste;
        }

        public double get_coste_total()
        {
            return coste_total;
        }

        public int[] get_vector_sol()
        {
            return vector_sol;
        }
    }

    private int num_iteraciones;
    private double tam_entorno;
    private double dism_entorno;
    private LectorTSP lector;
    private Random random;
    private double costeMejorSolucion;

    public BusquedaLocal(int n, double t, double d, LectorTSP l)
    {
        num_iteraciones = n;
        tam_entorno = t;
        dism_entorno = d;
        lector = l;
        costeMejorSolucion = Double.MAX_VALUE;
    }

    public void ejecutarBusquedaLocal(int[] s_inicial, Random r)
    {
        random = r;

        //Declaración de variables que emplearemos en el algoritmo
        int[] s = s_inicial.clone(); // Clonar la solución inicial para no modificar el original
        Vecino mejorVecino = new Vecino(s);
        double costeMejorSolucion = mejorVecino.get_coste_total();
        double costeMejorVecino;
        int i = 0;
        boolean mejora = true;

        int tamVecindario = (int) (num_iteraciones * tam_entorno); //Tamaño inicial del vecindario
        int cambio = (int) (num_iteraciones * dism_entorno); //Cada % de iteraciones

        while (i < num_iteraciones && mejora)
        {
            costeMejorVecino = Double.MAX_VALUE;
            mejora = false;
            Vecino mejorVecinoActual = null;

            //Explora el vecinadario
            for (int j = 0; j < tamVecindario; j++)
            {
                //Genera dos posiciones distintas al azar
                int p1 = random.nextInt(s.length);
                int p2 = random.nextInt(s.length);

                while (p2 == p1) //Se comprueba que sean distintas
                {
                    p2 = random.nextInt(s.length);
                }

                //Se genera una nueva solución aplicando el operador 2-opt
                int[] nuevaSol = operador_dos_opt(s, p1, p2);

                //Se crea un nuevo vecino y se calcula su coste
                Vecino vecino = new Vecino(nuevaSol);
                double coste = vecino.get_coste_total();

                //Se Actualiza el mejor vecino encontrado en este vecindario si es necesario
                if (coste < costeMejorVecino)
                {
                    costeMejorVecino = coste;
                    mejorVecinoActual = vecino;
                }
            }

            //Se ha encontrado una mejora?
            if (costeMejorVecino < costeMejorSolucion)
            {
                s = mejorVecinoActual.get_vector_sol();
                costeMejorSolucion = costeMejorVecino;
                mejorVecino = mejorVecinoActual;

                mejora = true;
                i++;
            }

            //Se reduce el tamaño del vecindario cada "cambio" iteraciones
            if ((i > 0) && (i % cambio == 0))
            {
                tamVecindario = (int) (tamVecindario * (1 - dism_entorno));
                tamVecindario = Math.max(tamVecindario, 1);
            }
        }
    }

    public int[] operador_dos_opt(int[] solucion, int p1, int p2)
    {
        int[] nueva_sol = solucion.clone();
        int temp = nueva_sol[p1];
        nueva_sol[p1] = nueva_sol[p2];
        nueva_sol[p2] = temp;
        return nueva_sol;
    }

    public double getMejorCoste()
    {
        return costeMejorSolucion;
    }
}
