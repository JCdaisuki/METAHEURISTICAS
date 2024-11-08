package Auxiliares;

import ProcesadoFicheros.LectorTSP;
import java.util.ArrayList;

public class Individuo {
    private LectorTSP lector;
    private int[] vectorSol;
    private double costeTotal;

    // Constructor que recibe un int[]
    public Individuo(int[] solucion, LectorTSP lector) {
        this.vectorSol = solucion;
        this.lector = lector;
        CalcularCosteTotal();
    }
    public Individuo(LectorTSP Lector) {
        this.vectorSol = null;  // Inicializa como null hasta que se asigne una solución.
        this.lector = Lector;     // Inicializa como null hasta que se asigne un lector.
        this.costeTotal = 0.0;  // Inicializa el coste como 0 por defecto.
    }

    // Constructor que recibe un ArrayList<Integer>
    public Individuo(ArrayList<Integer> solucion, LectorTSP lector) {
        // Convertir el ArrayList a int[]
        this.vectorSol = solucion.stream().mapToInt(Integer::intValue).toArray();
        this.lector = lector;
        CalcularCosteTotal();
    }

    public void CalcularCosteTotal() {
        double posible_nuevo_coste = 0;

        for (int i = 0; i < vectorSol.length; i++) {
            if (i != 0) {
                posible_nuevo_coste += lector.getDistancias()[vectorSol[i]][vectorSol[i - 1]];
            } else {
                posible_nuevo_coste += lector.getDistancias()[vectorSol[i]][vectorSol[vectorSol.length - 1]];
            }
        }

        costeTotal = posible_nuevo_coste;
    }

    public double getCosteTotal() {
        return costeTotal;
    }

    public int[] get_vector_sol() {
        return vectorSol;
    }

    public void setVectorSol(int[] sol) {
        vectorSol = sol;
        CalcularCosteTotal();
    }

    public void setVectorSol(ArrayList<Integer> sol) {
        // Convertir el ArrayList a int[]
        this.vectorSol = sol.stream().mapToInt(Integer::intValue).toArray();
        CalcularCosteTotal();
    }
}
