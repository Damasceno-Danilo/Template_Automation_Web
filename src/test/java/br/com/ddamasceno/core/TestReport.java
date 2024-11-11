package br.com.ddamasceno.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ddamasceno.core.report.utils.ReportProperties;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class TestReport {

    private WebDriver driver;
    private List<ScreenshotData> screenshots;
    private String testStatus;
    private ReportProperties reportProperties; // Adiciona a classe ReportProperties

    public TestReport(WebDriver driver) {
        this.driver = driver;
        this.screenshots = new ArrayList<>();
        this.reportProperties = new ReportProperties(); // Inicializa ReportProperties para carregar as propriedades
    }

    public void captureScreenshot(String description) {
        try {
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            if (screenshotFile.exists()) {
                screenshots.add(new ScreenshotData(description, screenshotFile));
            } else {
                System.err.println("Falha ao capturar o screenshot.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTestStatus(boolean isPassed) {
        this.testStatus = isPassed ? "Passed" : "Failed";
    }

    public void createPdfReport() {
        try {
            String baseDirectory = "src/evidencias/";
            String dateFolderName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String fullDirectoryPath = baseDirectory + "/" + dateFolderName;
            Files.createDirectories(Paths.get(fullDirectoryPath));

            String timeInFileName = new SimpleDateFormat("HH-mm-ss").format(new Date());
            String filePath = fullDirectoryPath + "/" + reportProperties.getProperty("test.name") + "-" + timeInFileName + ".pdf";

            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Relatório de Teste Automatizado").setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n"));

            String statusMessage = "Status do Teste: " + testStatus;
            Paragraph statusParagraph = new Paragraph(statusMessage).setFontSize(12).setBold();
            if ("Passed".equals(testStatus)) {
                statusParagraph.setBackgroundColor(new DeviceRgb(0, 255, 0));
            } else {
                statusParagraph.setBackgroundColor(new DeviceRgb(255, 0, 0));
            }
            document.add(statusParagraph);

            document.add(new Paragraph("Nome do Teste: " + reportProperties.getProperty("test.name")));
            document.add(new Paragraph("Testador: " + reportProperties.getProperty("tester.name")));
            document.add(new Paragraph("Tecnologia: " + reportProperties.getProperty("technology")));
            document.add(new Paragraph("Nome da Aplicação: " + reportProperties.getProperty("application.name")));
            document.add(new Paragraph("Data do Teste: " + new Date().toString()));
            document.add(new Paragraph("\n"));

            for (ScreenshotData screenshotData : screenshots) {
                document.add(new Paragraph(screenshotData.getDescription()).setFontSize(12).setBold());
                try {
                    if (screenshotData.getFile().exists()) {
                        byte[] imageBytes = FileUtils.readFileToByteArray(screenshotData.getFile());
                        ImageData imageData = ImageDataFactory.create(imageBytes);
                        Image pdfImage = new Image(imageData);
                        document.add(pdfImage);
                        document.add(new Paragraph("\n"));
                    } else {
                        System.err.println("Falha ao encontrar o arquivo de screenshot: " + screenshotData.getFile().getAbsolutePath());
                    }
                } catch (IOException e) {
                    System.err.println("Erro ao carregar o screenshot: " + screenshotData.getFile().getAbsolutePath());
                    e.printStackTrace();
                }
            }

            document.close();
            System.out.println("Relatório PDF criado com sucesso em " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ScreenshotData {
        private String description;
        private File file;

        public ScreenshotData(String description, File file) {
            this.description = description;
            this.file = file;
        }

        public String getDescription() {
            return description;
        }

        public File getFile() {
            return file;
        }
    }
}
