package Algoritmos;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import ProcesadoFicheros.CreaLogs;
import ProcesadoFicheros.LectorTSP;

public class AlgTabu_Clase03_Grupo04 {

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

        public double getCosteTotal()
        {
            return costeTotal;
        }
        public int[] get_vector_sol()
        {
            return vectorSol;
        }
        public void setVectorSol(int[] sol){vectorSol=sol;}
    }

    private LectorTSP lector;
    private Random random;
    private int numIteraciones;
    private double empeoramientoPermitido;
    private double mejorSolucion;
    double sinMejora ;// contador para soluciones sin mejora
    private Vecino mejorGlobal;
    private long semilla;
    private ArrayList<Vecino> listaTabu = new ArrayList<Vecino>();
    private int memoria[][];
    private CreaLogs log;


    //constructor tabu
    public AlgTabu_Clase03_Grupo04(LectorTSP Lector, int Maxiteraciones, double EmpeoramientoPermitido, long Semilla, CreaLogs Log){
        this.lector = Lector;
        this.numIteraciones = Maxiteraciones;
        this.empeoramientoPermitido = EmpeoramientoPermitido;
        this.semilla = Semilla;
        this.random = new Random(semilla);
        this.memoria = new int[lector.getCiudades().length][lector.getCiudades().length];
        log = Log;
    }

    /**
     * @Brief Ejecutor del Tabu
     * @param vInicial
     * @return costeMejorSolucion ( double )
     */
    public double ejecutarTabu(int[] vInicial){
         mejorGlobal = new Vecino(vInicial);
        Vecino solAct = new Vecino(vInicial);
        Vecino mejorLocal = new Vecino(vInicial);
        sinMejora = 0 ; //inicializo contador de no mejoras
        int ite=0;

        while(numIteraciones>ite){
            if(sinMejora>=numIteraciones*empeoramientoPermitido){
                solAct = generarEquilibrado();
                mejorLocal = solAct;
                hayMejora(solAct, mejorGlobal, ite,true);

            }else{
                generarVecino(solAct);
                hayMejora(solAct,mejorLocal,ite,false);
                if (solAct.getCosteTotal() == mejorLocal.getCosteTotal()) {//si el local es el mismo que el mejor local significa que hay cambios
                                            // y hay que comprobar si estamos ante un proximo mejor global
                    hayMejora(mejorLocal,mejorGlobal,ite,true);
                }

            }

            ite++;
        }
        mejorSolucion = mejorGlobal.getCosteTotal();
        return mejorSolucion;
    }

    /**
     * @Brief Comprobacion de mejora en la busqueda local
     * @param solAct
     * @param mejor
     */
    private void hayMejora(Vecino solAct, Vecino mejor, int iteracion,boolean introducirLog) {
        if(solAct.getCosteTotal() < mejor.getCosteTotal()){
            mejor.setVectorSol(solAct.get_vector_sol().clone());
            mejor.CalcularCosteTotal();
            updateMemoriaLargo(mejor);
            if(introducirLog){
                log.aniadirEncontrado("MejorLocal en iteracion " + iteracion + ": " + solAct.getCosteTotal() + " (Es mejor Global actual)");
                sinMejora = 0;
            }

        }else{
            sinMejora++;
            if(introducirLog){
                log.aniadirEncontrado("MejorLocal en iteracion" + iteracion + ": " + solAct.getCosteTotal() + " (No es mejor Global)" + ", MejorGlobal = " + mejorGlobal.getCosteTotal());
            }

        }
    }

    /**
     * @Brief funcion que genera el vecino cambiando dos posiciones del vector entre si
     * //hay comprobaciones previas como revisar si esta en la lista a corto plazo o tambien revisar que no sean la misma posicion
     * @param solAct
     * @return
     */
    private void generarVecino(Vecino solAct) {
        int[] nuevaSolucion = solAct.get_vector_sol();
        int p2;
        int p1;

        // Generar dos índices aleatorios para intercambiar usando el Random con semilla y revisando la memoria a corto
            p1 = random.nextInt(nuevaSolucion.length);
            do {
                p2 = random.nextInt(nuevaSolucion.length);
            } while (p1 == p2 || checkMemoriaCorto(p1,p2));



        // Intercambiar las ciudades en las posiciones p1 y p2
        int temp = nuevaSolucion[p1];
        nuevaSolucion[p1] = nuevaSolucion[p2];
        nuevaSolucion[p2] = temp;

        //actualizamos memoria a corto plazo con p1 y p2
        updateMemoriaCorto(p1,p2);

        // Crear un nuevo Vecino a partir de la solución modificada
        solAct.setVectorSol(nuevaSolucion);
        solAct.CalcularCosteTotal();
    }

    /**
     * @Brief Actualizacion de la memoria a Corto plazo
     * @param dere
     * @param izq
     */
    private void updateMemoriaCorto(int dere, int izq){
        //actualizacion corto plazo

        for(int i = 0; i < memoria.length ; i++ ){
            for(int j = i; j < memoria[i].length ; j++ ){
                if( j != i){
                    if(memoria[i][j] > 0){
                        memoria[i][j] --;
                    }
                }
            }
        }
        if(dere > izq ){
            memoria[dere][izq] = 10;
        }else{
            memoria[izq][dere] = 10;
        }
    }

    /**
     * @Brief revision de la memoria a corto plazo para ver el uso del cambio propuesto por el aleatorio
     *     //devuelve true en caso de estar en la memoria a corto plazo con mas de 1 "flag"
     * @param p1
     * @param p2
     * @return
     */
    private boolean checkMemoriaCorto(int p1, int p2){
        if(p1>p2){
            if(memoria[p2][p1] > 0){
                return true;
            }else{
                return false;
            }
        }else{
            if(memoria[p1][p2] > 0){
                return true;
            }else{
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
    private Vecino generarEquilibrado(){
        int numRand;
        int posAct;
        int[] nuevoVector = new int[mejorGlobal.get_vector_sol().length];
        for(int i  = 0 ; i < nuevoVector.length ; i++){
            nuevoVector[i] = -1;
        }
        for (int i = 0; i < mejorGlobal.get_vector_sol().length ; i++){
            do {
                numRand = random.nextInt(2) + 1; // Genera 1 o 2
            if (numRand == 1) { // profundidad , desplazamiento dirigido por la memoria a largo plazo
                   posAct = mejorLargoPlazo(random.nextInt(mejorGlobal.get_vector_sol().length));
            }else{  // anchura, desplazamiento aleatorizado
                    posAct = random.nextInt(mejorGlobal.get_vector_sol().length);
            }
            }while( contains(nuevoVector, posAct));  // evitamos repetidos

            nuevoVector[i] = posAct;  //el vector se rellena con la posicion conseguida por la busqueda de profundidad o anchura
        }

        return new Vecino(nuevoVector);
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
































/*
 //sino encuentra mejor vecino devuelve el mejor vecino previo, por lo que si al devolver es distinto al mejor vecino significa que es mejor
    private Vecino buscarMejorVecino(int tamVecindario,Vecino solAct) {
        Vecino mejorVecinoLocal = mejorGlobal;

        for (int j = 0; j < tamVecindario; j++) {
            Vecino vecino = generarVecino(solAct);  // Genera un nuevo vecino

            // Verifica que no esté en la lista tabú antes de considerarlo
            if (!esTabu(vecino) && vecino.getCosteTotal() < mejorGlobal.getCosteTotal()) {
                mejorVecinoLocal = vecino;
            }
        }

        return mejorVecinoLocal;  // Retorna el mejor vecino encontrado o el mejor anterior en caso de no encontrar mejor
    }
    private boolean esTabu(Vecino solucion) {
        for (Vecino solTabu : listaTabu) {

            if (Arrays.equals(solucion.get_vector_sol(), solTabu.get_vector_sol())) {
                return true;  // La solucion está en la lista tabú
            }
        }
        return false;  // La solucion no está en la lista tabú
    }

 */

}


