package br.com.ddamasceno.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

public class DriverFactory {

    private WebDriver driver;

    public DriverFactory(Browser navegador) {

        switch (navegador) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            break;
            case EDGE:
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
            break;
            case FIREFORX:
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            break;
            case SAFARI:
                WebDriverManager.safaridriver().setup();
                driver = new SafariDriver();
            break;
        }
        driver.manage().window().maximize();
    }
    public WebDriver getDriver() {
        return driver;
    }

}


