package br.com.ddamasceno.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import br.com.ddamasceno.core.report.ReportProperties;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import static com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD;

public class TestReport {

    private WebDriver driver;
    private List<ScreenshotData> screenshots;
    private String testStatus;
    private ReportProperties reportProperties;

    public TestReport(WebDriver driver) {
        this.driver = driver;
        this.screenshots = new ArrayList<>();
        this.reportProperties = new ReportProperties();
        this.testStatus = "Not Executed"; // valor padrão
    }

    /**
     * Captura um screenshot e salva em disco antes de adicionar ao relatório
     */
    public void captureScreenshot(String description) {
        try {
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            if (screenshotFile.exists()) {
                // Cria a pasta para armazenar screenshots
                String screenshotDir = "src/evidencias/screenshots/";
                Files.createDirectories(Paths.get(screenshotDir));

                // Nome único do arquivo
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File destFile = new File(screenshotDir + description.replaceAll("\\s+", "_") + "_" + timestamp + ".png");

                // Copia o screenshot do Selenium para o destino
                FileUtils.copyFile(screenshotFile, destFile);

                // Adiciona no relatório a referência ao arquivo salvo
                screenshots.add(new ScreenshotData(description, destFile));
            } else {
                System.err.println("Falha ao capturar o screenshot.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Define o status do teste (Passou ou Falhou).
     * Se falhou, captura automaticamente uma evidência.
     */
    public void setTestStatus(boolean isPassed) {
        this.testStatus = isPassed ? "Passed" : "Failed";

        if (!isPassed) {
            captureScreenshot("Evidência da falha");
        }
    }

    /**
     * Cria o PDF com todas as evidências do teste.
     */
    public void createPdfReport() {
        try {
            String baseDirectory = "src/evidencias/";
            String dateFolderName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            // Se falhou, cria dentro da pasta Failed
            String statusFolder = "Failed".equalsIgnoreCase(testStatus) ? "/Failed" : "/Passed";
            String fullDirectoryPath = baseDirectory + "/" + dateFolderName + statusFolder;
            Files.createDirectories(Paths.get(fullDirectoryPath));

            String timeInFileName = new SimpleDateFormat("HH-mm-ss").format(new Date());
            String filePath = fullDirectoryPath + "/" + reportProperties.getProperty("test.name")
                    + "-" + timeInFileName + ".pdf";

            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Criar tabela com 1 coluna para o cabeçalho e informações
            float[] columnWidths = {600F};
            Table table = new Table(columnWidths);
            table.setWidth(500);

            // Cabeçalho
            table.addCell(new Cell().add(new Paragraph("Relatório de Testes")
                    .setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER)));

            // Status dentro da tabela
            if (testStatus == null) {
                testStatus = "Not Executed";
            }

            Cell statusCell = new Cell().add(new Paragraph("Status do Teste: " + testStatus)
                    .setFontSize(12));

            switch (testStatus) {
                case "Passed":
                    statusCell.setBackgroundColor(new DeviceRgb(0, 255, 0));
                    break;
                case "Failed":
                    statusCell.setBackgroundColor(new DeviceRgb(255, 0, 0));
                    break;
                default:
                    statusCell.setBackgroundColor(new DeviceRgb(255, 255, 0));
                    break;
            }
            table.addCell(statusCell);

            // Dados do teste
            table.addCell(new Cell().add(new Paragraph("Nome do Teste: " + reportProperties.getProperty("test.name"))));
            table.addCell(new Cell().add(new Paragraph("Nome do QA: " + reportProperties.getProperty("tester.name"))));
            table.addCell(new Cell().add(new Paragraph("Tecnologia: " + reportProperties.getProperty("technology"))));
            table.addCell(new Cell().add(new Paragraph("Nome da Aplicação: " + reportProperties.getProperty("application.name"))));
            table.addCell(new Cell().add(new Paragraph("Data do Teste: " + new Date().toString())));

            // Adiciona a tabela no PDF
            document.add(table);
            document.add(new Paragraph("\n"));

            // Screenshots
            for (ScreenshotData screenshotData : screenshots) {
                try {
                    if (screenshotData.getFile().exists()) {
                        byte[] imageBytes = FileUtils.readFileToByteArray(screenshotData.getFile());
                        ImageData imageData = ImageDataFactory.create(imageBytes);
                        Image pdfImage = new Image(imageData);
                        document.add(pdfImage);
                        document.add(new Paragraph(screenshotData.getDescription()).setFontSize(10));
                        document.add(new Paragraph("\n"));
                    } else {
                        System.err.println("Arquivo de screenshot não encontrado: " + screenshotData.getFile().getAbsolutePath());
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

    /**
     * Classe auxiliar para armazenar screenshots
     */
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
