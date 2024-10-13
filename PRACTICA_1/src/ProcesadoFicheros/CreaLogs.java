package ProcesadoFicheros;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CreaLogs {
    private FileWriter escritor;
    private List<String> mejoresLocales; // Lista para almacenar los mejores locales
    public CreaLogs(String nombreArchivo) {
        try {
            escritor = new FileWriter(nombreArchivo, true); // true para añadir al archivo en lugar de sobrescribir
            mejoresLocales = new ArrayList<>();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void escribirLog(String mensaje) {
        try {
            escritor.write(mensaje + "\n");
            escritor.flush(); // Para asegurarse de que se escriben los datos inmediatamente
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cerrarLog() {
        try {
            if (escritor != null) {
                escritor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void aniadirEncontrado(String mensajeMejorLocal) {
        mejoresLocales.add(mensajeMejorLocal); // Añadir el mensaje a la lista
    }
    public void escribirMejoresLocales() {
        try {
            escritor.write("\n" + "\n" + "\n");
            for (String mejorLocal : mejoresLocales) {
                escritor.write(mejorLocal + "\n"); // Escribir cada string en una nueva línea
            }
            escritor.flush(); // Asegurar que se escribe inmediatamente
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

