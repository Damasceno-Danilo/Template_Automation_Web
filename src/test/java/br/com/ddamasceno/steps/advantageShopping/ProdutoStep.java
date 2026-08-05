package br.com.ddamasceno.steps.advantageShopping;

import br.com.ddamasceno.logic.advantageShopping.ProdutoLogic;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

/**
 * Step definitions para os cenários de busca e visualização de produtos
 * (compras.feature) da Advantage Shopping.
 */
public class ProdutoStep {

    private final ProdutoLogic produtoLogic = new ProdutoLogic();

    // ── Ações ─────────────────────────────────────────────────────────────────

    @Quando("buscar pelo produto {string}")
    public void buscarPeloProduto(String termo) {
        produtoLogic.buscarProduto(termo);
    }

    @Quando("acessar o catalogo de produtos")
    public void acessarOCatalogoDeProdutos() {
        produtoLogic.acessarCatalogoDeProdutos();
    }

    @Quando("acessar a secao de produtos especiais")
    public void acessarASecaoDeProdutosEspeciais() {
        produtoLogic.acessarSecaoDeProdutosEspeciais();
    }

    @Quando("acessar a secao de produtos populares")
    public void acessarASecaoDeProdutosPopulares() {
        produtoLogic.acessarSecaoDeProdutosPopulares();
    }

    @Quando("aplicar filtro de categoria na listagem de produtos")
    public void aplicarFiltroDeCategoriaNaListagemDeProdutos() {
        produtoLogic.aplicarFiltroDeCategoria();
    }

    @Quando("selecionar o produto {string}")
    public void selecionarOProduto(String nomeProduto) {
        produtoLogic.selecionarProduto(nomeProduto);
    }

    // ── Validações ────────────────────────────────────────────────────────────

    @Então("validar que o produto {string} foi encontrado nos resultados")
    public void validarQueOProdutoFoiEncontradoNosResultados(String nomeProduto) {
        produtoLogic.validarProdutoEncontrado(nomeProduto);
    }

    @Então("validar que os produtos do catalogo sao exibidos")
    public void validarQueOsProdutosDoCatalogoSaoExibidos() {
        produtoLogic.validarProdutosDoCatalogoExibidos();
    }

    @Então("validar que os produtos especiais sao exibidos")
    public void validarQueOsProdutosEspeciaisSaoExibidos() {
        produtoLogic.validarProdutosEspeciaisExibidos();
    }

    @Então("validar que os produtos populares sao exibidos")
    public void validarQueOsProdutosPopularesSaoExibidos() {
        produtoLogic.validarProdutosPopularesExibidos();
    }

    @Então("validar que os produtos filtrados sao exibidos")
    public void validarQueOsProdutosFiltradosSaoExibidos() {
        produtoLogic.validarProdutosFiltradosExibidos();
    }

    @Então("validar que nenhum produto foi encontrado na busca")
    public void validarQueNenhumProdutoFoiEncontradoNaBusca() {
        produtoLogic.validarNenhumProdutoEncontrado();
    }

    @Então("validar que a foto do produto e exibida")
    public void validarQueAFotoDoProdutoEExibida() {
        produtoLogic.validarFotoDoProdutoExibida();
    }

    @Então("validar que o nome do produto e exibido")
    public void validarQueONomeDoProdutoEExibido() {
        produtoLogic.validarNomeDoProdutoExibido();
    }

    @Então("validar que a descricao do produto e exibida")
    public void validarQueADescricaoDoProdutoEExibida() {
        produtoLogic.validarDescricaoDoProdutoExibida();
    }

    @Então("validar que o preco do produto e exibido")
    public void validarQueOPrecoDoProdutoEExibido() {
        produtoLogic.validarPrecoDoProdutoExibido();
    }

    @Então("validar que a quantidade em estoque e exibida")
    public void validarQueAQuantidadeEmEstoqueEExibida() {
        produtoLogic.validarQuantidadeEmEstoqueExibida();
    }

    @Então("validar que as avaliacoes do produto sao exibidas")
    public void validarQueAsAvaliacoesDoProdutoSaoExibidas() {
        produtoLogic.validarAvaliacoesDoProdutoExibidas();
    }

    @Então("validar que as opcoes de cores do produto sao exibidas")
    public void validarQueAsOpcoesDeCoresDoProdutoSaoExibidas() {
        produtoLogic.validarOpcoesDeCoresExibidas();
    }

    @Então("validar que o produto esta fora de estoque")
    public void validarQueOProdutoEstaForaDeEstoque() {
        produtoLogic.validarProdutoForaDeEstoque();
    }
}
