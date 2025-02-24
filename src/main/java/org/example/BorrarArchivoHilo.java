package org.example;

import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;

public class BorrarArchivoHilo implements Runnable {
    private String fileName;
    private static final String FTP_SERVER = "localhost";
    private static final int FTP_PORT = 21;
    private static final String FTP_USER = "usuario";
    private static final String FTP_PASS = "contraseña";
    private static final String FTP_FOLDER = "/";
    private static final String HISTORY_FOLDER = "/history/";

    public BorrarArchivoHilo(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(FTP_SERVER, FTP_PORT);
            ftpClient.login(FTP_USER, FTP_PASS);

            // Mover archivo a la carpeta history
            String movedFileName = HISTORY_FOLDER + fileName + "_deleted";
            boolean movido = ftpClient.rename(FTP_FOLDER + fileName, movedFileName);

            if (movido) {
                System.out.println("Archivo movido a historial: " + movedFileName);
            } else {
                System.out.println("No se pudo mover el archivo a historial.");
            }

            ftpClient.logout();
            ftpClient.disconnect();

        } catch (Exception e) {
            System.err.println("Error al mover el archivo a historial: " + fileName);
            e.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
