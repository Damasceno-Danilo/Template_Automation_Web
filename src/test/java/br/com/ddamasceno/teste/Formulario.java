package br.com.ddamasceno.teste;

import br.com.ddamasceno.core.Browser;
import br.com.ddamasceno.core.DriverFactory;
import org.junit.*;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class Formulario {
    WebDriver driver;
     DriverFactory driverFactory;

    @Before
    public void iniciarNavegador() {

        driverFactory = new DriverFactory(Browser.CHROME);
        driver = driverFactory.getDriver();
        driver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/componentes.html");
    }
    @After
    public void fecharNavegador() {
        //driver.quit();
    }

    @Test
    public void escrever() {
        driver.findElement(By.id("elementosForm:nome")).sendKeys("Danilo");
    }
    @Test
    public void escreverSobrenome() {
        driver.findElement(By.id("elementosForm:sobrenome")).sendKeys("Damasceno");
    }
    @Test
    public void clicarRadio() {
        driver.findElement(By.id("elementosForm:sexo:0")).click();
    }
    @Test
    public void clicarCheckBox() {
        driver.findElement(By.id("elementosForm:comidaFavorita:0")).click();
    }
    @Test
    public void selecionarComboPorIndex() {
        WebElement element = driver.findElement(By.id("elementosForm:escolaridade"));
        Select combo = new Select(element);
        combo.selectByIndex(4);
    }
    @Test
    public void selecionarComboPorValue() {
        WebElement element = driver.findElement(By.id("elementosForm:escolaridade"));
        Select combo = new Select(element);
        combo.selectByValue("mestrado");
    }
    @Test
    public void selecionarComboPorTexto() {
        WebElement element = driver.findElement(By.id("elementosForm:escolaridade"));
        Select combo = new Select(element);
        combo.selectByVisibleText("Superior");
    }
    @Test
    public void interagirAlerta() {
        driver.findElement(By.id("alert")).click();
        Alert alert = driver.switchTo().alert();
        Assert.assertEquals("Alert Simples", alert.getText());
        alert.accept();
    }

    @Test
    public void interagirAlertaConfim() {
        driver.findElement(By.id("confirm")).click();
        Alert alert = driver.switchTo().alert();
        Assert.assertEquals("Confirm Simples", alert.getText());
        alert.accept();
        Assert.assertEquals("Confirmado", alert.getText());
        alert.accept();

        driver.findElement(By.id("confirm")).click();
        alert = driver.switchTo().alert();
        Assert.assertEquals("Confirm Simples", alert.getText());
        alert.dismiss();
        Assert.assertEquals("Negado", alert.getText());
        alert.dismiss();
    }

    @Test
    public void interagirAlertaPrompt() {
        driver.findElement(By.id("prompt")).click();
        Alert alert = driver.switchTo().alert();
        Assert.assertEquals("Digite um numero", alert.getText());
        alert.sendKeys("13");
        alert.accept();
        Assert.assertEquals("Era 13?", alert.getText());
        alert.accept();
        Assert.assertEquals(":D", alert.getText());
        alert.accept();
    }
}
