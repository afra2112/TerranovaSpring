package com.proyecto.terranova.service;

import com.proyecto.terranova.entity.Venta;
import net.sf.jasperreports.pdf.JRPdfExporter;
import net.sf.jasperreports.renderers.Renderable;
import org.apache.batik.gvt.renderer.ImageRenderer;
import org.springframework.stereotype.Service;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ReportService {

    public byte[] generarReporteVentas(List<Venta> ventas) throws Exception {
        InputStream jasperStream = new ClassPathResource("reports/reporte-ventas.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperStream);

        Map<String, Object> parameters = new HashMap<>();

        int totalVentas = ventas.size();
        Long totalIngresos = ventas.stream()
                .filter(v -> v.getProducto() != null)
                .mapToLong(v -> v.getProducto().getPrecioProducto())
                .sum();
        Long totalGastos = ventas.stream()
                .mapToLong(Venta::getTotalGastos)
                .sum();
        Long balance = totalIngresos - totalGastos;

        parameters.put("totalVentas", totalVentas);
        parameters.put("totalIngresos", new BigDecimal(totalIngresos));
        parameters.put("totalGastos", new BigDecimal(totalGastos));
        parameters.put("balance", new BigDecimal(balance));
        parameters.put("logoPath", "classpath:static/images/logo.jpg");

        parameters.put("chartIngresos", generarGraficoLinea(ventas));
        parameters.put("chartPastel", generarGraficoPastel(ventas));

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ventas);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private BufferedImage generarGraficoLinea(List<Venta> ventasPorMes){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(15000, "Ingresos", "Ene");
        dataset.addValue(12000, "Gastos", "Ene");
        dataset.addValue(18000, "Ingresos", "Feb");
        dataset.addValue(14000, "Gastos", "Feb");
        dataset.addValue(22000, "Ingresos", "Mar");
        dataset.addValue(16000, "Gastos", "Mar");

        JFreeChart chart = ChartFactory.createLineChart(
                "", "", "", dataset, PlotOrientation.VERTICAL, true, true, false);

        chart.setBackgroundPaint(java.awt.Color.WHITE);

        return chart.createBufferedImage(800,400);
    }

    private BufferedImage generarGraficoPastel(List<Venta> ventas){
        DefaultPieDataset dataset = new DefaultPieDataset();

        Map<String, Integer> metodosPago = new HashMap<>();
        for (Venta venta : ventas) {
            String metodo = venta.getMetodoPago() != null ? venta.getMetodoPago() : "No especificado";
            metodosPago.merge(metodo, 1, Integer::sum);
        }

        for (Map.Entry<String, Integer> entry : metodosPago.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart("", dataset, true, true, false);
        chart.setBackgroundPaint(java.awt.Color.WHITE);

        return chart.createBufferedImage(400, 300);
    }

    private byte[] convertirImagenABytes(BufferedImage image) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

}