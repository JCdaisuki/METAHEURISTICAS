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
}

