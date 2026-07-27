# =============================================================================
# Cenários de formulário Contact Us — Advantage Shopping
# Tokens [CHAVE] resolvidos em test-data.properties / variáveis de ambiente
# =============================================================================

@contactUs
Feature: Contact Us no Advantage Shopping
  Como um usuario do sistema
  Quero entrar em contato atraves do formulario Contact Us
  Para enviar mensagens ou duvidas sobre produtos

  Background:
    Given Que acesse a aplicacao de login do site advantage
    When acessar a pagina de contact us

  # ── Positivo ──────────────────────────────────────────────────────────────

  @contactUsEnviarMensagemProduto
  Scenario: Enviar mensagem vinculada a um produto especifico
    When selecionar o produto "[PRODUCT_NAME]" no formulario de contato
    And preencher o email com "[NEW_EMAIL]"
    And preencher o subject com "Duvida sobre o produto"
    And preencher a mensagem com "Gostaria de mais informacoes sobre este produto"
    And enviar o formulario de contato
    Then validar que a mensagem foi enviada com sucesso

  @contactUsEnviarMensagemSemProduto
  Scenario: Enviar mensagem sem selecionar produto especifico
    When preencher o email com "[NEW_EMAIL]"
    And preencher o subject com "Duvida geral"
    And preencher a mensagem com "Tenho uma duvida geral sobre a loja"
    And enviar o formulario de contato
    Then validar que a mensagem foi enviada com sucesso

  # ── Negativo ──────────────────────────────────────────────────────────────

  @contactUsEnviarSemCamposObrigatorios
  Scenario: Tentar enviar mensagem sem preencher campos obrigatorios
    When enviar o formulario de contato
    Then validar que mensagem de campos obrigatorios e exibida

  @contactUsEnviarSemEmail
  Scenario: Tentar enviar mensagem sem preencher o email
    When preencher o subject com "Duvida geral"
    And preencher a mensagem com "Tenho uma duvida sobre a loja"
    And enviar o formulario de contato
    Then validar que o campo email e obrigatorio

  @contactUsEnviarSemSubject
  Scenario: Tentar enviar mensagem sem preencher o subject
    When preencher o email com "[NEW_EMAIL]"
    And preencher a mensagem com "Tenho uma duvida sobre a loja"
    And enviar o formulario de contato
    Then validar que o campo subject e obrigatorio
