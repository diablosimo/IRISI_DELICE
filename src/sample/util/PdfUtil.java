package sample.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

/**
 *
 * @author simob
 */
public class PdfUtil {

    private static String absoluteJasperPath;
    private static String absoluteWebPath;

    public static void generatePdf(List myList, Map<String, Object> params, String outPoutFileName, String bilan) throws JRException {
        if (myList == null || myList.isEmpty()) {
            myList = new ArrayList();
            myList.add(new Object());
        }
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(myList);
        JasperPrint jasperPrint = JasperFillManager.fillReport(PdfUtil.class.getResourceAsStream(bilan), params, jrBeanCollectionDataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint,"facture/"+outPoutFileName);
    }
}

