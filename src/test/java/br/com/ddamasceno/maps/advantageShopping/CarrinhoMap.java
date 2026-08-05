package br.com.ddamasceno.maps.advantageShopping;

import lombok.Getter;
import lombok.Setter;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
@Setter
public class CarrinhoMap {

    @FindBy(id = "shoppingCartLink")
    private WebElement iconCarrinho;

    @FindBy(xpath = "(//span[contains(@class,'cart') and contains(@class,'ng-binding')])[1]")
    private WebElement contadorCarrinho;

    @FindBy(xpath = "//li[contains(@ng-repeat,'cart.productsInCart')]")
    private WebElement primeiraLinhaCarrinho;

    @FindBy(xpath = "//a[contains(@class,'productName')][ancestor::li[contains(@ng-repeat,'cart.productsInCart')]]")
    private WebElement nomeProdutoNoCarrinho;

    @FindBy(xpath = "//a[@class='edit ng-scope' or @class='edit']")
    private WebElement linkEditarProduto;

    @FindBy(xpath = "//a[contains(@class,'remove') and contains(@data-ng-click,'removeProduct')]")
    private WebElement linkRemoverProduto;

    @FindBy(id = "checkOutButton")
    private WebElement btnCheckout;

    @FindBy(xpath = "//span[contains(@class,'cart-total')]")
    private WebElement totalCarrinho;
}
