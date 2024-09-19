package br.com.ddamasceno.core;

import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class WebActions {

    private WebDriver driver;
    private Alert alert;
    private DriverFactory driverFactory;

    public WebActions(WebDriver driver) {
        this.driver = driver;
    }

    public void iniciaNavegador(String url) {
        driverFactory = new DriverFactory(Browser.CHROME);
        driver = driverFactory.getDriver();
        driver.get(url);
    }

    public void insertText(WebElement element, String texto) {
        element.sendKeys(texto);
    }

    public String validatedText(WebElement element) {
       return element.getText();
    }

    public void click(WebElement element) {
        element.click();
    }

    public void selectForIndex(WebElement element, int index) {
        Select combo = new Select(element);
        combo.selectByIndex(index);
    }
    public void selectForValue(WebElement element, String value) {
        Select combo = new Select(element);
        combo.selectByValue(value);
    }

    public void selectForText(WebElement element, String texto) {
        Select combo = new Select(element);
        combo.selectByVisibleText(texto);
    }

    public void interactWithAlert(WebElement element, String alerta) {
        element.click();
        Alert alert = driver.switchTo().alert();
        Assert.assertEquals(alerta, alert.getText());
        alert.accept();
    }

    public void interactAlertConfim(WebElement element) {
        element.click();
        alert = driver.switchTo().alert();
        alert.accept();
        alert.accept();
    }

    public void interveneAlertDenied(WebElement element) {
        element.click();
       // driver.findElement(By.id("confirm")).click();
        alert = driver.switchTo().alert();
        alert.dismiss();
        alert.dismiss();
    }

    public void interactAlertPrompt(WebElement element) {
        element.click();
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }

    public void InteractWithFrame(WebElement element) {
       driver.switchTo().frame("frame1");
        driver.findElement(By.id("frameButton")).click();
        Alert alerta = driver.switchTo().alert();
        alerta.accept();
    }

    public void InteractWithWindows(WebElement element) {
        driver.findElement(By.id("buttonPopUpEasy")).click();
        driver.switchTo().window("Popup");
        driver.findElement(By.tagName("textarea"));
        driver.close();
    }

}
