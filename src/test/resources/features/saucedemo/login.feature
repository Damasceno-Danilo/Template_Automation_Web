# =============================================================================
# Cenários de login — Sauce Demo (https://www.saucedemo.com/)
# Credenciais referenciadas por tokens [CHAVE] resolvidos em test-data.properties
# =============================================================================

@saucedemoLogin
Feature: Login na aplicacao Sauce Demo
  Como um usuario do sistema
  Quero fazer login no Sauce Demo
  Para acessar a pagina de produtos

  Background:
    Given Que acesse a aplicacao de login do site saucedemo

  @saucedemoLoginValido
  Scenario: Login com usuario valido
    When inserir dados de login com "[SAUCEDEMO_VALID_USER]" e "[SAUCEDEMO_VALID_PASSWORD]"
    And clico no botao login do saucedemo
    Then valido que login foi realizado com sucesso no saucedemo

  @saucedemoLoginBloqueado
  Scenario: Login com usuario bloqueado
    When inserir dados de login com "[SAUCEDEMO_LOCKED_USER]" e "[SAUCEDEMO_VALID_PASSWORD]"
    And clico no botao login do saucedemo
    Then valido que login nao foi realizado com a mensagem "Epic sadface: Sorry, this user has been locked out."

  @saucedemoLoginSenhaInvalida
  Scenario: Login com senha invalida
    When inserir dados de login com "[SAUCEDEMO_VALID_USER]" e "senha_errada_xyz"
    And clico no botao login do saucedemo
    Then valido que login nao foi realizado com a mensagem "Epic sadface: Username and password do not match any user in this service"

  @saucedemoLoginUsuarioEmBranco
  Scenario: Login com usuario em branco
    When inserir dados de login com "" e "[SAUCEDEMO_VALID_PASSWORD]"
    And clico no botao login do saucedemo
    Then valido que login nao foi realizado com a mensagem "Epic sadface: Username is required"

  @saucedemoLoginSenhaEmBranco
  Scenario: Login com senha em branco
    When inserir dados de login com "[SAUCEDEMO_VALID_USER]" e ""
    And clico no botao login do saucedemo
    Then valido que login nao foi realizado com a mensagem "Epic sadface: Password is required"
