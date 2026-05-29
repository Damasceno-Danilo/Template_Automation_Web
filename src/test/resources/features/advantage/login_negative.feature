# =============================================================================
# Cenários negativos de login — Advantage Shopping
# Credenciais referenciadas por tokens [CHAVE] resolvidos em test-data.properties
# =============================================================================

@loginteste
Feature: Login negativo na aplicacao
  Como um usuario do sistema
  Quero validar tentativas de login invalidas
  Para garantir mensagens e comportamento esperados

  Background:
    Given Que acesse a aplicacao de login do site advantage
    When acessar o menu login

  @loginInvalidUser
  Scenario: Tentativa de login com usuario invalido
    When inserir dados com "[INVALID_USER]" e "[VALID_PASSWORD]"
    When clico no Botao SignIn
    Then valido que login nao foi realizado

  @loginInvalidPassword
  Scenario: Tentativa de login com senha invalida
    When inserir dados com "[VALID_USER]" e "[INVALID_PASSWORD]"
    When clico no Botao SignIn
    Then valido que login nao foi realizado
