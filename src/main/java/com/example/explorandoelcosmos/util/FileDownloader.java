package com.example.explorandoelcosmos.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.BiConsumer;

public class FileDownloader {

    /**
     * Descarga un archivo desde una URL a una ruta local, reportando el progreso.
     * @param url La URL del archivo a descargar.
     * @param localPath La ruta local donde se guardará el archivo.
     * @param progressCallback Un BiConsumer que acepta (bytesLeídos, totalBytes). Puede ser nulo.
     * @throws IOException Si ocurre un error de red o de archivo.
     */
    public static void downloadFile(String url, String localPath, BiConsumer<Long, Long> progressCallback) throws IOException {
        URL fileUrl = new URL(url);
        HttpURLConnection httpConn = (HttpURLConnection) fileUrl.openConnection();
        long totalBytes = httpConn.getContentLengthLong();

        File localFile = new File(localPath);
        localFile.getParentFile().mkdirs();

        try (InputStream in = httpConn.getInputStream();
             FileOutputStream out = new FileOutputStream(localFile)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            long totalBytesRead = 0;

            // Informar del progreso inicial (0%)
            if (progressCallback != null) {
                progressCallback.accept(0L, totalBytes);
            }

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                // Informar del progreso actual
                if (progressCallback != null) {
                    progressCallback.accept(totalBytesRead, totalBytes);
                }
            }
        }
    }
}
