# =============================================================================
# Cenários de pagamento — Advantage Shopping
# Tokens [CHAVE] resolvidos em test-data.properties / variáveis de ambiente
# ATENÇÃO: dados de cartão são sempre fictícios (sandbox) — nunca reais
# =============================================================================

@pagamentos
Feature: Pagamentos no Advantage Shopping
  Como um usuario logado com produto no carrinho
  Quero finalizar minha compra escolhendo a forma de pagamento
  Para concluir o pedido com sucesso

  Background:
    Given Que acesse a aplicacao de login do site advantage
    And acessar o menu login
    And inserir dados com "[VALID_USER]" e "[VALID_PASSWORD]"
    And clico no Botao SignIn
    And valido que login foi realizado com sucesso
    And selecionar o produto "[PRODUCT_NAME]"
    And adicionar o produto ao carrinho
    And iniciar o checkout

  # ── Positivo ──────────────────────────────────────────────────────────────

  @pagamentoSafePay
  Scenario: Realizar pagamento com SafePay
    When selecionar pagamento com SafePay
    And preencher usuario safepay com "[SAFEPAY_USER]"
    And preencher senha safepay com "[SAFEPAY_PASSWORD]"
    And confirmar pagamento
    Then validar que o pagamento foi realizado com sucesso

  @pagamentoMasterCredit
  Scenario: Realizar pagamento com cartao de credito valido
    When selecionar pagamento com MasterCredit
    And preencher numero do cartao com "[CARD_NUMBER]"
    And preencher cvv com "[CARD_CVV]"
    And preencher mes de vencimento com "[CARD_MONTH]"
    And preencher ano de vencimento com "[CARD_YEAR]"
    And preencher nome do titular com "[CARD_HOLDER]"
    And confirmar pagamento
    Then validar que o pagamento foi realizado com sucesso

  @pagamentoLoginDuranteCheckout
  Scenario: Logar dentro do fluxo de pagamento
    Given Que acesse a aplicacao de login do site advantage
    And selecionar o produto "[PRODUCT_NAME]"
    And adicionar o produto ao carrinho sem estar logado
    And iniciar o checkout
    When acessar o menu login
    And inserir dados com "[VALID_USER]" e "[VALID_PASSWORD]"
    And clico no Botao SignIn
    And valido que login foi realizado com sucesso
    Then validar que o checkout foi retomado apos o login

  @checarPagamentoSafePay
  Scenario: Checar historico de pagamento realizado com SafePay
    When selecionar pagamento com SafePay
    And preencher usuario safepay com "[SAFEPAY_USER]"
    And preencher senha safepay com "[SAFEPAY_PASSWORD]"
    And confirmar pagamento
    And acessar o historico de pedidos
    Then validar que o pedido consta no historico com status aprovado

  @checarPagamentoCartao
  Scenario: Checar historico de pagamento realizado com cartao de credito
    When selecionar pagamento com MasterCredit
    And preencher numero do cartao com "[CARD_NUMBER]"
    And preencher cvv com "[CARD_CVV]"
    And preencher mes de vencimento com "[CARD_MONTH]"
    And preencher ano de vencimento com "[CARD_YEAR]"
    And preencher nome do titular com "[CARD_HOLDER]"
    And confirmar pagamento
    And acessar o historico de pedidos
    Then validar que o pedido consta no historico com status aprovado

  @validarStatusPagamentoAprovado
  Scenario: Validar status de pagamento aprovado
    When selecionar pagamento com SafePay
    And preencher usuario safepay com "[SAFEPAY_USER]"
    And preencher senha safepay com "[SAFEPAY_PASSWORD]"
    And confirmar pagamento
    Then validar que o status do pedido e aprovado

  # ── Negativo ──────────────────────────────────────────────────────────────

  @pagamentoSemLogin
  Scenario: Tentar efetuar compra sem estar logado
    Given Que acesse a aplicacao de login do site advantage
    And selecionar o produto "[PRODUCT_NAME]"
    And adicionar o produto ao carrinho sem estar logado
    When iniciar o checkout
    Then validar que e solicitado login para continuar a compra

  @pagamentoCartaoNumeroInvalido
  Scenario: Efetuar pagamento com numero de cartao invalido
    When selecionar pagamento com MasterCredit
    And preencher numero do cartao com "[INVALID_CARD_NUMBER]"
    And preencher cvv com "[CARD_CVV]"
    And preencher mes de vencimento com "[CARD_MONTH]"
    And preencher ano de vencimento com "[CARD_YEAR]"
    And preencher nome do titular com "[CARD_HOLDER]"
    And confirmar pagamento
    Then validar que o pagamento foi recusado

  @pagamentoCvvInvalido
  Scenario: Efetuar pagamento com CVV invalido
    When selecionar pagamento com MasterCredit
    And preencher numero do cartao com "[CARD_NUMBER]"
    And preencher cvv com "[INVALID_CARD_CVV]"
    And preencher mes de vencimento com "[CARD_MONTH]"
    And preencher ano de vencimento com "[CARD_YEAR]"
    And preencher nome do titular com "[CARD_HOLDER]"
    And confirmar pagamento
    Then validar que o pagamento foi recusado

  @pagamentoMesVencimentoInvalido
  Scenario: Efetuar pagamento com mes de vencimento invalido
    When selecionar pagamento com MasterCredit
    And preencher numero do cartao com "[CARD_NUMBER]"
    And preencher cvv com "[CARD_CVV]"
    And preencher mes de vencimento com "[INVALID_CARD_MONTH]"
    And preencher ano de vencimento com "[CARD_YEAR]"
    And preencher nome do titular com "[CARD_HOLDER]"
    And confirmar pagamento
    Then validar que o pagamento foi recusado

  @pagamentoAnoVencimentoInvalido
  Scenario: Efetuar pagamento com ano de vencimento invalido
    When selecionar pagamento com MasterCredit
    And preencher numero do cartao com "[CARD_NUMBER]"
    And preencher cvv com "[CARD_CVV]"
    And preencher mes de vencimento com "[CARD_MONTH]"
    And preencher ano de vencimento com "[INVALID_CARD_YEAR]"
    And preencher nome do titular com "[CARD_HOLDER]"
    And confirmar pagamento
    Then validar que o pagamento foi recusado

  @pagamentoNomeTitularInvalido
  Scenario: Efetuar pagamento com nome do titular invalido
    When selecionar pagamento com MasterCredit
    And preencher numero do cartao com "[CARD_NUMBER]"
    And preencher cvv com "[CARD_CVV]"
    And preencher mes de vencimento com "[CARD_MONTH]"
    And preencher ano de vencimento com "[CARD_YEAR]"
    And preencher nome do titular com "[INVALID_CARD_HOLDER]"
    And confirmar pagamento
    Then validar que o pagamento foi recusado

  @pagamentoSaldoInsuficienteSafePay
  Scenario: Checar pagamento com saldo insuficiente no SafePay
    When selecionar pagamento com SafePay
    And preencher usuario safepay com "[SAFEPAY_USER]"
    And preencher senha safepay com "[SAFEPAY_PASSWORD]"
    And confirmar pagamento
    Then validar que o pagamento foi recusado por saldo insuficiente

  @validarStatusPagamentoRecusado
  Scenario: Validar status de pagamento recusado
    When selecionar pagamento com MasterCredit
    And preencher numero do cartao com "[INVALID_CARD_NUMBER]"
    And preencher cvv com "[CARD_CVV]"
    And preencher mes de vencimento com "[CARD_MONTH]"
    And preencher ano de vencimento com "[CARD_YEAR]"
    And preencher nome do titular com "[CARD_HOLDER]"
    And confirmar pagamento
    Then validar que o status do pedido e recusado
