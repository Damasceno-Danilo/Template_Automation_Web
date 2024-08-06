package br.com.ddamasceno.teste;

import br.com.ddamasceno.core.Browser;
import br.com.ddamasceno.core.WebActions;
import br.com.ddamasceno.core.DriverFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

public class Formulario {
    WebDriver driver;
     DriverFactory driverFactory;
     private WebActions webActions;

    @Before
    public void iniciarNavegador() {
        driverFactory = new DriverFactory(Browser.CHROME);
        driver = driverFactory.getDriver();
        driver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/componentes.html");
        webActions = new WebActions(driver);
    }
    @After
    public void fecharNavegador() {
        //driver.quit();
    }
    @Test
    public void escreverNome() {
        webActions.escrever("elementosForm:nome", "Danilo");
    }
    @Test
    public void escreverSobrenome() {
        webActions.escrever("elementosForm:sobrenome", "Damasceno");
    }
    @Test
    public void clicarRadio() {
       webActions.clicarCheckBox("elementosForm:sexo:0");
    }
    @Test
    public void interagirCheckBox() {
        webActions.clicarCheckBox("elementosForm:comidaFavorita:0");
    }
    @Test
    public void selecionarComboPorIndex() {
        webActions.selecionarPorIndex( "elementosForm:escolaridade", 4);
    }
    @Test
    public void selecionarComboPorValue() {
        webActions.selecionarComboPorValue("elementosForm:escolaridade", "Mestrado");
    }
    @Test
    public void selecionarComboPorTexto() {
        webActions.selecionarComboPorTexto("elementosForm:escolaridade", "Superior");
    }
    @Test
    public void interagirAlerta() {
        webActions.interagirAlerta("alert", "Alert Simples");
    }

    @Test
    public void alertaConfim() {
        webActions.interagirAlertaConfim("confirm", "Confirm Simples", "Confirmado");
    }

    @Test
    public void alertaNegado() {
        webActions.intergirAlertaNegado("confirm", "Confirm Simples", "Negado");
    }

    @Test
    public void interagirAlertaPrompt() {
        webActions.interagirAlertaPrompt("prompt");
    }
}
