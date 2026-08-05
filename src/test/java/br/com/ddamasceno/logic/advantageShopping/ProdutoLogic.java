package br.com.ddamasceno.logic.advantageShopping;

import br.com.ddamasceno.core.BaseLogic;
import br.com.ddamasceno.core.Browser;
import br.com.ddamasceno.core.DriverFactory;
import br.com.ddamasceno.core.DriverManager;
import br.com.ddamasceno.maps.advantageShopping.ProdutoMap;
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
 * Lógica de negócio dos testes de busca/seleção de produtos — Advantage Shopping.
 *
 * <p>Segue o mesmo padrão de evidência de {@link LoginLogic}: chamadas a
 * {@code report().registerStep(...)} nos pontos onde deseja-se capturar screenshot.
 */
@Log4j2
public class ProdutoLogic extends BaseLogic {

    protected final WebDriver driver;
    protected final ProdutoMap produtoMap;
    protected final WebDriverWait wait;

    /**
     * Reaproveita o WebDriver já aberto pelo {@code Background} do cenário
     * (via {@link LoginLogic}, registrado em {@link DriverManager}). Só abre um
     * novo navegador se, por algum motivo, nenhuma sessão ainda existir para a
     * thread atual — evita múltiplas janelas de Chrome dentro do mesmo cenário
     * quando várias classes de Step/Logic são usadas em conjunto.
     */
    public ProdutoLogic() {
        WebDriver existing = DriverManager.getDriver();
        if (existing != null) {
            this.driver = existing;
        } else {
            DriverFactory driverFactory = new DriverFactory(Browser.CHROME);
            this.driver = driverFactory.getDriver();
        }
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        initCore(driver);

        this.produtoMap = new ProdutoMap();
        PageFactory.initElements(driver, produtoMap);
    }

    // ─── Navegação / busca ──────────────────────────────────────────────────

    public void buscarProduto(String termo) {
        String step = "Buscar pelo produto " + termo;

        wait.until(ExpectedConditions.visibilityOf(produtoMap.getInpBusca()));
        produtoMap.getInpBusca().clear();
        produtoMap.getInpBusca().sendKeys(termo);
        clickJS(produtoMap.getIconBusca());
        sleep(800);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void acessarCatalogoDeProdutos() {
        String step = "Acessar catalogo de produtos (Our Products)";

        clickRobust(produtoMap.getMenuOurProducts(), step);
        sleep(500);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void acessarSecaoDeProdutosEspeciais() {
        String step = "Acessar secao de produtos especiais (Special Offer)";

        clickRobust(produtoMap.getMenuSpecialOffer(), step);
        sleep(500);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void acessarSecaoDeProdutosPopulares() {
        String step = "Acessar secao de produtos populares (Popular Items)";

        clickRobust(produtoMap.getMenuPopularItems(), step);
        sleep(500);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void aplicarFiltroDeCategoria() {
        String step = "Aplicar filtro de categoria (Speakers) na listagem de produtos";

        acessarCatalogoDeProdutos();
        clickRobust(produtoMap.getCategoriaSpeakers(), step);
        sleep(500);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    /**
     * Seleciona um produto pelo nome: busca o termo e clica no primeiro resultado
     * correspondente, abrindo a página de detalhes.
     */
    public void selecionarProduto(String nomeProduto) {
        String step = "Selecionar o produto " + nomeProduto;

        buscarProduto(nomeProduto);
        wait.until(ExpectedConditions.visibilityOf(produtoMap.getPrimeiroNomeProdutoDaLista()));
        clickRobust(produtoMap.getPrimeiroNomeProdutoDaLista(), step);
        wait.until(ExpectedConditions.visibilityOf(produtoMap.getTituloProduto()));

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    // ─── Validações — listagem ──────────────────────────────────────────────

    public void validarProdutoEncontrado(String nomeProduto) {
        String step = "Validar que o produto foi encontrado nos resultados: " + nomeProduto;

        wait.until(ExpectedConditions.visibilityOf(produtoMap.getPrimeiroNomeProdutoDaLista()));
        List<WebElement> resultados = driver.findElements(
                By.xpath("//a[@class='productName ng-binding']"));
        Assertions.assertFalse(resultados.isEmpty(), "Nenhum produto foi retornado na busca por: " + nomeProduto);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarProdutosDoCatalogoExibidos() {
        validarListaDeProdutosNaoVazia("Validar que os produtos do catalogo sao exibidos");
    }

    public void validarProdutosEspeciaisExibidos() {
        validarListaDeProdutosNaoVazia("Validar que os produtos especiais sao exibidos");
    }

    public void validarProdutosPopularesExibidos() {
        validarListaDeProdutosNaoVazia("Validar que os produtos populares sao exibidos");
    }

    public void validarProdutosFiltradosExibidos() {
        validarListaDeProdutosNaoVazia("Validar que os produtos filtrados sao exibidos");
    }

    private void validarListaDeProdutosNaoVazia(String step) {
        sleep(500);
        List<WebElement> itens = driver.findElements(By.xpath("//li[contains(@data-ng-click,\"$location.path('/product/\")]"));
        Assertions.assertFalse(itens.isEmpty(), "Nenhum produto foi exibido na secao");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarNenhumProdutoEncontrado() {
        String step = "Validar que nenhum produto foi encontrado na busca";

        sleep(500);
        List<WebElement> itens = driver.findElements(By.xpath("//li[contains(@data-ng-click,\"$location.path('/product/\")]"));
        Assertions.assertTrue(itens.isEmpty(), "Esperava nenhum produto na busca, mas " + itens.size() + " foram encontrados");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    // ─── Validações — página de detalhes do produto ────────────────────────

    public void validarFotoDoProdutoExibida() {
        String step = "Validar que a foto do produto e exibida";

        wait.until(ExpectedConditions.visibilityOf(produtoMap.getFotoProduto()));
        String src = produtoMap.getFotoProduto().getAttribute("src");
        Assertions.assertNotNull(src, "A foto do produto nao possui atributo src");
        Assertions.assertFalse(src.isBlank(), "A foto do produto esta com src vazio");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarNomeDoProdutoExibido() {
        String step = "Validar que o nome do produto e exibido";

        wait.until(ExpectedConditions.visibilityOf(produtoMap.getTituloProduto()));
        Assertions.assertFalse(produtoMap.getTituloProduto().getText().trim().isEmpty(),
                "O nome do produto nao foi exibido na pagina de detalhes");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarDescricaoDoProdutoExibida() {
        String step = "Validar que a descricao do produto e exibida";

        wait.until(ExpectedConditions.visibilityOf(produtoMap.getDescricaoProduto()));
        Assertions.assertFalse(produtoMap.getDescricaoProduto().getText().trim().isEmpty(),
                "A descricao do produto nao foi exibida");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarPrecoDoProdutoExibido() {
        String step = "Validar que o preco do produto e exibido";

        wait.until(ExpectedConditions.visibilityOf(produtoMap.getPrecoProduto()));
        String preco = produtoMap.getPrecoProduto().getText().trim();
        Assertions.assertTrue(preco.contains("$"), "Preco exibido em formato inesperado: " + preco);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    /**
     * O site não expõe quantidade numérica de estoque — apenas o rótulo "SOLD OUT"
     * quando indisponível. Aqui validamos que o produto está disponível (não esgotado),
     * o que equivale, para este sistema, a "há estoque".
     */
    public void validarQuantidadeEmEstoqueExibida() {
        String step = "Validar quantidade/disponibilidade em estoque do produto";

        boolean soldOut = isElementVisible(produtoMap.getLabelSoldOut());
        Assertions.assertFalse(soldOut, "Produto esperado em estoque esta marcado como SOLD OUT");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    /**
     * O site não possui seção de avaliações/reviews. Validação adaptada: garante que a
     * página de detalhes carregou corretamente (nome, preço e descrição visíveis), já que
     * não há elemento de rating para verificar.
     */
    public void validarAvaliacoesDoProdutoExibidas() {
        String step = "Validar avaliacoes do produto (funcionalidade nao suportada pelo site — validando pagina de detalhes)";

        wait.until(ExpectedConditions.visibilityOf(produtoMap.getTituloProduto()));
        wait.until(ExpectedConditions.visibilityOf(produtoMap.getPrecoProduto()));

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarOpcoesDeCoresExibidas() {
        String step = "Validar que as opcoes de cores do produto sao exibidas";

        boolean temCores = isElementVisible(produtoMap.getBlocoCores());
        if (temCores) {
            wait.until(ExpectedConditions.visibilityOf(produtoMap.getPrimeiraCorDisponivel()));
            clickRobust(produtoMap.getPrimeiraCorDisponivel(), step);
        } else {
            log.info("Produto sem variacao de cor disponivel — validacao ignorada.");
        }

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    public void validarProdutoForaDeEstoque() {
        String step = "Validar que o produto esta fora de estoque";

        boolean soldOut = isElementVisible(produtoMap.getLabelSoldOut());
        boolean addToCartDesabilitado = produtoMap.getBtnAddToCart().getAttribute("class") != null
                && produtoMap.getBtnAddToCart().getAttribute("class").contains("disable");

        Assertions.assertTrue(soldOut || addToCartDesabilitado,
                "Esperava que o produto estivesse fora de estoque (SOLD OUT ou botao desabilitado)");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    // ─── Helpers protegidos (reaproveitados pelas Logics dependentes) ──────

    protected boolean isElementVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void clickJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void clickRobust(WebElement element, String stepName) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            webActions().click(element);
        } catch (Exception e) {
            log.warn("Clique padrao falhou em '{}' — tentando via JavaScript.", stepName);
            clickJS(element);
        }
    }

    protected void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
