package br.com.ddamasceno.core;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Image;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestReport {

    private WebDriver driver;
    private List<ScreenshotData> screenshots = new ArrayList<>();

    public TestReport(WebDriver driver) {
        this.driver = driver;
    }

    // Método para capturar a tela com uma descrição
    public void captureScreenshot(String description) {
        try {
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Verifica se o screenshot foi salvo corretamente
            if (screenshotFile.exists()) {
                System.out.println("Screenshot capturado com sucesso: " + screenshotFile.getAbsolutePath());
                screenshots.add(new ScreenshotData(description, screenshotFile));
            } else {
                System.err.println("Falha ao capturar o screenshot.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para criar o PDF com todas as capturas e descrições
    public void createPdfReport(String testName, String testerName, String technology, String applicationName, String testDuration) {
        try {
            // Define o caminho base e formata a data para o nome da nova pasta
            String baseDirectory = "src/evidencias";
            String dateFolderName = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String fullDirectoryPath = baseDirectory + "/" + dateFolderName;

            // Cria o diretório "src/evidencias/YYYY-MM-DD_HH-MM-SS" se ele não existir
            Files.createDirectories(Paths.get(fullDirectoryPath));

            // Define o caminho completo do arquivo PDF
            String filePath = fullDirectoryPath + "/relatorio_de_teste.pdf";

            // Configura o documento PDF
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Adiciona o título e as informações do teste
            document.add(new Paragraph("Relatório de Teste Automatizado").setBold().setFontSize(16));
            document.add(new Paragraph("Nome do Teste: " + testName));
            document.add(new Paragraph("Testador: " + testerName));
            document.add(new Paragraph("Tecnologia: " + technology));
            document.add(new Paragraph("Nome da Aplicação: " + applicationName));
            document.add(new Paragraph("Duração do Teste: " + testDuration));
            document.add(new Paragraph("Data do Teste: " + new Date().toString()));
            document.add(new Paragraph("\n"));

            // Adiciona cada screenshot com sua descrição no PDF
            for (ScreenshotData screenshotData : screenshots) {
                document.add(new Paragraph(screenshotData.getDescription()).setFontSize(12).setBold());

                try {
                    // Verifica se o arquivo de screenshot existe antes de tentar adicioná-lo
                    if (screenshotData.getFile().exists()) {
                        // Carrega a imagem como ImageData e a insere no PDF
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
