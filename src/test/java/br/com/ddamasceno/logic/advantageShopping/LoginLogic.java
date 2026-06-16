package br.com.ddamasceno.logic.advantageShopping;

import br.com.ddamasceno.core.BaseLogic;
import br.com.ddamasceno.core.Browser;
import br.com.ddamasceno.core.DriverFactory;
import br.com.ddamasceno.core.config.TestDataConfig;
import br.com.ddamasceno.maps.advantageShopping.LoginMap;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Lógica de negócio dos testes de login — Advantage Shopping.
 *
 * <p>Padrão de evidência:
 * <ul>
 *   <li>Chame {@code report().registerStep(webActions().getScreenshot(), step, "screenshot")}
 *       nos passos onde deseja evidência no PDF.</li>
 *   <li>Passos sem essa chamada executam normalmente, sem gerar screenshot.</li>
 * </ul>
 *
 * <p>O relatório é finalizado automaticamente pelo Hook {@code @After} em {@link br.com.ddamasceno.core.Hooks}.
 */
@Log4j2
public class LoginLogic extends BaseLogic {

    private final WebDriver driver;
    private final LoginMap loginMap;

    /**
     * Wait de 30s para páginas mais lentas (Advantage Shopping).
     * Use {@code defaultWait()} para interações rápidas padrão.
     */
    private final WebDriverWait wait;

    /** Último username utilizado — necessário para validar falha de login. */
    private String lastUsername;

    public LoginLogic() {
        DriverFactory driverFactory = new DriverFactory(Browser.CHROME);
        this.driver = driverFactory.getDriver();
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Inicializa webActions() e report() — registra relatório no ReportContext
        initCore(driver);

        this.loginMap = new LoginMap();
        PageFactory.initElements(driver, loginMap);
    }

    // ─── Passos de navegação ──────────────────────────────────────────────────

    public void iniciarNavegador() {
        String step = "Acessar website Advantage Shopping";

        webActions().navigateURL("https://www.advantageonlineshopping.com/#/");
        wait.until(ExpectedConditions.visibilityOf(loginMap.getLogoAdvantage()));
        ignoreLoader();

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void clickBtnLogin() {
        String step = "Clicar no botao login";

        ignoreLoader();
        wait.until(ExpectedConditions.elementToBeClickable(loginMap.getIconUser()));
        webActions().click(loginMap.getIconUser());
        wait.until(ExpectedConditions.visibilityOf(loginMap.getLinkCreateAccount()));

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void clickBtnFechar() {
        String step = "Clicar no botao fechar modal login";

        clickRobust(loginMap.getIconFechar(), step);
        ignoreInvisibility(loginMap.getIconFechar());

        // Screenshot — evidência de que o botão foi clicado
        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    // ─── Preenchimento de formulário ──────────────────────────────────────────

    /** Sobrecarga sem parâmetros — usa credencial válida do TestDataConfig. */
    public void inserirDadosLogin() {
        inserirDadosLogin(
                TestDataConfig.get("test.user.valid"),
                TestDataConfig.get("test.password.valid")
        );
    }

    /**
     * Preenche usuário e senha.
     *
     * @param username nome de usuário (já resolvido pelo step via TestDataConfig.resolve)
     * @param password senha (já resolvida pelo step)
     */
    public void inserirDadosLogin(String username, String password) {
        String step = "Inserir dados de login";
        this.lastUsername = username;

        wait.until(ExpectedConditions.visibilityOf(loginMap.getInpUsername()));
        wait.until(ExpectedConditions.visibilityOf(loginMap.getInpPassword()));

        webActions().insertText(loginMap.getInpUsername(), username);
        webActions().insertText(loginMap.getInpPassword(), password);
        ignoreLoader();

        // Screenshot — evidência do preenchimento (sem expor a senha no nome do passo)
        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info("{} — usuário: {}", step, username);
    }

    public void clickRememberMe() {
        String step = "Clicar em Remember Me";

        clickRobust(loginMap.getClickRememberMe(), step);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void clickBtnSignIn() {
        String step = "Clicar no botao Sign In";

        clickRobust(loginMap.getBtnSignIn(), step);
        ignoreLoader();

        // Sem screenshot aqui — evidência será capturada na validação seguinte
        log.info(step);
    }

    // ─── Validações ───────────────────────────────────────────────────────────

    public void validarFecharModal() {
        String step = "Validar fechar modal login";

        ignoreInvisibility(loginMap.getIconFechar());

        boolean isCreateAccountVisible;
        try {
            isCreateAccountVisible = loginMap.getLinkCreateAccount().isDisplayed();
        } catch (Exception e) {
            isCreateAccountVisible = false;
        }

        Assertions.assertFalse(isCreateAccountVisible, "O modal de login deveria estar fechado");

        // Screenshot — evidência do modal fechado
        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarLogin() {
        String step = "Validar login realizado com sucesso";

        wait.until(ExpectedConditions.visibilityOf(loginMap.getTextDataLogin()));
        Assertions.assertEquals(
                TestDataConfig.get("test.user.valid"),
                loginMap.getTextDataLogin().getText()
        );

        // Screenshot — evidência do login bem-sucedido
        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarLoginFalhou() {
        String step = "Validar login nao realizado";

        ignoreLoader();

        boolean loggedIn = false;
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.visibilityOf(loginMap.getTextDataLogin()));
            String text = loginMap.getTextDataLogin().getText();
            loggedIn = text != null && lastUsername != null && text.equals(lastUsername);
        } catch (Exception ignored) {
            // Elemento não visível = usuário não logado (comportamento esperado)
        }

        Assertions.assertFalse(loggedIn,
                "Esperava que o login não fosse realizado, mas o usuário parece logado: " + lastUsername);

        // Screenshot — evidência de login negado
        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    // ─── Helpers privados ────────────────────────────────────────────────────

    /** Aguarda o loader desaparecer silenciosamente (se não existir, ignora). */
    private void ignoreLoader() {
        try {
            wait.until(ExpectedConditions.invisibilityOf(loginMap.getLoader()));
        } catch (Exception ignored) { }
    }

    /** Aguarda a invisibilidade de um elemento silenciosamente. */
    private void ignoreInvisibility(WebElement element) {
        try {
            wait.until(ExpectedConditions.invisibilityOf(element));
        } catch (Exception ignored) { }
    }

    /**
     * Clica em um elemento com retry automático para {@link ElementClickInterceptedException}
     * e {@link StaleElementReferenceException} — comum quando o loader sobrepõe o elemento
     * momentaneamente entre o check de "clicável" e o clique real (race condition típica
     * de ambientes headless/CI mais lentos ou mais rápidos que o local).
     *
     * <p>Se todas as tentativas falharem (ou o elemento nunca ficar clicável), cai para
     * um clique via JavaScript, que ignora overlays e estados de "disabled" via CSS/JS.
     */
    private void clickRobust(WebElement element, String stepName) {
        int attempts = 0;
        final int maxAttempts = 4;
        boolean clicked = false;
        Exception lastException = null;

        while (attempts < maxAttempts && !clicked) {
            try {
                ignoreLoader();
                wait.until(ExpectedConditions.elementToBeClickable(element));
                webActions().click(element);
                clicked = true;
            } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                lastException = e;
                attempts++;
                log.warn("Tentativa {} de clicar em '{}' falhou: {}", attempts, stepName, e.getClass().getSimpleName());
                sleep(400);
            } catch (TimeoutException e) {
                lastException = e;
                break;
            }
        }

        if (!clicked) {
            log.warn("Clique padrão falhou em '{}' (causa: {}) — tentando via JavaScript.",
                    stepName, lastException != null ? lastException.getClass().getSimpleName() : "desconhecida");
            webActions().clickJS(element);
        }
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
