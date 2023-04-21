package br.com.ddamasceno.teste;

import br.com.ddamasceno.core.Browser;
import br.com.ddamasceno.core.DriverFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class Formulario {
    WebDriver driver;
     DriverFactory driverFactory;
    @Before
    public void inicia() {
        driverFactory = new DriverFactory(Browser.CHROME);
        driver = driverFactory.getDriver();
        driver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/componentes.html");
    }

    @After
    public void finaliza() {
       driver.quit();
    }
    @Test
    public void clicar() {
        driver.findElement(By.id("elementosForm:sexo:0")).click();
    }
    @Test
    public void escrever() {
        driver.findElement(By.id("elementosForm:nome")).sendKeys("Danilo");
    }
    @Test
    public void selecionarComboIndex() {
        WebElement element = driver.findElement(By.id("elementosForm:escolaridade"));
        Select combo = new Select(element);
        combo.selectByIndex(3);
    }
    @Test
    public void selecionarComboValue() {
        WebElement element = driver.findElement(By.id("elementosForm:escolaridade"));
        Select combo = new Select(element);
        combo.selectByValue("superior");
    }
    @Test
    public void selecionarComboPorTexto() {
        WebElement element = driver.findElement(By.id("elementosForm:escolaridade"));
        Select combo = new Select(element);
        combo.selectByVisibleText("Superior");
    }
        
}
