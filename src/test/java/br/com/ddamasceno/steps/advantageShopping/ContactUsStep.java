package br.com.ddamasceno.steps.advantageShopping;

import br.com.ddamasceno.core.config.TestDataConfig;
import br.com.ddamasceno.logic.advantageShopping.ContactUsLogic;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

/**
 * Step definitions para os cenários de "Contact Us" (contact_us.feature)
 * da Advantage Shopping.
 */
public class ContactUsStep {

    private final ContactUsLogic contactUsLogic = new ContactUsLogic();

    // ── Ações ─────────────────────────────────────────────────────────────────

    @Quando("acessar a pagina de contact us")
    public void acessarAPaginaDeContactUs() {
        contactUsLogic.acessarPaginaDeContactUs();
    }

    @Quando("selecionar o produto {string} no formulario de contato")
    public void selecionarOProdutoNoFormularioDeContato(String nomeProduto) {
        contactUsLogic.selecionarProdutoNoFormulario(TestDataConfig.resolve(nomeProduto));
    }

    @Quando("preencher o email com {string}")
    public void preencherOEmailCom(String email) {
        contactUsLogic.preencherEmail(TestDataConfig.resolve(email));
    }

    @Quando("preencher o subject com {string}")
    public void preencherOSubjectCom(String subject) {
        contactUsLogic.preencherSubject(subject);
    }

    @Quando("preencher a mensagem com {string}")
    public void preencherAMensagemCom(String mensagem) {
        contactUsLogic.preencherMensagem(mensagem);
    }

    @Quando("enviar o formulario de contato")
    public void enviarOFormularioDeContato() {
        contactUsLogic.enviarFormularioDeContato();
    }

    // ── Validações ────────────────────────────────────────────────────────────

    @Então("validar que a mensagem foi enviada com sucesso")
    public void validarQueAMensagemFoiEnviadaComSucesso() {
        contactUsLogic.validarMensagemEnviadaComSucesso();
    }

    @Então("validar que mensagem de campos obrigatorios e exibida")
    public void validarQueMensagemDeCamposObrigatoriosEExibida() {
        contactUsLogic.validarMensagemDeCamposObrigatorios();
    }

    @Então("validar que o campo email e obrigatorio")
    public void validarQueOCampoEmailEObrigatorio() {
        contactUsLogic.validarCampoEmailObrigatorio();
    }

    @Então("validar que o campo subject e obrigatorio")
    public void validarQueOCampoSubjectEObrigatorio() {
        contactUsLogic.validarCampoSubjectObrigatorio();
    }
}
