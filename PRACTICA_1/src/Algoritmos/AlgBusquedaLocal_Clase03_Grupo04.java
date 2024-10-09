package Algoritmos;

import procesadoFicheros.LectorTSP;

import java.util.Random;

public class AlgBusquedaLocal_Clase03_Grupo04
{
    private class Vecino
    {
        private int[] vectorSol;
        private double costeTotal;

        public Vecino(int[] solucion)
        {
            this.vectorSol = solucion;
            CalcularCosteTotal();
        }

        public void CalcularCosteTotal()
        {
            double posible_nuevo_coste = 0;

            for (int i = 0; i < vectorSol.length; i++)
            {
                if (i != 0)
                {
                    posible_nuevo_coste += lector.getDistancias()[vectorSol[i]][vectorSol[i - 1]];
                }
                else
                {
                    posible_nuevo_coste += lector.getDistancias()[vectorSol[i]][vectorSol[vectorSol.length - 1]];
                }
            }

            costeTotal = posible_nuevo_coste;
        }

        public double GetCosteTotal()
        {
            return costeTotal;
        }

        public int[] get_vector_sol()
        {
            return vectorSol;
        }
    }

    private int numIteraciones;
    private double tamEntorno;
    private double dismEntorno;
    private LectorTSP lector;
    private Random random;
    private double costeMejorSolucion;

    public AlgBusquedaLocal_Clase03_Grupo04(int n, double t, double d, LectorTSP l)
    {
        numIteraciones = n;
        tamEntorno = t;
        dismEntorno = d;
        lector = l;
    }

    public void ejecutarBusquedaLocal(int[] s_inicial, Random r)
    {
        random = r;

        //Declaración de variables que emplearemos en el algoritmo
        int[] s = s_inicial.clone(); // Clonar la solución inicial para no modificar el original
        Vecino mejorVecino = new Vecino(s);
        costeMejorSolucion = mejorVecino.GetCosteTotal();
        double costeMejorVecino;

        int i = 0;
        boolean mejora = true;

        int tamVecindario = (int) (numIteraciones * (1 - tamEntorno)); //Tamaño inicial del vecindario
        int cambio = (int) (numIteraciones * dismEntorno); //Cada % de iteraciones

        while (i < numIteraciones && mejora)
        {
            costeMejorVecino = Double.MAX_VALUE;
            Vecino mejorVecinoActual = null;
            mejora = false;

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
                double coste = vecino.GetCosteTotal();

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

                i++;
                mejora = true;
            }

            //Se reduce el tamaño del vecindario cada "cambio" iteraciones
            if ((i > 0) && (i % cambio == 0))
            {
                tamVecindario = (int) (tamVecindario * (1 - tamEntorno));
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
