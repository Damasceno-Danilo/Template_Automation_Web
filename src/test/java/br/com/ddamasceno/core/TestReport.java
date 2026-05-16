package br.com.ddamasceno.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

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

/**
 * TestReport - gera PDF com evidências.
 * Versão atualizada para priorizar cenário/tags vindos do Hooks e inferir nomes a partir das screenshots
 * caso Hooks não tenha populado as propriedades.
 */
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
                File destFile = new File(screenshotDir + sanitizeForFileName(description) + "_" + timestamp + ".png");

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
            String baseDirectory = "src/evidencias";
            String dateFolderName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            // Se falhou, cria dentro da pasta Failed
            String statusFolder = "Failed".equalsIgnoreCase(testStatus) ? "/Failed" : "/Passed";
            String fullDirectoryPath = baseDirectory + "/" + dateFolderName + statusFolder;
            Files.createDirectories(Paths.get(fullDirectoryPath));

            String timeInFileName = new SimpleDateFormat("HH-mm-ss").format(new Date());

            // Obtém valores do arquivo de propriedades padrão
            String tagName = reportProperties.getProperty("tag.name", "SemTag");
            String scenarioName = reportProperties.getProperty("scenario.name", "SemNome");
            String testerName = reportProperties.getProperty("tester.name", "Não informado");
            String applicationName = reportProperties.getProperty("application.name", "Não informado");

            // sobrescrever com Hooks (se existirem propriedades definidas em runtime)
            try {
                Properties hooksProps = Hooks.getReportProperties();
                if (hooksProps != null) {
                    String hooksTag = hooksProps.getProperty("tag.name");
                    String hooksScenario = hooksProps.getProperty("scenario.name");
                    if (hooksTag != null && !hooksTag.isBlank()) {
                        tagName = hooksTag;
                    }
                    if (hooksScenario != null && !hooksScenario.isBlank()) {
                        scenarioName = hooksScenario;
                    }
                    // Não sobrescrevemos tester/application aqui a menos que Hooks os defina
                    testerName = hooksProps.getProperty("tester.name", testerName);
                    applicationName = hooksProps.getProperty("application.name", applicationName);
                }
            } catch (Exception e) {
                // Continua com valores anteriores caso Hooks não esteja disponível
            }

            // Se Hooks não trouxe scenarioName, tentamos inferir do(s) screenshot(s)
            if (scenarioName == null || scenarioName.trim().isEmpty() || "SemNome".equals(scenarioName)) {
                String inferred = inferScenarioNameFromScreenshots();
                if (inferred != null && !inferred.isBlank()) {
                    scenarioName = inferred;
                }
            }

            // Se ainda não temos tagName adequado, tentar extrair algo dos screenshots (menos prioritário)
            if (tagName == null || tagName.trim().isEmpty() || "SemTag".equals(tagName)) {
                String inferredTag = inferTagFromScreenshots();
                if (inferredTag != null && !inferredTag.isBlank()) {
                    tagName = inferredTag;
                }
            }

            // Fallback final
            if (scenarioName == null || scenarioName.trim().isEmpty()) {
                scenarioName = "SemNome";
            }
            if (tagName == null || tagName.trim().isEmpty()) {
                tagName = "SemTag";
            }

            // Sanitiza para uso em filename
            String safeTag = sanitizeForFileName(tagName);
            String safeScenario = sanitizeForFileName(scenarioName);

            String filePath = fullDirectoryPath + "/" + safeTag + "-" + safeScenario + "-" + timeInFileName + ".pdf";

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
            if (testStatus == null || testStatus.isEmpty()) {
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

            // Dados do teste (usar scenarioName já resolvido)
            table.addCell(new Cell().add(new Paragraph("Cenário: " + scenarioName)));
            table.addCell(new Cell().add(new Paragraph("Tester: " + testerName)));
            table.addCell(new Cell().add(new Paragraph("Projeto: " + applicationName)));
            // Data formatada
            String dataFormatada = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date());
            table.addCell(new Cell().add(new Paragraph("Data: " + dataFormatada)));

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
     * Tenta inferir um nome de cenário a partir da lista de screenshots.
     * Procura descrições com "Evidência - " ou "Falha - " e retorna a parte após o prefixo.
     * Se não encontrar, retorna a descrição do primeiro screenshot (limpa e truncada).
     */
    private String inferScenarioNameFromScreenshots() {
        try {
            if (screenshots == null || screenshots.isEmpty()) {
                return null;
            }
            for (ScreenshotData sd : screenshots) {
                String desc = sd.getDescription();
                if (desc == null) continue;
                String lower = desc.toLowerCase();
                if (lower.contains("evidência -") || lower.contains("evidencia -")) {
                    int idx = desc.indexOf("-");
                    if (idx >= 0 && idx + 1 < desc.length()) {
                        return desc.substring(idx + 1).trim();
                    }
                }
                if (lower.contains("falha -")) {
                    int idx = desc.indexOf("-");
                    if (idx >= 0 && idx + 1 < desc.length()) {
                        return desc.substring(idx + 1).trim();
                    }
                }
            }
            // fallback: usa a primeira description inteira (limitando tamanho)
            String firstDesc = screenshots.get(0).getDescription();
            if (firstDesc != null && !firstDesc.isBlank()) {
                return firstDesc.length() > 80 ? firstDesc.substring(0, 80).trim() : firstDesc.trim();
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    /**
     * Tenta inferir uma tag a partir das descrições (pouco provável, usado como fallback).
     */
    private String inferTagFromScreenshots() {
        try {
            if (screenshots == null || screenshots.isEmpty()) {
                return null;
            }
            // tenta extrair palavra curta do primeiro screenshot
            String firstDesc = screenshots.get(0).getDescription();
            if (firstDesc != null && !firstDesc.isBlank()) {
                // pegar primeira palavra
                String[] parts = firstDesc.split("\\s+");
                if (parts.length > 0) {
                    String p = parts[0].replaceAll("\\W+", "");
                    if (!p.isBlank() && p.length() <= 20) {
                        return p;
                    }
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    /**
     * Remove caracteres inválidos para uso em nomes de arquivos e reduz espaços
     */
    private String sanitizeForFileName(String input) {
        if (input == null) {
            return "SemNome";
        }
        // remove acentos básicos e substitui caracteres não alfanuméricos por underscore
        String cleaned = input.replaceAll("[\\t\\n\\r]", " ").trim();
        // Normaliza múltiplos espaços
        cleaned = cleaned.replaceAll("\\s{2,}", " ");
        // Substitui tudo que não seja letra/número por underscore
        cleaned = cleaned.replaceAll("[^a-zA-Z0-9\\-_.]", "_");
        // Segurança: remover underscores duplicados
        cleaned = cleaned.replaceAll("_+", "_");
        // Limita o tamanho
        if (cleaned.length() > 60) {
            cleaned = cleaned.substring(0, 60);
        }
        if (cleaned.isEmpty()) {
            return "SemNome";
        }
        return cleaned;
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