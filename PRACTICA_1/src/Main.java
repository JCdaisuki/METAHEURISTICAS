import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Define la ruta del archivo
        String rutaArchivo = "C:\\Githubs de clase\\Meta\\METAHEURISTICAS\\PRACTICA_1\\pr144.tsp";


        // Crear un objeto LectorTSP para leer las ciudades desde el archivo
        LectorTSP lector = new LectorTSP(rutaArchivo);

        double[][] distancias = lector.getDistancias();
        int numCiudades = distancias.length;

        List<Integer> solucion = new ArrayList<>();
        for (int i = 0; i < numCiudades; i++) {
            solucion.add(i);
        }
        System.out.println("Matriz de distancias:");
        for (int i = 0; i < distancias.length; i++) {
            for (int j = 0; j < distancias[i].length; j++) {
                System.out.printf("%.2f ", distancias[i][j]);
            }
            System.out.println(); // Nueva línea
        }

    }
}

