package procesadoFicheros;

import java.io.FileWriter;
import java.io.IOException;

public class CreaLogs {
    private FileWriter escritor;

    public CreaLogs(String nombreArchivo) {
        try {
            escritor = new FileWriter(nombreArchivo, true); // true para añadir al archivo en lugar de sobrescribir
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

