package Algoritmos;

import Auxiliares.GreedyAleatorizado;
import Auxiliares.Individuo;
import ProcesadoFicheros.LectorTSP;
import java.util.ArrayList;
import java.util.Random;

public class AlgEvolutivoGeneracional_Clase03_Grupo04
{
    private int tamPoblacion;
    private double porcientoGeneracion;
    private int tamCandidatosGreedy;
    private int kbest;
    private int kworst;
    private double probCruce;
    private double prob2opt;
    private int maxEvaluaciones;
    private double maxTiempo;
    private Random random;
    private LectorTSP lector;
    private ArrayList<Individuo> generacionActual;
    private int cantidadElite;
    private ArrayList<Individuo> elites;
    private long semilla;
    private int evaluaciones;

    public AlgEvolutivoGeneracional_Clase03_Grupo04(int tamPoblacion, double porcientoGeneracion, int tamCandidatosGreedy, int cantidadElites,
                                                    int kbest, int kworst, double probCruce, double prob2opt, int maxEvaluacion, double maxTiempo)
    {
        this.tamPoblacion = tamPoblacion;
        this.porcientoGeneracion = porcientoGeneracion;
        this.tamCandidatosGreedy = tamCandidatosGreedy;
        this.kbest = kbest;
        this.kworst = kworst;
        this.probCruce = probCruce;
        this.prob2opt = prob2opt;
        this.maxEvaluaciones = maxEvaluacion;
        this.maxTiempo = maxTiempo;
        this.cantidadElite = cantidadElites;
        this.generacionActual = new ArrayList<>();
        this.elites = new ArrayList<>();
        excepcionesInicializacion();
    }

    public void ejecutarGeneracional(long seed, LectorTSP lector)
    {
        this.semilla = seed;
        this.random = new Random(seed);
        this.lector = lector;

        long tiempoInicio = System.currentTimeMillis();
        evaluaciones = 0;
        inicializacion();

        while ((System.currentTimeMillis() - tiempoInicio) < maxTiempo && evaluaciones < maxEvaluaciones)
        {
            seleccionarElites();

            ArrayList<Individuo> postTorneo = seleccionTorneoGeneralizado();
            ArrayList<Individuo> hijos = cruce(postTorneo);

            remplazamiento(hijos);
            dosOpt();
        }
    }

    private void remplazamiento(ArrayList<Individuo> hijos )
    {
        generacionActual.clear();
        generacionActual.addAll(hijos);
        ArrayList<Individuo> elitesPermanecen = new ArrayList<>();

        for(int i = 0; i < cantidadElite ; i++)
        {
            if(!generacionActual.contains(elites.get(i)))
            {
                elitesPermanecen.add(elites.get(i));
            }
        }

        torneokworst(elitesPermanecen);
    }

    private void torneokworst(ArrayList<Individuo> permanecen)
    {
        double peor;

        for(int i =0; i<permanecen.size() ; i++)
        {
            peor = Double.MIN_VALUE;
            for (int j = 0;j < kbest; j++)
            {
                int r = random.nextInt(0, tamPoblacion);
                evaluaciones++;

                if (peor < generacionActual.get(j).getCosteTotal())
                {
                    peor = generacionActual.get(j).getCosteTotal();

                }
            }
            generacionActual.get(i).setVectorSol(permanecen.get(i).get_vector_sol());
        }

    }

    private int[] generacionAleatoria()
    {
        int numCiudades = lector.getCiudades().length;
        int[] vector = new int[numCiudades];

        for (int i = 0; i < numCiudades; i++)
        {
            vector[i] = i;
        }

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
        generacionActual.clear();

        for (int tamAct = 0; tamAct < tamPoblacion; tamAct++)
        {
            double probAleatoria = random.nextDouble();
            Individuo nuevoIndividuo = new Individuo(lector);

            if (probAleatoria < porcientoGeneracion)   //Diversificacion
            {
                nuevoIndividuo.setVectorSol(generacionAleatoria());
            }
            else                                       //Intensificacion
            {
                GreedyAleatorizado greedy = new GreedyAleatorizado();
                nuevoIndividuo = greedy.RealizarGreedy(tamCandidatosGreedy, semilla, lector);
            }

            generacionActual.add(nuevoIndividuo);
        }
    }

    private void dosOpt()
    {
        for (int indi = 0; indi < tamPoblacion; indi++)
        {
            if (random.nextDouble() < prob2opt)
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
        generacionActual.sort((ind1, ind2) -> Double.compare(ind1.getCosteTotal(), ind2.getCosteTotal()));
        elites.clear();

        for (int i = 0; i < cantidadElite && i < generacionActual.size(); i++)
        {
            elites.add(generacionActual.get(i));
        }
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
                if (indi.getCosteTotal() < mejorIndividuo.getCosteTotal())
                {
                    mejorIndividuo = indi;
                    evaluaciones++;
                }
            }
            nuevaPoblacion.add(mejorIndividuo);
        }

        return nuevaPoblacion;
    }

    private ArrayList<Individuo> cruce(ArrayList<Individuo> padres)
    {
        ArrayList<Individuo> hijos = new ArrayList<>();

        for (int cru = 0; cru < tamPoblacion / 2; cru++)
        {
            ArrayList<Individuo> nuevosHijos = new ArrayList<>();
            int pos1 = random.nextInt(tamPoblacion);
            int pos2;

            do
            {
                pos2 = random.nextInt(tamPoblacion);
            }
            while (pos1 == pos2);

            Individuo p1 = padres.get(pos1);
            Individuo p2 = padres.get(pos2);

            if (random.nextDouble() < probCruce)
            {
                nuevosHijos.add(ox2(p1, p2));
                nuevosHijos.add(ox2(p2, p1));
            }
            else
            {
                nuevosHijos = moc(p1, p2);
            }

            hijos.add(nuevosHijos.get(0));
            hijos.add(nuevosHijos.get(1));
        }
        return hijos;
    }

    private Individuo ox2(Individuo p1, Individuo p2)
    {
        Individuo hijo = new Individuo(lector);
        int[] v1 = p1.get_vector_sol().clone();
        int[] v2 = p2.get_vector_sol().clone();
        int cantidadIntercambiada = random.nextInt(1, lector.getCiudades().length / 4);
        int principioCambio = random.nextInt(lector.getCiudades().length - lector.getCiudades().length / 4);

        ArrayList<Integer> noCopiar = new ArrayList<>();
        for (int pos = principioCambio; pos < principioCambio + cantidadIntercambiada; pos++)
        {
            noCopiar.add(v1[pos]);
        }

        ArrayList<Integer> newVector = new ArrayList<>();
        int movimientoAux = -1;
        for (int i = 0; i < lector.getCiudades().length; i++)
        {
            if (i >= principioCambio && i < principioCambio + cantidadIntercambiada)
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
                }
                while (noCopiar.contains(indice) || newVector.contains(indice));

                newVector.add(indice);
            }
        }

        hijo.setVectorSol(newVector.stream().mapToInt(Integer::intValue).toArray());

        return hijo;
    }

    private ArrayList<Individuo> moc(Individuo p1, Individuo p2)
    {
        ArrayList<Individuo> hijos = new ArrayList<>();
        Individuo hijo1 = new Individuo(lector);
        Individuo hijo2 = new Individuo(lector);
        cruceMOC(p1.get_vector_sol(), p2.get_vector_sol(), hijo1, hijo2);
        hijos.add(hijo1);
        hijos.add(hijo2);
        return hijos;
    }

    private void cruceMOC(int[] p1, int[] p2, Individuo hijo1, Individuo hijo2)
    {
        int tam = p1.length;
        int puntoCruce = random.nextInt(tam - 1) + 1;
        ArrayList<Integer> aux = new ArrayList<>();

        for (int i = 0; i < puntoCruce; i++)
        {
            aux.add(p1[i]);
        }

        for (int val : p2)
        {
            if (!aux.contains(val))
            {
                aux.add(val);
            }
        }

        hijo1.setVectorSol(aux.stream().mapToInt(i -> i).toArray());
        aux.clear();

        for (int i = 0; i < puntoCruce; i++)
        {
            aux.add(p2[i]);
        }

        for (int val : p1)
        {
            if (!aux.contains(val))
            {
                aux.add(val);
            }
        }

        hijo2.setVectorSol(aux.stream().mapToInt(i -> i).toArray());
    }

    public double getMejorCoste()
    {
        double mejorCoste = Double.MAX_VALUE;

        for (Individuo individuo : generacionActual)
        {
            double costeIndividuo = individuo.getCosteTotal();
            if (costeIndividuo < mejorCoste)
            {
                mejorCoste = costeIndividuo;
            }
        }

        return mejorCoste;
    }

    private void excepcionesInicializacion()
    {
        if (tamPoblacion <= 0)
        {
            throw new IllegalArgumentException("Error: El tamaño de la población debe ser mayor que 0.");
        }

        if (kbest > tamPoblacion)
        {
            throw new IllegalArgumentException("Error: kbest no puede ser mayor que el tamaño de la población.");
        }

        if (kworst > tamPoblacion)
        {
            throw new IllegalArgumentException("Error: kworst no puede ser mayor que el tamaño de la población.");
        }

        if (porcientoGeneracion < 0 || porcientoGeneracion > 1)
        {
            throw new IllegalArgumentException("Error: El porcentaje de generación debe estar entre 0 y 1.");
        }

        if (probCruce < 0 || probCruce > 1)
        {
            throw new IllegalArgumentException("Error: La probabilidad de cruce debe estar entre 0 y 1.");
        }

        if (prob2opt < 0 || prob2opt > 1)
        {
            throw new IllegalArgumentException("Error: La probabilidad de mutación 2-opt debe estar entre 0 y 1.");
        }

        if (maxEvaluaciones <= 0)
        {
            throw new IllegalArgumentException("Error: El máximo de comprobaciones debe ser mayor que 0.");
        }

        if (maxTiempo <= 0)
        {
            throw new IllegalArgumentException("Error: El tiempo máximo debe ser mayor que 0.");
        }
    }
}