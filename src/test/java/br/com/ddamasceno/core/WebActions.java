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

    public void escrever(WebElement element, String texto) {
        element.sendKeys(texto);
    }

    public String validarTexto(WebElement element) {
       return element.getText();
    }

    public void clicarRadio(WebElement element) {
        element.click();
    }

    public void clicarCheckBox(WebElement element) {
       element.click();
    }

    public void selecionarPorIndex(WebElement element, int index) {
        Select combo = new Select(element);
        combo.selectByIndex(index);
    }
    public void selecionarComboPorValue(WebElement element, String value) {
        Select combo = new Select(element);
        combo.selectByValue(value);
    }

    public void selecionarComboPorTexto(WebElement element, String texto) {
        Select combo = new Select(element);
        combo.selectByVisibleText(texto);
    }

    public void interagirAlerta(WebElement element, String alerta) {
        element.click();
        Alert alert = driver.switchTo().alert();
        Assert.assertEquals(alerta, alert.getText());
        alert.accept();
    }

    public void interagirAlertaConfim(WebElement element) {
        element.click();
        alert = driver.switchTo().alert();
        alert.accept();
        alert.accept();
    }

    public void intergirAlertaNegado(WebElement element, String msgAlerta, String statusNegado) {
        driver.findElement(By.id("confirm")).click();
        alert = driver.switchTo().alert();
        Assert.assertEquals("Confirm Simples", alert.getText());
        alert.dismiss();
        Assert.assertEquals("Negado", alert.getText());
        alert.dismiss();
    }

    public void interagirAlertaPrompt(WebElement element) {
        element.click();
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }

}
