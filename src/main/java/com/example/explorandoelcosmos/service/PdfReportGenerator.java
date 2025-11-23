package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.Launch;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.List;

public class PdfReportGenerator {

    public void generateLaunchReport(List<Launch> launches, String dest, String reportTitle) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Título del reporte
        document.add(new Paragraph(reportTitle).setBold().setFontSize(18).setMarginBottom(20));

        // Crear tabla
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 2, 4, 2}));
        table.setWidth(UnitValue.createPercentValue(100));

        // Encabezados de la tabla
        table.addHeaderCell("N° Vuelo");
        table.addHeaderCell("Misión");
        table.addHeaderCell("Fecha (UTC)");
        table.addHeaderCell("Detalles");
        table.addHeaderCell("Resultado");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Llenar la tabla con los datos de los lanzamientos
        for (Launch launch : launches) {
            table.addCell(String.valueOf(launch.getFlightNumber()));
            table.addCell(launch.getName());
            table.addCell(launch.getDateUtc() != null ? sdf.format(launch.getDateUtc()) : "N/A");
            table.addCell(launch.getDetails() != null ? launch.getDetails() : "Sin detalles.");
            
            if (launch.isSuccess() == null) {
                table.addCell("Desconocido");
            } else {
                table.addCell(launch.isSuccess() ? "Éxito" : "Fallo");
            }
        }

        document.add(table);
        document.close();
    }
}
