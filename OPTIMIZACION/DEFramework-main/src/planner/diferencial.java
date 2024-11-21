package planner;

import dealib.components.Individual;
import dealib.components.Population;
import planner.configuration.Execution;

import java.util.Random;

public class diferencial {

    private Population poblacion;
    private int tam;
    private Execution exe;
    private int k;
    private int maxEv;
    private int evaluaciones;


    public void ejecutarDiferencias() {
        evaluaciones = 0;

        do{

        }while(evaluaciones < maxEv);

    }


    private Individual crearHijo(int posAct){
        Individual i1 = new Individual(exe);
        Individual i2 = new Individual(exe);
        i1 = poblacion.getKBestIndividual(k);
        i2 = poblacion.getKBestIndividual(k);


        Individual hijoFinal = new Individual(exe);
        hijoFinal = combinacion(poblacion.getIndividual(posAct),i1,i2);
        hijoFinal = recombinacion(hijoFinal);

        return sustitucion(hijoFinal,poblacion.getIndividual(posAct));
    }


    private Individual combinacion(Individual i0, Individual i1, Individual i2){
        Individual hijo = new Individual(exe);


        return hijo;
    }

    private Individual sustitucion(Individual hijo, Individual padre)
    {
        if(hijo.getFitness() > padre.getFitness()) //necesitamos el mas grande ?
        {
            return hijo;
        }
        else
        {
            return padre;
        }
    }

    private Individual recombinacion(Individual ind)
    {
        Random rand = new Random();

        if(rand.nextInt(0,1) > 0.5)
        {
            return ind;
        }
        else
        {
            return poblacion.getIndividual(poblacion.PopulationSize() - 1);
        }
    }

    private Individual torneo()
    {
        return poblacion.getKBestIndividual(2);
    }
}
