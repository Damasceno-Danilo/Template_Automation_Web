package br.com.ddamasceno.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Utils {

    private WebDriver driver;
    private WebDriverWait wait;

    public Utils(WebDriver driver, int timeInSeconds) {
        this.driver = driver;
    }

    public WebElement waitForElementToAppear(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public Boolean waitForElementToDisappear(WebElement element) {
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }
}
