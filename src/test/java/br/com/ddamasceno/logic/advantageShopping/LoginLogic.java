package br.com.ddamasceno.logic.advantageShopping;

import br.com.ddamasceno.core.*;
import br.com.ddamasceno.maps.advantageShopping.LoginMap;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

@Log4j2
public class LoginLogic {

    private WebDriver driver;
    private DriverFactory driverFactory;
    private WebActions webActions;
    private LoginMap loginMap;
    private WebDriverWait wait;

    public LoginLogic() {
        // Cria o driver via DriverFactory (DriverFactory deve registrar no DriverManager)
        driverFactory = new DriverFactory(Browser.CHROME);
        this.driver = driverFactory.getDriver();

        // Timeout maior para páginas mais lentas
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.webActions = new WebActions(driver);
        this.loginMap = new LoginMap();
        PageFactory.initElements(driver, loginMap);
    }

    public void iniciarNavegador() throws IOException {
        String step = "Acessar website Advantage Shopping";
        webActions.navigateURL("https://www.advantageonlineshopping.com/#/");

        // Espera explícita até o logo aparecer (ponto de referência do carregamento)
        wait.until(ExpectedConditions.visibilityOf(loginMap.getLogoAdvantage()));

        // Aguarda loader sumir se existir
        try {
            wait.until(ExpectedConditions.invisibilityOf(loginMap.getLoader()));
        } catch (Exception ignored) {
        }

        TestReport report = new TestReport(driver);
        report.captureScreenshot(step);
        log.info(step);
    }

    public void clickBtnLogin() throws IOException {
        String step = "Clicar no botao login";

        // Espera o ícone do usuário estar clicável e loader invisível
        try {
            wait.until(ExpectedConditions.invisibilityOf(loginMap.getLoader()));
        } catch (Exception ignored) {
        }
        wait.until(ExpectedConditions.elementToBeClickable(loginMap.getIconUser()));

        webActions.click(loginMap.getIconUser());

        // Após clicar, espera o link de criar conta ser visível (ou modal)
        wait.until(ExpectedConditions.visibilityOf(loginMap.getLinkCreateAccount()));

        TestReport report = new TestReport(driver);
        report.captureScreenshot(step);
        log.info(step);
    }

    public void clickBtnFechar() {
        String step = "Clicar no botao fechar modal login";

        // Espera o loader sumir e o botão fechar clicável
        try {
            wait.until(ExpectedConditions.invisibilityOf(loginMap.getLoader()));
        } catch (Exception ignored) {
        }
        wait.until(ExpectedConditions.elementToBeClickable(loginMap.getIconFechar()));
        webActions.click(loginMap.getIconFechar());

        // Espera até o botão fechar desaparecer (modal fechado)
        try {
            wait.until(ExpectedConditions.invisibilityOf(loginMap.getIconFechar()));
        } catch (Exception ignored) {
        }

        TestReport report = new TestReport(driver);
        report.captureScreenshot(step);
        log.info(step);
    }

    public void inserirDadosLogin() {
        String step = "Inserir dados de login";

        // Garante que os campos estejam visíveis e utilizáveis
        wait.until(ExpectedConditions.visibilityOf(loginMap.getInpUsername()));
        wait.until(ExpectedConditions.visibilityOf(loginMap.getInpPassword()));

        webActions.insertText(loginMap.getInpUsername(), "DanDama");
        webActions.insertText(loginMap.getInpPassword(), "Dan$23141812");

        // Aguarda possível loader desaparecer
        try {
            wait.until(ExpectedConditions.invisibilityOf(loginMap.getLoader()));
        } catch (Exception ignored) {
        }

        TestReport report = new TestReport(driver);
        report.captureScreenshot(step);
        log.info(step);
    }

    public void clickRememberMe() {
        String step = "Clicar em Remember Me";
        TestReport report = new TestReport(driver);

        // Tenta clicar respeitando overlay/loader com retries
        int attempts = 0;
        final int maxAttempts = 4;
        boolean clicked = false;

        while (attempts < maxAttempts && !clicked) {
            try {
                // Aguarda loader desaparecer (se existir) antes de tentar
                try {
                    wait.until(ExpectedConditions.invisibilityOf(loginMap.getLoader()));
                } catch (Exception ignored) {
                }

                // Aguarda elemento clicável
                wait.until(ExpectedConditions.elementToBeClickable(loginMap.getClickRememberMe()));

                webActions.click(loginMap.getClickRememberMe());
                clicked = true;
                // captura evidência do passo
                report.captureScreenshot(step);
                log.info(step);
            } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                attempts++;
                log.warn("Tentativa " + attempts + " de clicar em Remember Me falhou por interceptação/stale. Mensagem: " + e.getMessage());
                // pequena espera antes de tentar novamente
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                // Erro inesperado: captura e re-lança após gerar relatório e fechar navegador
                log.error("Erro ao clicar em Remember Me: " + e.getMessage(), e);
                try {
                    report.captureScreenshot("Falha - " + step);
                    report.setTestStatus(false);
                    report.createPdfReport();
                } catch (Exception reportEx) {
                    log.error("Erro ao gerar PDF após falha em clickRememberMe: " + reportEx.getMessage(), reportEx);
                } finally {
                    DriverManager.quitDriver();
                }
                throw e;
            }
        }

        if (!clicked) {
            // Não conseguiu clicar após retries: trata como falha
            String msg = "Não foi possível clicar em Remember Me após " + maxAttempts + " tentativas (overlay/loader persistente).";
            log.error(msg);
            report.captureScreenshot("Falha - " + step + " - nao clicado");
            report.setTestStatus(false);
            try {
                report.createPdfReport();
            } catch (Exception ex) {
                log.error("Erro ao criar PDF após falha persistente em clickRememberMe: " + ex.getMessage(), ex);
            } finally {
                DriverManager.quitDriver();
            }
            throw new RuntimeException(msg);
        }
    }

    public void clickBtnSignIn() {
        String step = "Clicar no botao sign in";

        try {
            try {
                wait.until(ExpectedConditions.invisibilityOf(loginMap.getLoader()));
            } catch (Exception ignored) {
            }
            wait.until(ExpectedConditions.elementToBeClickable(loginMap.getBtnSignIn()));
            webActions.click(loginMap.getBtnSignIn());

            // Após submeter, espere loader desaparecer e o elemento de confirmação aparecer
            try {
                wait.until(ExpectedConditions.invisibilityOf(loginMap.getLoader()));
            } catch (Exception ignored) {
            }

            TestReport report = new TestReport(driver);
            report.captureScreenshot(step);
            log.info(step);
        } catch (Exception e) {
            TestReport report = new TestReport(driver);
            log.error("Erro ao clicar em Sign In: " + e.getMessage(), e);
            report.captureScreenshot("Falha - " + step);
            report.setTestStatus(false);
            try {
                report.createPdfReport();
            } catch (Exception ex) {
                log.error("Erro ao gerar PDF após falha em clickBtnSignIn: " + ex.getMessage(), ex);
            } finally {
                DriverManager.quitDriver();
            }
            throw e;
        }
    }

    public void validarFecharModal() {
        String step = "Validar fechar modal login";
        TestReport report = new TestReport(driver);

        try {
            // Caso exista o ícone fechar, aguarda sua invisibilidade
            try {
                wait.until(ExpectedConditions.invisibilityOf(loginMap.getIconFechar()));
            } catch (Exception ignored) {
            }

            // Agora verifica que o link de criar conta NÃO está visível (modal fechado)
            boolean isCreateAccountVisible;
            try {
                isCreateAccountVisible = loginMap.getLinkCreateAccount().isDisplayed();
            } catch (Exception e) {
                // Se lançar exceção ao buscar o elemento, assume que não está visível
                isCreateAccountVisible = false;
            }

            Assert.assertFalse("O modal de login deveria estar fechado", isCreateAccountVisible);

            // Se chegou aqui, passou
            report.captureScreenshot(step);
            report.setTestStatus(true);
            log.info(step);
        } catch (AssertionError | Exception e) {
            log.error("Falha em validarFecharModal: " + e.getMessage(), e);
            report.captureScreenshot("Falha - " + step);
            report.setTestStatus(false);
            throw e; // re-lança para que o framework registre o erro também
        } finally {
            try {
                report.createPdfReport();
            } catch (Exception e) {
                log.error("Erro ao gerar PDF em validarFecharModal: " + e.getMessage(), e);
            } finally {
                // Garante fechamento do navegador imediatamente (proteção extra caso hooks não executem)
                DriverManager.quitDriver();
            }
        }
    }

    public void validarLogin() {
        String step = "Validar login realizado com sucesso";
        TestReport report = new TestReport(driver);

        try {
            // Espera o elemento que mostra o usuário logado
            wait.until(ExpectedConditions.visibilityOf(loginMap.getTextDataLogin()));

            Assert.assertEquals("DanDama", loginMap.getTextDataLogin().getText());

            report.captureScreenshot(step);
            report.setTestStatus(true);
            log.info(step);
        } catch (AssertionError | Exception e) {
            log.error("Falha em validarLogin: " + e.getMessage(), e);
            report.captureScreenshot("Falha - " + step);
            report.setTestStatus(false);
            throw e;
        } finally {
            try {
                report.createPdfReport();
            } catch (Exception ex) {
                log.error("Erro ao gerar PDF após validarLogin: " + ex.getMessage(), ex);
            }
            DriverManager.quitDriver();
        }
    }
}