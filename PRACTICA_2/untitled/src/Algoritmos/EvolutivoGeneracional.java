package Algoritmos;

import Auxiliares.GreedyAleatorizado;
import Auxiliares.Individuo;
import ProcesadoFicheros.LectorTSP;
import java.util.ArrayList;
import java.util.Random;

public class EvolutivoGeneracional
{
    private int tamPoblacion;
    private double porcientoGeneracion; // reflejada en ejemplo 0.7 = 70 %
    private int tamCandidatosGreedy ;
    private int kbest ;
    private int kworst;
    private double probCruce; // reflejada en ejemplo 0.7 = 70 %
    private double prob2opt; // reflejada en ejemplo 0.7 = 70 %
    private int maxComrobacion;
    private double maxTiempo;
    private Random random;
    private LectorTSP lector;
    private ArrayList<Individuo> generacionActual;
    private int cantidadElite;
    private ArrayList<Individuo> elites;
    private long semilla;


    public EvolutivoGeneracional(int tamPoblacion, double porcientoGeneracion, int tamCandidatosGreedy,int CantidadElites, int kbest, int kworst,
                                 double probCruce, double prob2opt, int maxComrobacion, double maxTiempo, long seed, LectorTSP Lector)
    {
        this.tamPoblacion = tamPoblacion;
        this.porcientoGeneracion = porcientoGeneracion;
        this.tamCandidatosGreedy = tamCandidatosGreedy;
        this.kbest = kbest;
        this.kworst = kworst;
        this.probCruce = probCruce;
        this.prob2opt = prob2opt;
        this.maxComrobacion = maxComrobacion;
        this.maxTiempo = maxTiempo;
        this.semilla = seed;
        this.lector = Lector;
        this.random = new Random(seed);
        this.cantidadElite = CantidadElites;
        excepciones();  // llamada a la funcion excepciones para comprobar que todos los datos pasados esten yendo bien

    }




    public void ejecutarGeneracional()
    {
        long tiempoInicio = System.currentTimeMillis();
        int comprobaciones = 0;
        inicializacion();
        seleccionarElites();
        while ((System.currentTimeMillis() - tiempoInicio) / 1000.0 < maxTiempo && comprobaciones < maxComrobacion) {

            ArrayList<Individuo> postTorneo = seleccionTorneoGeneralizado();
            ArrayList<Individuo> hijos = cruce(postTorneo);




            seleccionarElites();
            dosOpt();
            comprobaciones++;
        }

    }


    private int[] generacionAleatoria()
    {
        int numCiudades = lector.getCiudades().length;
        int[] vector = new int[numCiudades];

        for (int i = 0; i < numCiudades; i++) {
            vector[i] = i;
        }

        Random random = new Random();
        for (int i = numCiudades - 1; i > 0; i--)
        {
            int j = random.nextInt(i + 1);
            int temp = vector[i];
            vector[i] = vector[j];
            vector[j] = temp;
        }

        return vector;
    }


    private void inicializacion()
    {
        generacionActual.clear();  //limpiar el vector de la poblacion
        for(int tamAct = 0; tamAct < tamPoblacion; tamAct++)
        {
            double probAleatoria = random.nextDouble();
            Individuo nuevoIndividuo = new Individuo(lector);

            if (probAleatoria < porcientoGeneracion)
            {
                nuevoIndividuo.setVectorSol(generacionAleatoria());
            }
            else
            {
                GreedyAleatorizado greedy = new GreedyAleatorizado();
                nuevoIndividuo = greedy.RealizarGreedy(tamCandidatosGreedy, semilla, lector);
            }

            generacionActual.add(nuevoIndividuo);
        }
    }


    private void dosOpt()
    {
        for(int indi = 0; indi < tamPoblacion; indi++)
        {
            if(random.nextDouble() < prob2opt)
            {
                int[] nuevoVector = generacionActual.get(indi).get_vector_sol();
                int i = random.nextInt(nuevoVector.length - 1);
                int j = random.nextInt(nuevoVector.length - 1);

                while (i == j)
                {
                    j = random.nextInt(nuevoVector.length - 1);
                }

                int aux = nuevoVector[i];
                nuevoVector[i] = nuevoVector[j];
                nuevoVector[j] = aux;
                generacionActual.get(indi).setVectorSol(nuevoVector);
            }
        }
    }

    private void seleccionarElites()
    {
        generacionActual.sort((ind1, ind2) -> Double.compare(ind1.GetCosteTotal(), ind2.GetCosteTotal()));

        elites.clear();

        // Añadir los mejores "cantidadElite" individuos a la lista de élites
        for (int i = 0; i < cantidadElite && i < generacionActual.size(); i++)
        {
            elites.add(generacionActual.get(i));
        }

        //TODO como idea guardar en el log algunos elites que se
        // generan ( 5% o menos ya que tenemos el .tsp grande que
        // puede generar un retraso bastante importante en la ejecucion
        // si se rellena con demasiados elites)
    }

    private ArrayList<Individuo> seleccionTorneoGeneralizado()
    {
        ArrayList<Individuo> nuevaPoblacion = new ArrayList<>();

        for (int i = 0; i < tamPoblacion; i++)
        {
            ArrayList<Individuo> torneo = new ArrayList<>();

            for (int j = 0; j < kbest; j++)
            {
                int indiceAleatorio = random.nextInt(generacionActual.size());
                torneo.add(generacionActual.get(indiceAleatorio));
            }

            Individuo mejorIndividuo = torneo.get(0);

            for (Individuo indi : torneo)
            {
                if (indi.GetCosteTotal() < mejorIndividuo.GetCosteTotal())
                {
                    mejorIndividuo = indi;
                }
            }
            nuevaPoblacion.add(mejorIndividuo);
        }

        return nuevaPoblacion;
    }

    private ArrayList<Individuo> cruce (ArrayList<Individuo> padres)
    {
      ArrayList<Individuo> hijos = new ArrayList<>();

      for(int cru = 0; cru < tamPoblacion/2; cru++)
      {
          ArrayList<Individuo> nuevoshijos = new ArrayList<Individuo>();
          int pos1 = random.nextInt(0,tamPoblacion-1);
          int pos2;

          do
          {
              pos2 = random.nextInt(0,tamPoblacion-1);
          }while(pos1 == pos2);

          Individuo p1 = padres.get(pos1);
          Individuo p2 = padres.get(pos2);

          if(random.nextDouble() < probCruce)
          {
              nuevoshijos.add(ox2(p1,p2));
              nuevoshijos.add(ox2(p2,p1));
          }
          else
          {
                nuevoshijos = moc(p1,p2);
          }

          hijos.add(nuevoshijos.get(0));
          hijos.add(nuevoshijos.get(1));
        }

      return hijos;
    }

    private Individuo ox2(Individuo p1, Individuo p2)
    {
        Individuo hijo = new Individuo(lector);
        int[] v1 = p1.get_vector_sol().clone();
        int[] v2 = p2.get_vector_sol().clone();
        int cantidadIntercambiada = random.nextInt(1,lector.getCiudades().length/4);

        //Creamos una posicion aleatoria para hacer el cambio, pero revisamos que no nos saldremos por la derecha.
        int principioCambio = random.nextInt(0,lector.getCiudades().length - lector.getCiudades().length/4);

        ArrayList<Integer> noCopiar = new ArrayList<Integer>();

        for(int pos = principioCambio; pos < principioCambio+cantidadIntercambiada ; pos++)
        {
            noCopiar.add(v1[pos]);
        }

        ArrayList<Integer> newVector = new ArrayList<>();
        int movimientoAux = -1;

        for( int i = 0; i < lector.getCiudades().length ; i++)
        {
            if(i > principioCambio && i < principioCambio+cantidadIntercambiada )
            {
                newVector.add(v1[i]);
            }
            else
            {
                int indice;

                do
                {
                    movimientoAux++;
                    indice = v2[movimientoAux];
                }while(noCopiar.contains(indice) || newVector.contains(indice));
            }
        }

        hijo.setVectorSol(newVector);
        //TODO revisar cuantos elementos dentro de cada padre hay que coger,
        // actualmente se copian de uno a otro de 1 a 1/4 de los elementos
    return hijo;
    }

    private ArrayList<Individuo> moc(Individuo p1, Individuo p2)
    {
        ArrayList<Individuo> hijos = new ArrayList<>();


        //TODO
        return hijos;
    }






    private void excepciones() {
        if (tamPoblacion <= 0) {
            throw new IllegalArgumentException("Error: El tamaño de la población debe ser mayor que 0.");
        }

        if (kbest > tamPoblacion) {
            throw new IllegalArgumentException("Error: kbest no puede ser mayor que el tamaño de la población.");
        }

        if (kworst > tamPoblacion) {
            throw new IllegalArgumentException("Error: kworst no puede ser mayor que el tamaño de la población.");
        }

        if (porcientoGeneracion < 0 || porcientoGeneracion > 1) {
            throw new IllegalArgumentException("Error: El porcentaje de generación debe estar entre 0 y 1.");
        }

        if (probCruce < 0 || probCruce > 1) {
            throw new IllegalArgumentException("Error: La probabilidad de cruce debe estar entre 0 y 1.");
        }

        if (prob2opt < 0 || prob2opt > 1) {
            throw new IllegalArgumentException("Error: La probabilidad de mutación 2-opt debe estar entre 0 y 1.");
        }

        if (maxComrobacion <= 0) {
            throw new IllegalArgumentException("Error: El máximo de comprobaciones debe ser mayor que 0.");
        }

        if (maxTiempo <= 0) {
            throw new IllegalArgumentException("Error: El tiempo máximo debe ser mayor que 0.");
        }

    }

}
