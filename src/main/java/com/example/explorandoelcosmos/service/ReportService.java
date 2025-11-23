package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.MoonPhaseResponse;
import com.example.explorandoelcosmos.model.NasaData;
import com.example.explorandoelcosmos.model.NasaItem;
import com.example.explorandoelcosmos.model.Rocket;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DashedLine;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.io.image.ImageDataFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportService {

    public void generatePdfReport(String reportTitle, List<?> data, String filePath) throws IOException {
        if (data == null || data.isEmpty()) {
            throw new IOException("No hay datos para generar el reporte.");
        }

        try (PdfWriter writer = new PdfWriter(filePath);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            // Título del reporte
            document.add(new Paragraph(reportTitle)
                    .setBold()
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Generado por Explorando El Cosmos")
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new LineSeparator(new SolidLine(1f)));
            document.add(new Paragraph("\n"));

            // Contenido del reporte con los métodos correctos y manejo de nulos
            for (Object item : data) {
                if (item instanceof Rocket rocket) {
                    document.add(new Paragraph("Cohete: " + (rocket.getName() != null ? rocket.getName() : "N/A")).setBold());
                    document.add(new Paragraph("Descripción: " + (rocket.getDescription() != null ? rocket.getDescription() : "Sin descripción.")).setItalic());
                } else if (item instanceof MoonPhaseResponse moonPhaseResponse) {
                    document.add(new Paragraph("Fase Lunar").setBold());
                    document.add(new Paragraph("Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
                    if (moonPhaseResponse.getData() != null && moonPhaseResponse.getData().getImageUrl() != null) {
                        try {
                            Image moonImage = new Image(ImageDataFactory.create(new URL(moonPhaseResponse.getData().getImageUrl())));
                            moonImage.scaleToFit(200, 200); // Ajustar tamaño de la imagen
                            document.add(moonImage.setHorizontalAlignment(HorizontalAlignment.CENTER));
                        } catch (Exception e) {
                            document.add(new Paragraph("No se pudo cargar la imagen de la fase lunar."));
                            e.printStackTrace();
                        }
                    } else {
                        document.add(new Paragraph("Imagen de fase lunar no disponible."));
                    }
                } else if (item instanceof NasaItem nasaItem) {
                    if (nasaItem.getData() != null && !nasaItem.getData().isEmpty()) {
                        NasaData nasaData = nasaItem.getData().get(0);
                        document.add(new Paragraph("Biblioteca NASA: " + (nasaData.getTitle() != null ? nasaData.getTitle() : "N/A")).setBold());
                        document.add(new Paragraph("Descripción: " + (nasaData.getDescription() != null ? nasaData.getDescription() : "Sin descripción.")).setItalic());
                        document.add(new Paragraph("Fecha de Creación: " + (nasaData.getDateCreated() != null ? nasaData.getDateCreated() : "N/A")));
                        if (nasaItem.getPreviewImageUrl() != null) {
                            try {
                                Image nasaLibraryImage = new Image(ImageDataFactory.create(new URL(nasaItem.getPreviewImageUrl())));
                                nasaLibraryImage.scaleToFit(300, 300);
                                document.add(nasaLibraryImage.setHorizontalAlignment(HorizontalAlignment.CENTER));
                            } catch (Exception e) {
                                document.add(new Paragraph("No se pudo cargar la imagen de la Biblioteca NASA."));
                                e.printStackTrace();
                            }
                        } else {
                            document.add(new Paragraph("Imagen de la Biblioteca NASA no disponible."));
                        }
                    } else {
                        document.add(new Paragraph("Elemento de la Biblioteca NASA sin datos disponibles."));
                    }
                }
                document.add(new LineSeparator(new DashedLine()));
                document.add(new Paragraph("\n"));
            }
            
            System.out.println("Reporte PDF generado exitosamente en: " + filePath);
        }
    }
}
