package br.com.ddamasceno.maps.saucedemo;

import lombok.Getter;
import lombok.Setter;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
@Setter
public class LoginMap {

    @FindBy(id = "user-name")
    private WebElement inpUsername;

    @FindBy(id = "password")
    private WebElement inpPassword;

    @FindBy(id = "login-button")
    private WebElement btnLogin;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    @FindBy(className = "app_logo")
    private WebElement logoSwagLabs;

    @FindBy(className = "title")
    private WebElement pageTitle;

}
