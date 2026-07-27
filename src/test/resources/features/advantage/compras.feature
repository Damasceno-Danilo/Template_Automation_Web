# =============================================================================
# Cenários de busca e visualização de produtos — Advantage Shopping
# Tokens [CHAVE] resolvidos em test-data.properties / variáveis de ambiente
# =============================================================================

@compras
Feature: Compras no Advantage Shopping
  Como um usuario do sistema
  Quero pesquisar e visualizar produtos no catalogo
  Para encontrar o produto ideal antes de comprar

  Background:
    Given Que acesse a aplicacao de login do site advantage

  # ── Caminho Feliz ─────────────────────────────────────────────────────────

  @comprasBuscarProdutoEspecifico
  Scenario: Buscar produto especifico pelo nome
    When buscar pelo produto "[PRODUCT_NAME]"
    Then validar que o produto "[PRODUCT_NAME]" foi encontrado nos resultados

  @comprasBuscarPorCatalogo
  Scenario: Buscar produto navegando pelo catalogo
    When acessar o catalogo de produtos
    Then validar que os produtos do catalogo sao exibidos

  @comprasBuscarProdutosEspeciais
  Scenario: Buscar produtos em ofertas especiais
    When acessar a secao de produtos especiais
    Then validar que os produtos especiais sao exibidos

  @comprasBuscarProdutosPopulares
  Scenario: Buscar produtos populares
    When acessar a secao de produtos populares
    Then validar que os produtos populares sao exibidos

  @comprasFiltrarProduto
  Scenario: Selecionar produto por filtro de categoria
    When aplicar filtro de categoria na listagem de produtos
    Then validar que os produtos filtrados sao exibidos

  @comprasValidarDadosProduto
  Scenario: Validar todos os dados do produto selecionado
    When selecionar o produto "[PRODUCT_NAME]"
    Then validar que a foto do produto e exibida
    And validar que o nome do produto e exibido
    And validar que a descricao do produto e exibida
    And validar que o preco do produto e exibido
    And validar que a quantidade em estoque e exibida
    And validar que as avaliacoes do produto sao exibidas
    And validar que as opcoes de cores do produto sao exibidas

  # ── Caminho Não Feliz ─────────────────────────────────────────────────────

  @comprasProcurarProdutoInexistente
  Scenario: Procurar produto inexistente no catalogo
    When buscar pelo produto "produto_inexistente_xyz_123"
    Then validar que nenhum produto foi encontrado na busca

  @comprasValidarProdutoForaDeEstoque
  Scenario: Validar produto fora de estoque no catalogo
    When selecionar o produto "[OUT_OF_STOCK_PRODUCT]"
    Then validar que o produto esta fora de estoque
