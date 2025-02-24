package org.example;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SincronizadorFTP {
    private static final String LOCAL_FOLDER = "C:/CarpetaLocal";

    public static void main(String[] args) {
        try {
            Map<String, Long> archivosPrevios = new HashMap<>();

            System.out.println("Monitoreando la carpeta: " + LOCAL_FOLDER);

            while (true) {
                File carpeta = new File(LOCAL_FOLDER);
                File[] archivosActuales = carpeta.listFiles();
                Map<String, Long> archivosActualesMap = new HashMap<>();

                if (archivosActuales != null) {
                    for (File archivo : archivosActuales) {
                        archivosActualesMap.put(archivo.getName(), archivo.lastModified());

                        // Si el archivo es nuevo o ha sido modificado, se sube en un hilo separado
                        if (!archivosPrevios.containsKey(archivo.getName()) ||
                                archivosPrevios.get(archivo.getName()) != archivo.lastModified()) {
                            new Thread(new SubirArchivoHilo(archivo.getPath())).start();
                        }
                    }
                }

                // Revisar archivos eliminados en un hilo separado
                for (String archivoPrevio : archivosPrevios.keySet()) {
                    if (!archivosActualesMap.containsKey(archivoPrevio)) {
                        new Thread(new BorrarArchivoHilo(archivoPrevio)).start();
                    }
                }

                // Actualizar el estado de los archivos
                archivosPrevios = archivosActualesMap;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
