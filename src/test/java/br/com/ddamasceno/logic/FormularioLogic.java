package br.com.ddamasceno.logic;

import br.com.ddamasceno.core.Browser;
import br.com.ddamasceno.core.DriverFactory;
import br.com.ddamasceno.core.WebActions;
import br.com.ddamasceno.maps.FormularioMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Testes de formulário HTML local — componentes básicos de UI.
 * Utiliza JUnit 5 (@BeforeEach / @AfterEach / @Test).
 */
public class FormularioLogic {

    private WebDriver driver;
    private DriverFactory driverFactory;
    private WebActions webActions;
    private FormularioMap formularioMap;

    @BeforeEach
    public void iniciarNavegador() {
        driverFactory = new DriverFactory(Browser.CHROME);
        driver = driverFactory.getDriver();
        driver.get("file:///" + System.getProperty("user.dir") + "/src/main/resources/componentes.html");
        webActions = new WebActions(driver);
        formularioMap = new FormularioMap();
        PageFactory.initElements(driver, formularioMap);
    }

    @AfterEach
    public void fecharNavegador() {
        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void escreverNome() {
        webActions.insertText(formularioMap.getTextNome(), "Danilo");
    }

    @Test
    public void escreverSobrenome() {
        webActions.insertText(formularioMap.getSobrenome(), "Damasceno");
    }

    @Test
    public void clicarRadio() {
        webActions.click(formularioMap.getClickSexoMasculino());
    }

    @Test
    public void interagirCheckBox() {
        webActions.click(formularioMap.getComidaFavoritaCarne());
    }

    @Test
    public void selecionarComboPorIndex() {
        webActions.selectForIndex(formularioMap.getEscolaridadeSuperior(), 4);
    }

    @Test
    public void selecionarComboPorValue() {
        webActions.selectForValue(formularioMap.getEscolaridadeMestrado(), "mestrado");
    }

    @Test
    public void selecionarComboPorTexto() {
        webActions.selectForText(formularioMap.getEscolaridadeSuperiortexto(), "Superior");
    }

    @Test
    public void interagirAlerta() {
        webActions.interactWithAlert(formularioMap.getAlerta(), "Alert Simples");
    }

    @Test
    public void alertaConfim() {
        webActions.interactAlertConfim(formularioMap.getConfirm());
    }

    @Test
    public void alertaNegado() {
        webActions.interveneAlertDenied(formularioMap.getConfirm());
    }

    @Test
    public void interagirAlertaPrompt() {
        webActions.interactAlertPrompt(formularioMap.getPrompt());
    }
}
