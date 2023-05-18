package br.com.ddamasceno.core;

import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class DSL {

    private WebDriver driver;
    private Alert alert;
    private DriverFactory driverFactory;

    public DSL(WebDriver driver) {
        this.driver = driver;
    }

    public void iniciaNavegador(String url) {
        driverFactory = new DriverFactory(Browser.CHROME);
        driver = driverFactory.getDriver();
        driver.get(url);
    }

    public void escrever(String id_campo, String texto) {
        driver.findElement(By.id(id_campo)).sendKeys(texto);
    }

    public String validarTexto(String id_campo) {
       return driver.findElement(By.id(id_campo)).getText();
    }

    public void clicarRadio(String id_campo) {
        driver.findElement(By.id(id_campo)).click();
    }

    public void clicarCheckBox(String id_campo) {
        driver.findElement(By.id(id_campo)).click();
    }

    public void selecionarPorIndex(String id_campo, int index) {
        WebElement element = driver.findElement(By.id(id_campo));
        Select combo = new Select(element);
        combo.selectByIndex(index);
    }
    public void selecionarComboPorValue(String id_campo, String value) {
        WebElement element = driver.findElement(By.id(id_campo));
        Select combo = new Select(element);
        combo.selectByValue(value);
    }

    public void selecionarComboPorTexto(String id_campo, String texto) {
        WebElement element = driver.findElement(By.id(id_campo));
        Select combo = new Select(element);
        combo.selectByVisibleText(texto);
    }

    public void interagirAlerta(String id_campo, String alerta) {
        driver.findElement(By.id("alert")).click();
        Alert alert = driver.switchTo().alert();
        Assert.assertEquals(alerta, alert.getText());
        alert.accept();
    }

    public void interagirAlertaConfim(String id_campo, String texto, String statusConfirmado) {
        driver.findElement(By.id(id_campo)).click();
        alert = driver.switchTo().alert();
        Assert.assertEquals(texto, alert.getText());
        alert.accept();
        Assert.assertEquals(texto, alert.getText());
        alert.accept();
    }

    public void intergirAlertaNegado(String id_campo, String msgAlerta, String statusNegado) {
        driver.findElement(By.id("confirm")).click();
        alert = driver.switchTo().alert();
        Assert.assertEquals("Confirm Simples", alert.getText());
        alert.dismiss();
        Assert.assertEquals("Negado", alert.getText());
        alert.dismiss();
    }

    public void interagirAlertaPrompt(String id_campo) {
        driver.findElement(By.id("prompt")).click();
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }

}
