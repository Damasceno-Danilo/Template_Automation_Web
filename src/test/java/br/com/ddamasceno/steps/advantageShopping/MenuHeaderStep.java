package br.com.ddamasceno.steps.advantageShopping;

import br.com.ddamasceno.logic.advantageShopping.MenuHeaderLogic;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

/**
 * Step definitions para os cenários de navegação do menu header (menu_header.feature)
 * da Advantage Shopping.
 */
public class MenuHeaderStep {

    private final MenuHeaderLogic menuHeaderLogic = new MenuHeaderLogic();

    @Quando("clicar no menu Our Products")
    public void clicarNoMenuOurProducts() {
        menuHeaderLogic.clicarMenuOurProducts();
    }

    @Quando("clicar no menu Special Offer")
    public void clicarNoMenuSpecialOffer() {
        menuHeaderLogic.clicarMenuSpecialOffer();
    }

    @Quando("clicar no menu Contact Us")
    public void clicarNoMenuContactUs() {
        menuHeaderLogic.clicarMenuContactUs();
    }

    @Quando("clicar no menu Popular Items")
    public void clicarNoMenuPopularItems() {
        menuHeaderLogic.clicarMenuPopularItems();
    }

    @Então("validar que a pagina de Our Products foi carregada")
    public void validarQueAPaginaDeOurProductsFoiCarregada() {
        menuHeaderLogic.validarPaginaOurProductsCarregada();
    }

    @Então("validar que a pagina de Special Offer foi carregada")
    public void validarQueAPaginaDeSpecialOfferFoiCarregada() {
        menuHeaderLogic.validarPaginaSpecialOfferCarregada();
    }

    @Então("validar que a pagina de Contact Us foi carregada")
    public void validarQueAPaginaDeContactUsFoiCarregada() {
        menuHeaderLogic.validarPaginaContactUsCarregada();
    }

    @Então("validar que a pagina de Popular Items foi carregada")
    public void validarQueAPaginaDePopularItemsFoiCarregada() {
        menuHeaderLogic.validarPaginaPopularItemsCarregada();
    }
}
