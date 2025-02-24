package org.example;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class SubirArchivoHilo implements Runnable {
    private String filePath;
    private static final String FTP_SERVER = "localhost";
    private static final int FTP_PORT = 21;
    private static final String FTP_USER = "usuario";
    private static final String FTP_PASS = "contraseña";
    private static final String FTP_FOLDER = "/";
    private static final String HISTORY_FOLDER = "/history/";
    private static final String AES_PASSWORD = "ClaveDe32CaracteresAESClaveDe32Ca";

    public SubirArchivoHilo(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        FTPClient ftpClient = new FTPClient();
        File file = new File(filePath);

        try {
            if (!file.exists()) return;

            System.out.println("Subiendo archivo cifrado: " + file.getName());

            // Conectarse al servidor FTP
            ftpClient.connect(FTP_SERVER, FTP_PORT);
            ftpClient.login(FTP_USER, FTP_PASS);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // Leer contenido del archivo y cifrarlo
            FileInputStream fis = new FileInputStream(file);
            byte[] contenido = fis.readAllBytes();
            fis.close();

            String contenidoCifrado = AESSimpleManager.cifrar(new String(contenido), AESSimpleManager.obtenerClave(AES_PASSWORD, 32));

            // Subir directamente el contenido cifrado
            InputStream inputStreamCifrado = new ByteArrayInputStream(contenidoCifrado.getBytes());

            //Verificar si el archivo ya existe en el FTP (para guardar versiones)
            if (ftpClient.listNames(FTP_FOLDER + file.getName()) != null) {
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String versionedFileName = HISTORY_FOLDER + file.getName() + "_" + timestamp;
                ftpClient.rename(FTP_FOLDER + file.getName(), versionedFileName);
                System.out.println("Archivo movido a historial: " + versionedFileName);
            }

            // Subir la nueva versión
            boolean subido = ftpClient.storeFile(FTP_FOLDER + file.getName(), inputStreamCifrado);
            inputStreamCifrado.close();

            if (subido) {
                System.out.println("Archivo subido correctamente (cifrado): " + file.getName());
            } else {
                System.out.println("Error al subir el archivo: " + file.getName());
            }

            // Cerrar la conexión FTP
            ftpClient.logout();
            ftpClient.disconnect();

        } catch (Exception e) {
            System.err.println("Error al subir el archivo: " + file.getName());
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
