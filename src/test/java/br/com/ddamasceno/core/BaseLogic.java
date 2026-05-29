package br.com.ddamasceno.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Classe base para todas as classes de lógica de teste.
 *
 * <p>Fornece acesso centralizado ao {@link WebActions} e ao {@link TestReport}
 * via métodos acessores ({@link #webActions()} e {@link #report()}), em vez de
 * campos diretos.
 *
 * <p>Padrão de uso nas subclasses:
 * <pre>
 * // Capturar evidência de um passo (escolha onde quer screenshot)
 * report().registerStep(webActions().getScreenshot(), step, "screenshot");
 *
 * // Navegar sem evidência (sem screenshot)
 * webActions().navigateURL("https://...");
 *
 * // Aguardar elemento com timeout customizado
 * webActions().waitVisibleElement(elemento, 30);
 * </pre>
 *
 * <p>O relatório é registrado no {@link ReportContext} e finalizado pelo
 * Cucumber Hook {@code @After} em {@link Hooks}, garantindo que todos os passos
 * de um cenário sejam consolidados em um único PDF.
 */
public abstract class BaseLogic {

    private WebActions _webActions;
    private TestReport _report;
    private WebDriverWait _defaultWait;

    /**
     * Inicializa os acessores de ação e relatório.
     * Deve ser chamado no construtor de cada subclasse, após criar o driver.
     *
     * @param driver driver ativo para o cenário
     */
    protected void initCore(WebDriver driver) {
        this._webActions = new WebActions(driver);
        this._report     = new TestReport(driver);
        this._defaultWait = new WebDriverWait(driver, Duration.ofSeconds(WebActions.DEFAULT_TIMEOUT_SECONDS));
        ReportContext.set(_report);
    }

    /**
     * Retorna a instância de {@link WebActions} para interagir com o browser.
     *
     * <p>Exemplo: {@code webActions().click(loginMap.getBtnSignIn())}
     */
    protected WebActions webActions() {
        return _webActions;
    }

    /**
     * Retorna a instância de {@link TestReport} para registrar evidências.
     *
     * <p>Exemplo: {@code report().registerStep(webActions().getScreenshot(), step, "screenshot")}
     */
    protected TestReport report() {
        return _report;
    }

    /**
     * Retorna um {@link WebDriverWait} com o timeout padrão ({@link WebActions#DEFAULT_TIMEOUT_SECONDS}s).
     * Subclasses podem criar waits adicionais com timeouts maiores quando necessário.
     */
    protected WebDriverWait defaultWait() {
        return _defaultWait;
    }
}
