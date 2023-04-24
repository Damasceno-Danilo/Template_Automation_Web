package br.com.ddamasceno.teste;

import br.com.ddamasceno.core.Browser;
import br.com.ddamasceno.core.DriverFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class DesafioFormulario {

    private WebDriver driver;
    private DriverFactory driverFactory;
    @Before
    public void inicializaBrowser() {

        driverFactory = new DriverFactory(Browser.CHROME);
        driver = driverFactory.getDriver();
        driver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/componentes.html");
    }

    @After
    public void finalizaBrowser() {
        driver.quit();
    }

    @Test
    public void preencherFormulario() {
        driver.findElement(By.id("elementosForm:nome")).sendKeys("Danilo");
        driver.findElement(By.id("elementosForm:sobrenome")).sendKeys("Damasceno");
        driver.findElement(By.id("elementosForm:sexo")).click();
        driver.findElement(By.id("elementosForm:comidaFavorita:0")).click();
        new Select(driver.findElement(By.id("elementosForm:escolaridade"))).selectByVisibleText("Superior");
        new Select(driver.findElement(By.id("elementosForm:esportes"))).selectByValue("natacao");
        driver.findElement(By.id("elementosForm:cadastrar")).click();

        Assert.assertTrue(driver.findElement(By.id("resultado")).getText().startsWith("Cadastrado!"));
        Assert.assertTrue(driver.findElement(By.id("descNome")).getText().startsWith("Nome: Danilo"));
        Assert.assertTrue(driver.findElement(By.id("descSobrenome")).getText().startsWith("Sobrenome: Damasceno"));
        Assert.assertTrue(driver.findElement(By.id("descSexo")).getText().startsWith("Sexo: Masculino"));
        Assert.assertTrue(driver.findElement(By.id("descComida")).getText().startsWith("Comida: Carne"));
        Assert.assertTrue(driver.findElement(By.id("descEscolaridade")).getText().startsWith("Escolaridade: superior"));
        Assert.assertTrue(driver.findElement(By.id("descEsportes")).getText().startsWith("Esportes: Natacao"));
    }
}
