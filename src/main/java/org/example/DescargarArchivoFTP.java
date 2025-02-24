package org.example;

import java.io.*;
import java.nio.file.*;
import java.security.Key;
import java.util.Scanner;
import org.apache.commons.net.ftp.*;

public class DescargarArchivoFTP {
    private static final String FTP_SERVER = "localhost";
    private static final int FTP_PORT = 21;
    private static final String FTP_USER = "usuario";
    private static final String FTP_PASS = "contraseña";
    private static final String LOCAL_FOLDER = "C:/CarpetaDescargas/";
    private static final String FTP_FOLDER = "/";
    private static final String HISTORY_FOLDER = "/history/";
    private static final String AES_PASSWORD = "ClaveDe32CaracteresAESClaveDe32Ca";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el nombre del archivo a descargar (incluyendo la extensión): ");
        String archivoRemoto = scanner.nextLine();

        System.out.println("¿Desea descargar todas las versiones del archivo? (s/n)");
        String opcion = scanner.nextLine().trim().toLowerCase();
        scanner.close();

        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(FTP_SERVER, FTP_PORT);
            ftpClient.login(FTP_USER, FTP_PASS);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            if (opcion.equals("s")) {
                // Descargar historial y la versión actual
                descargarArchivo(ftpClient, archivoRemoto, LOCAL_FOLDER + archivoRemoto);
                descargarHistorial(ftpClient, archivoRemoto);
            } else {
                // Descargar solo la versión actual
                descargarArchivo(ftpClient, archivoRemoto, LOCAL_FOLDER + archivoRemoto);
            }

            ftpClient.logout();
            ftpClient.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Descargar un archivo específico del FTP y lo descifra.
     */
    private static void descargarArchivo(FTPClient ftpClient, String archivoRemoto, String destinoLocal) {
        try {
            System.out.println("Descargando: " + archivoRemoto);
            try (OutputStream outputStream = new FileOutputStream(destinoLocal)) {
                boolean success = ftpClient.retrieveFile(FTP_FOLDER + archivoRemoto, outputStream);
                if (success) {
                    System.out.println("Archivo descargado: " + destinoLocal);
                    descifrarArchivo(destinoLocal); // Descifrar después de la descarga
                } else {
                    System.out.println("No se encontró el archivo en el servidor FTP.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al descargar el archivo: " + archivoRemoto);
            e.printStackTrace();
        }
    }

    /**
     * Descargar todas las versiones del archivo en la carpeta `history/`
     */
    private static void descargarHistorial(FTPClient ftpClient, String archivoBase) {
        try {
            System.out.println("Buscando versiones antiguas en history/...");
            String[] archivosHistorial = ftpClient.listNames(HISTORY_FOLDER + archivoBase + "*");

            if (archivosHistorial == null || archivosHistorial.length == 0) {
                System.out.println("No hay versiones anteriores del archivo.");
                return;
            }

            for (String archivoVersionado : archivosHistorial) {
                String nombreLocal = LOCAL_FOLDER + archivoVersionado.substring(HISTORY_FOLDER.length());
                descargarArchivo(ftpClient, archivoVersionado, nombreLocal);
            }

        } catch (Exception e) {
            System.err.println("Error al descargar el historial de " + archivoBase);
            e.printStackTrace();
        }
    }

    /**
     * Descifrar un archivo descargado del FTP usando AES
     */
    private static void descifrarArchivo(String archivoCifrado) {
        try {
            String contenidoCifrado = new String(Files.readAllBytes(Paths.get(archivoCifrado)));
            Key clave = AESSimpleManager.obtenerClave(AES_PASSWORD, 32);
            String contenidoDescifrado = AESSimpleManager.descifrar(contenidoCifrado, clave);

            // Guardar el archivo descifrado con el mismo nombre original
            try (FileOutputStream fos = new FileOutputStream(archivoCifrado)) {
                fos.write(contenidoDescifrado.getBytes());
            }

            System.out.println("Archivo descifrado correctamente: " + archivoCifrado);

        } catch (Exception e) {
            System.out.println("Error al descifrar el archivo: " + archivoCifrado);
            e.printStackTrace();
        }
    }
}
