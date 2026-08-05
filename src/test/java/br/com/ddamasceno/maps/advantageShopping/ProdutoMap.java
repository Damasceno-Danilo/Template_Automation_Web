package br.com.ddamasceno.maps.advantageShopping;

import lombok.Getter;
import lombok.Setter;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
@Setter
public class ProdutoMap {

    // ─── Busca ──────────────────────────────────────────────────────────────

    @FindBy(id = "autoComplete")
    private WebElement inpBusca;

    @FindBy(id = "menuSearch")
    private WebElement iconBusca;

    // ─── Menus de catálogo (home page) ─────────────────────────────────────

    @FindBy(xpath = "//a[normalize-space(text())='OUR PRODUCTS']")
    private WebElement menuOurProducts;

    @FindBy(xpath = "//a[normalize-space(text())='SPECIAL OFFER']")
    private WebElement menuSpecialOffer;

    @FindBy(xpath = "//a[normalize-space(text())='POPULAR ITEMS']")
    private WebElement menuPopularItems;

    @FindBy(id = "SpeakersCategory")
    private WebElement categoriaSpeakers;

    // ─── Resultado de busca / listagem de produtos ─────────────────────────

    @FindBy(xpath = "//li[contains(@data-ng-click,\"$location.path('/product/\")]")
    private WebElement primeiroProdutoDaLista;

    @FindBy(xpath = "//a[@class='productName ng-binding']")
    private WebElement primeiroNomeProdutoDaLista;

    // ─── Página de detalhes do produto ─────────────────────────────────────

    @FindBy(xpath = "//h1[contains(@class,'roboto-regular')]")
    private WebElement tituloProduto;

    @FindBy(xpath = "//h2[contains(@class,'roboto-thin')]")
    private WebElement precoProduto;

    @FindBy(xpath = "//p[contains(@class,'roboto-light') and contains(@class,'ng-binding')]")
    private WebElement descricaoProduto;

    @FindBy(id = "mainImg")
    private WebElement fotoProduto;

    @FindBy(xpath = "//div[contains(@class,'colors')]")
    private WebElement blocoCores;

    @FindBy(xpath = "//div[contains(@class,'colors')]//a[contains(@class,'productColor')]")
    private WebElement primeiraCorDisponivel;

    @FindBy(name = "quantity")
    private WebElement inpQuantidade;

    @FindBy(name = "save_to_cart")
    private WebElement btnAddToCart;

    @FindBy(xpath = "//span[@translate='SOUL_OUT' or contains(text(),'SOLD OUT')]")
    private WebElement labelSoldOut;

    // ─── Contador do carrinho (header) ──────────────────────────────────────

    @FindBy(xpath = "(//span[contains(@class,'cart') and contains(@class,'ng-binding')])[1]")
    private WebElement contadorCarrinho;
}
