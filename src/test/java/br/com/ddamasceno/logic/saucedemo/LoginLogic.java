package br.com.ddamasceno.logic.saucedemo;

import br.com.ddamasceno.core.BaseLogic;
import br.com.ddamasceno.core.Browser;
import br.com.ddamasceno.core.DriverFactory;
import br.com.ddamasceno.core.config.TestDataConfig;
import br.com.ddamasceno.maps.saucedemo.LoginMap;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Lógica de negócio dos testes de login — Sauce Demo (https://www.saucedemo.com/).
 *
 * <p>Padrão de evidência: chame {@code report().registerStep(webActions().getScreenshot(), step, "screenshot")}
 * nos passos onde deseja evidência no PDF.
 */
@Log4j2
public class LoginLogic extends BaseLogic {

    private final WebDriver driver;
    private final LoginMap loginMap;
    private final WebDriverWait wait;

    public LoginLogic() {
        DriverFactory driverFactory = new DriverFactory(Browser.CHROME);
        this.driver = driverFactory.getDriver();
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));

        initCore(driver);

        this.loginMap = new LoginMap();
        PageFactory.initElements(driver, loginMap);
    }

    // ─── Navegação ────────────────────────────────────────────────────────────

    public void iniciarNavegador() {
        String step = "Acessar website Sauce Demo";

        webActions().navigateURL("https://www.saucedemo.com/");
        wait.until(ExpectedConditions.visibilityOf(loginMap.getBtnLogin()));

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    // ─── Preenchimento de formulário ──────────────────────────────────────────

    public void inserirDadosLogin(String username, String password) {
        String step = "Inserir dados de login";

        if (username != null && !username.isEmpty()) {
            webActions().insertText(loginMap.getInpUsername(), username);
        }
        if (password != null && !password.isEmpty()) {
            webActions().insertText(loginMap.getInpPassword(), password);
        }

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info("{} — usuário: {}", step, username);
    }

    public void clickBtnLogin() {
        String step = "Clicar no botao login";

        webActions().click(loginMap.getBtnLogin());

        log.info(step);
    }

    // ─── Validações ───────────────────────────────────────────────────────────

    public void validarLogin() {
        String step = "Validar login realizado com sucesso";

        wait.until(ExpectedConditions.urlContains("inventory.html"));
        wait.until(ExpectedConditions.visibilityOf(loginMap.getPageTitle()));
        Assertions.assertEquals("Products", loginMap.getPageTitle().getText());

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarLoginFalhou(String mensagemEsperada) {
        String step = "Validar login nao realizado";

        wait.until(ExpectedConditions.visibilityOf(loginMap.getErrorMessage()));
        Assertions.assertEquals(mensagemEsperada, loginMap.getErrorMessage().getText());

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }
}
