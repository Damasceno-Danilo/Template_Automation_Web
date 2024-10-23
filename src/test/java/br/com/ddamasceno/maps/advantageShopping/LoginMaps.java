package br.com.ddamasceno.maps.advantageShopping;

import lombok.Getter;
import lombok.Setter;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


@Getter
@Setter
public class LoginMaps {

    @FindBy(xpath = "//*[@class='logo']")
    private WebElement logoAdvantage;

    @FindBy(xpath = "//*[@id='menuUserLink']")
    private WebElement iconUser;

    @FindBy(xpath = "//*[@class='closeBtn loginPopUpCloseBtn']")
    private WebElement iconFechar;

    @FindBy(xpath = "//*[@class='PopUp']")
    private WebElement foraModal;

    @FindBy(xpath = "//*[contains(text(),'CREATE NEW ACCOUNT')]")
    private WebElement linkCreateAccount;

    @FindBy(xpath = "//*[@name='username']")
    private WebElement inpUsername;

    @FindBy(xpath = "//*[@name='password']")
    private WebElement inpPassword;

    @FindBy(xpath = "//*[@name='remember_me']")
    private WebElement clickRememberMe;

    @FindBy(xpath = "//*[@id='sign_in_btn']")
    private WebElement btnSignIn;

    @FindBy(xpath = "//*[@class='loader']")
    private WebElement loader;

    @FindBy(xpath = "//*[@id='menuUserLink']/span")
    private WebElement textDataLogin;

}
