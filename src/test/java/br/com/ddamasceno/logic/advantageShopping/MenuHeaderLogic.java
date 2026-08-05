package br.com.ddamasceno.logic.advantageShopping;

import br.com.ddamasceno.core.BaseLogic;
import br.com.ddamasceno.core.Browser;
import br.com.ddamasceno.core.DriverFactory;
import br.com.ddamasceno.core.DriverManager;
import br.com.ddamasceno.maps.advantageShopping.MenuHeaderMap;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Lógica de negócio dos testes de navegação do menu header — Advantage Shopping.
 *
 * <p>O header do site não navega para páginas distintas: cada item rola a home page
 * até a seção correspondente ({@code #our_products}, {@code #special_offer_items},
 * {@code #contact_us}, {@code #popular_items}). A validação "página carregada" é
 * adaptada para "seção alvo visível na viewport".
 */
@Log4j2
public class MenuHeaderLogic extends BaseLogic {

    private final WebDriver driver;
    private final MenuHeaderMap menuHeaderMap;
    private final WebDriverWait wait;

    public MenuHeaderLogic() {
        WebDriver existing = DriverManager.getDriver();
        if (existing != null) {
            this.driver = existing;
        } else {
            DriverFactory driverFactory = new DriverFactory(Browser.CHROME);
            this.driver = driverFactory.getDriver();
        }
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        initCore(driver);

        this.menuHeaderMap = new MenuHeaderMap();
        PageFactory.initElements(driver, menuHeaderMap);
    }

    public void clicarMenuOurProducts() {
        clicarMenu(menuHeaderMap.getMenuOurProducts(), "Clicar no menu Our Products");
    }

    public void clicarMenuSpecialOffer() {
        clicarMenu(menuHeaderMap.getMenuSpecialOffer(), "Clicar no menu Special Offer");
    }

    public void clicarMenuContactUs() {
        clicarMenu(menuHeaderMap.getMenuContactUs(), "Clicar no menu Contact Us");
    }

    public void clicarMenuPopularItems() {
        clicarMenu(menuHeaderMap.getMenuPopularItems(), "Clicar no menu Popular Items");
    }

    public void validarPaginaOurProductsCarregada() {
        validarSecaoCarregada(menuHeaderMap.getSecaoOurProducts(), "Validar pagina Our Products carregada");
    }

    public void validarPaginaSpecialOfferCarregada() {
        validarSecaoCarregada(menuHeaderMap.getSecaoSpecialOffer(), "Validar pagina Special Offer carregada");
    }

    public void validarPaginaContactUsCarregada() {
        validarSecaoCarregada(menuHeaderMap.getSecaoContactUs(), "Validar pagina Contact Us carregada");
    }

    public void validarPaginaPopularItemsCarregada() {
        validarSecaoCarregada(menuHeaderMap.getSecaoPopularItems(), "Validar pagina Popular Items carregada");
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private void clicarMenu(WebElement link, String step) {
        wait.until(ExpectedConditions.elementToBeClickable(link));
        try {
            webActions().click(link);
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        }
        sleep(500);

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    private void validarSecaoCarregada(WebElement secao, String step) {
        wait.until(ExpectedConditions.visibilityOf(secao));
        Assertions.assertTrue(estaNaViewport(secao), "A secao esperada nao esta visivel na viewport apos o clique no menu");

        report().registerStep(webActions().getScreenshot(), step, "screenshot");
        log.info(step);
    }

    private boolean estaNaViewport(WebElement element) {
        try {
            Object result = ((JavascriptExecutor) driver).executeScript(
                    "var r = arguments[0].getBoundingClientRect();" +
                            "return r.top < window.innerHeight && r.bottom > 0;",
                    element);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return element.isDisplayed();
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
