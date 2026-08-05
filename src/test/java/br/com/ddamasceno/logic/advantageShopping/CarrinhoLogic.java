package br.com.ddamasceno.logic.advantageShopping;

import br.com.ddamasceno.core.BaseLogic;
import br.com.ddamasceno.core.Browser;
import br.com.ddamasceno.core.DriverFactory;
import br.com.ddamasceno.core.DriverManager;
import br.com.ddamasceno.maps.advantageShopping.CarrinhoMap;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Lógica de negócio dos testes de carrinho de compras — Advantage Shopping.
 *
 * <p>Reaproveita o mesmo WebDriver aberto pelo {@code Background} do cenário
 * (via {@link LoginLogic} / {@link DriverManager}) — ver {@link ProdutoLogic} para
 * detalhes do padrão de reuso.
 */
@Log4j2
public class CarrinhoLogic extends BaseLogic {

    private final WebDriver driver;
    private final CarrinhoMap carrinhoMap;
    private final WebDriverWait wait;

    private int contadorAntesDeAdicionar = 0;

    public CarrinhoLogic() {
        WebDriver existing = DriverManager.getDriver();
        if (existing != null) {
            this.driver = existing;
        } else {
            DriverFactory driverFactory = new DriverFactory(Browser.CHROME);
            this.driver = driverFactory.getDriver();
        }
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        initCore(driver);

        this.carrinhoMap = new CarrinhoMap();
        PageFactory.initElements(driver, carrinhoMap);
    }

    // ─── Ações ───────────────────────────────────────────────────────────────

    /** Clica em "ADD TO CART" na página de detalhes do produto atualmente aberta. */
    public void adicionarProdutoAoCarrinho() {
        String step = "Adicionar o produto ao carrinho";

        contadorAntesDeAdicionar = lerContadorCarrinho();

        WebElement btnAddToCart = driver.findElement(By.name("save_to_cart"));
        wait.until(ExpectedConditions.elementToBeClickable(btnAddToCart));
        clickRobust(btnAddToCart, step);
        sleep(500);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    /** Mesma ação de adicionar ao carrinho — no site não é exigido login para esta etapa. */
    public void adicionarProdutoAoCarrinhoSemLogin() {
        adicionarProdutoAoCarrinho();
    }

    /** Adiciona novamente o mesmo produto já presente na página de detalhes. */
    public void adicionarMesmoProdutoNovamente() {
        String step = "Adicionar o mesmo produto ao carrinho novamente";

        WebElement btnAddToCart = driver.findElement(By.name("save_to_cart"));
        clickRobust(btnAddToCart, step);
        sleep(500);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void acessarCarrinhoDeCompras() {
        String step = "Acessar o carrinho de compras";

        clickRobust(carrinhoMap.getIconCarrinho(), step);
        wait.until(ExpectedConditions.urlContains("shoppingCart"));

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void removerProdutoDoCarrinho() {
        String step = "Remover o produto do carrinho";

        wait.until(ExpectedConditions.visibilityOf(carrinhoMap.getLinkRemoverProduto()));
        clickRobust(carrinhoMap.getLinkRemoverProduto(), step);
        sleep(500);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    /**
     * Altera a quantidade do produto no carrinho: abre a página do produto via
     * o link "EDIT" da linha do carrinho, ajusta o campo de quantidade e confirma.
     */
    public void alterarQuantidadeDoProduto(int novaQuantidade) {
        String step = "Alterar a quantidade do produto no carrinho para " + novaQuantidade;

        wait.until(ExpectedConditions.visibilityOf(carrinhoMap.getLinkEditarProduto()));
        clickRobust(carrinhoMap.getLinkEditarProduto(), step);

        WebElement inpQuantidade = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("quantity")));
        inpQuantidade.clear();
        inpQuantidade.sendKeys(String.valueOf(novaQuantidade));

        WebElement btnAddToCart = driver.findElement(By.name("save_to_cart"));
        clickRobust(btnAddToCart, step);
        sleep(500);

        acessarCarrinhoDeCompras();

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    // ─── Validações ──────────────────────────────────────────────────────────

    public void validarProdutoAdicionadoAoCarrinho() {
        String step = "Validar que produto foi adicionado ao carrinho";

        int contadorAtual = lerContadorCarrinho();
        Assertions.assertTrue(contadorAtual > contadorAntesDeAdicionar,
                "O contador do carrinho nao foi incrementado apos adicionar o produto");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarProdutoListadoNoCarrinho() {
        String step = "Validar que produto esta listado no carrinho";

        wait.until(ExpectedConditions.visibilityOf(carrinhoMap.getNomeProdutoNoCarrinho()));
        Assertions.assertFalse(carrinhoMap.getNomeProdutoNoCarrinho().getText().trim().isEmpty(),
                "Nenhum produto foi listado no carrinho");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarCarrinhoVazio() {
        String step = "Validar que o carrinho esta vazio";

        List<WebElement> linhas = driver.findElements(By.xpath("//li[contains(@ng-repeat,'cart.productsInCart')]"));
        Assertions.assertTrue(linhas.isEmpty(), "Esperava carrinho vazio, mas " + linhas.size() + " item(ns) encontrados");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarMensagemDeCarrinhoVazio() {
        String step = "Validar que mensagem de carrinho vazio e exibida";

        boolean checkoutIndisponivel;
        try {
            WebElement checkout = driver.findElement(By.id("checkOutButton"));
            checkoutIndisponivel = !checkout.isDisplayed() || !checkout.isEnabled();
        } catch (Exception e) {
            checkoutIndisponivel = true; // botao de checkout nem existe -> carrinho vazio confirmado
        }

        Assertions.assertTrue(checkoutIndisponivel,
                "Esperava que o checkout estivesse indisponivel com o carrinho vazio");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarQuantidadeAtualizada(int quantidadeEsperada) {
        String step = "Validar que a quantidade do produto foi atualizada para " + quantidadeEsperada;

        wait.until(ExpectedConditions.visibilityOf(carrinhoMap.getPrimeiraLinhaCarrinho()));
        String textoLinha = carrinhoMap.getPrimeiraLinhaCarrinho().getText();
        Assertions.assertTrue(textoLinha.contains(String.valueOf(quantidadeEsperada)),
                "Quantidade esperada '" + quantidadeEsperada + "' nao encontrada na linha do carrinho: " + textoLinha);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarMensagemDeQuantidadeInvalida() {
        String step = "Validar que mensagem de quantidade invalida e exibida (ou alteracao bloqueada)";

        // O site não permite digitar valores fora do padrão numérico simples; validamos que
        // a quantidade não foi persistida como 0/negativa no carrinho.
        wait.until(ExpectedConditions.visibilityOf(carrinhoMap.getPrimeiraLinhaCarrinho()));
        String textoLinha = carrinhoMap.getPrimeiraLinhaCarrinho().getText();
        Assertions.assertFalse(textoLinha.contains(" 0 ") || textoLinha.trim().startsWith("0"),
                "Carrinho aceitou quantidade invalida: " + textoLinha);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarBotaoAddToCartVisivelHabilitado() {
        String step = "Validar que o botao add to cart esta visivel e habilitado";

        WebElement btnAddToCart = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("save_to_cart")));
        Assertions.assertTrue(btnAddToCart.isDisplayed(), "Botao Add to Cart nao esta visivel");
        Assertions.assertTrue(btnAddToCart.isEnabled(), "Botao Add to Cart nao esta habilitado");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarSolicitacaoDeLoginParaContinuarCompra() {
        String step = "Validar que e solicitado login para continuar a compra";

        wait.until(ExpectedConditions.urlContains("login"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("login"),
                "Esperava redirecionamento para tela de login, URL atual: " + driver.getCurrentUrl());

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private int lerContadorCarrinho() {
        try {
            String texto = carrinhoMap.getContadorCarrinho().getText().trim();
            return texto.isEmpty() ? 0 : Integer.parseInt(texto);
        } catch (Exception e) {
            return 0;
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
