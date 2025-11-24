package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.MoonPhaseResponse;
import com.example.explorandoelcosmos.model.NasaData;
import com.example.explorandoelcosmos.model.NasaItem;
import com.example.explorandoelcosmos.model.Publication;
import com.example.explorandoelcosmos.model.Rocket;
import com.example.explorandoelcosmos.model.Launch;

import java.io.IOException;
import java.util.List;

/**
 * Stub implementation of ReportService.
 * The original PDF generation used iText which is not available in the module
 * path.
 * This version simply logs that a report would be generated.
 */
public class ReportService {

    public void generatePdfReport(String reportTitle, List<?> data, String filePath) throws IOException {
        if (data == null || data.isEmpty()) {
            throw new IOException("No hay datos para generar el reporte.");
        }
        // Stub: In a full implementation this would generate a PDF using iText.
        System.out.println("[ReportService] Generando reporte: " + reportTitle + " a " + filePath);
        System.out.println("[ReportService] Número de elementos: " + data.size());
    }
}
