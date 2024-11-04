import ProcesadoFicheros.LeerConfig;

public class Main
{
    public static void main(String[] args)
    {
        LeerConfig.leerConfiguracion("config.txt");

        String[] archivosTSP = LeerConfig.archivosTSP;
        String rutaBase = LeerConfig.rutaBase;
        String rutaLogs = LeerConfig.rutaLogs;
        String seed = LeerConfig.seed;
    }
}
