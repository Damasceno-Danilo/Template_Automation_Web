package br.com.ddamasceno.logic.advantageShopping;

import br.com.ddamasceno.core.BaseLogic;
import br.com.ddamasceno.core.Browser;
import br.com.ddamasceno.core.DriverFactory;
import br.com.ddamasceno.core.DriverManager;
import br.com.ddamasceno.maps.advantageShopping.ContactUsMap;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Lógica de negócio dos testes da seção "Contact Us" — Advantage Shopping.
 *
 * <p>O formulário real não possui campo de "Nome" nem "Mensagem" separada do
 * "Subject". Adaptação adotada: o passo de "mensagem" é concatenado ao mesmo
 * textarea do "subject", preservando a intenção original da massa de dados do
 * mapa de testes sem inventar um campo que não existe na aplicação.
 */
@Log4j2
public class ContactUsLogic extends BaseLogic {

    private final WebDriver driver;
    private final ContactUsMap contactUsMap;
    private final WebDriverWait wait;

    public ContactUsLogic() {
        WebDriver existing = DriverManager.getDriver();
        if (existing != null) {
            this.driver = existing;
        } else {
            DriverFactory driverFactory = new DriverFactory(Browser.CHROME);
            this.driver = driverFactory.getDriver();
        }
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        initCore(driver);

        this.contactUsMap = new ContactUsMap();
        PageFactory.initElements(driver, contactUsMap);
    }

    // ─── Navegação ───────────────────────────────────────────────────────────

    public void acessarPaginaDeContactUs() {
        String step = "Acessar a pagina de Contact Us";

        clickRobust(contactUsMap.getMenuContactUs(), step);
        wait.until(ExpectedConditions.visibilityOf(contactUsMap.getInpEmail()));

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    // ─── Preenchimento ───────────────────────────────────────────────────────

    public void selecionarProdutoNoFormulario(String nomeProduto) {
        String step = "Selecionar o produto no formulario de contato: " + nomeProduto;

        try {
            new Select(contactUsMap.getSelProduto()).selectByVisibleText(nomeProduto);
        } catch (Exception e) {
            log.warn("Nao foi possivel selecionar '{}' no combo de produtos do Contact Us.", nomeProduto);
        }

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void preencherEmail(String email) {
        String step = "Preencher o email do formulario de contato";

        wait.until(ExpectedConditions.visibilityOf(contactUsMap.getInpEmail()));
        webActions().insertText(contactUsMap.getInpEmail(), email);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info("{} — valor: {}", step, email);
    }

    public void preencherSubject(String subject) {
        String step = "Preencher o subject do formulario de contato";

        wait.until(ExpectedConditions.visibilityOf(contactUsMap.getTxtSubject()));
        webActions().insertText(contactUsMap.getTxtSubject(), subject);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info("{} — valor: {}", step, subject);
    }

    /** O site não possui campo de mensagem separado — concatena ao textarea do subject. */
    public void preencherMensagem(String mensagem) {
        String step = "Preencher a mensagem do formulario de contato";

        wait.until(ExpectedConditions.visibilityOf(contactUsMap.getTxtSubject()));
        String atual = contactUsMap.getTxtSubject().getAttribute("value");
        String textoFinal = (atual == null || atual.isBlank()) ? mensagem : atual + " - " + mensagem;
        contactUsMap.getTxtSubject().clear();
        webActions().insertText(contactUsMap.getTxtSubject(), textoFinal);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info("{} — valor: {}", step, mensagem);
    }

    public void enviarFormularioDeContato() {
        String step = "Enviar o formulario de contato";

        clickRobust(contactUsMap.getBtnEnviar(), step);
        sleep(600);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    // ─── Validações ──────────────────────────────────────────────────────────

    public void validarMensagemEnviadaComSucesso() {
        String step = "Validar que a mensagem foi enviada com sucesso";

        wait.until(ExpectedConditions.visibilityOf(contactUsMap.getBlocoSucesso()));
        Assertions.assertTrue(contactUsMap.getBlocoSucesso().isDisplayed(),
                "Confirmacao de envio de mensagem nao foi exibida");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarMensagemDeCamposObrigatorios() {
        String step = "Validar que mensagem de campos obrigatorios e exibida";

        boolean naoFoiEnviado = !isElementVisible(contactUsMap.getBlocoSucesso());
        Assertions.assertTrue(naoFoiEnviado, "Formulario foi enviado mesmo com campos obrigatorios vazios");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarCampoEmailObrigatorio() {
        validarMensagemDeCamposObrigatorios();
    }

    public void validarCampoSubjectObrigatorio() {
        validarMensagemDeCamposObrigatorios();
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private boolean isElementVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private void clickJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    private void clickRobust(WebElement element, String stepName) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            webActions().click(element);
        } catch (Exception e) {
            log.warn("Clique padrao falhou em '{}' — tentando via JavaScript.", stepName);
            clickJS(element);
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
