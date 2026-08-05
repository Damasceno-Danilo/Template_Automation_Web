package br.com.ddamasceno.steps.advantageShopping;

import br.com.ddamasceno.core.config.TestDataConfig;
import br.com.ddamasceno.logic.advantageShopping.PagamentoLogic;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

/**
 * Step definitions para os cenários de pagamento/checkout (pagamentos.feature)
 * da Advantage Shopping.
 */
public class PagamentoStep {

    private final PagamentoLogic pagamentoLogic = new PagamentoLogic();

    // ── Ações ─────────────────────────────────────────────────────────────────

    @Quando("iniciar o checkout")
    public void iniciarOCheckout() {
        pagamentoLogic.iniciarCheckout();
    }

    @Quando("selecionar pagamento com SafePay")
    public void selecionarPagamentoComSafePay() {
        pagamentoLogic.selecionarPagamentoSafePay();
    }

    @Quando("selecionar pagamento com MasterCredit")
    public void selecionarPagamentoComMasterCredit() {
        pagamentoLogic.selecionarPagamentoMasterCredit();
    }

    @Quando("preencher usuario safepay com {string}")
    public void preencherUsuarioSafepayCom(String usuario) {
        pagamentoLogic.preencherUsuarioSafePay(TestDataConfig.resolve(usuario));
    }

    @Quando("preencher senha safepay com {string}")
    public void preencherSenhaSafepayCom(String senha) {
        pagamentoLogic.preencherSenhaSafePay(TestDataConfig.resolve(senha));
    }

    @Quando("preencher numero do cartao com {string}")
    public void preencherNumeroDoCartaoCom(String numero) {
        pagamentoLogic.preencherNumeroCartao(TestDataConfig.resolve(numero));
    }

    @Quando("preencher cvv com {string}")
    public void preencherCvvCom(String cvv) {
        pagamentoLogic.preencherCvv(TestDataConfig.resolve(cvv));
    }

    @Quando("preencher mes de vencimento com {string}")
    public void preencherMesDeVencimentoCom(String mes) {
        pagamentoLogic.preencherMesVencimento(TestDataConfig.resolve(mes));
    }

    @Quando("preencher ano de vencimento com {string}")
    public void preencherAnoDeVencimentoCom(String ano) {
        pagamentoLogic.preencherAnoVencimento(TestDataConfig.resolve(ano));
    }

    @Quando("preencher nome do titular com {string}")
    public void preencherNomeDoTitularCom(String nome) {
        pagamentoLogic.preencherNomeTitular(TestDataConfig.resolve(nome));
    }

    @Quando("confirmar pagamento")
    public void confirmarPagamento() {
        pagamentoLogic.confirmarPagamento();
    }

    @Quando("acessar o historico de pedidos")
    public void acessarOHistoricoDePedidos() {
        pagamentoLogic.acessarHistoricoDePedidos();
    }

    // ── Validações ────────────────────────────────────────────────────────────

    @Então("validar que o pagamento foi realizado com sucesso")
    public void validarQueOPagamentoFoiRealizadoComSucesso() {
        pagamentoLogic.validarPagamentoRealizadoComSucesso();
    }

    @Então("validar que o pagamento foi recusado")
    public void validarQueOPagamentoFoiRecusado() {
        pagamentoLogic.validarPagamentoRecusado();
    }

    @Então("validar que o pagamento foi recusado por saldo insuficiente")
    public void validarQueOPagamentoFoiRecusadoPorSaldoInsuficiente() {
        pagamentoLogic.validarPagamentoRecusadoPorSaldoInsuficiente();
    }

    @Então("validar que o status do pedido e aprovado")
    public void validarQueOStatusDoPedidoEAprovado() {
        pagamentoLogic.validarStatusDoPedidoAprovado();
    }

    @Então("validar que o status do pedido e recusado")
    public void validarQueOStatusDoPedidoERecusado() {
        pagamentoLogic.validarStatusDoPedidoRecusado();
    }

    @Então("validar que o pedido consta no historico com status aprovado")
    public void validarQueOPedidoConstaNoHistoricoComStatusAprovado() {
        pagamentoLogic.validarPedidoNoHistoricoComStatusAprovado();
    }

    @Então("validar que o checkout foi retomado apos o login")
    public void validarQueOCheckoutFoiRetomadoAposOLogin() {
        pagamentoLogic.validarCheckoutRetomadoAposLogin();
    }
}
