/**
 * @author Juan Carlos Gonzalez Martinez
 * @author Jose Antonio Mayoral Luna
 */

public class LectorTSP
{
    private final String ruta;
    private final double ciudades[][];
    private final double distancias[][];
    public LectorTSP(String ruta){
        this.ruta = ruta.split("\\.")[0];
    }


    //get ruta
    public String getRuta()
    {
        return ruta;
    }
//get matriz ciudades
    public double[][] getCiudades()
    {
        return ciudades;
    }
//get matriz distancias
    public double[][] getDistancias()
    {
        return distancias;
    }




}
