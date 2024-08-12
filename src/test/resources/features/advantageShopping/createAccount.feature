Feature: Criar uma conta com sucesso
  como um usuario do sistema
  quero criar uma conta de usuario
  para utilizar o sistema

  Background:
    Given Que acesse a aplicacao de login do site advantage
    When clicar em create new account

  @createNewAccount
  Scenario: Clicar no link create new account
    Then validar acesso a pagina create account

  @createAccountObrigatorio
  Scenario: Criar um usuario valido com campos obrigatorios
    When preencher campos obrigatorios em ACCOUNT DETAILS "username", "email", "password", confirmPassword"
    And cliccar em I agree to the www.AdvantageOnlineShopping.com Conditions of Use and Privacy Notice
    Then validar usuario criado com sucesso

  @createAccountTodosCampos
  Scenario: Criar um usuario valido com todos os campos
    When preencher campos obrigatorios em ACCOUNT DETAILS "username", "email", "password", confirmPassword"
    When preencher campos em PERSONAL DETAILS "firstName", "lastName", "Phone number"
    When preencher campos em ADDRESS "country", "city", "address", "state", "postalCode"
    And cliccar em I agree to the www.AdvantageOnlineShopping.com Conditions of Use and Privacy Notice
    Then validar usuario criado com sucesso

  @createAccountCamposBranco
  Scenario: Realizar criação de usuario dados obrigatorios em branco
    When preencher campos obrigatorios em ACCOUNT DETAILS "username", "email", "password", confirmPassword" em branco
    And cliccar em I agree to the www.AdvantageOnlineShopping.com Conditions of Use and Privacy Notice
    Then validar que botao register desabilitado