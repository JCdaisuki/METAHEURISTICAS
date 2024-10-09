package Auxiliares;

public class CiudadInfo
{
    private int indice;
    private double distTotal; // Distancia total de la ciudad al resto de ciudades

    public CiudadInfo(int i, double d) {
        indice = i;
        distTotal = d;
    }

    public double GetDistTotal() {
        return distTotal;
    }
    public int getIndice() {
        return indice;
    }
}