package Auxiliares;

import ProcesadoFicheros.LectorTSP;

public class Vecino
{
    private LectorTSP lector;
    private int[] vectorSol;
    private double costeTotal;

    public Vecino(int[] solucion, LectorTSP lector)
    {
        this.vectorSol = solucion;
        this.lector = lector;
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

    public void setVectorSol(int[] sol) {
        vectorSol = sol;
        CalcularCosteTotal();
    }
}