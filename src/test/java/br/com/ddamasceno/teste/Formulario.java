package br.com.ddamasceno.teste;

import br.com.ddamasceno.core.Browser;
import br.com.ddamasceno.core.DSL;
import br.com.ddamasceno.core.DriverFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

public class Formulario {
    WebDriver driver;
     DriverFactory driverFactory;
     private DSL dsl;

    @Before
    public void iniciarNavegador() {
        driverFactory = new DriverFactory(Browser.CHROME);
        driver = driverFactory.getDriver();
        driver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/componentes.html");
        dsl = new DSL(driver);
    }
    @After
    public void fecharNavegador() {
        //driver.quit();
    }
    @Test
    public void escreverNome() {
        dsl.escrever("elementosForm:nome", "Danilo");
    }
    @Test
    public void escreverSobrenome() {
        dsl.escrever("elementosForm:sobrenome", "Damasceno");
    }
    @Test
    public void clicarRadio() {
        dsl.clicarCheckBox("elementosForm:sexo:0");
    }
    @Test
    public void interagirCheckBox() {
        dsl.clicarCheckBox("elementosForm:comidaFavorita:0");
    }
    @Test
    public void selecionarComboPorIndex() {
        dsl.selecionarPorIndex( "elementosForm:escolaridade", 4);
    }
    @Test
    public void selecionarComboPorValue() {
        dsl.selecionarComboPorValue("elementosForm:escolaridade", "Mestrado");
    }
    @Test
    public void selecionarComboPorTexto() {
        dsl.selecionarComboPorTexto("elementosForm:escolaridade", "Superior");
    }
    @Test
    public void interagirAlerta() {
        dsl.interagirAlerta("alert", "Alert Simples");
    }

    @Test
    public void alertaConfim() {
        dsl.interagirAlertaConfim("confirm", "Confirm Simples", "Confirmado");
    }

    @Test
    public void alertaNegado() {
        dsl.intergirAlertaNegado("confirm", "Confirm Simples", "Negado");
    }

    @Test
    public void interagirAlertaPrompt() {
        dsl.interagirAlertaPrompt("prompt");
    }
}
