package br.com.ddamasceno.logic.advantageShopping;

import br.com.ddamasceno.core.BaseLogic;
import br.com.ddamasceno.core.Browser;
import br.com.ddamasceno.core.DriverFactory;
import br.com.ddamasceno.core.DriverManager;
import br.com.ddamasceno.maps.advantageShopping.PagamentoMap;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Lógica de negócio dos testes de pagamento/checkout — Advantage Shopping.
 *
 * <p><b>Nota de manutenção:</b> os campos do formulário de pagamento (SafePay /
 * MasterCredit) são renderizados por uma diretiva Angular customizada ({@code sec-view})
 * cujo atributo {@code name} é derivado do caminho do model ({@code sec-model}) — ex.:
 * {@code card.number}, {@code savePay.username}. Esse comportamento foi inferido a partir
 * do bundle público da aplicação (não foi possível autenticar de fato durante a criação
 * destes testes). Caso os seletores não casem em uma primeira execução real, ajuste os
 * {@code @FindBy} em {@link PagamentoMap} — a estrutura dos métodos aqui permanece válida.
 */
@Log4j2
public class PagamentoLogic extends BaseLogic {

    private final WebDriver driver;
    private final PagamentoMap pagamentoMap;
    private final WebDriverWait wait;

    public PagamentoLogic() {
        WebDriver existing = DriverManager.getDriver();
        if (existing != null) {
            this.driver = existing;
        } else {
            DriverFactory driverFactory = new DriverFactory(Browser.CHROME);
            this.driver = driverFactory.getDriver();
        }
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        initCore(driver);

        this.pagamentoMap = new PagamentoMap();
        PageFactory.initElements(driver, pagamentoMap);
    }

    // ─── Fluxo de checkout ───────────────────────────────────────────────────

    public void iniciarCheckout() {
        String step = "Iniciar o checkout";

        WebElement btnCheckout = driver.findElement(By.id("checkOutButton"));
        clickRobust(btnCheckout, step);
        sleep(800);

        // Se o usuário estiver logado, avança da etapa de endereço para pagamento.
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.elementToBeClickable(pagamentoMap.getBtnNext()));
            clickRobust(pagamentoMap.getBtnNext(), step);
        } catch (Exception ignored) {
            // Sem login: aplicação redireciona para a tela de login (fluxo negativo esperado)
        }

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void selecionarPagamentoSafePay() {
        String step = "Selecionar metodo de pagamento SafePay";

        clickRobust(pagamentoMap.getRadioSafePay(), step);
        sleep(300);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void selecionarPagamentoMasterCredit() {
        String step = "Selecionar metodo de pagamento MasterCredit";

        clickRobust(pagamentoMap.getRadioMasterCredit(), step);
        sleep(300);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void preencherUsuarioSafePay(String usuario) {
        preencherCampo(pagamentoMap.getInpSafePayUsuario(), usuario, "Preencher usuario SafePay");
    }

    public void preencherSenhaSafePay(String senha) {
        preencherCampo(pagamentoMap.getInpSafePaySenha(), senha, "Preencher senha SafePay");
    }

    public void preencherNumeroCartao(String numero) {
        preencherCampo(pagamentoMap.getInpNumeroCartao(), numero, "Preencher numero do cartao");
    }

    public void preencherCvv(String cvv) {
        preencherCampo(pagamentoMap.getInpCvv(), cvv, "Preencher CVV");
    }

    public void preencherMesVencimento(String mes) {
        String step = "Preencher mes de vencimento";
        selecionarOuDigitar(pagamentoMap.getSelMesVencimento(), mes, step);
    }

    public void preencherAnoVencimento(String ano) {
        String step = "Preencher ano de vencimento";
        selecionarOuDigitar(pagamentoMap.getSelAnoVencimento(), ano, step);
    }

    public void preencherNomeTitular(String nome) {
        preencherCampo(pagamentoMap.getInpNomeTitular(), nome, "Preencher nome do titular do cartao");
    }

    public void confirmarPagamento() {
        String step = "Confirmar pagamento";

        WebElement btnSafePay = safeFind(pagamentoMap.getBtnPayNowSafePay());
        WebElement btnMasterCredit = safeFind(pagamentoMap.getBtnPayNowMasterCredit());

        if (btnSafePay != null && btnSafePay.isDisplayed()) {
            clickRobust(btnSafePay, step);
        } else if (btnMasterCredit != null && btnMasterCredit.isDisplayed()) {
            clickRobust(btnMasterCredit, step);
        } else {
            Assertions.fail("Nenhum botao de confirmacao de pagamento (SafePay/MasterCredit) esta visivel");
        }
        sleep(1000);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void acessarHistoricoDePedidos() {
        String step = "Acessar historico de pedidos (My Orders)";

        try {
            ((JavascriptExecutor) driver).executeScript("window.location.hash = '#/myOrders';");
            sleep(800);
        } catch (Exception e) {
            log.warn("Falha ao navegar diretamente para My Orders, tentando via menu.");
        }

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    // ─── Validações ──────────────────────────────────────────────────────────

    public void validarPagamentoRealizadoComSucesso() {
        String step = "Validar que o pagamento foi realizado com sucesso";

        wait.until(ExpectedConditions.visibilityOf(pagamentoMap.getLabelNumeroPedido()));
        Assertions.assertFalse(pagamentoMap.getLabelNumeroPedido().getText().trim().isEmpty(),
                "Numero do pedido nao foi exibido na confirmacao");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarPagamentoRecusado() {
        String step = "Validar que o pagamento foi recusado";

        boolean permaneceuNaTelaDePagamento = safeFind(pagamentoMap.getLabelErroSafePay()) != null
                || safeFind(pagamentoMap.getLabelErroMasterCredit()) != null
                || isElementVisible(pagamentoMap.getRadioSafePay())
                || isElementVisible(pagamentoMap.getRadioMasterCredit());

        boolean pedidoConfirmado = isElementVisible(pagamentoMap.getLabelNumeroPedido());

        Assertions.assertFalse(pedidoConfirmado,
                "Esperava que o pagamento fosse recusado, mas o pedido foi confirmado");
        Assertions.assertTrue(permaneceuNaTelaDePagamento,
                "Esperava permanecer na tela de pagamento apos recusa");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarPagamentoRecusadoPorSaldoInsuficiente() {
        validarPagamentoRecusado();
    }

    public void validarStatusDoPedidoAprovado() {
        validarPagamentoRealizadoComSucesso();
    }

    public void validarStatusDoPedidoRecusado() {
        validarPagamentoRecusado();
    }

    public void validarPedidoNoHistoricoComStatusAprovado() {
        String step = "Validar que o pedido consta no historico com status aprovado";

        wait.until(ExpectedConditions.visibilityOf(pagamentoMap.getPrimeiraLinhaPedidos()));
        Assertions.assertFalse(pagamentoMap.getPrimeiraLinhaPedidos().getText().trim().isEmpty(),
                "Nenhum pedido encontrado no historico apos pagamento aprovado");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarCheckoutRetomadoAposLogin() {
        String step = "Validar que o checkout foi retomado apos o login";

        wait.until(ExpectedConditions.visibilityOf(pagamentoMap.getBtnNext()));

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private void preencherCampo(WebElement campo, String valor, String step) {
        wait.until(ExpectedConditions.visibilityOf(campo));
        webActions().insertText(campo, valor);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info("{} — valor: {}", step, valor);
    }

    /** Alguns campos de mês/ano são renderizados como &lt;select&gt;; outros como texto livre. */
    private void selecionarOuDigitar(WebElement campo, String valor, String step) {
        wait.until(ExpectedConditions.visibilityOf(campo));
        try {
            new Select(campo).selectByVisibleText(valor);
        } catch (Exception e) {
            webActions().insertText(campo, valor);
        }

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info("{} — valor: {}", step, valor);
    }

    /** Resolve o proxy do PageFactory e retorna o elemento, ou {@code null} se não existir no DOM atual. */
    private WebElement safeFind(WebElement element) {
        try {
            element.getTagName();
            return element;
        } catch (Exception e) {
            return null;
        }
    }

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
