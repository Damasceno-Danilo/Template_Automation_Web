package br.com.ddamasceno.core;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

/**
 * Biblioteca de ações Selenium reutilizáveis.
 *
 * <p>O timeout padrão para esperas explícitas é controlado por
 * {@link #DEFAULT_TIMEOUT_SECONDS}. Métodos sobrecarregados aceitam um timeout
 * customizado para casos onde o carregamento da página é mais lento.
 */
public class WebActions {

    /** Timeout padrão para esperas explícitas (segundos). */
    public static final int DEFAULT_TIMEOUT_SECONDS = 10;

    private final WebDriver driver;
    private final WebDriverWait defaultWait;
    private Alert alert;
    private final TestReport report;

    public WebActions(WebDriver driver) {
        this.driver = driver;
        this.defaultWait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        this.report = new TestReport(driver);
    }

    // ─── Navegação ────────────────────────────────────────────────────────────

    public void navigateURL(String url) {
        driver.get(url);
    }

    public void closedBrowser() {
        driver.quit();
    }

    // ─── Interações básicas ───────────────────────────────────────────────────

    public void insertText(WebElement element, String texto) {
        element.sendKeys(texto);
    }

    public String validatedText(WebElement element) {
        return element.getText();
    }

    public void click(WebElement element) {
        element.click();
    }

    // ─── Esperas explícitas (com timeout padrão) ──────────────────────────────

    public void waitVisibleElement(WebElement element) {
        waitVisibleElement(element, DEFAULT_TIMEOUT_SECONDS);
    }

    public void waitInvisibleElement(WebElement element) {
        waitInvisibleElement(element, DEFAULT_TIMEOUT_SECONDS);
    }

    // ─── Esperas explícitas (com timeout customizado) ─────────────────────────

    /**
     * Aguarda a visibilidade de um elemento com timeout customizado.
     *
     * @param element elemento alvo
     * @param timeoutSeconds timeout em segundos (sobrescreve o padrão)
     */
    public void waitVisibleElement(WebElement element, int timeoutSeconds) {
        buildWait(timeoutSeconds).until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Aguarda a invisibilidade de um elemento com timeout customizado.
     *
     * @param element elemento alvo
     * @param timeoutSeconds timeout em segundos (sobrescreve o padrão)
     */
    public void waitInvisibleElement(WebElement element, int timeoutSeconds) {
        buildWait(timeoutSeconds).until(ExpectedConditions.invisibilityOfAllElements(element));
    }

    // ─── Dropdowns ────────────────────────────────────────────────────────────

    public void selectForIndex(WebElement element, int index) {
        new Select(element).selectByIndex(index);
    }

    public void selectForValue(WebElement element, String value) {
        new Select(element).selectByValue(value);
    }

    public void selectForText(WebElement element, String texto) {
        new Select(element).selectByVisibleText(texto);
    }

    // ─── Alertas ─────────────────────────────────────────────────────────────

    public void interactWithAlert(WebElement element, String expectedText) {
        element.click();
        Alert alert = driver.switchTo().alert();
        Assertions.assertEquals(expectedText, alert.getText(),
                "Texto do alerta não corresponde ao esperado");
        alert.accept();
    }

    public void interactAlertConfim(WebElement element) {
        element.click();
        alert = driver.switchTo().alert();
        alert.accept();
    }

    public void interveneAlertDenied(WebElement element) {
        element.click();
        alert = driver.switchTo().alert();
        alert.dismiss();
    }

    public void interactAlertPrompt(WebElement element) {
        element.click();
        driver.switchTo().alert().accept();
    }

    // ─── Frames e Janelas ─────────────────────────────────────────────────────

    public void interactWithFrame(WebElement element) {
        driver.switchTo().frame("frame1");
        driver.findElement(By.id("frameButton")).click();
        driver.switchTo().alert().accept();
    }

    public void interactWithWindows(WebElement element) {
        driver.findElement(By.id("buttonPopUpEasy")).click();
        driver.switchTo().window("Popup");
        driver.findElement(By.tagName("textarea"));
        driver.close();
    }

    // ─── Evidência ───────────────────────────────────────────────────────────

    /**
     * Captura um screenshot da tela atual, salva em disco e retorna o arquivo.
     *
     * <p>Use junto com {@code report().registerStep(...)} nas classes de lógica
     * para controlar exatamente onde uma evidência será incluída no relatório:
     * <pre>
     *   report().registerStep(webActions().getScreenshot(), step, "screenshot");
     * </pre>
     *
     * @return {@link File} do screenshot salvo, ou {@code null} em caso de erro
     */
    public File getScreenshot() {
        try {
            String screenshotDir = "src/evidencias/screenshots/";
            Files.createDirectories(Paths.get(screenshotDir));

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            File destFile = new File(screenshotDir + "screenshot_" + timestamp + ".png");
            FileUtils.copyFile(srcFile, destFile);
            return destFile;
        } catch (Exception e) {
            System.err.println("[WebActions] Erro ao capturar screenshot: " + e.getMessage());
            return null;
        }
    }

    /** @deprecated Use {@link #getScreenshot()} com {@code report().registerStep()} */
    @Deprecated
    public void screenShot() throws IOException {
        report.createPdfReport();
    }

    // ─── Utilitários internos ─────────────────────────────────────────────────

    private WebDriverWait buildWait(int timeoutSeconds) {
        if (timeoutSeconds == DEFAULT_TIMEOUT_SECONDS) {
            return defaultWait;
        }
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }
}
