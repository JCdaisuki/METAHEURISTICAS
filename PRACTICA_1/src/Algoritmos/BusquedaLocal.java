package Algoritmos;

import procesadoFicheros.LectorTSP;
import java.util.ArrayList;
import java.util.Random;

public class BusquedaLocal {

    private int num_iteraciones_max;
    private double tam_entorno;
    private double porciento_a_subir;
    private double ratio_de_subida;
    private LectorTSP lector;
    private Random random; // Objeto Random pasado desde el main

    private class Vecino {
        private int[] vector_sol;
        private double coste_total;

        public Vecino(int[] solucion) {
            this.vector_sol = solucion;
            calcular_coste_total();
        }

        public void calcular_coste_total() {
            double posible_nuevo_coste = 0;
            for (int i = 0; i < vector_sol.length; i++) {
                if (i != 0) {
                    posible_nuevo_coste += lector.getDistancias()[vector_sol[i]][vector_sol[i - 1]];
                } else {
                    posible_nuevo_coste += lector.getDistancias()[vector_sol[i]][vector_sol[vector_sol.length - 1]];
                }
            }
            coste_total = posible_nuevo_coste;
        }

        public double get_coste_total() {
            return coste_total;
        }

        public int[] get_vector_sol() {
            return vector_sol;
        }
    }

    public BusquedaLocal(int numero_maximo_iteraciones, double tam_inicial_entorno, double porciento_a_subir_, double ratio_de_subida_, LectorTSP lector, Random random) {
        num_iteraciones_max = numero_maximo_iteraciones;
        tam_entorno = tam_inicial_entorno;
        porciento_a_subir = porciento_a_subir_;
        ratio_de_subida = ratio_de_subida_;
        this.lector = lector;
        this.random = random; // Asignar el objeto Random
    }

    public double realizar_busqueda(int[] solucion_inicial) {
        double mejor_coste = Double.MAX_VALUE;
        Vecino mejor_sol_actual = new Vecino(solucion_inicial);
        mejor_coste = mejor_sol_actual.get_coste_total();
        int iteraciones_actuales = 0;

        boolean hay_mejora = true; // Variable para detectar si hubo mejora

        while (iteraciones_actuales < num_iteraciones_max && hay_mejora) {
            int vecinos_tamano = calcular_tamano_entorno(iteraciones_actuales);
            ArrayList<Vecino> vecinos = new ArrayList<>();

            for (int i = 0; i < vecinos_tamano; i++) {
                int[] nueva_solucion = operador_dos_opt(mejor_sol_actual.get_vector_sol());
                Vecino nuevo_vecino = new Vecino(nueva_solucion);
                vecinos.add(nuevo_vecino);
            }

            Vecino mejor_vecino = funcion_eva(vecinos, mejor_sol_actual);

            // Si el mejor vecino es mejor que la solución actual, lo actualizamos
            if (mejor_vecino.get_coste_total() < mejor_sol_actual.get_coste_total()) {
                mejor_sol_actual = mejor_vecino;
                mejor_coste = mejor_sol_actual.get_coste_total();
                hay_mejora = true; // Hubo una mejora
            } else {
                hay_mejora = false; // No hubo mejora, saldrá en la próxima iteración
            }

            iteraciones_actuales++;
        }

        return mejor_coste;
    }


    private int calcular_tamano_entorno(int iteracion_actual) {
        // Tamaño inicial del entorno, el 8% de las iteraciones totales
        int tam_base = (int) (num_iteraciones_max * tam_entorno);

        // Calcular cuántos bloques del 10% de iteraciones ya han pasado
        int num_bloques_10p = iteracion_actual / (int)(num_iteraciones_max * 0.1);

        // Reducir el entorno en un 10% por cada bloque de iteraciones del 10%
        int tam_reducido = (int) (tam_base * Math.pow(0.9, num_bloques_10p));

        // Asegurar que el tamaño del entorno sea al menos 1
        return Math.max(tam_reducido, 1);
    }


    public int[] operador_dos_opt(int[] solucion) {
        int size = solucion.length;
        int i = random.nextInt(size);
        int j = random.nextInt(size);

        // Intercambiar los índices i y j
        int temp = solucion[i];
        solucion[i] = solucion[j];
        solucion[j] = temp;

        return solucion;
    }

    public Vecino funcion_eva(ArrayList<Vecino> vecinos, Vecino mejor_sol_act) {
        Vecino mejor_vecino = mejor_sol_act;
        for (Vecino vecino : vecinos) {
            if (vecino.get_coste_total() < mejor_vecino.get_coste_total()) {
                mejor_vecino = vecino;
            }
        }
        return mejor_vecino;
    }
}
