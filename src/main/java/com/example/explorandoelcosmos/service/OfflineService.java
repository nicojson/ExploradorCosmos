package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.dao.OfflineContentDAO;
import com.example.explorandoelcosmos.dao.OfflineContentDAOImpl;
import com.example.explorandoelcosmos.dao.PublicationDAO;
import com.example.explorandoelcosmos.dao.PublicationDAOImpl;
import com.example.explorandoelcosmos.model.Publication;
import com.example.explorandoelcosmos.util.FileDownloader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.BiConsumer;

public class OfflineService {

    private static final OfflineContentDAO offlineContentDAO = new OfflineContentDAOImpl();
    private static final PublicationDAO publicationDAO = new PublicationDAOImpl();
    private static final String OFFLINE_DIR = "offline_content/";

    public static void downloadPublication(Publication publication, BiConsumer<Long, Long> progressCallback) throws IOException {
        String urlToDownload = "video".equals(publication.getContentType()) ? publication.getContentUrl() : publication.getImageUrl();

        if (urlToDownload != null && !urlToDownload.isEmpty()) {
            // Validar que el video sea formato MP4
            if ("video".equals(publication.getContentType()) && !urlToDownload.toLowerCase().endsWith(".mp4")) {
                throw new IOException("Formato de video no soportado. Solo se permite MP4.");
            }

            if (publication.getId() == 0) {
                publicationDAO.save(publication);
            }

            String fileName = new File(urlToDownload).getName();
            String localPath = OFFLINE_DIR + fileName;

            FileDownloader.downloadFile(urlToDownload, localPath, progressCallback);

            publication.setLocalPath(localPath);
            offlineContentDAO.save(publication);
        } else {
            throw new IOException("No hay una URL válida para descargar.");
        }
    }

    public static void deleteOfflinePublication(Publication publication) throws IOException {
        if (publication.getLocalPath() != null && !publication.getLocalPath().isEmpty()) {
            Files.deleteIfExists(Paths.get(publication.getLocalPath()));
        }
        offlineContentDAO.delete(publication.getId());
    }
}
