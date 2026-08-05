package br.com.ddamasceno.maps.advantageShopping;

import lombok.Getter;
import lombok.Setter;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
@Setter
public class MenuHeaderMap {

    @FindBy(xpath = "//a[normalize-space(text())='OUR PRODUCTS']")
    private WebElement menuOurProducts;

    @FindBy(xpath = "//a[normalize-space(text())='SPECIAL OFFER']")
    private WebElement menuSpecialOffer;

    @FindBy(xpath = "//a[normalize-space(text())='CONTACT US']")
    private WebElement menuContactUs;

    @FindBy(xpath = "//a[normalize-space(text())='POPULAR ITEMS']")
    private WebElement menuPopularItems;

    @FindBy(id = "our_products")
    private WebElement secaoOurProducts;

    @FindBy(id = "special_offer_items")
    private WebElement secaoSpecialOffer;

    @FindBy(id = "contact_us")
    private WebElement secaoContactUs;

    @FindBy(id = "popular_items")
    private WebElement secaoPopularItems;
}
