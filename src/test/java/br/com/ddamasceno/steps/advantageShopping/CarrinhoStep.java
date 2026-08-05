package br.com.ddamasceno.steps.advantageShopping;

import br.com.ddamasceno.logic.advantageShopping.CarrinhoLogic;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

/**
 * Step definitions para os cenários de carrinho de compras
 * (carrinho_compras.feature) da Advantage Shopping.
 */
public class CarrinhoStep {

    private final CarrinhoLogic carrinhoLogic = new CarrinhoLogic();

    // ── Ações ─────────────────────────────────────────────────────────────────

    @Quando("adicionar o produto ao carrinho")
    public void adicionarOProdutoAoCarrinho() {
        carrinhoLogic.adicionarProdutoAoCarrinho();
    }

    @Quando("adicionar o produto ao carrinho sem estar logado")
    public void adicionarOProdutoAoCarrinhoSemEstarLogado() {
        carrinhoLogic.adicionarProdutoAoCarrinhoSemLogin();
    }

    @Quando("adicionar o produto ao carrinho novamente")
    public void adicionarOProdutoAoCarrinhoNovamente() {
        carrinhoLogic.adicionarMesmoProdutoNovamente();
    }

    @Quando("acessar o carrinho de compras")
    public void acessarOCarrinhoDeCompras() {
        carrinhoLogic.acessarCarrinhoDeCompras();
    }

    @Quando("remover o produto do carrinho")
    public void removerOProdutoDoCarrinho() {
        carrinhoLogic.removerProdutoDoCarrinho();
    }

    @Quando("alterar a quantidade do produto para {int}")
    public void alterarAQuantidadeDoProdutoPara(int quantidade) {
        carrinhoLogic.alterarQuantidadeDoProduto(quantidade);
    }

    // ── Validações ────────────────────────────────────────────────────────────

    @Então("validar que produto foi adicionado ao carrinho")
    public void validarQueProdutoFoiAdicionadoAoCarrinho() {
        carrinhoLogic.validarProdutoAdicionadoAoCarrinho();
    }

    @Então("validar que produto esta listado no carrinho")
    public void validarQueProdutoEstaListadoNoCarrinho() {
        carrinhoLogic.validarProdutoListadoNoCarrinho();
    }

    @Então("validar que o carrinho esta vazio")
    public void validarQueOCarrinhoEstaVazio() {
        carrinhoLogic.validarCarrinhoVazio();
    }

    @Então("validar que mensagem de carrinho vazio e exibida")
    public void validarQueMensagemDeCarrinhoVazioEExibida() {
        carrinhoLogic.validarMensagemDeCarrinhoVazio();
    }

    @Então("validar que a quantidade do produto foi atualizada para {int}")
    public void validarQueAQuantidadeDoProdutoFoiAtualizadaPara(int quantidade) {
        carrinhoLogic.validarQuantidadeAtualizada(quantidade);
    }

    @Então("validar que mensagem de quantidade invalida e exibida")
    public void validarQueMensagemDeQuantidadeInvalidaEExibida() {
        carrinhoLogic.validarMensagemDeQuantidadeInvalida();
    }

    @Então("validar que o botao add to cart esta visivel e habilitado")
    public void validarQueOBotaoAddToCartEstaVisivelEHabilitado() {
        carrinhoLogic.validarBotaoAddToCartVisivelHabilitado();
    }

    @Então("validar que e solicitado login para continuar a compra")
    public void validarQueESolicitadoLoginParaContinuarACompra() {
        carrinhoLogic.validarSolicitacaoDeLoginParaContinuarCompra();
    }
}
