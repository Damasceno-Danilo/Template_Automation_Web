package br.com.ddamasceno.logic.advantageShopping;

import br.com.ddamasceno.core.*;
import br.com.ddamasceno.maps.advantageShopping.LoginMaps;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

public class LoginLogic {

     private WebDriver driver;
     private DriverFactory driverFactory;
     private WebActions webActions;
     private LoginMaps loginMaps;
     private WebDriverWait wait;
     private TestReport report;

     public LoginLogic() {
          driverFactory = new DriverFactory(Browser.CHROME);
          driver = driverFactory.getDriver();
          webActions = new WebActions(driver);
          loginMaps = new LoginMaps();
          report = new TestReport(driver);
          report = new TestReport(driver);
          PageFactory.initElements(driver, loginMaps);
     }

     public void iniciarNavegador() throws IOException {
          webActions.navigateURL("https://www.advantageonlineshopping.com/#/");
          webActions.waitVisibleElement(loginMaps.getLogoAdvantage());
          report.captureScreenshot("Abrir aplicação advantageShopping");

     }

     public void clickBtnLogin() throws IOException {
          webActions.click(loginMaps.getIconUser());
          webActions.waitVisibleElement(loginMaps.getLinkCreateAccount());
          report.captureScreenshot("Clicar Botao login");
     }

     public void clickBtnFechar() {
          webActions.waitInvisibleElement(loginMaps.getLoader());
          webActions.click(loginMaps.getIconFechar());
          report.captureScreenshot("Clicar Botao Fechar");
     }


     public void inserirDadosLogin() {
          webActions.waitVisibleElement(loginMaps.getLinkCreateAccount());
          webActions.insertText(loginMaps.getInpUsername(), "DanDama");
          webActions.insertText(loginMaps.getInpPassword(), "Dan$1418");
          webActions.waitInvisibleElement(loginMaps.getLoader());
          report.captureScreenshot("INSERIR DADOS DE LOGIN");

     }

     public void clickRememberMe() {
          webActions.click(loginMaps.getClickRememberMe());

     }

     public void clickBtnSignIn() {
          webActions.click(loginMaps.getBtnSignIn());
          report.captureScreenshot("CLICAR BOTAO SIGNIN");
     }

     public void validarFecharModal() throws IOException {
          webActions.waitInvisibleElement(loginMaps.getIconFechar());
          //Assert.assertFalse(loginMaps.getLinkCreateAccount().isDisplayed());
          report.captureScreenshot("Modal fechou");
          report.createPdfReport(
                  "Teste web.pdf",
                  "TESTE FEHCAR MODAL",
                  "Danilo Augusto",
                  "Selenium WebDriver",
                  "advantage shopping"
          );
          webActions.closedBrowser();
     }

    public void validarLogin() {
          webActions.waitInvisibleElement(loginMaps.getIconFechar());
          Assert.assertEquals("DanDama", loginMaps.getTextDataLogin().getText());
         report.captureScreenshot("VALIDAR LOGIN COM SUCESSO");
         report.createPdfReport(
                 "Teste web.pdf",
                 "Teste login",
                 "Danilo Augusto",
                 "Selenium WebDriver",
                 "advantage shopping"
         );
          webActions.closedBrowser();

    }
}
