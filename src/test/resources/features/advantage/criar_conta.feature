# =============================================================================
# Cenários de criação de conta — Advantage Shopping
# Tokens [CHAVE] resolvidos em test-data.properties / variáveis de ambiente
# =============================================================================

@criarConta
Feature: Criar nova conta no Advantage Shopping
  Como um novo usuario
  Quero criar uma conta na plataforma
  Para ter acesso as funcionalidades de compra

  Background:
    Given Que acesse a aplicacao de login do site advantage
    When acessar o menu login
    And clicar em create account

  # ── Positivo ──────────────────────────────────────────────────────────────

  @criarContaTodosCampos
  Scenario: Criar usuario preenchendo todos os campos do formulario
    When preencher o username com "[NEW_USER]"
    And preencher o email com "[NEW_EMAIL]"
    And preencher o password com "[NEW_PASSWORD]"
    And confirmar o password com "[NEW_PASSWORD]"
    And aceitar os termos e condicoes
    And confirmar a criacao de conta
    Then validar que a conta foi criada com sucesso

  @criarContaCamposObrigatorios
  Scenario: Criar usuario preenchendo apenas os campos obrigatorios
    When preencher o username com "[NEW_USER]"
    And preencher o password com "[NEW_PASSWORD]"
    And confirmar o password com "[NEW_PASSWORD]"
    And confirmar a criacao de conta
    Then validar que a conta foi criada com sucesso

  # ── Negativos ─────────────────────────────────────────────────────────────

  @criarContaUsernameExistente
  Scenario: Tentar criar usuario com username ja cadastrado
    When preencher o username com "[VALID_USER]"
    And preencher o password com "[NEW_PASSWORD]"
    And confirmar o password com "[NEW_PASSWORD]"
    And confirmar a criacao de conta
    Then validar que mensagem de username ja existente e exibida

  @criarContaPasswordDiferente
  Scenario: Tentar criar usuario com confirmacao de password diferente
    When preencher o username com "[NEW_USER]"
    And preencher o password com "[NEW_PASSWORD]"
    And confirmar o password com "[DIFFERENT_PASSWORD]"
    And confirmar a criacao de conta
    Then validar que mensagem de senha incompativel e exibida

  @criarContaEmailInvalido
  Scenario: Tentar criar usuario com email em formato invalido
    When preencher o username com "[NEW_USER]"
    And preencher o email com "[INVALID_EMAIL]"
    And preencher o password com "[NEW_PASSWORD]"
    And confirmar o password com "[NEW_PASSWORD]"
    And confirmar a criacao de conta
    Then validar que mensagem de email invalido e exibida

  @criarContaCamposObrigatoriosVazios
  Scenario: Tentar criar usuario sem preencher os campos obrigatorios
    When confirmar a criacao de conta
    Then validar que mensagem de campos obrigatorios e exibida
