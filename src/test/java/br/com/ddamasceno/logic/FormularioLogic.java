package br.com.ddamasceno.logic;

import br.com.ddamasceno.core.Browser;
import br.com.ddamasceno.core.WebActions;
import br.com.ddamasceno.core.DriverFactory;
import br.com.ddamasceno.maps.FormularioMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

//@Log4j2
public class FormularioLogic {
    WebDriver driver;
     DriverFactory driverFactory;
     private WebActions webActions;
     private FormularioMap formularioMap;


    @Before
    public void iniciarNavegador() {
        driverFactory = new DriverFactory(Browser.CHROME);
        driver = driverFactory.getDriver();
        driver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/componentes.html");
        webActions = new WebActions(driver);
        formularioMap = new FormularioMap();
        PageFactory.initElements(driver, formularioMap);

    }
    @After
    public void fecharNavegador() {
        driver.quit();
    }
    @Test
    public void escreverNome() throws InterruptedException {
        webActions.escrever(formularioMap.getTextNome(), "Danilo");

    }
    @Test
    public void escreverSobrenome() {
        webActions.escrever(formularioMap.getSobrenome(), "Damasceno");
    }
    @Test
    public void clicarRadio() {
       webActions.clicarCheckBox(formularioMap.getClickSexoMasculino());
    }
    @Test
    public void interagirCheckBox() {
        webActions.clicarCheckBox(formularioMap.getComidaFavoritaCarne());
    }
    @Test
    public void selecionarComboPorIndex() {
        webActions.selecionarPorIndex(formularioMap.getEscolaridadeSuperior(), 4);
    }
    @Test
    public void selecionarComboPorValue() {
        webActions.selecionarComboPorValue(formularioMap.getEscolaridadeMestrado(), "mestrado");
    }
    @Test
    public void selecionarComboPorTexto() {
        webActions.selecionarComboPorTexto(formularioMap.getEscolaridadeSuperiortexto(), "Superior");
    }
    @Test
    public void interagirAlerta() {
        webActions.interagirAlerta(formularioMap.getAlerta(), "Alert Simples");
    }

    @Test
    public void alertaConfim() {
        webActions.interagirAlertaConfim(formularioMap.getConfirm());
    }

    @Test
    public void alertaNegado() {
        webActions.intergirAlertaNegado(formularioMap.getConfirm());
    }

    @Test
    public void interagirAlertaPrompt() {
        webActions.interagirAlertaPrompt(formularioMap.getPrompt());
    }
}
