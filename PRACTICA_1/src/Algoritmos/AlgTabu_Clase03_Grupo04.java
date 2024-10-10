package Algoritmos;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
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
    private int empeoramientoPermitido;
    private double mejorSolucion;
    private Vecino mejorGlobal;
    private long semilla;
    private int k;
    private int sinMejora;
    private int tamVecindario;
    private ArrayList<Vecino> listaTabu = new ArrayList<Vecino>();
    private int maxTamanoTabu;
    private int memoria[][];


    //constructor tabu
    public AlgTabu_Clase03_Grupo04(LectorTSP Lector, int Maxiteraciones, int EmpeoramientoPermitido, Random r, int K, long Semilla){
        this.lector = Lector;
        this.numIteraciones = Maxiteraciones;
        this.empeoramientoPermitido = EmpeoramientoPermitido;
        this.k = K;
        this.semilla = Semilla;
        this.random = new Random(semilla);
        this.memoria = new int[lector.getCiudades().length][lector.getCiudades().length];
    }


    public double ejecutarTabu(int[] inicialS){
         mejorGlobal = new Vecino(inicialS);
        Vecino solAct = new Vecino(inicialS);
        Vecino mejorLocal = new Vecino(inicialS);
        sinMejora = 0;  // contador para soluciones sin mejora
        int ite=0;

        while(numIteraciones>ite){
            if(sinMejora>=numIteraciones*empeoramientoPermitido){
                AlgGreedy_Clase03_Grupo04 greedy = new AlgGreedy_Clase03_Grupo04(); //preparamos el greedy para ser realizado de nuevo
                solAct.setVectorSol(greedy.RealizarGreedy(k,semilla,lector)); // se lanza el greedy para intentar encontrar mejor sol por empeoramiento
                hayMejora(mejorLocal, mejorGlobal);
                mejorSolucion = mejorGlobal.getCosteTotal();
            }else{
                //TODO tabu

                hayMejora(solAct,mejorLocal);
            }

            ite++;
        }
        return mejorSolucion;
    }

    //funcion que va a tener dos usos, el primero es que si la busqueda en la zona local no encuentra mejoras para ver si es ya mejorLocal mejor que Gobal
    //lo segundo es que tambien se usa para ver que tan bien esta yendo la busqueda dentro de la zona local

    private void hayMejora(Vecino solAct, Vecino mejor) {
        if(solAct.getCosteTotal() < mejor.costeTotal){
            mejor = solAct;
        }else{
            sinMejora++;
        }
    }


    private Vecino generarVecino(Vecino solAct) {
        int[] nuevaSolucion = solAct.get_vector_sol();

        // Generar dos índices aleatorios para intercambiar usando el Random con semilla
        int p1 = random.nextInt(nuevaSolucion.length);
        int p2;
        do {
            p2 = random.nextInt(nuevaSolucion.length);
        } while (p1 == p2);

        // Intercambiar las ciudades en las posiciones p1 y p2
        int temp = nuevaSolucion[p1];
        nuevaSolucion[p1] = nuevaSolucion[p2];
        nuevaSolucion[p2] = temp;

        // Crear un nuevo Vecino a partir de la solución modificada
        return new Vecino(nuevaSolucion);
    }




    private void actualizarTabu() {

    }

    private void updateMemoria(int p1, int p2, Vecino mejorLocal){
            //para acceder a la parte de memoria corto plazo, arriba derecha, el menor es la fila memoria[menor][mayor]
            // para acceder a la parte de memoria largo plazo, abajo derecha, el mayor es la fila memoria[mayor][menor]


            //TODO hay que separar esta funcion para que se realice en cada momento
        // la memoria corto y largo plazo se actualiza cada vez que se hace un cambio mejore o no ademas de prevenir cambios futuros entre indices



        //actualizacion corto plazo

        for(int i = 0; i < memoria.length ; i++ ){
            for(int j = i; j < memoria[i].length ; j++ ){
                if( j != i){
                    memoria[i][j] --;
                }
            }
        }

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
     * @Brief Funcion auxiliar para comprobar si el cambio es tabu en la memoria a corto plazo
     * @param p1
     * @param p2
     * @return
     */
    private boolean cambioTabu(int p1 , int p2){

        if( p1 > p2 ){
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


