# =============================================================================
# Cenários de carrinho de compras — Advantage Shopping
# Tokens [CHAVE] resolvidos em test-data.properties / variáveis de ambiente
# =============================================================================

@carrinhoCompras
Feature: Carrinho de Compras no Advantage Shopping
  Como um usuario do sistema
  Quero gerenciar meu carrinho de compras
  Para controlar os produtos antes de finalizar a compra

  Background:
    Given Que acesse a aplicacao de login do site advantage

  # ── Caminho Feliz ─────────────────────────────────────────────────────────

  @adicionarProdutoCarrinho
  Scenario: Inserir produto no carrinho de compras
    Given acessar o menu login
    And inserir dados com "[VALID_USER]" e "[VALID_PASSWORD]"
    And clico no Botao SignIn
    And valido que login foi realizado com sucesso
    When selecionar o produto "[PRODUCT_NAME]"
    And adicionar o produto ao carrinho
    Then validar que produto foi adicionado ao carrinho

  @validarProdutoCarrinho
  Scenario: Validar produto exibido no carrinho de compras
    Given acessar o menu login
    And inserir dados com "[VALID_USER]" e "[VALID_PASSWORD]"
    And clico no Botao SignIn
    And valido que login foi realizado com sucesso
    When selecionar o produto "[PRODUCT_NAME]"
    And adicionar o produto ao carrinho
    And acessar o carrinho de compras
    Then validar que produto esta listado no carrinho

  @validarDadosProdutoCarrinho
  Scenario: Validar dados do produto selecionado no carrinho
    Given acessar o menu login
    And inserir dados com "[VALID_USER]" e "[VALID_PASSWORD]"
    And clico no Botao SignIn
    And valido que login foi realizado com sucesso
    When selecionar o produto "[PRODUCT_NAME]"
    Then validar que a foto do produto e exibida
    And validar que o nome do produto e exibido
    And validar que a descricao do produto e exibida
    And validar que o preco do produto e exibido
    And validar que a quantidade em estoque e exibida
    And validar que as avaliacoes do produto sao exibidas
    And validar que as opcoes de cores do produto sao exibidas

  @removerProdutoCarrinho
  Scenario: Remover produto do carrinho de compras
    Given acessar o menu login
    And inserir dados com "[VALID_USER]" e "[VALID_PASSWORD]"
    And clico no Botao SignIn
    And valido que login foi realizado com sucesso
    When selecionar o produto "[PRODUCT_NAME]"
    And adicionar o produto ao carrinho
    And acessar o carrinho de compras
    And remover o produto do carrinho
    Then validar que o carrinho esta vazio

  @alterarQuantidadeProduto
  Scenario: Alterar a quantidade de produto no carrinho
    Given acessar o menu login
    And inserir dados com "[VALID_USER]" e "[VALID_PASSWORD]"
    And clico no Botao SignIn
    And valido que login foi realizado com sucesso
    When selecionar o produto "[PRODUCT_NAME]"
    And adicionar o produto ao carrinho
    And acessar o carrinho de compras
    And alterar a quantidade do produto para 2
    Then validar que a quantidade do produto foi atualizada para 2

  @validarBotaoAddToCart
  Scenario: Validar acao do botao add to cart
    Given acessar o menu login
    And inserir dados com "[VALID_USER]" e "[VALID_PASSWORD]"
    And clico no Botao SignIn
    And valido que login foi realizado com sucesso
    When selecionar o produto "[PRODUCT_NAME]"
    Then validar que o botao add to cart esta visivel e habilitado

  # ── Caminho Não Feliz ─────────────────────────────────────────────────────

  @carrinhoComprarSemLogin
  Scenario: Tentar finalizar compra sem usuario logado
    When selecionar o produto "[PRODUCT_NAME]"
    And adicionar o produto ao carrinho sem estar logado
    Then validar que e solicitado login para continuar a compra

  @carrinhoBuscarProdutoInexistente
  Scenario: Buscar produto inexistente no catalogo
    When buscar pelo produto "produto_inexistente_xyz_123"
    Then validar que nenhum produto foi encontrado na busca

  @carrinhoBuscarProdutoSemEstoque
  Scenario: Tentar adicionar produto sem estoque ao carrinho
    When selecionar o produto "[OUT_OF_STOCK_PRODUCT]"
    Then validar que o produto esta fora de estoque

  @adicionarMesmoProdutoDuasVezes
  Scenario: Adicionar o mesmo produto duas vezes no carrinho
    Given acessar o menu login
    And inserir dados com "[VALID_USER]" e "[VALID_PASSWORD]"
    And clico no Botao SignIn
    And valido que login foi realizado com sucesso
    When selecionar o produto "[PRODUCT_NAME]"
    And adicionar o produto ao carrinho
    And adicionar o produto ao carrinho novamente
    Then validar que a quantidade do produto foi atualizada para 2

  @validarCarrinhoVazio
  Scenario: Validar acao com carrinho vazio
    Given acessar o menu login
    And inserir dados com "[VALID_USER]" e "[VALID_PASSWORD]"
    And clico no Botao SignIn
    And valido que login foi realizado com sucesso
    When acessar o carrinho de compras
    Then validar que o carrinho esta vazio
    And validar que mensagem de carrinho vazio e exibida

  @validarQuantidadeInvalidaCarrinho
  Scenario: Validar quantidade zero ou negativa no carrinho
    Given acessar o menu login
    And inserir dados com "[VALID_USER]" e "[VALID_PASSWORD]"
    And clico no Botao SignIn
    And valido que login foi realizado com sucesso
    When selecionar o produto "[PRODUCT_NAME]"
    And adicionar o produto ao carrinho
    And acessar o carrinho de compras
    And alterar a quantidade do produto para 0
    Then validar que mensagem de quantidade invalida e exibida
