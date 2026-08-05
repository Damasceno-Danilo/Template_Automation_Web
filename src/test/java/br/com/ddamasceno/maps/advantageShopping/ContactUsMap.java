package br.com.ddamasceno.maps.advantageShopping;

import lombok.Getter;
import lombok.Setter;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Mapa da seção "Contact Us" — Advantage Shopping.
 *
 * <p>O formulário real do site possui apenas 4 campos: Categoria, Produto, Email e
 * Subject (um textarea que funciona como corpo da mensagem) — não há campo de "Nome"
 * nem um campo de "Mensagem" separado do "Subject". Ver {@link br.com.ddamasceno.logic.advantageShopping.ContactUsLogic}
 * para a adaptação adotada.
 */
@Getter
@Setter
public class ContactUsMap {

    @FindBy(xpath = "//a[normalize-space(text())='CONTACT US']")
    private WebElement menuContactUs;

    @FindBy(xpath = "//*[@name='categoriesForContact.category']")
    private WebElement selCategoria;

    @FindBy(xpath = "//*[@name='productsForContact.product']")
    private WebElement selProduto;

    @FindBy(xpath = "//*[@name='supportModel.email']")
    private WebElement inpEmail;

    @FindBy(xpath = "//*[@name='supportModel.subject']")
    private WebElement txtSubject;

    @FindBy(xpath = "//*[@sec-send='sendSupportEmail()']")
    private WebElement btnEnviar;

    @FindBy(id = "registerSuccessCover")
    private WebElement blocoSucesso;

    @FindBy(xpath = "//p[contains(@class,'successMessage')]")
    private WebElement mensagemSucesso;

    @FindBy(xpath = "//label[contains(@class,'invalid')]")
    private WebElement labelMensagemErro;
}
