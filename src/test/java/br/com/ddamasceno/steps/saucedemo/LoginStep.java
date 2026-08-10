package br.com.ddamasceno.steps.saucedemo;

import br.com.ddamasceno.core.config.TestDataConfig;
import br.com.ddamasceno.logic.saucedemo.LoginLogic;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

/**
 * Step definitions para os cenários de login do Sauce Demo.
 *
 * <p>Credenciais são resolvidas via {@link TestDataConfig#resolve(String)},
 * que aceita tanto valores literais quanto tokens simbólicos do tipo {@code [CHAVE]}.
 */
public class LoginStep {

    private final LoginLogic loginLogic = new LoginLogic();

    @Dado("Que acesse a aplicacao de login do site saucedemo")
    public void queAcesseAAplicacaoDeLoginDoSiteSaucedemo() {
        loginLogic.iniciarNavegador();
    }

    @Quando("inserir dados de login com {string} e {string}")
    public void inserirDadosDeLoginComE(String username, String password) {
        loginLogic.inserirDadosLogin(
                TestDataConfig.resolve(username),
                TestDataConfig.resolve(password)
        );
    }

    @Quando("clico no botao login do saucedemo")
    public void clicoNoBotaoLoginDoSaucedemo() {
        loginLogic.clickBtnLogin();
    }

    @Então("valido que login foi realizado com sucesso no saucedemo")
    public void validoQueLoginFoiRealizadoComSucessoNoSaucedemo() {
        loginLogic.validarLogin();
    }

    @Então("valido que login nao foi realizado com a mensagem {string}")
    public void validoQueLoginNaoFoiRealizadoComAMensagem(String mensagemEsperada) {
        loginLogic.validarLoginFalhou(mensagemEsperada);
    }
}
