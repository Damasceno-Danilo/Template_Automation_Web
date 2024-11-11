package br.com.ddamasceno.logic.advantageShopping;

import br.com.ddamasceno.core.*;
import br.com.ddamasceno.maps.advantageShopping.LoginMap;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

@Log4j2
public class LoginLogic {

     private WebDriver driver;
     private DriverFactory driverFactory;
     private WebActions webActions;
     private LoginMap loginMap;
     private WebDriverWait wait;
     private TestReport report;



     public LoginLogic() {
          driverFactory = new DriverFactory(Browser.CHROME);
          driver = driverFactory.getDriver();
          webActions = new WebActions(driver);
          loginMap = new LoginMap();
          report = new TestReport(driver);
          report = new TestReport(driver);

          PageFactory.initElements(driver, loginMap);
     }

     public void iniciarNavegador() throws IOException {
          String step = "Abrir aplicação advantageShopping";
          webActions.navigateURL("https://www.advantageonlineshopping.com/#/");
          webActions.waitVisibleElement(loginMap.getLogoAdvantage());
          report.captureScreenshot(step);
          log.info(step);



     }

     public void clickBtnLogin() throws IOException {
          String step = "Clicar Botao login";
          webActions.click(loginMap.getIconUser());
          webActions.waitVisibleElement(loginMap.getLinkCreateAccount());
          report.captureScreenshot(step);
          log.info(step);
     }

     public void clickBtnFechar() {
          String step = "Clicar Botao Fechar";
          webActions.waitInvisibleElement(loginMap.getLoader());
          webActions.click(loginMap.getIconFechar());
          report.captureScreenshot(step);
          log.info(step);
     }


     public void inserirDadosLogin() {
          String step = "INSERIR DADOS DE LOGIN";
          webActions.waitVisibleElement(loginMap.getLinkCreateAccount());
          webActions.insertText(loginMap.getInpUsername(), "DanDama");
          webActions.insertText(loginMap.getInpPassword(), "Dan$1418");
          webActions.waitInvisibleElement(loginMap.getLoader());
          report.captureScreenshot(step);
          log.info(step);

     }

     public void clickRememberMe() {
          String step = "INSERIR DADOS DE LOGIN";
          webActions.click(loginMap.getClickRememberMe());
          report.captureScreenshot(step);
          log.info(step);

     }

     public void clickBtnSignIn() {
          String step = "CLICAR BOTAO SIGNIN";
          webActions.click(loginMap.getBtnSignIn());
          report.captureScreenshot(step);
          log.info(step);
     }

     public void validarFecharModal() throws IOException {
          String step = "VALIDAR MODAL FECHADA";
          webActions.waitInvisibleElement(loginMap.getIconFechar());
          Assert.assertFalse(loginMap.getLinkCreateAccount().isDisplayed());
          report.captureScreenshot(step);
          report.createPdfReport();
          webActions.closedBrowser();
          log.info(step);
     }

    public void validarLogin() {
          String step = "VALIDAR LOGIN COM SUCESSO";
          webActions.waitInvisibleElement(loginMap.getIconFechar());
          Assert.assertEquals("DanDama", loginMap.getTextDataLogin().getText());
          report.captureScreenshot(step);
          report.createPdfReport();
          webActions.closedBrowser();
          log.info(step);

    }
}


