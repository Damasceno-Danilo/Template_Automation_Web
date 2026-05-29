package br.com.ddamasceno.steps.advantageShopping;

import br.com.ddamasceno.core.config.TestDataConfig;
import br.com.ddamasceno.logic.advantageShopping.LoginLogic;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

import java.io.IOException;

/**
 * Step definitions para os cenários de login da Advantage Shopping.
 *
 * <p>Credenciais são resolvidas via {@link TestDataConfig#resolve(String)},
 * que aceita tanto valores literais quanto tokens simbólicos do tipo {@code [CHAVE]}.
 * Isso evita expor credenciais diretamente nos arquivos {@code .feature}.
 */
public class LoginStep {

    private final LoginLogic loginLogic = new LoginLogic();

    // ── Background ───────────────────────────────────────────────────────────

    @Dado("Que acesse a aplicacao de login do site advantage")
    public void queAcesseAAplicacaoDeLoginDoSiteAdvantage() throws IOException {
        loginLogic.iniciarNavegador();
    }

    @Quando("acessar o menu login")
    public void acessarOMenuLogin() throws IOException {
        loginLogic.clickBtnLogin();
    }

    // ── Ações ─────────────────────────────────────────────────────────────────

    @Quando("clicar no icone fechar")
    public void clicarNoIconeFechar() {
        loginLogic.clickBtnFechar();
    }

    @Quando("clicar em remember ME")
    public void clicarEmRememberMe() {
        loginLogic.clickRememberMe();
    }

    @Quando("clico no Botao SignIn")
    public void clicoNoBotaoSignIn() {
        loginLogic.clickBtnSignIn();
    }

    /**
     * Aceita tanto valores literais quanto tokens simbólicos.
     * Exemplos de uso no .feature:
     * <pre>
     *   When inserir dados com "[VALID_USER]" e "[VALID_PASSWORD]"
     *   When inserir dados com "[INVALID_USER]" e "[VALID_PASSWORD]"
     * </pre>
     */
    @Quando("inserir dados com {string} e {string}")
    public void inserirDadosComE(String username, String password) {
        loginLogic.inserirDadosLogin(
                TestDataConfig.resolve(username),
                TestDataConfig.resolve(password)
        );
    }

    // ── Validações ────────────────────────────────────────────────────────────

    @Então("validar que modal login fechou")
    public void validarQueModalLoginFechou() {
        loginLogic.validarFecharModal();
    }

    @Então("valido que login foi realizado com sucesso")
    public void validoQueLoginFoiRealizadoComSucesso() {
        loginLogic.validarLogin();
    }

    @Então("valido que login nao foi realizado")
    public void validoQueLoginNaoFoiRealizado() {
        loginLogic.validarLoginFalhou();
    }
}
