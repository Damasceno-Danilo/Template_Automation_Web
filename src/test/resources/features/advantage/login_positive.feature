# =============================================================================
# Cenários positivos de login — Advantage Shopping
# Credenciais referenciadas por tokens [CHAVE] resolvidos em test-data.properties
# =============================================================================

@loginteste
Feature: Login na aplicacao
  Como um usuario do sistema
  Quero fazer login no sistema
  Para acessar minha conta

  Background:
    Given Que acesse a aplicacao de login do site advantage
    When acessar o menu login

  @iconeFechaModal
  Scenario: Clicar no icone fechar na modal
    When clicar no icone fechar
    Then validar que modal login fechou

  @loginCamposObg
  Scenario: Login com usuario valido e campos obrigatorios
    When inserir dados com "[VALID_USER]" e "[VALID_PASSWORD]"
    When clico no Botao SignIn
    Then valido que login foi realizado com sucesso

  @loginTodosCampos
  Scenario: Login com usuario valido e todos os campos
    When inserir dados com "[VALID_USER]" e "[VALID_PASSWORD]"
    And clicar em remember ME
    When clico no Botao SignIn
    Then valido que login foi realizado com sucesso
