package Algoritmos;
import procesadoFicheros.LectorTSP;

import java.util.ArrayList;

public class BusquedaLocal {

    private int num_iteraciones_max;
    private double tam_entorno;
    private double porciento_a_subir;
    private double ratio_de_subida;

    private class vecino{
        private int[] vector_sol;
        private double coste_total;

        //funcion para conseguir almacenar dentro del atributo coste_total, el coste de viajar de una ciudad a otra dentro de la solucion del vecino.
        public void calcular_coste_total(LectorTSP lector){
            for(int i=0; i<vector_sol.length; i++){
                if(i!=0){
                    coste_total += lector.getDistancias()[i][i-1];

                }else{
                    coste_total += lector.getDistancias()[i][vector_sol.length-1];
                }
            }
        }

        public double get_coste_total(){return coste_total ;}
    }

    public BusquedaLocal(int numero_maximo_iteraciones, double tam_inicial_entorno , double porciento_a_subir_, double ratio_de_subida_) {
            num_iteraciones_max = numero_maximo_iteraciones;
            tam_entorno = tam_inicial_entorno;
            porciento_a_subir = porciento_a_subir_;
            ratio_de_subida = ratio_de_subida_;
    }
/*
    public double realizar_busqueda(){
        double mejorcoste;
        int iteraciones_actuales=0;
        vecino mejor_sol_actual ;
        ArrayList<vecino> vecinos = new ArrayList<>();
        boolean no_hay_mejor = false;
        do{



           vecino nuevo_vecino = funcion_eva(vecinos,mejor_sol_actual);
            if (nuevo_vecino != mejor_sol_actual) {
                mejor_sol_actual = nuevo_vecino;
            }else{
                no_hay_mejor = true;
            }
        }while(iteraciones_actuales<num_iteraciones_max);
        return mejorcoste;
    }
*/
    public void operador_dos_opt(){

    }

    public vecino funcion_eva(ArrayList<vecino> vecinos , vecino mejor_sol_act) {

        for(int i = 0; i < vecinos.size();i++){
            if(vecinos.get(i).get_coste_total()>mejor_sol_act.get_coste_total()){
                mejor_sol_act = vecinos.get(i);
            }
        }
        return mejor_sol_act;
    }



}
