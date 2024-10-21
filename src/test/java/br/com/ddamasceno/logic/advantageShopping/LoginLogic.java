package br.com.ddamasceno.logic.advantageShopping;

import br.com.ddamasceno.core.Browser;
import br.com.ddamasceno.core.DriverFactory;
import br.com.ddamasceno.core.WebActions;
import br.com.ddamasceno.maps.advantageShopping.LoginMaps;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginLogic {

     private WebDriver driver;
     private DriverFactory driverFactory;
     private WebActions webActions;
     private LoginMaps loginMaps;
     private WebDriverWait wait;

     public LoginLogic() {
          driverFactory = new DriverFactory(Browser.CHROME);
          driver = driverFactory.getDriver();
          webActions = new WebActions(driver);
          loginMaps = new LoginMaps();
          PageFactory.initElements(driver, loginMaps);
     }

     public void iniciarNavegador() {

          webActions.navigateURL("https://www.advantageonlineshopping.com/#/");
          webActions.waitVisibleElement(loginMaps.getLogoAdvantage());

     }

     public void clickBtnLogin() {
          webActions.click(loginMaps.getIconUser());

     }

     public void inserirDadosLogin() {
          webActions.waitVisibleElement(loginMaps.getLinkCreateAccount());
          webActions.insertText(loginMaps.getInpUsername(), "DanDama");
          webActions.insertText(loginMaps.getInpPassword(), "Dan$1418");
          webActions.waitInvisibleElement(loginMaps.getLoader());
          webActions.click(loginMaps.getClickRememberMe());

     }

     public void clickRememberMe() {
          webActions.waitVisibleElement(loginMaps.getLinkCreateAccount());
          webActions.click(loginMaps.getBtnSignIn());
     }

    public void validarLogin() {
         Assert.assertTrue(loginMaps.getIconUser().isDisplayed());
    }
}
