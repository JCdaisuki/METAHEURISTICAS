package Algoritmos;
import java.util.ArrayList;
import java.util.Random;

import Auxiliares.Vecino;
import ProcesadoFicheros.CreaLogs;
import ProcesadoFicheros.LectorTSP;

public class AlgTabu_Clase03_Grupo04
{
    private LectorTSP lector;
    private Random random;
    private int numIteraciones;
    private double estancamiento;
    private double mejorSolucion;
    double sinMejora;// contador para soluciones sin mejora
    private Vecino mejorGlobal;
    private long semilla;
    private ArrayList<Vecino> listaTabu = new ArrayList<Vecino>();
    private int memoria[][];
    private CreaLogs log;
    private double tamEntorno;
    private double disminucionEntorno;
    private int iteCambioEntorno;
    private int tenencia;
    private double oscilacion;
    private ArrayList<Integer> iteraciones;


    //constructor tabu
    public AlgTabu_Clase03_Grupo04(LectorTSP Lector, int Maxiteraciones, double Estancamiento, long Semilla, CreaLogs Log, double PorcientoTamEntorno, double DisminucionEntorno, int IteCambioEntorno, int Tenencia, double Oscilacion) {
        this.lector = Lector;
        this.numIteraciones = Maxiteraciones;
        this.estancamiento = Estancamiento;
        this.semilla = Semilla;
        this.random = new Random(semilla);
        this.memoria = new int[lector.getCiudades().length][lector.getCiudades().length];
        this.tamEntorno = numIteraciones * PorcientoTamEntorno;
        this.disminucionEntorno = DisminucionEntorno;
        this.iteCambioEntorno = IteCambioEntorno;
        this.tenencia = Tenencia;
        this.oscilacion = Oscilacion;
        log = Log;
        this.iteraciones = new ArrayList<>();


        int incremento = (int) (numIteraciones * (iteCambioEntorno / 100.0)); // Calcula el incremento del % en base a numIteraciones
        for (int i = incremento; i <= numIteraciones; i += incremento) {
            iteraciones.add(i); // Agrega cada múltiplo al ArrayList
        }
    }

    /**
     * @param vInicial
     * @return costeMejorSolucion ( double )
     * @Brief Ejecutor del Tabu
     */
    public double ejecutarTabu(int[] vInicial) {
        mejorGlobal = new Vecino(vInicial, lector);
        Vecino solAct = new Vecino(vInicial, lector);
        Vecino mejorLocal = new Vecino(vInicial, lector);
        sinMejora = 0; //inicializo contador de no mejoras
        int ite = 0;

        while (numIteraciones > ite) {
            if (sinMejora >= numIteraciones * estancamiento) {
                solAct = generarEquilibrado();
                mejorLocal.setVectorSol(solAct.get_vector_sol());
                comprobarMejorGlobal(solAct,ite);
                generarVecindario(solAct,mejorLocal,ite);

            } else {
                generarVecindario(solAct,mejorLocal,ite);
            }
            reducionEntorno(ite);
            ite++;
        }
        mejorSolucion = mejorGlobal.GetCosteTotal();
        return mejorSolucion;
    }


    private void reducionEntorno(int iteracion){

        if(iteraciones.contains(iteracion)){
            tamEntorno = tamEntorno * disminucionEntorno;
        }
    }

    private void generarVecindario(Vecino solAct, Vecino mejorLocal,int ite){
        for(int i = 0; i < tamEntorno ; i++){
            generarVecino(solAct);
            funcionEvaluacion(solAct,mejorLocal);
            comprobarMejorGlobal(solAct,ite);
        }
    }

    /**
     * @param solAct
     * @param mejor
      * @Brief Comprobacion de mejora en la busqueda local
     */
    private void funcionEvaluacion(Vecino solAct, Vecino mejor) {
        if (solAct.GetCosteTotal() < mejor.GetCosteTotal()) {
            mejor.setVectorSol(solAct.get_vector_sol().clone());
            updateMemoriaLargo(mejor);
        }
    }

    private void comprobarMejorGlobal(Vecino solAct, int iteracion){
        if(solAct.GetCosteTotal()<mejorGlobal.GetCosteTotal()){
            mejorGlobal.setVectorSol(solAct.get_vector_sol().clone());
            refreshMemoriaCorto();
            sinMejora = 0;
            log.aniadirEncontrado("MejorLocal en iteracion " + iteracion + ": " + solAct.GetCosteTotal() + " (Es mejor Global actual)");
        }else{
            sinMejora++;
            log.aniadirEncontrado("MejorLocal en iteracion" + iteracion + ": " + solAct.GetCosteTotal() + " (No es mejor Global)" + ", MejorGlobal = " + mejorGlobal.GetCosteTotal());

        }
    }

    /**
     * @param solAct
     * @return
     * @Brief funcion que genera el vecino cambiando dos posiciones del vector entre si
     * //hay comprobaciones previas como revisar si esta en la lista a corto plazo o tambien revisar que no sean la misma posicion
     */
    private void generarVecino(Vecino solAct) {
        int[] nuevaSolucion = solAct.get_vector_sol();
        int p2;
        int p1;

        // Generar dos índices aleatorios para intercambiar usando el Random con semilla y revisando la memoria a corto
        do {
            p1 = random.nextInt(nuevaSolucion.length);
            p2 = random.nextInt(nuevaSolucion.length);
        } while (p1 == p2 || checkMemoriaCorto(p1, p2));


        // Intercambiar las ciudades en las posiciones p1 y p2
        int temp = nuevaSolucion[p1];
        nuevaSolucion[p1] = nuevaSolucion[p2];
        nuevaSolucion[p2] = temp;

        //actualizamos memoria a corto plazo con p1 y p2
        updateMemoriaCorto(p1, p2);

        // Crear un nuevo Vecino a partir de la solución modificada
        solAct.setVectorSol(nuevaSolucion);
        solAct.CalcularCosteTotal();
    }

    /**
     * @param dere
     * @param izq
     * @Brief Actualizacion de la memoria a Corto plazo
     */
    private void updateMemoriaCorto(int dere, int izq) {
        //actualizacion corto plazo

        for (int i = 0; i < memoria.length; i++) {
            for (int j = i; j < memoria[i].length; j++) {
                if (j != i && memoria[i][j] > 0) {
                    memoria[i][j]--;
                }
            }
        }
        if (dere < izq) {
            memoria[dere][izq] = tenencia;
        } else {
            memoria[izq][dere] = tenencia;
        }
    }


    private void refreshMemoriaCorto(){
        for (int i = 0; i < memoria.length; i++) {
            for (int j = i; j < memoria[i].length; j++) {
                if (j != i && memoria[i][j] > 0) {
                    memoria[i][j] = 0;
                }
            }
        }
    }

    /**
     * @param p1
     * @param p2
     * @return
     * @Brief revision de la memoria a corto plazo para ver el uso del cambio propuesto por el aleatorio
     * //devuelve true en caso de estar en la memoria a corto plazo con mas de 1 "flag"
     */
    private boolean checkMemoriaCorto(int p1, int p2) {
        if (p1 > p2) {
            if (memoria[p2][p1] > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            if (memoria[p1][p2] > 0) {
                return true;
            } else {
                return false;
            }
        }
    }



    /**
     * @Brief Actualizacion de la Mermoria a largo plazo, sumando puntuacion en los arcos del vector incluyendo el ultimo con el primero
     * @param mejorLocal
     */
    private void updateMemoriaLargo(Vecino mejorLocal){
            //para acceder a la parte de memoria corto plazo, arriba derecha, el menor es la fila memoria[menor][mayor]
            // para acceder a la parte de memoria largo plazo, abajo derecha, el mayor es la fila memoria[mayor][menor]
            //update de la memoria a largo plazo apartir de la solucion LOCAL mejor
        for (int i = 0; i < mejorLocal.get_vector_sol().length; i++) {
            int izq = mejorLocal.get_vector_sol()[i];
            int dere;
            if( i == mejorLocal.get_vector_sol().length-1){
                 dere = mejorLocal.get_vector_sol()[0];
            }else{
                 dere = mejorLocal.get_vector_sol()[i+1];
            }

            if(dere > izq ){
                memoria[dere][izq] ++;
            }else{
                memoria[izq][dere] ++;
            }
        }
    }


    /**
     * @Brief genera un Vecino usando profundidad y anchura en su busqueda
     * @return Vecino con un vector equilibrado en anchura y profundidad
     */
    private Vecino generarEquilibrado() {
        int numRand;
        int posAct;
        int[] nuevoVector = new int[mejorGlobal.get_vector_sol().length];

        // Inicializamos el vector con -1 para evitar valores no deseados
        for(int i = 0; i < nuevoVector.length; i++) {
            nuevoVector[i] = -1;
        }

        // Recorremos el vector solución
        for (int i = 0; i < mejorGlobal.get_vector_sol().length; i++) {
            do {
                // Generamos un número aleatorio entre 0 y 100 para decidir según la oscilación
                numRand = random.nextInt(100); // 0 - 99

                if (numRand < oscilacion) { // profundidad, desplazamiento dirigido por la memoria a largo plazo
                    posAct = mejorLargoPlazo(random.nextInt(mejorGlobal.get_vector_sol().length));
                } else {  // anchura, desplazamiento aleatorizado
                    posAct = random.nextInt(mejorGlobal.get_vector_sol().length);
                }
            } while(contains(nuevoVector, posAct));  // Evitamos posiciones repetidas

            nuevoVector[i] = posAct;  // Rellenamos el vector con la posición seleccionada
        }

        return new Vecino(nuevoVector, lector);
    }

    /**
     * @Brief funcion auxiliar para encontrar el mejor candidato de una fila en desplazamiento en profundidad
     * @param fila
     * @return mejor pos
     */
    private int mejorLargoPlazo(int fila){
       int max = 0;
       int posicion = 0;
        for (int columna = 0; columna < fila; columna++) {
            if (memoria[fila][columna] > max) {
                max = memoria[fila][columna];
                posicion = columna;
            }
        }
        return posicion;
    }

    /**
     * @Brief funcion auxiliar para saber si el vector contiene un numero
     * @param v
     * @param num
     * @return
     */
    public boolean contains(int[] v, int num) {
        for (int i : v) {
            if (i == num) {
                return true;
            }
        }
        return false;
    }
}


