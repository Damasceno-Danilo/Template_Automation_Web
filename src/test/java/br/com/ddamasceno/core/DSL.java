package br.com.ddamasceno.core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DSL {

    private WebDriver driver;

    public DSL(WebDriver driver) {
        this.driver = driver;
    }
    public void clicar(String id_campo) {
        driver.findElement(By.id(id_campo)).click();
    }
    public void escrever(String id_campo) {
       driver.findElement(By.id(id_campo)).sendKeys();
    }
}
