package br.com.ddamasceno.Runner;

import br.com.ddamasceno.core.Browser;
import br.com.ddamasceno.core.DriverFactory;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TesteFrameEJanelas {

    private WebDriver driver;
    private DriverFactory driverFactory;

    @Test
    public void deveInteragircomFrame() {
        driverFactory = new DriverFactory(Browser.CHROME);
        driver = driverFactory.getDriver();
        driver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/componentes.html");

        driver.switchTo().frame("frame1");
        driver.findElement(By.id("frameButton")).click();
        Alert alerta = driver.switchTo().alert();
        Assert.assertEquals("Frame OK!", alerta.getText());
        alerta.accept();
    }

    @Test
    public void deveInteragircoJanelas() {
        driverFactory = new DriverFactory(Browser.CHROME);
        driver = driverFactory.getDriver();
        driver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/componentes.html");

        driver.findElement(By.id("buttonPopUpEasy")).click();
        driver.switchTo().window("Popup");
        driver.findElement(By.tagName("textarea")).sendKeys("Danilo");
        driver.close();
    }
}
