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
    private int memoria[][];
    private CreaLogs log;
    private double tamEntorno;
    private double disminucionEntorno;
    private int iteCambioEntorno;
    private int tenencia;
    private ArrayList<Integer> iteraciones;
    private Vecino mejor_momento_actual; // NUEVO: Variable para almacenar la mejor solución en la iteración actual


    //constructor tabu
    public AlgTabu_Clase03_Grupo04(LectorTSP Lector, int Maxiteraciones, double Estancamiento, double PorcientoTamEntorno, double DisminucionEntorno, int IteCambioEntorno, int Tenencia) {
        this.lector = Lector;
        this.numIteraciones = Maxiteraciones;
        this.estancamiento = Estancamiento;
        this.random = new Random(semilla);
        this.memoria = new int[lector.getCiudades().length][lector.getCiudades().length];
        this.tamEntorno = numIteraciones * PorcientoTamEntorno;
        this.disminucionEntorno = DisminucionEntorno;
        this.iteCambioEntorno = IteCambioEntorno;
        this.tenencia = Tenencia;
        this.iteraciones = new ArrayList<>();


        int incremento = (int) (numIteraciones * (iteCambioEntorno / 100.0)); // Calcula el incremento del % en base a numIteraciones
        for (int i = incremento; i <= numIteraciones; i += incremento) {
            iteraciones.add(i); // Agrega cada múltiplo al ArrayList
        }
    }

    public void SetSemilla(long semilla)
    {
        this.semilla = semilla;
    }

    public void SetLog(CreaLogs log)
    {
        this.log = log;
    }

    /**
     * @param vInicial
     * @Brief Ejecutor del Tabu
     */
    public void ejecutarTabu(int[] vInicial) {
        mejorGlobal = new Vecino(vInicial, lector);
        Vecino solAct = new Vecino(vInicial, lector);
        Vecino mejorLocal = new Vecino(vInicial, lector);
        mejor_momento_actual = new Vecino(vInicial, lector); // Inicializar mejor_momento_actual
        sinMejora = 0; // Inicializar contador de no mejoras
        int ite = 0;
        estancamiento = (int) (numIteraciones * 0.05); // 5% de movimientos de empeoramiento

        while (numIteraciones > ite)
        {
            // Si el algoritmo se ha estancado, generar una nueva solución aleatoria
            if (sinMejora >= estancamiento)
            {
                solAct = generarSolucionGreedy(); // Generar nueva solución inicial
                mejorLocal.setVectorSol(solAct.get_vector_sol());
                sinMejora = 0; // Resetear el contador de estancamiento
                mejor_momento_actual.setVectorSol(solAct.get_vector_sol());
                comprobarMejorGlobal(solAct, ite);
            }

            // Evaluar el vecindario
            generarVecindario(solAct, mejorLocal, ite);

            // Actualizar el mejor momento de la iteración
            if (mejorLocal.GetCosteTotal() < mejor_momento_actual.GetCosteTotal()) {
                mejor_momento_actual.setVectorSol(mejorLocal.get_vector_sol().clone());
            } else {
                // Incrementar el contador de movimientos de empeoramiento
                sinMejora++;
            }

            reducionEntorno(ite);
            ite++;
        }
        mejorSolucion = mejorGlobal.GetCosteTotal();
    }

    private Vecino generarSolucionGreedy() {
        int[] nuevoVector = new int[mejorGlobal.get_vector_sol().length];
        // Lógica para generar una solución greedy
        boolean[] visitado = new boolean[nuevoVector.length];
        nuevoVector[0] = random.nextInt(nuevoVector.length); // Seleccionar aleatoriamente la primera ciudad
        visitado[nuevoVector[0]] = true;

        for (int i = 1; i < nuevoVector.length; i++) {
            int mejorCiudad = -1;
            double mejorDistancia = Double.MAX_VALUE;
            for (int j = 0; j < nuevoVector.length; j++) {
                if (!visitado[j] && lector.getDistancia(nuevoVector[i - 1], j) < mejorDistancia) {
                    mejorDistancia = lector.getDistancia(nuevoVector[i - 1], j);
                    mejorCiudad = j;
                }
            }
            nuevoVector[i] = mejorCiudad;
            visitado[mejorCiudad] = true;
        }
        return new Vecino(nuevoVector, lector);
    }


    public double GetMejorSolucion()
    {
        return mejorSolucion;
    }

    private void reducionEntorno(int iteracion){

        if(iteraciones.contains(iteracion)){
            tamEntorno = tamEntorno * disminucionEntorno;
        }
    }

    private void generarVecindario(Vecino solAct, Vecino mejorLocal, int ite) {
        mejor_momento_actual = new Vecino(solAct.get_vector_sol(), lector); // Resetear mejor_momento_actual
        for (int i = 0; i < tamEntorno; i++) {
            generarVecino(solAct);
            funcionEvaluacion(solAct, mejorLocal);

            // Actualizar mejor_momento_actual si la solución actual es mejor que la mejor_momento_actual
            if (solAct.GetCosteTotal() < mejor_momento_actual.GetCosteTotal()) {
                mejor_momento_actual.setVectorSol(solAct.get_vector_sol().clone());
            }
            comprobarMejorGlobal(solAct, ite);
        }
        // Moverse a mejor_momento_actual aunque sea un movimiento de empeoramiento
        solAct.setVectorSol(mejor_momento_actual.get_vector_sol().clone());
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
        if(solAct.GetCosteTotal()<mejorGlobal.GetCosteTotal())
        {
            mejorGlobal.setVectorSol(solAct.get_vector_sol().clone());
            refreshMemoriaCorto();
            sinMejora = 0;
        }
        else
        {
            sinMejora++;
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
        for (int i = 0; i < memoria.length; i++) {
            for (int j = i; j < memoria[i].length; j++) {
                if (j != i && memoria[i][j] > 0) {
                    memoria[i][j]--;
                }
            }
        }
        if (dere < izq) {
            memoria[dere][izq] = (sinMejora > numIteraciones * 0.1) ? tenencia / 2 : tenencia; // Reducir tenencia en caso de empeoramiento
        } else {
            memoria[izq][dere] = (sinMejora > numIteraciones * 0.1) ? tenencia / 2 : tenencia;
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
}