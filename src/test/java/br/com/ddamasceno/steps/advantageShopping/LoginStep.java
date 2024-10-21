package br.com.ddamasceno.steps.advantageShopping;

import br.com.ddamasceno.logic.advantageShopping.LoginLogic;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginStep {

    private final LoginLogic loginLogic;

    public LoginStep() {
    loginLogic = new LoginLogic();
    }

    @Given("Que acesse a aplicacao de login do site advantage")
    public void que_acesse_a_aplicacao_de_login_do_site_advantage() {
        loginLogic.iniciarNavegador();
    }

    @When("acessar o menu login")
    public void acessar_o_menu_login() {
        loginLogic.clickBtnLogin();

    }

    @When("clicar no icone fechar")
    public void clicar_no_icone_fechar() {

    }

    @Then("validar que modal login fechou")
    public void validar_que_modal_login_fechou() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("clico fora da modal")
    public void clico_fora_da_modal() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("inserir dados com {string} e {string}")
    public void inserir_dados_com_e(String username, String spassword) {
        loginLogic.inserirDadosLogin();
    }

    @When("clicar em remember ME")
    public void clicar_em_remember_me() {
        loginLogic.clickRememberMe();
    }

    @Then("valido que login foi realizado com sucesso")
    public void valido_que_login_foi_realizado_com_sucesso() {
        loginLogic.validarLogin();
    }


}
