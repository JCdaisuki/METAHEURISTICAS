package Algoritmos;
import java.util.ArrayList;
import java.util.Random;
import procesadoFicheros.LectorTSP;

public class Tabu {

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
    private Vecino mejorVecino;
    private long semilla;
    private int k;
    private int sinMejora;
    private int tamVecindario;
    private ArrayList<Vecino> listaTabu = new ArrayList<Vecino>();
    private int maxTamanoTabu;


    //constructor tabu
    public Tabu(LectorTSP Lector, int Maxiteraciones, double EmpeoramientoPermitido,Random r, int K,long Semilla){
        this.lector = Lector;
        this.numIteraciones = Maxiteraciones;
        this.empeoramientoPermitido = EmpeoramientoPermitido;
        this.k = K;
        this.semilla = Semilla;
        this.random = new Random(semilla);
    }


    public double ejecutarTabu(int[] inicialS){
         mejorVecino = new Vecino(inicialS);
        Vecino solAct = new Vecino(inicialS);
        sinMejora = 0;  // contador para soluciones sin mejora
        int ite=0;
        while(numIteraciones>ite){


            if(sinMejora>=numIteraciones*empeoramientoPermitido){
                Greedy greedy = new Greedy(); //preparamos el greedy para ser realizado de nuevo
                solAct.setVectorSol(greedy.RealizarGreedy(k,semilla,lector)); // se lanza el greedy para intentar encontrar mejor sol por empeoramiento
                sinMejora = 0;
                hayMejora(solAct);

            }else{
                //TODO busqueda de tabu

                hayMejora(solAct);
            }

            ite++;
        }
        return mejorSolucion;
    }



    //funcion que se encarga de revisar si hay mejora o no en la ejecucion, en caso de no haber solucion aumenta el contador de empeoramiento
    private void hayMejora(Vecino solAct) {
        if(solAct.getCosteTotal() < mejorSolucion){
            sinMejora=0;
            mejorVecino=solAct;
            mejorSolucion = mejorVecino.getCosteTotal();
        }else{
            sinMejora++;
        }
    }

    //sino encuentra mejor vecino devuelve el mejor vecino previo, por lo que si al devolver es distinto al mejor vecino significa que es mejor
    private Vecino buscarMejorVecino(int tamVecindario,Vecino solAct) {
        Vecino mejorVecinoLocal = mejorVecino;

        for (int j = 0; j < tamVecindario; j++) {
            Vecino vecino = generarVecino(solAct);  // Genera un nuevo vecino

            // Verifica que no esté en la lista tabú antes de considerarlo
            if (!esTabu(vecino) && vecino.getCosteTotal() < mejorVecino.getCosteTotal()) {
                mejorVecinoLocal = vecino;
            }
        }

        return mejorVecinoLocal;  // Retorna el mejor vecino encontrado o el mejor anterior en caso de no encontrar mejor
    }
    private Vecino generarVecino(Vecino solAct) {
        int[] nuevaSolucion = solAct.get_vector_sol().clone();

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
    private boolean esTabu(Vecino solucion) {
        for (Vecino solTabu : listaTabu) {
            if (solucion == solTabu) {   //TODO revisar si hay que sobrecargar el operador == de los Vecinos
                return true;  // La solución está en la lista tabú
            }
        }
        return false;  // La solución no está en la lista tabú
    }
    private void actualizarTabu() {

    }


}


