package br.com.ddamasceno.teste;

import br.com.ddamasceno.core.Browser;
import br.com.ddamasceno.core.DriverFactory;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.sql.Driver;

public class Formulario {

    WebDriver driver;
     DriverFactory driverFactory;
    @Test
    public void inicia() {

        driverFactory = new DriverFactory(Browser.CHROME);
        driver = driverFactory.getDriver();
        driver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/componentes.html");
    }
}
